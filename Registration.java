import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.json.*;

import javafx.application.Application; 
import javafx.collections.FXCollections; 
import javafx.collections.ObservableList; 

import javafx.geometry.Insets; 
import javafx.geometry.Pos; 
import javafx.event.EventHandler; 
import javafx.scene.Group; 
import javafx.scene.Scene; 
import javafx.scene.control.TextField; 
import javafx.scene.control.RadioButton; 
import javafx.scene.layout.GridPane; 
import javafx.scene.text.Text; 
import javafx.stage.Stage; 
import javafx.scene.text.Font; 
import javafx.scene.control.Button; 
import javafx.scene.input.MouseEvent; 

public class Registration extends Application{
	
	private static String registerFilePath = "D:\\register.json";
	public JSONObject customerRegister = new JSONObject();
	
	@Override 
	public void start(Stage stage) {
		//Skapa text fält med för namnet
		Text name = new Text("Namn: ");
		name.setFont(Font.font(15));
		TextField nameField = new TextField();
		
		//Skapa text fält för adress
		Text adress = new Text("Adress: ");
		adress.setFont(Font.font(15));
		TextField adressField = new TextField();
		
		//Skapa text fält för säljare
		Text salesname = new Text("Säljare: ");
		salesname.setFont(Font.font(15));
		TextField salesnameField = new TextField();
		
		//Skapa text fält för pris
		Text price = new Text("Pris: ");
		price.setFont(Font.font(15));
		TextField priceField = new TextField();
		
		Button finished = new Button("Klar");
		finished.setMinSize(125, 20);
		customerRegister = readJsonFile(registerFilePath);
		finished.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//Lägg till i ordningen: namn, adress, säljare, pris
				
				JSONObject customer = new JSONObject();
				
				customer.put("namn",nameField.getText());
				customer.put("adress", adressField.getText());
				customer.put("säljare", salesnameField.getText());
				customer.put("pris", priceField.getText());
				
				
				//Lägg till kund i kundregister
				customerRegister.put(3, customer.toJSONString());
				try(FileWriter file = new FileWriter(registerFilePath)){
					file.write(customerRegister.toString());
					file.flush();
				}catch(IOException e) {
					e.printStackTrace();
				}
				
				System.out.println(customerRegister);
				
				stage.close();
			}
		});
		
		GridPane gridPane = new GridPane();
		gridPane.setMinSize(250, 250);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		
		gridPane.setVgap(5);
		gridPane.setHgap(5);
		
		//Setting the grid alignment
		gridPane.setAlignment(Pos.CENTER);
				
		//Arranging all the nodes in the grid
		gridPane.add(name, 0, 0);
		gridPane.add(nameField, 1, 0);
		gridPane.add(adress, 0, 1);
		gridPane.add(adressField, 1, 1);
		gridPane.add(salesname, 0, 2);
		gridPane.add(salesnameField, 1, 2);
		gridPane.add(price, 0, 3);
		gridPane.add(priceField, 1, 3);
		gridPane.add(finished, 1, 4);
		
		
		
		Scene scene = new Scene(gridPane);
		stage.setTitle("Kund registrering");
		stage.setScene(scene);
		stage.show();
	}
	
	public JSONObject readJsonFile(String filename) {
		try {
			FileReader jsonFile = new FileReader(filename);
			
			if(jsonFile.read() == -1) {
				try(FileWriter file = new FileWriter(registerFilePath)){
					return customerRegister;
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(filename));
			return jsonObject;
		}catch(FileNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}catch(IOException ex) {
			ex.printStackTrace();
			return null;
		}catch(ParseException ex){
			ex.printStackTrace();
			return null;
		}catch(NullPointerException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
}
