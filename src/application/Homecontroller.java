package application;
import javafx.event.ActionEvent; //handles button clicks or other actions
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.application.HostServices;

public class Homecontroller {
	private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
    @FXML
    private Button startPlanningButton;

    @FXML
    void handleStartPlanning(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/MainMenu.fxml"));
            Parent root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setHostServices(hostServices);  // pass HostServices to MainMenu

            Scene scene = new Scene(root);
            scene.setCursor(Cursor.HAND);
            stage.setScene(scene);
            stage.setTitle("Factrip - Main Menu");
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}