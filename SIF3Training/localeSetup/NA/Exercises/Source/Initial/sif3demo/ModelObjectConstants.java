/*
 * ModelObjectConstants.java
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

package sif3demo;

import sif.dd.us32.model.K12SchoolCollectionType;
import sif.dd.us32.model.K12SchoolType;
import sif.dd.us32.model.K12StudentCollectionType;
import sif.dd.us32.model.K12StudentType;
import sif3.common.conversion.ModelObjectInfo;

/**
 * @author Joerg Huber
 *
 */
public class ModelObjectConstants
{
	public static final ModelObjectInfo STUDENTS         = new ModelObjectInfo("k12Students", K12StudentCollectionType.class);
	public static final ModelObjectInfo STUDENT          = new ModelObjectInfo("k12Student", K12StudentType.class);
	public static final ModelObjectInfo SCHOOLS          = new ModelObjectInfo("k12Schools", K12SchoolCollectionType.class);
	public static final ModelObjectInfo SCHOOL           = new ModelObjectInfo("k12School", K12SchoolType.class);
}
