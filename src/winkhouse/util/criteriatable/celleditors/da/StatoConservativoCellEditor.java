package winkhouse.util.criteriatable.celleditors.da;

import org.eclipse.swt.widgets.Composite;

import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.criteriatable.celleditors.WinkComboBoxViewerCellEditor;
import winkhouse.util.criteriatable.valueprovider.da.StatoConservativoDaValueProvider;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
 
public class StatoConservativoCellEditor extends WinkComboBoxViewerCellEditor {

	public StatoConservativoCellEditor(Composite c) {
		super(c);
		StatoConservativoDaValueProvider scdv = new StatoConservativoDaValueProvider();
		setInput(scdv.getDaAValueObjects());
	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if (((entityOwner == ICriteriaOwners.IMMOBILI) || ((entityOwner == ICriteriaOwners.AFFITTI))) &&
			(criteriaTableModel.getPropertiesFieldDescriptior()
					 		   .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODSTATO))){
			return true;
		}else{
			return false;
		}
		
	}
		
}
