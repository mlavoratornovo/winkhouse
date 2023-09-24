package winkhouse.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import winkhouse.view.desktop.DesktopView;


public class DesktopPerspective implements IPerspectiveFactory {

	public final static String ID = "winkhouse.desktop";
	
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		IFolderLayout folder = layout.createFolder("Promemoria", IPageLayout.TOP, 0.5f, editorArea);
		folder.addView(DesktopView.ID);
		layout.getViewLayout(DesktopView.ID).setCloseable(false);
		layout.getViewLayout(DesktopView.ID).setMoveable(false);

//		layout.addStandaloneView(DesktopView.ID,  false, IPageLayout.LEFT, 0.25f, editorArea);
	}
}
