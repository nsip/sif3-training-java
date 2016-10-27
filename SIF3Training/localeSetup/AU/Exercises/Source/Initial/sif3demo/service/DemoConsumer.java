/*
 * DemoConsumer.java Created: 12/08/2014
 * 
 * Copyright 2014 Systemic Pty Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package sif3demo.service;

import java.util.List;

import sif.dd.au30.conversion.DataModelUnmarshalFactory;
import sif.dd.au30.model.StudentPersonalCollectionType;
import sif3.common.header.HeaderValues.RequestType;
import sif3.common.ws.Response;
import sif3.infra.rest.consumer.ConsumerLoader;
import sif3demo.consumer.StudentPersonalConsumer;
import au.com.systemic.framework.utils.FileReaderWriter;

/**
 * @author Joerg Huber
 * 
 */
public class DemoConsumer
{
//	private final static String SINGLE_STUDENT_FILE_NAME = "C:/DEV/eclipseWorkspace/SIF3Training/TestData/xml/input/StudentPersonal.xml";
//	private final static String MULTI_STUDENT_FILE_NAME = "C:/DEV/eclipseWorkspace/SIF3Training/TestData/xml/input/StudentPersonals5.xml";
//	private final static String SINGLE_STUDENT_FILE_NAME = "C:/Development/GitHubRepositories/SIF3Training/SIF3Training/TestData/xml/input/StudentPersonal.xml";
	private final static String MULTI_STUDENT_FILE_NAME = "C:/Development/GitHubRepositories/SIF3Training/SIF3Training/TestData/xml/input/StudentPersonals5.xml";
	private static final String CONSUMER_ID = "StudentConsumer";
//    private static final String CONSUMER_ID = "HITSStudentConsumer";
	
	private static final RequestType REQUEST_TYPE = RequestType.IMMEDIATE;
	
	@SuppressWarnings("unused")
    private void printResponses(List<Response> responses, StudentPersonalConsumer consumer)
	{
		try
		{
			if (responses != null)
			{
				int i = 1;
				for (Response response : responses)
				{
					System.out.println("Response "+i+":\n"+response);
					if (response.hasError())
					{
						System.out.println("Error for Response "+i+": "+response.getError());
					}
					else // We should have a student personal
					{
						if (response.getHasEntity())
						{
							System.out.println("Data Object Response "+i+": "+consumer.getMarshaller().marshalToXML(response.getDataObject()));
						}
						else
						{
							System.out.println("Data Object Response "+i+": No Data Returned. Respnse Status = "+response.getStatus()+" ("+response.getStatusMessage()+")");							
						}
					}
					i++;
				}
			}
			else
			{
				System.out.println("Responses from attempt to create Student: null");				
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
  @SuppressWarnings("unused")
  private StudentPersonalCollectionType loadStudents(DataModelUnmarshalFactory unmarshaller)
  {
    String inputEnvXML = FileReaderWriter.getFileContent(MULTI_STUDENT_FILE_NAME);
    try
    {
      return (StudentPersonalCollectionType) unmarshaller.unmarshalFromXML(inputEnvXML, StudentPersonalCollectionType.class);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
  }

	
	private StudentPersonalConsumer getConsumer()
	{
		return new StudentPersonalConsumer();
	}

	
	/* -----------------------
	 * Section for Exercise 2
	 ------------------------*/
	private void getStudents(StudentPersonalConsumer consumer)
	{
		System.out.println("Start 'Get List of Students' ...");

		//TODO: Exercise 2: Call to get a list of students using paging! => consumer.retrieve(...)
		
		//System.out.println("Responses from attempt to Get List of Students:");
		//printResponses(responses, consumer);
		
		System.out.println("Finished 'Get List of Students'.");
	}

	private void getStudent(StudentPersonalConsumer consumer)
	{
		System.out.println("Start 'Get Single Student'...");

		//TODO: Exercise 2: Call to get single student... use paging! => consumer.retrievByPrimaryKey(...)
		
		//System.out.println("Responses from attempt to Get Single Student:");
		//printResponses(responses, consumer);
		
		System.out.println("Finished 'Get Single Student'.");
	}
  /* -----------------------------
   * End of Section for Exercise 2
   -------------------------------*/
	

  /* -----------------------
   * Section for Exercise 4
   ------------------------*/
  private void deleteStudents(StudentPersonalConsumer consumer)
  {
    System.out.println("Start 'Delete List of Students' ...");

    //TODO: Exercise 4: Call to delete a list of students => consumer.deleteMany(...)
    
    System.out.println("Finished 'Delete List of Students'.");
  }

  private void updateStudent(StudentPersonalConsumer consumer)
  {
    System.out.println("Start 'Update List of Students'...");

    //TODO: Exercise 4: Call to update a list of student => consumer.updateMany(...)
    
    System.out.println("Finished 'Update List of Students'.");
  }
  /* -----------------------------
   * End of Section for Exercise 4
   -------------------------------*/

	/*
	 * ----------------------------------
	 * Section for Exercise: Service Path
	 * ----------------------------------
	 */
	/*
	 * Possible Solution for Exercise: Service Path
	 */	
	private void getStudentsByServicePath(String parent, String value, StudentPersonalConsumer consumer)
	{
	    System.out.println("Start 'Get Students By Service Path '...");
	    
	    //TODO: Service Path Exercise:
	    
	    //Step 1: Create a QueryCriteria and add a 'Predicate' (where clause) below
		
		try
		{
			//Step 2: Call consumer.retrieveByServicePath() with the QueryCriteria and other parameters.
		    //        Refer to Javadoc for details of the parameters to use.
		
			//Step 3: Print out the result - comment out the line below....
			//printResponses(responses, consumer);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		System.out.println("Finished 'Get Students By Service Path'.");
	}
	
	/*
	 * ----------------------------------------- 
	 * End of Section for Exercise: Service Path
	 * -----------------------------------------
	 */


	public static void main(String[] args)
	{
		DemoConsumer demo = new DemoConsumer();
		System.out.println("Start DemoConsumer...");

		if (ConsumerLoader.initialise(CONSUMER_ID))
		{
			StudentPersonalConsumer consumer = demo.getConsumer();

			// Use for Exercise 2
			demo.getStudent(consumer);
			demo.getStudents(consumer);
			
			// Use for Exercise 4 - uncomment the 2 lines below
			//demo.deleteStudents(consumer);
			//demo.updateStudent(consumer);
			
			// Use for Exercise: Service Path - uncomment the line below
			//demo.getStudentsByServicePath("TeachingGroups", "64A309DA063A2E35B359D75101A8C3D1", consumer);
		}

		ConsumerLoader.shutdown();
		System.out.println("End DemoConsumer.");
	}
}
