package winkhouse.action.abbinamenti;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.common.AbbinamentiView;
import winkhouse.wizard.RicercaWizard;


public class AbbinamentiManuale extends Action {


	public AbbinamentiManuale(String text, ImageDescriptor image) {
		super(text, image);

	}

	@Override
	public void run() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		RicercaWizard wizard = null;
		AbbinamentiView av = (AbbinamentiView)PlatformUI.getWorkbench()
	  	  												.getActiveWorkbenchWindow()
	  	  												.getActivePage()
	  	  												.findView(AbbinamentiView.ID);
		if (av.getImmobile() != null){
			wizard = new RicercaWizard(RicercaWizard.ABBINAMENTI_IMMOBILI);
		}
		if (av.getAnagrafica() != null){
			wizard = new RicercaWizard(RicercaWizard.ABBINAMENTI_ANAGRAFICHE);
		}
		if (wizard != null){
			WizardDialog dialog = new WizardDialog(window.getShell(), wizard);	
			dialog.setPageSize(400, 300);
			dialog.open();
		}else{
			MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
										   SWT.ICON_WARNING);
			mb.setText("selezionare un dettaglio immobile o anagrafica");
			mb.setMessage("selezionare un dettaglio immobile o anagrafica");			
			mb.open();													
		}
	}


}
