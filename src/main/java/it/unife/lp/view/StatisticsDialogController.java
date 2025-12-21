package it.unife.lp.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.unife.lp.MainApp;
import it.unife.lp.model.Articolo;
import it.unife.lp.model.VoceOrdine;
import it.unife.lp.util.AlertsUtil;
import it.unife.lp.util.StatisticsUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

public class StatisticsDialogController {
    // TABS
    @FXML
    private TabPane statisticsTabPane;
    @FXML
    private Tab dailySalesTab;
    @FXML
    private Tab inventoryTab;
    @FXML
    private Tab topItemsTab;

    // DAILY SALES TAB
    @FXML
    private DatePicker dailySalesDatePicker;
    @FXML
    private Button dailyRevenueButton;
    @FXML
    private BarChart<String, Integer> dailySalesBarChart;
    @FXML
    private CategoryAxis dailySalesXAxis;
    
    // INVENTORY TAB
    @FXML
    private BarChart<String, Integer> inventoryBarChart; 
    @FXML
    private CategoryAxis inventoryXAxis;

    // TOP ITEMS TAB
    @FXML
    private DatePicker topItemsStartDatePicker;
    @FXML
    private DatePicker topItemsEndDatePicker;
    @FXML
    private Button loadTopItemsButton;
    @FXML
    private BarChart<String, Integer> topItemsBarChart;
    @FXML
    private CategoryAxis topItemsXAxis;

    private MainApp mainApp;

    private Stage dialogStage;

    @FXML
    private void initialize() {

        statisticsTabPane.getSelectionModel().select(dailySalesTab);

        statisticsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == dailySalesTab) {
                loadDailySalesData(null);
            } else if (newTab == inventoryTab) {
                loadInventoryData();
            } else if (newTab == topItemsTab) {
                loadTopItemsData(null, null);
            }
        });

        // Clear charts data on tab close to free memory
        dailySalesTab.addEventHandler(
            Tab.TAB_CLOSE_REQUEST_EVENT,
            event -> dailySalesBarChart.getData().clear()
        );
        // Adds a listener to load data when the date is changed
        dailySalesDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            loadDailySalesData(newDate);
        });

        inventoryTab.addEventHandler(
            Tab.TAB_CLOSE_REQUEST_EVENT,
            event -> inventoryBarChart.getData().clear()
        );
        topItemsTab.addEventHandler(
            Tab.TAB_CLOSE_REQUEST_EVENT,
            event -> topItemsBarChart.getData().clear()
        );
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        dailySalesDatePicker.setValue(LocalDate.now());
        loadDailySalesData(LocalDate.now());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void loadDailySalesData(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        dailySalesDatePicker.setValue(date);

        Map<Articolo, Integer> dailySales = StatisticsUtil.dailySalesMap(mainApp.getOrdini(), date);
        dailySales.forEach((articolo, quantita) -> {
            System.out.println(articolo.getNome() + ": " + quantita);
        });
        
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Articoli Venduti - " + date.toString());
        for (Map.Entry<Articolo, Integer> entry : dailySales.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().getNome(), entry.getValue()));
        }

        // Set X axis categories
        List<String> categories = dailySales.keySet().stream().map(Articolo::getNome).collect(Collectors.toList());
        dailySalesXAxis.setCategories(FXCollections.observableArrayList(categories));

        dailySalesBarChart.getData().clear();
        dailySalesBarChart.getData().add(series);
    }

    private void loadInventoryData() {
        Map<Articolo, Integer> inventoryData = StatisticsUtil.inventoryMap(mainApp.getArticoli());
        
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Articoli in Magazzino");
        for (Map.Entry<Articolo, Integer> entry : inventoryData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().getNome(), entry.getValue()));
        }

        // Set X axis categories
        List<String> categories = inventoryData.keySet().stream().map(Articolo::getNome).collect(Collectors.toList());
        inventoryXAxis.setCategories(FXCollections.observableArrayList(categories));
        inventoryXAxis.setTickLabelRotation(45);

        System.out.println(categories);

        for (XYChart.Data<String, Integer> data : series.getData()) {
            Tooltip.install(data.getNode(), new Tooltip(data.getXValue()));
        }

        inventoryBarChart.setMinHeight(300);
        inventoryBarChart.setMinWidth(1500);

        inventoryBarChart.getData().clear();
        inventoryBarChart.getData().add(series);
    }

    private void loadTopItemsData(LocalDate startDate, LocalDate endDate) {
        // If dates are null, set default values (first day of current month to today)
        if (startDate == null || endDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1);
            endDate = LocalDate.now();
        }
        topItemsStartDatePicker.setValue(startDate);
        topItemsEndDatePicker.setValue(endDate);

        Map<Articolo, Integer> topItemsData = StatisticsUtil.topItemsMap(mainApp.getOrdini(), startDate, endDate);
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Articoli Più Venduti - Dal " + startDate.toString() + " Al " + endDate.toString());
        for (Map.Entry<Articolo, Integer> entry : topItemsData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().getNome(), entry.getValue()));
        }
        // Set X axis categories
        List<String> categories = topItemsData.keySet().stream().map(Articolo::getNome).collect(Collectors.toList());
        topItemsXAxis.setCategories(FXCollections.observableArrayList(categories));
        topItemsXAxis.setTickLabelRotation(45);
        topItemsBarChart.getData().clear();
        topItemsBarChart.getData().add(series);
    }

    private boolean isTopItemsDateValid(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            AlertsUtil.alertWarning(
                dialogStage,
                "Date non valide",
                "Date non selezionate",
                "Seleziona entrambe le date per caricare i dati!"
            );
            return false;
        }
        if (endDate.isBefore(startDate)) {
            AlertsUtil.alertWarning(
                dialogStage,
                "Date non valide",
                "Intervallo di date non valido",
                "La data di fine non può essere precedente alla data di inizio!"
            );
            return false;
        }
        return true;
    }

    @FXML
    private void handleLoadDailyRevenue() {
        this.mainApp.showDailyRevenueDialog(dailySalesDatePicker.getValue());
    }

    @FXML
    private void handleLoadTopItems() {
        LocalDate startDate = topItemsStartDatePicker.getValue();
        LocalDate endDate = topItemsEndDatePicker.getValue();
        if (!isTopItemsDateValid(startDate, endDate)) {
            return;
        }
        loadTopItemsData(startDate, endDate);
    }
}
