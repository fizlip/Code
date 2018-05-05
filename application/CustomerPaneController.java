package application;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.*;
import javafx.scene.control.TitledPane;
import javafx.scene.input.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CustomerPaneController {
	
	
	private static ObservableList<String> items = FXCollections.observableArrayList(); 
	private ObservableList<String> comments = FXCollections.observableArrayList();
	
	public static JSONObject searchResult = new JSONObject(); 
	private static List<Object> keys = new ArrayList<>();
	
	public static tcpCom.NetworkConnection connection;
	
	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
	
	@FXML
    private AnchorPane custPane;

    @FXML
    private Text postalIDText;

    @FXML
    private Text stateText;
    
    @FXML
    private Text eMailText;

    @FXML
    private Text mailBillText;

    @FXML
    private Text FSGText;

    @FXML
    private Text paidText;

    @FXML
    private Text makText;

    @FXML
    private Text statusText;

    @FXML
    private Text savedText;

    @FXML
    private Text payDateText;

    @FXML
    private Text serviceText;

    @FXML
    private Text commentText;

    @FXML
    private Text adressText;
    
    @FXML
    private TitledPane nameTab;
    
    @FXML
    private Text customerNumberText;
    
    @FXML
    private AnchorPane subPane;
    
    @FXML
    private DialogPane commentPane;
    
    @FXML
    private Text index;
    
    @FXML
    private Text badgeNumberText;
    
    @FXML
    private Text searchResultText;
    
    @FXML
    private Text serviceTextNew;
    
    @FXML
    private Text latestChangeTextNew;
    
    @FXML
    private Text salesManText;
    
    //Textfält
    @FXML
    private TextField serviceChangeTextField;
    
    @FXML
    private TextField commentField;
    
    @FXML
    private TextField customerIDTextField;
    
    @FXML
    private TextField FSGTextField;
    
    @FXML
    private TextField paidTextField;
    
    @FXML
    private TextField statusTextField;
    
    @FXML
    private TextField postalCodeTextField;
    
    @FXML
    private TextField stateTextField;
    
    @FXML
    private TextField eMailTextField;
    
    @FXML
    private TextField mailBillingTextField;
    
    @FXML
    private TextField adressTextField;
    
    //Knappar
    @FXML
    private RadioButton makButton;
    
    @FXML
    private RadioButton paidButton;
    
    @FXML
    private RadioButton registerKeyBadge;
    
    @FXML
    private RadioButton savedButton;
    
    @FXML
    private Accordion mainAccordion;
    
    @FXML
    private Accordion subAccordion;
    
    @FXML
    private TitledPane customerPane;
    
    @FXML
    private Text telephoneText;
    
    @FXML
    private Text personalNumberText;
    
    @FXML
    private Text dateText;
    
    @FXML
    private Text latestUpdateText;
    
    @FXML
    private Text salesManNameText;
    
    @FXML
    private Button historyButton;

    @FXML
    private ListView customerHistoryList;
    
    @FXML
    private Group nextPageGroup;
    
    @FXML
    private ListView<String> commentList;
    
    @FXML
    private String customerId;
    
    @FXML
    private Text nextPageText;
    
    public void giveConnection(tcpCom.NetworkConnection conn) {
    	this.connection = conn;
    }
    
    @FXML
    void openCustomerWindowAction(MouseEvent event) {
    	try {
    		Stage customerStage = new Stage();
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerWindow.fxml"));
		   	GridPane root = loader.load();
    		
    		Scene scene = new Scene(root, 520, 615);
    		CustomerWindowController con = loader.getController();
    		con.initWindow(customerId);
    		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    		customerStage.setTitle("Kundfönster");
    		customerStage.setScene(scene);
    		customerStage.setAlwaysOnTop(true);
    		customerStage.show();
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public String updateId(String id) {
    	return id;
    }
    
    @FXML
    void mouseEnteredAction(MouseEvent event) {
    	searchResultText.setUnderline(true);
    }
    
    @FXML
    void mouseExitAction(MouseEvent event) {
    	searchResultText.setUnderline(false);
    }

    @FXML
    void mouseEnterNextPage(MouseEvent event) {
    	nextPageText.setFill(Color.BLACK);

    }
    
    @FXML
    void mouseExitNextPage(MouseEvent event) {
    	nextPageText.setFill(Color.valueOf("#757575"));

    }
    
    @FXML
    void nextPagePressed(MouseEvent event) {
    	
    }
    
    public void printResult(String name, String customerNumber, String serviceText, String salesman, int currentPage) {
    	keys = Arrays.asList(searchResult.keySet().toArray());
    	customerId = updateId(customerNumber);
    	searchResultText.setText(customerNumber + ": " + name);
    	serviceTextNew.setText(serviceText);
    	latestChangeTextNew.setText("Senaste ändring: Ingen hittad");
    	salesManNameText.setText(salesman);
    	System.out.println(currentPage);
    }
    
    public AnchorPane getPane() {
    	return custPane;
    }
    
    @FXML
    void initialize() {
        assert custPane != null : "fx:id=\"customerPane\" was not injected: check your FXML file 'CustomerPane.fxml'.";
    }
}