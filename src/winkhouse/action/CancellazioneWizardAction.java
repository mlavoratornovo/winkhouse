package winkhouse.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.wizard.CancellazioneWizard;


public class CancellazioneWizardAction extends Action {

	public static String ID = "winkhouse.wizardcancellazioneaction";
	
	public CancellazioneWizardAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	@Override
	public void run() {
		
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		CancellazioneWizard wizard = new CancellazioneWizard();
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.setPageSize(400, 300);
		dialog.open();

	}

}
