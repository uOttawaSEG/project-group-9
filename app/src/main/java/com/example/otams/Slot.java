package com.example.otams;

import java.util.ArrayList;
import java.util.List;

/** Basic Slot class
 * @author Lauren [lhend093@uottawa.ca]
 */
public class Slot {
    // Instatiating variables
    private Tutor tutor;
    private int startTime;
    private int endTime;
    private int date;

    /** Paramterized constructor for slot
     * @param tutor
     */
    public Slot(Tutor tutor) {
        this.tutor = tutor;
    }

    /** Fully paramterized constructor for slot
     * @param tutor
     * @param start
     * @param end
     * @param date
     */
    public Slot(Tutor tutor, int start, int end, int date){
        this.tutor = tutor;
        this.startTime = start;
        this.endTime = end;
        this.date = date;
    }

    /** [SETTER] Sets the end time
     * @param time
     */
    public void setEndTime(int time){
        this.endTime = time;
    }

    /** [SETTER] Sets the start time
     * @param time
     */
    public void setStartTime(int time){
        this.startTime = time;
    }

    /** [SETTER] Sets the date
     * @param date
     */
    public void setDate(int date){
        this.date = date;
    }

    /** [GETTER] Gets the end time
     * @return end time
     */
    public int getEndTime(){
        return endTime;
    }

    /** [GETTER] Gets the start time
     * @return start time
     */
    public int getStartTime(){
        return startTime;
    }

    /** [GETTER] Gets the date
     * @return date
     */
    public int getDate(){
        return date;
    }

    /** [GETTER] Gets the tutor object
     * @return tutor
     */
    public Tutor getTutor(){
        return tutor;
    }

    //////    -----   HELPER METHODS    -----   //////

    /** Validates that the times are both valid times and 30 minutes apart
     */
    /** Validates that the times are both valid times and exactly 30 minutes apart
     */
    public boolean validateTimes(){
        if(startTime < 0 || endTime < 0)
            throw new IndexOutOfBoundsException("Time doesn't exist.");
        if(endTime - startTime != 30){
            return false;
        }
        return true;
    }
}