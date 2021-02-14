package winkhouse.view.permessi.handler;

import java.util.HashMap;

import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.model.AgentiModel;
import winkhouse.view.permessi.DettaglioPermessiAgenteView;
import winkhouse.vo.AgentiVO;

public class DettaglioPermessiAgentiHandler {

	private static DettaglioPermessiAgentiHandler instance = null;
	private HashMap dettagliPermessi = null;
	
	private DettaglioPermessiAgentiHandler(){
		super();
		dettagliPermessi = new HashMap<Integer, DettaglioPermessiAgenteView>();
	}
	
	public static DettaglioPermessiAgentiHandler getInstance(){
		if (instance == null){
			instance = new DettaglioPermessiAgentiHandler();			
		}
		return instance;
	}
	
	public DettaglioPermessiAgenteView getDettaglioPermessiAgenti(AgentiVO viewInstance){
		DettaglioPermessiAgenteView div = null;
		IViewReference vr = null;
		
		try {
			vr = PlatformUI.getWorkbench()
	           			   .getActiveWorkbenchWindow()
	           			   .getActivePage()
	           			   .findViewReference(DettaglioPermessiAgenteView.ID,
	           					   			  String.valueOf(viewInstance.getCodAgente().intValue()));
			
			if (vr != null){				
				div = (DettaglioPermessiAgenteView)PlatformUI.getWorkbench()
													   .getActiveWorkbenchWindow()
													   .getActivePage()															 
													   .showView(vr.getId(),vr.getSecondaryId(),IWorkbenchPage.VIEW_VISIBLE);
			}else{
				div = (DettaglioPermessiAgenteView)PlatformUI.getWorkbench()
											 		   .getActiveWorkbenchWindow()
											           .getActivePage()															 
											           .showView(DettaglioPermessiAgenteView.ID,
											        		   	 String.valueOf(viewInstance.getCodAgente()),
											        		   	 IWorkbenchPage.VIEW_CREATE);
			}
			div.setAgente(new AgentiModel(viewInstance));
			
			Activator.getDefault().getWorkbench()
	   		 		 .getActiveWorkbenchWindow()
	   		 		 .getActivePage()
	   		 		 .bringToTop(div);
			
			div.setFocus();
			
			
			return div;
			
		} catch (PartInitException e) {
			return null;
		}
	}

}
