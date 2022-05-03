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

package com.autognizant.core.selenium;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.MDC;

import com.autognizant.core.config.AutognizantConfig;
import com.autognizant.core.config.CoreConfig;
import com.autognizant.core.util.Constants;
import com.autognizant.core.util.Log;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Provides generic methods to communicate with the browser.
 * The methods contain core selenium webdriver commands.
 */
public class AutognizantDriver {
	private WebDriver driver=null;
	private JavascriptExecutor jsDriver=null;
	private WebDriverWait wait=null;
	private WebElement webElement=null;
	private ObjectRepository objectRepository;
	private final int  TIMEOUT_IN_SECONDS = AutognizantConfig.getElementWait();      
	private String currentFrame = "";
    final public Wait elementWait = this.new Wait();

	/**
	 * Default Constructor which creates ChromeDriver instance by default.
	 */
	public AutognizantDriver() {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(Duration.ZERO);			
			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_IN_SECONDS));
			jsDriver = (JavascriptExecutor)driver;
	}
	
	public void init() {
		if(CoreConfig.getExecutionType().equalsIgnoreCase("local")) {
		if(CoreConfig.getBrowserName().equalsIgnoreCase("Internet Explorer")){
			InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();		
			internetExplorerOptions.destructivelyEnsureCleanSession();
			internetExplorerOptions.requireWindowFocus();
			internetExplorerOptions.setAcceptInsecureCerts(false); // IE doesn't allow true value
			internetExplorerOptions.takeFullPageScreenshot();
			internetExplorerOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			internetExplorerOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT_AND_NOTIFY);
			if(CoreConfig.getExecutionMode().equalsIgnoreCase("HEADLESS")){
				throw new RuntimeException("Internet Explorer browser does not support headless execution !");
			}
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver(internetExplorerOptions);
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(Duration.ZERO);
			driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
			jsDriver = (JavascriptExecutor)driver;
			wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_IN_SECONDS));
			driver.manage().window().maximize();	
		}else if (CoreConfig.getBrowserName().equalsIgnoreCase("Google Chrome")){
			Map<String, Object> prefs = new Hashtable<String, Object>();
			prefs.put("download.prompt_for_download", "false");
			//prefs.put("download.default_directory", "Path of the default directory");
			//Turns off multiple download warning
			prefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1 );

			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("test-type");
			chromeOptions.addArguments("chrome.switches","--disable-extensions");
			chromeOptions.addArguments("--disable-print-preview");
			chromeOptions.addArguments("--disable-plugins");			
			chromeOptions.setExperimentalOption("prefs", prefs);
			if(CoreConfig.getExecutionMode().equalsIgnoreCase("HEADLESS")){
				System.out.println("Started execution in headless mode");
				chromeOptions.addArguments("--window-size=1920,1080");
				chromeOptions.addArguments("--disable-gpu");
				chromeOptions.addArguments("--disable-extensions");
				//This option is deprecated 
				//chromeOptions.setExperimentalOption("useAutomationExtension", false);
				chromeOptions.addArguments("--proxy-server='direct://'");
				chromeOptions.addArguments("--proxy-bypass-list=*");
				chromeOptions.addArguments("--start-maximized");
				chromeOptions.setHeadless(true);
			}
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver(chromeOptions);
			driver.manage().deleteAllCookies();
			wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_IN_SECONDS));
			driver.manage().window().maximize();
			jsDriver = (JavascriptExecutor)driver;
		}else if(CoreConfig.getBrowserName().equalsIgnoreCase("Mozilla Firefox")){
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.addPreference("browser.download.folderList",2);
			firefoxOptions.addPreference("browser.download.manager.showWhenStarting",false);
			firefoxOptions.addPreference("browser.helperApps.neverAsk.saveToDisk","text/csv, application/vnd.ms-excel, application/pdf, image/png, image/jpeg, text/html, text/plain, application/xml");
			firefoxOptions.addPreference("browser.helperApps.alwaysAsk.force", false);
			firefoxOptions.addPreference("browser.download.manager.alertOnEXEOpen", false);
			firefoxOptions.addPreference("browser.download.manager.focusWhenStarting", false);
			firefoxOptions.addPreference("browser.download.manager.useWindow", false);
			firefoxOptions.addPreference("browser.download.manager.showAlertOnComplete", false);
			firefoxOptions.addPreference("browser.download.manager.closeWhenDone", false);	
			// Set this to true to disable the pdf opening
			firefoxOptions.addPreference("pdfjs.disabled", true);
			firefoxOptions.addPreference("browser.download.manager.showWhenStartinge",false);
			firefoxOptions.addPreference("browser.download.panel.shown",false);
			firefoxOptions.addPreference("browser.download.useToolkitUI",true);
			
			firefoxOptions.setAcceptInsecureCerts(true);
			firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			firefoxOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT_AND_NOTIFY);
			
			firefoxOptions.addArguments("test-type");
			firefoxOptions.addArguments("chrome.switches","--disable-extensions");
			firefoxOptions.addArguments("--disable-print-preview");
			firefoxOptions.addArguments("--disable-plugins");	
			if(CoreConfig.getExecutionMode().equalsIgnoreCase("HEADLESS")){
				firefoxOptions.setHeadless(true);
			}
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver(firefoxOptions);
			wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_IN_SECONDS));
			driver.manage().window().maximize();
			jsDriver = (JavascriptExecutor)driver;
		}else if(CoreConfig.getBrowserName().equalsIgnoreCase("Microsoft Edge")){
			EdgeOptions edgeOptions = new EdgeOptions();		
			edgeOptions.setAcceptInsecureCerts(true);
			edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			edgeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT_AND_NOTIFY);
			if(CoreConfig.getExecutionMode().equalsIgnoreCase("HEADLESS")){
				edgeOptions.setHeadless(true);
			}
			WebDriverManager.edgedriver().setup();

			driver = new EdgeDriver(edgeOptions);
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(Duration.ZERO);
			driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
			jsDriver = (JavascriptExecutor)driver;
			wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_IN_SECONDS));
			driver.manage().window().maximize();
		}else if(CoreConfig.getBrowserName().equalsIgnoreCase("HtmlUnitDriver")){
			driver = new HtmlUnitDriver();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(Duration.ZERO);
			driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
			jsDriver = (JavascriptExecutor)driver;
			wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_IN_SECONDS));
			driver.manage().window().maximize();
		}
		}else if (CoreConfig.getExecutionType().equalsIgnoreCase("grid")) {
			if(CoreConfig.getBrowserName().equalsIgnoreCase("Internet Explorer")){
				InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
				try {
					driver = new RemoteWebDriver(new URL(CoreConfig.getGridURL()), internetExplorerOptions);
					Log.info("Execution started on Grid !");
				} catch (MalformedURLException e) {
					Log.error(e.getMessage(), e);
				}
			}else if (CoreConfig.getBrowserName().equalsIgnoreCase("Google Chrome")){
				ChromeOptions chromeOptions = new ChromeOptions();
				try {
					driver = new RemoteWebDriver(new URL(CoreConfig.getGridURL()), chromeOptions);
					Log.info("Execution started on Grid !");
				} catch (MalformedURLException e) {
					Log.error(e.getMessage(), e);
				}
			}else if(CoreConfig.getBrowserName().equalsIgnoreCase("Mozilla Firefox")){
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				try {
					driver = new RemoteWebDriver(new URL(CoreConfig.getGridURL()), firefoxOptions);
					Log.info("Execution started on Grid !");
				} catch (MalformedURLException e) {
					Log.error(e.getMessage(), e);
				}
			}else if(CoreConfig.getBrowserName().equalsIgnoreCase("Microsoft Edge")){
				EdgeOptions edgeOptions = new EdgeOptions();
				try {
					driver = new RemoteWebDriver(new URL(CoreConfig.getGridURL()), edgeOptions);
					Log.info("Execution started on Grid !");
				} catch (MalformedURLException e) {
					Log.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * Creates application log file.
	 */
	public void loggingSetup(String id){
		new File("target/logs").mkdirs();	
		try {
			new File(Constants.USER_DIR+"/target/logs/"+id).createNewFile();
			Log.info("Log file created for " + id);		
		} catch (IOException e) {
			e.printStackTrace();
		}

		MDC.put("logFileName", id);
		Log.info("Logging setup successful !");	
	}

	public void loadObjectRepository(){
		objectRepository = new ObjectRepository();
	}

	/**
	 * Parameterized Constructor which creates Internet Explorer instance.
	 * @param applicationType Type of the application like WEB, SERVICES, MOBILE
	 */
	public AutognizantDriver(String applicationType) {
		if (applicationType.equalsIgnoreCase("web")){
			init();
		}else if(applicationType.equalsIgnoreCase("services")){
			//System.out.println("do nothing");
			driver = new HtmlUnitDriver();
		}
	}

	/**
	 * Opens the web page for given URL.
	 * @param sURL URL of the web page to be opened.
	 */
	public void openBrowser(String sURL){
		deleteAllCookies();
		driver.get(sURL);	
		elementWait.waitForPageLoad();
		Log.info(sURL+ " URL opened in browser");
	}

	/**
	 * Navigates to the web page for given URL.
	 * @param sURL URL of the web page to be navigated.
	 */
	public void navigateToURL(String sURL){
		driver.navigate().to(sURL);
		elementWait.waitForPageLoad();
		Log.info("Navigated to "+sURL+ " in browser");
	}

	/**
	 * Refreshes the web page.
	 */
	public void refreshBrowser(){
		driver.navigate().refresh();
		Log.info("Browser refreshed");
	}

	/**
	 * Closes the browser.
	 * 
	 */ 
	public void closeBrowser(){
		String sTitle = driver.getTitle();
		driver.close();		
		Log.info(sTitle + " Window closed");
	}	 

	/**
	 * Closes all the browsers.
	 */   
	public void quitBrowser(){
		driver.quit();
		Log.info("Closed all browsers");
	}	

	/**
	 * Maximizes the browser.
	 */   
	public void maximizeBrowser(){
		driver.manage().window().maximize();
		Log.info("Browser window maximized");
	}	  	  

	/**
	 * Deletes all cookies
	 * 
	 */ 
	public void deleteAllCookies(){
		driver.manage().deleteAllCookies();
	}

	/**
	 * Gets the title of the browser.
	 * @return Returns the title of the browser.
	 */   
	public String getTitle(){
		Log.info("Title of the window is "+ driver.getTitle());
		return driver.getTitle();
	}	

	private void setWebElementProperties(String elementName) {
		this.elementWait.by = objectRepository.getWebElement(elementName);
		this.elementWait.elementName = elementName;
		this.elementWait.waitUntilElementIsPresent();
	}
	
	private void setDynamicWebElementProperties(String elementName, String dynamicText) {
		this.elementWait.by = objectRepository.getWebElement(elementName, dynamicText);
		this.elementWait.elementName = elementName;
		this.elementWait.waitUntilElementIsPresent();
	}
	
	/**
	 * Clicks the web element.
	 * @param elementName Element Name provided in the object repository.
	 * @throws Exception 
	 */ 
	public void clickElement(String elementName) throws Exception  {
		try {
			setWebElementProperties(elementName);
			driver.findElement(this.elementWait.by).click();
			Log.info("clicked on "+ elementName);
		} catch (Exception e) {
			Log.error("while clicking on "+ elementName,e);
			throwSeleniumException(e.getClass(), e.getMessage());
		}
	}	

	private <T extends Exception> void throwSeleniumException(Class<T> exceptionType,String message) throws Exception{
		throw exceptionType.getConstructor(String.class).newInstance(message);
	}

	/**
	 * Clicks the web element.
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @throws Exception 
	 */ 
	public void clickElement(String elementName, String dynamicText) throws Exception{
		try {
			setDynamicWebElementProperties(elementName, dynamicText);
			driver.findElement(this.elementWait.by).click();
			Log.info("clicked on "+ elementName);
		} catch (Exception e) {
			Log.error("while clicking on "+ elementName,e);
			throwSeleniumException(e.getClass(), e.getMessage());
		}
	}	

	/**
	 * Clicks the web element.
	 * @param elementName Element Name provided in the object repository.
	 * @throws Exception 
	 */ 
	public void submitForm(String elementName) throws Exception{
		try {
			setWebElementProperties(elementName);
			driver.findElement(this.elementWait.by).submit();
			Log.info("form submitted on "+ elementName);
		} catch (Exception e) {
			Log.error("while submitting form on "+ elementName,e);
			throwSeleniumException(e.getClass(), e.getMessage());
		}
	}	

	/**
	 * Clicks on web element using Javascript.
	 * This method can be used to click on web elements which are not visible but present on web page.
	 * @param elementName Element Name provided in the object repository.
	 */ 
	public void jsClickElement(String elementName) throws Exception{
		try{
			setWebElementProperties(elementName);
			jsDriver.executeScript("arguments[0].click();", driver.findElement(this.elementWait.by));
			Log.info("jsclicked on " + elementName);
		}catch(Exception e){
			Log.error("while jsclicking on "+ elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());
		}
	}

	/**
	 * Clicks on web element using Javascript.
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @throws Exception 
	 */ 
	public void jsClickElement(String elementName, String dynamicText) throws Exception{
		try{
			setDynamicWebElementProperties(elementName, dynamicText);
			jsDriver.executeScript("arguments[0].click();", driver.findElement(this.elementWait.by));
			Log.info("jsclicked on " + elementName);
		}catch(Exception e){
			Log.error("while jsclicking on "+ elementName, e);
			if(!e.getMessage().contains("unknown error: unhandled inspector error"))
				throwSeleniumException(e.getClass(), e.getMessage());
		}
	}	
	
	/**
	 * Sends Keys on web element using Javascript.
	 * This method can be used to click on web elements which are not visible but present on web page.
	 * @param elementName Element Name provided in the object repository.
	 * @param keysToSend Name of the Keys to be sent on Given web element
	 */ 
	public void sendKeys(String elementName, CharSequence keysToSend) throws Exception{
		try{
			setWebElementProperties(elementName);
			driver.findElement(this.elementWait.by).sendKeys(keysToSend);
			Log.info("sendkeys "+keysToSend+" on " + elementName);
		}catch(Exception e){
			Log.error("while sendkeys on "+ elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());
		}
	}

	/**
	 * Sends Keys on web element using Javascript.
	 * This method can be used to click on web elements which are not visible but present on web page.
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @param keysToSend Name of the Keys to be sent on Given web element
	 */ 
	public void sendKeys(String elementName, String dynamicText, CharSequence keysToSend) throws Exception{
		try{
			setDynamicWebElementProperties(elementName, dynamicText);
			driver.findElement(this.elementWait.by).sendKeys(keysToSend);
			Log.info("sendkeys "+keysToSend+" on " + elementName);
		}catch(Exception e){
			Log.error("while sendkeys on "+ elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());
		}
	} 

	/**
	 * Performs double click on the web element.
	 * @param elementName Element Name provided in the object repository.
	 * @throws Exception 
	 */ 
	public void doubleClickElement(String elementName) throws Exception{
		try {
			setWebElementProperties(elementName);
			webElement = driver.findElement(this.elementWait.by);
			Actions actions = new Actions(driver);
			actions.doubleClick(webElement).build().perform();
			Log.info("double clicked on "+ elementName);
		} catch (Exception e) {
			Log.error("while double clicking on "+ elementName,e);
			throwSeleniumException(e.getClass(), e.getMessage());
		}
	}

	/**
	 * Performs double click on the web element.
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @throws Exception 
	 */ 
	public void doubleClickElement(String elementName, String dynamicText) throws Exception{
		try {
			setDynamicWebElementProperties(elementName, dynamicText);
			webElement = driver.findElement(this.elementWait.by);
			Actions actions = new Actions(driver);
			actions.doubleClick(webElement).build().perform();
			Log.info("double clicked on "+ elementName);
		} catch (Exception e) {
			Log.error("while double clicking on "+ elementName,e);
			throwSeleniumException(e.getClass(), e.getMessage());
		}
	}	
	
	/**
	 * Executes javaScript on given Web Element.
	 * @param elementName Element Name provided in the object repository.
	 * @throws Exception Throws Exception 
	 */ 
	public void executeJavaScript(String elementName) throws Exception{
		try {
			switchToFrame(elementName);
			jsDriver.executeScript(objectRepository.getWebElementJavaScript(elementName));
			Log.info("Java script executed on "+ elementName);
		} catch (Exception e) {
			Log.error("while executing Java script on ",e);
			throwSeleniumException(e.getClass(), e.getMessage());
		}
	}	

	/**
	 * Executes javaScript on given Web Element.
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @throws Exception Throws Exception 
	 */ 
	public void executeJavaScript(String elementName, String dynamicText) throws Exception{
		try {
			switchToFrame(elementName);
			jsDriver.executeScript(objectRepository.getWebElementJavaScript(elementName,dynamicText));
			Log.info("Java script executed on "+ elementName);
		} catch (Exception e) {
			Log.error("while executing Java script on ",e);
			throwSeleniumException(e.getClass(), e.getMessage());
		}
	}	

	/**
	 * Performs right click on the web element.
	 * @param elementName Element Name provided in the object repository.
	 * @throws Exception 
	 */ 
	public void rightClickElement(String elementName) throws Exception{
		try {
			setWebElementProperties(elementName);
			webElement = driver.findElement(this.elementWait.by);
			Actions actions = new Actions(driver);
			actions.contextClick(webElement).build().perform();
			Log.info("right clicked on "+ elementName);
		} catch (Exception e) {
			Log.error("while right clicking on "+ elementName,e);
			throwSeleniumException(e.getClass(), e.getMessage());
		}
	}

	/**
	 * Gets the list of elements on web page.
	 * @param elementName Element Name provided in the object repository.
	 * @return Returns the list elements if found otherwise null
	 */ 
	public List<WebElement> findElements(String elementName){
		try {		
			setWebElementProperties(elementName);
			return driver.findElements(this.elementWait.by);
		} catch (Exception e) {
			Log.error(elementName + " is not found",e);	
		}
		return null;
	}	  

	/**
	 * Gets the list of elements on web page.
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @return Returns the list elements if found otherwise null
	 */ 
	public List<WebElement> findElements(String elementName,String dynamicText){
		try {		
			setDynamicWebElementProperties(elementName, dynamicText);
			return driver.findElements(this.elementWait.by);
		} catch (Exception e) {
			Log.error(elementName + " is not found",e);	
		}
		return null;
	}	 

	/**
	 * Gets web element value .
	 * @param elementName Element Name provided in the object repository.
	 * @return Returns the value of the web element.
	 */ 
	public String getElementText(String elementName){
		try{
			setWebElementProperties(elementName);
			String text = driver.findElement(this.elementWait.by).getText();
			Log.info(elementName +" = " + text);
			return text;
		}catch(Exception e){
			Log.error("While getting Text", e);
			return "";
		}
	}

	/**
	 * Gets web element value .
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @return Returns the value of the web element.
	 */ 
	public String getElementText(String elementName,String dynamicText){
		try{
			setDynamicWebElementProperties(elementName, dynamicText);
			String text = driver.findElement(this.elementWait.by).getText();
			Log.info(elementName +" = " + text);
			return text;
		}catch(Exception e){
			Log.error("While getting Text", e);
			return "";
		}
	}

	/**
	 * Gets the web element's attribute value. 
	 * @param elementName Element Name provided in the object repository.
	 * @param attributeName The attribute name such as id, name, class, value for which value needs to be retrieved.
	 * @return Returns the web element's attribute value. 
	 */ 
	public String getElementAttributeValue(String elementName,String attributeName){
		setWebElementProperties(elementName);
		String attributeValue = driver.findElement(this.elementWait.by).getAttribute(attributeName);
		Log.info("("+elementName+")'s "+attributeName+" value is " +attributeValue);
		return driver.findElement(objectRepository.getWebElement(elementName)).getAttribute(attributeName);
	}

	/**
	 * Verifies whether or not element is displayed
	 * @param elementName Element Name provided in the object repository.
	 * @return Returns true if element is found otherwise false.
	 */ 
	public boolean isElementDisplayed(String elementName){
		try {
			setWebElementProperties(elementName);
			if (driver.findElement(this.elementWait.by).isDisplayed()) {
				Log.info(elementName + " is displayed");
				return true;
			}else {
				Log.info(elementName + " is not displayed");
				return false;
			}
		} catch (Exception e) {
			Log.error(elementName + " is not displayed",e);	
		}
		return false;
	}	  

	/**
	 * Verifies whether or not element is displayed.
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements.
	 * @return Returns true if element is found otherwise false.
	 */ 
	public boolean isElementDisplayed(String elementName, String dynamicText){
		try {
			setDynamicWebElementProperties(elementName, dynamicText);
			if (driver.findElement(this.elementWait.by).isDisplayed()) {
				Log.info(elementName + " is displayed");
				return true;
			}else {
				Log.info(elementName + " is not displayed");
				return false;
			}
		} catch (Exception e) {
			Log.error(elementName + " is not displayed",e);	
		}
		return false;
	}	

	/**
	 * Verifies whether or not element is enabled
	 * @param elementName Element Name provided in the object repository.
	 * @return Returns true if element is found otherwise false.
	 */ 
	public boolean isElementEnabled(String elementName){
		try {
			setWebElementProperties(elementName);
			if (driver.findElement(this.elementWait.by).isEnabled()) {
				Log.info(elementName + " is enabled");
				return true;
			}else {
				Log.info(elementName + " is not enabled");
				return false;
			}
		} catch (Exception e) {
			Log.error(elementName + " is not enabled",e);	
		}
		return false;
	}

	/**
	 * Verifies whether or not element is enabled
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements.
	 * @return Returns true if element is found otherwise false.
	 */ 
	public boolean isElementEnabled(String elementName, String dynamicText){
		try {
			setDynamicWebElementProperties(elementName, dynamicText);			
			if (driver.findElement(this.elementWait.by).isEnabled()) {
				Log.info(elementName + " is enabled");
				return true;
			}else {
				Log.info(elementName + " is not enabled");
				return false;
			}
		} catch (Exception e) {
			Log.error(elementName + " is not enabled",e);	
		}
		return false;
	}	
	
	/**
	 * Verifies whether or not element is selected
	 * @param elementName Element Name provided in the object repository.
	 * @return Returns true if element is found otherwise false.
	 */ 
	public boolean isElementSelected(String elementName){
		try {
			setWebElementProperties(elementName);
			if (driver.findElement(this.elementWait.by).isSelected()) {
				Log.info(elementName + " is selected");	
				return true;
			}else {
				Log.info(elementName + " is not selected");	
				return false;
			}
		} catch (Exception e) {
			Log.error(elementName + " is not selected",e);	
		}
		return false;
	}
	
	/**
	 * Verifies whether or not element is selected
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements.
	 * @return Returns true if element is found otherwise false.
	 */ 
	public boolean isElementSelected(String elementName, String dynamicText){
		try {
			setDynamicWebElementProperties(elementName, dynamicText);
			if (driver.findElement(this.elementWait.by).isSelected()) {
				Log.info(elementName + " is selected");	
				return true;
			}else {
				Log.info(elementName + " is not selected");	
				return false;
			}
		} catch (Exception e) {
			Log.error(elementName + " is not selected",e);	
		}
		return false;
	}	
	
	/**
	 * Enters text in Text Field
	 * @param elementName Element Name provided in the object repository.
	 * @param text Text to be entered in Text Field
	 */ 
	public void enterText(String elementName, String text){
		try {
			setWebElementProperties(elementName);
			driver.findElement(this.elementWait.by).clear();
			driver.findElement(this.elementWait.by).sendKeys(text);  	
			Log.info(text +" entered in "+ elementName);
		} catch (Exception e) {
			Log.error("while entering "+text+" into "+elementName, e);
		}
	}

	/**
	 * Enters text in Text Field
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements.
	 * @param text Text to be entered in Text Field
	 */ 
	public void enterText(String elementName, String dynamicText, String text){
		try {
			setDynamicWebElementProperties(elementName, dynamicText);
			driver.findElement(this.elementWait.by).clear();
			driver.findElement(this.elementWait.by).sendKeys(text);    
			Log.info(text +" entered in "+ elementName);
		} catch (Exception e) {
			Log.error("while entering "+text+" into "+elementName, e);
		}
	}

	/**
	 * Accepts alert.
	 * @throws Exception 
	 */ 
	public void acceptAlert() throws Exception{
		try{
			Log.info("Accept Alert Message = " + driver.switchTo().alert().getText());
			driver.switchTo().alert().accept();
		}catch(Exception e){
			Log.error("Alert is not present", e);
			throwSeleniumException(e.getClass(), e.getMessage());			
		}
	}  

	/**
	 * Returns alert Text.
	 * @return  Returns alert Text.
	 * @throws Exception 
	 */ 
	public String getAlertText() throws Exception{
		try{
			Log.info("Alert Message Text= " + driver.switchTo().alert().getText());
			return driver.switchTo().alert().getText();
		}catch(Exception e){
			Log.error("Alert is not present", e);
			throwSeleniumException(e.getClass(), e.getMessage());	
			return null;
		}
	}  

	/**
	 * Dismisses alert.
	 * @throws Exception 
	 */ 
	public void dismissAlert() throws Exception{
		try{
			Log.info("Dismiss Alert Message = " + driver.switchTo().alert().getText());
			driver.switchTo().alert().dismiss();
		}catch(Exception e){
			Log.error("Alert is not present", e);
			throwSeleniumException(e.getClass(), e.getMessage());			
		}
	} 

	/**
	 * Selects the item in select list by providing visible text in DOM 
	 * @param elementName Element Name provided in the object repository.
	 * @param visibleText visible text in option tag in DOM
	 * @throws Exception 
	 */  
	public void selectOptionByText(String elementName, String visibleText) throws Exception{
		try {
			setWebElementProperties(elementName);
			Select item;			
			item = new Select(driver.findElement(this.elementWait.by));
			item.selectByVisibleText(visibleText);
			Log.info(visibleText +" selected in "+ elementName);
		} catch (Exception e) {
			Log.error("while selecting "+visibleText+" present in "+elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());			
		}
	}	 	

	/**
	 * Selects the item in select list by providing visible text in DOM 
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @param visibleText visible text in option tag in DOM
	 * @throws Exception 
	 */ 
	public void selectOptionByText(String elementName, String dynamicText, String visibleText) throws Exception{
		try {
			setDynamicWebElementProperties(elementName, dynamicText);
			Select item;            
			item = new Select(driver.findElement(this.elementWait.by));
			item.selectByVisibleText(visibleText);
			Log.info(visibleText +" selected in "+ elementName);
		} catch (Exception e) {
			Log.error("while selecting "+visibleText+" present in "+elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());           
		}
	}

	/**
	 * Selects the item in select list by providing option Value in DOM 
	 * @param elementName Element Name provided in the object repository.
	 * @param optionValue option Value in option tag in DOM
	 * @throws Exception 
	 */  
	public void selectOptionByValue(String elementName, String optionValue) throws Exception{
		try {
			setWebElementProperties(elementName);
			Select item;			
			item = new Select(driver.findElement(this.elementWait.by));
			item.selectByValue(optionValue);
			Log.info(optionValue +" selected in "+ elementName);
		} catch (Exception e) {
			Log.error("while selecting "+optionValue+" present in "+elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());			
		}
	}	 	

	/**
	 * Selects the item in select list by providing option Value in DOM 
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @param optionValue option Value in option tag in DOM
	 * @throws Exception 
	 */ 
	public void selectOptionByValue(String elementName, String dynamicText, String optionValue) throws Exception{
		try {
			setDynamicWebElementProperties(elementName, dynamicText);
			Select item;            
			item = new Select(driver.findElement(this.elementWait.by));
			item.selectByValue(optionValue);
			Log.info(optionValue +" selected in "+ elementName);
		} catch (Exception e) {
			Log.error("while selecting "+optionValue+" present in "+elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());           
		}
	}
	
	/**
	 * Selects the item in select list by providing index value in DOM 
	 * @param elementName Element Name provided in the object repository.
	 * @param index index Value in option tag in DOM
	 * @throws Exception 
	 */  
	public void selectOptionByIndex(String elementName, int index) throws Exception{
		try {
			setWebElementProperties(elementName);
			Select item;			
			item = new Select(driver.findElement(this.elementWait.by));
			item.selectByIndex(index);
			Log.info(index +" selected in "+ elementName);
		} catch (Exception e) {
			Log.error("while selecting "+index+" present in "+elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());			
		}
	}	 	

	/**
	 * Selects the item in select list by providing index value in DOM 
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @param index index Value in option tag in DOM
	 * @throws Exception 
	 */ 
	public void selectOptionByIndex(String elementName, String dynamicText, int index) throws Exception{
		try {
			setDynamicWebElementProperties(elementName, dynamicText);
			Select item;            
			item = new Select(driver.findElement(this.elementWait.by));
			item.selectByIndex(index);
			Log.info(index +" selected in "+ elementName);
		} catch (Exception e) {
			Log.error("while selecting "+index+" present in "+elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());           
		}
	}
	
	/**
	 * Deelects the item in select list by providing visible text in DOM 
	 * @param elementName Element Name provided in the object repository.
	 * @param visibleText visible text in option tag in DOM
	 * @throws Exception 
	 */  
	public void deselectOptionByText(String elementName, String visibleText) throws Exception{
		try {
			setWebElementProperties(elementName);
			Select item;			
			item = new Select(driver.findElement(this.elementWait.by));
			item.deselectByVisibleText(visibleText);
			Log.info(visibleText +" deselected in "+ elementName);
		} catch (Exception e) {
			Log.error("while deselecting "+visibleText+" present in "+elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());			
		}
	}	 	

	/**
	 * Deselects the item in select list by providing visible text in DOM 
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @param visibleText visible text in option tag in DOM
	 * @throws Exception 
	 */ 
	public void deselectOptionByText(String elementName, String dynamicText, String visibleText) throws Exception{
		try {
			setDynamicWebElementProperties(elementName, dynamicText);
			Select item;            
			item = new Select(driver.findElement(this.elementWait.by));
			item.deselectByVisibleText(visibleText);
			Log.info(visibleText +" deselected in "+ elementName);
		} catch (Exception e) {
			Log.error("while deselecting "+visibleText+" present in "+elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());           
		}
	}

	/**
	 * Deselects the item in select list by providing option Value in DOM 
	 * @param elementName Element Name provided in the object repository.
	 * @param optionValue option Value in option tag in DOM
	 * @throws Exception 
	 */  
	public void deselectOptionByValue(String elementName, String optionValue) throws Exception{
		try {
			setWebElementProperties(elementName);
			Select item;			
			item = new Select(driver.findElement(this.elementWait.by));
			item.deselectByValue(optionValue);
			Log.info(optionValue +" deselected in "+ elementName);
		} catch (Exception e) {
			Log.error("while deselecting "+optionValue+" present in "+elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());			
		}
	}	 	

	/**
	 * Deselects the item in select list by providing option Value in DOM 
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @param optionValue option Value in option tag in DOM
	 * @throws Exception 
	 */ 
	public void deselectOptionByValue(String elementName, String dynamicText, String optionValue) throws Exception{
		try {
			setDynamicWebElementProperties(elementName, dynamicText);
			Select item;            
			item = new Select(driver.findElement(this.elementWait.by));
			item.deselectByValue(optionValue);
			Log.info(optionValue +" deselected in "+ elementName);
		} catch (Exception e) {
			Log.error("while deselecting "+optionValue+" present in "+elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());           
		}
	}
	
	/**
	 * Deselects the item in select list by providing index value in DOM 
	 * @param elementName Element Name provided in the object repository.
	 * @param index index Value in option tag in DOM
	 * @throws Exception 
	 */  
	public void deselectOptionByIndex(String elementName, int index) throws Exception{
		try {
			setWebElementProperties(elementName);
			Select item;			
			item = new Select(driver.findElement(this.elementWait.by));
			item.deselectByIndex(index);
			Log.info(index +" deselected in "+ elementName);
		} catch (Exception e) {
			Log.error("while deselecting "+index+" present in "+elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());			
		}
	}	 	

	/**
	 * Deselects the item in select list by providing index value in DOM 
	 * @param elementName Element Name provided in the object repository.
	 * @param dynamicText Dynamic text to be provided for identifying dynamic web elements. 
	 * @param index index Value in option tag in DOM
	 * @throws Exception 
	 */ 
	public void deselectOptionByIndex(String elementName, String dynamicText, int index) throws Exception{
		try {
			setDynamicWebElementProperties(elementName, dynamicText);
			Select item;            
			item = new Select(driver.findElement(this.elementWait.by));
			item.deselectByIndex(index);
			Log.info(index +" deselected in "+ elementName);
		} catch (Exception e) {
			Log.error("while deselecting "+index+" present in "+elementName, e);
			throwSeleniumException(e.getClass(), e.getMessage());           
		}
	}
	
	/**
	 * Gets the item in select list.
	 * @param elementName Element Name provided in the object repository..
	 * @return Returns the selected item in the select list.
	 */ 
	public String getSelectedItem (String elementName){
		try {
			setWebElementProperties(elementName);
			Select item;
			item = new Select(driver.findElement(this.elementWait.by));
			Log.info("Selected option in "+elementName+" is "+item.getFirstSelectedOption().getText());
			return item.getFirstSelectedOption().getText();
		} catch (Exception e) {
			Log.error("while getting selected option present in "+elementName, e);
		}
		return null;
	}

	public void waitForWindow(String sTitle) throws InterruptedException {
		//wait until number of window handles become 2 or until 6 seconds are completed. 
		int timecount = 1; 
		do {
			driver.getWindowHandles(); 
			Thread.sleep(200);
			timecount++; 
			if (timecount > TIMEOUT_IN_SECONDS) 
			{ break; } 
		} while (driver.getWindowHandles().size() != 2);
	}

	public void switchToMainWindow() throws Exception { 
		String mainWindow = AutognizantConfig.getAppWindowTitle();
		waitForWindow(mainWindow);
		int timecount = 1; 
		do {
			try{
				for (String window : driver.getWindowHandles()) { 
					if (driver.switchTo().window(window).getTitle().equals(mainWindow)) { 
						driver.switchTo().window(window); 
						Log.info(mainWindow+" Main window is found");
						break;
					}
				}
			}catch (NoSuchWindowException e) {
				Log.warn("Main window is not found after "+timecount+" seconds");
			}
			Thread.sleep(1000);
			timecount++; 
			if (timecount > TIMEOUT_IN_SECONDS) {
				Log.error("while switching on "+mainWindow+" window", new NoSuchWindowException(mainWindow));
				throw new NoSuchWindowException(mainWindow);
			} 
		} while (!driver.getTitle().equals(mainWindow));
		currentFrame = "";
	}

	/**
	 * Switches to window.
	 * @param sTitle Title of the window.
	 */
	public void switchToWindow(String sTitle) throws Exception { 
		waitForWindow(sTitle);
		int timecount = 1; 
		boolean bFlag = false;
		do {
			try{
				for (String window : driver.getWindowHandles()) {
					if (driver.switchTo().window(window).getTitle().contains(sTitle)) {
						driver.switchTo().window(window); 
						Log.info(sTitle +" Window found");
						bFlag = true;
						break;
					}
				}
			}catch (Exception e) {
				Log.error("Ignoring NosuchWindow Exception", e);
			}
			Thread.sleep(1000);
			timecount++; 
			if (timecount > TIMEOUT_IN_SECONDS) { 
				Log.error("while switching on "+sTitle+" Window", new NoSuchWindowException(sTitle));
				throw new NoSuchWindowException(sTitle); 
			} 
		} while (bFlag == false);
		currentFrame = "";
	}

	/**
	 * Switches to frame on web page..
	 * @param elementName Element Name provided in the object repository.
	 */ 
	public void switchToFrame(String elementName){
		String sFrameName = objectRepository.getFrameName(elementName);
		if(sFrameName.equals("NoFrame") && currentFrame.equals("NoFrame")){
			//Log.info("No need to switch in Main Frame");
		}else if (sFrameName.equals("Main") && !(currentFrame.equals("Main"))){
			//Log.info("Need to switch in Main Frame as Current Frame is different");
			Log.info("driver is switched to defaultContent !");
			switchToDefaultContent();
		}else if(!sFrameName.equals(currentFrame)){
			switchToDefaultContent();
			String[] sFrames = sFrameName.split("->");
			for (String string : sFrames) {
				try{
					wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(By.name(string)),ExpectedConditions.presenceOfElementLocated(By.id(string))));
					driver.switchTo().frame(string);
					Log.info("driver is switched to frame " + string);
				}catch (Exception e) {
					Log.error(string+" frame is not loaded after waiting for "+TIMEOUT_IN_SECONDS+" seconds.",e);
					throw new NoSuchFrameException(e.getMessage());
				}
			}
		}else{
			//Log.info("No need to switch in Current Frame");
		}
		currentFrame = sFrameName;
	}

	public WebDriver getDriver(){
		return driver;
	}

	/**
	 * Switches to default content of the web page.
	 */ 
	public void switchToDefaultContent(){
		driver.switchTo().defaultContent();		
	}

	/**
	 * Captures screenshot of web page.
	 * @return Returns file object
	 */ 
	public File captureScreenshot(){
		File oFile = null;
		try{
			if(driver instanceof HtmlUnitDriver){
				Log.warn("HtmlUnitDriver doesn't support Screenshot !");
			}else{
				oFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				driver.getTitle();
				Log.info("Screenshot captured successfully");
			}
			return oFile;
		}catch (Exception e) {
			Log.error("while capturing screenshot", e);
			try{
				oFile =   new File(System.getProperty("java.io.tmpdir")+"temp.jpeg");
				if (oFile.exists()){
					oFile.delete();
				}
				oFile.createNewFile();
				BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				ImageIO.write(image, "png", oFile);
				try{
					driver.switchTo().alert().accept();
				}catch (Exception e2) {
					Log.info("Catched Exception = " + e2.getMessage());
				}
				Log.info("Screenshot captured successfully");
				return oFile;
			}catch (Exception e1) {
				Log.error("while capturing screenshot using Robot", e1);
			}
		}
		return null;
	}

    public class Wait {
        private By by;
        private String elementName;
        
    	/**
    	 * Sets implicit wait value as default timeout value.	
    	 */ 
    	public void implicitlyWait(){
    		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT_IN_SECONDS));
    	}	 

    	/**
    	 * Sets implicit wait	value as given timeout value.	
    	 * @param iTimeInSeconds Timeout value in seconds	   * 
    	 */ 
    	public void implicitlyWait(int iTimeInSeconds){
    		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(iTimeInSeconds));
    	}
    	
        /**
         * Most of the public wait methods call this method to perform the actual wait
         * @param condition An ExpectedCondition object
         */
        private void waitForCondition(ExpectedCondition<?> condition, String elementName) {
            try {
    			switchToFrame(elementName);			
                // Now wait for condition
                wait.ignoring(TimeoutException.class, NoSuchElementException.class)
                                                       .until(condition);
    			Log.info(elementName+" is found");
            } catch (Exception e) {
    			Log.error(elementName+" is not found after waiting for "+TIMEOUT_IN_SECONDS+" seconds.",e);
            }
        }
        
        /**
         * Most of the public fluent wait methods call this method to perform the actual wait
         * @param condition An ExpectedCondition object
         */
        private void fluentWaitForCondition(ExpectedCondition<?> condition, String elementName) {
            try {
                // Now wait for condition
                FluentWait<WebDriver> fluentWait = new FluentWait<WebDriver>(getDriver())
                        .withTimeout(Duration.ofSeconds(TIMEOUT_IN_SECONDS))
                        .pollingEvery(Duration.ofMillis(500))
                        .ignoring(
                                NoSuchElementException.class,
                                TimeoutException.class);
                
    			switchToFrame(elementName);			
            	fluentWait.until(condition);
    			Log.info(elementName+" is found");
            } catch (Exception e) {
    			Log.error(elementName+" is not found after fluent waiting for "+TIMEOUT_IN_SECONDS+" seconds.",e);
            }
        }
        
        /**
         * Waits until element is clickable.
         */
        public void waitUntilElementIsClickable(String elementName) {
            By by = objectRepository.getWebElement(elementName);
            ExpectedCondition<WebElement> condition = ExpectedConditions.elementToBeClickable(by);
            waitForCondition(condition, elementName);
        }
        
        /**
         * Waits until element is clickable.
         */
        public void waitUntilElementIsClickable(String elementName, String dynamicText) {
            By by = objectRepository.getWebElement(elementName, dynamicText);
            ExpectedCondition<WebElement> condition = ExpectedConditions.elementToBeClickable(by);
            waitForCondition(condition, elementName);
        }
        
        /**
         * Waits for the element to be not visible or missing in the DOM.
         */
        public void waitUntilElementIsInvisible(String elementName) {
            By by = objectRepository.getWebElement(elementName);
            ExpectedCondition<Boolean> condition = ExpectedConditions.invisibilityOfElementLocated(by);
            fluentWaitForCondition(condition,elementName);
        }
        
        /**
         * Waits for the element to be not visible or missing in the DOM.
         */
        public void waitUntilElementIsInvisible(String elementName, String dynamicText) {
            By by = objectRepository.getWebElement(elementName, dynamicText);
            ExpectedCondition<Boolean> condition = ExpectedConditions.invisibilityOfElementLocated(by);
            fluentWaitForCondition(condition,elementName);
        }
        
        /**
         * Waits until element is present on the DOM of a page.
         */
        public void waitUntilElementIsPresent(String elementName) {
            By by = objectRepository.getWebElement(elementName);
            ExpectedCondition<WebElement> condition = ExpectedConditions.presenceOfElementLocated(by);
            waitForCondition(condition, elementName);
        }
        
        /**
         * Waits until element is present on the DOM of a page.
         */
        public void waitUntilElementIsPresent(String elementName, String dynamicText) {
            By by = objectRepository.getWebElement(elementName, dynamicText);
            ExpectedCondition<WebElement> condition = ExpectedConditions.presenceOfElementLocated(by);
            waitForCondition(condition, elementName);
        }
        
        /**
         * Waits until element is present on the DOM of a page.
         */
        public void waitUntilElementIsPresent() {
            ExpectedCondition<WebElement> condition = ExpectedConditions.presenceOfElementLocated(this.by);
            waitForCondition(condition, this.elementName);
        }
        
        /**
         * Waits until element is present on the DOM of a page and visible.
         */
        public void waitUntilElementIsVisible(String elementName) {
            By by = objectRepository.getWebElement(elementName);
            ExpectedCondition<WebElement> condition = ExpectedConditions.visibilityOfElementLocated(by);
            waitForCondition(condition, elementName);
        }
        
        /**
         * Waits until element is present on the DOM of a page and visible.
         */
        public void waitUntilElementIsVisible(String elementName, String dynamicText) {
            By by = objectRepository.getWebElement(elementName, dynamicText);
            ExpectedCondition<WebElement> condition = ExpectedConditions.visibilityOfElementLocated(by);
            waitForCondition(condition, elementName);
        }
        
    	/**
    	 * Waits for alert to appear.
    	 */ 
    	public void waitForAlert(){
    		try { 	
    			wait.until(ExpectedConditions.alertIsPresent()); 	
    		} catch (Exception e) {
    			Log.error("Alert is not found after waiting for "+TIMEOUT_IN_SECONDS+" seconds.",e);
    		}
    	}
    	
    	/**
    	 * Waits for web page to load.
    	 */ 
    	public void waitForPageLoad() {
    		ExpectedCondition<Boolean> expectation = new
    				ExpectedCondition<Boolean>() {
    			public Boolean apply(WebDriver driver) {
    				Log.info("wait for page load status = " + jsDriver.executeScript("return document.readyState"));
    				return jsDriver.executeScript("return document.readyState").equals("complete");
    			}
    		};
    		try {
    			wait.until(expectation);
    		}catch (UnsupportedOperationException e) {
				Log.warn("java.lang.UnsupportedOperationException: Javascript is not enabled for this HtmlUnitDriver instance");
			} catch(Exception e) {
    			Log.error("Timeout waiting for Page Load Request to complete.", e);
    		}
    	}
    	
    	/**
    	 * Waits for ajax elements to load.
    	 */ 
    	public void waitForAjaxElementsToLoad() {
    		try {
    			Log.info("Checking active ajax calls by calling jquery.active");	
    			if (driver instanceof JavascriptExecutor) {
    				JavascriptExecutor jsDriver = (JavascriptExecutor)driver;
    				for (int i = 0; i< TIMEOUT_IN_SECONDS; i++) 
    				{
    					Object numberOfAjaxConnections = jsDriver.executeScript("return jQuery.active");
    					if (numberOfAjaxConnections instanceof Long) {
    						Long n = (Long)numberOfAjaxConnections;
    						Log.info("Number of active jquery ajax calls: " + n);
    						if (n.longValue() == 0L)
    							break;
    					}
    					Thread.sleep(1000);
    				}
    			}
    			else {
    				Log.info("Web driver: " + driver + " cannot execute javascript");
    			}
    		}
    		catch (Exception e) {
    			Log.error("while waiting for Ajax components " + e,e);
    		}
    	}
    }
}
