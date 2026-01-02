package com.example.comp2000restuarantapp;

/**
 * Model class representing a table reservation.
 * Links a guest email to a specific time and table number.
 */
public class Reservation {
    private int id;
    private String guestName;
    private String time;
    private String tableNumber;

    /**
     * Full constructor including Database ID.
     */
    public Reservation(int id, String guestName, String time, String tableNumber) {
        this.id = id;
        this.guestName = guestName;
        this.time = time;
        this.tableNumber = tableNumber;
    }

    /**
     * Constructor for creating a new reservation.
     */
    public Reservation(String guestName, String time, String tableNumber) {
        this.guestName = guestName;
        this.time = time;
        this.tableNumber = tableNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getTableNumber() { return tableNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
}