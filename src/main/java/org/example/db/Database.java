package org.example.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Database {
    private static String DB_USER="postgres";

    private static String DB_PASSWORD = "postgres";

    private static String TABLE_NAME= "pc_builder";

    private static String DB_URL = "jdbc:postgresql://localhost:5432/"+ "Pc-Builder";

    private static final ReentrantLock lock = new ReentrantLock();

    public static void initializeDatabase(){
    try(Connection conn = getConnection();
        Statement stmt = conn.createStatement()){

        stmt.execute("DROP TABLE IF EXISTS "+ TABLE_NAME);

        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("+
                "id SERIAL PRIMARY KEY, "+
                "component_name VARCHAR(255) NOT NULL,"+
                "type VARCHAR(255) NOT NULL,"+
                "brand VARCHAR(255) NOT NULL,"+
                "price DOUBLE PRECISION NOT NULL,"+
                "color VARCHAR(255) NOT NULL) ";
        stmt.execute(createTableSQL);

        String createUserTableSQL = """
    CREATE TABLE IF NOT EXISTS users (
        username TEXT PRIMARY KEY,
        password TEXT NOT NULL
    )
""";
        stmt.execute(createUserTableSQL);
        String createFavoritesTableSQL = """
    CREATE TABLE IF NOT EXISTS favorites (
        id SERIAL PRIMARY KEY,
        username TEXT NOT NULL,
        component_name TEXT NOT NULL,
        brand TEXT NOT NULL,
        price DOUBLE PRECISION NOT NULL,
        color TEXT NOT NULL,
        type TEXT NOT NULL
    )
""";
        stmt.execute(createFavoritesTableSQL);

        System.out.println("Database had been initialized");
    }
    catch (SQLException e){
        System.err.println("Unable to initialize the Database!"+ e.getMessage());
        System.exit(2);
    }

    }
    public static void saveBuildToFavorites(String username, List<Component> components) {
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("""
            INSERT INTO favorites (username, component_name, brand, price, color, type)
            VALUES (?, ?, ?, ?, ?, ?)
        """);

            for (Component c : components) {
                stmt.setString(1, username);
                stmt.setString(2, c.getName());
                stmt.setString(3, c.getBrand());
                stmt.setDouble(4, c.getPrice());
                stmt.setString(5, c.getColor());
                stmt.setString(6, c.getType());
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println(" Build saved to favorites!");
        } catch (SQLException e) {
            System.err.println(" Failed to save build: " + e.getMessage());
        }
    }
    public static List<Component> getFavoritesForUser(String username) {
        List<Component> favorites = new ArrayList<>();

        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("""
            SELECT component_name, brand, price, color, type
            FROM favorites
            WHERE username = ?
        """);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Component c = new Component(
                        rs.getString("component_name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getString("color"),
                        rs.getString("type")
                );
                favorites.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving favorites: " + e.getMessage());
        }

        return favorites;
    }


    public static List<Component> getAllComponents() {
        List<Component> components = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT component_name, brand, price, color, type FROM pc_builder")) {

            while (rs.next()) {
                Component component = new Component(
                        rs.getString("component_name"),
                        rs.getString("brand"),
                        rs.getDouble("price"),
                        rs.getString("color"),
                        rs.getString("type")
                );
                components.add(component);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving components: " + e.getMessage());
        }
        return components;
    }

    public static void insertComponent(List<Component> componentList) {
        lock.lock();
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO pc_builder (component_name, brand, price, color, type) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            conn.setAutoCommit(false);

            for (Component item : componentList) {
                pstmt.setString(1, item.getName());
                pstmt.setString(2, item.getBrand());
                pstmt.setDouble(3, item.getPrice());
                pstmt.setString(4, item.getColor());
                pstmt.setString(5, item.getType());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
    public static List<Component> searchByName(String name) {
        List<Component> result = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM pc_builder WHERE LOWER(component_name) LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + name.toLowerCase() + "%");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(extractComponent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Search by name failed: " + e.getMessage());
        }
        return result;
    }

    public static List<Component> filterByType(String type) {
        List<Component> result = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM pc_builder WHERE LOWER(type) = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type.toLowerCase());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(extractComponent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Filter by type failed: " + e.getMessage());
        }
        return result;
    }

    // Utility method to convert ResultSet into Component
    private static Component extractComponent(ResultSet rs) throws SQLException {
        String name = rs.getString("component_name");
        String brand = rs.getString("brand");
        double price = rs.getDouble("price");
        String color = rs.getString("color");
        String type = rs.getString("type");
        return new Component(name, brand, price, color, type);
    }



    public static Connection getConnection() throws SQLException {
        System.out.println(DB_URL + " " +  DB_USER + " " + DB_PASSWORD);
        return DriverManager.getConnection(DB_URL , DB_USER, DB_PASSWORD);

    }
}
