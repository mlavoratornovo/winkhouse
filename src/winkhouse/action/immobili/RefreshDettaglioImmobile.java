package winkhouse.action.immobili;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.model.ImmobiliModel;
import winkhouse.view.immobili.DettaglioImmobileView;

public class RefreshDettaglioImmobile extends Action {

	public RefreshDettaglioImmobile() {
		setImageDescriptor(Activator.getImageDescriptor("icons/adept_reinstall.png"));
		setToolTipText("Ricarica dati dettaglio");
	}

	public RefreshDettaglioImmobile(String text) {
		super(text);
	}

	public RefreshDettaglioImmobile(String text, ImageDescriptor image) {
		super(text, image);
	}

	public RefreshDettaglioImmobile(String text, int style) {
		super(text, style);
	}

	@Override
	public void run() {
		
		IWorkbenchPart iwp = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				  									  .getActivePage()
				  									  .getActivePart();

		if (iwp instanceof DettaglioImmobileView){

			ImmobiliModel im = ((DettaglioImmobileView)iwp).getImmobile();

			if (im.getCodImmobile() != null){
				ImmobiliDAO idao = new ImmobiliDAO();
				Object o = idao.getImmobileById(ImmobiliModel.class.getName(), im.getCodImmobile());
				if (o != null){
					im = (ImmobiliModel)o;
					((DettaglioImmobileView)iwp).setImmobile(im);
				}else{
					if (MessageDialog.openQuestion(iwp.getSite().getShell(), 
							"Attenzione l'anagrafica � stata cancellata", 
							"########################################## \n" +
							"                       L'IMMOBILE E' STATO CANCELLATO ! \n" +
							"########################################## \n\n" +
							"Vuoi provare ad eseguire il salvataggio come una nuova anagrafica ? \n\n" +
							"In caso AFFERMATIVO potrai eseguire il SALVATAGGIO. \n\n" +
							"in caso CONTRARIO alla chiusura del dettaglio i dati andranno persi")){

						im.setCodImmobile(0);
						((DettaglioImmobileView)iwp).setImmobile(im);

					} 
				}		
			}
		}
				
	}

	
}
