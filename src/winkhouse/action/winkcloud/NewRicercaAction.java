package winkhouse.action.winkcloud;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.winkcloud.WinkMonitorFTPModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.desktop.DesktopView;
import winkhouse.view.winkcloud.MonitorsTreeView;
import winkhouse.wizard.RicercaWizard;

public class NewRicercaAction extends Action{

	private WinkMonitorFTPModel winkMonitorFTPModel = null;
	
	public NewRicercaAction(String text, ImageDescriptor image, WinkMonitorFTPModel winkMonitorFTPModel) {
		super(text, image);
		this.winkMonitorFTPModel = winkMonitorFTPModel;
	}

	@Override
	public void run() {	
		
		MonitorsTreeView dv = (MonitorsTreeView)PlatformUI.getWorkbench()
														  .getActiveWorkbenchWindow()
														  .getActivePage()
														  .getActivePart();
		
		if (dv != null){
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			RicercaWizard wizard = new RicercaWizard(RicercaWizard.IMMOBILI);
			wizard.setWiztype(RicercaWizard.RICERCACLOUD);
			wizard.setReturnObject(this.winkMonitorFTPModel);
			wizard.setReturnObjectMethodName("setAl_criteri");
			wizard.setReturnType(ArrayList.class);
			
			WizardDialog dialog = new WizardDialog(window.getShell(), wizard);	
			dialog.setPageSize(400, 300);
			WinkhouseUtils.getInstance()
						  .setRicercaWiz(wizard);
			dialog.open();
			dv.getViewer().refresh();
		}
		
	}

	
}
