package com.example.otams;
import com.google.firebase.firestore.PropertyName;

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
    private String status;
    private String role;

    /** Empty constructor for firebase
     */
    public User() {
    }

    /** Parameterized constructor for User object
     * @param email
     */
    public User(String email) {
        this.email = email;
    }


    /////-------      MAIN METHODS      -------/////


    /** [GETTER] Returns the status
     * @return status
     */
    @PropertyName("status")
    public String getStatus(){ return this.status; }

    /** [SETTER] Sets the status
     * @param status
     */
    @PropertyName("status")
    public void setStatus(String status){ this.status = status; }

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
}
