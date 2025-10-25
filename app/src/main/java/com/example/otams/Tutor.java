package com.example.otams;

import java.util.ArrayList;

/**
 * Basic tutor class
 *
 * @author Lauren Hendley [lhend093@uottawa.ca]
 */
public class Tutor extends User {
    private int rating;
    private ArrayList<String> coursesOffered;
    private String degree;

    public Tutor(String email) {
        super(email);
    }

    /**
     * Parameterized constructor
     */
    public Tutor(String firstName, String lastName, int phoneNumber, ArrayList<String> coursesOffered, String degree) {
        super(firstName, lastName, phoneNumber);
        this.coursesOffered = coursesOffered;
        this.degree = degree;
    }


    /**
     * [GETTER] Returns the tutor's rating
     *
     * @return the rating
     */
    public int getRating() {
        return this.rating;
    }

    /**
     * [GETTER] Returns the coursesOffered
     *
     * @return the coursesOffered
     */
    public ArrayList<String> getCoursesOffered() {
        return this.coursesOffered;
    }

    /**
     * [GETTER] Returns their degree
     *
     * @return the degree
     */
    public String getDegree() {
        return this.degree;
    }

    /**
     * [SETTER] Sets the rating
     *
     * @param rating
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * [SETTER] Sets the courses offered
     *
     * @param coursesOffered
     */
    public void setCoursesOffered(ArrayList<String> coursesOffered) {
        this.coursesOffered = coursesOffered;
    }

    /**
     * [SETTER] Sets the degree
     *
     * @param degree
     */
    public void setDegree(String degree) {
        this.degree = degree;
    }


    //// ----- MAIN METHODS ----- \\\\


    /**
     * Approves the session request with a 1 for approved, 0 for not, and -1 for an error
     *
     * @param session
     * @param approved
     * @return integer of approval
     */
    public int approveSessionRequest(TutoringSessions session, int approved) {
        if (approved == 1) {
            session.setApproval(1);
            return 1;
        } else if (approved == 0) {
            session.setApproval(0);
            return 0;
        }
        return -1;
    }

    /**
     * Automatically approves all sessions
     *
     * @param listOfSessions
     * @return count of all sessions
     */
    public int approveAllSessionRequests(ArrayList<TutoringSessions> listOfSessions) {
        for (TutoringSessions session : listOfSessions) {
            approveSessionRequest(session, 1);
        }
        return listOfSessions.size();
    }
}
