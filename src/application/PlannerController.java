package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class PlannerController implements Initializable {
    
    // FXML Components
    @FXML private GridPane calendarGrid;
    @FXML private Label periodLabel;
    @FXML private Button monthViewBtn, weekViewBtn, dayViewBtn;
    @FXML private Button prevBtn, nextBtn, addEventBtn, backToMenuBtn;
    @FXML private ListView<String> eventListView;
    @FXML private VBox eventFormPanel;
    @FXML private TextField eventNameField, eventTimeField, eventLocationField;
    @FXML private DatePicker eventDatePicker;
    @FXML private TextArea eventDescriptionArea;
    @FXML private ComboBox<String> eventCategoryComboBox;
    @FXML private ComboBox<String> reminderComboBox; // New ComboBox for reminder
    @FXML private Button saveEventBtn, editEventBtn, deleteEventBtn, cancelEventBtn;
    
    // Calendar state
    private enum ViewMode { MONTH, WEEK, DAY }
    private ViewMode currentViewMode = ViewMode.MONTH;
    private YearMonth currentYearMonth = YearMonth.now();
    private LocalDate currentDate = LocalDate.now();
    private LocalDate selectedDate = LocalDate.now();
    
    // Event management
    private List<TripEvent> events = new ArrayList<>();
    private TripEvent selectedEvent = null;
    private boolean isEditingEvent = false;
    
    // Simple text file for data persistence
    private static final String EVENTS_FILE = "trip_events.txt";

    // Predefined categories and their colors
    private final Map<String, String> CATEGORY_COLORS = new HashMap<>();

    // Reminder options
    private final Map<String, Integer> REMINDER_OPTIONS = new LinkedHashMap<>();

    public PlannerController() {
        CATEGORY_COLORS.put("Travel", "#4CAF50"); // Green
        CATEGORY_COLORS.put("Meeting", "#2196F3"); // Blue
        CATEGORY_COLORS.put("Personal", "#FFC107"); // Amber
        CATEGORY_COLORS.put("Work", "#F44336"); // Red
        CATEGORY_COLORS.put("Other", "#9E9E9E"); // Grey

        REMINDER_OPTIONS.put("No Reminder", 0);
        REMINDER_OPTIONS.put("5 minutes before", 5);
        REMINDER_OPTIONS.put("15 minutes before", 15);
        REMINDER_OPTIONS.put("30 minutes before", 30);
        REMINDER_OPTIONS.put("1 hour before", 60);
        REMINDER_OPTIONS.put("1 day before", 1440);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize category ComboBox
        eventCategoryComboBox.setItems(FXCollections.observableArrayList(CATEGORY_COLORS.keySet()));
        eventCategoryComboBox.getSelectionModel().selectFirst();

        // Initialize reminder ComboBox
        reminderComboBox.setItems(FXCollections.observableArrayList(REMINDER_OPTIONS.keySet()));
        reminderComboBox.getSelectionModel().selectFirst();

        // Load events from text file
        loadEventsFromFile();
        
        // Initialize calendar view
        showMonthView();
        updateEventList();
        
        // Set up event list selection
        eventListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectEventFromList(newVal);
            }
        });

        // Start reminder checker
        startReminderChecker();
    }
    
    // View switching methods
    @FXML
    void showMonthView() {
        currentViewMode = ViewMode.MONTH;
        updateViewButtons();
        displayMonthView();
        updatePeriodLabel();
    }
    
    @FXML
    void showWeekView() {
        currentViewMode = ViewMode.WEEK;
        updateViewButtons();
        displayWeekView();
        updatePeriodLabel();
    }
    
    @FXML
    void showDayView() {
        currentViewMode = ViewMode.DAY;
        updateViewButtons();
        displayDayView();
        updatePeriodLabel();
    }
    
    // Navigation methods
    @FXML
    void previousPeriod() {
        switch (currentViewMode) {
            case MONTH:
                currentYearMonth = currentYearMonth.minusMonths(1);
                displayMonthView();
                break;
            case WEEK:
                currentDate = currentDate.minusWeeks(1);
                displayWeekView();
                break;
            case DAY:
                currentDate = currentDate.minusDays(1);
                displayDayView();
                break;
        }
        updatePeriodLabel();
        updateEventList();
    }
    
    @FXML
    void nextPeriod() {
        switch (currentViewMode) {
            case MONTH:
                currentYearMonth = currentYearMonth.plusMonths(1);
                displayMonthView();
                break;
            case WEEK:
                currentDate = currentDate.plusWeeks(1);
                displayWeekView();
                break;
            case DAY:
                currentDate = currentDate.plusDays(1);
                displayDayView();
                break;
        }
        updatePeriodLabel();
        updateEventList();
    }
    
    // Event management methods
    @FXML
    void showAddEventDialog() {
        clearEventForm();
        eventFormPanel.setVisible(true);
        isEditingEvent = false;
        selectedEvent = null;
        eventDatePicker.setValue(selectedDate);
        editEventBtn.setVisible(false);
        deleteEventBtn.setVisible(false);
        saveEventBtn.setText("Add Event");
        eventCategoryComboBox.getSelectionModel().selectFirst();
        reminderComboBox.getSelectionModel().selectFirst(); // Reset reminder selection
    }
    
    @FXML
    void saveEvent() {
        if (validateEventForm()) {
            TripEvent newOrUpdatedEvent = new TripEvent();
            newOrUpdatedEvent.setName(eventNameField.getText().trim());
            newOrUpdatedEvent.setDate(eventDatePicker.getValue());
            newOrUpdatedEvent.setTime(parseTime(eventTimeField.getText().trim()));
            newOrUpdatedEvent.setLocation(eventLocationField.getText().trim());
            newOrUpdatedEvent.setDescription(eventDescriptionArea.getText().trim());
            newOrUpdatedEvent.setCategory(eventCategoryComboBox.getValue());
            newOrUpdatedEvent.setReminderOffsetMinutes(REMINDER_OPTIONS.get(reminderComboBox.getValue())); // Save reminder offset
            
            if (isEditingEvent && selectedEvent != null) {
                newOrUpdatedEvent.setId(selectedEvent.getId());
                int index = -1;
                for (int i = 0; i < events.size(); i++) {
                    if (events.get(i).getId().equals(selectedEvent.getId())) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    events.set(index, newOrUpdatedEvent);
                } else {
                    events.add(newOrUpdatedEvent);
                }
            } else {
                newOrUpdatedEvent.setId(UUID.randomUUID().toString());
                events.add(newOrUpdatedEvent);
            }
            
            saveEventsToFile();
            refreshCalendarView();
            updateEventList();
            cancelEventForm();
            
            showAlert("Success", isEditingEvent ? "Event updated successfully!" : "Event added successfully!");
        }
    }
    
    @FXML
    void editEvent() {
        if (selectedEvent != null) {
            populateEventForm(selectedEvent);
            eventFormPanel.setVisible(true);
            isEditingEvent = true;
            editEventBtn.setVisible(true);
            deleteEventBtn.setVisible(true);
            saveEventBtn.setText("Update Event");
        }
    }
    
    @FXML
    void deleteEvent() {
        if (selectedEvent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Event");
            alert.setHeaderText("Are you sure you want to delete this event?");
            alert.setContentText(selectedEvent.getName());
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                events.remove(selectedEvent);
                saveEventsToFile();
                refreshCalendarView();
                updateEventList();
                cancelEventForm();
                showAlert("Success", "Event deleted successfully!");
            }
        }
    }
    
    @FXML
    void cancelEventForm() {
        eventFormPanel.setVisible(false);
        clearEventForm();
        isEditingEvent = false;
        selectedEvent = null;
    }
    
    @FXML
    void selectEvent(MouseEvent event) {
        String selectedItem = eventListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            selectEventFromList(selectedItem);
        }
    }
    
    @FXML
    void backToMainMenu(ActionEvent event) {
        try {
            Parent menuRoot = FXMLLoader.load(getClass().getResource("/application/mainmenu.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(menuRoot);
            stage.setScene(scene);
            stage.setTitle("Factrip - Main Menu");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not return to main menu: " + e.getMessage());
        }
    }
    
    // Calendar display methods
    private void displayMonthView() {
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear();
        calendarGrid.getRowConstraints().clear();
        
        // Add day headers
        String[] dayHeaders = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(dayHeaders[i]);
            dayLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 14));
            dayLabel.setTextFill(Color.web("#5b0101"));
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setPrefSize(120, 30);
            dayLabel.setStyle("-fx-background-color: #CC6666; -fx-border-color: #5b0101;");
            calendarGrid.add(dayLabel, i, 0);
        }
        
        // Get first day of month and number of days
        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int daysInMonth = currentYearMonth.lengthOfMonth();
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // Sunday = 0
        
        // Add calendar days
        int row = 1;
        int col = startDayOfWeek;
        
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            VBox dayBox = createDayBox(date);
            calendarGrid.add(dayBox, col, row);
            
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }
    
    private void displayWeekView() {
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear();
        calendarGrid.getRowConstraints().clear();
        
        // Get start of week (Sunday)
        LocalDate startOfWeek = currentDate.minusDays(currentDate.getDayOfWeek().getValue() % 7);
        
        // Add day headers and days
        String[] dayHeaders = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            
            // Header
            Label dayLabel = new Label(dayHeaders[i] + " " + date.getDayOfMonth());
            dayLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 14));
            dayLabel.setTextFill(Color.web("#5b0101"));
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setPrefSize(140, 30);
            dayLabel.setStyle("-fx-background-color: #CC6666; -fx-border-color: #5b0101;");
            calendarGrid.add(dayLabel, i, 0);
            
            // Day content
            VBox dayBox = createDayBox(date);
            dayBox.setPrefSize(140, 200);
            calendarGrid.add(dayBox, i, 1);
        }
    }
    
    private void displayDayView() {
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear();
        calendarGrid.getRowConstraints().clear();
        
        // Single day view
        VBox dayBox = createDayBox(currentDate);
        dayBox.setPrefSize(600, 400);
        dayBox.setStyle("-fx-background-color: white; -fx-border-color: #5b0101; -fx-border-width: 2; -fx-padding: 10;");
        
        Label dateLabel = new Label(currentDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        dateLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 18));
        dateLabel.setTextFill(Color.web("#5b0101"));
        dayBox.getChildren().add(0, dateLabel);
        
        calendarGrid.add(dayBox, 0, 0);
    }
    
    private VBox createDayBox(LocalDate date) {
        VBox dayBox = new VBox(2);
        dayBox.setAlignment(Pos.TOP_CENTER);
        dayBox.setPrefSize(120, 100);
        dayBox.setPadding(new Insets(5));
        
        // Highlight today and selected date
        String backgroundColor = "#FFFFFF";
        if (date.equals(LocalDate.now())) {
            backgroundColor = "#E8F5E8"; // Light blue for today
        } else if (date.equals(selectedDate)) {
            backgroundColor = "#FFF3E0"; // Light orange for selected
        }
        
        dayBox.setStyle("-fx-background-color: " + backgroundColor + "; -fx-border-color: #5b0101; -fx-cursor: hand;");
        
        // Day number
        Label dayLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dayLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 12));
        dayLabel.setTextFill(Color.web("#5b0101"));
        dayBox.getChildren().add(dayLabel);
        
        // Add events for this date
        List<TripEvent> dayEvents = getEventsForDate(date);
        for (TripEvent event : dayEvents) {
            Label eventLabel = new Label(event.getName());
            eventLabel.setFont(Font.font("Century Gothic", 11));
            eventLabel.setTextFill(Color.web("#5b0101"));
            eventLabel.setStyle("-fx-background-color: #E8F5E8; -fx-padding: 1; -fx-background-radius: 3;");
            eventLabel.setWrapText(true);
            eventLabel.setMaxWidth(160);
            eventLabel.setFont(Font.font("Century Gothic", 18));
            
            // Apply color based on category
            String categoryColor = CATEGORY_COLORS.getOrDefault(event.getCategory(), "#4CAF50"); // Default to green
            eventLabel.setTextFill(Color.web(categoryColor));
            eventLabel.setStyle("-fx-background-color: " + categoryColor + "33; -fx-padding: 1; -fx-background-radius: 3;"); // Light tint of category color
            
            eventLabel.setMaxWidth(110);
            dayBox.getChildren().add(eventLabel);
        }
        
        // Click handler
        dayBox.setOnMouseClicked(e -> {
            selectedDate = date;
            refreshCalendarView();
            updateEventList();
        });
        
        return dayBox;
    }
    
    // Helper methods
    private void updateViewButtons() {
        monthViewBtn.setStyle("-fx-background-color: " + (currentViewMode == ViewMode.MONTH ? " #D7D1C2" : "#B5AB99") + "; -fx-text-fill:  #5b0101;");
        weekViewBtn.setStyle("-fx-background-color: " + (currentViewMode == ViewMode.WEEK ? " #D7D1C2" : "#B5AB99") + "; -fx-text-fill:  #5b0101;");
        dayViewBtn.setStyle("-fx-background-color: " + (currentViewMode == ViewMode.DAY ? " #D7D1C2" : "#B5AB99") + "; -fx-text-fill:  #5b0101;");
    }
    
    private void updatePeriodLabel() {
        switch (currentViewMode) {
            case MONTH:
                periodLabel.setText(currentYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
                break;
            case WEEK:
                LocalDate startOfWeek = currentDate.minusDays(currentDate.getDayOfWeek().getValue() % 7);
                LocalDate endOfWeek = startOfWeek.plusDays(6);
                periodLabel.setText(startOfWeek.format(DateTimeFormatter.ofPattern("MMM d")) + " - " + 
                                  endOfWeek.format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
                break;
            case DAY:
                periodLabel.setText(currentDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
                break;
        }
    }
    
    private void refreshCalendarView() {
        switch (currentViewMode) {
            case MONTH:
                displayMonthView();
                break;
            case WEEK:
                displayWeekView();
                break;
            case DAY:
                displayDayView();
                break;
        }
    }
    
    private void updateEventList() {
        eventListView.getItems().clear();
        List<TripEvent> relevantEvents = getEventsForCurrentView();
        
        for (TripEvent event : relevantEvents) {
            String eventString = event.getDate().format(DateTimeFormatter.ofPattern("MMM d")) + 
                               (event.getTime() != null ? " " + event.getTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "") + 
                               " - " + event.getName() + " [" + event.getCategory() + "]";
            eventListView.getItems().add(eventString);
        }
    }
    
    private List<TripEvent> getEventsForCurrentView() {
        List<TripEvent> relevantEvents = new ArrayList<>();
        
        switch (currentViewMode) {
            case MONTH:
                for (TripEvent event : events) {
                    if (YearMonth.from(event.getDate()).equals(currentYearMonth)) {
                        relevantEvents.add(event);
                    }
                }
                break;
            case WEEK:
                LocalDate startOfWeek = currentDate.minusDays(currentDate.getDayOfWeek().getValue() % 7);
                LocalDate endOfWeek = startOfWeek.plusDays(6);
                for (TripEvent event : events) {
                    if (!event.getDate().isBefore(startOfWeek) && !event.getDate().isAfter(endOfWeek)) {
                        relevantEvents.add(event);
                    }
                }
                break;
            case DAY:
                relevantEvents = getEventsForDate(currentDate);
                break;
        }
        
        relevantEvents.sort(Comparator.comparing(TripEvent::getDate).thenComparing(e -> e.getTime() != null ? e.getTime() : LocalTime.MIN));
        return relevantEvents;
    }
    
    private List<TripEvent> getEventsForDate(LocalDate date) {
        return events.stream()
                    .filter(event -> event.getDate().equals(date))
                    .sorted(Comparator.comparing(e -> e.getTime() != null ? e.getTime() : LocalTime.MIN))
                    .collect(Collectors.toList());
    }
    
    private void selectEventFromList(String eventString) {
        // Parse the event string to find the corresponding event
        for (TripEvent event : events) {
            String compareString = event.getDate().format(DateTimeFormatter.ofPattern("MMM d")) + 
                                 (event.getTime() != null ? " " + event.getTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "") + 
                                 " - " + event.getName() + " [" + event.getCategory() + "]";
            if (compareString.equals(eventString)) {
                selectedEvent = event;
                populateEventForm(event);
                eventFormPanel.setVisible(true);
                isEditingEvent = true; // Set to true when selecting an existing event
                editEventBtn.setVisible(true);
                deleteEventBtn.setVisible(true);
                saveEventBtn.setText("Update Event");
                break;
            }
        }
    }
    
    private void populateEventForm(TripEvent event) {
        eventNameField.setText(event.getName());
        eventDatePicker.setValue(event.getDate());
        eventTimeField.setText(event.getTime() != null ? event.getTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "");
        eventLocationField.setText(event.getLocation());
        eventDescriptionArea.setText(event.getDescription());
        eventCategoryComboBox.setValue(event.getCategory());
        
        // Set reminder ComboBox value
        String reminderKey = REMINDER_OPTIONS.entrySet().stream()
                                .filter(entry -> entry.getValue().equals(event.getReminderOffsetMinutes()))
                                .map(Map.Entry::getKey)
                                .findFirst()
                                .orElse("No Reminder");
        reminderComboBox.setValue(reminderKey);
    }
    
    private void clearEventForm() {
        eventNameField.clear();
        eventDatePicker.setValue(LocalDate.now());
        eventTimeField.clear();
        eventLocationField.clear();
        eventDescriptionArea.clear();
        eventCategoryComboBox.getSelectionModel().selectFirst();
        reminderComboBox.getSelectionModel().selectFirst(); // Reset reminder selection
    }
    
    private boolean validateEventForm() {
        if (eventNameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Event name is required.");
            return false;
        }
        
        if (eventDatePicker.getValue() == null) {
            showAlert("Validation Error", "Event date is required.");
            return false;
        }
        
        if (!eventTimeField.getText().trim().isEmpty()) {
            try {
                parseTime(eventTimeField.getText().trim());
            } catch (DateTimeParseException e) {
                showAlert("Validation Error", "Invalid time format. Please use HH:MM format (e.g., 14:30).");
                return false;
            }
        }

        if (eventCategoryComboBox.getValue() == null || eventCategoryComboBox.getValue().isEmpty()) {
            showAlert("Validation Error", "Event category is required.");
            return false;
        }
        
        return true;
    }
    
    private LocalTime parseTime(String timeString) {
        if (timeString == null || timeString.trim().isEmpty()) {
            return null;
        }
        return LocalTime.parse(timeString.trim(), DateTimeFormatter.ofPattern("HH:mm"));
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Simple text file persistence methods (no Jackson needed)
    private void loadEventsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(EVENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                TripEvent event = parseEventFromString(line);
                if (event != null) {
                    events.add(event);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Events file not found, starting with empty list.");
        } catch (Exception e) {
            System.err.println("Error loading events from file: " + e.getMessage());
            events = new ArrayList<>();
        }
    }
    
    private void saveEventsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EVENTS_FILE))) {
            for (TripEvent event : events) {
                writer.println(eventToString(event));
            }
        } catch (Exception e) {
            System.err.println("Error saving events to file: " + e.getMessage());
            showAlert("Error", "Could not save events to file: " + e.getMessage());
        }
    }
    
    // Simple string format: ID|Name|Date|Time|Location|Description|Category|ReminderOffsetMinutes
    private String eventToString(TripEvent event) {
        return event.getId() + "|" +
               event.getName() + "|" +
               event.getDate().toString() + "|" +
               (event.getTime() != null ? event.getTime().toString() : "") + "|" +
               (event.getLocation() != null ? event.getLocation() : "") + "|" +
               (event.getDescription() != null ? event.getDescription() : "") + "|" +
               (event.getCategory() != null ? event.getCategory() : "Other") + "|" +
               event.getReminderOffsetMinutes();
    }
    
    private TripEvent parseEventFromString(String line) {
        try {
            String[] parts = line.split("\\|", -1);
            if (parts.length >= 8) { // Now expecting 8 parts for reminderOffsetMinutes
                TripEvent event = new TripEvent();
                event.setId(parts[0]);
                event.setName(parts[1]);
                event.setDate(LocalDate.parse(parts[2]));
                event.setTime(parts[3].isEmpty() ? null : LocalTime.parse(parts[3]));
                event.setLocation(parts[4].isEmpty() ? null : parts[4]);
                event.setDescription(parts[5].isEmpty() ? null : parts[5]);
                event.setCategory(parts[6].isEmpty() ? "Other" : parts[6]);
                event.setReminderOffsetMinutes(Integer.parseInt(parts[7]));
                return event;
            } else if (parts.length == 7) { // Handle old format for category
                TripEvent event = new TripEvent();
                event.setId(parts[0]);
                event.setName(parts[1]);
                event.setDate(LocalDate.parse(parts[2]));
                event.setTime(parts[3].isEmpty() ? null : LocalTime.parse(parts[3]));
                event.setLocation(parts[4].isEmpty() ? null : parts[4]);
                event.setDescription(parts[5].isEmpty() ? null : parts[5]);
                event.setCategory(parts[6].isEmpty() ? "Other" : parts[6]);
                event.setReminderOffsetMinutes(0); // Default to no reminder
                return event;
            } else if (parts.length == 6) { // Handle even older format without category
                TripEvent event = new TripEvent();
                event.setId(parts[0]);
                event.setName(parts[1]);
                event.setDate(LocalDate.parse(parts[2]));
                event.setTime(parts[3].isEmpty() ? null : LocalTime.parse(parts[3]));
                event.setLocation(parts[4].isEmpty() ? null : parts[4]);
                event.setDescription(parts[5].isEmpty() ? null : parts[5]);
                event.setCategory("Other"); // Assign default category for old events
                event.setReminderOffsetMinutes(0); // Default to no reminder
                return event;
            }
        } catch (Exception e) {
            System.err.println("Error parsing event from line: " + line + " - " + e.getMessage());
        }
        return null;
    }

    // Reminder Logic
 // ------------------ Reminder Logic ------------------
    

    private Timeline reminderTimeline;
    private Set<String> notifiedEvents = new HashSet<>(); // Prevent duplicate notifications

    private void startReminderChecker() {
        if (reminderTimeline != null) {
            reminderTimeline.stop();
        }
        // Check every 10 seconds
        reminderTimeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> checkReminders()));
        reminderTimeline.setCycleCount(Timeline.INDEFINITE);
        reminderTimeline.play();
    }

    private void checkReminders() {
        LocalDateTime now = LocalDateTime.now();

        for (TripEvent event : events) {
            if (event.getTime() == null || event.getReminderOffsetMinutes() <= 0) continue;

            LocalDateTime eventDateTime = LocalDateTime.of(event.getDate(), event.getTime());
            LocalDateTime reminderTime = eventDateTime.minusMinutes(event.getReminderOffsetMinutes());

            // Check if reminder is due and not yet notified
            if (!notifiedEvents.contains(event.getId()) && !now.isBefore(reminderTime) && now.isBefore(eventDateTime)) {
                notifiedEvents.add(event.getId()); // Mark as notified

                // Use Platform.runLater to ensure thread safety
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Upcoming Event Reminder!");
                    alert.setHeaderText(event.getName());
                    String timeString = event.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                    String location = event.getLocation() != null ? event.getLocation() : "No location";
                    alert.setContentText("Time: " + timeString + "\nLocation: " + location);
                    alert.show();
                });
            }
        }
    }

}