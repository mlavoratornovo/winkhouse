package winkhouse.util.criteriatable.celleditors.da;

import org.eclipse.swt.widgets.Composite;

import winkhouse.util.AnagraficheMethodName;
import winkhouse.util.criteriatable.celleditors.WinkComboBoxViewerCellEditor;
import winkhouse.util.criteriatable.valueprovider.da.ClassiClientiDaAValueProvider;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;

public class ClasseClienteCellEditor extends WinkComboBoxViewerCellEditor {

	public ClasseClienteCellEditor(Composite c) {
		super(c);
		ClassiClientiDaAValueProvider cedv = new ClassiClientiDaAValueProvider();
		setInput(cedv.getDaAValueObjects());						

	}
	
	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if ((entityOwner == ICriteriaOwners.ANAGRAFICHE) && 
			(criteriaTableModel.getPropertiesFieldDescriptior()
					 		   .getMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CODCLASSECLIENTE))){
			return true;
		}else{
			return false;
		}
		
	}


}
