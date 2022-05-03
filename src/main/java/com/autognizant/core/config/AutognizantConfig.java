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
public class AutognizantConfig {

	private static PropertyAggregator propertyAggregator;
	private static String scenarioIDPrefix;
	private static String appWindowTitle;
	private static int elementWait;

	/**
	 * AutognizantConfig constructor
	 */
    public AutognizantConfig() {
    	propertyAggregator =
    		    new PropertyAggregator.Builder()
    		    .withEnvironmentProperties()
    		    .withPropertiesFile(Constants.RESOURCES_PATH+"/config/autognizant.config")
    		    .build();
    }
    
    public void setAutognizantConfiguration() {
    	setScenarioIDPrefix(getProperty("SCENARIO_ID_PREFIX"));
    	setAppWindowTitle(getProperty("APP_WINDOW_TITLE"));
    	setElementWait(Integer.parseInt(getProperty("ELEMENT_WAIT")));
    }
    
    public static String getScenarioIDPrefix() {
		return scenarioIDPrefix;
	}

    public static String getAppWindowTitle() {
		return appWindowTitle;
	}

    public static int getElementWait() {
		return elementWait;
	}

	private void setScenarioIDPrefix(String scenarioIDPrefix) {
		AutognizantConfig.scenarioIDPrefix = scenarioIDPrefix;
	}

	private void setAppWindowTitle(String appWindowTitle) {
		AutognizantConfig.appWindowTitle = appWindowTitle;
	}

	private void setElementWait(int elementWait) {
		AutognizantConfig.elementWait = elementWait;
	}

	public static String getProperty(String propertyName) {
		String propertyValue = propertyAggregator.getProperty(propertyName);
		if(propertyValue != null) return propertyValue;
		else throw new RuntimeException(propertyName+" is not specified in the autognizant.config file.");
	}	
}
