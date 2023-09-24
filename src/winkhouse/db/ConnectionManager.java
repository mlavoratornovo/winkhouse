package winkhouse.db;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import winkhouse.Activator;
import winkhouse.db.server.HSQLDBHelper;
import winkhouse.util.WinkhouseUtils;



public class ConnectionManager {

	private static ConnectionManager instance = null;
	private Connection connection = null;
	private Connection connectionSelectConnection = null;
	private boolean errorMessage = true;
	private boolean chkDataSource = false; 
	private Text console = null;
	
	private ConnectionManager(){
		
	}
	
	public static ConnectionManager getInstance(){
		if (instance == null){
			instance = new ConnectionManager();
		}
		return instance;
	}
	
	private Connection getConnectionSelection(){
		
	
		if (checkDataSource()){
			
		
			String driver = "org.hsqldb.jdbcDriver";
			
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
					  				   			
		    try {
				Class.forName(driver);
			//	MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "test", "posizioneDB :" + posizioneDB + " username :" + username + " password :" + password);
				connectionSelectConnection = DriverManager.getConnection(url, username, password);							
				connectionSelectConnection.setReadOnly(true);
			} catch (ClassNotFoundException e) {
				if (errorMessage){
					Shell shell = new Shell(SWT.TOOL | SWT.NO_TRIM);
					MessageBox mb = new MessageBox(shell,SWT.ERROR);
					errorMessage = false;
					mb.setMessage("Driver database non trovati");
					mb.open();			
				}
			} catch (SQLException e) {
				if (errorMessage){
					Shell shell = new Shell(SWT.TOOL | SWT.NO_TRIM);

					MessageBox mb = new MessageBox(shell,SWT.ERROR);
					errorMessage = false;
					if (!WinkhouseUtils.getInstance().startConnectionErrorShow){
						WinkhouseUtils.getInstance().startConnectionErrorShow = true;
						mb.setMessage("Parametri di connessione non corretti, controllare su File -> Impostazioni.\n\n" +
									  "WinkhouseDBAgent potrebbe non essere attivo nel PC inserito alla voce \n" +
									  "File -> Impostazioni -> Database e files");
						mb.open();
					}
				}								
			}
		    
		}else{
			
			if (errorMessage){
				Shell shell = new Shell(SWT.TOOL | SWT.NO_TRIM);

				MessageBox mb = new MessageBox(shell,SWT.ERROR);
				errorMessage = false;
				if (!WinkhouseUtils.getInstance().startConnectionErrorShow){
					WinkhouseUtils.getInstance().startConnectionErrorShow = true;
					mb.setMessage("Parametri di connessione non corretti, controllare su File -> Impostazioni.\n\n" +
								  "WinkhouseDBAgent potrebbe non essere attivo nel PC inserito alla voce \n" +
								  "File -> Impostazioni -> Database e files \n");
					mb.open();
				}
			}								
			
			
		}
		
		return connectionSelectConnection;
	}
	
	public Connection getConnection(){
		
		String driver = "org.hsqldb.jdbcDriver";
		
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
			password = WinkhouseUtils.getInstance().DecryptStringStandard(password);
		}

	    try {
			Class.forName(driver);
		//	MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "test", "posizioneDB :" + posizioneDB + " username :" + username + " password :" + password);
			connection = DriverManager.getConnection(url, username, password);
			connection.setAutoCommit(false);				
		} catch (ClassNotFoundException e) {
			if (errorMessage){
				MessageBox mb = new MessageBox(Activator.getDefault()
														.getWorkbench().getActiveWorkbenchWindow().getShell(),												
											    SWT.ERROR);
				errorMessage = false;
				mb.setMessage("Driver database non trovati");
				mb.open();
			}
		} catch (SQLException e) {
			if (errorMessage){
				MessageBox mb = new MessageBox(Activator.getDefault()
														.getWorkbench().getActiveWorkbenchWindow().getShell(),
											   SWT.ERROR);
				errorMessage = false;
				mb.setMessage("Parametri di connessione non corretti, controllare su File -> Impostazioni.\n" +
						  	  "WinkhouseDBAgent potrebbe non essere attivo nel PC inserito nella voce \n" +
						      "File -> Impostazioni -> Database e files");
				
				mb.open();
			}
		} catch(Exception e){
			e.printStackTrace();
		}		  
	
	return connection;
}

	public Connection getConnectionSelectConnection() {
		try {
			if ((connectionSelectConnection == null) || 
				(connectionSelectConnection.isClosed())
				){
				connectionSelectConnection = getConnectionSelection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connectionSelectConnection;
	}
	
	public boolean checkConnection(){
		
			String driver = "org.hsqldb.jdbcDriver";
			
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
					  				   			
		    try {
				Class.forName(driver);
				Connection con = DriverManager.getConnection(url, username, password);							
				con.setReadOnly(true);
				chkDataSource = true;
			} catch (ClassNotFoundException e) {
				return false;
			} catch (SQLException e) {
				return false;
			}
		    
		    return true;
		
	}
	
	public boolean isLocalDataSource(){
	
		InetAddress addr = null;
		try {
			
			addr = InetAddress.getLocalHost();
			String localip = addr.getHostAddress();
			String url = getConnectionSelectConnection().getMetaData().getURL();
			
			if (url.contains(localip)){
				return true;
			}else{
				return false;
			}
		} catch (UnknownHostException e) {				
			return false;
		} catch (SQLException e) {
			return false;
		}
		
	}
	
	public boolean checkDataSource(){
		
		if (!chkDataSource){
			boolean chkcon = checkConnection();
			boolean bundleDB = WinkhouseUtils.getInstance()
						 					 .getPreferenceStore()
						 					 .getBoolean(WinkhouseUtils.BUNDLEDB);
			
			if (chkcon){
				if (isLocalDataSource()){
					chkDataSource = true;
					return true;					
				}else{
					if (bundleDB){
						return false;
					}else{
						chkDataSource = true;
						return true;
					}
					
				}
			}else{
				if (bundleDB){
					if (HSQLDBHelper.getInstance().checkDBFiles()){
						if (HSQLDBHelper.getInstance().startHSQLDB()){
							WinkhouseUtils.getInstance().setBundleDBRunning(true);
							chkDataSource = true;
							return checkConnection();
						}else{
							chkDataSource = false;
							return false;
						}
					}else{
						chkDataSource = false;
						return false;
					}
				}else{
					chkDataSource = false;
					return false;
				}
				
			}
		}
		return true;
		
	}

	public Text getConsole() {
		return console;
	}
	
	public void setConsole(Text console) {
		this.console = console;
	}
	
	public void resetConnections(){
		try {
			connection.close();
			connectionSelectConnection.close();
			connection = null;
			connectionSelectConnection = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
