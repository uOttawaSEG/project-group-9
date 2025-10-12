package com.example.otams;

/**
 * Basic session class
 *
 * @author Lauren Hendley [lhend093@uottawa.ca]
 */
public class tutoringSessions {
    // Instatiated variables
    private int approval;
    private int startTime;
    private int endTime;
    private Tutor tutor;
    private Student student;

    /**
     * Parameterized constructor
     *
     * @param approval
     * @param endTime
     * @param startTime
     * @param tutor
     * @param student
     */
    public tutoringSessions(int approval, int endTime, int startTime, Tutor tutor, Student student) {
        this.approval = approval;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tutor = tutor;
        this.student = student;
    }

    /**
     * Empty constructor
     */
    public tutoringSessions() {
        this.approval = 0;
    }


    //// ----- HELPER METHODS ----- \\\\


    /**
     * [SETTER] Sets the approval
     *
     * @param approved
     */
    public void setApproval(int approved) {
        this.approval = approved;
    }

    /**
     * [SETTER] Sets the end time
     *
     * @param endTime
     */
    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    /**
     * [SETTER] Sets the start time
     *
     * @param startTime
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    /**
     * [GETTER] Returns the approval
     *
     * @return approval
     */
    public int getApproval() {
        return this.approval;
    }

    /**
     * [GETTER] Returns the start time
     *
     * @return start time
     */
    public int getStartTime() {
        return this.startTime;
    }

    /**
     * [GETTER] Returns the end time
     *
     * @return end time
     */
    public int getEndTime() {
        return this.endTime;
    }

    /**
     * [GETTER] Returns the tutor object
     *
     * @return tutor
     */
    public Tutor getTutor() {
        return this.tutor;
    }

    /**
     * [GETTER] Returns the student object
     *
     * @return student
     */
    public Student getStudent() {
        return this.student;
    }


}
