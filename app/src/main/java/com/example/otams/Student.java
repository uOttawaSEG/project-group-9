package com.example.otams;

public class Student extends User {
    private String program;

    public Student() {
        super();
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }
//    public void rateTutor() {
//        System.out.println("Student rated a tutor.");
//    }
//
//    public void bookSession() {
//        System.out.println("Student booked a session.");
//    }
}
