package com.autognizant.core.selenium;

import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ObjectRepositoryTest {

	private ObjectRepository objectRepository;

    @BeforeClass
    public void setup() {
    	objectRepository = new ObjectRepository();
    }

    @Test
    public void testGetFrameName(){
    	Assert.assertEquals(objectRepository.getFrameName("Main_Header"), "Main", "Frame Name doesn't match");
    }

    @Test
    public void testGetWebElement(){
    	Assert.assertNotNull(objectRepository.getWebElement("Main_Header"),"By object is null");
    	Assert.assertTrue(objectRepository.getWebElement("Main_Header") instanceof By,"By object is null");
    }
    
    @Test(expectedExceptions = {NoSuchElementException.class})
    public void testGetWebElementForInvalidWebElementName(){
    	objectRepository.getWebElement("Invalid");
    }
    
    @Test
    public void testGetWebElementWithDynamicText(){
    	Assert.assertNotNull(objectRepository.getWebElement("Dynamic Web Element1","Value1"),"By object is null");
    	Assert.assertTrue(objectRepository.getWebElement("Dynamic Web Element1","Value1") instanceof By,"By object is null");
    }
    
    @Test(expectedExceptions = {NoSuchElementException.class})
    public void testGetWebElementWithDynamicTextForInvalidWebElementName(){
    	objectRepository.getWebElement("Invalid","Value1");
    }
    
    @Test
    public void testGetWebElementWithMultipleDynamicText(){
    	Assert.assertNotNull(objectRepository.getWebElement("Dynamic Web Element2","Value1,Value2"),"By object is null");
    	Assert.assertTrue(objectRepository.getWebElement("Dynamic Web Element2","Value1,Value2") instanceof By,"By object is null");
    	Assert.assertNotNull(objectRepository.getWebElement("Dynamic Web Element3","Value1,Value2,Value3"),"By object is null");
    	Assert.assertTrue(objectRepository.getWebElement("Dynamic Web Element3","Value1,Value2,Value3") instanceof By,"By object is null");
    }
    
    @Test(expectedExceptions = {NoSuchElementException.class})
    public void testGetWebElementWithMultipleDynamicTextForInvalidWebElementName(){
    	objectRepository.getWebElement("Invalid","Value1,Value2");
    }
}
