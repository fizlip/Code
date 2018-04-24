package app;

import java.io.IOException;



import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class HomeScreenController{

	
	@FXML
    private ResourceBundle resources;
	
	@FXML
	private AnchorPane menuPane;

    @FXML
    private URL location;

	
    @FXML
    private Text soldTodayText;

    @FXML
    private LineChart<String, Number> salesChart;

    @FXML
    private Text serverText;
    
    @FXML
    private Text serverCom;

    @FXML
    private TextField serverComField;
    
    @FXML
    void sendMessage(KeyEvent event) {
    	if(event.getCode().equals(KeyCode.ENTER)) {
    		send(serverComField.getText());
    		serverComField.clear();
    	}
    }

    protected void send(String message) {
		ChatController.arrived = false;
		chatAddMessage(message);
		
		try {
			AdminController.connection.send(message);
		} catch (Exception e) {
			AdminController.serverCom.set("Anslutning misslyckades");
			e.printStackTrace();
		}
    }

    
    protected void addSale() {
    	try {
    		salesChart.getData().add(CreateCharts.salesSeries);
    		send("Nytt Sälj");
    	}
    	catch(IllegalArgumentException e) {
    		System.out.println("Double");
    	}
    }

    protected void chatAddMessage(String message) {
    	try {
    		
    		ChatController.type = "m";
    		
	    	AdminController.currentMessage = message;
	    	FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("ChatPane.fxml"));
			AnchorPane chat = chatLoader.load();
			
			chat.setLayoutX(288);
			chat.setLayoutY(90 + ChatController.offset);
			ChatController.offset += chat.getPrefHeight();
			ChatController.offset += 5;
			menuPane.getChildren().add(chat);
			
    	}catch(IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void initialize() {
    	soldTodayText.textProperty().bind(AdminController.amountSoldToday);
    	serverText.textProperty().bind(AdminController.serverCom);
    	salesChart.getData().add(CreateCharts.salesSeries);
    }

}