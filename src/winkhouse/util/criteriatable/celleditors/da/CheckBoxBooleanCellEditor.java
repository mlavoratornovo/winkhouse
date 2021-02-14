package winkhouse.util.criteriatable.celleditors.da;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import winkhouse.Activator;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.criteriatable.celleditors.WinkComboBoxViewerCellEditor;
import winkhouse.util.criteriatable.valueprovider.da.BooleanDaValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.ClassiClientiDaAValueProvider;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;

public class CheckBoxBooleanCellEditor extends WinkComboBoxViewerCellEditor{
	
	private boolean boolVal = false;
	
	public CheckBoxBooleanCellEditor(Composite c) {
		super(c);
		BooleanDaValueProvider cedv = new BooleanDaValueProvider();
		setInput(cedv.getDaAValueObjects());						

	}
	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if ((entityOwner == ICriteriaOwners.COLLOQUI) && 
			(criteriaTableModel.getPropertiesFieldDescriptior()
			                   .getMethodName().equalsIgnoreCase(ColloquiMethodName.GET_SCADENZIERE))){
			return true;
		}
		
		return false;
	}


	
}
