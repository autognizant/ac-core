package com.autognizant.core.managers;

import org.testng.annotations.Test;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;

public class DriverManagerTest {

	private DriverManager driverManager;

	@BeforeClass
	public void beforeClass() {
		driverManager = new DriverManager();
	}

	@Test
	public void testGetDriver() {
		Assert.assertNotNull(driverManager.getAutognizantDriver(),"JsonDataReader Object is Null.");
		driverManager.getAutognizantDriver().openBrowser("https://google.co.in/");
		Assert.assertEquals(driverManager.getAutognizantDriver().getTitle(), "Google", "Browser Title is not matched");
	}

	@Test
	public void testQuitTestDriver() {
		driverManager.quitAutognizantDriver();
		//Assert.assertEquals(testDriverManager.getTestDriver().getDriver().toString(),"ChromeDriver: chrome on WINDOWS (null)","WebDriver Object is not Null.");
	}

}
