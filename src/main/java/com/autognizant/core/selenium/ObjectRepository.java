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

/**
 * This class loads the web element locator details from JSON object repository files and stores them in list of JsonWebElement class.
 */
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
		    if(!Constants.RESOURCES_PATH.contains("\\src\\test\\resources")){
		    	List <String> jsonFiles = getlistFiles();
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
	 * @param sLocatorType Locator Type of the web element mentioned in Object Repository.
	 * @param sLocatorValue Locator Value of the web element mentioned in Object Repository.
	 * @return Returns By property for web element.
	 * @throws Exception Runtime Exception
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
	
	/**
	 * Gets JsonWebElement object by using web element name.
	 * @param webElementName WebElement Name provided in the object repository.
	 * @return JsonWebElement object.
	 */ 
	private JsonWebElement getElementByName(String webElementName){
		return jsonWebElementList.stream().filter(x -> x.getWebElementName().equalsIgnoreCase(webElementName)).findAny().get();
	}
	
	/**
	 * Gets frame name of the web element.
	 * @param webElementName WebElement Name provided in the object repository.
	 * @return frame name.
	 */ 
	public String getFrameName(String webElementName) {
		webElement = getElementByName(webElementName);
		return webElement.getFrameName();
	}

	/**
	 * Gets By object of the web element.
	 * @param webElementName WebElement Name provided in the object repository.
	 * @return By object of the web element.
	 */ 
	public By getWebElement(String webElementName) {
		webElement = getElementByName(webElementName);
		String sLocatorType =  webElement.getEnglish().get("locatorType");
		String sLocatorValue = webElement.getEnglish().get("locatorValue");
		return getLocator(sLocatorType,sLocatorValue);
	}	
	
	/**
	 * Gets Javascript to be executed on the web element.
	 * @param webElementName WebElement Name provided in the object repository.
	 * @return Javascript to be executed on the web element.
	 */ 
	public String getWebElementJavaScript(String webElementName) {
		webElement = getElementByName(webElementName);
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
	
	/**
	 * Gets Javascript to be executed on the web element.
	 * @param webElementName WebElement Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @return Javascript to be executed on the web element.
	 */ 
	public String getWebElementJavaScript(String webElementName, String dynamicText) {
		webElement = getElementByName(webElementName);
		String sLocatorType =  webElement.getEnglish().get("locatorType");
		String sLocatorValue = webElement.getEnglish().get("locatorValue");
		if(sLocatorValue.contains("${data}")){
			sLocatorValue = sLocatorValue.replaceAll("\\$\\{data\\}", dynamicText);
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
	
	/**
	 * Retrieves the web element properties from Object Repository.
	 * @param webElementName WebElement Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @return Returns By property for web element.
	 */ 
	public By getWebElement(String webElementName, String dynamicText) {
		webElement = getElementByName(webElementName);
		String sLocatorType =  webElement.getEnglish().get("locatorType");
		String sLocatorValue = webElement.getEnglish().get("locatorValue");
		if(sLocatorValue.contains("${data}")){
			sLocatorValue = sLocatorValue.replaceAll("\\$\\{dynamicText\\}", dynamicText);
		}
		return getLocator(sLocatorType,sLocatorValue);
	}		
	
	/**
	 * Gets the lists of object repository JSON files present in the Object Repository folder.
	 * @return Returns the lists of object repository JSON files.
	 */
	private List<String> getlistFiles () {
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
