package winkhouse.dialogs.custom;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ColorChoserCellEditor extends DialogCellEditor {

	public ColorChoserCellEditor(Composite parent) {
		super(parent);
	}
	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		ColorDialog cd = new ColorDialog(cellEditorWindow.getShell());
		cd.setText("Selezionare un colore");
		RGB rgb = cd.open();		
		return rgb;
	}

}
