package com.example.otams;

/**
 * Basic Student class
 *
 * @author Sophia Hopkins [shopk012@uottawa.ca]
 */
public class Student extends User {
    private String program;

    /**
     * Parameterized constructor
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param phoneNumber
     * @param degree
     */
    public Student(String firstName, String lastName, String email,
                   String password, int phoneNumber, String program) {
        //Calls the constructor from the User class to set shared fields
        super(firstName, lastName, email, password, phoneNumber);
        this.program = program;
    }

    //// -----HELPER METHODS-----\\\\
    /**
     * [GETTER] Returns student's program
     * @return the program
     */
    public String getProgram() {
        return program;
    }
    /**
     * [SETTER] Sets the student's program
     * @param program
     */
    public void setProgram(String program) {
        this.program = program;
    }

    //---Student methods---
    /**
     * Allows the student o rate a tutor.
     *
     *
     */
    public void rateTutor(Tutor tutor, int rating) {
        //tutor.addRating(rating);
    }

    /**
     * Allows the student to book a session.
     *
     *
     */
    public void bookSession() {
        System.out.println("Student booked a session.");
    }
}
