package winkhouse;

import java.util.ArrayList;
import java.util.Locale;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.internal.WorkbenchWindow;

import winkhouse.dao.AgentiDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.dialogs.custom.LoggedUser;
import winkhouse.dialogs.custom.LoginDialog;
import winkhouse.util.IWinkSysProperties;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AgentiVO;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	

	public Object start(IApplicationContext context) {
		
		Locale.setDefault(Locale.ITALIAN);
		Display display = PlatformUI.createDisplay();
		
		if (ConnectionManager.getInstance().getConnectionSelectConnection() != null){
			
			AgentiDAO aDAO = new AgentiDAO();
			ArrayList al = aDAO.checkSetPassword(AgentiVO.class.getName());
        
	        if ((al.size() > 0) && 
	        	(((WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN) != null)
	        	   ? Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN))
	        	   : false) == true)
	           ){
	        	
	    		if (autentica(display)){
	    			try {
	    				int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
	    				
	    				if (returnCode == PlatformUI.RETURN_RESTART) {
	    					return IApplication.EXIT_RESTART;
	    				}
	    					    				
	    				return IApplication.EXIT_OK;
	    			} finally {
	    				display.dispose();
	    			}
	    			
	    		}else{
	    			
	    			return IApplication.EXIT_OK;
	    		}
	        	
	        }else{
    			try {
    				int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
    				
    				if (returnCode == PlatformUI.RETURN_RESTART) {
    					return IApplication.EXIT_RESTART;
    				}
    				
    				return IApplication.EXIT_OK;
    			} finally {
    				display.dispose();
    			}
	        	
	        }
        	
        }else{
			try {
				int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
				
				if (returnCode == PlatformUI.RETURN_RESTART) {
					return IApplication.EXIT_RESTART;
				}
				
				return IApplication.EXIT_OK;
			} finally {
				display.dispose();
			}

        }
	
		
		
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return;
		
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
	
	private boolean autentica(Display d){
		LoginDialog ld = new LoginDialog(d);
		ld.createDialog();
		return true;
	}
}
