package winkhouse.util.criteriatable;

import winkhouse.dao.AttributeDAO;
import winkhouse.model.AttributeModel;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.widgets.data.ICanEditCellHandler;
import winkhouse.widgets.data.ICriteriaTableModel;

public class ColloquiCanEditAValue implements ICanEditCellHandler {

	protected AttributeDAO attributeDAO = null;
	
	public ColloquiCanEditAValue() {
		attributeDAO = new AttributeDAO();
	}

	@Override
	public boolean canEdit(ICriteriaTableModel ctm) {
		try{
		if (ctm.getPropertiesFieldDescriptior().getIsPersonal()){
		
			AttributeModel am = attributeDAO.getAttributeByID(Integer.valueOf(ctm.getPropertiesFieldDescriptior()
												 								 .getMethodName()));
			return !(am.getFieldType().equalsIgnoreCase(String.class.getName()) || am.getFieldType().equalsIgnoreCase(Boolean.class.getName()));
			
		}else{
			if (
					ctm.getPropertiesFieldDescriptior() == null ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ColloquiMethodName.GET_TIPOLOGIA) ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ColloquiMethodName.GET_AGENTI) ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ColloquiMethodName.GET_ANAGRAFICHE) ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ColloquiMethodName.GET_IMMOBILE_ABBINATO) ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase("(") ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(")")
				){
			
				return false;
			}else{
				return true;
			}
		}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}

}
