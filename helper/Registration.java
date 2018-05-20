package helper;


import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//import org.json.*;

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

public class Registration {
	
	private static String registerFilePath = "Usec/register.json";
	private String user;
	
	public Registration(String user) {
		this.user = user;
	}
	
	public JSONObject registerNewCustomer(String nameField, String adressField,
									String priceField, String idField, String telephoneField, String commentField,
									String personalIdField, String stateField, String postalCodeField, 
									String service, String eMailField) {		
		//Lägg till i ordningen: namn, adress, säljare, pris, id
		
		JSONObject customer = new JSONObject();
		
		Double fsg = 0.8 * Double.parseDouble(priceField); 
		
		customer.put("Namn",nameField);
		customer.put("Adress", adressField);
		customer.put("Kundpris", priceField);
		//customer.put("Kundnummer", idField);
		customer.put("Telefon", telephoneField);
		customer.put("Betaldatum", 0);
		customer.put("Status", 0);
		customer.put("Kommentar", commentField);
		customer.put("Personnummer", personalIdField);
		customer.put("Kundpris", priceField);
		customer.put("Ort", stateField);
		customer.put("Postnummer", postalCodeField);
		customer.put("FSG (SEK)", fsg);
		customer.put("Tjänst", service);
		customer.put("E-Mail", eMailField);
				
		System.out.println("Customer added");
		return customer;
	}
}
