package winkhouse.util.criteriatable.celleditors.da;

import org.eclipse.swt.widgets.Composite;

import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.criteriatable.celleditors.WinkComboBoxViewerCellEditor;
import winkhouse.util.criteriatable.valueprovider.da.TipologiaImmobileDaValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.TipologiaStanzeDaValueProvider;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;

public class TipologiaStanzeCellEditor extends WinkComboBoxViewerCellEditor {

	public TipologiaStanzeCellEditor(Composite c) {
		super(c);
		TipologiaStanzeDaValueProvider tsdv = new TipologiaStanzeDaValueProvider();
		setInput(tsdv.getDaAValueObjects());			
	}
	
	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if (((entityOwner == ICriteriaOwners.IMMOBILI) || ((entityOwner == ICriteriaOwners.AFFITTI))) &&
			(criteriaTableModel.getPropertiesFieldDescriptior()
					 		   .getMethodName().equalsIgnoreCase(ImmobiliMethodName.MTIPOLOGIASTANZA))){
			return true;
		}else{
			return false;
		}
		
	}

}
