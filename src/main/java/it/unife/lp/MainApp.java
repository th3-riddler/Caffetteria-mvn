package it.unife.lp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

import it.unife.lp.model.Articolo;

/**
 * JavaFX App
 */
public class MainApp extends Application {

    private static Scene scene;
    private BorderPane rootLayout;
    private ObservableList<Articolo> articoli = FXCollections.observableArrayList();

    public MainApp() {
        // Sample data
        articoli.add(new Articolo("Espresso", "Caffè espresso classico", 1.50, 100));
        articoli.add(new Articolo("Cappuccino", "Caffè con latte e schiuma", 2.50, 80));
        articoli.add(new Articolo("Latte Macchiato", "Latte con una spruzzata di caffè", 2.80, 60));
        articoli.add(new Articolo("Cornetto Vuoto", "Cornetto semplice", 1.20, 150));
        articoli.add(new Articolo("Cornetto alla Crema", "Cornetto ripieno di crema", 1.50, 120));
        articoli.add(new Articolo("Torta al Cioccolato", "Fetta di torta al cioccolato", 3.00, 50));
        articoli.add(new Articolo("Torta di Mele", "Fetta di torta di mele", 2.80, 40));
        articoli.add(new Articolo("Succo d'Arancia", "Bicchiere di succo d'arancia fresco", 2.00, 70));
        articoli.add(new Articolo("Torta Salata", "Fetta di torta salata con verdure", 3.50, 30));
        articoli.add(new Articolo("Panino Prosciutto e Formaggio", "Panino con prosciutto e formaggio", 4.00, 90));
        articoli.add(new Articolo("Acqua Naturale", "Bottiglia di acqua naturale", 1.00, 200));
        articoli.add(new Articolo("Acqua Frizzante", "Bottiglia di acqua frizzante", 1.20, 180));
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}