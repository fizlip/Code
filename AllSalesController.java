package app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

public class AllSalesController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private LineChart<String, Number> allSalesLinechart;

    private void applyCharts() {
    	/*
    	 * Create all the charts on screen
    	 */	    	
    	//Populate linechart
    	allSalesLinechart.getData().add(CreateCharts.salesByDaySeries);
    	
    }
    
    @FXML
    void initialize() {
        assert allSalesLinechart != null : "fx:id=\"allSalesLinechart\" was not injected: check your FXML file 'AllSalesChart.fxml'.";
        applyCharts();
    }
}