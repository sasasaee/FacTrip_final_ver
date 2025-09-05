package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MemoryEntry {
    private int id;
    private String title;
    private String content;
    private String imagePath; // store path, not the raw image
    private boolean selected; // for checkboxes in TableView later
    private LocalDate date;
    
    public MemoryEntry(int id, String title, String content, LocalDate date, String imagePath) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
        this.date = date;        
        this.selected = false;
    }

    // Optional: convenience constructor without ID (for new entries before DB assigns ID)
    public MemoryEntry(String title, String content, LocalDate date, String imagePath) {
        this(0, title, content, date, imagePath);
    }


    // Getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }

    public LocalDate getRawDate() { return date; } // returns LocalDate object
    public String getDate() { // returns formatted date as String
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
    	return date.format(formatter);
    }
    public void setDate(LocalDate date) { this.date = date; }
}