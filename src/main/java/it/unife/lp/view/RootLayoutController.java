package it.unife.lp.view;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import it.unife.lp.MainApp;

/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 *
 * @author Marco Jakob
 */
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

    // /**
    //  * Creates an empty address book.
    //  */
    // @FXML
    // private void handleNew() {
    //     mainApp.getPersonData().clear();
    //     mainApp.setPersonFilePath(null);
    // }

    // /**
    //  * Opens a FileChooser to let the user select an address book to load.
    //  */
    // @FXML
    // private void handleOpen() {
    //     FileChooser fileChooser = new FileChooser();

    //     // Set extension filter
    //     FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");

    //     fileChooser.getExtensionFilters().add(extFilter);

    //     File personFile = mainApp.getPersonFilePath();

    //     if (personFile != null)
    //         fileChooser.initialFileNameProperty().set(personFile.getName());

    //     // Show save file dialog
    //     File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

    //     if (file != null) {
    //         mainApp.loadPersonDataFromFile(file);
    //     }
    // }

    // /**
    //  * Savesthe file to the person file that is currently open. If there is no
    //  * open file, the "save as" dialog is shown
    //  * .
    //  * 
    //  */
    // @FXML
    // private void handleSave() {
    //     File personFile

    //             = mainApp
    //                     .getPersonFilePath();

    //     if (personFile != null) {
    //         mainApp
    //                 .savePersonDataToFile

    //                 (personFile);

    //     } else {
    //         handleSaveAs();
    //     }
    // }

    // /**
    //  * Opens a FileChooser to let the user select a file to save to.
    //  */
    // @FXML
    // private void handleSaveAs() {
    //     FileChooser fileChooser = new FileChooser();
    //     FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");

    //     fileChooser.getExtensionFilters().add(extFilter);

    //     File personFile = mainApp.getPersonFilePath();

    //     if (personFile != null) {
    //         fileChooser.initialFileNameProperty().set(personFile.getName());
    //     }

    //     // Show save file dialog
    //     File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

    //     if (file != null) {
    //         // Make sure it has the correct extension
    //         if (!file.getPath().endsWith(".json")) {
    //             file = new File(file.getPath() + ".json");
    //         }
    //     }

    //     mainApp.savePersonDataToFile(file);
    // }

    // /**
    //  * Opens an about dialog.
    //  */
    // @FXML
    // private void handleAbout() {
    //     Alert alert = new Alert(AlertType.INFORMATION);
    //     alert.setTitle("AddressApp");
    //     alert.setHeaderText("About");
    //     alert.setContentText("Author: Marco Jakob\nWebsite: http://code.makery.ch");
    //     alert.showAndWait();
    // }

    // /**
    //  * Closes the application.
    //  */
    // @FXML
    // private void handleExit() {
    //     System.exit(0);
    // }

    // /**
    //  * Opens the birthday statistics.
    //  */
    // @FXML
    // private void handleShowBirthdayStatistics() {
    //     mainApp.showBirthdayStatistics();
    // }
}