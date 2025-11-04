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
	private int date

	/** Paramterized constructor for slot
	 */
    public Slot(Tutor tutor) {
    	this.tutor = tutor;
    }

    /** [SETTER] Sets the end time
     * @param time
     */
    public void setEndTime(int time){ this.endTime = time; }

    /** [SETTER] Sets the start time
     * @param time
     */
    public void setStartTime(int time){ this.startTime = time; }

    /** [SETTER] Sets the date
     * @param date
     */
    public void setDate(int date){ this.date = date; }

    /** [GETTER] Gets the end time
     * @return end time
     */
    public void getEndTime(){ return endTime; }

    /** [GETTER] Gets the start time
     * @return start time
     */
    public void getStartTime(){ return startTime; }

    /** [GETTER] Gets the date
     * @return date
     */
    public void getDate(){ return date; }

    /** [GETTER] Gets the tutor object
     * @return tutor 
     */
    public void getTutor(){ return tutor; }



    //////    -----   HELPER METHODS    -----   //////


    /** Validates that the times are both valid times and 30 minutes apart
     */
    public boolean validateTimes(){
    	if(startTime < 0 || endTime < 0) throw new IndexOutOfBoundsException("Time doesn't exist.");
    	if(endTime - startTime != 30){
    		return false;
    	} 
    	return true;
    }
}
