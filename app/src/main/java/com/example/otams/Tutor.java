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
    private int totalRatingPoints;
    private int totalRatings;

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

    /** [SETTER] Sets the past sessions of the tutor
     * @param pastSessions
     */
    public void setPastSessions(List<Session> pastSessions){ this.pastSessions = pastSessions; }

    /** [SETTER] Sets the upcoming sessions of the tutor
     * @param upcoming
     */
    public void setUpcomingSession(List<Session> upcoming){ this.upcomingSessions = upcoming; }

    /** [SETTER] Sets the available slots of the tutor
     * @param slots
     */
    public void setAvailableSlots(List<Slot> slots){ this.availableSlots = slots; }

    /** [GETTER] Gets the past sessions of the tutor
     * @return pastSessions
     */
    public List<Session> getPastSessions(){ return pastSessions; }

    /** [GETTER] Gets the upcoming sessions of the tutor
     * @return upcomingSessions
     */
    public List<Session> getUpcomingSessions(){ return upcomingSessions; }

    /** [GETTER] Gets the slots of the tutor
     * @return availableSlots
     */
    public List<Slot> getAvailableSlots(){ return availableSlots; }


    /**
     * [GETTER] Calculates and returns the tutor's average rating.
     * If the tutor has no ratings, this returns 0.0
     *
     * @return the average rating as a double value
     */
    public double calculateAverageRating(){
        if(totalRatings == 0) return 0.0;
        return(double) totalRatingPoints/totalRatings;
    }

    /**
     * [SETTER] Adds a new rating to the tutor.
     * Updates the total rating points and the number of ratings received.
     *
     * @param stars the rating value given by a student (1-5)
     */
    public void addRating(int stars){
        totalRatingPoints += stars;
        totalRatings++;
    }
    //////    -----   HELPER METHODS    -----   //////


    /** Sets the approval of the session
     * @param session 
     * @param approval
     */
    public void approveSessionRequest(Session session, String approval) { session.setApproval(approval); }

    /** Automatically approves all session requests
     * @param listOfSessions
     */
    public void approveAllSessionRequests(List<Session> listOfSessions) {
        for(Session s : listOfSessions){
            s.setApproval("approved");
        }
    }

    /** Creates a new slot for the tutor
     * @param start
     * @param end
     */
    public void createNewSlot(Tutor tutor, int start, int end, int date){
        Slot s = new Slot(tutor, start, end, date);
        availableSlots.add(s);
    }

    //////    -----   RATING METHODS    -----   //////

    /** [GETTER] Gets the total rating points of the tutor
     * @return totalRatingPoints
     */
    public int getTotalRatingPoints(){ return totalRatingPoints; }

    /** [SETTER] Sets the total rating points of the tutor
     * Updates the total rating points of the tutor.
     * @param totalRatingPoints
     */
    public void setTotalRatingPoints(int totalRatingPoints){ this.totalRatingPoints = totalRatingPoints; }

    /** [GETTER] Gets the total ratings of the tutor
     * @return totalRatings
     */
    public int getTotalRatings(){ return totalRatings; }

    /** [SETTER] Sets the total ratings of the tutor
     * Updates the total ratings of the tutor.
     * @param totalRatings
     */
    public void setTotalRatings(int totalRatings){ this.totalRatings = totalRatings; }
}
