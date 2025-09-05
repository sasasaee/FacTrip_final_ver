package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
	        Parent root = loader.load();

	        // Pass HostServices to MainMenuController
	        Homecontroller controller = loader.getController();
	        controller.setHostServices(getHostServices());

	        Scene scene = new Scene(root);
	        scene.setCursor(Cursor.HAND);
	        primaryStage.setScene(scene);
	        primaryStage.setTitle("Factrip - Plan explore and preserve");
	        primaryStage.setMaximized(true);
	        primaryStage.show();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	}

	
	public static void main(String[] args) {
		launch(args);
	}
}