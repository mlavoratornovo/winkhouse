package winkhouse.action.winkcloud;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.winkcloud.CloudMonitorState;
import winkhouse.model.winkcloud.MonitorModel;
import winkhouse.view.winkcloud.MonitorsTreeView;
import winkhouse.view.winkcloud.QueryFilesView;

public class AvviaCloudMonitorAction extends Action {

	public AvviaCloudMonitorAction() {
		
	}

	public AvviaCloudMonitorAction(String text) {
		super(text);	
	}

	public AvviaCloudMonitorAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public AvviaCloudMonitorAction(String text, int style) {
		super(text, style);
	}

	
	@Override
	public void run() {
		
		Object o = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		QueryFilesView qfv = (QueryFilesView)PlatformUI.getWorkbench()
					   								   .getActiveWorkbenchWindow()
					   								   .getActivePage()
					   								   .findView(QueryFilesView.ID);

		if (o instanceof MonitorsTreeView){
			
			if (((MonitorsTreeView)o).getViewer().getSelection() != null){
				
				Object objsel = ((StructuredSelection)((MonitorsTreeView)o).getViewer().getSelection()).getFirstElement();
				if (objsel instanceof MonitorModel){
					if (((MonitorModel)objsel).getStato() == null || ((MonitorModel)objsel).getStato() == CloudMonitorState.PAUSA){
						((MonitorModel)objsel).setStato(CloudMonitorState.ATTIVO);
						((MonitorModel)objsel).setQueryFilesView(qfv);
						((MonitorModel)objsel).start();
						((MonitorsTreeView)o).getViewer().refresh();
						((MonitorsTreeView)o).getViewer().expandAll();
					}
				}else{
					MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
											  "Avvio Monitor",
											  "Monitor già attivo");
				}
			}
		}
		
	}

}
