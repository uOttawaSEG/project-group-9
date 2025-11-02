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
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;
    private String status;
    private String userId;
    private String program;
    private String degree;
    private List<String> courses;
    private String password;
    private Timestamp timestamp;

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
    public RegistrationRequest(String firstName, String lastName, String email, String password, String phoneNumber, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = "pending";
        this.timestamp = Timestamp.now();
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
    public String getFirstName() { return firstName; }

    /** [SETTER] Sets the first name
     * @param firstName
     */
    @PropertyName("firstName")
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** [GETTER] Gets the last name
     * @return lastName
     */
    @PropertyName("lastName")
    public String getLastName() { return lastName; }

    /** [SETTER] Sets the last name
     * @param lastName
     */
    @PropertyName("lastName")
    public void setLastName(String lastName) { this.lastName = lastName; }

    /** [GETTER] Gets the email
     * @return email
     */
    @PropertyName("email")
    public String getEmail() { return email; }

    /** [SETTER] Sets the email
     * @param email
     */
    @PropertyName("email")
    public void setEmail(String email) { this.email = email; }

    /** [GETTER] Gets the phone number
     * @return phoneNumber
     */
    @PropertyName("phoneNumber")
    public String getPhoneNumber() { return phoneNumber; }

    /** [SETTER] Sets the phone number
     * @param phoneNumber
     */
    @PropertyName("phoneNumber")
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    /** [GETTER] Gets the role
     * @return role
     */
    @PropertyName("role")
    public String getRole() { return role; }

    /** [SETTER] Sets the role
     * @param role
     */
    @PropertyName("role")
    public void setRole(String role) { this.role = role; }

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
    public String getUserId() { return userId; }

    /** [SETTER] Sets the userId
     * @param userId
     */
    @PropertyName("userId")
    public void setUserId(String userId) { this.userId = userId; }

    /** [GETTER] Gets program
     * @return program
     */
    @PropertyName("program")
    public String getProgram() { return program; }

    /** [SETTER] Sets program
     * @param program
     */
    @PropertyName("program")
    public void setProgram(String program) { this.program = program; }

    /** [GETTER] Gets degree
     * @return degree
     */
    @PropertyName("degree")
    public String getDegree() { return degree; }

    /** [SETTER] Sets degree
     * @param degree
     */
    @PropertyName("degree")
    public void setDegree(String degree) { this.degree = degree; }

    /** [GETTER] Gets courses
     * @return courses
     */
    @PropertyName("courses")
    public List<String> getCourses() { return courses; }

    /** [SETTER] Sets courses
     * @param courses
     */
    @PropertyName("courses")
    public void setCourses(List<String> courses) { this.courses = courses; }

    /** [GETTER] Gets password
     * @return password
     */
    @PropertyName("password")
    public String getPassword() { return password; }

    /** [SETTER] Sets password
     * @param password
     */
    @PropertyName("password")
    public void setPassword(String password) { this.password = password; }

    /** [GETTER] Gets timestamp
     * @return timestamp
     */
    @PropertyName("timestamp")
    public String getPassword() { return timestamp; }

    /** [SETTER] Sets timestamp
     * @param timestamp
     */
    @PropertyName("timestamp")
    public void setPassword(Timestamp timestamp) { this.timestamp = timestamp; }



    /////-------      HELPER METHODS      -------/////




    /** Helper method to get courses as a comma-separated string
     * @return courses
     */
    public String getCoursesAsString() {
        if (courses == null || courses.isEmpty()) {
            return "N/A";
        }
        return String.join(", ", courses);
    }
}