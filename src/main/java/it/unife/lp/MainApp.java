package it.unife.lp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import it.unife.lp.model.Articolo;
import it.unife.lp.model.MetodoPagamento;
import it.unife.lp.model.Ordine;
import it.unife.lp.view.ItemEditDialogController;
import it.unife.lp.view.MenuOverviewController;
import it.unife.lp.view.NewOrderScreenController;
import it.unife.lp.view.OrderPaymentDialogController;
import it.unife.lp.view.OrdiniViewController;
import it.unife.lp.view.ReciptViewDialogController;
import it.unife.lp.view.RootLayoutController;

/**
 * JavaFX App
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Articolo> articoli = FXCollections.observableArrayList();
    private ObservableList<Ordine> ordini = FXCollections.observableArrayList();

    public MainApp() {
        // Sample data
        articoli.addAll(new Articolo("Espresso", "Caffè espresso classico", 1.50, 100),
                        new Articolo("Cappuccino", "Caffè con latte e schiuma", 2.50, 80),
                        new Articolo("Latte Macchiato", "Latte con una spruzzata di caffè", 2.80, 60),
                        new Articolo("Cornetto Vuoto", "Cornetto semplice", 1.20, 150),
                        new Articolo("Cornetto alla Crema", "Cornetto ripieno di crema", 1.50, 120),
                        new Articolo("Torta al Cioccolato", "Fetta di torta al cioccolato", 3.00, 50),
                        new Articolo("Torta di Mele", "Fetta di torta di mele", 2.80, 40),
                        new Articolo("Succo d'Arancia", "Bicchiere di succo d'arancia fresco", 2.00, 70),
                        new Articolo("Torta Salata", "Fetta di torta salata con verdure", 3.50, 30),
                        new Articolo("Panino Prosciutto e Formaggio", "Panino con prosciutto e formaggio", 4.00, 90),
                        new Articolo("Acqua Naturale", "Bottiglia di acqua naturale", 1.00, 200),
                        new Articolo("Acqua Frizzante", "Bottiglia di acqua frizzante", 1.00, 200));

        ordini.addAll(new Ordine(1));
        ordini.get(0).setVoci(FXCollections.observableArrayList(
            new it.unife.lp.model.VoceOrdine(articoli.get(0), 2),
            new it.unife.lp.model.VoceOrdine(articoli.get(3), 1),
            new it.unife.lp.model.VoceOrdine(articoli.get(0), 2),
            new it.unife.lp.model.VoceOrdine(articoli.get(3), 1),
            new it.unife.lp.model.VoceOrdine(articoli.get(0), 2),
            new it.unife.lp.model.VoceOrdine(articoli.get(3), 1),
            new it.unife.lp.model.VoceOrdine(articoli.get(0), 2),
            new it.unife.lp.model.VoceOrdine(articoli.get(3), 1),
            new it.unife.lp.model.VoceOrdine(articoli.get(0), 2),
            new it.unife.lp.model.VoceOrdine(articoli.get(3), 1),
            new it.unife.lp.model.VoceOrdine(articoli.get(0), 2),
            new it.unife.lp.model.VoceOrdine(articoli.get(3), 1)
        ));
        ordini.get(0).setScontoPercentuale(10);
        ordini.get(0).setMetodoPagamento(MetodoPagamento.CARTA_DI_CREDITO);
        ordini.get(0).setImportoRicevuto(30);
    }

    /**
     * Gets the articles observable list
     * @return ObservableList of Articolo
     */
    public ObservableList<Articolo> getArticoli() {
        return articoli;
    }

    /**
     * Gets the orders observable list
     * @return ObservableList of Ordine
     */
    public ObservableList<Ordine> getOrdini() {
        return ordini;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");
        initRootLayout();
        showMenuOverview();
    }

    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Try to load last opened person file.
        // File file = getPersonFilePath();
        // if (file != null) {
        //     loadPersonDataFromFile(file);
        // }
    }

    /**
     * Shows the menu overview inside the root layout.
     */
    public void showMenuOverview() {
        try {
            // Load menu overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MenuOverview.fxml"));
            BorderPane menuOverview = (BorderPane) loader.load();
            // Set menu overview into the center of root layout.
            rootLayout.setCenter(menuOverview);
            // Give the controller access to the main app.
            MenuOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showOrdiniView() {
        try {
            // Load Ordini view.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/OrdiniView.fxml"));
            BorderPane ordiniView = (BorderPane) loader.load();
            // Set ordini view into the center of root layout.
            rootLayout.setCenter(ordiniView);
            // Give the controller access to the main app.
            OrdiniViewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showItemEditDialog(Articolo articolo, String descrizioneDialog) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ItemEditDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(descrizioneDialog);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ItemEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setItem(articolo);
            controller.setDescrizionDialog(descrizioneDialog);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showNewOrderScreen(Ordine ordine, String descrizioneDialog) {
        try {
            // Load Ordini view.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/NewOrderScreen.fxml"));
            BorderPane ordiniView = (BorderPane) loader.load();
            // Set ordini view into the center of root layout.
            rootLayout.setCenter(ordiniView);
            // Give the controller access to the main app.
            NewOrderScreenController controller = loader.getController();
            controller.setItem(ordine);
            controller.setDescrizionDialog(descrizioneDialog);
            controller.setOrderId(ordine.getId());
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showOrderPaymentDialog(Ordine ordine) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/OrderPaymentDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Pagamento Ordine " + ordine.getId());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            OrderPaymentDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setItem(ordine);
            controller.setOrderId(ordine.getId());

            // controller.setMainApp(this);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isPaymentSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showReciptViewDialog(Ordine ordine) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ReciptViewDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ricevuta Ordine " + ordine.getId());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ReciptViewDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setItem(ordine);
            controller.setOrderId(ordine.getId());

            // controller.setMainApp(this);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            // return controller.isPaymentSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            // return false;
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}