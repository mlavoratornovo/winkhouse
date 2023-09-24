package winkhouse.view.report;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.PojoProperties;
//import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
//import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.report.CancellaReport;
import winkhouse.action.report.ExportReportAction;
import winkhouse.action.report.OpenTemplateAction;
import winkhouse.action.report.ReloadReportMarkersAction;
import winkhouse.action.report.SalvaReport;
import winkhouse.dao.ReportDAO;
import winkhouse.dialogs.custom.ReportParamsCellEditor;
import winkhouse.dialogs.custom.ReportParamsDescriptionCellEditor;
import winkhouse.engine.report.ReportEngine;
import winkhouse.model.AffittiModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.ReportModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.ObjectSearchGetters;
import winkhouse.vo.ReportMarkersVO;
import winkhouse.vo.ReportVO;



public class DettaglioReportView extends ViewPart {

	public final static String ID = "winkhouse.dettaglioreport";
	
	private ComboViewer cbvTipo = null;
	private Text tNome = null;
	private Text tTemplatePath = null;
	private Text tDescrizione = null;
	private TableViewer tvAssociazioni = null;
	private ScrolledForm f = null;
	private ReportParamsCellEditor tceParams = null;
	private ReportParamsDescriptionCellEditor tceParamsDesc = null;
	private String[] desGetterImmobili = null;
	private String[] desGetterAnagrafiche = null;
	private String[] desGetterColloqui = null;
	private String[] desGetterAppuntamneti = null;
	private String[] desGetterAffitti = null;
	private ReportDAO reportDAO = null;
	private ReportModel report = null;
	private Button bisLista = null;
	private ImageHyperlink ihConfermaTR = null;
	private ImageHyperlink ihCancella = null;
	
	private boolean isInCompareMode = false;

	private SalvaReport salvaReport = null;
	private CancellaReport cancellaReport = null;		
	private ExportReportAction exportReportAction = null;
	private ReloadReportMarkersAction reloadReportMarkersAction = null;		
	private OpenTemplateAction ota = null;	

	
	public DettaglioReportView() {
	}
	
	private Comparator<WinkhouseUtils.ObjectSearchGetters> comparatorSearchGetters = new Comparator<WinkhouseUtils.ObjectSearchGetters>(){

		@Override
		public int compare(ObjectSearchGetters arg0,
						   ObjectSearchGetters arg1) {
			return arg0.getMethodName().compareToIgnoreCase(arg1.getMethodName());			
		}
		
	};
	
	public void fillDescrizioniGettersImmobili(){						
		Collections.sort(WinkhouseUtils.getInstance().getImmobileReportSearchGetters(), comparatorSearchGetters);
		desGetterImmobili = new String[WinkhouseUtils.getInstance().getImmobileReportSearchGetters().size()];
		Iterator<ObjectSearchGetters> it = WinkhouseUtils.getInstance().getImmobileReportSearchGetters().iterator();
		int count = 0;
		while(it.hasNext()){
			desGetterImmobili[count] = it.next().getDescrizione();
			count++;
		}		
	}
	
	public void fillDescrizioniGettersAnagrafiche(){						
		Collections.sort(WinkhouseUtils.getInstance().getAnagraficheReportSearchGetters(), comparatorSearchGetters);
		desGetterAnagrafiche = new String[WinkhouseUtils.getInstance().getAnagraficheReportSearchGetters().size()];
		Iterator<ObjectSearchGetters> it = WinkhouseUtils.getInstance().getAnagraficheReportSearchGetters().iterator();
		int count = 0;
		while(it.hasNext()){
			desGetterAnagrafiche[count] = it.next().getDescrizione();
			count++;
		}		
	}
	
	public void fillDescrizioniGettersColloqui(){						
		Collections.sort(WinkhouseUtils.getInstance().getColloquiReportSearchGetters(), comparatorSearchGetters);
		desGetterColloqui = new String[WinkhouseUtils.getInstance().getColloquiReportSearchGetters().size()];
		Iterator<ObjectSearchGetters> it = WinkhouseUtils.getInstance().getColloquiReportSearchGetters().iterator();
		int count = 0;
		while(it.hasNext()){
			desGetterColloqui[count] = it.next().getDescrizione();
			count++;
		}		
	}
	
	public void fillDescrizioniGettersAppuntamneti(){						
		Collections.sort(WinkhouseUtils.getInstance().getAppuntamentiReportSearchGetters(), comparatorSearchGetters);
		desGetterAppuntamneti = new String[WinkhouseUtils.getInstance().getAppuntamentiReportSearchGetters().size()];
		Iterator<ObjectSearchGetters> it = WinkhouseUtils.getInstance().getAppuntamentiReportSearchGetters().iterator();
		int count = 0;
		while(it.hasNext()){
			desGetterAppuntamneti[count] = it.next().getDescrizione();
			count++;
		}		
	}

	public void fillDescrizioniGettersAffitti(){						
		Collections.sort(WinkhouseUtils.getInstance().getAffittiReportSearchGetters(),comparatorSearchGetters);
		desGetterAffitti = new String[WinkhouseUtils.getInstance().getAffittiReportSearchGetters().size()];
		Iterator<ObjectSearchGetters> it = WinkhouseUtils.getInstance().getAffittiReportSearchGetters().iterator();
		int count = 0;
		while(it.hasNext()){
			desGetterAffitti[count] = it.next().getDescrizione();
			count++;
		}		
	}
	
	@Override
	public void createPartControl(Composite parent) {
		
		reportDAO = new ReportDAO();
		FormToolkit ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createScrolledForm(parent);
		f.setImage(Activator.getImageDescriptor("/icons/kontact_news.png").createImage());
		f.setText("Dettaglio report");
		GridLayout gl = new GridLayout();
		gl.verticalSpacing = 7;
		
		f.getBody().setLayout(gl);
		ft.paintBordersFor(f.getBody());
		
		IToolBarManager mgr = f.getToolBarManager();
		
		salvaReport = new SalvaReport("Salva report",Activator.getImageDescriptor("icons/document-save.png"));
		mgr.add(salvaReport);
		cancellaReport = new CancellaReport("Cancella report",Activator.getImageDescriptor("icons/edittrash.png"));
		mgr.add(cancellaReport);
		exportReportAction = new ExportReportAction("Esporta report",Activator.getImageDescriptor("icons/export.png"));
		mgr.add(exportReportAction);
		reloadReportMarkersAction = new ReloadReportMarkersAction("Rileggi segnaposti template",Activator.getImageDescriptor("icons/adept_reinstall.png"));
		mgr.add(reloadReportMarkersAction);		
		ota = new OpenTemplateAction("Apri template con OpenOffice",Activator.getImageDescriptor("icons/oowriter.jpeg"));	
		mgr.add(ota);
		f.updateToolBar();
		
		GridData gdH = new GridData();
		gdH.grabExcessHorizontalSpace = true;
		gdH.horizontalAlignment = SWT.FILL;
				
		Label lNome = ft.createLabel(f.getBody(), "Nome");		
		tNome = ft.createText(f.getBody(),"",SWT.DOUBLE_BUFFERED);
		tNome.setLayoutData(gdH);
				
		Label lTipo = ft.createLabel(f.getBody(), "Tipologia report");				
		cbvTipo = new ComboViewer(f.getBody());
		cbvTipo.getCombo().setLayoutData(gdH);
		
		Label lspace = ft.createLabel(f.getBody(), "");
		
		bisLista = ft.createButton(f.getBody(), "lista", SWT.CHECK);
		bisLista.setToolTipText("disabilita la scelta del template ed inserisce una unica associazione, \n " + 
								"non cancellabile per creare una tabella");
		
		bisLista.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				report.setIsList(bisLista.getSelection());
				isListSettings(report.getIsList(),true);
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
				
		Label lPathTemplate = ft.createLabel(f.getBody(), "Percorso template Open Office");
		
		Composite cTemplate = ft.createComposite(f.getBody(), SWT.NONE);
			ft.paintBordersFor(cTemplate);
			GridLayout glTemplate = new GridLayout();
			glTemplate.numColumns = 2;			
			glTemplate.marginHeight = 0;
			glTemplate.marginWidth = 0;
			glTemplate.marginLeft = 2;
			
			GridData gdHTemplate = new GridData();
			gdHTemplate.grabExcessHorizontalSpace = true;
			gdHTemplate.horizontalAlignment = SWT.FILL;
			gdHTemplate.heightHint = 25;
		//	cTemplate.setBackground(new Color(null,255,255,0));
			cTemplate.setLayout(glTemplate);
			cTemplate.setLayoutData(gdHTemplate);
		
			tTemplatePath = ft.createText(cTemplate,"",SWT.DOUBLE_BUFFERED|SWT.READ_ONLY);
			tTemplatePath.setLayoutData(gdH);
		
			ihConfermaTR = ft.createImageHyperlink(cTemplate, SWT.WRAP);		
			ihConfermaTR.setImage(Activator.getImageDescriptor("/icons/fileopen.png").createImage());
			ihConfermaTR.setHoverImage(Activator.getImageDescriptor("/icons/fileopen_over.png").createImage());
			ihConfermaTR.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				FileDialog dd = new FileDialog(f.getShell(),SWT.OPEN);
				String[] filtri = new String[1];
				filtri[0] = "*.odt";				
				dd.setFilterExtensions(filtri);
				
		        dd.setText("Percorso template report");

		        String dir = dd.open();
		        if (dir != null) {
		        	tTemplatePath.setText(dir);
		        	report.setTemplate(tTemplatePath.getText());
		        	ArrayList<ReportMarkersVO> al = new ArrayList<ReportMarkersVO>();
		        	 try {
		        		 ReportEngine re = new ReportEngine();
		        		 ReportEngine.MarkerLoader ml = re.new MarkerLoader(al,tTemplatePath.getText(),report,tvAssociazioni);
		        	       new ProgressMonitorDialog(PlatformUI.getWorkbench()
		        	    		   							   .getActiveWorkbenchWindow()
		        	    		   							   .getShell()).run(false, false, ml);
		        	    } catch (InvocationTargetException e1) {
		        	    } catch (InterruptedException e1) {
		        	    }

/*		        	ReportEngine re = new ReportEngine();
		        	ArrayList<ReportMarkersVO> al = re.getextractReportMarkers(tTemplatePath.getText());
		        	report.setMarkers(al);
		        	tvAssociazioni.setInput(new Object());*/
		        }
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		
		Label lDescrizione = ft.createLabel(f.getBody(), "Descrizione");
	//	lDescrizione.setLayoutData(gdH);
		
		GridData gdDes = new GridData();
		gdDes.grabExcessHorizontalSpace = true;
//		gdDes.grabExcessVerticalSpace = true;
		gdDes.minimumHeight = 50;
		gdDes.heightHint = 150;
		gdDes.horizontalAlignment = SWT.FILL;
		gdDes.verticalAlignment = SWT.FILL;
		
		tDescrizione = ft.createText(f.getBody(), "", SWT.MULTI|SWT.DOUBLE_BUFFERED);
		tDescrizione.setLayoutData(gdDes);
		
		Section section = ft.createSection(f.getBody(), 
   										   Section.DESCRIPTION|Section.TITLE_BAR|
   										   Section.TWISTIE);

		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				f.reflow(true);
			}
			
		});

		GridData gdtabella = new GridData();
		gdtabella.grabExcessHorizontalSpace = true;
		gdtabella.grabExcessVerticalSpace = true;
		gdtabella.horizontalAlignment = SWT.FILL;
		gdtabella.verticalAlignment = SWT.FILL;		

		section.setLayoutData(gdtabella);
		section.setText("Associazioni");
		section.setDescription("Associazione marcatori/campi");
		
		Composite contenitore = ft.createComposite(section,SWT.NONE);
		ft.paintBordersFor(contenitore);
		contenitore.setLayout(new GridLayout());
		contenitore.setLayoutData(gdtabella);
		
		GridData gdass = new GridData();
		gdass.grabExcessHorizontalSpace = true;
		gdass.grabExcessVerticalSpace = true;
		gdass.horizontalAlignment = SWT.FILL;
		gdass.verticalAlignment = SWT.FILL;		
		gdass.minimumHeight = 150;
		
		Composite toolbar = ft.createComposite(contenitore,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));		
		ihCancella = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (tvAssociazioni.getSelection() != null){
					Iterator it = ((StructuredSelection)tvAssociazioni.getSelection()).iterator();
					while (it.hasNext()) {
						ReportMarkersVO rmVO = (ReportMarkersVO)it.next();
						getReport().getMarkers().remove(rmVO);
					}
					tvAssociazioni.refresh();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});				
		
		tvAssociazioni = new TableViewer(contenitore,SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvAssociazioni.getTable().setLayoutData(gdass);
		tvAssociazioni.getTable().setHeaderVisible(true);
		tvAssociazioni.getTable().setLinesVisible(true);
		tvAssociazioni.setContentProvider(new IStructuredContentProvider(){

			@Override
			public void dispose() {
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				
			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (report != null){
					return report.getMarkers().toArray();
				}else{
					return null;
				}
			}
			
		});
		
		tvAssociazioni.setLabelProvider(new ITableLabelProvider(){

			@Override
			public String getColumnText(Object element, int columnIndex) {
				switch (columnIndex){
					case 0: return ((ReportMarkersVO)element).getTipo();
												
					case 1: return ((ReportMarkersVO)element).getNome();
							
					case 2: if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
								WinkhouseUtils.getInstance()
												.findObjectSearchGettersByMethodName(((ReportMarkersVO)element).getGetMethodName(), 
																					 WinkhouseUtils.IMMOBILI, 
																					 WinkhouseUtils.FUNCTION_REPORT);
							}else if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
								WinkhouseUtils.getInstance()
												.findObjectSearchGettersByMethodName(((ReportMarkersVO)element).getGetMethodName(), 
																					 WinkhouseUtils.ANAGRAFICHE, 
																					 WinkhouseUtils.FUNCTION_REPORT);
								
							}else if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
								WinkhouseUtils.getInstance()
												.findObjectSearchGettersByMethodName(((ReportMarkersVO)element).getGetMethodName(), 
																					 WinkhouseUtils.COLLOQUI, 
																					 WinkhouseUtils.FUNCTION_REPORT);
								
							}else if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.APPUNTAMENTI)){
								WinkhouseUtils.getInstance()
												.findObjectSearchGettersByMethodName(((ReportMarkersVO)element).getGetMethodName(), 
																					 WinkhouseUtils.APPUNTAMENTI, 
																					 WinkhouseUtils.FUNCTION_REPORT);
								
							}else if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.AFFITTI)){
								WinkhouseUtils.getInstance()
												.findObjectSearchGettersByMethodName(((ReportMarkersVO)element).getGetMethodName(), 
																					 WinkhouseUtils.AFFITTI, 
																					 WinkhouseUtils.FUNCTION_REPORT);								
							}else{
								return "";
							}
							
					case 3: return ((ReportMarkersVO)element).getParams();
					case 4: return ((ReportMarkersVO)element).getParamsDesc();
							
					default : return "";							
							
				}
				
			}

			@Override
			public void addListener(ILabelProviderListener listener) {}

			@Override
			public void dispose() {}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {}

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			
		});
		
		TableColumn tcTipologiaMarker = new TableColumn(tvAssociazioni.getTable(),SWT.CENTER,0);
		tcTipologiaMarker.setText("Tipologia marker");
		tcTipologiaMarker.setWidth(100);

		TableColumn tcNomeMarker = new TableColumn(tvAssociazioni.getTable(),SWT.CENTER,1);
		tcNomeMarker.setText("Nome marker");
		tcNomeMarker.setWidth(200);

		TableColumn tcCampo = new TableColumn(tvAssociazioni.getTable(),SWT.CENTER,2);
		tcCampo.setText("Campo oggetto");
		tcCampo.setWidth(150);
		TableViewerColumn tcvCampo = new TableViewerColumn(tvAssociazioni,tcCampo);
		tcvCampo.setEditingSupport(new EditingSupport(tvAssociazioni){
			
			@Override
			protected void setValue(Object element, Object value) {
				if (((Integer)value).intValue() > -1){
					String methodstr = null;
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
						methodstr = WinkhouseUtils.getInstance()
						   							.getImmobileReportSearchGetters()
						   							.get((Integer)value)
						   							.getMethodName();																
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
						methodstr = WinkhouseUtils.getInstance()
													.getAnagraficheReportSearchGetters()
													.get((Integer)value)
													.getMethodName();																
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
						methodstr = WinkhouseUtils.getInstance()
													.getColloquiReportSearchGetters()
													.get((Integer)value)
													.getMethodName();																
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.APPUNTAMENTI)){
						methodstr = WinkhouseUtils.getInstance()
													.getAppuntamentiReportSearchGetters()
													.get((Integer)value)
													.getMethodName();
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.AFFITTI)){
						methodstr = WinkhouseUtils.getInstance()
													.getAffittiReportSearchGetters()
													.get((Integer)value)
													.getMethodName();																
					}

					
					if (!methodstr.equalsIgnoreCase(")")){
						((ReportMarkersVO)element).setGetMethodName(methodstr);
					}												
					
					tvAssociazioni.refresh();
				}
				
			}
			
			@Override
			protected Object getValue(Object element) {
				int index = -1; 
				
				if (((ReportMarkersVO)element).getGetMethodName() != null){
					
					WinkhouseUtils.ObjectSearchGetters isg = WinkhouseUtils.getInstance().new ObjectSearchGetters();
					isg.setMethodName(((ReportMarkersVO)element).getGetMethodName());
					
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
						index = Collections.binarySearch(WinkhouseUtils.getInstance().getImmobileReportSearchGetters(), 
														 isg,
														 comparatorSearchGetters);
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
						index = Collections.binarySearch(WinkhouseUtils.getInstance().getAnagraficheReportSearchGetters(), 
														 isg,
														 comparatorSearchGetters);
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
						index = Collections.binarySearch(WinkhouseUtils.getInstance().getColloquiReportSearchGetters(), 
														 isg,
														 comparatorSearchGetters);
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.APPUNTAMENTI)){
						index = Collections.binarySearch(WinkhouseUtils.getInstance().getAppuntamentiReportSearchGetters(), 
														 isg,
														 comparatorSearchGetters);
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.AFFITTI)){
						index = Collections.binarySearch(WinkhouseUtils.getInstance().getAffittiReportSearchGetters(), 
														 isg,
														 comparatorSearchGetters);
					}
					
					
				}
				return index;				
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				ComboBoxCellEditor cbce = null;
				if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
					cbce = new ComboBoxCellEditor(tvAssociazioni.getTable(),
							 					  desGetterImmobili,
							 					  SWT.READ_ONLY|SWT.DROP_DOWN);																	
				}
				if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
					cbce = new ComboBoxCellEditor(tvAssociazioni.getTable(),
							 					  desGetterAnagrafiche,
							 					  SWT.READ_ONLY|SWT.DROP_DOWN);																	
				}
				if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
					cbce = new ComboBoxCellEditor(tvAssociazioni.getTable(),
							 					  desGetterColloqui,
							 					  SWT.READ_ONLY|SWT.DROP_DOWN);																	
				}
				if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.APPUNTAMENTI)){
					cbce = new ComboBoxCellEditor(tvAssociazioni.getTable(),
							 					  desGetterAppuntamneti,
							 					  SWT.READ_ONLY|SWT.DROP_DOWN);																	
				}
				if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.AFFITTI)){
					cbce = new ComboBoxCellEditor(tvAssociazioni.getTable(),
							 					  desGetterAffitti,
							 					  SWT.READ_ONLY|SWT.DROP_DOWN);																	
				}

				return cbce;

			}
			
			@Override
			protected boolean canEdit(Object element) {
				if (report.getIsList()){
					return false;
				}else{
					return true;
				}
			}
		});
		
		tcvCampo.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				if ((((ReportMarkersVO)cell.getElement()).getGetMethodName() != null) && 
						 (!((ReportMarkersVO)cell.getElement()).getGetMethodName().equalsIgnoreCase(""))){
						
						WinkhouseUtils.ObjectSearchGetters isg = WinkhouseUtils.getInstance().new ObjectSearchGetters();
						isg.setMethodName(((ReportMarkersVO)cell.getElement()).getGetMethodName());
						
						if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
							Collections.sort(WinkhouseUtils.getInstance().getImmobileReportSearchGetters(), comparatorSearchGetters);
							int index = Collections.binarySearch(WinkhouseUtils.getInstance().getImmobileReportSearchGetters(), 
													 			 isg,
													 			 comparatorSearchGetters);
							if (index > -1){
								cell.setText(WinkhouseUtils.getInstance().getImmobileReportSearchGetters().get(index).getDescrizione());
							}
						}
						if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
							Collections.sort(WinkhouseUtils.getInstance().getAnagraficheReportSearchGetters(), comparatorSearchGetters);
							int index = Collections.binarySearch(WinkhouseUtils.getInstance().getAnagraficheReportSearchGetters(), 
													 			 isg,
													 			 comparatorSearchGetters);
							if (index > -1){
								cell.setText(WinkhouseUtils.getInstance().getAnagraficheReportSearchGetters().get(index).getDescrizione());
							}
						}
						if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
							Collections.sort(WinkhouseUtils.getInstance().getColloquiReportSearchGetters(), comparatorSearchGetters);
							int index = Collections.binarySearch(WinkhouseUtils.getInstance().getColloquiReportSearchGetters(), 
													 			 isg,
													 			 comparatorSearchGetters);
							if (index > -1){
								cell.setText(WinkhouseUtils.getInstance().getColloquiReportSearchGetters().get(index).getDescrizione());
							}
						}
						if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.APPUNTAMENTI)){
							Collections.sort(WinkhouseUtils.getInstance().getAppuntamentiReportSearchGetters(), comparatorSearchGetters);
							int index = Collections.binarySearch(WinkhouseUtils.getInstance().getAppuntamentiReportSearchGetters(), 
													 			 isg,
													 			 comparatorSearchGetters);
							if (index > -1){
								cell.setText(WinkhouseUtils.getInstance().getAppuntamentiReportSearchGetters().get(index).getDescrizione());
							}
						}
						if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.AFFITTI)){
							Collections.sort(WinkhouseUtils.getInstance().getAffittiReportSearchGetters(), comparatorSearchGetters);
							int index = Collections.binarySearch(WinkhouseUtils.getInstance().getAffittiReportSearchGetters(), 
													 			 isg,
													 			 comparatorSearchGetters);
							if (index > -1){
								cell.setText(WinkhouseUtils.getInstance().getAffittiReportSearchGetters().get(index).getDescrizione());
							}
						}
												
					}else{
						cell.setText("");
					}
				
			}
		});
		
		TableColumn tcParameter = new TableColumn(tvAssociazioni.getTable(),SWT.CENTER,3);
		tcParameter.setText("Parametri selezione");
		tcParameter.setWidth(200);
		section.setClient(contenitore);
		
		TableViewerColumn tvcParameter = new TableViewerColumn(tvAssociazioni, tcParameter);
		tvcParameter.setEditingSupport(new EditingSupport(tvAssociazioni) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((ReportMarkersVO)element).setParams(String.valueOf(value));
				tvAssociazioni.refresh();
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((ReportMarkersVO)element).getParams();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				//if (tceParams == null){
					tceParams = new ReportParamsCellEditor(tvAssociazioni.getTable(),((ReportMarkersVO)element).getParams());
			/*		tceParams.setValidator(new ICellEditorValidator() {
						
						@Override
						public String isValid(Object value) {
							System.out.println(value);
							return null;
						}
					});*/
			//	}
				return tceParams;
			}
			
			@Override
			protected boolean canEdit(Object element) {
				
				boolean returnValue = true;
				Class c = null;
				if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
					c = WinkhouseUtils.getInstance()
									    .getReturnTypeGetterMethod(ImmobiliModel.class, ((ReportMarkersVO)element).getGetMethodName());
				}
				if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
					c = WinkhouseUtils.getInstance()
									    .getReturnTypeGetterMethod(AnagraficheModel.class, ((ReportMarkersVO)element).getGetMethodName());					
				}
				if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
					c = WinkhouseUtils.getInstance()
									    .getReturnTypeGetterMethod(ColloquiModel.class, ((ReportMarkersVO)element).getGetMethodName());					
				}				
				if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.APPUNTAMENTI)){
					c = WinkhouseUtils.getInstance()
									    .getReturnTypeGetterMethod(AppuntamentiModel.class, ((ReportMarkersVO)element).getGetMethodName());					
				}				
				if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.AFFITTI)){
					c = WinkhouseUtils.getInstance()
									    .getReturnTypeGetterMethod(AffittiModel.class, ((ReportMarkersVO)element).getGetMethodName());					
				}				
				
				if ((c == null) && (report.getIsList())){
					returnValue = true;
				}else if (!c.getName().equalsIgnoreCase(ArrayList.class.getName())){
					returnValue = false;
				}				
				
				return returnValue;
			}
		});
		tvcParameter.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() != null){
					cell.setText(((ReportMarkersVO)cell.getElement()).getParams());
				}else{
					cell.setText("");
				}
				
			}
		});
		
		TableColumn tcParameterDescrizione = new TableColumn(tvAssociazioni.getTable(),SWT.CENTER,4);
		tcParameterDescrizione.setText("Parametri descrizione");
		tcParameterDescrizione.setWidth(150);
		
		TableViewerColumn tcvParameterDescrizione = new TableViewerColumn(tvAssociazioni, tcParameterDescrizione);
		tcvParameterDescrizione.setEditingSupport(new EditingSupport(tvAssociazioni) {
			
			@Override
			protected void setValue(Object element, Object value) {
				ObjectSearchGetters osg = WinkhouseUtils.getInstance()
				  										  .findObjectSearchGettersByMethodName(((ReportMarkersVO)element).getGetMethodName(),
				  												  							   getReport().getTipo(), 
				  												  							   WinkhouseUtils.FUNCTION_REPORT);
				if (osg != null){				
					((ReportMarkersVO)element).setParamsDesc((WinkhouseUtils.getInstance()
												 							  .translateNameToMethodNames((String)value,
												 									  					  osg.getParametrizedTypeName(),
												 									  					  WinkhouseUtils.FUNCTION_REPORT)));
				}else if (report.getIsList()){
					((ReportMarkersVO)element).setParamsDesc((WinkhouseUtils.getInstance()
							  				  								  .translateNameToMethodNames((String)value,
							  				  										  					  report.getTipo(),
							  				  										  					  WinkhouseUtils.FUNCTION_REPORT)));					
				}else{
					((ReportMarkersVO)element).setParamsDesc("");
				}

				tvAssociazioni.refresh();
			}
			
			@Override
			protected Object getValue(Object element) {
				
				ReportDAO rDAO = new ReportDAO();
				ReportModel rm = getReport();
				String returnValue = "";
				ObjectSearchGetters osg = null;
				
				if (rm.getTipo().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
					osg = WinkhouseUtils.getInstance()
										  .findObjectSearchGettersByMethodName(((ReportMarkersVO)element).getGetMethodName(), 
																		 	   WinkhouseUtils.IMMOBILI, 
																		 	   WinkhouseUtils.FUNCTION_REPORT);
				}else if (rm.getTipo().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
					osg = WinkhouseUtils.getInstance()
					  					  .findObjectSearchGettersByMethodName(((ReportMarkersVO)element).getGetMethodName(), 
					  												  			WinkhouseUtils.ANAGRAFICHE, 
					  												  			WinkhouseUtils.FUNCTION_REPORT);
				}else if (rm.getTipo().equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
					osg = WinkhouseUtils.getInstance()
					  					  .findObjectSearchGettersByMethodName(((ReportMarkersVO)element).getGetMethodName(), 
												  							   WinkhouseUtils.COLLOQUI, 
												  							   WinkhouseUtils.FUNCTION_REPORT);					
				}else if (rm.getTipo().equalsIgnoreCase(WinkhouseUtils.APPUNTAMENTI)){
					osg = WinkhouseUtils.getInstance()
					  					  .findObjectSearchGettersByMethodName(((ReportMarkersVO)element).getGetMethodName(), 
							  							   					   WinkhouseUtils.APPUNTAMENTI, 
							  							   					   WinkhouseUtils.FUNCTION_REPORT);
				}else{					
					osg = WinkhouseUtils.getInstance()
						  				  .findObjectSearchGettersByMethodName(((ReportMarkersVO)element).getGetMethodName(), 
													  						   WinkhouseUtils.AFFITTI, 
													  						   WinkhouseUtils.FUNCTION_REPORT);
				}
				String elementsType = null;
				if (osg != null){
					elementsType = osg.getParametrizedTypeName();
				}else{
					if ((rm.getTipo() == null) || (rm.getTipo().equalsIgnoreCase(""))){
						MessageDialog.openError(PlatformUI.getWorkbench()
														  .getActiveWorkbenchWindow()
														  .getShell(), "Attenzione", "Selezionare la tipologia del report");	
					}else{
						elementsType = rm.getTipo();
					}					
				}
				if (elementsType != null ){
					returnValue = WinkhouseUtils.getInstance()
						  						  .translateMethodNamesToNames(((ReportMarkersVO)element).getParamsDesc(), 
						  								  					   elementsType, 
						  								  					   WinkhouseUtils.FUNCTION_REPORT);
				}
								
				return returnValue;
 
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				ReportModel rm = getReport();
				ArrayList al = null;
				String entita = null;
				ObjectSearchGetters osg = WinkhouseUtils.getInstance()
				  										  .findObjectSearchGettersByMethodName(((ReportMarkersVO)element).getGetMethodName(),
				  												  							   rm.getTipo(), 
				  												  							   WinkhouseUtils.FUNCTION_REPORT);
				
				if (osg != null){
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
						  al = WinkhouseUtils.getInstance()
	  	  				  					   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  				  							  				 WinkhouseUtils.IMMOBILI, 
		  				  							  				 WinkhouseUtils.FUNCTION_REPORT);					  
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  					   						   				 WinkhouseUtils.ANAGRAFICHE, 
		  					   						   				 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.COLLOQUI, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.CONTATTI)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.CONTATTI, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.AGENTICOLLOQUIO)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.AGENTICOLLOQUIO, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.ALLEGATICOLLOQUIO)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.ALLEGATICOLLOQUIO, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHECOLLOQUIO)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.ANAGRAFICHECOLLOQUIO, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.CRITERIRICERCA)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.CRITERIRICERCA, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.IMMAGINI)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.IMMAGINI, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.STANZEIMMOBILI)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.STANZEIMMOBILI, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.ALLEGATIIMMOBILE)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.ALLEGATIIMMOBILE, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.ABBINAMENTI)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.ABBINAMENTI, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.AFFITTIRATE)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.AFFITTIRATE, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.AFFITTISPESE)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.AFFITTISPESE, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.AFFITTIANAGRAFICHE)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.AFFITTIANAGRAFICHE, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.DATICATASTALI)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				 .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.DATICATASTALI, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.TIPIAPPUNTAMENTI)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				 .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.TIPIAPPUNTAMENTI, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.CLASSIENERGETICHE)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				 .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.CLASSIENERGETICHE, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					if (osg.getParametrizedTypeName().equalsIgnoreCase(WinkhouseUtils.CAMPIPERSONALI)){
						
						  al = WinkhouseUtils.getInstance()
		  					   				 .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  							  				                 WinkhouseUtils.CAMPIPERSONALI, 
		  							  				                 WinkhouseUtils.FUNCTION_REPORT);
					}
					
					
					tceParamsDesc = new ReportParamsDescriptionCellEditor(tvAssociazioni.getTable(),
							  											  al,
							  											  osg.getParametrizedTypeName());

				}else{
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  					   						   				 WinkhouseUtils.IMMOBILI, 
		  					   						   				 WinkhouseUtils.FUNCTION_REPORT);					  
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  					   						   				 WinkhouseUtils.ANAGRAFICHE, 
		  					   						   				 WinkhouseUtils.FUNCTION_REPORT);					  
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  					   						   				 WinkhouseUtils.COLLOQUI, 
		  					   						   				 WinkhouseUtils.FUNCTION_REPORT);					  
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.AFFITTI)){
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  					   						   				 WinkhouseUtils.AFFITTI, 
		  					   						   				 WinkhouseUtils.FUNCTION_REPORT);					  
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.APPUNTAMENTI)){
						  al = WinkhouseUtils.getInstance()
		  					   				   .getObjFromStringList(((ReportMarkersVO)element).getParamsDesc(), 
		  					   						   				 WinkhouseUtils.APPUNTAMENTI, 
		  					   						   				 WinkhouseUtils.FUNCTION_REPORT);					  
					}
					
					tceParamsDesc = new ReportParamsDescriptionCellEditor(tvAssociazioni.getTable(),
							  											  al,
							  											  report.getTipo());

				}
				
				return tceParamsDesc;
			}
			
			@Override
			protected boolean canEdit(Object element) {
				
				boolean returnValue = true;
				Class c = null;
				
				if (!((ReportMarkersVO)element).getGetMethodName().equalsIgnoreCase("")){
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){
						c = WinkhouseUtils.getInstance()
										    .getReturnTypeGetterMethod(ImmobiliModel.class, ((ReportMarkersVO)element).getGetMethodName());
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
						c = WinkhouseUtils.getInstance()
										    .getReturnTypeGetterMethod(AnagraficheModel.class, ((ReportMarkersVO)element).getGetMethodName());					
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
						c = WinkhouseUtils.getInstance()
										    .getReturnTypeGetterMethod(ColloquiModel.class, ((ReportMarkersVO)element).getGetMethodName());					
					}
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.APPUNTAMENTI)){
						c = WinkhouseUtils.getInstance()
										    .getReturnTypeGetterMethod(AppuntamentiModel.class, ((ReportMarkersVO)element).getGetMethodName());					
					}				
					if (report.getTipo().equalsIgnoreCase(WinkhouseUtils.AFFITTI)){
						c = WinkhouseUtils.getInstance()
										    .getReturnTypeGetterMethod(AffittiModel.class, ((ReportMarkersVO)element).getGetMethodName());					
					}				
					
					if ((c == null) && (report.getIsList())){
						returnValue = true;
					}else{
						if (c.getName().equalsIgnoreCase(ArrayList.class.getName())){
							returnValue = true;
						}				
						if ((c.getPackage().getName().equalsIgnoreCase("winkhouse.vo")) || 
							(c.getPackage().getName().equalsIgnoreCase("winkhouse.model"))){
							returnValue = true;
						}
					}
				}
				return returnValue;
			}
		});
		tcvParameterDescrizione.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() != null){
					if ((getReport().getTipo() != null) ||
						(getReport().getTipo().equalsIgnoreCase(""))){
						
						ObjectSearchGetters osg = WinkhouseUtils.getInstance()
																  .findObjectSearchGettersByMethodName(((ReportMarkersVO)cell.getElement()).getGetMethodName(),
																		     						   getReport().getTipo(), 
																		     						   WinkhouseUtils.FUNCTION_REPORT);
						if (osg != null){				
							cell.setText(WinkhouseUtils.getInstance()
													 	 .translateMethodNamesToNames(((ReportMarkersVO)cell.getElement()).getParamsDesc(),
															 					  	  osg.getParametrizedTypeName(),
															 					  	  WinkhouseUtils.FUNCTION_REPORT));
						}else if (report.getIsList()){
							cell.setText(WinkhouseUtils.getInstance()
													 	 .translateMethodNamesToNames(((ReportMarkersVO)cell.getElement()).getParamsDesc(),
															 					  	  report.getTipo(),
															 					  	  WinkhouseUtils.FUNCTION_REPORT));							
						}else{
							cell.setText("");
						}
					}else{
						cell.setText("");
					}
					
				}else{
					cell.setText("");
				}
				
			}
		});
		
		section.setClient(contenitore);		
		fillDescrizioniGettersAnagrafiche();
		fillDescrizioniGettersImmobili();
		fillDescrizioniGettersColloqui();
		fillDescrizioniGettersAppuntamneti();
		fillDescrizioniGettersAffitti();
		f.reflow(true);
	}
		
	private void bindDatiReport(DataBindingContext bindingContext){
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tDescrizione), 
								 PojoProperties.value("descrizione").observe(report),
								 null, 
								 null);

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tNome), 
								 PojoProperties.value("nome").observe(report),
								 null, 
								 null);
		
		cbvTipo.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {				
				return ReportVO.tipireport;
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,Object newInput) {
			}
			
		});
		
		cbvTipo.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				return ((WinkhouseUtils.ReportTypes)element).getReportTypeName();
			}
			
		});
		
		cbvTipo.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				report.setTipo(((WinkhouseUtils.ReportTypes)((StructuredSelection)event.getSelection()).getFirstElement()).getClassName());				
			}
			
		});
						
		cbvTipo.setInput(new Object());
		if ((report.getTipo() != null) &&
			(!report.getTipo().equalsIgnoreCase(""))){
			for (int i = 0; i < ReportVO.tipireport.length; i++) {
				if (ReportVO.tipireport[i].getClassName().equalsIgnoreCase(report.getTipo())){
					StructuredSelection ss = new StructuredSelection(ReportVO.tipireport[i]);
					cbvTipo.setSelection(ss);					
				}
			}			
		}
		
		if ((report.getTemplate() != null) && (!report.getTemplate().equalsIgnoreCase(""))){
			tTemplatePath.setText(report.getTemplate());			
		}
		
		if ((report.getNome() != null) && (!report.getNome().equalsIgnoreCase(""))){
			tNome.setText(report.getNome());			
		}
				
		bisLista.setSelection(report.getIsList());
		tvAssociazioni.setInput(new Object());
		isListSettings(report.getIsList(),false);
	}

	@Override
	public void setFocus() {
	}

	public ReportModel getReport() {
		return report;
	}

	public void setReport(ReportModel report) {
		isInCompareMode = false;
		this.report = report;
		if ((this.report.getNome() == null) || (this.report.getNome().equalsIgnoreCase(""))){
			setPartName("Nuovo report");
		}else{
			setPartName(this.report.getNome());
		}
		bindDatiReport(new DataBindingContext());
	}

	private void isListSettings(Boolean islist,boolean byCheckbox){
		if (islist){
			report.setTemplate("");
			tTemplatePath.setText("");
			ihConfermaTR.setEnabled(false);
			ihCancella.setEnabled(false);
			if (byCheckbox){
				ReportMarkersVO rm = new ReportMarkersVO();
				rm.setCodReport(report.getCodReport());
				rm.setTipo(ReportEngine.MARKELEMENT_TAGTYPENAME_TABLE);
				ArrayList<ReportMarkersVO> al = new ArrayList<ReportMarkersVO>();
				al.add(rm);
				report.setMarkers(al);
			}
			tvAssociazioni.refresh();
		}else{
			ihConfermaTR.setEnabled(true);
			ihCancella.setEnabled(true);
			tvAssociazioni.refresh();
		}
	}

	public void setCompareView(boolean enabled){
		
		cbvTipo.getCombo().setEnabled(enabled);
		tNome.setEditable(enabled);
		tTemplatePath.setEditable(enabled);
		tDescrizione.setEditable(enabled);
		tvAssociazioni.getTable().setEnabled(enabled);
		bisLista.setEnabled(enabled);
		ihConfermaTR.setEnabled(enabled);
		ihCancella.setEnabled(enabled);
		
		salvaReport.setEnabled(enabled);
		cancellaReport.setEnabled(enabled);		
		exportReportAction.setEnabled(enabled);
		reloadReportMarkersAction.setEnabled(enabled);		
		ota.setEnabled(enabled);	
		
		isInCompareMode = true;

	}

}
