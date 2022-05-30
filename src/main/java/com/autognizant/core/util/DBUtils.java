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

package com.autognizant.core.util;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.autognizant.core.config.CoreConfig;

/**
 * This class includes methods for connecting to any database and to perform read/write operations on any database tables.
 */
public class DBUtils {

	private static DBUtils dbUtils = new DBUtils();
	private Connection connection;
	private Statement statement;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private List<HashMap<String,Object>> list;
	private Map<String, List<Object>> map;
	
	/**
	 * DBUtils constructor.
	 */
	private DBUtils() {
		connection = null;
		statement = null;
		preparedStatement = null;
		resultSet = null;
	}
	
	/**
	 * Gets the DBUtils singleton object.
	 * @return Returns the DBUtils singleton object.
	 */
	public static DBUtils getInstance( ) {
		return dbUtils;
	}
	
	/**
	 * Gets connection with database (Uses the database details provided in configuration.properties file).
	 */
	public void getConnection() {
		try {
			Class.forName(CoreConfig.getDbDriver());
			try {
				connection = DriverManager.getConnection(CoreConfig.getDbConnectionURL(), CoreConfig.getDbUserName(), CoreConfig.getDbPassword());
				statement =  connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//				statement.setQueryTimeout(10);
				Log.info("DB Connection established succesfully");
			} catch (SQLException ex) {
				Log.error("Failed to create the database connection.",ex); 
			}
		} catch (ClassNotFoundException ex) {
			Log.error("Driver not found.",ex); 
		}
	}

	/**
	 * Gets connection with database.
	 * @param driver Name of the database driver class.
	 * @param connectionURL database connection URL.
	 * @param userName database user name.
	 * @param password database password.
	 */
	public void getConnection(String driver, String connectionURL, String userName, String password) {
		try {
			Class.forName(driver);
			try {
				connection = DriverManager.getConnection(connectionURL, userName, password);
				statement =  connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//				statement.setQueryTimeout(10);
				Log.info("DB Connection established succesfully");
			} catch (SQLException ex) {
				Log.error("Failed to create the database connection.",ex); 
			}
		} catch (ClassNotFoundException ex) {
			Log.error("Driver not found.",ex); 
		}
	}
	
	/**
	 * Closes connection with database.
	 */
	public void closeConnection() {
		try {
			statement.close();
			connection.close();
			Log.info("DB Connection closed succesfully");
		} catch (Exception ex) {
			Log.error("Error Occured while closing Database Connection",ex); 
		}
	}
	
	/**
	 * Executes DML query.
	 * @param updateQuery DML query to be executed.
	 */
	public void executeUpdateQuery(String updateQuery){
		try {
			Log.info("updateQuery = " + updateQuery);
			statement.executeUpdate(updateQuery);
		} catch (SQLException e) {
			Log.error("SQL Exception Error", e);
		}	
	}
	
	/**
	 * Executes prepared select query and converts ResultSet into List and Map format.
	 * @param query select query to be executed.
	 * @param params parameters to be replaced with ? in the prepared query.
	 */
	public void executePreparedQuery(String query,String ... params){
		try {
			Log.info("query = " + query);
			preparedStatement = connection.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			for(int i=0; i < params.length; i++){
				preparedStatement.setString(i+1, params[i]);
			}
			resultSet = preparedStatement.executeQuery();
			convertResultSetToList();
			convertResultSetToMap();
			resultSet.close();
			preparedStatement.close();
		} catch (SQLException e) {
			Log.error("SQL Exception Error", e);
		}	
	}
	
	/**
	 * Executes select query and converts ResultSet into List and Map format.
	 * @param query select query to be executed.
	 * @throws SQLException throws SQLException
	 */
	public void executeQuery(String query) throws SQLException{
			Log.info("executeDBQuery = " + query);
			resultSet = statement.executeQuery(query);
			convertResultSetToList();
			convertResultSetToMap();
	}
	
	/**
	 * Converts ResultSet into List object.
	 */
	private void convertResultSetToList() throws SQLException {
	    ResultSetMetaData md = resultSet.getMetaData();
	    int columns = md.getColumnCount();
	    list = new ArrayList<HashMap<String,Object>>();
	    resultSet.beforeFirst();
	    while (resultSet.next()) {
	        HashMap<String,Object> row = new HashMap<String, Object>(columns);
	        for(int i=1; i<=columns; ++i) {
	            row.put(md.getColumnName(i),resultSet.getObject(i));
	        }
	        list.add(row);
	    }
	}
	
	/**
	 * Converts ResultSet into Map object.
	 */
	private void convertResultSetToMap() throws SQLException {
	    ResultSetMetaData md = resultSet.getMetaData();
	    int columns = md.getColumnCount();
	    map = new HashMap<>(columns);
	    for (int i = 1; i <= columns; ++i) {
	        map.put(md.getColumnName(i), new ArrayList<>());
	    }
	    resultSet.beforeFirst();
	    while (resultSet.next()) {
	        for (int i = 1; i <= columns; ++i) {
	            map.get(md.getColumnName(i)).add(resultSet.getObject(i));
	        }
	    }
	}
	
	/**
	 * Gets ResultSet into List object.
	 * @return Returns ResultSet in the form of List.
	 */
	public List<HashMap<String,Object>> getResultSetToList(){
		return list;
	}
	
	/**
	 * Gets ResultSet into Map object.
	 * @return Returns ResultSet in the form of Map.
	 */
	public Map<String, List<Object>> getResultSetToMap(){
		return map;
	}
	
	/**
	 * Executes SQL file.
	 * @param fileName SQL file to be executed. 
	 * @throws Exception Exception
	 */
	public void executeProcedure(String fileName) throws Exception{
		String scriptFilePath = Constants.RESOURCES_PATH+"/TestData/"+fileName;
		String plsql = FileUtils.readFileToString(new File(scriptFilePath),"UTF-8");
		CallableStatement cs = connection.prepareCall(plsql);
        cs.setString(1, fileName);
        cs.execute();
	}
}
