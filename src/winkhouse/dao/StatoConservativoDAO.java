package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.model.StatoConservativoModel;
import winkhouse.vo.StatoConservativoVO;


public class StatoConservativoDAO extends BaseDAO{

	public final static String LISTA_STATOCONSERVATIVO = "LISTA_STATO_CONSERVATIVO";
	public final static String LISTA_STATOCONSERVATIVO_COMUNE = "LISTA_STATO_CONSERVATIVO_COMUNE";
	public final static String STATOCONSERVATIVO_BY_ID = "STATO_CONSERVATIVO_BY_ID";
	public final static String STATOCONSERVATIVO_BY_NAME = "STATO_CONSERVATIVO_BY_NAME";
	public final static String INSERT_STATOCONSERVATIVO = "INSERT_STATO_CONSERVATIVO";
	public final static String UPDATE_STATOCONSERVATIVO = "UPDATE_STATO_CONSERVATIVO";
	public final static String DELETE_STATOCONSERVATIVO = "DELETE_STATO_CONSERVATIVO";
	public final static String GET_STATOCONSERVATIVO_AFFITTI = "GET_STATOCONSERVATIVO_AFFITTI";
	public final static String GET_STATOCONSERVATIVO_AFFITTI_COMUNE = "GET_STATOCONSERVATIVO_AFFITTI_COMUNE";
	public final static String UPDATE_STATOCONSERVATIVO_AGENTEUPDATE = "UPDATE_STATOCONSERVATIVO_AGENTE_UPDATE";
	
	public StatoConservativoDAO() {

	}
	
	public ArrayList list(String classType){
		return super.list(classType, LISTA_STATOCONSERVATIVO);
	}	

	public ArrayList<StatoConservativoModel> listByComune(String comune){
		
		ArrayList<StatoConservativoModel> returnValue = new ArrayList<StatoConservativoModel>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LISTA_STATOCONSERVATIVO_COMUNE);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			if (con != null){
				ps = con.prepareStatement(query);
				ps.setString(1, comune);
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add(new StatoConservativoModel(rs,comune));
				}
			}
		}catch(Exception sql){
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

	public ArrayList<StatoConservativoModel> listByAffittiComune(String comune){
		
		ArrayList<StatoConservativoModel> returnValue = new ArrayList<StatoConservativoModel>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(GET_STATOCONSERVATIVO_AFFITTI_COMUNE);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			if (con != null){
				ps = con.prepareStatement(query);
				ps.setString(1, comune);
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add(new StatoConservativoModel(rs,comune));
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
		return super.list(classType, GET_STATOCONSERVATIVO_AFFITTI);
	}	
	
	public boolean saveUpdate(StatoConservativoVO scVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((scVO.getCodStatoConservativo() == null) || (scVO.getCodStatoConservativo() == 0))
						? getQuery(INSERT_STATOCONSERVATIVO)
						:getQuery(UPDATE_STATOCONSERVATIVO);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, scVO.getDescrizione());
			if ((scVO.getCodStatoConservativo() != null) && 
			    (scVO.getCodStatoConservativo() != 0)){
				ps.setInt(2, scVO.getCodStatoConservativo());
			}
			ps.executeUpdate();
			if ((scVO.getCodStatoConservativo() == null) || 
				(scVO.getCodStatoConservativo() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					scVO.setCodStatoConservativo(key);
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
	
	public boolean saveUpdateWithException(StatoConservativoVO scVO, 
										   Connection connection, 
										   Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((scVO.getCodStatoConservativo() == null) || (scVO.getCodStatoConservativo() == 0))
						? getQuery(INSERT_STATOCONSERVATIVO)
						:getQuery(UPDATE_STATOCONSERVATIVO);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, scVO.getDescrizione());
			if ((scVO.getCodStatoConservativo() != null) && 
			    (scVO.getCodStatoConservativo() != 0)){
				ps.setInt(2, scVO.getCodStatoConservativo());
			}
			ps.executeUpdate();
			if ((scVO.getCodStatoConservativo() == null) || 
				(scVO.getCodStatoConservativo() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					scVO.setCodStatoConservativo(key);
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
	
	public boolean delete(Integer codStatoConservativo, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_STATOCONSERVATIVO, codStatoConservativo, con, doCommit);
	}	
		
	public Object getStatoConservativoById(String classType, Integer codStatoConservativo){
		return super.getObjectById(classType, STATOCONSERVATIVO_BY_ID, codStatoConservativo);
	}	

	public ArrayList getStatoConservativoByDescrizione(String classType, String descrizione){
		return super.getObjectsByStringFieldValue(classType, STATOCONSERVATIVO_BY_NAME, descrizione);
	}	

	public boolean updateStatoConservativoAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_STATOCONSERVATIVO_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	
	
}
