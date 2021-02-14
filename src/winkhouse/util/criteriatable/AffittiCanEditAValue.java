package winkhouse.util.criteriatable;

import winkhouse.util.ImmobiliAffittiMethodName;
import winkhouse.widgets.data.ICriteriaTableModel;

public class AffittiCanEditAValue extends ImmobiliCanEditAValue{

	public AffittiCanEditAValue(){

	}
	
	@Override
	public boolean canEdit(ICriteriaTableModel ctm) {
		
		boolean return_value = super.canEdit(ctm);
		
		if (return_value == true){
			
			if (ctm.getPropertiesFieldDescriptior() == null ||
				ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.COGNOME_ANAGRAFICA) ||
				ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.NOME_ANAGRAFICA) ||
				ctm.getPropertiesFieldDescriptior().getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.RAGIONESOCIALE_ANAGRAFICA)){
				
				return_value = false;
			}
			
		}
		
		return return_value;
	}

}
