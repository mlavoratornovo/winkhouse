package winkhouse.action.affitti;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import winkhouse.helper.AffittiHelper;
import winkhouse.model.AffittiModel;
import winkhouse.view.affitti.DettaglioAffittiView;
import winkhouse.view.affitti.ListaAffittiView;


public class CancellaAffittiAction extends Action {

	public static int DELETE_SINGLE = 0;
	public static int DELETE_MULTIPLE = 1;
	private int type = 0;
	
	public CancellaAffittiAction(String text, ImageDescriptor image, int type) {
		super(text, image);
		this.type = type;
	}

	@Override
	public void run() {
		
		AffittiHelper ah = new AffittiHelper();
		ListaAffittiView lav = (ListaAffittiView)PlatformUI.getWorkbench()
		   												   .getActiveWorkbenchWindow()
		   												   .getActivePage()
		   												   .findView(ListaAffittiView.ID);

		if (type == DELETE_MULTIPLE){
			if (lav != null){
				Iterator it = ((StructuredSelection)lav.getTvAffitti().getSelection()).iterator();
				while(it.hasNext()){
					AffittiModel af = (AffittiModel)it.next();
					HashMap result = ah.deleteAffitto(af, null);
					if (!(Boolean)result.get(AffittiHelper.RESULT_AFFITTI_DB)){
						MessageDialog.openError(PlatformUI.getWorkbench()
								  						  .getActiveWorkbenchWindow()
								  						  .getShell(), 
								  				"Errore cancellazione affitto", 
												"L'affitto non è stato cancellato a causa di un errore durante l'operazione");

					}else{
						IViewReference ivr = PlatformUI.getWorkbench()
						  		  					   .getActiveWorkbenchWindow()
						  		  					   .getActivePage()
						  		  					   .findViewReference(DettaglioAffittiView.ID, String.valueOf(af.getCodAffitti()));
						if (ivr != null){
							PlatformUI.getWorkbench()
							  		  .getActiveWorkbenchWindow()
							  		  .getActivePage()
							  		  .hideView(ivr);
						}

					}
					
				}
				
				lav.setImmobile(lav.getImmobile());
				lav.getTvAffitti().refresh();

			}
		}else{
			DettaglioAffittiView dav = (DettaglioAffittiView)PlatformUI.getWorkbench()
																	   .getActiveWorkbenchWindow()
																	   .getActivePage()
																	   .getActivePart();
			HashMap hm = ah.deleteAffitto(dav.getAffitto(),null);
			
			if ((Boolean)hm.get(AffittiHelper.RESULT_AFFITTI_DB)){
				
				PlatformUI.getWorkbench()
						  .getActiveWorkbenchWindow()
						  .getActivePage()
						  .hideView(dav);
				
				lav.setImmobile(lav.getImmobile());
				lav.getTvAffitti().refresh();

			}else{
				MessageDialog.openError(PlatformUI.getWorkbench()
												  .getActiveWorkbenchWindow()
												  .getShell(), 
										"Errore cancellazione affitto", 
										"L'affitto non è stato cancellato a causa di un errore durante l'operazione");
			}
			
		}
		
	}

	
}
