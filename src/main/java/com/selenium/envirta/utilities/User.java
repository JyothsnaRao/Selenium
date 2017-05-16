package com.selenium.envirta.utilities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public abstract class User {
	
	protected String firstName;
	protected String lastName;
	
	
	public User(Properties props){
		
		Enumeration<Object> keys = props.keys();
		
		while(keys.hasMoreElements()){
			String thisKey = (String) keys.nextElement();
			
			try{
				Field f = findFieldInclSuper(thisKey);
				String value = props.getProperty(thisKey);
				
				if(f.getGenericType().equals(Boolean.class)){
					f.set(this, pullAndConvertBoolean(thisKey, props));
				}else if(f.getGenericType().equals(List.class)){
					f.set(this, pullAndConvertList(thisKey, props));
				}else{
					f.set(this, value);
				}
			}catch(Exception e){
				
		}
	}
	
 }
	
	public abstract String printUserIdentifier();
	public String getFirstName() throws Exception{
		if(firstName!= null){
			return firstName;
		}else{
			throw new Exception("Specified user info first Name does not exist. Check user property file");
		}
	}
	
	public String getLastName() throws Exception{
		if(lastName!= null){
			return lastName;
		}else{
			throw new Exception("Specified user info last Name does not exist. Check user property file");
		}
	}
	
	protected ArrayList<String> pullAndConvertList(String propName, Properties props){
		
		String listInString = props.getProperty(propName);
		
		ArrayList<String> propList = new ArrayList<String>();
		String[] stringArray = listInString.replaceAll("//s", "").split((";"));
		
		for(int i =0; i<stringArray.length; i++){
			propList.add(stringArray[i]);
		}
		return propList;
	}
	
	protected Boolean pullAndConvertBoolean(String propName, Properties props){
		String propString = props.getProperty(propName);
		if(propString != null){
			if(propString.equalsIgnoreCase("true")){
				return true;
			}else
				return false;
		}else
			return null;
		
	}
	
	protected Field findFieldInclSuper(String fieldName) throws NoSuchFieldException{
		
		Class<?> currentClass = this.getClass();
		Field foundField = null;
		
		while(currentClass != null && foundField == null){
			try{
				foundField = currentClass.getDeclaredField(fieldName);
			}catch(Exception e){
				currentClass = currentClass.getSuperclass();
			}
		}
		if(foundField != null){
			return foundField;
		}else{
			throw new NoSuchFieldException();
		}
		
	}
	
	protected Object verifyNull(String varName) throws Exception{
		Field f = getClass().getDeclaredField(varName);
		Object o = f.get(this);
		if( o != null){
			return o;
		}
		else{
			String name = f.getName();
			throw new Exception("Specified user info" + name + "does not exist. Check user property file");
		}
	}
	
	
	
}
