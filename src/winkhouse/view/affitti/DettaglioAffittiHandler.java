package winkhouse.view.affitti;

import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.AffittiModel;
import winkhouse.vo.AffittiVO;


public class DettaglioAffittiHandler {

	private static DettaglioAffittiHandler instance = null;
	
	private DettaglioAffittiHandler(){
		super();		
	}
	
	public static DettaglioAffittiHandler getInstance(){
		if (instance == null){
			instance = new DettaglioAffittiHandler();			
		}
		return instance;
	}
	
	public DettaglioAffittiView getDettaglioAffitto(AffittiVO viewInstance){
		DettaglioAffittiView dav = null;
		IViewReference vr = null;
		try {
			
				vr = PlatformUI.getWorkbench()
					           .getActiveWorkbenchWindow()
							   .getActivePage()
							   .findViewReference(DettaglioAffittiView.ID,viewInstance.toString());
			
				if (vr != null){
					dav = (DettaglioAffittiView)PlatformUI.getWorkbench()
														  .getActiveWorkbenchWindow()
														  .getActivePage()															 
														  .showView(vr.getId(),vr.getSecondaryId(),IWorkbenchPage.VIEW_VISIBLE);
				}else{
					dav = (DettaglioAffittiView)PlatformUI.getWorkbench()
														  .getActiveWorkbenchWindow()
														  .getActivePage()															 
														  .showView(DettaglioAffittiView.ID,
															    	String.valueOf(viewInstance.getCodAffitti()),
																	IWorkbenchPage.VIEW_CREATE);
				}
				dav.setAffitto(new AffittiModel(viewInstance));
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
