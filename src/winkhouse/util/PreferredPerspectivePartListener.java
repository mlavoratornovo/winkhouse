package winkhouse.util;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

public class PreferredPerspectivePartListener implements IPartListener,
		IStartup {

	public PreferredPerspectivePartListener() {
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		refresh(part);
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {

	}

	@Override
	public void partClosed(IWorkbenchPart part) {

	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {

	}

	@Override
	public void partOpened(IWorkbenchPart part) {

	}

	@Override
	public void earlyStartup() {
		 PlatformUI.getWorkbench()
		 		   .getDisplay()
		 		   .getDefault().asyncExec(new Runnable() {

	            public void run() {
	                try {
	                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
	                            .addPartListener(new PreferredPerspectivePartListener());
	                } catch (Exception e) {
	                   e.printStackTrace();
	                }
	            }

	        });


	}
	
	 public static void refresh(final IWorkbenchPart part) {
	        if (!(part instanceof IPrefersPerspective)) {
	            return;
	        }

	        final IWorkbenchWindow workbenchWindow = part.getSite().getPage().getWorkbenchWindow();

	        IPerspectiveDescriptor activePerspective = workbenchWindow.getActivePage().getPerspective();
	        final String preferredPerspectiveId = ((IPrefersPerspective) part)
	                .getPreferredPerspectiveId();

	        if (preferredPerspectiveId == null) {
	            return;
	        }

	        if (activePerspective == null || !activePerspective.getId().equals(preferredPerspectiveId)) {
	            // Switching of the perspective is delayed using Display.asyncExec
	            // because switching the perspective while the workbench is
	            // activating parts might cause conflicts.
/*	            Display.getCurrent().asyncExec(new Runnable() {

	                public void run() {*/
	                    try {
	                        workbenchWindow.getWorkbench().showPerspective(preferredPerspectiveId,
	                                workbenchWindow);
	                    } catch (WorkbenchException e) {
	                    	e.printStackTrace();	                    
	                    }
	              /*  }

	            });*/
	        }
	 }

}
