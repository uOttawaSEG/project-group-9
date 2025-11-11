package com.example.otams;

/**
 * Model class representing a session request from a student
 */
public class SessionRequest {
    private String requestId;
    private String studentId;
    private String studentEmail;
    private String studentFirstName;
    private String studentLastName;
    private String studentPhone;
    private String course;
    private int date;
    private int startTime;
    private int endTime;

    public SessionRequest(String requestId, String studentId, String studentEmail,
                          String studentFirstName, String studentLastName, String studentPhone,
                          String course, int date, int startTime, int endTime) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.studentEmail = studentEmail;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.studentPhone = studentPhone;
        this.course = course;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public String getStudentFullName() {
        return studentFirstName + " " + studentLastName;
    }

    public String getCourse() {
        return course;
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
}