package winkhouse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import winkhouse.db.ConnectionManager;
import winkhouse.model.AttributeValueModel;
import winkhouse.vo.AttributeValueVO;

public class AttributeValueDAO extends BaseDAO {

	public final static String FIND_ATTRIBUTEVALUE_BY_ID = "FIND_ATTRIBUTEVALUE_BY_ID";
	public final static String FIND_ATTRIBUTEVALUE_BY_IDATTRIBUTE_IDOBJECT = "FIND_ATTRIBUTEVALUE_BY_IDATTRIBUTE_IDOBJECT";
	public final static String FIND_ATTRIBUTEVALUE_BY_IDATTRIBUTE = "FIND_ATTRIBUTEVALUE_BY_IDATTRIBUTE";	
	public final static String INSERT_ATTRIBUTEVALUE = "INSERT_ATTRIBUTEVALUE";
	public final static String UPDATE_ATTRIBUTEVALUE = "UPDATE_ATTRIBUTEVALUE";
	public final static String DELETE_ATTRIBUTEVALUE_BY_IDATTRIBUTE = "DELETE_ATTRIBUTEVALUE_BY_IDATTRIBUTE";
	public final static String DELETE_ATTRIBUTEVALUE_BY_IDATTRIBUTE_IDOBJECT = "DELETE_ATTRIBUTEVALUE_BY_IDATTRIBUTE_IDOBJECT";
	public final static String DELETE_ATTRIBUTEVALUE_BY_CLASSNAME_IDOBJECT = "DELETE_ATTRIBUTEVALUE_BY_CLASSNAME_IDOBJECT";
	public final static String UPDATE_ATTRIBUTEVALUE_AGENTEUPDATE = "UPDATE_ATTRIBUTEVALUE_AGENTE_UPDATE";
	
	public AttributeValueDAO() {}
	
	public AttributeValueModel getAttributeValueByIdAttributeIdObject(Integer idAttribute, Integer idObject){
		
		AttributeValueModel returnValue = null;
				
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;
		String query = getQuery(FIND_ATTRIBUTEVALUE_BY_IDATTRIBUTE_IDOBJECT);		
		try{
			con = ConnectionManager.getInstance().getConnectionSelectConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, idAttribute);
			ps.setInt(2, idObject);
			rs = ps.executeQuery();
			while (rs.next()) {
				returnValue = new AttributeValueModel(rs);
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

	public boolean saveUpdate(AttributeValueVO aVO, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;
		boolean generatedkey = false;
		ResultSet rs = null;
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = ((aVO.getIdValue() == null) || (aVO.getIdValue() == 0))
						? getQuery(INSERT_ATTRIBUTEVALUE)
						: getQuery(UPDATE_ATTRIBUTEVALUE);
		try{			
			
			ps = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			if (aVO.getIdField() == null) {
				ps.setNull(1, java.sql.Types.INTEGER);
			} else {
				ps.setInt(1, aVO.getIdField());
			}			
			if (aVO.getIdObject() == null) {
				ps.setNull(2, java.sql.Types.INTEGER);
			} else {
				ps.setInt(2, aVO.getIdObject());
			}			
						
			ps.setString(3, aVO.getFieldValue());
			if ((aVO.getIdValue() != null) &&
				(aVO.getIdValue() != 0)){
				ps.setInt(4, aVO.getIdValue());
			}
			ps.executeUpdate();
			if ((aVO.getIdValue() == null) ||
				(aVO.getIdValue() == 0)){
				rs = ps.getGeneratedKeys();
				while (rs.next()){
					Integer key = rs.getInt("");
					aVO.setIdValue(key);
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

	public boolean deleteByAttributeId(Integer codAttribute, Connection con, Boolean doCommit){
		return super.deleteObjectById(DELETE_ATTRIBUTEVALUE_BY_IDATTRIBUTE, codAttribute, con, doCommit);		
	}
	
	public <T> ArrayList<T> getAttributeValuesByIdAttribute(String classType, Integer idAttribute, Connection con){
		return super.getObjectsByIntFieldValue(classType, FIND_ATTRIBUTEVALUE_BY_IDATTRIBUTE, idAttribute);
	}
	
	public boolean deleteByAttributeIdObjectId(Integer codAttribute,Integer codObject, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;		
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(DELETE_ATTRIBUTEVALUE_BY_IDATTRIBUTE_IDOBJECT);
		try{			
			ps = con.prepareStatement(query);
			ps.setInt(1, codAttribute);
			ps.setInt(2, codObject);
			ps.executeUpdate();
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
	
	public boolean deleteByClassNameObjectId(String classname,Integer codObject, Connection connection, Boolean doCommit){
		
		boolean returnValue = false;		
		Connection con = (connection == null)? ConnectionManager.getInstance().getConnection():connection;
		PreparedStatement ps = null;
		String query = getQuery(DELETE_ATTRIBUTEVALUE_BY_CLASSNAME_IDOBJECT);
		try{			
			ps = con.prepareStatement(query);
			ps.setString(1, classname);
			ps.setInt(2, codObject);
			ps.executeUpdate();
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
	

	public boolean updateAttributeValueAgenteUpdate(Integer codAgenteOld, Integer codAgenteNew, Connection con, Boolean doCommit){
		return super.updateByIdWhereId(UPDATE_ATTRIBUTEVALUE_AGENTEUPDATE, codAgenteNew, codAgenteOld, con, doCommit);
	}	

}
