package com.selenium.test;

	
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.selenium.xls.read.Xls_Reader;

public class DriverScript {

	public static Logger APP_LOGS;
	Xls_Reader xls;
	int currentTestCaseID;
	int currentTestStepID;
	public String currentTestCaseName;
	public String currentKeyword;
	public Method method[];
	public Keywords keywords;
	public ArrayList<String> resultSet;
	public String keyword_result;
	public static Properties CONFIG;
	public static Properties OR;
	public String object;
	public String data;
	
	

	public DriverScript() throws IOException {
		keywords = new Keywords();
		method = keywords.getClass().getMethods();
		
		CONFIG = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//com//selenium//config//config.properties");
		CONFIG.load(fis);
		   
		 OR = new Properties();
		 fis = new FileInputStream(System.getProperty("user.dir")+"//src//com//selenium//config//OR.properties");
         OR.load(fis);		

	}

	public static void main(String[] args) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IOException {
		// TODO Auto-generated method stub
		
		
       		
	    DriverScript test = new DriverScript();
		test.start();
	}

	public void start() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		APP_LOGS = Logger.getLogger("devpinoyLogger");

		APP_LOGS.debug("Execution initiated");
		xls = new Xls_Reader(System.getProperty("user.dir")
				+ "\\src\\com\\selenium\\xls\\SampleTestSuite.xlsx");

		// Retrieving from TestCases sheet
		for (currentTestCaseID = 2; currentTestCaseID <= xls
				.getRowCount("TestCases"); currentTestCaseID++) {

			/*
			 * APP_LOGS.debug(xls.getCellData("TestCases", "TCID",
			 * currentTestCaseID)+ "--"+ xls.getCellData("TestCases", "Runmode",
			 * currentTestCaseID));
			 */
			currentTestCaseName = xls.getCellData("TestCases", "TCID",
					currentTestCaseID);
            resultSet = new ArrayList<String>();
			// Checking Runmode. If it is "Y", execute the test
			if (xls.getCellData("TestCases", "Runmode", currentTestCaseID)
					.equals("Y")) {

				APP_LOGS.debug(xls.getCellData("TestCases", "TCID",
						currentTestCaseID)
						+ "--"
						+ xls.getCellData("TestCases", "Runmode",
								currentTestCaseID));

				// Retrieve from TestSteps sheet

				executeKeywords();
				createXlsResultReport();
			}else{
				createXlsResultReport();
			}

		}

	}

	public void executeKeywords() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		
					
		
		for (currentTestStepID = 2; currentTestStepID <= xls
				.getRowCount("TestSteps"); currentTestStepID++) {
			
			
			// APP_LOGS.debug(xls.getCellData("TestSteps", "TCID",
			// currentTestStepID)+"--"+xls.getCellData("TestSteps",
			// "Keywords",
			// currentTestStepID));

			// Execute keywords, if TestSteps 'TCID' is matched with
			// TestCases
			// 'TCID'
			if (currentTestCaseName.equals(xls.getCellData("TestSteps", "TCID",
					currentTestStepID))) {
				APP_LOGS.debug(xls.getCellData("TestSteps", "TCID",
						currentTestStepID)
						+ "--"
						+ xls.getCellData("TestSteps", "Keywords",
								currentTestStepID));
				currentKeyword = xls.getCellData("TestSteps", "Keywords",
						currentTestStepID);
				APP_LOGS.debug(currentKeyword);
				
				data=xls.getCellData("TestSteps", "Data", currentTestStepID);
				object=xls.getCellData("TestSteps", "Object", currentTestStepID);
							

				for (int i = 0; i < method.length; i++) {
					// System.out.println(method[i].getName());
					if (method[i].getName().equals(currentKeyword)) {
						keyword_result = (String) method[i].invoke(keywords,object,data);
						resultSet.add(keyword_result);
				//		System.out.println(resultSet);
						
					}

				}

			}

		}
	}
	
	public void createXlsResultReport(){
		
		String colName= "Results";
		
		boolean isColExist = false;
		
		for(int c=0; c<xls.getColumnCount("TestSteps"); c++){
		//	System.out.println(xls.getCellData("TestSteps", c, 2));
			if(xls.getCellData("TestSteps", c, 1).equals(colName)){
				isColExist = true;
				break;
			}
		}
		
		if(!isColExist)
			xls.addColumn("TestSteps", colName);
			
			int index=0;
			
			for(int i=2; i<=xls.getRowCount("TestSteps"); i++){
				if(currentTestCaseName.equals(xls.getCellData("TestSteps", "TCID", i))){
					if(resultSet.size()==0)
						xls.setCellData("TestSteps", colName, i, "Skip");
					else
						xls.setCellData("TestSteps", colName, i, resultSet.get(index));
						index++;
					
				}
			}
			
		
		
		if(resultSet.size()==0){
			//Skip test
			xls.setCellData("TestCases", "Results", currentTestCaseID, "Skip");
			return;
		}else{
			for(int i=0; i<resultSet.size(); i++){
				if(!resultSet.get(i).equals("Pass")){
					xls.setCellData("TestCases", "Results", currentTestCaseID, "Fail");
					return;
				}
			}
		}
		xls.setCellData("TestCases", "Results", currentTestCaseID, "Pass");
  }
}