package winkhouse.action.winkcloud;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.winkcloud.CloudMonitorState;
import winkhouse.model.winkcloud.MonitorModel;
import winkhouse.view.winkcloud.MonitorsTreeView;

public class SalvaCloudMonitor extends Action {

	public SalvaCloudMonitor() {
	}

	public SalvaCloudMonitor(String text) {
		super(text);
	}

	public SalvaCloudMonitor(String text, ImageDescriptor image) {
		super(text, image);
	}

	public SalvaCloudMonitor(String text, int style) {
		super(text, style);
	}

	
	@Override
	public void run() {
		
		Object o = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		
		if (o instanceof MonitorsTreeView){
			
			if (((MonitorsTreeView)o).getViewer().getSelection() != null){
				
				Object objsel = ((StructuredSelection)((MonitorsTreeView)o).getViewer().getSelection()).getFirstElement();
				if (objsel instanceof MonitorModel){
					
					DirectoryDialog dd = new DirectoryDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
					
					String path = dd.open();
					((MonitorModel)objsel).save(path + File.separator + ((MonitorModel)objsel).toString().replace(":", "_")
																										 .replace(".", "_")
																										 .replace("/", "_")
																										 .replace("@", "_") + ".xml"); 
						
					
					
				}else{
					MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
											  "Pausa Monitor",
											  "Tipo monitor non riconosciuto");
				}
			}
		}
	}

	
}
