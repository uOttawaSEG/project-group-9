/** Basic session class
 * 
 * @author Lauren Hendley [lhend093@uottawa.ca]
 */
public class Session {
    // Instatiated variables
    int approval;
    int startTime;
    int endTime;
    Tutor tutor;
    Student student;

    /** Parameterized constructor
     * @param approval
     * @param endTime
     * @param startTime
     * @param tutor
     * @param student
     */
    public Session(int approval, int endTime, int startTime, Tutor tutor, Student student){
        this.approval = approval;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tutor = tutor;
        this.student = student;
    }

    /** Empty constructor
     */
    public Session(){
        this.approval = 0;
    }



    //// ----- HELPER METHODS ----- \\\\
    


    /** [SETTER] Sets the approval
     * @param approved
     */
    public void setApproval(int approved){
        this.approval = approved;
    }

    /** [SETTER] Sets the end time
     * @param endTime
     */
    public void setEndTime(int endTime){
        this.endTime = endTime;
    }

    /** [SETTER] Sets the start time
     * @param startTime
     */
    public void setStartTime(int startTime){
        this.startTime = startTime;
    }
}
