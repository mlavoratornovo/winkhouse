package winkhouse.action.winkcloud;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.winkcloud.CloudMonitorState;
import winkhouse.model.winkcloud.MonitorModel;
import winkhouse.model.xml.RicercheXMLModel;
import winkhouse.view.winkcloud.MonitorsTreeView;

public class StopRicercaWinkCloudAction extends Action {

	public StopRicercaWinkCloudAction() {
		// TODO Auto-generated constructor stub
	}

	public StopRicercaWinkCloudAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public StopRicercaWinkCloudAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public StopRicercaWinkCloudAction(String text, int style) {
		super(text, style);
	}

	
	@Override
	public void run() {
		
		Object o = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		
		if (o instanceof MonitorsTreeView){
			
			if (((MonitorsTreeView)o).getViewer().getSelection() != null){
				
				Object objsel = ((StructuredSelection)((MonitorsTreeView)o).getViewer().getSelection()).getFirstElement();
				if (objsel instanceof RicercheXMLModel){
					if (((RicercheXMLModel)objsel) == null || ((RicercheXMLModel)objsel).getState() == CloudMonitorState.ATTIVO){
						((RicercheXMLModel)objsel).setStato(CloudMonitorState.PAUSA);
						((RicercheXMLModel)objsel).stop();	
						((MonitorsTreeView)o).getViewer().refresh();
						((MonitorsTreeView)o).getViewer().expandAll();
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
