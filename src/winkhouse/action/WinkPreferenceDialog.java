package winkhouse.action;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.widgets.Shell;

public class WinkPreferenceDialog extends PreferenceDialog {

	public final static String ID = "winkhouse.preference";
	
	public WinkPreferenceDialog(Shell parentShell, PreferenceManager manager) {
		super(parentShell, manager);
	}

	@Override
	protected void okPressed() {
		super.okPressed();
		close();
	}

	
}
