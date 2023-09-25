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
import winkhouse.model.ReportModel;
import winkhouse.vo.ReportVO;


public class ReportDAO extends BaseDAO {

	public final static String REPORT_BY_ID = "REPORT_BY_ID";
	public final static String REPORT_BY_TIPOLOGIA = "REPORT_BY_TIPOLOGIA";
	public final static String REPORT_LISTA_BY_TIPOLOGIA = "REPORT_LISTA_BY_TIPOLOGIA";
	public final static String INSERT_REPORT = "INSERT_REPORT";
	public final static String UPDATE_REPORT = "UPDATE_REPORT";
	public final static String DELETE_REPORT = "DELETE_REPORT";
	public final static String UPDATE_REPORT_AGENTEUPDATE = "UPDATE_REPORT_AGENTE_UPDATE";
	
	public ReportDAO() {
	}
	
	public ArrayList getReportByTipologia(String classType, String tipologia){
		return super.getObjectsByStringFieldValue(classType, REPORT_BY_TIPOLOGIA,tipologia);
	}

	public ArrayList getReportListByTipologia(String classType, String tipologia){
		return super.getObjectsByStringFieldValue(classType, REPORT_LISTA_BY_TIPOLOGIA,tipologia);
	}

	public Object getReportByID(Integer codReport){
		return super.getObjectById(ReportModel.class.getName(), REPORT_BY_ID, codReport);
	}
	
	public boolean deleteReport(Integer codReport,Connection con, Boolean doCommit){
		return deleteObjectById(DELETE_REPORT, codReport, con, doCommit);
	}

	public boolean saveUpdate(ReportVO rVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((rVO.getCodReport() == null) || (rVO.getCodReport() == 0))
						? getQuery(INSERT_REPORT)
						: getQuery(UPDATE_REPORT);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, rVO.getNome());
			ps.setString(2, rVO.getTipo());
			ps.setString(3, rVO.getTemplate());
			ps.setString(4, rVO.getDescrizione());
			ps.setBoolean(5, rVO.getIsList());
			
			if (rVO.getCodUserUpdate() == null){
				ps.setNull(6, java.sql.Types.INTEGER);
			}else{ 
				ps.setInt(6, rVO.getCodUserUpdate());
			}
			if (rVO.getDateUpdate() == null){
				rVO.setDateUpdate(new Date());
			}

			ps.setTimestamp(7, new Timestamp(rVO.getDateUpdate().getTime()));
			
			
			if ((rVO.getCodReport() != null) &&
				(rVO.getCodReport() != 0)){
				ps.setInt(8, rVO.getCodReport());
			}
			ps.executeUpdate();
			if ((rVO.getCodReport() == null) ||
				(rVO.getCodReport() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					rVO.setCodReport(key);
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

	public boolean updateReportAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_REPORT_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}