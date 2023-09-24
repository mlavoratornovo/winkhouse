package winkhouse.action;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.AgentiAppuntamentiModel;
import winkhouse.model.AgentiModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.ColloquiAgentiModel_Age;
import winkhouse.model.ColloquiModel;
import winkhouse.util.IWinkSysProperties;
import winkhouse.util.WinkhouseUtils;
import winkhouse.wizard.GCalendarSyncWizard;

public class WizardGCalendarSyncAction extends Action {

	public static String ID = "winkhouse.wizardgcalendarsync";
	private AppuntamentiModel appuntamento = null;
	private ColloquiModel colloquio = null;
	
	public WizardGCalendarSyncAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	public WizardGCalendarSyncAction(String text, ImageDescriptor image, AppuntamentiModel appuntamento) {
		super(text, image);
		setId(ID);
		this.appuntamento = appuntamento;
	}

	public WizardGCalendarSyncAction(String text, ImageDescriptor image, ColloquiModel colloquio) {
		super(text, image);
		setId(ID);
		this.colloquio = colloquio;
	}

	@Override
	public void run() {
		
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		GCalendarSyncWizard wizard = new GCalendarSyncWizard();
		
		if (this.appuntamento != null){
			
			wizard.getGcalsyncVO().setUpl_from_detail(true);
			wizard.getGcalsyncVO().setFrom_appuntamento_detail(appuntamento);
			
			Iterator<AgentiAppuntamentiModel> it = null;
			
			if (((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN) != null)
				    ? Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN))
					: false)){
				
				ArrayList<AgentiAppuntamentiModel> al = new ArrayList<AgentiAppuntamentiModel>();
				AgentiAppuntamentiModel aam = new AgentiAppuntamentiModel();
				aam.setAgente(WinkhouseUtils.getInstance().getLoggedAgent());
				al.add(aam);
				
				it = al.iterator();				
				
			}else{
				it = this.appuntamento.getAgenti().iterator();
			}
			
			ArrayList<AgentiModel> agenti = new ArrayList<AgentiModel>();
			
			while (it.hasNext()) {
				AgentiAppuntamentiModel agentiAppuntamentiModel = (AgentiAppuntamentiModel) it.next();
				AgentiModel am = agentiAppuntamentiModel.getAgente();
				if (am != null){
					agenti.add(am);	
				}
			}
			
			wizard.getGcalsyncVO().setAlagenti(agenti);
		}else if (this.colloquio != null){
			
			wizard.getGcalsyncVO().setUpl_from_detail(true);
			wizard.getGcalsyncVO().setFrom_colloqui_detail(colloquio);
			
			Iterator<ColloquiAgentiModel_Age> it = null;			
			
			if (((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN) != null)
				    ? Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN))
					: false)){
				
				ArrayList<ColloquiAgentiModel_Age> al = new ArrayList<ColloquiAgentiModel_Age>();
				ColloquiAgentiModel_Age cam = new ColloquiAgentiModel_Age();
				cam.setAgente(WinkhouseUtils.getInstance().getLoggedAgent());
				al.add(cam);
				
				it = al.iterator();
				
			}else{
				it = this.colloquio.getAgenti().iterator();
			}			
			
			ArrayList<AgentiModel> agenti = new ArrayList<AgentiModel>();
			
			while (it.hasNext()) {
				ColloquiAgentiModel_Age colloquiAgentiModel = (ColloquiAgentiModel_Age) it.next();
				agenti.add(colloquiAgentiModel.getAgente());
			}
			
			wizard.getGcalsyncVO().setAlagenti(agenti);
			
		}
		
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);	
		dialog.setPageSize(400, 300);
		dialog.open();
		
	}

}
