package winkhouse.action.recapiti;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.helper.ContattiHelper;
import winkhouse.model.AnagraficheModel;
import winkhouse.view.common.RecapitiView;

public class ConfermaRecapitiAction extends Action {

	public ConfermaRecapitiAction() {
		// TODO Auto-generated constructor stub
	}

	public ConfermaRecapitiAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public ConfermaRecapitiAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public ConfermaRecapitiAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void run() {
		
		RecapitiView rv = null;
		
		rv = (RecapitiView)PlatformUI.getWorkbench()
									 .getActiveWorkbenchWindow()
				  					 .getActivePage()
				  					 .getActivePart();
		
		if (rv.getAnagrafiche() != null & rv.getAnagrafiche().size() > 0){
//			for (AnagraficheModel anagrafica : rv.getAnagrafiche()) {
//				
//				if ((anagrafica.getCodAnagrafica() != null) && 
//					(anagrafica.getCodAnagrafica() != 0)){
//					ContattiHelper ch = new ContattiHelper();
//					ch.updateListaContatti(anagrafica,null,true);					
//				}else{
//					MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
//											  "Salvataggio recapiti",
//											  "Eseguire il salvataggio dell'anagrafica");
//				}
//				
//			}
		}else{
			MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					  				  "Salvataggio recapiti",
					  				  "Nessuna anagrafica a cui associare i recapiti");
			
		}
		
		rv.getTvRecapiti().setInput(new Object());
		rv.getTvRecapiti().refresh();
		
	}

	
}
