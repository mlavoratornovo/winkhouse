package winkhouse.view.winkcloud;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.winkcloud.ApriCloudMonitorAction;
import winkhouse.action.winkcloud.ApriExportXMLWizardAction;
import winkhouse.action.winkcloud.ApriImportXMLWizardAction;
import winkhouse.action.winkcloud.ApriWinkCloudImportXMLWizardAction;
import winkhouse.action.winkcloud.AvviaCloudMonitorAction;
import winkhouse.action.winkcloud.AvviaRicercaWinkCloudAction;
import winkhouse.action.winkcloud.CancellaCloudMonitorAction;
import winkhouse.action.winkcloud.NewCloudMonitorAction;
import winkhouse.action.winkcloud.NewWinkCloudRicercaModel;
import winkhouse.action.winkcloud.RefreshMonitorsAction;
import winkhouse.action.winkcloud.SalvaCloudMonitor;
import winkhouse.action.winkcloud.StopCloudMonitorAction;
import winkhouse.action.winkcloud.StopRicercaWinkCloudAction;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.model.winkcloud.CloudMonitorState;
import winkhouse.model.winkcloud.CloudQueryModel;
import winkhouse.model.winkcloud.CloudQueryOrigin;
import winkhouse.model.winkcloud.CloudQueryTypes;
import winkhouse.model.winkcloud.ConnectorTypes;
import winkhouse.model.winkcloud.MonitorFTPModel;
import winkhouse.model.winkcloud.MonitorHTTPModel;
import winkhouse.model.winkcloud.MonitorModel;
import winkhouse.model.winkcloud.WinkMonitorFTPModel;
import winkhouse.model.xml.DummyColloquiCriteriRicercaXMLModel;
import winkhouse.model.xml.RicercheXMLModel;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.xmldeser.wizard.importer.vo.ImporterVO;



public class MonitorsTreeView extends ViewPart {
	
	public final static String ID = "winkhouse.monitorstreeview";
	
	private TreeViewer viewer;	
	private ArrayList monitors = null;
	
	private ImageDescriptor addMonitor = Activator.getImageDescriptor("icons/filenew.png");
	private Image MonitorFTP = Activator.getImageDescriptor("icons/wizardmonitor/FTPconnector16.png").createImage();
	private Image WinkMonitorFTP = Activator.getImageDescriptor("icons/wizardmonitor/WinkFTPconnector16.png").createImage();
	private ImageDescriptor MonitorGDRIVE = Activator.getImageDescriptor("icons/wizardmonitor/DriveConnector16.jpg");
	private ImageDescriptor MonitorWINKCLOUD = Activator.getImageDescriptor("icons/adept_updater.png");
	private ImageDescriptor MonitorWINKCLOUDStart = Activator.getImageDescriptor("icons/adept_updater_start.png");
	private ImageDescriptor startMonitor = Activator.getImageDescriptor("icons/player_play.png");
	private ImageDescriptor stopMonitor = Activator.getImageDescriptor("icons/player_pause.png");
	private Image MonitorFTPStart = Activator.getImageDescriptor("icons/wizardmonitor/FTPconnector16_start.png").createImage();
	private Image WinkMonitorFTPStart = Activator.getImageDescriptor("icons/wizardmonitor/WinkFTPconnector16_start.png").createImage();
	private ImageDescriptor MonitorGDriveStart = Activator.getImageDescriptor("icons/wizardmonitor/DriveConnector16_start.jpg");
	private ImageDescriptor salvaMonitor = Activator.getImageDescriptor("icons/salva.png");
	private ImageDescriptor openMonitor = Activator.getImageDescriptor("icons/folder.png");
	private ImageDescriptor refreshMonitors = Activator.getImageDescriptor("icons/adept_reinstall.png");
	private ImageDescriptor cancellaMonitor = Activator.getImageDescriptor("icons/edittrash.png");
	private ImageDescriptor wizardImportXml = Activator.getImageDescriptor("icons/importxml.png");
	private ImageDescriptor wizardExportXml = Activator.getImageDescriptor("icons/exportxml.png");
	private Image cloudricercaimmobili = Activator.getImageDescriptor("icons/cloudricercaimmobili.png").createImage();
	private ImageDescriptor ricerca = Activator.getImageDescriptor("icons/kfind.png");
	private Image criteriricerca = Activator.getImageDescriptor("icons/criteriricerca.png").createImage();
	private Image ricercatree = Activator.getImageDescriptor("icons/kfind.png").createImage();
	private Image ricercatreeStart = Activator.getImageDescriptor("icons/kfind_start.png").createImage();
	private Image cloudimportimmobili = Activator.getImageDescriptor("icons/cloudimportimmobili.png").createImage();
	
	
	private Menu treenodemenu = null;
	private MenuManager menuMgr = null; 
			
	public MonitorsTreeView() {
	}

	class ViewContentProvider implements ITreeContentProvider {
		
		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}
		
		private ArrayList filterByType(ConnectorTypes type){
			
			ArrayList returnType = new ArrayList();
			Iterator it = getMonitors().iterator();
			
			while (it.hasNext()) {
				
				Object type2 = (Object) it.next();
				if (type2 instanceof MonitorFTPModel && type == ConnectorTypes.FTP){
					returnType.add(type2);
				}
				if (type2 instanceof MonitorHTTPModel && type == ConnectorTypes.WINKCLOUD){
					returnType.add(type2);
				}
				
			}
						
			return returnType;
			
		} 

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			
			if (parentElement instanceof String[]){				
				return (String[])parentElement;
				
			}else if ((parentElement instanceof String) && (((String)parentElement).equalsIgnoreCase("FTP"))) {
				return filterByType(ConnectorTypes.FTP).toArray();
					
			}else if ((parentElement instanceof String) && (((String)parentElement).equalsIgnoreCase("WINKCLOUD"))) {
				return filterByType(ConnectorTypes.WINKCLOUD).toArray();
								
			}else if ((parentElement instanceof MonitorHTTPModel)) {
				ArrayList monitorsChilds = new ArrayList();				
				monitorsChilds.addAll(((MonitorHTTPModel)parentElement).getRicercheServite().entrySet());			
				monitorsChilds.addAll(((MonitorHTTPModel)parentElement).getRicerche());
				return monitorsChilds.toArray();
				
			}else if ((parentElement instanceof WinkMonitorFTPModel)) {
				return ((WinkMonitorFTPModel)parentElement).getRicerche().toArray();
				
			}else if ((parentElement instanceof RicercheXMLModel)) {
				ArrayList ricercheChilds = new ArrayList();
				ricercheChilds.addAll(((RicercheXMLModel)parentElement).getCriteri());
				if (((RicercheXMLModel)parentElement).getImporterVO().size() > 0){
					ricercheChilds.addAll(((RicercheXMLModel)parentElement).getImporterVO());
				}
				return ricercheChilds.toArray();
			}
			
			return new Object[1];
		}

		@Override
		public Object getParent(Object element) {
						
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {			
			if ((element instanceof String) || 
				(element instanceof WinkMonitorFTPModel) || 
				(element instanceof RicercheXMLModel) ||
				(element instanceof MonitorHTTPModel)){
				return true;
			}else{
				return false;
			}
		}

	}
	
	ColumnLabelProvider labelProvider = new ColumnLabelProvider() {

		@Override
		public String getToolTipText(Object element) {
			return getToolTipUnlimitedText(element);
		}

		@Override
		public Point getToolTipShift(Object object) {
			return new Point(5,5);
		}

		@Override
		public int getToolTipDisplayDelayTime(Object object) {
			return 2000;
		}

		@Override
		public int getToolTipTimeDisplayed(Object object) {
			return 5000;
		}

		@Override
		public void update(ViewerCell cell) {
			cell.setText(getText(cell.getElement()));
			cell.setImage(getImage(cell.getElement()));
		}
		
		private String getLimitedText(String text){
			if (text.length() > 100){
				text = text.substring(0, 100);
			}
			return text;
		}
		
		public String getText(Object obj) {
			
			if (obj instanceof String){
				return getLimitedText((String)obj);
			}
			
			if (obj.getClass().equals(MonitorFTPModel.class)){
				return getLimitedText(((MonitorFTPModel)obj).toString());
			}
			if (obj.getClass().equals(WinkMonitorFTPModel.class)){
				return getLimitedText(((MonitorFTPModel)obj).toString());
			}
			
			if (obj.getClass().equals(MonitorHTTPModel.class)){
				return getLimitedText(((MonitorHTTPModel)obj).toString());
			}

			if (obj.getClass().equals(RicercheXMLModel.class)){
				return getLimitedText(((RicercheXMLModel)obj).toString());
			}

			if (obj.getClass().equals(ColloquiCriteriRicercaModel.class)){
				return getLimitedText(((ColloquiCriteriRicercaModel)obj).toString());
			}
			
			if (obj.getClass().equals(DummyColloquiCriteriRicercaXMLModel.class)){
				return getLimitedText(((DummyColloquiCriteriRicercaXMLModel)obj).toString());
			}
			if (obj.getClass().equals(ImporterVO.class)){
				return getLimitedText(obj.toString());
			}
			if (obj instanceof Map.Entry){
				return getLimitedText(((String)((Map.Entry)obj).getKey()).toString() + "_" + ((Map.Entry)obj).getValue().toString());
			}

			String returnValue = null;
			
			return returnValue;
		}
		
		public String getToolTipUnlimitedText(Object obj) {
			
			if (obj instanceof String){
				return (String)obj;
			}
			
			if (obj.getClass().equals(MonitorFTPModel.class)){
				return ((MonitorFTPModel)obj).toString();
			}
			if (obj.getClass().equals(WinkMonitorFTPModel.class)){
				return ((MonitorFTPModel)obj).toString();
			}
			
			if (obj.getClass().equals(MonitorHTTPModel.class)){
				return ((MonitorHTTPModel)obj).toString();
			}

			if (obj.getClass().equals(RicercheXMLModel.class)){
				return ((RicercheXMLModel)obj).toString();
			}

			if (obj.getClass().equals(ColloquiCriteriRicercaModel.class)){
				return ((ColloquiCriteriRicercaModel)obj).toString();
			}
			
			if (obj.getClass().equals(DummyColloquiCriteriRicercaXMLModel.class)){
				return ((DummyColloquiCriteriRicercaXMLModel)obj).toString();
			}
			if (obj.getClass().equals(ImporterVO.class)){
				return obj.toString();
			}
			if (obj instanceof Map.Entry){
				return ((String)((Map.Entry)obj).getKey()).toString() + "_" + ((Map.Entry)obj).getValue().toString();
			}

			String returnValue = null;
			
			return returnValue;
		}
		
		public Image getImage(Object obj) {
			
			if (obj.getClass().equals(MonitorFTPModel.class)){
				
				if (((MonitorFTPModel)obj).getStato() == null || ((MonitorFTPModel)obj).getStato() == CloudMonitorState.PAUSA){
					return MonitorFTP;
				}else{
					return MonitorFTPStart;
				}
				
			}
			if (obj.getClass().equals(WinkMonitorFTPModel.class)){
				
				if (((WinkMonitorFTPModel)obj).getStato() == null || ((WinkMonitorFTPModel)obj).getStato() == CloudMonitorState.PAUSA){
					return WinkMonitorFTP;
				}else{
					return WinkMonitorFTPStart;
				}
				
			}
			
			if (obj.getClass().equals(MonitorHTTPModel.class)){
				
				if (((MonitorHTTPModel)obj).getStato() == null || ((MonitorHTTPModel)obj).getStato() == CloudMonitorState.PAUSA){
					return MonitorWINKCLOUD.createImage();
				}else{
					return MonitorWINKCLOUDStart.createImage();
				}
				
			}

			if (obj.getClass().equals(ColloquiCriteriRicercaModel.class)){
				
				return criteriricerca;
				
			}

			if (obj.getClass().equals(DummyColloquiCriteriRicercaXMLModel.class)){
				
				return criteriricerca;
				
			}

			if (obj.getClass().equals(RicercheXMLModel.class)){

				if (((RicercheXMLModel)obj).getStato() == null || ((RicercheXMLModel)obj).getStato() == CloudMonitorState.PAUSA){
					return ricercatree;
				}else{
					return ricercatreeStart;
				}				
				
			}
			if (obj.getClass().equals(ImporterVO.class)){
				return cloudimportimmobili;
			}
			
			if (obj instanceof Map.Entry){
				return cloudricercaimmobili;
			}
			
			return null;
		}

    };
    
	class ViewLabelProvider extends LabelProvider {
		
		public String getText(Object obj) {
			
			if (obj instanceof String){
				return (String)obj;
			}
			
			if (obj.getClass().equals(MonitorFTPModel.class)){
				return ((MonitorFTPModel)obj).toString();
			}
			if (obj.getClass().equals(WinkMonitorFTPModel.class)){
				return ((MonitorFTPModel)obj).toString();
			}
			
			if (obj.getClass().equals(MonitorHTTPModel.class)){
				return ((MonitorHTTPModel)obj).toString();
			}

			if (obj.getClass().equals(RicercheXMLModel.class)){
				return ((RicercheXMLModel)obj).toString();
			}

			if (obj.getClass().equals(ColloquiCriteriRicercaModel.class)){
				return ((ColloquiCriteriRicercaModel)obj).toString();
			}
			
			if (obj.getClass().equals(DummyColloquiCriteriRicercaXMLModel.class)){
				return ((DummyColloquiCriteriRicercaXMLModel)obj).toString();
			}
			if (obj.getClass().equals(ImporterVO.class)){
				return obj.toString();
			}
			if (obj instanceof Map.Entry){
				return ((String)((Map.Entry)obj).getKey()).toString() + "_" + ((Map.Entry)obj).getValue().toString();
			}

			String returnValue = null;
			
			return returnValue;
		}

		public Image getImage(Object obj) {
			
			if (obj.getClass().equals(MonitorFTPModel.class)){
				
				if (((MonitorFTPModel)obj).getStato() == null || ((MonitorFTPModel)obj).getStato() == CloudMonitorState.PAUSA){
					return MonitorFTP;
				}else{
					return MonitorFTPStart;
				}
				
			}
			if (obj.getClass().equals(WinkMonitorFTPModel.class)){
				
				if (((WinkMonitorFTPModel)obj).getStato() == null || ((WinkMonitorFTPModel)obj).getStato() == CloudMonitorState.PAUSA){
					return WinkMonitorFTP;
				}else{
					return WinkMonitorFTPStart;
				}
				
			}
			
			if (obj.getClass().equals(MonitorHTTPModel.class)){
				
				if (((MonitorHTTPModel)obj).getStato() == null || ((MonitorHTTPModel)obj).getStato() == CloudMonitorState.PAUSA){
					return MonitorWINKCLOUD.createImage();
				}else{
					return MonitorWINKCLOUDStart.createImage();
				}
				
			}

			if (obj.getClass().equals(ColloquiCriteriRicercaModel.class)){
				
				return criteriricerca;
				
			}

			if (obj.getClass().equals(DummyColloquiCriteriRicercaXMLModel.class)){
				
				return criteriricerca;
				
			}

			if (obj.getClass().equals(RicercheXMLModel.class)){

				if (((RicercheXMLModel)obj).getStato() == null || ((RicercheXMLModel)obj).getStato() == CloudMonitorState.PAUSA){
					return ricercatree;
				}else{
					return ricercatreeStart;
				}				
				
			}
			if (obj.getClass().equals(ImporterVO.class)){
				return cloudimportimmobili;
			}
			
			if (obj instanceof Map.Entry){
				return cloudricercaimmobili;
			}
			
			return null;
		}
	}
	
	public void createPartControl(Composite parent) {
		
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		ColumnViewerToolTipSupport.enableFor(viewer);
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		
		mgr.add(new ApriImportXMLWizardAction("Import XML", wizardImportXml));
		mgr.add(new ApriExportXMLWizardAction("Export XML", wizardExportXml));
		mgr.add(new RefreshMonitorsAction("Refresh monitors", refreshMonitors));
		mgr.add(new ApriCloudMonitorAction("Carica monitor", openMonitor));
		mgr.add(new NewCloudMonitorAction("Nuovo monitor", addMonitor));		
		mgr.add(new CancellaCloudMonitorAction("Cancella monitor", cancellaMonitor));
		
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(labelProvider);		
		viewer.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
				Object o = ((TreeSelection)event.getSelection()).getFirstElement();
				QueryFilesView qfv = (QueryFilesView)PlatformUI.getWorkbench()
															   .getActiveWorkbenchWindow()
															   .getActivePage()
															   .findView(QueryFilesView.ID);	
				
				if (o instanceof MonitorModel){					
					qfv.setQueries(((MonitorModel)o).getCloudQueries());					
				}
				if (o instanceof ImporterVO){
					
					RicercheXMLModel rm = null;
					ArrayList<CriteriRicercaModel> al_crm = new ArrayList<CriteriRicercaModel>(); 
					try{
						rm = (RicercheXMLModel)((TreeSelection)event.getSelection()).getPaths()[0].getSegment(2);
						ArrayList al_dcrm = rm.getCriteri();
						for (Iterator iterator = al_dcrm.iterator(); iterator.hasNext();) {
							DummyColloquiCriteriRicercaXMLModel dcrm = (DummyColloquiCriteriRicercaXMLModel) iterator.next();
							al_crm.add(dcrm.toCriteriRicercaModel());							
						}
					}catch(Exception e){
						rm = null;
					}
					
					ImporterVO ivo = ((ImporterVO)o);
					
					CloudQueryModel cqm = new CloudQueryModel(CloudQueryOrigin.DESKTOP, ivo.getWinkCloudPathFile(), ivo.getWinkCloudPathFile_receiveDate());
					cqm.setCriteri(al_crm);
					cqm.setQueryType(CloudQueryTypes.IMMOBILI);
					cqm.setResults(new ArrayList(ivo.getHmImmobili().values()));
					ArrayList<CloudQueryModel> al = new ArrayList<CloudQueryModel>();
					al.add(cqm);
					qfv.setQueries(al);
				}
				if (o instanceof Map.Entry){					
					
					RicercheXMLModel rm = null;
					CloudQueryModel cqm = null;
					Integer tipoRicerca = 0;
					ArrayList<CriteriRicercaModel> al_crm = new ArrayList<CriteriRicercaModel>(); 
					try{
						rm = (RicercheXMLModel)((Map.Entry)o).getValue();
						tipoRicerca = rm.getTipo();
						ArrayList al_dcrm = rm.getCriteri();
						for (Iterator iterator = al_dcrm.iterator(); iterator.hasNext();) {
							Object col = iterator.next();
							if (col instanceof DummyColloquiCriteriRicercaXMLModel) {
								DummyColloquiCriteriRicercaXMLModel dcrm = (DummyColloquiCriteriRicercaXMLModel)col;
								al_crm.add(dcrm.toCriteriRicercaModel());				
							}else if (col instanceof ColloquiCriteriRicercaModel) {
								DummyColloquiCriteriRicercaXMLModel dcrm = new DummyColloquiCriteriRicercaXMLModel((ColloquiCriteriRicercaVO)col);
								al_crm.add(dcrm.toCriteriRicercaModel());
							}
						}
					}catch(Exception e){
						rm = null;
					}
					if (tipoRicerca == 0){	
						cqm = new CloudQueryModel(CloudQueryOrigin.DESKTOP, "", new Date());
					}
					if (tipoRicerca == 1){	
						cqm = new CloudQueryModel(CloudQueryOrigin.MOBILE, "", new Date());
					}	
					if (cqm != null){
						cqm.setCriteri(al_crm);
						cqm.setQueryType(CloudQueryTypes.IMMOBILI);					
						ArrayList<CloudQueryModel> al = new ArrayList<CloudQueryModel>();
						al.add(cqm);
						qfv.setQueries(al);						
					}
					
				}
				
			}
			
		});

		menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		menuMgr.addMenuListener(new IMenuListener() {
			
		    @Override
		    public void menuAboutToShow(IMenuManager manager) {
		    	
		      if(viewer.getSelection().isEmpty()) {
		        return;
		      }
		      
		      if(viewer.getSelection() instanceof IStructuredSelection) {
		    	  
		        IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
//		        if (selection.getFirstElement() instanceof WinkMonitorFTPModel){
//		        	menuMgr.add(new NewRicercaAction("Nuova ricerca", ricerca, (WinkMonitorFTPModel)selection.getFirstElement()));
//		        	menuMgr.add(new SalvaCloudMonitor("Salva monitor",salvaMonitor));
//		        }
		        
		        if (selection.getFirstElement() instanceof MonitorHTTPModel){
		        			        
		        	menuMgr.add(new NewWinkCloudRicercaModel("Nuova ricerca", ricerca, (MonitorHTTPModel)selection.getFirstElement()));
		        	menuMgr.add(new SalvaCloudMonitor("Salva monitor",salvaMonitor));
		        	
		        	MonitorModel object = (MonitorModel)selection.getFirstElement();
		        	if (object.getStato() == null || object.getStato() == CloudMonitorState.PAUSA){
		        		menuMgr.add(new AvviaCloudMonitorAction("Avvia monitor",startMonitor));
		        	}else{
		        		menuMgr.add(new StopCloudMonitorAction("Ferma monitor",stopMonitor));
		        	}
		        	
		        }
		        
		        if (selection.getFirstElement() instanceof MonitorFTPModel){
		        	
		        	MonitorModel object = (MonitorModel)selection.getFirstElement();
		        	if (object.getStato() == null || object.getStato() == CloudMonitorState.PAUSA){
		        		menuMgr.add(new AvviaCloudMonitorAction("Avvia monitor",startMonitor));		        		
		        	}else{
		        		menuMgr.add(new StopCloudMonitorAction("Ferma monitor",stopMonitor));
		        	}
		        	menuMgr.add(new SalvaCloudMonitor("Salva monitor",salvaMonitor));
		        	
		        }		        	
		        
		        if (selection.getFirstElement() instanceof RicercheXMLModel){
		        	RicercheXMLModel object = (RicercheXMLModel)selection.getFirstElement();
		        	if (object.getState() == null || object.getState() == CloudMonitorState.PAUSA){
		        		menuMgr.add(new AvviaRicercaWinkCloudAction("Avvia ricerca",startMonitor));		        		
		        	}else{
		        		menuMgr.add(new StopRicercaWinkCloudAction("Ferma ricerca",stopMonitor));
		        	}
		        	
		        }
		        
		        if (selection.getFirstElement() instanceof ImporterVO){
		        	menuMgr.add(new ApriWinkCloudImportXMLWizardAction("Avvia wizard importazione dati", 
		        													   wizardImportXml, 
		        													   (ImporterVO)selection.getFirstElement()));
		        }
		        
  	          }
		        
		    }
		    
		  });
		  
		menuMgr.setRemoveAllWhenShown(true);
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
		
		viewer.setInput(new String[]{"FTP","WINKCLOUD"});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	/*	RefreshImmobiliAction ria = new RefreshImmobiliAction();
		ria.run();*/

	}

	public TreeViewer getViewer() {
		return viewer;
	}
	
	public ArrayList<MonitorModel> getMonitors() {
		if (monitors == null){
			monitors = new ArrayList<MonitorModel>();
		}
		return monitors;
	}
	
	public void setMonitors(ArrayList<MonitorFTPModel> monitors) {
		this.monitors = monitors;
	}

	public void addMonitor(MonitorModel model){
		// TODO aggiungere controllo se monitor già presente
		getMonitors().add(model);
		if (model instanceof MonitorHTTPModel){
			((MonitorHTTPModel)model).setTvMonitors(viewer);
		}
		viewer.setInput(new String[]{"FTP","WINKCLOUD"});
		viewer.expandAll();
	}

}
