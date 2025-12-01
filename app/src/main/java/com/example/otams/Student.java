package com.example.otams;
import java.util.ArrayList;

/** Basic Student class
 * 
 * @author Lauren [lhend093@uottawa.ca]
 */
public class Student extends User {
    // Instatiating variables
    private String program;

    /**
     * Email constructor for student object (for firebase)
     */
    public Student(String email) {
        super(email);
    }

    /**
     * Empty constructor for student object
     */
    public Student() {
        super();
    }

    /**
     * [GETTER] Gets the program of the student
     *
     * @return program
     */
    public String getProgram() {
        return program;
    }

    /**
     * [SETTER] Sets the program of the student
     *
     * @param program
     */
    public void setProgram(String program) {
        this.program = program;
    }


    /**
     *[GETTER] Retrieves all upcoming sessions for the user.
     *
     * @return upcomingSessions a list of upcoming Session objects
     */

    //public ArrayList<Session> viewUpcomingSessions(){return  this.upcomingSessions;}
    //implement for deliverable 4
    /**
     *[GETTER] Retrieves all past sessions for the user.
     *
     * @return pastSessions a list of past Session objects
     */
    //public ArrayList<Session> viewPastSessions(){return this.pastSessions;}
    //implement for deliverable 4
    /**
     * Allows a student to rate a tutor by providing a star value.
     * This method forwards the rating to the tutor's rating system,
     * where it is added to the tutor's total rating points and
     * increasing their number of received ratings.
     * @param tutor
     * @param stars
     */
    public void rateTutor(Tutor tutor, int stars){
        tutor.addRating(stars);
}

}