package winkhouse.view.immobili.handler;

import java.util.HashMap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.helper.ProfilerHelper;
import winkhouse.helper.ProfilerHelper.PermessoDetail;
import winkhouse.model.ImmobiliModel;
import winkhouse.util.IWinkSysProperties;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.immobili.DettaglioImmobileView;
import winkhouse.vo.ImmobiliVO;



public class DettaglioImmobiliHandler {
	
	private static DettaglioImmobiliHandler instance = null;
	private HashMap dettagliImmobili = null;
	
	private DettaglioImmobiliHandler(){
		super();
		dettagliImmobili = new HashMap<Integer, DettaglioImmobileView>();
	}
	
	public static DettaglioImmobiliHandler getInstance(){
		if (instance == null){
			instance = new DettaglioImmobiliHandler();			
		}
		return instance;
	}
	
	public DettaglioImmobileView getDettaglioImmobile(ImmobiliVO viewInstance){
		DettaglioImmobileView div = null;
		IViewReference vr = null;
		
		try {
			if (((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN) != null)
			    ? Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN))
				: false)){
				if (ProfilerHelper.getInstance().getPermessoUI(DettaglioImmobileView.ID)){
					
					PermessoDetail pd = ProfilerHelper.getInstance().getPermessoImmobile(viewInstance.getCodImmobile(), false);
					if (pd != null){			
						vr = PlatformUI.getWorkbench()
				           			   .getActiveWorkbenchWindow()
				           			   .getActivePage()
				           			   .findViewReference(DettaglioImmobileView.ID,
				           					   			  String.valueOf(viewInstance.getCodImmobile().intValue()));
						
						if (vr != null){				
							div = (DettaglioImmobileView)PlatformUI.getWorkbench()
																   .getActiveWorkbenchWindow()
																   .getActivePage()															 
																   .showView(vr.getId(),vr.getSecondaryId(),IWorkbenchPage.VIEW_VISIBLE);
						}else{
							div = (DettaglioImmobileView)PlatformUI.getWorkbench()
														 		   .getActiveWorkbenchWindow()
														           .getActivePage()															 
														           .showView(DettaglioImmobileView.ID,
														        		   	 String.valueOf(viewInstance.getCodImmobile()),
														        		   	 IWorkbenchPage.VIEW_CREATE);
						}
						
						if (!pd.getCanwrite()){
							div.setCompareView(true);
						}
						
						div.setImmobile(new ImmobiliModel(viewInstance));
						
						Activator.getDefault().getWorkbench()
				   		 		 .getActiveWorkbenchWindow()
				   		 		 .getActivePage()
				   		 		 .bringToTop(div);
						
						div.setFocus();
						
						
						return div;
					}else{
						MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
			  					  					  "Controllo permessi accesso dati",
			  					  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
			  					  					  " non ha il permesso di accedere ai dati dell'immobile" + 
			  					  					  viewInstance.toString());
						return null;
					}
						
				}else{
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							  					  "Controllo permessi accesso vista",
							  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
							  					  " non ha il permesso di accedere alla vista " + 
							  					  DettaglioImmobileView.ID);
					return null;
				}
				
			}else{
				try{
					vr = PlatformUI.getWorkbench()
		           			   .getActiveWorkbenchWindow()
		           			   .getActivePage()
		           			   .findViewReference(DettaglioImmobileView.ID,
		           					   			  String.valueOf(viewInstance.getCodImmobile().intValue()));
				}catch(Exception e){
					vr = null;
				}
				if (vr != null){				
					div = (DettaglioImmobileView)PlatformUI.getWorkbench()
														   .getActiveWorkbenchWindow()
														   .getActivePage()															 
														   .showView(vr.getId(),vr.getSecondaryId(),IWorkbenchPage.VIEW_VISIBLE);
				}else{
					div = (DettaglioImmobileView)PlatformUI.getWorkbench()
												 		   .getActiveWorkbenchWindow()
												           .getActivePage()															 
												           .showView(DettaglioImmobileView.ID,
												        		   	 String.valueOf(viewInstance.getCodImmobile()),
												        		   	 IWorkbenchPage.VIEW_CREATE);
				}
				
				
				div.setImmobile(new ImmobiliModel(viewInstance));
				
				Activator.getDefault().getWorkbench()
		   		 		 .getActiveWorkbenchWindow()
		   		 		 .getActivePage()
		   		 		 .bringToTop(div);
				
				div.setFocus();
				
				
				return div;
					
			}
		} catch (PartInitException e) {
			return null;
		}
	}
	
}
