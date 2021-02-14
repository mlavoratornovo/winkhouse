package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.ImmobiliPropietariVO;

public class ImmobiliPropietariDAO extends BaseDAO {

	public final static String INSERT_IMMOBILEPROPIETARIO = "INSERT_IMMOBILIPROPIETARI";
	public final static String DELETE_IMMOBILEPROPIETARIO = "DELETE_IMMOBILEPROPIETARIO";
	public final static String DELETE_IMMOBILEPROPIETARIO_BY_CODIMMOBILE = "DELETE_IMMOBILEPROPIETARIO_BY_CODIMMOBILE";
	public final static String DELETE_IMMOBILEPROPIETARIO_BY_CODANAGRAFICA = "DELETE_IMMOBILEPROPIETARIO_BY_CODANAGRAFICA";
	public final static String LIST_IMMOBILIPROPIETARI_BY_CODIMMOBILE = "LIST_IMMOBILIPROPIETARI_BY_CODIMMOBILE";
	public final static String IMMOBILIPROPIETARI_BY_CODIMMOBILE_CODANAGRAFICA = "IMMOBILIPROPIETARI_BY_CODIMMOBILE_CODANAGRAFICA";
	
	public ImmobiliPropietariDAO() {
		// TODO Auto-generated constructor stub
	}

	public boolean insert(ImmobiliPropietariVO ipVO, Connection connection, Boolean doCommit){
			
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(INSERT_IMMOBILEPROPIETARIO);
						
		try{			
			ps = con.prepareStatement(query);	
			if (ipVO.getCodImmobile() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, ipVO.getCodImmobile());
			}
			
			if (ipVO.getCodAnagrafica() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, ipVO.getCodAnagrafica());
			}

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
				if (generatedkey){
					rs.close();
				}
			} catch (SQLException e) {
				rs = null;
			}
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
	
	public boolean delete(ImmobiliPropietariVO ipVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(DELETE_IMMOBILEPROPIETARIO);
						
		try{			
			ps = con.prepareStatement(query);			
			ps.setInt(1, ipVO.getCodImmobile());
			ps.setInt(2, ipVO.getCodAnagrafica());
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
				if (generatedkey){
					rs.close();
				}
			} catch (SQLException e) {
				rs = null;
			}
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

	public boolean deleteByCodImmobile(Integer codImmobile, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(DELETE_IMMOBILEPROPIETARIO_BY_CODIMMOBILE);
						
		try{			
			ps = con.prepareStatement(query);			
			ps.setInt(1, codImmobile);			
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
				if (generatedkey){
					rs.close();
				}
			} catch (SQLException e) {
				rs = null;
			}
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

	public boolean deleteByCodAnagrafica(Integer codAnagrafica, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(DELETE_IMMOBILEPROPIETARIO_BY_CODANAGRAFICA);
						
		try{			
			ps = con.prepareStatement(query);			
			ps.setInt(1, codAnagrafica);
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
				if (generatedkey){
					rs.close();
				}
			} catch (SQLException e) {
				rs = null;
			}
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

	public ArrayList getImmobiliPropietariByCodImmobile(Integer codImmobile){
		return super.getObjectsByIntFieldValue(ImmobiliPropietariVO.class.getName(), LIST_IMMOBILIPROPIETARI_BY_CODIMMOBILE, codImmobile);
	}

	public ImmobiliPropietariVO getImmobiliPropietariByCodImmobileCodAnagrafica(Integer codImmobile,Integer codAnagrafica){
		
		ImmobiliPropietariVO returnValue = null;		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(IMMOBILIPROPIETARI_BY_CODIMMOBILE_CODANAGRAFICA);
		con = ConnectionManager.getInstance().getConnectionSelectConnection();
		if (con != null){
			try{
				
				ps = con.prepareStatement(query);			
				ps.setInt(1, codImmobile);
				ps.setInt(2, codAnagrafica);
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue = new ImmobiliPropietariVO(rs);
					break;
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
	
}

