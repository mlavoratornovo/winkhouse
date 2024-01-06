package winkhouse.view.anagrafica.handler;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.helper.ProfilerHelper;
import winkhouse.helper.ProfilerHelper.PermessoDetail;
import winkhouse.model.AnagraficheModel;
import winkhouse.orm.Anagrafiche;
import winkhouse.util.IWinkSysProperties;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.anagrafica.DettaglioAnagraficaView;
import winkhouse.vo.AnagraficheVO;


public class DettaglioAnagraficaHandler {
	
	private static DettaglioAnagraficaHandler instance = null;
	
	private DettaglioAnagraficaHandler(){
		super();		
	}
	
	public static DettaglioAnagraficaHandler getInstance(){
		if (instance == null){
			instance = new DettaglioAnagraficaHandler();			
		}
		return instance;
	}
	
	public DettaglioAnagraficaView getDettaglioAnagrafica(Anagrafiche viewInstance){
		
		
	
		DettaglioAnagraficaView dav = null;
		IViewReference vr = null;
				
		try {
			
			if (((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN) != null)
				    ? Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN))
					: false)){			
			
				if (ProfilerHelper.getInstance().getPermessoUI(DettaglioAnagraficaView.ID)){
					
					PermessoDetail pd = ProfilerHelper.getInstance().getPermessoAnagrafica(viewInstance.getCodAnagrafica(), false);
					if (pd != null){			
				
						vr = PlatformUI.getWorkbench()
							           .getActiveWorkbenchWindow()
									   .getActivePage()
									   .findViewReference(DettaglioAnagraficaView.ID,
											   			  String.valueOf(viewInstance.getCodAnagrafica()));
					
						if (vr != null){
							dav = (DettaglioAnagraficaView)PlatformUI.getWorkbench()
																	 .getActiveWorkbenchWindow()
																	 .getActivePage()															 
																	 .showView(vr.getId(),vr.getSecondaryId(),IWorkbenchPage.VIEW_VISIBLE);
						}else{
							dav = (DettaglioAnagraficaView)PlatformUI.getWorkbench()
																	 .getActiveWorkbenchWindow()
																	 .getActivePage()															 
																	 .showView(DettaglioAnagraficaView.ID,
																			   String.valueOf(viewInstance.getCodAnagrafica()),
																			   IWorkbenchPage.VIEW_CREATE);
						}
						if (!pd.getCanwrite()){
							dav.setCompareView(true);
						}else{
							dav.setCompareView(false);
						}
						dav.setAnagrafica(viewInstance);
						PlatformUI.getWorkbench()
				 		  		  .getActiveWorkbenchWindow()
				 		  		  .getActivePage()
				 		  		  .bringToTop(dav);				
						dav.setFocus();
						return dav;
						
					}else{
						MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
			  					  "Controllo permessi accesso dati",
			  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
			  					  " non ha il permesso di accedere ai dati dell'anagrafica" + 
			  					  viewInstance.toString());
						return null;
	
					}
				}else{
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							  					  "Controllo permessi accesso vista",
							  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
							  					  " non ha il permesso di accedere alla vista " + 
							  					  DettaglioAnagraficaView.ID);
					return null;
				}

			}else{
				
				vr = PlatformUI.getWorkbench()
				           .getActiveWorkbenchWindow()
						   .getActivePage()
						   .findViewReference(DettaglioAnagraficaView.ID,
								   			  String.valueOf(viewInstance.getCodAnagrafica()));
		
				if (vr != null){
					dav = (DettaglioAnagraficaView) PlatformUI.getWorkbench()
															  .getActiveWorkbenchWindow()
															  .getActivePage()
															  .showView(vr.getId(), vr.getSecondaryId(),IWorkbenchPage.VIEW_VISIBLE);
				}else{
					dav = (DettaglioAnagraficaView)PlatformUI.getWorkbench()
															 .getActiveWorkbenchWindow()
															 .getActivePage()															 
															 .showView(DettaglioAnagraficaView.ID,
																	   String.valueOf(viewInstance.getCodAnagrafica()),
																	   IWorkbenchPage.VIEW_CREATE);
				}
				dav.setAnagrafica(viewInstance);
				PlatformUI.getWorkbench()
		 		  		  .getActiveWorkbenchWindow()
		 		  		  .getActivePage()
		 		  		  .bringToTop(dav);				
				dav.setFocus();
				return dav;
				
				
			}
					
		} catch (PartInitException e) {
			return null;
		}
	}
		
}