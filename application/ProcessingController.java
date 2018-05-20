package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

public class ProcessingController {

	public static StringProperty loadProp = new SimpleStringProperty("");
	public static DoubleProperty progressProp = new SimpleDoubleProperty(0.0);
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text loadText;

    @FXML
    private ProgressIndicator loadingProgress;
    
    public static void setProgress(Double p) {
    }
    
    @FXML
    void initialize() {
        assert loadText != null : "fx:id=\"loadText\" was not injected: check your FXML file 'ProcessingWindow.fxml'.";
        loadText.textProperty().bind(loadProp);
        loadingProgress.progressProperty().bind(progressProp);
        

    }
}
