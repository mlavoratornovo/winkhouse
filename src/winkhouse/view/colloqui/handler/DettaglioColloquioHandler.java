package winkhouse.view.colloqui.handler;

import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.ColloquiModel;
import winkhouse.view.colloqui.DettaglioColloquioView;


public class DettaglioColloquioHandler {

	private static DettaglioColloquioHandler instance = null;
	private boolean comparerView = false;
	
	private DettaglioColloquioHandler() {
		super();
	}
	
	public static DettaglioColloquioHandler getInstance(){
		if (instance == null){
			instance = new DettaglioColloquioHandler();			
		}
		return instance;
	}
	
	public DettaglioColloquioView getDettaglioColloquio(ColloquiModel viewInstance){
		
		DettaglioColloquioView dcv = null;
		IViewReference vr = null;
		try {
			
				vr = PlatformUI.getWorkbench()
					           .getActiveWorkbenchWindow()
							   .getActivePage()
							   .findViewReference(DettaglioColloquioView.ID,
									   			  String.valueOf(viewInstance.getCodColloquio()));
			
				if (vr != null){
					dcv = (DettaglioColloquioView)PlatformUI.getWorkbench()
															 .getActiveWorkbenchWindow()
															 .getActivePage()															 
															 .showView(vr.getId(),vr.getSecondaryId(),IWorkbenchPage.VIEW_VISIBLE);
				}else{
					dcv = (DettaglioColloquioView)PlatformUI.getWorkbench()
															 .getActiveWorkbenchWindow()
															 .getActivePage()															 
															 .showView(DettaglioColloquioView.ID,
																	   String.valueOf(viewInstance.getCodColloquio()),
																	   IWorkbenchPage.VIEW_CREATE);
				}
				dcv.setColloquio(viewInstance);
				dcv.setCompareView(isComparerView());
				PlatformUI.getWorkbench()
		 		  		  .getActiveWorkbenchWindow()
		 		  		  .getActivePage()
		 		  		  .bringToTop(dcv);				
				dcv.setFocus();
				return dcv;
		} catch (PartInitException e) {
			return null;
		}
	}

	public boolean isComparerView() {
		return comparerView;
	}

	public void setComparerView(boolean comparerView) {
		this.comparerView = comparerView;
	}


}
