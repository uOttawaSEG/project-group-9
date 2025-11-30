package com.example.otams;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    //checks if average is calculated correctly
    @Test
    public void average_multipleRatings_computesCorrectly() {
        Tutor tutor = new Tutor();
        tutor.addRating(4);
        tutor.addRating(5);
        tutor.addRating(3);

        double avg = tutor.getAverageRating();

        assertEquals(4.0, avg, 0.001);
    }
    //if tutor has no rating it returns 0
    @Test
    public void average_noRatings_returnsZero() {
        Tutor tutor = new Tutor();

        double avg = tutor.getAverageRating();

        assertEquals(0.0, avg, 0.0);
    }

    // Tests the creation of an administrator object
    @Test
    public void administrator_creation_test(){
        Administrator admin = new Administrator("admin@example.com");
        assertNotNull("Admin not null",admin);
        assertNotNull("Requests not null",admin.getRequests());
    }

    // Tests the approval of a user
    @Test
    public void administrator_approval_test(){
        Administrator admin = new Administrator("admin@example.com");
        
        Student a = new Student("student@email.com");
        Tutor b = new Tutor("tutor@email.com");

        RegistrationRequest reqA = new RegistrationRequest(a);
        RegistrationRequest reqB = new RegistrationRequest(b);

        admin.getRequests().add(reqA);
        admin.getRequests().add(reqB);

        admin.approveAll();

        assertEquals("approved", reqA.getStatus());
        assertEquals("approved", reqB.getStatus());
    }
}