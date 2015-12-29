/*
 * StudentPersonalIterator.java
 * Created: 19/03/2014
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

package sif3demo.provider.iterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sif.dd.us33.model.XStudentCollectionType;
import sif.dd.us33.model.XStudentType;
import sif3.common.header.HeaderValues.EventAction;
import sif3.common.header.HeaderValues.UpdateType;
import sif3.common.interfaces.SIFEventIterator;
import sif3.common.model.SIFEvent;
import au.com.systemic.framework.utils.AdvancedProperties;

/**
 * @author Joerg Huber
 *
 */
public class StudentIterator implements SIFEventIterator<XStudentCollectionType>
{
	private int currentPos = 0;
	private int currentCycle = 1;
	private int numRecycle = 1;
	
	private List<XStudentType> studentEvents =  new ArrayList<XStudentType>();
	
	public StudentIterator(@SuppressWarnings("unused") String providerID, AdvancedProperties serviceProperties, HashMap<String, XStudentType> students)
	{
		currentPos = 0;
		if (students != null)
		{
			studentEvents = new ArrayList<XStudentType>(students.values());
//			for (XStudentType student : students.values())
//			{
//				student.setRefId(UUIDGenerator.getUUID());
//			}
		}
	    numRecycle = serviceProperties.getPropertyAsInt("provider.events.recycle", 1);

	}
	
	/* (non-Javadoc)
     * @see sif3.common.interfaces.SIFEventIterator#getNextEvents(int)
     */
    @Override
    public SIFEvent<XStudentCollectionType> getNextEvents(int maxListSize)
    {
		SIFEvent<XStudentCollectionType> events = null;
		if (hasNext())
		{
			events = new SIFEvent<XStudentCollectionType>(new XStudentCollectionType(), EventAction.UPDATE, UpdateType.FULL, 0);
			while ((events.getListSize() < maxListSize) && hasNext())
			{
				events.getSIFObjectList().getXStudent().add(studentEvents.get(currentPos));
				events.setListSize(events.getListSize()+1);
				currentPos++;
				if (currentPos == studentEvents.size())
				{
					if (currentCycle < numRecycle)
					{
						currentPos = 0;
						currentCycle++;
					}
				}
			}
		}

	    return events;
    }

	/* (non-Javadoc)
     * @see sif3.common.interfaces.SIFEventIterator#hasNext()
     */
    @Override
    public boolean hasNext()
    {
		return (currentPos < studentEvents.size());
    }

	/* (non-Javadoc)
     * @see sif3.common.interfaces.SIFEventIterator#releaseResources()
     */
    @Override
    public void releaseResources()
    {
    }
}
