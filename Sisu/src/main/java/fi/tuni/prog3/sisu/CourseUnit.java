package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;

/**
 * A class for storing and handling individual courses, extends Module class. 
 * @author Tatu Anttila
 */

public class CourseUnit extends Module {
    
    private boolean courseCompleted; // is course completed?
    
    /**
    * Constructor for CourseUnit class. 
    * @param data JSON object containing the course data.
    */
    public CourseUnit (JsonObject data) {
        this.initMinCredits(data);
        this.initName(data);
        this.initGroupId(data);
        this.initCode(data);
        this.initType(data);
        this.setMandatory(false);
        this.courseCompleted = false;
    }
    /** 
     * Constructor for CourseUnit class. 
     * @param JSON object containing the course data.
     * @param isMandatory data is course mandatory part of the module?
     */
    public CourseUnit (JsonObject data, boolean isMandatory) {
        this.initMinCredits(data);
        this.initName(data);
        this.initGroupId(data);
        this.initCode(data);
        this.initType(data);
        this.setMandatory(isMandatory);
        this.courseCompleted = false;
    }
    /**
     * Returns the value of courseCompleted field (true/false).
     * @return value of courseCompleted field.
     */
    public boolean isCompleted() {
        return this.courseCompleted;
    }
    
    /**
     * Sets the completion status of the course.
     * @param status Is course completed (true) or not (false)?
     */
    public void setCourseCompleted(boolean status) {
       this.courseCompleted= status;
    }
    
    /**
    * Sets the minimum number of study credits for completing the course unit 
    * based on given JsonObject.
    * @return Minimum number of credits for completing the course unit.
    */    
    @Override    
    // public int getMinCredits() {
    public void initMinCredits(JsonObject data) {    
        if(data.has("credits")) {
            JsonObject target= data.get("credits").getAsJsonObject();
            this.setMinCredits(target.get("min").getAsInt()); 
        }
        else
            this.setMinCredits(0);
    }
        
}

