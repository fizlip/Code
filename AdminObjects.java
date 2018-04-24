package app;

import java.net.URL;
import java.util.ResourceBundle;

import com.lynden.gmapsfx.GoogleMapView;

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

public class AdminObjects extends AdminParams{

	//Groups
	@FXML
	protected Group nextPageGroup;
	
	@FXML
	protected Group titleGroup;
	
	// Rectangles
	@FXML
	protected Rectangle showMoreRectangle;
	
	@FXML
	protected Rectangle menuRect;
	
	@FXML
	protected Rectangle menuRect2;
	
	@FXML
	protected ResourceBundle resources;
	
	//Google FXML
	@FXML
	protected GoogleMapView googleMapView;
	
	//Lines
	@FXML
	protected Line nameUnderline;
	
	//Web services
	@FXML
	protected WebView webBrowser;
	
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
    @FXML
    protected GridPane salesTabPane;
    
    @FXML
    protected GridPane adminHeader;
    
    @FXML
    protected GridPane mapGrid;
    
    @FXML
    protected GridPane salesGridPane;
    
    //Progress indicator
    @FXML
    protected ProgressIndicator searchProgress;
    
    //TabPane
    @FXML
    protected TabPane adminView;
    
    //Contextmenu
    @FXML
    protected ContextMenu mapFullscreenButton;
    
    //ScrollPane
    @FXML
    protected ScrollPane salesSearchPane;
    
    //Text
    
    //Menu
    @FXML
    protected Text salesPageTitleText;

    @FXML
    protected Text totalSalesSubtitleText;

    @FXML
    protected Text salesStatsSubtitleText;

    @FXML
    protected Text customerPageTitleText;

    @FXML
    protected Text customerPropSutitleText;

    @FXML
    protected Text totCustomersSubtitleText;
    
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
    protected Text avgPriceTextTitle;
    
    @FXML
    protected Text hiredTextTitle;
    
    @FXML
    protected Text totalSoldTextTitle;
    
    @FXML
    protected Text avgSoldTextTitle;
        
    @FXML
    protected Text resultsTextTitle;
    
    //Numbers specific
    @FXML
    protected Text salesManSalaryText;
    
    //Numbers general
    @FXML 
    protected Text totalText;
    
    @FXML
    protected Text makAmountText;
    
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
    
    @FXML
    protected Text showMoreText;

    
    //Labels
    @FXML
    protected Label driveSearchLabel;
    
    //TextField
    @FXML
    protected TextField searchField;
    
    @FXML
    protected TextField serverComField;
    
    //ListView
    @FXML
    protected ListView<String> eventList;
    
    @FXML
    protected ListView<String> salesList;
    
    //TableView
    @FXML
    protected TableView<Customer> allCustomersTable;
    
    //Button
    @FXML
    protected Button summaryButton;
    
    @FXML
    protected Button simSaleButton;

    @FXML
    protected Button adminSearch;
    
    //LineChart
    @FXML
    protected LineChart<Integer, Integer> searchedSalesManLineChart;
    
    @FXML
    protected LineChart<Double, Double> totalSalesLineChart;
    
    @FXML
    protected LineChart<String, Number> salesByDayLinechart;
    
    @FXML
    protected LineChart<String, Number> salesManLineChart;
    
    @FXML
    protected LineChart<String, Number> salesChart;
    
    //BarChart
    
    @FXML
    protected StackedBarChart<String, Integer> totalSalesBarChart;
    
    @FXML
    protected BarChart<String, Integer> salesmanBarchart;
    
    @FXML
    protected BarChart<String, Double> priceBarChart;
	
}
