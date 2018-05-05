package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ParseException;


import org.json.simple.JSONObject;

import com.google.api.services.drive.Drive;

import helper.Customer;
import helper.ReadCustomerData;
import helper.SalesForce;
import helper.SalesMan;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.application.Application;

public abstract class AdminParams extends Application{

public List<SalesMan> data = SalesForce.getSalesForce();

	protected boolean isServer = true;
	
	public static Map<Double[], String[]> postionMap = new HashMap<>();
	public List<SalesMan> salesmen = new ArrayList<SalesMan>();
	public static ObservableList<String> items = FXCollections.observableArrayList(); 
	public static ObservableList<String> itemsSold = FXCollections.observableArrayList();
	public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
	protected static JSONObject isggData = ReadCustomerData.readData("C:\\Users\\f_ill\\eclipse-workspace\\isggApp\\data.json");
	protected static boolean resultFound = false;
	public String path = "C:\\Users\\f_ill\\eclipse-workspace\\isggApp\\data.json";
	public static int pageSize = 10;
	public static int currentPage = 0;
	
	//Properties
	protected DoubleProperty translateX = new SimpleDoubleProperty(-475.0);
	protected DoubleProperty translateX2 = new SimpleDoubleProperty(-475.0);
	public static StringProperty serverCom = new SimpleStringProperty("Ingen Koppling");
	public static StringProperty sent = new SimpleStringProperty("");
	public static StringProperty amountSoldToday = new SimpleStringProperty("");
	public static StringProperty user = new SimpleStringProperty("");
	public static int sold = 0;
	public static String username;
	
	//Search variables
	public static JSONObject searchResult = new JSONObject(); 
	protected static List<Object> keys = new ArrayList<>();
	
	//private static StringProperty salesName = new SimpleStringProperty("Förnamn");
	
	protected Drive service;
	protected double heightAdd = 95;
	protected double totalMade = 0;
	protected String salesman;
	
	// Fieldnames in datastructure
	public static String[] newColNames = {"Personnummer", "Namn", "Tjänst", "Kundpris", "Telefon", "Adress", "Postnummer" , "Ort",
											"E-Mail", "Mailfaktura", "Nyhetsmail", "Datum", "Kommentar", "FSG (SEK)", "Betalt", 
											"Mak", "Status", "Räddat", "Betaldatum", "Kundnummer", "Bricknummer"};
	
	//Chart data 
	public static XYChart.Series<String, Number> series = new XYChart.Series<>();	//amount sold barchart in the salesmanview
	protected XYChart.Series<String, Double> priceBarChartSeries = new XYChart.Series<>(); //Price bar chart in total sales tab
	protected XYChart.Series<Integer, Integer> priceSeries = new XYChart.Series<>();	//Sales linechart
	protected XYChart.Series<Double, Double> totalSalesSeries = new XYChart.Series<>();
	protected XYChart.Series<String, Double> salesByDaySeries = new XYChart.Series<>();
	
	
	//Mappings
	protected Map<String, Double> salesByDayMapping = new HashMap<>();
	protected Map<String, Double> salesPriceByNameMapping = new HashMap<>();
	
	public int totalAmountSold = 0;
	public int totMakAmount = 0;
	public int y = 50;
	public int results = 0;
	public int totalHired = 0;
	public Double height = 0.0;
	
	protected static int totalSold = 0;
	
	//Chat
	public static String currentMessage;
	
	// Server
	public static tcpCom.NetworkConnection connection; 
	public static ObservableList<String[]> messages = FXCollections.observableArrayList(); 
	public static boolean messageAdded = false;
	public static Initiater listener;
	
	
	//Menu
	public static String title;
	public static String titleImage;
	protected static String wd = "C:\\Users\\f_ill\\eclipse-workspace\\NewSystem\\build\\build\\src\\application\\";
	
	//Salesman
	//Text
	public static StringProperty salesmanName = new SimpleStringProperty("Namn");
	public static StringProperty salesToday = new SimpleStringProperty("0.0");
	public static StringProperty salesmanSalary = new SimpleStringProperty("0.0");
	public static StringProperty avgPrice = new SimpleStringProperty("0.0");
	public static StringProperty authority = new SimpleStringProperty("0");
	public static StringProperty mValue = new SimpleStringProperty("0");
}
