package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.AffittiRateVO;


public class AffittiRateDAO extends BaseDAO {

	public final static String FIND_AFFITTIRATE_BY_ID = "FIND_AFFITTIRATE_BY_ID";
	public final static String FIND_AFFITTIRATE_BY_CODAFFITTO = "FIND_AFFITTIRATE_BY_CODAFFITTO";		
	public final static String INSERT_AFFITTIRATE = "INSERT_AFFITTIRATE";
	public final static String UPDATE_AFFITTIRATE = "UPDATE_AFFITTIRATE";
	public final static String DELETE_AFFITTIRATE = "DELETE_AFFITTIRATE";
	public final static String DELETE_AFFITTIRATE_BY_CODAFFITTO = "DELETE_AFFITTIRATE_BY_CODAFFITTO";
	public final static String DELETE_AFFITTIRATE_BY_CODANAGRAFICA = "DELETE_AFFITTIRATE_BY_CODANAGRAFICA";
	public final static String FIND_AFFITTIRATE_BY_PROPERTIES = "FIND_AFFITTIRATE_BY_PROPERTIES";
	public final static String UPDATE_AFFITTIRATE_AGENTEUPDATE = "UPDATE_AFFITTIRATE_AGENTE_UPDATE";
	
	public AffittiRateDAO() {}
	
	public Object getAffittiRateByID(String className, Integer codAffittoRate){
		return super.getObjectById(className, FIND_AFFITTIRATE_BY_ID, codAffittoRate);
	}
	
	public ArrayList getAffittiRateByCodAffitto(String className, Integer codAffitto){
		return super.getObjectsByIntFieldValue(className, FIND_AFFITTIRATE_BY_CODAFFITTO, codAffitto);
	}
	
	public ArrayList getAffittiRateByProperties(String className,AffittiRateVO affittiRate){
		
		ArrayList returnValue = new ArrayList();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(FIND_AFFITTIRATE_BY_PROPERTIES);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			
			ps.setInt(1, affittiRate.getCodAnagrafica());
			ps.setInt(2, affittiRate.getCodAffitto());
			ps.setDouble(3, affittiRate.getImporto());
			ps.setTimestamp(4, new Timestamp(affittiRate.getDataPagato().getTime()));
			ps.setInt(5, affittiRate.getMese());
			ps.setTimestamp(6, new Timestamp(affittiRate.getScadenza().getTime()));
			ps.setString(7, affittiRate.getNota());
			
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add(getRowObject(className, rs));
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
		
		return returnValue;
		
	}
	
	public boolean deleteAffittiRateByID(Integer codAffittoRate, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_AFFITTIRATE, codAffittoRate, connection, doCommit);
	}

	public boolean deleteAffittiRateByCodAffitto(Integer codAffitto, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_AFFITTIRATE_BY_CODAFFITTO, codAffitto, connection, doCommit);
	}

	public boolean deleteAffittiRateByCodAnagrafica(Integer codAnagrafica, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_AFFITTIRATE_BY_CODANAGRAFICA, codAnagrafica, connection, doCommit);
	}

	public boolean saveUpdate(AffittiRateVO arVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((arVO.getCodAffittiRate() == null) || (arVO.getCodAffittiRate() == 0))
						? getQuery(INSERT_AFFITTIRATE)
						: getQuery(UPDATE_AFFITTIRATE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			if (arVO.getCodAffitto() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, arVO.getCodAffitto());
			}			
			if (arVO.getCodParent() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, arVO.getCodParent());
			}						
			
			ps.setInt(3, arVO.getMese());
			ps.setTimestamp(4, new Timestamp(arVO.getScadenza().getTime()));
			
			if(arVO.getDataPagato()!= null){
				ps.setTimestamp(5, new Timestamp(arVO.getDataPagato().getTime()));
			}else{
				ps.setNull(5, java.sql.Types.TIMESTAMP);
			}

			ps.setDouble(6, arVO.getImporto().doubleValue());
			if (arVO.getCodAnagrafica() == null) {
				ps.setNull(7, java.sql.Types.INTEGER);
			} else {
				ps.setInt(7, arVO.getCodAnagrafica());
			}						
									
			ps.setString(8, arVO.getNota());
			if ((arVO.getCodAffittiRate() != null) &&
				(arVO.getCodAffittiRate() != 0)){
				ps.setInt(9, arVO.getCodAffittiRate());
			}
			ps.executeUpdate();
			if ((arVO.getCodAffittiRate() == null) ||
				(arVO.getCodAffittiRate() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					arVO.setCodAffittiRate(key);
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

	public boolean saveUpdateWithException(AffittiRateVO arVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((arVO.getCodAffittiRate() == null) || (arVO.getCodAffittiRate() == 0))
						? getQuery(INSERT_AFFITTIRATE)
						: getQuery(UPDATE_AFFITTIRATE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);				
			if (arVO.getCodAffitto() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, arVO.getCodAffitto());
			}			
			if (arVO.getCodParent() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, arVO.getCodParent());
			}						
			
			ps.setInt(3, arVO.getMese());
			ps.setTimestamp(4, new Timestamp(arVO.getScadenza().getTime()));
			
			if(arVO.getDataPagato()!= null){
				ps.setTimestamp(5, new Timestamp(arVO.getDataPagato().getTime()));
			}else{
				ps.setNull(5, java.sql.Types.TIMESTAMP);
			}

			ps.setDouble(6, arVO.getImporto().doubleValue());
			if (arVO.getCodAnagrafica() == null) {
				ps.setNull(7, java.sql.Types.INTEGER);
			} else {
				ps.setInt(7, arVO.getCodAnagrafica());
			}						
			ps.setString(8, arVO.getNota());
			if ((arVO.getCodAffittiRate() != null) &&
				(arVO.getCodAffittiRate() != 0)){
				ps.setInt(9, arVO.getCodAffittiRate());
			}
			ps.executeUpdate();
			if ((arVO.getCodAffittiRate() == null) ||
				(arVO.getCodAffittiRate() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					arVO.setCodAffittiRate(key);
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

	public boolean updateAffittiRateAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_AFFITTIRATE_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
