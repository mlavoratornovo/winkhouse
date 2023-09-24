package winkhouse.util;

import java.util.ArrayList;

import winkhouse.model.AttributeModel;
import winkhouse.model.EntityModel;

public interface IEntityAttribute {

	public Integer getIdInstanceObject();
	
	public EntityModel getEntity();
	
	public ArrayList<AttributeModel> getAttributes();
	
}
