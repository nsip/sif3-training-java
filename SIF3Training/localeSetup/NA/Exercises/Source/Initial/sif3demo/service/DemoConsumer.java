/*
 * DemoConsumer.java 
 * Created: 12/08/2014
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

import sif.dd.us32.conversion.DataModelUnmarshalFactory;
import sif.dd.us32.model.K12StudentCollectionType;
import sif3.common.ws.Response;
import sif3.infra.rest.consumer.ConsumerLoader;
import sif3demo.consumer.StudentConsumer;
import au.com.systemic.framework.utils.FileReaderWriter;

/**
 * @author Joerg Huber
 * 
 */
public class DemoConsumer
{
//	private final static String SINGLE_STUDENT_FILE_NAME = "C:/DEV/eclipseWorkspace/SIF3Training-US/TestData/xml/input/StudentPersonal.xml";
//	private final static String MULTI_STUDENT_FILE_NAME = "C:/DEV/eclipseWorkspace/SIF3Training-US/TestData/xml/input/K12Students2.xml";
//	private final static String SINGLE_STUDENT_FILE_NAME = "C:/Development/GitHubRepositories/SIF3Training-US/SIF3Training-US/TestData/xml/input/StudentPersonal.xml";
	private final static String MULTI_STUDENT_FILE_NAME = "C:/Development/SIF3Training-US/TestData/xml/input/K12Students2.xml";
	private static final String CONSUMER_ID = "StudentConsumer";
	
//	private static final RequestType REQUEST_TYPE = RequestType.IMMEDIATE;
	
	@SuppressWarnings("unused")
    private void printResponses(List<Response> responses, StudentConsumer consumer)
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
  private K12StudentCollectionType loadStudents(DataModelUnmarshalFactory unmarshaller)
  {
    String inputEnvXML = FileReaderWriter.getFileContent(MULTI_STUDENT_FILE_NAME);
    try
    {
      return (K12StudentCollectionType) unmarshaller.unmarshalFromXML(inputEnvXML, K12StudentCollectionType.class);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
  }

	
	private StudentConsumer getConsumer()
	{
		return new StudentConsumer();
	}

	
	/* -----------------------
	 * Section for Exercise 2
	 ------------------------*/
	private void getStudents(StudentConsumer consumer)
	{
		System.out.println("Start 'Get List of Students' ...");

		//TODO: Exercise 2: Call to get a list of students using paging! => consumer.retrieve(...)
		
		//System.out.println("Responses from attempt to Get List of Students:");
		//printResponses(responses, consumer);
		
		System.out.println("Finished 'Get List of Students'.");
	}

	private void getStudent(StudentConsumer consumer)
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
  private void deleteStudents(StudentConsumer consumer)
  {
    System.out.println("Start 'Delete List of Students' ...");

    //TODO: Exercise 4: Call to delete a list of students => consumer.deleteMany(...)
    
    System.out.println("Finished 'Delete List of Students'.");
  }

  private void updateStudent(StudentConsumer consumer)
  {
    System.out.println("Start 'Update List of Students'...");

    //TODO: Exercise 4: Call to update a list of student => consumer.updateMany(...)
    
    System.out.println("Finished 'Update List of Students'.");
  }
  /* -----------------------------
   * End of Section for Exercise 4
   -------------------------------*/

	
	public static void main(String[] args)
	{
		DemoConsumer demo = new DemoConsumer();
		System.out.println("Start DemoConsumer...");

		if (ConsumerLoader.initialise(CONSUMER_ID))
		{
			StudentConsumer consumer = demo.getConsumer();

			// Use for Exercise 2
			demo.getStudent(consumer);
			demo.getStudents(consumer);
			
			// Use for Exercise 4 - uncomment the 2 lines below
			//demo.deleteStudents(consumer);
			//demo.updateStudent(consumer);
		}

		ConsumerLoader.shutdown();
		System.out.println("End DemoConsumer.");
	}
}
