package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.ContattiVO;


public class ContattiDAO extends BaseDAO{

	public final static String LISTA_CONTATTI_ANAGRAFICA = "LISTA_CONTATTI_ANAGRAFICA";
	public final static String LISTA_CONTATTI_TIPOLOGIA = "LISTA_CONTATTI_TIPOLOGIA";
	public final static String LISTA_CONTATTI_AGENTE = "LISTA_CONTATTI_AGENTE";
	public final static String LISTA_CONTATTI_CONTATTO = "LISTA_CONTATTI_CONTATTO";
	public final static String INSERT_CONTATTI = "INSERT_CONTATTI";
	public final static String UPDATE_CONTATTI = "UPDATE_CONTATTI";
	public final static String UPDATE_CONTATTI_TIPOLOGIA = "UPDATE_CONTATTI_TIPOLOGIA";
	public final static String DELETE_CONTATTI = "DELETE_CONTATTI";
	public final static String DELETE_CONTATTI_ANAGRAFICA = "DELETE_CONTATTI_ANAGRAFICA";
	public final static String DELETE_CONTATTI_AGENTE = "DELETE_CONTATTI_AGENTE";
	public final static String CONTATTI_BY_ID = "CONTATTI_BY_ID";
	public final static String UPDATE_CONTATTI_AGENTEUPDATE = "UPDATE_CONTATTI_AGENTE_UPDATE";
	
	
	public ContattiDAO() {
	}
	
	public <T> ArrayList<T> listByAnagrafica(String classType, Integer codAnagrafica){
		return super.getObjectsByIntFieldValue(classType, LISTA_CONTATTI_ANAGRAFICA, codAnagrafica);
	}

	public <T> ArrayList<T> listByTipologia(String classType, Integer codTipologiaContatti){
		return super.getObjectsByIntFieldValue(classType, LISTA_CONTATTI_TIPOLOGIA, codTipologiaContatti);
	}
	
	public <T> ArrayList<T> listByAgente(String classType, Integer codAgente){
		return super.getObjectsByIntFieldValue(classType, LISTA_CONTATTI_AGENTE, codAgente);
	}
	
	public boolean delete(Integer codContatto, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_CONTATTI, codContatto, con, doCommit);
	}	

	public boolean deleteByAnagrafica(Integer codAnagrafica, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_CONTATTI_ANAGRAFICA, codAnagrafica, con, doCommit);
	}	

	public boolean deleteByAgente(Integer codAgente, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_CONTATTI_AGENTE, codAgente, con, doCommit);
	}	

	public Object getContattoById(String classType, Integer codContatto){
		return super.getObjectById(classType, CONTATTI_BY_ID, codContatto);
	}
	
	public boolean updateTipologiaContatti(Integer codTipologiaContattiOld, Integer codTipologiaContattiNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_CONTATTI_TIPOLOGIA, codTipologiaContattiNew, codTipologiaContattiOld, con, doCommit);
	}	

	public <T> ArrayList<T> getContattoByContatto(String classType, String contatto){
		return super.getObjectsByStringFieldValue(classType, LISTA_CONTATTI_CONTATTO, contatto);
	}
	
	public boolean saveUpdate(ContattiVO cVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((cVO.getCodContatto() == null) || (cVO.getCodContatto() == 0))
						? getQuery(INSERT_CONTATTI)
						: getQuery(UPDATE_CONTATTI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setString(1, cVO.getContatto());
			ps.setString(2, cVO.getDescrizione());
			
			if (cVO.getCodTipologiaContatto() == null) {
				ps.setNull(3, java.sql.Types.INTEGER);
			} else {
				ps.setInt(3, cVO.getCodTipologiaContatto());
			}

			if (cVO.getCodAnagrafica() == null) {
				ps.setNull(4, java.sql.Types.INTEGER);
			} else {
				ps.setInt(4, cVO.getCodAnagrafica());
			}

			if (cVO.getCodAgente() == null) {
				ps.setNull(5, java.sql.Types.INTEGER);
			} else {
				ps.setInt(5, cVO.getCodAgente());
			}
			
			if ((cVO.getCodContatto() != null) &&
				(cVO.getCodContatto() != 0)){
				ps.setInt(6, cVO.getCodContatto());
			}
			ps.executeUpdate();
			if ((cVO.getCodContatto() == null) ||
				(cVO.getCodContatto() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					cVO.setCodContatto(key);
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

	public boolean saveUpdateWithException(ContattiVO cVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((cVO.getCodContatto() == null) || (cVO.getCodContatto() == 0))
						? getQuery(INSERT_CONTATTI)
						: getQuery(UPDATE_CONTATTI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setString(1, cVO.getContatto());
			ps.setString(2, cVO.getDescrizione());
			
			if (cVO.getCodTipologiaContatto() == null) {
				ps.setNull(3, java.sql.Types.INTEGER);
			} else {
				ps.setInt(3, cVO.getCodTipologiaContatto());
			}

			if (cVO.getCodAnagrafica() == null) {
				ps.setNull(4, java.sql.Types.INTEGER);
			} else {
				ps.setInt(4, cVO.getCodAnagrafica());
			}

			if (cVO.getCodAgente() == null) {
				ps.setNull(5, java.sql.Types.INTEGER);
			} else {
				ps.setInt(5, cVO.getCodAgente());
			}
			
			if ((cVO.getCodContatto() != null) &&
				(cVO.getCodContatto() != 0)){
				ps.setInt(6, cVO.getCodContatto());
			}
			
			ps.executeUpdate();
			if ((cVO.getCodContatto() == null) ||
				(cVO.getCodContatto() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					cVO.setCodContatto(key);
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

	public boolean updateContattoAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_CONTATTI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}