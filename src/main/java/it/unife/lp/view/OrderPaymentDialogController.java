package it.unife.lp.view;

import it.unife.lp.model.MetodoPagamento;
import it.unife.lp.model.Ordine;
import it.unife.lp.model.VoceOrdine;
import it.unife.lp.util.AlertsUtil;
import it.unife.lp.util.OrderTableUtil;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class OrderPaymentDialogController {

    private Stage dialogStage;
    private Ordine ordineOriginale, ordine;
    private boolean isPaymentSuccessful = false;

    @FXML
    private TableView<VoceOrdine> vociOrdineLeftTable;
    @FXML
    private TableColumn<VoceOrdine, String> nomeArticoloLeftTableColumn;
    @FXML
    private TableColumn<VoceOrdine, String> prezzoArticoloUnitarioLeftTableColumn;
    @FXML
    private TableColumn<VoceOrdine, Number> quantitaArticoloLeftTableColumn;
    @FXML
    private TableColumn<VoceOrdine, String> prezzoTotaleArticoloLeftTableColumn;

    @FXML
    private ChoiceBox<MetodoPagamento> metodoPagamentoChoiceBox;

    @FXML
    private VBox contantiPaymentVBox;
    @FXML
    private TextField importoRicevutoTextField;
    @FXML
    private Label restoDovutoLabel;

    @FXML
    private Label totaleDaPagareLabel;
    @FXML
    private Label scontoLabel;

    @FXML
    private Label idLabel;

    @FXML
    private void initialize() {
        // Populate the payment method choice box (setConverter is needed to show user-friendly strings (CARTA_DI_CREDITO -> "Carta di Credito", etc.))
        metodoPagamentoChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(MetodoPagamento metodo) {
                if (metodo == null)
                    return "";
                switch (metodo) {
                    case CONTANTI:
                        return "Contanti";
                    case CARTA_DI_CREDITO:
                        return "Carta di Credito";
                    case CARTA_DI_DEBITO:
                        return "Carta di Debito";
                    default:
                        return "";
                }
            }

            @Override
            public MetodoPagamento fromString(String string) {
                return null; // Not needed (I won't convert from String to MetodoPagamento)
            }
        });

        // Populates choice box with payment methods
        metodoPagamentoChoiceBox.getItems().setAll(MetodoPagamento.values());

        // Shows the contantiPaymentVBox only if CONTANTI is selected as payment method
        contantiPaymentVBox.visibleProperty().bind(
            metodoPagamentoChoiceBox.valueProperty().isEqualTo(MetodoPagamento.CONTANTI)
        );

        importoRicevutoTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                double importoRicevuto = Double.parseDouble(newValue);
                ordine.setImportoRicevuto(importoRicevuto);
            } catch (NumberFormatException e) {
                ordine.setImportoRicevuto(0.0);
            }
        });
    }

    public void setItem(Ordine ordineOriginale) {
        this.ordineOriginale = ordineOriginale;
        this.ordine = ordineOriginale.copy(); // Work on a copy of the order to avoid modifying the original until saved

        OrderTableUtil.populateItemsTable(
            vociOrdineLeftTable,
            nomeArticoloLeftTableColumn,
            prezzoArticoloUnitarioLeftTableColumn,
            quantitaArticoloLeftTableColumn,
            prezzoTotaleArticoloLeftTableColumn,
            ordine
        );

        scontoLabel.setText(Double.toString(ordine.getSconto()));

        totaleDaPagareLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> String.format("%.2f €", ordine.getPrezzoTotaleFinale()),
                ordine.prezzoTotaleFinaleProperty()
            )
        );

        restoDovutoLabel.textProperty().bind(
            Bindings.createStringBinding(
                () -> String.format("%.2f €", ordine.getResto()),
                ordine.restoProperty()
            )
        );
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setOrderId(int orderId) {
        this.idLabel.setText(Integer.toString((orderId)));
    }

    public boolean isPaymentSuccessful() {
        return isPaymentSuccessful;
    }

    private boolean isPaymentValid() {
        String errorMessage = "";

        if (metodoPagamentoChoiceBox.getValue() == null) {
            errorMessage += "Metodo di pagamento non selezionato!\n";
        } else if (metodoPagamentoChoiceBox.getValue() == MetodoPagamento.CONTANTI) {
            try {
                double importoRicevuto = Double.parseDouble(importoRicevutoTextField.getText());
                if (importoRicevuto < ordine.getPrezzoTotaleFinale()) {
                    errorMessage += "L'importo ricevuto deve essere almeno pari al totale da pagare!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Importo ricevuto non corretto! Inserire un numero valido.\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            AlertsUtil.alertWarning(
                dialogStage,
                "Input non valido",
                "Correggi i campi non validi",
                errorMessage
            );
            return false;
        }
    }

    @FXML
    private void handleConfirmPayment() {
        if (isPaymentValid()) {
            // If validation passes, updates the original order and closes the dialog
            ordineOriginale.setMetodoPagamento(metodoPagamentoChoiceBox.getValue());
            ordineOriginale.setImportoRicevuto(ordine.getImportoRicevuto());
            ordineOriginale.setPagato(true); // Marks the order as paid
    
            isPaymentSuccessful = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
