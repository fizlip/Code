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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.Dimension;
import java.awt.Toolkit;
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
	private Stage processingStage;
	
	/* Server ip */
	public static String serverAdress = "127.0.0.1";

	
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
    void mouseEnter(MouseEvent event) {
    	Button b = (Button) event.getSource();
    	b.setStyle("-fx-background-color: white; -fx-border-color: dodgerblue; -fx-border-width: 1px;");
    }

    @FXML
    void mouseExit(MouseEvent event) {
    	Button b = (Button) event.getSource();
    	b.setStyle("-fx-background-color: white; -fx-border-color: gray; -fx-border-width: 1px;"); 
    }
    
    @FXML
    void loginAction(ActionEvent event) {
    	String givenUsername = userNameField.getText();
    	String givenPassword = passwordField.getText();
    	if(givenPassword.equals("Server")) {
    		ServerInterface server = new ServerInterface();
    		Register.path = givenUsername;
    		Stage stage = new Stage();
    		try {
				server.start(stage);
				server.init();
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    	}
    	else {
    		send("AUTH: " + givenUsername + ": " + givenPassword);
    		try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		ProcessingController.loadProp.set("Auktentiserar...");
    		showProcessingWindow();
    	}
    }
    
    private boolean isDone() {
    	return done.get();
    }
    
    private void showProcessingWindow() {
    	processingStage = new Stage();
    	try {
	    	FXMLLoader windowLoader = new FXMLLoader(getClass().getResource("ProcessingWindow.fxml"));
	    	GridPane window = windowLoader.load();
    		Scene scene = new Scene(window,600,400);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			processingStage.initStyle(StageStyle.UNDECORATED);
			processingStage.setAlwaysOnTop(true);
			processingStage.setScene(scene);
			processingStage.show();
		}catch(IOException e) {
			//Om inladdning misslyckas skriv ut felet i terminalen
			e.printStackTrace();
    	}
    }
    
    private void closeProcessingWindow() {
    	processingStage.close();
    }
    
    private void openMainWindow(String username, String screen) {
    	Stage primaryStage = new Stage();
    	AdminParams.user.set(username);
    	UsecController.user.set(username);
    	try {
    		ProcessingController.loadProp.set("Tar reda på skärm egenskaper...");
    		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    		GridPane root = FXMLLoader.load(getClass().getResource(screen));
    		Scene scene = new Scene(root,screenSize.getWidth(),screenSize.getHeight());
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Usecurity");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest(event -> {
			    System.out.println("CLOSE WINDOW");
			    AdminController.teminateThread = true;
			    CreateCharts.terminateChartThread = true;
			    AdminController.stopRegistration = true;
			    AdminController.stopSalesmanThread = true;
			    UsecController.terminateRegistrationThread = true;
			    System.exit(0);
			});
			closeProcessingWindow();
			
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
    	return new tcpCom.Client(serverAdress, 55555, data ->{
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
    	
		ProcessingController.progressProp.set(0.25);
		
    	Stage regStage = (Stage) loginButton.getScene().getWindow();
    	regStage.close();
    	AdminController.user.set(userNameField.getText());
		UsecController.user.set(userNameField.getText());
		userProp.set(userNameField.getText());
		user = userNameField.getText();
    	if(auth.toString().equals("1") || auth.toString().equals("2")) {
    		UsecController.isActive = true;
    		openMainWindow(userNameField.getText(), "UsecurityDashboard.fxml");
		}
    	else if(auth.toString().equals("3")) {
    		UsecController.isActive = false;
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