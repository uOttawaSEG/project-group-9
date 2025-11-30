package com.example.otams;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    //calculating average of multiple ratings
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

}