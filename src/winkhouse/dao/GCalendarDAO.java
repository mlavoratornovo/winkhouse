package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.GCalendarVO;

public class GCalendarDAO extends BaseDAO {

	public final static String GOOGLE_CALENDAR_BY_ID = "GOOGLE_CALENDAR_BY_ID";
	public final static String GOOGLE_CALENDAR_BY_CODGDATA = "GOOGLE_CALENDAR_BY_CODGDATA";
	public final static String GOOGLE_CALENDAR_SAVE = "GOOGLE_CALENDAR_SAVE";
	public final static String GOOGLE_CALENDAR_UPDATE = "GOOGLE_CALENDAR_UPDATE";
	public final static String DELETE_CALENDAR_BY_ID = "DELETE_CALENDAR_BY_ID";
	public final static String DELETE_CALENDAR_BY_CODGDATA = "DELETE_CALENDAR_BY_CODGDATA";
	public final static String UPDATE_CALENDAR_DATA_AGENTEUPDATE = "UPDATE_CALENDAR_DATA_AGENTE_UPDATE";
	
	public GCalendarDAO() {
		super();
	}

	public Object getGCalendarById(String classType, Integer codGCalendar){
		return super.getObjectById(classType, GOOGLE_CALENDAR_BY_ID, codGCalendar);
	}
	
	public Object getGCalendarsByCodGData(String classType, Integer codGData){
		return super.getObjectsByIntFieldValue(classType, GOOGLE_CALENDAR_BY_CODGDATA, codGData);
	}
	
	public boolean deleteById(Integer codGCalendar,Connection con, boolean doCommit){
		return super.deleteObjectById(DELETE_CALENDAR_BY_ID, codGCalendar, con, doCommit);
	}

	public boolean deleteByCodGData(Integer codGData,Connection con, boolean doCommit){
		return super.deleteObjectById(DELETE_CALENDAR_BY_CODGDATA, codGData, con, doCommit);
	}
	
	public boolean saveUpdate(GCalendarVO gcVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)
						 ? ConnectionManager.getInstance().getConnection()
						 : connection;
		PreparedStatement ps = null;
		String query = ((gcVO.getCodGCalendar() == null) || (gcVO.getCodGCalendar() == 0))
						? getQuery(GOOGLE_CALENDAR_SAVE)
						: getQuery(GOOGLE_CALENDAR_UPDATE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setString(1, gcVO.getPrivateUrl());
			ps.setString(2, gcVO.getAllUrl());
			if (gcVO.getCodGData() == null){
				ps.setNull(3, java.sql.Types.INTEGER);
			}else{
				ps.setInt(3, gcVO.getCodGData());
			}
			if ((gcVO.getCodGCalendar() != null) &&
				(gcVO.getCodGCalendar() != 0)){
				ps.setInt(4, gcVO.getCodGCalendar());
			}
			ps.executeUpdate();
			if ((gcVO.getCodGCalendar() == null) ||
				(gcVO.getCodGCalendar() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					gcVO.setCodGCalendar(key);
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
	
	public boolean updateGCalendarsAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_CALENDAR_DATA_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
