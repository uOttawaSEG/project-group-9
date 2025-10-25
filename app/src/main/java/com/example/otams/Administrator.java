package com.example.otams;

import java.util.ArrayList;
import java.util.List;

/** Administrator class
 * 
 * @author Lauren Hendley [lhend093@uottawa.ca]
 */
public class Administrator extends User {
    // Instantiating variables
    private List<Request> requests = new ArrayList<>();
    private String email;
    private String password;

    /** Constructor for the administrator object
     * @param email
     * @param password
     */
    public Administrator(String email, String password){
        this.email = email;
        this.password = password;
    }

    /** [GETTER] Returns the list of requests
     * @return list of requests
     */
    public List<Request> getRequests(){
        return this.requests;
    }

    /** Approves all the requests in the list of requests
     */
    public void approveAll(){
        for(Request r : requests){
            r.setApprovalApproved();
        }
    }

    /** Denys all the requests in the list of requests
     */
    public void denyAll(){
        for(Request r : requests){
            r.setApprovalNotApproved();
        }
    }

    /** Approves a certain request under a user's email
     * @param userEmail
     */
    public void approve(String userEmail){
        for(Request r : requests){
            if(r.getUser().getEmail == userEmail){
                r.setApprovalApproved();
                return;
            }
        }
    }

    /** Denies a certain request under a user's email
     * @param userEmail
     */
    public void deny(String userEmail){
        for(Request r : requests){
            if(r.getUser().getEmail == userEmail){
                r.setApprovalNotApproved();
                return;
            }
        }
    }
}
