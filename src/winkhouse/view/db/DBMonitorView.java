package winkhouse.view.db;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.dbmonitor.ADLogRegister;
import winkhouse.action.dbmonitor.BackUpAction;
import winkhouse.action.dbmonitor.CleanLog;
import winkhouse.action.dbmonitor.RestoreBackUpAction;
import winkhouse.action.dbmonitor.SaveLog;
import winkhouse.util.WinkhouseUtils;


public class DBMonitorView extends ViewPart {

	public final static String ID = "winkhouse.dbmonitorview";
	
	private FormToolkit ft = null;
	private ScrolledForm f = null;
	private Text console = null;
	
	
	public DBMonitorView() {}
	
	@Override
	public void createPartControl(Composite parent) {
		
		ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createScrolledForm(parent);
		f.setExpandVertical(true);
		f.setImage(Activator.getImageDescriptor("/icons/dbmonitor16.png").createImage());
		f.setText("Console database");		
		f.getBody().setLayout(new GridLayout());
		
		IToolBarManager mgr = f.getToolBarManager();
		
		boolean bundledb = WinkhouseUtils.getInstance().isBundleDBRunning();
				
		ADLogRegister adlr = new ADLogRegister(ADLogRegister.CONNECT_TOOLTIP,Action.AS_CHECK_BOX);
		adlr.setEnabled(bundledb);
		if (!bundledb) 
			adlr.setToolTipText("Funzioni abilitate solo con base dati autonoma");
		mgr.add(adlr);
		
		CleanLog cl =new CleanLog("Pulisci il log", Activator.getImageDescriptor("/icons/history_clear.png"));
		cl.setEnabled(bundledb);
		if (!bundledb) 
			cl.setToolTipText("Funzioni abilitate solo con base dati autonoma");
		mgr.add(cl);
		
		SaveLog sl = new SaveLog("Salva il log", Activator.getImageDescriptor("/icons/document-save.png"));		 
		sl.setEnabled(bundledb);
		if (!bundledb)
			sl.setToolTipText("Funzioni abilitate solo con base dati autonoma");
		mgr.add(sl);
		
		BackUpAction bckA = new BackUpAction("Esegui BackUp base dati", Activator.getImageDescriptor("/icons/DatabaseBCK16.png"));
		bckA.setEnabled(bundledb);
		if (!bundledb)
			bckA.setToolTipText("Funzioni abilitate solo con base dati autonoma");
		mgr.add(bckA);

		RestoreBackUpAction rbckA = new RestoreBackUpAction("Ripristina BackUp base dati", Activator.getImageDescriptor("/icons/DatabaseRBCK16.png"));
		rbckA.setEnabled(bundledb);
		if (!bundledb)
			rbckA.setToolTipText("Funzioni abilitate solo con base dati autonoma");
		mgr.add(rbckA);

		f.updateToolBar();
		
		Section section = ft.createSection(f.getBody(), 
				   						   Section.DESCRIPTION|Section.TITLE_BAR|
				   						   Section.TWISTIE);
		section.setExpanded(true);
		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				f.reflow(true);
			}

		});
		
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		section.setLayout(new GridLayout());
		section.setLayoutData(gd);
		section.setText("Log Database");
		section.setDescription("mostra le istruzioni SQL eseguite nel database");						
		
		console = ft.createText(section,"",SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL);
		console.setLayoutData(gd);
		console.setBackground(new Color(null,0,0,0));
		console.setForeground(new Color(null,76,217,27));
		console.setEditable(false);
		console.setCursor(PlatformUI.getWorkbench().getDisplay().getSystemCursor(SWT.CURSOR_HAND));
		
		section.setClient(console);
				
	}
	
	public class ConsoleUpdater implements Runnable {
		
		private Text console = null;
		private String value = null;
		
		public ConsoleUpdater(Text console, String value){
			this.console = console;
			this.value = value;
		}
		
		public void run() {
			console.append(value);
		}
	}
	
	public void updateconsole(String value){
		if (!PlatformUI.getWorkbench().getDisplay().isDisposed()){
            PlatformUI.getWorkbench().getDisplay().asyncExec(new ConsoleUpdater(console, value));
        }
	} 
	
//	@Override
//	public void dispose() {
//    	File f = new File(Activator.getDefault()
//				   .getStateLocation()
//				   .toFile()
//				   .toString() + File.separator + "lock.lock");
//
//	 	if (f.exists()){
//	 		f.delete();
//	 	}
//		
//	 	HSQLDBHelper.getInstance().chiudiDatabase();
//		super.dispose();
//	}
	
	@Override
	public void setFocus() {
		
	}

	public Text getConsole() {
		return console;
	}

}
