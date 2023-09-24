package winkhouse.db.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.swt.widgets.Text;
import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.server.Server;

import winkhouse.Activator;
import winkhouse.db.ConnectionManager;
import winkhouse.util.DBLogManager;
import winkhouse.util.WinkhouseUtils;


public class HSQLDBHelper {

	private static HSQLDBHelper instance = null;
	private boolean doLog = false;
	private Text console = null;
	private Server s = null;
	
	public static HSQLDBHelper getInstance(){
		if (instance == null){
			instance = new HSQLDBHelper();
		}
		return instance;
	}

	private HSQLDBHelper() {

	}
	
	class LoggingOutputStream extends ByteArrayOutputStream { 
		 
	    private String lineSeparator; 
	 
	    private DBLogManager logmanager; 
	    private String record;
	    
	    /** 
	     * Constructor 
	     * @param logger Logger to write to 
	     * @param level Level at which to write the log message 
	     */ 
	    public LoggingOutputStream(DBLogManager logmanager) { 
	        super(); 
	        this.logmanager = logmanager; 
	        lineSeparator = System.getProperty("line.separator"); 
	    } 
	 
	    /** 
	     * upon flush() write the existing contents of the OutputStream
	     * to the logger as a log record. 
	     * @throws java.io.IOException in case of error 
	     */ 
	    public void flush() throws IOException { 
	 
	        
	        synchronized(this) { 
	            super.flush(); 
	            record = this.toString(); 
	            super.reset(); 
	 
	            if (record.length() == 0 || record.equals(lineSeparator)) { 
	                // avoid empty records 
	                return; 
	            }
	            if (doLog){
	            	logmanager.appendLog(record);
	            }
//	            if (!PlatformUI.getWorkbench().getDisplay().isDisposed()){
//		            PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
//		            	public void run() {
//							if ((console != null) && (!console.isDisposed()) && doLog){
//								console.append(record);							
//							}
//						}
//					});
//	            }
	        } 
	    }

		public Text getConsole() {
			return console;
		}

	}
	
	public boolean startHSQLDB(){
		
		s = new Server();
		try{
			   
			PrintStream ps = new PrintStream(new LoggingOutputStream(DBLogManager.getInstance()));

			String dataPath = Activator.getDefault()
		   							   .getStateLocation()
		   							   .toFile()
		   							   .toString() + File.separator + "datidb";	
			
			String ip = null;
			InetAddress addr = null;
			addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress();
			   
			s.setAddress(ip);
			s.setDatabasePath(0, "file:"+dataPath+"\\winkhouse");
			s.setDatabaseName(0, "winkhouse");
			s.setLogWriter(new PrintWriter(ps));
			s.setTrace(true);
			s.setDaemon(true);
			s.start();
			return true;
		
		}catch(Exception e){
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < e.getStackTrace().length; i++) {
				sb.append(((StackTraceElement)e.getStackTrace()[i]).toString() + "\n");
			}
			writeToConsole(sb.toString());
			return false;
		}
		
		
	}
	
	public void chiudiDatabase(){
		writeToConsole("chiusura database in corso... ");
		shutdownHSQL();
		writeToConsole("database chiuso bye bye ");
		WinkhouseUtils.getInstance().setBundleDBRunning(false);
	}
	
	private void shutdownHSQL(){
		try {
			Class.forName("org.hsqldb.jdbcDriver");

			InetAddress addr = null;
			String posizioneDB = null;
			try {
				addr = InetAddress.getLocalHost();
				posizioneDB = addr.getHostAddress();
			} catch (UnknownHostException e) {				
				e.printStackTrace();
			}

			String url = "jdbc:hsqldb:hsql://"+posizioneDB+"/winkhouse";
			Connection con = ConnectionManager.getInstance().getConnection();
			String sql = "SHUTDOWN COMPACT";
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();					
		}finally{
			if (s != null){
				s.stop();
			}
		}
	}

	public void writeToConsole(String argument){
		if (console != null){
			if (doLog){
				console.setText((console.getText().length() != 0)
								?"\n" + argument
							    : argument);
			}
		}
	}

	public Text getConsole() {
		return console;
	}

	public void setConsole(Text console) {
		this.console = console;		
	}

	public boolean isDoLog() {
		return doLog;
	}

	public void setDoLog(boolean doLog) {
		this.doLog = doLog;
	}

	public boolean executeSQLFile(File updFile,Connection con){
		
		boolean return_value = true;
		
		try{
			Connection connection = (con == null)
									 ? ConnectionManager.getInstance().getConnection()
									 : con;
			
			
			
			SqlFile sqlFile = new SqlFile(updFile);
			sqlFile.setConnection(con);
			sqlFile.execute();
						
		}catch(Exception e){
			return_value = false;			
		}
		
		return return_value;
	}
	
	public boolean updateUserPws(String pws){
		
		boolean return_value = true;
		
		Connection con = ConnectionManager.getInstance().getConnection();
		PreparedStatement ps = null;
				
		if (con != null){
			try{
				
				ps = con.prepareStatement("ALTER USER SA SET PASSWORD '"+pws+"'");
				ps.execute();		
				con.commit();
			
			}catch(SQLException sql){
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				sql.printStackTrace();
				return_value = false;
			}finally{
				try {
					ps.close();
				} catch (SQLException e) {
					ps = null;
				}
				try {
					con.close();
				} catch (SQLException e) {
					con = null;
				}				
				
			}
			
			return return_value;
			
		}else{
			
			return return_value;
			
		}
		
	}

	public boolean checkDBFiles(){

		String dataPath = Activator.getDefault()
				   				   .getStateLocation()
				   				   .toFile()
				   				   .toString() + File.separator + "datidb" + File.separator + "winkhouse.script";

		String dataPath2 = Activator.getDefault()
				   		 		    .getStateLocation()
				   		 		    .toFile()
				   				    .toString() + File.separator + "datidb" + File.separator + "winkhouse.properties";
		
		File dbfile = new File(dataPath);
		if (dbfile.exists()){
			return true;
		}else{
			
			File f = new File(Activator.getDefault()
	   				   				   .getStateLocation()
	   				   				   .toFile()
	   				   				   .toString() + File.separator + "datidb");
			if (!f.exists()){
				f.mkdirs();
			}
			
			BufferedInputStream bis = new BufferedInputStream(getClass().getResourceAsStream("/winkhouse/configuration/winkhouse.script"));
			
			byte[] tmp = null;
			try {
				tmp = new byte[bis.available()];
				bis.read(tmp);
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bis.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(new File(dataPath));
				fos.write(tmp);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
			
			BufferedInputStream bis2 = new BufferedInputStream(getClass().getResourceAsStream("/winkhouse/configuration/winkhouse.properties"));
			
			byte[] tmp2 = null;
			try {
				tmp2 = new byte[bis2.available()];
				bis2.read(tmp2);
				bis2.close();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bis2.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			FileOutputStream fos2;
			try {
				fos = new FileOutputStream(new File(dataPath2));
				fos.write(tmp2);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
		return true;
	}
}
