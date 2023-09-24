package winkhouse.export.helpers;

import winkhouse.dao.AttributeDAO;
import winkhouse.dao.AttributeValueDAO;
import winkhouse.dao.EntityDAO;
import winkhouse.model.AttributeModel;
import winkhouse.model.AttributeValueModel;
import winkhouse.model.EntityModel;
import winkhouse.vo.AttributeVO;
import winkhouse.vo.AttributeValueVO;
import winkhouse.vo.EntityVO;

public class CampiPersonaliHelper {

	public CampiPersonaliHelper() {
		// TODO Auto-generated constructor stub
	}

	public EntityModel getEntityById(Integer idEntity){
		
		EntityDAO eDAO = new EntityDAO();
		return eDAO.getEntityByID(idEntity);
		
	}
	
	public EntityModel getEntityByClassName(String className){
		
		EntityDAO eDAO = new EntityDAO();
		return eDAO.getEntityByClassName(className);
		
	}
	
	public boolean saveUpdateEntity(EntityVO entity){
		
		EntityDAO eDAO = new EntityDAO();
		return eDAO.saveUpdate(entity, null, true);
	}
	
	public AttributeModel getAttributeByIdClassFieldName(Integer idEntity, String attributeName){
		
		AttributeDAO aDAO = new AttributeDAO();
		Object o = aDAO.getAttributeByIdEntityAttributeName(AttributeModel.class.getName(), idEntity, attributeName);
		if (o != null){
			return (AttributeModel)o;
		}else{
			return null;
		}
		
	}

	public AttributeModel getAttributeByClassNameAttributeNameAttributeType(String className, 
																			String attributeName, 
																			String attributeType){
		
		AttributeDAO aDAO = new AttributeDAO();
		
		Object o = aDAO.getAttributeByEntityClassNameAttributeNameAttributeType(AttributeModel.class.getName(), 
																			    className, 
																			    attributeName, 
																			    attributeType);
		if (o != null){
			return (AttributeModel)o;
		}else{
			return null;
		}
		
	}	
	
	public boolean saveUpdateAttribute(AttributeVO attribute){
		
		AttributeDAO aDAO = new AttributeDAO();
		return aDAO.saveUpdate(attribute, null, true);
	}

	public AttributeValueModel getAttributeValueByIdFieldIdObject(Integer idAttribute, Integer idObject){
		
		AttributeValueDAO avDAO = new AttributeValueDAO();
		return avDAO.getAttributeValueByIdAttributeIdObject(idAttribute, idObject);
	}
	
	public boolean saveUpdateAttributeValue(AttributeValueVO attributeValue){
		
		AttributeValueDAO avDAO = new AttributeValueDAO();
		return avDAO.saveUpdate(attributeValue, null, true);
	}
	
	
}
