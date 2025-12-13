package it.unife.lp.view;

import java.time.LocalDate;

import it.unife.lp.MainApp;
import it.unife.lp.model.Articolo;
import it.unife.lp.model.Ordine;
import it.unife.lp.model.VoceOrdine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class OrdiniViewController {
    @FXML
    private TableView<Ordine> ordiniLeftTable;
    @FXML
    private TableColumn<Ordine, Number> idOrdineLeftColumn;
    @FXML
    private TableColumn<Ordine, LocalDate> dataOrdineLeftColumn;
    @FXML
    private TableColumn<Ordine, Number> totaleOrdineLeftColumn;
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
    private TableColumn<VoceOrdine, Number> prezzoUnitarioVoceRightColumn;
    @FXML
    private TableColumn<VoceOrdine, Number> quantitaVoceRightColumn;
    @FXML
    private TableColumn<VoceOrdine, Number> prezzoTotaleVoceRightColumn;

    @FXML
    private Label scontoLabel;
    @FXML
    private Label totaleLabel;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        // Initialize the items table with the columns.
        idOrdineLeftColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        dataOrdineLeftColumn.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
        totaleOrdineLeftColumn.setCellValueFactory(cellData -> cellData.getValue().prezzoTotaleProperty());

        // Create a cell value factory that translates the boolean 'pagato' into a string with a String-Boolean binding
        statoOrdineLeftColumn.setCellValueFactory(cellData ->
            Bindings.createStringBinding(
                () -> cellData.getValue().isPagato() ? "Pagato" : "Non Pagato",
                cellData.getValue().pagatoProperty()
            )
        );
        // Clear item details.
        showOrderDetails(null);
        // Listen for selection changes and show the item details when changed.
        ordiniLeftTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldValue, newValue) -> showOrderDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        // Add observable list data to the table
        ordiniLeftTable.setItems(mainApp.getOrdini());
    }

    private void showOrderDetails(Ordine ordine) {
        idLabel.setText(ordine != null ? String.valueOf(ordine.getId()) : "");
        dataLabel.setText(ordine != null ? String.valueOf(ordine.getDataOra()) : "");
        statoLabel.setText(ordine != null ? ordine.isPagato() ? "Pagato" : "Non Pagato" : "");
        if (ordine != null) { populateItemsTable(ordine); }
    }

    private void populateItemsTable(Ordine ordine) {
        nomeVoceRightColumn.setCellValueFactory(cellData -> cellData.getValue().getArticolo().nomeProperty());
        prezzoUnitarioVoceRightColumn.setCellValueFactory(cellData -> cellData.getValue().getArticolo().prezzoProperty());
        quantitaVoceRightColumn.setCellValueFactory(cellData -> cellData.getValue().quantitaProperty());
        prezzoTotaleVoceRightColumn.setCellValueFactory(cellData -> 
            new ReadOnlyObjectWrapper<>(cellData.getValue().getArticolo().getPrezzo() * cellData.getValue().getQuantita())
        );
        vociOrdineRightTable.setItems(ordine.getVoci());
        scontoLabel.setText(String.format("%.2f %%", ordine.getSconto()));
        totaleLabel.setText(String.format("%.2f €", ordine.getPrezzoTotale()));
    }

    // TODO: Fare meglio sta roba qua. Popolazione della tabella con le voci dell'ordine va fatta meglio, e attualmente non si vedono gli ordini nella tabella a sinistra.
}
