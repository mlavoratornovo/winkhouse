package winkhouse.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DBLogManager {

	private StringBuffer sb = null; 
	private Method guiUpdaterMethod = null;
	private Object updaterObj = null;
	private static DBLogManager instance = null;
	
	private DBLogManager() {
		sb = new StringBuffer();
	}
	
	public static DBLogManager getInstance(){
		if (instance == null){
			instance = new DBLogManager();
		} 
		return instance;
	}
	
	public String flushLog(){
		
		String returnValue = sb.toString();
		sb = new StringBuffer();
		
		return returnValue;
	}

	public void flushLogByUpdMethod(){
		
		if ((guiUpdaterMethod != null)&& (updaterObj != null)){
			
			String returnValue = sb.toString();
			sb = new StringBuffer();
		
			try {
				guiUpdaterMethod.invoke(updaterObj, returnValue);
			} catch (IllegalArgumentException e) {			
				e.printStackTrace();
			} catch (IllegalAccessException e) {			
				e.printStackTrace();
			} catch (InvocationTargetException e) {			
				e.printStackTrace();
			}
		}
		
	}

	public synchronized void appendLog(String value){
		sb.append(value);
		//flushLogByUpdMethod();
	}

	public Method getGuiUpdaterMethod() {
		return guiUpdaterMethod;
	}

	public void setGuiUpdaterMethod(Method guiUpdaterMethod) {
		this.guiUpdaterMethod = guiUpdaterMethod;
	}

	public Object getUpdaterObj() {
		return updaterObj;
	}

	public void setUpdaterObj(Object updaterObj) {
		this.updaterObj = updaterObj;
	}
	

}
