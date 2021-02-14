package winkhouse.util.criteriatable.celleditors.da;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import winkhouse.model.AnagraficheModel;
import winkhouse.util.ColloquiMethodName;
import winkhouse.view.anagrafica.PopUpRicercaAnagrafica;
import winkhouse.vo.AgentiVO;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.IDaACellEditor;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;


public class AnagraficheSearchEditor extends DialogCellEditor implements IDaACellEditor{

	public final static String ID = "winkhouse.angraficheminisearch";
	private AnagraficaDAaValue returnValue = null;
	private Object objectReturn = null;
	private String setterMethodName = null;
	
	public class AnagraficaDAaValue implements IDaAValueObject{
		
		private AnagraficheModel anagrafica = null;
		
		public AnagraficaDAaValue(AnagraficheModel anagrafica){
			this.anagrafica = anagrafica; 
		}
		
		@Override
		public String getDisplayValue() {
			return this.anagrafica.getCognome() + " " + 
				   this.anagrafica.getNome() + " " + 
				   this.anagrafica.getRagioneSociale();
		}

		@Override
		public Object getBindValue() {
			return this.anagrafica;
		}

		@Override
		public String getCodValue() {
			return String.valueOf(this.anagrafica.getCodAnagrafica());
		}
		
	}
	
	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if ((entityOwner == ICriteriaOwners.COLLOQUI) && 
			(criteriaTableModel.getPropertiesFieldDescriptior()
	                           .getMethodName().equalsIgnoreCase(ColloquiMethodName.GET_ANAGRAFICHE))){
		  return true;
		}
		
		return false;
	}

	public AnagraficheSearchEditor() {}

	public AnagraficheSearchEditor(Composite parent) {
		super(parent);		
	}

	public AnagraficheSearchEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		PopUpRicercaAnagrafica pupRA = new PopUpRicercaAnagrafica(cellEditorWindow.getShell());
		pupRA.setCallerObj(this);
		pupRA.setSetterMethodName("setReturnValue");
		pupRA.open();
		return returnValue;
	}

	public Object getObjectReturn() {
		return objectReturn;
	}

	public void setObjectReturn(Object objectReturn) {
		this.objectReturn = objectReturn;
	}

	public String getSetterMethodName() {
		return setterMethodName;
	}

	public void setSetterMethodName(String setterMethodName) {
		this.setterMethodName = setterMethodName;
	}

	public void setReturnValue(AnagraficheModel anagrafica){
		returnValue = new AnagraficaDAaValue(anagrafica);
	}

}
