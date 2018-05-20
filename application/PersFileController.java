package application;

import java.net.URL;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class PersFileController {

	public JSONObject personData;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea personFile;

    @FXML
    private Text updateText;

    @FXML
    private Text personText;

    @FXML
    void mouseEnterUpdate(MouseEvent event) {
    	updateText.setUnderline(true);
    }

    @FXML
    void mouseExitUpdate(MouseEvent event) {
    	updateText.setUnderline(false);
    }

    @FXML
    void updateAction(MouseEvent event) {
    	if(updateText.getText().equals("Uppdatera")) {
	    	personText.setVisible(false);
	    	personFile.setVisible(true);
	    	personFile.toFront();
	    	updateText.setText("Klar");
    	}
    	else if(updateText.getText().equals("Klar")) {
	    	personText.setText(personFile.getText());
    		personText.setVisible(true);
	    	personFile.setVisible(false);
	    	updateText.setText("Uppdatera");
    	}
    }

    @FXML
    void initialize() {
        assert personFile != null : "fx:id=\"personFile\" was not injected: check your FXML file 'persFile.fxml'.";
        assert updateText != null : "fx:id=\"updateText\" was not injected: check your FXML file 'persFile.fxml'.";
        assert personText != null : "fx:id=\"personText\" was not injected: check your FXML file 'persFile.fxml'.";

    }
}