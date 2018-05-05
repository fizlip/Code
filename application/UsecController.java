package application;


import java.io.FileInputStream;
import helper.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;


import java.text.SimpleDateFormat;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.chart.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

interface UsecInterface {
	XYChart.Data barChart(String day, Integer amountSold);
}

public class UsecController {
	
	//Statiska instansvariabler
	private static XYChart.Series<Integer, Integer> personalSales = RegisterController.personalSales;
	private static XYChart.Series personalSalesAmount = RegisterController.personalSalesAmount;
	private static ObservableList<String> items = RegisterController.items; 
	public static int amountSold = 0;
	public static float progress = 0.0f;
	//public Map<String, Map<String, String>> customerData = ReadCustomerData.getMetaMapping();
	public List<SalesMan> data = SalesForce.getSalesForce();
	public static double x = 0;
	private double heightAdd = 95;
	
	public static String[] newColNames = {"Personnummer", "Namn", "Tjänst", "Kundpris", "Telefon", "Adress", "Postnummer" , "Ort",
			"E-Mail", "Mailfaktura", "Nyhetsmail", "Datum", "Kommentar", "FSG (SEK)", "Betalt", 
			"Mak", "Status", "Räddat", "Betaldatum", "Kundnummer", "Bricknummer"};
	
	//private RegisterController register = new RegisterController(this.user);
	protected static List<Object> keys = new ArrayList<>();
	public static XYChart.Series<String, Number> series = new XYChart.Series<>();	//amount sold barchart in the salesmanview

	
	private static boolean log = false;
	public static JSONObject searchResult = new JSONObject();
	public int results = 0;
	private String currentSearch;
	protected static boolean resultFound = false;
	public static int currentPage = 0;
	protected static String wd = "C:\\Users\\f_ill\\eclipse-workspace\\NewSystem\\build\\build\\src\\application\\";

	public static StringProperty salesmanName = new SimpleStringProperty("Namn");
	public static StringProperty salesToday = new SimpleStringProperty("0.0");
	public static StringProperty salesmanSalary = new SimpleStringProperty("0.0");
	public static StringProperty avgPrice = new SimpleStringProperty("0.0");
	public static StringProperty user = new SimpleStringProperty("");
	public int y = 50;
	public Double height = 0.0;
	public static tcpCom.NetworkConnection connection; 
	private boolean isServer;
	public static boolean keepRunning = true;
	public static int pageSize = 10;
	public static Stage passwordStage = new Stage();

	
    @FXML
    private ResourceBundle resources;
    
    @FXML
    private ImageView logo;
    
    @FXML
    private MenuButton settings;
    
    @FXML
    private GridPane salesMainWindow;
    
    @FXML
    private AnchorPane customerSearchRequest;
    
    @FXML
    private Group usecTitle;
    
    @FXML
    private URL location;
    
    @FXML
    private ImageView userImage;

    @FXML
    private Button newCustomer;

    @FXML
    private TextField searchFieldAction;
    
    @FXML
    private TextField newGoalTextField;

    @FXML
    private Button searchButton;

    @FXML
    private ScrollPane searchPane;
    
    @FXML
    private MenuItem changePasswordMenu;
    
    @FXML
    void openSetting(MouseEvent event) {
    	settings.show();
    }
    
    @FXML
    void showPasswordDialog(ActionEvent event) {
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
    void newCustomerAction(ActionEvent event) {
		//registration.showWindow();
    	
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
				while(true) {
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
    
    @FXML
    void backToMain(MouseEvent event) {
    	salesMainWindow.setVisible(true);
    	searchPane.setVisible(false);
    }
    
    private void send(String message) {
		ChatController.arrived = false;
		ChatController.type = "s";
		
		try {
			connection.send(message);
		} catch (Exception e) {
			//serverCom.set("Anslutning misslyckades");
			e.printStackTrace();
		}
    }
    
    @FXML
    void searchAction(MouseEvent event) {
    	salesMainWindow.setVisible(false);
    	searchPane.setVisible(true);
    	customerSearchRequest.setVisible(true);
    	customerSearchRequest.getChildren().clear();
    	customerSearchRequest.setPrefHeight(0);
		System.out.println("STARTING SEARCH");
		String search = searchFieldAction.getText();
		try {
			System.out.println(user.get() + " - " + "GET: " + search);
			send(user.get() + " - GET: " + search + ": sök");
		} catch (Exception e) {
			System.out.println("Could not send message");
			e.printStackTrace();
		}
    }
    
    @FXML
    private MenuItem userStats;
    
    @FXML
    private Text AmountSold;
    
    @FXML
    private Text postalCode;
    
    @FXML
    private Text commentText;
    
    @FXML
    private Text PersonalBest;
    
    @FXML
    private Text dailyGoal;

    @FXML
    private Text monthlyGoal;

    @FXML
    private Text customerNameText;
    
    @FXML
    private Text personalIDText;
    
    @FXML
    private Text customerTelephoneText;
    
    @FXML
    private Text dateOfSaleText;
    
    @FXML
    private MenuItem newGoal;
    
    @FXML
    private LineChart<Integer, Integer> personalSalesChart;
    
    @FXML
    private BarChart<?, ?> salesAmount;

    @FXML
    private Text salary;
    
    @FXML
    private Text logBookTitle;
    
    @FXML
    private Text leaderBoardText;
    
    @FXML
    private ProgressBar DailyGoalBar;

    @FXML
    private ProgressBar MonthlyGoalBar;
    
    @FXML
    private ListView logBook;
    
    @FXML
    private ListView leaderBoard;
    
    @FXML
    void showStats(ActionEvent event) {
    	salesMainWindow.setVisible(false);
    	searchPane.setVisible(true);
    	customerSearchRequest.setVisible(true);
    	customerSearchRequest.getChildren().clear();
    	customerSearchRequest.setPrefHeight(0);
    	send(user.get() + " - GET: " + user.get() + ": sök");
    }
    
    @FXML
    void handleSalaryAction(MouseEvent event) {
    	SalaryText.toggleSec();
    }
    
    @FXML
    void newGoalAction(ActionEvent event) {
    	newGoalTextField.setVisible(true);
    }
    
    @FXML
    void titleClicked(MouseEvent event) {
    	if (log){
	    	logBook.setVisible(false);
	    	logBookTitle.setVisible(false);
	    	leaderBoard.setVisible(true);
	    	leaderBoardText.setVisible(true);
    	}else{
    		logBook.setVisible(true);
	    	logBookTitle.setVisible(true);
	    	leaderBoard.setVisible(false);
	    	leaderBoardText.setVisible(false);
    	}
    	System.out.println(log);
    	log = !log;
    }
    
    @FXML
    void newGoalWrittenAction(KeyEvent event) {
    	if(event.getCode().equals(KeyCode.ENTER)) {
    		Double salary = SalaryText.stringToDouble(SalaryText.property());
    		DailyProgressText.goalProperty().set(newGoalTextField.getText() + " SEK");
    		Double newGoal = Double.parseDouble(newGoalTextField.getText());
    		Intermediate.setProperty(DailyProgressText.progressProperty(), salary / newGoal);
    		newGoalTextField.setVisible(false);
    	}
    }
    
    public static void incAmountSold() {
    	amountSold++;
    }
    
    public static void incX() {
    	x++;
    }
    
    
    public void createBarChart() {
    	
    	personalSalesAmount.getData().add(new XYChart.Data<String, Integer>("Måndag", 36));
        personalSalesAmount.getData().add(new XYChart.Data<String, Integer>("Tisdag", 24));
        personalSalesAmount.getData().add(new XYChart.Data<String, Integer>("Onsdag", 42));
        personalSalesAmount.getData().add(new XYChart.Data<String, Integer>("Torsdag", 25));
        
    	XYChart.Data<String, Integer> value = new XYChart.Data<>("Fredag", amountSold);
    	personalSalesAmount.getData().add(value);
    	salesAmount.getData().add(personalSalesAmount);
    }
    
    public void createLineChart() {
    	
    	Double record = Double.parseDouble(Intermediate.getProperty(AmountSoldText.bestProperty()));
    	
    	XYChart.Data personalBest = new XYChart.Data<Double, Double>(record,(Double)299.0);
        XYChart.Series<Integer, Integer> recordSeries = new XYChart.Series<>();
    	recordSeries.getData().add(personalBest);
    	
    	personalSalesChart.getData().add(recordSeries);
    	personalSalesChart.getData().add(personalSales);
    }
    
    public void getSalesman() {
    	
    }
    
    private tcpCom.Client createClient(){
    	return new tcpCom.Client("127.0.0.1", 55555, data ->{
    		Platform.runLater(() ->{
    			String[] formattedMessage = data.toString().split(" START");
    			System.out.println(Arrays.toString(formattedMessage));
    			if(ChatController.type.equals("m")) {
    				//AdminController.listener.addMessage();
    			}
    			else if(formattedMessage[0].equals("SEND")) {
    				post(formattedMessage[1]);
    			}
    			else if(formattedMessage[0].equals("DATA")) {
    				if(formattedMessage[1].equals("AUTHENTICATED")) {
    					passwordStage.close();
    					Alert alert = new Alert(AlertType.CONFIRMATION, "Ditt lösenord har nu uppdaterats", 
								ButtonType.OK); 
    					alert.showAndWait();
    				}
    				else {				
    					PasswordDialogController.errorText.set("Gammalt lösenord felaktigt");
    				}
    			}
    			else {
    				
    			}
    		});
    	});
    }
    
    private void connectClient() {
    	try {
    		isServer = false;
    		connection = createClient();
    		connection.startConnection();
    	}catch(Exception e) {
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
    
    private void post(String message) {
    	JSONObject searchJson = new JSONObject();
		searchJson = parseSearch(message);
		currentPage = 0;
		y = 0;
		searchResult = searchJson;
    	results = searchJson.size();
    	keys = Arrays.asList(searchResult.keySet().toArray());
    	Object[] searchKeys = searchJson.keySet().toArray();
    	CustomerPaneController.searchResult = searchJson;
    	
    	 	
    	//Salesman search
    	try{
    		searchJson.get("meta data").equals(null);
    		searchResult = searchJson;
    		AdminController.series = new XYChart.Series<>();
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
				    	getResult(searchJson);
				    	currentPage=0;
				    	break;
				     }
	    		}else {
	    			/*
	    			nextPageGroup.setVisible(true);
	    			nextPageGroup.setLayoutY(y);
	    			nextPageGroup.toFront();
	    			*/
	    			break;}
	    	}
    	}
    	//Misc search
    }
    
    public static String getUser() {
    	return user.get();
    }
    
    public void getResult(JSONObject customerInfo) {
    	try {
	   		// Ladda in fxml filen för customerPane och lagra dess pane i root.
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerPane.fxml"));
		   	AnchorPane root = loader.load();
		   	// Lägg till root till customerSearchPane
		   	customerSearchRequest.getChildren().add(root);
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
        	customerSearchRequest.setPrefHeight(height);
	    }catch(IOException e) {
			//Om inladdning misslyckas skriv ut felet i terminalen
			e.printStackTrace();
	   }
    }
    
    private void salesManSearch(String name, JSONObject salesman) {
    	//Initialize sales view
    	List<Long> contained = new ArrayList<>();
		contained.add(0, 0L);
		// Get data for sales-barchart
		SalesMan s = new helper.SalesMan(user.get(), (JSONObject) salesman);
		
		s.getBarData();
		Map<String, Number> mapping = s.getMapping();
		
		//Set text fields
		AdminController.salesmanName.set(s.getName());
		AdminController.salesToday.set(Integer.toString(s.getAmountSold()));
		AdminController.salesmanSalary.set(Double.toString(s.getSalary()));
		AdminController.avgPrice.set(Double.toString((s.getSalary() / s.getAmountSold())));
		
		//Get formatted date of every day in the mapping
		AdminController.series = DateHandler.getDateSeries(AdminController.series, mapping, contained);
		getSalesManPane(AdminController.series);
		// Add data to charts
		CreateCharts.getSalemanStatusData(salesman);

		
		
		searchFinished();
    }
    
    private void searchFinished() {
    	searchFieldAction.setText("");
    	searchFieldAction.setPromptText("Tryck på Enter för att söka");
		searchFieldAction.getScene().setCursor(Cursor.DEFAULT);
		currentPage = 0;
    }
    
    private void getSalesManPane(XYChart.Series<String, Number> salesman) {    	
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("SalesManPane.fxml"));
    		AnchorPane salesmanPane = loader.load();
    		FXMLLoader daysLoader = new FXMLLoader(getClass().getResource("Days.fxml"));
    		AnchorPane days = daysLoader.load();
    		days.setLayoutY(salesmanPane.getPrefHeight() + 50);
    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    		helper.Days gridData = new helper.Days();
    		
    		ObservableList<XYChart.Data<String, Number>> data = salesman.getData();
    		int[][] points = gridData.getPoints(data);
    		List<Rectangle> rects = gridData.createDayGrid(days);
    		List<Rectangle> rs = gridData.paint(rects, points);
    		for(Rectangle rect : rs) {
    			days.getChildren().add(rect);
    		}
    		
       		customerSearchRequest.getChildren().addAll(salesmanPane, days);
    		customerSearchRequest.setPrefHeight(customerSearchRequest.getPrefHeight() + salesmanPane.getPrefHeight() + days.getPrefHeight() + 50);
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
    }
    
    private void openPasswordDialog() {
    	Stage stage = new Stage();
    	try {
    		FXMLLoader passwordLoader = new FXMLLoader(getClass().getResource("PasswordChange.fxml"));
    		AnchorPane passwordDialog = passwordLoader.load();
    		Scene scene = new Scene(passwordDialog, 470, 280);
    		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    		stage.setTitle("Ändra Lösenord");
    		stage.setScene(scene);
    		stage.show();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @FXML
    void initialize() {        
        // Binding text properties
    	connectClient();
    	dailyGoal.textProperty().bind(DailyProgressText.goalProperty());
        monthlyGoal.textProperty().bind(MonthlyProgressText.goalProperty());
        salary.textProperty().bind(SalaryText.property());
        PersonalBest.textProperty().bind(AmountSoldText.bestProperty());
        AmountSold.textProperty().bind(AmountSoldText.salesProperty());
        logBook.setItems(items);
        
        ObservableList<String> leader = FXCollections.observableArrayList(); 
        leader.addAll("1: Bästa säljaren, XXX st.", "2: Näst Bästa säljaren, YYY st.", "3: Tredje bästa säljaren ZZZ st." ,"osv.");
        leaderBoard.setItems(leader);
        
        
        MonthlyGoalBar.setProgress(progress);
        MonthlyGoalBar.progressProperty().bind(MonthlyProgressText.progressProperty());
        
        DailyGoalBar.progressProperty().bind(DailyProgressText.progressProperty());
        
        CustomerPaneController.connection = connection;

        createBarChart();
    	createLineChart();
    	salesAmount.lookupAll(".default-color0.chart-bar")
    	.forEach(n -> n.setStyle("-fx-bar-fill: #3b9ff7;"));
    	send(user.get() + " - GET: WEEK" + ": data");
    	
    }
}
