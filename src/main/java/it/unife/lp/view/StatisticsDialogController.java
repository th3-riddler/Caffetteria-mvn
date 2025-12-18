package it.unife.lp.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.unife.lp.MainApp;
import it.unife.lp.model.Articolo;
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

    MainApp mainApp;


    @FXML
    private void initialize() {

        statisticsTabPane.getSelectionModel().select(dailySalesTab);

        statisticsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab == dailySalesTab) {
                loadDailySalesData(null);
            } else if (newTab == inventoryTab) {
                loadInventoryData();
            } else if (newTab == topItemsTab) {
                loadTopItemsData();
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

    private void loadTopItemsData() {
        
    }

    @FXML
    private void handleLoadDailyRevenue() {
        this.mainApp.showDailyRevenueDialog(dailySalesDatePicker.getValue());
    }
}
