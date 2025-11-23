package com.example.otams;

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
    public ArrayList<Session> viewUpcomingSessions(){return  this.upcomingSessions;}

    /**
     *[GETTER] Retrieves all past sessions for the user.
     *
     * @return pastSessions a list of past Session objects
     */
    public ArrayList<Session> viewPastSessions(){return this.pastSessions;}

    public void rateTutor(Tutor tutor, int stars){
        tutor.addRating(stars);
}

}