package winkhouse.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import winkhouse.view.affitti.DettaglioAffittiView;
import winkhouse.view.affitti.ListaAffittiView;
import winkhouse.view.common.AbbinamentiView;
import winkhouse.view.common.ColloquiView;
import winkhouse.view.common.EAVView;
import winkhouse.view.common.MapView;
import winkhouse.view.common.RecapitiView;
import winkhouse.view.immobili.AnagrafichePropietarieView;
import winkhouse.view.immobili.DettaglioImmobileView;
import winkhouse.view.immobili.ImmaginiImmobiliView;
import winkhouse.view.immobili.ImmobiliTreeView;


public class AffittiPerspective implements IPerspectiveFactory {

	public final static String ID = "winkhouse.affitti";
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);		
		IFolderLayout fllistaimmobili = layout.createFolder("listaimmobili", IPageLayout.LEFT, 0.20f, editorArea);
		fllistaimmobili.addView(ImmobiliTreeView.ID);
		layout.getViewLayout(ImmobiliTreeView.ID).setCloseable(false);
		layout.getViewLayout(ImmobiliTreeView.ID).setMoveable(false);
		
		IFolderLayout fldettaglioimmobile = layout.createFolder("dettaglioaffitto", IPageLayout.RIGHT, 0.25f, ImmobiliTreeView.ID);
		fldettaglioimmobile.addPlaceholder(DettaglioImmobileView.ID);
		fldettaglioimmobile.addPlaceholder(DettaglioImmobileView.ID+":*");
		fldettaglioimmobile.addPlaceholder(DettaglioAffittiView.ID);
		fldettaglioimmobile.addPlaceholder(DettaglioAffittiView.ID+":*");
		
		
		IFolderLayout fldatiimmobile = layout.createFolder("listaaffitti", IPageLayout.BOTTOM, 0.8f, "dettaglioaffitto");
		
		fldatiimmobile.addView(ListaAffittiView.ID);
		layout.getViewLayout(ListaAffittiView.ID).setCloseable(false);
		layout.getViewLayout(ListaAffittiView.ID).setMoveable(false);
		
		fldatiimmobile.addView(RecapitiView.ID);
		layout.getViewLayout(RecapitiView.ID).setCloseable(false);
		layout.getViewLayout(RecapitiView.ID).setMoveable(false);
		
		fldatiimmobile.addView(ColloquiView.ID);
		layout.getViewLayout(ColloquiView.ID).setCloseable(false);
		layout.getViewLayout(ColloquiView.ID).setMoveable(false);
		
		fldatiimmobile.addView(ImmaginiImmobiliView.ID);
		layout.getViewLayout(ImmaginiImmobiliView.ID).setCloseable(false);
		layout.getViewLayout(ImmaginiImmobiliView.ID).setMoveable(false);
		
		fldatiimmobile.addView(AnagrafichePropietarieView.ID);
		layout.getViewLayout(AnagrafichePropietarieView.ID).setCloseable(false);
		layout.getViewLayout(AnagrafichePropietarieView.ID).setMoveable(false);		

		fldatiimmobile.addView(AbbinamentiView.ID);
		layout.getViewLayout(AbbinamentiView.ID).setCloseable(false);
		layout.getViewLayout(AbbinamentiView.ID).setMoveable(false);

		fldatiimmobile.addView(EAVView.ID);
		layout.getViewLayout(EAVView.ID).setCloseable(false);
		layout.getViewLayout(EAVView.ID).setMoveable(false);

		fldatiimmobile.addView(MapView.ID);
		layout.getViewLayout(MapView.ID).setCloseable(false);
		layout.getViewLayout(MapView.ID).setMoveable(false);

	}

}
