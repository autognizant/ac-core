/*
 * Copyright 2022 Autognizant.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.autognizant.core.config;

import com.autognizant.core.util.Constants;

import blog.softwaretester.properties.PropertyAggregator;

/**
 * AutognizantConfig class is used to configure framework level parameters.
 */
/**
 * @author Rameshwar-Shiral
 *
 */
/**
 * @author Rameshwar-Shiral
 *
 */
public class AutognizantConfig {

	private static PropertyAggregator propertyAggregator;
	private static String scenarioIDPrefix;
	private static String appWindowTitle;
	private static int elementWait;

	private static String scenarioUniqueID;
	private static String scenarioName;
	private static String logFile;

	/**
	 * AutognizantConfig constructor.
	 */
    public AutognizantConfig() {
    	propertyAggregator =
    		    new PropertyAggregator.Builder()
    		    .withEnvironmentProperties()
    		    .withPropertiesFile(Constants.RESOURCES_PATH+"/config/autognizant.config")
    		    .build();
    }
    
	/**
	 * Sets Autognizant framework configuration from autognizant.config file.
	 */ 
    public void setAutognizantConfiguration() {
    	setScenarioIDPrefix(getProperty("SCENARIO_ID_PREFIX"));
    	setAppWindowTitle(getProperty("APP_WINDOW_TITLE"));
    	setElementWait(Integer.parseInt(getProperty("ELEMENT_WAIT")));
    }
    
	/**
	 * Gets Scenario ID Prefix value.
	 * @return Scenario ID Prefix
	 */ 
    public static String getScenarioIDPrefix() {
		return scenarioIDPrefix;
	}

	/**
	 * Gets application window title.
	 * @return application window title.
	 */ 
    public static String getAppWindowTitle() {
		return appWindowTitle;
	}

	/**
	 * Gets global explicit web element wait value.
	 * @return global explicit web element wait
	 */ 
    public static int getElementWait() {
		return elementWait;
	}

	/**
	 * Sets Scenario ID Prefix value.
	 * @param scenarioIDPrefix Scenario ID Prefix
	 */ 
	private void setScenarioIDPrefix(String scenarioIDPrefix) {
		AutognizantConfig.scenarioIDPrefix = scenarioIDPrefix;
	}

	/**
	 * Sets application window title.
	 * @param appWindowTitle application window title.
	 */ 
	private void setAppWindowTitle(String appWindowTitle) {
		AutognizantConfig.appWindowTitle = appWindowTitle;
	}

	/**
	 * Sets global explicit web element wait value.
	 * @param elementWait explicit web element wait in seconds.
	 */ 
	private void setElementWait(int elementWait) {
		AutognizantConfig.elementWait = elementWait;
	}

	/**
	 * Gets property value from autognizant.config file.
	 * @param propertyName Name of the property.
	 * @return Value of the property.
	 */ 
	public static String getProperty(String propertyName) {
		String propertyValue = propertyAggregator.getProperty(propertyName);
		if(propertyValue != null) return propertyValue;
		else throw new RuntimeException(propertyName+" is not specified in the autognizant.config file.");
	}

	/**
	 * Gets Scenario Unique ID.
	 * @return Scenario Unique ID.
	 */ 
	public static String getScenarioUniqueID() {
		return scenarioUniqueID;
	}
	
	/**
	 * Gets Scenario Name value.
	 * @return Scenario Name.
	 */ 
	public static String getScenarioName() {
		return scenarioName;
	}

	/**
	 * Gets the automation execution log file name value.
	 * @return the automation execution log file name.
	 */ 
	public static String getLogFile() {
		return logFile;
	}

	/**
	 * Sets Scenario Unique ID.
	 * @param scenarioUniqueID Scenario Unique ID.
	 */ 
	public static void setScenarioUniqueID(String scenarioUniqueID) {
		AutognizantConfig.scenarioUniqueID = scenarioUniqueID;
	}

	/**
	 * Sets Scenario Name file.
	 * @param scenarioName Scenario Name.
	 */ 
	public static void setScenarioName(String scenarioName) {
		AutognizantConfig.scenarioName = scenarioName;
	}

	/**
	 * Sets log file name value.
	 * @param logFile log file name.
	 */ 
	public static void setLogFile(String logFile) {
		AutognizantConfig.logFile = logFile;
	}	
	
}
