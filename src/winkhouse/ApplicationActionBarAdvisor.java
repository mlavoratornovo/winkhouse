package winkhouse;

import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.ActionSetRegistry;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;

import winkhouse.action.AboutDialogAction;
import winkhouse.action.ComboRicercaAction;
import winkhouse.action.PreferenceAction;
import winkhouse.action.WizardColloquiAction;
import winkhouse.action.WizardGCalendarSyncAction;
import winkhouse.action.WizardRicercaAction;
import winkhouse.action.immobili.CambiaArchivioAction;
import winkhouse.action.navigation.FirstItemAction;
import winkhouse.action.navigation.LastItemAction;
import winkhouse.action.navigation.NextItemAction;
import winkhouse.action.navigation.PreviousItemAction;
import winkhouse.action.plugins.PluginsLauncher;
import winkhouse.action.report.ImportReportAction;
import winkhouse.dialogs.custom.LoggedUser;
import winkhouse.dialogs.custom.RicercaCombo;
import winkhouse.export.exporter.IWinkPlugin;


/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    // Actions - important to allocate these only in makeActions, and then use them
    // in the fill methods.  This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
    private IWorkbenchAction exitAction;
    private IWorkbenchAction aboutAction;
/*    private IWorkbenchAction newWindowAction;
    private OpenViewAction openViewAction;
    private Action messagePopupAction;*/
    private PreferenceAction preferenceAction;
    private IContributionItem perspectivesMenu;
    private WizardColloquiAction wizardColloquiAction = null;
//    private CancellazioneWizardAction wizardCancellazioniAction = null;
    private CambiaArchivioAction cambiaArchivioAction = null;
    private WizardRicercaAction wizardRicercaAction = null;
    private WizardGCalendarSyncAction wizardGCalendarSyncAction= null;
    
    private FirstItemAction firstItemAction = null;
    private LastItemAction lastItemAction = null;
    private NextItemAction nextItemAction = null;
    private PreviousItemAction previousItemAction = null;
    private ImportReportAction importReportAction = null;
    private AboutDialogAction about = null;
    private LoggedUser loggedUser = null;
    //private ComboRicercaAction comboRicercaAction = null;
    private RicercaCombo ricercaCombo = null;
    private static final String IWINKPLUG_ID = "winkhouse.export.PluginPoint";
    public static final String PLUGINS = "plugins";
    
    private final static String[] actionsDefault = {"org.eclipse.ant.ui.actionSet.presentation",
    	"org.eclipse.debug.ui.breakpointActionSet",
    	"org.eclipse.debug.ui.debugActionSet",
    	"org.eclipse.debug.ui.launchActionSet",
    	"org.eclipse.debug.ui.profileActionSet",
    	"org.eclipse.egit.ui.gitaction",
    	"org.eclipse.egit.ui.navigation",
    	"org.eclipse.jdt.debug.ui.JDTDebugActionSet",
    	"org.eclipse.jdt.junit.JUnitActionSet",
    	"org.eclipse.jdt.ui.text.java.actionSet.presentation",
    	"org.eclipse.jdt.ui.JavaElementCreationActionSet",
    	"org.eclipse.jdt.ui.JavaActionSet",
    	"org.eclipse.jdt.ui.A_OpenActionSet",
    	"org.eclipse.jdt.ui.CodingActionSet",
    	"org.eclipse.jdt.ui.SearchActionSet",
    	"org.eclipse.mylyn.context.ui.actionSet",
    	"org.eclipse.mylyn.java.actionSet",
    	"org.eclipse.mylyn.java.actionSet.browsing",
    	"org.eclipse.mylyn.doc.actionSet",
    	"org.eclipse.mylyn.tasks.ui.navigation",
    	"org.eclipse.mylyn.tasks.ui.navigation.additions",
    	"org.eclipse.pde.ui.SearchActionSet",
    	"org.eclipse.ui.cheatsheets.actionSet",
    	"org.eclipse.search.searchActionSet",
    	"org.eclipse.team.cvs.ui.CVSActionSet",
    	"org.eclipse.team.ui.actionSet",
    	"org.eclipse.ui.edit.text.actionSet.annotationNavigation",
    	"org.eclipse.ui.edit.text.actionSet.navigation",
    	"org.eclipse.ui.edit.text.actionSet.convertLineDelimitersTo",
    	"org.eclipse.ui.externaltools.ExternalToolsSet",
    	"org.eclipse.ui.NavigateActionSet",
    	"org.eclipse.ui.actionSet.keyBindings",
    	"org.eclipse.ui.WorkingSetModificationActionSet",
    	"org.eclipse.ui.WorkingSetActionSet",
    	"org.eclipse.ui.actionSet.openFiles",
    	"org.eclipse.ui.edit.text.actionSet.presentation",
    	"org.eclipse.wb.core.ui.actionset"};
    
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }
    
    protected void makeActions(final IWorkbenchWindow window) { 
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.

        exitAction = ActionFactory.QUIT.create(window);
        exitAction.setToolTipText("Chiude il programma");
        exitAction.setImageDescriptor(Activator.getImageDescriptor("icons/application-exit.png"));
        register(exitAction);
        
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction); 
        
        preferenceAction = new PreferenceAction(window,"Impostazioni",Activator.getImageDescriptor("icons/settings16.png"));
        register(preferenceAction);
        
        perspectivesMenu = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);
        
        wizardColloquiAction = new WizardColloquiAction(
        						   		"Wizard inserimento colloqui",
        						   		Activator.getImageDescriptor("icons/colloqui.png")
        												);
        register(wizardColloquiAction);
        
        cambiaArchivioAction = new CambiaArchivioAction("Visualizza archivio storico",Action.AS_CHECK_BOX);
        cambiaArchivioAction.setImageDescriptor(Activator.getImageDescriptor("icons/filesave.png"));
        
        wizardRicercaAction = new WizardRicercaAction("Wizard ricerca",
				  									  Activator.getImageDescriptor("icons/ricercabig.png"));
        register(wizardRicercaAction);
        /*
        wizardGCalendarSyncAction = new WizardGCalendarSyncAction(
        										"wizard sincronizzazione con GCalendar",
        										Activator.getImageDescriptor("icons/google_calendar_20.png"));

        register(wizardGCalendarSyncAction);
        */
        firstItemAction = new FirstItemAction("Vai al primo elemento", 
        									  Activator.getImageDescriptor("icons/2leftarrow.png"));
        register(firstItemAction);
        
        previousItemAction = new PreviousItemAction("Vai al precendente elemento", 
				    								Activator.getImageDescriptor("icons/1leftarrow.png"));
        register(previousItemAction);

        
        nextItemAction = new NextItemAction("Vai al prossimo elemento", 
				  						    Activator.getImageDescriptor("icons/1rightarrow.png"));
        register(nextItemAction);

        lastItemAction = new LastItemAction("Vai all'ultimo elemento", 
				    						Activator.getImageDescriptor("icons/2rightarrow.png"));
        register(lastItemAction);

        importReportAction = new ImportReportAction("Importazione report", 
        										    Activator.getImageDescriptor("icons/import.png"));
        register(importReportAction);

        about = new AboutDialogAction("About");
        register(about);
        
        loggedUser = new LoggedUser();
        ricercaCombo = new RicercaCombo();
        
        //comboRicercaAction = new ComboRicercaAction(null, Activator.getImageDescriptor("icons/wizardricerca/rapidkfind.png"));
/*        newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
        register(newWindowAction);
    
        openViewAction = new OpenViewAction(window, "Open Another Message View", View.ID);
        register(openViewAction);
        
        messagePopupAction = new MessagePopupAction("Open Message", window);
        register(messagePopupAction);
        */
    }
    
	private void fillPluginMenu(MenuManager pluginMenu) {
		
		//pluginMenu.add(new InstallPluginAction("install plugin"));
		
		IConfigurationElement[] config = Platform.getExtensionRegistry()												 
												 .getConfigurationElementsFor(IWINKPLUG_ID);
		try {
			for (IConfigurationElement e : config) {
				System.out.println("Evaluating extension");
				final Object o = e.createExecutableExtension("class");
				if (o instanceof IWinkPlugin) {
					
					pluginMenu.add(new PluginsLauncher((IWinkPlugin)o));
/*					ISafeRunnable runnable = new ISafeRunnable() {
						@Override
						public void handleException(Throwable exception) {
							System.out.println("Exception in client");
						}

						@Override
						public void run() throws Exception {
							((IWinkPlugin) o).run();
						}
					};
					SafeRunner.run(runnable);*/
				}
			}
		} catch (CoreException ex) {
			System.out.println(ex.getMessage());
		}
	}
    
    protected void fillMenuBar(IMenuManager menuBar) {
    	
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        MenuManager plugInsMenu = new MenuManager("&Plugins", PLUGINS);
        MenuManager helpMenu = new MenuManager("&About", null);
        
        menuBar.add(fileMenu);
        // Add a group marker indicating where action set menus will appear.
        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        menuBar.add(plugInsMenu);
        menuBar.add(helpMenu);        
        
        fileMenu.add(new Separator());
        fileMenu.add(preferenceAction);
        fileMenu.add(importReportAction);
        fileMenu.add(new Separator());        
        fileMenu.add(exitAction);
        
        fillPluginMenu(plugInsMenu);
        
        helpMenu.add(about);
        
        removeActions();
    }
    
    private void removeActions(){
    	ActionSetRegistry asr = WorkbenchPlugin.getDefault().getActionSetRegistry();
    	IActionSetDescriptor[] actions = asr.getActionSets();
    	for (int i = 0; i < actions.length; i++) {
    		//System.out.println(actions[i].getId());
    		if (Arrays.asList(actionsDefault).contains(actions[i].getId())){
				IExtension ext = actions[i].getConfigurationElement()				
      				  					   .getDeclaringExtension();
						   asr.removeExtension(ext, new Object[] { actions[i]});
    			
    		}
		}
    }

    
    protected void fillCoolBar(ICoolBarManager coolBar) {
        IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        coolBar.add(new ToolBarContributionItem(toolbar, "main"));   
        
        toolbar.add(exitAction);
        toolbar.add(wizardColloquiAction);
        //toolbar.add(wizardCancellazioniAction);
        toolbar.add(wizardRicercaAction);
        toolbar.add(cambiaArchivioAction);
        //toolbar.add(wizardGCalendarSyncAction);
        toolbar.add(new Separator());
        toolbar.add(firstItemAction);
        toolbar.add(previousItemAction);
        toolbar.add(nextItemAction);
        toolbar.add(lastItemAction);
        toolbar.add(new Separator());
        toolbar.add(ricercaCombo);
        toolbar.add(new Separator());
        toolbar.add(loggedUser);
    }
}
