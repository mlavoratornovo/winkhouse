package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.WinkSysVO;

public class WinkSysDAO extends BaseDAO {

	private static String GET_WINKSYS_PROPERTY = "GET_WINKSYS_PROPERTY";
	private static String GET_WINKSYS_PROPERTIES = "GET_WINKSYS_PROPERTIES";
	private static String INSERT_WINKSYS_PROPERTY = "INSERT_WINKSYS_PROPERTY";
	private static String UPDATE_WINKSYS_PROPERTY = "UPDATE_WINKSYS_PROPERTY";
	
	public WinkSysDAO() {}

	public WinkSysVO getPropertyByName(String propertyName){
		
		ArrayList al = getObjectsByStringFieldValue(WinkSysVO.class.getName(), GET_WINKSYS_PROPERTY, propertyName);
		if ((al != null) && (al.size() > 0)){
			return (WinkSysVO)al.get(0);
		}
		return null;
		
	}
	
	public ArrayList<WinkSysVO> getProperties(){
		
		return (ArrayList<WinkSysVO>)list("winkhouse.vo.WinkSysVO", GET_WINKSYS_PROPERTIES);
		
	}
	
	public boolean insert(WinkSysVO wsVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(INSERT_WINKSYS_PROPERTY);
		
		try{			
			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setString(1, wsVO.getPropertyName());
			ps.setString(2, wsVO.getPropertyValue());
			ps.executeUpdate();
			returnValue = true;
			if (doCommit){
				con.commit();
			}
		}catch(SQLException sql){
			if (doCommit){
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}			
			sql.printStackTrace();
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
			try {
				if (doCommit){
					con.close();
				}
			} catch (SQLException e) {
				con = null;
			}
			
		}		
				
		return returnValue;
	}
	
	public boolean update(WinkSysVO wsVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(UPDATE_WINKSYS_PROPERTY);
		
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);						
			ps.setString(1, wsVO.getPropertyValue());
			ps.setString(2, wsVO.getPropertyName());
			ps.executeUpdate();
			returnValue = true;
			if (doCommit){
				con.commit();
			}
		}catch(SQLException sql){
			if (doCommit){
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}			
			sql.printStackTrace();
		}finally{
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
			try {
				if (doCommit){
					con.close();
				}
			} catch (SQLException e) {
				con = null;
			}
			
		}		
				
		return returnValue;
	}
	
}