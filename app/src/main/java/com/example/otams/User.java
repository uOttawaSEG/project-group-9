package com.example.otams;

public abstract class User {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    private String role;

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
