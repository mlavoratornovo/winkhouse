package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.StanzeImmobiliVO;


public class StanzeDAO extends BaseDAO {

	public final static String LISTA_STANZE = "LIST_STANZE";
	public final static String LISTA_STANZE_BY_IMMOBILE = "LISTA_STANZE_BY_IMMOBILE";
	public final static String LISTA_STANZE_BY_TIPOLOGIA = "LISTA_STANZE_BY_TIPOLOGIA";
	public final static String LISTA_STANZE_BY_IMMOBILE_TIPOLOGIA = "LISTA_STANZE_BY_IMMOBILE_TIPOLOGIA";
	public final static String STANZA_BY_ID = "STANZA_BY_ID";	
	public final static String INSERT_STANZA = "INSERT_STANZA";
	public final static String UPDATE_STANZA = "UPDATE_STANZA";
	public final static String UPDATE_STANZA_TIPOLOGIA = "UPDATE_STANZA_TIPOLOGIA";
	public final static String DELETE_STANZA = "DELETE_STANZA";
	public final static String DELETE_STANZA_BY_IMMOBILE = "DELETE_STANZA_BY_IMMOBILE";
	public final static String UPDATE_STANZA_AGENTEUPDATE = "UPDATE_STANZA_AGENTE_UPDATE";
	
	public StanzeDAO() {
	}
	
	public ArrayList list(String classType){
		return super.list(classType, LISTA_STANZE);
	}

	public ArrayList listByImmobile(String classType, 
			 						Integer codImmobile){
		return super.getObjectsByIntFieldValue(classType, LISTA_STANZE_BY_IMMOBILE, codImmobile);
	}
	
	public ArrayList listByTipologia(String classType, 
									 Integer codTipologiaStanze){
		return super.getObjectsByIntFieldValue(classType, LISTA_STANZE_BY_TIPOLOGIA, codTipologiaStanze);
}		

	public ArrayList listByImmobileTipologia(String classType,Integer codImmobile, Integer codTipologia){
		
		ArrayList returnValue = new ArrayList();		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LISTA_STANZE_BY_IMMOBILE_TIPOLOGIA);
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);			
			ps.setInt(1, codImmobile);
			ps.setInt(2, codTipologia);
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
				
		return returnValue;

	}
	
	public boolean delete(Integer codStanza, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_STANZA, codStanza, con, doCommit);
	}

	public boolean deleteByImmobile(Integer codImmobile, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_STANZA_BY_IMMOBILE, codImmobile, con, doCommit);
	}
	
	public Object getStanzaById(String classType, Integer codStanza){
		return super.getObjectById(classType, STANZA_BY_ID, codStanza);
	}

	public boolean updateTipologia(Integer codTipologiaStanzaOld,Integer codTipologiaStanzaNew,Connection con,Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_STANZA_TIPOLOGIA, codTipologiaStanzaNew, codTipologiaStanzaOld, con, doCommit);
	}
	
	public boolean saveUpdate(StanzeImmobiliVO siVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((siVO.getCodStanzeImmobili() == null) || (siVO.getCodStanzeImmobili() == 0))
						? getQuery(INSERT_STANZA)
						:getQuery(UPDATE_STANZA);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setInt(1, siVO.getCodImmobile());
			ps.setInt(2, siVO.getCodTipologiaStanza());
			ps.setInt(3, siVO.getMq());
			if ((siVO.getCodStanzeImmobili() != null) && 
				(siVO.getCodStanzeImmobili() != 0)){
				ps.setInt(4, siVO.getCodStanzeImmobili());
			}
			ps.executeUpdate();
			if ((siVO.getCodStanzeImmobili() == null) || 
				(siVO.getCodStanzeImmobili() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					siVO.setCodStanzeImmobili(key);
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
	
	public boolean saveUpdateWithException(StanzeImmobiliVO siVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((siVO.getCodStanzeImmobili() == null) || (siVO.getCodStanzeImmobili() == 0))
						? getQuery(INSERT_STANZA)
						:getQuery(UPDATE_STANZA);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setInt(1, siVO.getCodImmobile());
			ps.setInt(2, siVO.getCodTipologiaStanza());
			ps.setInt(3, siVO.getMq());
			if ((siVO.getCodStanzeImmobili() != null) && 
				(siVO.getCodStanzeImmobili() != 0)){
				ps.setInt(4, siVO.getCodStanzeImmobili());
			}
			ps.executeUpdate();
			if ((siVO.getCodStanzeImmobili() == null) || 
				(siVO.getCodStanzeImmobili() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					siVO.setCodStanzeImmobili(key);
					generatedkey = true;
					break;
				}
			}
			returnValue = true;
			if (doCommit){
				con.commit();
			}
		}catch(SQLException sql){
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

	public boolean updateStanzeAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_STANZA_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
