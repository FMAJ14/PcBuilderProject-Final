package org.example.db;

public class Component {
    private String name;
    private String brand;
    private double price;
    private String color;
    private String type;

    public Component(String name, String brand, double price, String color, String type) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.color = color;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }
}
