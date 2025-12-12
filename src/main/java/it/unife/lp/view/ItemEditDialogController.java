package it.unife.lp.view;

import it.unife.lp.model.Articolo;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ItemEditDialogController {
    @FXML
    private TextField nomeField;
    @FXML
    private TextField descrizioneField;
    @FXML
    private TextField prezzoField;
    @FXML
    private TextField stoccaggioField;
    @FXML
    private Label descrizioneDialog;

    private Stage dialogStage;
    private Articolo articolo;
    private boolean okClicked = false;

    @FXML
    private void initialize() {}

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setItem(Articolo articolo) {
        this.articolo = articolo;
        nomeField.setText(articolo.getNome());
        descrizioneField.setText(articolo.getDescrizione());
        prezzoField.setText(Double.toString(articolo.getPrezzo()));
        stoccaggioField.setText(Integer.toString(articolo.getStoccaggio()));
    }

    public void setDescrizionDialog(String descrizioneDialog) {
        this.descrizioneDialog.setText(descrizioneDialog);
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            articolo.setNome(nomeField.getText());
            articolo.setDescrizione(descrizioneField.getText());
            articolo.setPrezzo(Double.parseDouble(prezzoField.getText()));
            articolo.setStoccaggio(Integer.parseInt(stoccaggioField.getText()));
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (nomeField.getText() == null || nomeField.getText().length() == 0) {
            errorMessage += "Nome articolo non valido!\n";
        }
        if (descrizioneField.getText() == null || descrizioneField.getText().length() == 0) {
            errorMessage += "Descrizione articolo non valida!\n";
        }
        if (prezzoField.getText() == null || prezzoField.getText().length() == 0) {
            errorMessage += "Prezzo articolo non valido!\n";
        } else {
            try {
                Double.parseDouble(prezzoField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Il prezzo deve essere un numero!\n";
            }
        }
        if (stoccaggioField.getText() == null || stoccaggioField.getText().length() == 0) {
            errorMessage += "Stoccaggio articolo non valido!\n";
        } else {
            try {
                Integer.parseInt(stoccaggioField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Lo stoccaggio deve essere un numero intero!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

}
