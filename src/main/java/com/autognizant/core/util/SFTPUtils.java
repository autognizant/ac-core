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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;

import org.junit.Assert;

import com.autognizant.core.config.CoreConfig;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * This class contains methods for connecting to any SFTP server and to perform upload/download operations.
 */
public class SFTPUtils {

	private static SFTPUtils sftpUtils = new SFTPUtils();
	private String SFTP_HOST = CoreConfig.getSftpHost();
	private String SFTP_USERNAME = CoreConfig.getSftpUserName();
	private String SFTP_PASSWORD = CoreConfig.getSftpPassword();
	private JSch jsch = null;
	private Session session = null;
	private Channel channel = null;
	private ChannelSftp channelSftp = null;	
	
	/**
	 * Gets the SFTPUtils singleton object.
	 * @return Returns the SFTPUtils singleton object.
	 */
	public static SFTPUtils getInstance( ) {
		return sftpUtils;
	}
	
	/**
	 * Gets connection with SFTP server (Uses the server details provided in configuration.properties file).
	 */
	public void getConnection() {
		try {
			jsch = new JSch();
			session = jsch.getSession(SFTP_USERNAME, SFTP_HOST);
			session.setPassword(SFTP_PASSWORD);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
//			config.put("kex", "diffie-hellman-group1-sha1");
			config.put("kex", "diffie-hellman-group-exchange-sha256");
			session.setConfig(config);
			session.setConfig("PreferredAuthentications", 
	                  "publickey,keyboard-interactive,password");
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			Log.info("SFTP Connection established succesfully");
		} catch (Exception e) {
			Log.error("SFTP Error occured", e);
			Assert.assertTrue("SFTP Error occured", false); 
		}
	}
	
	/**
	 * Closes SFTP connection.
	 */
	public void closeConnection() {
		try {
			channelSftp.exit();
			Log.info("sftp Channel exited.");
			channel.disconnect();
			Log.info("Channel disconnected.");
			session.disconnect();
			Log.info("Host Session disconnected.");
			Log.info("SFTP Connection closed succesfully");			
		} catch (Exception ex) {
			Log.error("Error Occured while closing Database Connection",ex); 
		}
	}
	
	/**
	 * Uploads a file to SFTP server.
	 * @param file file to be uploaded.
	 * @param locationPath location path where the file is to be uploaded to.
	 */
	public void uploadFile(File file, String locationPath) {
		try {
			channelSftp.cd(locationPath);
			channelSftp.put(new FileInputStream(file), file.getName());
//			channelSftp.chmod(777, file.getName());
			channelSftp.chmod(511, file.getName());
			Log.info("File Uploaded using SFTP Channel");
		} catch (Exception e) {
			if(isFileExists(file.getName(), locationPath)){
				Log.info("Exception Occured but File Uploaded using SFTP Channel");
				try {
					channelSftp.chmod(511, file.getName());
				} catch (SftpException e1) {
					Log.error("SFTP Error occured while changing permission", e);
					Assert.assertTrue("SFTP Error occured", false);					
				}
			}else{
				Log.error("SFTP Error occured", e);
				Assert.assertTrue("SFTP Error occured", false);
			}
		} 
	}
	
	/**
	 * Downloads a file from SFTP server.
	 * @param file file to be downloaded.
	 * @param locationPath location path where the file is to be downloaded from.
	 * @return Returns the downloaded file.
	 */
	public File downloadFile(File file, String locationPath) {
		try {
			channelSftp.cd(locationPath);
			FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
			channelSftp.get(file.getName(), fos);
			Log.info("File downloaded using SFTP Channel");
			return file;
		} catch (Exception e) {
			Log.error("SFTP Error occured", e);
			Assert.assertTrue("SFTP Error occured", false);
		} 
		return null;
	}

	/**
	 * Checks if a file exists on a SFTP server.
	 * @param fileName file
	 * @param locationPath location path where the file is to be searched.
	 * @return Returns true if the file exists on a SFTP server otherwise false.
	 */
	@SuppressWarnings("unchecked")
	public boolean isFileExists(String fileName, String locationPath) {
		boolean bFlag = false;
		try {
			channelSftp.cd(locationPath);
			Vector<ChannelSftp.LsEntry> list = channelSftp.ls(locationPath);
			for(ChannelSftp.LsEntry entry : list) {
			     if(entry.getFilename().equals(fileName)){
			    	 bFlag = true;
			    	 break;
			     }else{
			    	 continue;
			     }
			}
		} catch (Exception e) {
			Log.error("SFTP Error occured", e);
			Assert.assertTrue("SFTP Error occured", false);
		}
		return bFlag;
	}
}
