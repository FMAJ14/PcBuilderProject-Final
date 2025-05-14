package org.example;

import org.example.db.Component;
import org.example.db.Database;
import org.example.io.CsvImporter;
import org.example.networking.Wikipedia;
import org.example.UserManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App {
    private final Scanner scanner;
    private UserManager userManager;
    private boolean loggedIn = false;
    private String username;

    public App() {
        this.scanner = new Scanner(System.in);
    }
    private void displayLoginMenu() {
        System.out.println("\n===== User Login =====");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("0. Exit");
        System.out.println("=======================");
    }

    private void handleRegister() {
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userManager.register(username, password)) {
            System.out.println("Registration successful.");
        } else {
            System.out.println("Username already exists. Try again.");
        }
    }

    private void handleLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userManager.login(username, password)) {
            this.username = username;  // Save logged-in username
            loggedIn = true;
            System.out.println("Login successful. Welcome, " + username + "!");
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }



    public void startCLI() {
        while (!loggedIn) {
            displayLoginMenu();
            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1 -> handleRegister();
                case 2 -> handleLogin();
                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }

        // Main application menu
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> searchByComponentName();
                case 2 -> filterByComponentType();
                case 3 -> getWikipediaInfo();
                case 4 -> new org.example.PcBuilder(username).start();
                case 0 -> {
                    running = false;
                    System.out.println("Exiting application. Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n===== PC Component CLI =====");
        System.out.println("1. Search by component name");
        System.out.println("2. Filter by component type");
        System.out.println("3. Get Wikipedia information for a component");
        System.out.println("4. Build your own PC");
        System.out.println("0. Exit");
        System.out.println("============================");
    }

    private void searchByComponentName() {
        System.out.print("Enter component name to search (Press Enter for All components): ");
        String name = scanner.nextLine();

        List<Component> results = Database.searchByName(name);
        displayResults(results);
    }

    private void filterByComponentType() {
        System.out.print("Enter component type (e.g., CPU, GPU, Memory): ");
        String type = scanner.nextLine();

        List<Component> results = Database.filterByType(type);
        displayResults(results);
    }

    private void getWikipediaInfo() {
        System.out.print("Enter component name to get Wikipedia information: ");
        String componentName = scanner.nextLine();

        try {
            String info = Wikipedia.getWikipediaInformation(componentName);
            System.out.println("\n--- Wikipedia Information ---");
            System.out.println(info);
        } catch (IOException e) {
            System.out.println("Failed to retrieve Wikipedia information: " + e.getMessage());
        }
    }

    private void displayResults(List<Component> results) {
        if (results.isEmpty()) {
            System.out.println("No results found.");
            return;
        }

        System.out.printf("%-20s %-15s %-10s %-10s %-10s\n", "Name", "Brand", "Price", "Color", "Type");
        System.out.println("-".repeat(70));

        for (Component c : results) {
            System.out.printf("%-20s %-15s %-10.2f %-10s %-10s\n",
                    c.getName(), c.getBrand(), c.getPrice(), c.getColor(), c.getType());
        }
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        Database.initializeDatabase();
        CsvImporter.importComponentsFromCsv("src/main/resources/combined_components.csv");

        App app = new App();
        app.userManager = new UserManager(Database.getConnection());
        app.startCLI();
    }
}
