package winkhouse.view.winkcloud;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.winkcloud.AvviaWebServer;
import winkhouse.engine.search.SearchEngineAnagrafiche;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.model.winkcloud.CloudQueryModel;
import winkhouse.model.winkcloud.HttpServerConnector;
import winkhouse.model.winkcloud.MonitorHTTPModel;
import winkhouse.model.winkcloud.jobs.HTTPJob;
import winkhouse.util.WinkhouseUtils;

public class QueryFilesView extends ViewPart {

	public final static String ID = "winkhouse.queryfiles";
	
	private FormToolkit ft = null;
	private Form f = null;	
	private TableViewer tvQueryFiles = null;
	
	private Image richiesta = Activator.getImageDescriptor("icons/cloudquery.png").createImage();
	private Image richiesta16 = Activator.getImageDescriptor("icons/cloudquery16.png").createImage();
	private ImageDescriptor MonitorWINKCLOUD = Activator.getImageDescriptor("icons/adept_updater.png");
	
	public QueryFilesView() {
	}
	
	class ResultFinder implements IRunnableWithProgress{
		
		private ResultsView rv;
		private CloudQueryModel cqm;
		private ArrayList criteria;		
		
		public ResultFinder(ResultsView rv, CloudQueryModel cqm){
			this.rv = rv;
			this.cqm = cqm;			
		}

		@Override
		public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {
						
			ArrayList resultItems = new ArrayList();
			int maxcloud = Integer.valueOf((WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.MAXCLOUDRESULT).equalsIgnoreCase(""))
					? WinkhouseUtils.getInstance().getPreferenceStore().getDefaultString(WinkhouseUtils.MAXCLOUDRESULT)
					: WinkhouseUtils.getInstance().getPreferenceStore().getString(WinkhouseUtils.MAXCLOUDRESULT));
			
			switch (this.cqm.getType()){
			case SEARCH_IMMOBILI:{
				monitor.beginTask("Ricerca immobili", 2);
				SearchEngineImmobili sei = new SearchEngineImmobili(this.cqm.getCriteri());
				monitor.worked(1);
				try {
					resultItems = sei.find();
					
					if (resultItems.size() > maxcloud){
						resultItems = new ArrayList(resultItems.subList(0, maxcloud));
					}
					
					this.cqm.setResults(resultItems);
					rv.getViewSite().getShell().getDisplay().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							rv.setResults(cqm.getResults(), cqm.getType());
						}
					});
					monitor.worked(2);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				break;
			}
			case SEARCH_ANAGRAFICHE:{
				monitor.beginTask("Ricerca anagrafiche", 2);
				SearchEngineAnagrafiche sei = new SearchEngineAnagrafiche(this.cqm.getCriteri());
				monitor.worked(1);
				try {
					resultItems = sei.find();
					
					if (resultItems.size() > maxcloud){
						resultItems = new ArrayList(resultItems.subList(0, maxcloud));
					}
					
					this.cqm.setResults(resultItems);
					rv.getViewSite().getShell().getDisplay().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							rv.setResults(cqm.getResults(), cqm.getType());
						}
					});
					monitor.worked(2);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				break;
				
			}
			case POST_PUT_IMMOBILE:{
				monitor.beginTask("Ricezione immobili", 1);
				rv.getViewSite().getShell().getDisplay().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						rv.setResults(cqm.getResults(), cqm.getType());
					}
				});
				monitor.worked(1);
				break;
				
			}
			}
			
		}		
		
	}
	
	@Override
	public void createPartControl(Composite parent) {
		
		ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createForm(parent);
		f.setImage(richiesta);
		f.setText("Richieste ricevute");
		f.getBody().setLayout(new GridLayout());
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		
		AvviaWebServer startWebServer = new AvviaWebServer("Avvia server", Action.AS_CHECK_BOX);
		startWebServer.setImageDescriptor(MonitorWINKCLOUD);
		int winkport = 80;
		boolean onlyusers = false;
		try{
			winkport = WinkhouseUtils.getInstance().getPreferenceStore().getInt("winkcloudport");
		}catch(Exception e){
			winkport = 80;
		}
		try{
			onlyusers = WinkhouseUtils.getInstance().getPreferenceStore().getBoolean("onlyregisteredusers");
		}catch(Exception e){
			onlyusers = false;
		}
		
		HttpServerConnector c = new HttpServerConnector(winkport, onlyusers);
		MonitorHTTPModel m = new MonitorHTTPModel(c);
		startWebServer.setJob(new HTTPJob("Web Server", m, this));
		mgr.add(startWebServer);
		
		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;	

		tvQueryFiles = new TableViewer(f.getBody(),SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION|SWT.H_SCROLL | SWT.V_SCROLL);
		tvQueryFiles.getTable().setLayoutData(gdExpVH);
		tvQueryFiles.getTable().setHeaderVisible(true);
		tvQueryFiles.getTable().setLinesVisible(true);
		tvQueryFiles.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				
				if (inputElement instanceof ArrayList){
					return ((ArrayList)inputElement).toArray();
				}
				return null;
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			
		});
		tvQueryFiles.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				
				if (columnIndex == 0){
					if (element instanceof CloudQueryModel){
						return richiesta16;
					}
				}
				
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				switch(columnIndex){
					case 1:return((CloudQueryModel)element).getTypeDescription();
//					case 2:return((CloudQueryModel)element).getQueryTypeDescription();
//					case 3:return((CloudQueryModel)element).getFilePathName();
					case 2:return((CloudQueryModel)element).getDataRicezioneFile().toString();
					case 3:return((CloudQueryModel)element).getDescrizioneQuery(); 
				}
				return null;
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
			}
			
		});
		tvQueryFiles.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
				Object o = ((StructuredSelection)event.getSelection()).getFirstElement();
				
				if (o instanceof CloudQueryModel){
					ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());

					ResultsView rv = (ResultsView)PlatformUI.getWorkbench()
					   		  							    .getActiveWorkbenchWindow()
					   		  								.getActivePage()
					   		  								.findView(ResultsView.ID);
					
					try {
						pmd.run(true, true, new ResultFinder(rv,(CloudQueryModel)o));
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}
		});
		
		TableColumn tcimg = new TableColumn(tvQueryFiles.getTable(),SWT.CENTER,0);
		tcimg.setWidth(20);

		TableColumn tcTipologia = new TableColumn(tvQueryFiles.getTable(),SWT.CENTER,1);
		tcTipologia.setWidth(200);
		tcTipologia.setText("Origine");

//		TableColumn tcTipoQuery = new TableColumn(tvQueryFiles.getTable(),SWT.CENTER,2);
//		tcTipoQuery.setWidth(60);
//		tcTipoQuery.setText("Tipo dati");
//		
//		TableColumn tcfilePathName = new TableColumn(tvQueryFiles.getTable(),SWT.CENTER,3);
//		tcfilePathName.setWidth(100);
//		tcfilePathName.setText("File richiesta");

		TableColumn tcRicezione = new TableColumn(tvQueryFiles.getTable(),SWT.CENTER,2);
		tcRicezione.setWidth(200);
		tcRicezione.setText("Data ricezione");

		TableColumn tcRichiesta = new TableColumn(tvQueryFiles.getTable(),SWT.CENTER,3);
		tcRichiesta.setWidth(400);
		tcRichiesta.setText("Richiesta");
		

	}

	@Override
	public void setFocus() {}

	public void setQueries(ArrayList<CloudQueryModel> queries) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable(){

			@Override
			public void run() {
				tvQueryFiles.setInput(queries);				
			}
			
		});
		
	}

}
