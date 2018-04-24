package app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.AnchorPane;

public class ChatController {

	public static double offset = 50;
	
	public static String currentMessage;
	public static boolean arrived = false;
	public static String type;

	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text nameText;

    @FXML
    private Text messageText;
    
    @FXML
    private AnchorPane messageBg;
    
    public void initNewMessage(String name, String message) {
    	nameText.setText(name);
    	messageText.setText(message);
    }
    
    
    @FXML
    void initialize() {
        assert nameText != null : "fx:id=\"nameText\" was not injected: check your FXML file 'ChatPane.fxml'.";
        assert messageText != null : "fx:id=\"messageText\" was not injected: check your FXML file 'ChatPane.fxml'.";
        messageText.setText(AdminController.currentMessage);
        if(!arrived) {
    		messageText.setTextAlignment(TextAlignment.RIGHT);
    	}
        if(arrived) {
    		messageText.setTextAlignment(TextAlignment.LEFT);
    	}
        if(type.equals("s")){
        	messageBg.setStyle("-fx-background-color: #e6ffe5");
        }
        else {
        	messageBg.setStyle("-fx-background-color: #eff6ff");
        }
    }
}