package winkhouse.view.winkcloud;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.winkcloud.AvviaWebServer;
import winkhouse.action.winkcloud.CancellaRichiesteCloud;
import winkhouse.action.winkcloud.CercaQueriesAction;
import winkhouse.engine.search.SearchEngineAnagrafiche;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.model.winkcloud.CloudQueryModel;
import winkhouse.model.winkcloud.CloudQueryOrigin;
import winkhouse.model.winkcloud.HttpServerConnector;
import winkhouse.model.winkcloud.MonitorHTTPModel;
import winkhouse.model.winkcloud.jobs.HTTPJob;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.winkcloud.QueryFilesView.inputTipoRichiesta;
import winkhouse.view.winkcloud.QueryFilesView.serverAliveLabel;

public class QueryFilesView extends ViewPart {

	public final static String ID = "winkhouse.queryfiles";
	
	private FormToolkit ft = null;
	private Form f = null;	
	private TableViewer tvQueryFiles = null;
	
	private Image richiesta = Activator.getImageDescriptor("icons/cloudquery.png").createImage();
	private Image richiesta16 = Activator.getImageDescriptor("icons/cloudquery16.png").createImage();
	private ImageDescriptor MonitorWINKCLOUD = Activator.getImageDescriptor("icons/adept_updater.png");
	private serverAliveLabel serverAliveLabel = null;
	private AvviaWebServer startWebServer = null;
	private ComboViewer cvTipiRichieste = null;
	private CloudQueryOrigin selTipo = null;
	
	public TableViewer getTvQueryFiles() {
		return tvQueryFiles;
	}

	public void setTvQueryFiles(TableViewer tvQueryFiles) {
		this.tvQueryFiles = tvQueryFiles;
	}

	public CloudQueryOrigin getSelTipo() {
		return selTipo;
	}

	public void setSelTipo(CloudQueryOrigin selTipo) {
		this.selTipo = selTipo;
	}

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
		
		this.serverAliveLabel = new serverAliveLabel("Checklabel");
		this.startWebServer = new AvviaWebServer("Avvia server", Action.AS_CHECK_BOX);
		this.startWebServer.setImageDescriptor(MonitorWINKCLOUD);
		this.startWebServer.setSal(this.serverAliveLabel);
		
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
        mgr.add((IContributionItem)this.serverAliveLabel);
        mgr.add((IAction)this.startWebServer);

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
		
        CancellaRichiesteCloud crc = new CancellaRichiesteCloud();
        crc.setTv(this.tvQueryFiles);
        mgr.add((IAction)crc);
        mgr.add((IContributionItem)new inputTipoRichiesta("tiporichiesta"));
        mgr.add((IAction)new CercaQueriesAction("filtraqueries"));


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
	
    public class inputTipoRichiesta
    extends ControlContribution {
        public inputTipoRichiesta(String id) {
            super(id);
        }

        protected Control createControl(Composite parent) {
            Composite c = new Composite(parent, 0);
            GridLayout gl = new GridLayout(3, false);
            c.setLayout((Layout)gl);
            Label lAgente = new Label(c, 0);
            lAgente.setText("Filtro tipo :");
            QueryFilesView.this.cvTipiRichieste = new ComboViewer(c, 8);
            QueryFilesView.this.cvTipiRichieste.addSelectionChangedListener(new ISelectionChangedListener(){

                public void selectionChanged(SelectionChangedEvent event) {
                    StructuredSelection ss = (StructuredSelection)event.getSelection();
                    String dummy = (String)ss.getFirstElement();
                    if (dummy == CloudQueryOrigin.CORE_CLASSICLIENTI.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.CORE_CLASSICLIENTI;
                    }
                    if (dummy == CloudQueryOrigin.CORE_CLASSIENERGETICHE.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.CORE_CLASSIENERGETICHE;
                    }
                    if (dummy == CloudQueryOrigin.CORE_RISCALDAMENTI.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.CORE_RISCALDAMENTI;
                    }
                    if (dummy == CloudQueryOrigin.CORE_STATOCONSERVATIVO.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.CORE_STATOCONSERVATIVO;
                    }
                    if (dummy == CloudQueryOrigin.CORE_TIPICOLLOQUI.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.CORE_TIPICOLLOQUI;
                    }
                    if (dummy == CloudQueryOrigin.CORE_TIPICONTATTI.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.CORE_TIPICONTATTI;
                    }
                    if (dummy == CloudQueryOrigin.CORE_TIPIIMMOBILI.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.CORE_TIPIIMMOBILI;
                    }
                    if (dummy == CloudQueryOrigin.CORE_TIPISTANZE.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.CORE_TIPISTANZE;
                    }
                    if (dummy == CloudQueryOrigin.GET_ABBINAMENTI_ANAGRAFICHE_IMMOBILE.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.GET_ABBINAMENTI_ANAGRAFICHE_IMMOBILE;
                    }
                    if (dummy == CloudQueryOrigin.GET_ABBINAMENTI_IMMOBILI_ANAGRAFICA.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.GET_ABBINAMENTI_IMMOBILI_ANAGRAFICA;
                    }
                    if (dummy == CloudQueryOrigin.GET_ANAGRAFICA.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.GET_ANAGRAFICA;
                    }
                    if (dummy == CloudQueryOrigin.GET_COLLOQUI_ANAGRAFICA.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.GET_COLLOQUI_ANAGRAFICA;
                    }
                    if (dummy == CloudQueryOrigin.GET_COLLOQUI_IMMOBILE.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.GET_COLLOQUI_IMMOBILE;
                    }
                    if (dummy == CloudQueryOrigin.GET_IMMOBILE.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.GET_IMMOBILE;
                    }
                    if (dummy == CloudQueryOrigin.POST_PUT_ANAGRAFICA.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.POST_PUT_ANAGRAFICA;
                    }
                    if (dummy == CloudQueryOrigin.POST_PUT_COLLOQUI.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.POST_PUT_COLLOQUI;
                    }
                    if (dummy == CloudQueryOrigin.POST_PUT_IMMAGINI.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.POST_PUT_IMMAGINI;
                    }
                    if (dummy == CloudQueryOrigin.POST_PUT_IMMOBILE.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.POST_PUT_IMMOBILE;
                    }
                    if (dummy == CloudQueryOrigin.SEARCH_ANAGRAFICHE.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.SEARCH_ANAGRAFICHE;
                    }
                    if (dummy == CloudQueryOrigin.SEARCH_IMMOBILI.toString()) {
                        QueryFilesView.this.selTipo = CloudQueryOrigin.SEARCH_IMMOBILI;
                    }
                }
            });
            QueryFilesView.this.cvTipiRichieste.setContentProvider((IContentProvider)new IStructuredContentProvider(){

                public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
                }

                public void dispose() {
                }

                public Object[] getElements(Object inputElement) {
                    return Arrays.stream((CloudQueryOrigin[])CloudQueryOrigin.class.getEnumConstants()).map(Enum::name).toArray(String[]::new);
                }
            });
            QueryFilesView.this.cvTipiRichieste.setLabelProvider((IBaseLabelProvider)new LabelProvider(){

                public String getText(Object element) {
                    return (String)element;
                }
            });
            ImageHyperlink ihConferma = new ImageHyperlink(c, 64);
            ihConferma.setImage(Activator.getImageDescriptor("icons/button_cancel12.png").createImage());
            ihConferma.setHoverImage(Activator.getImageDescriptor("icons/button_cancel12.png").createImage());
            ihConferma.addMouseListener(new MouseListener(){

                public void mouseUp(MouseEvent e) {
                    QueryFilesView.this.cvTipiRichieste.setInput(new Object());
                    QueryFilesView.this.selTipo = null;
                }

                public void mouseDown(MouseEvent e) {
                }

                public void mouseDoubleClick(MouseEvent e) {
                }
            });
            QueryFilesView.this.cvTipiRichieste.setInput(new Object());
            return c;
        }
    }
	
	
    public class serverAliveLabel
    extends ControlContribution {
        private StyledText lStatusServer;
        private Composite c;

        public serverAliveLabel(String id) {
            super(id);
            this.lStatusServer = null;
            this.c = null;
        }

        protected Control createControl(Composite parent) {
            this.c = new Composite(parent, 0);
            GridData gdExpVH = new GridData();
            gdExpVH.grabExcessHorizontalSpace = true;
            gdExpVH.grabExcessVerticalSpace = true;
            gdExpVH.horizontalAlignment = 4;
            gdExpVH.verticalAlignment = 4;
            this.c.setLayout((Layout)new GridLayout());
            this.c.setLayoutData((Object)gdExpVH);
            this.lStatusServer = new StyledText(this.c, 0x1800000);
            this.lStatusServer.setEditable(false);
            this.lStatusServer.setForeground(new Color(null, 255, 255, 255));
            this.lStatusServer.setText("Server non attivo");
            this.lStatusServer.setLineAlignment(0, 1, 0x1000000);
            StyleRange styleRange = new StyleRange();
            styleRange.start = 0;
            styleRange.length = this.lStatusServer.getText().length();
            styleRange.fontStyle = 1;
            styleRange.foreground = this.c.getDisplay().getSystemColor(1);
            FontData fd = new FontData();
            fd.setStyle(1);
            Font f = new Font((Device)this.c.getDisplay(), fd);
            this.lStatusServer.setFont(f);
            this.lStatusServer.setStyleRange(styleRange);
            this.lStatusServer.setBackground(new Color(null, 255, 0, 0));
            return this.c;
        }

        public void setActiveLabel() {
            this.c.getShell().getDisplay().asyncExec(new Runnable(){

                @Override
                public void run() {
                    serverAliveLabel.this.lStatusServer.setText("Server attivo");
                    serverAliveLabel.this.lStatusServer.setLineAlignment(0, 1, 0x1000000);
                    serverAliveLabel.this.lStatusServer.setBackground(new Color(null, 0, 255, 0));
                }
            });
        }

        public void setNotActiveLabel() {
            this.c.getShell().getDisplay().asyncExec(new Runnable(){

                @Override
                public void run() {
                    serverAliveLabel.this.lStatusServer.setText("Server non attivo");
                    serverAliveLabel.this.lStatusServer.setLineAlignment(0, 1, 0x1000000);
                    serverAliveLabel.this.lStatusServer.setBackground(new Color(null, 255, 0, 0));
                }
            });
        }
    }	

}
