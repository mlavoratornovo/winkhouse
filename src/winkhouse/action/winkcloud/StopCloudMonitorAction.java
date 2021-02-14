package winkhouse.action.winkcloud;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.winkcloud.CloudMonitorState;
import winkhouse.model.winkcloud.MonitorModel;
import winkhouse.view.winkcloud.MonitorsTreeView;

public class StopCloudMonitorAction extends Action {

	public StopCloudMonitorAction() {
		// TODO Auto-generated constructor stub
	}

	public StopCloudMonitorAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public StopCloudMonitorAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public StopCloudMonitorAction(String text, int style) {
		super(text, style);
	}

	
	@Override
	public void run() {
		
		Object o = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		
		if (o instanceof MonitorsTreeView){
			
			if (((MonitorsTreeView)o).getViewer().getSelection() != null){
				
				Object objsel = ((StructuredSelection)((MonitorsTreeView)o).getViewer().getSelection()).getFirstElement();
				if (objsel instanceof MonitorModel){
					if (((MonitorModel)objsel) == null || ((MonitorModel)objsel).getStato() == CloudMonitorState.ATTIVO){
						((MonitorModel)objsel).setStato(CloudMonitorState.PAUSA);
						((MonitorModel)objsel).stop();
						((MonitorsTreeView)o).getViewer().refresh();
					}
				}else{
					MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
											  "Pausa Monitor",
											  "Monitor già in pausa");
				}
			}
		}

	}

}
