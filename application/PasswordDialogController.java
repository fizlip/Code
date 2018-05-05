package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

public class PasswordDialogController {
	
	public static StringProperty errorText = new SimpleStringProperty("");
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private PasswordField oldPassword;

    @FXML
    private PasswordField newPassword;

    @FXML
    private PasswordField passwordConf;

    @FXML
    private Button finish;
    
    @FXML
    private Text ErrorText;
    
    @FXML
    void newPassword(ActionEvent event) {
    	if(oldPassword.getText().length() == 0 ||
    			newPassword.getText().length() == 0 ||
    			passwordConf.getText().length() == 0) {
    		errorText.set("Något fält är tomt");
    		return;
    	}
    	if(!newPassword.getText().equals(passwordConf.getText())) {
    		errorText.set("Bekräfade lösenordet matchar\ninte det nya lösenordet");
    		return;
    	}
    	try {
			UsecController.connection.send(UsecController.user.get() + " - AUTH: " + "NEW" + ": " + oldPassword.getText() + ";" + newPassword.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }

    @FXML
    void initialize() {
    	ErrorText.textProperty().bind(errorText);
    }
}