package it.unife.lp.view;

import it.unife.lp.MainApp;
import it.unife.lp.model.Articolo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MenuOverviewController {
    @FXML
    private TableView<Articolo> articoliTable;
    @FXML
    private TableColumn<Articolo, String> nomeArticoloColumn;
    @FXML
    private TableColumn<Articolo, Number> prezzoArticoloColumn;
    @FXML
    private Label nomeLabel;
    @FXML
    private Label descrizioneLabel;
    @FXML
    private Label prezzoLabel;
    @FXML
    private Label stoccaggioLabel;

    private MainApp mainApp;


    public MenuOverviewController() {}

    @FXML
    private void initialize() {
        // Initialize the items table with the columns.
        nomeArticoloColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        prezzoArticoloColumn.setCellValueFactory(cellData -> cellData.getValue().prezzoProperty());
        // Clear item details.
        showItemDetails(null);
        // Listen for selection changes and show the item details when changed.
        articoliTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldValue, newValue) -> showItemDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        // Add observable list data to the table
        articoliTable.setItems(mainApp.getArticoli());
    }

    private void showItemDetails(Articolo articolo) {
        nomeLabel.setText(articolo != null ? articolo.getNome() : "");
        descrizioneLabel.setText(articolo != null ? articolo.getDescrizione() : "");
        prezzoLabel.setText(articolo != null ? String.valueOf(articolo.getPrezzo()) : "");
        stoccaggioLabel.setText(articolo != null ? String.valueOf(articolo.getStoccaggio()) : "");
    }

}
