package winkhouse.action.report;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import winkhouse.helper.ReportHelper;
import winkhouse.model.ReportModel;
import winkhouse.view.report.DettaglioReportView;


public class ExportReportAction extends Action {

	public ExportReportAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	@Override
	public void run() {
		
		IWorkbenchPart drv = PlatformUI.getWorkbench()
									   .getActiveWorkbenchWindow()
									   .getActivePage()
									   .getActivePart();
		
		if ((drv != null) && (drv instanceof DettaglioReportView)){
			ReportModel rm = ((DettaglioReportView)drv).getReport();
			ReportHelper rh = new ReportHelper();
			rh.exportReport(rm);
		}
		
	}


}
