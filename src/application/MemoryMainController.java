package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.util.List;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;




public class MemoryMainController {

    @FXML private AnchorPane anchorPane;
    @FXML private CheckBox selectAll;
    @FXML private Button newEntry, delete;
    @FXML private VBox listOfEntries;
    @FXML private ScrollPane scrollPane;


    private ObservableList<MemoryEntry> entries = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        DatabaseConnector.initializeDatabase();

        // VBox settings
        listOfEntries.setSpacing(20);   // space between entry HBoxes
        listOfEntries.setPadding(new Insets(15)); // padding inside VBox
        listOfEntries.setStyle(
            "-fx-background-color: #F0ECE5;" // slightly darker than ScrollPane
        );

        // ScrollPane background
        scrollPane.setStyle(
            "-fx-background: #E8E3D6;" +            // viewport background
            "-fx-background-color: #E8E3D6;"        // content background
        );

        reloadEntriesFromDB();
    }


    // Fetch from DB and update UI
    public void reloadEntriesFromDB() {
        entries.clear();
        entries.addAll(DatabaseConnector.getAllMemoryEntries()); // fetch from DB
        loadEntriesToUI();
    }
 // Utility method to remove HTML tags
    private String stripHtml(String html) {
        if (html == null) return "";
        return html.replaceAll("\\<.*?\\>", "");
    }


    // Populate the VBox with HBoxes for each entry
    private void loadEntriesToUI() {
        listOfEntries.getChildren().clear();
        for (MemoryEntry entry : entries) {
            HBox entryBox = createEntryBox(entry);
            listOfEntries.getChildren().add(entryBox);
        }
    }
    private void openViewEntry(MemoryEntry entry) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("viewEntry.fxml"));
            Parent root = loader.load();

            // Get the controller of the viewEntry.fxml
            ViewEntryController controller = loader.getController();
            controller.setEntry(entry); // Pass the selected entry

            // Open in a new window
            Stage stage = new Stage();
            stage.setTitle("View/Edit Entry");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Wait until the window is closed

            // Refresh the list after possible edits
            reloadEntriesFromDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private HBox createEntryBox(MemoryEntry entry) {
        HBox box = new HBox(20);
        box.setMinHeight(120);
        box.setSpacing(15);
        box.setPadding(new Insets(15));
        box.setStyle(
            "-fx-background-color: #E8E4D9;" +  // slightly darker than scrollpane
            "-fx-border-color: #C0B8A2;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;"
        );

        // CheckBox styling
        CheckBox cb = new CheckBox();
        cb.setSelected(entry.isSelected());
        cb.setOnAction(e -> entry.setSelected(cb.isSelected()));
        cb.setStyle(
            "-fx-scale-x: 1.4;" +      // slightly bigger
            "-fx-scale-y: 1.4;" +
            "-fx-accent: #800000;"     // maroon checkbox when selected
        );

        // Text labels
        Label title = new Label(entry.getTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: #4B2E2E;"); // maroon-ish text
        title.setOnMouseClicked(e -> openViewEntry(entry));

        Label detail = new Label(stripHtml(entry.getContent()));
        detail.setWrapText(true);
        detail.setMaxWidth(400);
        detail.setStyle("-fx-font-size: 14; -fx-text-fill: #333333;");
        detail.setOnMouseClicked(e -> openViewEntry(entry));

        Label dateLabel = new Label(entry.getDate());
        dateLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #7D7D7D;");

        // Inner VBox for text with background
        VBox textBox = new VBox(8, title, detail, dateLabel);
        textBox.setPadding(new Insets(10));
        textBox.setStyle(
            "-fx-background-color: #E8E4D9;" + // darker than scrollpane, lighter than box
            "-fx-background-radius: 10;"
        );

        box.getChildren().addAll(cb, textBox);
        return box;
    }




    @FXML
    private void selectAllEntries() {
        boolean select = selectAll.isSelected();
        for (MemoryEntry entry : entries) entry.setSelected(select);
        loadEntriesToUI();
    }

    @FXML
    private void AddNewEntry() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddEntry.fxml"));
            Parent root = loader.load();

            AddEntryController controller = loader.getController();
            controller.setEntries(entries);
            controller.setMainController(this); // important: pass main controller

            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void DeleteSelectedEntry() {
        try {
            // remove from database
            for (MemoryEntry entry : entries) {
                if (entry.isSelected()) {
                    DatabaseConnector.deleteMemoryEntry(entry.getId());
                }
            }
            // remove from in-memory list
            entries.removeIf(MemoryEntry::isSelected);
            loadEntriesToUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void selectSpecificEntry(ActionEvent event) {
        // TODO: implement what should happen when the button is clicked
        System.out.println("Select button clicked!");
    }

    @FXML
    private void handleBack(ActionEvent event) {
        // Close the current stage (window)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    private void mainMenuOn(ActionEvent event) {
        try {
            // Load your main menu FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the main menu scene
            stage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  


}