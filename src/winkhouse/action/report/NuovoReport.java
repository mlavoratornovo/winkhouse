package winkhouse.action.report;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import winkhouse.model.ReportModel;
import winkhouse.view.report.handler.DettaglioReportHandler;


public class NuovoReport extends Action {

	public NuovoReport(String text, ImageDescriptor image) {
		super(text, image);
	}

	@Override
	public void run() {
		ReportModel rModel;	
		rModel = new ReportModel();
		DettaglioReportHandler.getInstance().getDettaglioImmobile(rModel);	

	}
	
	

}
