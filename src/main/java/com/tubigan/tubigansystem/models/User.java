package com.tubigan.tubigansystem.models;

public class User {
    private int id;
    private String fullName;
    private String email;
    private String role; // "ADMIN" or "CUSTOMER"

    public User(int id, String fullName, String email, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    // Getters
    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}