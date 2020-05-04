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

import java.util.List;

import au.com.systemic.framework.utils.AdvancedProperties;
import au.com.systemic.framework.utils.FileReaderWriter;
import sif.dd.au30.conversion.DataModelUnmarshalFactory;
//import sif.dd.au30.model.FinancialQuestionnaireSubmissionCollectionType;
//import sif.dd.au30.model.FinancialQuestionnaireSubmissionType;
import sif.dd.au30.model.ObjectFactory;
import sif.dd.au30.model.StudentPersonalCollectionType;
import sif3.common.conversion.MarshalFactory;
import sif3.common.header.HeaderValues.RequestType;
import sif3.common.ws.Response;
import sif3.infra.common.env.mgr.ConsumerEnvironmentManager;
import sif3.infra.rest.consumer.ConsumerLoader;
//import sif3demo.consumer.FQReportingConsumer;
import sif3demo.consumer.StudentPersonalConsumer;

/**
 * @author Joerg Huber
 * 
 */
public class DemoConsumer
{
    // All paths below are relative to the install directory of this project.
//	private final static String SINGLE_STUDENT_FILE_NAME = "/TestData/xml/input/StudentPersonal.xml";
//	private final static String MULTI_STUDENT_FILE_NAME = "/TestData/xml/input/StudentPersonals5.xml";
//	private final static String SINGLE_STUDENT_FILE_NAME = "/TestData/xml/input/StudentPersonal.xml";
	private final static String MULTI_STUDENT_FILE_NAME = "/TestData/xml/input/StudentPersonals5.xml";
//  private final static String MULTI_FQ_FILE_NAME = "/TestData/xml/input/FQReportings.xml";
//    private final static String SINGLE_FINSUBMISSION_FILE_NAME = "/TestData/xml/input/FinancialQuestionnaireSubmission.xml";
	private static final String CONSUMER_ID = "StudentConsumer";
//    private static final String CONSUMER_ID = "HITSStudentConsumer";
	
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
					System.out.println("Response "+i+":\n"+response);
					if (response.hasError())
					{
						System.out.println("Error for Response "+i+": "+response.getError());
					}
					else // We should have a student personal
					{
						if (response.getHasEntity())
						{
							System.out.println("Data Object Response "+i+": "+marshaller.marshalToXML(response.getDataObject()));
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
    
    private StudentPersonalConsumer getStudentConsumer()
    {
        return new StudentPersonalConsumer();
    }

    // Removed in SIF AU 3.4.6
/*    
    @SuppressWarnings("unused")
    private FinancialQuestionnaireSubmissionType loadFQReports(DataModelUnmarshalFactory unmarshaller)
    {
        String inputEnvXML = FileReaderWriter.getFileContent(rootInstallDir+SINGLE_FINSUBMISSION_FILE_NAME);
        try
        {
            return (FinancialQuestionnaireSubmissionType) unmarshaller.unmarshalFromXML(inputEnvXML, FinancialQuestionnaireSubmissionType.class);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    private FQReportingConsumer getFQConsumer()
    {
        return new FQReportingConsumer();
    }
*/

    /* ----------------------------------
	 * Section for Exercise 2 - Option 1
	 ----------------------------------*/
	private void getStudents(StudentPersonalConsumer consumer)
	{
		System.out.println("Start 'Get List of Students' ...");

		//TODO: Exercise 2: Call to get a list of students using paging! => consumer.retrieve(...)
		
		//System.out.println("Responses from attempt to Get List of Students:");
		//printResponses(responses, consumer.getMarshaller());
		
		System.out.println("Finished 'Get List of Students'.");
	}

	private void getStudent(StudentPersonalConsumer consumer)
	{
		System.out.println("Start 'Get Single Student'...");

		//TODO: Exercise 2: Call to get single student... use paging! => consumer.retrievByPrimaryKey(...)
		
		//System.out.println("Responses from attempt to Get Single Student:");
		//printResponses(responses, consumer.getMarshaller());
		
		System.out.println("Finished 'Get Single Student'.");
	}

	/* ---------------------------------------
     * End Section for Exercise 2 - Option 1
     ----------------------------------------*/

	/* -----------------------------------
     * Section for Exercise 2 - Option 2
     ------------------------------------*/
    // Removed in SIF AU 3.4.6
/*   
    private void submitFQ(FQReportingConsumer consumer)
    {
        System.out.println("Start 'Submit/Create List of FinancialQuestionnaireSubmission Objects' ...");

        //TODO: Exercise 2: A FinancialQuestionnaireSubmissionType must be populated first ...
        // Option 1: Load data from a file (see loadFQReports() method in this class.
        // Option 2: Manually create a FinancialQuestionnaireSubmissionType object and populate it with data
        
        //TODO: submit the FinancialQuestionnaireSubmissionType object by calling the "createSingle" method on the
        //      consumer. 
        //List<Response> responses = consumer.createSingle(...);
       
        //System.out.println("Responses from attempt to Submit/Create List of FinancialQuestionnaireSubmission Objects:" + responses);
    }

    private void getFQ(FQReportingConsumer consumer)
    {
        System.out.println("Start 'Get List of FinancialQuestionnaireSubmission Objects'...");

        //TODO: Exercise 2: Call to get a list of FinancialQuestionnaireSubmission using paging! => consumer.retrieve(...)
        
        //System.out.println("Responses from attempt to Get All Financial Questionnaire Submission:");
        //printResponses(responses, consumer.getMarshaller());

        
        System.out.println("Finished 'Get List of FinancialQuestionnaireSubmission Objects'.");
    }
*/
    /* ---------------------------------------
     * End Section for Exercise 2 - Option 2
     ---------------------------------------*/

	/* -----------------------
	 * Section for Exercise 4
     ------------------------*/
    private void deleteStudents(StudentPersonalConsumer consumer)
    {
        System.out.println("Start 'Delete List of Students' ...");

        // TODO: Exercise 4: Call to delete a list of students => consumer.deleteMany(...)

        System.out.println("Finished 'Delete List of Students'.");
    }

    private void updateStudent(StudentPersonalConsumer consumer)
    {
        System.out.println("Start 'Update List of Students'...");

        // TODO: Exercise 4: Call to update a list of student => consumer.updateMany(...)

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
			//printResponses(responses, consumer.getMarshaller());
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
			

			//
			// Exercise 2: You can choose to implement Student methods (Option 1) or FQ Methods (Option 2)
			//             Depending on the option, you need to un-comment out the appropriate methods.
			
			// Use for Exercise 2 - Option 1: Student Methods
			//demo.getStudent(studentConsumer); // Implement that method ...
			//demo.getStudents(studentConsumer); // Implement that method ...

            // Removed in SIF AU 3.4.6
/*			
            //FQReportingConsumer fqConsumer = demo.getFQConsumer();

			// Use for Exercise 2 - Option : FQ Methods
			//demo.submitFQ(fqConsumer); // Implement that method ...
			//demo.getFQ(fqConsumer); // Implement that method ...
*/			
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
