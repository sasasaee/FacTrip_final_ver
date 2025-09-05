package application;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.nio.file.Files;
import java.sql.SQLException;

public class ViewEntryController {

    @FXML private HTMLEditor htmlEditor;
    @FXML private Label titleLabel;
    @FXML private Label dateLabel;
    @FXML private Button saveButton;
    @FXML private Button selectImageButton;

    private MemoryEntry entry;
    
    @FXML
    public void initialize() {
        // Initially disable buttons if needed
        saveButton.setDisable(true);
        selectImageButton.setDisable(true);

        // Enable buttons when any key is pressed while HTMLEditor is focused
        htmlEditor.setOnKeyPressed(event -> {
            enableButtons();
            // Ctrl+S shortcut for saving
            if (event.isControlDown() && event.getCode() == KeyCode.S) {
                handleSave();
                event.consume();
            }
        });

        // Enable buttons when HTMLEditor is clicked
        htmlEditor.setOnMouseClicked(event -> enableButtons());
    }

    private void enableButtons() {
        saveButton.setDisable(false);
        selectImageButton.setDisable(false);
    }

    private void handleKeyPressed(KeyEvent event) {
        enableButtons();
    }

    // Called by MemoryMainController when opening this entry
    public void setEntry(MemoryEntry entry) {
        this.entry = entry;

        titleLabel.setText(entry.getTitle());
        dateLabel.setText(entry.getDate() != null ? entry.getDate() : ""); // optional
        htmlEditor.setHtmlText(entry.getContent());
    }


    @FXML
    private void addPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(htmlEditor.getScene().getWindow());
        if (selectedFile != null) {
            try {
                byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
                String base64Image = java.util.Base64.getEncoder().encodeToString(fileContent);
                String extension = "";

                String fileName = selectedFile.getName().toLowerCase();
                if (fileName.endsWith(".png")) extension = "png";
                else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) extension = "jpeg";
                else if (fileName.endsWith(".gif")) extension = "gif";

                String imgTag = "<img src='data:image/" + extension + ";base64," + base64Image + "' width='300'/>";
                
                // Append image to current HTML
                String currentHtml = htmlEditor.getHtmlText();
                currentHtml = currentHtml.replace("</body>", imgTag + "</body>");
                htmlEditor.setHtmlText(currentHtml);

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
        if (entry != null) {
            entry.setContent(htmlEditor.getHtmlText());

            try {
                DatabaseConnector.updateMemoryEntry(entry);
                System.out.println("Entry updated: " + entry.getTitle());
                showSaveConfirmation("Your entry has been saved successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}