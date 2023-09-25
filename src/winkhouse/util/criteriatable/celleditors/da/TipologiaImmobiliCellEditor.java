package winkhouse.util.criteriatable.celleditors.da;

import org.eclipse.swt.widgets.Composite;

import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.criteriatable.celleditors.WinkComboBoxViewerCellEditor;
import winkhouse.util.criteriatable.valueprovider.da.TipologiaImmobileDaValueProvider;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;

public class TipologiaImmobiliCellEditor extends WinkComboBoxViewerCellEditor {

	public TipologiaImmobiliCellEditor(Composite c) {
		super(c);
		TipologiaImmobileDaValueProvider tidv = new TipologiaImmobileDaValueProvider();
		setInput(tidv.getDaAValueObjects());			
	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if (((entityOwner == ICriteriaOwners.IMMOBILI) || ((entityOwner == ICriteriaOwners.AFFITTI))) &&
			(criteriaTableModel.getPropertiesFieldDescriptior()
					 		   .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODTIPOLOGIA))){
			return true;
		}else{
			return false;
		}
		
	}
	
}