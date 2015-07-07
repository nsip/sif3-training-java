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

import java.util.HashMap;
import java.util.List;

import sif.dd.us32.model.ObjectFactory;
import sif.dd.us32.model.K12SchoolCollectionType;
import sif.dd.us32.model.K12SchoolType;
import sif3.common.conversion.MarshalFactory;
import sif3.common.conversion.ModelObjectInfo;
import sif3.common.conversion.UnmarshalFactory;
import sif3.common.exception.PersistenceException;
import sif3.common.exception.UnsupportedQueryException;
import sif3.common.model.PagingInfo;
import sif3.common.model.RequestMetadata;
import sif3.common.model.SIFContext;
import sif3.common.model.SIFZone;
import sif3.common.ws.CreateOperationStatus;
import sif3.common.ws.OperationStatus;
import sif3.infra.rest.provider.BaseProvider;
import au.com.systemic.framework.utils.FileReaderWriter;

/**
 * @author Joerg Huber
 * 
 */
public class SchoolProvider extends BaseProvider
{
  private static HashMap<String, K12SchoolType> schools = null; // = new HashMap<String, K12SchoolType>();
  
  @SuppressWarnings("unused")
  private ObjectFactory dmObjectFactory = new ObjectFactory();

  /*
   * Will read a number of SchoolInfo Objects from a XML file. The location of the XML File is stored in the provider.properties file
   * in the "provider.school.file.location" property.
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
	        K12SchoolCollectionType schoolList = (K12SchoolCollectionType)getUnmarshaller().unmarshalFromXML(inputXML, getMultiObjectClassInfo().getObjectType());
	        if ((schoolList != null) && (schoolList.getK12School() != null))
	        {
				schools = new HashMap<String, K12SchoolType>();
				for (K12SchoolType schoolInfo : schoolList.getK12School())
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
			schools = new HashMap<String, K12SchoolType>();
		}
	}
  }
  
  //----------------------------------
  // Start Exercise 5
  //----------------------------------
  @Override
  public MarshalFactory getMarshaller()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public UnmarshalFactory getUnmarshaller()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ModelObjectInfo getSingleObjectClassInfo()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ModelObjectInfo getMultiObjectClassInfo()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object createSingle(Object data, boolean useAdvisory, SIFZone zone, SIFContext context, RequestMetadata metadata) throws IllegalArgumentException, PersistenceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object retrieve(SIFZone zone, SIFContext context, PagingInfo pagingInfo, RequestMetadata metadata) throws PersistenceException, UnsupportedQueryException
  {
    // TODO Auto-generated method stub
    return null;
  }
  //----------------------------------
  // End of Exercise 5
  //----------------------------------


  //--------------------------------------------------------------------
  // Optional: Implement the following methods as part of Exercise 5...
  //--------------------------------------------------------------------
  @Override
  public List<CreateOperationStatus> createMany(Object data, boolean useAdvisory, SIFZone zone, SIFContext context, RequestMetadata metadata) throws IllegalArgumentException, PersistenceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<OperationStatus> updateMany(Object data, SIFZone zone, SIFContext context, RequestMetadata metadata) throws IllegalArgumentException, PersistenceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<OperationStatus> deleteMany(List<String> resourceIDs, SIFZone zone, SIFContext context, RequestMetadata metadata) throws IllegalArgumentException, PersistenceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object retrievByPrimaryKey(String resourceID, SIFZone zone, SIFContext context, RequestMetadata metadata) throws IllegalArgumentException, PersistenceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean updateSingle(Object data, String resourceID, SIFZone zone, SIFContext context, RequestMetadata metadata) throws IllegalArgumentException, PersistenceException
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean deleteSingle(String resourceID, SIFZone zone, SIFContext context, RequestMetadata metadata) throws IllegalArgumentException, PersistenceException
  {
    // TODO Auto-generated method stub
    return false;
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
