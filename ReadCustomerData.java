package helper;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.Writer;
import java.io.FileWriter;
import java.util.*;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import Http.GetLatLong;

public class ReadCustomerData {
	
	public static String csvFile = "C:\\Users\\f_ill\\eclipse-workspace\\UsecDashboard\\build\\build\\classes\\application\\Usec\\Säljare";
	
	public static String[] colNames = {"Personnummer", "Namn", "Tjänst", "Kundpris", "Telefon", "Adress", "Postnr och ort", " ",
										"E-Mail", "Mailfaktura", "Nyhetsmail", "Datum", "Kommentar", "FSG (SEK)", "Betalt", 
										"Mak", "Status", "Räddat", "Betaldatum", "Kundnummer", "Bricknummer", "Kommentar1", "Logga"};
	
	public static String[] realColNames = {"Personnummer", "Namn", "Tjänst", "Kundpris", "Telefon", "Adress", "Postnummer", "Ort",
			"E-Mail", "Mailfaktura", "Nyhetsmail", "Datum", "Kommentar", "FSG (SEK)", "Betalt", 
			"Mak", "Status", "Räddat", "Betaldatum", "Kundnummer", "Bricknummer", "Kommentar1"};
	
	//LÄGG TILL NIFA, JASMIN, TEDDIE SAMT ALLA ANDRA SOM INTE BLIVIT NEDLADDADE
	public static String[] allSales = {"Adrian", "DT", "Hamza", "Harald", "Jimmy",
											"Julia", "Kazem", "Lorans", "Magnus", "Markus",
											"Roberto", "Shane", "Timmy", "Viktor"
											};
	
	public static String[] header = {"Namn", "TjÃ¤nst", "Kundpris", "Betaldatum", "Adress", "Postnr och", "ort"};
	
	public static Map<String, Customer> metaMapping = new HashMap<>();
	
	private static int emptyCount = 0;
	private static boolean headerBreak = false;
	
	public static JsonObject isggData = new JsonObject();
	public static JsonObject sales = new JsonObject();
	public static JsonObject salesDep = new JsonObject(); 

	
	private static void createJsonMain() {
		
	}
	
	public static void createJsonSales(String salesName, JsonObject customers) {
		JsonObject salesNameData = new JsonObject();
		JsonObject salesNameMetadata = new JsonObject();
		salesDep.add(salesName, salesNameData);
		//Säljarens fält
		salesNameData.add("kunder", customers);
		salesNameData.add("meta data", salesNameMetadata);
		
		writeDataToFile(isggData);
		
	}
	
	private static void writeDataToFile(JsonObject data) {
		try{
			
			FileWriter file = new FileWriter("data.json");
			file.write(data.toString());
			file.close();
			System.out.println("data written to data.json");
		}catch(IOException e) {e.printStackTrace();}
			
	}
	
	public static JSONObject readData(String file) {
		//Returns the entire customer register as a JSONObject
		JSONObject jObject = new JSONObject();
		try {
			FileReader reader = new FileReader(file);
			JSONParser jsonParser = new JSONParser();
			jObject = (JSONObject) jsonParser.parse(reader);
			System.out.println("Data read");
		}catch(FileNotFoundException ex) {
			ex.printStackTrace();
		}catch(IOException ex) {
			ex.printStackTrace();
		}catch(ParseException ex){
			ex.printStackTrace();
		}catch(NullPointerException ex) {
			ex.printStackTrace();
		}
		return jObject;
	}
	
	public static void getDataNewIdentifier(String salesData, String path) {
		// metaMapping innehåller all kundinfo med colNames som sökbara fält.
		metaMapping = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))){
			//currentLine kommer vara all data om en given kund.
			String currentLine;
			while((currentLine = br.readLine()) != null) {
				//Gör currenLine mer läsbar för datorn
				List<String> modLine = new ArrayList<>();
				String[] line = currentLine.replace(",", ", ").split(",");
				//Rensa upp datan
				for(int i = 0; i < line.length-1; i++) {
					if(line[i].indexOf('"') >= 0 && i > 0) {
						line[i] = line[i].replace('"', ' ').trim();
						
						if(NumberUtils.isCreatable(line[i+1].replace('"', ' ').trim())){
							line[i] += "," + line[i+1].replace('"', ' ').trim();
						}else {line[i] += " " + line[i+1].replace('"', ' ').trim();}
						line[i+1] = "EMPTY";
					}
					
					line[i] = line[i].replaceAll("Ã¥", "å");line[i] = line[i].replaceAll("Ã…", "Å");
					line[i] = line[i].replaceAll("Ã¤", "ä");line[i] = line[i].replaceAll("Ã„", "Ä");
					line[i] = line[i].replaceAll("Ã¶", "ö");line[i] = line[i].replaceAll("Ã–", "Ö");
					line[i] = line[i].replaceAll("Ã©", "é");
					
					if(!line[i].equals("EMPTY")) {
						modLine.add(line[i]);
					}
				}
				//System.out.println(Arrays.toString(line));
				String[] ref  = new String[modLine.size()];
				line = modLine.toArray(ref);
				// skapa kunden
				Customer newCustomer = new Customer();
				String[] customerID = {""};
				int emptyCount = 0;
				for(String e : line) {
					if( e == null || e.equals(" ") || e.equals("")) {
						emptyCount += 1;
					}	
				}
				
				if (emptyCount < 15 && line.length > 15) {
					try {
						//Länka varje fält i colNames till sin data hos varje kund
						for(int i = 0; i < colNames.length; i++) {
							if(colNames[i] == " " || line[i].trim().equals("Personnummer")) {continue;}
							else if(Arrays.asList(header).contains(line[i].trim())) {headerBreak=true;break;}
							if(colNames[i].equals("Kundnummer")) {
								customerID[0] = line[i].replace('"', ' ').trim(); 
								newCustomer.updateText("Kundnummer", customerID[0]);
								continue;}
							else if(colNames[i].equals("Postnr och ort")) {
								String[] postAndState = getPostAndState(line[i]);
								newCustomer.updateText("Postnummer", postAndState[0]);
								newCustomer.updateText("Ort", postAndState[1]);
							}
							else {
								newCustomer.updateText(colNames[i], line[i].trim());
							}
						}
					}
					catch(ArrayIndexOutOfBoundsException e) {
						emptyCount = 0;
						continue;
					}
					if(!headerBreak) {
						metaMapping.put(customerID[0], newCustomer);
					}
					headerBreak=false;
						
				}
				emptyCount = 0;
				
				}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
		
	private static String[] getPostAndState(String s) {
		if(s.equals(" ") || s.equals("")) {
			s = "N/A N/A N/A";
		}
		String[] tempString = s.trim().split(" ");
		
		String postString;
		postString = tempString[0]; postString += " " + tempString[1];
		String[] finalString = {postString, tempString[2]};
		return finalString;
		
		
	}
	
	public static Map<String,Customer> getMetaMapping(String name, String path) {
		getDataNewIdentifier(name, path);
		return metaMapping;
	}
	
	public static JSONObject getJsonData(String path) {
		return readData(path);
	}
	
	public static void main(String[] args) {}	
}
