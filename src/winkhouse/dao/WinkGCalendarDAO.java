package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.model.WinkGCalendarModel;
import winkhouse.vo.WinkGCalendarVO;

public class WinkGCalendarDAO extends BaseDAO {

	public final static String INSERT_WINKCALENDAR = "INSERT_WINKCALENDAR";
	public final static String UPDATE_WINKCALENDAR = "UPDATE_WINKCALENDAR";
	public final static String GET_WINKCALENDAR_BY_CODAPPUNTAMENTO = "GET_WINKCALENDAR_BY_CODAPPUNTAMENTO";
	public final static String GET_WINKCALENDAR_BY_CODCOLLOQUIO = "GET_WINKCALENDAR_BY_CODCOLLOQUIO";
	public final static String GET_WINKCALENDAR_BY_CODAGENTE = "GET_WINKCALENDAR_BY_CODAGENTE";
	public final static String GET_WINKCALENDAR_BY_CODAGENTE_CALENDARID_EVENTID = "GET_WINKCALENDAR_BY_CODAGENTE_CALENDARID_EVENTID";
	public final static String GET_WINKCALENDAR_BY_CODAGENTE_CODAPPUNTAMENTO_CALENDARID = "GET_WINKCALENDAR_BY_CODAGENTE_CODAPPUNTAMENTO_CALENDARID";
	public final static String GET_WINKCALENDAR_BY_CODAGENTE_CODCOLLOQUIO_CALENDARID = "GET_WINKCALENDAR_BY_CODAGENTE_CODCOLLOQUIO_CALENDARID";
	public final static String DELETE_WINKCALENDAR_BY_ID = "DELETE_WINKCALENDAR_BY_ID";
	public final static String UPDATE_WINKCALENDAR_AGENTE = "UPDATE_WINKCALENDAR_AGENTE";
	
	public WinkGCalendarDAO() {
		
	}

	public ArrayList getWinkGCalendarByCodAppuntamento(Integer codAppuntamento){
		return (ArrayList<WinkGCalendarModel>)super.getObjectsByIntFieldValue(WinkGCalendarModel.class.getName(), GET_WINKCALENDAR_BY_CODAPPUNTAMENTO, codAppuntamento);
	}

	public ArrayList getWinkGCalendarByCodColloquio(Integer codColloquio){
		return (ArrayList<WinkGCalendarModel>)super.getObjectsByIntFieldValue(WinkGCalendarModel.class.getName(), GET_WINKCALENDAR_BY_CODCOLLOQUIO, codColloquio);
	}

	public ArrayList<WinkGCalendarModel> getWinkGCalendarByCodAgente(Integer codAgente){
		return (ArrayList<WinkGCalendarModel>)super.getObjectsByIntFieldValue(WinkGCalendarModel.class.getName(), GET_WINKCALENDAR_BY_CODAGENTE, codAgente);
	}

	public ArrayList getWinkGCalendarByCodAgenteCalendarIdEventId(Integer codAgente,String calendarid, String eventid){

		ArrayList returnValue = new ArrayList();		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(GET_WINKCALENDAR_BY_CODAGENTE_CALENDARID_EVENTID);
		con = ConnectionManager.getInstance().getConnectionSelectConnection();
		if (con != null){
			try{

				ps = con.prepareStatement(query);			
				ps.setInt(1, codAgente);
				ps.setString(2, calendarid);
				ps.setString(3, eventid);
				
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add(getRowObject(WinkGCalendarModel.class.getName(), rs));
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
			}		
		}
		
		return returnValue;

	}

	public Object getWinkGCalendarByCodAgenteCodAppuntamentoCalendarId(String returnType, Integer codAgente,Integer codAppuntamento,String calendarid){
				
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(GET_WINKCALENDAR_BY_CODAGENTE_CODAPPUNTAMENTO_CALENDARID);
		con = ConnectionManager.getInstance().getConnectionSelectConnection();
		if (con != null){
			try{

				ps = con.prepareStatement(query);			
				ps.setInt(1, codAgente);
				ps.setInt(2, codAppuntamento);
				ps.setString(3, calendarid);				
				
				rs = ps.executeQuery();
				while (rs.next()) {
					return getRowObject(returnType, rs);
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
			}		
		}
		
		return null;

	}

	public Object getWinkGCalendarByCodAgenteCodColloquioCalendarId(String returnType,Integer codAgente,Integer codColloquio,String calendarid){
		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(GET_WINKCALENDAR_BY_CODAGENTE_CODCOLLOQUIO_CALENDARID);
		con = ConnectionManager.getInstance().getConnectionSelectConnection();
		if (con != null){
			try{

				ps = con.prepareStatement(query);			
				ps.setInt(1, codAgente);
				ps.setInt(2, codColloquio);
				ps.setString(3, calendarid);				
				
				rs = ps.executeQuery();
				while (rs.next()) {
					return getRowObject(returnType, rs);
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
			}		
		}
		
		return null;

	}

	public boolean saveUpdate(WinkGCalendarVO wgcVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((wgcVO.getCodWinkGCalendar() == null) || (wgcVO.getCodWinkGCalendar() == 0))
						? getQuery(INSERT_WINKCALENDAR)
						: getQuery(UPDATE_WINKCALENDAR);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setInt(1, wgcVO.getCodAgente());
			
			if (wgcVO.getCodAppuntamento() != null){
				ps.setInt(2, wgcVO.getCodAppuntamento());
			}else{
				ps.setNull(2, java.sql.Types.INTEGER);
			}

			if (wgcVO.getCodColloquio() != null){
				ps.setInt(3, wgcVO.getCodColloquio());
			}else{
				ps.setNull(3, java.sql.Types.INTEGER);
			}
			
			ps.setString(4, wgcVO.getCalendarId());
			ps.setString(5, wgcVO.getEventId());
						
			if ((wgcVO.getCodWinkGCalendar() != null) &&
				(wgcVO.getCodWinkGCalendar() != 0)){
				ps.setInt(6, wgcVO.getCodWinkGCalendar());
			}
			ps.executeUpdate();
			if ((wgcVO.getCodWinkGCalendar() == null) ||
				(wgcVO.getCodWinkGCalendar() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					wgcVO.setCodWinkGCalendar(key);
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

	public boolean deleteById(Integer codWinkCalendar, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_WINKCALENDAR_BY_ID, codWinkCalendar, connection, doCommit);
	}

	public boolean updateWinkGCalendarAgente(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_WINKCALENDAR_AGENTE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
