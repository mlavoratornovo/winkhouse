package winkhouse.db.server;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Text;

public class UpdateDBHelper {

	private Text console = null;
	
	public UpdateDBHelper() {
		// TODO Auto-generated constructor stub
	}

	private File getUpdateFile(String filename) throws URISyntaxException{
		
		File f = new File(Platform.getInstanceLocation().getURL().getPath()+ "update_script" + File.separator + filename);
		
		return f;
	}
	
	public boolean goToVersion_1_1(Connection con){
	
		boolean return_value = true;
		
		try {
			if (console != null){
				console.append("CONTROLLO AGENTI INIZIO\n");
			}
			return_value = checkFieldExist("SELECT * FROM AGENTI LIMIT 1", "CODUSERUPDATE", 
					   					   this.getClass().getMethod("executeFile", new Class[]{String.class,Connection.class}),
					   					   new Object[]{"optimisticlock.sql",con});
			if (console != null){
				console.append("CONTROLLO AGENTI FINE\n");
			}
			
			if (return_value){
				if (console != null){
					console.append("CONTROLLO PERMESSI INIZO\n");
				}
				
				return_value = checkTableExist("PERMESSI", 
				   							   this.getClass().getMethod("executeFile", new Class[]{String.class,Connection.class}),
				   							   new Object[]{"profilazione.sql",con});
				if (console != null){
					console.append("CONTROLLO PERMESSI FINE\n");
				}

			
				if (return_value){
					if (console != null){
						console.append("CONTROLLO PROMEMORIA INIZO\n");
					}
					
					return_value = checkTableExist("PROMEMORIA", 
			   									   this.getClass().getMethod("executeFile", new Class[]{String.class,Connection.class}),
			   									   new Object[]{"promemoria.sql",con});
					if (console != null){
						console.append("CONTROLLO PROMEMORIA FINE\n");
					}
				
					if (return_value){
						if (console != null){
							console.append("CONTROLLO WINKSYS INIZO\n");
						}
						
						return_value = checkTableExist("WINKSYS", 
													   this.getClass().getMethod("executeFile", new Class[]{String.class,Connection.class}),
													   new Object[]{"syssettingtable.sql",con});
						if (console != null){
							console.append("CONTROLLO WINKSYS FINE\n");
						}

					}
					
				}
				
			}
			
		} catch (SecurityException e) {
			for (int i = 0; i < e.getStackTrace().length; i++) {
				console.append(((StackTraceElement)e.getStackTrace()[i]).toString() + "\n");
			}				
			return_value = false;
		} catch (NoSuchMethodException e) {
			for (int i = 0; i < e.getStackTrace().length; i++) {
				console.append(((StackTraceElement)e.getStackTrace()[i]).toString() + "\n");
			}							
			return_value = false;
		}
		
		return return_value;
	}
	
	public boolean executeFile(String updFile, Connection con){
		
		boolean return_value = true;
		
		try {
			return_value = HSQLDBHelper.getInstance().executeSQLFile(getUpdateFile(updFile),con);
		} catch (URISyntaxException e) {
			return_value = false;		}
		
		return return_value;
		
	}
	
	public boolean checkFieldExist(String sqlQuery, String fieldName, Method notFoundMethod, Object[] nFMParams){
		
		boolean return_value = true;
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try{

			if (nFMParams[1] != null){
				ps = ((Connection)nFMParams[1]).prepareStatement(sqlQuery);
				rs = ps.executeQuery();			
				rs.findColumn(fieldName);
			}
		}catch(SQLException sql){
			try {
				return_value = (Boolean)notFoundMethod.invoke(this, nFMParams);
			} catch (IllegalArgumentException e) {
				return_value = false;
			} catch (IllegalAccessException e) {
				return_value = false;
			} catch (InvocationTargetException e) {
				return_value = false;
			}
		}finally{
		
			try {
				if (rs != null){
					rs.close();
				}
			} catch (SQLException e) {
				rs = null;
			}
			try {
				if (ps != null){		
					ps.close();
				}
			} catch (SQLException e) {
				ps = null;
			}		
			
		}			
		
		return return_value;
		
	}
	
	public boolean checkTableExist(String tableName,Method notFoundMethod, Object[] nFMParams){
		
		boolean return_value = true;
		boolean isPresent = false;
		
		if (nFMParams[1] != null){
			String[] tablesType = {"TABLE"};
			try {
				ResultSet rs = ((Connection)nFMParams[1]).getMetaData()
				   				  .getTables(null, "PUBLIC", tableName, tablesType);
				
				while (rs.next()){
					isPresent = true;
				}
				if(!isPresent){
					try {
						return_value = (Boolean)notFoundMethod.invoke(this, nFMParams);
					} catch (IllegalArgumentException e) {
						return_value = false;
					} catch (IllegalAccessException e) {
						return_value = false;
					} catch (InvocationTargetException e) {
						return_value = false;
					}
				}
			} catch (SQLException e) {
				return_value = false;
			}
		}
		
		return return_value;
		
	}

	public Text getConsole() {
		return console;
	}

	public void setConsole(Text console) {
		this.console = console;
	}

}
