package it.unife.lp.util;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AlertsUtil {
    public static void alertWarning(Stage dialogStage, String title, String headerText, String contextText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
        return;
    }
}
