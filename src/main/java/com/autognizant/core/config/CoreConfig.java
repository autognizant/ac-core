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
 * CoreConfig class is used to configure project level parameters.
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
	
	/**
	 * CoreConfig constructor.
	 */
    public CoreConfig() {
    	propertyAggregator =
    		    new PropertyAggregator.Builder()
    		    .withEnvironmentProperties()
    		    .withPropertiesFile(Constants.RESOURCES_PATH+"/config/configuration.properties")
    		    .build();
    }
    
	/**
	 * Sets project configuration from configuration.properties file.
	 */ 
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
    
	/**
	 * Gets project name value.
	 * @return project name.
	 */ 
	public static String getProjectName() {
		return projectName;
	}
	
	/**
	 * Gets client name value.
	 * @return client name.
	 */ 
	public static String getClientName() {
		return clientName;
	}
	
	/**
	 * Gets application type value.
	 * @return application type.
	 */ 
	public static String getApplicationType() {
		return applicationType;
	}
	
	/**
	 * Gets application URL value.
	 * @return application URL.
	 */ 
	public static String getApplicationURL() {
		return applicationURL;
	}
	
	/**
	 * Gets execution environment value.
	 * @return execution environment.
	 */ 
	public static String getEnvironment() {
		return environment;
	}
	
	/**
	 * Gets browser name value.
	 * @return browser name.
	 */ 
	public static String getBrowserName() {
		return browserName;
	}
	
	/**
	 * Gets execution type value.
	 * @return execution type.
	 */ 
	public static String getExecutionType() {
		return executionType;
	}
	
	/**
	 * Gets Grid URL value.
	 * @return Grid URL.
	 */ 
	public static String getGridURL() {
		return gridURL;
	}
	
	/**
	 * Gets execution mode value. REAL OR HEADLESS.
	 * @return execution mode value. REAL OR HEADLESS.
	 */ 
	public static String getExecutionMode() {
		return executionMode;
	}
	
	/**
	 * Gets language value.
	 * @return language.
	 */ 
	public static String getLanguageName() {
		return languageName;
	}
	
	/**
	 * Gets database driver value.
	 * @return database driver class name.
	 */ 
	public static String getDbDriver() {
		return dbDriver;
	}
	
	/**
	 * Gets database connection URL value.
	 * @return database connection URL.
	 */ 
	public static String getDbConnectionURL() {
		return dbConnectionURL;
	}
	
	/**
	 * Gets database user name value.
	 * @return database user name.
	 */ 
	public static String getDbUserName() {
		return dbUserName;
	}
	
	/**
	 * Gets database password value.
	 * @return database password.
	 */ 
	public static String getDbPassword() {
		return dbPassword;
	}
	
	/**
	 * Gets SFTP Host value.
	 * @return SFTP Host.
	 */ 
	public static String getSftpHost() {
		return sftpHost;
	}
	
	/**
	 * Gets SFTP port value.
	 * @return SFTP port.
	 */ 
	public static String getSftpPort() {
		return sftpPort;
	}
	
	/**
	 * Gets SFTP UserName value.
	 * @return SFTP UserName. 
	 */ 
	public static String getSftpUserName() {
		return sftpUserName;
	}
	
	/**
	 * Gets SFTP Password value.
	 * @return SFTP Password. 
	 */ 
	public static String getSftpPassword() {
		return sftpPassword;
	}
	
	/**
	 * Sets project name value.
	 * @param projectName project name.
	 */ 
	private void setProjectName(String projectName) {
		CoreConfig.projectName = projectName;
	}
	
	/**
	 * Sets client name value.
	 * @param clientName client name.
	 */ 
	private void setClientName(String clientName) {
		CoreConfig.clientName = clientName;
	}
	
	/**
	 * Sets application type value.
	 * @param applicationType application type.
	 */ 
	private void setApplicationType(String applicationType) {
		CoreConfig.applicationType = applicationType;
	}
	
	/**
	 * Sets application URL value.
	 * @param applicationURL application URL.
	 */ 
	private void setApplicationURL(String applicationURL) {
		CoreConfig.applicationURL = applicationURL;
	}
	
	/**
	 * Sets execution environment value.
	 * @param environment execution environment
	 */ 
	private void setEnvironment(String environment) {
		CoreConfig.environment = environment;
	}
	
	/**
	 * Sets browser name value.
	 * @param browserName browser name.
	 */ 
	private void setBrowserName(String browserName) {
		CoreConfig.browserName = browserName;
	}
	
	/**
	 * Sets execution type value.
	 * @param executionType execution type. LOCAL OR GRID.
	 */ 
	private void setExecutionType(String executionType) {
		CoreConfig.executionType = executionType;
	}
	
	/**
	 * Sets Grid URL value.
	 * @param gridURL Selenium Grid URL.
	 */ 
	private void setGridURL(String gridURL) {
		CoreConfig.gridURL = gridURL;
	}
	
	/**
	 * Sets execution mode value.
	 * @param executionMode execution mode. REAL or Headless.
	 */ 
	private void setExecutionMode(String executionMode) {
		CoreConfig.executionMode = executionMode;
	}
	
	/**
	 * Sets language value.
	 * @param languageName language name.
	 */ 
	private void setLanguageName(String languageName) {
		CoreConfig.languageName = languageName;
	}
	
	/**
	 * Sets database driver value.
	 * @param dbDriver database driver class name.
	 */ 
	private void setDbDriver(String dbDriver) {
		CoreConfig.dbDriver = dbDriver;
	}
	
	/**
	 * Sets database connection URL value.
	 * @param dbConnectionURL database connection URL.
	 */ 
	private void setDbConnectionURL(String dbConnectionURL) {
		CoreConfig.dbConnectionURL = dbConnectionURL;
	}
	
	/**
	 * Sets database user name value.
	 * @param dbUserName database user name.
	 */ 
	private void setDbUserName(String dbUserName) {
		CoreConfig.dbUserName = dbUserName;
	}
	
	/**
	 * Sets database password value.
	 * @param dbPassword database password.
	 */ 
	private void setDbPassword(String dbPassword) {
		CoreConfig.dbPassword = dbPassword;
	}
	
	/**
	 * Sets SFTP Host value.
	 * @param sftpHost SFTP Host name or IP Address.
	 */ 
	private void setSftpHost(String sftpHost) {
		CoreConfig.sftpHost = sftpHost;
	}
	
	/**
	 * Sets SFTP port value.
	 * @param sftpPort SFTP port.
	 */ 
	private void setSftpPort(String sftpPort) {
		CoreConfig.sftpPort = sftpPort;
	}
	
	/**
	 * Sets SFTP UserName value.
	 * @param sftpUserName SFTP UserName. 
	 */ 
	private void setSftpUserName(String sftpUserName) {
		CoreConfig.sftpUserName = sftpUserName;
	}
	
	/**
	 * Sets SFTP Password value.
	 * @param sftpPassword SFTP Password.
	 */ 
	private void setSftpPassword(String sftpPassword) {
		CoreConfig.sftpPassword = sftpPassword;
	}
	
	/**
	 * Gets property value from configuration.properties file.
	 * @param propertyName Name of the property.
	 * @return Value of the property.
	 */ 
	public static String getProperty(String propertyName) {
		String propertyValue = propertyAggregator.getProperty(propertyName);
		if(propertyValue != null) return propertyValue;
		else throw new RuntimeException(propertyName+" is not specified in the configuration.properties file.");
	}	
}
