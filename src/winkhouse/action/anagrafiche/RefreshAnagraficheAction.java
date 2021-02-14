package winkhouse.action.anagrafiche;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.anagrafica.AnagraficaTreeView;


public class RefreshAnagraficheAction extends Action {

	public RefreshAnagraficheAction() {

	}

	public RefreshAnagraficheAction(String text) {
		super(text);

	}

	public RefreshAnagraficheAction(String text, ImageDescriptor image) {
		super(text, image);

	}

	public RefreshAnagraficheAction(String text, int style) {
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
				public void run(IProgressMonitor monitor) throws InvocationTargetException,InterruptedException {
					
					AnagraficaTreeView atv = null;
					
					atv = (AnagraficaTreeView)PlatformUI.getWorkbench()
													  .getActiveWorkbenchWindow()
								  					  .getActivePage()
								  					  .findView(AnagraficaTreeView.ID);
					atv.getViewer().setInput(new Object());
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

}
