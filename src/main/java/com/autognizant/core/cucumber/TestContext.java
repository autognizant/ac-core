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

import java.util.HashMap;
import java.util.Map;

import com.autognizant.core.config.AutognizantConfig;
import com.autognizant.core.config.CoreConfig;
import com.autognizant.core.managers.DriverManager;

/**
 * A implementation that shares the information between step definitions across step definition classes of cucumber.
 * This class also shares autognizant driver object across step definition classes of cucumber.
 */
public class TestContext {

	private DriverManager driverManager;
	private CoreConfig coreConfig;
	private AutognizantConfig autognizantConfig;
	private Map<String, Object> context;

	/**
	 * TestContext constructor.
	 */
	public TestContext(){
    	autognizantConfig = new AutognizantConfig();
    	autognizantConfig.setAutognizantConfiguration();
    	coreConfig = new CoreConfig();
    	coreConfig.setConfiguration();
		driverManager = new DriverManager();
		context = new HashMap<>();
	}
	
	/**
	 * Gets driver manager object.
	 * @return driver manager object.
	 */ 
	public DriverManager getDriverManager() {
		return driverManager;
	}
	
    /**
     * Sets the context using key-value pair.
     * @param key key value
     * @param value of the given key.
     */
    public void setContext(String key, Object value) {
    	context.put(key, value);
    }

    /**
     * Gets the context value for the specified key.
     * @param key key value 
     * @return value of the given key.
     */
    public Object getContext(String key){
        return context.get(key);
    }

    /**
     * Verifies if the given key is present or not.
     * @param key key value
     * @return Returns true if the given key is present otherwise false.
     */
    public Boolean isContains(String key){
        return context.containsKey(key);
    }
}
