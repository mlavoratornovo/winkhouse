package winkhouse.action.report;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.report.ReportTreeView;


public class RefreshReportTreeAction extends Action {

	public RefreshReportTreeAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	@Override
	public void run() {		
		IViewPart ivp = PlatformUI.getWorkbench()
				  				  .getActiveWorkbenchWindow()
				  				  .getActivePage()
				  				  .findView(ReportTreeView.ID);
		
		if ((ivp != null) &&
			(ivp instanceof ReportTreeView)){
			((ReportTreeView)ivp).getViewer().refresh();
		}
		
	}

	

}
