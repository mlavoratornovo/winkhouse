package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.vo.ImmagineVO;


public class ImmaginiDAO extends BaseDAO{
		
	public final static String IMMAGINI_BY_IMMOBILE = "IMMAGINI_BY_IMMOBILE";
	public final static String INSERT_IMMAGINI = "INSERT_IMMAGINI";
	public final static String UPDATE_IMMAGINI = "UPDATE_IMMAGINI";
	public final static String DELETE_IMMAGINI = "DELETE_IMMAGINI";
	public final static String DELETE_IMMAGINI_BY_IMMOBILE = "DELETE_IMMAGINI_BY_IMMOBILE";
	public final static String IMMAGINI_BY_ID = "IMMAGINI_BY_ID";
	public final static String GET_MAX_ORDINE_BY_IMMOBILE = "GET_MAX_ORDINE_BY_IMMOBILE";
	public final static String UPDATE_IMMAGINI_AGENTEUPDATE = "UPDATE_IMMAGINI_AGENTE_UPDATE";
	
	public ImmaginiDAO() {
		
	}
	
	public Integer getMaxOrdineImmaginiByImmobile(Integer codImmobile){
		
		Integer returnValue = null;
				
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(GET_MAX_ORDINE_BY_IMMOBILE);
		try{
			con = ConnectionManager.getInstance().getConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, codImmobile);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue = rs.getInt("MAX");
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
		
		return (returnValue == null)?new Integer(0):returnValue;
		
	}

	public boolean delete(Integer codImmagine, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_IMMAGINI, codImmagine, con, doCommit);
	}
	
	public boolean deleteByImmobile(Integer codImmobile, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_IMMAGINI_BY_IMMOBILE, codImmobile, con, doCommit);
	}		
		
	public Object getImmaginiById(String classType, Integer codImmagine){
		return super.getObjectById(classType, IMMAGINI_BY_ID, codImmagine);
	}	
	
	public ArrayList getImmaginiByImmobile(String classType,Integer codImmobile){
		return super.getObjectsByIntFieldValue(classType, IMMAGINI_BY_IMMOBILE, codImmobile);
	}
	
	public boolean saveUpdate(ImmagineVO iVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((iVO.getCodImmagine() == null) || (iVO.getCodImmagine() == 0))
						? getQuery(INSERT_IMMAGINI)
						: getQuery(UPDATE_IMMAGINI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			if (iVO.getCodImmobile() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, iVO.getCodImmobile());
			}
			
			ps.setInt(2, iVO.getOrdine());
			ps.setString(3, iVO.getPathImmagine());
			ps.setString(4, iVO.getImgPropsStr());
			if ((iVO.getCodImmagine() != null) &&
				(iVO.getCodImmagine() != 0)){
				ps.setInt(5, iVO.getCodImmagine());
			}
			ps.executeUpdate();
			if ((iVO.getCodImmagine() == null) ||
				(iVO.getCodImmagine() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					iVO.setCodImmagine(key);
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

	public boolean saveUpdateWithException(ImmagineVO iVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((iVO.getCodImmagine() == null) || (iVO.getCodImmagine() == 0))
						? getQuery(INSERT_IMMAGINI)
						: getQuery(UPDATE_IMMAGINI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, iVO.getCodImmobile());
			ps.setInt(2, iVO.getOrdine());
			ps.setString(3, iVO.getPathImmagine());
			ps.setString(4, iVO.getImgPropsStr());
			if ((iVO.getCodImmagine() != null) &&
				(iVO.getCodImmagine() != 0)){
				ps.setInt(5, iVO.getCodImmagine());
			}
			ps.executeUpdate();
			if ((iVO.getCodImmagine() == null) ||
				(iVO.getCodImmagine() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					iVO.setCodImmagine(key);
					generatedkey = true;
					break;
				}
			}
			returnValue = true;
			if (doCommit){
				con.commit();
			}
		}catch(SQLException sql){
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

	public boolean updateImmaginiAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_IMMAGINI_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
