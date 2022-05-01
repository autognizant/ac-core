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

public class AutomationHooks {

	private TestContext testContext;
	public String scenarioUniqueID;
	public String scenarioName;
	public String logFile;

	public AutomationHooks(TestContext context) {
		testContext = context;
	}

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
	
	public void beforeScenario(Scenario scenario) throws Exception{
		List<String> filtered = new ArrayList<String>(scenario.getSourceTagNames());
		scenarioUniqueID = getScenariotag(filtered);
		scenarioName = scenario.getName();
		logFile = (scenarioUniqueID+"-"+scenario.getId()).replaceAll(":", "-").replaceAll("/", "-").replaceAll("\\.", "-")+".log";
		testContext.getDriverManager().getAutognizantDriver().loggingSetup(logFile);
		testContext.getDriverManager().getAutognizantDriver().loadObjectRepository();
		Log.info("scenario.getId = " + scenario.getId());
		Log.info("scenarioName = " + scenarioName);
	}

	public void afterScenario(Scenario scenario) throws Exception {
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
