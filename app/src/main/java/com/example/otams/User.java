package com.example.otams;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
    private String firstName;
    private String lastName;
    private String password;
    private int phoneNumber;
    private String email;
    private List<String> pastSessions;
    private List<String> upcomingSessions;

    //---Constructors---
    public User() {
        this.pastSessions = new ArrayList<>();
        this.upcomingSessions = new ArrayList<>();
    }

    public User(String firstName, String lastName, String password, int phoneNumber, String email, ArrayList<tutoringSessions> pastSessions, ArrayList<tutoringSessions> upcomingSessions) {
    }

    //----Getters and Setters----

    //first name
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    //last name
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    //password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //phone number
    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //past sessions
    public List<String> getPastSessions() {
        return pastSessions;
    }

    public void setPastSessions(List<String> pastSessions) {
        this.pastSessions = pastSessions;
    }

    //upcoming sessions
    public List<String> getUpcomingSessions() {
        return upcomingSessions;
    }

    public void setUpcomingSessions(List<String> upcomingSessions) {
        this.upcomingSessions = upcomingSessions;
    }

    //Methods
    //view past sessions
    public List<String> viewPastSessions() {
        return pastSessions;
    }

    //view upcoming sessions
    public List<String> viewUpcomingSessions() {
        return upcomingSessions;
    }

    //cancel sessions
    public boolean cancelSession(String sessionName) {
        return upcomingSessions.remove(sessionName);
    }

    //approve registration
    public void approveRegistration(String userEmail) {
        System.out.println("Approved registration for:" + userEmail);
    }

    //validating programming
    public boolean validateProgram(String program) {
        return program != null && !program.trim().isEmpty();
    }
    /*
    //validate course
    public boolean validateCourse(String course){
        return course!= null && !course.trim().isEmpty();
    }
    //validating degree
    public boolean validateDegree(String degree){
        return course!= null && !course.trim().isEmpty();
    }
     */
}
