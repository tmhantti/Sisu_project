package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * An abstract class for storing information on Modules and Courses.
 * @author Tatu Anttila
 */
public abstract class Module implements iAPI { 
    // private JsonObject moduleData;       
    private boolean mandatory; // is Module     
    private String name; // name of Module
    private String groupId; // groupID of Module
    private String code; // code of Module
    private String type; // type of Module
    private int minCredits; // minimum number of credits
    
    public abstract void initMinCredits(JsonObject data);
                       
    /**
     * Sets the name of the Module based on JsonObject data. 
     * When the Module has multiple languages choices, name in Finnish is chosen. 
     * @param JsonObject containing the Module data.
     */    
    
    // define pair of integers (used for noRequiredModules field): 
    class Pair {
        int min, max;
    }   
    
    public void initName (JsonObject data) {
        if (data.has("name"))  {
        // check if there are no optional languages:
            if (data.get("name").isJsonPrimitive()) {
            this.name = data.get("name").getAsString();
            // otherwise, default language is Finnish (then English):
            }
            else { 
                JsonObject courseNames= data.get("name").getAsJsonObject();
                if (courseNames.has("fi"))
                    this.name= courseNames.get("fi").getAsString();    
                else if (courseNames.has("en")) {
                    this.name= courseNames.get("en").getAsString(); 
                } else
                    this.name= null; // unknown language
            }         
        }
        else
            this.name= null;
        
    } 
    
    /**
     * Returns the name of the Module / CourseUnit. 
     */
    public String getName() {
        return this.name;
    } 

    /**
     * Sets the name of the Module based on given String.
     * @param name Name of the Module.
     */        
    public void setName(String name) {
        this.name = name;        
    }
    
     /**
     * Sets the type of the Module/CourseUnit based on JsonObject. 
     * Note that CourseUnits have no type, hence it is null in this case.
     * @param data JsonObject containing module data. 
     */ 
    public void initType(JsonObject data) {
        if (data.has("type"))
            this.type= data.get("type").getAsString();   
        else
            this.type = null;
    }
    
     /**
     * Sets the type of the Module/CourseUnit based on given String. 
     * @param type type of the Module. 
     */ 
    private void setType (String type) {
        this.type= type;
    }
    
    /**
     * Returns the type of the Module/CourseUnit.
     * @return Type of the Module (if not found, return null).
     */ 
    public String getType() {
    /*     if (this.moduleData.has("type"))
            return this.moduleData.get("type").getAsString();   
        else
            return null; */
            return this.type;
    }       

    /**
     * Sets the GroupID of the Module or Course based on JsonObject.
     * @return group id of the Module or Course.
     */
    public void initGroupId(JsonObject data) {
        if (data.has("groupId"))
            this.groupId= data.get("groupId").getAsString();        
        else 
            this.groupId= null;
    }   
    
     /**
     * Sets the GroupID of the Module or Course based on given String.
     * @return GroupId of the Module or Course.
     */
    public void setGroupId(String id) {
        this.groupId= id;
    }  
    
    /**
     * Returns the GroupID of the Module/CourseUnit.
     * @return group id of the Module/CourseUnit.
     */
    public String getGroupId() {
        /* 
        if (this.moduleData.has("groupId"))
            return this.moduleData.get("groupId").getAsString();        
        else 
            return null; */
        return this.groupId;
    }
    

    /**
     * Sets the code of the Module or Course based on given JsonObject.
     * The code is set to null if no "code" key was found, or it is not
     * primitive Json type. 
     * @pararm data of the Module or Course (if not found, return null).
     */    
    public void initCode(JsonObject data) {
        if (data.has("code")) {
            if (data.get("code").isJsonPrimitive() ) {
                this.code = data.get("code").getAsString();        
            }
        }
        else 
            this.code = null;      
    }
    
    /**
     * Returns the code of the Module or Course.
     * @return code of the Module or Course (if not found, return null).
     */    
    public String getCode() {
        /* if (this.moduleData.has("code")) {
            if (this.moduleData.get("code").isJsonPrimitive() ) {
                return this.moduleData.get("code").getAsString();        
            }
        }
        return null;         */ 
        return this.code;
    }
    
    /**
     * Sets the code of the Module or Course.
     * @param code code of the Module or Course.
     */    
    public void setCode(String code) {
        this.code= code;
    }    
    
     /**
     * Sets the minimum number of credits associated with the Module or Course.
     * @param credits Number of credits.
     */  
    public void setMinCredits (int credits) {
        this.minCredits = credits;
    }
    
     /**
     * Sets the minimum number of credits associated with the Module or Course.
     * @param credits Number of credits.
     */  
    public int getMinCredits () {
        return this.minCredits;
    }    
    
    /**
    * Check if the module/course is mandatory. 
    * @return Boolean: true or false.
    */
    public boolean isMandatory() {
        return this.mandatory;
    }

    /**
    * Set the Module or Course Unit status depending on 
    * whether is it mandatory for the current degree.
    * @param b: Boolean - true or false.
    */
    public void setMandatory(boolean b) {
        this.mandatory = b;
    }    
    
     // iAPI implementation:
    /**
     * Returns JSON object retrieved from Sisu API with given URL. 
     * @param urlString URL address as String.
     * @return JSON object retrieved from Sisu API with given URL.
     * @throws MalformedURLException URL address was malformed.
     * @throws IOException Problem(s) with accessing Sisu API with given URL.
     */    
    @Override
    public JsonObject getJsonObjectFromApi (String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException ex) {
            System.out.println(ex);
        }
        URLConnection request = null;
        try {
            request = url.openConnection();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        try {
            request.connect();
        } catch (IOException ex) {
            System.out.println(ex);
        }

        JsonObject rootObj= new JsonObject(); 
        int count= 0;
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root= null;
        try {
        //Convert the input stream to a json element            
            root = jp.parse(new InputStreamReader((InputStream) request.getContent())); 
        } catch (IOException ex) {
           System.out.println(ex);
        }
        if (root.isJsonArray()) { // Check if JsonElement is JsonArray
            // convert json element to json array
            JsonArray rootArr = root.getAsJsonArray(); 
            // get object(s) in the array:
            for (JsonElement element: rootArr) {
                if (element.isJsonObject()) {
                    rootObj = element.getAsJsonObject();
                    count++;
                    }
                }         
            }
        else if (root.isJsonObject() ) { // Check if JsonElement is JsonObject
            rootObj = root.getAsJsonObject();
        }
        else {
            // TODO: throw some exception
        }             
        return rootObj;
    }    

    /**
     * Returns URL address of a Sisu module based on groupID key. 
     * @param groupID groupID of JSON data file based on Sisu API.
     * @return URL address as a String. 
     */         
    @Override   
    public String getModuleUrlBasedOnId (String groupID) {
        StringBuilder sb = new StringBuilder();          
        sb.append("https://sis-tuni.funidata.fi/kori/api/modules/");
        sb.append("by-group-id?groupId=");
        sb.append(groupID);
        sb.append("&universityId=tuni-university-root-id");
        return sb.toString();       
    }
        /**
     * Returns URL address of a Sisu Course Unit based on groupID key. 
     * @param groupID groupID of JSON data file based on Sisu API.
     * @return URL address as a String. 
     */ 
    @Override     
    public String getCourseUrlBasedOnId(String groupID) {        
        StringBuilder sb = new StringBuilder();          
        sb.append("https://sis-tuni.funidata.fi/kori/api/course-units/");
        sb.append("by-group-id?groupId=");
        sb.append(groupID);
        sb.append("&universityId=tuni-university-root-id");
        return sb.toString();        
    }
}