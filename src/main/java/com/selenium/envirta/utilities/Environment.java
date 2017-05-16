package com.selenium.envirta.utilities;

public enum Environment {
	
	QAENV, DEVENV, UATENV, STAGINGENV;
	
	private final String QAENVURL = "https:/www.careerbuilder.com";
	
	private final String DEVENVURL = "http://www.newtours.demoaut.com";
	private final String UATENVURL = "https:/www.google.com";
	private final String STAGINGENVURL = "https:/www.google.com";
	
	public String toString(){
		switch(this){
		case QAENV:
			return "QAENV";
		case DEVENV:
			return "DEVENV";
		case UATENV:
			return "UATENV";
		case STAGINGENV:
			return "STAGINGENV";
		default:
			return "NOT_SET";
		}
	}
	
	public String getUrl(){
		switch(this){
		case QAENV:
			  return QAENVURL;
		case DEVENV:
			  return DEVENVURL;
		case UATENV:
			  return UATENVURL;
		case STAGINGENV:
			  return STAGINGENVURL;
	    default:
	    	return QAENVURL;
		}
	}
	
	
	
	

}
