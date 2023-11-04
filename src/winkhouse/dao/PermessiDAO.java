package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.PermessiVO;

public class PermessiDAO extends BaseDAO {

	public final static String INSERT_PERMESSO = "INSERT_PERMESSO";
	public final static String UPDATE_PERMESSO = "UPDATE_PERMESSO";
	public final static String DELETE_PERMESSO = "DELETE_PERMESSO";
	public final static String DELETE_PERMESSO_BY_CODRICERCA = "DELETE_PERMESSO_BY_CODRICERCA";
	public final static String DELETE_PERMESSO_BY_CODAGENTE = "DELETE_PERMESSO_BY_CODAGENTE";
	public final static String LIST_PERMESSI_BY_AGENTE = "LIST_PERMESSI_BY_AGENTE";
	public final static String LIST_PERMESSIIMMOBILI_BY_AGENTE = "LIST_PERMESSIIMMOBILI_BY_AGENTE";
	public final static String LIST_PERMESSIANAGRAFICHE_BY_AGENTE = "LIST_PERMESSIANAGRAFICHE_BY_AGENTE";
	public final static String LIST_PERMESSIAFFITTI_BY_AGENTE = "LIST_PERMESSIAFFITTI_BY_AGENTE";
	public final static String UPDATE_PERMESSI_AGENTEUPDATE = "UPDATE_PERMESSI_AGENTEUPDATE";
	
	public PermessiDAO() {

	}
	
	public <T> ArrayList<T> getPermessiByAgente(String classType, Integer codAgente){
		return super.getObjectsByIntFieldValue(classType, LIST_PERMESSI_BY_AGENTE, codAgente);
	}
	
	public Boolean deletePermesso(Integer codPermesso, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_PERMESSO, codPermesso, connection, doCommit);
	}

	public Boolean deletePermessoByCodRicerca(Integer codRicerca, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_PERMESSO_BY_CODRICERCA, codRicerca, connection, doCommit);
	}

	public Boolean deletePermessoByCodAgente(Integer codAgente, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_PERMESSO_BY_CODAGENTE, codAgente, connection, doCommit);
	}

	public boolean saveUpdate(PermessiVO pVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((pVO.getCodPermesso() == null) || (pVO.getCodPermesso() == 0))
						? getQuery(INSERT_PERMESSO)
						: getQuery(UPDATE_PERMESSO);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			if (pVO.getCodAgente() == null){
				ps.setNull(1, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(1, pVO.getCodAgente());
			}
						
			if (pVO.getCodRicerca() == null){
				ps.setNull(2, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(2, pVO.getCodRicerca());
			}
			
			ps.setString(3, pVO.getDescrizione());
			ps.setBoolean(4, pVO.getIsNot());			
			ps.setBoolean(5, pVO.getCanWrite());
			
			if (pVO.getCodUserUpdate() == null){
				ps.setNull(6, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(6, pVO.getCodUserUpdate());
			}
			if (pVO.getDateUpdate() == null){
				pVO.setDateUpdate(new Date());
			}

			ps.setTimestamp(7, new Timestamp(pVO.getDateUpdate().getTime()));
			
			if ((pVO.getCodPermesso() != null) &&
				(pVO.getCodPermesso() != 0)){
				ps.setInt(8, pVO.getCodPermesso());
			}
			ps.executeUpdate();
			if ((pVO.getCodPermesso() == null) ||
				(pVO.getCodPermesso() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					pVO.setCodPermesso(key);
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
	
	public <T> ArrayList<T> getPermessiImmobiliByAgente(String classType, Integer codAgente){
		return super.getObjectsByIntFieldValue(classType, LIST_PERMESSIIMMOBILI_BY_AGENTE, codAgente);
	}
	
	public <T> ArrayList<T> getPermessiAnagraficheByAgente(String classType, Integer codAgente){
		return super.getObjectsByIntFieldValue(classType, LIST_PERMESSIANAGRAFICHE_BY_AGENTE, codAgente);
	}

	public <T> ArrayList<T> getPermessiAffittiByAgente(String classType, Integer codAgente){
		return super.getObjectsByIntFieldValue(classType, LIST_PERMESSIAFFITTI_BY_AGENTE, codAgente);
	}

	public boolean updatePermessiAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_PERMESSI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
