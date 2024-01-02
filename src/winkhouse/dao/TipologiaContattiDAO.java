package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;

import winkhouse.db.ConnectionManager;
import winkhouse.db.orm.CayenneContextManager;
import winkhouse.orm.Classicliente;
import winkhouse.orm.Tipologiecontatti;
import winkhouse.vo.TipologiaContattiVO;


public class TipologiaContattiDAO extends BaseDAO{

	public final static String LISTA_TIPOLOGIECONTATTI = "LISTA_TIPOLOGIE_CONTATTI";
	public final static String TIPOLOGIECONTATTI_BY_ID = "TIPOLOGIE_CONTATTI_BY_ID";
	public final static String TIPOLOGIECONTATTI_BY_NAME = "TIPOLOGIE_CONTATTI_BY_NAME";
	public final static String INSERT_TIPOLOGIECONTATTI = "INSERT_TIPOLOGIE_CONTATTI";
	public final static String UPDATE_TIPOLOGIECONTATTI = "UPDATE_TIPOLOGIE_CONTATTI";
	public final static String DELETE_TIPOLOGIECONTATTI = "DELETE_TIPOLOGIE_CONTATTI";	
	public final static String UPDATE_TIPOLOGIECONTATTI_AGENTEUPDATE = "UPDATE_TIPOLOGIECONTATTI_AGENTE_UPDATE";
	
	public TipologiaContattiDAO() {

	}
	public ArrayList<Tipologiecontatti> list(){
		ObjectContext context = CayenneContextManager.getInstance().getContext();
		return new ArrayList<Tipologiecontatti>(ObjectSelect.query(Tipologiecontatti.class).select(context));
	}

	public <T> ArrayList<T> list(String classType){
		return super.list(classType, LISTA_TIPOLOGIECONTATTI);
	}
	
	public Object getTipologiaContattoById (String classType, Integer codTipologiaContatto){
		return super.getObjectById(classType, TIPOLOGIECONTATTI_BY_ID, codTipologiaContatto);
	}
	
	public <T> ArrayList<T> getTipologiaContattoByDescrizione(String classType, String descrizione){
		return super.getObjectsByStringFieldValue(classType, TIPOLOGIECONTATTI_BY_NAME, descrizione);
	}
	
	public boolean delete(Integer codTipologiaContatto, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_TIPOLOGIECONTATTI, codTipologiaContatto, con, doCommit);
	}
	
	public boolean saveUpdate(TipologiaContattiVO tcVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((tcVO.getCodTipologiaContatto() == null) || (tcVO.getCodTipologiaContatto() == 0))
						? getQuery(INSERT_TIPOLOGIECONTATTI)
						:getQuery(UPDATE_TIPOLOGIECONTATTI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, tcVO.getDescrizione());
			if ((tcVO.getCodTipologiaContatto() != null) && 
				(tcVO.getCodTipologiaContatto() != 0)){
				ps.setInt(2, tcVO.getCodTipologiaContatto());
			}
			ps.executeUpdate();
			if ((tcVO.getCodTipologiaContatto() == null) || 
				(tcVO.getCodTipologiaContatto() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					tcVO.setCodTipologiaContatto(key);
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

	public boolean saveUpdateWithException(TipologiaContattiVO tcVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((tcVO.getCodTipologiaContatto() == null) || (tcVO.getCodTipologiaContatto() == 0))
						? getQuery(INSERT_TIPOLOGIECONTATTI)
						:getQuery(UPDATE_TIPOLOGIECONTATTI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, tcVO.getDescrizione());
			if ((tcVO.getCodTipologiaContatto() != null) && 
				(tcVO.getCodTipologiaContatto() != 0)){
				ps.setInt(2, tcVO.getCodTipologiaContatto());
			}
			ps.executeUpdate();
			if ((tcVO.getCodTipologiaContatto() == null) || 
				(tcVO.getCodTipologiaContatto() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					tcVO.setCodTipologiaContatto(key);
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

	public boolean updateTipologiaContattoAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_TIPOLOGIECONTATTI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
