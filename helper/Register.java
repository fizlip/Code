package helper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

public class Register {
	
	public static String path = "C:\\ISGG\\data.json";
	private static String registerName = "register.json";
	public static String currentSalesman;
	private static double avgPrice = 0; 
	private static int bestDay = 0;
	private static double w1 = 10;
	private static double w2 = 0.1;
	private static double w3 = 0.01;
	public static Integer nextId = 0;
	public static boolean isRunning = false;
	
	
	public static void add(String data, String field, String position, String elementId) {
		/*
		 * 'data' är datan som ska hittar 'field' är fältet som det kommer att hittas i.
		 * Denna metod kommer att användas för att lägga till ett speciellt element för en kund
		 * om elementet redan finns kommer det att ersättas
		 */
		JSONObject main = ReadCustomerData.readData(path);
		JSONObject isggData = (JSONObject) main.get("isgg");
		JSONObject sales = (JSONObject) isggData.get("försäljning");
		Object[] salesmen = sales.keySet().toArray();
		for(Object s : salesmen) {
			String saleName = s.toString();
			JSONObject salesmanData = (JSONObject) sales.get(saleName);
			if(position.equals("meta data") && saleName.equals(elementId)) {
				JSONObject mdata = (JSONObject) salesmanData.get("meta data");
				mdata.put(field, data);
				update(path, main);
				return;
			}
			else if(field.equals("kunder")) {
				JSONObject customers = (JSONObject) salesmanData.get("kunder");
				Object[] cs = customers.keySet().toArray();
				for (Object c : cs) {
					String id = c.toString();
					if(id.equals(elementId)) {
						JSONObject customer = (JSONObject) customers.get(id);
						System.out.println(customer.toJSONString());
						customer.put(field, data);
						update(path, main);
						return;
					}
				}
			}
		}
		
	}
	
	public static void addCustomer(JSONObject customer, String id, String salesman) {
		JSONObject main = ReadCustomerData.readData(path);
		JSONObject isggData = (JSONObject) main.get("isgg");
		JSONObject sales = (JSONObject) isggData.get("försäljning");
		Object[] salesnames = sales.keySet().toArray();
		for(Object s : salesnames) {
			String salesName = s.toString();
			JSONObject salesData = (JSONObject) sales.get(salesName);
			JSONObject customers = (JSONObject) salesData.get("kunder");
			if(salesName.equals(salesman)) {
				customers.put(id, customer);
				Double fsg = (Double) customer.get("FSG (SEK)");
				JSONObject salesMData = (JSONObject) salesData.get("meta data");
				Long totSales = (Long) salesMData.get("tot sälj");
				JSONObject week = (JSONObject) salesMData.get("vecka");
				Double totFsg = (Double) salesMData.get("FSG");
				try {
					totFsg += fsg;
				}
				catch(NullPointerException e) {
					totFsg = fsg;
				}
				salesMData.put("FSG", totFsg);
				try {
					Long day = (Long) week.get(DateHandler.getCurrentWeekdaySwe());
					day += 1;
					week.put(DateHandler.getCurrentWeekdaySwe(), day);
				}
				catch(ClassCastException e) {
					Double day = (Double) week.get(DateHandler.getCurrentWeekdaySwe());
					day += 1;
					week.put(DateHandler.getCurrentWeekdaySwe(), day);
				}
				totSales += 1;
				salesMData.put("tot sälj", totSales);
				salesData.put("meta data", salesMData);
				sales.put(salesName, salesData);
				isggData.put("försäljning", sales);
				main.put("isgg", isggData);
			}
			
		}
		update(path, main);
	}
		
	public static void addMetaData(String newData, String field, String salesman) {
		JSONObject main = ReadCustomerData.readData(path);
		JSONObject isggData = (JSONObject) main.get("isgg");
		JSONObject sales = (JSONObject) isggData.get("försäljning");
		Object[] salesnames = sales.keySet().toArray();
		for(Object s : salesnames) {
			String salesName = s.toString();
			JSONObject salesData = (JSONObject) sales.get(salesName);
			JSONObject mdata = (JSONObject) salesData.get("meta data");
			if(salesName.equals(salesman)) {
				mdata.put(field, newData);
			}
		}
		update(path, main);
	}
	
	public static void add(JSONObject data, String field, String position) {
		/*
		 * Denna metod används för att lägga till ett JSONObject till registret
		 * exempel är en ny anställd eller ny avdelning.
		 */
		JSONObject main = ReadCustomerData.readData(path);
		JSONObject isggData = (JSONObject) main.get("isgg");
		if(position.equals("isgg")) {
			isggData.put(field, data);
		}
		JSONObject sales = (JSONObject) isggData.get("försäljning");
		if(isggData.keySet().contains(position)) {
			sales.put(field, data);
			update(path, main);
			return;
		}
		Object[] salesnames = sales.keySet().toArray();
		for(Object s : salesnames) {
			String salesName = s.toString();
			if(position.equals(salesName)) {
				//Lägg till ny anställd
				sales.put(field, data);
			}
			JSONObject salesData = (JSONObject) sales.get(salesName);
			JSONObject customers = (JSONObject) salesData.get("kunder");
			try {
				Object[] ids = customers.keySet().toArray();
				for(Object i : ids) {
					String id = i.toString();
					if(position.equals(id)) {
						customers.put(id, data);
					}
				}
			}catch(NullPointerException e) {}
		}
		update(path, main);
		
	}
	
	public static JSONObject get(String field) {
		/*
		 * method to get a certain field from the register
		 */
		JSONObject result = new JSONObject();
		JSONObject main = ReadCustomerData.readData(path);
		JSONObject isggData = (JSONObject) main.get("isgg");
		if(field.toLowerCase().equals("isgg")) {
			Object[] isggKeys = isggData.keySet().toArray();
			for(Object k : isggKeys) {
				String key = k.toString();
				JSONObject value = (JSONObject) isggData.get(key);
				result.put(key, value);
			}
		}
		Object[] isggKeys = isggData.keySet().toArray();
		for(Object k : isggKeys) {
			String key = k.toString();
			if(field.toLowerCase().equals(key.toLowerCase())) {
				JSONObject r = (JSONObject) isggData.get(key);
				return r;
			}
		}
		
		JSONObject salesDep = (JSONObject) isggData.get("försäljning");
		if(field.toLowerCase().equals("försäljning")) {
			return salesDep;
		}
		Object[] salesPeople = salesDep.keySet().toArray();
		for(Object s : salesPeople) {
			String salesPerson = s.toString();
			JSONObject salesPersonData = (JSONObject) salesDep.get(salesPerson);
			if(field.toLowerCase().equals(salesPerson.toLowerCase())) {
				return salesPersonData;
			}
			JSONObject customers = (JSONObject) salesPersonData.get("kunder");
			if(field.toLowerCase().equals("kunder")) {
				result.put(salesPerson, customers);
				continue;
			}
			try {
				Object[] customerIds = customers.keySet().toArray();
				for(Object i : customerIds) {
					String id = i.toString();
					JSONObject customer = (JSONObject) customers.get(id);
					if(field.equals(id) && NumberUtils.isCreatable(id)){
						currentSalesman = salesPerson;
						return customer;
					}
					Object[] customerFields = customer.keySet().toArray();
					for(Object f : customerFields) {
						String customerField = f.toString();
						String fieldResult = customer.get(customerField).toString();
						if(fieldResult.toLowerCase().toLowerCase().contains(field.toLowerCase())) {
							result.put(id, customer);
						}
					}
				}
			}catch(NullPointerException e) {return result;}
		}
		return result;
	}
	
	private static JSONObject getIsggData() {
		JSONObject main = ReadCustomerData.readData("register.json");
		JSONObject isggData = (JSONObject) main.get("isgg");
		return isggData;
	}
		
	public static void remove(String field) {
		/*
		 * remove all elements with given field name
		 */
		JSONObject main = ReadCustomerData.readData(path);
		JSONObject isggData = (JSONObject) main.get("isgg");
		JSONObject empty = new JSONObject();
		JSONObject salesDep = (JSONObject) isggData.get("försäljning");
		if(field.equals("försäljning")) {
			main.put("försäljning",empty);
			update("register.json",main);
			return;
		}
		
		Object[] isggKeys = isggData.keySet().toArray();
		for(Object k : isggKeys) {
			String key = k.toString();
			if(field.equals(key)) {
				isggData.remove(key);
			}
		}
		
		Object[] salesPeople = salesDep.keySet().toArray();
		for(Object s : salesPeople) {
			String salesPerson = s.toString();
			JSONObject salesPersonData = (JSONObject) salesDep.get(salesPerson);
			if(field.equals(salesPerson)) {
				salesDep.remove(salesPerson);
				update(path ,main);
				System.out.println("Removing: " + salesPerson);
				return;
			}
			JSONObject customers = (JSONObject) salesPersonData.get("kunder");
			if(field.equals("kunder")) {
				salesPersonData.remove("kunder");
				continue;
			}
			Object[] customerIds = customers.keySet().toArray();
			for(Object i : customerIds) {
				String id = i.toString();
				JSONObject customer = (JSONObject) customers.get(id);
				if(field.equals(id)){
					customers.remove(id);
					System.out.println(s + " removing: " + id);
					update(path,main);
					return;
				}
				Object[] customerFields = customer.keySet().toArray();
				for(Object f : customerFields) {
					String customerField = f.toString();
					String fieldResult = customer.get(customerField).toString();
					if(field.equals(fieldResult)) {
						customer.remove(customerField);
					}
				}
			}
		}
		update("register.json",main);
		
	}

	public static int getNextId(){
		JSONObject sales = get("försäljning");
		for(Object k : sales.keySet()) {
			JSONObject salesman = (JSONObject) sales.get(k);
			JSONObject customers = (JSONObject) salesman.get("kunder");
			for(Object k2 : customers.keySet()) {
				try {
					Integer id = Integer.parseInt(k2.toString());
					if(id >= nextId) {
						nextId = id;
					}
				}catch(NumberFormatException e) {
				}
			}
		}
		return nextId;
	}
	
	private static void update(String file, JSONObject register) {
		/*
		 * Update the register with added data
		 */
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(register.toJSONString());
			writer.close();
			System.out.println("register updated");
			
		}catch(IOException e) {e.printStackTrace();}
	}
	
	public static void addGeneral(double amount, String field) {
		JSONObject general = get("generellt");
		System.out.println(general.toJSONString());
		double total = Double.parseDouble(general.get(field).toString());
		total += amount;
		general.put(field, total);
		add(general, "generellt", "isgg");
	}
	
	public static void initMetaData() {
		JSONObject sales = get("försäljning");
		avgPrice = 0;
		bestDay = 0;
		for(Object k : sales.keySet()) {
			JSONObject salesData = (JSONObject) sales.get(k);
			JSONObject customers = (JSONObject) salesData.get("kunder");
			JSONObject metaData = new JSONObject();
			JSONObject week = new JSONObject();
			System.out.println(k);
			metaData.put("användarnamn", k.toString());
			metaData.put("lösenord", "ändra detta");
			metaData.put("auktoritet", "1");
			
			week.put("måndag", 0);
			week.put("tisdag", 0);
			week.put("onsdag", 0);
			week.put("torsdag", 0);
			week.put("fredag", 0);
			metaData.put("vecka", week);
			metaData.put("FSG", 0.0);
			
			try {
				metaData.put("tot sälj", customers.size());
				salesData = getStatus(salesData);
			}catch(NullPointerException e) {
				metaData.put("tot sälj", "KUNDE INTE HITTAS");
				e.printStackTrace();
			}
			add(salesData, k.toString(), "försäljning");
		}
	}
	
	public static JSONObject getStatus(JSONObject salesman) {
		// Tar emot all säljare data inklusive "kunder" och "meta data".
		int mak = 0;
		int fak = 0;
		int kund = 0;
		int order = 0;
		double fsg = 0;
		JSONObject result = new JSONObject();
		JSONObject customers = (JSONObject) salesman.get("kunder");
		JSONObject mData = (JSONObject) salesman.get("meta data");
		for(Object k : customers.keySet()) {
			try {
				Integer id = Integer.parseInt(k.toString());
				if(id > nextId) {
					nextId = id;
				}
			}catch(Exception e) {}
			try {
				JSONObject customer = (JSONObject) customers.get(k);
				try {
					Double price = Double.parseDouble(customer.get("Kundpris").toString());
					Double fsgPrice = Double.parseDouble(customer.get("FSG (SEK)").toString().replace(',', '.'));
					if(customer.get("Status").equals("Kund")) {
						fsg += fsgPrice;
					}
					avgPrice += price;
				}catch(NumberFormatException e) {System.out.println("PARSE ERROR");;}
				
				if(customer.get("Status").toString().toLowerCase().equals("makulerad")) {
					Double price = Double.parseDouble(customer.get("FSG (SEK)").toString().replace(',', '.'));
					mak += price;
				}
				else if(customer.get("Status").toString().toLowerCase().equals("fakturerad")) {
					Double price = Double.parseDouble(customer.get("FSG (SEK)").toString().replace(',', '.'));
					fak += price;
				}
				else if(customer.get("Status").toString().toLowerCase().equals("kund")) {
					Double price = Double.parseDouble(customer.get("FSG (SEK)").toString().replace(',', '.'));
					kund += price;
				}
				else if(customer.get("Status").toString().toLowerCase().equals("order")) {
					Double price = Double.parseDouble(customer.get("FSG (SEK)").toString().replace(',', '.'));
					order += price;
				}
			}catch(Exception e) {System.out.println("Kunde ej beräknas");}
			}
			result.put("makulerad", mak + 0.0);
			result.put("fakturerad", fak + 0.0);
			result.put("kund", kund + 0.0);
			result.put("order", order + 0.0);
			mData.put("FSG", fsg);
			mData.put("status", result);
			
			Double mvp = w3 * avgPrice/customers.size() * ( w1*(kund) / (mak + 0.01) + w2 * fak);
			if(mvp.equals(null)) {
				mData.put("mvp", 0.0);
			}
			else {
				mData.put("mvp", mvp);
			}
			
		return salesman;
	}
	
	public static void initGeneral() {
		int count = 0;
		JSONObject sales = get("försäljning");
		for(Object name : sales.keySet()) {
			JSONObject salesData = (JSONObject) sales.get(name);
			salesData = (JSONObject) salesData.get("kunder");
			Object[] ids = salesData.keySet().toArray();
			addGeneral(salesData.size(), "antal sälj");
			//Traverse ids
			for(Object k : ids) {
				JSONObject customer = (JSONObject) salesData.get(k);
				
				String salaryString = customer.get("FSG (SEK)").toString().replace(',', '.');
				if(NumberUtils.isCreatable(salaryString) && customer.get("Status").equals("Kund")) {
					Double added = Double.parseDouble(salaryString);
					addGeneral(added, "intjänat");
				}
				else {
					System.out.println("Error at string: " + customer.get("FSG (SEK)").toString().replace(',', '.'));
				}
				
				count+= 1;
			}
			
		}
	}
	
	public static void changePassword(String username, String newPassword) {
		add(newPassword, "lösenord", "meta data", username);
	}
	
	public static String getAuth(String salesman) {
		JSONObject data = get(salesman);
		JSONObject mData = (JSONObject) data.get("meta data");
		String auth = (String) mData.get("auktoritet");
		return auth;
	}
	
	public static String authenticate(String username, String passwordGiven) {
		JSONObject user = get(username);
		if(user.isEmpty()) {
			return "USERNAME INVALID";
		}
		JSONObject mData = (JSONObject) user.get("meta data");
		String userPassword = (String) mData.get("lösenord");
		if(userPassword.equals(passwordGiven)) {
			return "AUTHENTICATED";
		}
		return "WRONG PASSWORD";
	}
	
	public static void newSalesman(String name, String username, String password) {
		JSONObject salesmanData = new JSONObject();
		JSONObject metaData = new JSONObject();
		JSONObject customers = new JSONObject();
		
		metaData = popMetaData(StringUtils.capitalize(username), password, metaData, name);
		salesmanData.put("meta data", metaData);
		salesmanData.put("kunder", customers);
		add(salesmanData, StringUtils.capitalize(username), "försäljning");
	}
	
	private static JSONObject popMetaData(String username, String password, JSONObject metaData, String name) {
		metaData.put("användarnamn", username);
		metaData.put("lösenord", password);
		metaData.put("auktoritet", "1");
		metaData.put("Fullständigt Namn", name);
		JSONObject week = new JSONObject();
		JSONObject status = new JSONObject();
		
		status.put("makulerad", 0.0);
		status.put("kund", 0.0);
		status.put("fakturerad", 0.0);
		status.put("order", 0.0);
		
		week.put("måndag", 0.0);
		week.put("tisdag", 0.0);
		week.put("onsdag", 0.0);
		week.put("torsdag", 0.0);
		week.put("fredag", 0.0);
		metaData.put("vecka", week);
		
		metaData.put("tot sälj", 0);
		metaData.put("status", status);
		metaData.put("mvp", (Double) 0.0);
		return metaData;
		
	}
	
	public static List<Double> calcTop10() {
		List<Double> top = new ArrayList<>();
		JSONObject salesForce = get("försäljning");
		for(Object k : salesForce.keySet()) {
			JSONObject salesman = (JSONObject) salesForce.get(k);
			JSONObject mData = (JSONObject) salesman.get("meta data");
			Double m = (Double) mData.get("mvp");
			if(m != null) {
				top = insert(m, top);
			}
		}
		return top;
	}
	
	public static List<Double> insert(Double m, List<Double> top) {
		if(top.size() == 0) {
			top.add(m);
			return top;
		}
		Double middle = top.get((top.size())/2);
		System.out.println(top);
		if(m >= middle) {
			List<Double> topN = new ArrayList<>();
			List<Double> sublist1 = top.subList(0, (top.size())/2);
			List<Double> sublist2 = top.subList((top.size()-1)/2, top.size());
			topN.addAll(sublist1);topN.addAll(insert(m, sublist2));
			return topN;
		}
		else {
			List<Double> topN = new ArrayList<>();
			List<Double> sublist1 = top.subList(0, (top.size())/2);
			List<Double> sublist2 = top.subList((top.size()-1)/2, top.size());
			topN.addAll(insert(m, sublist1));
			topN.addAll(sublist2);
			return topN;
		}
	}
	
	
	private static void addSalesmanToCustomer() {
		//private static void update(String file, JSONObject register)
		JSONObject main = get("isgg");
		JSONObject sales = (JSONObject) main.get("försäljning");
		for(Object s : sales.keySet()) {
			JSONObject salesman = (JSONObject) sales.get(s);
			JSONObject customers = (JSONObject) salesman.get("kunder");
			for(Object cs : customers.keySet()) {
				JSONObject customer = (JSONObject) customers.get(cs);
				customer.put("Säljare", s.toString());
			}
		}
		JSONObject sys = new JSONObject();
		sys.put("isgg", main);
		update(path, sys);
	}
	
	private static void addLatestChange() {
		//private static void update(String file, JSONObject register)
		JSONObject main = get("isgg");
		JSONObject sales = (JSONObject) main.get("försäljning");
		for(Object s : sales.keySet()) {
			JSONObject salesman = (JSONObject) sales.get(s);
			JSONObject customers = (JSONObject) salesman.get("kunder");
			for(Object cs : customers.keySet()) {
				JSONObject customer = (JSONObject) customers.get(cs);
				customer.put("Senaste ändring", "Ingen hittad");
			}
		}
		JSONObject sys = new JSONObject();
		sys.put("isgg", main);
		update(path, sys);
	}

	public static void addOrder() {
		//private static void update(String file, JSONObject register)
		JSONObject main = get("isgg");
		JSONObject orders = new JSONObject();
		JSONObject sales = (JSONObject) main.get("försäljning");
		for(Object s : sales.keySet()) {
			JSONObject salesman = (JSONObject) sales.get(s);
			JSONObject customers = (JSONObject) salesman.get("kunder");
			for(Object cs : customers.keySet()) {
				JSONObject customer = (JSONObject) customers.get(cs);
				if(customer.get("Status").equals("Order")) {
					orders.put(cs.toString(), customer);
				}
			}
		}
		JSONObject general = (JSONObject) main.get("generellt");
		general.put("order", orders);
		JSONObject sys = new JSONObject();
		sys.put("isgg", main);
		update(path, sys);
	}
	
	
	public static JSONObject getCustomerData() {
		JSONObject cData = new JSONObject();
		JSONObject customers = new JSONObject();
		
		JSONObject mak = get("Makulerad");
		JSONObject fak = get("Fakturerad");
		JSONObject order = get("Order");
		JSONObject kund = get("Kund");
		
		for(Object id : kund.keySet()) {
			JSONObject customer = (JSONObject) kund.get(id);
			if(customer.get("Status").toString().equals("Kund")) {
				customers.put(id, customer);
			}
		}
		
		cData.put("makulerad", mak);
		cData.put("fakturerad", fak);
		cData.put("order", order);
		cData.put("kund", customers);
		return cData;
	}
	
	public static void main(String[] args) {
		JSONObject cData = getCustomerData();
		JSONObject customers = (JSONObject) cData.get("kund");
		for(Object c : customers.keySet()) {
			JSONObject customer = (JSONObject) customers.get(c);
			System.out.println(customer.get("Status"));
		}
	}
}
