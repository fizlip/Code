package app;

//Native libraries
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

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
import javafx.stage.Stage;
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
import com.sun.javafx.charts.Legend;
import com.sun.javafx.charts.Legend.LegendItem;
import javafx.scene.Group;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.util.StringUtil;

//Internet
import Http.*;
import helper.Customer;
import helper.DateHandler;
import helper.ReadCustomerData;
import helper.Register;
import helper.RegisterController;
import helper.SalesForce;
import helper.SalesMan;
import helper.SalesMan.*;

//JSON
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.api.services.drive.Drive;
//Google maps
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
//import com.lynden.gmapsfx.shapes.Rectangle;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.InfoWindow;
import com.lynden.gmapsfx.javascript.object.InfoWindowOptions;

//JSON
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class AdminController extends AdminObjects implements MapComponentInitializedListener, Initializable{
	
	//Colors
	public String nightWhite = "cdcdcd";
	
	private GoogleMap map;
	
	private HomeScreenController homeScreen;
	
	//Server
	//Client newClient = new Client("192.168.1.67", 24);
	
	/*
	 * FXML defenitions
	 */
    
    @FXML
    void backToDefault(MouseEvent event) {
    	//If logo pressed go back to home screen
    	adminView.setVisible(true);adminView.toFront();
    	salesSearchPane.setVisible(false);salesSearchPane.toBack();
		salesManPane.setVisible(false);

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
	    		showMoreText.setText("Inga fler resultat hittades");
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
			
			chat.setLayoutX(288);
			chat.setLayoutY(90 + ChatController.offset);
			ChatController.offset += chat.getPrefHeight();
			ChatController.offset += 5;
			menuPane.getChildren().add(chat);
			
    	}catch(IOException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void sendMessage(KeyEvent event) {
    	if(event.getCode().equals(KeyCode.ENTER)) {
    		send(serverComField.getText());
    		serverComField.clear();
    	}
    }
    
    private void send(String message) {
		ChatController.arrived = false;
		chatAddMessage(message);
		
		try {
			connection.send(message);
		} catch (Exception e) {
			serverCom.set("Anslutning misslyckades");
			e.printStackTrace();
		}
    }
    
    @FXML
    void mouseEnterShowMore(MouseEvent event) {
    	showMoreRectangle.setStroke(Color.BLACK);
    	showMoreText.setFill(Color.BLACK);
    }

    @FXML
    void mouseExitShowMore(MouseEvent event) {
    	showMoreRectangle.setStroke(Color.valueOf("#757575"));
    	showMoreText.setFill(Color.valueOf("#757575"));
    }
    
    @FXML
    void mouseEnterTitle(MouseEvent event) {

    	Text sourceText = (Text) event.getSource();
    	sourceText.setFill(Color.BLACK);
    	sourceText.setUnderline(true);
    	
    }
    
    @FXML
    void mouseExitTitle(MouseEvent event) {
    	
    	Text sourceText = (Text) event.getSource();
    	sourceText.setFill(Color.valueOf("#5b5b5b"));
    	sourceText.setUnderline(false);
    	
    }
    
    @FXML
    void titlePressed(MouseEvent event) {
    	Text sourceText = (Text) event.getSource();
    	System.out.println(sourceText.getText());
    	
    	menuPane.getChildren().clear();
    	
    	getMenuItem(sourceText.getText());
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
    			chatAddMessage(data.toString());
    			//serverCom.set(data.toString());
    		});
    	});
    }
    
    private tcpCom.Client createClient(){
    	return new tcpCom.Client("127.0.0.1", 55555, data ->{
    		Platform.runLater(() ->{
    			ChatController.arrived = true;
    			chatAddMessage(data.toString());
    			//serverCom.set(data.toString());
    		});
    	});
    }
    
    @FXML
    void addSale(ActionEvent event) {

    	CreateCharts.addSale();
    	try {
    		send("Nytt Sälj");
    	}
    	catch(IllegalArgumentException e) {
    		System.out.println("Double");
    	}
    	
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
	    	driveSearchLabel.setText(Integer.toString(i));
	    	results = 0;
	    			
	    	//User presses search button
	    	searchField.getScene().setCursor(Cursor.WAIT);
	    	
	    	//Update series every time a search is made
	    	//series = new XYChart.Series<>();
			priceSeries.getData().clear();
			
			//Remove any excessive components from panels
			adminView.setVisible(false);adminView.toBack();
			salesSearchPane.setVisible(false);
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
    
    @FXML
    void searchKeyReleased(KeyEvent event) {
    	//User Releases search button
    	if(event.getCode().equals(KeyCode.ENTER)) {
    		String search = searchField.getText().trim().toLowerCase();
    		initSearch();
	    	if(search.length() == 0) {
				return;
	    	}
	    	
	    	else if(search.toLowerCase().split(" ")[0].trim().equals("ändra")) {
	    		String id = search.split(" ")[1].trim();
	    		String field = search.split(" ")[2].trim();
	    		String newElement = search.split(" ")[3].trim();
	    		Register.add(StringUtils.capitalize(newElement), StringUtils.capitalize(field), id);
	    	}
	    	
	    	else if(search.toLowerCase().equals("ny kund")) {
	    		RegisterController registration = new RegisterController();
	    		registration.showWindow();
	    	}
	    	//Searches are put in three categories: salesman, command line and customer searches 
	    	else if (search.length() > 0){
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
		    	salesSearchPane.setVisible(true);salesGridPane.setVisible(false);
		    	driveSearchLabel.setVisible(true);menuPane.setVisible(true);
		    	driveSearchLabel = new Label(searchResult);
		    	menuPane.setPrefSize(driveSearchLabel.getPrefHeight(), driveSearchLabel.getPrefWidth());
		    	menuPane.getChildren().add(driveSearchLabel);
		    	
		    	searchFinished();
		    	return;
		    		
		    }
	    }
    }
    
    private void salesManSearch(String name, JSONObject salesman) {
    	//Initialize sales view
    	List<Long> contained = new ArrayList<>();
		contained.add(0, 0L);
    	getSalesManPane();
		// Get data for sales-barchart
		SalesMan s = new helper.SalesMan(name, (JSONObject) salesman);
		
		s.getBarData();
		Map<String, Number> mapping = s.getMapping();
		
		//Set text fields
		salesmanName.set(s.getName());
		salesToday.set(Integer.toString(s.getAmountSold()));
		salesmanSalary.set(Double.toString(s.getSalary()));
		avgPrice.set(Double.toString((s.getSalary() / s.getAmountSold())));
		
		//Get formatted date of every day in the mapping
		series = DateHandler.getDateSeries(series, mapping, contained);
		
		// Add data to charts
		CreateCharts.getSalemanStatusData(name);

		
		
		searchFinished();
    }
    
    private void otherSearch(String search, JSONObject customer, String id) {
    	//Hantera sökning annan än personnummer
    	Object[] fields = (Object[]) customer.keySet().toArray();
    	for(Object f : fields) {
    		String field = f.toString();    		
    		if(customer.get(field.trim()).equals(search.trim())) {
    			resultFound = true;
    			salesGridPane.setVisible(false);salesGridPane.toBack();
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
		    Register.get(kundnummer);
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
    	salesList.setItems(itemsSold);
    }
    
    @Override
    public void mapInitialized() {
    	MapOptions mapOptions = new MapOptions();
        
        mapOptions.center(new LatLong(62.5359, 16.3751))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .streetViewControl(false)
                .scaleControl(false)
                .zoom(4);

        map = googleMapView.createMap(mapOptions);
        JSONArray latLongData = GetLatLong.getLatLongData();
        for(Map.Entry<Double[], String[]> indvPos : postionMap.entrySet()) {
        	Double lat = indvPos.getKey()[0];
        	Double lng = indvPos.getKey()[1];
        	LatLong pos = new LatLong(lat, lng);
        	String name = indvPos.getValue()[0];
        	String status = indvPos.getValue()[1];
        	addMarker(pos, name, status);
        }
        
    }
    
    private void addMarker(LatLong pos, String name, String status) {
    	MarkerOptions options = new MarkerOptions();
    	options.position(pos);
    	if(status.equals("Order")) {
	    	//Order
	    	options.icon("https://upload.wikimedia.org/wikipedia/commons/thumb/3/35/Location_dot_blue.svg/4px-Location_dot_blue.svg.png");
    	}
    	else if(status.equals("Makulerad")) {
		    //Makulerad
	    	options.icon("https://upload.wikimedia.org/wikipedia/commons/thumb/9/92/Location_dot_red.svg/4px-Location_dot_red.svg.png");
    	}
    	else if(status.equals("Kund")) {
		    //Kund
	    	options.icon("https://upload.wikimedia.org/wikipedia/commons/thumb/6/60/Lightgreen_pog.svg/4px-Lightgreen_pog.svg.png");
    	}
    	else if(status.equals("Fakturerad")) {
		    //Fakturerad
	    	options.icon("https://upload.wikimedia.org/wikipedia/commons/thumb/0/01/Yellow_ffff00_pog.svg/4px-Yellow_ffff00_pog.svg.png");
    	}
    	else {
		    //Inget
	    	options.icon("https://upload.wikimedia.org/wikipedia/commons/thumb/c/c0/Location_dot_black.svg/4px-Location_dot_black.svg.png");
    	}
	    Marker marker = new Marker(options);
        marker.setPosition(pos);
        marker.setTitle(name);
        map.addMarker(marker);
    }
    
    /*
     * Menu functions
     */
    
    private void getSalesMenu() {
    	try {
    		FXMLLoader allSalesLoader = new FXMLLoader(getClass().getResource("AllSalesChart.fxml"));
    		GridPane chart = allSalesLoader.load();

    		FXMLLoader salesStatsLoader = new FXMLLoader(getClass().getResource("SalesStats.fxml"));
    		GridPane stats = salesStatsLoader.load();
    		
    		chart.setLayoutY(550);
    		stats.setLayoutY(chart.getPrefHeight() + 600);
    		
    		menuPane.getChildren().addAll(chart, stats);
    		menuPane.setPrefHeight(menuPane.getPrefHeight() + stats.getPrefHeight() + chart.getPrefHeight());
    		
    	}catch(IOException e) {
			//Om inladdning misslyckas skriv ut felet i terminalen
			e.printStackTrace();
    	}
    }
    
    private void getSalesManPane() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("SalesManPane.fxml"));
    		AnchorPane salesmanPane = loader.load();
    		
    		menuPane.getChildren().add(salesmanPane);
    		menuPane.setPrefHeight(menuPane.getPrefHeight() + salesmanPane.getPrefHeight());
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    private void getCustomersMenu() {
		try {
	    	FXMLLoader customerTable = new FXMLLoader(getClass().getResource("AllCustomersTable.fxml"));
			GridPane table = customerTable.load();
			
			FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("MapFullscreen.fxml"));
			GridPane map = mapLoader.load();
			
			table.setLayoutY(550);
			map.setLayoutY(table.getPrefHeight() + 600);
			
			menuPane.getChildren().addAll(table, map);
			menuPane.setPrefHeight(menuPane.getPrefHeight() + table.getPrefHeight() + map.getPrefHeight());
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
    	if(title.equals("Försäljning")) {
    		getSalesMenu();
    	}
    	else if(title.equals("Kunder")) {
    		getCustomersMenu();
    	}
    	else if(title.equals("Hem")) {
    		getHomeScreen();
    	}
    	else if(title.equals("Säljare")) {
    		getSalesManPane();
    	}
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    	
    }  
    
}
    
   