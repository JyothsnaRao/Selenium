package com.selenium.envirta.features.editprofilescenario;

import java.util.Properties;

import com.selenium.envirta.utilities.BasicController;
import com.selenium.envirta.utilities.Environment;
import com.selenium.envirta.utilities.Xls_Reader;

public class EditProfile_Controller extends BasicController {
	
	Properties editProfileProps;
	Xls_Reader xls;
	
	public EditProfile_Controller(Environment environ, Properties editProfileProperties, Properties globalProps, Xls_Reader xlsReader){
		super(environ, globalProps);
		editProfileProps=editProfileProperties;
		xls = xlsReader;
		
	}
	
	

}
