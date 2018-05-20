package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NewSalesman {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private Text errorText;
    
    @FXML
    private TextField nameField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confPasswordfield;

    @FXML
    private Button finishedButton;

    @FXML
    void buttonEnter(MouseEvent event) {
    	Button b = (Button) event.getSource();
    	b.setStyle("-fx-background-color: white; -fx-border-color: dodgerblue; -fx-border-width: 1px;");
    }

    @FXML
    void buttonExit(MouseEvent event) {
    	Button b = (Button) event.getSource();
    	b.setStyle("-fx-background-color: white; -fx-border-color: gray; -fx-border-width: 1px;");
    }
    
    @FXML
    void finishedAction(ActionEvent event) {
    	if(nameField.getText().equals("") || usernameField.getText().equals("") ||
    			passwordField.getText().equals("") || confPasswordfield.getText().equals("")) {
    		errorText.setText("Något fält är tomt.");
    		return;
    	}
    	if(!passwordField.getText().equals(confPasswordfield.getText())) {
    		errorText.setText("Lösenorden matchar inte");
    		return;
    	}
    	try {
			AdminController.connection.send(AdminController.user.get() + " - NEW: salesman: " + nameField.getText() + ";" 
											+ usernameField.getText() + ";" + passwordField.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	Stage stage = (Stage) finishedButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    
    @FXML
    void initialize() {

    }
}