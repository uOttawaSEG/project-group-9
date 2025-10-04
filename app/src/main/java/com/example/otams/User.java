package com.example.otams;
import java.util.ArrayList;
import java.util.List;

public abstract class User {
    private String firstName;
    private String lastName;
    private String password;
    private int phoneNumber;
    private String email;
    private List<String> pastSessions;
    private List<String> upcomingSessions;

    //Constructors
    public User(){
        this.pastSessions = new ArrayList<>();
        this.upcomingSessions = new ArrayList<>();
    }

    //Methods
    public
}
