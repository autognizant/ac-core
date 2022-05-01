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

package com.autognizant.core.cucumber;

import com.autognizant.core.config.AutognizantConfig;
import com.autognizant.core.config.CoreConfig;
import com.autognizant.core.managers.DriverManager;

public class TestContext {

	private DriverManager driverManager;
	public ScenarioContext scenarioContext;
	
	private CoreConfig coreConfig;
	private AutognizantConfig autognizantConfig;
	
	public TestContext(){
    	coreConfig = new CoreConfig();
    	coreConfig.setConfiguration();
    	autognizantConfig = new AutognizantConfig();
    	autognizantConfig.setAutognizantConfiguration();
    	
		driverManager = new DriverManager();
		scenarioContext = new ScenarioContext();
	}
	
	public DriverManager getDriverManager() {
		return driverManager;
	}
	
	public ScenarioContext getScenarioContext() {
		return scenarioContext;
	}	
}
