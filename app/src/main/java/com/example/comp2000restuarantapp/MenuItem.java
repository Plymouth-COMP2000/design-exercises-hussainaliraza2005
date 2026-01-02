package com.example.comp2000restuarantapp;

/**
 * Model class representing a food item on the menu.
 * Contains details such as name, price, and description.
 */
public class MenuItem {
    private int id;
    private String name;
    private double price;
    private String description;

    /**
     * Constructor for items retrieved from the database (with ID).
     */
    public MenuItem(int id, String name, double price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    /**
     * Constructor for creating new items before they are saved to DB.
     */
    public MenuItem(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}