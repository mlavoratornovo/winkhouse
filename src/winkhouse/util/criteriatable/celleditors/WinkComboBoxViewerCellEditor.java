package winkhouse.util.criteriatable.celleditors;

import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.swt.widgets.Composite;

import winkhouse.util.criteriatable.StandardContentProvider;
import winkhouse.util.criteriatable.StandardLabelProvider;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.IDaACellEditor;

public class WinkComboBoxViewerCellEditor extends ComboBoxViewerCellEditor
										  implements IDaACellEditor {

	public WinkComboBoxViewerCellEditor(Composite c){
		super(c);
		this.setContentProvider(new StandardContentProvider());
		this.setLabelProvider(new StandardLabelProvider());
	}
		
	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		return false;
	}

}
