package winkhouse.action.winkcloud;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.winkcloud.CloudMonitorState;
import winkhouse.model.winkcloud.MonitorModel;
import winkhouse.model.winkcloud.jobs.HTTPRicercaJob;
import winkhouse.model.xml.RicercheXMLModel;
import winkhouse.view.winkcloud.MonitorsTreeView;

public class AvviaRicercaWinkCloudAction extends Action {

	public AvviaRicercaWinkCloudAction() {
	}

	public AvviaRicercaWinkCloudAction(String text) {
		super(text);
	}

	public AvviaRicercaWinkCloudAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public AvviaRicercaWinkCloudAction(String text, int style) {
		super(text, style);
	}

	@Override
	public void run() {
		Object o = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
//		QueryFilesView qfv = (QueryFilesView)PlatformUI.getWorkbench()
//					   								   .getActiveWorkbenchWindow()
//					   								   .getActivePage()
//					   								   .findView(QueryFilesView.ID);

		if (o instanceof MonitorsTreeView){
			
			if (((MonitorsTreeView)o).getViewer().getSelection() != null){
				
				Object objsel = ((StructuredSelection)((MonitorsTreeView)o).getViewer().getSelection()).getFirstElement();
				if (objsel instanceof RicercheXMLModel){
					if (((RicercheXMLModel)objsel).getState() == null || ((RicercheXMLModel)objsel).getState() == CloudMonitorState.PAUSA){
						((RicercheXMLModel)objsel).setTvMonitors(((MonitorsTreeView) o).getViewer());
						((RicercheXMLModel)objsel).setStato(CloudMonitorState.ATTIVO);
						((RicercheXMLModel)objsel).start();	
						((MonitorsTreeView)o).getViewer().refresh();
						((MonitorsTreeView)o).getViewer().expandToLevel(4);
					}
				}else{
					MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
											  "Avvio Ricerca",
											  "Ricerca già attiva");
				}
			}
		}

	}

	
}
