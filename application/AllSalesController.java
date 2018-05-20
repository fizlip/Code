package application;

import java.net.URL;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

public class AllSalesController {

	public JSONObject data;
	
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
    	System.out.println("SIZE: " + CreateCharts.salesByDaySeries.getData().size());
    	allSalesLinechart.getData().add(CreateCharts.salesByDaySeries);
    	
    }
    
    public static void updateChart(JSONObject register) {
    	CreateCharts.resetCharts();
    	CreateCharts.initSeries(register);
    }
    
    @FXML
    void initialize() {
        assert allSalesLinechart != null : "fx:id=\"allSalesLinechart\" was not injected: check your FXML file 'AllSalesChart.fxml'.";
        applyCharts();
    }
}