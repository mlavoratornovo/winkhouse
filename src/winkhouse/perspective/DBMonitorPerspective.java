package winkhouse.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import winkhouse.view.db.DBMonitorView;


public class DBMonitorPerspective implements IPerspectiveFactory {

	public final static String ID = "winkhouse.dbmonitor";
	
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);		
		layout.addStandaloneView(DBMonitorView.ID,  false, IPageLayout.LEFT, 0.25f, editorArea);
	}
}
