package winkhouse.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import winkhouse.view.anagrafica.AnagraficaTreeView;
import winkhouse.view.anagrafica.DettaglioAnagraficaView;
import winkhouse.view.anagrafica.ImmobiliPropietaView;
import winkhouse.view.colloqui.DettaglioColloquioView;
import winkhouse.view.common.AbbinamentiView;
import winkhouse.view.common.ColloquiView;
import winkhouse.view.common.EAVView;
import winkhouse.view.common.MapView;
import winkhouse.view.common.RecapitiView;
import winkhouse.view.immobili.DettaglioImmobileView;


public class AnagrafichePerspective implements IPerspectiveFactory {

	public final static String ID = "winkhouse.anagrafica";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);		
		
		IFolderLayout fllistaanagrafiche = layout.createFolder("listaanagrafiche", IPageLayout.LEFT|IPageLayout.TOP, 0.20f, editorArea);
		fllistaanagrafiche.addView(AnagraficaTreeView.ID);		
		layout.getViewLayout(AnagraficaTreeView.ID).setCloseable(false);
		layout.getViewLayout(AnagraficaTreeView.ID).setMoveable(false);
		
		IFolderLayout fldettaglioanagrafica = layout.createFolder("dettaglioanagrafica", IPageLayout.RIGHT, 0.20f, "listaanagrafiche");		
		fldettaglioanagrafica.addPlaceholder(DettaglioAnagraficaView.ID);
		fldettaglioanagrafica.addPlaceholder(DettaglioAnagraficaView.ID+":*");
		fldettaglioanagrafica.addPlaceholder(DettaglioColloquioView.ID);
		fldettaglioanagrafica.addPlaceholder(DettaglioColloquioView.ID+":*");
		fldettaglioanagrafica.addPlaceholder(DettaglioImmobileView.ID);
		fldettaglioanagrafica.addPlaceholder(DettaglioImmobileView.ID+":*");
		
		IFolderLayout fldatianagrafica = layout.createFolder("datianagrafica", IPageLayout.BOTTOM, 0.8f, "dettaglioanagrafica");
		
		fldatianagrafica.addView(RecapitiView.ID);
		layout.getViewLayout(RecapitiView.ID).setCloseable(false);
		layout.getViewLayout(RecapitiView.ID).setMoveable(false);
		
		fldatianagrafica.addView(ColloquiView.ID);
		layout.getViewLayout(ColloquiView.ID).setCloseable(false);
		layout.getViewLayout(ColloquiView.ID).setMoveable(false);
		
		fldatianagrafica.addView(AbbinamentiView.ID);
		layout.getViewLayout(AbbinamentiView.ID).setCloseable(false);
		layout.getViewLayout(AbbinamentiView.ID).setMoveable(false);

		fldatianagrafica.addView(ImmobiliPropietaView.ID);
		layout.getViewLayout(ImmobiliPropietaView.ID).setCloseable(false);
		layout.getViewLayout(ImmobiliPropietaView.ID).setMoveable(false);

		fldatianagrafica.addView(EAVView.ID);
		layout.getViewLayout(EAVView.ID).setCloseable(false);
		layout.getViewLayout(EAVView.ID).setMoveable(false);

		fldatianagrafica.addView(MapView.ID);
		layout.getViewLayout(MapView.ID).setCloseable(false);
		layout.getViewLayout(MapView.ID).setMoveable(false);

	}

}
