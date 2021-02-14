package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.AffittiAllegatiVO;


public class AffittiAllegatiDAO extends BaseDAO {

	public final static String FIND_AFFITTIALLEGATI_BY_ID = "FIND_AFFITTIALLEGATI_BY_ID";
	public final static String FIND_AFFITTIALLEGATI_BY_CODAFFITTO = "FIND_AFFITTIALLEGATI_BY_CODAFFITTO";		
	public final static String INSERT_AFFITTIALLEGATI = "INSERT_AFFITTIALLEGATI";
	public final static String UPDATE_AFFITTIALLEGATI = "UPDATE_AFFITTIALLEGATI";
	public final static String DELETE_AFFITTIALLEGATI = "DELETE_AFFITTIALLEGATI";
	public final static String DELETE_AFFITTIALLEGATI_BY_CODAFFITTO = "DELETE_AFFITTIALLEGATI_BY_CODAFFITTO";
	public final static String UPDATE_AFFITTIALLEGATI_AGENTEUPDATE = "UPDATE_AFFITTIALLEGATI_AGENTE_UPDATE";

	public AffittiAllegatiDAO() {}
	
	public Object getAffittiAllegatiByID(String className, Integer codAffittoAllegato){
		return super.getObjectById(className, FIND_AFFITTIALLEGATI_BY_ID, codAffittoAllegato);
	}
	
	public ArrayList getAffittiAllegatiByCodAffitto(String className, Integer codAffitto){
		return super.getObjectsByIntFieldValue(className, FIND_AFFITTIALLEGATI_BY_CODAFFITTO, codAffitto);
	}
	
	public boolean deleteAffittiAllegatiByID(Integer codAffittoAllegati, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_AFFITTIALLEGATI, codAffittoAllegati, connection, doCommit);
	}

	public boolean deleteAffittiAllegatiByCodAffitto(Integer codAffitto, Connection connection, Boolean doCommit){
		return super.deleteObjectById(DELETE_AFFITTIALLEGATI_BY_CODAFFITTO, codAffitto, connection, doCommit);
	}
	
	public boolean saveUpdate(AffittiAllegatiVO aaVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aaVO.getCodAffittiAllegati() == null) || (aaVO.getCodAffittiAllegati() == 0))
						? getQuery(INSERT_AFFITTIALLEGATI)
						: getQuery(UPDATE_AFFITTIALLEGATI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);	
			if (aaVO.getCodAffitto() == null){
				ps.setNull(1, java.sql.Types.INTEGER);
			}else{
				ps.setInt(1, aaVO.getCodAffitto());
			}						
			ps.setString(2, aaVO.getNome());
			ps.setString(3, aaVO.getDescrizione());
			if ((aaVO.getCodAffittiAllegati() != null) &&
				(aaVO.getCodAffittiAllegati() != 0)){
				ps.setInt(4, aaVO.getCodAffittiAllegati());
			}
			ps.executeUpdate();
			if ((aaVO.getCodAffittiAllegati() == null) ||
				(aaVO.getCodAffittiAllegati() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aaVO.setCodAffittiAllegati(key);
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

	public boolean saveUpdateWithException(AffittiAllegatiVO aaVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aaVO.getCodAffittiAllegati() == null) || (aaVO.getCodAffittiAllegati() == 0))
						? getQuery(INSERT_AFFITTIALLEGATI)
						: getQuery(UPDATE_AFFITTIALLEGATI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			if (aaVO.getCodAffitto() == null){
				ps.setNull(1, java.sql.Types.INTEGER);
			}else{
				ps.setInt(1, aaVO.getCodAffitto());
			}						
			ps.setString(2, aaVO.getNome());
			ps.setString(3, aaVO.getDescrizione());
			if ((aaVO.getCodAffittiAllegati() != null) &&
				(aaVO.getCodAffittiAllegati() != 0)){
				ps.setInt(4, aaVO.getCodAffittiAllegati());
			}
			ps.executeUpdate();
			if ((aaVO.getCodAffittiAllegati() == null) ||
				(aaVO.getCodAffittiAllegati() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aaVO.setCodAffittiAllegati(key);
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

	public boolean updateAffittiAllegatiAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_AFFITTIALLEGATI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
