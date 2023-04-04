
package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Class for testing classes Degrees, Module, DegreeModule, contentModule and
 * CourseUnit and also interface iAPI.
 * @author Miia Raerinne
 */

public class sisuTests {

    /**
      * Test for finding degree's groupID based on it's name
      * @throws IOException
      */
     @Test
      public void testFindID() throws IOException
      {
        Degrees degrees = new Degrees();
        String degreeName = "Teatteritaiteen maisteriohjelma"; 
        
        String expID = "uta-tohjelma-1749";
        
        String resultID = degrees.getGroupIdBasedOnName(degreeName);
        
        assertEquals(expID, resultID);
         
      }
      
      
      /**
       * Test for finding degree's groupID based on it's name
       * when given degree name is not found.
       * @throws IOException 
       */
      @Test
      public void testNotFindID() throws IOException
      {
        Degrees degrees = new Degrees();
        String degreeName = "Ei tutkinto"; 
        
        String expID = null;
        
        String resultID = degrees.getGroupIdBasedOnName(degreeName);
        
        assertEquals(expID, resultID);
         
      }
      
      
      /**
       * Test for finding degree's groupID based on it's name
       * when given degree name is in unknown language.
       * @throws IOException 
       */
      @Test
      public void testGetDegreeNameUnknownLanguage() throws IOException
      {
        Degrees degrees = new Degrees();
        String degreeName = "Datavetenskap kandidat"; 
        
        String expID = null;
        
        String resultID = degrees.getGroupIdBasedOnName(degreeName);
        
        assertEquals(expID, resultID);
         
      }
      
      
      /**
       * Test for getting degree's name based on knowing it's groupID.
       * @throws IOException 
       */
      @Test
      public void testGetDegreeName() throws IOException
      {
          String degreeID = "uta-tohjelma-1749";
          DegreeModule degreeModule = new DegreeModule(degreeID);
          
          String expDegreeName = "Teatteritaiteen maisteriohjelma";
          
          String resultDegreeName = degreeModule.getName();
          
          assertEquals(expDegreeName, resultDegreeName);    
      }
      
      
      /**
       * Test for getting degree's minimum credits based on knowing it's groupID.
       * @throws IOException 
       */
      @Test
      public void testGetDegreeMinCredits() throws IOException
      {
          String degreeID = "uta-tohjelma-1705";
          DegreeModule degreeModule = new DegreeModule(degreeID);
          
          int expMinCredits = 180;
          
          int resultMinCredits = degreeModule.getMinCredits(); 
          
          assertEquals(expMinCredits, resultMinCredits);
               
      }
      
      
      /**
       * Test for getting course's name based on knowing it's degree's groupID
       * and course's groupID.
       * @throws IOException 
       */
      @Test
      public void testGetCourseName() throws IOException
      {
          String degreeID = "uta-tohjelma-1705";   
          DegreeModule degreeModule = new DegreeModule(degreeID);
          HashSet<contentModule> allModules = degreeModule.getDegreeStructure();

          String expcourseID = "tut-cu-g-45501";
          
          String expCourseName = "Kyberturvallisuus I: perusteet";
          
          String resultCourseName = "NOTFOUND";
          
          for (var module : allModules)
          {
              ArrayList<CourseUnit> courses = module.getCourses();
              
              for (var course : courses)
              {
                  if (course.getGroupId().equals(expcourseID))
                  {
                      resultCourseName = course.getName();
                  }
              }
          }
          
          assertEquals(expCourseName, resultCourseName);
            
      }
      
      
      /**
       * Test for getting course's code based on knowing it's degree's groupID
       * and course's groupID.
       * @throws IOException 
       */
      @Test
      public void testGetCourseCode() throws IOException
      {
          String degreeID = "uta-tohjelma-1705";  
          DegreeModule degreeModule = new DegreeModule(degreeID);
          HashSet<contentModule> allModules = degreeModule.getDegreeStructure();

          String expcourseID = "tut-cu-g-45501";
          
          String expCourseCode = "COMP.SEC.100";
          
          String resultCourseCode = "NOTFOUND";
          
          for (var module : allModules)
          {
              ArrayList<CourseUnit> courses = module.getCourses();
              
              for (var course : courses)
              {
                  if (course.getGroupId().equals(expcourseID))
                  {
                      resultCourseCode = course.getCode();
                  }
              }
          }
          
          assertEquals(expCourseCode, resultCourseCode);    
      }
      
      
      /**
       * Test for getting course's minimum credits based on knowing it's
       * degree's groupID and course's groupID.
       * @throws IOException 
       */
      @Test
      public void testGetCourseMinCredits() throws IOException
      {
          String degreeID = "uta-tohjelma-1705";  
          DegreeModule degreeModule = new DegreeModule(degreeID);
          HashSet<contentModule> allModules = degreeModule.getDegreeStructure();

          String expcourseID = "tut-cu-g-45501";
          
          int expMinCredits = 5;
          
          int resultMinCredits = 0;
          
          for (var module : allModules)
          {
              ArrayList<CourseUnit> courses = module.getCourses();
              
              for (var course : courses)
              {
                  if (course.getGroupId().equals(expcourseID))
                  {
                      resultMinCredits = course.getMinCredits();
                  }
              }
          }
          
          assertEquals(expMinCredits, resultMinCredits);    
      }
      
      
      /**
       * Test for initializing Degree with groupID that is null.
       * NullPOinterException is thrown.
       * @throws IOException 
       */
      @Test
      public void testDegreeIDNull() throws IOException
      {
          String degreeID = null;  
          
          Exception exception = assertThrows(NullPointerException.class, () -> {
            new DegreeModule(degreeID);
            });   
      }
      
      
      /**
       * Test for initializing Degree with groupID that has no type.
       * IOException is thrown with message.
       * @throws IOException 
       */
      @Test
      public void testDegreeNoType() throws IOException
      {
          String degreeID = "uta-tohjelma-13";
          
           Exception exception = assertThrows(IOException.class, () -> {
            new DegreeModule(degreeID);
            });
           
           String expectedMessage = "DegreeModule: retrieved JsonObject has no "
                                    + "node type.";
           String actualMessage = exception.getMessage();
           assertTrue(actualMessage.contains(expectedMessage));
            
      }
      
      
      /**
       * Test for initializing Degree with groupID that's type is not
       * DegreeProgramme. (It's StudyModule)
       * IOException is thrown with message.
       * @throws IOException 
       */
      @Test
      public void testDegreeWrongType() throws IOException
      {
          String degreeID = "tut-sm-g-3725";
          
           Exception exception = assertThrows(IOException.class, () -> {
            new DegreeModule(degreeID);
            });
           
           String expectedMessage = "DegreeModule: retrieved JsonObject must "
                                    + "have DegreeProgramme as type value.";
           String actualMessage = exception.getMessage();
           assertTrue(actualMessage.contains(expectedMessage));
            
      }
}
