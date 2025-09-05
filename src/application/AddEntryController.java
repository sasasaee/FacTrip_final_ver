package application;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;


public class AddEntryController {

    @FXML private TextField addTitle;
    @FXML private HTMLEditor addNewEntryhtml;
    @FXML private Button selectImageButton;
    @FXML private Button saveButton;
    @FXML private Button goBackButton;
    @FXML private DatePicker addDate; // now a DatePicker


    private ObservableList<MemoryEntry> entries;
    private MemoryMainController mainController; // reference to main controller
    private String selectedImagePath = "";

    @FXML
    public void initialize() {
        // Wait until the HTMLEditor is added to a scene
        addNewEntryhtml.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    switch (event.getCode()) {
                        case S -> {
                            if (event.isControlDown()) {
                                handleSave(); // call your save method
                                event.consume(); // prevent default behavior
                            }
                        }
                    }
                });
            }
        });
    }

    
    // Called from MemoryMainController to pass the entries list
    public void setEntries(ObservableList<MemoryEntry> entries) {
        this.entries = entries;
    }

    // Called from MemoryMainController to pass itself
    public void setMainController(MemoryMainController controller) {
        this.mainController = controller;
    }

    @FXML
    private void addPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(addNewEntryhtml.getScene().getWindow());
        if (selectedFile != null) {
            try {
                byte[] fileContent = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                String base64Image = java.util.Base64.getEncoder().encodeToString(fileContent);
                String extension = "";

                String fileName = selectedFile.getName().toLowerCase();
                if (fileName.endsWith(".png")) extension = "png";
                else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) extension = "jpeg";
                else if (fileName.endsWith(".gif")) extension = "gif";

                String imgTag = "<img src='data:image/" + extension + ";base64," + base64Image + "' width='300'/>";

                // Append to existing HTML
                String currentHtml = addNewEntryhtml.getHtmlText();
                currentHtml = currentHtml.replace("</body>", imgTag + "</body>");
                addNewEntryhtml.setHtmlText(currentHtml);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showSaveConfirmation(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Save Successful");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    private void handleSave() {
        String title = addTitle.getText().trim();
        String content = addNewEntryhtml.getHtmlText();

        // Get date from DatePicker
        LocalDate selectedDate = addDate.getValue();
        
        if (selectedDate == null) {
            selectedDate = LocalDate.now(); // fallback if user didn't pick a date
        }

        if ((title == null || title.isEmpty()) && (content == null || content.isEmpty())) {
            System.out.println("Nothing entered, skipping save.");
            return;
        }

        try {
            // 1️⃣ Create MemoryEntry with LocalDate
            MemoryEntry newEntry = new MemoryEntry(0, title, content, selectedDate, selectedImagePath);

            // 2️⃣ Save to database
            DatabaseConnector.insertMemoryEntry(newEntry);

            // 3️⃣ Add to in-memory list
            if (entries != null) entries.add(newEntry);

            System.out.println("Saved entry to database: " + title);

            // 4️⃣ Refresh main controller's list
            if (mainController != null) {
                mainController.reloadEntriesFromDB();
            }
            
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Save Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your entry has been saved successfully!");
            alert.showAndWait();

            // 5️⃣ Close window
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void goBacktoMainMemory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("memoryMain.fxml"));
            Parent root = loader.load();

            // Get current stage from the event source instead of goBackButton
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}