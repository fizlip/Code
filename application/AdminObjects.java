package application;

import java.net.URL;
import java.util.ResourceBundle;


import helper.Customer;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class AdminObjects extends AdminParams{

	//Groups
	@FXML
	protected Group nextPageGroup;
	
	@FXML
	protected Group titleGroup;
	
	// Rectangles
	
	@FXML
	protected ResourceBundle resources;

	@FXML
	protected Text resultsTextTitle;

	@FXML
	protected Text userText;
	
	//Imageview
	@FXML
	protected ImageView logoPic;
	
	@FXML
	protected ImageView logoPicBig;
	
	// URL
    @FXML
    protected URL location;
    
    //AnchorPane
    @FXML
    protected AnchorPane salesManPane;
    
    @FXML
    protected AnchorPane customerInfoPane;
    
    @FXML
    protected AnchorPane headerPane;
    
    @FXML
    protected AnchorPane menuPane;
    
    //GridPane
    
    
    //Progress indicator
    
    //Contextmenu
    
    //ScrollPane
    @FXML
    protected ScrollPane salesSearchPane;
    
    //Text
    
    //Menu
    
    //Other
    @FXML
    protected Text soldTodayText;
    
    @FXML
    protected Text serverText;
    
    @FXML
    protected Text titleText;
    
    @FXML
    protected Text salesmenTitleText;
    
    @FXML
    protected Text adminTitle;
    
    @FXML
    protected Text salesManNameText;
    
    @FXML
    protected Text soldText;
    
    @FXML
    protected Text forText;
    
    @FXML
    protected Text adminHeaderText;
    
    @FXML
    protected Text perSec;
    
    //Numbers specific
    @FXML
    protected Text salesManSalaryText;
    
    //Numbers general
    @FXML 
    protected Text totalText;
    
    @FXML
    protected Text makAmountText;
    
    @FXML
    protected Text amountSoldText;
    
    @FXML
    protected Text avgPricePerSale;
    
    @FXML
    protected Text avgPriceText;
    
    @FXML
    protected Text totSoldText;
    
    @FXML
    protected Text avgTotSalesText;
    
    @FXML
    protected Text totalCustomerText;
    
    @FXML
    protected Text totalAmountSoldText;
    
    @FXML
    protected Text salesTodayText;

    
    //Labels
    @FXML
    protected Label driveSearchLabel;
    
    //TextField
    @FXML
    protected TextField searchField;
    
    @FXML
    protected TextField serverComField; 
    
    //Button
    @FXML
    protected Button summaryButton;
    
    @FXML
    protected Button simSaleButton;

    @FXML
    protected Button adminSearch;
    
    @FXML
    protected ImageView homeLogo;

    @FXML
    protected ImageView salesLogo;

    @FXML
    protected ImageView customersLogo;

    @FXML
    protected ImageView salesmenLogo;

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
    
    
	
}
