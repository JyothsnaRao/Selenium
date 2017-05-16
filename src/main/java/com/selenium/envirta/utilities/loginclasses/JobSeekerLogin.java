package com.selenium.envirta.utilities.loginclasses;

import java.util.Properties;

import org.openqa.selenium.WebDriver;

import com.selenium.envirta.utilities.Environment;
import com.selenium.envirta.utilities.REGUser;

public class JobSeekerLogin extends LogonMethods {

	public JobSeekerLogin(Properties globalProps, WebDriver driven, Environment enviro) {
		super(globalProps, driven, enviro);
		// TODO Auto-generated constructor stub
	}
	
	public void run(REGUser user) throws Exception{
		
		String username = user.getJobseeker_userName();
		String password = user.getJobseeker_password();
		
		driver.get(testEnvironment.getUrl());
		
		driver.manage().window().maximize();
		
		 click(globalProps.getProperty("cb_homepage_signInbutton"));
		
        input(globalProps.getProperty("cb_loginpage_userNameTextbox"), username);
        input(globalProps.getProperty("cb_loginpage_passwordTextbox"), password);
        
        click(globalProps.getProperty("cb_loginpage_signInbutton"));
		
		
	}

}
