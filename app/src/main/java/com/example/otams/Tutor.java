package com.example.otams;

import java.util.ArrayList;
import java.util.List;

/** Basic Tutor class
 * @author Lauren [lhend093@uottawa.ca]
 */
public class Tutor extends User {
    // Instatiated variables
    private List<String> coursesOffered = new ArrayList<>();
    private List<Session> pastSessions = new ArrayList<>();
    private List<Slot> availableSlots = new ArrayList<>();
    private List<Session> upcomingSessions = new ArrayList<>();
    private String degree;

    /** Empty constructor for tutor object
     */
    public Tutor() {
        super();
    }

    /** Constructor with email (for firebase)
     */
    public Tutor(String email) {
        super(email);
    }

    /** [GETTER] Gets the courses offered by the tutor
     * @return courses
     */
    public List<String> getCourses() { return coursesOffered; }

    /** [SETTER] Sets the courses offered by the tutor
     * @param coursesOffered
     */
    public void setCourses(List<String> coursesOffered) { this.coursesOffered = coursesOffered; }

    /** [GETTER] Gets the degree of the tutor
     * @return degree
     */
    public String getDegree() { return degree; }

    /** [SETTER] Sets the degree of the tutor
     * @param degree
     */
    public void setDegree(String degree) { this.degree = degree; }


    /**
     */
    public void approveSessionRequest(Session session, int approved) {
        
    }

    public void approveAllSessionRequests(List<Session> listOfSessions) {

    }
}
