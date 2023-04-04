package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;


/**
 * A class for storing and handling degree modules, extends Module class. 
 * @author Tatu Anttila
*/


public class DegreeModule extends Module {            
    // degreeStructure object contains modules of the study program:
    private HashSet<contentModule> degreeStructure;
    private contentModule rootModule;
    
    /**
    * Constructor for DegreeModule class. The method checks if given groupID
    * has corresponding JSON object (retrieved from Sisu Api) with key named 
    * "type" and further if the "type" has value "DegreeProgramme". 
    * If that is _not_ the case, constructor does nothing.
    * @param groupID groupID for the DegreeModule.
    * @return Created DegreeModule object. 
    * @throws IOException JsonObject could not be retrieved from Sisu API.
    * @throws IOException Retrieved JsonObject has no key type.
    * @throws IOException DegreeModule: retrieved JsonObject must have 
    * DegreeProgramme as type value. 
    * 
    */
    public DegreeModule (String groupID) throws IOException {
        String url= getModuleUrlBasedOnId(groupID);       
        // retrieve data from API
        JsonObject degreeData= getJsonObjectFromApi(url); 
        // check if retrieved JSon object is found and it has key "type":
         if (degreeData == null) {
            throw new IOException ("DegreeModule: JsonObject could not be "
                    + "retrieved from Sisu API.");
        }
        if (!degreeData.has("type")) {
            throw new IOException ("DegreeModule: retrieved JsonObject has no "
                    + "node type.");    
        }
        if (degreeData != null && degreeData.has("type")) {            
            // check that retrieved Json object has type "DegreeProgramme":
            // if so, create data structure:
            if (! (degreeData.get("type").getAsString().equals("DegreeProgramme"))) {
                throw new IOException ("DegreeModule: retrieved JsonObject must "
                        + "have DegreeProgramme as type value.");     
            }            
            if (degreeData.get("type").getAsString().equals("DegreeProgramme")) {
               this.setGroupId(groupID);
               this.initName(degreeData);
               this.initMinCredits(degreeData);
               // this.degreeStructure = new TreeMap<>();
               this.degreeStructure = new HashSet<>();
               this.degreeStructure = createDegreeStructure (degreeData);
            }           
        }                       
    }
    
    /** Returns the root module for the degree program.
     * @ contentModule root of the degree structure.
     * 
     */
    public contentModule getRootModule()  {
        return this.rootModule;
    }            
    
    /**
    * Returns the structure of studies for the degree program.     
    * @return Data structure (Map) describing DegreeModule.
    */
    // public TreeMap<String, contentModule> getDegreeStructure () {
    public HashSet<contentModule> getDegreeStructure () {
        return this.degreeStructure;
    }
    
    /**
     * Return contentModule with given name, or null if no such module was found.
     * Note that here it is implicitly assumed that names are unique. 
     * @param name name of the module that is being searched. 
     * @return contentModule contentModule with given name (or null). 
     */
    public contentModule getModuleBasedOnName (String name) {
        contentModule retModule = null;
        for (var mod : this.degreeStructure) {
            if (mod.getName().equals(name)) {
                retModule = mod;
            }
        }
        return retModule;
    }
     
    
    // NEW (28.11.2022)    
     /** 
     * display name of Modules that are children of given Module, 
     * their sub modules etc. with given groupID.
     * @param contentModule rootModule root module.
     */
    
    public void displayAllSubModules (contentModule rootModule) {
        ArrayList<contentModule> subModules = new ArrayList<>();                
        String curId;
        // String parentId;
        contentModule parent;
        contentModule current;

        subModules= rootModule.getSubModules();
        for (var subModule: subModules) {
            StringBuilder sb = new StringBuilder();
            int level = 0;
            parent= subModule.getParentModule();
            current= subModule;
            // level of the module in the hierarchy: 
            while (parent!= null) {                    
                level++;                                                                            
                current= parent;
                parent= parent.getParentModule();
                }
                // sb.append(""); 
                for (int i = 0; i<level; i++) {
                    sb.append("-> ");
                }
                String stars= sb.toString();  
                // display module name:
                // System.out.println(stars + " " + subModule.getName() + " " + subModule.getGroupId()); 
                System.out.println(stars + " " + subModule.getName() + " " + subModule.getMinCredits() + "( "+subModule.getNoSubModules().min+ " / " + subModule.getNoSubModules().max +") " + subModule.isMandatory() + " " + subModule.getAnyModuleRule());
                // go further down in hierarchy if module has children: 
                this.displayAllSubModules(subModule);                                                
            }                     
    }
          
    /** 
     * Returns Minimum amount of study credit points related to the module.   
     * @return Minimum amount of study credit points related to the module. 
     */        
    @Override    
    // public int getMinCredits() {
    public void initMinCredits(JsonObject data) {
        if (data.has("targetCredits")) {
            JsonObject target= data.get("targetCredits").getAsJsonObject();
            this.setMinCredits(target.get("min").getAsInt()); 
        }
        else
            this.setMinCredits(0);         
    }
    
     /** 
     * Creates data structure which describes the degree program structure. 
     * This method is used in the constructor for DegreeModule class. 
     * The algorithm is based on the following:
     * 1) JSON object which is given as input (JsonObject obj) represents "root",
     *    i.e. a study degree.
     * 2) The sub modules are "processed" by one level at time in the hierarchy 
     *    of JSON objects retrieved from API. 
     * 3) Processing is done by maintaining a queue data structure which 
     *    handles all modules encountered by the method processRule (see below).
     *      .   
     * @param obj JSON object containing degree data. 
     * @return HashSet<> Retrieved data structure. 
     */

    // public TreeMap<String, contentModule> createDegreeStructure (JsonObject obj) 
    private HashSet<contentModule> createDegreeStructure (JsonObject obj)
            throws IOException {         
        HashSet<contentModule> retSet= new HashSet<>();
        Queue<contentModule> moduleQueue = new LinkedList<>();
        ArrayList<contentModule> addedModules = new ArrayList<>();
        String curID;
        contentModule curModule;
        // put the initial module (given as argument to the method) the structure:        
        this.rootModule= new contentModule (obj); 
        retSet.add(this.rootModule);
        // initialize queue: 
        // moduleQueue.add(parentID);
        moduleQueue.add(this.rootModule);
        // continue until there are no more modules to process:
        while (!moduleQueue.isEmpty()) {            
            curModule= moduleQueue.remove(); // pop queue: 
            curID= curModule.getGroupId();
            // get the current JSON object:
            obj= getJsonObjectFromApi(getModuleUrlBasedOnId(curID));
            // check if the current JSON object contains key "rule".
            // if so, then process the "rule" node using processRules method:
            if (obj.has("rule")) {
                JsonObject rule= obj.get("rule").getAsJsonObject();  
                // processRule(rule, retMap, false, curID, addedIDs);
                processRules(rule, retSet, curModule, addedModules);
            }   
            // update queue:                                   
            for (var module: addedModules) {
                moduleQueue.add(module);
            }
            addedModules.clear(); // all modules added to queue, so clear this list
        }
        return retSet;
    }    
       
     /** 
     * Handles recursively JSON object data retrieved from Sisu API. 
     *      .   
     * @param ruleObj JSON object.
     * @param struct Data structure created during the recursive calls. 
     * implementation which goes deeper down in JSON object hierarchy.   
     * @param curModule current contentModule. 
     * @param addedModules List of modules encountered during the recursion.
     */ 
    private void processRules (JsonObject ruleObj, HashSet<contentModule> struct, 
            contentModule curModule, ArrayList<contentModule> addedModules) 
            throws IOException {
        boolean isMandatory = false;
        boolean hasAnyModuleRule= false;
        Pair noModules = new Pair();
        noModules.min= 0;
        noModules.max= 99;
        // does ruleObj contain further rule?
        if (ruleObj.has("rule")) {
            processRules(ruleObj.get("rule").getAsJsonObject(), struct, 
             curModule, addedModules);
        }
        // check if ruleObj contains rule "allMandatory":
        if (ruleObj.has("allMandatory")) {
            isMandatory= ruleObj.get("allMandatory").getAsBoolean();
        }      
         // check if ruleObj contains rule "require":
        if (ruleObj.has("require")) {       
            noModules= processRequireRule(ruleObj);
            curModule.setNoSubModules(noModules);   
        }    
        //  does ruleObj JSON object contain key "rules"? 
        if (ruleObj.has("rules")) {
            JsonArray tmpRules= ruleObj.get("rules").getAsJsonArray();    
            // go through contents of JSON array:
            for (JsonElement e : tmpRules) {
                // check if element is JSON object
                if (e.isJsonObject()) { 
                    JsonObject tmpObj = e.getAsJsonObject(); 
                    // check if tmpObj contains rule "allMandatory":
                    if (tmpObj.has("allMandatory")) {
                        isMandatory= ruleObj.get("allMandatory").getAsBoolean();
                    }                         
                    // check if tmpObj contains rule "Require":
                    if (tmpObj.has("require")) {                                                      
                        noModules= processRequireRule(tmpObj);
                        curModule.setNoSubModules(noModules);   
                    }
                    // check if tmpObj has node "type":
                    if (tmpObj.has("type")) {
                        String type = tmpObj.get("type").getAsString();  
                        // AnyModuleRule encountered:                             
                        if (type.equals("AnyModuleRule")) {
                            hasAnyModuleRule= true;
                            curModule.setAnyModuleRule(hasAnyModuleRule); 
                        }                           
                        // ModuleRule encountered: process the module found:
                        if (type.equals("ModuleRule")) {   
                            // get groupID:
                            String id = tmpObj.get("moduleGroupId").getAsString(); 
                            // get the corresponding JSON object:
                            JsonObject moduleObj = 
                               getJsonObjectFromApi(getModuleUrlBasedOnId(id));
                            // initialize new contentModule:
                            contentModule newMod= processModuleRule (moduleObj, 
                                isMandatory, curModule, hasAnyModuleRule, 
                                noModules, struct);
                            // update degree structure:
                            struct.add(newMod);  
                            // set new module as being children of current module:
                            curModule.addSubModule(newMod);
                            //add module to the "processed" list                                
                            addedModules.add(newMod);                                 
                        }   
                        // "CourseUnitRule" encountered: add courses under the module
                        if (type.equals("CourseUnitRule")) {
                            CourseUnit newCourse = processCourseUnitRule 
                                (tmpObj, isMandatory);
                            curModule.addCourse(newCourse);
                        } 
                            // if type is "CompositeRule" go further down in hierarchy:                            
                            if (type.equals("CompositeRule")) {
                                processRules(tmpObj, struct, curModule, addedModules);
                            }                            
                        }                     
                    }
                }
            }
        }       
    
    /** 
    * Sets the minimum and maximum number of submodules based on rule
    * require in ruleObj Json data object.
    * @param ruleObj Json data object.
    * @return Pair minimum and maximum number of sub modules. 
    */
    private Pair processRequireRule(JsonObject ruleObj) {                             
        Pair noModules = new Pair();
        noModules.min= 0;
        noModules.max= 99;
        if (!ruleObj.get("require").isJsonNull()) {
            JsonObject reqObj= ruleObj.get("require").getAsJsonObject();
            if (reqObj.has("min")) {                                    
                if (!reqObj.get("min").isJsonNull()) {
                    noModules.min= reqObj.get("min").getAsInt();                                    
                }
            }
            if (reqObj.has("max")) {
                if (!reqObj.get("max").isJsonNull()) {
                    noModules.max= reqObj.get("max").getAsInt();
                }
            } 
        }
        return noModules;
    }
    
    /**
     * Initializes new contentModule based on given arguments.
     * @param moduleObj Json data object. 
     * @param isMandatory Is module mandatory as a sub module?
     * @param curModule Parent module. 
     * @param hasAnyModuleRule can modules under the new module be freely removed/deleted.
     * @param noModules Minimum and maximum number of allowed sub modules. 
     * @param struct Current degree data structure.
     * @return Initialized contentModule. 
     */
        
    private contentModule processModuleRule (JsonObject moduleObj, boolean isMandatory,
            contentModule curModule, boolean hasAnyModuleRule, Pair noModules,
            HashSet<contentModule> struct) {
            // initialize new contentModule:
        contentModule newMod = new contentModule(moduleObj, isMandatory, 
                curModule, hasAnyModuleRule, noModules);                                
        // check if there exists module with the same name. 
        // If so, modify name slightly:
        String newName= newMod.getName();
            for (var mod : struct) {
                String curName = mod.getName();                         
                if (newName.equals(curName)) {
                    newName = newName + " (" + curModule.getName() + ")";
                      newMod.setName(newName);
                }
            }                
        return newMod;    
    }
    
    /** 
     * 
     * @param tmpObj Json data object. 
     * @param isMandatory Is course mandatory part of the module?
     * @return Initialized CourseUnit object.
     */
    private CourseUnit processCourseUnitRule (JsonObject tmpObj, boolean isMandatory) {
        String id = tmpObj.get("courseUnitGroupId").getAsString();
        JsonObject courseObj = getJsonObjectFromApi(getCourseUrlBasedOnId(id)); 
        CourseUnit newCourse = new CourseUnit (courseObj,isMandatory);    
        return newCourse;
    }
}