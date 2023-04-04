package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;

/**
 * Interface for extracting data from the Sisu API.
 */
public interface iAPI {
    /**
     * Returns a JsonObject that is extracted from the Sisu API,
     * returns URL address based on GroupId of Module,
     * returns URL address based on GroupId of Course Unit.
     * @param urlString URL for retrieving information from the Sisu API.
     * @param ID groupID. 
     * @return JsonObject.
     */
    JsonObject getJsonObjectFromApi(String urlString);
    String getModuleUrlBasedOnId (String ID);
    String getCourseUrlBasedOnId (String ID);
}
