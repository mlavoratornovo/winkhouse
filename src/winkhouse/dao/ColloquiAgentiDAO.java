package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.model.ColloquiAgentiModel_Age;
import winkhouse.vo.ColloquiAgentiVO;


public class ColloquiAgentiDAO extends BaseDAO {

	public final static String LISTA_COLLOQUIOAGENTI_BY_COLLOQUIO = "LISTA_COLLOQUIO_AGENTI_BY_COLLOQUIO";
	public final static String LISTA_COLLOQUIOAGENTI_BY_AGENTE = "LISTA_COLLOQUIO_AGENTI_BY_AGENTE";
	public final static String LISTA_COLLOQUIOAGENTI_BY_AGENTE_COLLOQUIO = "LISTA_COLLOQUIO_AGENTI_BY_AGENTE_COLLOQUIO";
	public final static String COLLOQUIOAGENTI_BY_ID = "COLLOQUIO_AGENTI_BY_ID";	
	public final static String INSERT_COLLOQUIOAGENTI = "INSERT_COLLOQUIO_AGENTI";  
	public final static String UPDATE_COLLOQUIOAGENTI = "UPDATE_COLLOQUIO_AGENTI";
	public final static String UPDATE_COLLOQUIOAGENTI_AGENTE = "UPDATE_COLLOQUIO_AGENTI_AGENTE";
	public final static String UPDATE_COLLOQUIOAGENTI_AGENTEUPDATE = "UPDATE_COLLOQUIO_AGENTI_AGENTE_UPDATE";
	public final static String DELETE_COLLOQUIOAGENTI = "DELETE_COLLOQUIO_AGENTI";
	public final static String DELETE_COLLOQUIOAGENTI_BY_COLLOQUIO = "DELETE_COLLOQUIO_AGENTI_BY_COLLOQUIO";	
	
	public ColloquiAgentiDAO() {
	}

	public ArrayList getColloquiAgentiByColloquio(String classType, Integer codColloquio){
		return super.getObjectsByIntFieldValue(ColloquiAgentiModel_Age.class.getName(), LISTA_COLLOQUIOAGENTI_BY_COLLOQUIO, codColloquio);
	}
	
	public ArrayList getColloquiAgentiByAgente(String classType, Integer codAgente){
		return super.getObjectsByIntFieldValue(ColloquiAgentiModel_Age.class.getName(), LISTA_COLLOQUIOAGENTI_BY_AGENTE, codAgente);
	}	

	public Object getColloquiAgentiById(String classType, Integer codColloquiAgenti){
		return super.getObjectById(ColloquiAgentiModel_Age.class.getName(), COLLOQUIOAGENTI_BY_ID, codColloquiAgenti);
	}
	
	public boolean delete(Integer codColloquiAgenti, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_COLLOQUIOAGENTI, codColloquiAgenti, con, doCommit);
	}
	
	public boolean deleteByColloquio(Integer codColloquio, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_COLLOQUIOAGENTI_BY_COLLOQUIO, codColloquio, con, doCommit);
	}
	
	public boolean updateAgente(Integer codAgentiOld, Integer codAgentiNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_COLLOQUIOAGENTI_AGENTE, codAgentiNew, codAgentiOld, con, doCommit);
	}

	public boolean updateAgenteUpdate(Integer codAgentiOld, Integer codAgentiNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_COLLOQUIOAGENTI_AGENTEUPDATE, codAgentiNew, codAgentiOld, con, doCommit);
	}

	public boolean saveUpdate(ColloquiAgentiVO caVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((caVO.getCodColloquioAgenti() == null) || (caVO.getCodColloquioAgenti() == 0))
						? getQuery(INSERT_COLLOQUIOAGENTI)
						:getQuery(UPDATE_COLLOQUIOAGENTI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);		
			ps.setInt(1, caVO.getCodColloquio());
			if (caVO.getCodAgente() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, caVO.getCodAgente());
			}
			
			ps.setString(3, caVO.getCommento());
			if ((caVO.getCodColloquioAgenti() != null) && 
				(caVO.getCodColloquioAgenti() != 0)){
				ps.setInt(4, caVO.getCodColloquioAgenti());
			}
			ps.executeUpdate();
			if ((caVO.getCodColloquioAgenti() == null) || 
				(caVO.getCodColloquioAgenti() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					caVO.setCodColloquioAgenti(key);
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
			if (doCommit){
				try {
					con.rollback();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}			
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

	public boolean saveUpdateWithException(ColloquiAgentiVO caVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((caVO.getCodColloquioAgenti() == null) || (caVO.getCodColloquioAgenti() == 0))
						? getQuery(INSERT_COLLOQUIOAGENTI)
						:getQuery(UPDATE_COLLOQUIOAGENTI);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);		
			ps.setInt(1, caVO.getCodColloquio());
			if (caVO.getCodAgente() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, caVO.getCodAgente());
			}

			ps.setString(3, caVO.getCommento());
			if ((caVO.getCodColloquioAgenti() != null) && 
				(caVO.getCodColloquioAgenti() != 0)){
				ps.setInt(4, caVO.getCodColloquioAgenti());
			}
			ps.executeUpdate();
			if ((caVO.getCodColloquioAgenti() == null) || 
				(caVO.getCodColloquioAgenti() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					caVO.setCodColloquioAgenti(key);
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
	
	public Object getColloquiAgentiByAgenteColloquio(String classType, Integer codAgente, Integer codColloquio){
		
		Object returnValue = null;		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LISTA_COLLOQUIOAGENTI_BY_AGENTE_COLLOQUIO);
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, codAgente);
			ps.setInt(2, codColloquio);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue = getRowObject(classType, rs);
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
		/*	try {
				con.close();
			} catch (SQLException e) {
				con = null;
			}*/
			
		}		
				
		return returnValue;

	}
}
