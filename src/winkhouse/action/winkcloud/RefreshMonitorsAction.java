package winkhouse.action.winkcloud;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.immobili.ImmobiliTreeView;
import winkhouse.view.winkcloud.MonitorsTreeView;


public class RefreshMonitorsAction extends Action {

	public RefreshMonitorsAction() {
	}

	public RefreshMonitorsAction(String text) {
		super(text);
	}

	public RefreshMonitorsAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public RefreshMonitorsAction(String text, int style) {
		super(text, style);
	}

	@Override
	public void run() {
		
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench()
																	    .getActiveWorkbenchWindow()
																	    .getShell());
		try {
			pmd.run(false, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
																 InterruptedException {
					MonitorsTreeView itv = null;
					
					itv = (MonitorsTreeView)PlatformUI.getWorkbench()
													  .getActiveWorkbenchWindow()
								  					  .getActivePage()
								  					  .findView(MonitorsTreeView.ID);
					itv.getViewer().refresh();					
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

	}

}
