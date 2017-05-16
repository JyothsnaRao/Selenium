package com.selenium.envirta.utilities.loginclasses;

import java.util.Properties;

import org.openqa.selenium.WebDriver;

import com.selenium.envirta.utilities.Environment;
import com.selenium.envirta.utilities.REGUser;

public class MercuryflightLogin extends LogonMethods {

	public MercuryflightLogin(Properties globalProps, WebDriver driven, Environment enviro) {
		super(globalProps, driven, enviro);
		// TODO Auto-generated constructor stub
	}
	
	public void run(REGUser user) throws Exception{
		
		String username = user.getJobseeker_userName();
		System.out.println("userName: " + username);
		String password = user.getJobseeker_password();
		System.out.println("password: " + password);
		
		driver.get(testEnvironment.getUrl());
		
		driver.manage().window().maximize();
		
	//	 click(globalProps.getProperty("cb_homepage_signInbutton"));
		
		Thread.sleep(3000L);
		
		System.out.println("We are at mercury login page");
		
        input(globalProps.getProperty("mercury_loginPage_userName"), username);
        input(globalProps.getProperty("mercury_loginPage_password"), password);
        
        click(globalProps.getProperty("mercury_loginPage_signButon"));
		
		
	}

}
