package application;

import java.net.URL;
import java.util.ResourceBundle;

import helper.Register;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.concurrent.TimeUnit;


public class LoginController{
	
	private static boolean login = false;
	private String file = "C:\\Users\\f_ill\\eclipse-workspace\\UsecDashboard\\src\\application\\Usec\\users.txt";
	public static tcpCom.NetworkConnection connection; 
	public static JSONObject week = new JSONObject();
	public static JSONObject cachedData = new JSONObject();
	public BooleanProperty done = new SimpleBooleanProperty(false);
	public static String user;
	public static StringProperty userProp = new SimpleStringProperty("");


	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button loginButton;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private TextField userNameField;

    @FXML
    void loginAction(ActionEvent event) {
    	String givenUsername = userNameField.getText();
    	String givenPassword = passwordField.getText();
    	send("AUTH: " + givenUsername + ": " + givenPassword);
    }
    
    private boolean isDone() {
    	return done.get();
    }
    
    private void openMainWindow(String username, String screen) {
    	Stage primaryStage = new Stage();
    	AdminParams.user.set(username);
    	UsecController.user.set(username);
    	try {
			AnchorPane root = FXMLLoader.load(getClass().getResource(screen));
    		Scene scene = new Scene(root,1290,650);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Usecurity");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    private String getScreen(String username) {
    	if(username.equals("admin")) {
    		return "AdminDashboard.fxml";
    	}
    	else {
    		return "UsecurityDasboard.fxml";
    	}
    }
    
    private void authenticateUser(String username, String password) {
    	Map<String, String> userMap = getUsers();
    	if (userMap.get(username).equals(password)) {
    		System.out.println(userMap.get(username));
    		login = true;
    	}
    	else {
    		login = false;
    	}
    	
    }
    
    private Map<String, String> getUsers(){
    	Map<String, String> users = new HashMap<>();
    	try (BufferedReader br = new BufferedReader(new FileReader(file))){
    		String currentLine;
    		while((currentLine = br.readLine()) != null) {
    			String[] input = currentLine.split(" ");
    			users.put(input[0].trim(), input[1].trim());
    		}
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    	return users;
    }
    
    private tcpCom.Client createClient(){
    	return new tcpCom.Client("127.0.0.1", 55555, data ->{
    		Platform.runLater(() ->{
    			ChatController.arrived = true;
    			AdminController.currentMessage = data.toString();
    			//System.out.println((AdminController.currentMessage));
    			String[] formattedMessage = AdminController.currentMessage.split(" START");
    			if(ChatController.type.equals("m")) {
    				AdminController.listener.addMessage();
    			}
    			else if(formattedMessage[0].equals("DATA")) {
    				JSONObject userData = parseSearch(formattedMessage[1]);
    				giveUser(userData);
    			}
    			
    			else if(formattedMessage[0].equals("SEND")) {
    				JSONObject userData = parseSearch(formattedMessage[1]);
    				giveUser(userData);
    			}
    			
    			else {
    				AdminController.listener.addSale();
    			}
    		});
    	});
    }
    
    public void giveUser(JSONObject userData) {		
    	
    	JSONObject mData = (JSONObject) userData.get("meta data");
		System.out.println(mData.toJSONString());
		String auth = (String) mData.get("auktoritet");
		System.out.println(auth);
    	
    	Stage regStage = (Stage) loginButton.getScene().getWindow();
    	regStage.close();
    	AdminController.user.set(userNameField.getText());
		UsecController.user.set(userNameField.getText());
		userProp.set(userNameField.getText());
		user = userNameField.getText();
    	if(auth.toString().equals("1") || auth.toString().equals("2")) {
    		openMainWindow(userNameField.getText(), "UsecurityDasboard.fxml");
		}
    	else if(auth.toString().equals("3")) {
    		openMainWindow(userNameField.getText(), "AdminDashboard.fxml");
    	}
    	try {
			connection.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void updateDone() {
    	this.done.set(true);
    }
    
    private JSONObject parseSearch(String search) {
    	JSONObject searchJson = new JSONObject();
    	JSONParser parser = new JSONParser();
    	try {
			searchJson = (JSONObject) parser.parse(search.toString());
		} catch (ParseException e1) {
			System.out.println("Parse error");
			e1.printStackTrace();
			System.out.println(search);
		}
    	return searchJson;
    }
    
    private void connectClient() {
    	try {
    		connection = createClient();
    		connection.startConnection();
    		//serverCom.set("Klient kopplad till localhost");
    	}catch(Exception e) {
    		//serverCom.set("Kunde inte koppla klient till localhost");
    		e.printStackTrace();
    	}
    }
    
    private void send(String message) {
		ChatController.arrived = false;
		ChatController.type = "s";
		
		try {
			connection.send(AdminController.username + " - " + message);
		} catch (Exception e) {
			//serverCom.set("Anslutning misslyckades");
			e.printStackTrace();
		}
    }
    
    @FXML
    void initialize() {
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'LoginScreen.fxml'.";
        connectClient();
        
    }
}