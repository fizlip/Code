package helper;

import java.util.*;
import java.io.FileReader;

import org.apache.poi.util.SystemOutLogger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.lynden.gmapsfx.javascript.object.LatLong;

import app.AdminController;
import helper.SalesMan.*;

public class SalesForce {
	private static List<SalesMan> salesForce = new ArrayList<>();
	public static List<SalesMan> getSalesForce() {return salesForce;}
	//LÄGG TILL JASMIN, TEDDIE
	public static String[] allSales = {"Adrian", "DT", "Hamza", "Harald", "Jimmy",
										"Julia", "Kazem", "Lorans", "Magnus", "Markus",
										"Roberto", "Shane", "Timmy", "Viktor", "Nifa", "Filippa",
										"Isak", "Julietta"
										};
	private static String path = "C:\\Users\\f_ill\\eclipse-workspace\\isggApp\\bin";
	private static int missing = 0;
	
	
	public static void createSalesForce() {
		ReadCustomerData.isggData.add("isgg", ReadCustomerData.sales);
		ReadCustomerData.sales.add("försäljning", ReadCustomerData.salesDep);
		for(String salesMan : allSales) {
			SalesMan s = new SalesMan(salesMan);
			System.out.println("Starting " +  salesMan);
			salesForce.add((SalesMan) s);
		}
	}
	public static void readSalesForce() {
		JSONObject isgg = ReadCustomerData.getJsonData("data.json");
		JSONObject main = (JSONObject) isgg.get("isgg");
		JSONObject sales = (JSONObject) main.get("försäljning");
		for(String name : allSales) {
			JSONObject saleData = (JSONObject) sales.get(name);
			JSONObject customers = (JSONObject) saleData.get("kunder");
			Object[] cs = customers.keySet().toArray();
			for(Object c : cs) {
				String id = (String) c;
				JSONObject customer = (JSONObject) customers.get(id);
				String position = (String) customer.get("Position");
				String cName = (String) customer.get("Namn");
				String comment = (String) customer.get("Kommentar");
				String status = (String) customer.get("Status");
				if(!position.equals("{}")) {
					String[] resultList = position.split(":");
					Double lng = Double.parseDouble(resultList[1].split(",")[0].replace('}', ' ').trim());
					Double lat = Double.parseDouble(resultList[2].replace('}', ' ').trim());
					Double[] pos = {lat, lng};
					String[] messageStatus = {cName + ": " + comment + "\n-" + name, status};
					AdminController.postionMap.put(pos, messageStatus);
				}
				else {
					missing += 1;
				}
			}

			//AdminController.postionMap.put(arg0, arg1)
			SalesMan s = new helper.SalesMan(name, (JSONObject)saleData);
			salesForce.add((SalesMan) s);
		}
		System.out.println("Missing: " + missing);
	}
}
