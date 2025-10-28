package com.example.otams;

/** Basic request class
 *
 * @author Lauren Hendley [lhend093@uottawa.ca]
 */
public class Request {
    // Instantiating variables
    private User user;
    private boolean approval;

    /** Constructor for request object
     * @param user
     */
    public Request(User user){
        this.user = user;
        this.approval = false;
    }

    /** Set approval to approved
     */
    public void setApprovalApproved(){
        this.approval = true;
    }

    /** Set approval to not approved
     */
    public void setApprovalNotApproved(){
        this.approval = false;
    }

    /** [GETTER] Gets the approval of the request
     * @return approval
     */
    public boolean getApproval(){
        return this.approval;
    }

    /** [GETTER] Gets the user of the request
     * @return user
     */
    public User getUser(){
        return this.user;
    }
}