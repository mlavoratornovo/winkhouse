package winkhouse.util.criteriatable.celleditors.da;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import winkhouse.model.AnagraficheModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.criteriatable.celleditors.da.AnagraficheSearchEditor.AnagraficaDAaValue;
import winkhouse.view.anagrafica.PopUpRicercaAnagrafica;
import winkhouse.view.immobili.PopUpRicercaImmobile;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.IDaACellEditor;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class ImmobiliSearchCellEditor extends DialogCellEditor implements IDaACellEditor {
	
	private ImmobileDAaValue returnValue = null;
	
	public class ImmobileDAaValue implements IDaAValueObject{
		
		private ImmobiliModel immobile = null;
		
		public ImmobileDAaValue(ImmobiliModel immobile){
			this.immobile = immobile; 
		}
		
		@Override
		public String getDisplayValue() {
			return this.immobile.getCitta() + " " + 
				   this.immobile.getIndirizzo();
				   
		}

		@Override
		public Object getBindValue() {
			return this.immobile;
		}

		@Override
		public String getCodValue() {
			return String.valueOf(this.immobile.getCodImmobile());
		}
		
	}

	public ImmobiliSearchCellEditor() {
	}

	public ImmobiliSearchCellEditor(Composite parent) {
		super(parent);
	}

	public ImmobiliSearchCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		if ((entityOwner == ICriteriaOwners.COLLOQUI) && 
			(criteriaTableModel.getPropertiesFieldDescriptior()
		                           .getMethodName().equalsIgnoreCase(ColloquiMethodName.GET_IMMOBILE_ABBINATO))){
			  return true;
		}
			
		return false;

	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		PopUpRicercaImmobile pupRA = new PopUpRicercaImmobile(cellEditorWindow.getShell());
		pupRA.setCallerObj(this);
		pupRA.setSetterMethodName("setReturnValue");
		pupRA.open();
		return returnValue;

	}
	
	public void setReturnValue(ImmobiliModel immobile){
		returnValue = new ImmobileDAaValue(immobile);
	}

}
