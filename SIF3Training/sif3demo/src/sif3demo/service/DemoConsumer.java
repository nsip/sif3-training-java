/*
 * DemoConsumer.java Created: 12/08/2014
 * 
 * Copyright 2014-2018 Systemic Pty Ltd
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

import au.com.systemic.framework.utils.AdvancedProperties;
import au.com.systemic.framework.utils.FileReaderWriter;

import sif.dd.au30.conversion.DataModelUnmarshalFactory;
import sif.dd.au30.model.FQReportingCollectionType;
import sif.dd.au30.model.FQReportingType;
import sif.dd.au30.model.ObjectFactory;
import sif.dd.au30.model.StudentPersonalCollectionType;
import sif3.common.conversion.MarshalFactory;
import sif3.common.exception.PersistenceException;
import sif3.common.exception.ServiceInvokationException;
import sif3.common.header.HeaderValues.RequestType;
import sif3.common.model.PagingInfo;
import sif3.common.model.QueryCriteria;
import sif3.common.model.ServicePathPredicate;
import sif3.common.utils.UUIDGenerator;
import sif3.common.ws.BulkOperationResponse;
import sif3.common.ws.CreateOperationStatus;
import sif3.common.ws.OperationStatus;
import sif3.common.ws.Response;
import sif3.infra.common.env.mgr.ConsumerEnvironmentManager;
import sif3.infra.rest.consumer.ConsumerLoader;
import sif3demo.consumer.FQReportingConsumer;
import sif3demo.consumer.StudentPersonalConsumer;
import sif3demo.service.DemoConsumer;

/**
 * @author Joerg Huber
 * 
 */
public class DemoConsumer
{
    // All paths below are relative to the install directory of this project.
//  private final static String SINGLE_STUDENT_FILE_NAME = "/TestData/xml/input/StudentPersonal.xml";
//  private final static String MULTI_STUDENT_FILE_NAME = "/TestData/xml/input/StudentPersonals5.xml";
//  private final static String SINGLE_STUDENT_FILE_NAME = "/TestData/xml/input/StudentPersonal.xml";
    private final static String MULTI_STUDENT_FILE_NAME = "/TestData/xml/input/StudentPersonals5.xml";
    private final static String MULTI_FQ_FILE_NAME = "/TestData/xml/input/FQReportings.xml";
//    private static final String CONSUMER_ID = "StudentConsumer";
    private static final String CONSUMER_ID = "HITSStudentConsumer";
    
    private static final RequestType REQUEST_TYPE = RequestType.IMMEDIATE;

    private ObjectFactory of = new ObjectFactory();
    private String rootInstallDir = null;

    public DemoConsumer() throws Exception
    {
        if (ConsumerLoader.initialise(CONSUMER_ID))
        {
            AdvancedProperties properties = ConsumerEnvironmentManager.getInstance().getServiceProperties();
            rootInstallDir = properties.getPropertyAsString("test.install.root.dir", null);
            System.out.println("Root Install Dir is '"+rootInstallDir+"'");
            if (rootInstallDir == null)
            {
                shutdown();
                throw new Exception("Please set the property 'test.install.root.dir' in the file '"+CONSUMER_ID+".properties' to the value of the install directory of this project.");
            }
        }
        else
        {
            throw new Exception("Failed to initialise consumer. See previous error log entries.");
        }
    }
    
    public void shutdown()
    {
        ConsumerLoader.shutdown();
    }

    @SuppressWarnings("unused")
    private void printResponses(List<Response> responses, MarshalFactory marshaller)
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
							System.out.println("Data Object Response " + i + ": " + marshaller.marshalToXML(response.getDataObject()));
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
                System.out.println("Responses retrieved: null");                
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
		String inputEnvXML = FileReaderWriter.getFileContent(rootInstallDir+MULTI_STUDENT_FILE_NAME);
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

    @SuppressWarnings("unused")
    private FQReportingCollectionType loadFQReports(DataModelUnmarshalFactory unmarshaller)
    {
        String inputEnvXML = FileReaderWriter.getFileContent(rootInstallDir+MULTI_FQ_FILE_NAME);
        try
        {
            return (FQReportingCollectionType) unmarshaller.unmarshalFromXML(inputEnvXML, FQReportingCollectionType.class);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    private StudentPersonalConsumer getStudentConsumer()
    {
        return new StudentPersonalConsumer();
    }

    private FQReportingConsumer getFQConsumer()
    {
        return new FQReportingConsumer();
    }

    /* ----------------------------------
     * Section for Exercise 2 - Option 1
     ----------------------------------*/
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
			printResponses(responses, consumer.getMarshaller());
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
		    // The primary key must be a refID value of a student object.
			List<Response> responses = consumer.retrievByPrimaryKey("24ed508e1ed04bba82198233efa55859", null);
			System.out.println("Responses from attempt to Get Student:");
			printResponses(responses, consumer.getMarshaller());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		System.out.println("Finished 'Get Single Student'.");
	}

    /* ---------------------------------------
     * End Section for Exercise 2 - Option 1
     ----------------------------------------*/

    /* -----------------------------------
     * Section for Exercise 2 - Option 2
     ------------------------------------*/
    private void submitFQ(FQReportingConsumer consumer)
    {
        System.out.println("Start 'Submit/Create List of FQReporting Objects' ...");

        // Option 1: Load data from a file (see loadFQReports() method in this class.
        try
        {
            FQReportingCollectionType fqs = loadFQReports((DataModelUnmarshalFactory)consumer.getUnmarshaller());
            List<BulkOperationResponse<CreateOperationStatus>> responses = consumer.createMany(fqs, null, REQUEST_TYPE);
            System.out.println("Responses from attempt to Submit/Create List of FQReporting Objects:" + responses);
        }
        catch (IllegalArgumentException | PersistenceException | ServiceInvokationException e)
        {
            e.printStackTrace();
        }
        
        // Option 2: Manually create a FQReportingCollectionType object and populate it with data
        try
        {
            FQReportingCollectionType fqs = new FQReportingCollectionType();
            FQReportingType fq = new FQReportingType();
            fq.setRefId(UUIDGenerator.getUUID());
            fq.setLocalId(of.createFQReportingTypeLocalId("1198"));
            fq.setACARAId(of.createFQReportingTypeACARAId("A4099"));
            fq.setReportingAuthority(of.createFQReportingTypeReportingAuthority("St. Mary College"));
            fq.setReportingAuthoritySystem(of.createFQReportingTypeReportingAuthoritySystem("Catholic"));
            // etc...
            
            fqs.getFQReporting().add(fq);
            
            List<BulkOperationResponse<CreateOperationStatus>> responses = consumer.createMany(fqs, null, REQUEST_TYPE);
            System.out.println("Responses from attempt to Submit/Create List of FQReporting Objects:" + responses);
        }
        catch (IllegalArgumentException | PersistenceException | ServiceInvokationException e)
        {
            e.printStackTrace();
        }
        
    }

    private void getFQ(FQReportingConsumer consumer)
    {
        System.out.println("Start 'Get List of FQReporting Objects'...");
        try
        {
            List<Response> responses = consumer.retrieve(new PagingInfo(10, 1), null, REQUEST_TYPE);
            System.out.println("Responses from attempt to Get All FQ Reports:");
            printResponses(responses, consumer.getMarshaller());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        System.out.println("Finished 'Get List of FQReporting Objects'.");
    }
    
    /* ---------------------------------------
     * End Section for Exercise 2 - Option 2
     ---------------------------------------*/
	
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
	    resourceIDs.add(UUIDGenerator.getUUID());
		resourceIDs.add(UUIDGenerator.getUUID());
		resourceIDs.add(UUIDGenerator.getUUID());
		resourceIDs.add(UUIDGenerator.getUUID());
		resourceIDs.add(UUIDGenerator.getUUID());
	    
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
	    StudentPersonalCollectionType students = loadStudents((DataModelUnmarshalFactory)consumer.getUnmarshaller());
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
			printResponses(responses, consumer.getMarshaller());
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
        try
        {
            System.out.println("Start DemoConsumer...");
            DemoConsumer demo = new DemoConsumer();
                
            StudentPersonalConsumer studentConsumer = demo.getStudentConsumer();
            FQReportingConsumer fqConsumer = demo.getFQConsumer();

            //
            // Exercise 2: You can choose to implement Student methods (Option 1) or FQ Methods (Option 2)
            //             Depending on the option, you need to un-comment out the appropriate methods.
            
            // Use for Exercise 2 - Option 1: Student Methods
            //demo.getStudent(studentConsumer); // Implement that method ...
            //demo.getStudents(studentConsumer); // Implement that method ...

            // Use for Exercise 2 - Option : FQ Methods
            //demo.submitFQ(fqConsumer); // Implement that method ...
            //demo.getFQ(fqConsumer); // Implement that method ...
            
            //
            // End Exercise 2
            //
            
            //
            // End Exercise 4 - uncomment the 2 lines below
            //
            
            //demo.deleteStudents(studentConsumer); // Implement that method ...
            //demo.updateStudent(studentConsumer); // Implement that method ...
            
            //
            // End Exercise 4
            //

            // Use for Exercise: Service Path - uncomment the line below
            //demo.getStudentsByServicePath("TeachingGroups", "64A309DA063A2E35B359D75101A8C3D1", studentConsumer); // Implement that method ...

            demo.shutdown();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        System.out.println("End DemoConsumer.");
	}

}
