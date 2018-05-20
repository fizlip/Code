package application;

import java.net.URL;
import java.util.*;

import org.json.simple.JSONObject;

import helper.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;
import java.net.*;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.text.*;
import java.time.LocalDate;

import javafx.scene.input.*;
import javafx.scene.control.TextArea;

public class RegisterController{
	
	public static XYChart.Series personalSalesAmount = new XYChart.Series<>();
	public static XYChart.Series<Integer, Double> personalSales = new XYChart.Series<>();
	public static ObservableList<String> items = FXCollections.observableArrayList(); 
	public static float progress = 0.0f;
	public static JSONObject customer;
	private int x = 0;
	private String user;
	public static BooleanProperty newCustomer = new SimpleBooleanProperty(false);
	private static int boxSelected = 0;
	private static LocalDate date;
	//private static StringProperty c = new SimpleStringProperty();
	
    @FXML
    private ResourceBundle resources;

    @FXML
	private TextField nameField;
    
    @FXML
   	private TextField adressField;
    
    @FXML
   	private TextField salesnameField;
    
    @FXML
    private TextField personalIdField;
    
    @FXML
    private TextField postCodeField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField ortField;
    
    @FXML
    private TextField amountField;
    
    @FXML
    private TextField priceField;
    
    @FXML
    private TextArea commentField;
    
    @FXML
   	private TextField idField;
    
    @FXML
    private TextField telField;
    
    @FXML
    private URL location;

    @FXML
    private Button finished;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private Rectangle box1;

    @FXML
    private Rectangle box2;

    @FXML
    private Rectangle box3;

    @FXML
    private Rectangle box4;

    @FXML
    private Rectangle box5;
    
    @FXML
    void datePicked(InputMethodEvent event) {

    }

    @FXML
    void dateSelected(ActionEvent event) {
    	date = datePicker.getValue();
    	System.out.println("SELECTED DATE: " + datePicker.getValue());
    }
    
    @FXML
    void boxEntered(MouseEvent event) {
    	Rectangle r = (Rectangle) event.getSource();
    	Rectangle[] rs = {box1, box2, box3, box4, box5};
    	if(r.getId().equals("box1") && boxSelected < 1) {
    		for(int i = boxSelected ; i < 1; i++) {
    			rs[i].setFill(Color.DODGERBLUE);rs[i].setStrokeWidth(0);
    		}
    	}
    	if(r.getId().equals("box2") && boxSelected < 2) {
    		for(int i = boxSelected ; i < 2; i++) {
    			rs[i].setFill(Color.DODGERBLUE);rs[i].setStrokeWidth(0);
    		}
    	}
    	if(r.getId().equals("box3") && boxSelected < 3) {
    		for(int i = boxSelected ; i < 3; i++) {
    			rs[i].setFill(Color.DODGERBLUE);rs[i].setStrokeWidth(0);
    		}
    	}
    	if(r.getId().equals("box4") && boxSelected < 4) {
    		for(int i = boxSelected ; i < 4; i++) {
    			rs[i].setFill(Color.DODGERBLUE);rs[i].setStrokeWidth(0);
    		}
	
    	}
    	if(r.getId().equals("box5") && boxSelected < 5) {
    		for(int i = boxSelected ; i < 5; i++) {
    			rs[i].setFill(Color.DODGERBLUE);rs[i].setStrokeWidth(0);
    		}
    	}

    }

    @FXML
    void boxExited(MouseEvent event) {
    	Rectangle r = (Rectangle) event.getSource();
    	Rectangle[] rs = {box1, box2, box3, box4, box5};
    	if(r.getId().equals("box1") && boxSelected < 1) {
    		for(int i = boxSelected ; i < 1; i++) {
    			rs[i].setFill(Color.WHITE);rs[i].setStrokeWidth(1);
    		}
    	}
    	else if(r.getId().equals("box2") && boxSelected < 2) {
    		for(int i = boxSelected ; i < 2; i++) {
    			rs[i].setFill(Color.WHITE);rs[i].setStrokeWidth(1);
    		}
    	}
    	else if(r.getId().equals("box3") && boxSelected < 3) {
    		for(int i = boxSelected ; i < 3; i++) {
    			rs[i].setFill(Color.WHITE);rs[i].setStrokeWidth(1);
    		}
    	}
    	else if(r.getId().equals("box4") && boxSelected < 4) {
    		for(int i = boxSelected ; i < 4; i++) {
    			rs[i].setFill(Color.WHITE);rs[i].setStrokeWidth(1);
    		}
	
    	}
    	else if(r.getId().equals("box5") && boxSelected < 5) {
    		for(int i = boxSelected ; i < 5; i++) {
    			rs[i].setFill(Color.WHITE);rs[i].setStrokeWidth(1);
    		}
    	}
    }

    @FXML
    void boxSelected(MouseEvent event) {
    	Rectangle r = (Rectangle) event.getSource();
    	if(r.getId().equals("box1")) {
    		boxSelected = 1;
    	}
    	else if(r.getId().equals("box2")) {
    		boxSelected = 2;
    	}
    	else if(r.getId().equals("box3")) {
    		boxSelected = 3;
    	}
    	else if(r.getId().equals("box4")) {
    		boxSelected = 4;
    	}
    	else if(r.getId().equals("box5")) {
    		boxSelected = 5;
    	}
    }
    
    @FXML
    void registerEnterKey(KeyEvent event) {
    	if(event.getCode().equals(KeyCode.ENTER)) {
    		ActionEvent e = new ActionEvent();
    		//customer = registerNewCustomerAction(e);
    	}
    }
    
    public static JSONObject getCustomer() {
    	return customer;
    }
    
    @FXML
    void registerNewCustomerAction(ActionEvent event) {
    	// Spara informatio om kund och skapa word dokument
    	Registration reg = new Registration(user);
    	customer = reg.registerNewCustomer(nameField.getText(), adressField.getText(), 
    							priceField.getText(), personalIdField.getText(), telField.getText(), commentField.getText(),
    							personalIdField.getText(), ortField.getText(), postCodeField.getText(), "Nyckelbricka",
    							emailField.getText());
    	    	
    	customer.put("Säljare", LoginController.userProp.get());
    	customer.put("Samtals Kvalitet", boxSelected);
    	customer.put("Fakturering", date.toString());
    	customer.put("Datum", DateHandler.getCurrentFormattedTime());
    	customer.put("Status", "Order");
    	customer.put("Kommentar1", "");
    	newCustomer.set(true);
    	// Uppdatera alla klienters skärmar
    	addPeripherals();    	
    	// Uppdatera all text som ska uppdateras
    	updateText();
    	showGif();

    	Stage regStage = (Stage) finished.getScene().getWindow();
    	regStage.close();
    	//return newCustomer;
    }
    
    private void addPeripherals() {
    	Long now = System.currentTimeMillis();
    	DateFormat formatter = new SimpleDateFormat("HH:mm");
    	String formatedTime = formatter.format(now);
    	String newSaleLogbookMessage = " " + AdminController.username + ": Nytt sälj, " + priceField.getText() + " SEK";  
    	items.add(formatedTime + newSaleLogbookMessage);
    	SalaryText.addToSalary(priceField.getText().replace(",", "."));

    	// Uppdatera grafer
    	updateCharts();
    	
    	Double fsg = Double.parseDouble(priceField.getText().toString().replace(',', '.'));
    	
    	UsecController.incAmountSold(fsg);
    	UsecController.incX();
    }
    
    private void showGif() {
    	try {
    		GifExample.runGif();
    		GifExample.specificGif = null;
    	}catch(MalformedURLException e){
    		e.printStackTrace();
    	}
    }
    
    private void updateText() {
    	DailyProgressText.subFromGoal(DailyProgressText.goalProperty(), priceField.getText());
    	MonthlyProgressText.subFromGoal(MonthlyProgressText.goalProperty(), priceField.getText());
    	
    	Double salary = SalaryText.stringToDouble(SalaryText.property());
    	Double percentage = salary/ 50000;
    	Double dailyGoal = Intermediate.stringToDouble(DailyProgressText.goalProperty());
    	//Uppdatera månadstext och dagstext.
    	Intermediate.setProperty(MonthlyProgressText.progressProperty(), percentage);
    	Intermediate.setProperty(DailyProgressText.progressProperty(), salary / dailyGoal);
    	//Uppdatera dagsrekord om detta har slagits
    	if (UsecController.amountSold >= Integer.parseInt(Intermediate.getProperty(AmountSoldText.bestProperty()))) {
    		Intermediate.setProperty(AmountSoldText.bestProperty(), Integer.toString(UsecController.amountSold));
    	}
    	//Uppdatera lön text
    	Intermediate.setProperty(AmountSoldText.salesProperty(), Integer.toString(UsecController.amountSold));
    }
    
    private void updateCharts() {
    	///Histogram data
    	XYChart.Data newSale = new XYChart.Data(DateHandler.getCurrentFormattedTime(), UsecController.amountSold);
    	//Line chart data
    	XYChart.Data priceOfSale = new XYChart.Data<Double, Double>(UsecController.x,Double.parseDouble(priceField.getText()));
    	
    	personalSalesAmount.getData().add(newSale);
    	personalSales.getData().add(priceOfSale);
    }
    
    public String getPrice() {
    	return priceField.getText();
    }
    /*
    public void showWindow() {
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
    }
    */
    @FXML
    void initialize() {
        assert finished != null : "fx:id=\"finished\" was not injected: check your FXML file 'CustomerRegistration.fxml'.";
    }
}