package winkhouse.action.report;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import winkhouse.helper.ReportHelper;
import winkhouse.view.report.ReportTreeView;


public class ImportReportAction extends Action {

	public ImportReportAction(String text, ImageDescriptor image) {
		super(text, image);
		setId("winkhouse.importreport");
	}

	@Override
	public void run() {
		ReportHelper rh = new ReportHelper();
		rh.importReport();	
		IViewPart ivp = PlatformUI.getWorkbench()
				  .getActiveWorkbenchWindow()
				  .getActivePage()
				  .findView(ReportTreeView.ID);
		if (ivp != null){
			((ReportTreeView)ivp).getViewer().refresh();
		}
	}

}
