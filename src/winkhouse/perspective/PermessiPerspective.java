package winkhouse.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import winkhouse.view.permessi.AgentiView;
import winkhouse.view.permessi.DettaglioPermessiAgenteView;

public class PermessiPerspective implements IPerspectiveFactory {
	
	public final static String ID = "winkhouse.permessi";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);		
		IFolderLayout fllistaagenti = layout.createFolder("listaagenti", IPageLayout.LEFT, 0.20f, editorArea);
		fllistaagenti.addView(AgentiView.ID);
		layout.getViewLayout(AgentiView.ID).setCloseable(false);
		layout.getViewLayout(AgentiView.ID).setMoveable(false);
		
		IFolderLayout fldettaglioimmobile = layout.createFolder("dettagliopermessoagente", IPageLayout.RIGHT, 0.25f, AgentiView.ID);
		fldettaglioimmobile.addPlaceholder(DettaglioPermessiAgenteView.ID);
		fldettaglioimmobile.addPlaceholder(DettaglioPermessiAgenteView.ID+":*");


	}

}
