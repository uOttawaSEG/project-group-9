package com.example.otams;

/** Basic session class (extends slot class)
 *
 * @author Lauren Hendley [lhend093@uottawa.ca]
 */
public class Session extends Slot {
    // Instatiated variables
    private string approval;
    private Student student;

    /** Parameterized constructor for session object
     */
    public Session(Student student) {
        super(tutor);
        this.student = student;
        this.approval = "pending";
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
}
