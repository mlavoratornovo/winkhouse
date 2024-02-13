package winkhouse.view.agenda.handler;

import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.AppuntamentiModel;
import winkhouse.orm.Appuntamenti;
import winkhouse.view.agenda.DettaglioAppuntamentoView;
import winkhouse.vo.AppuntamentiVO;


public class DettaglioAppuntamentoHandler {
	
	private static DettaglioAppuntamentoHandler instance = null;
	
	private DettaglioAppuntamentoHandler(){
		super();		
	}
	
	public static DettaglioAppuntamentoHandler getInstance(){
		if (instance == null){
			instance = new DettaglioAppuntamentoHandler();			
		}
		return instance;
	}
	
	public DettaglioAppuntamentoView getDettaglioAppuntamento(Appuntamenti viewInstance){
		DettaglioAppuntamentoView dav = null;
		IViewReference vr = null;
		try {
			
				vr = PlatformUI.getWorkbench()
					           .getActiveWorkbenchWindow()
							   .getActivePage()
							   .findViewReference(DettaglioAppuntamentoView.ID,viewInstance.toString());
			
				if (vr != null){
					dav = (DettaglioAppuntamentoView)PlatformUI.getWorkbench()
															 .getActiveWorkbenchWindow()
															 .getActivePage()															 
															 .showView(vr.getId(),vr.getSecondaryId(),IWorkbenchPage.VIEW_VISIBLE);
				}else{
					dav = (DettaglioAppuntamentoView)PlatformUI.getWorkbench()
															 .getActiveWorkbenchWindow()
															 .getActivePage()															 
															 .showView(DettaglioAppuntamentoView.ID,
																	   String.valueOf(viewInstance.getCodAppuntamento()),
																	   IWorkbenchPage.VIEW_CREATE);
				}
				dav.setAppuntamento(viewInstance);
				PlatformUI.getWorkbench()
		 		  		  .getActiveWorkbenchWindow()
		 		  		  .getActivePage()
		 		  		  .bringToTop(dav);				
				dav.setFocus();
				return dav;
		} catch (PartInitException e) {
			return null;
		}
	}
		
}
