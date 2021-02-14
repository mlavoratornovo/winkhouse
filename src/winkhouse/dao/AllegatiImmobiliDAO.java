package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.AllegatiImmobiliVO;


public class AllegatiImmobiliDAO extends BaseDAO {

	public final static String LISTA_ALLEGATI_BY_IMMOBILE = "LISTA_ALLEGATI_BY_IMMOBILE";
	public final static String ALLEGATIIMMOBILE_BY_ID = "ALLEGATIIMMOBILE_BY_ID";	
	public final static String INSERT_ALLEGATIIMMOBILE = "INSERT_ALLEGATIIMMOBILE";  
	public final static String UPDATE_ALLEGATIIMMOBILE = "UPDATE_ALLEGATIIMMOBILE";
	public final static String DELETE_ALLEGATIIMMOBILE = "DELETE_ALLEGATIIMMOBILE";
	public final static String DELETE_ALLEGATI_BY_IMMOBILE = "DELETE_ALLEGATI_BY_IMMOBILE";
	public final static String UPDATE_ALLEGATIIMMOBILE_AGENTEUPDATE = "UPDATE_ALLEGATIIMMOBILE_AGENTE_UPDATE";

	public AllegatiImmobiliDAO() {
	}

	public ArrayList getAllegatiByImmobile(String classType, Integer codImmobile){
		return super.getObjectsByIntFieldValue(AllegatiImmobiliVO.class.getName(), LISTA_ALLEGATI_BY_IMMOBILE, codImmobile);
	}

	public Object getAllegatiById(String classType, Integer codAllegatiImmobili){
		return super.getObjectById(AllegatiImmobiliVO.class.getName(), ALLEGATIIMMOBILE_BY_ID, codAllegatiImmobili);
	}
	
	public boolean delete(Integer codAllegatiImmobili, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_ALLEGATIIMMOBILE, codAllegatiImmobili, con, doCommit);
	}
	
	public boolean deleteByImmobile(Integer codImmobile, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_ALLEGATI_BY_IMMOBILE, codImmobile, con, doCommit);
	}
	
	public boolean saveUpdate(AllegatiImmobiliVO aiVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aiVO.getCodAllegatiImmobili() == null) || (aiVO.getCodAllegatiImmobili() == 0))
						? getQuery(INSERT_ALLEGATIIMMOBILE)
						:getQuery(UPDATE_ALLEGATIIMMOBILE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);	
			if (aiVO.getCodImmobile() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, aiVO.getCodImmobile());
			}			
						
			ps.setString(2, aiVO.getCommento());
			ps.setString(3, aiVO.getNome());
			if ((aiVO.getCodAllegatiImmobili() != null) && 
				(aiVO.getCodAllegatiImmobili() != 0)){
				ps.setInt(4, aiVO.getCodAllegatiImmobili());
			}
			ps.executeUpdate();
			if ((aiVO.getCodAllegatiImmobili() == null) || 
				(aiVO.getCodAllegatiImmobili() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aiVO.setCodAllegatiImmobili(key);
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
	
	public boolean saveUpdateWithException(AllegatiImmobiliVO aiVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aiVO.getCodAllegatiImmobili() == null) || (aiVO.getCodAllegatiImmobili() == 0))
						? getQuery(INSERT_ALLEGATIIMMOBILE)
						:getQuery(UPDATE_ALLEGATIIMMOBILE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			if (aiVO.getCodImmobile() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, aiVO.getCodImmobile());
			}			

			ps.setString(2, aiVO.getCommento());
			ps.setString(3, aiVO.getNome());
			if ((aiVO.getCodAllegatiImmobili() != null) && 
				(aiVO.getCodAllegatiImmobili() != 0)){
				ps.setInt(4, aiVO.getCodAllegatiImmobili());
			}
			ps.executeUpdate();
			if ((aiVO.getCodAllegatiImmobili() == null) || 
				(aiVO.getCodAllegatiImmobili() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aiVO.setCodAllegatiImmobili(key);
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

	public boolean updateAllegatiImmobiliAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_ALLEGATIIMMOBILE_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
