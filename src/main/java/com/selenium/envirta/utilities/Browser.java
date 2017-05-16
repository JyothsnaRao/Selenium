package com.selenium.envirta.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public enum Browser {
	
	FIREFOX, IE,CHROME;
	
	public WebDriver getDriver() throws IOException{
		
		Properties config = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\localconfig.properties");
		config.load(fis);
		fis.close();
		
		switch(this){
		case FIREFOX:
			String firefoxPath=null;
			try{
				firefoxPath=config.getProperty("FIREFOXBROWSER_FILEPATH");
			}catch(Exception e){
								
			}
			if(firefoxPath!=null){
				File firefoxLoc = new File(firefoxPath);
				System.setProperty("webdriver.firefox.bin", firefoxLoc.getAbsolutePath());
			}
			
			FirefoxProfile prof = new FirefoxProfile();
			prof.setPreference("intl.accept_languages", "no, en-us, en");
			 return new FirefoxDriver(prof);
			 
		case IE:
			String iePath = null;
			try {
				iePath = config.getProperty("IEBROWSER_FILEPATH");
			} catch (Exception e) {
			}

			if (iePath != null) {
				File ieLoc = new File(iePath);
				System.setProperty("iexploreDefaultPath",
						ieLoc.getAbsolutePath());
			}

			File ieLoc = new File(config.getProperty("IEDRIVER_FILEPATH"));
			System.setProperty("webdriver.ie.driver", ieLoc.getAbsolutePath());
			DesiredCapabilities ieCaps = DesiredCapabilities.internetExplorer();
			ieCaps.setCapability(
					InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
					true);
			return new InternetExplorerDriver(ieCaps);
			
		case CHROME:
			ChromeOptions opt = new ChromeOptions();
			
			String chromePath = null;
			try{
				chromePath=config.getProperty("CHROMEDRIVER_FILEPATH");
			}catch(Exception e){
				}
			
			if(chromePath!=null){
				File chromeLoc = new File(chromePath);
				opt.setBinary(chromeLoc);
				}
			File chromeBrowserLoc = new File(config.getProperty("CHROMEDRIVER_FILEPATH"));
			System.setProperty("webdriver.chrome.driver", chromeBrowserLoc.getAbsolutePath());
			
			return new ChromeDriver(opt);
			
			default:
				return new FirefoxDriver();
		}
	}	
		
	public String toString(){
		switch(this){
		case FIREFOX:
			return "FIREFOX";
		case IE:
			return "IE";
		case CHROME:
			return "CHROME";
		default:
			return "FIREFOX";
		
		}
	}

}