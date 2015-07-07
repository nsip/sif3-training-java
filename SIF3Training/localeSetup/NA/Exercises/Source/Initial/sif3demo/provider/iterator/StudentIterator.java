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

import sif.dd.us32.model.K12StudentCollectionType;
import sif.dd.us32.model.K12StudentType;
import sif3.common.header.HeaderValues.EventAction;
import sif3.common.header.HeaderValues.UpdateType;
import sif3.common.interfaces.SIFEventIterator;
import sif3.common.model.SIFEvent;
import au.com.systemic.framework.utils.AdvancedProperties;

/**
 * @author Joerg Huber
 *
 */
public class StudentIterator implements SIFEventIterator<K12StudentCollectionType>
{
	private int currentPos = 0;
	private int currentCycle = 1;
	private int numRecycle = 1;
	
	private List<K12StudentType> studentEvents =  new ArrayList<K12StudentType>();
	
	public StudentIterator(String providerID, AdvancedProperties serviceProperties, HashMap<String, K12StudentType> students)
	{
		currentPos = 0;
		if (students != null)
		{
			studentEvents = new ArrayList<K12StudentType>(students.values());
//			for (K12StudentType student : K12StudentType)
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
    public SIFEvent<K12StudentCollectionType> getNextEvents(int maxListSize)
    {
		SIFEvent<K12StudentCollectionType> events = null;
		if (hasNext())
		{
			events = new SIFEvent<K12StudentCollectionType>(new K12StudentCollectionType(), EventAction.UPDATE, UpdateType.FULL, 0);
			while ((events.getListSize() < maxListSize) && hasNext())
			{
				events.getSIFObjectList().getK12Student().add(studentEvents.get(currentPos));
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
