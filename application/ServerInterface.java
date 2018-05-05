package application;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tcpCom.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import helper.Register;
import javafx.application.*;

public class ServerInterface extends Application{

	//public static NetworkConnection connection;
	private NetworkConnection connection = createServer();
	private int portNumber = 55555;
	private TextArea messages = new TextArea();
	private Server server;
	
	private Parent createContent() {
		messages.setPrefHeight(550);
		TextField input = new TextField();
		VBox root = new VBox(20, messages, input);
		root.setPrefSize(600, 200);
		return root;
	}
	
	private Server createServer() {
    	server = new Server(55555, data ->{
    		Platform.runLater(() -> {
    			ChatController.arrived = true;
    			AdminController.currentMessage = data.toString();
    			String[] getUser = AdminController.currentMessage.split(" - ");
    			String[] formattedMessage = getUser[1].split(": ");
    			String user = getUser[0];
    			System.out.println(Arrays.toString(formattedMessage));
    			processMessage(user, formattedMessage);
    		});
    	});
    	return server;
    }
	
	public static void waitForConn() {
		Client client;
		while(true) {
			client = new tcpCom.Client("127.0.0.1", 55555, data ->{
	    		Platform.runLater(() ->{
	    			ChatController.arrived = true;
	    			AdminController.currentMessage = data.toString();
	    			String[] formattedMessage = AdminController.currentMessage.split(" START");
	    			System.out.println(formattedMessage[0]);
	    		});
	    	});
		}
	}
	
	private void processMessage(String user, String[] formattedMessage) {
		if(formattedMessage[0].equals("m")) {
			//TODO send message to all users
		}
		
		else if(formattedMessage[0].equals("AUTH")) {
			messages.appendText("\n--------------------\n");
			messages.appendText(user + " - Auktentiserar: " + formattedMessage[1] + "\n");
			authenticate(formattedMessage[1], formattedMessage[2], user);
		}
		
		else if(formattedMessage[0].equals("UPDATE")) {
			messages.appendText("\n--------------------\n");
			messages.appendText(user + " - Uppdatera element: " + formattedMessage[1] + "\n");
			update(formattedMessage[1], formattedMessage[2], formattedMessage[3], user);
		}
		else if(formattedMessage[0].equals("NEW")) {
			messages.appendText("\n--------------------\n");
			messages.appendText(user + " - Nytt element: " + formattedMessage[1] + "\n");
			add(formattedMessage[1], formattedMessage[2]);
		}
		else if(formattedMessage[0].equals("GET")) {
			messages.appendText("\n--------------------\n");
			if(formattedMessage[2].equals("meny")) {
				messages.appendText(user + " - Säljare data: " + formattedMessage[1] + "\n");
				getMenuData(formattedMessage[1], user);
			}
			else {
				messages.appendText(user + " - Sökning: " + formattedMessage[1] + "\n");
				search(StringUtils.capitalize(formattedMessage[1]), user);
			}
		}
		else if(formattedMessage[0].equals("GET") && formattedMessage[1].equals("DT")) {
			search("DT", user);
		}
		else if (formattedMessage[0].equals("s")){
			AdminController.listener.addSale();
		}
	}
	
	private void authenticate(String givenUsername, String givenPassword, String user) {
		if(Register.authenticate(givenUsername, givenPassword).equals("AUTHENTICATED")) {
    		AdminController.username = givenUsername;
    		messages.appendText("Auktentiserad\n");
    		try {
    			connection.send("DATA START" + Register.get(givenUsername));
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	else {
    		try {
    			if(givenUsername.trim().equals("NEW")) {
    				String[] passwords = givenPassword.split(";");
    				if(Register.authenticate(user, passwords[0]).equals("AUTHENTICATED")) {
    					messages.appendText("Ändrar lösenordet för: " + user +"\n");
	    				Register.changePassword(user.trim(), passwords[1]);
	    				connection.send("DATA START" + "AUTHENTICATED");
    				}
    			}
    			else {
    				messages.appendText(Register.authenticate(givenUsername, givenPassword)+"\n");
    				connection.send("DATA START" + Register.authenticate(givenUsername, givenPassword));

    			}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
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
	
	private void update(String field, String position, String data, String user) {
		messages.appendText("Uppdaterar...\n");
		if(data.equals("meta data")) {
			Register.add(field, position, data, StringUtils.capitalize(user));
			messages.appendText("uppdaterade " + data + "i registret\n");
			messages.appendText("Klart\n");
			return;
		}
		else {
			messages.appendText("Användare: " + user + "\n");
			messages.appendText("Användares auktoritet: " + Register.getAuth(user) + "\n");
			if(!Register.getAuth(user).equals("1") || position.equals(user)) {
				JSONObject customer = parseSearch(data);
				Register.add(customer, field, field);
				messages.appendText("lade till " + data + "i registret\n");
				messages.appendText("Klart\n");
			}
			else {
				Alert alert = new Alert(AlertType.ERROR, "Auktoritet 2 eller högre krävs för att göra denna ändringen.\nUppdaterigen som skrevs kommer INTE att sparas.", 
										ButtonType.OK);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.setAlwaysOnTop(true);
				alert.showAndWait();
			}
		}
	}
	
	private void add(String field, String data) {
		messages.appendText("Lägger till...\n");
		JSONObject customer = parseSearch(data);
		Register.addCustomer(customer, Integer.toString(Register.getNextId()), field);
		messages.appendText("lade till " + data + "i registret\n");
		messages.appendText("Klart\n");

	}
	
	private void getMenuData(String s, String user) {
		JSONObject result = Register.get(s);
		messages.appendText("Skickar: " + "RESULTAT" + "\n");
		try {
			connection.send("MENU START" + result);
			messages.appendText("Klart\n");
		} catch (Exception e) {
			messages.appendText("Message failed to send.");
			e.printStackTrace();
		}
	}
	
	private void search(String s, String user) {
		System.out.println("SÖKNING " + s);
		messages.appendText("Sökning sker...\n");
		if(s.trim().equals("WEEK")) {
			JSONObject sMan = (JSONObject) Register.get(user);
			JSONObject mData = (JSONObject) sMan.get("meta data");
			JSONObject week = (JSONObject) mData.get("vecka");
			//System.out.println(week.toJSONString());
			try {
				messages.appendText("Skickar: " + week + "\n");
				connection.send("DATA START" + week);
				messages.appendText("Klart\n");
			} catch (Exception e) {
				messages.appendText("Message failed to send.");
				e.printStackTrace();
			}
		}
		else {
			JSONObject result = (JSONObject) Register.get(s);
			try {
				messages.appendText("Skickar: " + "RESULTAT" + "\n");
				connection.send("SEND START" + result);
				
				messages.appendText("Klart\n");
			} catch (Exception e) {
				messages.appendText("Message failed to send.");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void init() throws Exception{
		connection.startConnection();
		messages.appendText("Listening on port " + portNumber + "\n");
	}
	
	@Override
	public void stop() throws Exception{
		server.keepRunning = false;
		connection.closeConnection();
		messages.appendText("Connection closed\n");
	}
	
	private void connectServer() {
    	try {
    		connection.startConnection();
    		AdminController.serverCom.set("Localhost öppen");
    		System.out.println("Connection open");
    	}catch(Exception e) {
    		AdminController.serverCom.set("Localhost kunde inte öppnas");
    		e.printStackTrace();
    	}
    }
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.show();
		primaryStage.setAlwaysOnTop(true);
	}
	
}
