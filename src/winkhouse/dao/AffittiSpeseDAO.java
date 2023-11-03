package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.AffittiSpeseVO;


public class AffittiSpeseDAO extends BaseDAO {

	public final static String FIND_AFFITTISPESE_BY_ID = "FIND_AFFITTISPESE_BY_ID";
	public final static String FIND_AFFITTISPESE_BY_CODAFFITTO = "FIND_AFFITTISPESE_BY_CODAFFITTO";		
	public final static String INSERT_AFFITTISPESE = "INSERT_AFFITTISPESE";
	public final static String UPDATE_AFFITTISPESE = "UPDATE_AFFITTISPESE";
	public final static String DELETE_AFFITTISPESE = "DELETE_AFFITTISPESE";
	public final static String DELETE_AFFITTISPESE_BY_CODAFFITTO = "DELETE_AFFITTISPESE_BY_CODAFFITTO";
	public final static String DELETE_AFFITTISPESE_BY_CODANAGRAFICA = "DELETE_AFFITTISPESE_BY_CODANAGRAFICA";
	public final static String FIND_AFFITTISPESE_BY_PROPERTIES = "FIND_AFFITTISPESE_BY_PROPERTIES";
	public final static String UPDATE_AFFITTISPESE_AGENTEUPDATE = "UPDATE_AFFITTISPESE_AGENTE_UPDATE";
	
	public AffittiSpeseDAO() {}
	
	public Object getAffittiSpeseByID(String className, Integer codAffittoSpese){
		return super.getObjectById(className, FIND_AFFITTISPESE_BY_ID, codAffittoSpese);
	}
	
	public <T> ArrayList<T> getAffittiSpeseByCodAffitto(String className, Integer codAffitto){
		return super.getObjectsByIntFieldValue(className, FIND_AFFITTISPESE_BY_CODAFFITTO, codAffitto);
	}

	public boolean deleteAffittiSpeseByID(Integer codAffittoSpese, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_AFFITTISPESE, codAffittoSpese, connection, doCommit);
	}

	public boolean deleteAffittiSpeseByCodAnagrafica(Integer codAnagrafica, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_AFFITTISPESE_BY_CODANAGRAFICA, codAnagrafica, connection, doCommit);
	}

	public boolean deleteAffittiSpeseByCodAffitto(Integer codAffitto, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_AFFITTISPESE_BY_CODAFFITTO, codAffitto, connection, doCommit);
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> getAffittiSpeseByProperties(String className,AffittiSpeseVO affittiSpese){
		
		ArrayList<T> returnValue = new ArrayList<T>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(FIND_AFFITTISPESE_BY_PROPERTIES);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			
			ps.setInt(1, affittiSpese.getCodAnagrafica());
			ps.setInt(2, affittiSpese.getCodAffitto());
			ps.setTimestamp(3, new Timestamp(affittiSpese.getScadenza().getTime()));
			ps.setTimestamp(4, new Timestamp(affittiSpese.getDataPagato().getTime()));
			ps.setTimestamp(5, new Timestamp(affittiSpese.getDataInizio().getTime()));
			ps.setTimestamp(6, new Timestamp(affittiSpese.getDataFine().getTime()));
			ps.setDouble(7, affittiSpese.getImporto());
			ps.setDouble(8, affittiSpese.getVersato());
			ps.setString(9, affittiSpese.getDescrizione());
			
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue.add((T) getRowObject(className, rs));
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
		
	public boolean saveUpdate(AffittiSpeseVO asVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((asVO.getCodAffittiSpese() == null) || (asVO.getCodAffittiSpese() == 0))
						? getQuery(INSERT_AFFITTISPESE)
						: getQuery(UPDATE_AFFITTISPESE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			if (asVO.getCodAffitto() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, asVO.getCodAffitto());
			}			
			if (asVO.getCodParent() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, asVO.getCodParent());
			}						
			ps.setTimestamp(3, new Timestamp(asVO.getDataInizio().getTime()));
			ps.setTimestamp(4, new Timestamp(asVO.getDataFine().getTime()));
			ps.setTimestamp(5, new Timestamp(asVO.getScadenza().getTime()));
			ps.setDouble(6, asVO.getImporto().doubleValue());
			ps.setDouble(7, asVO.getVersato().doubleValue());
			ps.setString(8, asVO.getDescrizione());
			
			if(asVO.getDataPagato()!= null){
				ps.setTimestamp(9, new Timestamp(asVO.getDataPagato().getTime()));
			}else{
				ps.setNull(9, java.sql.Types.TIMESTAMP);
			}
			
			if (asVO.getCodAnagrafica() == null) {
				ps.setNull(10, java.sql.Types.INTEGER);
			} else {
				ps.setInt(10, asVO.getCodAnagrafica());
			}						
									
			if ((asVO.getCodAffittiSpese() != null) &&
				(asVO.getCodAffittiSpese() != 0)){
				ps.setInt(11, asVO.getCodAffittiSpese());
			}
			ps.executeUpdate();
			if ((asVO.getCodAffittiSpese() == null) ||
				(asVO.getCodAffittiSpese() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					asVO.setCodAffittiSpese(key);
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
	
	public boolean saveUpdateWithException(AffittiSpeseVO asVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((asVO.getCodAffittiSpese() == null) || (asVO.getCodAffittiSpese() == 0))
						? getQuery(INSERT_AFFITTISPESE)
						: getQuery(UPDATE_AFFITTISPESE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			if (asVO.getCodAffitto() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, asVO.getCodAffitto());
			}			
			if (asVO.getCodParent() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, asVO.getCodParent());
			}						
			ps.setTimestamp(3, new Timestamp(asVO.getDataInizio().getTime()));
			ps.setTimestamp(4, new Timestamp(asVO.getDataFine().getTime()));
			ps.setTimestamp(5, new Timestamp(asVO.getScadenza().getTime()));
			ps.setDouble(6, asVO.getImporto().doubleValue());
			ps.setDouble(7, asVO.getVersato().doubleValue());
			ps.setString(8, asVO.getDescrizione());

			if(asVO.getDataPagato()!= null){
				ps.setTimestamp(9, new Timestamp(asVO.getDataPagato().getTime()));
			}else{
				ps.setNull(9, java.sql.Types.TIMESTAMP);
			}
			
			if (asVO.getCodAnagrafica() == null) {
				ps.setNull(10, java.sql.Types.INTEGER);
			} else {
				ps.setInt(10, asVO.getCodAnagrafica());
			}						

			if ((asVO.getCodAffittiSpese() != null) &&
				(asVO.getCodAffittiSpese() != 0)){
				ps.setInt(11, asVO.getCodAffittiSpese());
			}
			ps.executeUpdate();
			if ((asVO.getCodAffittiSpese() == null) ||
				(asVO.getCodAffittiSpese() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					asVO.setCodAffittiSpese(key);
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

	public boolean updateAffittiSpeseAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_AFFITTISPESE_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
