package it.unife.lp.view;

import it.unife.lp.model.Ordine;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ReciptViewDialogController {
    
    private Stage dialogStage;
    private Ordine ordine;


    @FXML
    private Label idLabel;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setOrderId(int orderId) {
        this.idLabel.setText(Integer.toString((orderId)));
    }
}
