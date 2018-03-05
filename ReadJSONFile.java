import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadJSONFile {
	
	public static final String filePath = "D:\\register.json";
	
	public static void main(String[] args) {
		try {
			//Read json file
			FileReader reader = new FileReader(filePath);
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			
			// get a String from the JSON object
			String jsonData = jsonObject.toJSONString();
			System.out.println(jsonData);
		}catch(FileNotFoundException ex) {
			ex.printStackTrace();
		}catch(IOException ex) {
			ex.printStackTrace();
		}catch(ParseException ex){
			ex.printStackTrace();
		}catch(NullPointerException ex) {
			ex.printStackTrace();
		}
		
	}
	
}
