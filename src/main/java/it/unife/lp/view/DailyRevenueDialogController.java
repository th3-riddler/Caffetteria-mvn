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
import it.unife.lp.util.OrderTableUtil;
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
    private TableColumn<VoceOrdine, Number> idColumn;
    @FXML
    private TableColumn<VoceOrdine, String> itemNameColumn;
    @FXML
    private TableColumn<VoceOrdine, String> itemUnitRevenueColumn;
    @FXML
    private TableColumn<VoceOrdine, Number> itemQuantityColumn;
    @FXML
    private TableColumn<VoceOrdine, String> itemTotalRevenueColumn;

    private MainApp mainApp;

    @FXML
    private void initialize() {}

    public void setItem(LocalDate date) {

        /*
            If an order has a discount, I need to apply it to the articles in that order to calculate the correct revenue for that item.
            Because if I didn't do that, the revenue would be calculated on the original price of the article, not the discounted one.
            And so, I wouldn't know how much revenue that item generated for that order in that day.
        */

        // Collects all orders for that day (collecting copies to not modify the original orders)
        List<Ordine> dailyOrders = this.mainApp.getOrdini().stream()
            .filter(o -> o.getData().isEqual(date)).map(Ordine::copy).collect(Collectors.toList());
        
        // Applies discounts to articles in daily orders, in order to calculate the correct revenue for that single item
        dailyOrders.forEach(order -> {
            order.getVoci().forEach(voce -> {
                voce.setPrezzoArticolo(voce.getArticolo().getPrezzo() * (1 - (order.getScontoPercentuale() / 100.0)));
            });
});

        // idColumn.setCellValueFactory(cellData -> cellData.getValue().getArticolo().idProperty());

        // Now all the items in dailyOrders have the correct discounted price, so I can populate the itemsRevenueTable
        List<VoceOrdine> voci = dailyOrders.stream().flatMap(o -> o.getVoci().stream()).collect(Collectors.toList());
        OrderTableUtil.populateItemsTable(
            itemsRevenueTable,
            itemNameColumn,
            itemUnitRevenueColumn,
            itemQuantityColumn,
            itemTotalRevenueColumn,
            FXCollections.observableArrayList(voci)
        );

        dateLabel.setText(DateUtil.format(date));

        double totalRevenue = dailyOrders.stream()
            .flatMap(o -> o.getVoci().stream())
            .mapToDouble(voce -> voce.getArticolo().getPrezzo() * voce.getQuantita())
            .sum();
        revenueLabel.setText(String.format("%.2f €", totalRevenue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
