package helper;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ListView;
import javafx.collections.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.Group;
import java.util.*;

import app.AmountSoldText;
import app.DailyProgressText;
import app.Intermediate;

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
	private double heightAdd = 25;
	
	public static String[] newColNames = {"Personnummer", "Namn", "Tjänst", "Kundpris", "Telefon", "Adress", "Postnummer" , "Ort",
			"E-Mail", "Mailfaktura", "Nyhetsmail", "Datum", "Kommentar", "FSG (SEK)", "Betalt", 
			"Mak", "Status", "Räddat", "Betaldatum", "Kundnummer", "Bricknummer"};
	
	private RegisterController register = new RegisterController();
	
	private static boolean log = false;
	
    @FXML
    private ResourceBundle resources;
    
    @FXML
    private ImageView logoPic;
    
    @FXML
    private GridPane salesMainWindow;
    
    @FXML
    private AnchorPane customerSearchRequest;
    
    @FXML
    private Group usecTitle;
    
    @FXML
    private URL location;

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
    void newCustomerAction(ActionEvent event) {
    	register.showWindow();
    }
    
    @FXML
    void backToMain(MouseEvent event) {
    	salesMainWindow.setVisible(true);
    	searchPane.setVisible(false);
    }
    
    @FXML
    void searchAction(MouseEvent event) {
    	salesMainWindow.setVisible(false);
    	searchPane.setVisible(true);
    	searchFieldAction.setPromptText("Namn");
    	String search = searchFieldAction.getText();
    	int x = 0;
    	int y = 50;
    	int count = 0;
    	customerSearchRequest.setPrefHeight(0.0);
    	Double height = 0.0;
    	customerSearchRequest.getChildren().clear();
    
    	SalesForce.createSalesForce();
    	for(int i = 0; i < data.size(); i++) {
	    	//Om sök fältet är tom vid tryck av knapp ges all säljarens kunder
	    	if (search.length() == 0) {
	    		giveAll(i);
	    	}
	    	//Annars kontrolleras om sökning är ett kommando
	    	else if(search.split(" ")[0].equals("ändra")) {
	    		String[] splittedSearch = search.split(" ");
	    		data.get(i).getSalesMapping().get(splittedSearch[1].trim()).put(splittedSearch[2].trim(), splittedSearch[4].trim());
	    	}
	    	//Slutligen testa vilken sorts sökning som har gjorts
	    	else{
		   		Customer customer = data.get(i).getSalesMapping().get(search.trim());
		    	//Om sökningen var ett personnummer och kunden finns skriv ut på skrämen
		    	if(customer != null) {
		    		//Rita ut all kunddata på skärmen
		    		//getCustomerPane(customer, search, y);
		    		
		    		//Uppdatera skärmen
		    		y += heightAdd + 5;
		    		height += heightAdd;
		    		customerSearchRequest.setPrefHeight(height);
		    	}
		    	//Om sökningen inte var ett personnummer kolla om det var ett annat fält som söktes
		    	else {
		    		otherSearch(search, y, height, i);
		    	}
	    	}
	    	searchFieldAction.setText("");
	    }
    }
    
    private void otherSearch(String search, int y, double height, int index) {
    	//Hantera sökning annan än personnummer
    	for(Map.Entry<String, Customer> customerSearch: data.get(index).getSalesMapping().entrySet()) {
	    	Customer customerInfo = customerSearch.getValue();
		   	
	    	//Titta igenom varje fält för varje kund och se om värdet för detta fältet matchar sökningen.
	    	for (String col : newColNames) {
		    	if (!col.equals("Personnummer") && customerInfo.get(col).trim().equals(search.trim())) {
		    		
		    		// Rita ut datan på skärmen om matching har hittats, uppdatera även skärmen
		    		//getCustomerPane(customerInfo, customerSearch.getKey(), y);
		    		y += heightAdd + 5;
		        	height += heightAdd + 9;
		    	   	customerSearchRequest.setPrefHeight(height);
	    		}
	    	}
    	}
    }
    
    private void giveAll(int index) {
    	int x = 0;
    	int y = 50;
    	Double height = customerSearchRequest.getPrefHeight();
    	for(Map.Entry<String, Customer> customer: data.get(index).getSalesMapping().entrySet()) {
	   		//Lagra kun i customerInfo variabel och skriv all data till en customerPane.
	   		Customer customerInfo = customer.getValue();;
	   		//getCustomerPane(customerInfo, customer.getKey(), y);
	   	
	    	// Justera höjden på customerSearchPane för varje kund som laddas in
	    	y += heightAdd + 5;
	    	height += heightAdd + 9;
		   	customerSearchRequest.setPrefHeight(height);
	    }
    }
    /*
     * FIXA DETTA NÅGON GÅNG
     * 
    private void getCustomerPane(Customer customerInfo, String search, int y) {
    	try {
			// Ladda in fxml filen för customerPane och lagra dess pane i root.
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerPane.fxml"));
		   	AnchorPane root = loader.load();
		   	
		   	// Lägg till root till customerSearchPane
		   	customerSearchRequest.getChildren().add(root);
		    CustomerPaneController con = loader.getController();
		    
		    // Skriv in all aktuell kundata i den aktuella customerPanen
		    con.setText(
		    		customerInfo.get("Namn"), search, customerInfo.get("Telefon"),customerInfo.get("Datum"),
		    		customerInfo.get("Postnummer"), customerInfo.get("Ort"), customerInfo.get("Adress"),
		    		customerInfo.get("E-Mail"), customerInfo.get("Mailfaktura"), customerInfo.get("FSG (SEK)"),
		    		customerInfo.get("Betalt"), customerInfo.get("Mak"), customerInfo.get("Status"),
		    		customerInfo.get("Räddat"), customerInfo.get("Betaldatum"), customerInfo.get("Tjänst"),
		    		customerInfo.get("Kundnummer"), customerInfo.get("Kommentar"), customerInfo.get("Bricknummer")
		    		);
		    
		    //Rita customerPane på korrekt plats på skärmen
		    root.setLayoutX(x); root.setLayoutY(y);  root.toBack();
		}catch(IOException e) {
			//Om inladdning misslyckas skriv ut felet i terminalen
			e.printStackTrace();
	    }
	}
    */
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
    
    @FXML
    void initialize() {
    	assert newCustomer != null : "fx:id=\"newCustomer\" was not injected: check your FXML file 'UsecurityDasboard.fxml'.";
        assert searchFieldAction != null : "fx:id=\"searchFieldAction\" was not injected: check your FXML file 'UsecurityDasboard.fxml'.";
        assert searchButton != null : "fx:id=\"searchAction\" was not injected: check your FXML file 'UsecurityDasboard.fxml'.";
        assert personalSalesChart != null : "fx:id=\"personalSalesChart\" was not injected: check your FXML file 'UsecurityDasboard.fxml'.";
        assert dailyGoal != null : "fx:id=\"dailyGoal\" was not injected: check your FXML file 'UsecurityDasboard.fxml'.";
        assert monthlyGoal != null : "fx:id=\"monthlyGoal\" was not injected: check your FXML file 'UsecurityDasboard.fxml'.";
        assert DailyGoalBar != null : "fx:id=\"DailyGoalBar\" was not injected: check your FXML file 'UsecurityDasboard.fxml'.";
        assert MonthlyGoalBar != null : "fx:id=\"MonthlyGoalBar\" was not injected: check your FXML file 'UsecurityDasboard.fxml'.";
        assert salesAmount != null : "fx:id=\"salesAmount\" was not injected: check your FXML file 'UsecurityDasboard.fxml'.";
        assert salary != null : "fx:id=\"salary\" was not injected: check your FXML file 'UsecurityDasboard.fxml'.";
        
        // Binding text properties
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
        
        createBarChart();
    	createLineChart();
    	salesAmount.lookupAll(".default-color0.chart-bar")
    	.forEach(n -> n.setStyle("-fx-bar-fill: #3b9ff7;"));
    	
    	try {
	        FileInputStream input = new FileInputStream("C:\\Users\\f_ill\\eclipse-workspace\\UsecDashboard\\build\\build\\classes\\application\\ISGG-logga.png");
	        Image logo = new Image(input); logoPic.setImage(logo);
        }catch(FileNotFoundException e) {e.printStackTrace();}
    	
    }
}
