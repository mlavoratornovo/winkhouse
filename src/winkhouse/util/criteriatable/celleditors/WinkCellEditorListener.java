package winkhouse.util.criteriatable.celleditors;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;

public class WinkCellEditorListener implements ICellEditorListener {

	private CellEditor cellEditor = null;
	
	public WinkCellEditorListener(CellEditor cellEditor){
		this.cellEditor = cellEditor; 
	}

	@Override
	public void applyEditorValue() {
		if (cellEditor.getErrorMessage() != null){
			MessageDialog.openError(cellEditor.getControl().getShell(), 
									"Errore", 
									cellEditor.getErrorMessage());
		}
	}

	@Override
	public void cancelEditor() {
		
	}

	@Override
	public void editorValueChanged(boolean oldValidState,
			boolean newValidState) {
		
	}
		
}
