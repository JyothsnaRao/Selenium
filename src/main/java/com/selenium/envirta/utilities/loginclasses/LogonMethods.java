package com.selenium.envirta.utilities.loginclasses;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.selenium.envirta.utilities.Environment;

public class LogonMethods {
	
	WebDriver driver;
	WebDriverWait waiter;
	Properties globalProps;
	Environment testEnvironment;
	
	public LogonMethods(Properties globalProps, WebDriver driven, Environment enviro){
		this.driver = driven;
		this.globalProps = globalProps;
		this.waiter = new WebDriverWait(driver, 65);
		this.testEnvironment = enviro;
		
	}
	
	 protected WebElement click(String xpath) throws Exception{
	    try{
	    	waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
	       	waiter.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
	    	Actions hoverEle = new Actions(this.driver);
	    	WebElement mover = this.driver.findElement(By.xpath(xpath));
	    	hoverEle.moveToElement(mover).build().perform();
	    	mover.click();
	    	return mover;
	    }catch(Exception e){
	    	throw new Exception("Click Failed on Element with xpath " + xpath + " due to: " +e.getMessage());
	    }
	     }
	
	 protected WebElement input(String xpath, String data) throws Exception{
		 try{
	    	waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
	    	WebElement ele = driver.findElement(By.xpath(xpath));
	    	ele.click();
	    	ele.clear();
	    	ele.sendKeys(data);
	    	return ele;
		 }
		 catch(Exception e){
			 throw new Exception("Input Failed on Element with xpath " + xpath);
		 }
	    }
	
	

}
