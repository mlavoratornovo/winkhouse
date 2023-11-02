package winkhouse;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.WorkbenchWindow;

import winkhouse.dao.CheckDBDAO;
import winkhouse.db.server.HSQLDBHelper;
import winkhouse.dialogs.custom.LoggedUser;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.listener.DBMonitorListener;
import winkhouse.view.listener.DettaglioAffittoListener;
import winkhouse.view.listener.DettaglioAnagraficaListener;
import winkhouse.view.listener.DettaglioColloquioListener;
import winkhouse.view.listener.DettaglioImmobiliListener;


public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	
	private DettaglioImmobiliListener dil = null;
	private DettaglioAnagraficaListener dal = null;
	private DettaglioColloquioListener dcl = null;
	private DettaglioAffittoListener dafl = null;
	private ApplicationPerspectiveAdapter apa = null;
	private DBMonitorListener dbml = null;
	
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {    	
    	
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(600, 400));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(false);
        configurer.setShowPerspectiveBar(true);
        configurer.setTitle("winkhouse");
        PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
		IPerspectiveRegistry perspectiveRegistry = PlatformUI.getWorkbench().getPerspectiveRegistry();
		IPerspectiveDescriptor[] perspectives = perspectiveRegistry.getPerspectives();
		
		for (IPerspectiveDescriptor iPerspectiveDescriptor : perspectives) {
			
			if (
					iPerspectiveDescriptor.getId().equals("org.eclipse.debug.ui.DebugPerspective") ||
					iPerspectiveDescriptor.getId().equals("org.eclipse.jdt.ui.JavaBrowsingPerspective") ||
					iPerspectiveDescriptor.getId().equals("org.eclipse.wst.xml.ui.perspective") ||
					iPerspectiveDescriptor.getId().equals("org.eclipse.jdt.ui.JavaHierarchyPerspective") ||
					iPerspectiveDescriptor.getId().equals("org.eclipse.team.ui.TeamSynchronizingPerspective") ||
					iPerspectiveDescriptor.getId().equals("org.eclipse.mylyn.tasks.ui.perspectives.planning") ||
					iPerspectiveDescriptor.getId().equals("org.eclipse.team.cvs.ui.cvsPerspective") ||
					iPerspectiveDescriptor.getId().equals("org.eclipse.pde.ui.PDEPerspective") ||
					iPerspectiveDescriptor.getId().equals("org.eclipse.jdt.ui.JavaPerspective") ||
					iPerspectiveDescriptor.getId().equals("org.eclipse.ui.resourcePerspective") ||
					iPerspectiveDescriptor.getId().equals("org.eclipse.egit.ui.GitRepositoryExploring") 
			) {
		       perspectiveRegistry.deletePerspective(iPerspectiveDescriptor);
		    }
		}	
        
        
        configurer.getWindow().addPerspectiveListener(new ApplicationPerspectiveAdapter());
        checkDB();
    }

	@Override
	public void postWindowCreate() {
		
		super.postWindowCreate();
		
		dil = new DettaglioImmobiliListener();
        Activator.getDefault().getWorkbench()
		 		 .getActiveWorkbenchWindow()
		 		 .getPartService()
		 		 .addPartListener(dil);
        
		dal = new DettaglioAnagraficaListener();
        Activator.getDefault().getWorkbench()
		 		 .getActiveWorkbenchWindow()
		 		 .getPartService()
		 		 .addPartListener(dal);
        
		dcl = new DettaglioColloquioListener();
        Activator.getDefault().getWorkbench()
		 		 .getActiveWorkbenchWindow()
		 		 .getPartService()
		 		 .addPartListener(dcl);

        dafl = new DettaglioAffittoListener();
        Activator.getDefault().getWorkbench()
		 		 .getActiveWorkbenchWindow()
		 		 .getPartService()
		 		 .addPartListener(dafl);

        dbml = new DBMonitorListener();
        Activator .getDefault().getWorkbench()
        		  .getActiveWorkbenchWindow()
        		  .getPartService()        		  
        		  .addPartListener(dbml);
        
        apa = new ApplicationPerspectiveAdapter();
        Activator .getDefault().getWorkbench()
        		  .getActiveWorkbenchWindow()
        		  .addPerspectiveListener(apa);

		getWindowConfigurer().getWindow().getShell().setMaximized(true);

		if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

			if(window instanceof IWorkbenchWindow) {
				
				ICoolBarManager coolBarManager = null;
				
				if(((WorkbenchWindow) window).getCoolBarVisible()) {
					coolBarManager = ((WorkbenchWindow)window).getCoolBarManager2();
					ToolBarContributionItem tm = (ToolBarContributionItem)coolBarManager.find("main");
					LoggedUser lu = (LoggedUser)tm.getToolBarManager().find(LoggedUser.ID);
					
					lu.setAgenteVO(WinkhouseUtils.getInstance().getLoggedAgent());							
					
				}
				String persp_id = (WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.STARTPERSPECTIVE).equalsIgnoreCase(""))
						   		   ? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.STARTPERSPECTIVE)
						   		   : WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.STARTPERSPECTIVE);

						  
					try {
					PlatformUI.getWorkbench().showPerspective(persp_id, PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				} catch (WorkbenchException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
//		checkDB();
	}
	
	private void checkDB(){
		CheckDBDAO chkDBDAO = new CheckDBDAO();
		chkDBDAO.checkWinkGCalendar();
		chkDBDAO.checkPromemoriaLinks();
		chkDBDAO.setTransactionControl();
		chkDBDAO.checkImmobiliNCivico();
		chkDBDAO.checkAnagraficheNCivico();
		chkDBDAO.checkAttributeEnum();
//		chkDBDAO.checkWinkGCalendar();
//		chkDBDAO.checkImmobiliPropietari();
//		chkDBDAO.checkComuni();
//		chkDBDAO.checkPromemoriaOggetti();
	}

	
	@Override
	public boolean preWindowShellClose() {
		
		PlatformUI.getWorkbench()
							 .getActiveWorkbenchWindow()
							 .getPartService()
							 .removePartListener(dil);
		
		PlatformUI.getWorkbench()
							  .getActiveWorkbenchWindow()
							  .removePerspectiveListener(apa);
		
		PlatformUI.getWorkbench()
		 		  .getActiveWorkbenchWindow()
		 	      .getPartService()
		 	      .removePartListener(dal);
		
		PlatformUI.getWorkbench()
		 					  .getActiveWorkbenchWindow()
		 					  .getPartService()
		 					  .removePartListener(dcl);
		
		HSQLDBHelper.getInstance().chiudiDatabase();
		
		return super.preWindowShellClose();
	}
    
}
