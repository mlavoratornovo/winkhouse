package winkhouse.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.helper.ProfilerHelper;
import winkhouse.util.WinkhouseUtils;
import winkhouse.wizard.ColloquiWizard;


public class WizardColloquiAction extends Action {

	public static String ID = "winkhouse.wizardcolloquiaction";
	
	public WizardColloquiAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	@Override
	public void run() {
		if (ProfilerHelper.getInstance().getPermessoUI(ColloquiWizard.ID)){
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			ColloquiWizard wizard = new ColloquiWizard();
			WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
			dialog.setPageSize(400, 300);
			dialog.open();
		}else{
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					  					  "Controllo permessi accesso vista",
					  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
					  					  " non ha il permesso di accedere alla vista " + 
					  					  ColloquiWizard.ID);

		}
	}

}
