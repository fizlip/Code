package Http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GetLatLong {
	
	private static String latLongPath = "C:\\Users\\f_ill\\eclipse-workspace\\isggApp\\build\\build\\src\\Http\\lat_longs.json";
	
	public static JSONObject writeLatLong(String address, String state, String postalCode) {
		//API key given by Google
		String api_key = "AIzaSyAjVY1zYIDOkWkUP4MduEYoKbubQrVKxLk";
		//Get URL and write json for address and write data to file
		String url = "https://maps.googleapis.com/maps/api/geocode/json";
		File output = new File("request.json");
		
		//Full search for given address in and get geolocation from google geocoding API.
		String fullAddress = address + " " + state + " " + postalCode;		
		HttpRequest.get(url, true, "address", fullAddress, "key", api_key).receive(output);
		
		JSONObject location = new JSONObject();
		
		//Read through temp json file and obtain lat long data for given adress
		try {
			JSONObject j = readJSON("request.json");
			JSONArray js = (JSONArray) j.get("results");
			JSONObject firstI = (JSONObject) js.get(0);
			JSONObject geometry = (JSONObject) firstI.get("geometry");
			location = (JSONObject) geometry.get("location");
			//writeToJSON(location, "lat_longs.json");
		}catch(IndexOutOfBoundsException e) {System.out.println(fullAddress + " could not be bound");}
		return location;
	}
	
	public static void writeToJSON(JSONObject location, String filename) {
		JSONParser parser = new JSONParser();
		try {
			JSONArray locs = (JSONArray) parser.parse(new FileReader(latLongPath + filename));
			//Dont add duplicate locations
			if(locs.contains(location)) {System.out.println("The location: " + location.toJSONString() + " has already been added.");return;}
			
			locs.add(location);
			
			// Add location to JSON file that contains the lats and longs.
			FileWriter file = new FileWriter(latLongPath + filename);
			file.write(locs.toJSONString());
			file.close();
			System.out.println(locs.toJSONString());
			System.out.println("Locations written to lat_longs.json");
			
		}
		catch(FileNotFoundException e) {e.printStackTrace();}
		catch(IOException e) {e.printStackTrace();}
		catch(ParseException e) {e.printStackTrace();}
	}
	
	public static JSONArray getLatLongData() {
		JSONArray jsonArray = new JSONArray();
		try {
			FileReader reader = new FileReader(latLongPath);
			JSONParser jsonParser = new JSONParser();
			jsonArray = (JSONArray) jsonParser.parse(reader);
			
		}catch(FileNotFoundException ex) {
			ex.printStackTrace();
		}catch(IOException ex) {
			ex.printStackTrace();
		}catch(ParseException ex){
			ex.printStackTrace();
		}catch(NullPointerException ex) {
			ex.printStackTrace();
		}
		return jsonArray;
	}
	
	public static JSONObject readJSON(String filePath) {		
		//Read given json file using simple-JSON
		JSONObject jsonObject = new JSONObject();
		try {
			FileReader reader = new FileReader(filePath);
			
			JSONParser jsonParser = new JSONParser();
			jsonObject = (JSONObject) jsonParser.parse(reader);
			
		}catch(FileNotFoundException ex) {
			ex.printStackTrace();
		}catch(IOException ex) {
			ex.printStackTrace();
		}catch(ParseException ex){
			ex.printStackTrace();
		}catch(NullPointerException ex) {
			ex.printStackTrace();
		}
		return jsonObject;
		
	}
	
	 public static void main(String[] args) {}
}
