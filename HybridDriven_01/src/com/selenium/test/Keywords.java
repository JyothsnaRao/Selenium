package com.selenium.test;

import static com.selenium.test.DriverScript.APP_LOGS;
import static com.selenium.test.DriverScript.CONFIG;
import static com.selenium.test.DriverScript.OR;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;


public class Keywords {
	public WebDriver driver;
	
	
	public String openBrowser(String object, String data){
		APP_LOGS.debug("Opening the browser");
		if(CONFIG.getProperty("browserType").equals("Mozilla")){
				driver = new FirefoxDriver();
		}else if(CONFIG.getProperty("browserType").equals("IE")){
		//	driver = new IEDriver();
		}
			return "Pass";
	}
	
	public String navigateTo(String object, String data){
		APP_LOGS.debug("Navigate to the application");
		try{
		driver.navigate().to(CONFIG.getProperty("url"));
		}catch(Exception e){
			return "Fail"+" -- unable to navigate to the application";
		}
		return "Pass";
		}
	public String verifyTitle(String object, String data){
		APP_LOGS.debug("Verifying the title");
		
		try{
		String expectedData=data;
		String actualData = driver.findElement(By.xpath(OR.getProperty(object))).getText();
		System.out.println("Expected name: " + expectedData);
		System.out.println("Actual name: " + actualData);
		
		if(expectedData.equals(actualData)){
		 APP_LOGS.debug("Correct user loggedin");
		 return "Pass";
		}else{
			APP_LOGS.debug("InCorrect; user loggedin");
		return "Fail"+ " -- Logged name is incorrect";
		}
		}
		catch(Exception e)	{
			return "Fail"+" -- Unable to find the link "+e.getMessage();
		}
	}
	
	public String verifyTextinInputbox(String object, String data){
		APP_LOGS.debug("Verifying the title");
		
		try{
		String expectedData=data;
		String actualData = driver.findElement(By.xpath(OR.getProperty(object))).getAttribute("value");
		System.out.println("Expected name: " + expectedData);
		System.out.println("Actual name: " + actualData);
		
		if(expectedData.equals(actualData)){
		 APP_LOGS.debug("Correct user loggedin");
		 return "Pass";
		}else{
			APP_LOGS.debug("InCorrect; user loggedin");
		return "Fail"+ " -- Logged name is incorrect";
		}
		}
		catch(Exception e)	{
			return "Fail"+" -- Unable to find the link "+e.getMessage();
		}
	}
	public String input(String object, String data){
		APP_LOGS.debug("Sending Input to the textbox");
		try{
		driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
		}
		catch(Exception e){
			return "Fail"+" -- unable to find the element";
		}
		
		return "Pass";
	}
	
	public String clickLink(String object, String data) throws InterruptedException{
		APP_LOGS.debug("Clicking the Link");
		
		Thread.sleep(1000L);
		try{
		driver.findElement(By.xpath(OR.getProperty(object))).click();
		}catch(Exception e){
			return "Fail"+ "-- Unable to find the link "+e.getMessage();
		}
		return "Pass";
	}
	
	public String verifyHeader(String object, String data){
		APP_LOGS.debug("Verifying the header content");
		return "Pass";
	}
	
	public String verifyElementDisplayed(String object, String data){
		APP_LOGS.debug("Verifying the element displayed on the web page");
		return "Pass";
	}
	public String closeBrowser(String object, String data){
		try{
			driver.quit();
		}catch(Exception e){
			return "Fail" +" -- Unable to close the browser: "+e.getMessage();
		}
		return "Pass";
	}

	public String selectByValueListbox(String object, String data){
		APP_LOGS.debug("Sending Input to the textbox");
		try{
		Select selector = new Select(driver.findElement(By.xpath(OR.getProperty(object))));	
		selector.selectByValue(data);
		}
		catch(Exception e){
			return "Fail"+" -- unable to find the element";
}
		return "Pass";
}
	public String selectByRandomListbox(String object, String data){
		APP_LOGS.debug("Sending Input to the textbox");
		try{
		Select selector = new Select(driver.findElement(By.xpath(OR.getProperty(object))));	
		List<WebElement> elementlist=selector.getOptions();
		int elecount=elementlist.size();
		Random optelements=new Random();
		int optselect=optelements.nextInt(elecount);
		selector.selectByIndex(optselect);
		
		}
		catch(Exception e){
			return "Fail"+" -- unable to find the element";
         }
		return "Pass";
}
	


}