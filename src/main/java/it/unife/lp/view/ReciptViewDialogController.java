package it.unife.lp.view;

import it.unife.lp.model.Ordine;
import it.unife.lp.model.VoceOrdine;
import it.unife.lp.util.DateUtil;
import it.unife.lp.util.OrderTableUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class ReciptViewDialogController {

    @FXML
    private Label orderIdLabel;
    
    @FXML
    private TableView<VoceOrdine> vociOrdineTable;
    @FXML
    private TableColumn<VoceOrdine, String> vociOrdineNomeColumn;
    @FXML
    private TableColumn<VoceOrdine, String> vociOrdinePrezzoUnitarioColumn;
    @FXML
    private TableColumn<VoceOrdine, Number> vociOrdineQuantitaColumn;
    @FXML
    private TableColumn<VoceOrdine, String> vociOrdinePrezzoTotaleColumn;
    
    @FXML
    private Label subtotaleLabel;
    @FXML
    private Label scontoPercentualeLabel;
    @FXML
    private Label scontoNumericoLabel;
    @FXML
    private Label totaleScontatoLabel;
    @FXML
    private Label metodoPagamentoLabel;
    @FXML
    private Label restoLabel;

    @FXML
    private Label dataLabel;

    public void setOrderId(int orderId) {
        this.orderIdLabel.setText(Integer.toString((orderId)));
    }

    public void setItem(Ordine ordine) {

        OrderTableUtil.populateItemsTable(
            vociOrdineTable,
            vociOrdineNomeColumn,
            vociOrdinePrezzoUnitarioColumn,
            vociOrdineQuantitaColumn,
            vociOrdinePrezzoTotaleColumn,
            ordine.getVociProperty()
        );

        subtotaleLabel.setText(String.format("%.2f €", ordine.getPrezzoTotaleParziale()));
        scontoPercentualeLabel.setText(String.format("(-%.2f %%)", ordine.getScontoPercentuale()));
        // Gets the numeric value of the discount
        scontoNumericoLabel.setText(String.format("-%.2f €", ordine.getPrezzoTotaleParziale() * ordine.getScontoPercentuale() / 100));

        totaleScontatoLabel.setText(String.format("%.2f €", ordine.getPrezzoTotaleFinale()));
        metodoPagamentoLabel.setText(ordine.getMetodoPagamento().toString());

        dataLabel.setText(DateUtil.format(ordine.getData()));
        restoLabel.setText(String.format("%.2f €", ordine.getResto()));
    }
}
