package org.example.io;

import org.example.db.Component;
import org.example.db.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvImporter {

    public static void importComponentsFromCsv(String filePath) {
        List<Component> componentList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] fields = line.split(",");
                if (fields.length < 5) continue;

                String name = fields[0].trim();
                String brand = fields[1].trim();
                String priceStr = fields[2].trim();
                double price = 0.0;

                if (!priceStr.isEmpty()) {
                    try {
                        price = Double.parseDouble(priceStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price: " + priceStr + " on line: " + line);
                        continue;
                    }
                } else {
                    System.out.println("Empty price on line: " + line);
                    continue;
                }

                String color = fields[3].trim();
                String type = fields[4].trim();

                Component component = new Component(name, brand, price, color, type);
                componentList.add(component);
            }

            Database.insertComponent(componentList);
            System.out.println("Imported " + componentList.size() + " components.");

        } catch (IOException e) {
            System.err.println("CSV import error: " + e.getMessage());
        }
    }
}