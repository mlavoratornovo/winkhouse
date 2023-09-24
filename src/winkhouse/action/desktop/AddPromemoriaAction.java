package winkhouse.action.desktop;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.model.PromemoriaModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.desktop.DesktopView;
import winkhouse.view.desktop.PopUpDettaglioPromemoria;
import winkhouse.wizard.RicercaWizard;

public class AddPromemoriaAction extends Action {

	private DesktopView desktop = null;
	
	public AddPromemoriaAction(DesktopView desktop) {
		this.setToolTipText("Aggiungi promemoria");
		this.setImageDescriptor(Activator.getImageDescriptor("icons/sample2.gif"));
		this.desktop = desktop;
	}

	@Override
	public void run() {
		
		DesktopView dv = (DesktopView)PlatformUI.getWorkbench()
												.getActiveWorkbenchWindow()
												.getActivePage()
												.getActivePart();
		
		if (dv.getDesktop_type() == DesktopView.PROMEMORIA_TYPE){

			PopUpDettaglioPromemoria pdp = new PopUpDettaglioPromemoria(desktop);
			pdp.setPromemoria(new PromemoriaModel());
			
		}else{
			
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			RicercaWizard wizard = new RicercaWizard(RicercaWizard.PROMEMORIA);
			wizard.setWiztype(RicercaWizard.PROMEMORIA);
			wizard.setReturnObject(dv);
			wizard.setReturnObjectMethodName("addItemToExplore");
			wizard.setReturnType(ArrayList.class);
			WizardDialog dialog = new WizardDialog(window.getShell(), wizard);	
			dialog.setPageSize(400, 300);
			WinkhouseUtils.getInstance()
						  .setRicercaWiz(wizard);
			dialog.open();				

			
		}
		
		
	}

	
}
