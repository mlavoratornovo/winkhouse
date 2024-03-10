package winkhouse.xmlser.wizard.exporter.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.configuration.EnvSettingsFactory;
import winkhouse.dialogs.custom.SWTCalendarDialog;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.export.helpers.ExportedRicercheHelper;
import winkhouse.export.helpers.UtilsHelper;
import winkhouse.export.models.ObjectSearchGetters;
import winkhouse.helper.RicercheHelper;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.RicercheModel;
import winkhouse.orm.Agenti;
import winkhouse.orm.Riscaldamenti;
import winkhouse.orm.Statoconservativo;
import winkhouse.orm.Tipologieimmobili;
import winkhouse.util.ImmobiliAffittiMethodName;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.RicercheVO;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.vo.TipologieImmobiliVO;
import winkhouse.wizard.PopUpEditRicerca;
import winkhouse.wizard.PopUpRicercaRicerche;
import winkhouse.Activator;
import winkhouse.xmlser.wizard.exporter.ExporterWizard;



public class ListaCriteriImmobiliAffitti extends WizardPage {

	private Composite container = null;
	private TableViewer tvCriteri = null;
	private String[] desGetters = null;
	private String[] desTipologiaImmobile = null;
	private Integer[] codTipologiaImmobile = null;
	private String[] desStatoConservativo = null;
	private Integer[] codStatoConservativo = null;
	private String[] desRiscaldamenti = null;
	private Integer[] codRiscaldamenti = null;
	private String[] desAgenti = null;
	private Integer[] codAgenti = null;
	private Label lRicercaSelectedName = null;
	private RicercheModel ricerca = null;
	
	public ListaCriteriImmobiliAffitti(String pageName) {
		super(pageName);
	}

	public ListaCriteriImmobiliAffitti(String pageName, String title,
								ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}
	
	private Comparator<ObjectSearchGetters> comparatorImmobileAffittiSearchGetters = new Comparator<ObjectSearchGetters>(){

		@Override
		public int compare(ObjectSearchGetters arg0,
						   ObjectSearchGetters arg1) {
			return arg0.getMethodName().compareToIgnoreCase(arg1.getMethodName());			
		}
		
	};

	private Comparator c = new Comparator<String>(){

		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
		
	};
	
	Comparator<CriteriRicercaModel> cMethodName = new Comparator<CriteriRicercaModel>(){

		@Override
		public int compare(CriteriRicercaModel o1, CriteriRicercaModel o2){
			return o1.getGetterMethodName().compareTo(o2.getGetterMethodName());			
		}
		
	};

	private Comparator<CriteriRicercaModel> comparatorLineNumber = new Comparator<CriteriRicercaModel>(){

		@Override
		public int compare(CriteriRicercaModel o1, CriteriRicercaModel o2) {
			int returnValue=0;
			if (o1.getLineNumber().intValue() > o2.getLineNumber().intValue()){
				return 1;
			}else if (o1.getLineNumber().intValue() < o2.getLineNumber().intValue()){
				return -1;
			}
			return 0;
			
		}
		
	};

	private boolean checkPeriodoAffittoExist(ArrayList criteria){
		boolean returnValue = false;
		
		Collections.sort(criteria, cMethodName);
		
		CriteriRicercaModel ccrKey = new CriteriRicercaModel();
		ccrKey.setGetterMethodName(ImmobiliAffittiMethodName.PERIODOAFFITTO);
		
		int index = Collections.binarySearch(criteria, ccrKey, cMethodName);
		
		returnValue = (index > -1)?true:false; 
		return returnValue;
	}
	
	@Override
	public void createControl(Composite parent) {
				
		setTitle(((ExporterWizard)getWizard()).getVersion());
		
		fillDescrizioniGetters();
		fillDescrizioniRiscaldamenti();
		fillDescrizioniStatiConservativi();
		fillDescrizioniTipologieImmobili();
		fillDescrizioniAgenti();
		loadCriteriColloquio();
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		container = new Composite(parent,SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(gdFillHV);
		
		Composite toolbar = new Composite(container,SWT.NONE);
		toolbar.setLayout(new GridLayout(9,false));
		GridData gdtoolbar = new GridData();
		gdtoolbar.grabExcessHorizontalSpace = true;
		gdtoolbar.horizontalAlignment = SWT.FILL;
		toolbar.setLayoutData(gdtoolbar);		
		
		ImageHyperlink ihNew = new ImageHyperlink(toolbar, SWT.WRAP);	
		ihNew.setToolTipText("nuovo criterio");
		ihNew.setImage(Activator.getImageDescriptor("/icons/wizardexport/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/filenewhover.png").createImage());
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if ((((ExporterWizard)getWizard()).getExporterVO()
												 .getCriteriRicerca()
												 .size() == 0) ||
					(((ColloquiCriteriRicercaModel)((ExporterWizard)getWizard()).getExporterVO()
							 											    .getCriteriRicerca()
							 											    .get(((ExporterWizard)getWizard()).getExporterVO()
							 											    								 .getCriteriRicerca()
							 											    								 .size() - 1))
							 											    								 .getGetterMethodName() != null)){
					CriteriRicercaModel crVO = new CriteriRicercaModel();
					ColloquiCriteriRicercaModel crModel = new ColloquiCriteriRicercaModel(crVO);
					((ExporterWizard)getWizard()).getExporterVO()
					 							.getCriteriRicerca().add(crModel);					
					reloadLineNumber();
	/*				Collections.sort(((ExporterWizard)getWizard()).getExporterVO()
							 									 .getCriteriRicerca(), 
							 		 comparatorLineNumber);*/
					tvCriteri.refresh();
					TableItem ti = tvCriteri.getTable().getItem(tvCriteri.getTable().getItemCount()-1);
					Object[] sel = new Object[1];
					sel[0] = ti.getData();

					StructuredSelection ss = new StructuredSelection(sel);
					
					tvCriteri.setSelection(ss, true);

					Event ev = new Event();
					ev.item = ti;
					ev.data = ti.getData();
					ev.widget = tvCriteri.getTable();
					tvCriteri.getTable().notifyListeners(SWT.Selection, ev);

					
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = new ImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/wizardexport/edittrash.png").createImage());
		ihCancella.setToolTipText("cancella criterio");
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/edittrash_hover.png").createImage());
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				StructuredSelection ss = (StructuredSelection)tvCriteri.getSelection();
				if ((ss != null) && (ss.getFirstElement() != null)){
					((ExporterWizard)getWizard()).getExporterVO()
					 							.getCriteriRicerca()
					 							.remove((CriteriRicercaModel)ss.getFirstElement());
					reloadLineNumber();
//					Collections.sort(((ExporterWizard)getWizard()).getExporterVO()
	//						 									 .getCriteriRicerca(), 
		//					 		 comparatorLineNumber);					
					tvCriteri.refresh();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		ImageHyperlink ihCheck = new ImageHyperlink(toolbar, SWT.WRAP);		
		ihCheck.setImage(Activator.getImageDescriptor("/icons/wizardexport/spellcheck.png").createImage());
		ihCheck.setToolTipText("controllo sintassi");
		ihCheck.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/spellcheck_over.png").createImage());
		ihCheck.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (((ExporterWizard)getWizard()).checkCriteriSyntax(true,ImmobiliModel.class.getName())){
					SearchEngineImmobili sei = new SearchEngineImmobili(((ExporterWizard)getWizard()).getExporterVO()
							 																		.getCriteriRicerca());
					if (sei.verifyQuery()){
						MessageBox mb = new MessageBox(getWizard().getContainer().getShell(),SWT.ICON_INFORMATION);
						mb.setText("Interrogazione dati");
						mb.setMessage("Interrogazione generata corretta");			
						mb.open();

					}else{
						MessageBox mb = new MessageBox(getWizard().getContainer().getShell(),SWT.ICON_ERROR);
						mb.setText("Interrogazione dati");
						mb.setMessage("Interrogazione generata non corretta");			
						mb.open();						
					}
				}				
				((ExporterWizard)getWizard()).getContainer().updateButtons();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		Label lseparator = new Label(toolbar,SWT.FLAT);
		lseparator.setImage(Activator.getImageDescriptor("icons/wizardexport/separator.png").createImage());
		
		ImageHyperlink ihOpen = new ImageHyperlink(toolbar, SWT.WRAP);		
		ihOpen.setImage(Activator.getImageDescriptor("/icons/wizardexport/fileopen.png").createImage());
		ihOpen.setToolTipText("apri ricerca salvata");
		ihOpen.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/fileopen_over.png").createImage());
		ihOpen.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				PopUpRicercaRicerche puRR = new PopUpRicercaRicerche(RicercheVO.RICERCHE_IMMOBILI_AFFITTI,
													 				 (ListaCriteriImmobiliAffitti)((ExporterWizard)getWizard()).getPage("Lista criteri selezione immobili per affitto"),
													 				 "setRicerca");
				
				((ExporterWizard)getWizard()).getContainer().updateButtons();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		

		ImageHyperlink ihSave = new ImageHyperlink(toolbar, SWT.WRAP);		
		ihSave.setImage(Activator.getImageDescriptor("/icons/wizardexport/document-save.png").createImage());
		ihSave.setToolTipText("salva ricerca");
		ihSave.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/document-save_over.png").createImage());
		ihSave.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if ((((ExporterWizard)getWizard()).getExporterVO()
												 .getCriteriRicerca() != null) &&
					(((ExporterWizard)getWizard()).getExporterVO()
												 .getCriteriRicerca().size() != 0)){
					
					PopUpEditRicerca puER = new PopUpEditRicerca();
					getRicerca().setTipo(RicercheVO.RICERCHE_IMMOBILI_AFFITTI);
					puER.setRicerca(getRicerca());
					((ExporterWizard)getWizard()).getContainer().updateButtons();					
				}else{
					MessageDialog.openWarning(PlatformUI.getWorkbench()
  														.getActiveWorkbenchWindow()
  														.getShell(), 
  											  "Salvataggio ricerca", 
											  "Inserire almeno un criterio");							
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		

		ImageHyperlink ihCancellaRicerca = new ImageHyperlink(toolbar, SWT.WRAP);		
		ihCancellaRicerca.setImage(Activator.getImageDescriptor("/icons/wizardexport/edittrash.png").createImage());
		ihCancellaRicerca.setToolTipText("cancella ricerca");
		ihCancellaRicerca.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/edittrash_hover.png").createImage());
		ihCancellaRicerca.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (ricerca != null){
					ExportedRicercheHelper rh = new ExportedRicercheHelper();
					if (!rh.deleteRicerca(ricerca)){
						MessageDialog.openError(PlatformUI.getWorkbench()
														  .getActiveWorkbenchWindow()
														  .getShell(), 
												"Errore cancellazione ricerca", 
												"Si � verificato un errore nella cancellazione della ricerca");		
					}else{
						MessageDialog.openInformation(PlatformUI.getWorkbench()
								  								.getActiveWorkbenchWindow()
								  								.getShell(), 
								  					  "Cancellazione ricerca", 
													  "cancellazione ricerca eseguita con successo");		

						lRicercaSelectedName.setText("");
						ricerca = null;
					}
				}else{
					MessageDialog.openWarning(PlatformUI.getWorkbench()
							  							.getActiveWorkbenchWindow()
							  							.getShell(), 
							  				  "Selezione ricerca", 
											  "Selezionare la ricerca da cancellare");		
					
				}

				((ExporterWizard)getWizard()).getContainer().updateButtons();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		
		
		Label lRicercaSelected = new Label(toolbar, SWT.FLAT);
		lRicercaSelected.setText("Ricerca selezionata : ");
		
		lRicercaSelectedName = new Label(toolbar, SWT.FLAT);
		GridData gdRicercaSelected = new GridData();
		gdRicercaSelected.grabExcessHorizontalSpace = true;
		gdRicercaSelected.horizontalAlignment = SWT.FILL;
		lRicercaSelectedName.setLayoutData(gdRicercaSelected);		
		
		
		tvCriteri = new TableViewer(container,SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		tvCriteri.getTable().setLayoutData(gdFillHV);
		tvCriteri.getTable().setHeaderVisible(true);
		tvCriteri.getTable().setLinesVisible(true);
		tvCriteri.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return ((ExporterWizard)getWizard()).getExporterVO()
				 								   .getCriteriRicerca().toArray();
			}

			@Override
			public void dispose() {
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				
			}
			
		});
		tvCriteri.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
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
		
		TableColumn tcParametro = new TableColumn(tvCriteri.getTable(),SWT.CENTER,0);
		tcParametro.setWidth(100);
		tcParametro.setText("Parametro");
		
		TableViewerColumn tvcParametro = new TableViewerColumn(tvCriteri,tcParametro);
		tvcParametro.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				if ((((CriteriRicercaModel)cell.getElement()).getGetterMethodName() != null) && 
					 (!((CriteriRicercaModel)cell.getElement()).getGetterMethodName().equalsIgnoreCase(""))){
					
					ObjectSearchGetters isg = new ObjectSearchGetters();
					isg.setMethodName(((CriteriRicercaModel)cell.getElement()).getGetterMethodName());
					Collections.sort(UtilsHelper.getInstance().getImmobileAffittiSearchGetters(), comparatorImmobileAffittiSearchGetters);
					int index = Collections.binarySearch(UtilsHelper.getInstance().getImmobileAffittiSearchGetters(), 
											 			 isg,
											 			 comparatorImmobileAffittiSearchGetters);
					if (index > -1){
						cell.setText(UtilsHelper.getInstance().getImmobileAffittiSearchGetters().get(index).getDescrizione());
					}
				}else{
					cell.setText("");
				}
			}
			
		});
		tvcParametro.setEditingSupport(new EditingSupport(tvCriteri){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				ComboBoxCellEditor cbce = new ComboBoxCellEditor(tvCriteri.getTable(),
																 desGetters,
						 										 SWT.READ_ONLY|SWT.DROP_DOWN);												
				return cbce;
			}

			@Override
			protected Object getValue(Object element) {
				int index = -1; 
				
				if (((CriteriRicercaModel)element).getGetterMethodName() != null){
					
					ObjectSearchGetters isg = new ObjectSearchGetters();
					isg.setMethodName(((CriteriRicercaModel)element).getGetterMethodName());
					
					index = Collections.binarySearch(UtilsHelper.getInstance().getImmobileAffittiSearchGetters(), 
										             isg,
										             comparatorImmobileAffittiSearchGetters);
				}
				return index;				
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (((Integer)value).intValue() > -1){
					String methodstr = UtilsHelper.getInstance()
													   .getImmobileAffittiSearchGetters()
													   .get((Integer)value)
													   .getMethodName();
					
					if (((ExporterWizard)getWizard()).getExporterVO()
							 						.getCriteriRicerca().size() == 1){ 
						if (!methodstr.equalsIgnoreCase(")")){
							if (!methodstr.equalsIgnoreCase(ImmobiliAffittiMethodName.PERIODOAFFITTO)){
								((CriteriRicercaModel)element).setGetterMethodName(methodstr);
							}else{
								if (!checkPeriodoAffittoExist(((ExporterWizard)getWizard()).getExporterVO()
										 												  .getCriteriRicerca())){
									((CriteriRicercaModel)element).setGetterMethodName(methodstr);
								}else{
									MessageBox mb = new MessageBox(getWizard().getContainer().getShell(),SWT.ICON_ERROR);
									mb.setText("Periodo affito");
									mb.setMessage("E' possibile inserire un unico periodo di affitto");			
									mb.open();						

								}
							}
						}												
					}else{
						CriteriRicercaModel ccrVO = getPrevCriterioByLineNumber(((CriteriRicercaModel)element).getLineNumber());
						if (methodstr.equalsIgnoreCase(")")){
							if(!ccrVO.getGetterMethodName().equalsIgnoreCase("(")){
								if (!methodstr.equalsIgnoreCase(ImmobiliAffittiMethodName.PERIODOAFFITTO)){
									((CriteriRicercaModel)element).setGetterMethodName(methodstr);
								}else{
									if (!checkPeriodoAffittoExist(((ExporterWizard)getWizard()).getExporterVO()
											 												  .getCriteriRicerca())){
										((CriteriRicercaModel)element).setGetterMethodName(methodstr);
									}else{
										MessageBox mb = new MessageBox(getWizard().getContainer().getShell(),SWT.ICON_ERROR);
										mb.setText("Periodo affito");
										mb.setMessage("E' possibile inserire un unico periodo di affitto");			
										mb.open();						
									}
								}
							}
						}else{
							if (!methodstr.equalsIgnoreCase(ImmobiliAffittiMethodName.PERIODOAFFITTO)){
								((CriteriRicercaModel)element).setGetterMethodName(methodstr);
							}else{
								if (!checkPeriodoAffittoExist(((ExporterWizard)getWizard()).getExporterVO()
										 												  .getCriteriRicerca())){
									((CriteriRicercaModel)element).setGetterMethodName(methodstr);
								}else{
									MessageBox mb = new MessageBox(getWizard().getContainer().getShell(),SWT.ICON_ERROR);
									mb.setText("Periodo affito");
									mb.setMessage("E' possibile inserire un unico periodo di affitto");			
									mb.open();						
								}
							}

						}
					}
					
					tvCriteri.refresh();
					((ExporterWizard)getWizard()).getContainer().updateButtons();
					
				}
			}
			
		});
		
		TableColumn tcValoreDa = new TableColumn(tvCriteri.getTable(),SWT.CENTER,1);
		tcValoreDa.setWidth(100);
		tcValoreDa.setText("Da");
		
		TableViewerColumn tvcValoreDa = new TableViewerColumn(tvCriteri,tcValoreDa);		
		tvcValoreDa.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				CriteriRicercaModel ccrVO = (CriteriRicercaModel)cell.getElement();
				
				if ((!ccrVO.getGetterMethodName().equalsIgnoreCase("(")) &&
						(!ccrVO.getGetterMethodName().equalsIgnoreCase(")"))
					){

					Class methodReturnType = UtilsHelper.getInstance().getReturnTypeGetterMethod(ImmobiliModel.class,ccrVO.getGetterMethodName());
					
					if (methodReturnType != null){
						
						if ((ccrVO.getFromValue() != null)&& (!ccrVO.getFromValue().equalsIgnoreCase(""))){
							
							if (methodReturnType.getName().equalsIgnoreCase(String.class.getName())){
								
								cell.setText(String.valueOf(ccrVO.getFromValue()));
								
							}else if (methodReturnType.getName().equalsIgnoreCase(Integer.class.getName())){
								
								if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODTIPOLOGIA)){
								
									TipologieImmobiliVO tiVO = UtilsHelper.getInstance()
									  											   .findTipologiaByCod(Integer.valueOf(ccrVO.getFromValue()));
		
									cell.setText((tiVO == null)?"":tiVO.getDescrizione());
									
									
								}else if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODSTATO)){
		
									StatoConservativoVO scVO = UtilsHelper.getInstance()
									   											   .findStatoConservativoByCod(Integer.valueOf(ccrVO.getFromValue()));
		
									cell.setText((scVO == null)?"":scVO.getDescrizione());							
									
								}else if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODRISCALDAMENTO)){
									
									RiscaldamentiVO rVO = UtilsHelper.getInstance()
																			  .findRiscaldamentiByCod(Integer.valueOf(ccrVO.getFromValue()));
									
									cell.setText((rVO == null)?"":rVO.getDescrizione());
									
								}else if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODAGENTEINSERITORE)){
																	
									AgentiVO aVO = UtilsHelper.getInstance()
																	   .findAgentiByCod(Integer.valueOf(ccrVO.getFromValue()));
									
									cell.setText((aVO == null)?"":aVO.getCognome() + " " + aVO.getNome());
									
								}else{
									
									try {
										cell.setText(String.valueOf(Integer.valueOf(ccrVO.getFromValue())));
									} catch (NumberFormatException e) {
										cell.setText("");
									}
									
								}
							}else if (methodReturnType.getName().equalsIgnoreCase(Double.class.getName())){
								try {
									cell.setText(String.valueOf(Double.valueOf(ccrVO.getFromValue()).doubleValue()));
								} catch (NumberFormatException e) {
									cell.setText("");
								}
							}else if (methodReturnType.getName().equalsIgnoreCase(Date.class.getName())){
								cell.setText(ccrVO.getFromValue());
							}
						}else{
							cell.setText("");
						}
					}else{
						cell.setText("");
					}
				}
			}
		});
		
		tvcValoreDa.setEditingSupport(new EditingSupport(tvCriteri){

			@Override
			protected boolean canEdit(Object element) {				
				if (
						((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase("(") ||
						((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(")")
					){
					return false;
				}else{
					return true;
				}

			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				CellEditor returnValue = null;
				if (((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODTIPOLOGIA)){
					returnValue = new ComboBoxCellEditor(tvCriteri.getTable(),
							 							 desTipologiaImmobile,
							 							 SWT.READ_ONLY|SWT.DROP_DOWN); 
				}else if (((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODSTATO)){
					returnValue = new ComboBoxCellEditor(tvCriteri.getTable(),
														 desStatoConservativo,
														 SWT.READ_ONLY|SWT.DROP_DOWN); 					
				}else if (((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODRISCALDAMENTO)){
					returnValue = new ComboBoxCellEditor(tvCriteri.getTable(),
														 desRiscaldamenti,
														 SWT.READ_ONLY|SWT.DROP_DOWN); 
				}else if(((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODAGENTEINSERITORE)){
					returnValue = new ComboBoxCellEditor(tvCriteri.getTable(),
							 							 desAgenti,
							 							 SWT.READ_ONLY|SWT.DROP_DOWN);
				}else if((((CriteriRicercaModel)element).getGetterMethodName()
															 .equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO)) ||
						 (((CriteriRicercaModel)element).getGetterMethodName()
															 .equalsIgnoreCase(ImmobiliAffittiMethodName.PERIODOAFFITTO))															 
						){
					SWTCalendarDialog fdce = new SWTCalendarDialog(tvCriteri.getTable());
					fdce.setButtonImage(Activator.getImageDescriptor("/icons/wizardexport/calendario.png").createImage());
					fdce.setTootTipButton("Seleziona data");
					return fdce;

				}else{
					returnValue = new TextCellEditor(tvCriteri.getTable());
				}
																
				return returnValue;
			}

			@Override
			protected Object getValue(Object element) {	
				CriteriRicercaModel ccrVO = (CriteriRicercaModel)element;
				if ((ccrVO.getFromValue() != null)){
					if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODTIPOLOGIA)){
						Integer cod = 0;
						try {
							cod = Integer.valueOf(ccrVO.getFromValue());							
						} catch (NumberFormatException e) {
							cod = 0;
						}
						TipologieImmobiliVO tiVO = UtilsHelper.getInstance().findTipologiaByCod(cod);
						//Arrays.sort(desTipologiaImmobile, c);
						if (tiVO != null){
							return Arrays.binarySearch(desTipologiaImmobile, tiVO.getDescrizione(), c);
						}else{
							return -1;
						}
					}else if (ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODSTATO)){
						Integer cod = 0;
						try {
							cod = Integer.valueOf(ccrVO.getFromValue());
						} catch (NumberFormatException e) {
							cod = 0;
						}						
						StatoConservativoVO scVO = UtilsHelper.getInstance().findStatoConservativoByCod(cod);
					//	Arrays.sort(desStatoConservativo, c);
						if (scVO != null){
							return Arrays.binarySearch(desStatoConservativo, scVO.getDescrizione(), c);
						}else{
							return -1;
						}
					}else if (ccrVO.getGetterMethodName().equalsIgnoreCase("getCodRiscaldamento")){
						Integer cod = 0;
						try {
							cod = Integer.valueOf(ccrVO.getFromValue());
						} catch (NumberFormatException e) {
							cod = 0;
						}						
						RiscaldamentiVO rVO = UtilsHelper.getInstance().findRiscaldamentiByCod(cod);
					//	Arrays.sort(desRiscaldamenti, c);
						if (rVO != null){
							return Arrays.binarySearch(desRiscaldamenti, rVO.getDescrizione(), c);
						}else{
							return -1;
						}
					}else if (ccrVO.getGetterMethodName().equalsIgnoreCase("getCodAgenteInseritore")){
						Integer cod = 0;
						try {
							cod = Integer.valueOf(ccrVO.getFromValue());
						} catch (NumberFormatException e) {
							cod = 0;
						}
						
						AgentiVO aVO = UtilsHelper.getInstance().findAgentiByCod(cod);
						//Arrays.sort(desAgenti, c);
						if (aVO != null){
							return Arrays.binarySearch(desAgenti, aVO.getCognome() + " " + aVO.getNome(), c);
						}else{
							return -1;
						}
					}else{
						return ccrVO.getFromValue();
					}				
				}else{
					if (
							ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODTIPOLOGIA) ||
							ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODSTATO) ||
							ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODRISCALDAMENTO) ||
							ccrVO.getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODAGENTEINSERITORE) 
						){
						return -1;
					}else{
						return "";
					}					
				}							
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODTIPOLOGIA)){
					if (((Integer)value).intValue() > -1){
						((CriteriRicercaModel)element).setFromValue(String.valueOf(codTipologiaImmobile[((Integer)value).intValue()]));
					}
				}else if (((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODSTATO)){
					if (((Integer)value).intValue() > -1){
						((CriteriRicercaModel)element).setFromValue(String.valueOf(codStatoConservativo[((Integer)value).intValue()]));
					}					
				}else if (((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODRISCALDAMENTO)){
					if (((Integer)value).intValue() > -1){
						((CriteriRicercaModel)element).setFromValue(String.valueOf(codRiscaldamenti[((Integer)value).intValue()]));
					}										
				}else if (((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODAGENTEINSERITORE)){
					if (((Integer)value).intValue() > -1){
						
						((CriteriRicercaModel)element).setFromValue(String.valueOf(codAgenti[((Integer)value).intValue()]));
					}										
				}else{
					((CriteriRicercaModel)element).setFromValue(String.valueOf(value));
				}				
				tvCriteri.refresh();
				((ExporterWizard)getWizard()).getContainer().updateButtons();
			}
			
		});

		TableColumn tcValoreA = new TableColumn(tvCriteri.getTable(),SWT.CENTER,2);
		tcValoreA.setWidth(100);
		tcValoreA.setText("A");
		
		TableViewerColumn tvcValoreA = new TableViewerColumn(tvCriteri,tcValoreA);
		tvcValoreA.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				if (((CriteriRicercaModel)cell.getElement()).getToValue() != null){
					
					CriteriRicercaModel ccrVO = (CriteriRicercaModel)cell.getElement();
					if ((!ccrVO.getGetterMethodName().equalsIgnoreCase("(")) &&
							(!ccrVO.getGetterMethodName().equalsIgnoreCase(")"))
						){
						Class methodReturnType = UtilsHelper.getInstance().getReturnTypeGetterMethod(ImmobiliModel.class,ccrVO.getGetterMethodName());
						
						if (methodReturnType != null){
						
							if (methodReturnType.getName().equalsIgnoreCase(Double.class.getName())){
								try {
									cell.setText(String.valueOf(Double.valueOf(ccrVO.getToValue()).doubleValue()));
								} catch (NumberFormatException e) {
									cell.setText("0.0");
								}
							}else{
								cell.setText(String.valueOf(((CriteriRicercaModel)cell.getElement()).getToValue()));
							}
						}else{
							cell.setText("");
						}
					}
				}
			}
			
		});
		tvcValoreA.setEditingSupport(new EditingSupport(tvCriteri){

			@Override
			protected boolean canEdit(Object element) {
				
				if (
						((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODTIPOLOGIA) ||
						((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODSTATO) ||
						((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODRISCALDAMENTO) ||
						((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CODAGENTEINSERITORE) ||
						((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_ZONA) ||
						((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CITTA) ||
						((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_PROVINCIA) ||
						((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CAP) ||
						((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_RIF) ||						
						((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase("(") ||
						((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase(")")
					){
				
					return false;
				}else{
					return true;
				}
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				CellEditor returnValue = null;
				if (
						((CriteriRicercaModel)element).getGetterMethodName()
													       .equalsIgnoreCase(ImmobiliAffittiMethodName.GET_DATALIBERO) ||
						((CriteriRicercaModel)element).getGetterMethodName()
														   .equalsIgnoreCase(ImmobiliAffittiMethodName.PERIODOAFFITTO)
													   
				   ){
					SWTCalendarDialog fdce = new SWTCalendarDialog(tvCriteri.getTable());
					fdce.setButtonImage(Activator.getImageDescriptor("/icons/wizardexport/calendario.png").createImage());
					fdce.setTootTipButton("Seleziona data");
					returnValue = fdce;
				}else{
					returnValue = new TextCellEditor(tvCriteri.getTable());
				}
				return returnValue;
			}

			@Override
			protected Object getValue(Object element) {				
				if (((CriteriRicercaModel)element).getToValue() != null){
					return ((CriteriRicercaModel)element).getToValue();
				}else{
					return "";
				}							
			}

			@Override
			protected void setValue(Object element, Object value) {
				((CriteriRicercaModel)element).setToValue(String.valueOf(value));
				tvCriteri.refresh();
				((ExporterWizard)getWizard()).getContainer().updateButtons();
			}
			
		});

		TableColumn tcOpLogico = new TableColumn(tvCriteri.getTable(),SWT.CENTER,3);
		tcOpLogico.setWidth(80);
		tcOpLogico.setText("Op. Logico");
		
		TableViewerColumn tvcOpLogico = new TableViewerColumn(tvCriteri,tcOpLogico);
		tvcOpLogico.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				if (((CriteriRicercaModel)cell.getElement()).getLogicalOperator() != null){
					cell.setText(
							String.valueOf(((CriteriRicercaModel)cell.getElement()).getLogicalOperator())
								);
				}else{
					cell.setText("");
				}
			}
			
		});
		tvcOpLogico.setEditingSupport(new EditingSupport(tvCriteri){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				CellEditor returnValue = null;
				returnValue = new ComboBoxCellEditor(tvCriteri.getTable(),
													 new String[]{"","AND","OR"},
													 SWT.READ_ONLY|SWT.DROP_DOWN); 

				return returnValue;
			}

			@Override
			protected Object getValue(Object element) {				
				if ((((CriteriRicercaModel)element).getLogicalOperator() != null) && 
					(!((CriteriRicercaModel)element).getLogicalOperator().equalsIgnoreCase(""))){
					return (((CriteriRicercaModel)element).getLogicalOperator().equalsIgnoreCase("AND"))?1:2;
				}else{
					return 0;
				}							
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (!((CriteriRicercaModel)element).getGetterMethodName().equalsIgnoreCase("(")){
					((CriteriRicercaModel)element).setLogicalOperator(
							(((Integer)value).intValue() == 0)
							 ? ""
							 :(((Integer)value).intValue() == 1)
							  ? "AND" 
							  : "OR" 
																	);					
				}
				tvCriteri.refresh();
			}
			
		});
		
		
		tvCriteri.setInput(((ExporterWizard)getWizard()).getExporterVO()
				 									   .getCriteriRicerca());
		setPageComplete(true);
		setControl(container);
	}
	
	public void fillDescrizioniGetters(){						
		Collections.sort(UtilsHelper.getInstance().getImmobileAffittiSearchGetters(), comparatorImmobileAffittiSearchGetters);
		desGetters = new String[UtilsHelper.getInstance().getImmobileAffittiSearchGetters().size()];
		Iterator<ObjectSearchGetters> it = UtilsHelper.getInstance().getImmobileAffittiSearchGetters().iterator();
		int count = 0;
		while(it.hasNext()){
			desGetters[count] = it.next().getDescrizione();
			count++;
		}		
	}

	public void fillDescrizioniTipologieImmobili(){				
	
		desTipologiaImmobile = new String[UtilsHelper.getInstance().getTipologieImmobili().size()];
		codTipologiaImmobile = new Integer[UtilsHelper.getInstance().getTipologieImmobili().size()];
		Iterator<Tipologieimmobili> it = UtilsHelper.getInstance().getTipologieImmobili().iterator();
		int count = 0;
		while(it.hasNext()){
			Tipologieimmobili tiVO = it.next();
			desTipologiaImmobile[count] = tiVO.getDescrizione();
			codTipologiaImmobile[count] = tiVO.getCodTipologiaImmobile();
			count++;
		}		
	}

	public void fillDescrizioniStatiConservativi(){				
	
		desStatoConservativo = new String[UtilsHelper.getInstance().getStatiConservativi().size()];
		codStatoConservativo = new Integer[UtilsHelper.getInstance().getStatiConservativi().size()];
		Iterator<Statoconservativo> it = UtilsHelper.getInstance().getStatiConservativi().iterator();
		int count = 0;
		while(it.hasNext()){
			Statoconservativo scVO = it.next();
			desStatoConservativo[count] = scVO.getDescrizione();
			codStatoConservativo[count] = scVO.getCodStatoConservativo();
			count++;
		}		
	}

	public void fillDescrizioniRiscaldamenti(){				
	
		desRiscaldamenti = new String[UtilsHelper.getInstance().getRiscaldamenti().size()];
		codRiscaldamenti = new Integer[UtilsHelper.getInstance().getRiscaldamenti().size()];
		Iterator<Riscaldamenti> it = UtilsHelper.getInstance().getRiscaldamenti().iterator();
		int count = 0;
		while(it.hasNext()){
			Riscaldamenti rVO = it.next();
			desRiscaldamenti[count] = rVO.getDescrizione();
			codRiscaldamenti[count] = rVO.getCodRiscaldamento();
			count++;
		}		
	}
	
	public void fillDescrizioniAgenti(){						
		desAgenti = new String[UtilsHelper.getInstance().getAgenti().size()];
		codAgenti = new Integer[UtilsHelper.getInstance().getAgenti().size()];
		Iterator<Agenti> it = UtilsHelper.getInstance().getAgenti().iterator();
		int count = 0;
		while(it.hasNext()){
			Agenti aVO = it.next();
			desAgenti[count] = aVO.getCognome() + " " + aVO.getNome();
			codAgenti[count] = aVO.getCodAgente();
			count++;
		}		
	}
	
	public void loadCriteriColloquio(){		
	//	((ExporterWizard)getWizard()).getExporterVO().setCriteriRicerca(new ArrayList<CriteriRicercaModel>());		
	}
	
	private void reloadLineNumber(){
		if (((ExporterWizard)getWizard()).getExporterVO()
				 						.getCriteriRicerca() != null){
			int count = 0;
			for (Iterator<ColloquiCriteriRicercaModel> iterator = ((ExporterWizard)getWizard()).getExporterVO()
					 																	   .getCriteriRicerca()
					 																	   .iterator(); iterator.hasNext();) {
				ColloquiCriteriRicercaModel ccrVO = iterator.next(); 
				ccrVO.setLineNumber(count);
				count++;
			}
		}
	}
	
	private CriteriRicercaModel getPrevCriterioByLineNumber(Integer lineNumber){
		CriteriRicercaModel ccrVO = new CriteriRicercaModel();
		ccrVO.setLineNumber(lineNumber);
		return null;
		/*
		Collections.sort(((ExporterWizard)getWizard()).getExporterVO()
				 .getCriteriRicerca(), comparatorLineNumber);
		int index = Collections.binarySearch(((ExporterWizard)getWizard()).getExporterVO()
				 .getCriteriRicerca(), ccrVO, comparatorLineNumber);
		if (index > 0){
			return (CriteriRicercaModel)((ExporterWizard)getWizard()).getExporterVO()
			 															 .getCriteriRicerca().get(index-1);
		}else{
			return (CriteriRicercaModel)((ExporterWizard)getWizard()).getExporterVO()
			 															 .getCriteriRicerca().get(index);
		}
		*/
	}

	public void setRicerca(RicercheModel rm){
		ricerca = rm;
		lRicercaSelectedName.setText(ricerca.getNome());
		lRicercaSelectedName.pack();
		lRicercaSelectedName.redraw();		
		((ExporterWizard)getWizard()).getExporterVO()
									.setCriteriRicerca((ArrayList)ricerca.getCriteri().clone());
		tvCriteri.setInput(((ExporterWizard)getWizard()).getExporterVO()
				   									   .getCriteriRicerca());
	}

	public RicercheModel getRicerca() {
		if (ricerca == null){
			ricerca = new RicercheModel();
			ricerca.setTipo(EnvSettingsFactory.getInstance()
	   				   						  .getTipologieColloqui()
	   				   						  .get(0)
	   				   						  .getCodTipologiaColloquio());
		}
		ArrayList al = (ArrayList)((ExporterWizard)getWizard()).getExporterVO()
        													  .getCriteriRicerca().clone();

		ArrayList <ColloquiCriteriRicercaModel> alm = new ArrayList<ColloquiCriteriRicercaModel>();
		Iterator it = al.iterator();
		while (it.hasNext()) {
			ColloquiCriteriRicercaModel object = new ColloquiCriteriRicercaModel((CriteriRicercaModel) it.next());
			object.setCodColloquio(0);
			alm.add(object);
		}
		ricerca.setCriteri(alm);			
		
		return ricerca;
	}
}