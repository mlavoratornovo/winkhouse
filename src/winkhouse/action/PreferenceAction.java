package winkhouse.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.helper.ProfilerHelper;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.preference.DataBasePage;
import winkhouse.view.preference.DynamicFieldsSettingsPage;
import winkhouse.view.preference.GeneralSettingPage;
import winkhouse.view.preference.PluginSystemSettingsPage;
import winkhouse.view.preference.SecurityPage;
import winkhouse.view.preference.WinkCloudPage;


public class PreferenceAction extends Action {

	private static IWorkbenchWindow window = null;
	private final static String ID = "preferenceaction";
	
	public PreferenceAction(IWorkbenchWindow window, String text, ImageDescriptor image) {
		super(text, image);
		this.window = window;
		setId(ID);
		setText(text);
		setImageDescriptor(image);
	}

	@Override
	public void run() {
		if (ProfilerHelper.getInstance().getPermessoUI(WinkPreferenceDialog.ID)){
					
			PreferenceManager pm = new PreferenceManager();
			
			GeneralSettingPage gsp = new GeneralSettingPage();
			DataBasePage dbp = new DataBasePage();
			
			PluginSystemSettingsPage pssp = new PluginSystemSettingsPage();
			DynamicFieldsSettingsPage dfsp = new DynamicFieldsSettingsPage();
			
			WinkCloudPage wcp = new WinkCloudPage();
			wcp.setImageDescriptor(Activator.getImageDescriptor("icons/adept_updater.png"));
			wcp.setTitle("WinkCloud");
			
			SecurityPage sp = new SecurityPage();
			
			sp.setTitle("Profilazione");
			sp.setImageDescriptor(Activator.getImageDescriptor("icons/lock16.png"));
			
			gsp.setTitle("Impostazioni generali");
			gsp.setImageDescriptor(Activator.getImageDescriptor("icons/linuxconf.png"));
			
			dbp.setImageDescriptor(Activator.getImageDescriptor("icons/db.png"));
			dbp.setTitle("Database e files");
				
			pssp.setImageDescriptor(Activator.getImageDescriptor("icons/plugins-settings.png"));
			pssp.setTitle("Estensioni");
			
			dfsp.setImageDescriptor(Activator.getImageDescriptor("icons/campi_personali.png"));
			dfsp.setTitle("Campi personalizzati");
			
			PreferencePage root = new DataBasePage();
			root.setTitle("Impostazioni");			
			
			IPreferenceNode nodegeneral = new PreferenceNode("0",gsp);
			IPreferenceNode nodedb = new PreferenceNode("1",dbp);
			IPreferenceNode nodesp = new PreferenceNode("2",sp);
			IPreferenceNode nodepssp = new PreferenceNode("3",pssp);
			IPreferenceNode nodedfsp = new PreferenceNode("4",dfsp);
			IPreferenceNode nodewcp = new PreferenceNode("5",wcp);
			
			pm.addToRoot(nodegeneral);
			pm.addToRoot(nodedb);
			pm.addToRoot(nodesp);
			pm.addToRoot(nodepssp);
			pm.addToRoot(nodedfsp);
			pm.addToRoot(nodewcp);
			
//			if (WinkhouseUtils.getInstance().getPreferenceStore().getBoolean(WinkhouseUtils.BUNDLEDB)){
//				pm.addToRoot(nodesp);	
//			}
			
			
			WinkPreferenceDialog dialog = new WinkPreferenceDialog(window.getShell(), pm);
			
			dialog.open();
		}else{
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					  					  "Controllo permessi accesso vista",
					  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
					  					  " non ha il permesso di accedere alla vista " + 
					  					WinkPreferenceDialog.ID);
		}
			

	}

}
