package com.autognizant.core.cucumber;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ScenarioContextTest {

	private ScenarioContext scenarioContext;

    @BeforeClass
    public void setup() {
        scenarioContext = new ScenarioContext();
    	scenarioContext.setContext("Key1","Value1");
    	scenarioContext.setContext("Key2","Value2");
    }

    @Test
    public void testGetContext(){
        Assert.assertTrue(scenarioContext.getContext("Key1").equals("Value1"), "Value1 is not found");
    }
    
    @Test
    public void testIsContainsSuccessTest(){
        Assert.assertTrue(scenarioContext.isContains("Key2"), "isContains() return true failed");
    }

    @Test
    public void testIsContainsFailedTest(){
        Assert.assertFalse(scenarioContext.isContains("Key3"), "isContains() return false failed");
    }
}
