package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * A class for handling StudyModule / GroupingModule JSON objects retrieved
 * from Sisu API. Extends Module class.
 * @author Tatu Anttila
 */

public class contentModule extends Module {
    private contentModule parentModule; // "parent" of the module
    private ArrayList<contentModule> subModules; // list of submodules    
    private ArrayList<CourseUnit> courses; // set of courses in the module
    // can module be modified by adding/removing courses or modules contained
    // in the module? 
    private Boolean AnyModuleRule; // minimum and maximum number of submodules
    private Pair noSubModules;
    
    // TODO : minCredits / targetCredits separately
    
 /**  Constructor for class contentModule class. 
  * @param data JsonObject retrieved from Sisu API. 
  * @return contentModule object. 
*/ 
    public contentModule(JsonObject data) {      
        this.initMinCredits(data);
        this.initName(data);
        this.initGroupId(data);
        this.initCode(data);
        this.initType(data);        
        // this.setParentId(null);
        this.parentModule = null;
        this.subModules= new ArrayList<>();
        this.setMandatory(false);
        this.courses = new ArrayList<>();
        this.AnyModuleRule = false;
        this.noSubModules = new Pair();
        this.noSubModules.min = 0;
        this.noSubModules.max = 99;        
    }
     /**
     * 
     * @param data JsonObject retrieved from Sisu API. 
     * @param isMandatory is module mandary part of studies?
     * @param parent parent module.
     * @param anyModuleRule can modules/courser be freely added/modified?
     * @param minNoModules minimum number of sub modules.
     * @param maxNoModules  maximum number of sub modules.
     */
    public contentModule(JsonObject data, boolean isMandatory, contentModule parent,
            boolean anyModuleRule, Pair noModules) {      
        this.initMinCredits(data);
        this.initName(data);
        this.initGroupId(data);
        this.initCode(data);
        this.initType(data);        
        // this.setParentId(null);
        this.parentModule = parent;
        this.subModules= new ArrayList<>();
        this.setMandatory(isMandatory);
        this.courses = new ArrayList<>();
        this.AnyModuleRule = anyModuleRule;
        this.noSubModules = new Pair();
        this.noSubModules = noModules;        
    }    
    
    // NEW (28.11.2022) : 
    // set minimum and maximum number of submodules 
    /**
     * Get minimum and maximum number of sub modules allowed for.
     * @return Pair of integers (noSubModules.min, noSubModules.max).
     */
     public Pair getNoSubModules() {
         return this.noSubModules;
     }
    /**
     * Set minimum and maximum number of sub modules allowed for.
     * @param noModules Pair of integers (noSubModules.min, noSubModules.max).
     */
     public void setNoSubModules(Pair noModules) {
         this.noSubModules.min = noModules.min;
         this.noSubModules.max = noModules.max;
     }     
     
    /**
     * Returns AnyModuleRule, i.e. can module be modified by adding/removing 
     * courses or modules contained in the module? 
     * @return boolean (true/false). 
     */    
    public boolean getAnyModuleRule() {
        return this.AnyModuleRule;
    }
    
     /**
     * Set hasAnyModuleRule, i.e. can module be modified by adding/removing 
     * courses or modules contained in the module? 
     * @param b boolean (true/false).
     */    
    public void setAnyModuleRule(boolean b) {
        this.AnyModuleRule = b;
    }
        
     /**
     * Get parent module of the module.
     * @return contentModule Parent module.
     */   
    public contentModule getParentModule() {
        return this.parentModule;
    }
    
   /** Sets "parent" module of ContentModule.
   * @param parent "Parent" module (ContentModule object).
   */      
    public void setParentModule(contentModule parent) {
        this.parentModule = parent;
    }
    
    /** Returns sub modules (as contentModule objects) of the given module.
    * @return ArrayList containing sub modules of the module. 
    */     
    public ArrayList<contentModule> getSubModules() {
        return this.subModules;
    }
    
    /** Add of a module with given groupId variable as a sub module to the 
     * module.
     * @param module module to be added.   
    */     
    public void addSubModule (contentModule module) {
        // check if  module with given groupId is already contained in the set;
        boolean isModuleAlreadyAdded = false;
        for (var subModules : this.getSubModules()) {
            if (subModules.getGroupId().equals(module.getGroupId())) 
                isModuleAlreadyAdded = true;            
        }
        if(!isModuleAlreadyAdded) 
            this.subModules.add(module);
    }    
    
        /**
     * Remove sub module  with given name from the module. Only non-mandatory 
     * modules can be removed. If module is not found or it is mandatory, 
     * do nothing.
     * @param name name of the module to be removed.
     */
    public void removeSubModule (String name) {
        // check if course with given name is included under the module:
        for (int i= 0; i<this.subModules.size(); i++) {
            // if course with given name is found and it is not mandatory, 
            // then remove it from courses field:
            contentModule curModule= this.subModules.get(i);
            if (curModule.getName().equals(name) && curModule.isMandatory()) {
                this.courses.remove(i);
            }
        }
    }
    
    /** Adds a course (as CourseUnit object) to the module. 
     * @param course GroupId.   
    */       
    public void addCourse(CourseUnit course) {
        boolean isCourseAlreadyAdded = false;
        for (var existingCourses : this.getCourses()) {
            if (existingCourses.getGroupId().equals(course.getGroupId())) 
                isCourseAlreadyAdded = true;                                    
            }
        if (!isCourseAlreadyAdded) {
            this.courses.add(course);                                
        }
    }
    /**
     * Remove course with given name from the module. Only non-mandatory courses
     * can be removed. If course not found or it is mandatory, do nothing.
     * @param name name of the course to be removed.
     */
    public void removeCourse (String name) {
        // check if course with given name is included under the module:
        for (int i= 0; i<this.courses.size(); i++) {
            // if course with given name is found and it is not mandatory, 
            // then remove it from courses field:
            CourseUnit curCourse= this.courses.get(i);
            if (curCourse.getName().equals(name) && curCourse.isMandatory()) {
                this.courses.remove(i);
            }
        }
    }
    
    /** Returns HashSet of courses (as CourseUnit objects) that are contained 
     * in the given module.
    * @return ArrayList containing the courses contained in the module.    
    */   
    public ArrayList<CourseUnit> getCourses() {
        return this.courses;
    }    
      
    /* 
    @Override    
    /** 
     * Get minimum number of credits related to the module. Retrieved from 
     * Json object node named "targetCredits". 
     * @return Minimum number of credits related to the module. 
     */    
    public void initMinCredits (JsonObject data) {
        if(data.has("targetCredits")) {
            JsonObject obj= data.get("targetCredits").getAsJsonObject();
            this.setMinCredits(obj.get("min").getAsInt()); 
        }
        else
            this.setMinCredits(0);
    }         
    
    /** 
     * Calculate total number of credits from completed courses that are
     * under the module. 
     * @return Total number of credits earned from courses under the module.
     */
    public int getCreditsEarned() {
        int sum= 0;
        for (var course : this.getCourses()) {
            if (course.isCompleted())
                sum= sum + course.getMinCredits();
        }
        return sum;
    }
}