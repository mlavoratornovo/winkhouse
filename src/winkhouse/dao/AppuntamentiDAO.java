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
import winkhouse.vo.AppuntamentiVO;


public class AppuntamentiDAO extends BaseDAO {

	public final static String INSERT_APPUNTAMENTO = "INSERT_APPUNTAMENTO";
	public final static String UPDATE_APPUNTAMENTO = "UPDATE_APPUNTAMENTO";
	public final static String LIST_APPUNTAMENTI_AGENTI_DA_A = "LIST_APPUNTAMENTI_AGENTI_DA_A";
	public final static String LIST_APPUNTAMENTI_DA_A = "LIST_APPUNTAMENTI_DA_A";
	public final static String LIST_APPUNTAMENTI_BY_ANAGRAFICA = "LIST_APPUNTAMENTI_BY_ANAGRAFICA";
	public final static String LIST_APPUNTAMENTI_BY_CODTIPOAPPUNTAMENTO = "LIST_APPUNTAMENTI_BY_CODTIPOAPPUNTAMENTO";
	public final static String DELETE_APPUNTAMENTO = "DELETE_APPUNTAMENTO";
	public final static String UPDATE_APPUNTAMENTI_CODTIPO = "UPDATE_APPUNTAMENTI_CODTIPO";
	public final static String LIST_APPUNTAMENTI_AGENTI_DA_A_ICAL_NULL = "LIST_APPUNTAMENTI_AGENTI_DA_A_ICAL_NULL";
	public final static String LIST_APPUNTAMENTI_AGENTI_DA_A_NOT_ICAL_NULL = "LIST_APPUNTAMENTI_AGENTI_DA_A_NOT_ICAL_NULL";
	public final static String GET_APPUNTAMENTO_BY_ID = "GET_APPUNTAMENTO_BY_ID";
	public final static String GET_APPUNTAMENTO_BY_ICALID = "GET_APPUNTAMENTO_BY_ICALID";
	public final static String UPDATE_APPUNTAMENTI_AGENTEUPDATE = "UPDATE_APPUNTAMENTI_AGENTE_UPDATE";
	
	
	public AppuntamentiDAO() {}
	
	public Object getAppuntamentoByID(String ClassName,Integer codAppuntamento){
		return super.getObjectById(ClassName, GET_APPUNTAMENTO_BY_ID, codAppuntamento);
	}
	
	public boolean delete(Integer codAppuntamento,Connection con,Boolean doCommit){
		return super.deleteObjectById(DELETE_APPUNTAMENTO, codAppuntamento, con, doCommit);
	}
	
	public ArrayList listAppuntamentiByCodAnagrafica(String ClassName,Integer codAngrafica){
		return super.getObjectsByIntFieldValue(ClassName, LIST_APPUNTAMENTI_BY_ANAGRAFICA, codAngrafica);
	}

	public ArrayList listAppuntamentiByCodTipoAppuntamento(String ClassName,Integer codTipoAppuntamento){
		return super.getObjectsByIntFieldValue(ClassName, LIST_APPUNTAMENTI_BY_CODTIPOAPPUNTAMENTO, codTipoAppuntamento);
	}	
	
	public boolean updateTipoAppuntamento(Integer codTipoAppuntamentoOld, Integer codTipoAppuntamentoNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_APPUNTAMENTI_CODTIPO, codTipoAppuntamentoNew, codTipoAppuntamentoOld, con, doCommit);
	}
	
	public boolean saveUpdate(AppuntamentiVO aVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aVO.getCodAppuntamento() == null) || (aVO.getCodAppuntamento() == 0))
						? getQuery(INSERT_APPUNTAMENTO)
						: getQuery(UPDATE_APPUNTAMENTO);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setTimestamp(1, new Timestamp((aVO.getDataInserimento()==null)
											  ? new Date().getTime()
											  : aVO.getDataInserimento().getTime()
											 ));
			ps.setTimestamp(2, new Timestamp((aVO.getDataAppuntamento()==null)
											  ? new Date().getTime()
											  : aVO.getDataAppuntamento().getTime()
					 						 ));			
			ps.setString(3, aVO.getDescrizione());
			ps.setString(4, aVO.getLuogo());
			ps.setTimestamp(5, new Timestamp((aVO.getDataFineAppuntamento()==null)
											  ? new Date().getTime()
											  : aVO.getDataFineAppuntamento().getTime()));
			ps.setString(6,aVO.getiCalUID());
			if (aVO.getCodTipoAppuntamento() == null) {
				ps.setNull(7, java.sql.Types.INTEGER);
			} else {
				ps.setInt(7, aVO.getCodTipoAppuntamento());
			}
			if (aVO.getCodPadre() == null) {
				ps.setNull(8, java.sql.Types.INTEGER);
			} else {
				ps.setInt(8, aVO.getCodPadre());
			}
			
			
			
			
			if (aVO.getCodUserUpdate() == null){
				ps.setNull(9, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(9, aVO.getCodUserUpdate());
			}
			if (aVO.getDateUpdate() == null){
				aVO.setDateUpdate(new Date());
			}

			ps.setTimestamp(10, new Timestamp(aVO.getDateUpdate().getTime()));
						
			
			if ((aVO.getCodAppuntamento() != null) &&
				(aVO.getCodAppuntamento() != 0)){
				ps.setInt(11, aVO.getCodAppuntamento());
			}
			ps.executeUpdate();
			if ((aVO.getCodAppuntamento() == null) ||
				(aVO.getCodAppuntamento() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aVO.setCodAppuntamento(key);
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
	
	public ArrayList listAppuntamentiByAgenteDaA(String classType, Integer codAgente, Date dataDA, Date dataA){
		
		ArrayList returnValue = new ArrayList();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LIST_APPUNTAMENTI_AGENTI_DA_A);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, codAgente);
			ps.setTimestamp(2, new Timestamp(dataDA.getTime()));
			ps.setTimestamp(3, new Timestamp(dataA.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add(getRowObject(classType, rs));
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
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

	public ArrayList listAppuntamentiByAgenteDaAICALL_Null(String classType, Integer codAgente, Date dataDA, Date dataA){
		
		ArrayList returnValue = new ArrayList();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LIST_APPUNTAMENTI_AGENTI_DA_A_ICAL_NULL);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, codAgente);
			ps.setTimestamp(2, new Timestamp(dataDA.getTime()));
			ps.setTimestamp(3, new Timestamp(dataA.getTime()));
			ps.setInt(4, codAgente);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add(getRowObject(classType, rs));
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
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

	public ArrayList listAppuntamentiByAgenteDaA_Not_ICALL_Null(String classType, Integer codAgente, Date dataDA, Date dataA){
		
		ArrayList returnValue = new ArrayList();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LIST_APPUNTAMENTI_AGENTI_DA_A_NOT_ICAL_NULL);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, codAgente);
			ps.setTimestamp(2, new Timestamp(dataDA.getTime()));
			ps.setTimestamp(3, new Timestamp(dataA.getTime()));
			ps.setInt(4, codAgente);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add(getRowObject(classType, rs));
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
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
	
	public ArrayList listAppuntamentiByDaA(String classType, Date dataDA, Date dataA){
		
		ArrayList returnValue = new ArrayList();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LIST_APPUNTAMENTI_DA_A);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);			
			ps.setTimestamp(1, new Timestamp(dataDA.getTime()));
			ps.setTimestamp(2, new Timestamp(dataA.getTime()));
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add(getRowObject(classType, rs));
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
			try {
				ps.close();
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

	public ArrayList getAppuntamentoByICalID(String classType, String icalid){
		return super.getObjectsByStringFieldValue(classType, GET_APPUNTAMENTO_BY_ICALID, icalid);		
	}
	
	public boolean updateAppuntamentiAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_APPUNTAMENTI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
