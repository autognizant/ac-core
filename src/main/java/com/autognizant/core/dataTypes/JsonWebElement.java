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

public class JsonWebElement {
	
	private String logicalName;
	private String elementType;
	private String frameName;
	private Map<String, String> English;
	private Map<String, String> Thai;
    
	public String getLogicalName() {
		return logicalName;
	}
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public String getFrameName() {
		return frameName;
	}
	public void setFrameName(String frameName) {
		this.frameName = frameName;
	}
	public Map<String, String> getEnglish() {
		return English;
	}
	public void setEnglish(Map<String, String> english) {
		English = english;
	}
	public Map<String, String> getThai() {
		return Thai;
	}
	public void setThai(Map<String, String> thai) {
		Thai = thai;
	}  
    
    
}

