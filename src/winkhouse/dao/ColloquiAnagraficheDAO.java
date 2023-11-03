package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.model.ColloquiAnagraficheModel_Ang;
import winkhouse.vo.ColloquiAnagraficheVO;


public class ColloquiAnagraficheDAO extends BaseDAO {

	public final static String LISTA_COLLOQUIOANAGRAFICHE_BY_COLLOQUIO = "LISTA_COLLOQUIO_ANAGRAFICHE_BY_COLLOQUIO";
	public final static String COLLOQUIOANAGRAFICHE_BY_ID = "COLLOQUIO_ANAGRAFICHE_BY_ID";	
	public final static String INSERT_COLLOQUIOANAGRAFICHE = "INSERT_COLLOQUIO_ANAGRAFICHE";  
	public final static String UPDATE_COLLOQUIOANAGRAFICHE = "UPDATE_COLLOQUIO_ANAGRAFICHE";
	public final static String DELETE_COLLOQUIOANAGRAFICHE = "DELETE_COLLOQUIO_ANAGRAFICHE";
	public final static String DELETE_COLLOQUIOANAGRAFICHE_BY_COLLOQUIO = "DELETE_COLLOQUIO_ANAGRAFICHE_BY_COLLOQUIO";
	public final static String DELETE_COLLOQUIOANAGRAFICHE_BY_ANAGRAFICA = "DELETE_COLLOQUIOANAGRAFICHE_BY_ANAGRAFICA";
	public final static String LISTA_COLLOQUIOANAGRAFICA_BY_ANAGRAFICA_COLLOQUIO = "LISTA_COLLOQUIO_ANAGRAFICA_BY_ANAGRAFICA_COLLOQUIO";
	public final static String UPDATE_COLLOQUIOANAGRAFICA_AGENTEUPDATE = "UPDATE_COLLOQUIOANAGRAFICA_AGENTE_UPDATE";
	
	public ColloquiAnagraficheDAO() {
	}

	public <T> ArrayList<T> getColloquiAnagraficheByColloquio(String classType, Integer codColloquio){
		return super.getObjectsByIntFieldValue(ColloquiAnagraficheModel_Ang.class.getName(), LISTA_COLLOQUIOANAGRAFICHE_BY_COLLOQUIO, codColloquio);
	}

	public Object getColloquiAnagraficheById(String classType, Integer codColloquiAnagrafiche){
		return super.getObjectById(ColloquiAnagraficheModel_Ang.class.getName(), COLLOQUIOANAGRAFICHE_BY_ID, codColloquiAnagrafiche);
	}
	
	public boolean delete(Integer codColloquiAnagrafiche, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_COLLOQUIOANAGRAFICHE, codColloquiAnagrafiche, con, doCommit);
	}
	
	public boolean deleteByColloquio(Integer codColloquio, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_COLLOQUIOANAGRAFICHE_BY_COLLOQUIO, codColloquio, con, doCommit);
	}

	public boolean deleteByAnagrafica(Integer codAnagrafica, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_COLLOQUIOANAGRAFICHE_BY_ANAGRAFICA, codAnagrafica, con, doCommit);
	}

	public boolean saveUpdate(ColloquiAnagraficheVO caVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((caVO.getCodColloquioAnagrafiche() == null) || (caVO.getCodColloquioAnagrafiche() == 0))
						? getQuery(INSERT_COLLOQUIOANAGRAFICHE)
						:getQuery(UPDATE_COLLOQUIOANAGRAFICHE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);					
			ps.setInt(1, caVO.getCodColloquio());
			if (caVO.getCodAnagrafica() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, caVO.getCodAnagrafica());
			}
						
			ps.setString(3, caVO.getCommento());
			if ((caVO.getCodColloquioAnagrafiche() != null) && 
				(caVO.getCodColloquioAnagrafiche() != 0)){
				ps.setInt(4, caVO.getCodColloquioAnagrafiche());
			}
			ps.executeUpdate();
			if ((caVO.getCodColloquioAnagrafiche() == null) || 
				(caVO.getCodColloquioAnagrafiche() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					caVO.setCodColloquioAnagrafiche(key);
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

	public boolean saveUpdateWithException(ColloquiAnagraficheVO caVO, Connection connection, Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((caVO.getCodColloquioAnagrafiche() == null) || (caVO.getCodColloquioAnagrafiche() == 0))
						? getQuery(INSERT_COLLOQUIOANAGRAFICHE)
						:getQuery(UPDATE_COLLOQUIOANAGRAFICHE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);					
			ps.setInt(1, caVO.getCodColloquio());
			if (caVO.getCodAnagrafica() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, caVO.getCodAnagrafica());
			}

			ps.setString(3, caVO.getCommento());
			if ((caVO.getCodColloquioAnagrafiche() != null) && 
				(caVO.getCodColloquioAnagrafiche() != 0)){
				ps.setInt(4, caVO.getCodColloquioAnagrafiche());
			}
			ps.executeUpdate();
			if ((caVO.getCodColloquioAnagrafiche() == null) || 
				(caVO.getCodColloquioAnagrafiche() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					caVO.setCodColloquioAnagrafiche(key);
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
	
	public Object getColloquiAnagraficheByAnagraficaColloquio(String classType, Integer codAnagrafica, Integer codColloquio){
		
		Object returnValue = null;		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LISTA_COLLOQUIOANAGRAFICA_BY_ANAGRAFICA_COLLOQUIO);
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, codAnagrafica);
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

	public boolean updateColloquiAnagraficheAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_COLLOQUIOANAGRAFICA_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
