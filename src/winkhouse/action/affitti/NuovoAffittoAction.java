package winkhouse.action.affitti;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.model.AffittiModel;
import winkhouse.view.affitti.DettaglioAffittiView;
import winkhouse.view.affitti.ListaAffittiView;



public class NuovoAffittoAction extends Action {


	public NuovoAffittoAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	@Override
	public void run() {
		try {
			ListaAffittiView lav = (ListaAffittiView)PlatformUI.getWorkbench()
					  										   .getActiveWorkbenchWindow()
					  										   .getActivePage()
					  										   .findView(ListaAffittiView.ID);
			
			if ((lav != null) && 
				((lav.getImmobile() != null) && (lav.getImmobile().getCodImmobile() != 0))
			   ){
				DettaglioAffittiView dav = (DettaglioAffittiView)PlatformUI.getWorkbench()
 				   														   .getActiveWorkbenchWindow()
 				   														   .getActivePage()
 				   														   .showView(DettaglioAffittiView.ID);			
				AffittiModel am = new AffittiModel();
				am.setCodImmobile(lav.getImmobile().getCodImmobile());
				dav.setAffitto(am);
				dav.setFocus();
				
			}else{
				MessageDialog.openWarning(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
						  				  "Errore nuovo affitto", 
						  				  "Selezionare un dettaglio immobile");

			}
			
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}


}
