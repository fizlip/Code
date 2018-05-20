package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OrderController {

	public JSONObject customer = new JSONObject();
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane orderPane;
    
    @FXML
    void mouseEnter(MouseEvent event) {
    	orderPane.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
    }

    @FXML
    void mouseExit(MouseEvent event) {
    	orderPane.setStyle("-fx-background-color: lightblue; -fx-border-color: white;");

    }

    @FXML
    void mousePressed(MouseEvent event) {
    	try {
    		Stage customerStage = new Stage();
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerWindow.fxml"));
		   	GridPane root = loader.load();
    		
    		Scene scene = new Scene(root, 520, 615);
    		CustomerWindowController con = loader.getController();
    		con.customer = customer;
    		con.initWindow(customer);
    		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    		customerStage.setTitle("Kundfönster");
    		customerStage.setScene(scene);
    		customerStage.setAlwaysOnTop(true);
    		customerStage.show();
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    
    public void write() {
    	Text idText;
    	try {
    		idText = new Text((String) customer.get("Kundnummer"));
    	}
    	catch(ClassCastException e) {
    		idText = new Text(Long.toString((Long)customer.get("Kundnummer")));
    	}
        Text nameText = new Text((String) customer.get("Namn"));
        Text adressText = new Text((String) customer.get("Adress"));
        Text postalCodeText = new Text((String) customer.get("Postnr"));
        Text serviceText = new Text((String) customer.get("Tjänst"));
        Text amountText = new Text((String) customer.get("2"));
        Text badgeIdText = new Text((String) customer.get("Bricknummer"));
        Text priceText = new Text((String) customer.get("Kundpris"));
        
        orderPane.add(idText, 0, 0);
        orderPane.add(nameText, 1, 0);
        orderPane.add(adressText, 2, 0);
        orderPane.add(postalCodeText, 3, 0);
        orderPane.add(serviceText, 4, 0);
        orderPane.add(amountText, 5, 0);
        orderPane.add(badgeIdText, 6, 0);
        orderPane.add(priceText, 7, 0);
    }
    
    @FXML
    void initialize() {
        assert orderPane != null : "fx:id=\"orderPane\" was not injected: check your FXML file 'OrderPane.fxml'.";
    }
}