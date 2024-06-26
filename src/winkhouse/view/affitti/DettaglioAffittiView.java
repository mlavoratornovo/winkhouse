package winkhouse.view.affitti;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.PojoProperties;
//import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
//import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.widgets.calendarcombo.CalendarCombo;
import org.eclipse.nebula.widgets.calendarcombo.ICalendarListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.affitti.CancellaAffittiAction;
import winkhouse.action.affitti.SalvaAffittiAction;
import winkhouse.action.stampa.StampaAffittiAction;
import winkhouse.dialogs.custom.FileDialogCellEditor;
import winkhouse.dialogs.custom.SWTCalendarCellEditor;
import winkhouse.helper.ProfilerHelper;
import winkhouse.orm.Affitti;
import winkhouse.orm.Affittiallegati;
import winkhouse.orm.Affittianagrafiche;
import winkhouse.orm.Affittirate;
import winkhouse.orm.Affittispese;
//import winkhouse.model.Affittianagrafiche;
//import winkhouse.model.AffittiModel;
//import winkhouse.model.Affittirate;
//import winkhouse.model.Affittispese;
//import winkhouse.model.AgentiModel;
//import winkhouse.model.AnagraficheModel;
//import winkhouse.model.ContattiModel;
import winkhouse.orm.Agenti;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Contatti;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.anagrafica.PopUpRicercaAnagrafica;
import winkhouse.view.common.EAVView;
//import winkhouse.vo.AffittiAllegatiVO;
//import winkhouse.vo.AffittiRateVO;
//import winkhouse.vo.AgentiVO;



public class DettaglioAffittiView extends ViewPart {

	public final static String ID = "winkhouse.dettaglioaffittiview";
	private ScrolledForm f = null;	
	private FormToolkit ft = null;
	private ComboViewer cvagenteinseritoreanagrafica = null;
	private CalendarCombo dataInizio = null;
	private CalendarCombo dataFine = null;
	private Text cauzione = null;
	private Text rata = null;
	private Text descrizione = null;
	private TableViewer tvAnagrafiche = null;
	private TableViewer tvRecapitiAnagrafiche = null;
	private TableViewer tvAllegati = null;
	private TableViewer tvRate = null;
	private TableViewer tvSpese = null;
	private Affitti affitto = null;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatterEditor = new SimpleDateFormat("yyyy-MM-dd");
	private TableColumn tcDescrizioneAllegato = null;
	private TableColumn tcPathFrom = null;

	private TableColumn tcRataMese = null;
	private TableColumn tcRataScadenza = null;
	private TableColumn tcRataDataPagato = null;
	private TableColumn tcRataImporto = null;
	private TableColumn tcRataAnagrafica = null;
		
	private String[] desMesi = new String[12];
	private String[] desAnagrafiche = null;
	private ArrayList<Anagrafiche> workAnagrafiche = null;

	private TableColumn tcDescrizioneSpesa = null;
	private TableColumn tcSpeseInizioPeriodo = null;
	private TableColumn tcSpeseFinePeriodo = null;
	private TableColumn tcSpeseScadenza = null;
	private TableColumn tcSpeseImporto = null;
	private TableColumn tcSpeseVersato = null;
	private TableColumn tcSpeseDataPagato = null;
	private TableColumn tcSpeseAnagrafica = null;
	private SimpleDateFormat formatterIT = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatterENG = new SimpleDateFormat("yyyy-MM-dd");
	private Text tImmobile = null;
	
	private Image affittoImg = Activator.getImageDescriptor("icons/affitti20.png").createImage();

	boolean isInCompareMode = false;
	
	private SalvaAffittiAction saa = null;
	private CancellaAffittiAction caa = null;
	private ImageHyperlink ihNewAnagrafica = null;
	//private ImageHyperlink ihNewInsAnagrafica = null;	
	private ImageHyperlink ihCancellaAnagrafica = null;;		
	private ImageHyperlink ihNewAllegato = null;
	private ImageHyperlink ihCancellaAllegato = null;		
	private ImageHyperlink ihNewRata = null;
	private ImageHyperlink ihCancellaRata = null;		
	private ImageHyperlink ihNewSpese = null;
	private ImageHyperlink ihCancellaSpese = null;		
	
	
	public DettaglioAffittiView() {
	}
	
	private Comparator<Agenti> comparatorAgenti = new Comparator<Agenti>(){

		@Override
		public int compare(Agenti arg0,Agenti arg1) {
			return Integer.valueOf(arg0.getCodAgente()).compareTo(arg1.getCodAgente());
		}
		
	};

	private Comparator<Anagrafiche> comparatorAnagrafiche = new Comparator<Anagrafiche>(){

		@Override
		public int compare(Anagrafiche arg0,Anagrafiche arg1) {
			if (arg0.getCodAnagrafica() == arg1.getCodAnagrafica()){
				return 0;
			}else if (arg0.getCodAnagrafica() > arg1.getCodAnagrafica()){
				return 1;
			}else{
				return -1;
			}				
		}
		
	};
	
	public void fillDescrizioniMesi(){						
							
		Iterator it = WinkhouseUtils.getInstance()
		 							  .getMesi()
		 							  .iterator();
		int i = 0;
		while(it.hasNext()){
			desMesi[i] = ((WinkhouseUtils.Mese)it.next()).getNome();
			i++;
		}
		
	}
	
	public void fillDescrizioniAnagrafiche(){						
		
		if (affitto.getAffittianagrafiches() != null){
//			desAnagrafiche = new String[affitto.getSavedAnagrafiche()];
//			workAnagrafiche = new ArrayList<AnagraficheModel>();
//			Iterator<Affittianagrafiche> it = affitto.getAnagrafiche().iterator();			
//			while(it.hasNext()){				
//				AnagraficheModel am = it.next().getAnagrafiche();
//				if ((am != null) && (am.getCodAnagrafica() != 0)){
//					workAnagrafiche.add(am);
//				}
//			}
			
//			Collections.sort(workAnagrafiche, comparatorAnagrafiche);
			
//			Iterator<AnagraficheModel> ite = workAnagrafiche.iterator();
//			int i = 0;
//			while (ite.hasNext()){
//				AnagraficheModel am = ite.next();
//				if (am.getCodAnagrafica() != 0){
//					desAnagrafiche[i] = am.toString();
//					i++;
//				}
//			}
			
		}else{
//			desAnagrafiche = new String[0];
//			workAnagrafiche = new ArrayList<AnagraficheModel>();
		}		
		
		
	}
	
	@Override
	public void createPartControl(Composite parent) {
		fillDescrizioniMesi();
		ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createScrolledForm(parent);
		f.setMinHeight(200);
		f.setExpandVertical(true);
		f.setImage(affittoImg);
		f.setText("Dettaglio affitto");
		f.getBody().setLayout(new GridLayout());
		
		IToolBarManager mgr = f.getToolBarManager();
		
		saa = new SalvaAffittiAction("Salva affitto",
				   					 Activator.getImageDescriptor("icons/document-save.png"));
		
		caa = new CancellaAffittiAction("Cancella affitto",
				  						Activator.getImageDescriptor("icons/edittrash.png"),
				  						CancellaAffittiAction.DELETE_SINGLE);
		mgr.add(saa);
		mgr.add(caa);
		
		mgr.add(new StampaAffittiAction("Report affitti", 
				 						Action.AS_DROP_DOWN_MENU));		

		f.updateToolBar();
		
		GridLayout glForm = new GridLayout();
		glForm.numColumns = 2;
		f.getBody().setLayout(glForm);
		ft.paintBordersFor(f.getBody());
		GridData gdSpan2 = new GridData();
		gdSpan2.horizontalSpan = 2;
		gdSpan2.grabExcessVerticalSpace = false;
		gdSpan2.grabExcessHorizontalSpace = true;
		gdSpan2.horizontalAlignment = SWT.FILL;
		
		GridData gdSpan2_50 = new GridData();
		gdSpan2_50.horizontalSpan = 2;
		gdSpan2_50.grabExcessHorizontalSpace =  true;
		gdSpan2_50.grabExcessVerticalSpace =  true;
		gdSpan2_50.horizontalAlignment = SWT.FILL;
		gdSpan2_50.verticalAlignment = SWT.FILL;
		gdSpan2_50.minimumHeight = 50;

		GridData gdSpan = new GridData();		
		gdSpan.grabExcessHorizontalSpace =  true;		
		gdSpan.horizontalAlignment = SWT.FILL;
		gdSpan.verticalAlignment = SWT.FILL;
		gdSpan.minimumHeight = 15;

		tImmobile = ft.createText(f.getBody(), "Immobile : ", SWT.FLAT);
		tImmobile.setLayoutData(gdSpan2);
		tImmobile.setEditable(false);
		
		Label lAgente = ft.createLabel(f.getBody(), "Agente inseritore");
		lAgente.setLayoutData(gdSpan2);
		
		GridData gdSpan2Combo = new GridData();
		gdSpan2Combo.horizontalSpan = 2;
		gdSpan2Combo.grabExcessVerticalSpace = false;
		gdSpan2Combo.grabExcessHorizontalSpace = true;
		gdSpan2Combo.horizontalAlignment = SWT.FILL;
		gdSpan2Combo.heightHint = 15;
		
		cvagenteinseritoreanagrafica = new ComboViewer(f.getBody(), SWT.DROP_DOWN | SWT.READ_ONLY | SWT.DOUBLE_BUFFERED);
		cvagenteinseritoreanagrafica.getCombo().setLayoutData(gdSpan2Combo);		
		
		Label lDataInizio = ft.createLabel(f.getBody(), "Data inizio");
		
		Label lDataFine = ft.createLabel(f.getBody(), "Data fine");
		
		dataInizio = new CalendarCombo(f.getBody(), SWT.READ_ONLY|SWT.DOUBLE_BUFFERED);		
		dataInizio.setLayoutData(gdSpan);
		dataInizio.addCalendarListener(new ICalendarListener() {
			
			@Override
			public void popupClosed() {
				if (dataInizio.getDate().before(dataFine.getDate()) || dataFine.getDate() == null){
					affitto.setDatainizio(LocalDateTime.ofInstant(dataInizio.getDate().getTime().toInstant(), ZoneId.systemDefault()));
				}else if (dataFine.getDate() != null){
					dataFine.getDate().add(Calendar.DAY_OF_MONTH, -1);
					dataInizio.setDate(dataFine.getDate().getTime());
				}
			}
			
			@Override
			public void dateRangeChanged(Calendar arg0, Calendar arg1) {
		
				
			}
			
			@Override
			public void dateChanged(Calendar arg0) {
				if(affitto.getDatafine() != null){
					Calendar cFine = Calendar.getInstance(Locale.ITALIAN);
					cFine.setTime(Date.from(affitto.getDatafine().atZone(ZoneId.systemDefault()).toInstant()));
					if (arg0.before(cFine)){
						affitto.setDatainizio(LocalDateTime.ofInstant(arg0.getTime().toInstant(), ZoneId.systemDefault()));
					}else{
						cFine.add(Calendar.DAY_OF_MONTH, -1);
						dataInizio.setDate(cFine.getTime());
					}
				}
				
			}

		});
		dataFine = new CalendarCombo(f.getBody(), SWT.READ_ONLY|SWT.DOUBLE_BUFFERED);		
		dataFine.setLayoutData(gdSpan);
		dataFine.addCalendarListener(new ICalendarListener() {
			
			@Override
			public void popupClosed() {
				if (dataFine.getDate() != null){
					if (dataFine.getDate().after(dataInizio.getDate())){
						affitto.setDatainizio(LocalDateTime.ofInstant(dataInizio.getDate().getTime().toInstant(), ZoneId.systemDefault()));
					}else{
						dataInizio.getDate().add(Calendar.DAY_OF_MONTH, 1);
						dataFine.setDate(dataInizio.getDate().getTime());
					}
				}
			}
			
			@Override
			public void dateRangeChanged(Calendar arg0, Calendar arg1) {
		
				
			}
			
			@Override
			public void dateChanged(Calendar arg0) {
				Calendar cInizio = Calendar.getInstance(Locale.ITALIAN);
				cInizio.setTime(Date.from(affitto.getDatainizio().atZone(ZoneId.systemDefault()).toInstant()));
				if (arg0 != null){
					if (arg0.after(cInizio)){
						affitto.setDatafine(LocalDateTime.ofInstant(arg0.getTime().toInstant(),ZoneId.systemDefault()));
					}else{
						cInizio.add(Calendar.DAY_OF_MONTH, 1);
						dataFine.setDate(cInizio.getTime());
					}
				}else{
					affitto.setDatafine(null);
				}				
			}
			
		});

		Label lCauzione = ft.createLabel(f.getBody(), "Cauzione");		
		Label lRata = ft.createLabel(f.getBody(), "Rata");
		
		
		cauzione = ft.createText(f.getBody(), "0", SWT.DOUBLE_BUFFERED);
		cauzione.setLayoutData(gdSpan);
		cauzione.addVerifyListener(new VerifyListener(){
			public void verifyText(VerifyEvent e) {				
				try {
					Integer prezzo = new Integer(e.text.replaceAll("[.]",""));				
				} catch (NumberFormatException e1) {
					e.text = "";
				}					
			}			
		});

		rata = ft.createText(f.getBody(), "0", SWT.DOUBLE_BUFFERED);
		rata.setLayoutData(gdSpan);
		rata.addVerifyListener(new VerifyListener(){
			public void verifyText(VerifyEvent e) {				
				try {
					Integer prezzo = new Integer(e.text.replaceAll("[.]",""));				
				} catch (NumberFormatException e1) {
					e.text = "";
				}					
			}			
		});
		
		createAnagraficheSection();
		rateAffito();
		speseAffito();
		allegatiAffito();
		
		Label lDescrizione = ft.createLabel(f.getBody(), "Descrizione");
		lDescrizione.setLayoutData(gdSpan2);
		
		descrizione = ft.createText(f.getBody(), "", SWT.DOUBLE_BUFFERED|SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL|SWT.WRAP);
		descrizione.setLayoutData(gdSpan2_50);
	}
	
	private void createAnagraficheSection(){

		Section section = ft.createSection(
				   				f.getBody(), 
				   				Section.DESCRIPTION|Section.TITLE_BAR|
				   				Section.TWISTIE);

		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				f.reflow(true);
			}
			
		});

		GridData tabella = new GridData();
		tabella.grabExcessHorizontalSpace = true;
		tabella.horizontalAlignment = SWT.FILL;
		tabella.heightHint = 200;

		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		
		GridData gdtabella = new GridData();
		gdtabella.horizontalSpan = 2;
		gdtabella.grabExcessHorizontalSpace = true;
		gdtabella.grabExcessVerticalSpace = false;
		gdtabella.horizontalAlignment = SWT.FILL;
		gdtabella.verticalAlignment = SWT.FILL;		

		GridData gdSection = new GridData();
		gdSection.grabExcessHorizontalSpace = true;
		gdSection.grabExcessVerticalSpace = true;
		gdSection.horizontalAlignment = SWT.FILL;
		gdSection.verticalAlignment = SWT.FILL;		
		
		section.setLayoutData(gdtabella);
		section.setText("Anagrafiche");
		section.setDescription("Anagrafiche presenti all'appuntamento");
		
		Composite sectionClient = ft.createComposite(section);
		ft.paintBordersFor(sectionClient);
		sectionClient.setLayoutData(gdSection);
		sectionClient.setLayout(new GridLayout());
		Composite toolbar = new Composite(sectionClient,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ihNewAnagrafica = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNewAnagrafica.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNewAnagrafica.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNewAnagrafica.setToolTipText("Aggiungi anagrafica da archivio");
		ihNewAnagrafica.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (ProfilerHelper.getInstance().getPermessoUI(PopUpRicercaAnagrafica.ID)){
					PopUpRicercaAnagrafica pra = new PopUpRicercaAnagrafica(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell());
					pra.setCallerObj(PlatformUI.getWorkbench()
											   .getActiveWorkbenchWindow()
											   .getActivePage()
											   .getActivePart());
					pra.setSetterMethodName("addAnagrafica");
					pra.open();
				}else{
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
		  					  "Controllo permessi accesso vista",
		  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
		  					  " non ha il permesso di accedere alla vista " + 
		  					  PopUpRicercaAnagrafica.ID);
				}

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

//		ihNewInsAnagrafica = ft.createImageHyperlink(toolbar, SWT.WRAP);		
//		ihNewInsAnagrafica.setImage(Activator.getImageDescriptor("/icons/filenewins.png").createImage());
//		ihNewInsAnagrafica.setHoverImage(Activator.getImageDescriptor("/icons/filenewinshover.png").createImage());
//		ihNewInsAnagrafica.setToolTipText("Aggiungi nuova anagrafica");
//		ihNewInsAnagrafica.addMouseListener(new MouseListener(){
//
//			@Override
//			public void mouseUp(MouseEvent e) {
//				Affittianagrafiche aaModel = new Affittianagrafiche();				
//				aaModel.setCodAffitto(affitto.getCodAffitti());
//				AnagraficheModel anagrafica = new AnagraficheModel();
//				aaModel.setAnagrafica(anagrafica);
//				if (affitto.getAnagrafiche() == null){
//					affitto.setAnagrafiche(new ArrayList<Affittianagrafiche>());
//				}
//				affitto.getAnagrafiche().add(aaModel);
//				tvAnagrafiche.setInput(new Object());
//				tvAnagrafiche.refresh();
//				
//				TableItem ti = tvAnagrafiche.getTable().getItem(tvAnagrafiche.getTable().getItemCount()-1);
//				Object[] sel = new Object[1];
//				sel[0] = ti.getData();
//
//				StructuredSelection ss = new StructuredSelection(sel);
//				
//				tvAnagrafiche.setSelection(ss, true);
//
//				Event ev = new Event();
//				ev.item = ti;
//				ev.data = ti.getData();
//				ev.widget = tvAnagrafiche.getTable();
//				tvAnagrafiche.getTable().notifyListeners(SWT.Selection, ev);
//
//			}
//
//			@Override
//			public void mouseDoubleClick(MouseEvent e) {
//			}
//
//			@Override
//			public void mouseDown(MouseEvent e) {
//			}
//			
//		});
		
		ihCancellaAnagrafica = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancellaAnagrafica.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaAnagrafica.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaAnagrafica.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				if (tvAnagrafiche.getSelection() != null){
					Iterator it = ((StructuredSelection)tvAnagrafiche.getSelection()).iterator();
					while (it.hasNext()) {
						affitto.removeFromAffittianagrafiches((Affittianagrafiche)it.next());							   
					}
					tvAnagrafiche.setInput(new Object());
					tvAnagrafiche.refresh();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		//--				
		tvAnagrafiche = new TableViewer(sectionClient,SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
	/*	TableViewerEditor.create(tvAgenti,
								 new ColumnViewerEditorActivationStrategy(tvAgenti),
								 ColumnViewerEditor.TABBING_HORIZONTAL|ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR|ColumnViewerEditor.TABBING_VERTICAL);
		*/
		tvAnagrafiche.getTable().setHeaderVisible(true);
		tvAnagrafiche.getTable().setLinesVisible(true);
		tvAnagrafiche.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				tvRecapitiAnagrafiche.setInput(new Object());
			}
		});
		
		TableColumn tcNome = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,0);
		tcNome.setText("Nome");
		tcNome.setWidth(200);
		
		TableViewerColumn tvcNome = new TableViewerColumn(tvAnagrafiche, tcNome);
		tvcNome.setEditingSupport(new EditingSupport(tvAnagrafiche) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((Affittianagrafiche)element).getAnagrafiche().setNome((String)value);
				tvAnagrafiche.refresh();
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((Affittianagrafiche)element).getAnagrafiche().getNome();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvAnagrafiche.getTable());
			}
			
			@Override
			protected boolean canEdit(Object element) {				
				return (((Affittianagrafiche)element).getAnagrafiche().getCodAnagrafica() == 0)
						? true
						: false;
			}
		});
		
		tvcNome.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Affittianagrafiche)cell.getElement()).getAnagrafiche()
																	.getNome());
			}
		});		

		
		TableColumn tcCognome = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,1);
		tcCognome.setText("Cognome");
		tcCognome.setWidth(200);

		TableViewerColumn tvcCognome = new TableViewerColumn(tvAnagrafiche, tcCognome);
		tvcCognome.setEditingSupport(new EditingSupport(tvAnagrafiche) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((Affittianagrafiche)element).getAnagrafiche().setCognome((String)value);
				tvAnagrafiche.refresh();
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((Affittianagrafiche)element).getAnagrafiche().getCognome();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvAnagrafiche.getTable());
			}
			
			@Override
			protected boolean canEdit(Object element) {				
				return (((Affittianagrafiche)element).getAnagrafiche().getCodAnagrafica() == 0)
						? true
						: false;
			}
		});

		tvcCognome.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Affittianagrafiche)cell.getElement()).getAnagrafiche()
																		 .getCognome());
			}
		});		

		TableColumn tcRagioneSociale = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,2);
		tcRagioneSociale.setText("Ragione sociale");
		tcRagioneSociale.setWidth(200);

		TableViewerColumn tvcRagioneSociale = new TableViewerColumn(tvAnagrafiche, tcRagioneSociale);
		tvcRagioneSociale.setEditingSupport(new EditingSupport(tvAnagrafiche) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((Affittianagrafiche)element).getAnagrafiche().setRagsoc((String)value);
				tvAnagrafiche.refresh();
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((Affittianagrafiche)element).getAnagrafiche().getRagsoc();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvAnagrafiche.getTable());
			}
			
			@Override
			protected boolean canEdit(Object element) {				
				return (((Affittianagrafiche)element).getAnagrafiche().getCodAnagrafica() == 0)
						? true
						: false;
			}
		});

		tvcRagioneSociale.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Affittianagrafiche)cell.getElement()).getAnagrafiche()
																		 .getRagsoc());
			}
		});		

		TableColumn tcCap = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,3);
		tcCap.setText("Cap");
		tcCap.setWidth(50);
		
		TableViewerColumn tvcCap = new TableViewerColumn(tvAnagrafiche, tcCap);
		tvcCap.setEditingSupport(new EditingSupport(tvAnagrafiche) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((Affittianagrafiche)element).getAnagrafiche().setCap((String)value);
				tvAnagrafiche.refresh();
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((Affittianagrafiche)element).getAnagrafiche().getCap();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvAnagrafiche.getTable());
			}
			
			@Override
			protected boolean canEdit(Object element) {				
				return (((Affittianagrafiche)element).getAnagrafiche().getCodAnagrafica() == 0)
						? true
						: false;
			}
		});		
		
		tvcCap.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Affittianagrafiche)cell.getElement()).getAnagrafiche().getCap());
			}
		});
		
		
		TableColumn tcProvincia = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,4);
		tcProvincia.setText("Provincia");
		tcProvincia.setWidth(100);
			
		TableViewerColumn tvcProvincia = new TableViewerColumn(tvAnagrafiche, tcProvincia);
		tvcProvincia.setEditingSupport(new EditingSupport(tvAnagrafiche) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((Affittianagrafiche)element).getAnagrafiche().setProvincia((String)value);
				tvAnagrafiche.refresh();
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((Affittianagrafiche)element).getAnagrafiche().getProvincia();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvAnagrafiche.getTable());
			}
			
			@Override
			protected boolean canEdit(Object element) {				
				return (((Affittianagrafiche)element).getAnagrafiche().getCodAnagrafica() == 0)
						? true
						: false;
			}
		});	
		
		tvcProvincia.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Affittianagrafiche)cell.getElement()).getAnagrafiche()
																		 .getProvincia());
			}
		});		
		
		TableColumn tcCitta = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,5);
		tcCitta.setText("Citta");
		tcCitta.setWidth(150);
		
		TableViewerColumn tvcCitta = new TableViewerColumn(tvAnagrafiche, tcCitta);
		tvcCitta.setEditingSupport(new EditingSupport(tvAnagrafiche) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((Affittianagrafiche)element).getAnagrafiche().setCitta((String)value);
				tvAnagrafiche.refresh();
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((Affittianagrafiche)element).getAnagrafiche().getCitta();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvAnagrafiche.getTable());
			}
			
			@Override
			protected boolean canEdit(Object element) {				
				return (((Affittianagrafiche)element).getAnagrafiche().getCodAnagrafica() == 0)
						? true
						: false;
			}
		});

		tvcCitta.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Affittianagrafiche)cell.getElement()).getAnagrafiche()
																		 .getCitta());
			}
		});		
		
		TableColumn tcIndirizzo = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,6);
		tcIndirizzo.setText("Indirizzo");
		tcIndirizzo.setWidth(200);
		
		TableViewerColumn tvcIndirizzo = new TableViewerColumn(tvAnagrafiche, tcIndirizzo);
		tvcIndirizzo.setEditingSupport(new EditingSupport(tvAnagrafiche) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((Affittianagrafiche)element).getAnagrafiche().setIndirizzo((String)value);
				tvAnagrafiche.refresh();
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((Affittianagrafiche)element).getAnagrafiche().getIndirizzo();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvAnagrafiche.getTable());
			}
			
			@Override
			protected boolean canEdit(Object element) {				
				return (((Affittianagrafiche)element).getAnagrafiche().getCodAnagrafica() == 0)
						? true
						: false;
			}
		});		
		
		tvcIndirizzo.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Affittianagrafiche)cell.getElement()).getAnagrafiche()
																		 .getIndirizzo());
			}
		});

		
		
		TableColumn tcNote = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,7);
		tcNote.setText("Note");
		tcNote.setWidth(200);
		
		TableViewerColumn tvcNote = new TableViewerColumn(tvAnagrafiche, tcNote);
		tvcNote.setEditingSupport(new EditingSupport(tvAnagrafiche) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((Affittianagrafiche)element).setNota(String.valueOf(value));
				tvAnagrafiche.refresh();
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((Affittianagrafiche)element).getNota();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvAnagrafiche.getTable());
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		
		tvcNote.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Affittianagrafiche)cell.getElement()).getNota());
			}
		});
		
		tvAnagrafiche.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return (affitto != null && affitto.getAffittianagrafiches()!= null)
				   		? affitto.getAffittianagrafiches().toArray()
				   		: new ArrayList().toArray();
			}

			@Override
			public void dispose() {
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				
			}
			
		});
		
		tvAnagrafiche.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				switch(columnIndex){
				case 0:return (((Affittianagrafiche)element).getAnagrafiche() == null)
							   ? ""
							   :((Affittianagrafiche)element).getAnagrafiche().getNome();
				case 1:return (((Affittianagrafiche)element).getAnagrafiche() == null)
							   ? ""
							   :((Affittianagrafiche)element).getAnagrafiche().getCognome();
				case 2:return (((Affittianagrafiche)element).getAnagrafiche() == null)
						  	   ? ""
						       :((Affittianagrafiche)element).getAnagrafiche().getRagsoc();				
				case 3:return (((Affittianagrafiche)element).getAnagrafiche() == null)
							  ? ""
							  :((Affittianagrafiche)element).getAnagrafiche().getCap();
				case 4:return (((Affittianagrafiche)element).getAnagrafiche() == null)
				              ? ""
				              :((Affittianagrafiche)element).getAnagrafiche().getProvincia();
				case 5:return (((Affittianagrafiche)element).getAnagrafiche() == null)
							  ? ""
							  :((Affittianagrafiche)element).getAnagrafiche().getCitta();
				case 6:return (((Affittianagrafiche)element).getAnagrafiche() == null)
							  ? ""
							  :((Affittianagrafiche)element).getAnagrafiche().getIndirizzo();
				case 7:return ((Affittianagrafiche)element).getNota();
				default: return "";
				}				
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
		tvAnagrafiche.getTable().setLayoutData(tabella);
		tvAnagrafiche.setInput(new Object());
		
		GridData gdTableRecapiti = new GridData();
		gdTableRecapiti.heightHint = 100;

		Composite cRecapiti = ft.createComposite(sectionClient, SWT.NONE);
		ft.paintBordersFor(cRecapiti);
		cRecapiti.setLayout(new GridLayout());
		cRecapiti.setLayoutData(gdTableRecapiti);

		tvRecapitiAnagrafiche = new TableViewer(cRecapiti,SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		tvRecapitiAnagrafiche.getTable().setLayoutData(gdTableRecapiti);
		tvRecapitiAnagrafiche.getTable().setLayoutData(tabella);
		tvRecapitiAnagrafiche.getTable().setHeaderVisible(true);
		tvRecapitiAnagrafiche.getTable().setLinesVisible(true);
		tvRecapitiAnagrafiche.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return (((StructuredSelection)tvAnagrafiche.getSelection()).getFirstElement() == null)
					   ? new ArrayList().toArray()
					   : (((Affittianagrafiche)((StructuredSelection)tvAnagrafiche.getSelection()).getFirstElement()).getAnagrafiche() == null)
					     ? new ArrayList().toArray()
					     : (((Affittianagrafiche)((StructuredSelection)tvAnagrafiche.getSelection()).getFirstElement()).getAnagrafiche()
					    		 																					   .getContattis() == null)
				           ? new ArrayList().toArray()
				           : ((Affittianagrafiche)((StructuredSelection)tvAnagrafiche.getSelection()).getFirstElement()).getAnagrafiche()
					 																									.getContattis().toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			
		});
		tvRecapitiAnagrafiche.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				Contatti cModel = ((Contatti)element);
				switch (columnIndex){
				case 0: return ((cModel.getTipologiecontatti() == null)
							   ? ""
							   : (cModel.getTipologiecontatti().getDescrizione() == null)
							     ? ""
							     : cModel.getTipologiecontatti().getDescrizione());					        
				
				case 1: return ((cModel.getContatto() == null) || 
								(cModel.getContatto().equalsIgnoreCase(""))
				   			   ? "nuovo recapito"
				   			   : cModel.getContatto());
				
				default : return "";
			}

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
		TableColumn tcTipologia = new TableColumn(tvRecapitiAnagrafiche.getTable(),SWT.CENTER,0);
		tcTipologia.setWidth(150);
		tcTipologia.setText("Tipologia recapito");
				
		TableColumn tcContatto = new TableColumn(tvRecapitiAnagrafiche.getTable(),SWT.CENTER,1);
		tcContatto.setWidth(150);
		tcContatto.setText("Recapito");
				
		TableColumn tcDescrizione = new TableColumn(tvRecapitiAnagrafiche.getTable(),SWT.CENTER,2);
		tcDescrizione.setWidth(150);
		tcDescrizione.setText("Descrizione");

		section.setClient(sectionClient);
		
	}

	@Override
	public void setFocus() {
		if ((cvagenteinseritoreanagrafica != null) &&
			(cvagenteinseritoreanagrafica.getContentProvider() != null)){
			cvagenteinseritoreanagrafica.setInput(new Object());
			if (affitto.getAgenti1() != null){
				int index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getAgenti(), affitto.getAgenti1(), comparatorAgenti);
				Object[] sel = new Object[1];
				sel[0] = MobiliaDatiBaseCache.getInstance().getAgenti().get(index);
				StructuredSelection ss = new StructuredSelection(sel);
				cvagenteinseritoreanagrafica.setSelection(ss);
			}

		}
		
		IViewPart eavv = PlatformUI
 				  .getWorkbench()
 				  .getActiveWorkbenchWindow()
 				  .getActivePage()
 				  .findView(EAVView.ID);

		if (eavv != null){			
//			if ((affitto != null) && (affitto.getEntity() != null) && (affitto.getEntity().getAttributes()!= null)){
//				((EAVView)eavv).setAttributes(affitto.getEntity().getAttributes(), affitto.getCodAffitti());
//			}
		}

	}

	public Affitti getAffitto() {
		return affitto;
	}

	public void setAffitto(Affitti affitto) {
		
		isInCompareMode = false;
		this.affitto = affitto;
		DataBindingContext bindingContext = new DataBindingContext();
		this.setPartName(formatter.format(affitto.getDatainizio())+ 
						 " - " +
						 ((affitto.getDatafine() != null)
						  ? formatter.format(affitto.getDatafine())
						  :""));		
		bindAffitto(bindingContext);
		

	}
	
	private void bindAffitto(DataBindingContext bindingContext){
		
		tImmobile.setText("Immobile : " + affitto.getImmobili().toString());
		
		cvagenteinseritoreanagrafica.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				
				return MobiliaDatiBaseCache.getInstance().getAgenti().toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,Object newInput) {				
			}
			
		});
		
		cvagenteinseritoreanagrafica.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				return ((Agenti)element).getCognome() + " " + ((Agenti)element).getNome();
			}
			
		});
		
		cvagenteinseritoreanagrafica.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {				
				affitto.setAgenti1(((Agenti)((StructuredSelection)event.getSelection()).getFirstElement()));				
			}
			
		});
		

						
		cvagenteinseritoreanagrafica.setInput(new Object());
		
		if (affitto.getAgenti1() != null){
			int index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getAgenti(), affitto.getAgenti1(), comparatorAgenti);
			Object[] sel = new Object[1];
			sel[0] = MobiliaDatiBaseCache.getInstance().getAgenti().get(index);
			StructuredSelection ss = new StructuredSelection(sel);
			cvagenteinseritoreanagrafica.setSelection(ss);
		}

		UpdateValueStrategy uvsToModel = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		uvsToModel.setConverter(new IConverter() {
			
			@Override
			public Object getToType() {
				return String.class;
			}
			
			@Override
			public Object getFromType() {				
				return Double.class;
			}
			
			@Override
			public Object convert(Object arg0) {
				Double d = (Double)arg0;
				return d.toString().replaceAll("[,]", "");
			}
		});
		
		UpdateValueStrategy uvsToText = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		uvsToText.setConverter(new IConverter() {
			
			@Override
			public Object getToType() {
				return Double.class;
			}
			
			@Override
			public Object getFromType() {				
				return String.class;
			}
			
			@Override
			public Object convert(Object arg0) {
				String s = (String)arg0;
				return Double.valueOf(s);
			}
		});
		
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(cauzione), 
								 PojoProperties.value("cauzione").observe(affitto),
								 uvsToText, 
								 uvsToModel);

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(rata), 
								 PojoProperties.value("rata").observe(affitto),
								 uvsToText, 
								 uvsToModel);

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(descrizione), 
								 PojoProperties.value("descrizione").observe(affitto),
								 null, 
								 null);
		
		Calendar cinizio = Calendar.getInstance(Locale.ITALIAN);
		cinizio.setTime(Date.from(affitto.getDatainizio().atZone(ZoneId.systemDefault()).toInstant()));
		dataInizio.setDate(cinizio);
		
		Calendar cfine = Calendar.getInstance(Locale.ITALIAN);
		if (affitto.getDatafine() != null){       
			cfine.setTime(Date.from(affitto.getDatafine().atZone(ZoneId.systemDefault()).toInstant()));
			dataFine.setDate(cfine);
		}
		
			
//		bindingContext.bindValue(SWTObservables.observeText(descrizione,SWT.Modify), 
//								 PojoObservables.observeValue(affitto, "descrizione"),
//								 null, 
//								 null);
		
		tvAnagrafiche.setInput(new Object());
		tvRecapitiAnagrafiche.setInput(new Object());
		bindAllegatiAffitto(bindingContext);
		tvRate.setInput(new Object());
		bindRateAffitto(bindingContext);
		tvSpese.setInput(new Object());
		bindSpeseAffitto(bindingContext);

	}
	
	private boolean checkAnagrafica(Integer codAnagrafica){
		boolean returnValue = true;
		if (affitto.getAffittianagrafiches() != null){
			Iterator<Affittianagrafiche> it = affitto.getAffittianagrafiches().iterator();
			while(it.hasNext()){
				if (it.next().getAnagrafiche().getCodAnagrafica() == codAnagrafica.intValue()){
					returnValue = false;
					break;
				}
			}
		}
		return returnValue;
	}

	public void addAnagrafica(Anagrafiche anagrafica){
		if (checkAnagrafica(anagrafica.getCodAnagrafica())){
			Affittianagrafiche aaModel = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Affittianagrafiche.class);
			aaModel.setAnagrafiche(anagrafica);
			aaModel.setAffitti(affitto);
			
//			if (affitto.getAffittianagrafiches() == null){				
//				affitto.setAnagrafiche(new ArrayList<Affittianagrafiche>());
//			}
			affitto.addToAffittianagrafiches(aaModel);
			tvAnagrafiche.setInput(new Object());
			tvAnagrafiche.refresh();
		}else{
			MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					  				  "Errore selezione anagrafica", 
					  				  "Anagrafica presente in lista");			
		}
	}
	
	private void allegatiAffito(){
		
		Section section = ft.createSection(f.getBody(), 
				   						   Section.DESCRIPTION|Section.TITLE_BAR|
				   						   Section.TWISTIE);

		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				f.reflow(true);
			}
			
		});

		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;
		
		GridData gdExpVHSection = new GridData();
		gdExpVHSection.horizontalSpan = 2;
		gdExpVHSection.grabExcessHorizontalSpace = true;
		gdExpVHSection.grabExcessVerticalSpace = false;
		gdExpVHSection.horizontalAlignment = SWT.FILL;
		gdExpVHSection.verticalAlignment = SWT.FILL;	
		
		GridData gdExpVHTable = new GridData();
		gdExpVHTable.grabExcessHorizontalSpace = true;
		gdExpVHTable.grabExcessVerticalSpace = true;
		gdExpVHTable.horizontalAlignment = SWT.FILL;
		gdExpVHTable.verticalAlignment = SWT.FILL;	
		gdExpVHTable.minimumHeight = 80;
		
		section.setLayout(new GridLayout());
		section.setLayoutData(gdExpVHSection);
		section.setText("Allegati affitto");
		section.setDescription("documenti allegati all'affitto");
		
		Composite contenitore = ft.createComposite(section,SWT.NONE);
		ft.paintBordersFor(contenitore);
		contenitore.setLayout(new GridLayout());
		contenitore.setLayoutData(gdExpVH);

		Composite toolbar = ft.createComposite(contenitore,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ihNewAllegato = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNewAllegato.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNewAllegato.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNewAllegato.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				Affittiallegati aaVO = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Affittiallegati.class);
				aaVO.setAffitti(affitto);				
//				if (affitto.getAllegati() == null){
//					affitto.setAllegati(new ArrayList<AffittiAllegatiVO>());
//				}
				affitto.addToAffittiallegatis(aaVO);
				tvAllegati.setInput(affitto.getAffittiallegatis());
				tvAllegati.refresh();	
				
				TableItem ti = tvAllegati.getTable().getItem(tvAllegati.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvAllegati.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvAllegati.getTable();
				tvAllegati.getTable().notifyListeners(SWT.Selection, ev);

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ihCancellaAllegato = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancellaAllegato.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaAllegato.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaAllegato.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (tvAllegati.getSelection() != null){
					Iterator it = ((StructuredSelection)tvAllegati.getSelection()).iterator();
					while (it.hasNext()) {
						Affittiallegati aiVO = (Affittiallegati)it.next();
						affitto.removeFromAffittiallegatis(aiVO);
					}
					tvAllegati.refresh();
					
					
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		
		
		tvAllegati = new TableViewer(contenitore,SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvAllegati.getTable().setLayoutData(gdExpVHTable);
		tvAllegati.getTable().setHeaderVisible(true);
		tvAllegati.getTable().setLinesVisible(true);
		tvAllegati.getTable().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
				String pathRepositoryAllegati = (WinkhouseUtils.getInstance()
																 .getPreferenceStore()
																 .getString(WinkhouseUtils.ALLEGATIPATH)
																 .equalsIgnoreCase(""))
										        ? WinkhouseUtils.getInstance()
											       				 .getPreferenceStore()
											       				 .getDefaultString(WinkhouseUtils.ALLEGATIPATH)
											     : WinkhouseUtils.getInstance()
											       				 .getPreferenceStore()
											       				 .getString(WinkhouseUtils.ALLEGATIPATH);
										
				pathRepositoryAllegati += File.separator + "affitti";
																
				Affittiallegati aaVO = (Affittiallegati)((StructuredSelection)tvAllegati.getSelection()).getFirstElement();
				
				if ((aaVO.getFromPath() == null) ||
					(aaVO.getFromPath().equalsIgnoreCase(""))){	
					if ((aaVO.getNome() != null) &&
							(!aaVO.getNome().equalsIgnoreCase(""))){

						Program.launch(pathRepositoryAllegati +
								       File.separator +
								       aaVO.getAffitti().getCodAffitti() +
								       File.separator +
								       aaVO.getNome());
					}
				}else if((aaVO.getFromPath() != null) &&
						(!aaVO.getFromPath().equalsIgnoreCase(""))){
					Program.launch(aaVO.getFromPath());					
				}
			}
		});
		
		
		tcDescrizioneAllegato = new TableColumn(tvAllegati.getTable(),SWT.CENTER,0);
		tcDescrizioneAllegato.setWidth(100);
		tcDescrizioneAllegato.setText("Descrizione");
				
		tcPathFrom = new TableColumn(tvAllegati.getTable(),SWT.CENTER,1);
		tcPathFrom.setWidth(200);
		tcPathFrom.setText("Documento");
		
		section.setClient(contenitore);
	}
	
	private void bindAllegatiAffitto(DataBindingContext bindingContext){
		
		tvAllegati.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return (affitto.getAffittiallegatis()==null)
				       ? new ArrayList().toArray()
				       : affitto.getAffittiallegatis().toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
			
		});
		
		tvAllegati.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				Affittiallegati aaVO = (Affittiallegati)element;
				switch (columnIndex){
					case 0: return (aaVO.getDescrizione() == null)
								   ? ""
								   : aaVO.getDescrizione();
					
					case 1: return (aaVO.getNome() == null)
					   			   ? ""
					   			   : aaVO.getNome();
					
					default : return "";
				}
				
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
		
		TableViewerColumn tvcDescrizione = new TableViewerColumn(tvAllegati,tcDescrizioneAllegato);
		tvcDescrizione.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				if (((Affittiallegati)cell.getElement()).getDescrizione() != null){
					cell.setText(((Affittiallegati)cell.getElement()).getDescrizione());
				}else{
					cell.setText("");
				}
			}
			
		});
		tvcDescrizione.setEditingSupport(new EditingSupport(tvAllegati){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvAllegati.getTable());
			}

			@Override
			protected Object getValue(Object element) {
				if (((Affittiallegati)element).getDescrizione() != null){
					return ((Affittiallegati)element).getDescrizione();
				}else{
					return "";
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Affittiallegati)element).setDescrizione(String.valueOf(value));
				tvAllegati.refresh();
			}
			
		});
		
		TableViewerColumn tvcPathFrom = new TableViewerColumn(tvAllegati,tcPathFrom);
		tvcPathFrom.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				if (((Affittiallegati)cell.getElement()).getFromPath() == null){
					cell.setText(((Affittiallegati)cell.getElement()).getNome());
				}else{
					cell.setText(((Affittiallegati)cell.getElement()).getFromPath());
				}
			}
			
		});
		tvcPathFrom.setEditingSupport(new EditingSupport(tvAllegati){

			@Override
			protected boolean canEdit(Object element) {
				if (((Affittiallegati)element).getNome().equalsIgnoreCase("")){
					return true;
				}else{
					return false;
				}
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				FileDialogCellEditor fdce = new FileDialogCellEditor(tvAllegati.getTable());
				fdce.setButtonImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
				fdce.setTootTipButton("Seleziona documento");
				return fdce;
			}

			@Override
			protected Object getValue(Object element) {				
				if (((Affittiallegati)element).getFromPath() != null){
					return ((Affittiallegati)element).getFromPath();
				}else{
					return ((Affittiallegati)element).getNome();
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Affittiallegati)element).setFromPath(String.valueOf(value));
				((Affittiallegati)element).setNome(((Affittiallegati)element).getFromPath()
																	     	 .substring(((Affittiallegati)element).getFromPath()
																						   				   		  .lastIndexOf(File.separator)+1)
												      );
				tvAllegati.refresh();
			}
			
		});
		

		
		tvAllegati.setInput(new Object());
		tvAllegati.refresh();				
		
	};

	private void rateAffito(){
		
		Section section = ft.createSection(f.getBody(), 
				   						   Section.DESCRIPTION|Section.TITLE_BAR|
				   						   Section.TWISTIE);

		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				f.reflow(true);
			}
			
		});

		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;
		
		GridData gdExpVHSection = new GridData();
		gdExpVHSection.horizontalSpan = 2;
		gdExpVHSection.grabExcessHorizontalSpace = true;
		gdExpVHSection.grabExcessVerticalSpace = false;
		gdExpVHSection.horizontalAlignment = SWT.FILL;
		gdExpVHSection.verticalAlignment = SWT.FILL;	
		
		GridData gdExpVHTable = new GridData();
		gdExpVHTable.grabExcessHorizontalSpace = true;
		gdExpVHTable.grabExcessVerticalSpace = true;
		gdExpVHTable.horizontalAlignment = SWT.FILL;
		gdExpVHTable.verticalAlignment = SWT.FILL;	
		gdExpVHTable.minimumHeight = 80;
		
		section.setLayout(new GridLayout());
		section.setLayoutData(gdExpVHSection);
		section.setText("Rate affitto");
		section.setDescription("Gestione rate affitto");
		
		Composite contenitore = ft.createComposite(section,SWT.NONE);
		ft.paintBordersFor(contenitore);
		contenitore.setLayout(new GridLayout());
		contenitore.setLayoutData(gdExpVH);

		Composite toolbar = ft.createComposite(contenitore,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ihNewRata = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNewRata.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNewRata.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNewRata.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				Affittirate arModel = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Affittirate.class);
				arModel.setAffitti(affitto);
//				if (affitto.getAffittirates() == null){
//					affitto.setRate(new ArrayList<Affittirate>());
//				}
				affitto.addToAffittirates(arModel);
				tvRate.setInput(affitto.getAffittirates());
				tvRate.refresh();	
				
				TableItem ti = tvRate.getTable().getItem(tvRate.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvRate.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvRate.getTable();
				tvRate.getTable().notifyListeners(SWT.Selection, ev);

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ihCancellaRata = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancellaRata.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaRata.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaRata.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (tvRate.getSelection() != null){
					Iterator it = ((StructuredSelection)tvRate.getSelection()).iterator();
					while (it.hasNext()) {
						Affittirate aiVO = (Affittirate)it.next();
						affitto.removeFromAffittirates(aiVO);
					}
					tvRate.refresh();
					
					
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		
		
		tvRate = new TableViewer(contenitore,SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvRate.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return (affitto.getAffittirates()==null)
				       ? new ArrayList().toArray()
				       : affitto.getAffittirates().toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
			
		});
		
		tvRate.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				Affittirate arModel = (Affittirate)element;
				switch (columnIndex){
					case 0: return arModel.getNomeMese();
					
					case 1: return (arModel.getAnagrafiche() != null)?arModel.getAnagrafiche().toString():"";
					
					case 2: return formatter.format(arModel.getScadenza());
					
					case 3: return (arModel.getDatapagato() != null)?formatter.format(arModel.getDatapagato()):"";
					
					case 4: return String.valueOf(arModel.getImporto());					
					
					default : return "";
				}
				
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
		
		tvRate.getTable().setLayoutData(gdExpVHTable);
		tvRate.getTable().setHeaderVisible(true);
		tvRate.getTable().setLinesVisible(true);
		tvRate.getTable().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}
		});
		
		tcRataMese = new TableColumn(tvRate.getTable(),SWT.CENTER,0);
		tcRataMese.setWidth(100);
		tcRataMese.setText("Mese rata");
		
		tcRataAnagrafica = new TableColumn(tvRate.getTable(),SWT.CENTER,1);
		tcRataAnagrafica.setWidth(200);
		tcRataAnagrafica.setToolTipText("Lista delle anagrafiche salvate nella sezione superiore");
		tcRataAnagrafica.setText("anagrafica");

		tcRataScadenza = new TableColumn(tvRate.getTable(),SWT.CENTER,2);
		tcRataScadenza.setWidth(100);
		tcRataScadenza.setText("Scadenza rata");

		tcRataDataPagato = new TableColumn(tvRate.getTable(),SWT.CENTER,3);
		tcRataDataPagato.setWidth(100);
		tcRataDataPagato.setText("Data pagamento");
		
		tcRataImporto = new TableColumn(tvRate.getTable(),SWT.CENTER,4);
		tcRataImporto.setWidth(100);
		tcRataImporto.setText("Importo rata");
		 
		
		section.setClient(contenitore);
	}	

	private void bindRateAffitto(DataBindingContext bindingContext){
		
		
		TableViewerColumn tvcRataMese = new TableViewerColumn(tvRate,tcRataMese);
		tvcRataMese.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Affittirate)cell.getElement()).getNomeMese());
/*				if (((Affittirate)cell.getElement()).getNomeMese() != null){
					cell.setText(((Affittirate)cell.getElement()).getNomeMese());
				}else{
					cell.setText("");
				}*/
			}
			
		});
		tvcRataMese.setEditingSupport(new EditingSupport(tvRate){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				ComboBoxCellEditor cbce = new ComboBoxCellEditor(tvRate.getTable(),
																 desMesi,
						 										 SWT.READ_ONLY|SWT.DROP_DOWN);												
				return cbce;

			}

			@Override
			protected Object getValue(Object element) {
				return ((Affittirate)element).getMese();
				/*
				if (((Affittirate)element).getNomeMese() != null){
					return ((Affittirate)element).getNomeMese();
				}else{
					return 0;
				}*/
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Affittirate)element).setMese((Integer)value);
				tvRate.refresh();
			}
			
		});
		
		TableViewerColumn tvcRataScadenza = new TableViewerColumn(tvRate,tcRataScadenza);
		tvcRataScadenza.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				if (((Affittirate)cell.getElement()).getScadenza() != null){
					try {
						cell.setText(formatterIT.format(((Affittirate)cell.getElement()).getScadenza()));
					} catch (Exception e) {
						cell.setText("");
					}															
				}else{
					cell.setText("");
				}
			}
			
		});
		tvcRataScadenza.setEditingSupport(new EditingSupport(tvRate){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
			
				Calendar c = null;
				if (((Affittirate)element).getScadenza() != null){
					try {
						c = Calendar.getInstance(Locale.ITALIAN);
						c.setTime(Date.from(((Affittirate)element).getScadenza().atZone(ZoneId.systemDefault()).toInstant()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				SWTCalendarCellEditor fdce = new SWTCalendarCellEditor(tvRate.getTable(),c);
				return fdce;
			}

			@Override
			protected Object getValue(Object element) {				
				if (((Affittirate)element).getScadenza() != null){
					return formatterIT.format(((Affittirate)element).getScadenza());
				}else{
					return formatterIT.format(new Date());
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				try {
					((Affittirate)element).setScadenza(LocalDateTime.ofInstant(((Calendar)value).getTime().toInstant(), ZoneId.systemDefault()));
				} catch (Exception e) {
					((Affittirate)element).setScadenza(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));;
				}
				tvRate.refresh();
			}
			
		});

		TableViewerColumn tvcRataDataPagato = new TableViewerColumn(tvRate,tcRataDataPagato);
		tvcRataDataPagato.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				if (((Affittirate)cell.getElement()).getDatapagato() != null){
					cell.setText(formatterIT.format(((Affittirate)cell.getElement()).getDatapagato()));
				}else{
					cell.setText("");
				}
			}
			
		});
		tvcRataDataPagato.setEditingSupport(new EditingSupport(tvRate){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				Calendar c = null;
				if (((Affittirate)element).getDatapagato() != null){
					try {
						c = Calendar.getInstance(Locale.ITALIAN);
						c.setTime(Date.from(((Affittirate)element).getDatapagato().atZone(ZoneId.systemDefault()).toInstant()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				SWTCalendarCellEditor fdce = new SWTCalendarCellEditor(tvRate.getTable(),c);
				return fdce;
			}

			@Override
			protected Object getValue(Object element) {				
				if (((Affittirate)element).getDatapagato() != null){
					return formatterIT.format(((Affittirate)element).getDatapagato());
				}else{
					return formatterIT.format(new Date());
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				try {
					((Affittirate)element).setDatapagato(LocalDateTime.ofInstant(((Calendar)value).getTime().toInstant(), ZoneId.systemDefault()));
				} catch (Exception e) {
					((Affittirate)element).setDatapagato(null);
				}
				tvRate.refresh();
			}
			
		});
		
		TableViewerColumn tvcRataImporto = new TableViewerColumn(tvRate,tcRataImporto);
		tvcRataImporto.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				if (((Affittirate)cell.getElement()).getImporto() != 0.0){
					cell.setText(String.valueOf(((Affittirate)cell.getElement()).getImporto()));
				}else{
					cell.setText("0.0");
				}
			}
			
		});
		tvcRataImporto.setEditingSupport(new EditingSupport(tvRate){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvRate.getTable());
			}

			@Override
			protected Object getValue(Object element) {				
				if (((Affittirate)element).getImporto() != 0.0){
					return String.valueOf(((Affittirate)element).getImporto());
				}else{
					return String.valueOf(0.0);
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				try {
					((Affittirate)element).setImporto(Double.parseDouble((String)value));
				} catch (NumberFormatException e) {
					((Affittirate)element).setImporto(0.0);
				}
				tvRate.refresh();
			}
			
		});		
		
		TableViewerColumn tvcRataAnagrafica = new TableViewerColumn(tvRate,tcRataAnagrafica);
		tvcRataAnagrafica.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				if (((Affittirate)cell.getElement()).getAnagrafiche() != null){
					cell.setText(((Affittirate)cell.getElement()).getAnagrafiche().toString());
				}else{
					cell.setText("");
				}
			}
			
		});
		tvcRataAnagrafica.setEditingSupport(new EditingSupport(tvRate){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				fillDescrizioniAnagrafiche();
				ComboBoxCellEditor cbce = new ComboBoxCellEditor(tvRate.getTable(),
						 										 desAnagrafiche,
						 										 SWT.READ_ONLY|SWT.DROP_DOWN);
				return cbce;

			}

			@Override
			protected Object getValue(Object element) {				
				if (((Affittirate)element).getAnagrafiche() != null){
					int index = Collections.binarySearch(workAnagrafiche, ((Affittirate)element).getAnagrafiche(), comparatorAnagrafiche);
					return (index >= 0)?index:0;
				}else{
					return 0;
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (((Integer)value).intValue() > -1){
					((Affittirate)element).setAnagrafiche(workAnagrafiche.get((Integer)value));					
		        }
				tvRate.refresh();
			}
			
		});
		
		tvRate.setInput(new Object());
		tvRate.refresh();				
		
	};	

	private void speseAffito(){
		
		Section section = ft.createSection(f.getBody(), 
				   						   Section.DESCRIPTION|Section.TITLE_BAR|
				   						   Section.TWISTIE);

		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				f.reflow(true);
			}
			
		});

		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;
		
		GridData gdExpVHSection = new GridData();
		gdExpVHSection.horizontalSpan = 2;
		gdExpVHSection.grabExcessHorizontalSpace = true;
		gdExpVHSection.grabExcessVerticalSpace = false;
		gdExpVHSection.horizontalAlignment = SWT.FILL;
		gdExpVHSection.verticalAlignment = SWT.FILL;	
		
		GridData gdExpVHTable = new GridData();
		gdExpVHTable.grabExcessHorizontalSpace = true;
		gdExpVHTable.grabExcessVerticalSpace = true;
		gdExpVHTable.horizontalAlignment = SWT.FILL;
		gdExpVHTable.verticalAlignment = SWT.FILL;	
		gdExpVHTable.minimumHeight = 80;
		
		section.setLayout(new GridLayout());
		section.setLayoutData(gdExpVHSection);
		section.setText("Spese affitto");
		section.setDescription("Gestione spese affitto");
		
		Composite contenitore = ft.createComposite(section,SWT.NONE);
		ft.paintBordersFor(contenitore);
		contenitore.setLayout(new GridLayout());
		contenitore.setLayoutData(gdExpVH);

		Composite toolbar = ft.createComposite(contenitore,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ihNewSpese = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNewSpese.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNewSpese.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNewSpese.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				Affittispese asModel = affitto.getObjectContext().newObject(Affittispese.class);
				
//				if (affitto.getAffittispeses() == null){
//					affitto.setSpese(new ArrayList<Affittispese>());
//				}
				affitto.addToAffittispeses(asModel);
				tvSpese.setInput(affitto.getAffittispeses());
				tvSpese.refresh();	
				
				TableItem ti = tvSpese.getTable().getItem(tvSpese.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvSpese.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvSpese.getTable();
				tvSpese.getTable().notifyListeners(SWT.Selection, ev);

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ihCancellaSpese = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancellaSpese.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaSpese.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaSpese.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (tvSpese.getSelection() != null){
					Iterator it = ((StructuredSelection)tvSpese.getSelection()).iterator();
					while (it.hasNext()) {
						Affittispese asModel = (Affittispese)it.next();
						affitto.removeFromAffittispeses(asModel);
					}
					tvSpese.refresh();
					
					
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		
		
		tvSpese = new TableViewer(contenitore,SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvSpese.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return (affitto.getAffittispeses()==null)
				       ? new ArrayList().toArray()
				       : affitto.getAffittispeses().toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
			
		});
		
		tvSpese.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				Affittispese asModel = (Affittispese)element;
				switch (columnIndex){
					case 0: return asModel.getDescrizione();
					
					case 1: return (asModel.getAnagrafiche() != null)?asModel.getAnagrafiche().toString():"";
					
					case 2: return formatter.format(asModel.getDatainizio());
					
					case 3: return formatter.format(asModel.getDatafine());
					
					case 4: return formatter.format(asModel.getScadenza());
					
					case 5: return String.valueOf(asModel.getImporto());
					
					case 6: return String.valueOf(asModel.getVersato());
					
					case 7: return ((asModel.getDatapagato() != null)?formatter.format(asModel.getDatapagato()):"");					
					
					default : return "";
				}
				
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
		
		tvSpese.getTable().setLayoutData(gdExpVHTable);
		tvSpese.getTable().setHeaderVisible(true);
		tvSpese.getTable().setLinesVisible(true);
		tvSpese.getTable().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}
		});

		tcDescrizioneSpesa = new TableColumn(tvSpese.getTable(),SWT.CENTER,0);
		tcDescrizioneSpesa.setWidth(150);
		tcDescrizioneSpesa.setText("Descrizione spesa");
		
		tcSpeseAnagrafica = new TableColumn(tvSpese.getTable(),SWT.CENTER,1);
		tcSpeseAnagrafica.setWidth(200);
		tcSpeseAnagrafica.setText("anagrafica");
		
		tcSpeseInizioPeriodo = new TableColumn(tvSpese.getTable(),SWT.CENTER,2);
		tcSpeseInizioPeriodo.setWidth(100);
		tcSpeseInizioPeriodo.setText("Data inizio periodo");

		tcSpeseFinePeriodo = new TableColumn(tvSpese.getTable(),SWT.CENTER,3);
		tcSpeseFinePeriodo.setWidth(100);
		tcSpeseFinePeriodo.setText("Data fine periodo");

		tcSpeseScadenza = new TableColumn(tvSpese.getTable(),SWT.CENTER,4);
		tcSpeseScadenza.setWidth(100);
		tcSpeseScadenza.setText("Scadenza");
		
		tcSpeseImporto = new TableColumn(tvSpese.getTable(),SWT.CENTER,5);
		tcSpeseImporto.setWidth(100);
		tcSpeseImporto.setText("Importo spesa");

		tcSpeseVersato = new TableColumn(tvSpese.getTable(),SWT.CENTER,5);
		tcSpeseVersato.setWidth(100);
		tcSpeseVersato.setText("Importo versato");
		
		tcSpeseDataPagato = new TableColumn(tvSpese.getTable(),SWT.CENTER,7);
		tcSpeseDataPagato.setWidth(100);
		tcSpeseDataPagato.setText("Data pagamento");
				
		section.setClient(contenitore);
	}	

	private void bindSpeseAffitto(DataBindingContext bindingContext){
		
		TableViewerColumn tvcDescrizioneSpese = new TableViewerColumn(tvSpese,tcDescrizioneSpesa);
		tvcDescrizioneSpese.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() != null){
					cell.setText(((Affittispese)cell.getElement()).getDescrizione());
				}
			}
			
		});
		tvcDescrizioneSpese.setEditingSupport(new EditingSupport(tvSpese){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvSpese.getTable());
			}

			@Override
			protected Object getValue(Object element) {
				return ((Affittispese)element).getDescrizione();
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Affittispese)element).setDescrizione((String)value);
				tvSpese.refresh();
			}
			
		});
		
		TableViewerColumn tvcSpeseInizioPeriodo = new TableViewerColumn(tvSpese,tcSpeseInizioPeriodo);
		tvcSpeseInizioPeriodo.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				if (((Affittispese)cell.getElement()).getDatainizio() != null){
					cell.setText(formatterIT.format(((Affittispese)cell.getElement()).getDatainizio()));
				}else{
					cell.setText("");
				}

			}
			
		});
		
		tvcSpeseInizioPeriodo.setEditingSupport(new EditingSupport(tvSpese){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				Calendar c = null;
				if (((Affittispese)element).getScadenza() != null){
					try {
						c = Calendar.getInstance(Locale.ITALIAN);
						c.setTime(Date.from(((Affittispese)element).getDatainizio().atZone(ZoneId.systemDefault()).toInstant()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				SWTCalendarCellEditor fdce = new SWTCalendarCellEditor(tvSpese.getTable(),c);
				return fdce;
			}

			@Override
			protected Object getValue(Object element) {
				if (((Affittispese)element).getDatainizio()!= null){
					return formatterIT.format(((Affittispese)element).getDatainizio());
				}else{
					return formatterIT.format(new Date());
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (value instanceof String){
					try {
						((Affittispese)element).setDatainizio(LocalDateTime.ofInstant(formatterIT.parse((String)value).toInstant(), ZoneId.systemDefault()));
					} catch (ParseException e) {
						((Affittispese)element).setDatainizio(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
					}
				}
				if (value instanceof Calendar){
					try {
						((Affittispese)element).setDatainizio(LocalDateTime.ofInstant(((Calendar)value).getTime().toInstant(), ZoneId.systemDefault()));
					} catch (Exception e) {
						((Affittispese)element).setDatainizio(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
					}					
				}				
				tvSpese.refresh();
			}
			
		});
		
		TableViewerColumn tvcSpeseFinePeriodo = new TableViewerColumn(tvSpese,tcSpeseFinePeriodo);
		tvcSpeseFinePeriodo.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				if (((Affittispese)cell.getElement()).getDatafine() != null){
					cell.setText(formatterIT.format(((Affittispese)cell.getElement()).getDatafine()));
				}else{
					cell.setText("");
				}

			}
			
		});
		tvcSpeseFinePeriodo.setEditingSupport(new EditingSupport(tvSpese){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				Calendar c = null;
				if (((Affittispese)element).getScadenza() != null){
					try {
						c = Calendar.getInstance(Locale.ITALIAN);
						c.setTime(Date.from(((Affittispese)element).getDatafine().atZone(ZoneId.systemDefault()).toInstant()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				SWTCalendarCellEditor fdce = new SWTCalendarCellEditor(tvSpese.getTable(),c);
				return fdce;
			}

			@Override
			protected Object getValue(Object element) {
				if (((Affittispese)element).getDatafine()!= null){
					return formatterIT.format(((Affittispese)element).getDatafine());
				}else{
					return formatterIT.format(new Date());
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (value instanceof String){
					try {
						((Affittispese)element).setDatafine(LocalDateTime.ofInstant(formatterIT.parse((String)value).toInstant(),ZoneId.systemDefault()));
					} catch (ParseException e) {
						((Affittispese)element).setDatafine(LocalDateTime.ofInstant(new Date().toInstant(),ZoneId.systemDefault()));
					}
				}
				if (value instanceof Calendar){
					try {
						((Affittispese)element).setDatafine(LocalDateTime.ofInstant(((Calendar)value).getTime().toInstant(),ZoneId.systemDefault()));
					} catch (Exception e) {
						((Affittispese)element).setDatafine(LocalDateTime.ofInstant(new Date().toInstant(),ZoneId.systemDefault()));
					}					
				}				
				tvSpese.refresh();
			}
			
		});

		TableViewerColumn tvcSpeseScadenza = new TableViewerColumn(tvSpese,tcSpeseScadenza);
		tvcSpeseScadenza.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				if (((Affittispese)cell.getElement()).getScadenza() != null){
					cell.setText(formatterIT.format(((Affittispese)cell.getElement()).getScadenza()));
				}else{
					cell.setText("");
				}

			}
			
		});
		tvcSpeseScadenza.setEditingSupport(new EditingSupport(tvSpese){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				Calendar c = null;
				if (((Affittispese)element).getScadenza() != null){
					try {
						c = Calendar.getInstance(Locale.ITALIAN);
						c.setTime(Date.from(((Affittispese)element).getScadenza().atZone(ZoneId.systemDefault()).toInstant()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				SWTCalendarCellEditor fdce = new SWTCalendarCellEditor(tvSpese.getTable(),c);
				return fdce;
			}

			@Override
			protected Object getValue(Object element) {
				if (((Affittispese)element).getScadenza()!= null){
					return formatterIT.format(((Affittispese)element).getScadenza());
				}else{
					return formatterIT.format(new Date());
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (value instanceof String){
					try {
						((Affittispese)element).setScadenza(LocalDateTime.ofInstant(formatterIT.parse((String)value).toInstant(),ZoneId.systemDefault()));
					} catch (ParseException e) {
						((Affittispese)element).setScadenza(LocalDateTime.ofInstant(new Date().toInstant(),ZoneId.systemDefault()));
					}
				}
				if (value instanceof Calendar){
					try {
						((Affittispese)element).setScadenza(LocalDateTime.ofInstant(((Calendar)value).getTime().toInstant(),ZoneId.systemDefault()));
					} catch (Exception e) {
						((Affittispese)element).setScadenza(LocalDateTime.ofInstant(new Date().toInstant(),ZoneId.systemDefault()));
					}					
				}				
				tvSpese.refresh();
			}
			
		});
		
		TableViewerColumn tvcSpeseImporto = new TableViewerColumn(tvSpese,tcSpeseImporto);
		tvcSpeseImporto.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				if (((Affittispese)cell.getElement()).getImporto() != 0.0){
					cell.setText(String.valueOf(((Affittispese)cell.getElement()).getImporto()));
				}else{
					cell.setText("0.0");
				}
			}
			
		});
		tvcSpeseImporto.setEditingSupport(new EditingSupport(tvSpese){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvSpese.getTable());
			}

			@Override
			protected Object getValue(Object element) {				
				if (((Affittispese)element).getImporto() != 0.0){
					return String.valueOf(((Affittispese)element).getImporto());
				}else{
					return String.valueOf(0.0);
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				try {
					((Affittispese)element).setImporto(Double.parseDouble((String)value));
				} catch (NumberFormatException e) {
					((Affittispese)element).setImporto(0.0);
				}
				tvSpese.refresh();
			}
			
		});		

		TableViewerColumn tvcSpeseVersato = new TableViewerColumn(tvSpese,tcSpeseVersato);
		tvcSpeseVersato.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				if (((Affittispese)cell.getElement()).getVersato() != 0.0){
					cell.setText(String.valueOf(((Affittispese)cell.getElement()).getVersato()));
				}else{
					cell.setText("0.0");
				}
			}
			
		});
		tvcSpeseVersato.setEditingSupport(new EditingSupport(tvSpese){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvSpese.getTable());
			}

			@Override
			protected Object getValue(Object element) {				
				if (((Affittispese)element).getVersato() != 0.0){
					return String.valueOf(((Affittispese)element).getVersato());
				}else{
					return String.valueOf(0.0);
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				try {
					((Affittispese)element).setVersato(Double.parseDouble((String)value));
				} catch (NumberFormatException e) {
					((Affittispese)element).setVersato(0.0);
				}
				tvSpese.refresh();
			}
			
		});		

		TableViewerColumn tvcSpeseDataPagato = new TableViewerColumn(tvSpese,tcSpeseDataPagato);
		tvcSpeseDataPagato.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				if (((Affittispese)cell.getElement()).getDatapagato() != null){
					cell.setText(formatterIT.format(((Affittispese)cell.getElement()).getDatapagato()));
				}else{
					cell.setText("");
				}
			}
			
		});
		tvcSpeseDataPagato.setEditingSupport(new EditingSupport(tvSpese){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				Calendar c = null;
				if (((Affittispese)element).getScadenza() != null){
					try {
						if (((Affittispese)element).getDatapagato() != null){
							c = Calendar.getInstance(Locale.ITALIAN);
							c.setTime(Date.from(((Affittispese)element).getDatapagato().atZone(ZoneId.systemDefault()).toInstant()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				SWTCalendarCellEditor fdce = new SWTCalendarCellEditor(tvSpese.getTable(),c);
				return fdce;
			}

			@Override
			protected Object getValue(Object element) {				
				if (((Affittispese)element).getDatapagato()!= null){
					return formatterIT.format(((Affittispese)element).getDatapagato());
				}else{
					return formatterIT.format(new Date());
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (value instanceof String){
					try {
						((Affittispese)element).setDatapagato(LocalDateTime.ofInstant(formatterIT.parse((String)value).toInstant(),ZoneId.systemDefault()));
					} catch (ParseException e) {
						((Affittispese)element).setDatapagato(null);
					}
				}
				if (value instanceof Calendar){
					try {
						((Affittispese)element).setDatapagato(LocalDateTime.ofInstant(((Calendar)value).getTime().toInstant(),ZoneId.systemDefault()));
					} catch (Exception e) {
						((Affittispese)element).setDatapagato(null);
					}					
				}				
				tvSpese.refresh();
			}
			
		});
		
		TableViewerColumn tvcSpeseAnagrafica = new TableViewerColumn(tvSpese,tcSpeseAnagrafica);
		tvcSpeseAnagrafica.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				if (((Affittispese)cell.getElement()).getAnagrafiche() != null){
					cell.setText(((Affittispese)cell.getElement()).getAnagrafiche().toString());
				}else{
					cell.setText("");
				}
			}
			
		});
		tvcSpeseAnagrafica.setEditingSupport(new EditingSupport(tvSpese){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				fillDescrizioniAnagrafiche();
				ComboBoxCellEditor cbce = new ComboBoxCellEditor(tvSpese.getTable(),
						 										 desAnagrafiche,
						 										 SWT.READ_ONLY|SWT.DROP_DOWN);
				return cbce;

			}

			@Override
			protected Object getValue(Object element) {				
				if (((Affittispese)element).getAnagrafiche() != null){
					int index = Collections.binarySearch(workAnagrafiche, 
														 ((Affittispese)element).getAnagrafiche(), 
														 comparatorAnagrafiche);
					return (index >= 0)?index:0;
				}else{
					return 0;
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				if(((Integer)value).intValue() > -1){
					((Affittispese)element).setAnagrafiche(workAnagrafiche.get((Integer)value));
				}
				tvSpese.refresh();
			}
			
		});
		
		tvSpese.setInput(new Object());
		tvSpese.refresh();				
		
	};	

	public void setCompareView(boolean enabled){
		
		saa.setEnabled(!enabled);
		caa.setEnabled(!enabled);
		
		ihNewAnagrafica.setEnabled(!enabled);
		//ihNewInsAnagrafica.setEnabled(!enabled);
		ihCancellaAnagrafica.setEnabled(!enabled);
		ihNewAllegato.setEnabled(!enabled);
		ihCancellaAllegato.setEnabled(!enabled);		
		ihNewRata.setEnabled(!enabled);
		ihCancellaRata.setEnabled(!enabled);		
		ihNewSpese.setEnabled(!enabled);
		ihCancellaSpese.setEnabled(!enabled);		

		tvAllegati.getTable().setEnabled(!enabled);
		cvagenteinseritoreanagrafica.getCombo().setEnabled(!enabled);
		dataInizio.setEnabled(!enabled);
		dataFine.setEnabled(!enabled);
		cauzione.setEnabled(!enabled);
		rata.setEnabled(!enabled);
		descrizione.setEnabled(!enabled);
		tvAnagrafiche.getTable().setEnabled(!enabled);
		tvRecapitiAnagrafiche.getTable().setEnabled(!enabled);		
		tvRate.getTable().setEnabled(!enabled);
		tvSpese.getTable().setEnabled(!enabled);

		isInCompareMode = enabled;

	}

}
