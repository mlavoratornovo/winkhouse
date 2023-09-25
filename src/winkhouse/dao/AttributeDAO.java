package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.model.AttributeModel;
import winkhouse.vo.AttributeVO;

public class AttributeDAO extends BaseDAO {

	public final static String FIND_ATTRIBUTE_BY_ID = "FIND_ATTRIBUTE_BY_ID";
	public final static String FIND_ATTRIBUTES_BY_IDENTITY = "FIND_ATTRIBUTES_BY_IDENTITY";
	public final static String FIND_ATTRIBUTE_BY_IDENTITY_ATTRIBUTENAME = "FIND_ATTRIBUTE_BY_IDENTITY_ATTRIBUTENAME";
	public final static String INSERT_ATTRIBUTE = "INSERT_ATTRIBUTE";
	public final static String UPDATE_ATTRIBUTE = "UPDATE_ATTRIBUTE";
	public final static String DELETE_ATTRIBUTE_BY_ID = "DELETE_ATTRIBUTE_BY_ID";
	public final static String FIND_ATTRIBUTE_BY_ENTITYCLASSNAME_ATTRIBUTENAME_ATTRIBUTETYPE = "FIND_ATTRIBUTE_BY_ENTITYCLASSNAME_ATTRIBUTENAME_ATTRIBUTETYPE";
	public final static String UPDATE_ATTRIBUTE_AGENTEUPDATE = "UPDATE_ATTRIBUTE_AGENTE_UPDATE";
	
	public AttributeDAO() {
		
	}
	
	public AttributeModel getAttributeByID(Integer idAttribute){
		
		Object o = super.getObjectById(AttributeModel.class.getName(), FIND_ATTRIBUTE_BY_ID, idAttribute);
		
		if (o != null){
			return (AttributeModel)o;
		}
		return null;
		
	}
	
	public ArrayList<AttributeModel> getAttributeByIdEntity(Integer idEntity){
		return super.getObjectsByIntFieldValue(AttributeModel.class.getName(), FIND_ATTRIBUTES_BY_IDENTITY, idEntity);
	}
	
	public Object getAttributeByIdEntityAttributeName(String classType, Integer idEntity, String attributeName){

		Object returnValue = null;		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(FIND_ATTRIBUTE_BY_IDENTITY_ATTRIBUTENAME);
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);			
			ps.setInt(1, idEntity);
			ps.setString(2, attributeName);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue = getRowObject(classType, rs);
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
		}		

		return returnValue;

	}
	
	public Object getAttributeByEntityClassNameAttributeNameAttributeType(String classType, 
																		 String entityClassName, 
																		 String attributeName,
																		 String attributeType){

		Object returnValue = null;		
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(FIND_ATTRIBUTE_BY_ENTITYCLASSNAME_ATTRIBUTENAME_ATTRIBUTETYPE);
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);			
			ps.setString(1, entityClassName);
			ps.setString(2, attributeName);
			ps.setString(3, attributeType);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue = getRowObject(classType, rs);
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
		}		

		return returnValue;

	}
		

	public boolean saveUpdate(AttributeVO aVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aVO.getIdAttribute() == null) || (aVO.getIdAttribute() == 0))
						? getQuery(INSERT_ATTRIBUTE)
						: getQuery(UPDATE_ATTRIBUTE);
		try{			
			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			if (aVO.getIdClassEntity() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, aVO.getIdClassEntity());
			}			
			ps.setString(2, aVO.getAttributeName());
			ps.setString(3, aVO.getFieldType());
			if (aVO.getLinkedIdEntity() != null){
				ps.setInt(4, aVO.getLinkedIdEntity());
			}else{
				ps.setNull(4, java.sql.Types.INTEGER);
			}
			if (aVO.getEnumFieldValues() != null){
				ps.setString(5, aVO.getEnumFieldValues());
			}else{
				ps.setNull(5, java.sql.Types.VARCHAR);
			}
			
			if ((aVO.getIdAttribute() != null) &&
				(aVO.getIdAttribute() != 0)){
				ps.setInt(6, aVO.getIdAttribute());
			}
			ps.executeUpdate();
			if ((aVO.getIdAttribute() == null) ||
				(aVO.getIdAttribute() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aVO.setIdAttribute(key);
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

	public boolean delete(Integer codAttribute, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_ATTRIBUTE_BY_ID, codAttribute, con, doCommit);		
	}

	public boolean updateAttributeAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_ATTRIBUTE_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}