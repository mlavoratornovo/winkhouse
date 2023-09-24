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
import winkhouse.vo.PermessiUIVO;

public class PermessiUIDAO extends BaseDAO {

	public final static String INSERT_PERMESSOUI = "INSERT_PERMESSOUI";
	public final static String UPDATE_PERMESSOUI = "UPDATE_PERMESSOUI";
	public final static String DELETE_PERMESSOUI = "DELETE_PERMESSOUI";
	public final static String DELETE_PERMESSOUI_BY_AGENTE = "DELETE_PERMESSOUI_BY_AGENTE";
	public final static String LIST_PERMESSIUI_BY_AGENTE = "LIST_PERMESSIUI_BY_AGENTE";
	public final static String LIST_PERMESSIUI_PERSPECTIVE_BY_AGENTE = "LIST_PERMESSIUI_PERSPECTIVE_BY_AGENTE";
	public final static String LIST_PERMESSIUI_VIEW_BY_AGENTE = "LIST_PERMESSIUI_VIEW_BY_AGENTE";
	public final static String LIST_PERMESSIUI_DIALOG_BY_AGENTE = "LIST_PERMESSIUI_DIALOG_BY_AGENTE";
	public final static String UPDATE_PERMESSOUI_AGENTE = "UPDATE_PERMESSOUI_AGENTE";
	public final static String UPDATE_PERMESSOUI_AGENTEUPDATE = "UPDATE_PERMESSOUI_AGENTE_UPDATE";
	

	public PermessiUIDAO() {}	
	
	public ArrayList getPermessiByAgente(String classType, Integer codAgente){
		return super.getObjectsByIntFieldValue(classType, LIST_PERMESSIUI_BY_AGENTE, codAgente);
	}

	public ArrayList getPermessiPerspectiveByAgente(String classType, Integer codAgente){
		return super.getObjectsByIntFieldValue(classType, LIST_PERMESSIUI_PERSPECTIVE_BY_AGENTE, codAgente);
	}

	public ArrayList getPermessiViewByAgente(String classType, Integer codAgente){
		return super.getObjectsByIntFieldValue(classType, LIST_PERMESSIUI_VIEW_BY_AGENTE, codAgente);
	}

	public ArrayList getPermessiDialogByAgente(String classType, Integer codAgente){
		return super.getObjectsByIntFieldValue(classType, LIST_PERMESSIUI_DIALOG_BY_AGENTE, codAgente);
	}

	public Boolean deletePermesso(Integer codPermessoui, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_PERMESSOUI, codPermessoui, connection, doCommit);
	}
	
	public boolean saveUpdate(PermessiUIVO pVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((pVO.getCodPermessoUi() == null) || (pVO.getCodPermessoUi() == 0))
						? getQuery(INSERT_PERMESSOUI)
						: getQuery(UPDATE_PERMESSOUI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			if (pVO.getCodAgente() == null){
				ps.setNull(1, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(1, pVO.getCodAgente());
			}
			
			ps.setString(2, pVO.getPerspectiveId());
			ps.setString(3, pVO.getViewId());
			ps.setString(4, pVO.getDialogId());
			ps.setBoolean(5, pVO.getIsNot());
			
			if (pVO.getCodUserUpdate() == null){
				ps.setNull(6, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(6, pVO.getCodUserUpdate());
			}
			if (pVO.getDateUpdate() == null){
				pVO.setDateUpdate(new Date());
			}

			ps.setTimestamp(7, new Timestamp(pVO.getDateUpdate().getTime()));
			
			if ((pVO.getCodPermessoUi() != null) &&
				(pVO.getCodPermessoUi() != 0)){
				ps.setInt(8, pVO.getCodPermessoUi());
			}
			ps.executeUpdate();
			if ((pVO.getCodPermessoUi() == null) ||
				(pVO.getCodPermessoUi() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					pVO.setCodPermessoUi(key);
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

	public boolean updatePermessiAgente(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_PERMESSOUI_AGENTE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

	public boolean updatePermessiAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_PERMESSOUI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}
	
	public Boolean deletePermessoByAgente(Integer codAgente, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_PERMESSOUI_BY_AGENTE, codAgente, connection, doCommit);
	}

}
