package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.AffittiAnagraficheVO;


public class AffittiAnagraficheDAO extends BaseDAO {

	public final static String FIND_AFFITTIANAGRAFICHE_BY_ID = "FIND_AFFITTIANAGRAFICHE_BY_ID";
	public final static String FIND_AFFITTIANAGRAFICHE_BY_CODAFFITTO = "FIND_AFFITTIANAGRAFICHE_BY_CODAFFITTO";
	public final static String FIND_AFFITTIANAGRAFICHE_BY_CODAFFITTO_CODANAGRAFICA = "FIND_AFFITTIANAGRAFICHE_BY_CODAFFITTO_CODANAGRAFICA";
	public final static String INSERT_AFFITTIANAGRAFICHE = "INSERT_AFFITTIANAGRAFICHE";
	public final static String UPDATE_AFFITTIANAGRAFICHE = "UPDATE_AFFITTIANAGRAFICHE";
	public final static String DELETE_AFFITTIANAGRAFICHE = "DELETE_AFFITTIANAGRAFICHE";
	public final static String DELETE_AFFITTIANAGRAFICHE_BY_CODAFFITTO = "DELETE_AFFITTIANAGRAFICHE_BY_CODAFFITTO";
	public final static String DELETE_AFFITTIANAGRAFICHE_BY_CODANAGRAFICA = "DELETE_AFFITTIANAGRAFICHE_BY_CODANAGRAFICA";
	public final static String UPDATE_AFFITTIANAGRAFICHE_AGENTEUPDATE = "UPDATE_AFFITTIANAGRAFICHE_AGENTE_UPDATE";
	
	public AffittiAnagraficheDAO() {}

	public Object getAffittiAnagraficheByID(String className, Integer codAffittiAnagrafiche){
		return super.getObjectById(className, FIND_AFFITTIANAGRAFICHE_BY_ID, codAffittiAnagrafiche);
	}
	
	public ArrayList getAffittiAnagraficheByAffitto(String className, Integer codAffitto){
		return super.getObjectsByIntFieldValue(className, FIND_AFFITTIANAGRAFICHE_BY_CODAFFITTO, codAffitto);
	}

	public Object getAffittiAnagraficheByAffittoAnagrafica(String className,Integer codAffitto, Integer codAnagrafica){
		
		Object returnValue = null;
		
		ResultSet rs = null;
		Connection con = ConnectionManager.getInstance().getConnectionSelectConnection();
		PreparedStatement ps = null;
		String query = getQuery(FIND_AFFITTIANAGRAFICHE_BY_CODAFFITTO_CODANAGRAFICA);
						
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setInt(1, codAffitto);
			ps.setInt(2, codAnagrafica);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue = getRowObject(className, rs);
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
				
		return returnValue;

	}
	
	public boolean deleteAffittiAnagraficheByID(Integer codAffittiAnagrafiche, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_AFFITTIANAGRAFICHE, codAffittiAnagrafiche, connection, doCommit);
	}

	public boolean deleteAffittiAnagraficheByCodAffitto(Integer codAffitto, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_AFFITTIANAGRAFICHE_BY_CODAFFITTO, codAffitto, connection, doCommit);
	}

	public boolean deleteAffittiAnagraficheByCodAnagrafica(Integer codAnagrafica, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_AFFITTIANAGRAFICHE_BY_CODANAGRAFICA, codAnagrafica, connection, doCommit);
	}
	
	public boolean saveUpdate(AffittiAnagraficheVO aaVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aaVO.getCodAffittiAnagrafiche() == null) || (aaVO.getCodAffittiAnagrafiche() == 0))
						? getQuery(INSERT_AFFITTIANAGRAFICHE)
						: getQuery(UPDATE_AFFITTIANAGRAFICHE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			if (aaVO.getCodAffitto() == null){
				ps.setNull(1, java.sql.Types.INTEGER);
			}else{
				ps.setInt(1, aaVO.getCodAffitto());
			}
			if (aaVO.getCodAnagrafica() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			}else{
				ps.setInt(2, aaVO.getCodAnagrafica());	
			}			
			ps.setString(3, aaVO.getNota());
			if ((aaVO.getCodAffittiAnagrafiche() != null) &&
				(aaVO.getCodAffittiAnagrafiche() != 0)){
				ps.setInt(4, aaVO.getCodAffittiAnagrafiche());
			}
			ps.executeUpdate();
			if ((aaVO.getCodAffittiAnagrafiche() == null) ||
				(aaVO.getCodAffittiAnagrafiche() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aaVO.setCodAffittiAnagrafiche(key);
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

	public boolean saveUpdateWithException(AffittiAnagraficheVO aaVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aaVO.getCodAffittiAnagrafiche() == null) || (aaVO.getCodAffittiAnagrafiche() == 0))
						? getQuery(INSERT_AFFITTIANAGRAFICHE)
						: getQuery(UPDATE_AFFITTIANAGRAFICHE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			if (aaVO.getCodAffitto() == null){
				ps.setNull(1, java.sql.Types.INTEGER);
			}else{
				ps.setInt(1, aaVO.getCodAffitto());
			}
			if (aaVO.getCodAnagrafica() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			}else{
				ps.setInt(2, aaVO.getCodAnagrafica());	
			}			
			ps.setString(3, aaVO.getNota());
			if ((aaVO.getCodAffittiAnagrafiche() != null) &&
				(aaVO.getCodAffittiAnagrafiche() != 0)){
				ps.setInt(4, aaVO.getCodAffittiAnagrafiche());
			}
			ps.executeUpdate();
			if ((aaVO.getCodAffittiAnagrafiche() == null) ||
				(aaVO.getCodAffittiAnagrafiche() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aaVO.setCodAffittiAnagrafiche(key);
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

	public boolean updateAffittiAnagraficheAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_AFFITTIANAGRAFICHE_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
