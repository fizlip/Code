package application;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONObject;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.*;
import javafx.collections.*;

import helper.DateHandler;
import helper.Register;
import helper.SalesMan;

import java.util.*;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.shape.Rectangle;

public class CreateCharts {
	
	//SalesTab series
	public static XYChart.Series<String, Double> totalMakCat = new XYChart.Series<>();
	public static XYChart.Series<String, Double> totalCustomersCat = new XYChart.Series<>();
	public static XYChart.Series<String, Double> totalBilledCat = new XYChart.Series<>();
	public static XYChart.Series<String, Double> totalOrderCat = new XYChart.Series<>();
	
	//Salesman page
	//Status series
	public static XYChart.Series<String, Double> makBySalesman = new XYChart.Series<>();
	public static XYChart.Series<String, Double> customersBySalesman = new XYChart.Series<>();
	public static XYChart.Series<String, Double> billedBySalesman = new XYChart.Series<>();
	public static XYChart.Series<String, Double> orderBySalesman = new XYChart.Series<>();
	//status mappings
	private static Map<String, SalesMan> salesmanMapping = new HashMap<>();
	//Sales series
	public static XYChart.Series<String, Number> salesByDaySeries = new XYChart.Series<>();
	//Sales mappings
	private static Map<String, Number> salesByDayMapping = new HashMap<>();
	
	//FSG sales
	public static XYChart.Series<String, Double> fsgSeriesByName = new XYChart.Series<>();
	private static Double fsgByName = 0.0;
	
	//Sales Today
	public static List<XYChart.Series<String, Number>> salesTodayList = new ArrayList<>();
	public static XYChart.Series<String, Number> salesSeries = new XYChart.Series<>();
	
	private static String time;
	public static Number amountSold = 0;
	
	private static int errors = 0;
	public static Thread chartThread;
	public static boolean terminateChartThread = false;
	
	
	public static void startThread() {
		chartThread = new Thread("Home chart thread") {
			public void run() {
				while(!terminateChartThread) {
					time = DateHandler.getCurrentTime();
					XYChart.Data<String, Number> data = new XYChart.Data<>(time,amountSold);
		    	    Rectangle rect = new Rectangle(0, 0);
		    	    rect.setVisible(false);
		    	    data.setNode(rect);
					salesSeries.getData().add(data);
					try {
						sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		chartThread.start();
	}
	
	public static void addSale() {
		
		amountSold = amountSold.intValue() + 1;
	}
	
	public static void resetCharts() {
		//SalesTab series
		totalMakCat = new XYChart.Series<>();
		totalCustomersCat = new XYChart.Series<>();
		totalBilledCat = new XYChart.Series<>();
		totalOrderCat = new XYChart.Series<>();
		
		//Salesman page
		//Status series
		makBySalesman = new XYChart.Series<>();
		customersBySalesman = new XYChart.Series<>();
		billedBySalesman = new XYChart.Series<>();
		orderBySalesman = new XYChart.Series<>();
		//status mappings
		salesmanMapping = new HashMap<>();
		//Sales series
		salesByDaySeries = new XYChart.Series<>();
		//Sales mappings
		salesByDayMapping = new HashMap<>();
		
		//FSG sales
		fsgSeriesByName = new XYChart.Series<>();
		fsgByName = 0.0;
	}
	
	public static void initSeries(JSONObject salesDep) {
		// Get customer JSON
		errors = 0;
		ProcessingController.loadProp.set("Skapar grafer...");
    	Object[] salesPersons = salesDep.keySet().toArray();
    	Map<String, Double> amountMapping = new HashMap<>();
    	//Traverse customer data
    	for(Object s : salesPersons) {
    		//Initialize amount status to 0 for every salesman
    		String salesPerson = s.toString();
    		traverseIds(amountMapping, salesDep, salesPerson, errors);
    		
	    	totalMakCat.setName("Makulerad");
	    	totalCustomersCat.setName("Kund");
	    	totalBilledCat.setName("Fakturerad");
	    	totalOrderCat.setName("Order");
    	
	    	//Sales by day barchart
	    	addData(salesPerson, amountMapping);
	    	
	    	fsgByName = 0.0;
		}
    	
    	
    	List<Long> sortedDates = DateHandler.getFormattedDate(salesByDayMapping);
    	sortedDates = DateHandler.sortDate(sortedDates);
    	salesByDaySeries = DateHandler.getDateSeries(salesByDaySeries, salesByDayMapping, sortedDates);
    	
    	addGeneralData();
    	System.out.println(errors + " customers had no Status");
	}
	
	/*
	 * Add data to the all series
	 */
	private static void addData(String salesPerson, Map<String, Double> amountMapping) {
		//All sales series
		totalMakCat.getData().add(new XYChart.Data<>(salesPerson, amountMapping.get("Makulerad")));
		totalCustomersCat.getData().add(new XYChart.Data<>(salesPerson, amountMapping.get("Kund")));
		totalOrderCat.getData().add(new XYChart.Data<>(salesPerson, amountMapping.get("Order")));
		totalBilledCat.getData().add(new XYChart.Data<>(salesPerson, amountMapping.get("Fakturerad")));
		
		//FSG data
		fsgSeriesByName.getData().add(new XYChart.Data<>(salesPerson, fsgByName));
		//Store data for salesman page
		JSONObject salesPersonData = Register.get(salesPerson);
		SalesMan salesman = new SalesMan(salesPerson,salesPersonData);
		salesman.initStatus(amountMapping.get("Makulerad"), amountMapping.get("Fakturerad"), amountMapping.get("Kund"), 
				amountMapping.get("Order"));
		salesmanMapping.put(salesPerson, salesman);
	}
	
	/*
	 * Create series for totaSales for charts on the main salesman tab
	 */
	private static void traverseIds(Map<String, Double> amountMapping, JSONObject salesDep, String salesPerson, int errors){

		//Initialize amount status to 0 for every salesman
		amountMapping.put("Makulerad", 0.0);amountMapping.put("Kund", 0.0);
		amountMapping.put("Fakturerad", 0.0);amountMapping.put("Order", 0.0);
		
		JSONObject salesData = (JSONObject) salesDep.get(salesPerson);
		salesData = (JSONObject) salesData.get("kunder");
		try {
			Object[] ids = salesData.keySet().toArray();
			//Traverse ids
			for(Object k : ids) {
				String id = k.toString();
				JSONObject customer = (JSONObject) salesData.get(id);
				//Update status values for series, if there is no status print an error message
				allSalesSeries(customer, amountMapping);
				
				getFSG(customer);
				
				personalSalesmanSeries(customer);	
			}	
		}catch(NullPointerException e) {}
	}
	
	private static void getFSG(JSONObject customer) {
		String salaryString = customer.get("FSG (SEK)").toString().replace(',', '.');
		if(NumberUtils.isCreatable(salaryString) && customer.get("Status").equals("Kund")) {
			Double added = Double.parseDouble(salaryString);
			fsgByName += added;
		}
	}
	
	private static void addGeneralData() {
		//Allsales linechart
    	for(Map.Entry<String, Number> i : salesByDayMapping.entrySet()) {
    	    		
    		String date = i.getKey();
    	    Number amount = i.getValue();
    	    		
    	    XYChart.Data<String, Number> input = new XYChart.Data<>(date, amount);
    	    Rectangle rect = new Rectangle(0, 0);
    	    rect.setVisible(false);
    	    input.setNode(rect);
    	    	
    	    salesByDaySeries.getData().add(input);
    	}
	}
	
	public static void getSalemanStatusData(JSONObject salesman) {
		JSONObject metaData = (JSONObject) salesman.get("meta data");
		JSONObject status = (JSONObject) metaData.get("status");
		System.out.println("REQD DATA: " + status.toJSONString());
		//SalesMan person = salesmanMapping.get(metaData.get(arg0));
		Double mak = (Double) status.get("makulerad");
		Double customers = (Double) status.get("kund");
		Double order = (Double) status.get("order");
		Double billed = (Double) status.get("fakturerad");
		makBySalesman.getData().clear();
		customersBySalesman.getData().clear();
		billedBySalesman.getData().clear();
		orderBySalesman.getData().clear();
		
		makBySalesman.getData().add(new XYChart.Data<>("Makulerad", mak));
		customersBySalesman.getData().add(new XYChart.Data<>("Kund", customers));
		billedBySalesman.getData().add(new XYChart.Data<>("Fakturerad", billed));
		orderBySalesman.getData().add(new XYChart.Data<>("Order", order));
	}
	
	/*
	 * Add data to salesman personal screen
	 */
	private static void personalSalesmanSeries(JSONObject customer) {
		//Store dates for and sales for that day for the main saleslinechart in the salesman search
		if(salesByDayMapping.get(customer.get("Datum").toString()) != null){
			salesByDayMapping.put(customer.get("Datum").toString(), 
					salesByDayMapping.get(customer.get("Datum").toString()).doubleValue() + 1.0);
		}
		else {
			salesByDayMapping.put(customer.get("Datum").toString(), 0.0);
		}
	}
	
	/*
	 * Add data to allSales barchart on the main sales tab
	 */
	private static void allSalesSeries(JSONObject customer, Map<String, Double> amountMapping) {
		//Update status values for series, if there is no status print an error message
		try {
			Double amount = (Double) amountMapping.get(customer.get("Status").toString());
			Double price = Double.parseDouble(customer.get("FSG (SEK)").toString().replace(',', '.'));
			amountMapping.put(customer.get("Status").toString(),amount+=price);
		}
		catch(Exception e) {
			errors+=1;
		}
	}
}
