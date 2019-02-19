# License
> Copyright 2013 - 2016 Systemic Pty Ltd
> 
> Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
> 
> [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0 "Apache License, Version 2.0")
> 
> Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

# Summary
The SIF3 Training is a Java project designed for SIF3 Training courses. This includes training examples
and exercises for the Australia (AU) and Northern America (NA). The default configuration is for AU. To
configure the training material for NA the following steps need to be done:

1. Set the property 'locale' in the ant.properties file to NA (i.e. locale=NA).
2. Run the ant task called '99-prepare-training'.
3. In most IDEs you need to 'refresh' the project for the changes of the ant target of step 2 to take 
   effect.

The training exercise descriptions can be found in the directory: localeSetup/<locale>/Exercises.
There are more than one set of exercises depending on the training course duration.

# Version History and Update
## Version from July 07, 2015: Merged US and AU training Project
This version is upgraded to use the SIF3 Framework v0.7.0. Further it merges the US Training Github 
project into this project to keep the two in-line with changes. Please refer to the summary section of 
this README.txt for details on how to configure the training project for the Northern America (NA) locale.

## Version from December 29, 2015: Changed Exercises for NA
This version changes the exercises for NA to use the SIF xPress 3.3 Data Model. It also added
service path exercises.

## Version from March 21, 2017: Updated Infrastructure to 3.2.1
Updated all libraries and sample files to infrastructure 3.2.1. Also updated AU exercises to SIF AU 3.4.1.

## Version from March 27, 2018: Updated to SIF3 Framework v0.13.0
Updated all libraries and sample files to infrastructure 3.2.1. Also updated AU exercises to SIF AU 3.4.3.

## Version from October 2, 2018: Updated to SIF3 Framework v0.14.0
- Project is now based on **Java 8**. It also includes the latest SIF3 Framework Libraries that support Functional services.

## Version from October 23, 2018: Added FQ Use-Case Exercises.
- Exercises for FQ Use-Case added and updated to SIF AU 3.4.4 draft data model.

## Version from Jan 22, 2019: Updates SIF3 Framework Library v0.14.0
- New libraries support XQUERYTEMPLATE functionality also known as Named Query Templates.

## Version from Jan 31, 2019: Updates SIF3 Framework Library v0.14.1
- New libraries support for Dynamic Queries

## Version from Feb 19, 2019: Updates Exercises for AU FQ Use-Case
- Exercises for FQ Use-Case updated to match officially released SIF AU 3.4.4 data model.

# Download Instructions

How to download this project:

## Option 1 - As a Zip.
Click on the button marked "ZIP" available from the Code tab.


## Option 2 - Using a Git client.
From the command-line type: git clone git://github.com/nsip/sif3-training-java.git

Note that if you want to use this option but don't have the client installed, it can be 
downloaded from http://git-scm.com/download.