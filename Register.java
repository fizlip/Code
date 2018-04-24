package helper;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;

public class Register {
	
	private static String path = "data.json";
	private static String registerName = "register.json";
	public static String currentSalesman;
	
	
	public static void add(String data, String field, String customerId) {
		/*
		 * 'data' är datan som ska hittar 'field' är fältet som det kommer att hittas i.
		 * Denna metod kommer att användas för att lägga till ett speciellt element för en kund
		 * om elementet redan finns kommer det att ersättas
		 */
		JSONObject main = ReadCustomerData.readData("data.json");
		JSONObject isggData = (JSONObject) main.get("isgg");
		JSONObject sales = (JSONObject) isggData.get("försäljning");
		Object[] salesmen = sales.keySet().toArray();
		for(Object s : salesmen) {
			String saleName = s.toString();
			JSONObject salesmanData = (JSONObject) sales.get(saleName);
			JSONObject customers = (JSONObject) salesmanData.get("kunder");
			Object[] cs = customers.keySet().toArray();
			for (Object c : cs) {
				String id = c.toString();
				if(id.equals(customerId)) {
					JSONObject customer = (JSONObject) customers.get(id);
					System.out.println(customer.toJSONString());
					customer.put(field, data);
					update(path, main);
					return;
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
		JSONObject main = ReadCustomerData.readData("data.json");
		JSONObject isggData = (JSONObject) main.get("isgg");
		if(field.equals("isgg")) {
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
			if(field.equals(key)) {
				JSONObject r = (JSONObject) isggData.get(key);
				return r;
			}
		}
		
		JSONObject salesDep = (JSONObject) isggData.get("försäljning");
		if(field.equals("försäljning")) {
			return salesDep;
		}
		Object[] salesPeople = salesDep.keySet().toArray();
		for(Object s : salesPeople) {
			String salesPerson = s.toString();
			JSONObject salesPersonData = (JSONObject) salesDep.get(salesPerson);
			if(field.equals(salesPerson)) {
				return salesPersonData;
			}
			JSONObject customers = (JSONObject) salesPersonData.get("kunder");
			if(field.equals("kunder")) {
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
						if(field.equals(fieldResult)) {
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
		JSONObject main = ReadCustomerData.readData("register.json");
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
				salesDep.put(salesPerson, empty);
				update("register.json",main);
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
					customers.put(id, empty);
					update("register.json",main);
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
	
	public static void initGeneral() {
		int count = 0;
		JSONObject sales = get("försäljning");
		for(Object name : sales.keySet()) {
			JSONObject salesData = (JSONObject) sales.get(name);
			salesData = (JSONObject) salesData.get("kunder");
			Object[] ids = salesData.keySet().toArray();
			//Traverse ids
			for(Object k : ids) {
				JSONObject customer = (JSONObject) salesData.get(k);
				addGeneral(1, "antal sälj");
				
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
	
	
	
	public static void main(String[] args) {	
		JSONObject newCustomer = new JSONObject();
		String id = "000000000000";
		addCustomer(newCustomer, id, "Filip Zlatoidsky");
	}
}
