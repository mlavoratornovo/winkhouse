package winkhouse.action.anagrafiche;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.model.AnagraficheModel;
import winkhouse.view.anagrafica.DettaglioAnagraficaView;

public class RefreshDettaglioAnagrafica extends Action {

	public RefreshDettaglioAnagrafica() {
		setImageDescriptor(Activator.getImageDescriptor("icons/adept_reinstall.png"));
		setToolTipText("Ricarica dati dettaglio");
	}

	public RefreshDettaglioAnagrafica(String text) {
		super(text);
	}

	public RefreshDettaglioAnagrafica(String text, ImageDescriptor image) {
		super(text, image);
	}

	public RefreshDettaglioAnagrafica(String text, int style) {
		super(text, style);
	}

	@Override
	public void run() {
		
		IWorkbenchPart iwp = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								 					  .getActivePage()
								 					  .getActivePart();
		
		if (iwp instanceof DettaglioAnagraficaView){
			
			AnagraficheModel am = ((DettaglioAnagraficaView)iwp).getAnagrafica();
			
			if (am.getCodAnagrafica() != 0){
				AnagraficheDAO adao = new AnagraficheDAO();
				Object o = adao.getAnagraficheById(AnagraficheModel.class.getName(), am.getCodAnagrafica());
				if (o != null){
					am = (AnagraficheModel)o;
					((DettaglioAnagraficaView)iwp).setAnagrafica(am);
				}else{
					if (MessageDialog.openQuestion(iwp.getSite().getShell(), 
											   "Attenzione l'anagrafica � stata cancellata", 
											   "########################################## \n" +
											   "                       L'ANAGRAFICA E' STATA CANCELLATA ! \n" +
											   "########################################## \n\n" +
											   "Vuoi provare ad eseguire il salvataggio come una nuova anagrafica ? \n\n" +
											   "In caso AFFERMATIVO potrai eseguire il SALVATAGGIO. \n\n" +
											   "in caso CONTRARIO alla chiusura del dettaglio i dati andranno persi")){
						
						am.setCodAnagrafica(0);
						((DettaglioAnagraficaView)iwp).setAnagrafica(am);
						
					} 
				}		
			}
		}
	}
		
}
