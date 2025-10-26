package com.example.otams;

import java.util.ArrayList;
import java.util.List;

public class Tutor extends User {
    private List<String> coursesOffered = new ArrayList<>();
    private String degree;

    public Tutor() {
        super();
    }

    public Tutor(String email) {
        super(email);
    }

    public List<String> getCoursesOffered() {
        return coursesOffered;
    }

    public void setCoursesOffered(List<String> coursesOffered) {
        this.coursesOffered = coursesOffered;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

//    public void approveSessionRequest(TutoringSessions session, int approved) {
//        if (approved == 1) {
//            session.setApproval(1);
//        } else if (approved == 0) {
//            session.setApproval(0);
//        }
//    }
//
//    public int approveAllSessionRequests(ArrayList<TutoringSessions> listOfSessions) {
//        for (TutoringSessions session : listOfSessions) {
//            approveSessionRequest(session, 1);
//        }
//        return listOfSessions.size();
//    }
}
