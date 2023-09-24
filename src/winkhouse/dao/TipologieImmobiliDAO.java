package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.model.TipologieImmobiliModel;
import winkhouse.vo.TipologieImmobiliVO;


public class TipologieImmobiliDAO extends BaseDAO{

	public final static String LISTA_TIPOLOGIEIMMOBILI = "LIST_TIPOLOGIE_IMMOBILI";
	public final static String LISTA_TIPOLOGIEIMMOBILI_COMUNE = "LIST_TIPOLOGIEIMMOBILI_COMUNE";
	public final static String TIPOLOGIEIMMOBILI_BY_ID = "TIPOLOGIE_IMMOBILI_BY_ID";
	public final static String TIPOLOGIEIMMOBILI_BY_NAME = "TIPOLOGIE_IMMOBILI_BY_NAME";
	public final static String INSERT_TIPOLOGIEIMMOBILI = "INSERT_TIPOLOGIE_IMMOBILI";
	public final static String UPDATE_TIPOLOGIEIMMOBILI = "UPDATE_TIPOLOGIE_IMMOBILI";
	public final static String DELETE_TIPOLOGIEIMMOBILI = "DELETE_TIPOLOGIE_IMMOBILI";	
	public final static String GET_TIPOLOGIEIMMOBILI_AFFITTI = "GET_TIPOLOGIE_IMMOBILI_AFFITTI";
	public final static String GET_TIPOLOGIEIMMOBILI_AFFITTI_COMUNE = "GET_TIPOLOGIEIMMOBILI_AFFITTI_COMUNE";
	public final static String UPDATE_TIPOLOGIEIMMOBILI_AGENTEUPDATE = "UPDATE_TIPOLOGIEIMMOBILI_AGENTE_UPDATE";
	
	public TipologieImmobiliDAO() {

	}

	public ArrayList list(String classType){
		
		return super.list(classType, LISTA_TIPOLOGIEIMMOBILI);
		
	}
	
	public ArrayList<TipologieImmobiliModel> listByComune(String comune){
		
		ArrayList<TipologieImmobiliModel> returnValue = new ArrayList<TipologieImmobiliModel>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LISTA_TIPOLOGIEIMMOBILI_COMUNE);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			if (con != null){
				ps = con.prepareStatement(query);
				ps.setString(1, comune);
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add(new TipologieImmobiliModel(rs,comune));
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

    public ArrayList listByAffitti(String classType){
		
		return super.list(classType, GET_TIPOLOGIEIMMOBILI_AFFITTI);
		
	}

    public ArrayList listByAffittiComune(String comune){
		
		ArrayList<TipologieImmobiliModel> returnValue = new ArrayList<TipologieImmobiliModel>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(GET_TIPOLOGIEIMMOBILI_AFFITTI_COMUNE);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			if (con != null){
				ps = con.prepareStatement(query);
				ps.setString(1, comune);
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add(new TipologieImmobiliModel(rs,comune));
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

	public boolean saveUpdate(TipologieImmobiliVO tiVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((tiVO.getCodTipologiaImmobile() == null) || (tiVO.getCodTipologiaImmobile() == 0))
						? getQuery(INSERT_TIPOLOGIEIMMOBILI)
						:getQuery(UPDATE_TIPOLOGIEIMMOBILI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, tiVO.getDescrizione());
			if ((tiVO.getCodTipologiaImmobile() != null) && 
				(tiVO.getCodTipologiaImmobile() != 0)){
				ps.setInt(2, tiVO.getCodTipologiaImmobile());
			}
			ps.executeUpdate();
			if ((tiVO.getCodTipologiaImmobile() == null) || 
				(tiVO.getCodTipologiaImmobile() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					tiVO.setCodTipologiaImmobile(key);
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

	public boolean saveUpdateWithException(TipologieImmobiliVO tiVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((tiVO.getCodTipologiaImmobile() == null) || (tiVO.getCodTipologiaImmobile() == 0))
						? getQuery(INSERT_TIPOLOGIEIMMOBILI)
						:getQuery(UPDATE_TIPOLOGIEIMMOBILI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, tiVO.getDescrizione());
			if ((tiVO.getCodTipologiaImmobile() != null) && 
				(tiVO.getCodTipologiaImmobile() != 0)){
				ps.setInt(2, tiVO.getCodTipologiaImmobile());
			}
			ps.executeUpdate();
			if ((tiVO.getCodTipologiaImmobile() == null) || 
				(tiVO.getCodTipologiaImmobile() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					tiVO.setCodTipologiaImmobile(key);
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
		
	public boolean delete(Integer codTipologiaImmobile, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_TIPOLOGIEIMMOBILI, codTipologiaImmobile, con, doCommit);
	}	
		
	public Object getTipologieImmobiliById(String classType, Integer codTipologiaImmobile){
		return super.getObjectById(classType, TIPOLOGIEIMMOBILI_BY_ID, codTipologiaImmobile);
	}	

	public ArrayList getTipologieImmobiliByDescrizione(String classType, String descrizione){
		return super.getObjectsByStringFieldValue(classType, TIPOLOGIEIMMOBILI_BY_NAME, descrizione);
	}	

	public boolean updateTipologieImmobiliAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_TIPOLOGIEIMMOBILI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
