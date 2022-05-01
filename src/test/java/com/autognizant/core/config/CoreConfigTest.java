package com.autognizant.core.config;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CoreConfigTest {

	private CoreConfig coreConfig;

    @BeforeClass
    public void setup() {
    	coreConfig = new CoreConfig();
    	coreConfig.setConfiguration();
    }

    @Test
    public void testGetProperties(){
        Assert.assertEquals(CoreConfig.getProjectName(), "Example Project", "Example Project is not found");
        Assert.assertEquals(CoreConfig.getClientName(), "Autognizant", "Example Project is not found");
        Assert.assertEquals(CoreConfig.getApplicationType(), "WEB", "WEB is not found");
        Assert.assertEquals(CoreConfig.getApplicationURL(), "https://www.google.co.in/", "Example Project is not found");
        Assert.assertEquals(CoreConfig.getEnvironment(), "QA", "QA is not found");
        Assert.assertEquals(CoreConfig.getBrowserName(), "HtmlUnitDriver", "HtmlUnitDriver Project is not found");
        Assert.assertEquals(CoreConfig.getExecutionType(), "LOCAL", "LOCAL is not found");
        Assert.assertEquals(CoreConfig.getExecutionMode(), "HEADLESS", "HEADLESS is not found");
        Assert.assertEquals(CoreConfig.getLanguageName(), "ENGLISH", "ENGLISH is not found");
        Assert.assertEquals(CoreConfig.getProperty("BUILD"), "QA.v1", "QA.v1 is not found");
    }

}
