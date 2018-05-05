package application;

//Native libraries
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

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
import javafx.scene.text.*;
import javafx.scene.chart.*;
import javafx.fxml.Initializable;
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
	private Stage processingStage = new Stage();
	
	//Server
	//Client newClient = new Client("192.168.1.67", 24);
	
	
	@Override
	public void stop() throws Exception{
	    teminateThread = true;
	    CreateCharts.terminateChartThread = true;
	    connection.closeConnection();
		System.out.println("Connection closed");
	}
	
	/*
	 * FXML defenitions
	 */
	
	@FXML
	public void exitApplication(ActionEvent event) {
		teminateThread = true;
	    CreateCharts.terminateChartThread = true;   
	    System.out.println("STOP");
		Platform.exit();
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
    	return new tcpCom.Client("127.0.0.1", 55555, data ->{
    		Platform.runLater(() ->{
    			ChatController.arrived = true;
    			currentMessage = data.toString();
    			String[] formattedMessage = AdminController.currentMessage.split(" START");
    			System.out.println(formattedMessage[0]);
    			if(ChatController.type.equals("m")) {
    				listener.addMessage();
    			}
    			else if(formattedMessage[0].equals("SEND")) {
    				post(formattedMessage[1]);
    			}
    			
    			else if(formattedMessage[0].equals("DATA")) {
    				data(formattedMessage[1]);
    			}
    			
    			else if(formattedMessage[0].equals("MENU")) {
    				JSONObject sales = parseSearch(formattedMessage[1]);
    				getSalesManPane(true, sales);
    			}
    			
    			else {
    				listener.addSale();
    			}
    		});
    	});
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
    
    
    private void data(String message) {
    	JSONObject searchJson = new JSONObject();
		reqdData =  parseSearch(message);
		
    }
    
    private void post(String message) {
    	JSONObject searchJson = new JSONObject();
    	
		searchJson = parseSearch(message);reqdData = searchJson;
		
		searchResult = searchJson;
    	results = searchJson.size();
    	keys = Arrays.asList(searchResult.keySet().toArray());
    	Object[] searchKeys = searchJson.keySet().toArray();
    	CustomerPaneController.searchResult = searchJson;
    	
    	
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
    		e1.printStackTrace();
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
	    			nextPageGroup.setVisible(true);
	    			nextPageGroup.setLayoutY(y);
	    			nextPageGroup.toFront();
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
    	menuPane.getChildren().add(nextPageGroup);
    }
    
    private void sendSearch(String search) {
    	ChatController.setType("GET");
		System.out.println("STARTING SEARCH");
		try {
			System.out.println("GET: " + ChatController.type);
			send(user.get() + " - GET: " + search);
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
	    		//RegisterController registration = new RegisterController(user);
	    		//registration.showWindow();
	    		Thread registrationThread = new Thread() {
	    			public void run() {
	    				while(true) {
	    					if(RegisterController.customer != null) {
	    						send(user.get() + " - NEW: " + user + ": " + RegisterController.customer);
	    						RegisterController.customer = null;
	    					}
	    				}
	    			}
	    		};
	    	}
	    	//Searches are put in three categories: salesman, command line and customer searches 
	    	else if (search.length() > 0){
	    		ChatController.setType("GET");
	    		System.out.println("STARTING SEARCH");
	    		try {
	    			System.out.println("GET: " + ChatController.type);
					send(user.get() + " - GET: " + search);
				} catch (Exception e) {
					System.out.println("Could not send message");
					e.printStackTrace();
				}
	    	}
	    		/*
	    		JSONObject searchJson = (JSONObject) Register.get(StringUtils.capitalize(search));
	    		JSONObject salesmen = (JSONObject) Register.get("försäljning");
	    		searchResult = searchJson;
		    	Set<Object> salesmenKey = salesmen.keySet();
		    	results = searchJson.size();
		    	for(Object key : salesmenKey.toArray()) {
		    		if(key.toString().toLowerCase().equals(search)) {
		    			System.out.println(StringUtils.capitalize(search));
		    			series = new XYChart.Series<>();
		    			salesManSearch(key.toString(), (JSONObject) Register.get(key.toString()));
		    			//Search finished
		    			searchFinished();
		    			resultFound = true;
		    			return;
		    		}
		    	}
		    	keys = Arrays.asList(searchResult.keySet().toArray());
		    	Object[] searchKeys = searchJson.keySet().toArray();
		    	CustomerPaneController.searchResult = searchJson;
		    	for(Object k : searchKeys) {
		    		if(currentPage < pageSize) {
		    			String key = k.toString();
		    			//salesGridPane.setVisible(false);salesGridPane.toBack();
				    	//salesSearchPane.setVisible(true);
				    	resultFound = true;
		    			currentPage += 1;
		    			try {
				    		JSONObject customer = (JSONObject) searchJson.get(key);
				    		getResult(customer);
		    			}
					     catch(ClassCastException e) {
					    	getResult(searchJson);currentPage=0;break;
					     }
		    		}else {
		    			nextPageGroup.setVisible(true);
		    			nextPageGroup.setLayoutY(y);
		    			nextPageGroup.toFront();
		    			break;}
		    	}
		    }
		    resultsTextTitle.setText("Resultat för " + search.trim() + ": " + Integer.toString(results));
		    menuPane.getChildren().add(resultsTextTitle);
		    //customerInfoPane.getChildren().add(resultsText);resultsText.setText(Integer.toString(results));
		    searchFinished();
		    if(resultFound) {
		    	return;
		    }
		    String searchResult = GetFile.searchDrive(search, service); 
		    if(!searchResult.trim().equals("Inga resultat")) {
		    	salesSearchPane.setVisible(true);
		    	driveSearchLabel.setVisible(true);menuPane.setVisible(true);
		    	driveSearchLabel = new Label(searchResult);
		    	menuPane.setPrefSize(driveSearchLabel.getPrefHeight(), driveSearchLabel.getPrefWidth());
		    	menuPane.getChildren().add(driveSearchLabel);
		    	
		    	searchFinished();
		    	return;
		    		
		    }
		*/
	    }
    }
    
    private void salesManSearch(String name, JSONObject salesman) {
    	//Initialize sales view
    	String format = "#.#";
    	DecimalFormat newFormat = new DecimalFormat(format);
    	JSONObject metaData = (JSONObject) salesman.get("meta data");
    	List<Long> contained = new ArrayList<>();
		contained.add(0, 0L);
    	getSalesManPane(false, null);
		// Get data for sales-barchart
		SalesMan s = new helper.SalesMan(name, (JSONObject) salesman);
		
		s.getBarData();
		Map<String, Number> mapping = s.getMapping();
		
		//Set text fields
		salesmanName.set(s.getName());
		salesToday.set(Integer.toString(s.getAmountSold()));
		salesmanSalary.set(newFormat.format(s.getSalary()));
		avgPrice.set(newFormat.format((s.getSalary() / s.getAmountSold())));
		mValue.set(newFormat.format(metaData.get("mvp")));
		authority.set(metaData.get("auktoritet").toString());

		
		//Get formatted date of every day in the mapping
		series = DateHandler.getDateSeries(series, mapping, contained);
		
		// Add data to charts
		CreateCharts.getSalemanStatusData(salesman);
		//System.out.println("STARTING SALES THREAD");
		Thread salesmanThread = new Thread("listen for salesman change") {
			public void run() {
				while(true) {
					if(SalesManPaneController.update.get()) {
						send(user.get() + " - UPDATE" + ": " + authority.get() + ": " + "auktoritet" + ": " + "meta data" + ": " + name);
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
		    
		    String namn = (String) customerInfo.get("Namn");
		    String kundnummer = (String) customerInfo.get("Kundnummer");
		    String tjänst = (String) customerInfo.get("Tjänst");
		    //Register.get(kundnummer);
		    con.printResult(namn, kundnummer, tjänst, Register.currentSalesman, currentPage);
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
	    		item = SalesForce.allSales[i] + ": " + customerInfo.get("Kundpris");
	    		itemsSold.add(item);
    		}
    	}
    }
    
  
  
    /*
     * Menu functions
     */
    
    private void getSalesMenu() {
    	title = "Försäljning";
    	titleImage = wd+"business-statistics-graphic_318-56146.jpg";
    	try {
    		FXMLLoader titleLoader = new FXMLLoader(getClass().getResource("FronPage.fxml"));
    		GridPane title = titleLoader.load();
    		FXMLLoader allSalesLoader = new FXMLLoader(getClass().getResource("AllSalesChart.fxml"));
    		GridPane chart = allSalesLoader.load();
    		
    		FrontPageController.uTitle1Prop.set("Alltid");
			FrontPageController.uTitle2Prop.set("Säljare");
			FrontPageController.uTitle3Prop.set("");
			FrontPageController.uTitle4Prop.set("");

    		FXMLLoader salesStatsLoader = new FXMLLoader(getClass().getResource("SalesStats.fxml"));
    		GridPane stats = salesStatsLoader.load();
    		
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
	    		menuPane.getChildren().add(title);
	    		FrontPageController.uTitle1Prop.set("Säljare");
				FrontPageController.uTitle2Prop.set("");
				FrontPageController.uTitle3Prop.set("");
				FrontPageController.uTitle4Prop.set("");
						
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
        		AnchorPane salesmanPane = loader.load();
        		menuPane.getChildren().add(salesmanPane);
        		menuPane.setPrefHeight(menuPane.getPrefHeight() + salesmanPane.getPrefHeight());
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
	    	FXMLLoader customerTable = new FXMLLoader(getClass().getResource("AllCustomersTable.fxml"));
			GridPane table = customerTable.load();
			
			FrontPageController.uTitle1Prop.set("Orderkollen");
			FrontPageController.uTitle2Prop.set("Faktureringar");
			FrontPageController.uTitle3Prop.set("");
			FrontPageController.uTitle4Prop.set("");
			
			//FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("MapFullscreen.fxml"));
			//GridPane map = mapLoader.load();
			
			table.setLayoutY(500);
			
			menuPane.getChildren().addAll(title, table);
			menuPane.setPrefHeight(menuPane.getPrefHeight() + table.getPrefHeight());
		}catch(IOException e) {
			//Om inladdning misslyckas skriv ut felet i terminalen
			e.printStackTrace();
    	}
    }
    
    private void getHomeScreen() {
		FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("HomeScreen.fxml"));
		try {
			AnchorPane home = homeLoader.load();
    		menuPane.getChildren().add(home);
    		menuPane.setPrefHeight(menuPane.getPrefHeight());
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private void getMenuItem(String title) {
    	menuPane.setPrefHeight(570);
    	if(title.equals("salesLogo")) {
    		getSalesMenu();
    	}
    	else if(title.equals("customersLogo")) {
    		getCustomersMenu();
    	}
    	else if(title.equals("homeLogo")) {
    		getHomeScreen();
    	}
    	else if(title.equals("salesmenLogo")) {
    		send(user.get() + " - GET: försäljning: meny");
    	}
    }
    
    
    private void showProcessingWindow() {
    	processingStage = new Stage();
    	try {
	    	FXMLLoader windowLoader = new FXMLLoader(getClass().getResource("ProcessingWindow.fxml"));
	    	AnchorPane window = windowLoader.load();
    		Scene scene = new Scene(window,400,170);
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	userText.textProperty().bind(user);
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
        CreateCharts.initSeries();
        //serverText.textProperty().bind(serverCom);
        getSalesMenu();
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
        connectClient();
    	
    }  
    
}
    
   