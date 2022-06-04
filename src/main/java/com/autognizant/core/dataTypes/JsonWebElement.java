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

package com.autognizant.core.dataTypes;

import java.util.Map;

/**
 * This is a POJO class used to configure web element details from object repository JSON files.
 */
public class JsonWebElement {
	
	private String webElementName;
	private String webElementType;
	private String frameName;
	private Map<String, String> English;
    
	/**
	 * Gets web element name value.
	 * @return web element name.
	 */
	public String getWebElementName() {
		return webElementName;
	}
	
	/**
	 * Sets web element name value.
	 * @param webElementName web element name.
	 */	
	public void setWebElementName(String webElementName) {
		this.webElementName = webElementName;
	}
	
	/**
	 * Gets web element type value.
	 * @return web element type.
	 */
	public String getWebElementType() {
		return webElementType;
	}
	
	/**
	 * Sets web element type value.
	 * @param webElementType web element type.
	 */
	public void setWebElementType(String webElementType) {
		this.webElementType = webElementType;
	}
	
	/**
	 * Gets frame name value.
	 * @return frame name.
	 */
	public String getFrameName() {
		return frameName;
	}
	
	/**
	 * Sets frame name value.
	 * @param frameName frame name.
	 */
	public void setFrameName(String frameName) {
		this.frameName = frameName;
	}
	
	/**
	 * Gets English language key-pair value.
	 * @return the key-pair value for locator type and locator value.
	 */
	public Map<String, String> getEnglish() {
		return English;
	}
	
	/**
	 * Sets English language value.
	 * @param english key-pair value for locator type and locator value.
	 */
	public void setEnglish(Map<String, String> english) {
		English = english;
	}        
}

