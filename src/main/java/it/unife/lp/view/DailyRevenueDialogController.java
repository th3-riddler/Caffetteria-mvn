package it.unife.lp.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.unife.lp.MainApp;
import it.unife.lp.model.Articolo;
import it.unife.lp.model.Ordine;
import it.unife.lp.model.VoceOrdine;
import it.unife.lp.util.DateUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DailyRevenueDialogController {

    @FXML
    private Label dateLabel;
    @FXML
    private Label revenueLabel;

    @FXML
    private TableView<VoceOrdine> itemsRevenueTable;
    @FXML
    private TableColumn<VoceOrdine, String> itemNameColumn;
    @FXML
    private TableColumn<VoceOrdine, Number> itemQuantityColumn;
    @FXML
    private TableColumn<VoceOrdine, String> itemTotalRevenueColumn;

    private MainApp mainApp;

    @FXML
    private void initialize() {
    }

    public void setItem(LocalDate date) {
        /*
         * If an order has a discount, I need to apply it to the articles in that order
         * to calculate the correct revenue for each item.
         * Because if I didn't do that, the revenue would be calculated on the original
         * price of the article, not the discounted one.
         * And so, I wouldn't know how much revenue that item generated for that order
         * in that day.
         */

        // Collects all orders for that day (collecting copies to not modify the original orders)
        List<Ordine> dailyOrders = this.mainApp.getOrdini().stream()
                .filter(o -> o.getData().isEqual(date)).map(Ordine::copy).collect(Collectors.toList());

        // Applies discounts to the articles in daily orders, in order to calculate the correct revenue for that single item
        dailyOrders.forEach(order -> {
            order.getVoci().forEach(voce -> {
                voce.setPrezzoArticolo(voce.getArticolo().getPrezzo() * (1 - (order.getScontoPercentuale() / 100.0)));
            });
        });

        // Now all the items in dailyOrders have the correct discounted price, so I can
        // populate the itemsRevenueTable. To do that, I need to aggregate the items by article, summing their quantities and total revenues.

        // Flattens all the items from all the orders into a single list
        List<VoceOrdine> voci = dailyOrders.stream().flatMap(o -> o.getVoci().stream()).collect(Collectors.toList());

        // Groups the items by article to aggregate quantities and total revenues
        Map<String, List<VoceOrdine>> vociPerArticolo = voci.stream()
                .collect(Collectors.groupingBy(v -> v.getArticolo().getNome()));

        // Creates aggregated VoceOrdine objects for each article
        List<VoceOrdine> vociAggregate = vociPerArticolo.entrySet().stream()
                .map(entry -> {
                    Articolo articolo = entry.getValue().get(0).getArticolo();
                    int quantitaTotale = entry.getValue().stream().mapToInt(VoceOrdine::getQuantita).sum();
                    double ricavoTotale = entry.getValue().stream().mapToDouble(VoceOrdine::getPrezzoTotale).sum();
                    VoceOrdine voceAggregata = new VoceOrdine(articolo, quantitaTotale);
                    voceAggregata.setPrezzoTotale(ricavoTotale);
                    return voceAggregata;
                })
                .collect(Collectors.toList());

        // vociAggregate.forEach(
        //     v -> System.out.println(v.getPrezzoTotale())
        // );

        // Now I populate the itemsRevenueTable with the aggregated items
        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().getArticolo().nomeProperty());
        itemQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantitaProperty());
        itemTotalRevenueColumn.setCellValueFactory(cellData -> 
            Bindings.createStringBinding(
                () -> String.format("%.2f €", cellData.getValue().getPrezzoTotale()),
                cellData.getValue().prezzoTotaleProperty()
            )
        );
        itemsRevenueTable.setItems(FXCollections.observableArrayList(vociAggregate));

        dateLabel.setText(DateUtil.format(date));
        double totalRevenue = vociAggregate.stream()
                .mapToDouble(VoceOrdine::getPrezzoTotale).sum();
        revenueLabel.setText(String.format("%.2f €", totalRevenue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
