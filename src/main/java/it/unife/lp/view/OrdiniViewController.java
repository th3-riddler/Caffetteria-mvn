package it.unife.lp.view;

import java.time.LocalDate;

import it.unife.lp.MainApp;
import it.unife.lp.model.Articolo;
import it.unife.lp.model.Ordine;
import it.unife.lp.model.VoceOrdine;
import it.unife.lp.util.DateUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public class OrdiniViewController {
    @FXML
    private TableView<Ordine> ordiniLeftTable;
    @FXML
    private TableColumn<Ordine, Number> idOrdineLeftColumn;
    @FXML
    private TableColumn<Ordine, LocalDate> dataOrdineLeftColumn;
    @FXML
    private TableColumn<Ordine, String> totaleOrdineLeftColumn;
    @FXML
    private TableColumn<Ordine, String> statoOrdineLeftColumn;

    @FXML
    private Label idLabel;
    @FXML
    private Label dataLabel;
    @FXML
    private Label statoLabel;

    @FXML
    private TableView<VoceOrdine> vociOrdineRightTable;
    @FXML
    private TableColumn<VoceOrdine, String> nomeVoceRightColumn;
    @FXML
    private TableColumn<VoceOrdine, String> prezzoUnitarioVoceRightColumn;
    @FXML
    private TableColumn<VoceOrdine, Number> quantitaVoceRightColumn;
    @FXML
    private TableColumn<VoceOrdine, String> prezzoTotaleVoceRightColumn;

    @FXML
    private Label scontoLabel;
    @FXML
    private Label totaleLabel;

    @FXML
    private HBox orderActionsHBox; // HBox containing actions related to the selected order (Edit and Delete Buttons)

    private MainApp mainApp;

    @FXML
    private void initialize() {
        // Initializes the orders table with the columns.
        idOrdineLeftColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        dataOrdineLeftColumn.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
        totaleOrdineLeftColumn.setCellValueFactory(cellData -> 
            Bindings.createStringBinding(
                () -> String.format("%.2f €", cellData.getValue().getPrezzoTotale()),
                cellData.getValue().prezzoTotaleProperty()
            )
        );

        // Creates a cell value factory that translates the boolean 'pagato' into a string with a String-Boolean binding
        statoOrdineLeftColumn.setCellValueFactory(cellData ->
            Bindings.createStringBinding(
                () -> cellData.getValue().isPagato() ? "Pagato" : "Non Pagato",
                cellData.getValue().pagatoProperty()
            )
        );
        // Clears order details.
        showOrderDetails(null);

        // Listens for selection changes and show the order details when changed.
        ordiniLeftTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldValue, newValue) -> showOrderDetails(newValue));

        // Binds the visibility of the order actions HBox to whether an order is selected in the left table
        orderActionsHBox.visibleProperty().bind(
            ordiniLeftTable.getSelectionModel().selectedItemProperty().isNotNull()
        );
        // Binds the management of the order actions HBox to whether an order is selected in the left table (so that it doesn't take up space when not visible)
        orderActionsHBox.managedProperty().bind(
            ordiniLeftTable.getSelectionModel().selectedItemProperty().isNotNull()
        );
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        ordiniLeftTable.setItems(mainApp.getOrdini());
    }

    private void showOrderDetails(Ordine ordine) {
        idLabel.setText(ordine != null ? String.valueOf(ordine.getId()) : "");
        dataLabel.setText(ordine != null ? String.valueOf(DateUtil.formatDateTime(ordine.getDataOra())) : "");
        statoLabel.setText(ordine != null ? ordine.isPagato() ? "Pagato" : "Non Pagato" : "");

        scontoLabel.setText(ordine != null ? String.format("%.2f %%", ordine.getSconto()) : "");
        totaleLabel.setText(ordine != null ? String.format("%.2f €", ordine.getPrezzoTotale()) : "");

        if (ordine != null) {  }

        if (ordine != null) { populateItemsTable(ordine); }
    }

    private void populateItemsTable(Ordine ordine) {
        nomeVoceRightColumn.setCellValueFactory(cellData -> cellData.getValue().getArticolo().nomeProperty());

        // Uses Bindings to format the price values as strings with the euro symbol in order to keep them updated if the price of the article changes
        prezzoUnitarioVoceRightColumn.setCellValueFactory(cellData -> 
            Bindings.createStringBinding(
                () -> String.format("%.2f €", cellData.getValue().getArticolo().getPrezzo()),
                cellData.getValue().getArticolo().prezzoProperty()
            )
        );
        quantitaVoceRightColumn.setCellValueFactory(cellData -> cellData.getValue().quantitaProperty());

        // Uses Bindings to format the total price values as strings with the euro symbol in order to keep them updated if the price of the article or the quantity changes
        prezzoTotaleVoceRightColumn.setCellValueFactory(cellData -> 
            Bindings.createStringBinding(
                () -> String.format("%.2f €", cellData.getValue().getPrezzoTotale()),
                cellData.getValue().prezzoTotaleProperty()
            )
        );
        vociOrdineRightTable.setItems(ordine.getVoci());
        scontoLabel.setText(String.format("%.2f %%", ordine.getSconto()));
        totaleLabel.setText(String.format("%.2f €", ordine.getPrezzoTotale()));
    }
}
