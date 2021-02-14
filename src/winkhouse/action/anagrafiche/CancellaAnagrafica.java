package winkhouse.action.anagrafiche;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.helper.AnagraficheHelper;
import winkhouse.model.AnagraficheModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.anagrafica.DettaglioAnagraficaView;



public class CancellaAnagrafica extends Action {

	public CancellaAnagrafica() {}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getImageDescriptor("icons/edittrash.png");
	}

	@Override
	public String getText() {
		return "Cancella anagrafica";
	}

	@Override
	public void run() {
		

			DettaglioAnagraficaView dav = (DettaglioAnagraficaView)PlatformUI.getWorkbench()
												 							 .getActiveWorkbenchWindow()
												 							 .getActivePage()
												 							 .getActivePart();
			
			if (MessageDialog.openConfirm(PlatformUI.getWorkbench()
													.getActiveWorkbenchWindow()
													.getShell(), 
										  "Cancellazione anagrafica", 
										  "La cancellazione elimina in modo permanente tutti i dati relativi all'anagrafica. \n" +										  
 	  				 					  "Per procedere con la cancellazione permanente premere OK altrimenti premere Cancel ")){
			
				AnagraficheModel anagrafica = dav.getAnagrafica();
	
				AnagraficheHelper ah = new AnagraficheHelper();
				if ((Boolean)ah.deleteAnagrafica(anagrafica)){
					PlatformUI.getWorkbench()
							  .getActiveWorkbenchWindow()
							  .getActivePage()
							  .hideView(dav);
					
					RefreshAnagraficheAction rfa = new RefreshAnagraficheAction();
					rfa.run();
					WinkhouseUtils.getInstance().setCodiciAnagrafiche(null);
				}
				
			}
		
	}

}
