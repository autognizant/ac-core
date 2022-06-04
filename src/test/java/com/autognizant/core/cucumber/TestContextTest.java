package com.autognizant.core.cucumber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.autognizant.core.managers.DriverManager;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TestContext.class)
public class TestContextTest {

	private TestContext testContext;
	private DriverManager testDriverManager;

    @BeforeClass
    public void setup() {
    	testContext = PowerMockito.mock(TestContext.class);
    	testDriverManager = PowerMockito.mock(DriverManager.class);
    	testContext.setContext("Key1","Value1");
    	testContext.setContext("Key2","Value2");
    }
    
    @Test
    public void testGetTestDriverManager(){
    	PowerMockito.when(testContext.getDriverManager()).thenReturn(testDriverManager);
    	assertThat(testContext.getDriverManager(), instanceOf(DriverManager.class));
    }
    
    @Test
    public void testGetContext(){
        Assert.assertTrue(testContext.getContext("Key1").equals("Value1"), "Value1 is not found");
    }
    
    @Test
    public void testIsContainsSuccessTest(){
        Assert.assertTrue(testContext.isContains("Key2"), "isContains() return true failed");
    }

    @Test
    public void testIsContainsFailedTest(){
        Assert.assertFalse(testContext.isContains("Key3"), "isContains() return false failed");
    }
}
