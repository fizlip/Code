package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.util.SystemOutLogger;
import org.json.simple.JSONObject;

import helper.Customer;
import helper.Register;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CustomersTableController {

	public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
	
	public static ObservableList<Customer> custs = FXCollections.observableArrayList();
	public static ObservableList<Customer> maks = FXCollections.observableArrayList();
	public static ObservableList<Customer> faks = FXCollections.observableArrayList();
	public static ObservableList<Customer> orders = FXCollections.observableArrayList();
	
	public static JSONObject custData;
	public static JSONObject fakData;
	public static  JSONObject orderData;
	public static JSONObject makData;
	
	public static JSONObject customerData;
	public static boolean keepRunning = false;
	public static List<String> openedCustomers = new ArrayList<>();
	private static Stage customerStage;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Customer> allCustomersTable;
    
    @FXML
    private TableView<Customer> orderTable;

    @FXML
    private TableView<Customer> fakTable;

    @FXML
    private TableView<Customer> custTable;
    
    private void addCustomer(JSONObject customer, ObservableList<Customer> list) {
    	Customer individual = new Customer();
    	Object[] fields = customer.keySet().toArray();
    	for(Object f : fields) {
	    	String field = f.toString();
			individual.updateText(
					field, customer.get(field).toString());
		}
		list.add(individual);
    }
    
    public static void updateData(JSONObject data) {
    	customerData = data;
    	
    	custData = (JSONObject) customerData.get("kund");
    	fakData = (JSONObject) customerData.get("fakturerad");
    	orderData = (JSONObject) customerData.get("order");
    	makData = (JSONObject) customerData.get("makulerad");
    }
    
    private void addCustomers(JSONObject data, ObservableList<Customer> list) {
    	for(Object id : data.keySet()) {
    		JSONObject customer = (JSONObject) data.get(id);
    		addCustomer(customer, list);
    	}
    }
    
    private void createAllcustomersTable(boolean salesDashboard) {
    	//Get all customers    
    	
		addCustomers(custData, custs);
		addCustomers(fakData, faks);
		addCustomers(orderData, orders);
		addCustomers(makData, maks);	
    	
		/*
    	allCustomersTable.setEditable(true);
    	orderTable.setEditable(true);
    	custTable.setEditable(true);
    	//makTable.setEditable(true);
    	fakTable.setEditable(true);
    	*/
    	TableColumn<Customer, String> IDColFak = new TableColumn<>("Personnummer");
    	IDColFak.setCellValueFactory(new PropertyValueFactory<>("personalIDText"));
    	IDColFak.setMinWidth(100);
    	TableColumn<Customer, String> IDColCust = new TableColumn<>("Personnummer");
    	IDColCust.setCellValueFactory(new PropertyValueFactory<>("personalIDText"));
    	IDColCust.setMinWidth(100);
    	TableColumn<Customer, String> IDColMak = new TableColumn<>("Personnummer");
    	IDColMak.setCellValueFactory(new PropertyValueFactory<>("personalIDText"));
    	IDColMak.setMinWidth(100);
    	TableColumn<Customer, String> IDColOrder = new TableColumn<>("Personnummer");
    	IDColOrder.setCellValueFactory(new PropertyValueFactory<>("personalIDText"));
    	IDColOrder.setMinWidth(100);
    	
    	TableColumn<Customer, String> nameCol = new TableColumn<>("Namn");
    	nameCol.setCellValueFactory(new PropertyValueFactory<>("nameText"));
    	nameCol.setMinWidth(175);
    	
    	TableColumn<Customer, String> custPriceCol = new TableColumn<>("Kundpris");
    	custPriceCol.setCellValueFactory(new PropertyValueFactory<>("customerPriceText"));
    	custPriceCol.setMinWidth(100);
    	
    	TableColumn<Customer, String> stateCol = new TableColumn<>("Ort");
    	stateCol.setCellValueFactory(new PropertyValueFactory<>("stateText"));
    	stateCol.setMinWidth(100);
    	
    	TableColumn<Customer, String> postalCodeCol = new TableColumn<>("Postnummer");
    	postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCodeText"));
    	stateCol.setMinWidth(100);
    	
    	TableColumn<Customer, String> adressCol = new TableColumn<>("Adress");
    	adressCol.setCellValueFactory(new PropertyValueFactory<>("adressText"));
    	adressCol.setMinWidth(200);

    	
    	TableColumn<Customer, String> telephoneCol = new TableColumn<>("Telefon");
    	telephoneCol.setCellValueFactory(new PropertyValueFactory<>("telephoneText"));
    	telephoneCol.setMinWidth(100);
    	
    	TableColumn<Customer, String> badgeNumber = new TableColumn<>("Bricknummer");
    	badgeNumber.setCellValueFactory(new PropertyValueFactory<>("badgeNumberText"));
    	badgeNumber.setMinWidth(100); 
    	
    	TableColumn<Customer, String> mak = new TableColumn<>("Mak");
    	mak.setCellValueFactory(new PropertyValueFactory<>("makText"));
    	mak.setCellFactory(column -> {
    		return new TableCell<Customer, String>(){
    			@Override
    			protected void updateItem(String item, boolean empty) {
    				super.updateItem(item, empty);
    				if(item == null || empty) {
    					setText(null);
    					setStyle("");
    				}
    				else {
    					try {
	    					if(Double.parseDouble(item.replace(',', '.').trim()) > 0) {
	    						setText(item);
		    					setTextFill(Color.WHITE);
		    					setStyle("-fx-background-color: FF4141");
	    					}
	    					else {
	    						setText(item);
		    					setTextFill(Color.BLACK);
		    					setStyle("");
	    					}
    					}catch(NumberFormatException e) {setText("FEL I INPUT");}
    				allCustomersTable.requestFocus();
    				}
    			}
    		};
    	});
    	mak.setMinWidth(50);
    	
    	TableColumn<Customer, String> custNumber = new TableColumn<>("Kundnummer");
    	custNumber.setCellValueFactory(new PropertyValueFactory<>("customerNumberText"));
    	custNumber.setMinWidth(100);
    	
    	TableColumn<Customer, String> badgeNumberCol = new TableColumn<>("Bricknummer");
    	badgeNumberCol.setCellValueFactory(new PropertyValueFactory<>("badgeNumberText"));
    	badgeNumberCol.setMinWidth(100);
    	
    	TableColumn<Customer, String> statusCol = new TableColumn<>("Status");
    	statusCol.setCellValueFactory(new PropertyValueFactory<>("statusText"));
    	statusCol.setCellFactory(column -> {
    		return new TableCell<Customer, String>(){
    			@Override
    			protected void updateItem(String item, boolean empty) {
    				super.updateItem(item, empty);
    				if(item == null || empty) {
    					setText(null);
    					setStyle("");
    				}
    				else {
    					try {
	    					if(item.equals("Fakturerad")) {
		    					setText(item);setTextFill(Color.BLACK);setStyle("-fx-background-color: #FFFA00");
	    					}
	    					else if(item.equals("Kund")) {
		    					setText(item);setTextFill(Color.BLACK);setStyle("-fx-background-color: #00CE0D");
	    					}
	    					else if(item.equals("Makulerad")) {
		    					setText(item);setTextFill(Color.WHITE);setStyle("-fx-background-color: #FF0700");
	    					}
	    					else if(item.equals("Order")) {
		    					setText(item);setTextFill(Color.WHITE);setStyle("-fx-background-color: #6B00B3");
	    					}
	    					else {
	    						setText(item);setTextFill(Color.BLACK);setStyle("");
	    					}
    					}catch(NumberFormatException e) {setText("FEL I INPUT");}
    				allCustomersTable.requestFocus();
    				}
    			}
    		};
    	});
    	//totalSalesLineChart.getData().add(totalSalesSeries);
    	statusCol.setMinWidth(75);
    	
    	
    	//allCustomersTable.setItems(allCustomers);
    	
    	
    	fakTable.setItems(faks);
    	fakTable.getColumns().addAll(IDColFak, adressColFak, custNumberFak);
    	orderTable.setItems(orders);
    	orderTable.getColumns().addAll(IDCol, adressCol, custNumber);
    	//makTable.setItems(maks);
    	custTable.setItems(custs);
    	custTable.getColumns().addAll(IDCol, adressCol, custNumber);
    }
    
    
    private void openCustomerWindow(JSONObject customer) {
    	try {
    		customerStage = new Stage();
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerWindow.fxml"));
		   	GridPane root = loader.load();
    		
    		Scene scene = new Scene(root, 520, 615);
    		CustomerWindowController con = loader.getController();
    		con.customer = customer;
    		con.initWindow(customer);
    		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    		customerStage.setTitle("Kundfönster");
    		customerStage.setScene(scene);
    		customerStage.setAlwaysOnTop(true);
    		customerStage.show();
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void getSelectedCustomer(MouseEvent event) {
    	//getSelected(makTable);
    	getSelected(orderTable, orderData);
    	getSelected(fakTable, fakData);
    	getSelected(custTable, custData);
    }
    
    private void getSelected(TableView<Customer> table, JSONObject data){
    	for(Customer cust : table.getSelectionModel().getSelectedItems()) {
			JSONObject customer = (JSONObject) data.get(cust.customerNumberTextProperty().get());
			openCustomerWindow(customer);
		}
    }
    
    @FXML
    void enterGetSelected(KeyEvent event) {
    	if(event.getCode().equals(KeyCode.ENTER)) {
    		//enterSelected(makTable);
    		enterSelected(orderTable, orderData);
    		enterSelected(fakTable, fakData);
    		enterSelected(custTable, custData);
    	}
    	if(event.getCode().equals(KeyCode.ESCAPE)) {
			customerStage.close();
			System.out.println("close");
    	}
    }
    
    private void enterSelected(TableView<Customer> table, JSONObject data) {
    	for(Customer cust : table.getSelectionModel().getSelectedItems()) {
			JSONObject customer = (JSONObject) data.get(cust.customerNumberTextProperty().get());
			openCustomerWindow(customer);
			table.requestFocus();
		}
    }
    
    @FXML
    void initialize() {
        
    	assert allCustomersTable != null : "fx:id=\"allCustomersTable\" was not injected: check your FXML file 'AllCustomersTable.fxml'.";        
        
    	createAllcustomersTable(UsecController.isActive);

    }
}