package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.AbbinamentiVO;


public class AbbinamentiDAO extends BaseDAO {

	public final static String FIND_ABBINAMENTO_BY_ID = "FIND_ABBINAMENTO_BY_ID";
	public final static String FIND_ABBINAMENTI_BY_CODIMMOBILE = "FIND_ABBINAMENTI_BY_CODIMMOBILE";
	public final static String FIND_ABBINAMENTI_BY_CODANAGRAFICA = "FIND_ABBINAMENTI_BY_CODANAGRAFICA";
	public final static String FIND_ABBINAMENTI_BY_CODIMMOBILE_CODANAGRAFICA = "FIND_ABBINAMENTI_BY_CODIMMOBILE_CODANAGRAFICA";
	public final static String INSERT_ABBINAMENTO = "INSERT_ABBINAMENTO";
	public final static String UPDATE_ABBINAMENTO = "UPDATE_ABBINAMENTO";
	public final static String DELETE_ABBINAMENTO = "DELETE_ABBINAMENTO";
	public final static String DELETE_ABBINAMENTO_BY_CODIMMOBILE = "DELETE_ABBINAMENTO_BY_CODIMMOBILE";
	public final static String DELETE_ABBINAMENTO_BY_CODANAGRAFICA = "DELETE_ABBINAMENTO_BY_CODANAGRAFICA";
	public final static String UPDATE_ABBINAMENTI_AGENTEUPDATE = "UPDATE_ABBINAMENTI_AGENTE_UPDATE";

	public AbbinamentiDAO() {
		
	}

	public boolean deleteAbbinamenti(Integer idAbbinamento, Connection connection, Boolean doCommit){
		return deleteObjectById(DELETE_ABBINAMENTO, idAbbinamento, connection, doCommit);
	}
	
	public boolean deleteAbbinamentiByCodImmobile(Integer codImmobile, Connection connection, Boolean doCommit){
		return deleteObjectById(DELETE_ABBINAMENTO_BY_CODIMMOBILE, codImmobile, connection, doCommit);
	}

	public boolean deleteAbbinamentiByCodAnagrafica(Integer codAnagrafca, Connection connection, Boolean doCommit){
		return deleteObjectById(DELETE_ABBINAMENTO_BY_CODANAGRAFICA, codAnagrafca, connection, doCommit);
	}
	
	public Object getAbbinamentoById(String classType, Integer idAbbinamento){
		return super.getObjectById(classType, FIND_ABBINAMENTO_BY_ID, idAbbinamento);
	}	
	
	public boolean saveUpdate(AbbinamentiVO aVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aVO.getCodAbbinamento() == null) || (aVO.getCodAbbinamento() == 0))
						? getQuery(INSERT_ABBINAMENTO)
						: getQuery(UPDATE_ABBINAMENTO);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			if (aVO.getCodImmobile() == null){
				ps.setNull(1, java.sql.Types.INTEGER);
			}else{
				ps.setInt(1, aVO.getCodImmobile());
			}
			if (aVO.getCodAnagrafica() == null){
				ps.setNull(2, java.sql.Types.INTEGER);
			}else{
				ps.setInt(2, aVO.getCodAnagrafica());
			}		
			
			if ((aVO.getCodAbbinamento() != null) &&
				(aVO.getCodAbbinamento() != 0)){
				ps.setInt(3, aVO.getCodAbbinamento());
			}
			ps.executeUpdate();
			if ((aVO.getCodAbbinamento() == null) ||
				(aVO.getCodAbbinamento() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aVO.setCodAbbinamento(key);
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

	public boolean saveUpdateWithException(AbbinamentiVO aVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aVO.getCodAbbinamento() == null) || (aVO.getCodAbbinamento() == 0))
						? getQuery(INSERT_ABBINAMENTO)
						: getQuery(UPDATE_ABBINAMENTO);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			if (aVO.getCodImmobile() == null){
				ps.setNull(1, java.sql.Types.INTEGER);
			}else{
				ps.setInt(1, aVO.getCodImmobile());
			}
			if (aVO.getCodAnagrafica() == null){
				ps.setNull(2, java.sql.Types.INTEGER);
			}else{
				ps.setInt(2, aVO.getCodAnagrafica());
			}		
			if ((aVO.getCodAbbinamento() != null) &&
				(aVO.getCodAbbinamento() != 0)){
				ps.setInt(3, aVO.getCodAbbinamento());
			}
			ps.executeUpdate();
			if ((aVO.getCodAbbinamento() == null) ||
				(aVO.getCodAbbinamento() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aVO.setCodAbbinamento(key);
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
	
	public ArrayList findAbbinamentiByCodImmobile(String classType, Integer codImmobile){
		return super.getObjectsByIntFieldValue(classType, 
											   FIND_ABBINAMENTI_BY_CODIMMOBILE, 
											   codImmobile);
	}

	public ArrayList findAbbinamentiByCodAnagrafica(String classType, Integer codAnagrafica){
		return super.getObjectsByIntFieldValue(classType, 
											   FIND_ABBINAMENTI_BY_CODANAGRAFICA, 
											   codAnagrafica);
	}

	public Object findAbbinamentiByCodImmobileCodAnagrafica(String classType, Integer codAnagrafica, Integer codImmobile){
		
			
			Object returnValue = null;
			boolean generatedkey = false;
			ResultSet rs = null;
			Connection con = ConnectionManager.getInstance().getConnectionSelectConnection();
			PreparedStatement ps = null;
			String query = getQuery(FIND_ABBINAMENTI_BY_CODIMMOBILE_CODANAGRAFICA);
			try{			
				ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
				ps.setInt(1, codImmobile);
				ps.setInt(2, codAnagrafica);
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue = getRowObject(classType, rs);
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
				
			}		
					
			return returnValue;
	}
	
	public boolean updateAbbinamentiAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_ABBINAMENTI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
