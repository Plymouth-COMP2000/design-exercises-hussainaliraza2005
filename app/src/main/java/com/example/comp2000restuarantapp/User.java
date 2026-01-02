package com.example.comp2000restaurantapp;

public class User {
    private String username;
    private String password;
    private String userType;

    /**
     * Creating a new User.
     * @param username The user's login identifier (email or username).
     * @param password The user's password.
     * @param userType The role of the user.
     */
    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
}