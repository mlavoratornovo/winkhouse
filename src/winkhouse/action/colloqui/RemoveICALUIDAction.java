package winkhouse.action.colloqui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.action.WizardGCalendarSyncAction;
import winkhouse.action.agenda.CercaAppuntamentiAction;
import winkhouse.model.ColloquiModel;
import winkhouse.perspective.AgendaPerspective;
import winkhouse.view.agenda.CalendarioView;
import winkhouse.view.agenda.DettaglioAppuntamentoView;
import winkhouse.view.agenda.ListaAppuntamentiView;
import winkhouse.view.colloqui.DettaglioColloquioView;
import winkhouse.view.common.PopUpGCalendarEvents;

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
		
		if (wp instanceof DettaglioColloquioView){
			
			if (isChecked()){
				
				WizardGCalendarSyncAction wizard = new WizardGCalendarSyncAction(null, null, ((DettaglioColloquioView) wp).getColloquio());				
				wizard.run();
				((DettaglioColloquioView) wp).getColloquio().setWinkGCalendarModels(null);
				((DettaglioColloquioView) wp).setColloquio(((DettaglioColloquioView) wp).getColloquio());
				if (((DettaglioColloquioView) wp).getColloquio().getWinkGCalendarModels().size() == 0){
					setChecked(false);
					
				}
				((DettaglioColloquioView) wp).getColloquio().setWinkGCalendarModels(null);
				((DettaglioColloquioView) wp).setColloquio(((DettaglioColloquioView) wp).getColloquio());
			}else{
				PopUpGCalendarEvents popsc = new PopUpGCalendarEvents(((DettaglioColloquioView) wp).getColloquio());
				((DettaglioColloquioView) wp).getColloquio().setWinkGCalendarModels(null);
				if (((DettaglioColloquioView) wp).getColloquio().getWinkGCalendarModels().size() > 0){
					setChecked(true);
				}
				((DettaglioColloquioView) wp).getColloquio().setWinkGCalendarModels(null);
				((DettaglioColloquioView) wp).setColloquio(((DettaglioColloquioView) wp).getColloquio());
			}

		}


	}


}
