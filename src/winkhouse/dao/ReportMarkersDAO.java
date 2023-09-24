package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.model.ReportMarkersModel;
import winkhouse.vo.ReportMarkersVO;


public class ReportMarkersDAO extends BaseDAO {

	public final static String REPORTMARKERS_BY_CODREPORT = "REPORTMARKERS_BY_CODREPORT";
	public final static String INSERT_REPORTMARKERS = "INSERT_REPORTMARKERS";
	public final static String UPDATE_REPORTMARKERS = "UPDATE_REPORTMARKERS";
	public final static String DELETE_REPORTMARKERS_BY_CODREPORT = "DELETE_REPORTMARKERS_BY_CODREPORT";
	public final static String DELETE_REPORTMARKERS_BY_ID = "DELETE_REPORTMARKERS_BY_ID";
	public final static String UPDATE_REPORTMARKERS_AGENTEUPDATE = "UPDATE_REPORTMARKERS_AGENTE_UPDATE";
	
	public ReportMarkersDAO() {
	}

	public ArrayList getMarkersByReport(Integer codReport){
		return getObjectsByIntFieldValue(ReportMarkersModel.class.getName(), REPORTMARKERS_BY_CODREPORT, codReport);
	}
	
	public boolean deleteReportMarkersByReport(Integer codReport, Connection connection, Boolean doCommit){
		return deleteObjectById(DELETE_REPORTMARKERS_BY_CODREPORT, codReport, connection, doCommit);
	}

	public boolean deleteReportMarkersByID(Integer codMarker, Connection connection, Boolean doCommit){
		return deleteObjectById(DELETE_REPORTMARKERS_BY_ID, codMarker, connection, doCommit);
	}
	
	public boolean saveUpdate(ReportMarkersVO rVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((rVO.getCodMarker() == null) || (rVO.getCodMarker() == 0))
						? getQuery(INSERT_REPORTMARKERS)
						: getQuery(UPDATE_REPORTMARKERS);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			if (rVO.getCodReport() == null){
				ps.setNull(1, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(1, rVO.getCodReport());
			}
						
			ps.setString(2, rVO.getTipo());
			ps.setString(3, rVO.getNome());
			ps.setString(4, rVO.getGetMethodName());
			ps.setString(5, rVO.getParams());
			ps.setString(6, rVO.getParamsDesc());
			
			if ((rVO.getCodMarker() != null) &&
				(rVO.getCodMarker() != 0)){
				ps.setInt(7, rVO.getCodMarker());
			}
			ps.executeUpdate();
			if ((rVO.getCodMarker() == null) ||
				(rVO.getCodMarker() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					rVO.setCodMarker(key);
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

	public boolean updateReportMarkersAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_REPORTMARKERS_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	
	
}
