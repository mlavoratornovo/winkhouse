package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.AllegatiColloquiVO;


public class AllegatiColloquiDAO extends BaseDAO {

	public final static String LISTA_ALLEGATI_BY_COLLOQUIO = "LISTA_ALLEGATI_BY_COLLOQUIO";
	public final static String ALLEGATI_BY_ID = "ALLEGATI_BY_ID";	
	public final static String INSERT_ALLEGATI = "INSERT_ALLEGATI";  
	public final static String UPDATE_ALLEGATI = "UPDATE_ALLEGATI";
	public final static String DELETE_ALLEGATI = "DELETE_ALLEGATI";
	public final static String DELETE_ALLEGATI_BY_COLLOQUIO = "DELETE_ALLEGATI_BY_COLLOQUIO";
	public final static String UPDATE_ALLEGATI_AGENTEUPDATE = "UPDATE_ALLEGATI_AGENTE_UPDATE";
	
	public AllegatiColloquiDAO() {

	}
	
	public <T> ArrayList<T> getAllegatiByColloquio(String classType, Integer codColloquio){
		return super.getObjectsByIntFieldValue(AllegatiColloquiVO.class.getName(), LISTA_ALLEGATI_BY_COLLOQUIO, codColloquio);
	}

	public Object getAllegatiById(String classType, Integer codAllegatiColloquio){
		return super.getObjectById(AllegatiColloquiVO.class.getName(), ALLEGATI_BY_ID, codAllegatiColloquio);
	}
	
	public boolean delete(Integer codAllegatiColloquio, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_ALLEGATI, codAllegatiColloquio, con, doCommit);
	}
	
	public boolean deleteByColloquio(Integer codColloquio, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_ALLEGATI_BY_COLLOQUIO, codColloquio, con, doCommit);
	}
	
	public boolean saveUpdate(AllegatiColloquiVO acVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((acVO.getCodAllegatiColloquio() == null) || (acVO.getCodAllegatiColloquio() == 0))
						? getQuery(INSERT_ALLEGATI)
						:getQuery(UPDATE_ALLEGATI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);	
			if (acVO.getCodColloquio() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, acVO.getCodColloquio());
			}			
			ps.setString(2, acVO.getDescrizione());
			ps.setString(3, acVO.getNome());
			if ((acVO.getCodAllegatiColloquio() != null) && 
				(acVO.getCodAllegatiColloquio() != 0)){
				ps.setInt(4, acVO.getCodAllegatiColloquio());
			}
			ps.executeUpdate();
			if ((acVO.getCodAllegatiColloquio() == null) || 
				(acVO.getCodAllegatiColloquio() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					acVO.setCodAllegatiColloquio(key);
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

	public boolean saveUpdateWithException(AllegatiColloquiVO acVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((acVO.getCodAllegatiColloquio() == null) || (acVO.getCodAllegatiColloquio() == 0))
						? getQuery(INSERT_ALLEGATI)
						:getQuery(UPDATE_ALLEGATI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			if (acVO.getCodColloquio() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, acVO.getCodColloquio());
			}			
			ps.setString(2, acVO.getDescrizione());
			ps.setString(3, acVO.getNome());
			if ((acVO.getCodAllegatiColloquio() != null) && 
				(acVO.getCodAllegatiColloquio() != 0)){
				ps.setInt(4, acVO.getCodAllegatiColloquio());
			}
			ps.executeUpdate();
			if ((acVO.getCodAllegatiColloquio() == null) || 
				(acVO.getCodAllegatiColloquio() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					acVO.setCodAllegatiColloquio(key);
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

	public boolean updateAllegatiColloquioAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_ALLEGATI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	


}
