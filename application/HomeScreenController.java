package application;

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
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ScrollPane;

public class HomeScreenController implements NewSaleListener{

	
	
	@FXML
    private ResourceBundle resources;
	
	@FXML
    private AnchorPane messagePane;
	
	@FXML
    private ScrollPane messageScrollPane;
	
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
    		String[] m = {serverComField.getText(), "m"};
    		AdminController.messages.add(m);
    		send(serverComField.getText());
    		serverComField.clear();
    	}
    }

    @Override
    public void newMessage() {
    	System.out.println(ChatController.type);
    	chatAddMessage(AdminController.currentMessage, ChatController.type);
    }
    
    @Override
	public void newSale() {
    	try {
    		ChatController.type = "s";
    		
	    	FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("ChatPane.fxml"));
			AnchorPane chat = chatLoader.load();
			chat.setLayoutX(0);
			chat.setLayoutY(-50 + ChatController.offset);
			ChatController.offset += chat.getPrefHeight();
			ChatController.offset += 5;
			messagePane.getChildren().add(chat);
			if(chat.getLayoutY() > messagePane.getHeight()) {
				messagePane.setPrefHeight(messagePane.getHeight() + chat.getPrefHeight() + 5);
				messagePane.setPrefWidth(990);
				messageScrollPane.setVvalue(1);
			}
			
    	}catch(IOException e) {
			System.out.println("ERROR IN LOADING CHATPANE");;
		}
	}
    
    
    protected void send(String message) {
		ChatController.arrived = false;
		chatAddMessage(message, "m");
		
		try {
			AdminController.connection.send(message);
			System.out.println(message);
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

    public void chatAddMessage(String message, String type) {
    	try {
    		double height = messagePane.getPrefHeight();
    		AdminController.currentMessage = message;
    		ChatController.type = type;
    		
	    	FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("ChatPane.fxml"));
			AnchorPane chat = chatLoader.load();
			chat.setLayoutX(0);
			chat.setLayoutY(-50 + ChatController.offset);
			ChatController.offset += chat.getPrefHeight();
			ChatController.offset += 5;
			messagePane.getChildren().add(chat);
			if(chat.getLayoutY() > messagePane.getHeight()) {
				height += chat.getPrefHeight() + 5;
				messagePane.setPrefHeight(height);
				messagePane.setPrefWidth(990);
				messageScrollPane.setVvalue(1);
			}
			
    	}catch(IOException e) {
			System.out.println("ERROR IN LOADING CHATPANE");;
		}
    }
    
    @FXML
    void initialize() {
    	soldTodayText.textProperty().bind(AdminController.amountSoldToday);
    	serverText.textProperty().bind(AdminController.serverCom);
    	salesChart.getData().add(CreateCharts.salesSeries);
    	AdminController.listener.addListener(this);
    	ChatController.offset = 50;
    	messagePane.setPrefHeight(259);
    	messagePane.getChildren().clear();
    	for(String[] message : AdminController.messages) {
    		chatAddMessage(message[0], message[1]);
    	}
    }

}