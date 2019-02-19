/*
 * SchoolInfoProvider.java
 * Created: 18/08/2014
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

import au.com.systemic.framework.utils.StringUtils;
import sif.dd.au30.conversion.DataModelMarshalFactory;
import sif.dd.au30.conversion.DataModelUnmarshalFactory;
import sif.dd.au30.model.FinancialQuestionnaireSubmissionCollectionType;
import sif.dd.au30.model.FinancialQuestionnaireSubmissionType;
import sif.dd.au30.model.ObjectFactory;
import sif3.common.conversion.MarshalFactory;
import sif3.common.conversion.ModelObjectInfo;
import sif3.common.conversion.UnmarshalFactory;
import sif3.common.exception.DataTooLargeException;
import sif3.common.exception.PersistenceException;
import sif3.common.exception.UnsupportedQueryException;
import sif3.common.header.HeaderProperties;
import sif3.common.model.PagingInfo;
import sif3.common.model.RequestMetadata;
import sif3.common.model.ResponseParameters;
import sif3.common.model.SIFContext;
import sif3.common.model.SIFZone;
import sif3.common.utils.UUIDGenerator;
import sif3.common.ws.CreateOperationStatus;
import sif3.common.ws.OperationStatus;
import sif3.infra.rest.provider.BaseProvider;
import sif3demo.ModelObjectConstants;

/**
 * @author Joerg Huber
 * 
 */
public class FQReportingProvider extends BaseProvider
{
    private static DataModelUnmarshalFactory unmarshaller = new DataModelUnmarshalFactory();
    private static DataModelMarshalFactory marshaller = new DataModelMarshalFactory();

    private static HashMap<String, FinancialQuestionnaireSubmissionType> fqs = new HashMap<String, FinancialQuestionnaireSubmissionType>();

    private ObjectFactory dmObjectFactory = new ObjectFactory();

    /*
     * 
     */
    public FQReportingProvider()
    {
        super();
        logger.debug("Constructor for FQReportingProvider has been called.");
    }
  
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
        return ModelObjectConstants.FQSUMISSION_OBJECT;
    }

    @Override
    public ModelObjectInfo getMultiObjectClassInfo()
    {
        return ModelObjectConstants.FQSUMISSION_OBJECTS;
    }

    @Override
    public Object createSingle(Object data, boolean useAdvisory, SIFZone zone, SIFContext context, RequestMetadata metadata, ResponseParameters customResponseParams) throws IllegalArgumentException, PersistenceException
    {
        // Must be of type FQReportingType
        if (data instanceof FinancialQuestionnaireSubmissionType)
        {
            FinancialQuestionnaireSubmissionType fq = (FinancialQuestionnaireSubmissionType)data;
            if (useAdvisory)
            {
                if (StringUtils.isEmpty(fq.getRefId())) //should use advisory but is empty => we must allocate
                {
                    // Create new GUID because there is no GUID.
                    fq.setRefId(UUIDGenerator.getUUID());
                }
            }
            else // Don't use advisory and create one
            {
                // Create new GUID because there is no GUID.
                fq.setRefId(UUIDGenerator.getUUID());        
            }
        
            //In the real implementation we would call a BL method here to create the FQ in the DB.
            //Right now only insert it into our FQ hashmap without checking if it already exists. If it does it is simply overriden!
            fqs.put(fq.getRefId(), fq);
      
            // Return the school as required by the SIF Spec.
            return fq;
        }
        else
        {
            throw new IllegalArgumentException("Expected Object Type  = FinancialQuestionnaireSubmissionType. Received Object Type = "+data.getClass().getSimpleName());
        }
    }

    @Override
    public Object retrievByPrimaryKey(String resourceID, SIFZone zone, SIFContext context, RequestMetadata metadata, ResponseParameters customResponseParams) throws IllegalArgumentException, PersistenceException
    {
        if (StringUtils.isEmpty(resourceID))
        {
            throw new IllegalArgumentException("Resource ID is null or empty. It must be provided to retrieve an entity.");
        }
    
        logger.debug("Retrieve student with Resoucre ID = "+resourceID);
    
        return fqs.get(resourceID);
    }

    @Override
    public Object retrieve(SIFZone zone, SIFContext context, PagingInfo pagingInfo, RequestMetadata metadata, ResponseParameters customResponseParams) throws PersistenceException, UnsupportedQueryException
    {
        ArrayList<FinancialQuestionnaireSubmissionType> fqList = new ArrayList<FinancialQuestionnaireSubmissionType>();
        if (pagingInfo == null) //return all
        {
            fqList.addAll(fqs.values());
        }
        else
        {
            pagingInfo.setTotalObjects(fqs.size());
            int startPos = pagingInfo.getPageSize() * (pagingInfo.getCurrentPageNo() - 1);
            int endPos = startPos + pagingInfo.getPageSize() - 1;
            logger.debug("Start Position = "+startPos+"    End Position = "+endPos);
            
            if (startPos >= fqs.size())
            {
                return null; // Requested page outside of limits.
            }
      
            // retrieve applicable FQ Objects
            Collection<FinancialQuestionnaireSubmissionType> allFQs = fqs.values();
            int i = 0;
            for (Iterator<FinancialQuestionnaireSubmissionType> iter = allFQs.iterator(); iter.hasNext();)
            {
                FinancialQuestionnaireSubmissionType fq = iter.next();
                if ((i>=startPos) && (i<=endPos))
                {
                    fqList.add(fq);
                }
                i++;
            }
        }
    
        FinancialQuestionnaireSubmissionCollectionType fqCollection = dmObjectFactory.createFinancialQuestionnaireSubmissionCollectionType();
        fqCollection.getFinancialQuestionnaireSubmission().addAll(fqList);
        return fqCollection;
    }

    @Override
    public List<CreateOperationStatus> createMany(Object data, boolean useAdvisory, SIFZone zone, SIFContext context, RequestMetadata metadata, ResponseParameters customResponseParams) throws IllegalArgumentException, PersistenceException
    {
        // Must be of type FQReportingObjectCollectionType
        if (data instanceof FinancialQuestionnaireSubmissionCollectionType)
        {
            logger.debug("Create schools (Bulk Operation)");
            FinancialQuestionnaireSubmissionCollectionType fqCollection = (FinancialQuestionnaireSubmissionCollectionType)data;
            ArrayList<CreateOperationStatus> opStatus = new ArrayList<CreateOperationStatus>();
            for (FinancialQuestionnaireSubmissionType fq : fqCollection.getFinancialQuestionnaireSubmission())
            {
                if (useAdvisory)
                {
                    // Advisory refId was used. Set resourceId and advisoryId to the same
                    opStatus.add(new CreateOperationStatus(fq.getRefId(), fq.getRefId(), 201));
                }
                else
                {
                    // Create a new refId (resourceID) but we must also report back the original RefId as the advisory if it was available.
                    String newRefID = UUIDGenerator.getUUID();
                    opStatus.add(new CreateOperationStatus(newRefID, fq.getRefId(), 201));
                    fq.setRefId(newRefID);
                }
                fqs.put(fq.getRefId(), fq);
            }

            return opStatus;
        }
        else
        {
            throw new IllegalArgumentException("Expected Object Type  = FQReportingObjectCollectionType. Received Object Type = "+data.getClass().getSimpleName());
        }
    }

    @Override
    public void shutdown()
    {
        // Leave as null for the moment...
    }
  
    /* -----------------------------------------------------------------------------------
     * Note: The following methods are unlikely to be used as part of the AG/FQ use-case
     -------------------------------------------------------------------------------------*/

    @Override
    public List<OperationStatus> updateMany(Object data, SIFZone zone, SIFContext context, RequestMetadata metadata, ResponseParameters customResponseParams) throws IllegalArgumentException, PersistenceException
    {
        return null;
    }

    @Override
    public List<OperationStatus> deleteMany(List<String> resourceIDs, SIFZone zone, SIFContext context, RequestMetadata metadata, ResponseParameters customResponseParams) throws IllegalArgumentException, PersistenceException
    {
        return null;
    }

    @Override
    public boolean updateSingle(Object data, String resourceID, SIFZone zone, SIFContext context, RequestMetadata metadata, ResponseParameters customResponseParams) throws IllegalArgumentException, PersistenceException
    {
        return false;
    }

    @Override
    public boolean deleteSingle(String resourceID, SIFZone zone, SIFContext context, RequestMetadata metadata, ResponseParameters customResponseParams) throws IllegalArgumentException, PersistenceException
    {
        return false;
    }

    /* (non-Javadoc)
     * @see sif3.infra.rest.provider.BaseProvider#getCustomServiceInfo(sif3.common.model.SIFZone, sif3.common.model.SIFContext, sif3.common.model.PagingInfo, sif3.common.model.RequestMetadata)
     */
    @Override
    public HeaderProperties getCustomServiceInfo(SIFZone zone, SIFContext context, PagingInfo pagingInfo, RequestMetadata metadata) throws PersistenceException, UnsupportedQueryException, DataTooLargeException
    {
        return null;
    }
}
