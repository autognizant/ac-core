package com.autognizant.core.managers;

import org.testng.annotations.Test;

import com.autognizant.core.dataTypes.JsonWebElement;
import com.autognizant.core.util.Constants;
import com.autognizant.core.util.JsonDataReader;

import java.io.File;
import java.util.List;

import org.testng.Assert;

public class JsonDataReaderTest {

	@Test
	public void testGetJsonReader() {
		JsonDataReader jsonDataReader = new JsonDataReader();
		
		File jsonFile = new File(Constants.RESOURCES_PATH+"/ObjectRepository/LoginPage.json");
		List<JsonWebElement> jsonWebElements = jsonDataReader.getObjectRepositoryData(jsonFile);
		
		Assert.assertNotNull(jsonWebElements, "jsonWebElements Object is Null.");
		Assert.assertEquals(jsonWebElements.get(0).getWebElementName(), "Main_Header", "Web Element Name is not matched");
		Assert.assertEquals(jsonWebElements.get(1).getWebElementName(), "UserName", "Web Element Name is not matched");
		Assert.assertEquals(jsonWebElements.get(1).getWebElementType(), "TextBox", "Web Element Type is not matched");
		Assert.assertEquals(jsonWebElements.get(1).getFrameName(), "Main", "Frame Name is not matched");
		Assert.assertEquals(jsonWebElements.get(1).getEnglish().get("locatorType"), "id", "Locator Type is not matched");
		Assert.assertEquals(jsonWebElements.get(1).getEnglish().get("locatorValue"), "userName", "Locator Type is not matched");
	}
}
