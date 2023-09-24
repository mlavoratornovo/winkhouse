package winkhouse.action.permessi;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.view.permessi.DettaglioPermessiAgenteView;

public class RefreshPermessiAgente extends Action {

	public RefreshPermessiAgente() {
		setImageDescriptor(Activator.getImageDescriptor("icons/adept_reinstall.png"));
		setToolTipText("Aggiorna i permessi dell'agente");
	}

	@Override
	public void run() {
		
		DettaglioPermessiAgenteView div = (DettaglioPermessiAgenteView)PlatformUI.getWorkbench()
																				 .getActiveWorkbenchWindow()
																				 .getActivePage()
																				 .getActivePart();
		
		div.refreshMe();
	}
	
	

}
