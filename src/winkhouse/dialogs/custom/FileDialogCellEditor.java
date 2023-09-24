package winkhouse.dialogs.custom;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;

public class FileDialogCellEditor extends DialogCellEditor {

	private Image buttonImage= null;
	private String tootTipButton = null;
	
	public FileDialogCellEditor() {
	}

	public FileDialogCellEditor(Composite parent) {
		super(parent);
	}

	public FileDialogCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	public Image getButtonImage() {
		return buttonImage;
	}

	public void setButtonImage(Image buttonImage) {
		this.buttonImage = buttonImage;
	}

	public String getTootTipButton() {
		return tootTipButton;
	}

	public void setTootTipButton(String tootTipButton) {
		this.tootTipButton = tootTipButton;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		FileDialog fd = new FileDialog(cellEditorWindow.getShell());
	    fd.setText("Open");
	    fd.setFilterPath("C:/");
	    String[] filterExt = { "*.*", "*.txt", "*.doc", ".rtf", ".pdf" };
	    fd.setFilterExtensions(filterExt);
	    fd.setFilterIndex(0);
	    String selected = fd.open();
		return selected;
	}

}
