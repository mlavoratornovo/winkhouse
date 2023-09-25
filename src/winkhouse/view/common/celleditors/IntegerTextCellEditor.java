
package winkhouse.view.common.celleditors;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public class IntegerTextCellEditor extends TextCellEditor {

	public IntegerTextCellEditor() {

	}

	public IntegerTextCellEditor(Composite parent) {
		super(parent);

	}

	public IntegerTextCellEditor(Composite parent, int style) {
		super(parent, style);

	}

	
	@Override
	protected Object doGetValue() {
		return super.doGetValue();
	}

	
	@Override
	protected void doSetValue(Object value) {
		try {
			Integer.valueOf((String) value);
		} catch (NumberFormatException e) {
			value = "0";
		}catch(NullPointerException npe){
			value = "0";
		}		
		super.doSetValue(value);
	}

	
	
}
