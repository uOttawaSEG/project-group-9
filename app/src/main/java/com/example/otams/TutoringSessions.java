package com.example.otams;

/**
 * Basic session class
 *
 * @author Lauren Hendley [lhend093@uottawa.ca]
 */
public class TutoringSessions {
    // Instatiated variables
    private int approval;
    private int startTime;
    private int endTime;
    private Tutor tutor;
    private Student student;


    public TutoringSessions(int approval, int endTime, int startTime, Tutor tutor, Student student) {
        this.approval = approval;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tutor = tutor;
        this.student = student;
    }


    public TutoringSessions() {
        this.approval = 0;
    }


    public void setApproval(int approved) {
        this.approval = approved;
    }


    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }


    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }


    public int getApproval() {
        return this.approval;
    }


    public int getStartTime() {
        return this.startTime;
    }


    public int getEndTime() {
        return this.endTime;
    }


    public Tutor getTutor() {
        return this.tutor;
    }


    public Student getStudent() {
        return this.student;
    }


}
