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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.io.FileUtils;

import com.autognizant.core.config.AutognizantConfig;
import com.autognizant.core.config.CoreConfig;
import com.autognizant.core.util.Log;

import io.cucumber.java.Scenario;

/**
 * AutomationHooks class is used to configure "@Before" and "@After" annotation methods in cucumber.
 * These methods are responsible for launching the browser, performing logging setup, loading object repository.
 * "@Before" method gets executed before starting the execution of the cucumber scenario.
 * "@After" method gets executed after finishing the execution of the cucumber scenario.
 */
public class AutomationHooks {

	private TestContext testContext;
	private String scenarioUniqueID;
	private String scenarioName;
	private String logFile;

	/**
	 * AutomationHooks constructor.
	 * @param context TestContext object used to pass the information between cucumber step definition classes.
	 */
	public AutomationHooks(TestContext context) {
		testContext = context;
	}

	/**
	 * Gets source Tag Names value for the scenario.
	 * @param all tags mentioned at scenario level.
	 * @return Returns unique Scenario ID 
	 */ 
	private String getScenariotag(Collection<String> sourceTagNames) {
		CollectionUtils.filter(sourceTagNames,
				new Predicate<String>() {
			public boolean evaluate(String o) {
				return ((String)o).startsWith(AutognizantConfig.getScenarioIDPrefix());
			}
		}
		);
		return sourceTagNames.iterator().next();
	}
	
	/**
	 * Executes before starting the execution of the cucumber scenario.
	 * @param scenario Cucumber scenario object.
	 */ 
	public void beforeScenario(Scenario scenario){
		List<String> filtered = new ArrayList<String>(scenario.getSourceTagNames());
		scenarioUniqueID = getScenariotag(filtered);
		AutognizantConfig.setScenarioUniqueID(scenarioUniqueID);
		scenarioName = scenario.getName();
		AutognizantConfig.setScenarioName(scenarioName);
		logFile = (scenarioUniqueID+"-"+scenario.getId()).replaceAll(":", "-").replaceAll("/", "-").replaceAll("\\.", "-")+".log";
		AutognizantConfig.setLogFile(logFile);
		testContext.getDriverManager().getAutognizantDriver().loggingSetup(logFile);
		testContext.getDriverManager().getAutognizantDriver().loadObjectRepository();
		Log.info("scenario.getId = " + scenario.getId());
		Log.info("scenarioName = " + scenarioName);
	}

	/**
	 * Executes after finishing the execution of the cucumber scenario.
	 * @param scenario Cucumber scenario object.
	 * @throws IOException IOException
	 */ 
	public void afterScenario(Scenario scenario) throws IOException{
		if(CoreConfig.getApplicationType().equalsIgnoreCase("web")) {
			if(scenario.isFailed()){
				File screenshotFile = testContext.getDriverManager().getAutognizantDriver().captureScreenshot();
				if(screenshotFile != null) {
					String screenshotName = scenario.getName().replaceAll(" ", "_");
					scenario.attach(FileUtils.readFileToByteArray(screenshotFile), "image/png", screenshotName);
				}
			}
		}
		testContext.getDriverManager().quitAutognizantDriver();
		scenario.attach(FileUtils.readFileToByteArray(new File("target/logs/"+logFile)), "text/plain",logFile);
	}	
}
