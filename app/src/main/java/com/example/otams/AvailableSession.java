package com.example.otams;

/**
 * Model class representing an available tutoring session
 */
public class AvailableSession {
    private String slotId;
    private String tutorEmail;
    private String tutorId;
    private int date;
    private int startTime;
    private int endTime;
    private String course;
    private boolean requiresApproval;

    public AvailableSession(String slotId, String tutorEmail, String tutorId, int date,
                            int startTime, int endTime, String course, boolean requiresApproval) {
        this.slotId = slotId;
        this.tutorEmail = tutorEmail;
        this.tutorId = tutorId;
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
        return tutorEmail;
    }

    public String getTutorId() {
        return tutorId;
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
