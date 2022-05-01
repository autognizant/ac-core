package com.autognizant.core.cucumber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.hamcrest.core.IsInstanceOf;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.autognizant.core.managers.DriverManager;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TestContext.class)
public class TestContextTest {

	private TestContext testContext;
	private DriverManager testDriverManager;
	private ScenarioContext scenarioContext;

    @BeforeClass
    public void setup() {
    	testContext = PowerMockito.mock(TestContext.class);
    	testDriverManager = PowerMockito.mock(DriverManager.class);
    	scenarioContext = PowerMockito.mock(ScenarioContext.class);
    }
    
    @Test
    public void testGetTestDriverManager(){
    	PowerMockito.when(testContext.getDriverManager()).thenReturn(testDriverManager);
    	assertThat(testContext.getDriverManager(), instanceOf(DriverManager.class));
    }
    
    @Test
    public void testGetScenarioContext(){
    	PowerMockito.when(testContext.getScenarioContext()).thenReturn(scenarioContext);
    	assertThat(testContext.getScenarioContext(), IsInstanceOf.instanceOf(ScenarioContext.class));
    }
}
