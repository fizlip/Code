package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.text.Text;
import javafx.scene.control.MenuItem;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;

public class SalesManPaneController {
	
	public static BooleanProperty update = new SimpleBooleanProperty(false);

	public static String currentAuth;
	
    @FXML
    private ResourceBundle resources;
    
    @FXML
    private MenuItem auth1;

    @FXML
    private MenuItem auth2;

    @FXML
    private MenuItem auth3;
    
    @FXML
    private Text mvpValue;
    
    @FXML
    private Text authorityText;

    @FXML
    private URL location;

    @FXML
    private Text nameText;

    @FXML
    private BarChart<String, Double> statusBarChart;

    @FXML
    private LineChart<String, Number> salesLineChart;

    @FXML
    private Text amountSoldText;

    @FXML
    private Text priceText;

    @FXML
    private Text avgPriceText;
    
    @FXML
    void updateAuth(ActionEvent event) {
    	MenuItem item = (MenuItem) event.getSource();
    	AdminController.authority.set(item.getText());
    	update.set(true);

    }

    @FXML
    void initialize() {
    	
    	avgPriceText.textProperty().bind(AdminController.avgPrice);
    	priceText.textProperty().bind(AdminController.salesmanSalary);
    	amountSoldText.textProperty().bind(AdminController.amountSoldToday);
    	nameText.textProperty().bind(AdminController.salesmanName);
    	authorityText.textProperty().bind(AdminController.authority);
    	mvpValue.textProperty().bind(AdminController.mValue);
    	
    	statusBarChart.getData().addAll(CreateCharts.makBySalesman, CreateCharts.billedBySalesman,
				CreateCharts.customersBySalesman, CreateCharts.orderBySalesman);
    	
    	System.out.println("DATA SIZE AT SALESMANPANE: " + AdminController.series.getData().size());
    	salesLineChart.getData().add(AdminController.series);

    	currentAuth = AdminController.authority.toString();
    }
}