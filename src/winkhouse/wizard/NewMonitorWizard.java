package winkhouse.wizard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import winkhouse.Activator;
import winkhouse.model.winkcloud.ConnectorTypes;
import winkhouse.model.winkcloud.FTPConnector;
import winkhouse.model.winkcloud.HTTPConnector;
import winkhouse.model.winkcloud.MonitorFTPModel;
import winkhouse.model.winkcloud.MonitorHTTPModel;
import winkhouse.model.winkcloud.WinkMonitorFTPModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.wizard.cloudmonitor.FTPMonitorSettings;
import winkhouse.wizard.cloudmonitor.SceltaTipoMonitor;
import winkhouse.wizard.cloudmonitor.WinkCloudMonitorSettings;

public class NewMonitorWizard extends Wizard {

	private FTPMonitorSettings ftpMonitor = null;
	private WinkCloudMonitorSettings wcMonitor = null;
	private SceltaTipoMonitor sceltaTipo = null;
	private MonitorFTPModel newMonitor = null;
	private WinkMonitorFTPModel newWinkMonitor = null;
	private MonitorHTTPModel monitorHTTP = null;	
	private ConnectorTypes tipoMonitor = ConnectorTypes.FTP;
	
	private Object returnObject = null;
	private Method returnMethod = null;
	
	public NewMonitorWizard() {
	
	}

	@Override
	public boolean performFinish() {
		if ((getReturnObject() != null) && (getReturnMethod() != null)){
			try {
				if (tipoMonitor == ConnectorTypes.FTP){
					if (getNewMonitor().isWink()){
						getReturnMethod().invoke(getReturnObject(), getNewWinkMonitor(getNewMonitor()));
					}else{
						getReturnMethod().invoke(getReturnObject(), getNewMonitor());
					}
				}
				if (tipoMonitor == ConnectorTypes.WINKCLOUD){
					getReturnMethod().invoke(getReturnObject(), getMonitorHTTP());
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}

	
	@Override
	public void addPages() {
		
		sceltaTipo = new SceltaTipoMonitor("SceltaTipo", 
										   "Scelta tipo Monitor", 
										   Activator.getImageDescriptor("icons/wizardmonitor/utilities-system-monitor.png"));
		addPage(sceltaTipo);
		
		ftpMonitor = new FTPMonitorSettings("FTPMonitor", 
											"FTP Monitor", 
											Activator.getImageDescriptor("icons/wizardmonitor/FTPConnector.png"));
		addPage(ftpMonitor);
		
		wcMonitor = new WinkCloudMonitorSettings("WinkCloudMonitor",
												 "WinkCloud Monitor", 
												 Activator.getImageDescriptor("icons/wizardmonitor/WinkCloudConnector.png"));
		addPage(wcMonitor);
	}

	
	@Override
	public boolean canFinish() {
		
		if (tipoMonitor == ConnectorTypes.FTP){
			
			if (getNewMonitor().getConnector() != null){
				
				if (getNewMonitor().getConnector().getPassword() != null &&
					getNewMonitor().getConnector().getUsername() != null &&
					getNewMonitor().getConnector().getUrl() != null &&
					((FTPConnector)getNewMonitor().getConnector()).getPorta() != 0){
					
					return getNewMonitor().testConnection();
				}else{
					return false;
				}
				
			}else{
				return false;
			}
			
		}
		if (tipoMonitor == ConnectorTypes.WINKCLOUD){
			if (getNewMonitor().getConnector() != null){
				
				if (getMonitorHTTP().getConnector().getUrl() != null &&
					getMonitorHTTP().getConnector().getCode() != null){
					return true;
				}
				return false;
			}

		}
		return false;
	}

	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		
		if (page instanceof SceltaTipoMonitor) {
			
			if (tipoMonitor == ConnectorTypes.FTP){
				
				return ftpMonitor;
			}
			if (tipoMonitor == ConnectorTypes.WINKCLOUD){
				if ((WinkhouseUtils.getInstance().getPreferenceStore().getString("winkcloudid") != null) && 
					(!WinkhouseUtils.getInstance().getPreferenceStore().getString("winkcloudid").trim().equalsIgnoreCase(""))){
					return wcMonitor;
				}else{
					MessageDialog.openError(getShell(), "WINKCLOUDID", "Il WinkCloudID non è valorizzato, File->Impostazioni->WinkCloud");
				}
				
			}
			
		}
		return null;
	}

	public MonitorFTPModel getNewMonitor() {
		
		if (newMonitor == null){
			newMonitor = new MonitorFTPModel();
		}
		
		return newMonitor;
	}

	public WinkMonitorFTPModel getNewWinkMonitor(MonitorFTPModel monitor) {
		
		if (newWinkMonitor == null){
			newWinkMonitor = new WinkMonitorFTPModel(monitor);
		}
		
		return newWinkMonitor;
	}

	public void setNewMonitor(MonitorFTPModel newMonitor) {
		this.newMonitor = newMonitor;
	}

	public ConnectorTypes getTipoMonitor() {
		return tipoMonitor;
	}

	public void setTipoMonitor(ConnectorTypes tipoMonitor) {
		this.tipoMonitor = tipoMonitor;
	}

	public Object getReturnObject() {
		return returnObject;
	}

	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}

	public Method getReturnMethod() {
		return returnMethod;
	}

	public void setReturnMethod(Method returnMethod) {
		this.returnMethod = returnMethod;
	}

	
	public MonitorHTTPModel getMonitorHTTP() {
		
		if (monitorHTTP == null){
			monitorHTTP = new MonitorHTTPModel(new HTTPConnector());			
		}
						
		return monitorHTTP;
	}

	
	public void setMonitorGDrive(MonitorHTTPModel monitorHTTP) {
		this.monitorHTTP = monitorHTTP;
	}


}
