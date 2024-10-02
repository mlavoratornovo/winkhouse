package winkhouse.action.agenda;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.action.WizardGCalendarSyncAction;
import winkhouse.helper.GoogleCalendarV3Helper;
//import winkhouse.model.AppuntamentiModel;
import winkhouse.view.agenda.CalendarioView;
import winkhouse.view.agenda.DettaglioAppuntamentoView;
import winkhouse.view.agenda.ListaAppuntamentiView;
import winkhouse.view.common.PopUpGCalendarEvents;
import winkhouse.view.common.PopUpSceltaComune;

public class RemoveICALUIDAction extends Action {
	
	private String returnView = null;
	
	public RemoveICALUIDAction(String returnView) {
		this.returnView = returnView;
	}


	public RemoveICALUIDAction(String text, int style) {
		super(text, style);
	}

	@Override
	public void run() {
		IWorkbenchPart wp = PlatformUI.getWorkbench()
				  					  .getActiveWorkbenchWindow()
				  					  .getActivePage()
				  					  .getActivePart();
		
		if (wp instanceof DettaglioAppuntamentoView){
			
		
			if ((((DettaglioAppuntamentoView) wp).getAppuntamento() != null) && 
				(((DettaglioAppuntamentoView) wp).getAppuntamento().getCodAppuntamento() != 0)){
				
				if (isChecked()){
					
					WizardGCalendarSyncAction wizard = new WizardGCalendarSyncAction(null, null, ((DettaglioAppuntamentoView) wp).getAppuntamento());				
					wizard.run();
					//((DettaglioAppuntamentoView) wp).getAppuntamento().setWinkGCalendarModels(null);
					((DettaglioAppuntamentoView) wp).setAppuntamento(((DettaglioAppuntamentoView) wp).getAppuntamento());
					if (((DettaglioAppuntamentoView) wp).getAppuntamento().getWinkgcalendars().size() == 0){
						setChecked(false);
						
					}
					//((DettaglioAppuntamentoView) wp).getAppuntamento().setWinkGCalendarModels(null);
					((DettaglioAppuntamentoView) wp).setAppuntamento(((DettaglioAppuntamentoView) wp).getAppuntamento());
				}else{
					PopUpGCalendarEvents popsc = new PopUpGCalendarEvents(((DettaglioAppuntamentoView) wp).getAppuntamento());
					//((DettaglioAppuntamentoView) wp).getAppuntamento().setWinkGCalendarModels(null);
					if (((DettaglioAppuntamentoView) wp).getAppuntamento().getWinkgcalendars().size() > 0){
						setChecked(true);
					}
					//((DettaglioAppuntamentoView) wp).getAppuntamento().setWinkGCalendarModels(null);
					((DettaglioAppuntamentoView) wp).setAppuntamento(((DettaglioAppuntamentoView) wp).getAppuntamento());
				}
				
			}else{
				MessageDialog.openWarning(wp.getSite().getShell(), "SALVATAGGIO DATI", "Eseguire il salvataggio dell'appuntamento \nprima di avviare la sincronizzazione con Google Calendar");
			}

		}


	}



}
