package it.unife.lp.view;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

public class StatisticsViewController {
    // DAILY SALES TAB
    @FXML
    private DatePicker dailySalesDatePicker;
    @FXML
    private Button loadDailySalesButton;
    @FXML
    private Button dailyRevenueButton;
    @FXML
    private BarChart<String, Number> dailySalesBarChart;
    @FXML
    private CategoryAxis dailySalesXAxis;
    
    // INVENTORY TAB
    @FXML
    private BarChart<String, Number> inventoryBarChart; 
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
    private BarChart<String, Number> topItemsBarChart;
    @FXML
    private CategoryAxis topItemsXAxis;
}
