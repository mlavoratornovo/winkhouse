package winkhouse.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import winkhouse.view.report.DettaglioReportView;
import winkhouse.view.report.ReportTreeView;


public class ReportPerspective implements IPerspectiveFactory {

	public final static String ID = "winkhouse.report";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		IFolderLayout fllistreport = layout.createFolder("listareport", IPageLayout.LEFT|IPageLayout.TOP, 0.20f, editorArea);
		fllistreport.addView(ReportTreeView.ID);
		layout.getViewLayout(ReportTreeView.ID).setCloseable(false);
		layout.getViewLayout(ReportTreeView.ID).setMoveable(false);

		IFolderLayout fldettaglioreport = layout.createFolder("dettaglioreport", IPageLayout.RIGHT, 0.20f, "listareport");		
		fldettaglioreport.addPlaceholder(DettaglioReportView.ID);
		fldettaglioreport.addPlaceholder(DettaglioReportView.ID+":*");

	}

}
