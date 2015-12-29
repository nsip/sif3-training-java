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

import java.util.ArrayList;
import java.util.List;

import sif.dd.us33.conversion.DataModelUnmarshalFactory;
import sif.dd.us33.model.XStudentCollectionType;
import sif3.common.header.HeaderValues.RequestType;
import sif3.common.model.PagingInfo;
import sif3.common.model.QueryCriteria;
import sif3.common.model.ServicePathPredicate;
import sif3.common.utils.UUIDGenerator;
import sif3.common.ws.BulkOperationResponse;
import sif3.common.ws.OperationStatus;
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
//	private final static String SINGLE_STUDENT_FILE_NAME = "C:/DEV/lunaWorkspace/SIF3Training/TestData/xml/input/xStudent.xml";
//	private final static String MULTI_STUDENT_FILE_NAME = "C:/DEV/lunaWorkspace/SIF3Training/TestData/xml/input/xStudents2.xml";
//	private final static String SINGLE_STUDENT_FILE_NAME = "C:/Development/GitHubRepositories/SIF3Training/SIF3Training/TestData/xml/input/xStudent.xml";
	private final static String MULTI_STUDENT_FILE_NAME = "C:/Development/GitHubRepositories/SIF3Training/SIF3Training/TestData/xml/input/xStudents2.xml";
	private static final String CONSUMER_ID = "StudentConsumer";

	private static final RequestType REQUEST_TYPE = RequestType.IMMEDIATE;

	private void printResponses(List<Response> responses, StudentConsumer consumer)
	{
		try
		{
			if (responses != null)
			{
				int i = 1;
				for (Response response : responses)
				{
					System.out.println("Response " + i + ":\n" + response);
					if (response.hasError())
					{
						System.out.println("Error for Response " + i + ": " + response.getError());
					}
					else
					// We should have a student personal
					{
						if (response.getHasEntity())
						{
							System.out.println("Data Object Response " + i + ": " + consumer.getMarshaller().marshalToXML(response.getDataObject()));
						}
						else
						{
							System.out.println("Data Object Response " + i + ": No Data Returned. Respnse Status = " + response.getStatus() + " (" + response.getStatusMessage() + ")");
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
	private XStudentCollectionType loadStudents(DataModelUnmarshalFactory unmarshaller)
	{
		String inputEnvXML = FileReaderWriter.getFileContent(MULTI_STUDENT_FILE_NAME);
		try
		{
			return (XStudentCollectionType) unmarshaller.unmarshalFromXML(inputEnvXML, XStudentCollectionType.class);
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

	/*
	 * ----------------------- 
	 * Section for Exercise 2 
	 * ------------------------
	 */
	/*
	 * Possible Solution for Exercise 2
	 */
	private void getStudents(StudentConsumer consumer)
	{
		System.out.println("Start 'Get List of Students' ...");

		try
		{
			List<Response> responses = consumer.retrieve(new PagingInfo(5, 1), null, REQUEST_TYPE);
			System.out.println("Responses from attempt to Get All Students:");
			printResponses(responses, consumer);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		System.out.println("Finished 'Get List of Students'.");
	}

	/*
	 * Possible Solution for Exercise 2
	 */
	private void getStudent(StudentConsumer consumer)
	{
		System.out.println("Start 'Get Single Student'...");

		try
		{
			List<Response> responses = consumer.retrievByPrimaryKey("7FD01E92-9BD8-4BDB-933F-726171A3C0E9", null);
			System.out.println("Responses from attempt to Get Student:");
			printResponses(responses, consumer);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		System.out.println("Finished 'Get Single Student'.");
	}

	/*
	 * ----------------------------- 
	 * End of Section for Exercise 2 
	 * -------------------------------
	 */

	/*
	 * ----------------------- 
	 * Section for Exercise 4 
	 * ------------------------
	 */
	/*
	 * Possible Solution for Exercise 4
	 */
	private void deleteStudents(StudentConsumer consumer)
	{
		System.out.println("Start 'Delete List of Students' ...");

		// Create an ArrayList of 5 GUIDs.
	    ArrayList<String> resourceIDs = new ArrayList<String>();
	    resourceIDs.add("93DD6FA1-B8CD-4633-A113-01F4E30CB1BD");
		resourceIDs.add("71A459CC-BB41-437C-9EEB-10CCB450827F");
		resourceIDs.add("11A459CC-BB41-437C-9EEB-10CCB4508271"); // does not exist
		resourceIDs.add("0120E185-2528-4471-897B-AF8C7A908662");
		resourceIDs.add("C6794A1A-A716-4243-B8C8-19B32DC371A1");
	    
	    try
	    {
	      List<BulkOperationResponse<OperationStatus>> responses = consumer.deleteMany(resourceIDs, null, REQUEST_TYPE);

	      // Print Response to deleteMany to the Screen...
	      if (responses != null)
	      {
	        int i = 1;
	        for (BulkOperationResponse<OperationStatus> response : responses)
	        {
	          System.out.println("Response "+i+":\n"+response);
	          i++;
	        }
	      }
	      else
	      {
	        System.out.println("Responses from attempt to delete Students: null");        
	      }
	    }
	    catch (Exception ex)
	    {
	      ex.printStackTrace();
	    }

		System.out.println("Finished 'Delete List of Students'.");
	}

	/*
	 * Possible Solution for Exercise 4
	 */
	private void updateStudent(StudentConsumer consumer)
	{
		System.out.println("Start 'Update List of Students'...");

		// Load a set of students from a file. Ensure that MULTI_STUDENT_FILE_NAME constant at the 
		// top of this class points to the correct location where the StudentPersonals5.xml is located.
	    XStudentCollectionType students = loadStudents((DataModelUnmarshalFactory)consumer.getUnmarshaller());
	    try
	    {
	      List<BulkOperationResponse<OperationStatus>> responses = consumer.updateMany(students, null, REQUEST_TYPE);

	      // Print Response to deleteMany to the Screen...
	      if (responses != null)
	      {
	        int i = 1;
	        for (BulkOperationResponse<OperationStatus> response : responses)
	        {
	          System.out.println("Response "+i+":\n"+response);
	          if (response.hasError())
	          {
	            System.out.println("Error for Response "+i+": "+response.getError());
	          }
	          else // We should have a student personal
	          {
	            System.out.println("Student Response "+i+": "+response.getOperationStatuses());
	          }
	          i++;
	        }
	      }
	      else
	      {
	        System.out.println("Responses from attempt to update Students: null");        
	      }
	    }
	    catch (Exception ex)
	    {
	      ex.printStackTrace();
	    }

		System.out.println("Finished 'Update List of Students'.");
	}

	/*
	 * ----------------------------- 
	 * End of Section for Exercise 4 
	 * -------------------------------
	 */
	
	/*----------------------------------------*/
	/*-- Section for Exercise: Service Path --*/
	/*----------------------------------------*/

  	/*
	 * Possible Solution for Exercise: Service Path
	 */	
	private void getStudentsByServicePath(String parent, String value, StudentConsumer consumer)
	{
	    System.out.println("Start 'Get Students By Service Path '...");
	    
		QueryCriteria criteria = new QueryCriteria();
		criteria.addPredicate(new ServicePathPredicate(parent, value));
		try
		{
			// Get all students for a service path criteria. Get 5 students per page (i.e page 0). 
			List<Response> responses = consumer.retrieveByServicePath(criteria, new PagingInfo(5, 0), null, REQUEST_TYPE);
			System.out.println("Responses from attempt to Get All Students for '" + criteria + "': ");
			printResponses(responses, consumer);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		System.out.println("Finished 'Get Students By Service Path'.");
	}
	
	/*-----------------------------------------------*/
	/*-- End of Section for Exercise: Service Path --*/
	/*-----------------------------------------------*/
	

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
//			demo.deleteStudents(consumer);
//			demo.updateStudent(consumer);

			// Use for Exercise: Service Path - uncomment the line below
//			demo.getStudentsByServicePath("xSchools", "2351B3F4-C788-4500-177B-23E0D87AEC97", consumer);
		}

		ConsumerLoader.shutdown();
		System.out.println("End DemoConsumer.");
	}

}
