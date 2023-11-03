package winkhouse.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;

import winkhouse.db.ConnectionManager;

import com.google.api.client.util.IOUtils;


public class CheckDBDAO extends BaseDAO {
	
	public final static String SET_TRANSACTION_CONTROL_MVLOCK = "SET_TRANSACTION_CONTROL_MVLOCK";
	
	public CheckDBDAO() {

	}
	
	public void checkImmobiliPropietari(){
		
		Connection con = ConnectionManager.getInstance().getConnectionSelectConnection();
		boolean isPresent = false;
		if (con != null){
			String[] tablesType = {"TABLE"};
			try {
				ResultSet rs = con.getMetaData()
				   				  .getTables(null, "PUBLIC", "IMMOBILIPROPIETARI", tablesType);
				
				while (rs.next()){
					isPresent = true;
				}
				if(!isPresent){
					createImmobiliPropietari();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void checkImmobiliNCivico(){
		
		Connection con = ConnectionManager.getInstance().getConnectionSelectConnection();
		try {
			con.prepareStatement("SELECT NCIVICO FROM IMMOBILI");
		} catch (SQLException e1) {
			createImmobiliNCivico();
		}
	}
	
	public void createImmobiliNCivico(){
		
		InputStream is = getClass().getClassLoader()
								   .getResourceAsStream("winkhouse/configuration/immobiliNcivico.sql");
		
		try {
			File tempFile = File.createTempFile("immobilencivico", "sql");
			tempFile.deleteOnExit();
			FileOutputStream out = new FileOutputStream(tempFile);
			IOUtils.copy(is, out);
			SqlFile sqlf = new SqlFile(tempFile);
			Connection con = ConnectionManager.getInstance().getConnection();
			sqlf.setConnection(con);
			try {
				sqlf.execute();
				con.commit();
			} catch (SqlToolError e) {
				e.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void checkAnagraficheNCivico(){
		
		Connection con = ConnectionManager.getInstance().getConnectionSelectConnection();
		try {
			con.prepareStatement("SELECT NCIVICO FROM ANAGRAFICHE");
		} catch (SQLException e1) {
			createAnagraficheNCivico();
		}
	}
	
	public void createAnagraficheNCivico(){
		
		InputStream is = getClass().getClassLoader()
								   .getResourceAsStream("winkhouse/configuration/anagraficheNcivico.sql");
		
		try {
			File tempFile = File.createTempFile("anagraficancivico", "sql");
			tempFile.deleteOnExit();
			FileOutputStream out = new FileOutputStream(tempFile);
			IOUtils.copy(is, out);
			SqlFile sqlf = new SqlFile(tempFile);
			Connection con = ConnectionManager.getInstance().getConnection();
			sqlf.setConnection(con);
			try {
				sqlf.execute();
				con.commit();
			} catch (SqlToolError e) {
				e.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void checkAttributeEnum(){
		
		Connection con = ConnectionManager.getInstance().getConnectionSelectConnection();
		try {
			con.prepareStatement("SELECT ENUMFIELDVALUES FROM ATTRIBUTE");
		} catch (SQLException e1) {
			createAttributeEnum();
		}
	}
	
	public void createAttributeEnum(){
		
		InputStream is = getClass().getClassLoader()
								   .getResourceAsStream("winkhouse/configuration/attributeEnumFieldValues.sql");
		
		try {
			File tempFile = File.createTempFile("attributeenumfieldvalues", "sql");
			tempFile.deleteOnExit();
			FileOutputStream out = new FileOutputStream(tempFile);
			IOUtils.copy(is, out);
			SqlFile sqlf = new SqlFile(tempFile);
			Connection con = ConnectionManager.getInstance().getConnection();
			sqlf.setConnection(con);
			try {
				sqlf.execute();
				con.commit();
			} catch (SqlToolError e) {
				e.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void checkPromemoriaOggetti(){
		
		Connection con = ConnectionManager.getInstance().getConnectionSelectConnection();
		boolean isPresent = false;
		if (con != null){
			String[] tablesType = {"TABLE"};
			try {
				ResultSet rs = con.getMetaData()
				   				  .getTables(null, "PUBLIC", "PROMEMORIAOGGETTI", tablesType);
				
				while (rs.next()){
					isPresent = true;
				}
				if(!isPresent){
					createPromemoriaOggetti();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void createPromemoriaOggetti(){
		
//		InputStream is = getClass().getClassLoader()
//								   .getResourceAsStream("winkhouse/configuration/promemoriaoggetti.sql");
//		InputStreamReader isr = new InputStreamReader(is);
//		try {
//			SqlFile sqlf = new SqlFile(isr, "promemoriaoggetti", null, "Cp1252", false);
//			Connection con = ConnectionManager.getInstance().getConnection();
//			sqlf.setConnection(con);
//			try {
//				sqlf.execute();
//				con.commit();
//			} catch (SqlToolError e) {
//				e.printStackTrace();
//				try {
//					con.rollback();
//				} catch (SQLException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//				try {
//					con.rollback();
//				} catch (SQLException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}

	public void createImmobiliPropietari(){
		
//		InputStream is = getClass().getClassLoader()
//								   .getResourceAsStream("winkhouse/configuration/immobili-propietari.sql");
//		InputStreamReader isr = new InputStreamReader(is);
//		try {
//			SqlFile sqlf = new SqlFile(isr, "create_immobilipropietari", null, "Cp1252", false);
//			Connection con = ConnectionManager.getInstance().getConnection();
//			sqlf.setConnection(con);
//			try {
//				sqlf.execute();
//				con.commit();
//			} catch (SqlToolError e) {
//				e.printStackTrace();
//				try {
//					con.rollback();
//				} catch (SQLException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//				try {
//					con.rollback();
//				} catch (SQLException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}

	public void checkComuni(){
		
		Connection con = ConnectionManager.getInstance().getConnectionSelectConnection();
		boolean isPresent = false;
		if (con != null){
			String[] tablesType = {"TABLE"};
			try {
				ResultSet rs = con.getMetaData()
				   				  .getTables(null, "PUBLIC", "COMUNI", tablesType);
				
				while (rs.next()){
					isPresent = true;
				}
				if(!isPresent){
					createComuni();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void checkPromemoriaLinks(){
		
		Connection con = ConnectionManager.getInstance().getConnectionSelectConnection();
		boolean isPresent = false;
		if (con != null){
			String[] tablesType = {"TABLE"};
			try {
				ResultSet rs = con.getMetaData()
				   				  .getTables(null, "PUBLIC", "PROMEMORIALINKS", tablesType);
				
				while (rs.next()){
					isPresent = true;
				}
				if(!isPresent){
					createPromemoriaLinks();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void createComuni(){
		
//		InputStream is = getClass().getClassLoader()
//								   .getResourceAsStream("winkhouse/configuration/create_and_fill_comuni.sql");
//		InputStreamReader isr = new InputStreamReader(is);
//		try {
//			SqlFile sqlf = new SqlFile(isr, "create_comuni", null, "Cp1252", false);
//			Connection con = ConnectionManager.getInstance().getConnection();
//			sqlf.setConnection(con);
//			try {
//				sqlf.execute();
//				con.commit();
//			} catch (SqlToolError e) {
//				e.printStackTrace();
//				try {
//					con.rollback();
//				} catch (SQLException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//				try {
//					con.rollback();
//				} catch (SQLException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}

	public void checkWinkGCalendar(){
		
		Connection con = ConnectionManager.getInstance().getConnectionSelectConnection();
		boolean isPresent = false;
		if (con != null){
			String[] tablesType = {"TABLE"};
			try {
				ResultSet rs = con.getMetaData()
				   				  .getTables(null, "PUBLIC", "WINKGCALENDAR", tablesType);
				
				while (rs.next()){
					isPresent = true;
				}
				if(!isPresent){
					createWinkGCalendar();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void createWinkGCalendar(){
		
		InputStream is = getClass().getClassLoader()
				   				   .getResourceAsStream("winkhouse/configuration/winkGCalendar.sql");
		InputStreamReader isr = new InputStreamReader(is);
		
		try {
			
			SqlFile sqlf = new SqlFile(isr, "create_gcalendar", null, "Cp1252", false, null);
			Connection con = ConnectionManager.getInstance().getConnection();
			sqlf.setConnection(con);
			try {
				sqlf.execute();
				con.commit();
			} catch (SqlToolError e) {
				e.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void createPromemoriaLinks(){
		
		InputStream is = getClass().getClassLoader()
								   .getResourceAsStream("winkhouse/configuration/promemorialinks_setdefaultnulls.sql");
		InputStreamReader isr = new InputStreamReader(is);
		
		try {
		
			SqlFile sqlf = new SqlFile(isr,"",null,"Cp1252",false,null); 
			Connection con = ConnectionManager.getInstance().getConnection();
			sqlf.setConnection(con);
			try {
				sqlf.execute();
				con.commit();
			} catch (SqlToolError e) {
				e.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setTransactionControl(){
		if (executeAlterDB(SET_TRANSACTION_CONTROL_MVLOCK, null)){
			ConnectionManager.getInstance().resetConnections();
		}
	}
}
