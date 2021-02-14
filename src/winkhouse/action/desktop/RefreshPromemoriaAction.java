package winkhouse.action.desktop;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.view.desktop.DesktopView;

public class RefreshPromemoriaAction extends Action {
	
	private DesktopView desktop = null;
	
	public RefreshPromemoriaAction(DesktopView desktop) {
		this.setToolTipText("Aggiorna desktop");
		this.setImageDescriptor(Activator.getImageDescriptor("icons/adept_reinstall.png"));
		this.desktop = desktop;

	}
	
	@Override
	public void run() {
		
		DesktopView dv = (DesktopView)PlatformUI.getWorkbench()
												.getActiveWorkbenchWindow()
												.getActivePage()
												.getActivePart();

		if (dv.getDesktop_type() == DesktopView.PROMEMORIA_TYPE){

			desktop.setAgente(desktop.getAgente());
			
		}else{
			desktop.addItemToExplore(new ArrayList());
		}
		
	}

}
