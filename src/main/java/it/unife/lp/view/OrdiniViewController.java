package it.unife.lp.view;

import java.time.LocalDate;

import it.unife.lp.MainApp;
import it.unife.lp.model.Articolo;
import it.unife.lp.model.Ordine;
import it.unife.lp.model.VoceOrdine;
import it.unife.lp.util.DateUtil;
import it.unife.lp.util.OrderTableUtil;
import javafx.beans.binding.Bindings;
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
    private Label totaleParzialeLabel;
    @FXML
    private Label totaleFinaleLabel;

    @FXML
    private HBox editDeleteOrderHbox; // HBox containing actions related to the selected order (Edit and Delete Buttons)
    @FXML
    private HBox payReciptOrderHbox; // HBox containing actions related to the selected order (Pay and Receipt Buttons)

    private MainApp mainApp;

    @FXML
    private void initialize() {
        // Initializes the orders table with the columns.
        idOrdineLeftColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        dataOrdineLeftColumn.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
        
        totaleOrdineLeftColumn.setCellValueFactory(cellData -> 
            Bindings.createStringBinding(
                () -> String.format("%.2f €", cellData.getValue().getPrezzoTotaleFinale()),
                cellData.getValue().prezzoTotaleFinaleProperty()
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
        editDeleteOrderHbox.visibleProperty().bind(
            ordiniLeftTable.getSelectionModel().selectedItemProperty().isNotNull()
        );
        // Binds the management of the order actions HBox to whether an order is selected in the left table (so that it doesn't take up space when not visible)
        editDeleteOrderHbox.managedProperty().bind(
            ordiniLeftTable.getSelectionModel().selectedItemProperty().isNotNull()
        );

        payReciptOrderHbox.visibleProperty().bind(
            ordiniLeftTable.getSelectionModel().selectedItemProperty().isNotNull()
        );

        payReciptOrderHbox.managedProperty().bind(
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

        totaleParzialeLabel.setText(ordine != null ? String.format("%.2f €", ordine.getPrezzoTotaleParziale()) : "");
        totaleFinaleLabel.setText(ordine != null ? String.format("%.2f €", ordine.getPrezzoTotaleFinale()) : "");

        if (ordine != null) { 
            OrderTableUtil.populateItemsTable(
                    vociOrdineRightTable,
                    nomeVoceRightColumn,
                    prezzoUnitarioVoceRightColumn,
                    quantitaVoceRightColumn,
                    prezzoTotaleVoceRightColumn,
                    ordine
            );
        } else {
            vociOrdineRightTable.setItems(null);
        }
    }

    @FXML
    private void handleNewOrder() {
        // Finds the next available ID based on the existing orders
        int nextId = mainApp.getOrdini().stream()
                .mapToInt(Ordine::getId)
                .max()
                .orElse(0) + 1;

        Ordine ordineTmp = new Ordine(nextId);
        boolean saveClicked = mainApp.showOrderEditDialog(ordineTmp, "Nuovo Ordine");
        if (saveClicked) {
            mainApp.getOrdini().add(ordineTmp);
        }
    }

    // @FXML
    // private void handleRemoveOrder() {
    //     Ordine selectedOrdine = ordiniLeftTable.getSelectionModel().getSelectedItem();

    //     if (selectedOrdine != null) {
    //         mainApp.getOrdini().remove(selectedOrdine);
    //     }
        
    // }

    @FXML
    private void handleEditOrder() {
        Ordine selectedOrdine = ordiniLeftTable.getSelectionModel().getSelectedItem();

        if (selectedOrdine != null) {
            boolean saveClicked = mainApp.showOrderEditDialog(selectedOrdine, "Modifica Ordine");
            if (saveClicked) {
                showOrderDetails(selectedOrdine);
            } 
        }
    }

    // @FXML
    // private void handlePayOrder() {
    //     Ordine selectedOrdine = ordiniLeftTable.getSelectionModel().getSelectedItem();

    //     if (selectedOrdine != null) {
    //         boolean isPaymentSuccessful = mainApp.showOrderPaymentDialog(selectedOrdine);
    //         if (isPaymentSuccessful) {
    //             showOrderDetails(selectedOrdine);
    //         } 
    //     }
    // }

}
