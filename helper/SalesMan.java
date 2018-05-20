package helper;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.io.File;

import org.apache.commons.lang3.math.NumberUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SalesMan {
	
	private String name;
	private double salary = 0;
	private int amountSold;
	private int maxSold;
	private JSONObject salesJSON;
	private Map<String, Customer> salesMapping = new HashMap<>();
	
	private Map<String, Number> mapping = new HashMap<>();
	private List<Integer> pricingList = new ArrayList<>();
	
	private Double mak;
	private Double customers;
	private Double billed;
	private Double orders;
	
	public String[] realColNames = {"Personnummer", "Namn", "Tjänst", "Kundpris", "Telefon", "Adress", "Postnummer", "Ort",
			"E-Mail", "Mailfaktura", "Nyhetsmail", "Datum", "Kommentar", "FSG (SEK)", "Betalt", 
			"Mak", "Status", "Räddat", "Betaldatum", "Kundnummer", "Bricknummer", "Kommentar1"};
	
	//public SalesMan(String name, Map<String, Customer> salesMapping) {
	//	this.name = name;
	//	this.salesMapping = salesMapping;
	//	salesMapping = createMetaMapping();		
	//}
	public SalesMan(String name, double salary, int amountSold, int maxSold) {
		this.name = name;
		this.salary = salary;
		this.amountSold = amountSold;
		this.maxSold = maxSold;
		createMetaMapping();		
	}
	public SalesMan(String name, double salary, int amountSold) {
		this.name = name;
		this.salary = salary;
		this.amountSold = amountSold;
		createMetaMapping();		
	}
	public SalesMan(String name, double salary) {
		this.name = name;
		this.salary = salary;
		createMetaMapping();
	}
	
	public SalesMan(String name) {
		this.name = name; 
		createMetaMapping();
		salary = getSalary();
		amountSold = salesMapping.keySet().size();
		}
	
	public SalesMan(String name, JSONObject salesManJson) {
		this.name = name;
		this.salesJSON = salesManJson;
		createSalary((JSONObject)salesManJson.get("kunder"));
		salary = getSalary();
		mapping = getBarData();
		JSONObject customers = (JSONObject) salesManJson.get("kunder");
		try {
			amountSold = customers.keySet().size();
		}catch(NullPointerException e) {amountSold = 0;}
	}
	
	//Få egenskaper
	public String getName() {return name;}
	public int getMaxSold() {return maxSold;}

	public int getAmountSold() {
		return amountSold;
	}
	
	public void createSalary(JSONObject customerData) {
		String salaryString = new String();
		try {
			Object[] keys = customerData.keySet().toArray();
			for(Object key: keys) {
				String customerID = key.toString();
				JSONObject customer = (JSONObject) customerData.get(customerID);
				salaryString = customer.get("FSG (SEK)").toString().replace(',', '.');
				if(NumberUtils.isCreatable(salaryString)) {
					salary += Double.parseDouble(salaryString);
				}
			}
		}catch(NullPointerException e) {}
	}
	
	public double getSalary() {
		return salary;
	}
	
	public Map<String, Customer> getSalesMapping(){
		return salesMapping;
	}
	
	public void createMetaMapping() {
		String path = "C:\\Users\\f_ill\\eclipse-workspace\\UsecDashboard\\build\\build\\classes\\application\\Usec\\Säljare\\";
		File folder = new File(path);

		JsonObject customers = new JsonObject();
		for (final File fileEntry : folder.listFiles()) {
			String[] sList = fileEntry.getName().toString().split("\\.");
			String[] array = (sList[0].split(" "));
			if(Arrays.asList(array).contains(name)) {
				System.out.println(fileEntry.toString());
				Map<String, Customer> mapping = ReadCustomerData.getMetaMapping(name, fileEntry.toString());
				for(Map.Entry<String, Customer> customerInfo : mapping.entrySet()) {
					JsonObject jsonCustomer = new JsonObject();
					Customer c = customerInfo.getValue();
					String id = customerInfo.getKey();
					for(String field : realColNames) {
						jsonCustomer.addProperty(field, c.get(field));
					}
					customers.add(id, jsonCustomer);
				}
			}
	    }
		ReadCustomerData.createJsonSales(name, customers);
		System.out.println(name + " finished.");
	}
	
	public void addToList(Integer i) {
		pricingList.add(i);
	}
	
	public List<Integer> getPricingList(){return pricingList;}
	public Map<String, Number> getMapping(){return mapping;}
	
	public void initStatus(Double mak, Double billed, Double customers, Double order) {
		this.mak = mak;
		this.billed = billed;
		this.customers = customers;
		this.orders = order;
	}
	
	public Double getMak() {return mak;}
	public Double getBilled() {return billed;}
	public Double getCustomers() {return customers;}
	public Double getOrders() {return orders;}
	
	public Map<String, Number> getBarData(){
		JSONObject customers = (JSONObject) salesJSON.get("kunder");
		Map<String, Number> barData = new HashMap<>();
		try {
			Object[] keys = customers.keySet().toArray();
			for (Object key : keys){
				JSONObject customerInfo = (JSONObject) customers.get(key);
				if (barData.containsKey(customerInfo.get("Datum"))){
					Integer oldVal = (Integer)barData.get(customerInfo.get("Datum"));
					oldVal += 1;
					barData.put((String) customerInfo.get("Datum"), (Number)oldVal);
				}
				else {
					barData.put((String)customerInfo.get("Datum"), 1);
				}
				
			}
		}catch(NullPointerException e) {}
		return barData;
	}
	
	
}
