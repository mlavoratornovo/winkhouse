package winkhouse.action.winkcloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import winkhouse.helper.WinkCloudHelper;
import winkhouse.model.winkcloud.MonitorFTPModel;
import winkhouse.model.winkcloud.MonitorHTTPModel;
import winkhouse.model.winkcloud.MonitorModel;
import winkhouse.model.winkcloud.WinkMonitorFTPModel;
import winkhouse.view.winkcloud.MonitorsTreeView;

public class ApriCloudMonitorAction extends Action {

	private final static String FTP = "FTP";
	private final static String WINKFTP = "WINKFTP";
	private final static String HTTP = "HTTP";
	
	public ApriCloudMonitorAction() {
	}

	public ApriCloudMonitorAction(String text) {
		super(text);
	}

	public ApriCloudMonitorAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public ApriCloudMonitorAction(String text, int style) {
		super(text, style);
	}
	
	@Override
	public void run() {
		
		Object o = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		
		if (o instanceof MonitorsTreeView){
				
			FileDialog fd = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
			fd.setFilterExtensions(new String[]{"*.xml"});
			String pathFile = fd.open();
			
			WinkCloudHelper wch = new WinkCloudHelper();
			String importType = checkImportType(pathFile);
			
			if ((importType != null) && (!importType.equalsIgnoreCase(""))){ 
				if (importType.equalsIgnoreCase(WINKFTP)){
					
						ArrayList al_monitors = wch.loadFromFile(new File(pathFile), WinkMonitorFTPModel.class.getName(), WinkMonitorFTPModel.class);
						if (al_monitors.size() > 0){
							
							boolean findit = false;
							for (Object object : ((MonitorsTreeView)o).getMonitors()) {
								if (object instanceof WinkMonitorFTPModel){
									if (((WinkMonitorFTPModel)object).getConnector()
																 .getUrl().equalsIgnoreCase(((WinkMonitorFTPModel)al_monitors.get(0)).getConnector().getUrl())){
										findit = true;
										break;
									}
								}
							}
							if (!findit){
								WinkMonitorFTPModel mm = (WinkMonitorFTPModel)al_monitors.get(0);
								((MonitorsTreeView)o).addMonitor(mm);
							}else{
								MessageDialog.openWarning(((MonitorsTreeView)o).getSite().getShell(), "ATTENZIONE", "Monitor già presente");
							}
							
						}					
				}
				if (importType.equalsIgnoreCase(FTP)){
					
					ArrayList al_monitors = wch.loadFromFile(new File(pathFile), MonitorFTPModel.XMLTAG, MonitorFTPModel.class);
					if (al_monitors.size() > 0){
						
						boolean findit = false;
						for (Object object : ((MonitorsTreeView)o).getMonitors()) {
							if (object instanceof MonitorFTPModel){		
								if (((MonitorFTPModel)object).getConnector()
															 .getUrl().equalsIgnoreCase(((MonitorFTPModel)al_monitors.get(0)).getConnector().getUrl())){
									findit = true;
									break;
								}
							}
						}
						if (!findit){
							MonitorModel mm = (MonitorModel)al_monitors.get(0);
							((MonitorsTreeView)o).addMonitor(mm);
						}else{
							MessageDialog.openWarning(((MonitorsTreeView)o).getSite().getShell(), "ATTENZIONE", "Monitor già presente");
						}
					}
				}
				if (importType.equalsIgnoreCase(HTTP)){
					
					ArrayList al_monitors = wch.loadFromFile(new File(pathFile), MonitorHTTPModel.class.getName(), MonitorHTTPModel.class);
					
					if (al_monitors.size() > 0){
											
						MonitorHTTPModel mm = (MonitorHTTPModel)al_monitors.get(0);
						boolean findit = false;
						for (Object object : ((MonitorsTreeView)o).getMonitors()) {
							if (object instanceof MonitorHTTPModel){
								if (((MonitorHTTPModel)object).getConnector()
															  .getCode().equalsIgnoreCase(((MonitorHTTPModel)al_monitors.get(0)).getConnector().getCode())){
									findit = true;
									break;
								}
							}
						}
						if (!findit){
							mm.setPathloadingFile(pathFile);
							((MonitorsTreeView)o).addMonitor(mm);
						}else{
							MessageDialog.openWarning(((MonitorsTreeView)o).getSite().getShell(), "ATTENZIONE", "Monitor già presente");
						}
					}
				}
			}
		}
	}
	
	protected String checkImportType(String file){
		
		FileReader f = null;
		
		try{
			
		    f=new FileReader(file);
	
		    String s = null;
		    BufferedReader b = new BufferedReader(f);

		    StringBuffer sb = new StringBuffer();
	
		    while(true) {
		    	s = b.readLine();
		    	if(s==null){
		    		break;
		    	}
		    	sb.append(s);
		    }
		    
		    if (sb.toString().contains("WinkMonitorFTPModel")){
		    	return WINKFTP;
		    }
		    if (sb.toString().contains("MonitorFTPModel")){
		    	return FTP;
		    }
		    if (sb.toString().contains("MonitorHTTPModel")){
		    	return HTTP;
		    }
	    
		}catch(Exception e){
			return null;
		}
		return "";
	} 
	
}
