package app;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.math.NumberUtils;
import org.json.simple.JSONObject;

import helper.Customer;
import helper.Register;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CustomersTableController {

	public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Customer> allCustomersTable;
    
    private void addCustomer(JSONObject customer) {
    	Customer individual = new Customer();
    	Object[] fields = customer.keySet().toArray();
    	for(Object f : fields) {
	    	String field = f.toString();
			individual.updateText(
					field, customer.get(field).toString());
		}
		allCustomers.add(individual);
    }
    
    
    private void createAllcustomersTable() {
    	double totPrice = 0;
    	int totAmountSold = 0;
    	Double counter = 0.0;
    	//Get all customers
    	JSONObject salesData = Register.get("kunder");
    	Object[] salesPeople = salesData.keySet().toArray();
    	for(Object n : salesPeople) {
    		String name = n.toString();
    		try {
	    		JSONObject customers = (JSONObject) salesData.get(name);
	    		Object[] ids = customers.keySet().toArray();
	    		for(Object i : ids) {
	    			String id = i.toString();
	    			
	    			JSONObject cust = (JSONObject) customers.get(id);
	    			totAmountSold += 1;
	    			try {
		    			Double customerPrice = Double.parseDouble(cust.get("Kundpris").toString().replace(',', '.'));
			    		XYChart.Data<Double, Double> input = new XYChart.Data<>(counter, customerPrice);
			    		Rectangle rect = new Rectangle(0, 0);
			    		rect.setVisible(false);
			    		input.setNode(rect);
			    		
						String salaryString = cust.get("FSG (SEK)").toString().replace(',', '.');
			    		if(NumberUtils.isCreatable(salaryString) && cust.get("Status").equals("Kund")) {
							Double added = Double.parseDouble(salaryString);
							totPrice += added;
						}			    		
	    			}catch(NumberFormatException e) {System.out.println("Format exception: " + cust.get("Kundpris").toString());}
	    			
	    			
	    	    	//Initialize text
	    	    	//totSoldText.setText(Integer.toString(totalAmountSold));
		    		
		    		counter += 1;
		    		addCustomer(cust);
	    		}
	    		//Add sales of salesmen to barchart
    		}catch(NullPointerException e) {}
    	}
    	
    	allCustomersTable.setEditable(true);
    	
    	TableColumn<Customer, String> IDCol = new TableColumn<>("Personnummer");
    	IDCol.setCellValueFactory(new PropertyValueFactory<>("personalIDText"));
    	IDCol.setMinWidth(100);
    	
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
    	
    	allCustomersTable.setItems(allCustomers);
    	allCustomersTable.getColumns().addAll(IDCol, nameCol, custPriceCol, postalCodeCol, stateCol, 
    											adressCol, telephoneCol, custNumber, badgeNumberCol, 
    											mak, statusCol);
    }
    
    
    

    @FXML
    void initialize() {
        assert allCustomersTable != null : "fx:id=\"allCustomersTable\" was not injected: check your FXML file 'AllCustomersTable.fxml'.";
        createAllcustomersTable();
    }
}