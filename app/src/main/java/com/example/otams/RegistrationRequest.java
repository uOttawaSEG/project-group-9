package com.example.otams;

import com.google.firebase.firestore.PropertyName;
import java.util.List;

public class RegistrationRequest {
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

    // Empty constructor needed for Firestore
    public RegistrationRequest() {
    }

    public RegistrationRequest(String firstName, String lastName, String email,
                               String password, String phoneNumber, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = "pending";
    }

    // Getters and Setters - IMPORTANT: PropertyName on getters/setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    @PropertyName("firstName")
    public String getFirstName() { return firstName; }
    @PropertyName("firstName")
    public void setFirstName(String firstName) { this.firstName = firstName; }

    @PropertyName("lastName")
    public String getLastName() { return lastName; }
    @PropertyName("lastName")
    public void setLastName(String lastName) { this.lastName = lastName; }

    @PropertyName("email")
    public String getEmail() { return email; }
    @PropertyName("email")
    public void setEmail(String email) { this.email = email; }

    @PropertyName("phoneNumber")
    public String getPhoneNumber() { return phoneNumber; }
    @PropertyName("phoneNumber")
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @PropertyName("role")
    public String getRole() { return role; }
    @PropertyName("role")
    public void setRole(String role) { this.role = role; }

    @PropertyName("status")
    public String getStatus() { return status; }
    @PropertyName("status")
    public void setStatus(String status) { this.status = status; }

    @PropertyName("userId")
    public String getUserId() { return userId; }
    @PropertyName("userId")
    public void setUserId(String userId) { this.userId = userId; }

    @PropertyName("program")
    public String getProgram() { return program; }
    @PropertyName("program")
    public void setProgram(String program) { this.program = program; }

    @PropertyName("degree")
    public String getDegree() { return degree; }
    @PropertyName("degree")
    public void setDegree(String degree) { this.degree = degree; }

    @PropertyName("courses")
    public List<String> getCourses() { return courses; }
    @PropertyName("courses")
    public void setCourses(List<String> courses) { this.courses = courses; }

    @PropertyName("password")
    public String getPassword() { return password; }
    @PropertyName("password")
    public void setPassword(String password) { this.password = password; }

    // Helper method to get courses as a comma-separated string
    public String getCoursesAsString() {
        if (courses == null || courses.isEmpty()) {
            return "N/A";
        }
        return String.join(", ", courses);
    }
}