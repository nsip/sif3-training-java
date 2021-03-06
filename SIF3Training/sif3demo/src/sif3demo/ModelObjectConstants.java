/*
 * ModelObjectConstants.java
 * Created: 01/10/2013
 *
 * Copyright 2013 Systemic Pty Ltd
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

//import sif.dd.au30.model.FinancialQuestionnaireSubmissionCollectionType;
//import sif.dd.au30.model.FinancialQuestionnaireSubmissionType;
import sif.dd.au30.model.SchoolInfoCollectionType;
import sif.dd.au30.model.SchoolInfoType;
import sif.dd.au30.model.StudentDailyAttendanceCollectionType;
import sif.dd.au30.model.StudentDailyAttendanceType;
import sif.dd.au30.model.StudentPersonalCollectionType;
import sif.dd.au30.model.StudentPersonalType;
import sif.dd.au30.model.TeachingGroupCollectionType;
import sif.dd.au30.model.TeachingGroupType;
import sif3.common.conversion.ModelObjectInfo;

/**
 * @author Joerg Huber
 *
 */
public class ModelObjectConstants
{
    public static final String UTF_8 = "UTF-8";

    public static final ModelObjectInfo STUDENT_PERSONALS         = new ModelObjectInfo("StudentPersonals", StudentPersonalCollectionType.class);
	public static final ModelObjectInfo STUDENT_PERSONAL          = new ModelObjectInfo("StudentPersonal", StudentPersonalType.class);
	public static final ModelObjectInfo SCHOOL_INFOS              = new ModelObjectInfo("SchoolInfos", SchoolInfoCollectionType.class);
	public static final ModelObjectInfo SCHOOL_INFO               = new ModelObjectInfo("SchoolInfo", SchoolInfoType.class);
	public static final ModelObjectInfo STUDENT_DAILY_ATTENDANCES = new ModelObjectInfo("StudentDailyAttendances", StudentDailyAttendanceCollectionType.class);
	public static final ModelObjectInfo STUDENT_DAILY_ATTENDANCE  = new ModelObjectInfo("StudentDailyAttendance", StudentDailyAttendanceType.class);
	public static final ModelObjectInfo TEACHING_GROUPS           = new ModelObjectInfo("TeachingGroups", TeachingGroupCollectionType.class);
	public static final ModelObjectInfo TEACHING_GROUP            = new ModelObjectInfo("TeachingGroups", TeachingGroupType.class);

//    Removed in 3.4.6
//    public static final ModelObjectInfo FQSUMISSION_OBJECTS       = new ModelObjectInfo("FinancialQuestionnaireSubmissions", FinancialQuestionnaireSubmissionCollectionType.class);
//    public static final ModelObjectInfo FQSUMISSION_OBJECT        = new ModelObjectInfo("FinancialQuestionnaireSubmission", FinancialQuestionnaireSubmissionType.class);

}
