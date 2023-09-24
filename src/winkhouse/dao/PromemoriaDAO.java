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
import winkhouse.vo.PromemoriaVO;

public class PromemoriaDAO extends BaseDAO {
	
	public final static String INSERT_PROMEMORIA = "INSERT_PROMEMORIA";
	public final static String UPDATE_PROMEMORIA = "UPDATE_PROMEMORIA";
	public final static String DELETE_PROMEMORIA = "DELETE_PROMEMORIA";
	public final static String LIST_PROMEMORIA_BY_AGENTE = "LIST_PROMEMORIA_BY_AGENTE";
	public final static String LIST_PROMEMORIA = "LIST_PROMEMORIA";
	public final static String LIST_PROMEMORIA_AGENTE_IS_NULL = "LIST_PROMEMORIA_AGENTE_IS_NULL";
	public final static String UPDATE_PROMEMORIA_AGENTE = "UPDATE_PROMEMORIA_AGENTE";
	public final static String UPDATE_PROMEMORIA_AGENTEUPDATE = "UPDATE_PROMEMORIA_AGENTE_UPDATE";
	
	public PromemoriaDAO() {
		
	}

	public ArrayList getPromemoriaByAgente(String className, Integer codAgente){
		return super.getObjectsByIntFieldValue(className, LIST_PROMEMORIA_BY_AGENTE, codAgente);
	}

	public ArrayList getAllPromemoria(String className){
		return super.list(className, LIST_PROMEMORIA);
	}
	
	public ArrayList getAllPromemoriaAgenteIsNull(String className){
		return super.list(className, LIST_PROMEMORIA_AGENTE_IS_NULL);
	}
	
	public Boolean deletePromemoria(Integer codPromemoria,Connection connection,Boolean doCommit){
		return super.deleteObjectById(DELETE_PROMEMORIA, codPromemoria, connection, doCommit);
	}
	
	public boolean saveUpdate(PromemoriaVO iVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((iVO.getCodPromemoria() == null) || (iVO.getCodPromemoria() == 0))
						? getQuery(INSERT_PROMEMORIA)
						: getQuery(UPDATE_PROMEMORIA);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			if (iVO.getCodAgente() == null){
				ps.setNull(1, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(1, iVO.getCodAgente());
			}
						
			ps.setString(2, iVO.getDescrizione());
			
			if (iVO.getCodUserUpdate() == null){
				ps.setNull(3, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(3, iVO.getCodUserUpdate());
			}
			if (iVO.getDateUpdate() == null){
				iVO.setDateUpdate(new Date());
			}

			ps.setTimestamp(4, new Timestamp(iVO.getDateUpdate().getTime()));
			
			if ((iVO.getCodPromemoria() != null) &&
				(iVO.getCodPromemoria() != 0)){
				ps.setInt(5, iVO.getCodPromemoria());
			}
			ps.executeUpdate();
			if ((iVO.getCodPromemoria() == null) ||
				(iVO.getCodPromemoria() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					iVO.setCodPromemoria(key);
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

	public boolean updatePromemoriaAgente(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_PROMEMORIA_AGENTE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

	public boolean updatePromemoriaAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_PROMEMORIA_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
