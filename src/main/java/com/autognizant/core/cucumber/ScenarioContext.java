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

/**
 * A implementation that shares the information between step definitions across step definition classes of cucumber.
 */
public class ScenarioContext {

	private  Map<String, Object> scenarioContext;

    public ScenarioContext(){
        scenarioContext = new HashMap<>();
    }

    /**
     * Sets the context using key-value pair.
     * @param key
     * @param value 
     */
    public void setContext(String key, Object value) {
        scenarioContext.put(key, value);
    }

    /**
     * Gets the context value for the specified key.
     * @param key
     * @return value
     */
    public Object getContext(String key){
        return scenarioContext.get(key);
    }

    /**
     * Verifies if the given key is present or not.
     * @param key
     * @return Returns true if the given key is present otherwise false.
     */
    public Boolean isContains(String key){
        return scenarioContext.containsKey(key);
    }
}
