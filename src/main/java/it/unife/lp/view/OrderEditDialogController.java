package it.unife.lp.view;

import it.unife.lp.model.Articolo;
import it.unife.lp.model.Ordine;
import it.unife.lp.model.VoceOrdine;
import it.unife.lp.util.AlertsUtil;
import it.unife.lp.util.OrderTableUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class OrderEditDialogController {

    private Stage dialogStage;
    private Ordine ordine;
    private boolean okClicked = false;
    private boolean saveClicked = false;

    @FXML
    private TableView<Articolo> articoliLeftTable;
    @FXML
    private TableColumn<Articolo, String> nomeArticoloLeftTableColumn;
    @FXML
    private TableColumn<Articolo, String> prezzoArticoloLeftTableColumn;
    
    @FXML
    private Label descrizioneDialog;

    @FXML
    private TableView<VoceOrdine> vociOrdineRightTable;
    @FXML
    private TableColumn<VoceOrdine, String> nomeArticoloRightTableColumn;
    @FXML
    private TableColumn<VoceOrdine, String> prezzoArticoloUnitarioRightTableColumn;
    @FXML
    private TableColumn<VoceOrdine, Number> quantitaArticoloRightTableColumn;
    @FXML
    private TableColumn<VoceOrdine, String> prezzoTotaleArticoloRightTableColumn;

    @FXML
    private TextField scontoPercentualeField;
    @FXML
    private Label totaleLabel;

    @FXML
    private Button addItemButton; // Button to add item to the order
    @FXML
    private Button removeItemButton; // Button to remove item from the order

    @FXML
    private HBox quantitaBox; // Box containing quantity label and text field
    @FXML
    private TextField quantitaField;

    @FXML
    private void initialize() {
        // Initialize the items table with the columns.
        nomeArticoloLeftTableColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        prezzoArticoloLeftTableColumn.setCellValueFactory(cellData -> 
            Bindings.createStringBinding(
                () -> String.format("%.2f €", cellData.getValue().getPrezzo()),
                cellData.getValue().prezzoProperty()
            )
        );

        // Listener to update the order's discount percentage when the text field changes
        scontoPercentualeField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                double sconto = Double.parseDouble(newVal);
                ordine.setScontoPercentuale(sconto);
            } catch (NumberFormatException e) {
                ordine.setScontoPercentuale(0);
            }
        });

        // The addItemButton is visible only when an item is selected on the left table and no item is selected on the right table
        addItemButton.visibleProperty().bind(
            Bindings.and(
                articoliLeftTable.getSelectionModel().selectedItemProperty().isNotNull(), // An item is selected on the left
                vociOrdineRightTable.getSelectionModel().selectedItemProperty().isNull() // No item is selected on the right
            )
        );

        addItemButton.managedProperty().bind(
            addItemButton.visibleProperty()
        );

        // The removeItemButton is visible only when an item is selected on the right table and no item is selected on the left table
        removeItemButton.visibleProperty().bind(
            Bindings.and(
                vociOrdineRightTable.getSelectionModel().selectedItemProperty().isNotNull(), // An item is selected on the right
                articoliLeftTable.getSelectionModel().selectedItemProperty().isNull() // No item is selected on the left
            )
        );

        removeItemButton.managedProperty().bind(
            removeItemButton.visibleProperty()
        );

        quantitaBox.visibleProperty().bind(
            Bindings.or(
                articoliLeftTable.getSelectionModel().selectedItemProperty().isNotNull(),
                vociOrdineRightTable.getSelectionModel().selectedItemProperty().isNotNull()
            )
        );
        
        quantitaBox.managedProperty().bind(
            quantitaBox.visibleProperty()
        );

        // Listeners to clear selection on the opposite table when an item is selected
        vociOrdineRightTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                articoliLeftTable.getSelectionModel().clearSelection();
                quantitaField.setText("1");
            }
        });

        // Listener to clear selection on the opposite table when an item is selected
        articoliLeftTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                vociOrdineRightTable.getSelectionModel().clearSelection();
                quantitaField.setText("1");
            }
        });
    }

    public void setArticoli(ObservableList<Articolo> articoli) {
        articoliLeftTable.setItems(articoli);
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSaveOrder() {
        if (isOrderValidtoSave()) {
            saveClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    private void handlePayOrder() { /* TODO */ }

    @FXML
    private void handleCancel() { /* TODO */ }
    
    private boolean isQuantitaValid() {
        String errorMessage = "";
        try {
            int quantita = Integer.parseInt(quantitaField.getText());
            if (quantita < 1) {
                errorMessage += "La quantità deve essere un numero intero positivo.\n";
            }

        } catch (NumberFormatException e) {
            errorMessage += "La quantità deve essere un numero intero valido.\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Input non valido");
            alert.setHeaderText("Correggi i campi non validi");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    @FXML
    private void handleAddItem() {
        Articolo selectedArticolo = articoliLeftTable.getSelectionModel().getSelectedItem();
        if (selectedArticolo != null) {
            // If the quantity is not valid, returns
            if (!isQuantitaValid()) { return; }

            // Else, gets the quantity to add
            int quantitaDaAggiungere = Integer.parseInt(quantitaField.getText());

            

            System.out.println("Adding quantity: " + quantitaDaAggiungere + " of article: " + selectedArticolo.getNome());
            ordine.getVoci().stream()
                .filter(voce -> voce.getArticolo().equals(selectedArticolo))
                .findFirst()
                .ifPresentOrElse(
                    voce -> {
                        if (quantitaDaAggiungere + voce.getQuantita() > selectedArticolo.getStoccaggio()) {
                            AlertsUtil.alertWarning(
                                dialogStage,
                                "Input non valido",
                                "Correggi i campi non validi",
                                "La quantità richiesta supera lo stoccaggio disponibile (" + selectedArticolo.getStoccaggio() + ").\n"
                            );
                            return;
                        }
                        voce.setQuantita(voce.getQuantita() + quantitaDaAggiungere);
                    },
                    () -> {
                        if (quantitaDaAggiungere > selectedArticolo.getStoccaggio()) {
                            AlertsUtil.alertWarning(
                                dialogStage,
                                "Input non valido",
                                "Correggi i campi non validi",
                                "La quantità richiesta supera lo stoccaggio disponibile (" + selectedArticolo.getStoccaggio() + ").\n"
                            );
                            return;
                        }
                        VoceOrdine nuovaVoce = new VoceOrdine(selectedArticolo, quantitaDaAggiungere);
                        ordine.aggiungiVoce(nuovaVoce);
                    }
                );
        }
    }

    @FXML
    private void handleRemoveItem() {
        VoceOrdine selectedVoce = vociOrdineRightTable.getSelectionModel().getSelectedItem();
        if (selectedVoce != null) {
            // If the quantity is not valid, returns
            if (!isQuantitaValid()) { return; }

            // Else, gets the quantity to add
            int quantitaDaRimuovere = Integer.parseInt(quantitaField.getText());

            if (quantitaDaRimuovere > selectedVoce.getQuantita()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("Input non valido");
                alert.setHeaderText("Correggi i campi non validi");
                alert.setContentText("La quantità da rimuovere supera la quantità presente nell'ordine (" + selectedVoce.getQuantita() + ").\n");
                alert.showAndWait();
                return;
            }

            ordine.rimuoviVoce(selectedVoce, quantitaDaRimuovere);
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setDescrizionDialog(String descrizioneDialog) {
        this.descrizioneDialog.setText(descrizioneDialog);
    }
    
    public boolean isOkClicked() {
        return okClicked;
    }

    public void setItem(Ordine ordine) {
        this.ordine = ordine;

        OrderTableUtil.populateItemsTable(
            vociOrdineRightTable,
            nomeArticoloRightTableColumn,
            prezzoArticoloUnitarioRightTableColumn,
            quantitaArticoloRightTableColumn,
            prezzoTotaleArticoloRightTableColumn,
            ordine
        );

        scontoPercentualeField.setText(Double.toString(ordine.getSconto()));
        // totaleLabel.setText(String.format("%.2f €", ordine.getPrezzoTotale()));
        totaleLabel.textProperty().bind(
        Bindings.createStringBinding(
            () -> String.format("%.2f €", ordine.getPrezzoTotale()),
            ordine.prezzoTotaleProperty()
        )
    );
    }

    private boolean isOrderValidtoSave() {
        String errorMessage = "";
        if (ordine.getVoci().isEmpty()) {
            errorMessage += "L'ordine deve contenere almeno una voce.\n";
        }
        try {
            double sconto = Double.parseDouble(scontoPercentualeField.getText());
            if (sconto < 0 || sconto > 100) {
                errorMessage += "Lo sconto percentuale deve essere compreso tra 0 e 100.\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Lo sconto percentuale deve essere un numero valido.\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Input non valido");
            alert.setHeaderText("Correggi i campi non validi");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
