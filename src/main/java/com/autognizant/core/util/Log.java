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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * Provides logger service to log events, activities and errors.
 * slf4j Logger utility is used in this class.
 */
public class Log {

    private static final Logger logger = LoggerFactory.getLogger(Log.class);
    
	/**
	 * Logs information message in log file.
	 * @param sMessage information message.
	 */
	public static void info(String sMessage) {
		logger.info(sMessage);
	}

	/**
	 * Logs warn message in log file.
	 * @param sMessage warn message.
	 */
	public static void warn(String sMessage) {
		logger.warn(sMessage);
	}

	/**
	 * Logs error message in log file.
	 * @param sMessage error message.
	 * @param e Exception message.
	 */
	public static void error(String sMessage,Exception e) {
		logger.error("Error = " +sMessage);
		logger.error("Exception Message = " + e.getMessage());
		logger.error("Backtrace = " +StringUtils.join(ExceptionUtils.getRootCauseStackTrace(e), "\n"));
	}

	/**
	 * Logs trace message in log file.
	 * @param sMessage trace message.
	 */
	public static void fatal(String sMessage) {
		logger.trace(sMessage);
	}

	/**
	 * Logs debug message in log file.
	 * @param sMessage debug message.
	 * @param e Exception message.
	 */
	public static void debug(String sMessage,Exception e) {
		logger.debug(sMessage,e);
	}
}
