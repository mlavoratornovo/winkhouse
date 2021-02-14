package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.TipologiaStanzeVO;


public class TipologiaStanzeDAO extends BaseDAO{

	public final static String LISTA_TIPOLOGIESTANZE = "LIST_TIPOLOGIE_STANZE";
	public final static String TIPOLOGIESTANZE_BY_ID = "TIPOLOGIE_STANZE_BY_ID";
	public final static String TIPOLOGIESTANZE_BY_NAME = "TIPOLOGIE_STANZE_BY_NAME";
	public final static String INSERT_TIPOLOGIESTANZE = "INSERT_TIPOLOGIE_STANZE";
	public final static String UPDATE_TIPOLOGIESTANZE = "UPDATE_TIPOLOGIE_STANZE";
	public final static String DELETE_TIPOLOGIESTANZE = "DELETE_TIPOLOGIE_STANZE";	
	public final static String UPDATE_TIPOLOGIESTANZE_AGENTEUPDATE = "UPDATE_TIPOLOGIESTANZE_AGENTE_UPDATE";
	
	public TipologiaStanzeDAO() {

	}
	
	public ArrayList list(String classType){
		return super.list(classType, LISTA_TIPOLOGIESTANZE);
	}
	
	public Object getTipologiaStanzaById (String classType, Integer codTipologiaStanza){
		return super.getObjectById(classType, TIPOLOGIESTANZE_BY_ID, codTipologiaStanza);
	}
	
	public ArrayList getTipologiaStanzaByDescrizione(String classType, String descrizione){
		return super.getObjectsByStringFieldValue(classType, TIPOLOGIESTANZE_BY_NAME, descrizione);
	}
	
	public boolean delete(Integer codTipologiaStanza, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_TIPOLOGIESTANZE, codTipologiaStanza, con, doCommit);
	}
	
	public boolean saveUpdate(TipologiaStanzeVO tsVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((tsVO.getCodTipologiaStanza() == null) || (tsVO.getCodTipologiaStanza() == 0))
						? getQuery(INSERT_TIPOLOGIESTANZE)
						:getQuery(UPDATE_TIPOLOGIESTANZE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, tsVO.getDescrizione());
			if ((tsVO.getCodTipologiaStanza() != null) && 
				(tsVO.getCodTipologiaStanza() != 0)){
				ps.setInt(2, tsVO.getCodTipologiaStanza());
			}
			ps.executeUpdate();
			if ((tsVO.getCodTipologiaStanza() == null) || 
				(tsVO.getCodTipologiaStanza() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					tsVO.setCodTipologiaStanza(key);
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

	public boolean saveUpdateWithException(TipologiaStanzeVO tsVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((tsVO.getCodTipologiaStanza() == null) || (tsVO.getCodTipologiaStanza() == 0))
						? getQuery(INSERT_TIPOLOGIESTANZE)
						:getQuery(UPDATE_TIPOLOGIESTANZE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, tsVO.getDescrizione());
			if ((tsVO.getCodTipologiaStanza() != null) && 
				(tsVO.getCodTipologiaStanza() != 0)){
				ps.setInt(2, tsVO.getCodTipologiaStanza());
			}
			ps.executeUpdate();
			if ((tsVO.getCodTipologiaStanza() == null) || 
				(tsVO.getCodTipologiaStanza() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					tsVO.setCodTipologiaStanza(key);
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

	public boolean updateTipologiaStanzeAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_TIPOLOGIESTANZE_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
