package winkhouse.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import winkhouse.view.datibase.DatiBaseView;


public class DatiBasePerspective implements IPerspectiveFactory {

	public final static String ID = "winkhouse.datibase";
	
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);		
		layout.addStandaloneView(DatiBaseView.ID,  false, IPageLayout.LEFT, 0.25f, editorArea);
	}
}
