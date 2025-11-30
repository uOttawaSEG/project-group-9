package com.example.otams;

/**
 * Model class representing an available tutoring session
 */
public class AvailableSession {
    private Tutor tutor;
    private String slotId;
    private String tutorId;
    private int date;
    private int startTime;
    private int endTime;
    private String course;
    private boolean requiresApproval;
    
    public AvailableSession(String slotId, Tutor tutor, int date, int startTime, int endTime, String course, boolean requiresApproval) {
        this.slotId = slotId;
        this.tutor = tutor;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.course = course;
        this.requiresApproval = requiresApproval;
    }

    public String getSlotId() {
        return slotId;
    }

    public String getTutorEmail() {
        return tutor.getEmail();
    }

    public String getTutorId() {
        return tutor.getId();
    }

    public int getDate() {
        return date;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public String getCourse() {
        return course;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }
}
