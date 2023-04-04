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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

 /**  Class for handling all the degrees available, to use (example):
 * Degrees allStudyPrograms = new Degrees ();
 * HashMap <String,JsonObject> allDegrees = 
 *      allStudyPrograms.getAllDegrees();
 * ArrayList<String> degreeNames = allStudyPrograms.getNames(); 
 * @author Tatu Anttila
*/ 
public class Degrees implements iAPI {    
    // URL address for fetching all the degree modules available at TUNI:
    private static final String searchURL= "https://sis-tuni.funidata.fi/kori/api/"
        + "module-search?curriculumPeriodId=uta-lvv-2021&"
        + "universityId=tuni-university-root-id&moduleType=Degree"
        + "Programme&limit=1000";     
    // structure containing the degrees (as JSon objects) 
    private HashMap <String,JsonObject> allDegrees; 
   
  /** Constructor for Degrees class. 
  */
    public Degrees() throws IOException {        
        this.allDegrees = new HashMap<>();
        JsonObject rootObj = getJsonObjectFromApi(searchURL);
        JsonArray searchResults = rootObj.getAsJsonArray("searchResults"); 
        for (JsonElement element: searchResults) {
            if (element.isJsonObject()) {
                JsonObject degreeObj = element.getAsJsonObject();               
                String id = degreeObj.get("groupId").getAsString(); 
                this.allDegrees.put(id, degreeObj);
            }
        }         
    }     
    
    /** Returns data structure containing all the available degree modules.
    * @return Structure (HashMap) containing all the available degree modules.
    */      
    // Return structure that contains all the available degree modules.
    public HashMap <String,JsonObject> getAllDegrees() {
        return this.allDegrees;
    }
    
    /** Returns data of all the available degree modules as 
    * ArrayList <JsonObject> structure.
    * @return ArrayLisr structure containing all the retrieved degree data.
    */   
    public ArrayList<JsonObject> getAllDegreesAsList() {
        ArrayList<JsonObject> retArr= new ArrayList<>();
        for (var e: this.allDegrees.entrySet()) {
            retArr.add(e.getValue());
        }   
        return retArr;
}
    
    /** Returns groupIDs of all the available degree modules as 
    * ArrayList structure.
    * @return ArrayList structure containing all the groupIDs of the 
    * retrieved degree data.
    */ 
    public ArrayList<String> getDegreeIds() {
        ArrayList<String> retArr = new ArrayList<>();
        for (var e: this.allDegrees.entrySet()) {
            retArr.add(e.getKey());
        }        
        return retArr;
    }
    
    /** Returns names of all the available degree modules as 
    * ArrayList structure.
    * @return ArrayLisr structure containing all the names of the 
    * retrieved degree module data.
    */ 
    public ArrayList<String> getNames() {
        ArrayList<String> retArr = new ArrayList<>();
        for (var e: this.allDegrees.entrySet()) {
            String name= this.getName(e.getValue());
            retArr.add(name);            
        }        
        Collections.sort(retArr); // sort alphabetically
        return retArr;
    }
    
    /** Returns the name of a study degree (contained in JsonObject).
    * Note: only degree programmes in Finnish or in English are accounted for.
    * @param JsonObject from which key "name" is searched.
    * @return Name of the study module. 
    */     
    private String getName(JsonObject obj) {
        if (obj.has("name"))  {
            if (obj.get("name").isJsonPrimitive()) // no language choices
                return obj.get("name").getAsString();
            else { // default language is Finnish
                JsonObject courseNames= obj.get("name").getAsJsonObject();
                if (courseNames.has("fi"))
                    return courseNames.get("fi").getAsString();    
                else if (courseNames.has("en")) 
                    return courseNames.get("fi").getAsString(); 
                else
                    return null; // unknown language
                }         
        }
        else
            return null;
    }     

    /** Returns groupID of a study degree with given name. 
    * @param String name of the degreeProgramme module. 
    * @return groupID of the degreeProgramme module. 
    */     
    String getGroupIdBasedOnName(String name) {
        String retId = null;
        for (var degrees: this.allDegrees.entrySet()) {
            String curID= degrees.getKey();
            String curName = this.getName(degrees.getValue()); 
            if (curName.equals(name)) {
                retId= curID;
                return retId;
            }
        }        
        return retId;
    }
     
    // iAPI implementation:  
    /**
     * Returns JSON object retrieved from Sisu API with given URL. 
     * @param urlString URL address as String.
     * @return JSON object retrieved from Sisu API with given URL.
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
            root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        } catch (IOException ex) {
           System.out.println(ex);
        }
        if (root.isJsonArray()) { // Check if JsonElement is JsonArray
            JsonArray rootArr = root.getAsJsonArray(); // convert json element to json array
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
    
    @Override
    /**
     * Returns URL address of a Sisu module based on groupID key). 
     * @param groupID groupID of JSON data file based on Sisu API.
     * @return URL address as a string. 
     */      
    public String getModuleUrlBasedOnId (String groupID) {
        StringBuilder sb = new StringBuilder();          
        sb.append("https://sis-tuni.funidata.fi/kori/api/modules/");
        sb.append("by-group-id?groupId=");
        sb.append(groupID);
        sb.append("&universityId=tuni-university-root-id");
        return sb.toString();       
    }
    @Override
    /**
     * Returns URL address of a Sisu Course Unit based on groupID key. 
     * @param groupID groupID of JSON data file based on Sisu API.
     * @return URL address as a string. 
     */        
    public String getCourseUrlBasedOnId(String groupID) {        
        StringBuilder sb = new StringBuilder();          
        sb.append("https://sis-tuni.funidata.fi/kori/api/course-units/");
        sb.append("by-group-id?groupId=");
        sb.append(groupID);
        sb.append("&universityId=tuni-university-root-id");
        return sb.toString();        
    }
}