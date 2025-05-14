package org.example;

import org.example.db.Component;
import org.example.db.Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PcBuilder {
    private final Scanner scanner = new Scanner(System.in);
    private final List<Component> selectedComponents = new ArrayList<>();
    private double budget;
    private double totalCost = 0.0;
    private final String username;

    public PcBuilder(String username) {
        this.username = username;
    }


    public void start() {
        System.out.print("Enter your budget: ");
        budget = Double.parseDouble(scanner.nextLine().trim());
try {
    boolean building = true;
    while (building) {
        System.out.println("\n--- Available Actions ---");
        System.out.println("1. View all components");
        System.out.println("2. Add component by name");
        System.out.println("3. View selected components");
        System.out.println("4. Finish and checkout");
        System.out.println("5. Save build to favorites");
        System.out.println("6. View my favorite builds");
        System.out.print("Choose an option: ");
        int choice = Integer.parseInt(scanner.nextLine().trim());

        switch (choice) {
            case 1 -> viewAllComponents();
            case 2 -> addComponentByName();
            case 3 -> viewSelectedComponents();
            case 4 -> {
                building = false;
                finishBuild();

            }
            case 5 -> saveBuild();
            case 6 -> viewFavorites();


            default -> System.out.println("Invalid option.");
        }
    }
}catch (NumberFormatException e){
    System.out.println("Error: Enter a valid option");
}

    }
    private void viewFavorites() {
        List<Component> favorites = Database.getFavoritesForUser(username);
        if (favorites.isEmpty()) {
            System.out.println("You have no saved favorites yet.");
            return;
        }

        System.out.println("\n=== Your Favorite Build Components ===");
        System.out.printf("%-20s %-15s %-10s %-10s %-10s\n", "Name", "Brand", "Price", "Color", "Type");
        System.out.println("-".repeat(70));
        for (Component c : favorites) {
            System.out.printf("%-20s %-15s %-10.2f %-10s %-10s\n",
                    c.getName(), c.getBrand(), c.getPrice(), c.getColor(), c.getType());
        }
    }

    private void saveBuild() {
        if (selectedComponents.isEmpty()) {
            System.out.println(" No components to save.");
            return;
        }

        new Thread(() -> {
            Database.saveBuildToFavorites(username, selectedComponents);
        }).start();

    }


    private void viewAllComponents() {
        List<Component> all = Database.getAllComponents(); // You need to add this method
        if (all.isEmpty()) {
            System.out.println("No components available.");
            return;
        }

        System.out.printf("%-20s %-15s %-10s %-10s %-10s\n", "Name", "Brand", "Price", "Color", "Type");
        System.out.println("-".repeat(70));
        for (Component c : all) {
            System.out.printf("%-20s %-15s %-10.2f %-10s %-10s\n",
                    c.getName(), c.getBrand(), c.getPrice(), c.getColor(), c.getType());
        }
    }

    private void addComponentByName() {
        System.out.print("Enter component name: ");
        String name = scanner.nextLine().trim();
        List<Component> results = Database.searchByName(name);

        if (results.isEmpty()) {
            System.out.println("No component found.");
            return;
        }

        Component comp = results.get(0);
        if (totalCost + comp.getPrice() > budget) {
            System.out.printf("Cannot add '%s'. It exceeds your budget (%.2f remaining).\n", comp.getName(), budget - totalCost);
        } else {
            selectedComponents.add(comp);
            totalCost += comp.getPrice();
            System.out.printf("Added '%s'. Total cost: %.2f\n", comp.getName(), totalCost);
        }
    }

    private void viewSelectedComponents() {
        if (selectedComponents.isEmpty()) {
            System.out.println("No components selected.");
            return;
        }

        System.out.println("--- Selected Components ---");
        for (Component c : selectedComponents) {
            System.out.printf("%-20s %-10.2f %-10s\n", c.getName(), c.getPrice(), c.getType());
        }
        System.out.printf("Total Cost: %.2f | Remaining Budget: %.2f\n", totalCost, budget - totalCost);
    }

    private void finishBuild() {
        System.out.println("\n=== Final PC Build ===");
        viewSelectedComponents();
        System.out.println("Thank you for using PcBuilder!");
    }
}
