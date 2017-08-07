package trail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionApi {

	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		
		ReflectionApi ra = new ReflectionApi();
		
	//	ra.newyork();
	//	ra.denver();
		
		Method method[] = ra.getClass().getMethods();
		
		for(int i=0; i<=method.length; i++){
		//	System.out.println(method[i].getName());
			
			if(method[i].getName().equals("newyork")){
				method[i].invoke(ra, method);
			}
			
			
		}

	}
	
	private static Object method(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public void newyork(){
		System.out.println("I am in New York");
	}
	
	public void denver(){
		System.out.println("I am in Denver");
	}

}
