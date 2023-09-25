package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.PromemoriaLinksVO;
import winkhouse.vo.PromemoriaOggettiVO;

public class PromemoriaLinksDAO extends BaseDAO {

	public final static String INSERT_PROMEMORIALINKS = "INSERT_PROMEMORIALINKS";
	public final static String DELETE_PROMEMORIALINKS = "DELETE_PROMEMORIALINKS";
	public final static String GET_PROMEMORIALINKS_BY_CODPROMEMORIA = "GET_PROMEMORIALINKS_BY_CODPROMEMORIA";
	public final static String DELETE_PROMEMORIALINKS_BY_CODPROMEMORIA = "DELETE_PROMEMORIALINKS_BY_CODPROMEMORIA";

	public PromemoriaLinksDAO() {
		// TODO Auto-generated constructor stub
	}

	public boolean insert(PromemoriaLinksVO plVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(INSERT_PROMEMORIALINKS);
						
		try{			
			ps = con.prepareStatement(query);			
			ps.setInt(1, plVO.getCodPromemoria());
			ps.setString(2, plVO.getDescrizione());
			ps.setString(3, plVO.getUrilink());
			ps.setBoolean(4, plVO.getIsFile());
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
	
	public boolean delete(PromemoriaLinksVO plVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(DELETE_PROMEMORIALINKS);
						
		try{			
			ps = con.prepareStatement(query);			
			ps.setInt(1, plVO.getCodPromemoria());
			ps.setString(2, plVO.getDescrizione());
			ps.setString(3, plVO.getUrilink());
			ps.setBoolean(4, plVO.getIsFile());
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

	public boolean deleteByCodPromemoria(Integer codPromemoria, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_PROMEMORIALINKS_BY_CODPROMEMORIA, codPromemoria, connection, doCommit);
	}
	
	public ArrayList listByCodPromemoria(String classType, Integer codPromemoria){
		return (ArrayList)super.getObjectsByIntFieldValue(classType, GET_PROMEMORIALINKS_BY_CODPROMEMORIA, codPromemoria);
	}

}