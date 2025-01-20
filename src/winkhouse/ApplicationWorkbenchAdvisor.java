package winkhouse;

import java.util.ArrayList;

import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import winkhouse.dao.AgentiDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.util.IWinkSysProperties;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AgentiVO;


/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	
//	private static final String PERSPECTIVE_ID = "winkhouse.immobili";
	
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
    	
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	@Override
	public void postStartup() {
		
		super.postStartup();
		
			
	}

	public String getInitialWindowPerspectiveId() {
		
		if (ConnectionManager.getInstance().getConnectionSelectConnection() != null){
	        AgentiDAO aDAO = new AgentiDAO();
	        ArrayList al = aDAO.checkSetPassword();
	        
	        if ((al.size() > 0) && 
	            (((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN) != null)
	               ? Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN))
	               : false) == true)){		
	        	return null;
	        }else{
	    			String persp_id = (WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.STARTPERSPECTIVE).equalsIgnoreCase(""))
	    							   ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.STARTPERSPECTIVE)
	    							   : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.STARTPERSPECTIVE);
	
		           return persp_id;
	        	
	        }
		}
		return null;
	    
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {		
		super.initialize(configurer);
		configurer.setSaveAndRestore(false);		
	}

	
}
