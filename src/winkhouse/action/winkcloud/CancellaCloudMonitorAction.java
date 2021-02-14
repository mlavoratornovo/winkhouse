package winkhouse.action.winkcloud;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.winkcloud.CloudMonitorState;
import winkhouse.model.winkcloud.MonitorFTPModel;
import winkhouse.model.winkcloud.MonitorGDriveModel;
import winkhouse.model.winkcloud.MonitorHTTPModel;
import winkhouse.model.xml.RicercheXMLModel;
import winkhouse.view.winkcloud.MonitorsTreeView;

public class CancellaCloudMonitorAction extends Action {

	public CancellaCloudMonitorAction() {
	}

	public CancellaCloudMonitorAction(String text) {
		super(text);
	}

	public CancellaCloudMonitorAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public CancellaCloudMonitorAction(String text, int style) {
		super(text, style);
	}

	
	@Override
	public void run() {		
		
		Object o = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		
		if (o instanceof MonitorsTreeView){
			
			if (((MonitorsTreeView)o).getViewer().getSelection() != null){
				
				Object objsel = ((TreeSelection)((MonitorsTreeView)o).getViewer().getSelection()).getFirstElement();
				if (objsel instanceof MonitorFTPModel){
					if (((MonitorFTPModel)objsel).getStato() == null || ((MonitorFTPModel)objsel).getStato() == CloudMonitorState.PAUSA){
						((MonitorFTPModel)objsel).setChkvar(false);
						((MonitorsTreeView)o).getMonitors().remove(objsel);
						((MonitorsTreeView)o).getViewer().refresh();
					}
				}else if (objsel instanceof MonitorHTTPModel){
					
						ArrayList<RicercheXMLModel> ricerche = ((MonitorHTTPModel)objsel).getRicerche();
						
						for (Iterator iterator = ricerche.iterator(); iterator.hasNext();) {
							RicercheXMLModel ricercheXMLModel = (RicercheXMLModel) iterator.next();
							ricercheXMLModel.stop();
						}
						
						((MonitorHTTPModel)objsel).stop();
						((MonitorsTreeView)o).getMonitors().remove(objsel);
						((MonitorsTreeView)o).getViewer().refresh();
										
				}else if (objsel instanceof Map.Entry){
					Object oparent = ((TreeSelection)((MonitorsTreeView)o).getViewer().getSelection()).getPaths()[0].getParentPath().getLastSegment();
					if (oparent instanceof MonitorHTTPModel){
						((MonitorHTTPModel)oparent).getRicercheServite().remove(((Map.Entry)objsel).getKey()); 
						if (((MonitorHTTPModel)oparent).getPathloadingFile() != null){
							((MonitorHTTPModel)oparent).save(((MonitorHTTPModel)oparent).getPathloadingFile());
						}

						((MonitorsTreeView)o).getViewer().refresh();
					}
				}else if (objsel instanceof RicercheXMLModel){
						Object oparent = ((TreeSelection)((MonitorsTreeView)o).getViewer().getSelection()).getPaths()[0].getParentPath().getLastSegment();
						if (oparent instanceof MonitorHTTPModel){
							int index = -1;
							for (int i = 0; i < ((MonitorHTTPModel)oparent).getRicerche().size(); i++) {
								RicercheXMLModel array_element = ((MonitorHTTPModel)oparent).getRicerche().get(i);
								if (array_element == objsel){
									index = i;
									break;
								}
							}
							if (index != -1){
								((MonitorHTTPModel)oparent).getRicerche().remove(index);
							}
							if (((MonitorHTTPModel)oparent).getPathloadingFile() != null){
								((MonitorHTTPModel)oparent).save(((MonitorHTTPModel)oparent).getPathloadingFile());
							}
							((MonitorsTreeView)o).getViewer().refresh();
						}					
				}else{
					MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
											  "Cancellazione Monitor",
											  "Tipo monitor non riconosciuto");
				}
			}
		}
		
		
	}

}
