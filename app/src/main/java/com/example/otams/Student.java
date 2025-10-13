package com.example.otams;

public class Student extends User {
    private String program;

    //---Constructors---
    //This is required by Firebase
    public Student() {
        super();
    }

    public Student(String firstName, String lastName, String email,
                   String password, int phoneNumber, String program) {
        //Calls the constructor from the User class to set shared fields
        super(firstName, lastName, email, password, phoneNumber);
        this.program = program;
    }

    //---Getter and Setter for program---
    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    //---Student methods---
    public void rateTutor() {
        System.out.println("Student rated a tutor.");
    }

    public void bookSession() {
        System.out.println("Student booked a session.");
    }
}
