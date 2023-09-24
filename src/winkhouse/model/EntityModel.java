package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import winkhouse.dao.AttributeDAO;
import winkhouse.vo.EntityVO;

public class EntityModel extends EntityVO {

	private ArrayList<AttributeModel> attributes = null;
	
	public EntityModel() {
		
	}

	public EntityModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	public ArrayList<AttributeModel> getAttributes() {
		
		if (attributes == null){
		
			AttributeDAO aDAO = new AttributeDAO();
			attributes = aDAO.getAttributeByIdEntity(super.getIdClassEntity());
			
		}
			
		return attributes;
	}

	public void setAttributes(ArrayList<AttributeModel> attributes) {
		this.attributes = attributes;
	}

}
