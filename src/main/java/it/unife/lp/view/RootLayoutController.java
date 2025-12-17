package it.unife.lp.view;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import it.unife.lp.MainApp;

public class RootLayoutController {
    // Reference to the main application
    private MainApp mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleNew() {
        mainApp.getArticoli().clear();
        mainApp.getOrdini().clear();

        mainApp.setCafeFilePath(null);
    }

    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");

        fileChooser.getExtensionFilters().add(extFilter);

        File cafeFile = mainApp.getCafeFilePath();

        if (cafeFile != null) {
            fileChooser.initialFileNameProperty().set(cafeFile.getName());
        }

        // Show save file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadCafeDataFromFile(file);
        }
    }

    @FXML
    private void handleSave() {
        File cafeFile = mainApp.getCafeFilePath();

        if (cafeFile != null) {
            mainApp.saveCafeDataToFile(cafeFile);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");

        fileChooser.getExtensionFilters().add(extFilter);

        File cafeFile = mainApp.getCafeFilePath();

        if (cafeFile != null) {
            fileChooser.initialFileNameProperty().set(cafeFile.getName());
        }

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".json")) {
                file = new File(file.getPath() + ".json");
            }
        }

        mainApp.saveCafeDataToFile(file);
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleShowMenuOverview() {
        mainApp.showMenuOverview();
    }

    @FXML
    private void handleShowOrdiniView() {
        mainApp.showOrdiniView();
    }

    @FXML
    private void handleShowStatisticsView() {
        mainApp.showStatisticsView();
    }
}