package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The {@code MemoryEntry} class represents a single memory entry in the application.
 * Each entry contains an ID, title, content, date, and an optional image path.
 * 
 * <p>It also includes a {@code selected} flag which is useful when displaying
 * entries in a TableView with checkboxes for selection.</p>
 * 
 * <p>This class provides constructors for both new entries (without a database ID yet)
 * and existing entries (with an assigned ID), along with standard getters and setters.</p>
 */

public class MemoryEntry {
    private int id;
    private String title;
    private String content;
    private String imagePath; // store path, not the raw image
    private boolean selected; // for checkboxes in TableView later
    private LocalDate date;
    
 // ---------------------------
    // Constructors
    // ---------------------------

    /**
     * Constructs a {@code MemoryEntry} with all details provided.
     *
     * @param id        the unique ID of the entry (0 if not yet assigned by DB)
     * @param title     the title of the entry
     * @param content   the detailed content of the entry
     * @param date      the date associated with the entry
     * @param imagePath the path of the image file linked to this memory
     */
    
    public MemoryEntry(int id, String title, String content, LocalDate date, String imagePath) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
        this.date = date;        
        this.selected = false;
    }
    
    /**
     * Convenience constructor for creating new entries
     * before a database ID has been assigned.
     *
     * @param title     the title of the entry
     * @param content   the detailed content of the entry
     * @param date      the date associated with the entry
     * @param imagePath the path of the image file linked to this memory
     */

    public MemoryEntry(String title, String content, LocalDate date, String imagePath) {
        this(0, title, content, date, imagePath);
    }


    // Getters & setters
    public int getId() { return id; }     /** @return the unique ID of the entry */
    public void setId(int id) { this.id = id; }      /** @param id sets the unique ID of the entry */
    public String getTitle() { return title; }	    /** @return the title of the entry */
    public void setTitle(String title) { this.title = title; }      /** @param title sets the title of the entry */
    public String getContent() { return content; }      /** @return the content of the entry */
    public void setContent(String content) { this.content = content; }      /** @param content sets the content of the entry */
    public String getImagePath() { return imagePath; }      /** @return the path of the associated image */
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }      /** @param imagePath sets the path of the associated image */
    public boolean isSelected() { return selected; }      /** @return whether this entry is currently selected in the UI */
    public void setSelected(boolean selected) { this.selected = selected; }

    public LocalDate getRawDate() { return date; } // returns LocalDate object
    /**
     * Returns the date formatted as a human-readable string.
     * Format: {@code dd MMM yyyy} (e.g., 29 Sep 2025).
     * 
     * @return the formatted date as a string
     */
    public String getDate() { // returns formatted date as String
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
    	return date.format(formatter);
    }
    public void setDate(LocalDate date) { this.date = date; }
}