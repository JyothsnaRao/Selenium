package com.selenium.envirta.utilities;

import java.util.Properties;

public class REGUser extends User {
	
	protected String jobseeker_userName;
	protected String jobseeker_password;
	
   public REGUser(Properties props){
	   super(props);
   }
   
   public String getJobseeker_userName() throws Exception{
	   if(jobseeker_userName!=null){
		   return jobseeker_userName;
	   }
		   else{
			   throw new Exception("Specified user info Jobseeker_username does not exist. Check user property file");
		   }
	   }
   
   public String getJobseeker_password() throws Exception{
	   if(jobseeker_password!=null){
		   return jobseeker_password;
	   }
		   else{
			   throw new Exception("Specified user info Jobseeker_password does not exist. Check user property file");
		   }
	   }
   @Override
    public String printUserIdentifier(){
	   return jobseeker_userName;
	   
   }
	   
   }
