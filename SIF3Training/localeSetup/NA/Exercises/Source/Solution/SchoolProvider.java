/*
 * SchoolProvider.java
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

import org.apache.log4j.Logger;

import sif.dd.us33.conversion.DataModelMarshalFactory;
import sif.dd.us33.conversion.DataModelUnmarshalFactory;
import sif.dd.us33.model.ObjectFactory;
import sif.dd.us33.model.XSchoolCollectionType;
import sif.dd.us33.model.XSchoolType;
import sif3.common.conversion.MarshalFactory;
import sif3.common.conversion.ModelObjectInfo;
import sif3.common.conversion.UnmarshalFactory;
import sif3.common.exception.PersistenceException;
import sif3.common.exception.UnsupportedQueryException;
import sif3.common.model.PagingInfo;
import sif3.common.model.RequestMetadata;
import sif3.common.model.SIFContext;
import sif3.common.model.SIFZone;
import sif3.common.utils.UUIDGenerator;
import sif3.common.ws.CreateOperationStatus;
import sif3.common.ws.ErrorDetails;
import sif3.common.ws.OperationStatus;
import sif3.infra.rest.provider.BaseProvider;
import sif3demo.ModelObjectConstants;
import au.com.systemic.framework.utils.FileReaderWriter;
import au.com.systemic.framework.utils.StringUtils;

/**
 * @author Joerg Huber
 * 
 */
public class SchoolProvider extends BaseProvider
{
	protected final Logger logger = Logger.getLogger(getClass());

	private static DataModelUnmarshalFactory unmarshaller = new DataModelUnmarshalFactory();
	private static DataModelMarshalFactory marshaller = new DataModelMarshalFactory();

	private static HashMap<String, XSchoolType> schools = null; // = new HashMap<String, XSchoolType>();
  
	private ObjectFactory dmObjectFactory = new ObjectFactory();

	/*
	 * Will read a number of SchoolInfo Objects from a XML file. The location of the XML File is
	 * stored in the provider.properties file in the "provider.school.file.location" property.
	 */
	public SchoolProvider()
	{
	    super();
		logger.debug("Constructor for SchoolProvider has been called.");
		if (schools == null)
		{
			logger.debug("Constructor for SchoolProvider called for the first time. Try to load students from XML file...");
		    // Load all school so that we can do some real stuff here.
		    String schoolFile = getServiceProperties().getPropertyAsString("provider.school.file.location", null);
		    if (schoolFile != null)
		    {
		      try
		      {
		        String inputXML = FileReaderWriter.getFileContent(schoolFile);
		        XSchoolCollectionType schoolList = (XSchoolCollectionType)getUnmarshaller().unmarshalFromXML(inputXML, getMultiObjectClassInfo().getObjectType());
		        if ((schoolList != null) && (schoolList.getXSchool() != null))
		        {
					schools = new HashMap<String, XSchoolType>();
					for (XSchoolType schoolInfo : schoolList.getXSchool())
					{
						schools.put(schoolInfo.getRefId(), schoolInfo);
					}
					logger.debug("Loaded " + schools.size() + " schools into memory.");
		        }
		      }
		      catch (Exception ex)
		      {
		        ex.printStackTrace();
		        logger.debug("Loaded " + schools.size() + " schools into memory.");
		      }
		    }
			// If schools are still null then something must have failed and would have been logged. For
			// the purpose of making things work ok we initialise the students hashmap now. It will avoid null pointer errors.
			if (schools == null)
			{
				schools = new HashMap<String, XSchoolType>();
			}
		}
  	}
  
	// ----------------------------------
	// Start Exercise 5
	// ----------------------------------
	@Override
	public MarshalFactory getMarshaller()
	{
		return marshaller;
	}

	@Override
	public UnmarshalFactory getUnmarshaller()
	{
		return unmarshaller;
	}

	@Override
	public ModelObjectInfo getSingleObjectClassInfo()
	{
		// return ModelObjectInfo("XSchool", XSchoolType.class);
		return ModelObjectConstants.SCHOOL;
	}

	@Override
	public ModelObjectInfo getMultiObjectClassInfo()
	{
		// return new ModelObjectInfo("XSchools", XSchoolCollectionType.class);
		return ModelObjectConstants.SCHOOLS;
	}

	@Override
	public Object createSingle(Object data, boolean useAdvisory, SIFZone zone, SIFContext context, RequestMetadata metadata) throws IllegalArgumentException, PersistenceException
	{
		// Must be of type SchoolInfoType
		if (data instanceof XSchoolType)
		{
			XSchoolType school = (XSchoolType)data;
			if (useAdvisory)
			{
				if (StringUtils.isEmpty(school.getRefId())) //should use advisory but is empty => we must allocate
				{
					// Create new GUID because there is no GUID.
					school.setRefId(UUIDGenerator.getUUID());
				}
			}
			else // Don't use advisory and create one
			{
				// Create new GUID because there is no GUID.
				school.setRefId(UUIDGenerator.getUUID());        
			}
        
			//In the real implementation we would call a BL method here to create the school in the DB.
			//Right now only insert it into our school hashmap without checking if it already exists. If it does it is simply overriden!
			schools.put(school.getRefId(), school);
      
			// Return the school as required by the SIF Spec.
			return school;
		}
		else
		{
			throw new IllegalArgumentException("Expected Object Type  = XSchoolType. Received Object Type = "+data.getClass().getSimpleName());
		}
	}

	@Override
	public Object retrieve(SIFZone zone, SIFContext context, PagingInfo pagingInfo, RequestMetadata metadata) throws PersistenceException, UnsupportedQueryException
	{
		ArrayList<XSchoolType> schoolList = new ArrayList<XSchoolType>();
		if (pagingInfo == null) //return all
		{
			schoolList.addAll(schools.values());
		}
		else
		{
			pagingInfo.setTotalObjects(schools.size());
			if ((pagingInfo.getPageSize() * (pagingInfo.getCurrentPageNo()+1)) > schools.size())
			{
				return null; // Requested page outside of limits.
			}
      
			// retrieve applicable school
			Collection<XSchoolType> allSchools = schools.values();
			int i = 0;
			int startPos = pagingInfo.getPageSize() * pagingInfo.getCurrentPageNo();
			int endPos = startPos + pagingInfo.getPageSize();
			for (Iterator<XSchoolType> iter = allSchools.iterator(); iter.hasNext();)
			{
				XSchoolType school = iter.next();
				if ((i>=startPos) && (i<endPos))
				{
					schoolList.add(school);
				}
				i++;
			}
		}
    
		XSchoolCollectionType schoolCollection = dmObjectFactory.createXSchoolCollectionType();
		schoolCollection.getXSchool().addAll(schoolList);
		return schoolCollection;
	}

	//----------------------------------
	// End of Exercise 5
	//----------------------------------


	//--------------------------------------------------------------------
	// Optional: Implement the following methods as part of Exercise 5...
	//--------------------------------------------------------------------
	/*
	 * Note: This method WILL NOT insert the data into the schools hashmap. This is intentional for this exercise only.
	 * Note: We pretend that each 3rd insert is a failure. This is for illustration purpose only!
	 */
	@Override
	public List<CreateOperationStatus> createMany(Object data, boolean useAdvisory, SIFZone zone, SIFContext context, RequestMetadata metadata) throws IllegalArgumentException, PersistenceException
	{
		// Must be of type XSchoolCollectionType
		if (data instanceof XSchoolCollectionType)
		{
			logger.debug("Create schools (Bulk Operation)");
			XSchoolCollectionType schools = (XSchoolCollectionType)data;
			ArrayList<CreateOperationStatus> opStatus = new ArrayList<CreateOperationStatus>();
			int i=0;
			for (XSchoolType school : schools.getXSchool())
			{
				if ((i % 3) == 0) // pretend that every third create fails...
				{
					// Set advisoryID the same as resourceID.
					opStatus.add(new CreateOperationStatus(school.getRefId(), school.getRefId(), 404, new ErrorDetails(400, "Data not good.")));
				}
				else
				{
					if (useAdvisory)
					{
						// Advisory refId was used. Set resourceId and advisoryId to the same
						opStatus.add(new CreateOperationStatus(school.getRefId(), school.getRefId(), 201));
					}
					else
					{
						// Create a new refId (resourceID) but we must also report back the original RefId as the advisory if it was available.
						opStatus.add(new CreateOperationStatus(UUIDGenerator.getUUID(), school.getRefId(), 201));
					}
				}
				i++;
			}

			return opStatus;
		}
		else
		{
			throw new IllegalArgumentException("Expected Object Type  = XSchoolCollectionType. Received Object Type = "+data.getClass().getSimpleName());
		}
	}

	/*
	 * Exercise 5: Possible solution
	 */
	@Override
	public List<OperationStatus> updateMany(Object data, SIFZone zone, SIFContext context, RequestMetadata metadata) throws IllegalArgumentException, PersistenceException
	{
		// Must be of type XSchoolCollectionType
		if (data instanceof XSchoolCollectionType)
		{
			logger.debug("Update schools (Bulk Operation)");
			XSchoolCollectionType xSchools = (XSchoolCollectionType) data;
			ArrayList<OperationStatus> opStatus = new ArrayList<OperationStatus>();
			for (XSchoolType school : xSchools.getXSchool())
			{
				if (schools.get(school.getRefId()) == null)
				{
					opStatus.add(new OperationStatus(school.getRefId(), 404, new ErrorDetails(404, "School with RefId = " + school.getRefId() + " does not exist.")));
				}
				else
				{
					schools.put(school.getRefId(), school);
					opStatus.add(new OperationStatus(school.getRefId(), 200));
				}
			}

			return opStatus;
		}
		else
		{
			throw new IllegalArgumentException("Expected Object Type  = XSchoolCollectionType. Received Object Type = " + data.getClass().getSimpleName());
		}
	}

	/*
	 * Exercise 5: Possible solution
	 */
	@Override
	public List<OperationStatus> deleteMany(List<String> resourceIDs, SIFZone zone, SIFContext context, RequestMetadata metadata) throws IllegalArgumentException, PersistenceException
	{
		// In the real implementation we would call a BL method here to modify the School
		ArrayList<OperationStatus> opStatus = new ArrayList<OperationStatus>();
		for (String resourceID : resourceIDs)
		{
			if (schools.remove(resourceID) == null) // did not exist in hashmap
			{
				opStatus.add(new OperationStatus(resourceID, 404, new ErrorDetails(404, "School with RefId = " + resourceID + " does not exist.")));
			
			}
			else
			{
				opStatus.add(new OperationStatus(resourceID, 200));
			}
		}
		return opStatus;
	}

	@Override
	public Object retrievByPrimaryKey(String resourceID, SIFZone zone, SIFContext context, RequestMetadata metadata) throws IllegalArgumentException, PersistenceException
	{
		if (StringUtils.isEmpty(resourceID))
		{
			throw new IllegalArgumentException("Resource ID is null or empty. It must be provided to retrieve an entity.");
		}
    
		logger.debug("Retrieve school with Resoucre ID = "+resourceID);
    
		return schools.get(resourceID);
	}

	@Override
	public boolean updateSingle(Object data, String resourceID, SIFZone zone, SIFContext context, RequestMetadata metadata) throws IllegalArgumentException, PersistenceException
	{
		if (StringUtils.isEmpty(resourceID))
		{
			throw new IllegalArgumentException("Resource ID is null or empty. It must be provided to update an entity.");
		}
    
		//Must be of type XSchoolType
		if (data instanceof XSchoolType)
		{
			logger.debug("Update school with Resoucre ID = "+resourceID);

			//In the real implementation we would call a BL method here to modify the Student.
			return true;
		}
		else
		{
			throw new IllegalArgumentException("Expected Object Type  = XSchoolType. Received Object Type = "+data.getClass().getSimpleName());
		}
	}

	@Override
	public boolean deleteSingle(String resourceID, SIFZone zone, SIFContext context, RequestMetadata metadata) throws IllegalArgumentException, PersistenceException
	{
		if (StringUtils.isEmpty(resourceID))
		{
			throw new IllegalArgumentException("Resource ID is null or empty. It must be provided to delete an entity.");
		}
    
		logger.debug("Remove XSchool with Resoucre ID = "+resourceID);

		//In the real implementation we would call a BL method here to remove the School. Right now we only remove it from the hasmap.

		if (schools.get(resourceID) == null)
		{
			return false; // No school with the given resourceID (UUID) exists.
		}
		else
		{
			schools.remove(resourceID);
			return true;
		}
	}
	//--------------------------------------------------------------------
	// End Optional part of Exercise 5...
	//--------------------------------------------------------------------

	@Override
	public void shutdown()
	{
		// Leave as null for the moment...
	}
}
