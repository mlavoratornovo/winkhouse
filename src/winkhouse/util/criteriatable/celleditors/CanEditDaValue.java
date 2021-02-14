package winkhouse.util.criteriatable.celleditors;

import winkhouse.widgets.data.ICanEditCellHandler;
import winkhouse.widgets.data.ICriteriaTableModel;

public class CanEditDaValue implements ICanEditCellHandler {

	@Override
	public boolean canEdit(ICriteriaTableModel ctm) {
		
		if (
				ctm.getPropertiesFieldDescriptior() == null ||
				ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase("(") ||
				ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(")")
			){
			return false;
		}else{
			
			return true;
		}

	}
	
}
