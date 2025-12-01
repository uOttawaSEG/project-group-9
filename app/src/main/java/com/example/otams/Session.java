package com.example.otams;

/** Basic session class (extends slot class)
 *
 * @author Lauren Hendley [lhend093@uottawa.ca]
 */
public class Session extends Slot {
    // Instatiated variables
    private String approval;
    private String course;
    private Student student;
    private boolean completed;
    private boolean rated;
    private int rating;



    /** Parameterized constructor for session object
     * @param student
     */
    public Session(Tutor tutor, Student student) {
        super(tutor);
        this.student = student;
        this.course = course;
        this.approval = "pending";
        this.rated = false;
        this.rating = 0;
    }

    /** [SETTER] Sets the approval
     * @param approval
     */
    public void setApproval(String approval) { this.approval = approval; }

    /** [GETTER] Gets the approval
     * @return approval
     */
    public String getApproval() { return this.approval; }

    /** [GETTER] Gets the student object
     * @return student
     */
    public Student getStudent() { return this.student; }

    public String getCourse() { return this.course; }

    public void setCourse(String course) {this.course = course; }

    /** [SETTER] Sets the status of the rating and the rating.
     * @param rating
     */
    public void setRating(int rating){
        this.rating = rating;
        this.rated = (rating > 0);
    }

    /** [GETTER] Gets the rating.
     * @return rated
     */
    public int getRating(){ return this.rating; }

    /** [GETTER] Gets the status of the rating.
     * @return rated
     */
    public boolean isRated(){ return this.rated; }
}
