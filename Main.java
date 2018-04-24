package app;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//AnchorPane root = FXMLLoader.lofad(getClass().getResource("UsecurityDasboard.fxml"));
			AnchorPane root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
			Scene scene = new Scene(root,1290,650);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Logga in");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
