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

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.autognizant.core.util.Log;

/**
 * This class manages the multiple WebDriver instances created during parallel execution using ThreadLocal.
 */
public final class DriverFactory {

	private static ThreadLocal<WebDriver> drivers = new ThreadLocal<>();

	// To quit the drivers and browsers at the end only.
	private static List<WebDriver> storedDrivers = new ArrayList<>();

	/*
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				storedDrivers.forEach(WebDriver::quit);
			}
		});
		Log.info("ShutdownHook successfully executed !");
	}*/

	/**
	 * DriverFactory constructor.
	 */
	private DriverFactory() {}

	/**
	 * Gets WebDriver object.
	 * @return WebDriver object.
	 */ 
	public static WebDriver getDriver() {
		return drivers.get();
	}

	/**
	 * Adds WebDriver object in storedDrivers list.
	 * @param driver WebDriver object.
	 */ 
	public static void addDriver(WebDriver driver) {
		storedDrivers.add(driver);
		drivers.set(driver);
	}

	/**
	 * Removes WebDriver object from storedDrivers list.
	 */ 
	public static void removeDriver() {
		Log.info("Beore storedDrivers list = " + storedDrivers.size());
		storedDrivers.remove(drivers.get());
		drivers.remove();
		Log.info("After storedDrivers list = " + storedDrivers.size());
	}
}
