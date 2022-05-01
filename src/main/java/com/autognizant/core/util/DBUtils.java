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

public class DBUtils {

	private static DBUtils dbUtils = new DBUtils();
	private Connection connection;
	private Statement statement;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private List<HashMap<String,Object>> list;
	private Map<String, List<Object>> map;
	
	private DBUtils() {
		connection = null;
		statement = null;
		preparedStatement = null;
		resultSet = null;
	}
	
	public static DBUtils getInstance( ) {
		return dbUtils;
	}
	
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
	
	public String executeDBQuery(String query){
		String data = null;
		try {
			Log.info("query = " + query);
			resultSet = statement.executeQuery(query);
			resultSet.next();
			data = resultSet.getString(1);
			resultSet.close();
		} catch (SQLException e) {
			Log.error("SQL Exception Error", e);
		}	
		return data;
	}

	public void executeDBPreparedQuery(String query,String ... params){
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
	
	public List<String> executeDBQuery_arraylist(String query){
		List<String> data = new ArrayList<String>();
		try {
			Log.info("query = " + query);
			resultSet = statement.executeQuery(query);
			while(resultSet.next()){
				data.add(resultSet.getString(1));
			}
			
			resultSet.close();
		} catch (SQLException e) {
			Log.error("SQL Exception Error", e);
		}	
		return data;
	}
	
	public void executeUpdateQuery(String sUpdateQuery){
		try {
			Log.info("query = " + sUpdateQuery);
			statement.executeUpdate(sUpdateQuery);
		} catch (SQLException e) {
			Log.error("SQL Exception Error", e);
		}	
	}
	
	public void closeConnection() {
		try {
			statement.close();
			connection.close();
			Log.info("DB Connection closed succesfully");
		} catch (Exception ex) {
			Log.error("Error Occured while closing Database Connection",ex); 
		}
	}
	
	public void executeDBQuery_resultSet(String query) throws SQLException{
			Log.info("executeDBQuery = " + query);
			resultSet = statement.executeQuery(query);
			convertResultSetToList();
			convertResultSetToMap();
	}
	
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
	
	public List<HashMap<String,Object>> getResultSetToList(){
		return list;
	}
	
	public Map<String, List<Object>> getResultSetToMap(){
		return map;
	}
	
	public void executeProcedure(String sFileName) throws Exception{
		String scriptFilePath = Constants.RESOURCES_PATH+"/TestData/PURGE.sql";
		String plsql = FileUtils.readFileToString(new File(scriptFilePath),"UTF-8");
		CallableStatement cs = connection.prepareCall(plsql);
        cs.setString(1, sFileName);
        cs.execute();
	}
}
