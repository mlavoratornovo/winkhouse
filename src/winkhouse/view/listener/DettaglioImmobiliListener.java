package winkhouse.view.listener;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Immobili;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.affitti.ListaAffittiView;
import winkhouse.view.colloqui.DettaglioColloquioView;
import winkhouse.view.common.AbbinamentiView;
import winkhouse.view.common.ColloquiView;
import winkhouse.view.common.EAVView;
import winkhouse.view.common.RecapitiView;
import winkhouse.view.immobili.AnagrafichePropietarieView;
import winkhouse.view.immobili.DettaglioImmobileView;
import winkhouse.view.immobili.ImmaginiImmobiliView;


public class DettaglioImmobiliListener implements IPartListener {

	public DettaglioImmobiliListener() {
	}

	@Override
	public void partActivated(IWorkbenchPart part) {

	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {

	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		if (part instanceof DettaglioImmobileView || part instanceof DettaglioColloquioView){
				
				try {
					WinkhouseUtils.getInstance().setLastCodImmobileSelected(null);
					WinkhouseUtils.getInstance().setLastEntityTypeFocused(null);
					IViewPart ivpRecapiti = PlatformUI.getWorkbench()
					   								  .getActiveWorkbenchWindow()
					   								  .getActivePage()														   
					   								  .findView(RecapitiView.ID);
					if (ivpRecapiti != null){
//						ApriDettaglioRecapitiAction adra = new ApriDettaglioRecapitiAction(null,false);
//						adra.run();
//						
						RecapitiView riv = (RecapitiView)ivpRecapiti;
						riv.setAnagrafiche(null);
					}

					IViewPart ivpImmagini = PlatformUI.getWorkbench()
					   								  .getActiveWorkbenchWindow()
					   								  .getActivePage()
					   								  .findView(ImmaginiImmobiliView.ID);
					if (ivpImmagini != null){
						ImmaginiImmobiliView iiv = (ImmaginiImmobiliView)ivpImmagini;
						iiv.setImmobile(WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Immobili.class));
					}
					
					IViewPart ivpColloqui = PlatformUI.getWorkbench()
					  					 			  .getActiveWorkbenchWindow()
					  					 			  .getActivePage() 
					  					 			  .findView(ColloquiView.ID);
					if (ivpColloqui != null){
						ColloquiView cv = (ColloquiView)ivpColloqui;
						cv.setImmobile(WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Immobili.class));			
					}
					
					IViewPart ivpAbbinamenti = PlatformUI.getWorkbench()
														 .getActiveWorkbenchWindow()
														 .getActivePage() 
														 .findView(AbbinamentiView.ID);
					if (ivpAbbinamenti != null){
						AbbinamentiView av = (AbbinamentiView)ivpAbbinamenti;
						av.setImmobile(null);

					}
					
					IViewPart ivpAffitti = PlatformUI.getWorkbench()
					 									  .getActiveWorkbenchWindow()
					 									  .getActivePage() 
					 									  .findView(ListaAffittiView.ID);
					if (ivpAffitti != null){
						ListaAffittiView lav = (ListaAffittiView)ivpAffitti;
						lav.setImmobile(WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Immobili.class));
					}

					IViewPart ivpEAV = PlatformUI.getWorkbench()
							  						 .getActiveWorkbenchWindow()
							  						 .getActivePage() 
							  						 .findView(EAVView.ID);
					if (ivpEAV != null){
						EAVView eav = (EAVView)ivpEAV;
						eav.setAttributes(null, 0);
					}

					IViewPart ivpAPV = PlatformUI.getWorkbench()
	  						 					 .getActiveWorkbenchWindow()
	  						 					 .getActivePage() 
	  						 					 .findView(AnagrafichePropietarieView.ID);
					if (ivpAPV != null){
						AnagrafichePropietarieView apv = (AnagrafichePropietarieView)ivpAPV;
						apv.setAnagrafica(null);
					}


				} catch (Exception e) {
				}
		}

	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
	
	}

	@Override
	public void partOpened(IWorkbenchPart part) {

	}

}
