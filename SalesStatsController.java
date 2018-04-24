package app;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.charts.Legend.LegendItem;

import helper.Register;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

public class SalesStatsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private StackedBarChart<String, Integer> statusBarchart;

    @FXML
    private BarChart<String, Double> fsgBarchart;
    
    @FXML
    private Text totalHiredText;

    @FXML
    private Text amountSoldText;

    @FXML
    private Text avgTotSalesText;

    @FXML
    private Text avgEarnedText;
    
    public void initText() {
    	JSONObject general = (JSONObject) Register.get("generellt");
    	
    	Object amountSold = general.get("antal sälj");
    	Object hired = general.get("anställda");
    	Object income = general.get("intjänat");
    	
    	DecimalFormat df = new DecimalFormat("#.##");
    	
    	amountSoldText.setText(df.format(income));
        totalHiredText.setText(hired.toString());
        
        String avgTotSales = df.format((Double)amountSold / (Double)hired);
        String avgIncome = df.format((Double)income/(Double)hired);
        avgTotSalesText.setText(avgTotSales);
    	avgEarnedText.setText(avgIncome);
    }
    
    public void getData() {
    	/*
    	 * Create all the charts on screen
    	 */	    	
    	//Customer status series
    	statusBarchart.setTitle("Kundstatus");
    	fsgBarchart.setTitle("FSG");
    	    	
    	statusBarchart.getData().addAll(CreateCharts.totalCustomersCat,CreateCharts.totalBilledCat,
    										CreateCharts.totalMakCat,CreateCharts.totalOrderCat);
    	statusBarchart.setAnimated(true);
    	fsgBarchart.getData().add(CreateCharts.fsgSeriesByName);
    	fsgBarchart.setAnimated(false);
    	setBarColors();
    	
    }
    
    private void setBarColors() {
    	/*
    	 * Edit color of bars in total sales barchart
    	 */
    	Map<String,String> colorMapping = new HashMap<>();
    	colorMapping.put("Makulerad", "-fx-bar-fill: #003977;");
    	colorMapping.put("Kund", "-fx-bar-fill: #00c194;");
    	colorMapping.put("Fakturerad", "-fx-bar-fill: #0046af;");
    	colorMapping.put("Order", "-fx-bar-fill: #0079ff;");
    	for(XYChart.Series<String, Integer> info : statusBarchart.getData()) {
    		
    		for(XYChart.Data<String, Integer> i : info.getData()) {
    			String value = colorMapping.get(info.getName());
    			i.getNode().setStyle(value);
    		}
    	}
    	
    	for(Node n : statusBarchart.getChildrenUnmodifiable()) {
    		if(n instanceof Legend) {
    			for(LegendItem items : ((Legend)n).getItems()) {
    				{
    					String value = colorMapping.get(items.getText());
    					items.getSymbol().setStyle(value);
    				}
    			}
    		}
    	}
    }

    @FXML
    void initialize() {
        assert statusBarchart != null : "fx:id=\"statusBarchart\" was not injected: check your FXML file 'SalesStats.fxml'.";
        assert fsgBarchart != null : "fx:id=\"fsgBarchart\" was not injected: check your FXML file 'SalesStats.fxml'.";
        //Charts
        getData();
        //Numbers
        initText();

    }
}