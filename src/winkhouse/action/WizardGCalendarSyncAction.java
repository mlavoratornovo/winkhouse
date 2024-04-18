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
import winkhouse.orm.Agenti;
import winkhouse.orm.Agentiappuntamenti;
import winkhouse.orm.Appuntamenti;
import winkhouse.orm.Colloqui;
import winkhouse.util.IWinkSysProperties;
import winkhouse.util.WinkhouseUtils;
import winkhouse.wizard.GCalendarSyncWizard;

public class WizardGCalendarSyncAction extends Action {

	public static String ID = "winkhouse.wizardgcalendarsync";
	private Appuntamenti appuntamento = null;
	private Colloqui colloquio = null;
	
	public WizardGCalendarSyncAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	public WizardGCalendarSyncAction(String text, ImageDescriptor image, Appuntamenti appuntamento) {
		super(text, image);
		setId(ID);
		this.appuntamento = appuntamento;
	}

	public WizardGCalendarSyncAction(String text, ImageDescriptor image, Colloqui colloquio) {
		super(text, image);
		setId(ID);
		this.colloquio = colloquio;
	}

	@Override
	public void run() {
		
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		GCalendarSyncWizard wizard = new GCalendarSyncWizard();
		// TODO convertire orm
		if (this.appuntamento != null){
			
			wizard.getGcalsyncVO().setUpl_from_detail(true);
			wizard.getGcalsyncVO().setFrom_appuntamento_detail(appuntamento);
			
			Iterator<Agentiappuntamenti> it = null;
			
			if (((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN) != null)
				    ? Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN))
					: false)){
				
				ArrayList<Agentiappuntamenti> al = new ArrayList<Agentiappuntamenti>();
				Agentiappuntamenti aam = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Agentiappuntamenti.class);
				//aam.setAgente(WinkhouseUtils.getInstance().getLoggedAgent());
				al.add(aam);
				
				it = al.iterator();				
				
			}else{
				it = this.appuntamento.getAgentiappuntamentis().iterator();
			}
			
			ArrayList<Agenti> agenti = new ArrayList<Agenti>();
			
			while (it.hasNext()) {
				Agentiappuntamenti agentiAppuntamentiModel = (Agentiappuntamenti) it.next();
				Agenti am = agentiAppuntamentiModel.getAgenti1();
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
				//cam.setAgente(WinkhouseUtils.getInstance().getLoggedAgent());
				al.add(cam);
				
				it = al.iterator();
				
			}else{
				//it = this.colloquio.getAllegaticolloquios().iterator();
			}			
			
			ArrayList<AgentiModel> agenti = new ArrayList<AgentiModel>();
			
			while (it.hasNext()) {
				ColloquiAgentiModel_Age colloquiAgentiModel = (ColloquiAgentiModel_Age) it.next();
				agenti.add(colloquiAgentiModel.getAgente());
			}
			
			//wizard.getGcalsyncVO().setAlagenti(agenti);
			
		}
		
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);	
		dialog.setPageSize(400, 300);
		dialog.open();
		
	}

}
