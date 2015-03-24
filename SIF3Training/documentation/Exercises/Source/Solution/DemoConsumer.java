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

import java.util.ArrayList;
import java.util.List;

import au.com.systemic.framework.utils.FileReaderWriter;

import sif.dd.au30.conversion.DataModelUnmarshalFactory;
import sif.dd.au30.model.StudentCollectionType;
import sif3.common.header.HeaderValues.RequestType;
import sif3.common.model.PagingInfo;
import sif3.common.model.QueryCriteria;
import sif3.common.model.ServicePathPredicate;
import sif3.common.utils.UUIDGenerator;
import sif3.common.ws.BulkOperationResponse;
import sif3.common.ws.OperationStatus;
import sif3.common.ws.Response;
import sif3.infra.rest.consumer.ConsumerLoader;
import sif3demo.consumer.StudentPersonalConsumer;
import sif3demo.service.DemoConsumer;

/**
 * @author Joerg Huber
 * 
 */
public class DemoConsumer
{
//	private final static String SINGLE_STUDENT_FILE_NAME = "C:/DEV/eclipseWorkspace/SIF3Training/TestData/xml/input/StudentPersonal.xml";
	private final static String MULTI_STUDENT_FILE_NAME = "C:/DEV/eclipseWorkspace/SIF3Training/TestData/xml/input/StudentPersonals5.xml";
//	private final static String SINGLE_STUDENT_FILE_NAME = "C:/Development/GitHubRepositories/SIF3Training/SIF3Training/TestData/xml/input/StudentPersonal.xml";
//	private final static String MULTI_STUDENT_FILE_NAME = "C:/Development/GitHubRepositories/SIF3Training/SIF3Training/TestData/xml/input/StudentPersonals5.xml";

	private static final String CONSUMER_ID = "StudentConsumer";

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
	private StudentCollectionType loadStudents(DataModelUnmarshalFactory unmarshaller)
	{
		String inputEnvXML = FileReaderWriter.getFileContent(MULTI_STUDENT_FILE_NAME);
		try
		{
			return (StudentCollectionType) unmarshaller.unmarshalFromXML(inputEnvXML, StudentCollectionType.class);
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

	/*
	 * ----------------------- 
	 * Section for Exercise 2 
	 * ------------------------
	 */
	/*
	 * Possible Solution for Exercise 2
	 */
	private void getStudents(StudentPersonalConsumer consumer)
	{
		System.out.println("Start 'Get List of Students' ...");

		try
		{
			List<Response> responses = consumer.retrieve(new PagingInfo(5, 17), null, REQUEST_TYPE);
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
	private void getStudent(StudentPersonalConsumer consumer)
	{
		System.out.println("Start 'Get Single Student'...");

		try
		{
			List<Response> responses = consumer.retrievByPrimaryKey("24ed508e1ed04bba82198233efa55859", null);
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
	private void deleteStudents(StudentPersonalConsumer consumer)
	{
		System.out.println("Start 'Delete List of Students' ...");

		// Create an ArrayList of 5 GUIDs.
	    ArrayList<String> resourceIDs = new ArrayList<String>();
	    resourceIDs.add(UUIDGenerator.getSIF2GUIDUpperCase());
		resourceIDs.add(UUIDGenerator.getSIF2GUIDUpperCase());
		resourceIDs.add(UUIDGenerator.getSIF2GUIDUpperCase());
		resourceIDs.add(UUIDGenerator.getSIF2GUIDUpperCase());
		resourceIDs.add(UUIDGenerator.getSIF2GUIDUpperCase());
	    
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
	private void updateStudent(StudentPersonalConsumer consumer)
	{
		System.out.println("Start 'Update List of Students'...");

		// Load a set of students from a file. Ensure that MULTI_STUDENT_FILE_NAME constant at the 
		// top of this class points to the correct location where the StudentPersonals5.xml is located.
	    StudentCollectionType students = loadStudents((DataModelUnmarshalFactory)consumer.getUnmarshaller());
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
	    
		QueryCriteria criteria = new QueryCriteria();
		criteria.addPredicate(new ServicePathPredicate(parent, value));
		try
		{
			// Get all students for a service path cirteria. Get 5 students per page (i.e page 1). 
			List<Response> responses = consumer.retrieveByServicePath(criteria, new PagingInfo(5, 1), null, REQUEST_TYPE);
			System.out.println("Responses from attempt to Get All Students for '" + criteria + "': ");
			printResponses(responses, consumer);
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
