package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.model.ClasseEnergeticaModel;
import winkhouse.vo.ClasseEnergeticaVO;

public class ClassiEnergeticheDAO extends BaseDAO {

	public final static String LISTA_CLASSI_ENERGETICHE = "LISTA_CLASSI_ENERGETICHE";
	public final static String LISTA_CLASSI_ENERGETICHE_COMUNE = "LISTA_CLASSI_ENERGETICHE_COMUNE";
	public final static String CLASSI_ENERGETICHE_BY_ID = "CLASSI_ENERGETICHE_BY_ID";
	public final static String CLASSI_ENERGETICHE_BY_NAME = "CLASSI_ENERGETICHE_BY_NAME";
	public final static String INSERT_CLASSI_ENERGETICHE = "INSERT_CLASSI_ENERGETICHE";
	public final static String UPDATE_CLASSI_ENERGETICHE = "UPDATE_CLASSI_ENERGETICHE";
	public final static String DELETE_CLASSI_ENERGETICHE = "DELETE_CLASSI_ENERGETICHE";
	public final static String GET_CLASSI_ENERGETICHE_AFFITTI = "GET_CLASSI_ENERGETICHE_AFFITTI";
	public final static String GET_CLASSI_ENERGETICHE_AFFITTI_COMUNE = "GET_CLASSI_ENERGETICHE_AFFITTI_COMUNE";
	public final static String UPDATE_CLASSI_ENERGETICHE_AGENTEUPDATE = "UPDATE_CLASSI_ENERGETICHE_AGENTE_UPDATE";
	
	public ClassiEnergeticheDAO() {
	}

	
	public ArrayList listByAffitti(String classType){
		return super.list(classType, GET_CLASSI_ENERGETICHE_AFFITTI);
	}	
	
	public ArrayList listClassiEnergetiche(String className){
		return super.list(className, LISTA_CLASSI_ENERGETICHE);
	};
	
	public ArrayList<ClasseEnergeticaModel> listByComune(String comune){
		
		ArrayList<ClasseEnergeticaModel> returnValue = new ArrayList<ClasseEnergeticaModel>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(LISTA_CLASSI_ENERGETICHE_COMUNE);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			if (con != null){
				ps = con.prepareStatement(query);
				ps.setString(1, comune);
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add(new ClasseEnergeticaModel(rs,comune));
				}
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				if (rs != null){
					rs.close();
				}
			} catch (SQLException e) {
				rs = null;
			}
			try {
				if (ps != null){
					ps.close();
				}
			} catch (SQLException e) {
				ps = null;
			}
	/*		try {
				con.close();
			} catch (SQLException e) {
				con = null;
			}*/
			
		}		
		
		return returnValue;

	}

	public ArrayList<ClasseEnergeticaModel> listByAffittiComune(String comune){
		
		ArrayList<ClasseEnergeticaModel> returnValue = new ArrayList<ClasseEnergeticaModel>();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(GET_CLASSI_ENERGETICHE_AFFITTI_COMUNE);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			if (con != null){
				ps = con.prepareStatement(query);
				ps.setString(1, comune);
				rs = ps.executeQuery();
				while (rs.next()) {
					returnValue.add(new ClasseEnergeticaModel(rs,comune));
				}
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}finally{
			try {
				if (rs != null){
					rs.close();
				}
			} catch (SQLException e) {
				rs = null;
			}
			try {
				if (ps != null){
					ps.close();
				}
			} catch (SQLException e) {
				ps = null;
			}
	/*		try {
				con.close();
			} catch (SQLException e) {
				con = null;
			}*/
			
		}		
		
		return returnValue;

	}

	
	public Object getClassiEnergeticheByID(String className,Integer codClassiEnergetiche){
		return super.getObjectById(className, CLASSI_ENERGETICHE_BY_ID, codClassiEnergetiche);
	}
	
	public ArrayList getClassiEnergeticheByName(String className,String name){
		return super.getObjectsByStringFieldValue(className, CLASSI_ENERGETICHE_BY_NAME, name);
	}
	
	public boolean delete(Integer codClassiEnergetiche, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_CLASSI_ENERGETICHE, codClassiEnergetiche, con, doCommit);
	}
	
	public boolean saveUpdate(ClasseEnergeticaVO ceVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((ceVO.getCodClasseEnergetica() == null) || (ceVO.getCodClasseEnergetica() == 0))
						? getQuery(INSERT_CLASSI_ENERGETICHE)
						:getQuery(UPDATE_CLASSI_ENERGETICHE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, ceVO.getNome());
			ps.setString(2, ceVO.getDescrizione());
			ps.setInt(3, ceVO.getOrdine());
			if ((ceVO.getCodClasseEnergetica() != null) && 
			    (ceVO.getCodClasseEnergetica() != 0)){
				ps.setInt(4, ceVO.getCodClasseEnergetica());
			}
			ps.executeUpdate();
			if ((ceVO.getCodClasseEnergetica() == null) || 
				(ceVO.getCodClasseEnergetica() == 0)){ 
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					ceVO.setCodClasseEnergetica(key);
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

	public boolean saveUpdateWithException(ClasseEnergeticaVO ceVO, 
										   Connection connection, 
										   Boolean doCommit) throws SQLException{
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((ceVO.getCodClasseEnergetica() == null) || (ceVO.getCodClasseEnergetica() == 0))
						? getQuery(INSERT_CLASSI_ENERGETICHE)
						:getQuery(UPDATE_CLASSI_ENERGETICHE);
		try{			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, ceVO.getNome());
			ps.setString(2, ceVO.getDescrizione());
			ps.setInt(3, ceVO.getOrdine());
			if ((ceVO.getCodClasseEnergetica() != null) && 
			    (ceVO.getCodClasseEnergetica() != 0)){
				ps.setInt(4, ceVO.getCodClasseEnergetica());
			}
			ps.executeUpdate();
			if ((ceVO.getCodClasseEnergetica() == null) || 
				(ceVO.getCodClasseEnergetica() == 0)){ 
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					ceVO.setCodClasseEnergetica(key);
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

	public boolean updateClassiEnergeticheAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_CLASSI_ENERGETICHE_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
