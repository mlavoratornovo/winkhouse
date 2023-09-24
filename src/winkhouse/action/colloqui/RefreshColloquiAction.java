package winkhouse.action.colloqui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.colloqui.ColloquiTreeView;
import winkhouse.view.immobili.ImmobiliTreeView;


public class RefreshColloquiAction extends Action {

	public RefreshColloquiAction() {
	}

	public RefreshColloquiAction(String text) {
		super(text);
	}

	public RefreshColloquiAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public RefreshColloquiAction(String text, int style) {
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
					ColloquiTreeView ctv = null;
					
					ctv = (ColloquiTreeView)PlatformUI.getWorkbench()
													  .getActiveWorkbenchWindow()
								  					  .getActivePage()
								  					  .findView(ColloquiTreeView.ID);
					ctv.getViewer().setInput(new Object());					
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

	}

}
