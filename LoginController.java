package app;

import java.net.URL;
import java.util.ResourceBundle;
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

public class LoginController {
	
	private static boolean login = false;
	private String file = "C:\\Users\\f_ill\\eclipse-workspace\\UsecDashboard\\src\\application\\Usec\\users.txt";
	
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
    	authenticateUser(userNameField.getText(), passwordField.getText());
    	if(login) {
	    	Stage regStage = (Stage) loginButton.getScene().getWindow();
	    	regStage.close();
	    	openMainWindow(userNameField.getText());
    	}
    	else {
    		System.out.println("Användarnamn eller lösenord fel.");
    	}
    }
    
    private void openMainWindow(String username) {
    	
    	String screen = getScreen(username);
    	
    	Stage primaryStage = new Stage();
    	try {
			//AnchorPane root = FXMLLoader.load(getClass().getResource("UsecurityDasboard.fxml"));
			Pane root = FXMLLoader.load(getClass().getResource(screen));
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
    
    @FXML
    void initialize() {
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'LoginScreen.fxml'.";

    }
}