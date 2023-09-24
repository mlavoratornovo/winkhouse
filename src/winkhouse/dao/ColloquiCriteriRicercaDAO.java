package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.ColloquiCriteriRicercaVO;

public class ColloquiCriteriRicercaDAO extends BaseDAO {

	public final static String LISTA_COLLOQUIOCRITERIRICERCA_BY_COLLOQUIO = "LISTA_COLLOQUIO_CRITERI_RICERCA_BY_COLLOQUIO";
	public final static String LISTA_COLLOQUIOCRITERIRICERCA_BY_RICERCA = "LISTA_COLLOQUIO_CRITERI_RICERCA_BY_RICERCA";
	public final static String LISTA_COLLOQUIOCRITERIRICERCA_BY_CRITERIA = "LISTA_COLLOQUIOCRITERIRICERCA_BY_CRITERIA";
	public final static String COLLOQUIOCRITERIRICERCA_BY_ID = "COLLOQUIO_CRITERI_RICERCA_BY_ID";	
	public final static String INSERT_COLLOQUIOCRITERIRICERCA = "INSERT_COLLOQUIO_CRITERI_RICERCA";  
	public final static String UPDATE_COLLOQUIOCRITERIRICERCA = "UPDATE_COLLOQUIO_CRITERI_RICERCA";
	public final static String DELETE_COLLOQUIOCRITERIRICERCA = "DELETE_COLLOQUIO_CRITERI_RICERCA";
	public final static String DELETE_COLLOQUIOCRITERIRICERCA_BY_COLLOQUIO = "DELETE_COLLOQUIO_CRITERI_RICERCA_BY_COLLOQUIO";	
	public final static String DELETE_COLLOQUIOCRITERIRICERCA_BY_RICERCA = "DELETE_COLLOQUIO_CRITERI_RICERCA_BY_RICERCA";
	public final static String UPDATE_COLLOQUIOCRITERIRICERCA_AGENTEUPDATE = "UPDATE_COLLOQUI_AGENTE_UPDATE";
	
	public ColloquiCriteriRicercaDAO() {
		
	}
	
	public ArrayList getColloquiCriteriRicercaByColloquio(String classType, Integer codColloquio){
		return super.getObjectsByIntFieldValue(classType, LISTA_COLLOQUIOCRITERIRICERCA_BY_COLLOQUIO, codColloquio);
	}

	public ArrayList getColloquiCriteriRicercaByRicerca(String classType, Integer codRicerca){
		return super.getObjectsByIntFieldValue(classType, LISTA_COLLOQUIOCRITERIRICERCA_BY_RICERCA, codRicerca);
	}
	
	public Object getColloquiCriteriRicercaById(String classType, Integer codColloquiCriteriRicerca){
		return super.getObjectById(classType, COLLOQUIOCRITERIRICERCA_BY_ID, codColloquiCriteriRicerca);
	}
	
	public boolean delete(Integer codColloquiCriteriRicerca, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_COLLOQUIOCRITERIRICERCA, codColloquiCriteriRicerca, con, doCommit);
	}
	
	public boolean deleteByColloquio(Integer codColloquio, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_COLLOQUIOCRITERIRICERCA_BY_COLLOQUIO, codColloquio, con, doCommit);
	}

	public boolean deleteByRicerca(Integer codRicerca, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_COLLOQUIOCRITERIRICERCA_BY_RICERCA, codRicerca, con, doCommit);
	}

	public boolean saveUpdate(ColloquiCriteriRicercaVO ccrVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((ccrVO.getCodCriterioRicerca() == null) || (ccrVO.getCodCriterioRicerca() == 0))
						? getQuery(INSERT_COLLOQUIOCRITERIRICERCA)
						:getQuery(UPDATE_COLLOQUIOCRITERIRICERCA);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);				
			
			if (ccrVO.getCodColloquio() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, ccrVO.getCodColloquio());
			}
			
			ps.setString(2, ccrVO.getGetterMethodName());
			ps.setString(3, ccrVO.getFromValue());
			ps.setString(4, ccrVO.getToValue());
			ps.setString(5, ccrVO.getLogicalOperator());
			
			if (ccrVO.getCodRicerca() == null) {
				ps.setNull(6, java.sql.Types.INTEGER);
			} else {
				ps.setInt(6, ccrVO.getCodRicerca());
			}
						
			ps.setBoolean(7, ccrVO.getIsPersonal());
			if ((ccrVO.getCodCriterioRicerca() != null) && 
				(ccrVO.getCodCriterioRicerca() != 0)){
				ps.setInt(8, ccrVO.getCodCriterioRicerca());
			}
			ps.executeUpdate();
			if ((ccrVO.getCodCriterioRicerca() == null) || 
				(ccrVO.getCodCriterioRicerca() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					ccrVO.setCodCriterioRicerca(key);
					generatedkey = true;
					break;
				}
			}
			returnValue = true;
			if (doCommit){
				con.commit();
			}
		}catch(SQLException sql){
			sql.printStackTrace();
			if (doCommit){
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}			
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

	public boolean saveUpdateWithException(ColloquiCriteriRicercaVO ccrVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((ccrVO.getCodCriterioRicerca() == null) || (ccrVO.getCodCriterioRicerca() == 0))
						? getQuery(INSERT_COLLOQUIOCRITERIRICERCA)
						: getQuery(UPDATE_COLLOQUIOCRITERIRICERCA);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);				
			ps.setInt(1, ccrVO.getCodColloquio());
			ps.setString(2, ccrVO.getGetterMethodName());
			ps.setString(3, ccrVO.getFromValue());
			ps.setString(4, ccrVO.getToValue());
			ps.setString(5, ccrVO.getLogicalOperator());
			ps.setInt(6, ccrVO.getCodRicerca());
			ps.setBoolean(7, ccrVO.getIsPersonal());
			if ((ccrVO.getCodCriterioRicerca() != null) && 
				(ccrVO.getCodCriterioRicerca() != 0)){
				ps.setInt(8, ccrVO.getCodCriterioRicerca());
			}
			ps.executeUpdate();
			if ((ccrVO.getCodCriterioRicerca() == null) || 
				(ccrVO.getCodCriterioRicerca() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					ccrVO.setCodCriterioRicerca(key);
					generatedkey = true;
					break;
				}
			}
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
			throw sql;
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
	
	
	public ArrayList getCriteriRicercaByCriteria(String classType,
												 String getterMethodName, 
												 String fromValue,
												 String toValue,
												 String logicalOperator){
		
		ArrayList returnValue = new ArrayList();		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LISTA_COLLOQUIOCRITERIRICERCA_BY_CRITERIA);
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);			
			ps.setString(1, getterMethodName);
			ps.setString(2, fromValue);
			ps.setString(3, toValue);
			ps.setString(4, logicalOperator);
			
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
			}
			*/
		}		
				
		return returnValue;
	}	

	public boolean updateCriteriRicercaAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_COLLOQUIOCRITERIRICERCA_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
