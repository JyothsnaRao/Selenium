package com.selenium.envirta.utilities;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.selenium.envirta.utilities.loginclasses.JobSeekerLogin;
import com.selenium.envirta.utilities.loginclasses.MercuryflightLogin;

public class BasicController {
	
	protected WebDriver driving;
	protected WebDriverWait waiter;
	protected Properties globalProps;
	protected Environment testEnviron;
	
	
	public BasicController(Environment environ, Properties globalProperties){
		testEnviron = environ;
		globalProps = globalProperties;
	}
	
	public void setDriver(WebDriver driver){
		this.driving = driver;
		this.waiter = new WebDriverWait(driver, 85);
	}
	
	//Write browser controller methods
	
    public WebElement click(String xpath){
    	try{
    	waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    	waiter.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
    	Actions hoverEle = new Actions(this.driving);
    	WebElement mover = this.driving.findElement(By.xpath(xpath));
    	hoverEle.moveToElement(mover).build().perform();
    	mover.click();
    	Reporter.log("Clicked Element: " + xpath +".");
    	return mover;
    	}catch(Exception e){
    		Reporter.log("Attempted to click on (xpath: " +xpath+ ". Failed due to " + e.getMessage());
    		throw e;
    	}
     }
    
    public WebElement input(String xpath, String data){
    	try{
    	waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    	WebElement ele = driving.findElement(By.xpath(xpath));
    	ele.click();
    	ele.clear();
    	ele.sendKeys(data);
    	Reporter.log("Enter input at: " +xpath );
    	return ele;
    	}catch(Exception e){
    		Reporter.log("Failed enter the input at xpath: " + xpath + ". Failed due to " + e.getMessage());
    		throw e;
    	}
    	
    }
    
    public void navigateTo(String url){
    	driving.get(url);
    }
    
    public Select select(String xpath, String value){
    	waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    	Select selector = new Select(driving.findElement(By.xpath(xpath)));
    	selector.selectByValue(value);
    	return selector;
      }
    
    public void verifyText(String xpath, String data){
    	waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    	String expecteddata = data;
    	String actualData = driving.findElement(By.xpath(xpath)).getText();
    	if(expecteddata.equals(actualData)){
    	}
    }
    
    public void loginAsJobSeeker(REGUser user) throws Exception{
    	try{
    	JobSeekerLogin jsl = new JobSeekerLogin(globalProps, driving, testEnviron);
    	jsl.run(user);
    	}catch(Exception e){
    		Reporter.log("Job Seeker Login failed due to: " + e.getMessage() );
    		throw e;
    	}
       }
    public void loginToMercutyFlight(REGUser user) throws Exception{
    	try{
    		MercuryflightLogin jsl = new MercuryflightLogin(globalProps, driving, testEnviron);
    	jsl.run(user);
    	}catch(Exception e){
    		Reporter.log("Job Seeker Login failed due to: " + e.getMessage() );
    		throw e;
    	}
       }
}
