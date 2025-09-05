package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MemoryDAO {

    // Create table if not exists
    public static void createTableIfNotExists() {
        String sql = """
        	    CREATE TABLE IF NOT EXISTS entries (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                content TEXT,
                image_path TEXT,
                date TEXT
            );
            """;
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'entries' ensured.");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }

    // Insert entry
    public static int insertEntry(MemoryEntry entry) {
        String sql = "INSERT INTO entries(title, content, image_path,date) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, entry.getTitle());
            pstmt.setString(2, entry.getContent());
            pstmt.setString(3, entry.getImagePath());
            pstmt.setString(4, entry.getRawDate().toString()); 
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    entry.setId(id);
                    System.out.println("Inserted entry with id: " + id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
        }
        return -1;
    }
    
    public static void update(MemoryEntry entry) {
    	String sql = "UPDATE entries SET title = ?, content = ?, image_path = ?, date = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entry.getTitle());
            pstmt.setString(2, entry.getContent());
            pstmt.setString(3, entry.getImagePath());
            pstmt.setInt(4, entry.getId());
            pstmt.setInt(5, entry.getId());

            pstmt.executeUpdate();
            System.out.println("Entry updated: " + entry.getId());
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Fetch all entries
    public static List<MemoryEntry> getAllEntries() {
        List<MemoryEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM entries ORDER BY id DESC";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
        	while (rs.next()) {
        	    int id = rs.getInt("id");
        	    String title = rs.getString("title");
        	    String content = rs.getString("content");
        	    String imagePath = rs.getString("image_path");
        	    String dateText = rs.getString("date"); // read from DB

        	    LocalDate date = (dateText != null && !dateText.isEmpty()) 
        	                     ? LocalDate.parse(dateText) 
        	                     : LocalDate.now(); // fallback

        	    list.add(new MemoryEntry(id, title, content, date, imagePath));

        	}

            System.out.println("Fetched " + list.size() + " entries.");
        } catch (SQLException e) {
            System.err.println("Fetch failed: " + e.getMessage());
        }
        return list;
    }

    // Test main
    public static void main(String[] args) {
        createTableIfNotExists();
        MemoryEntry e = new MemoryEntry("First memory", "This is a test note", LocalDate.now(), null);
        insertEntry(e);
        getAllEntries();
    }
}