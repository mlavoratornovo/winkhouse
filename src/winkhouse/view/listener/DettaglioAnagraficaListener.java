package winkhouse.view.listener;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import winkhouse.action.recapiti.ApriDettaglioRecapitiAction;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.anagrafica.DettaglioAnagraficaView;
import winkhouse.view.anagrafica.ImmobiliPropietaView;
import winkhouse.view.colloqui.DettaglioColloquioView;
import winkhouse.view.common.AbbinamentiView;
import winkhouse.view.common.ColloquiView;
import winkhouse.view.common.EAVView;
import winkhouse.view.common.RecapitiView;


public class DettaglioAnagraficaListener implements IPartListener {

	@Override
	public void partActivated(IWorkbenchPart part) {
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		if (part instanceof DettaglioAnagraficaView || part instanceof DettaglioColloquioView){
			
			try {
				WinkhouseUtils.getInstance().setLastCodAnagraficaSelected(null);
				IViewPart ivpRecapiti = PlatformUI.getWorkbench()
					  							  .getActiveWorkbenchWindow()
					  							  .getActivePage()														   
					  							  .findView(RecapitiView.ID);
				if (ivpRecapiti != null){
					ApriDettaglioRecapitiAction adra = new ApriDettaglioRecapitiAction(null,false);
					adra.run();

//					RecapitiView riv = (RecapitiView)ivpRecapiti;
//					riv.setAnagrafica(new AnagraficheModel());
				}


				IViewPart ivpColloqui = PlatformUI.getWorkbench()
				  								  .getActiveWorkbenchWindow()
				  								  .getActivePage() 
				  								  .findView(ColloquiView.ID);
				if (ivpColloqui != null){
					ColloquiView cv = (ColloquiView)ivpColloqui;
					cv.setImmobile(new ImmobiliModel());			
				}
				
				IViewPart ivpAbbinamenti = PlatformUI.getWorkbench()
				  								  .getActiveWorkbenchWindow()
				  								  .getActivePage() 
				  								  .findView(AbbinamentiView.ID);
				if (ivpAbbinamenti != null){
					AbbinamentiView av = (AbbinamentiView)ivpAbbinamenti;					
					av.setAnagrafica(null);
				}
				
				IViewPart ivpImmobiliPropieta = PlatformUI.getWorkbench()
				  										  .getActiveWorkbenchWindow()
				  										  .getActivePage() 
				  										  .findView(ImmobiliPropietaView.ID);
				if (ivpImmobiliPropieta != null){
					ImmobiliPropietaView av = (ImmobiliPropietaView)ivpImmobiliPropieta;
					av.setAnagrafica(new AnagraficheModel());
				}
				
				IViewPart ivpEAV = PlatformUI.getWorkbench()
 						 					 .getActiveWorkbenchWindow()
 						 					 .getActivePage() 
 						 					 .findView(EAVView.ID);
				if (ivpEAV != null){
					EAVView eav = (EAVView)ivpEAV;
					eav.setAttributes(null, 0);
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
