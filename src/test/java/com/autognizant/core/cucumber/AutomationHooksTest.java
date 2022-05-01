package com.autognizant.core.cucumber;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.cucumber.java.Scenario;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AutomationHooks.class)
public class AutomationHooksTest {

	private AutomationHooks automationHooks;
	private Scenario scenario;
	private TestContext testContext;

    @BeforeClass
    public void setup() {
    	scenario = PowerMockito.mock(Scenario.class);
    	testContext = new TestContext();
    	PowerMockito.when(scenario.getName()).thenReturn("Sample Test Scenario");
    	PowerMockito.when(scenario.getId()).thenReturn("Sample Test Scenario ID");
    	PowerMockito.when(scenario.getSourceTagNames()).thenReturn(Arrays.asList(new String[] {"@ECAUTO-Scenario1","@smoke","@samplefeature"}));
    	PowerMockito.when(scenario.isFailed()).thenReturn(true);
    	automationHooks = new AutomationHooks(testContext);
    }
	
	@Test
	public void testBeforeScenario() throws Exception {
		automationHooks.beforeScenario(scenario);
		Assert.assertTrue(automationHooks.scenarioUniqueID.equals("@ECAUTO-Scenario1"), "Scenario ID is not captured !");
		Assert.assertTrue(automationHooks.scenarioName.equals("Sample Test Scenario"), "Scenario Name is not captured !");
		Assert.assertTrue(automationHooks.logFile.equals("@ECAUTO-Scenario1-Sample Test Scenario ID.log"), "Scenario LogFile is not captured !");
	}
	
	@Test(dependsOnMethods = {"testBeforeScenario"})
	public void testAfterScenario() throws Exception {
		automationHooks.afterScenario(scenario);
		//Assert.assertEquals(testContext.getTestDriverManager().getTestDriver().getDriver().toString(),"ChromeDriver: chrome on WINDOWS (null)","WebDriver Object is not Null.");
	}
}
