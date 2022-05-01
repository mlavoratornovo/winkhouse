package winkhouse.db.orm;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.datasource.DataSourceBuilder;

import winkhouse.util.WinkhouseUtils;

public class CayenneContextManager {

	private static CayenneContextManager instance = null;
	private ServerRuntime cayenneRuntime = null;
	private static final String driver = "org.hsqldb.jdbcDriver";

	private CayenneContextManager() {
		
		String username = (WinkhouseUtils.getInstance()
				   .getPreferenceStore()
				   .getString(WinkhouseUtils.USERNAME).equalsIgnoreCase(""))
				   ? WinkhouseUtils.getInstance()
						   .getPreferenceStore()
						   .getDefaultString(WinkhouseUtils.USERNAME)
				   : WinkhouseUtils.getInstance()
				   		   .getPreferenceStore()
  					       .getString(WinkhouseUtils.USERNAME);			

		String password = (WinkhouseUtils.getInstance()
					  	 .getPreferenceStore()
					  	 .getString(WinkhouseUtils.USERDBPWD).equalsIgnoreCase(""))
		? WinkhouseUtils.getInstance()
					  	   .getPreferenceStore()
					  	   .getDefaultString(WinkhouseUtils.USERDBPWD)
		: WinkhouseUtils.getInstance()
					  	   .getPreferenceStore()
					  	   .getString(WinkhouseUtils.USERDBPWD);
			  				 
		if (!password.equalsIgnoreCase("")){
			try{
				password = WinkhouseUtils.getInstance().DecryptStringStandard(password);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		
		cayenneRuntime = ServerRuntime.builder()
	            .dataSource(DataSourceBuilder
	                    .url(this.getPosizioneDB())
	                    .driver(driver)
	                    .userName(username)
	                    .password(password).build())
	            .addConfig("winkhouse/orm/cayenne-winkhouse.xml")
	            .build();
	        
		
	}
	
	public static CayenneContextManager getInstance(){
		if (instance == null){
			instance = new CayenneContextManager();
		}
		return instance;
	}
	
	protected String getPosizioneDB(){
		
		Boolean bundleDB = WinkhouseUtils.getInstance()
				     .getPreferenceStore()
					 .getBoolean(WinkhouseUtils.BUNDLEDB);
		String posizioneDB = null;

		if (bundleDB){
			InetAddress addr = null;
			try {
				addr = InetAddress.getLocalHost();
				posizioneDB = addr.getHostAddress();
			} catch (UnknownHostException e) {				
				e.printStackTrace();
			}
		}else{
			posizioneDB = (WinkhouseUtils.getInstance()
					.getPreferenceStore()
					.getString(WinkhouseUtils.POSIZIONEDB).equalsIgnoreCase(""))
					? WinkhouseUtils.getInstance()
							.getPreferenceStore()
				  			.getDefaultString(WinkhouseUtils.POSIZIONEDB)
				  	: WinkhouseUtils.getInstance()
		  					.getPreferenceStore()
		  					.getString(WinkhouseUtils.POSIZIONEDB);
		}

		String url = "jdbc:hsqldb:hsql://"+posizioneDB+"/winkhouse";
		
		return url;

	}
	
	public ObjectContext getContext(){
		if (this.cayenneRuntime != null){
			return this.cayenneRuntime.newContext();
		}
		return null;
	}
	

}
