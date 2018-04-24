package app;

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
	public static XYChart.Series<String, Integer> totalMakCat = new XYChart.Series<>();
	public static XYChart.Series<String, Integer> totalCustomersCat = new XYChart.Series<>();
	public static XYChart.Series<String, Integer> totalBilledCat = new XYChart.Series<>();
	public static XYChart.Series<String, Integer> totalOrderCat = new XYChart.Series<>();
	
	//Salesman page
	//Status series
	public static XYChart.Series<String, Integer> makBySalesman = new XYChart.Series<>();
	public static XYChart.Series<String, Integer> customersBySalesman = new XYChart.Series<>();
	public static XYChart.Series<String, Integer> billedBySalesman = new XYChart.Series<>();
	public static XYChart.Series<String, Integer> orderBySalesman = new XYChart.Series<>();
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
	
	
	private static int errors = 0;
	
	public static void addSale() {
		XYChart.Data<String, Number> data = new XYChart.Data<>(DateHandler.getCurrentTime(),1);
		salesSeries.getData().add(data);
		
	}
	
	public static void initSeries() {
		// Get customer JSON
		JSONObject salesDep = (JSONObject) Register.get("kunder");
    	Object[] salesPersons = salesDep.keySet().toArray();
    	Map<String, Integer> amountMapping = new HashMap<>();
    	
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
	private static void addData(String salesPerson, Map<String, Integer> amountMapping) {
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
	private static void traverseIds(Map<String, Integer> amountMapping, JSONObject salesDep, String salesPerson, int errors){

		//Initialize amount status to 0 for every salesman
		amountMapping.put("Makulerad", 0);amountMapping.put("Kund", 0);
		amountMapping.put("Fakturerad", 0);amountMapping.put("Order", 0);
		
		JSONObject salesData = (JSONObject) salesDep.get(salesPerson);
		try {
			Object[] ids = salesData.keySet().toArray();
			//Traverse ids
			for(Object k : ids) {
				String id = k.toString();
				JSONObject customer = (JSONObject) salesData.get(id);
				//Update status values for series, if there is no status print an error message
				System.out.println("ID: " + id);
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
	
	public static void getSalemanStatusData(String name) {
		SalesMan person = salesmanMapping.get(name);
		Integer mak = person.getMak();
		Integer customers = person.getCustomers();
		Integer order = person.getOrders();
		Integer billed = person.getBilled();	
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
	private static void allSalesSeries(JSONObject customer, Map<String, Integer> amountMapping) {
		//Update status values for series, if there is no status print an error message
		try {
			Integer amount = (Integer) amountMapping.get(customer.get("Status").toString());
			amountMapping.put(customer.get("Status").toString(),amount+=1);
		}
		catch(NullPointerException e) {
			errors+=1;
		}
	}
}
