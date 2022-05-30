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

package com.autognizant.core.managers;

import com.autognizant.core.config.CoreConfig;
import com.autognizant.core.selenium.AutognizantDriver;
import com.autognizant.core.selenium.DriverFactory;

/**
 * The DriverManager class used to manage AutognizantDriver object.
 */
public class DriverManager {
	
	private AutognizantDriver driver;

	/**
	 * DriverManager constructor.
	 */
	public DriverManager() {
		if(driver == null){
			driver = new AutognizantDriver(CoreConfig.getApplicationType());
			DriverFactory.addDriver(driver.getDriver());
		}
	}

	/**
	 * Gets AutognizantDriver object.
	 * @return AutognizantDriver object.
	 */
	public AutognizantDriver getAutognizantDriver() {
		return driver;
	}
	
	/**
	 * Quits AutognizantDriver object.
	 */
	public void quitAutognizantDriver(){
		driver.quitBrowser();
		DriverFactory.removeDriver();
	}
}
