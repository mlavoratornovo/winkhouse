package winkhouse.view.common.celleditors;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class DoubleTextCellEditor extends TextCellEditor {

	public DoubleTextCellEditor() {}

	public DoubleTextCellEditor(Composite parent) {
		super(parent);

	}

	public DoubleTextCellEditor(Composite parent, int style) {
		super(parent, style);

	}

	
	@Override
	protected void doSetValue(Object value) {
		try {
			Double.valueOf((String) value);
		} catch (NumberFormatException e) {
			value = "0.0";
		}catch(NullPointerException npe){
			value = "0.0";
		}

		super.doSetValue(value);
	}

	
}
