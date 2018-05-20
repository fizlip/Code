package application;

//Native libraries
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.awt.Dimension;
import java.awt.Toolkit;

import org.json.simple.parser.ParseException;
//import org.json.JSONObject;


import DriveConfig.GetFile;
import DriveConfig.Quickstart;

import tcpCom.Client;
import javafx.application.Platform;
// Javafx
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.*;
import javafx.scene.chart.*;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.web.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Node;
import javafx.scene.Group;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.util.StringUtil;
import javafx.scene.image.ImageView;

//Internet
import Http.*;
import helper.Customer;
import helper.DateHandler;
import helper.ReadCustomerData;
import helper.Register;
import helper.SalesForce;
import helper.SalesMan;
import helper.SalesMan.*;

//JSON
import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;

import com.google.api.services.drive.Drive;
//Google maps
//import com.sun.deploy.util.SystemPropertyUtil;

//JSON
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class AdminController extends AdminObjects implements Initializable{
	public Double counter = 0.0;
	//Colors
	
	public static boolean teminateThread = false;
	private static JSONObject reqdData = new JSONObject();
	private Thread thread;
	private String currentSearch;
	public static Stage passwordStage = new Stage();
	private JSONObject homeData;
	private JSONObject statsData;
	private JSONObject customerData;
	private JSONObject salesData;
	private Long updateFreq = 60L;
	private Long lastUpdate = 0L;
	//private JSONObject customerData;
	
	//Server
	//Client newClient = new Client("192.168.1.67", 24);
	
	@FXML
    void userOptionsPressed(ActionEvent event) {

    }
	@FXML
    void changePasswordAction(ActionEvent event) {
		System.out.println("SHOW PASSWORD");
    	try {
    		FXMLLoader passwordLoader = new FXMLLoader(getClass().getResource("PasswordChange.fxml"));
    		AnchorPane passwordDialog = passwordLoader.load();
    		Scene passwordScene = new Scene(passwordDialog, 470, 280);
    		passwordScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    		passwordStage.setTitle("Ändra Lösenord");
    		passwordStage.setScene(passwordScene);
    		passwordStage.show();
    		passwordStage.setAlwaysOnTop(true);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
		
    }
	
	@FXML
    void userPressed(MouseEvent event) {
		userOptions.show();
    }
	
	
	@FXML
	private GridPane adminHeader;
	
	@Override
	public void stop() throws Exception{
	    teminateThread = true;
	    CreateCharts.terminateChartThread = true;
	    connection.closeConnection();
		System.out.println("Connection closed");
		System.exit(0);
	}
	
	/*
	 * FXML defenitions
	 */
	
	@FXML
	public void exitApplication(ActionEvent event) {
		teminateThread = true;
	    CreateCharts.terminateChartThread = true;   
	    System.out.println("STOP");
		//System.exit(0);
		}
	
	@FXML
    void backToDefault(MouseEvent event) {
		//TODO add code
    }
	
    @FXML
    void nextPageAction(MouseEvent event) {
    	try {
	    	keys = keys.subList(5, keys.size()-1);
	    	for(int i = 0; i < pageSize; i++) {
	    		JSONObject customer = (JSONObject) searchResult.get(keys.get(i));
	    		getResult(customer);
	    		nextPageGroup.setLayoutY(y);
	    		nextPageGroup.setVisible(true);
	    		
	    	}
    	}catch(IllegalArgumentException e) {
    		for(Object k : keys.toArray()) {
    			JSONObject customer = (JSONObject) searchResult.get(k);
	    		getResult(customer);
	    		//showMoreText.setText("Inga fler resultat hittades");
	    		nextPageGroup.setLayoutY(y);
	    		nextPageGroup.setVisible(true);
    		}
    		keys = new ArrayList<>();
    	}
    }
    
    private void chatAddMessage(String message) {
    	try {
    		if(message.equals("Nytt Sälj")){
    			sold += 1;
    			amountSoldText.setText(Integer.toString(sold));
        		amountSoldToday.set(Integer.toString(sold));
				System.out.println(message);
				CreateCharts.addSale();
				ChatController.type = "s";
			}
    		else {
    			ChatController.type = "m";
    		}
    		
	    	currentMessage = message;
	    	FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("ChatPane.fxml"));
			AnchorPane chat = chatLoader.load();
			String[] m = {message, ChatController.type};
			messages.add(m);
			messageAdded = true;
			/*
			chat.setLayoutX(75);
			chat.setLayoutY(90 + ChatController.offset);
			ChatController.offset += chat.getPrefHeight();
			ChatController.offset += 5;
			menuPane.getChildren().add(chat);
			*/
			
    	}catch(IOException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void sendMessage(KeyEvent event) {
    	if(event.getCode().equals(KeyCode.ENTER)) {
    		send(user.get() + " - " + serverComField.getText());
    		serverComField.clear();
    	}
    }
    
    private void send(String message) {
		ChatController.arrived = false;
		ChatController.type = "s";
		
		try {
			connection.send(message);
		} catch (Exception e) {
			serverCom.set("Anslutning misslyckades");
			e.printStackTrace();
		}
    }
    
    @FXML
    void mouseEnterTitle(MouseEvent event) {

    	ImageView sourceImage = (ImageView) event.getSource();
    	sourceImage.setOpacity(1);
    	
    }
    
    @FXML
    void mouseExitTitle(MouseEvent event) {
    	
    	ImageView sourceImage = (ImageView) event.getSource();
    	sourceImage.setOpacity(0.75);
    	
    }
    
    @FXML
    void titlePressed(MouseEvent event) {
    	ImageView sourceLogo = (ImageView) event.getSource();
    	String id = sourceLogo.getId();
    	
    	menuPane.getChildren().clear();
    	
    	getMenuItem(id);
    }
    
    @FXML
    void connectToServer(MouseEvent event) {
    
    	try {
    		isServer = true;
    		connection = createServer();
    		connection.startConnection();
    		serverCom.set("Localhost öppen");
    	}catch(Exception e) {
    		serverCom.set("Localhost kunde inte öppnas");
    		e.printStackTrace();
    	}
    }
    
    
    public void connectServer() {
    	try {
    		isServer = true;
    		connection = createServer();
    		connection.startConnection();
    		serverCom.set("Localhost öppen");
    	}catch(Exception e) {
    		serverCom.set("Localhost kunde inte öppnas");
    		e.printStackTrace();
    	}
    }
    
    private void connectClient() {
    	try {
    		isServer = false;
    		connection = createClient();
    		connection.startConnection();
    		ProcessingController.loadProp.set("Ansluter till server...");
    		serverCom.set("Klient kopplad till localhost");
    	}catch(Exception e) {
    		serverCom.set("Kunde inte koppla klient till localhost");
    		e.printStackTrace();
    	}
    }
    
    @FXML
    void connectClient(MouseEvent event) {
    
    	try {
    		isServer = false;
    		connection = createClient();
    		connection.startConnection();
    		serverCom.set("Klient kopplad till localhost");
    	}catch(Exception e) {
    		serverCom.set("Kunde inte koppla klient till localhost");
    		e.printStackTrace();
    	}
    
    }
    
    private tcpCom.Server createServer() {
    	return new tcpCom.Server(55555, data ->{
    		Platform.runLater(() -> {
    			ChatController.arrived = true;
    			currentMessage = data.toString();
    			if(ChatController.type.equals("m")) {
    				listener.addMessage();
    			}
    			else {
    				listener.addSale();
    			}
    		});
    	});
    }
    
    private tcpCom.Client createClient(){
    	return new tcpCom.Client(LoginController.serverAdress, 55555, data ->{
    		Platform.runLater(() ->{
    			ChatController.arrived = true;
    			currentMessage = data.toString();
    			String[] formattedMessage = AdminController.currentMessage.split(" START");
    			String[] messageDef = formattedMessage[0].split(";");
    			System.out.println("Typen: " + Arrays.toString(messageDef));
        		
    			//progressProp.set("Konfigurerar data");
    			//progressCircleProp.set(0.25);
    			
    			if(ChatController.type.equals("m")) {
    				listener.addMessage();
    			}
    			else if(messageDef[0].equals("INC LOGIN")) {
    				Integer c = Integer.parseInt(count.get());
    				c += 1;
    				count.set(Integer.toString(c));
    			}
    			else if(messageDef[0].equals("SEND")) {
    				post(formattedMessage[1]);
    			}
    			
    			else if(messageDef[0].equals("DATA")) {
    				
    				if(formattedMessage[1].equals("AUTHENTICATED")) {
    					passwordStage.close();
    					Alert alert = new Alert(AlertType.CONFIRMATION, "Ditt lösenord har nu uppdaterats", 
								ButtonType.OK); 
    					alert.showAndWait();
    				}
    				else if(formattedMessage[1].equals("WRONG PASSWORD")) {
    					PasswordDialogController.errorText.set("Gammalt lösenord felaktigt");
    				}
    				else {
    					data(formattedMessage[1], messageDef[1]);
    				}
    				
    			}
    			else if(messageDef[0].trim().equals("SALE")) {
    				incSale(formattedMessage[1]);
    			}
    			else if(menuSearch(messageDef[0])) {
    				JSONObject sales = parseSearch(formattedMessage[1]);
    				if(messageDef[1].toLowerCase().equals("försäljning")) {
    					if(messageDef[0].equals("SALESMEN")) {
    						getSalesManPane(true, sales);
    					}
    					if(messageDef[0].equals("STATS")) {
    						//AllSalesController.updateChart(sales);
    						getSalesMenu(sales);
    					}
    				}
    				if(messageDef[1].toLowerCase().equals("användare")) {
    					//init user
    					JSONObject userData = parseSearch(formattedMessage[1]); 
    					JSONObject mData = (JSONObject) userData.get("meta data");
    					welcomeUser.set( "Välkommen " + (String) mData.get("användarnamn") );
    				}
    				if(messageDef[1].toLowerCase().equals("kunddata")) {
    					JSONObject customerData = parseSearch(formattedMessage[1]);
    					updateCustomerTableData(customerData);
    				    					
    					//CreateCharts.initSeries(customerData);
    				}
    				else if(messageDef[1].toLowerCase().equals("order")) {
    					data(formattedMessage[1], messageDef[1]);
    				}
    			}
    			
    			else {
    				listener.addSale();
    			}
    			//progressProp.set("Klar");
    			//progressCircleProp.set(1);
    			//progress.setVisible(false);
    			//progressText.setVisible(false);
    		});
    	});
    }
    
    private boolean menuSearch(String item) {
    	String[] menuItems = {"HOME", "STATS", "CUSTOMERS", "SALESMEN", "OTHER"};
    	if(Arrays.asList(menuItems).contains(item)) {
    		return true;
    	}
    	return false;
    }
    
    private void handleMenuInput(String input) {
    	
    }
    
    private void updateCustomerTableData(JSONObject customerData) {
    	CustomersTableController.customerData = (JSONObject) customerData.get("kund");
    	CustomersTableController.updateData(customerData);
    }
    
    private void incSale(String customer) {
    	JSONObject cData = parseSearch(customer);
    	//billing.addOrder(data);
    	listener.addOrder(cData);
    	CreateCharts.addSale();
    	listener.addSale();
    	sold += 1;
    	String[] m = {"Nytt sälj", "s"};
    	messages.add(m);
    	amountSoldText.setText(Integer.toString(sold));
    }
    
    @FXML
    void addSale(ActionEvent event) {

    	CreateCharts.addSale();
    	listener.addSale();
    	currentMessage = "Nytt sälj";
    	sold += 1;
    	String[] m = {"Nytt sälj", "s"};
    	messages.add(m);
    	amountSoldText.setText(Integer.toString(sold));
    	try {
    		ChatController.type = "s";
    		send(user.get() + " - " + currentMessage);
    	}
    	catch(IllegalArgumentException e) {
    		System.out.println("Double");
    	}
    	
    }
    
    
    private void data(String message, String type) {
    	JSONObject searchJson = new JSONObject();
    	if(type.toLowerCase().equals("order")) {
    		System.out.println("TYP " + type);
    		System.out.println(searchJson.toJSONString());
    		orderData = parseSearch(message);
    		BillingController.allOrders = parseSearch(message);
    	}
    	if(type.toLowerCase().equals("update")) {
    		System.out.println("UPDATE");
    		orderData = parseSearch(message);
    		listener.updateStatus();
    	}
    	else {
    		reqdData =  parseSearch(message);
    	}		
    }
    
    private void post(String message) {
    	JSONObject searchJson = new JSONObject();
    	
		searchJson = parseSearch(message);reqdData = searchJson;
		
		searchResult = searchJson;
    	results = searchJson.size();
    	keys = Arrays.asList(searchResult.keySet().toArray());
    	Object[] searchKeys = searchJson.keySet().toArray();
    	CustomerPaneController.searchResult = searchJson;
    	currentPage = 0;
    	
    	
    	//Salesman search
    	try{
    		searchJson.get("meta data").equals(null);
    		searchResult = searchJson;
			System.out.println(StringUtils.capitalize(currentSearch));
			series = new XYChart.Series<>();
			salesManSearch(currentSearch, searchJson);
	    	results = searchJson.size();
	    	searchFinished();
			resultFound = true;
    	}
    	// misc search
    	catch(NullPointerException e1) {
    		for(Object k : searchKeys) {
	    		if(currentPage < pageSize) {
	    			String key = k.toString();
			    	resultFound = true;
	    			currentPage += 1;
	    			try {
			    		JSONObject customer = (JSONObject) searchJson.get(key);
			    		getResult(customer);
	    			}
				     catch(ClassCastException e2) {
				    	getResult(searchJson);currentPage=0;break;
				     }
	    		}else {
	    			//nextPageGroup.setVisible(true);
	    			//nextPageGroup.setLayoutY(y);
	    			//nextPageGroup.toFront();
	    			break;}
	    	}
    	}
    	//Misc search
    }
    
    /*
     * Search functions
     */
    private int i = 0;
    
    @FXML
    void searchKeyPressed(KeyEvent event) {
    	if(event.getCode().equals(KeyCode.ENTER)) {
	    	//searchProgress.setVisible(true);
	    	i+=1;
	    	//driveSearchLabel.setText(Integer.toString(i));
	    	results = 0;
	    			
	    	//User presses search button
	    	searchField.getScene().setCursor(Cursor.WAIT);
	    	
	    	//Update series every time a search is made
	    	//series = new XYChart.Series<>();
			priceSeries.getData().clear();
			
			//Remove any excessive components from panels
			//salesSearchPane.setVisible(false);
			menuPane.setVisible(true);
    	}
    }
    
    private void initSearch() {
    	isggData = ReadCustomerData.readData(path);
    	y = 50; //y pos of initial customer panel
    	height = menuPane.getPrefHeight();
    	menuPane.getChildren().clear();
    	//menuPane.getChildren().add(nextPageGroup);
    }
    
    private void sendSearch(String search) {
    	ChatController.setType("GET");
		System.out.println("STARTING SEARCH");
		try {
			System.out.println("GET: " + ChatController.type);
			send(user.get() + " - GET: " + search + ": sök");
		} catch (Exception e) {
			System.out.println("Could not send message");
			e.printStackTrace();
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
    
    @FXML
    void searchKeyReleased(KeyEvent event) {
    	//User Releases search button
    	if(event.getCode().equals(KeyCode.ENTER)) {
    		String search = searchField.getText().trim().toLowerCase();
    		searchField.clear();
    		currentSearch = search;
    		initSearch();
	    	if(search.length() == 0) {
				return;
	    	}
	    	
	    	else if(search.toLowerCase().split(" ")[0].trim().equals("ändra")) {
	    		String id = search.split(" ")[1].trim();
	    		String field = search.split(" ")[2].trim();
	    		String newElement = search.split(" ")[3].trim();
	    		//Register.add(StringUtils.capitalize(newElement), StringUtils.capitalize(field), id);
	    	}
	    	
	    	else if(search.toLowerCase().equals("ny kund")) {
	    		Stage stage = new Stage();
	         	try {
	     			AnchorPane root = FXMLLoader.load(getClass().getResource("CustomerRegistration.fxml"));
	     			Scene scene = new Scene(root,900,650);
	     			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	     			stage.setAlwaysOnTop(true);
	     			stage.setScene(scene);
	     			stage.setTitle("Kund Registrering");
	     			stage.show();
	     		} catch(Exception e) {
	     			e.printStackTrace();
	     		}
	         	
	        	Thread registrationThread = new Thread() {
	    			public void run() {
	    				while(!stopRegistration) {
	    					if(RegisterController.newCustomer.get()) {
	    						send(user.get() + " - " + "NEW: " + ": " + RegisterController.getCustomer());
	    						RegisterController.newCustomer.set(false);
	    						break;
	    					}
	    				}
	    			}
	    		};
	    		registrationThread.start();
	    	}
	    	//Searches are put in three categories: salesman, command line and customer searches 
	    	else if (search.length() > 0){
	    		ChatController.setType("GET");
	    		System.out.println("STARTING SEARCH");
	    		try {
	    			System.out.println("GET: " + ChatController.type);
					send(user.get() + " - GET: " + search + ": sök");
				} catch (Exception e) {
					System.out.println("Could not send message");
					e.printStackTrace();
				}
	    	}
	    }
    }
    
    private void salesManSearch(String name, JSONObject salesman) {
    	//Initialize sales view
    	stopSalesmanThread = true;
    	String format = "#.#";
    	DecimalFormat newFormat = new DecimalFormat(format);
    	JSONObject metaData = (JSONObject) salesman.get("meta data");
    	List<Long> contained = new ArrayList<>();
		contained.add(0, 0L);
		// Get data for sales-barchart
		SalesMan s = new helper.SalesMan(name, (JSONObject) salesman);
		
		s.getBarData();
		Map<String, Number> mapping = s.getMapping();
		
		//Set text fields
		//progressProp.set("Initierar skärm");
		//progressCircleProp.set(0.5);
		salesmanName.set(s.getName());
		salesToday.set(Integer.toString(s.getAmountSold()));
		salesmanSalary.set(newFormat.format(s.getSalary()));
		avgPrice.set(newFormat.format((s.getSalary() / s.getAmountSold())));
		try {
			mValue.set(newFormat.format(metaData.get("mvp")));
		}catch(Exception e) {
			e.printStackTrace();
			mValue.set("Kan ej hittas");
		}
		authority.set(metaData.get("auktoritet").toString());

		
		//Get formatted date of every day in the mapping
		series = DateHandler.getDateSeries(series, mapping, contained);
		getSalesManPane(false, salesman);
		// Add data to charts
		CreateCharts.getSalemanStatusData(salesman);
		//System.out.println("STARTING SALES THREAD");
		stopSalesmanThread = false;
		Thread salesmanThread = new Thread("listen for salesman change") {
			public void run() {
				while(!stopSalesmanThread) {
					if(SalesManPaneController.update.get()) {
						send(user.get() + " - UPDATE" + ": " + authority.get() + ": " + "auktoritet" + ": " + "meta data" + ": " + salesmanName.get());
						SalesManPaneController.update.set(false);
					}
				}
			}
		};
		salesmanThread.start();
		//System.out.println("ENDING SALES THREAD");
		
		searchFinished();
    }
    
    private void otherSearch(String search, JSONObject customer, String id) {
    	//Hantera sökning annan än personnummer
    	Object[] fields = (Object[]) customer.keySet().toArray();
    	for(Object f : fields) {
    		String field = f.toString();    		
    		if(customer.get(field.trim()).equals(search.trim())) {
    			resultFound = true;
	    		salesSearchPane.setVisible(true);
	    		results += 1;
	    		// Rita ut datan på skärmen om matching har hittats, uppdatera även skärmen
	    		getResult(customer);
	    		//getCustomerPane(customer, id);
	    		y += heightAdd + 5;
	        	height += heightAdd + 9;
	    	   	menuPane.setPrefHeight(height);
    		}
    	}
    	
    }
    
    public void getResult(JSONObject customerInfo) {
    	try {
	   		// Ladda in fxml filen för customerPane och lagra dess pane i root.
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerPane.fxml"));
		   	AnchorPane root = loader.load();
		   	
		   	// Lägg till root till customerSearchPane
		   	menuPane.getChildren().add(root);
		   	
		    CustomerPaneController con = loader.getController();
		    //Register.get(kundnummer);
		    con.customer = customerInfo;
		    con.printResult(customerInfo, currentPage);
		    //Rita customerPane på korrekt plats på skärmen
		    root.setLayoutY(y); root.toBack();
		    y += heightAdd + 5;
        	height += heightAdd + 9;
    	   	menuPane.setPrefHeight(height);
	    }catch(IOException e) {
			//Om inladdning misslyckas skriv ut felet i terminalen
			e.printStackTrace();
	   }
    }
    
    private void searchFinished() {
    	searchField.setText("");
    	searchField.setPromptText("Tryck på Enter för att söka");
		searchField.getScene().setCursor(Cursor.DEFAULT);
		currentPage = 0;
    }
    
    /*
     * End of search functions
     */
    
    @FXML
    void salesNameMouseEnter(MouseEvent event) {
    	salesManNameText.setUnderline(true);
    	salesManNameText.setFill(Color.valueOf("#480000"));
    }
    @FXML
    void salesNameMouseExit(MouseEvent event) {
    	salesManNameText.setUnderline(false);
    	salesManNameText.setFill(Color.BLACK);
    }
    
    @FXML
    void showMapFullscreen(ActionEvent event) {
    	//Show map in new window
    	Stage mapStage = new Stage();
    	try {
    		GridPane root = FXMLLoader.load(getClass().getResource("MapFullscreen.fxml"));
    		Scene scene = new Scene(root, 600, 600);
    		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    		mapStage.setTitle("Kund karta");
    		mapStage.setScene(scene);
    		mapStage.show();
    	}catch(Exception e) {e.printStackTrace();}
    }
    
    @FXML
    void showSummaryAction(ActionEvent event) {
    	//Show summary page when button pressed
    	Stage summaryStage = new Stage();
    	try {
			//AnchorPane root = FXMLLoader.load(getClass().getResource("UsecurityDasboard.fxml"));
			GridPane root = FXMLLoader.load(getClass().getResource("SummaryScreen.fxml"));
			Scene scene = new Scene(root,1290,650);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			summaryStage.setTitle("Sammanfattning");
			summaryStage.setScene(scene);
			summaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    
    
    @FXML
    private void createSalesList() {
    	for(int i = 0; i < data.size(); i++) {
    		String item = new String();
    		for(Map.Entry<String, Customer> customer: data.get(i).getSalesMapping().entrySet()) {
	    		Customer customerInfo = customer.getValue();
	    		item = SalesForce.allSales.get(i) + ": " + customerInfo.get("Kundpris");
	    		itemsSold.add(item);
    		}
    	}
    }
    
  
  
    /*
     * Menu functions
     */
    
    private void getSalesMenu(JSONObject data) {
    	ProcessingController.loadProp.set("Initierar hemskärm...");
    	title = "Försäljning";
    	titleImage = wd+"business-statistics-graphic_318-56146.jpg";
    	try {
    		FXMLLoader titleLoader = new FXMLLoader(getClass().getResource("FronPage.fxml"));
    		GridPane title = titleLoader.load();
    		title.setPrefWidth(menuPane.getPrefWidth());
    		
    		if((DateHandler.getCurrentTimeSec() - lastUpdate) > updateFreq) {
    			AllSalesController.updateChart(data);
    			lastUpdate = DateHandler.getCurrentTimeSec();
    		}
    		
    		FXMLLoader allSalesLoader = new FXMLLoader(getClass().getResource("AllSalesChart.fxml"));
    		GridPane chart = allSalesLoader.load();
    		chart.setPrefWidth(menuPane.getPrefWidth());
    		chart.setPrefHeight(560 * heightFactor);
    		
    		FrontPageController.uTitle1Prop.set("Alltid");
			FrontPageController.uTitle2Prop.set("Säljare");
			FrontPageController.uTitle3Prop.set("");
			FrontPageController.uTitle4Prop.set("");

    		FXMLLoader salesStatsLoader = new FXMLLoader(getClass().getResource("SalesStats.fxml"));
    		GridPane stats = salesStatsLoader.load();
    		stats.setPrefWidth(menuPane.getPrefWidth());
    		stats.setPrefHeight(560 * heightFactor);
    		
    		chart.setLayoutY(500);
    		stats.setLayoutY(chart.getPrefHeight() + 600);
    		menuPane.getChildren().addAll(title, chart, stats);
    		menuPane.setPrefHeight(menuPane.getPrefHeight() + stats.getPrefHeight() + chart.getPrefHeight());
    		
    	}catch(IOException e) {
			//Om inladdning misslyckas skriv ut felet i terminalen
			e.printStackTrace();
    	}
    }
    
    private void createSalesmanElement(AnchorPane menu, int i, Object k, JSONObject sales) throws IOException, java.text.ParseException {
    	FXMLLoader daysLoader = new FXMLLoader(getClass().getResource("Days.fxml"));
		AnchorPane days = daysLoader.load();
		//Namn
		Text s1 = new Text(k.toString());
		s1.setY(400 + i*225);s1.setX(50);
		s1.setFont(Font.font("Times New Roman", 18));
		JSONObject data = (JSONObject) sales.get(k);
		JSONObject mData = (JSONObject) data.get("meta data");
		//Antal sälj i år
		Long totSales = (Long) mData.get("tot sälj");
		Text s2 = new Text("Sälj: " + totSales);
		s2.setY(400 + i*225);s2.setX(200);
		s2.setFont(Font.font("Times New Roman", 18));
		//m-värde
		System.out.println(mData.get("mvp"));
		Double mvp = (Double) mData.get("mvp");
		Text s3 = new Text("m: " + mvp);
		s3.setY(400 + i*225);s3.setX(350);
		s3.setFont(Font.font("Times New Roman", 18));
		
		menuPane.getChildren().addAll(s1, s2, s3);
		
		createGrid(k, sales, days, i);
    }
    
    private void createGrid(Object k, JSONObject sales, AnchorPane days, int i) throws java.text.ParseException {
    	List<Long> contained = new ArrayList<>();
		contained.add(0, 0L);
		SalesMan salesman = new helper.SalesMan(k.toString(), (JSONObject) sales.get(k));
		
		AdminController.series = new XYChart.Series<>();
		
		Map<String, Number> mapping = salesman.getMapping();
		System.out.println(k);
		AdminController.series = DateHandler.getDateSeries(AdminController.series, mapping, contained);
		
		helper.Days gridData = new helper.Days();
		
		ObservableList<XYChart.Data<String, Number>> data = AdminController.series.getData();
		int[][] points = gridData.getPoints(data);
		List<Rectangle> rects = gridData.createDayGrid(days);
		List<Rectangle> rs = gridData.paint(rects, points);
		for(Rectangle rect : rs) {
			days.getChildren().add(rect);
		}
		days.setLayoutY(425 + i*225);
		menuPane.getChildren().add(days);
    }
    
    private void getSalesManPane(boolean header, JSONObject sales) {
    	title = "Säljare";
    	titleImage = wd+"customerlogo.png";
    	try {
    		if(header) {
	    		FXMLLoader titleLoader = new FXMLLoader(getClass().getResource("FronPage.fxml"));
	    		GridPane title = titleLoader.load();
	    		title.setPrefWidth(menuPane.getPrefWidth());
	    		menuPane.getChildren().add(title);
	    		FrontPageController.uTitle1Prop.set("Säljare");
				FrontPageController.uTitle2Prop.set("");
				FrontPageController.uTitle3Prop.set("");
				FrontPageController.uTitle4Prop.set("");
					
				if((DateHandler.getCurrentTimeSec() - lastUpdate) > updateFreq) {
	    			CreateCharts.resetCharts();
	    			CreateCharts.initSeries(sales);
	    			lastUpdate = DateHandler.getCurrentTimeSec();
	    		}
				
				Text tot = new Text();
				tot.setText("Antal äntällda: " + Integer.toString(sales.size()));
				tot.setY(300);
				tot.setX(50);
				menuPane.getChildren().add(tot);
				menuPane.setPrefHeight(menuPane.getPrefHeight());
				int i = 0;
				for(Object k : sales.keySet()) {
					createSalesmanElement(menuPane, i, k, sales);
					i+=1;
					
				}
				menuPane.setPrefHeight(menuPane.getPrefHeight() + 450 + i*200);
    		}
    		else {
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("SalesManPane.fxml"));
        		GridPane salesmanPane = loader.load();
        		salesmanPane.setPrefWidth(menuPane.getPrefWidth());
        		salesmanPane.setPrefHeight(600 * heightFactor);

        		SalesDataController.data = (JSONObject) sales.get("meta data");
        		SalesDataController.customers = (JSONObject) sales.get("kunder");
        		FXMLLoader dataLoader = new FXMLLoader(getClass().getResource("SalesmanData.fxml"));
        		GridPane dataPane = dataLoader.load();
        		dataPane.setPrefWidth(menuPane.getPrefWidth());
        		dataPane.setPrefHeight(190 * heightFactor);

        		FXMLLoader personFileLoader = new FXMLLoader(getClass().getResource("persFile.fxml"));
        		GridPane personFile = personFileLoader.load();
        		personFile.setPrefWidth(menuPane.getPrefWidth());
        		personFile.setPrefHeight(650 * heightFactor);
        		
        		SalesManPaneController controller = loader.getController();
        		controller.salesman = SalesDataController.data;
        		controller.write();
        		menuPane.getChildren().add(dataPane);
        		menuPane.getChildren().add(salesmanPane);
        		dataPane.setLayoutY(salesmanPane.getPrefHeight() + 50);
        		personFile.setLayoutY(salesmanPane.getPrefHeight() + dataPane.getPrefHeight() + 100);
        		menuPane.getChildren().add(personFile);
        		personFile.toFront();
        		menuPane.setPrefHeight(menuPane.getPrefHeight() + salesmanPane.getPrefHeight() + dataPane.getHeight() + + personFile.getHeight() + 100);
    		}
    	}
    	catch(IOException | java.text.ParseException e) {
    		e.printStackTrace();
    	}
    }
    
    private void getCustomersMenu() {
    	title = "Kunder";
    	titleImage = wd+"custServ.png";
    	try {
	    	FXMLLoader titleLoader = new FXMLLoader(getClass().getResource("FronPage.fxml"));
    		GridPane title = titleLoader.load();
    		title.setPrefWidth(menuPane.getPrefWidth());

    		
    		/*
    		send(user.get() + " - GET: order: init");
    		try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/
	    	FXMLLoader customerTable = new FXMLLoader(getClass().getResource("AllCustomersTable.fxml"));
			GridPane table = customerTable.load();
    		table.setPrefWidth(menuPane.getPrefWidth());
    		table.setPrefHeight(560 * heightFactor);

			FXMLLoader billingLoader = new FXMLLoader(getClass().getResource("Billing.fxml"));
			BillingController.allOrders = orderData;
			ScrollPane billingTable = billingLoader.load();
			
			FrontPageController.uTitle1Prop.set("Orderkollen");
			FrontPageController.uTitle2Prop.set("Faktureringar");
			FrontPageController.uTitle3Prop.set("");
			FrontPageController.uTitle4Prop.set("");
			
			//FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("MapFullscreen.fxml"));
			//GridPane map = mapLoader.load();
			billingTable.setLayoutY(500);
			table.setLayoutY(billingTable.getLayoutY() + 500);
			
			menuPane.getChildren().addAll(title, table, billingTable);
			menuPane.setPrefHeight(menuPane.getPrefHeight() + table.getPrefHeight() + billingTable.getPrefHeight());
		}catch(IOException e) {
			//Om inladdning misslyckas skriv ut felet i terminalen
			e.printStackTrace();
    	}
    }
    
    private void getHomeScreen() {
		FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("HomeScreen.fxml"));
		try {
			GridPane home = homeLoader.load();
    		home.setPrefWidth(menuPane.getPrefWidth());
    		home.setPrefHeight(550 * heightFactor);
    		
    		menuPane.getChildren().add(home);
    		menuPane.setPrefHeight(menuPane.getPrefHeight());
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private void getQuestionScreen() {
    	FXMLLoader helpLoader = new FXMLLoader(getClass().getResource("HelpScreen.fxml"));
		try {
			GridPane help = helpLoader.load();

			help.setPrefWidth(700);
			help.setPrefHeight(2000);
    		menuPane.getChildren().add(help);
    		help.setLayoutX(menuPane.getWidth()/4);
    		menuPane.setPrefHeight(help.getPrefHeight());
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private void getMenuItem(String title) {
    	menuPane.setPrefHeight(570);
    	if(title.equals("salesLogo")) {
    		//progress.setVisible(true);
    		//progressText.setVisible(true);
    		//progressProp.set("Kontaktar server");
    		send(user.get() + " - GET: försäljning;statistik: meny");
    	}
    	else if(title.equals("customersLogo")) {
    		getCustomersMenu();
    	}
    	else if(title.equals("homeLogo")) {
    		getHomeScreen();
    	}
    	else if(title.equals("salesmenLogo")) {
    		//progress.setVisible(true);
    		//progressText.setVisible(true);
    		//progressProp.set("Kontaktar server");
    		send(user.get() + " - GET: försäljning;säljare: meny");
    	}
    	else if(title.equals("questionLogo")) {
    		getQuestionScreen();
    	}
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	adminHeader.setPrefHeight(screenSize.getHeight());
    	adminHeader.setPrefWidth(screenSize.getWidth());
    	menuPane.prefWidthProperty().bind(adminHeader.widthProperty().multiply(0.935));
    	SalesManPaneController.userRequest = false;
    	//menuPane.prefHeightProperty().bind(adminHeader.heightProperty().multiply(0.935));
    	
    	heightFactor = ((Double) screenSize.getHeight()) / 650;
    	
    	userText.textProperty().bind(user);
    	welcomeText.textProperty().bind(welcomeUser);
    	connectClient();
    	//SalesForce.createSalesForce();
    	//googleMapView.addMapInializedListener(this);
        assert summaryButton != null : "fx:id=\"summaryButton\" was not injected: check your FXML file 'AdminDashboard.fxml'.";
        //salesManLineChart.getData();
        /*
         * DETTA SKA LÄGGAS TILL I KALENDERN
         * eventList.setItems(items); 
         */ 
        //createSalesList();
        //createAllcustomersTable();
        
        //driveSearchLabel.setText("Null");
        
        //WebEngine engine = webBrowser.getEngine();
        //engine.load("https://www.merinfo.se/");
        /*
        try {
        	service = Quickstart.getDriveService();
        }catch(IOException e) {System.out.println("Google Drive connection failure");e.printStackTrace();}
        */
        try {
	        FileInputStream input = new FileInputStream("C:\\Users\\f_ill\\eclipse-workspace\\UsecDashboard\\build\\build\\classes\\application\\ISGG-logga.png");
	        Image logo = new Image(input);
	        //logoPic.setImage(logo);
        }catch(FileNotFoundException e) {e.printStackTrace();}
        //serverText.textProperty().bind(serverCom);
        listener = new Initiater();
        thread = new Thread("PerSec") {
        	public void run() {
        		while(!teminateThread) {
        			counter += 0.1;
        			perSec.setText(Double.toString(counter));
	        		try {
						sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
        		}
        	}
        };
        thread.start();
        CreateCharts.startThread();
        CustomerPaneController.connection = connection;
        loggedIn.textProperty().bind(count);
        send(user.get() + " - INC: LOGIN");
        send(user.get() + " - GET: order: init");
        //progressText.textProperty().bind(progressProp);
        //progress.progressProperty().bind(progressCircleProp);
        //getSalesMenu();
    }  
    
}
    
   