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

/**
 * Controller for viewing and editing a single memory entry.
 *
 * <p>This controller allows the user to:
 * <ul>
 *   <li>View entry details such as title and date.</li>
 *   <li>Edit content using an {@link HTMLEditor}.</li>
 *   <li>Add images to the entry as base64-encoded HTML tags.</li>
 *   <li>Save changes back into the database.</li>
 * </ul>
 * </p>
 */
public class ViewEntryController {

    @FXML private HTMLEditor htmlEditor;
    @FXML private Label titleLabel;
    @FXML private Label dateLabel;
    @FXML private Button saveButton;
    @FXML private Button selectImageButton;

    private MemoryEntry entry;
    
    /**
     * Called automatically when the FXML is loaded.
     * Initializes button states and configures event handlers
     * for enabling buttons and handling shortcuts.
     */
    
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
    
    /**
     * Enables save and image-select buttons
     * once the user has interacted with the editor.
     */

    private void enableButtons() {
        saveButton.setDisable(false);
        selectImageButton.setDisable(false);
    }
    
    /**
     * Extra handler (unused in current version) for enabling buttons on key events.
     * @param event key press event
     */

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

    /**
     * Opens a file chooser to allow the user to select an image.
     * Converts the image to Base64, embeds it into the HTML content,
     * and appends it to the editor.
     */
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
    //show confirmation after saving 
    private void showSaveConfirmation(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Save Successful");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Saves the current entry's content into the database.
     * Updates the {@link MemoryEntry} object and closes the window.
     */
    
    @FXML
    private void handleSave() {
        if (entry != null) {
            // Update entry with the current HTML content
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