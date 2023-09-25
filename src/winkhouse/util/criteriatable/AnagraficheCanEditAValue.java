package winkhouse.util.criteriatable;

import winkhouse.dao.AttributeDAO;
import winkhouse.model.AttributeModel;
import winkhouse.util.AnagraficheMethodName;
import winkhouse.widgets.data.ICanEditCellHandler;
import winkhouse.widgets.data.ICriteriaTableModel;

public class AnagraficheCanEditAValue implements ICanEditCellHandler {

	protected AttributeDAO attributeDAO = null;
	
	public AnagraficheCanEditAValue(){
		attributeDAO = new AttributeDAO();
	}
	
	@Override
	public boolean canEdit(ICriteriaTableModel ctm) {
		
		if (ctm.getPropertiesFieldDescriptior().getIsPersonal()){
		
			AttributeModel am = attributeDAO.getAttributeByID(Integer.valueOf(ctm.getPropertiesFieldDescriptior()
												 								 .getMethodName()));
			return !am.getFieldType().equalsIgnoreCase(String.class.getName());
			
		}else{
			if (ctm.getPropertiesFieldDescriptior() == null ||
				(ctm.getPropertiesFieldDescriptior()
				    .getMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CODAGENTEINSERITORE) ||
				 ctm.getPropertiesFieldDescriptior()
					.getMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CODCLASSECLIENTE) ||
				 ctm.getPropertiesFieldDescriptior()
					.getMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_DATAINSERIMENTO)) ||
				 ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase("(") ||
				 ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(")")
				){
			
				return false;
			}else{
				return true;
			}
		}

	}

}