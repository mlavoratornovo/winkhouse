package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.AgentiAppuntamentiVO;


public class AgentiAppuntamentiDAO extends BaseDAO {

	public final static String INSERT_AGENTIAPPUNTAMENTI = "INSERT_AGENTIAPPUNTAMENTI";
	public final static String UPDATE_AGENTIAPPUNTAMENTI = "UPDATE_AGENTIAPPUNTAMENTI";
	public final static String LIST_AGENTIAPPUNTAMENTI_BY_CODAPPUNTAMENTO = "LIST_AGENTIAPPUNTAMENTI_BY_CODAPPUNTAMENTO";
	public final static String LIST_AGENTIAPPUNTAMENTI_BY_CODAGENTE = "LIST_AGENTIAPPUNTAMENTI_BY_CODAGENTE";
	public final static String DELETE_AGENTIAPPUNTAMENTI = "DELETE_AGENTIAPPUNTAMENTI"; 
	public final static String DELETE_AGENTIAPPUNTAMENTI_BY_CODAGENTE = "DELETE_AGENTIAPPUNTAMENTI_BY_CODAGENTE";
	public final static String DELETE_AGENTIAPPUNTAMENTI_BY_CODAPPUNTAMENTO = "DELETE_AGENTIAPPUNTAMENTI_BY_CODAPPUNTAMENTO";
	public final static String UPDATE_AGENTE_AGENTIAPPUNTAMENTI = "UPDATE_AGENTE_AGENTIAPPUNTAMENTI";
	public final static String UPDATE_AGENTIAPPUNTAMENTI_AGENTEUPDATE = "UPDATE_AGENTIAPPUNTAMENTI_AGENTE_UPDATE";
	
	public AgentiAppuntamentiDAO() {}

	public ArrayList listAgentiAppuntamentiByAppuntamento(String classType, Integer codAppuntamento){
		return super.getObjectsByIntFieldValue(classType, 
											   LIST_AGENTIAPPUNTAMENTI_BY_CODAPPUNTAMENTO, 
											   codAppuntamento);
	} 
	
	public ArrayList listAgentiAppuntamentiByAgente(String classType, Integer codAgente){
		return super.getObjectsByIntFieldValue(classType, 
											   LIST_AGENTIAPPUNTAMENTI_BY_CODAGENTE, 
											   codAgente);
	}

	public boolean deleteAgentiAppuntamenti(Integer codAgenteAppuntamento,Connection con,Boolean doCommit){
		return super.deleteObjectById(DELETE_AGENTIAPPUNTAMENTI, codAgenteAppuntamento, con, doCommit);
	}

	public boolean deleteAgentiAppuntamentiByAgente(Integer codAgente,Connection con,Boolean doCommit){
		return super.deleteObjectById(DELETE_AGENTIAPPUNTAMENTI_BY_CODAGENTE, codAgente, con, doCommit);
	}

	public boolean deleteAgentiAppuntamentiByAppuntamento(Integer codAppuntamento,Connection con,Boolean doCommit){
		return super.deleteObjectById(DELETE_AGENTIAPPUNTAMENTI_BY_CODAPPUNTAMENTO, codAppuntamento, con, doCommit);
	}

	public boolean saveUpdate(AgentiAppuntamentiVO aaVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aaVO.getCodAgentiAppuntamenti() == null) || (aaVO.getCodAgentiAppuntamenti() == 0))
						? getQuery(INSERT_AGENTIAPPUNTAMENTI)
						: getQuery(UPDATE_AGENTIAPPUNTAMENTI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			if (aaVO.getCodAgente() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, aaVO.getCodAgente());
			}			
			if (aaVO.getCodAppuntamento() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, aaVO.getCodAppuntamento());
			}			
				
			ps.setString(3, aaVO.getNote());
			if ((aaVO.getCodAgentiAppuntamenti() != null) &&
				(aaVO.getCodAgentiAppuntamenti() != 0)){
				ps.setInt(4, aaVO.getCodAgentiAppuntamenti());
			}
			ps.executeUpdate();
			if ((aaVO.getCodAgentiAppuntamenti() == null) ||
				(aaVO.getCodAgentiAppuntamenti() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aaVO.setCodAgentiAppuntamenti(key);
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
	
	public boolean updateAgente(Integer oldCodAgente, Integer newCodAgente, Connection connection, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_AGENTE_AGENTIAPPUNTAMENTI, newCodAgente, oldCodAgente, connection, doCommit);
	}

	public boolean updateAgentiAppuntamentiAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_AGENTIAPPUNTAMENTI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
