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

package com.autognizant.core.selenium;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


import org.openqa.selenium.By;

import com.autognizant.core.dataTypes.JsonWebElement;
import com.autognizant.core.util.Constants;
import com.autognizant.core.util.JsonDataReader;
import com.autognizant.core.util.Log;

public class ObjectRepository {

	private List<JsonWebElement> jsonWebElementList;
	private JsonWebElement webElement=null;
	private JsonDataReader jsonDataReader;
	/**
	 * Default Constructor of the ObjectMap class
	 * Loads Object Repository.
	 */
	public ObjectRepository(){
		try{
			Log.info("Loading Object Repository.....");	
			jsonWebElementList = new ArrayList<JsonWebElement>();
			jsonDataReader = new JsonDataReader();
//		    if(!AutomationHooks.RESOURCES_PATH.contains("/src/test/resources")){
		    	if(!Constants.RESOURCES_PATH.contains("\\src\\test\\resources")){
		    	List <String> jsonFiles = ObjectRepository.getlistFiles();
		    	for (String string : jsonFiles) {
		    		List<JsonWebElement> temp = jsonDataReader.getObjectRepositoryData(this.getClass().getResourceAsStream("/"+string));
					jsonWebElementList.addAll(temp);
					Log.info("File Loaded : " + string);	
				}
		    }else{
			File directory = new File(System.getProperty("user.dir")+"\\src\\test\\resources\\objectRepository");
			
			String sPath = directory.getAbsolutePath();
			File[] jsonFiles = new File(sPath).listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".json");
				}
			});
			for(int i=0; i<jsonFiles.length;i++){
				List<JsonWebElement> temp = jsonDataReader.getObjectRepositoryData(jsonFiles[i]);
				jsonWebElementList.addAll(temp);
				Log.info("File Loaded : " + jsonFiles[i].getAbsolutePath());
			}
			Log.info("Object Repository loaded successfully...");
		    }
		}catch (Exception e){
			Log.error("while loading object repository", e);
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the web element properties from Object Repository.
	 * @param sElementName Logical Name for web element mentioned in Object Repository.
	 * @return Returns By property for web element.
	 * @throws Exception
	 */
	private By getLocator(String sLocatorType,String sLocatorValue){		
		if (sLocatorType.toLowerCase().equals("id"))
			return By.id(sLocatorValue);
		else if(sLocatorType.toLowerCase().equals("name"))
			return By.name(sLocatorValue);
		else if((sLocatorType.toLowerCase().equals("classname")) || (sLocatorType.toLowerCase().equals("class")))
			return By.className(sLocatorValue);
		else if((sLocatorType.toLowerCase().equals("tagname")) || (sLocatorType.toLowerCase().equals("tag")))
			return By.tagName(sLocatorValue);
		else if((sLocatorType.toLowerCase().equals("linktext")) || (sLocatorType.toLowerCase().equals("link")))
			return By.linkText(sLocatorValue);
		else if(sLocatorType.toLowerCase().equals("partiallinktext") || (sLocatorType.toLowerCase().equals("partiallink")))
			return By.partialLinkText(sLocatorValue);
		else if((sLocatorType.toLowerCase().equals("cssselector")) || (sLocatorType.toLowerCase().equals("css")))
			return By.cssSelector(sLocatorValue);
		else if(sLocatorType.toLowerCase().equals("xpath"))
			return By.xpath(sLocatorValue);
		else{
			try {
				throw new Exception("sLocator type '" + sLocatorType + "' not defined!!");
			} catch (Exception e) {
				Log.error("while getting Web Object details", e);
				return null;
			}
		}
	}
	
	public String getFrameName(String sElementName) {
		webElement = getElementByName(sElementName);
		return webElement.getFrameName();
	}

	private JsonWebElement getElementByName(String logicalName){
		return jsonWebElementList.stream().filter(x -> x.getLogicalName().equalsIgnoreCase(logicalName)).findAny().get();
	}
	
	public By getWebElement(String sElementName) {
		webElement = getElementByName(sElementName);
		String sLocatorType =  webElement.getEnglish().get("locatorType");
		String sLocatorValue = webElement.getEnglish().get("locatorValue");
		return getLocator(sLocatorType,sLocatorValue);
	}	
	
	public String getWebElementJavaScript(String sElementName) {
		webElement = getElementByName(sElementName);
		String sLocatorType =  webElement.getEnglish().get("locatorType");
		String sLocatorValue = webElement.getEnglish().get("locatorValue");
		if(sLocatorType.toLowerCase().equals("javascript"))
			return sLocatorValue;
		else{
			try {
				throw new Exception("sLocator type '" + sLocatorType + "' not defined!!");
			} catch (Exception e) {
				Log.error("while getting Web Object java script details", e);
				return null;
			}
		}
	}	
	
	public String getWebElementJavaScript(String sElementName, String sValue) {
		webElement = getElementByName(sElementName);
		String sLocatorType =  webElement.getEnglish().get("locatorType");
		String sLocatorValue = webElement.getEnglish().get("locatorValue");
		if(sLocatorValue.contains("${data}")){
			sLocatorValue = sLocatorValue.replaceAll("\\$\\{data\\}", sValue);
		}
		if(sLocatorType.toLowerCase().equals("javascript"))
			return sLocatorValue;
		else{
			try {
				throw new Exception("sLocator type '" + sLocatorType + "' not defined!!");
			} catch (Exception e) {
				Log.error("while getting Web Object java script details", e);
				return null;
			}
		}
	}	
	
	public By getWebElement(String sElementName, String dynamicText) {
		webElement = getElementByName(sElementName);
		String sLocatorType =  webElement.getEnglish().get("locatorType");
		String sLocatorValue = webElement.getEnglish().get("locatorValue");
		if(sLocatorValue.contains("${data}")){
			sLocatorValue = sLocatorValue.replaceAll("\\$\\{dynamicText\\}", dynamicText);
		}
		return getLocator(sLocatorType,sLocatorValue);
	}		
	
	public static List<String> getlistFiles () {
		List<String> list =null;
		try {
			CodeSource src = ObjectRepository.class.getProtectionDomain().getCodeSource();
			list = new ArrayList<String>();
			List<File> listFile = new ArrayList<File>();
			if( src != null ) {
				URL jar = src.getLocation();
				ZipInputStream zip = new ZipInputStream( jar.openStream());
				ZipEntry ze = null;
				while( ( ze = zip.getNextEntry() ) != null ) {
					String entryName = ze.getName();
					if( entryName.startsWith("objectRepository/") &&  entryName.endsWith(".json") ) {
						list.add( entryName  );
						listFile.add(new File(entryName));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
