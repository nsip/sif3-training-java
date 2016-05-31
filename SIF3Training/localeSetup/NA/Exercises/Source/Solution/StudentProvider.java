/*
 * StudentProvider.java
 * Created: 30/09/2014
 *
 * Copyright 2014 Systemic Pty Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License 
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package sif3demo.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import sif.dd.us33.model.ObjectFactory;
import sif.dd.us33.model.XStudentCollectionType;
import sif.dd.us33.model.XStudentType;
import sif3.common.conversion.ModelObjectInfo;
import sif3.common.exception.DataTooLargeException;
import sif3.common.exception.PersistenceException;
import sif3.common.exception.UnmarshalException;
import sif3.common.exception.UnsupportedMediaTypeExcpetion;
import sif3.common.exception.UnsupportedQueryException;
import sif3.common.header.HeaderProperties;
import sif3.common.interfaces.QueryProvider;
import sif3.common.interfaces.SIFEventIterator;
import sif3.common.model.PagingInfo;
import sif3.common.model.QueryCriteria;
import sif3.common.model.QueryPredicate;
import sif3.common.model.RequestMetadata;
import sif3.common.model.ResponseParameters;
import sif3.common.model.SIFContext;
import sif3.common.model.SIFEvent;
import sif3.common.model.SIFZone;
import sif3.common.utils.UUIDGenerator;
import sif3.common.ws.CreateOperationStatus;
import sif3.common.ws.ErrorDetails;
import sif3.common.ws.OperationStatus;
import sif3demo.ModelObjectConstants;
import sif3demo.provider.iterator.StudentIterator;
import au.com.systemic.framework.utils.FileReaderWriter;
import au.com.systemic.framework.utils.StringUtils;

/**
 * @author Joerg Huber
 *
 */
public class StudentProvider extends USDataModelProviderWithEvents<XStudentCollectionType> implements QueryProvider
{
	private static HashMap<String, XStudentType> students = null; //new HashMap<String, XStudentType>();
	private ObjectFactory dmObjectFactory = new ObjectFactory();
	
    public StudentProvider()
    {
	    super();
	    
	    logger.debug("Constructor for StudentProvider has been called.");
	    if (students == null)
	    {
			logger.debug("Constructor for StudentProvider called for the first time. Try to load students from XML file...");
			// Load all students so that we can do some real stuff here.
			String studentFile = getServiceProperties().getPropertyAsString("provider.student.file.location", null);
			if (studentFile != null)
			{
				String inputXML = FileReaderWriter.getFileContent(studentFile, ModelObjectConstants.UTF_8);
				try
				{
				    XStudentCollectionType studentList = (XStudentCollectionType) getUnmarshaller().unmarshalFromXML(inputXML, getMultiObjectClassInfo().getObjectType());
					if ((studentList != null) && (studentList.getXStudent() != null))
					{
	    				students = new HashMap<String, XStudentType>();
						for (XStudentType xStudent : studentList.getXStudent())
						{
							students.put(xStudent.getRefId(), xStudent);
						}
						logger.debug("Loaded " + students.size() + " students into memory.");
					}
				}
				catch (UnmarshalException ex)
				{
					ex.printStackTrace();
				}
				catch (UnsupportedMediaTypeExcpetion ex)
				{
					ex.printStackTrace();
				}
			}
	    }
	    // If students are still null then something must have failed and would have been logged. For the purpose of making things work ok
	    // we initialise the students hashmap now. It will avoid null pointer errors.
		if (students == null)
		{
			students = new HashMap<String, XStudentType>();
		}
    }

    /*-------------------------------------*/
    /*-- EventProvider Interface Methods --*/
    /*-------------------------------------*/
    /*
     * (non-Javadoc)
     * @see sif3.common.interfaces.EventProvider#getSIFEvents()
     */
    @Override
    public SIFEventIterator<XStudentCollectionType> getSIFEvents()
    {
 	    return new StudentIterator(getServiceName(), getServiceProperties(), students);
    }
    
    
    /*
     * (non-Javadoc)
     * @see sif3.common.interfaces.EventProvider#onEventError(sif3.common.model.SIFEvent, sif3.common.model.SIFZone, sif3.common.model.SIFContext)
     */
    @Override
    public void onEventError(SIFEvent<XStudentCollectionType> sifEvent, SIFZone zone,SIFContext context)
    {
	    //We need to deal with the error. At this point in time we just log it.
    	if ((sifEvent != null) && (sifEvent.getSIFObjectList() != null))
    	{
    		try
    		{
    			String eventXML = getMarshaller().marshalToXML(sifEvent.getSIFObjectList());
        		logger.error("Failed to sent the following Objects as and Event to Zone ("+zone+") and Context ("+context+"):\n"+eventXML);
    		}
    		catch (Exception ex)
    		{
    			logger.error("Failed to marshall events.", ex);
    		}
    	}
    	else
    	{
    		logger.error("sifEvent Object is null, or there are no events on sifEvent.sifObjectList");
    	}
	    
    }

    /* (non-Javadoc)
     * @see sif3.common.interfaces.EventProvider#modifyBeforeSent(sif3.common.model.SIFEvent, sif3.common.model.SIFZone, sif3.common.model.SIFContext)
     */
    @Override
    public SIFEvent<XStudentCollectionType> modifyBeforePublishing(SIFEvent<XStudentCollectionType> sifEvent, SIFZone zone, SIFContext context, HeaderProperties customHTTPHeaders)
    {
    	// At this point we don't need to modify anything. Just send as is...
	    return sifEvent;
    }


    /*--------------------------------*/
    /*-- Provider Interface Methods --*/
    /*--------------------------------*/

    /* (non-Javadoc)
     * @see sif3.common.interfaces.Provider#retrievByPrimaryKey(java.lang.String, sif3.common.model.SIFZone, sif3.common.model.SIFContext)
     */
    @Override
    public Object retrievByPrimaryKey(String resourceID, SIFZone zone, SIFContext context, RequestMetadata metadata, ResponseParameters customResponseParams) throws IllegalArgumentException, PersistenceException
    {
    	if (StringUtils.isEmpty(resourceID))
    	{
    		throw new IllegalArgumentException("Resource ID is null or empty. It must be provided to retrieve an entity.");
    	}
    	
    	logger.debug("Retrieve student with Resoucre ID = "+resourceID+" and "+getZoneAndContext(zone, context));
    	
    	return students.get(resourceID);
    }

	/* (non-Javadoc)
     * @see sif3.common.interfaces.Provider#createSingle(java.lang.Object, sif3.common.model.SIFZone, sif3.common.model.SIFContext)
     */
    @Override
    public Object createSingle(Object data, boolean useAdvisory, SIFZone zone, SIFContext context, RequestMetadata metadata, ResponseParameters customResponseParams) throws IllegalArgumentException, PersistenceException
    {
    	logger.debug("Create Single Student for "+getZoneAndContext(zone, context));

    	// Must be of type K12StudentType
    	if (data instanceof XStudentType)
    	{
    	    XStudentType student = (XStudentType)data;
    		if (StringUtils.isEmpty(student.getRefId()))
    		{
    			//In future this should be a UUID instead of a GUID
    		  if (!useAdvisory)
    		  {
    		    // Create new UUID because the advisory shall not be used.
    		    student.setRefId(UUIDGenerator.getUUID());
    		  }
    		  // else leave student UUID untouched.
    		}
    		
    		//In the real implementation we would call a BL method here to create the Student.
    		return student;
    	}
    	else
    	{
    		throw new IllegalArgumentException("Expected Object Type  = XStudentType. Received Object Type = "+data.getClass().getSimpleName());
    	}
    }

	/* (non-Javadoc)
     * @see sif3.common.interfaces.Provider#updateSingle(java.lang.Object, java.lang.String, sif3.common.model.SIFZone, sif3.common.model.SIFContext)
     */
    @Override
    public boolean updateSingle(Object data, String resourceID, SIFZone zone, SIFContext context, RequestMetadata metadata, ResponseParameters customResponseParams) throws IllegalArgumentException, PersistenceException
    {
    	if (StringUtils.isEmpty(resourceID))
    	{
    		throw new IllegalArgumentException("Resource ID is null or empty. It must be provided to update an entity.");
    	}
    	
    	// Must be of type XStudentType
    	if (data instanceof XStudentType)
    	{
    		logger.debug("Update student with Resoucre ID = "+resourceID+" and "+getZoneAndContext(zone, context));

    		//In the real implementation we would call a BL method here to modify the Student.
    		return true;
    	}
    	else
    	{
    		throw new IllegalArgumentException("Expected Object Type  = XStudentType. Received Object Type = "+data.getClass().getSimpleName());
    	}
    }

	/* (non-Javadoc)
     * @see sif3.common.interfaces.Provider#deleteSingle(java.lang.String, sif3.common.model.SIFZone, sif3.common.model.SIFContext)
     */
    @Override
    public boolean deleteSingle(String resourceID, SIFZone zone, SIFContext context, RequestMetadata metadata, ResponseParameters customResponseParams) throws IllegalArgumentException, PersistenceException
    {
    	if (StringUtils.isEmpty(resourceID))
    	{
    		throw new IllegalArgumentException("Resource ID is null or empty. It must be provided to delete an entity.");
    	}
    	
    	logger.debug("Remove student with Resoucre ID = "+resourceID+" and "+getZoneAndContext(zone, context));

    	// In the real implementation we would call a BL method here to remove the Student. Here we only remove it from the hashmap. If
    	// student exists then the remove() will return a not null value.
    	return students.remove(resourceID) != null;
    }

    
	/* (non-Javadoc)
     * @see sif3.common.interfaces.Provider#retrive(sif3.common.model.SIFZone, sif3.common.model.SIFContext, sif3.common.model.PagingInfo)
     */
    @Override
    public Object retrieve(SIFZone zone, SIFContext context, PagingInfo pagingInfo, RequestMetadata metadata, ResponseParameters customResponseParams) throws PersistenceException, UnsupportedQueryException
    {
        logger.debug("Retrieve Students for "+getZoneAndContext(zone, context));

        ArrayList<XStudentType> studentList = fetchStudents(students, pagingInfo);
        
        XStudentCollectionType studentCollection = dmObjectFactory.createXStudentCollectionType();
        
        if (studentList != null)
        {
            studentCollection.getXStudent().addAll(studentList);
            return studentCollection;
        }
        else
        {
            return null;
        }
    }

	/* (non-Javadoc)
     * @see sif3.common.interfaces.Provider#createMany(java.lang.Object, sif3.common.model.SIFZone, sif3.common.model.SIFContext)
     */
    @Override
    public List<CreateOperationStatus> createMany(Object data, boolean useAdvisory, SIFZone zone, SIFContext context, RequestMetadata metadata, ResponseParameters customResponseParams) throws IllegalArgumentException, PersistenceException
    {
    	// Must be of type XStudentCollectionType
    	if (data instanceof XStudentCollectionType)
    	{
			logger.debug("Create students (Bulk Operation) for "+getZoneAndContext(zone, context));
			XStudentCollectionType students = (XStudentCollectionType) data;
			ArrayList<CreateOperationStatus> opStatus = new ArrayList<CreateOperationStatus>();
			int i = 0;
			for (XStudentType student : students.getXStudent())
			{
				if ((i % 3) == 0)
				{
					// Set advisoryID the same as resourceID.
					opStatus.add(new CreateOperationStatus(student.getRefId(), student.getRefId(), 404, new ErrorDetails(400, "Data not good.")));
				}
				else
				{
					if (useAdvisory)
					{
						// Advisory refId was used. Set resourceId and advisoryId to the same
						opStatus.add(new CreateOperationStatus(student.getRefId(), student.getRefId(), 201));
					}
					else
					{
						// Create a new refId (resourceID) but we must also report back the original RefId as the advisory if it was available.
						opStatus.add(new CreateOperationStatus(UUIDGenerator.getUUID(), student.getRefId(), 201));
					}
				}
				i++;
			}

    		return opStatus;
    	}
    	else
    	{
    		throw new IllegalArgumentException("Expected Object Type  = XStudentCollectionType. Received Object Type = "+data.getClass().getSimpleName());
    	}
    }

	/* (non-Javadoc)
     * @see sif3.common.interfaces.Provider#updateMany(java.lang.Object, sif3.common.model.SIFZone, sif3.common.model.SIFContext)
     */
    @Override
    public List<OperationStatus> updateMany(Object data, SIFZone zone, SIFContext context, RequestMetadata metadata, ResponseParameters customResponseParams) throws IllegalArgumentException, PersistenceException
    {
    	// Must be of type StudentPersonalType
    	if (data instanceof XStudentCollectionType)
    	{
			logger.debug("Update students (Bulk Operation) for " + getZoneAndContext(zone, context));
			XStudentCollectionType xStudents = (XStudentCollectionType) data;
			ArrayList<OperationStatus> opStatus = new ArrayList<OperationStatus>();
			for (XStudentType student : xStudents.getXStudent())
			{
				if (students.get(student.getRefId()) == null)
				{
					opStatus.add(new OperationStatus(student.getRefId(), 404, new ErrorDetails(404, "Student with RefId = " + student.getRefId() + " does not exist.")));
				}
				else
				{
					students.put(student.getRefId(), student);
					opStatus.add(new OperationStatus(student.getRefId(), 200));
				}
			}

    		return opStatus;
    	}
    	else
    	{
    		throw new IllegalArgumentException("Expected Object Type  = XStudentCollectionType. Received Object Type = "+data.getClass().getSimpleName());
    	}
    }

	/* (non-Javadoc)
     * @see sif3.common.interfaces.Provider#deleteMany(java.lang.Object, sif3.common.model.SIFZone, sif3.common.model.SIFContext)
     */
    @Override
    public List<OperationStatus> deleteMany(List<String> resourceIDs, SIFZone zone, SIFContext context, RequestMetadata metadata, ResponseParameters customResponseParams) throws IllegalArgumentException, PersistenceException
    {
    	logger.debug("Delete Students (Bulk Operation) for "+getZoneAndContext(zone, context));

    	//In the real implementation we would call a BL method here to modify the Student.
		ArrayList<OperationStatus> opStatus = new ArrayList<OperationStatus>();
		for (String resourceID : resourceIDs)
		{
			if (students.remove(resourceID) == null) // did not exist in hashmap
			{
				opStatus.add(new OperationStatus(resourceID, 404, new ErrorDetails(404, "Student with RefId = " + resourceID + " does not exist.")));
			
			}
			else
			{
				opStatus.add(new OperationStatus(resourceID, 200));
			}
		}
	    return opStatus;
    }
    
    
    /*-------------------------------------*/
    /*-- QueryProvider Interface Methods --*/
    /*-------------------------------------*/

	@Override
	public Object retrieveByServicePath(QueryCriteria queryCriteria, SIFZone zone, SIFContext context, PagingInfo pagingInfo, RequestMetadata metadata, ResponseParameters customResponseParams) throws PersistenceException, UnsupportedQueryException
	{
		logger.debug("Performing query by service path.");
		if (logger.isDebugEnabled())
		{
			logger.debug("Query Condition (given by service path): " + queryCriteria);
		}
		
		// Check if the query is xSchools
		List<QueryPredicate> predicates = queryCriteria.getPredicates();
		if ((predicates != null) && (predicates.size() == 1)) // ensure it is a valid condition
		{
			if ("xSchools".equals(predicates.get(0).getSubject()))
			{
		    	logger.debug("Retrieve Students for School "+predicates.get(0).getValue()+" for "+getZoneAndContext(zone, context)+" and RequestMetadata = "+metadata);
		    	
		    	// Get the school's refID from the query condition. 
		    	String schoolRefId = predicates.get(0).getValue();

                // In the real implementation the line below would be a call to your DB or service layer to retrieve the 
                // students for the given school.
		    	ArrayList<XStudentType> studentList = fetchStudentsForSchool(schoolRefId, pagingInfo);
		    	
		    	// Create the SIF Object
		    	XStudentCollectionType studentCollection = dmObjectFactory.createXStudentCollectionType();

		    	// Put final students into SIF Object
		    	if (studentList != null)
		    	{
		    		studentCollection.getXStudent().addAll(studentList);
		    		return studentCollection;
		    	}
		    	else
		    	{
		    		return null;
		    	}
			}
			else // all other service path query conditions are not supported by this implementation
			{
				throw new UnsupportedQueryException("The query condition (driven by the service path) "+queryCriteria+" is not supported by the provider.");
			}
		}
		else // not supported query (only single level service path query supported by this provider)
		{
			throw new UnsupportedQueryException("The query condition (driven by the service path) "+queryCriteria+" is not supported by the provider.");
		}
    }

	@Override
	public Object retrieveByQBE(Object exampleObject, SIFZone zone, SIFContext context, PagingInfo pagingInfo, RequestMetadata metadata, ResponseParameters customResponseParams) throws PersistenceException, UnsupportedQueryException, DataTooLargeException
	{
		// As part if this training course we do not implement QBE. Just return UnsupportedQueryException. 
		throw new UnsupportedQueryException("QBE not supported for xStudent Provider");
	}

    /*--------------------------------------*/
    /*-- Other required Interface Methods --*/
    /*--------------------------------------*/
	/* (non-Javadoc)
     * @see sif3.common.interfaces.DataModelLink#getSingleObjectClassInfo()
     */
    @Override
    public ModelObjectInfo getSingleObjectClassInfo()
    {
	    return ModelObjectConstants.STUDENT ;
    }

	/* (non-Javadoc)
     * @see sif3.common.interfaces.DataModelLink#getMultiObjectClassInfo()
     */
    @Override
    public ModelObjectInfo getMultiObjectClassInfo()
    {
	    return ModelObjectConstants.STUDENTS;
    }

    /*---------------------*/
    /*-- Private Methods --*/
    /*---------------------*/
	private String getZoneAndContext(SIFZone zone, SIFContext context)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("Zone = ").append((zone==null) ? "null" : zone.getId()+(zone.getIsDefault()?" (dafault)":"")).append(" ");
		buffer.append("- Context = ").append((context == null) ? "null" : context.getId());
		
		return buffer.toString();
	}

    private ArrayList<XStudentType> fetchStudents(HashMap<String, XStudentType> studentMap, PagingInfo pagingInfo)
    {
        ArrayList<XStudentType> studentList = new ArrayList<XStudentType>();
        if (pagingInfo == null) //return all
        {
            studentList.addAll(studentMap.values());
        }
        else
        {
            pagingInfo.setTotalObjects(studentMap.size());
            if ((pagingInfo.getPageSize() * (pagingInfo.getCurrentPageNo())) > studentMap.size())
            {
                return null; // Requested page outside of limits.
            }
            
            // retrieve applicable students
            Collection<XStudentType> allStudent = studentMap.values();
            int i = 0;
            int startPos = pagingInfo.getPageSize() * pagingInfo.getCurrentPageNo();
            int endPos = startPos + pagingInfo.getPageSize();
            for (Iterator<XStudentType> iter = allStudent.iterator(); iter.hasNext();)
            {
                XStudentType student = iter.next();
                if ((i>=startPos) && (i<endPos))
                {
                    studentList.add(student);
                }
                i++;
            }
            // Set the number of object that are returned in the paging info. Will ensure HTTP headers are set correctly.
            pagingInfo.setPageSize(studentList.size());
        }
        
        return studentList;
    }
    
    private ArrayList<XStudentType> fetchStudentsForSchool(String schoolRefId, PagingInfo pagingInfo)
    {
        // Get all students that have an enrolment at the given school from the in-memory student hashmap.
        HashMap<String, XStudentType> studentMap = new HashMap<String, XStudentType>();
        for (XStudentType student : students.values())
        {
            //System.out.println("Student ("+student.getRefId()+") has enrolment:" + (student.getEnrollment() != null));
            if ((student.getEnrollment() != null) && (student.getEnrollment().getSchoolRefId() != null) && (student.getEnrollment().getSchoolRefId().equals(schoolRefId)))
            {
                studentMap.put(student.getRefId(), student);
            }
        }
        
        // Check paging before returning above result
        return fetchStudents(studentMap, pagingInfo);
    }

}
