package winkhouse.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import winkhouse.configuration.EnvSettingsFactory;
import winkhouse.db.ConnectionManager;


public class BaseDAO {

	public BaseDAO(){}
	
	public String getQuery(String queryName){
		String query = null;
		
		try {
			query = EnvSettingsFactory.getInstance().getQueries()
							  		  .get(queryName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return query;
	}
	
	public Object getRowObject(String classType, ResultSet rs){
		Object returnValue = null;
		try {			
			Class cl = Class.forName(classType);
			Constructor c = cl.getConstructor(ResultSet.class);
			returnValue = c.newInstance(rs);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e){
			e.printStackTrace();
		}
		
		return returnValue;
		
	}
	
	public ArrayList list(String classType, String queryName){
		
		ArrayList returnValue = new ArrayList();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(queryName);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			if (con != null){
				ps = con.prepareStatement(query);
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add(getRowObject(classType, rs));
				}
			}
		}catch(SQLException sql){
			sql.printStackTrace();
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
	/*		try {
				con.close();
			} catch (SQLException e) {
				con = null;
			}*/
			
		}		
		
		return returnValue;
	}	
	
	public Object getObjectById(String classType, String queryName, Integer key){
		
		Object returnValue = null;		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(queryName);
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, key);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue = getRowObject(classType, rs);
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
		/*	try {
				con.close();
			} catch (SQLException e) {
				con = null;
			}*/
			
		}		
				
		return returnValue;
		
	}	

	public ArrayList getObjectsByStringFieldValue(String classType, 
												  String queryName, 
												  String stringFieldValue){
		
		ArrayList returnValue = new ArrayList();		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(queryName);
		con = ConnectionManager.getInstance().getConnectionSelectConnection();
		if (con != null){
			try{
				
				ps = con.prepareStatement(query);
				ps.setString(1, stringFieldValue);
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add(getRowObject(classType, rs));
				}
			}catch(SQLException sql){
				sql.printStackTrace();
			}finally{
				try {
					rs.close();
				} catch (SQLException e) {
					rs = null;
				}
				try {
					ps.close();
				} catch (SQLException e) {
					ps = null;
				}
			/*	try {
					con.close();
				} catch (SQLException e) {
					con = null;
				}*/
				
			}
		}
				
		return returnValue;
		
	}
	
	public ArrayList getObjectsByIntFieldValue(String classType, 
											   String queryName, 
											   Integer integerFieldValue){
		
		ArrayList returnValue = new ArrayList();		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(queryName);
		con = ConnectionManager.getInstance().getConnectionSelectConnection();
		if (con != null){
			try{
				
				ps = con.prepareStatement(query);
				if (integerFieldValue != null) {
					ps.setInt(1, integerFieldValue);
				}				
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add(getRowObject(classType, rs));
				}
			}catch(SQLException sql){
				sql.printStackTrace();
			}finally{
				try {
					rs.close();
				} catch (SQLException e) {
					rs = null;
				}
				try {
					ps.close();
				} catch (SQLException e) {
					ps = null;
				}
			}		
		}
		return returnValue;
		
	}	

	
	public ArrayList getObjectsByIntStrFieldValue(String classType, 
											      String queryName, 
											      Integer integerFieldValue,
											      String stringFieldValue){
		
		ArrayList returnValue = new ArrayList();		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(queryName);
		con = ConnectionManager.getInstance().getConnectionSelectConnection();
		if (con != null){
			try{
				
				ps = con.prepareStatement(query);
				if (integerFieldValue != null){
					ps.setInt(1, integerFieldValue);
					ps.setString(2, stringFieldValue);
				}else{
					ps.setString(1, stringFieldValue);
				}
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add(getRowObject(classType, rs));
				}
			}catch(SQLException sql){
				sql.printStackTrace();
			}finally{
				try {
					rs.close();
				} catch (SQLException e) {
					rs = null;
				}
				try {
					ps.close();
				} catch (SQLException e) {
					ps = null;
				}
			}		
		}
		return returnValue;
		
	}	

	
	
	public boolean deleteObjectById(String queryName, 
									Integer key, 
									Connection connection, 
									Boolean doCommit){
		boolean returnValue = false;		
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(queryName);
		try{			
			ps = con.prepareStatement(query);
			ps.setInt(1, key);
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

	public boolean updateByIdWhereId(String queryName,
									 Integer byId,
									 Integer whereId,
									 Connection connection, 
									 Boolean doCommit){
		boolean returnValue = false;		
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(queryName);
		try{			
			ps = con.prepareStatement(query);
			if (byId == null){
				ps.setNull(1, java.sql.Types.INTEGER);
			}else{
				ps.setInt(1, byId);
			}
			
			ps.setInt(2, whereId);
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
	
	public ArrayList listFinder(String classType, String querysql){
		
		ArrayList returnValue = new ArrayList();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
	
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(querysql);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add(getRowObject(classType, rs));
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
			} catch (SQLException e) {
				ps = null;
			}
/*			try {
				con.close();
			} catch (SQLException e) {
				con = null;
			}*/
			
		}		
		
		return returnValue;
	}	

	public boolean executeAlterDB(String queryName, Connection connection){
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(queryName);
		boolean returnValue = true;
		if (con != null){
			try{
				
					ps = con.prepareStatement(query);
					ps.execute();		
					if (connection == null){
						con.commit();
					}
			
			}catch(SQLException sql){
				try {
					if (connection == null){
						con.rollback();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				sql.printStackTrace();
				returnValue = false;
			}finally{
				try {
					ps.close();
				} catch (SQLException e) {
					ps = null;
				}
				try {
					if (connection == null){
						con.close();
					}
				} catch (SQLException e) {
					con = null;
				}
				
			}
		}
		return returnValue;
	}
}
