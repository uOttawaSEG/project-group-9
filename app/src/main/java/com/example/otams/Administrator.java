package com.example.otams;

import java.util.ArrayList;
import java.util.List;

/**
 * Administrator class
 *
 * @author Lauren Hendley [lhend093@uottawa.ca]
 */
public class Administrator extends User {
    // Instantiating variables
    private final List<RegistrationRequest> requests = new ArrayList<>();
    private String email = "admin@example.com";

    /**
     * Constructor for the administrator object
     *
     * @param email
     */
    public Administrator(String email) {
        super(email);
    }

    /**
     * [GETTER] Returns the list of requests
     *
     * @return list of requests
     */
    public List<RegistrationRequest> getRequests() {
        return this.requests;
    }

    /**
     * Approves all the requests in the list of requests
     */
    public void approveAll() {
        for (RegistrationRequest r : requests) {
            r.setStatus("approved");
        }
    }

    /**
     * Denys all the requests in the list of requests
     */
    public void denyAll() {
        for (RegistrationRequest r : requests) {
            r.setStatus("rejected");
        }
    }

    /** Approves a certain request under a user's email
     * @param userEmail
     */
    public void approve(String userEmail) {
        for (RegistrationRequest r : requests) {
            if (r.getEmail() == userEmail) {
                r.setStatus("approved");
                return;
            }
        }
    }

    /**
     * Denies a certain request under a user's email
     *
     * @param userEmail
     */
    public void deny(String userEmail) {
        for (RegistrationRequest r : requests) {
            if (r.getEmail() == userEmail) {
                r.setStatus("rejected");
                return;
            }
        }
    }
}
