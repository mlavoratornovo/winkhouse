package winkhouse.action.map;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.common.MapView;

public class MapViewRefreshAction extends Action {

	public MapViewRefreshAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	@Override
	public void run() {
		
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench()
			    														.getActiveWorkbenchWindow()
			    														.getShell());
		try {
			pmd.run(false, false, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,InterruptedException {
					MapView mv = null;

					mv = (MapView)PlatformUI.getWorkbench()
											 .getActiveWorkbenchWindow()
											 .getActivePage()			
											 .findView(MapView.ID);
					
					mv.updateBrowser(mv.getCurrenturl());
					
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
