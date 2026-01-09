package com.example.comp2000restuarantapp;

public class User {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String contact;
    private String userType;

    public User() {
    }

    public User(String username, String password, String firstname, String lastname, String email, String contact, String userType) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contact = contact;
        this.userType = userType;
    }

    // Legacy constructor compatibility (optional, but good to keep if used elsewhere)
    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
}