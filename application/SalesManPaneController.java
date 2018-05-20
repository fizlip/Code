package application;

import java.net.URL;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.text.Text;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;

public class SalesManPaneController {
	
	public static BooleanProperty update = new SimpleBooleanProperty(false);

	public JSONObject salesman;
	
	public static boolean userRequest = false;
	
	public static String currentAuth;
	
    @FXML
    private ResourceBundle resources;
    
    @FXML
    private MenuItem auth1;

    @FXML
    private MenuItem auth2;

    @FXML
    private MenuItem auth3;
    
    @FXML
    private MenuButton authMenu;

    
    @FXML
    private Text mvpValue;
    
    @FXML
    private Text authorityText;

    @FXML
    private URL location;

    @FXML
    public Text nameText;

    @FXML
    private BarChart<String, Double> statusBarChart;

    @FXML
    private LineChart<String, Number> salesLineChart;

    @FXML
    private Text amountSoldText;

    @FXML
    private Text priceText;

    @FXML
    private Text avgPriceText;
    
    @FXML
    private Text userText;
    
    @FXML
    private GridPane userSettings;
    
    @FXML
    void updateAuth(ActionEvent event) {
    	MenuItem item = (MenuItem) event.getSource();
    	if(item.getText().equals("S�ljare")) {
    		authMenu.setText("S�ljare");
    		authorityText.setText("S�ljare");
    		AdminController.authority.set("1");
    	}
    	if(item.getText().equals("Ut�kad S�ljare")) {
    		authMenu.setText("Ut�kad S�ljare");
    		authorityText.setText("Ut�kad S�ljare");
    		AdminController.authority.set("2");
    	}
		if(item.getText().equals("Administrat�r")) {
			authMenu.setText("Administrat�r");
			authorityText.setText("Administrat�r");
			AdminController.authority.set("3");
		}
    	update.set(true);

    }

    public void write() {
    	authorityText.setText((String) salesman.get("auktoritet"));
    	userText.setText((String) salesman.get("anv�ndarnamn"));
    	nameText.setText((String) salesman.get("anv�ndarnamn"));
    }
    
    @FXML
    void initialize() {    	
    	statusBarChart.getData().addAll(CreateCharts.makBySalesman, CreateCharts.billedBySalesman,
				CreateCharts.customersBySalesman, CreateCharts.orderBySalesman);
    	
    	System.out.println("DATA SIZE AT SALESMANPANE: " + AdminController.series.getData().size());
    	salesLineChart.getData().add(AdminController.series);
    	
    	if(userRequest) {userSettings.setVisible(false);}
    	else {userSettings.setVisible(true);}

    	currentAuth = AdminController.authority.toString();
    }
}