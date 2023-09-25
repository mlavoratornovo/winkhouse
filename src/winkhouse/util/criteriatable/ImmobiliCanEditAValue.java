package winkhouse.util.criteriatable;

import winkhouse.dao.AttributeDAO;
import winkhouse.model.AttributeModel;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.widgets.data.ICanEditCellHandler;
import winkhouse.widgets.data.ICriteriaTableModel;

public class ImmobiliCanEditAValue implements ICanEditCellHandler {

	protected AttributeDAO attributeDAO = null;
	
	public ImmobiliCanEditAValue(){
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
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODTIPOLOGIA) ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODSTATO) ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODRISCALDAMENTO) ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODAGENTEINSERITORE) ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODCLASSEENERGETICA) ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.MTIPOLOGIASTANZA) ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_ZONA) ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CITTA) ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_PROVINCIA) ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CAP) ||
					ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_RIF) ||					
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