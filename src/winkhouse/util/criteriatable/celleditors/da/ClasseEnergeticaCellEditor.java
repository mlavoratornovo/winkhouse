package winkhouse.util.criteriatable.celleditors.da;

import org.eclipse.swt.widgets.Composite;

import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.criteriatable.celleditors.WinkComboBoxViewerCellEditor;
import winkhouse.util.criteriatable.valueprovider.da.ClasseEnergeticaDaAValueProvider;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;

public class ClasseEnergeticaCellEditor extends WinkComboBoxViewerCellEditor {


	public ClasseEnergeticaCellEditor(Composite c) {
		super(c);
		ClasseEnergeticaDaAValueProvider cedv = new ClasseEnergeticaDaAValueProvider();
		setInput(cedv.getDaAValueObjects());						
	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if (((entityOwner == ICriteriaOwners.IMMOBILI) || ((entityOwner == ICriteriaOwners.AFFITTI))) && 
			(criteriaTableModel.getPropertiesFieldDescriptior()
					 		   .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CODCLASSEENERGETICA))){
			return true;
		}else{
			return false;
		}
		
	}
	
}
