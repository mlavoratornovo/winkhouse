package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.model.EntityModel;
import winkhouse.vo.EntityVO;

public class EntityDAO extends BaseDAO {

	public final static String FIND_ENTITY_BY_ID = "FIND_ENTITY_BY_ID";
	public final static String FIND_ENTITY_BY_CLASSNAME = "FIND_ENTITY_BY_CLASSNAME";
	public final static String INSERT_ENTITY = "INSERT_ENTITY";
	public final static String UPDATE_ENTITY_AGENTEUPDATE = "UPDATE_ENTITY_AGENTE_UPDATE";
	
	public EntityDAO() {
		
	}
	
	public EntityModel getEntityByID(Integer idEntity){
		
		Object o = super.getObjectById(EntityModel.class.getName(), FIND_ENTITY_BY_ID, idEntity);
		if (o != null){
			return (EntityModel)o;
		}
		return null;
		
	}

	public EntityModel getEntityByClassName(String className){
		
		ArrayList al = super.getObjectsByStringFieldValue(EntityModel.class.getName(),
													  FIND_ENTITY_BY_CLASSNAME,
													  className);
		if (al.size() > 0){
			return (EntityModel)al.get(0);
		}
		return null;
		
	}

	public boolean saveUpdate(EntityVO eVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(INSERT_ENTITY);
		if (con != null){
			try{			
				ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, eVO.getIdClassEntity());
				ps.setString(2, eVO.getClassName());
				ps.setString(3, eVO.getDescription());
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					eVO.setIdClassEntity(key);
					generatedkey = true;
					break;
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
		}
		return returnValue;
	}

	public boolean updateEntityAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_ENTITY_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
