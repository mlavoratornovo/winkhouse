package winkhouse.action.report;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import winkhouse.helper.ReportHelper;
import winkhouse.model.ReportModel;
import winkhouse.view.report.DettaglioReportView;
import winkhouse.view.report.ReportTreeView;


public class CancellaReport extends Action {

	public CancellaReport() {
	}

	public CancellaReport(String text) {
		super(text);
	}

	public CancellaReport(String text, ImageDescriptor image) {
		super(text, image);
	}

	public CancellaReport(String text, int style) {
		super(text, style);
	}

	@Override
	public void run() {
		
		DettaglioReportView drv = (DettaglioReportView)PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			 													 .getActivePage()
			 													 .getActivePart();

		ReportTreeView rtv = (ReportTreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow()
													   .getActivePage()
													   .findView(ReportTreeView.ID);
		
		ReportModel rm = drv.getReport();
		
		ReportHelper rh = new ReportHelper();
		if (rh.deleteReport(rm)){
			PlatformUI.getWorkbench()
			  		  .getActiveWorkbenchWindow()
			  		  .getActivePage()
			  		  .hideView(drv);
	
			rtv.getViewer().refresh();
			
		}else{
			MessageBox mb = new MessageBox(drv.getSite().getShell(),SWT.ERROR);
			mb.setText("Errore cancellazione");
			mb.setMessage("Errore nella cancellazione del report");			
			mb.open();			
		}
		
	}	
	

}
