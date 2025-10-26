package com.example.otams;

public abstract class User {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    private String role;

    protected User() {
    }

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
