package winkhouse.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import winkhouse.view.winkcloud.MonitorsTreeView;
import winkhouse.view.winkcloud.QueryFilesView;
import winkhouse.view.winkcloud.ResultsView;

public class CloudPerspective implements IPerspectiveFactory {

	public final static String ID = "winkhouse.winkcloud";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);		
		IFolderLayout flmonitorcartelle = layout.createFolder("monitorcartelle", IPageLayout.LEFT, 0.20f, editorArea);
		flmonitorcartelle.addView(MonitorsTreeView.ID);
		layout.getViewLayout(MonitorsTreeView.ID).setCloseable(false);
		layout.getViewLayout(MonitorsTreeView.ID).setMoveable(false);
		
		IFolderLayout fldettagliomonitor = layout.createFolder("dettagliomonitor", IPageLayout.RIGHT, 0.25f, MonitorsTreeView.ID);
		fldettagliomonitor.addView(QueryFilesView.ID);
		layout.getViewLayout(QueryFilesView.ID).setCloseable(false);
		layout.getViewLayout(QueryFilesView.ID).setMoveable(false);
		
		IFolderLayout fldettagliorisposta = layout.createFolder("dettagliorisposta", IPageLayout.BOTTOM, 0.5f, QueryFilesView.ID);
		fldettagliorisposta.addView(ResultsView.ID);
		layout.getViewLayout(ResultsView.ID).setCloseable(false);
		layout.getViewLayout(ResultsView.ID).setMoveable(false);
		
	}

}
