package com.example.otams;

/**
 * Model class representing a student's session
 */
public class StudentSession {
    private String requestId;
    private String slotId;
    private String course;
    private String tutorEmail;
    private int date;
    private int startTime;
    private int endTime;
    private String status; // pending, approved, rejected, cancelled

    public StudentSession(String requestId, String slotId, String course, String tutorEmail,
                          int date, int startTime, int endTime, String status) {
        this.requestId = requestId;
        this.slotId = slotId;
        this.course = course;
        this.tutorEmail = tutorEmail;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getSlotId() {
        return slotId;
    }

    public String getCourse() {
        return course;
    }

    public String getTutorEmail() {
        return tutorEmail;
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

    public String getStatus() {
        return status;
    }

    public boolean canCancel() {
        // Only allow cancel for pending or approved sessions
        return "pending".equals(status) || "approved".equals(status);
    }
}