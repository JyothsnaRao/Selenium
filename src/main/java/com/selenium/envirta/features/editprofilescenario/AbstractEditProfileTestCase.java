package com.selenium.envirta.features.editprofilescenario;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.selenium.envirta.utilities.AbstractTestCase;
import com.selenium.envirta.utilities.Browser;
import com.selenium.envirta.utilities.Environment;
import com.selenium.envirta.utilities.Xls_Reader;

public abstract class AbstractEditProfileTestCase extends AbstractTestCase {
	
	protected EditProfile_Controller controller;
	protected Properties editProfileProps;
	protected Properties globalProps;
	protected Xls_Reader xls;
	
	public AbstractEditProfileTestCase(Browser browser, String userName, Environment enviro, String SpreadsheetName) throws IOException {
		super(browser, userName, enviro, SpreadsheetName);
		// TODO Auto-generated constructor stub
		
		editProfileProps = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\com\\selenium\\envirta\\featureproperties\\editProfile.properties");
		editProfileProps.load(fis);
		fis.close();
		
		globalProps = new Properties();
		FileInputStream fig = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\resources\\com\\selenium\\envirta\\featureproperties\\global.properties");
		globalProps.load(fig);
		fig.close();
		
		xls = new Xls_Reader(System.getProperty("user.dir")+"\\src\\main\\resources\\com\\selenium\\envirta\\xls\\"+SpreadsheetName+".xlsx");
		
		
		controller = new EditProfile_Controller(
				enviro, editProfileProps, globalProps, xls);
		       setController(controller);
	}
	
}
