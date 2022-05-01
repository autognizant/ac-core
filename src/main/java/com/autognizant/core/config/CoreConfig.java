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
 * Core configuration class
 */
public class CoreConfig {

	private static PropertyAggregator propertyAggregator;
	private static String projectName;
	private static String clientName;
	private static String applicationType;
	private static String applicationURL;
	private static String environment;
	private static String browserName;
	private static String executionType;
	private static String gridURL;
	private static String executionMode;
	private static String languageName;
	private static String dbDriver;
	private static String dbConnectionURL;
	private static String dbUserName;
	private static String dbPassword;
	private static String sftpHost;
	private static String sftpPort;
	private static String sftpUserName;
	private static String sftpPassword;
	
    public CoreConfig() {
    	propertyAggregator =
    		    new PropertyAggregator.Builder()
    		    .withEnvironmentProperties()
    		    .withPropertiesFile(Constants.RESOURCES_PATH+"/config/configuration.properties")
    		    .build();
    }
    
    public void setConfiguration() {
    	setProjectName(getProperty("PROJECT"));
    	setClientName(getProperty("CLIENT_NAME"));
    	setApplicationType(getProperty("APPLICATION_TYPE"));
    	setApplicationURL(getProperty("APPLICATION_URL"));
    	setEnvironment(getProperty("ENVIRONMENT"));
    	setBrowserName(getProperty("BROWSER"));
    	setExecutionType(getProperty("EXECUTION_TYPE"));
    	setGridURL(getProperty("GRID_URL"));    	
    	setExecutionMode(getProperty("EXECUTION_MODE"));
    	setLanguageName(getProperty("LANGUAGE"));
    	setDbDriver(getProperty("DB_DRIVER"));
    	setDbConnectionURL(getProperty("DB_CONNECTION_URL"));
    	setDbUserName(getProperty("DB_USERNAME"));
    	setDbPassword(getProperty("DB_PASSWORD"));
    	setSftpHost(getProperty("SFTP_HOST"));
    	setSftpPort(getProperty("SFTP_PORT"));
    	setSftpUserName(getProperty("SFTP_USERNAME"));
    	setSftpPassword(getProperty("SFTP_PASSWORD"));
    }
    
	public static String getProjectName() {
		return projectName;
	}
	public static String getClientName() {
		return clientName;
	}
	public static String getApplicationType() {
		return applicationType;
	}
	public static String getApplicationURL() {
		return applicationURL;
	}
	public static String getEnvironment() {
		return environment;
	}
	public static String getBrowserName() {
		return browserName;
	}	
	public static String getExecutionType() {
		return executionType;
	}
	public static String getGridURL() {
		return gridURL;
	}	
	public static String getExecutionMode() {
		return executionMode;
	}
	public static String getLanguageName() {
		return languageName;
	}
	public static String getDbDriver() {
		return dbDriver;
	}
	public static String getDbConnectionURL() {
		return dbConnectionURL;
	}
	public static String getDbUserName() {
		return dbUserName;
	}
	public static String getDbPassword() {
		return dbPassword;
	}
	public static String getSftpHost() {
		return sftpHost;
	}
	public static String getSftpPort() {
		return sftpPort;
	}
	public static String getSftpUserName() {
		return sftpUserName;
	}
	public static String getSftpPassword() {
		return sftpPassword;
	}
	private void setProjectName(String projectName) {
		CoreConfig.projectName = projectName;
	}
	private void setClientName(String clientName) {
		CoreConfig.clientName = clientName;
	}
	private void setApplicationType(String applicationType) {
		CoreConfig.applicationType = applicationType;
	}
	private void setApplicationURL(String applicationURL) {
		CoreConfig.applicationURL = applicationURL;
	}
	private void setEnvironment(String environment) {
		CoreConfig.environment = environment;
	}
	private void setBrowserName(String browserName) {
		CoreConfig.browserName = browserName;
	}
	private void setExecutionType(String executionType) {
		CoreConfig.executionType = executionType;
	}
	private void setGridURL(String gridURL) {
		CoreConfig.gridURL = gridURL;
	}
	private void setExecutionMode(String executionMode) {
		CoreConfig.executionMode = executionMode;
	}
	private void setLanguageName(String languageName) {
		CoreConfig.languageName = languageName;
	}
	private void setDbDriver(String dbDriver) {
		CoreConfig.dbDriver = dbDriver;
	}
	private void setDbConnectionURL(String dbConnectionURL) {
		CoreConfig.dbConnectionURL = dbConnectionURL;
	}
	private void setDbUserName(String dbUserName) {
		CoreConfig.dbUserName = dbUserName;
	}
	private void setDbPassword(String dbPassword) {
		CoreConfig.dbPassword = dbPassword;
	}
	private void setSftpHost(String sftpHost) {
		CoreConfig.sftpHost = sftpHost;
	}
	private void setSftpPort(String sftpPort) {
		CoreConfig.sftpPort = sftpPort;
	}
	private void setSftpUserName(String sftpUserName) {
		CoreConfig.sftpUserName = sftpUserName;
	}
	private void setSftpPassword(String sftpPassword) {
		CoreConfig.sftpPassword = sftpPassword;
	}
	
	public static String getProperty(String propertyName) {
		String propertyValue = propertyAggregator.getProperty(propertyName);
		if(propertyValue != null) return propertyValue;
		else throw new RuntimeException(propertyName+" is not specified in the configuration.properties file.");
	}	
}
