package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import org.json.simple.parser.ParseException;

import helper.Register;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {	
	@Override
	public void start(Stage primaryStage) {
		try {
			//AnchorPane root = FXMLLoader.lofad(getClass().getResource("UsecurityDasboard.fxml"));
			AnchorPane root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
			Scene scene = new Scene(root,400,170);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Logga in");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest(event -> {
			    System.out.println("CLOSE WINDOW");
				AdminController.teminateThread = true;
			    CreateCharts.terminateChartThread = true;
			    AdminController.stopRegistration = true;
			    AdminController.stopSalesmanThread = true;
			    UsecController.terminateRegistrationThread = true;
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
