package helper;

import java.net.URL;
import java.util.*;

import app.AmountSoldText;
import app.DailyProgressText;
import app.GifExample;
import app.Intermediate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import java.net.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.text.*;
import javafx.scene.input.*;
import javafx.scene.control.TextArea;

public class RegisterController{
	
	public static XYChart.Series personalSalesAmount = new XYChart.Series<>();
	public static XYChart.Series<Integer, Integer> personalSales = new XYChart.Series<>();
	public static ObservableList<String> items = FXCollections.observableArrayList(); 
	public static float progress = 0.0f;
	private int x = 0;
	
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
    void registerEnterKey(KeyEvent event) {
    	if(event.getCode().equals(KeyCode.ENTER)) {
    		ActionEvent e = new ActionEvent();
    		registerNewCustomerAction(e);
    	}
    }

    @FXML
    void registerNewCustomerAction(ActionEvent event) {
    	UsecController.incAmountSold();
    	// Spara informatio om kund och skapa word dokument
    	Registration reg = new Registration();
    	reg.registerNewCustomer(nameField.getText(), adressField.getText(), 
    							priceField.getText(), personalIdField.getText(), telField.getText(), commentField.getText(),
    							personalIdField.getText(), ortField.getText(), postCodeField.getText(), "Nyckelbricka",
    							emailField.getText());
    	
    	Long now = System.currentTimeMillis();
    	Date date = new Date(now);
    	DateFormat formatter = new SimpleDateFormat("HH:mm");
    	String formatedTime = formatter.format(now);
    	String newSaleLogbookMessage = salesnameField.getText() + ": Nytt sälj, " + priceField.getText() + " SEK";
    	int padding = 155 - (newSaleLogbookMessage.length());
    	
    	String padString = "";
    	for(int i = 0; i < padding-1; i++) {
    		padString += " ";
    	}
  
    	items.add(newSaleLogbookMessage + padString + formatedTime);
    	
    	SalaryText.addToSalary(priceField.getText());
    	// Uppdatera grafer
    	updateCharts();
    	
    	UsecController.incX();
    	
    	// Uppdatera all text som ska uppdateras
    	updateText();
    	
    	try {
    		GifExample.runGif();
    		GifExample.specificGif = null;
    	}catch(MalformedURLException e){
    		e.printStackTrace();
    	}
    	Stage regStage = (Stage) finished.getScene().getWindow();
    	regStage.close();
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
    	XYChart.Data newSale = new XYChart.Data("Fredag", UsecController.amountSold);
    	//Line chart data
    	XYChart.Data priceOfSale = new XYChart.Data<Double, Double>(UsecController.x,Double.parseDouble(priceField.getText()));
    	
    	personalSalesAmount.getData().add(newSale);
    	personalSales.getData().add(priceOfSale);
    }
    
    public String getPrice() {
    	return priceField.getText();
    }
    
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
    
    @FXML
    void initialize() {
        assert finished != null : "fx:id=\"finished\" was not injected: check your FXML file 'CustomerRegistration.fxml'.";

    }
}