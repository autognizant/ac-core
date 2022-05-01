package com.autognizant.core.config;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AutognizantConfigTest {

	private AutognizantConfig autognizantConfig;

    @BeforeClass
    public void setup() {
    	autognizantConfig = new AutognizantConfig();
    	autognizantConfig.setAutognizantConfiguration();
    }

    @Test
    public void testGetProperties(){
        Assert.assertEquals(AutognizantConfig.getScenarioIDPrefix(), "@ACAUTO-", "Example Project is not found");
        Assert.assertEquals(AutognizantConfig.getAppWindowTitle(), "Google", "Example Project is not found");
        Assert.assertEquals(AutognizantConfig.getElementWait(), 30, "30 second is not found");
    }

}
