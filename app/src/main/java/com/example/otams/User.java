package com.example.otams;
import com.google.firebase.firestore.PropertyName;
import java.util.List;

/** Basic User class
 * 
 * @author Sophia
 * @author Lauren
 */
public abstract class User {
    // Instatiating variables 
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String role;
    private String program;
    private List<String> courses;
    private String degree;
    private String userId;
    private String status;

    /** Empty constructor for firebase
     */
    public User() {
    }

    /** Parameterized constructor for User object
     * @param email
     */
    public User(String email) {
        this.email = email;
        this.status = "pending";
    }


    /////-------      MAIN METHODS      -------/////


    /** [GETTER] Returns the firstName
     * @return firstName
     */
    @PropertyName("firstName")
    public String getFirstName() { return firstName; }

    /** [SETTER] Sets the first name
     * @param firstName
     */
    @PropertyName("firstName")
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** [GETTER] Returns the last name
     * @return lastName
     */
    @PropertyName("lastName")
    public String getLastName() { return lastName; }

    /** [SETTER] Sets the last name
     * @param lastName
     */
    @PropertyName("lastName")
    public void setLastName(String lastName) { this.lastName = lastName; }

    /** [GETTER] Returns the phone number
     * @return phoneNumber
     */
    @PropertyName("phoneNumber")
    public String getPhoneNumber() { return phoneNumber; }

    /** [SETTER] Sets the phone number
     * @param phoneNumber
     */
    @PropertyName("phoneNumber")
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    /** [GETTER] Returns the email
     * @return email
     */
    @PropertyName("email")
    public String getEmail() { return email; }

    /** [SETTER] Sets the email
     * @param email
     */
    @PropertyName("email")
    public void setEmail(String email) { this.email = email; }

    /** [GETTER] Returns the role
     * @return role
     */
    @PropertyName("role")
    public String getRole() { return role; }

    /** [SETTER] Sets the role
     * @param role
     */
    @PropertyName("role")
    public void setRole(String role) { this.role = role; }

    /** [GETTER] Returns the degree
     * @return degree
     */
    @PropertyName("degree")
    public String getDegree() { return degree; }

    /** [SETTER] Sets the degree
     * @param degree
     */
    @PropertyName("degree")
    public void setDegree(String degree) { this.degree = degree; }

    /** [GETTER] Returns the courses
     * @return courses
     */
    @PropertyName("courses")
    public List<String> getCourses() { return courses; }

    /** [SETTER] Sets the courses
     * @param courses
     */
    @PropertyName("courses")
    public void setCourses(List<String> courses) { this.courses = courses; }

    /** [GETTER] Returns the program
     * @return program
     */
    @PropertyName("program")
    public String getProgram() { return program; }

    /** [SETTER] Sets the program
     * @param program
     */
    @PropertyName("program")
    public void setProgram(String program) { this.program = program; }

    /** [GETTER] Returns the userId
     * @return userId
     */
    @PropertyName("userId")
    public String getUserId() { return userId; }

    /** [SETTER] Sets the userId
     * @param userId
     */
    @PropertyName("userId")
    public void setUserId(String userId) { this.userId = userId; }

    /** [GETTER] Returns the status
     * @return status
     */
    @PropertyName("status")
    public String getStatus() { return status; }

    /** [SETTER] Sets the status
     * @param status
     */
    @PropertyName("status")
    public void setStatus(String status) { this.status = status; }




}
