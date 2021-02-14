package winkhouse.util.criteriatable.celleditors.da;

import org.eclipse.swt.widgets.Composite;

import winkhouse.util.AnagraficheMethodName;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.criteriatable.celleditors.WinkComboBoxViewerCellEditor;
import winkhouse.util.criteriatable.valueprovider.da.AgenteDaValueProvider;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;

public class AgenteInseritoreCellEditor extends WinkComboBoxViewerCellEditor {

	public AgenteInseritoreCellEditor(Composite c) {
		super(c);
		AgenteDaValueProvider adv = new AgenteDaValueProvider();
		setInput(adv.getDaAValueObjects());
	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if (((entityOwner == ICriteriaOwners.IMMOBILI) || ((entityOwner == ICriteriaOwners.AFFITTI))) && 
			(criteriaTableModel.getPropertiesFieldDescriptior()
					 		   .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODAGENTEINSERITORE))){
			return true;
		}else if ((entityOwner == ICriteriaOwners.ANAGRAFICHE) && 
				  (criteriaTableModel.getPropertiesFieldDescriptior()
			 		                 .getMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CODAGENTEINSERITORE))){
	 			  return true;
			
		}else if ((entityOwner == ICriteriaOwners.COLLOQUI) && 
				  (criteriaTableModel.getPropertiesFieldDescriptior()
	 		                         .getMethodName().equalsIgnoreCase(ColloquiMethodName.GET_AGENTI))){
				  return true;
		}else{
		
			return false;
		}
		
	}
	

}
