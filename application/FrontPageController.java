package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import java.io.*;

public class FrontPageController {

	public static StringProperty uTitle1Prop = new SimpleStringProperty("titel");
	public static StringProperty uTitle2Prop = new SimpleStringProperty("titel");
	public static StringProperty uTitle3Prop = new SimpleStringProperty("titel");
	public static StringProperty uTitle4Prop = new SimpleStringProperty("titel");
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView titelImage;

    @FXML
    private Text titleText;
    
    @FXML
    private Text uTitle1;
    
    @FXML
    private Text uTitle2;
    
    @FXML
    private Text uTitle3;
    
    @FXML
    private Text uTitle4;

    @FXML
    void initialize() {
        assert titelImage != null : "fx:id=\"titelImage\" was not injected: check your FXML file 'FronPage.fxml'.";
        assert titleText != null : "fx:id=\"titleText\" was not injected: check your FXML file 'FronPage.fxml'.";
        titleText.setText(AdminController.title);
        FileInputStream file;
		try {
			file = new FileInputStream(AdminController.titleImage);
			Image image = new Image(file);
	        titelImage.setImage(image);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		uTitle1.textProperty().bind(uTitle1Prop);
		uTitle2.textProperty().bind(uTitle2Prop);
		uTitle3.textProperty().bind(uTitle3Prop);
		uTitle4.textProperty().bind(uTitle4Prop);
    }
}