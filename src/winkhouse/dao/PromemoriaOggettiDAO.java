package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.PromemoriaOggettiVO;

public class PromemoriaOggettiDAO extends BaseDAO {
	
	public final static String INSERT_PROMEMORIAOGGETTI = "INSERT_PROMEMORIAOGGETTI";
	public final static String DELETE_PROMEMORIAOGGETTI = "DELETE_PROMEMORIAOGGETTI";
	public final static String GET_PROMEMORIAOGGETTI_BY_CODPROMEMORIA = "GET_PROMEMORIAOGGETTI_BY_CODPROMEMORIA";
	public final static String DELETE_PROMEMORIAOGGETTI_BY_CODPROMEMORIA = "DELETE_PROMEMORIAOGGETTI_BY_CODPROMEMORIA";
	
	public PromemoriaOggettiDAO() {
		// TODO Auto-generated constructor stub
	}

	public boolean insert(PromemoriaOggettiVO poVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(INSERT_PROMEMORIAOGGETTI);
						
		try{			
			ps = con.prepareStatement(query);			
			ps.setInt(1, poVO.getCodPromemoria());
			ps.setInt(2, poVO.getCodOggetto());
			ps.setInt(3, poVO.getTipo());
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
	
	public boolean delete(PromemoriaOggettiVO poVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(DELETE_PROMEMORIAOGGETTI);
						
		try{			
			ps = con.prepareStatement(query);			
			ps.setInt(1, poVO.getCodPromemoria());
			ps.setInt(2, poVO.getCodOggetto());
			ps.setInt(3, poVO.getTipo());
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
		return super.deleteObjectById(DELETE_PROMEMORIAOGGETTI_BY_CODPROMEMORIA, codPromemoria, connection, doCommit);
	}
	
	public <T> ArrayList<T> listByCodPromemoria(String classType, Integer codPromemoria){
		return super.getObjectsByIntFieldValue(classType, GET_PROMEMORIAOGGETTI_BY_CODPROMEMORIA, codPromemoria);
	}
}