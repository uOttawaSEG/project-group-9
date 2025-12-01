package com.example.otams;

import com.google.firebase.firestore.PropertyName;
import java.util.List;

/** Basic object class for registration request
 * 
 * @author 
 * @author Lauren Hendley [lhend093@uottawa.ca]
 */
public class RegistrationRequest {
    // Instatiating variables
    private String requestId;
    private String status;
    private User user;

    /** Empty constructor for Firestore
     */
    public RegistrationRequest() {
    }

    /** Fully parameterized constructor for Registration Request object
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @param phoneNumber
     * @param role
     */
    public RegistrationRequest(User user) {
        this.user = user;
        this.status = "pending";
    }



    /////-------      MAIN METHODS      -------/////



    /** [GETTER] Returns request id
     * @return requestId
     */
    public String getRequestId() { return requestId; }

    /** [SETTER] Sets request id
     * @param requestId
     */
    public void setRequestId(String requestId) { this.requestId = requestId; }

    /** [GETTER] Gets the first name
     * @return firstName
     */
    @PropertyName("firstName")
    public String getFirstName() { return user.getFirstName(); }

    /** [SETTER] Sets the first name
     * @param firstName
     */
    @PropertyName("firstName")
    public void setFirstName(String firstName) { user.setFirstName(firstName); }

    /** [GETTER] Gets the last name
     * @return lastName
     */
    @PropertyName("lastName")
    public String getLastName() { return user.getLastName(); }

    /** [SETTER] Sets the last name
     * @param lastName
     */
    @PropertyName("lastName")
    public void setLastName(String lastName) { user.setLastName(lastName); }

    /** [GETTER] Gets the email
     * @return email
     */
    @PropertyName("email")
    public String getEmail() { return user.getEmail(); }

    /** [SETTER] Sets the email
     * @param email
     */
    @PropertyName("email")
    public void setEmail(String email) { user.setEmail(email); }

    /** [GETTER] Gets the phone number
     * @return phoneNumber
     */
    @PropertyName("phoneNumber")
    public String getPhoneNumber() { return user.getPhoneNumber(); }

    /** [SETTER] Sets the phone number
     * @param phoneNumber
     */
    @PropertyName("phoneNumber")
    public void setPhoneNumber(String phoneNumber) { user.setPhoneNumber(phoneNumber); }

    /** [GETTER] Gets the role
     * @return role
     */
    @PropertyName("role")
    public String getRole() { return user.getRole(); }

    /** [SETTER] Sets the role
     * @param role
     */
    @PropertyName("role")
    public void setRole(String role) { user.setRole(role); }

    /** [GETTER] Gets the status
     * @return status
     */
    @PropertyName("status")
    public String getStatus() { return status; }

    /** [SETTER] Sets the status
     * @param status
     */
    @PropertyName("status")
    public void setStatus(String status) { this.status = status; }

    /** [GETTER] Gets the userId
     * @return userId
     */
    @PropertyName("userId")
    public String getUserId() { return user.getUserId(); }

    /** [SETTER] Sets the userId
     * @param userId
     */
    @PropertyName("userId")
    public void setUserId(String userId) { user.setUserId(userId); }

    /** [GETTER] Gets program
     * @return program
     */
    @PropertyName("program")
    public String getProgram() { return user.getProgram(); }

    /** [SETTER] Sets program
     * @param program
     */
    @PropertyName("program")
    public void setProgram(String program) { user.setProgram(program); }

    /** [GETTER] Gets degree
     * @return degree
     */
    @PropertyName("degree")
    public String getDegree() { return user.getDegree(); }

    /** [SETTER] Sets degree
     * @param degree
     */
    @PropertyName("degree")
    public void setDegree(String degree) { user.setDegree(degree); }

    /** [GETTER] Gets courses
     * @return courses
     */
    @PropertyName("courses")
    public List<String> getCourses() { return user.getCourses(); }

    /** [SETTER] Sets courses
     * @param courses
     */
    @PropertyName("courses")
    public void setCourses(List<String> courses) { user.setCourses(courses); }

    /** [GETTER] Gets user
     * @return user
     */
    @PropertyName("user")
    public User getUser() { return user; }

    /** [SETTER] Sets user
     * @param user
     */
    @PropertyName("user")
    public void setUser(User user) { this.user = user; }


    /////-------      HELPER METHODS      -------/////




    /** Helper method to get courses as a comma-separated string
     * @return courses
     */
    public String getCoursesAsString() {
        List<String> courses = user.getCourses();
        if (courses == null || courses.isEmpty()) {
            return "N/A";
        }
        return String.join(", ", courses);
    }
}