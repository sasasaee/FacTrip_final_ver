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

/**
 * Controller class for the application's Main Menu screen.
 *
 * Responsibilities:
 * - Handles navigation to different features: Explore Facts, Plan Trip, and Memory Entry.
 * - Passes HostServices (for opening links) to sub-controllers when needed.
 */

public class MainMenuController {
	private HostServices hostServices; // Reference to HostServices (provided by JavaFX Application)
	
	/**
     * Setter to allow the main Application to pass in HostServices.
     * HostServices is needed for features like opening links in the browser.
     * 
     * @param hostServices the HostServices instance provided by Application
     */

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;  
    }
	
    /**
     * Navigates to the "Explore Facts" screen.
     * Passes HostServices to ExploreFactsController.
     * 
     * @param event ActionEvent triggered by button click
     */
    
	@FXML
	void handleExploreFacts(ActionEvent event) {
	    try {
            // Get current stage from the event source

	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load ExploreFacts.fxml
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
	
	/**
     * Navigates to the "Plan Trip" screen.
     * Loads planner.fxml.
     * 
     * @param event ActionEvent triggered by button click
     */

    @FXML
    void handlePlanTrip(ActionEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/planner.fxml"));
            Parent root = loader.load();

            // Replace current scene with Planner
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Plan your Trip");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private Button memoEntry; // Button to access Memory Entry screen

    /**
     * Navigates to the "Memory Entry" screen.
     * Loads memoryMain.fxml.
     * 
     * @param event ActionEvent triggered by button click
     */
    @FXML
    private void handleMemoryEntry(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/memoryMain.fxml"));
            Parent root = loader.load();

            // Replace current scene with Memory Entry
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Memory Entry");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
