package app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.text.Text;

public class SalesManPaneController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text nameText;

    @FXML
    private BarChart<String, Integer> statusBarChart;

    @FXML
    private LineChart<String, Number> salesLineChart;

    @FXML
    private Text amountSoldText;

    @FXML
    private Text priceText;

    @FXML
    private Text avgPriceText;

    @FXML
    void initialize() {
    	
    	avgPriceText.textProperty().bind(AdminController.avgPrice);
    	priceText.textProperty().bind(AdminController.salesmanSalary);
    	amountSoldText.textProperty().bind(AdminController.amountSoldToday);
    	nameText.textProperty().bind(AdminController.salesmanName);
    	
    	statusBarChart.getData().addAll(CreateCharts.makBySalesman, CreateCharts.billedBySalesman,
				CreateCharts.customersBySalesman, CreateCharts.orderBySalesman);
    	salesLineChart.getData().add(AdminController.series);

    }
}