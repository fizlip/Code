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
	private int logInCount = 0;
	
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
		
		else if(formattedMessage[0].equals("INC")) {
			messages.appendText("\n--------------------\n");
			messages.appendText("Ny koppling");
			inc(formattedMessage[1], user);
		}
		
		else if(formattedMessage[0].equals("AUTH")) {
			messages.appendText("\n--------------------\n");
			messages.appendText(user + " - Auktentiserar: " + formattedMessage[1] + "\n");
			authenticate(formattedMessage[1], formattedMessage[2], user);
		}
		
		else if(formattedMessage[0].equals("UPDATE")) {
			messages.appendText("\n--------------------\n");
			messages.appendText(user + " - Uppdatera element: " + formattedMessage[1] + "\n");
			update(formattedMessage[1], formattedMessage[2], formattedMessage[3], formattedMessage[4], user);
		}
		else if(formattedMessage[0].equals("NEW")) {
			messages.appendText("\n--------------------\n");
			messages.appendText(user + " - Nytt element: " + formattedMessage[1] + "\n");
			add(formattedMessage[1], formattedMessage[2], user);
		}
		else if(formattedMessage[0].equals("GET")) {
			messages.appendText("\n--------------------\n");
			if(formattedMessage[2].equals("init")) {
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
	
	private void inc(String cmd, String user) {
		messages.appendText("\nBörjar inkrementering...\n");
		if(cmd.equals("LOGIN")) {
			messages.appendText("Antal inloggade: " + connection.getConns().size() + "\n" );

			messages.appendText("LOGIN...\n");
			try {
				connection.sendAll("INC LOGIN");
				messages.appendText("Klart\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void authenticate(String givenUsername, String givenPassword, String user) {
		if(!givenUsername.trim().equals("NEW") && Register.authenticate(givenUsername, givenPassword).equals("AUTHENTICATED")) {
    		AdminController.username = givenUsername;
    		messages.appendText("Auktentiserar...\n");
    		try {
    			connection.send("DATA START" + Register.get(givenUsername));
    			messages.appendText("Klart\n");
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
	
	private void update(String field, String position, String data, String key, String user) {
		messages.appendText("Uppdaterar...\n");
		System.out.println(field + " " + position + " " + data + " " + user);
		if(data.equals("meta data")) {
			JSONObject search = Register.get(key);
			JSONObject mData = (JSONObject) search.get("meta data");
			String salesman = (String) mData.get("användarnamn");
			Register.add(field, position, data, salesman);
			messages.appendText("Uppdaterade " + position +  " för " + salesman + " i registret\n");
			messages.appendText("Klart\n");
			return;
		}
		else {
			messages.appendText("Användare: " + user + "\n");
			messages.appendText("Användares auktoritet: " + Register.getAuth(user) + "\n");
			if(!Register.getAuth(user).equals("1") || position.equals(user)) {
				JSONObject customer = parseSearch(data);
				Register.add(customer, field, field);
				Register.addOrder();
				try {
					connection.send("DATA;update START" + Register.get("order"));
				} catch (Exception e) {
					e.printStackTrace();
				}
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
	
	private void add(String field, String data, String user) {
		messages.appendText("Lägger till...\n");
		if(field.equals("salesman")) {
			String[] newUser = data.split(";");
			Register.newSalesman(newUser[0], newUser[1], newUser[2]);
			messages.appendText(newUser[0] + " har blivit tillagd.\n");
			messages.appendText("Klart\n");
			Alert alert = new Alert(AlertType.CONFIRMATION, newUser[0] + " har lagts till hos ISGG.", 
					ButtonType.OK);
			alert.showAndWait();
		}
		else {
			JSONObject customer = parseSearch(data);
			System.out.println(customer.toJSONString());
			Integer id = Register.getNextId()+1;
			customer.put("Kundnummer", id.toString());
			messages.appendText(user + "nytt sälj\n");
			Register.addCustomer(customer, Integer.toString(id), StringUtils.capitalize(user));
			try {
				messages.appendText("Skickar säljs signal\n");
				connection.send("SALE START" + customer.toJSONString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			messages.appendText("lade till " + data + "i registret\n");
			messages.appendText("Klart\n");
		}

	}
	
	private void getMenuData(String s, String user) {
		JSONObject result = Register.get(s);
		JSONObject userData = Register.get(user);
		messages.appendText("Skickar: " + "resultatet för " + s + "\n");
		try {
			connection.send("OTHER;" + s + " START" + result);
			connection.send("OTHER;användare" + " START" + userData);
			connection.send("CUSTOMERS;kunddata" + " START" + Register.getCustomerData());
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
		
		if(s.split(";")[0].toLowerCase().trim().equals("försäljning")) {
			JSONObject result = (JSONObject) Register.get("försäljning");
			if(s.split(";")[1].equals("säljare")) {
				try {
					messages.appendText("Skickar: " + "SALESMEN;försäljning" + " RESULTAT" + "\n");
					connection.send("SALESMEN;försäljning START" + result);
					
					messages.appendText("Klart\n");
				} catch (Exception e) {
					messages.appendText("Message failed to send.");
					e.printStackTrace();
				}
			}
			if(s.split(";")[1].equals("statistik")) {
				try {
					messages.appendText("Skickar: " + "STATS;försäljning" + " RESULTAT" + "\n");
					connection.send("STATS;försäljning START" + result);
					
					messages.appendText("Klart\n");
				} catch (Exception e) {
					messages.appendText("Message failed to send.");
					e.printStackTrace();
				}
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
