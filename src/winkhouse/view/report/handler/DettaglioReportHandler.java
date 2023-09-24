package winkhouse.view.report.handler;

import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.ReportModel;
import winkhouse.view.report.DettaglioReportView;


public class DettaglioReportHandler {
	
	private static DettaglioReportHandler instance = null;	
	
	private DettaglioReportHandler(){
		super();
	}
	
	public static DettaglioReportHandler getInstance(){
		if (instance == null){
			instance = new DettaglioReportHandler();			
		}
		return instance;
	}

	public DettaglioReportView getDettaglioImmobile(ReportModel viewInstance){
		DettaglioReportView drv = null;
		IViewReference vr = null;
		
		try {
			vr = PlatformUI.getWorkbench()
	           			   .getActiveWorkbenchWindow()
	           			   .getActivePage()
	           			   .findViewReference(DettaglioReportView.ID,viewInstance.toString());

			if (vr != null){
				drv = (DettaglioReportView)PlatformUI.getWorkbench()
													 .getActiveWorkbenchWindow()
													 .getActivePage()															 
													 .showView(vr.getId(),vr.getSecondaryId(),IWorkbenchPage.VIEW_VISIBLE);
			}else{
				drv = (DettaglioReportView)PlatformUI.getWorkbench()
											 		 .getActiveWorkbenchWindow()
											         .getActivePage()															 
											         .showView(DettaglioReportView.ID,
											        		   String.valueOf(viewInstance.getCodReport()),
											        		   IWorkbenchPage.VIEW_CREATE);
			}
			drv.setReport(viewInstance);

			PlatformUI.getWorkbench()
					  .getActiveWorkbenchWindow()
					  .getActivePage()
					  .bringToTop(drv);				
			drv.setFocus();
			return drv;
		} catch (PartInitException e) {
			return null;
		}
	}

}
