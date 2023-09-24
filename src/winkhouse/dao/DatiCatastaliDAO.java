package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.DatiCatastaliVO;


public class DatiCatastaliDAO extends BaseDAO {

	public final static String FIND_DATI_CATASTALI_BY_ID = "FIND_DATI_CATASTALI_BY_ID";
	public final static String FIND_DATI_CATASTALI_BY_CODIMMOBILE = "FIND_DATI_CATASTALI_BY_CODIMMOBILE";
	public final static String FIND_DATI_CATASTALI_BY_PROPERTIES = "FIND_DATI_CATASTALI_BY_PROPERTIES";
	public final static String DELETE_DATI_CATASTALI_BY_CODIMMOBILE = "DELETE_DATI_CATASTALI_BY_CODIMMOBILE";
	public final static String DELETE_DATI_CATASTALI_BY_ID = "DELETE_DATI_CATASTALI_BY_ID";
	public final static String INSERT_DATI_CATASTALI = "INSERT_DATI_CATASTALI";
	public final static String UPDATE_DATI_CATASTALI = "UPDATE_DATI_CATASTALI";
	public final static String UPDATE_DATI_CATASTALI_AGENTEUPDATE = "UPDATE_DATI_CATASTALI_AGENTE_UPDATE";
	
	public DatiCatastaliDAO() {

	}
	
	public boolean deleteDatiCatastaliById(Integer idDatoCatastale, Connection connection, Boolean doCommit){
		return deleteObjectById(DELETE_DATI_CATASTALI_BY_ID, idDatoCatastale, connection, doCommit);
	}
	
	public boolean deleteDatiCatastaliByCodImmobile(Integer idDatoCatastale, Connection connection, Boolean doCommit){
		return deleteObjectById(DELETE_DATI_CATASTALI_BY_CODIMMOBILE, idDatoCatastale, connection, doCommit);
	}

	public Object getDatiCatastaliById(String classType, Integer idDatoCatastale){
		return super.getObjectById(classType, FIND_DATI_CATASTALI_BY_ID, idDatoCatastale);
	}	
	
	public ArrayList findADatiCatastaliByCodImmobile(String classType, Integer codImmobile){
		return super.getObjectsByIntFieldValue(classType, 
											   FIND_DATI_CATASTALI_BY_CODIMMOBILE, 
											   codImmobile);
	}
	
	public DatiCatastaliVO getDatiCatastaliByProperties(Integer codImmobile,String foglio, String particella, String subalterno,
													    String categoria, Double rendita, Double redditoDomenicale,
													    Double redditoAgricolo, Double dimensione){
		
		DatiCatastaliVO returnValue = null;		
		ResultSet rs = null;
		Connection con = ConnectionManager.getInstance().getConnectionSelectConnection();
		PreparedStatement ps = null;
		String query = getQuery(FIND_DATI_CATASTALI_BY_PROPERTIES);
						
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setString(1, foglio);
			ps.setString(2, particella);
			ps.setString(3, subalterno);
			ps.setString(4, categoria);
			ps.setDouble(5, rendita);
			ps.setDouble(6, redditoDomenicale);
			ps.setDouble(7, redditoAgricolo);
			ps.setDouble(8, dimensione);
			ps.setInt(9, codImmobile);			
			rs = ps.executeQuery();
			while (rs.next()){
				returnValue = new DatiCatastaliVO(rs);
				break;
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
			try {
				con.close();
			} catch (SQLException e) {
				con = null;
			}
			
		}		
				
		return returnValue;
	}
	
	public boolean saveUpdate(DatiCatastaliVO dcVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((dcVO.getCodDatiCatastali() == null) || (dcVO.getCodDatiCatastali() == 0))
						? getQuery(INSERT_DATI_CATASTALI)
						: getQuery(UPDATE_DATI_CATASTALI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setString(1, dcVO.getFoglio());
			ps.setString(2, dcVO.getParticella());
			ps.setString(3, dcVO.getSubalterno());
			ps.setString(4, dcVO.getCategoria());
			ps.setDouble(5, dcVO.getRendita());
			ps.setDouble(6, dcVO.getRedditoDomenicale());
			ps.setDouble(7, dcVO.getRedditoAgricolo());
			ps.setDouble(8, dcVO.getDimensione());
			
			if (dcVO.getCodImmobile() == null){
				ps.setNull(9, java.sql.Types.INTEGER);
			}else{
				ps.setInt(9, dcVO.getCodImmobile());
			}
			
			
			if ((dcVO.getCodDatiCatastali() != null) &&
				(dcVO.getCodDatiCatastali() != 0)){
				ps.setInt(10, dcVO.getCodDatiCatastali());
			}
			ps.executeUpdate();
			if ((dcVO.getCodDatiCatastali() == null) ||
				(dcVO.getCodDatiCatastali() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					dcVO.setCodDatiCatastali(key);
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

	public boolean saveUpdateWithException(DatiCatastaliVO dcVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((dcVO.getCodDatiCatastali() == null) || (dcVO.getCodDatiCatastali() == 0))
						? getQuery(INSERT_DATI_CATASTALI)
						: getQuery(UPDATE_DATI_CATASTALI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);			
			ps.setString(1, dcVO.getFoglio());
			ps.setString(2, dcVO.getParticella());
			ps.setString(3, dcVO.getSubalterno());
			ps.setString(4, dcVO.getCategoria());
			ps.setDouble(5, dcVO.getRendita());
			ps.setDouble(6, dcVO.getRedditoDomenicale());
			ps.setDouble(7, dcVO.getRedditoAgricolo());
			ps.setDouble(8, dcVO.getDimensione());
			ps.setInt(9, dcVO.getCodImmobile());
			
			if ((dcVO.getCodDatiCatastali() != null) &&
				(dcVO.getCodDatiCatastali() != 0)){
				ps.setInt(10, dcVO.getCodDatiCatastali());
			}
			ps.executeUpdate();
			if ((dcVO.getCodDatiCatastali() == null) ||
				(dcVO.getCodDatiCatastali() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					dcVO.setCodDatiCatastali(key);
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
	
	public boolean updateDatiCatastaliAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_DATI_CATASTALI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
