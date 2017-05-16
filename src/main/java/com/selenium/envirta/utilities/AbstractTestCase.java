package com.selenium.envirta.utilities;

import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.selenium.envirta.utilities.reporters.ExtendedLogger;

@Test
public abstract class AbstractTestCase {
	
	private User reportedUser;
	
	protected REGUser regUser;
	protected Environment testEnvironment;
	protected String spreadsheetAttribute;
	protected Browser webBrowser;
	protected WebDriver driving;
	protected BasicController superController;
	
	
	
	public AbstractTestCase(Browser browser, String userName, Environment enviro, String SpreadsheetName) throws IOException{
		
		webBrowser = browser;
		testEnvironment = enviro;
		spreadsheetAttribute = SpreadsheetName;
		
		regUser = createREGUserObject(userName);
		reportedUser  = regUser;
		
	}
	private REGUser createREGUserObject(String userPropName) throws IOException {
		// TODO Auto-generated method stub
		
       Properties userProps = new Properties();
		
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\com\\selenium\\envirta\\users\\" + userPropName);
		userProps.load(fis);
		return new REGUser(userProps);
	}
	public abstract void runTestCase() throws Exception, Throwable;
	
	
	@BeforeMethod
	public void aaaStartDriver() throws IOException{
		driving = webBrowser.getDriver();
		superController.setDriver(driving);
	}
	
	@AfterMethod(alwaysRun=true)
	public void aaatakeAScreenshot(ITestResult result){ 
		if(result.getStatus()==ITestResult.FAILURE){ 
			byte[] screenshot = ((TakesScreenshot)driving).getScreenshotAs(OutputType.BYTES); 
			ExtendedLogger.logScreenshot(screenshot); 
			}
	}
	
	@AfterMethod
	@Parameters({"debugMode"})
	public void zzztearDown(ITestResult result, @Optional("no") String debug) throws InterruptedException{
		
		if(result.getStatus()==ITestResult.FAILURE){
				
		if(!debug.equalsIgnoreCase("yes")){
			if(this.driving!=null){
				Thread.sleep(1000L);
				this.driving.close();
			}
		}
		}else{
			if(this.driving!=null){
				Thread.sleep(1000L);
				this.driving.close();
				
			}
		}
	}
	
	protected void setController(BasicController controller){
		superController = controller;
		
	}
	
	public HashMap<String, String> returnInstanceParameters(){
		
		HashMap<String, String> instanceParameter = new HashMap<String, String>();
		instanceParameter.put("environment", testEnvironment.toString());
		instanceParameter.put("browser", webBrowser.toString());
		instanceParameter.put("user", reportedUser.printUserIdentifier());
			
		return instanceParameter;
		
		
	}
	
	
	

}
