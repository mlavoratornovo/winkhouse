package winkhouse.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import winkhouse.view.anagrafica.AnagraficaTreeView;
import winkhouse.view.anagrafica.DettaglioAnagraficaView;
import winkhouse.view.colloqui.ColloquiTreeView;
import winkhouse.view.colloqui.DettaglioColloquioView;
import winkhouse.view.common.AbbinamentiView;
import winkhouse.view.common.EAVView;
import winkhouse.view.common.RecapitiView;
import winkhouse.view.immobili.DettaglioImmobileView;

public class ColloquiPerspective implements IPerspectiveFactory {
	
	public final static String ID = "winkhouse.colloqui";
	
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);		
		
		IFolderLayout fllistacolloqui = layout.createFolder("listacolloqui", IPageLayout.LEFT|IPageLayout.TOP, 0.20f, editorArea);
		fllistacolloqui.addView(ColloquiTreeView.ID);		
		layout.getViewLayout(ColloquiTreeView.ID).setCloseable(false);
		layout.getViewLayout(ColloquiTreeView.ID).setMoveable(false);
		
		IFolderLayout fldettagliocolloquio = layout.createFolder("dettagliocolloquio", IPageLayout.RIGHT, 0.20f, "listacolloqui");		
		fldettagliocolloquio.addPlaceholder(DettaglioAnagraficaView.ID);
		fldettagliocolloquio.addPlaceholder(DettaglioAnagraficaView.ID+":*");
		fldettagliocolloquio.addPlaceholder(DettaglioColloquioView.ID);
		fldettagliocolloquio.addPlaceholder(DettaglioColloquioView.ID+":*");
		fldettagliocolloquio.addPlaceholder(DettaglioImmobileView.ID);
		fldettagliocolloquio.addPlaceholder(DettaglioImmobileView.ID+":*");

		IFolderLayout fldaticolloquio = layout.createFolder("daticolloquio", IPageLayout.BOTTOM, 0.8f, "dettagliocolloquio");
		
		fldaticolloquio.addView(AbbinamentiView.ID);
		layout.getViewLayout(AbbinamentiView.ID).setCloseable(false);
		layout.getViewLayout(AbbinamentiView.ID).setMoveable(false);
		
		fldaticolloquio.addView(EAVView.ID);
		layout.getViewLayout(EAVView.ID).setCloseable(false);
		layout.getViewLayout(EAVView.ID).setMoveable(false);

		
	}

}
