package application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;
import javafx.application.HostServices;

public class MainMenuController {
	private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
	
	@FXML
	void handleExploreFacts(ActionEvent event) {
	    try {
	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/ExploreFacts.fxml"));
	        Parent root = loader.load();

	        // Pass hostServices to ExploreFactsController
	        ExploreFactsController controller = loader.getController();
	        controller.setHostServices(hostServices);

	        Scene scene = new Scene(root);
	        scene.setCursor(Cursor.HAND);
	        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	        stage.setScene(scene);
	        stage.setTitle("Explore Fun Facts");
	        stage.setMaximized(true);
	        stage.show();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

    @FXML
    void handlePlanTrip(ActionEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/planner.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Plan your Trip");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private Button memoEntry;

    @FXML
    private void handleMemoryEntry(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/memoryMain.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Memory Entry");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
