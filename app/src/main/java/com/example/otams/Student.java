package com.example.otams;

/** Basic Student class
 * 
 * @author Lauren [lhend093@uottawa.ca]
 */
public class Student extends User {
    // Instatiating variables
    private String program;

    /**
     * Email constructor for student object (for firebase)
     */
    public Student(String email) {
        super(email);
    }

    /**
     * [GETTER] Gets the program of the student
     *
     * @return program
     */
    public String getProgram() {
        return program;
    }

    /**
     * [SETTER] Sets the program of the student
     *
     * @param program
     */
    public void setProgram(String program) {
        this.program = program;
    }


}