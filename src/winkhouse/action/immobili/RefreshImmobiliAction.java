package winkhouse.action.immobili;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.immobili.ImmobiliTreeView;


public class RefreshImmobiliAction extends Action {

	public RefreshImmobiliAction() {
	}

	public RefreshImmobiliAction(String text) {
		super(text);
	}

	public RefreshImmobiliAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public RefreshImmobiliAction(String text, int style) {
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
					ImmobiliTreeView itv = null;
					
					itv = (ImmobiliTreeView)PlatformUI.getWorkbench()
													  .getActiveWorkbenchWindow()
								  					  .getActivePage()
								  					  .findView(ImmobiliTreeView.ID);
					itv.getViewer().setInput(new Object());					
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

	}

}
