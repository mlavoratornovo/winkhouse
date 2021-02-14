package winkhouse.action.winkcloud;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.winkcloud.MonitorModel;
import winkhouse.view.winkcloud.MonitorsTreeView;
import winkhouse.wizard.NewMonitorWizard;

public class NewCloudMonitorAction extends Action {

	public NewCloudMonitorAction() {
		// TODO Auto-generated constructor stub
	}

	public NewCloudMonitorAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public NewCloudMonitorAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public NewCloudMonitorAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	public void run() {
		
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		NewMonitorWizard wizard = new NewMonitorWizard();
		
		IWorkbenchPart wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		if (wp instanceof MonitorsTreeView){
			wizard.setReturnObject(wp);
			try {
				wizard.setReturnMethod(MonitorsTreeView.class.getMethod("addMonitor", new Class[]{MonitorModel.class}));
				WizardDialog dialog = new WizardDialog(window.getShell(), wizard);	
				dialog.setPageSize(400, 300);			
				dialog.open();				
			} catch (SecurityException e) {
			} catch (NoSuchMethodException e) {
			}
		}
	}

}
