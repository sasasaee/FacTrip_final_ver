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

/**
 * Controller class for the "Add Entry" screen of the memory application.
 * 
 * Responsibilities:
 * - Handles input of title, content, date, and optional image for a memory entry.
 * - Allows users to insert images into the HTML editor.
 * - Saves the new entry to both the database and the in-memory list.
 * - Provides navigation back to the main memory screen.
 */

public class AddEntryController {
    // --- UI Components injected from FXML ---

    @FXML private TextField addTitle; // Input field for entry title
    @FXML private HTMLEditor addNewEntryhtml;  // Rich text editor for memory content
    @FXML private Button selectImageButton;  // Button to insert an image
    @FXML private Button saveButton;	// Button to save entry
    @FXML private Button goBackButton;	// Button to navigate back
    @FXML private DatePicker addDate; // now a DatePicker

    // --- Internal references ---
    private ObservableList<MemoryEntry> entries;	// List of memory entries
    private MemoryMainController mainController; // reference to main controller
    private String selectedImagePath = "";	// Stores selected image path (if any)
    
    /**
     * Initializes the controller.
     * Adds keyboard shortcut: Ctrl+S triggers save when editor is focused.
     */

    @FXML
    public void initialize() {
        // Wait until the HTMLEditor is attached to a scene before adding listener
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
    
    /**
     * Opens a FileChooser to select an image and inserts it into the HTML editor.
     */

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
                // Convert image file to Base64 string
                byte[] fileContent = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                String base64Image = java.util.Base64.getEncoder().encodeToString(fileContent);
                
                // Detect file extension
                String extension = "";
                String fileName = selectedFile.getName().toLowerCase();
                if (fileName.endsWith(".png")) extension = "png";
                else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) extension = "jpeg";
                else if (fileName.endsWith(".gif")) extension = "gif";

                // Build <img> HTML tag
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

    //Utility to show confirmation popup after saving.
    private void showSaveConfirmation(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Save Successful");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    /**
     * Saves the current memory entry.
     * - Collects title, content, and date.
     * - Creates MemoryEntry object and inserts into database.
     * - Updates in-memory list and main controller.
     * - Closes window after saving.
     */

    @FXML
    private void handleSave() {
        String title = addTitle.getText().trim();
        String content = addNewEntryhtml.getHtmlText();

        // Get date from DatePicker (default to today if not selected)
        LocalDate selectedDate = addDate.getValue();
        
        if (selectedDate == null) {
            selectedDate = LocalDate.now(); // fallback if user didn't pick a date
        }
        // Skip saving if nothing entered

        if ((title == null || title.isEmpty()) && (content == null || content.isEmpty())) {
            System.out.println("Nothing entered, skipping save.");
            return;
        }

        try {
            // 1️. Create MemoryEntry with LocalDate
            MemoryEntry newEntry = new MemoryEntry(0, title, content, selectedDate, selectedImagePath);

            // 2️. Save to database
            DatabaseConnector.insertMemoryEntry(newEntry);

            // 3️. Add to in-memory list
            if (entries != null) entries.add(newEntry);

            System.out.println("Saved entry to database: " + title);

            // 4️. Refresh main controller's list
            if (mainController != null) {
                mainController.reloadEntriesFromDB();
            }
            // 5. show confirmation
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Save Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your entry has been saved successfully!");
            alert.showAndWait();

            // 6. Close window
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Navigates back to the main memory screen.
     * Triggered when the "Go Back" button is clicked.
     */

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