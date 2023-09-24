package winkhouse.util.criteriatable.celleditors.da;

import org.eclipse.swt.widgets.Composite;

import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.criteriatable.celleditors.WinkComboBoxViewerCellEditor;
import winkhouse.util.criteriatable.valueprovider.da.RiscaldamentoDaValueProvider;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;

public class RiscaldamentoCellEditor extends WinkComboBoxViewerCellEditor {

	public RiscaldamentoCellEditor(Composite c) {
		super(c);
		RiscaldamentoDaValueProvider rdv = new RiscaldamentoDaValueProvider();
		setInput(rdv.getDaAValueObjects());			

	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if (((entityOwner == ICriteriaOwners.IMMOBILI) || ((entityOwner == ICriteriaOwners.AFFITTI))) && 
			(criteriaTableModel.getPropertiesFieldDescriptior()
					 		   .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODRISCALDAMENTO))){
			return true;
		}else{
			return false;
		}
		
	}
	
}
