package it.unife.lp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.prefs.Preferences;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import it.unife.lp.model.Articolo;
import it.unife.lp.model.CaffetteriaData;
import it.unife.lp.model.MetodoPagamento;
import it.unife.lp.model.Ordine;
import it.unife.lp.model.VoceOrdine;
import it.unife.lp.view.DailyRevenueDialogController;
import it.unife.lp.view.ItemEditDialogController;
import it.unife.lp.view.MenuOverviewController;
import it.unife.lp.view.NewOrderScreenController;
import it.unife.lp.view.OrderPaymentDialogController;
import it.unife.lp.view.OrdiniViewController;
import it.unife.lp.view.ReciptViewDialogController;
import it.unife.lp.view.RootLayoutController;
import it.unife.lp.view.StatisticsDialogController;

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
        articoli.addAll(
            new Articolo("Espresso", "Caffè espresso classico", 1.50, 100),
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
            new Articolo("Acqua Frizzante", "Bottiglia di acqua frizzante", 1.00, 200),
            new Articolo("Brioche al Cioccolato", "Brioche con ripieno di cioccolato", 1.60, 100),
            new Articolo("Muffin Mirtilli", "Muffin ai mirtilli", 2.20, 80),
            new Articolo("Caffè Americano", "Caffè lungo all'americana", 1.80, 60)
        );

        Articolo espresso = articoli.get(0);
        Articolo cappuccino = articoli.get(1);
        Articolo cornettoVuoto = articoli.get(3);
        Articolo tortaCioccolato = articoli.get(5);
        Articolo succoArancia = articoli.get(7);
        Articolo acquaNaturale = articoli.get(10);
        Articolo muffinMirtilli = articoli.get(13);
        Articolo caffeAmericano = articoli.get(14);
        
        // Ordine 1 - oggi
        Ordine ordine1 = new Ordine(1);
        ordine1.setdata(LocalDate.now());
        ordine1.setMetodoPagamento(MetodoPagamento.CONTANTI);
        ordine1.setScontoPercentuale(0);
        ordine1.aggiungiVoce(new VoceOrdine(espresso, 2));
        ordine1.aggiungiVoce(new VoceOrdine(cappuccino, 1));
        ordine1.aggiungiVoce(new VoceOrdine(cornettoVuoto, 2));
        ordine1.setImportoRicevuto(10.0);
        
        // Ordine 2 - ieri
        Ordine ordine2 = new Ordine(2);
        ordine2.setdata(LocalDate.now().minusDays(1));
        ordine2.setMetodoPagamento(MetodoPagamento.CARTA_DI_CREDITO);
        ordine2.setScontoPercentuale(10);
        ordine2.aggiungiVoce(new VoceOrdine(tortaCioccolato, 1));
        ordine2.aggiungiVoce(new VoceOrdine(succoArancia, 2));
        ordine2.setImportoRicevuto(0.0);
        
        // Ordine 3 - due giorni fa
        Ordine ordine3 = new Ordine(3);
        ordine3.setdata(LocalDate.now().minusDays(2));
        ordine3.setMetodoPagamento(MetodoPagamento.CARTA_DI_DEBITO);
        ordine3.setScontoPercentuale(5);
        ordine3.aggiungiVoce(new VoceOrdine(espresso, 1));
        ordine3.aggiungiVoce(new VoceOrdine(acquaNaturale, 3));
        ordine3.aggiungiVoce(new VoceOrdine(muffinMirtilli, 2));
        ordine3.setImportoRicevuto(0.0);
        
        // Ordine 4 - oggi
        Ordine ordine4 = new Ordine(4);
        ordine4.setdata(LocalDate.now());
        ordine4.setMetodoPagamento(MetodoPagamento.CARTA_DI_CREDITO);
        ordine4.setScontoPercentuale(0);
        ordine4.aggiungiVoce(new VoceOrdine(caffeAmericano, 2));
        ordine4.aggiungiVoce(new VoceOrdine(cappuccino, 1));
        ordine4.setImportoRicevuto(0.0);
        
        // Ordine 5 - tre giorni fa
        Ordine ordine5 = new Ordine(5);
        ordine5.setdata(LocalDate.now().minusDays(3));
        ordine5.setMetodoPagamento(MetodoPagamento.CONTANTI);
        ordine5.setScontoPercentuale(15);
        ordine5.aggiungiVoce(new VoceOrdine(cornettoVuoto, 3));
        ordine5.aggiungiVoce(new VoceOrdine(tortaCioccolato, 1));
        ordine5.setImportoRicevuto(10.0);
        
        // Ordine 6 - oggi
        Ordine ordine6 = new Ordine(6);
        ordine6.setdata(LocalDate.now());
        ordine6.setMetodoPagamento(MetodoPagamento.CARTA_DI_DEBITO);
        ordine6.setScontoPercentuale(0);
        ordine6.aggiungiVoce(new VoceOrdine(succoArancia, 1));
        ordine6.aggiungiVoce(new VoceOrdine(muffinMirtilli, 1));
        ordine6.setImportoRicevuto(0.0);
        
        // Ordine 7 - ieri
        Ordine ordine7 = new Ordine(7);
        ordine7.setdata(LocalDate.now().minusDays(1));
        ordine7.setMetodoPagamento(MetodoPagamento.CARTA_DI_CREDITO);
        ordine7.setScontoPercentuale(5);
        ordine7.aggiungiVoce(new VoceOrdine(acquaNaturale, 2));
        ordine7.aggiungiVoce(new VoceOrdine(caffeAmericano, 1));
        ordine7.setImportoRicevuto(0.0);
        
        // Ordine 8 - oggi
        Ordine ordine8 = new Ordine(8);
        ordine8.setdata(LocalDate.now());
        ordine8.setMetodoPagamento(MetodoPagamento.CONTANTI);
        ordine8.setScontoPercentuale(0);
        ordine8.aggiungiVoce(new VoceOrdine(espresso, 1));
        ordine8.aggiungiVoce(new VoceOrdine(cappuccino, 1));
        ordine8.aggiungiVoce(new VoceOrdine(muffinMirtilli, 1));
        ordine8.setImportoRicevuto(5.0);
        
        // Aggiungi tutti gli ordini
        ordini.addAll(ordine1, ordine2, ordine3, ordine4, ordine5, ordine6, ordine7, ordine8);
    }

    /**
     * Gets the articles observable list
     * 
     * @return ObservableList of Articolo
     */
    public ObservableList<Articolo> getArticoli() {
        return articoli;
    }

    /**
     * Gets the orders observable list
     * 
     * @return ObservableList of Ordine
     */
    public ObservableList<Ordine> getOrdini() {
        return ordini;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Cafe App");
        initRootLayout();
        showMenuOverview();
    }

    public File getCafeFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("CafefilePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setCafeFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("CafefilePath", file.getPath());
            // Update the stage title.
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("CafefilePath");
            // Update the stage title.
            primaryStage.setTitle("Cafe App");
        }
    }

    public void loadCafeDataFromFile(File file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            CaffetteriaData data = mapper.readValue(file, CaffetteriaData.class);
            articoli.setAll(FXCollections.observableArrayList(data.getArticoli()));
            ordini.setAll(FXCollections.observableArrayList(data.getOrdini()));

            setCafeFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());
            alert.showAndWait();

            System.out.println(e.getMessage());
        }
    }

    public void saveCafeDataToFile(File file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            mapper.registerModule(new JavaTimeModule());

            CaffetteriaData caffetteriaData = new CaffetteriaData();
            caffetteriaData.setArticoli(FXCollections.observableArrayList(articoli));
            caffetteriaData.setOrdini(FXCollections.observableArrayList(ordini));
            mapper.writeValue(file, caffetteriaData);

            // Save the file path to the registry.
            setCafeFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());
            alert.showAndWait();

            System.out.println(e.getMessage());
        }
    }

    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);

            scene.getStylesheets().add(
                    getClass().getResource("css/Style.css").toExternalForm());

            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Try to load last opened person file.
        File file = getCafeFilePath();
        if (file != null) {
            loadCafeDataFromFile(file);
        }
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

            scene.getStylesheets().add(
                    MainApp.class.getResource("/it/unife/lp/css/Style.css").toExternalForm());

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
            BorderPane newOrderScreenBorderPane = (BorderPane) loader.load();
            rootLayout.setCenter(newOrderScreenBorderPane);

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

            scene.getStylesheets().add(
                    MainApp.class.getResource("/it/unife/lp/css/Style.css").toExternalForm());

            OrderPaymentDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setItem(ordine);
            controller.setOrderId(ordine.getId());

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

            scene.getStylesheets().add(
                    MainApp.class.getResource("/it/unife/lp/css/Style.css").toExternalForm());

            ReciptViewDialogController controller = loader.getController();
            controller.setItem(ordine);
            controller.setOrderId(ordine.getId());

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStatisticsDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/StatisticsDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            // dialogStage.setTitle("Ricavi Giornalieri - " + date.toString());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            scene.getStylesheets().add(
                    MainApp.class.getResource("/it/unife/lp/css/Style.css").toExternalForm());

            StatisticsDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDailyRevenueDialog(LocalDate date) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/DailyRevenueDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ricavi Giornalieri - " + date.toString());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            scene.getStylesheets().add(
                    MainApp.class.getResource("/it/unife/lp/css/Style.css").toExternalForm());

            DailyRevenueDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setItem(date);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}