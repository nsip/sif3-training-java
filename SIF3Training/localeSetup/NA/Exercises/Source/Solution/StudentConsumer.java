/*
 * StudentConsumer.java
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

package sif3demo.consumer;

import sif.dd.us32.conversion.DataModelMarshalFactory;
import sif.dd.us32.conversion.DataModelUnmarshalFactory;
import sif3.common.conversion.MarshalFactory;
import sif3.common.conversion.ModelObjectInfo;
import sif3.common.conversion.UnmarshalFactory;
import sif3.common.model.PagingInfo;
import sif3.common.model.QueryCriteria;
import sif3.common.model.delayed.DelayedResponseReceipt;
import sif3.common.ws.CreateOperationStatus;
import sif3.common.ws.ErrorDetails;
import sif3.common.ws.OperationStatus;
import sif3.common.ws.model.MultiOperationStatusList;
import sif3.infra.rest.consumer.AbstractConsumer;
import sif3demo.ModelObjectConstants;

/**
 * @author Joerg Huber
 *
 */
public class StudentConsumer extends AbstractConsumer
{	
    private static DataModelUnmarshalFactory unmarshaller = new DataModelUnmarshalFactory();
    private static DataModelMarshalFactory marshaller = new DataModelMarshalFactory();

    public StudentConsumer()
    {
	    super();
    }

	/* (non-Javadoc)
     * @see sif3.common.interfaces.DataModelLink#getMarshaller()
     */
    @Override
    public MarshalFactory getMarshaller()
    {
	    return marshaller;
    }

    /* (non-Javadoc)
     * @see sif3.common.interfaces.DataModelLink#getUnmarshaller()
     */
    @Override
    public UnmarshalFactory getUnmarshaller()
    {
      return unmarshaller;
    }

    /* (non-Javadoc)
     * @see sif3.common.interfaces.DataModelLink#getMultiObjectClassInfo()
     */
    @Override
    public ModelObjectInfo getMultiObjectClassInfo()
    {
	    return ModelObjectConstants.STUDENTS;
    }

	/* (non-Javadoc)
     * @see sif3.common.interfaces.DataModelLink#getSingleObjectClassInfo()
     */
    @Override
    public ModelObjectInfo getSingleObjectClassInfo()
    {
	    return ModelObjectConstants.STUDENT;
    }


	/* (non-Javadoc)
     * @see sif3.infra.rest.consumer.AbstractConsumer#shutdown()
     */
    @Override
    public void shutdown()
    {
	    // TODO Auto-generated method stub
    }
    

	/*-----------------------------------------------------------------------------*/
	/*-- Methods required for DELAYED response processing. We do not use them as --*/
	/*-- of this training, so we simply null them out.                           --*/ 
	/*-----------------------------------------------------------------------------*/

	@Override
    public void processDelayedCreateMany(MultiOperationStatusList<CreateOperationStatus> arg0, DelayedResponseReceipt arg1)
    {}

	@Override
    public void processDelayedDeleteMany(MultiOperationStatusList<OperationStatus> arg0, DelayedResponseReceipt arg1)
    {}

	@Override
    public void processDelayedError(ErrorDetails arg0, DelayedResponseReceipt arg1)
    {}

	@Override
    public void processDelayedQuery(Object arg0, PagingInfo arg1, DelayedResponseReceipt arg2)
    {}

	@Override
    public void processDelayedServicePath(Object arg0, QueryCriteria arg1, PagingInfo arg2, DelayedResponseReceipt arg3)
    {}

	@Override
    public void processDelayedUpdateMany(MultiOperationStatusList<OperationStatus> arg0, DelayedResponseReceipt arg1)
    {}
	
}
