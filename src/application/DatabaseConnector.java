package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;


import java.io.File;
import java.sql.*;

public class DatabaseConnector {

    private static final String DB_URL = "jdbc:sqlite:memory_entries.db";

    // Get DB connection
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        System.out.println("Connected to SQLite: " + new File("memory_entries.db").getAbsolutePath());
        return conn;
    }

    // Initialize database (create table if it doesn't exist)
    public static void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS memory_entries (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "content TEXT," +
                "image_path TEXT" +
                ")";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Ensure table exists
            stmt.execute(createTableSQL);
            System.out.println("Database table ensured.");

            // Add date column if it doesn't exist (for older DBs)
            try {
                stmt.execute("ALTER TABLE memory_entries ADD COLUMN date TEXT");
                System.out.println("Date column added.");
            } catch (SQLException e) {
                System.out.println("Date column already exists or failed to add: " + e.getMessage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Insert a new MemoryEntry into DB
    public static void insertMemoryEntry(MemoryEntry entry) throws SQLException {
        String insertSQL = "INSERT INTO memory_entries(title, content, image_path, date) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, entry.getTitle());
            pstmt.setString(2, entry.getContent());
            pstmt.setString(3, entry.getImagePath());
            pstmt.setString(4, entry.getRawDate().toString()); // save the selected date
            pstmt.executeUpdate();

            // Retrieve auto-generated ID
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    entry.setId(keys.getInt(1));
                }
            }
        }
    }

    // Load all entries from DB into an ObservableList
    public static ObservableList<MemoryEntry> getAllMemoryEntries() {
        ObservableList<MemoryEntry> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM memory_entries ORDER BY id DESC";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String imagePath = rs.getString("image_path");
                String dateText = rs.getString("date");

                LocalDate date = (dateText != null && !dateText.isEmpty())
                        ? LocalDate.parse(dateText)
                        : LocalDate.now();
                
                MemoryEntry entry = new MemoryEntry(id, title, content, date, imagePath);
                list.add(entry);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Delete entry by ID
    public static void deleteMemoryEntry(int id) {
        String deleteSQL = "DELETE FROM memory_entries WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Test run
    public static void main(String[] args) {
        initializeDatabase();
        System.out.println("Database ready.");
    }
    
    public static void updateMemoryEntry(MemoryEntry entry) throws SQLException {
        String updateSQL = "UPDATE memory_entries SET title = ?, content = ?, image_path = ?, date = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            pstmt.setString(1, entry.getTitle());
            pstmt.setString(2, entry.getContent());
            pstmt.setString(3, entry.getImagePath());
            pstmt.setString(4, entry.getRawDate().toString());
            pstmt.setInt(5, entry.getId());

            pstmt.executeUpdate();
        }
    }
}