package com.selenium.envirta.features.editprofilescenario;

import java.io.IOException;

import org.testng.Reporter;

import com.selenium.envirta.utilities.Browser;
import com.selenium.envirta.utilities.Environment;

public class EditProfile_RegressionScenario_01 extends AbstractEditProfileTestCase {

	public EditProfile_RegressionScenario_01(Browser browser, String userName, Environment enviro,
			String SpreadsheetName) throws IOException {
		super(browser, userName, enviro, SpreadsheetName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void runTestCase() throws Exception{
		controller.loginToMercutyFlight(regUser);
		
		long start = System.currentTimeMillis();

		controller.click(globalProps.getProperty("mercury_accountPage_signOffButon"));
		
		long finish = System.currentTimeMillis();

		long totalTimeMillis = finish - start;
		
		System.out.println("Login response time in MilliSecs: " + totalTimeMillis);
		Reporter.log("Signoff response time: "+ totalTimeMillis);
		
		/*controller.click(editProfileProps.getProperty("cb_mycbpage_editProfilLink"));*/
		
		
	}

}
