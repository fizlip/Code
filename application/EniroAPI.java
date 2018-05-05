package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;


public class EniroAPI { 
	private static ObjectMapper objectMapper = new ObjectMapper (); 
	private static String APIkey = "wlawTx5uEyyn1eKRuRKeffb4ZpHWHi3PeGK6YM89";
	private static String username = "fizlip";
	
	public static void main(String[] args) throws JsonParseException, JsonProcessingException, IOException { 
	URL eniroApiUrl = new URL("https://api.eniro.com/cs/search/basic?profile=Fizlip&key=259547291624653236&country=se" +
	"&version=1.1.3&search_word=pizza"); 
	
	HttpURLConnection eniroUrlConn = (HttpURLConnection) eniroApiUrl.openConnection(); 
	
	if (eniroUrlConn == null) { 
		return; 
	} 
	int respCode = eniroUrlConn.getResponseCode();
	
	if (respCode != 200) { 
		return; 
	} 
	
	
	BufferedReader in = new BufferedReader(new InputStreamReader(eniroUrlConn.getInputStream(), "UTF-8")); 
	StringBuilder strResult = new StringBuilder(); 
	
	String inputLine; 
	while ((inputLine = in.readLine()) != null) { 
		strResult.append(inputLine); 
	} 
	in.close(); 
	JsonNode json = objectMapper.readTree(objectMapper.getJsonFactory().createJsonParser(strResult.toString())); 
	
	System.out.println(json); 
	
	} 
}
