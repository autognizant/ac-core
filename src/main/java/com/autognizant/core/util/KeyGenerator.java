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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.*;

/** 
 * Provides utility to get unique strings for automation.
 */
public class KeyGenerator {

	public static String getDate(String sDate,String sFormat) {
		String dateString=null;
		Date date = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat(sFormat);
		if(sDate.equals("CurrentDate") || sDate.equals("Today")){
			dateString = format.format(date);
		}else if (sDate.contains("CurrentDate+")){
			int days = Integer.parseInt(sDate.replaceAll("CurrentDate+", ""));
			c.add(Calendar.DATE, days);
			dateString = format.format(c.getTime());
		}else if (sDate.contains("CurrentDate-")){
			int days = Integer.parseInt(sDate.replaceAll("CurrentDate-", ""));
			c.add(Calendar.DATE, - days);
			dateString = format.format(c.getTime());
		}else if(sDate.equals("Holiday")){
			while(c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
				c.add(Calendar.DAY_OF_WEEK, 1);
			}
			dateString = format.format(c.getTime());
		}
		return dateString;
	}
	
	/**
	 * Gets unique random integer for given no. of digits.
	 * @param iNumberOfDigits Number of digits.
	 * @return Returns unique random integer as string for given no. of digits.
	 */
	public static String getRandomInteger(int iNumberOfDigits){	
		return RandomStringUtils.randomNumeric(iNumberOfDigits);
	}

	/**
	 * Gets unique random alphabetic for given no. of characters.
	 * @param iNumberOfCharacters Number of characters.
	 * @return Returns unique random alphabetic for given no. of characters.
	 */
	public static String getRandomString(int iNumberOfCharacters){	
		return RandomStringUtils.randomAlphabetic(iNumberOfCharacters);
	}

	/**
	 * Gets unique random alphanumeric for given no. of characters.
	 * @param iNumberOfCharacters Number of characters.
	 * @return Returns unique random alphanumeric for given no. of characters.
	 */
	public static String getrandomAlphanumericString(int iNumberOfCharacters){	
		return RandomStringUtils.randomAlphanumeric(iNumberOfCharacters);
	}
}
