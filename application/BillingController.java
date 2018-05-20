package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class BillingController implements NewSaleListener{
	
	private static List<String[]> orderList = new ArrayList<>();
	public static JSONObject allOrders;
		
	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane mainList;
    
    public void addOrder(JSONObject salesData) {
    	String[] customer = {(String) salesData.get("Kundnummer"),
    							(String) salesData.get("Namn"),
    							(String) salesData.get("Adress"),
    							(String) salesData.get("Postnummer"),
    							(String) salesData.get("Tjänst"),
    							(String) salesData.get("Kundnummer"),
    							(String) salesData.get("Tjänst"),
    							(String) salesData.get("Kundpris").toString(),};
    	orderList.add(customer);
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderPane.fxml"));
	   	AnchorPane root;
		try {
			root = loader.load();
			// Lägg till root till customerSearchPane
		   	mainList.getChildren().add(root);
		   	mainList.setPrefHeight(500);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
	@Override
	public void newSale() {
		
	}

	@Override
	public void newMessage() {
		
	}

	@Override
	public void newOrder(JSONObject customer) {
		System.out.println("NEW ORDER\n");
		System.out.println("CUSTOMER: " + customer.toJSONString());
		addOrder(customer);
	}
    
	@Override
	public void statusUpdate() {
		initialize();
	}
	
    private void show() {
    	int i = 0;
    	for(Object id : AdminController.orderData.keySet()) {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderPane.fxml"));
		   	GridPane root;
			try {
				System.out.println(id);
				root = loader.load();
				OrderController c = loader.getController();
				c.customer = (JSONObject) AdminController.orderData.get(id);
				c.write();
				// Lägg till root till customerSearchPane
			   	root.setLayoutY(31*(i + 1));
				mainList.getChildren().add(root);
			   	i += 1;
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    @FXML
    void initialize() {
        
    	mainList.getChildren().clear();
    	
    	assert mainList != null : "fx:id=\"mainList\" was not injected: check your FXML file 'Billing.fxml'.";
        System.out.println("ÖPPNA BILLING");
        System.out.println(AdminController.orderData.toJSONString());
        show();
        AdminController.listener.addListener(this);        
        
    }
}
