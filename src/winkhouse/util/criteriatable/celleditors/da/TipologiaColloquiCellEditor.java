package winkhouse.util.criteriatable.celleditors.da;

import org.eclipse.swt.widgets.Composite;

import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.criteriatable.celleditors.WinkComboBoxViewerCellEditor;
import winkhouse.util.criteriatable.valueprovider.da.TipologiaColloquiDaValueProvider;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;

public class TipologiaColloquiCellEditor extends WinkComboBoxViewerCellEditor {

	public TipologiaColloquiCellEditor(Composite c) {
		super(c);
		TipologiaColloquiDaValueProvider tidv = new TipologiaColloquiDaValueProvider();
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
