package winkhouse.view.colloqui;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
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
import winkhouse.action.colloqui.CancellaColloquio;
import winkhouse.action.colloqui.RemoveICALUIDAction;
import winkhouse.action.colloqui.SalvaColloquio;
import winkhouse.action.immobili.OpenPopUpImmobili;
import winkhouse.action.stampa.StampaColloquiAction;
import winkhouse.configuration.EnvSettingsFactory;
import winkhouse.dialogs.custom.FileDialogCellEditor;
import winkhouse.engine.search.SearchEngineImmobili;
import winkhouse.helper.ProfilerHelper;
import winkhouse.helper.RicercheHelper;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiAgentiModel_Age;
import winkhouse.model.ColloquiAnagraficheModel_Ang;
import winkhouse.model.ColloquiCriteriRicercaModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.RicercheModel;
import winkhouse.util.CriteriaTableUtilsFactory;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.ObjectSearchGetters;
import winkhouse.view.affitti.ListaAffittiView;
import winkhouse.view.agenda.ListaAppuntamentiView;
import winkhouse.view.agenti.PopUpRicercaAgenti;
import winkhouse.view.anagrafica.ImmobiliPropietaView;
import winkhouse.view.anagrafica.PopUpRicercaAnagrafica;
import winkhouse.view.common.AbbinamentiView;
import winkhouse.view.common.ColloquiView;
import winkhouse.view.common.EAVView;
import winkhouse.view.common.RecapitiView;
import winkhouse.view.immobili.ImmaginiImmobiliView;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AllegatiColloquiVO;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.vo.RicercheVO;
import winkhouse.vo.TipologieColloquiVO;
import winkhouse.wizard.PopUpEditRicerca;
import winkhouse.wizard.PopUpRicercaRicerche;



public class DettaglioColloquioView extends ViewPart {

	public final static String ID = "winkhouse.dettagliocolloquioview";
	
	private ComboViewer c_agenteinseritore = null;
	private ComboViewer c_tipologiacolloquio = null;
	private CalendarCombo dcdataincontro = null;
	private Text oraincontro = null;
	private Text luogo = null;
	private Text immobile = null;
	private Text descrizione = null;
	private Text varieAgenzia = null;
	private Text varieClienti = null;
	private Text datainserimento = null;
	private Button inagenda = null;
	
	private TableViewer tvAnagrafiche = null;
	private TableViewer tvCriteri = null;
	private TableViewer tvAgenti = null;
	private TableViewer tvAllegati = null;
	
	private FormToolkit ft = null;
	private ScrolledForm f = null;
	
	private Section sectionAgenti = null;
	private Section sectionAllegati = null;
	private Section sectionAnagrafiche = null;
	private Section sectionCriteri = null;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
	private SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	private ColloquiModel colloquio = null;
	private String[] desGetters = null;
	private String[] desTipologiaImmobile = null;
	private Integer[] codTipologiaImmobile = null;
	private String[] desStatoConservativo = null;
	private Integer[] codStatoConservativo = null;
	private String[] desRiscaldamenti = null;
	private Integer[] codRiscaldamenti = null;
	private String[] desAgenti = null;
	private Integer[] codAgenti = null;
	private SimpleDateFormat formatterIT = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatterENG = new SimpleDateFormat("yyyy-MM-dd");

	private OpenPopUpImmobili opi = null;
	private SalvaColloquio salvaColloquio = null;
	private CancellaColloquio cancellaColloquio = null;
	private RemoveICALUIDAction removeICALUIDAction = null;

	private ImageHyperlink ihNewAnagrafica = null;
	private ImageHyperlink ihCancellaAnagrafica = null;		
	private ImageHyperlink ihNewAllegati = null;
	private ImageHyperlink ihCancellaAllegati = null;		
	private ImageHyperlink ihNewCriterio = null;
	private ImageHyperlink ihCancellaCriterio = null;
	private ImageHyperlink ihNewAgenti = null;
	private ImageHyperlink ihCancellaAgenti = null;		
	private ImageDescriptor noGCalImg = Activator.getImageDescriptor("icons/no_google_calendar.png");
	private ImageDescriptor gCalImg = Activator.getImageDescriptor("icons/google_calendar.png");
	private Image colloquioGCalImg = Activator.getImageDescriptor("icons/colloqui-gcal.png").createImage();
	private Image colloquioImg = Activator.getImageDescriptor("icons/colloqui.png").createImage();
	
	private Label lrisultatoCriteri = null;

	private RicercheModel ricerca = null;
	
	private Label lRicercaSelectedName = null;

	private boolean isInCompareMode = false;	
	
	private Comparator<TipologieColloquiVO> comparatorTipologia = new Comparator<TipologieColloquiVO>(){

		@Override
		public int compare(TipologieColloquiVO arg0,TipologieColloquiVO arg1) {
			if (arg0.getCodTipologiaColloquio().intValue() == arg1.getCodTipologiaColloquio().intValue()){
				return 0;
			}else if (arg0.getCodTipologiaColloquio().intValue() > arg1.getCodTipologiaColloquio().intValue()){
				return 1;
			}else{
				return -1;
			}				
		}
		
	};
	
	
	private Comparator<AgentiVO> comparatorAgenti = new Comparator<AgentiVO>(){

		@Override
		public int compare(AgentiVO arg0,AgentiVO arg1) {
			if (arg0.getCodAgente().intValue() == arg1.getCodAgente().intValue()){
				return 0;
			}else if (arg0.getCodAgente().intValue() > arg1.getCodAgente().intValue()){
				return 1;
			}else{
				return -1;
			}				
		}
		
	};
	
	public DettaglioColloquioView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		
		ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createScrolledForm(parent);
		f.setExpandVertical(true);
		f.setImage(Activator.getImageDescriptor("/icons/colloqui.png").createImage());
		f.setText("Dettaglio colloquio");
		f.getBody().setLayout(new GridLayout());
		
		IToolBarManager mgr = f.getToolBarManager();
		salvaColloquio = new SalvaColloquio();
		mgr.add(salvaColloquio);
		cancellaColloquio = new CancellaColloquio();
		mgr.add(cancellaColloquio);
		removeICALUIDAction = new RemoveICALUIDAction(null,Action.AS_CHECK_BOX);
		mgr.add(removeICALUIDAction);
		opi = new OpenPopUpImmobili();
		opi.setImageDescriptor(Activator.getImageDescriptor("icons/gohome.png"));
		mgr.add(opi);
		mgr.add(new StampaColloquiAction("Report colloqui", 
				    					 Action.AS_DROP_DOWN_MENU));
		
		
		f.updateToolBar();
		
		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;
		
		
		GridData gdExpVH2 = new GridData();
		gdExpVH2.grabExcessHorizontalSpace = true;
		gdExpVH2.grabExcessVerticalSpace = true;
		gdExpVH2.horizontalAlignment = SWT.FILL;
		gdExpVH2.verticalAlignment = SWT.FILL;
		gdExpVH2.horizontalSpan = 2;
		
		GridData gdExpH = new GridData();
		gdExpH.grabExcessHorizontalSpace = true;		
		gdExpH.horizontalAlignment = SWT.FILL;
		gdExpH.verticalAlignment = SWT.TOP;
		gdExpH.minimumHeight = 25;

		GridData gdExpHbottom = new GridData();
		gdExpHbottom.grabExcessHorizontalSpace = true;		
		gdExpHbottom.horizontalAlignment = SWT.FILL;
		gdExpHbottom.verticalAlignment = SWT.BOTTOM;
		gdExpHbottom.minimumHeight = 25;


		GridData gdExpH2 = new GridData();
		gdExpH2.grabExcessHorizontalSpace = true;		
		gdExpH2.horizontalAlignment = SWT.FILL;
		gdExpH2.minimumHeight = 25;
		gdExpH2.horizontalSpan = 2;
		
		Composite cdata = ft.createComposite(f.getBody());
		cdata.setLayoutData(gdExpVH);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.verticalSpacing = 7;
		cdata.setLayout(gl);
		ft.paintBordersFor(cdata);
		Label l_agenteinseritore = ft.createLabel(cdata,"Agente inseritore");		

		Label l_tipocolloquio = ft.createLabel(cdata,"Tipologia colloquio");		
		
		c_agenteinseritore = new ComboViewer(cdata);
		c_agenteinseritore.getCombo().setLayoutData(gdExpH);
		
		c_tipologiacolloquio = new ComboViewer(cdata);
		c_tipologiacolloquio.getCombo().setLayoutData(gdExpH);
		
		Label l_Data = ft.createLabel(cdata,"Data/Ora");

//		l_Data.setLayoutData(gdExpH2);
		Label l_datainserimento = ft.createLabel(cdata, "Data inserimento");
		
		GridData gdH2 = new GridData();
		gdH2.grabExcessHorizontalSpace = true;		
		gdH2.horizontalAlignment = SWT.FILL;
		gdH2.verticalAlignment = SWT.TOP;
		gdH2.heightHint = 15;
		gdH2.horizontalSpan = 2;

		
		Composite cDataIncontro = new Composite(cdata,SWT.NONE);		
		cDataIncontro.setBackground(new Color(null,255,255,255));
		GridLayout glDataIncontro = new GridLayout();
		glDataIncontro.numColumns = 3;
		glDataIncontro.marginWidth = 0;
		glDataIncontro.marginTop = 0;
		glDataIncontro.marginBottom = 0;
		glDataIncontro.marginHeight = 0;
		glDataIncontro.marginLeft = 2;
		ft.paintBordersFor(cDataIncontro);
		cDataIncontro.setLayout(glDataIncontro);
//		cDataIncontro.setLayoutData(gdH2);
		
		GridData gdExpData = new GridData();
		gdExpData.grabExcessHorizontalSpace = true;		
		gdExpData.horizontalAlignment = SWT.FILL;
		gdExpData.verticalAlignment = SWT.TOP;
		gdExpData.minimumHeight = 100;
		
		
		cDataIncontro.setLayoutData(gdExpData);
		
		dcdataincontro = new CalendarCombo(cDataIncontro,SWT.READ_ONLY|SWT.DOUBLE_BUFFERED);		
		GridData dfdcdata = new GridData();
		dfdcdata.minimumWidth = 200;
		dfdcdata.widthHint = 200;		
		dcdataincontro.setLayoutData(dfdcdata);
		dcdataincontro.addCalendarListener(new ICalendarListener() {
			
			@Override
			public void popupClosed() {}
			
			@Override
			public void dateRangeChanged(Calendar arg0, Calendar arg1) {}
			
			@Override
			public void dateChanged(Calendar arg0) {
				try {
					Date tmp = formatterDateTime.parse(formatter.format(arg0.getTime()) + " " + oraincontro.getText());
					colloquio.setDataColloquio(tmp);
				} catch (ParseException e) {
					colloquio.setDataColloquio(new Date());
				}
				
			}
		});
		oraincontro = ft.createText(cDataIncontro, "", SWT.DOUBLE_BUFFERED);
		oraincontro.setText(formatterTime.format(new Date()));
		oraincontro.setTextLimit(5);
		oraincontro.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
		
				
				try {
//					System.out.println(formatterDateTime.parse(dcdataincontro.getDateAsString() + " " + oraincontro.getText()));
					Date tmp = formatterDateTime.parse(dcdataincontro.getDateAsString() + " " + oraincontro.getText());
	//				System.out.println(tmp.toString());
					colloquio.setDataColloquio(tmp);
				} catch (ParseException e1) {					
					colloquio.setDataColloquio(new Date());
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				
			}
		});
		oraincontro.addVerifyListener(new VerifyListener(){

			@Override
			public void verifyText(VerifyEvent e) {
				
				System.out.println(e.keyCode);
				if (e.keyCode == 0){
					e.doit = true;
				}else if (e.keyCode == 127){
					e.doit = true;
				}else if(e.keyCode == 8){
					e.doit = true;
				}else{
					if(e.start == 0){
						try {
							int uno = Integer.valueOf(String.valueOf(e.character)).intValue();
							if (uno > 2){
								e.doit = false;
							}else{
								e.doit = true;
							}
						} catch (NumberFormatException e1) {							
							e.doit = false;
						}
					}
					if(e.start == 1){
						try {
							int due = Integer.valueOf(String.valueOf(e.character)).intValue();
							e.doit = true;
						} catch (NumberFormatException e1) {
							e.doit = false;
						}
					}
					
					if(e.start == 2){
						if (e.character != ':'){
							e.doit = false;
						}else{
							e.doit = true;
						}
					}
					if(e.start == 3){
						try {
							int tre = Integer.valueOf(String.valueOf(e.character)).intValue();
							if (tre > 5){							
								e.doit = false;
							}else{
								e.doit = true;
							}
						} catch (NumberFormatException e1) {
							oraincontro.setText(oraincontro.getText(0, 2) + "0");
							e.doit = false;
						}
					}
					if(e.start == 4){
						try {
							int quattro = Integer.valueOf(String.valueOf(e.character)).intValue();
							e.doit = true;
						} catch (NumberFormatException e1) {
							oraincontro.setText(oraincontro.getText(0, 3) + "0");
							e.doit = false;
						}
					}
				}
				
			}
			
		});
		
		datainserimento = ft.createText(cdata, "", SWT.DOUBLE_BUFFERED);
		datainserimento.setLayoutData(gdExpH);
		datainserimento.setEditable(false);

		Label l_Luogo = ft.createLabel(cdata, "Luogo");
		l_Luogo.setLayoutData(gdExpH2);
		
		luogo = ft.createText(cdata, "",SWT.DOUBLE_BUFFERED);
		luogo.setLayoutData(gdH2);
		
		Composite cimmobile = ft.createComposite(cdata);
//		cimmobile.setBackground(new Color(null,100,100,100));
		
		cimmobile.setLayout(new GridLayout(2, false));
		cimmobile.setLayoutData(gdExpVH2);
		
		Label l_immobile = ft.createLabel(cimmobile, "Immobile");		
		l_immobile.setLayoutData(gdExpH2);
		
		immobile = ft.createText(cimmobile, "",SWT.DOUBLE_BUFFERED);
		immobile.setLayoutData(gdExpHbottom);
		immobile.setEditable(false);
		
		Button resetimmobile = ft.createButton(cimmobile, "", SWT.FLAT);
//		resetimmobile.setLayoutData(gdExpH);
		resetimmobile.setImage(Activator.getImageDescriptor("icons/edittrash.png").createImage());
		resetimmobile.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				colloquio.setImmobileAbbinato(null);
				setColloquio(colloquio);
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		ft.paintBordersFor(cimmobile);
		
		GridData gdExpVH2Multi = new GridData();
		gdExpVH2Multi.grabExcessHorizontalSpace = true;
		gdExpVH2Multi.grabExcessVerticalSpace = true;
		gdExpVH2Multi.horizontalAlignment = SWT.FILL;
		gdExpVH2Multi.verticalAlignment = SWT.FILL;
		gdExpVH2Multi.horizontalSpan = 2;
		gdExpVH2Multi.minimumHeight = 100;
		
		Label l_descrizione = ft.createLabel(cdata, "Descrizione",SWT.NONE);
		l_descrizione.setLayoutData(gdExpH2);
		
		descrizione = ft.createText(cdata, "",SWT.MULTI|SWT.DOUBLE_BUFFERED);
		descrizione.setLayoutData(gdExpVH2Multi);
		
		GridData gdExpVHMulti = new GridData();
		gdExpVHMulti.grabExcessHorizontalSpace = true;
		gdExpVHMulti.grabExcessVerticalSpace = true;
		gdExpVHMulti.horizontalAlignment = SWT.FILL;
		gdExpVHMulti.verticalAlignment = SWT.FILL;
		gdExpVHMulti.minimumHeight = 100;
		
		Label varieAgenzia = ft.createLabel(cdata, "Varie agenzia");
		Label varieClienti = ft.createLabel(cdata, "Varie clienti");
		
		this.varieAgenzia = ft.createText(cdata, "",SWT.MULTI|SWT.DOUBLE_BUFFERED);
		this.varieAgenzia.setLayoutData(gdExpVHMulti);
		this.varieClienti = ft.createText(cdata, "",SWT.MULTI|SWT.DOUBLE_BUFFERED);
		this.varieClienti.setLayoutData(gdExpVHMulti);;

		anagrafiche();
		agenti();
		criteriRicerca();
		allegati();
		f.reflow(true);
	}
	
	private void anagrafiche(){
		
		sectionAnagrafiche = ft.createSection(f.getBody(), 
											  Section.DESCRIPTION|Section.TITLE_BAR|
				   						   	  Section.TWISTIE);
		sectionAnagrafiche.setExpanded(false);
		sectionAnagrafiche.addExpansionListener(new ExpansionAdapter(){

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
		//gdExpVH.minimumHeight = 150;
		
		sectionAnagrafiche.setLayout(new GridLayout());
		sectionAnagrafiche.setLayoutData(gdExpVH);
		sectionAnagrafiche.setText("Anagrafiche");				

		GridData gdVH = new GridData();
		gdVH.grabExcessHorizontalSpace = true;
		gdVH.grabExcessVerticalSpace = true;
		gdVH.horizontalAlignment = SWT.FILL;
		gdVH.verticalAlignment = SWT.FILL;		
		gdVH.minimumHeight = 150;
		
		
		Composite canagrafiche = ft.createComposite(sectionAnagrafiche,SWT.FLAT);
		canagrafiche.setLayout(new GridLayout());
		ft.paintBordersFor(canagrafiche);
		
		Composite cbuttons = ft.createComposite(canagrafiche,SWT.FLAT);
		cbuttons.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ihNewAnagrafica = ft.createImageHyperlink(cbuttons, SWT.WRAP);		
		ihNewAnagrafica.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNewAnagrafica.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
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
					if (tvAnagrafiche.getTable().getItemCount()>0){
						TableItem ti = tvAnagrafiche.getTable().getItem(tvAnagrafiche.getTable().getItemCount()-1);
						Object[] sel = new Object[1];
						sel[0] = ti.getData();
		
						StructuredSelection ss = new StructuredSelection(sel);
						
						tvAnagrafiche.setSelection(ss, true);
		
						Event ev = new Event();
						ev.item = ti;
						ev.data = ti.getData();
						ev.widget = tvAnagrafiche.getTable();
						tvAnagrafiche.getTable().notifyListeners(SWT.Selection, ev);
					}
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
		
		ihCancellaAnagrafica = ft.createImageHyperlink(cbuttons, SWT.WRAP);		
		ihCancellaAnagrafica.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaAnagrafica.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaAnagrafica.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				StructuredSelection ss = (StructuredSelection)tvAnagrafiche.getSelection();
				if (ss.getFirstElement() != null){
					ColloquiAnagraficheModel_Ang cam = (ColloquiAnagraficheModel_Ang)ss.getFirstElement();
					colloquio.getAnagrafiche().remove(cam);
					tvAnagrafiche.refresh();
				}else{
					MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
												   SWT.ERROR);
					mb.setText("Errore cancellazione");
					mb.setMessage("Selezionare anagrafica da cancellare");			
					mb.open();

				}

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		
		tvAnagrafiche = new TableViewer(canagrafiche, SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvAnagrafiche.getTable().setHeaderVisible(true);
		tvAnagrafiche.getTable().setLinesVisible(true);
		tvAnagrafiche.getTable().setLayoutData(gdVH);
		
		tvAnagrafiche.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				if ((colloquio != null) && (colloquio.getAnagrafiche() != null)){
					return colloquio.getAnagrafiche().toArray();
					//ColloquiAnagraficheModel_Ang
				}else{
					return null;
				}
			}
		});
		
		tvAnagrafiche.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				ColloquiAnagraficheModel_Ang cam_ang = (ColloquiAnagraficheModel_Ang)element;
				switch (columnIndex){
					case 0 : return (cam_ang.getAnagrafica()!= null)
									? cam_ang.getAnagrafica().getNome() + " " + cam_ang.getAnagrafica().getCognome()
									: "";
					case 1 : return cam_ang.getCommento();
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
		
		TableColumn tcAnagrafica = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,0);
		tcAnagrafica.setText("Anagrafica");
		tcAnagrafica.setWidth(200);
		
		TableColumn tcCommento = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,1);
		tcCommento.setText("Commento");
		tcCommento.setWidth(200);
		
		TableViewerColumn tvcCommento = new TableViewerColumn(tvAnagrafiche,tcCommento);
		tvcCommento.setLabelProvider(new CellLabelProvider(){
			@Override
			public void update(ViewerCell cell) {
				ColloquiAnagraficheModel_Ang cModel = ((ColloquiAnagraficheModel_Ang)cell.getElement());
				cell.setText(cModel.getCommento());				
			}
			
		});
		tvcCommento.setEditingSupport(new EditingSupport(tvAnagrafiche){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvAnagrafiche.getTable());
			}

			@Override
			protected Object getValue(Object element) {
				if (element != null){
					ColloquiAnagraficheModel_Ang cam = (ColloquiAnagraficheModel_Ang)element;
					return cam.getCommento();
				}
				return null;
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element != null){
					((ColloquiAnagraficheModel_Ang)element).setCommento(String.valueOf(value));
					tvAnagrafiche.refresh();
				}				
			}
			
		});
		
		sectionAnagrafiche.setClient(canagrafiche);

	}
	
	private void allegati(){
		
		sectionAllegati = ft.createSection(f.getBody(), 
				   						   Section.DESCRIPTION|Section.TITLE_BAR|
				   						   Section.TWISTIE);
		sectionAllegati.setExpanded(false);
		sectionAllegati.addExpansionListener(new ExpansionAdapter(){

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
	//	gdExpVH.minimumHeight = 150;
		
		sectionAllegati.setLayout(new GridLayout());
		sectionAllegati.setLayoutData(gdExpVH);
		sectionAllegati.setText("Allegati");				

		GridData gdVH = new GridData();
		gdVH.grabExcessHorizontalSpace = true;
		gdVH.grabExcessVerticalSpace = true;
		gdVH.horizontalAlignment = SWT.FILL;
		gdVH.verticalAlignment = SWT.FILL;		
		gdVH.minimumHeight = 150;

		Composite callegati = ft.createComposite(sectionAllegati,SWT.FLAT);
		callegati.setLayout(new GridLayout());
		ft.paintBordersFor(callegati);
		
		Composite cbuttons = ft.createComposite(callegati,SWT.FLAT);
		cbuttons.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ihNewAllegati = ft.createImageHyperlink(cbuttons, SWT.WRAP);		
		ihNewAllegati.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNewAllegati.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNewAllegati.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				AllegatiColloquiVO acVO = new AllegatiColloquiVO();
				acVO.setCodColloquio(colloquio.getCodColloquio());				
				colloquio.getAllegati().add(acVO);
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
		
		ihCancellaAllegati = ft.createImageHyperlink(cbuttons, SWT.WRAP);		
		ihCancellaAllegati.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaAllegati.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaAllegati.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {		
				StructuredSelection ss = (StructuredSelection)tvAllegati.getSelection();
				if (ss.getFirstElement() != null){
					AllegatiColloquiVO acVO = (AllegatiColloquiVO)ss.getFirstElement();
					colloquio.getAllegati().remove(acVO);
					tvAllegati.refresh();
				}else{
					MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
												   SWT.ERROR);
					mb.setText("Errore cancellazione");
					mb.setMessage("Selezionare allegato da cancellare");			
					mb.open();

				}				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		
		tvAllegati = new TableViewer(callegati, SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvAllegati.getTable().setHeaderVisible(true);
		tvAllegati.getTable().setLinesVisible(true);
		tvAllegati.getTable().setLayoutData(gdVH);
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
										
				pathRepositoryAllegati += File.separator + "colloqui";
												
				AllegatiColloquiVO acVO = (AllegatiColloquiVO)((StructuredSelection)tvAllegati.getSelection()).getFirstElement();
				
				if ((acVO.getFromPath() == null) ||
					(acVO.getFromPath().equalsIgnoreCase(""))){	
					if ((acVO.getNome() != null) &&
						(!acVO.getNome().equalsIgnoreCase(""))){
						Program.launch(pathRepositoryAllegati +
									   File.separator +
									   acVO.getCodColloquio() +
									   File.separator +
									   acVO.getNome());
					}
				}else if((acVO.getFromPath() != null) &&
						(!acVO.getFromPath().equalsIgnoreCase(""))){
						Program.launch(acVO.getFromPath());					
				}
										
				/*
				Program.launch(pathRepositoryAllegati +
						       File.separator +
						       acVO.getCodColloquio() +
						       File.separator +
						       acVO.getNome());
			    */
			}
		});
		
		tvAllegati.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				if ((colloquio != null) && (colloquio.getAllegati() != null)){
					return colloquio.getAllegati().toArray();
				}else{
					return null;
				}
			}
		});
		
		tvAllegati.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				AllegatiColloquiVO acVO = (AllegatiColloquiVO)element;
				switch (columnIndex){
					case 0 : return acVO.getNome();
					case 1 : return acVO.getDescrizione();
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
		
		TableColumn tcNome = new TableColumn(tvAllegati.getTable(),SWT.CENTER,0);
		tcNome.setText("Nome");
		tcNome.setWidth(200);
		
		TableColumn tcDescrizione = new TableColumn(tvAllegati.getTable(),SWT.CENTER,1);
		tcDescrizione.setText("Descrizione");
		tcDescrizione.setWidth(200);		
		TableViewerColumn tvcDescrizione = new TableViewerColumn(tvAllegati,tcDescrizione);
		tvcDescrizione.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() != null){
					AllegatiColloquiVO acVO = (AllegatiColloquiVO)cell.getElement();
					cell.setText(acVO.getDescrizione());
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
				return (element!= null)
					   ?((AllegatiColloquiVO)element).getDescrizione()
					   :"";
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element != null){
					if (value != null){
						((AllegatiColloquiVO)element).setDescrizione(String.valueOf(value));
					}else{
						((AllegatiColloquiVO)element).setDescrizione("");
					}
					tvAllegati.refresh();
				}
			}
			
		});
		
		TableColumn tcPathFrom = new TableColumn(tvAllegati.getTable(),SWT.CENTER,2);
		tcPathFrom.setWidth(200);
		tcPathFrom.setText("Documento");
		
		TableViewerColumn tvcPathFrom = new TableViewerColumn(tvAllegati,tcPathFrom);
		tvcPathFrom.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				if (((AllegatiColloquiVO)cell.getElement()).getFromPath() != null){
					cell.setText(((AllegatiColloquiVO)cell.getElement()).getFromPath());
				}else{
					cell.setText("");
				}
			}
			
		});
		tvcPathFrom.setEditingSupport(new EditingSupport(tvAllegati){

			@Override
			protected boolean canEdit(Object element) {
				return true;
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
				if (((AllegatiColloquiVO)element).getFromPath() != null){
					return ((AllegatiColloquiVO)element).getFromPath();
				}else{
					return "";
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				((AllegatiColloquiVO)element).setFromPath(String.valueOf(value));
				((AllegatiColloquiVO)element).setNome(((AllegatiColloquiVO)element).getFromPath()
																				   .substring(((AllegatiColloquiVO)element).getFromPath()
																						   								   .lastIndexOf(File.separator)+1)
												      );
				tvAllegati.refresh();
			}
			
		});
		
		sectionAllegati.setClient(callegati);

	}
	
	private Comparator<WinkhouseUtils.ObjectSearchGetters> comparatorImmobileSearchGetters = new Comparator<WinkhouseUtils.ObjectSearchGetters>(){

		@Override
		public int compare(ObjectSearchGetters arg0,
						   ObjectSearchGetters arg1) {
			return arg0.getMethodName().compareToIgnoreCase(arg1.getMethodName());			
		}
		
	};
		
	public void fillDescrizioniAgenti(){						
		desAgenti = new String[MobiliaDatiBaseCache.getInstance().getAgenti().size()];
		codAgenti = new Integer[MobiliaDatiBaseCache.getInstance().getAgenti().size()];
		Iterator<AgentiVO> it = MobiliaDatiBaseCache.getInstance().getAgenti().iterator();
		int count = 0;
		while(it.hasNext()){
			AgentiVO aVO = it.next();
			desAgenti[count] = aVO.getCognome() + " " + aVO.getNome();
			codAgenti[count] = aVO.getCodAgente();
			count++;
		}		
	}	

	private ColloquiCriteriRicercaVO getPrevCriterioByLineNumber(Integer lineNumber){
		ColloquiCriteriRicercaVO ccrVO = new ColloquiCriteriRicercaVO();
		ccrVO.setLineNumber(lineNumber);
		Collections.sort(colloquio.getCriteriRicerca(), comparatorLineNumber);
		int index = Collections.binarySearch(colloquio.getCriteriRicerca(), ccrVO, comparatorLineNumber);
		if (index > 0){
			return (ColloquiCriteriRicercaVO)colloquio.getCriteriRicerca().get(index-1);
		}else{
			return (ColloquiCriteriRicercaVO)colloquio.getCriteriRicerca().get(index);
		}
	}
	
	private Comparator<ColloquiCriteriRicercaVO> comparatorLineNumber = new Comparator<ColloquiCriteriRicercaVO>(){

		@Override
		public int compare(ColloquiCriteriRicercaVO o1, ColloquiCriteriRicercaVO o2) {
			int returnValue=0;
			if (o1.getLineNumber().intValue() > o2.getLineNumber().intValue()){
				return 1;
			}else if (o1.getLineNumber().intValue() < o2.getLineNumber().intValue()){
				return -1;
			}
			return 0;
			
		}
		
	};

	
	private Comparator c = new Comparator<String>(){

		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
		
	};

	private void criteriRicerca(){
		
		sectionCriteri = ft.createSection(f.getBody(), 
				   						  Section.DESCRIPTION|Section.TITLE_BAR|
				   						  Section.TWISTIE);
		sectionCriteri.setExpanded(false);
		sectionCriteri.addExpansionListener(new ExpansionAdapter(){

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
	//	gdExpVH.minimumHeight = 150;
		
		sectionCriteri.setLayout(new GridLayout());
		sectionCriteri.setLayoutData(gdExpVH);
		sectionCriteri.setText("Criteri ricerca");						
		
		GridData gdVH = new GridData();
		gdVH.grabExcessHorizontalSpace = true;
		gdVH.grabExcessVerticalSpace = true;
		gdVH.horizontalAlignment = SWT.FILL;
		gdVH.verticalAlignment = SWT.FILL;		
		gdVH.minimumHeight = 150;

		Composite ccriteriRicerca = ft.createComposite(sectionCriteri,SWT.FLAT);
		ccriteriRicerca.setLayout(new GridLayout());
		ft.paintBordersFor(ccriteriRicerca);
		
		Composite cInterrogazioneCriteri = ft.createComposite(ccriteriRicerca, SWT.FLAT);
		cInterrogazioneCriteri.setLayout(new GridLayout(2, false));
		cInterrogazioneCriteri.setLayoutData(gdExpVH);
		//cInterrogazioneCriteri.setBackground(new Color(null, new RGB(100, 100, 0)));
		Label lrisultatoCriteriMsg = ft.createLabel(cInterrogazioneCriteri, "Risultato criteri ricerca : ");
		lrisultatoCriteri = ft.createLabel(cInterrogazioneCriteri, "            ");
		lrisultatoCriteri.setForeground(new Color(null,new RGB(0, 0, 0)));		
		
		Composite cbuttons = ft.createComposite(ccriteriRicerca,SWT.FLAT);
		cbuttons.setLayout(new GridLayout(9,false));
		GridData gdtoolbar = new GridData();
		gdtoolbar.grabExcessHorizontalSpace = true;
		gdtoolbar.horizontalAlignment = SWT.FILL;
		cbuttons.setLayoutData(gdtoolbar);
		
//		cbuttons.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ihNewCriterio = ft.createImageHyperlink(cbuttons, SWT.WRAP);		
		ihNewCriterio.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNewCriterio.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNewCriterio.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if ((colloquio.getCriteriRicerca().size() == 0) ||
					(((ColloquiCriteriRicercaModel)colloquio.getCriteriRicerca().get(colloquio.getCriteriRicerca().size() - 1)).getGetterMethodName() != null)){
					
						ColloquiCriteriRicercaVO crVO = new ColloquiCriteriRicercaVO();
						ColloquiCriteriRicercaModel crModel = new ColloquiCriteriRicercaModel(crVO); 
						colloquio.getCriteriRicerca().add(crModel);
						reloadLineNumber();
						Collections.sort(colloquio.getCriteriRicerca(), comparatorLineNumber);
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
						
						checkCriteri(colloquio);

				}
				
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

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		ihCancellaCriterio = ft.createImageHyperlink(cbuttons, SWT.WRAP);		
		ihCancellaCriterio.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaCriterio.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaCriterio.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				StructuredSelection ss = (StructuredSelection)tvCriteri.getSelection();
				if ((ss != null) && (ss.getFirstElement() != null)){
					colloquio.getCriteriRicerca().remove((ColloquiCriteriRicercaVO)ss.getFirstElement());
					reloadLineNumber();
					Collections.sort(colloquio.getCriteriRicerca(), comparatorLineNumber);					
					tvCriteri.refresh();
					checkCriteri(colloquio);
				}else{
					MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							   					   SWT.ERROR);
					mb.setText("Errore cancellazione");
					mb.setMessage("Selezionare criterio da cancellare");			
					mb.open();

				}
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		
		
		ImageHyperlink ihCheck = new ImageHyperlink(cbuttons, SWT.WRAP);		
		ihCheck.setImage(Activator.getImageDescriptor("/icons/spellcheck.png").createImage());
		ihCheck.setToolTipText("controllo sintassi");
		ihCheck.setHoverImage(Activator.getImageDescriptor("/icons/spellcheck_over.png").createImage());
		ihCheck.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				SearchEngineImmobili sei = new SearchEngineImmobili(colloquio.getCriteriRicerca());
				if (sei.verifyQuery()){
					MessageBox mb = new MessageBox(getViewSite().getShell(),SWT.ICON_INFORMATION);
					mb.setText("Interrogazione dati");
					mb.setMessage("Interrogazione generata corretta");			
					mb.open();
					checkCriteri(colloquio);

				}else{
					MessageBox mb = new MessageBox(getViewSite().getShell(),SWT.ICON_ERROR);
					mb.setText("Interrogazione dati");
					mb.setMessage("Interrogazione generata non corretta");			
					mb.open();				
					checkCriteri(colloquio);
				}
					
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		Label lseparator = new Label(cbuttons,SWT.FLAT);
		lseparator.setImage(Activator.getImageDescriptor("icons/separator.png").createImage());
		
		ImageHyperlink ihOpen = new ImageHyperlink(cbuttons, SWT.WRAP);		
		ihOpen.setImage(Activator.getImageDescriptor("/icons/fileopen.png").createImage());
		ihOpen.setToolTipText("apri ricerca salvata");
		ihOpen.setHoverImage(Activator.getImageDescriptor("/icons/fileopen_over.png").createImage());
		ihOpen.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				PopUpRicercaRicerche puRR = new PopUpRicercaRicerche(RicercheVO.RICERCHE_IMMOBILI,
													 				 (DettaglioColloquioView)getViewSite().getPart(),
													 				 "setCriteriColloquio");
								
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		

		ImageHyperlink ihSave = new ImageHyperlink(cbuttons, SWT.WRAP);		
		ihSave.setImage(Activator.getImageDescriptor("/icons/document-save.png").createImage());
		ihSave.setToolTipText("salva ricerca");
		ihSave.setHoverImage(Activator.getImageDescriptor("/icons/document-save_over.png").createImage());
		ihSave.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if ((colloquio.getCriteriRicerca() != null) &&
					(colloquio.getCriteriRicerca().size() != 0)){
					
					PopUpEditRicerca puER = new PopUpEditRicerca();
					getRicerca().setTipo(RicercheVO.RICERCHE_IMMOBILI);
					puER.setRicerca(getRicerca());										
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

		ImageHyperlink ihCancellaRicerca = new ImageHyperlink(cbuttons, SWT.WRAP);		
		ihCancellaRicerca.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaRicerca.setToolTipText("cancella ricerca");
		ihCancellaRicerca.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaRicerca.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (ricerca != null){
					RicercheHelper rh = new RicercheHelper();
					if (!rh.deleteRicerca(ricerca,0)){
						MessageDialog.openError(PlatformUI.getWorkbench()
														  .getActiveWorkbenchWindow()
														  .getShell(), 
												"Errore cancellazione ricerca", 
												"Si è verificato un errore nella cancellazione della ricerca");		
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
			
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		
		
		Label lRicercaSelected = new Label(cbuttons, SWT.FLAT);
		lRicercaSelected.setText("Ricerca selezionata : ");
		
		lRicercaSelectedName = new Label(cbuttons, SWT.FLAT);
		GridData gdRicercaSelected = new GridData();
		gdRicercaSelected.grabExcessHorizontalSpace = true;
		gdRicercaSelected.horizontalAlignment = SWT.FILL;
		lRicercaSelectedName.setLayoutData(gdRicercaSelected);		
	
		CriteriaTableUtilsFactory ctuf = new CriteriaTableUtilsFactory();
		
		tvCriteri = ctuf.getSearchImmobiliCriteriaTable(ccriteriRicerca,
														null);
		
		tvCriteri.getTable().setLayoutData(gdExpVH);
		tvCriteri.getTable().setHeaderVisible(true);
		tvCriteri.getTable().setLinesVisible(true);
		tvCriteri.getTable().setLayoutData(gdVH);

		sectionCriteri.setClient(ccriteriRicerca);
		
	};
	
	private void agenti(){
		
		sectionAgenti = ft.createSection(f.getBody(), 
		   						   		 Section.DESCRIPTION|Section.TITLE_BAR|
		   						   		 Section.TWISTIE);
		sectionAgenti.setExpanded(false);
		sectionAgenti.addExpansionListener(new ExpansionAdapter(){

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
	//	gdExpVH.minimumHeight = 150;
		
		sectionAgenti.setLayout(new GridLayout());
		sectionAgenti.setLayoutData(gdExpVH);
		sectionAgenti.setText("Agenti");	
		
		GridData gdVH = new GridData();
		gdVH.grabExcessHorizontalSpace = true;
		gdVH.grabExcessVerticalSpace = true;
		gdVH.horizontalAlignment = SWT.FILL;
		gdVH.verticalAlignment = SWT.FILL;		
		gdVH.minimumHeight = 150;

		Composite cagenti = ft.createComposite(sectionAgenti,SWT.FLAT);
		cagenti.setLayout(new GridLayout());
		ft.paintBordersFor(cagenti);
		
		Label l_agenda = ft.createLabel(cagenti, "in agenda");
//		l_agenda.setLayoutData(gdExpH2);
		
		inagenda = ft.createButton(cagenti,"",SWT.CHECK);
		inagenda.setToolTipText("selezionandolo, il colloquio appare anche nella prospettiva agenda tra gli appuntamenti inseriti");
//		inagenda.setLayoutData(gdExpH2);

		
		Composite cbuttons = ft.createComposite(cagenti,SWT.FLAT);
		cbuttons.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ihNewAgenti = ft.createImageHyperlink(cbuttons, SWT.WRAP);		
		ihNewAgenti.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNewAgenti.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNewAgenti.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (ProfilerHelper.getInstance().getPermessoUI(PopUpRicercaAgenti.ID)){
					PopUpRicercaAgenti pra = new PopUpRicercaAgenti();
					pra.setCallerObj(PlatformUI.getWorkbench()
											   .getActiveWorkbenchWindow()
											   .getActivePage()
											   .getActivePart());
					pra.setSetterMethodName("addAgente");
					if (tvAgenti.getTable().getItemCount() > 0){
						TableItem ti = tvAgenti.getTable().getItem(tvAgenti.getTable().getItemCount()-1);
						Object[] sel = new Object[1];
						sel[0] = ti.getData();
		
						StructuredSelection ss = new StructuredSelection(sel);
						
						tvAgenti.setSelection(ss, true);
		
						Event ev = new Event();
						ev.item = ti;
						ev.data = ti.getData();
						ev.widget = tvAgenti.getTable();
						tvAgenti.getTable().notifyListeners(SWT.Selection, ev);
					}
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		ihCancellaAgenti = ft.createImageHyperlink(cbuttons, SWT.WRAP);		
		ihCancellaAgenti.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaAgenti.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaAgenti.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				StructuredSelection ss = (StructuredSelection)tvAgenti.getSelection();
				if (ss.getFirstElement() != null){
					ColloquiAgentiModel_Age cam = (ColloquiAgentiModel_Age)ss.getFirstElement();
					colloquio.getAgenti().remove(cam);
					tvAgenti.refresh();
				}else{
					MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
												   SWT.ERROR);
					mb.setText("Errore cancellazione");
					mb.setMessage("Selezionare agente da cancellare");			
					mb.open();

				}
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		
		tvAgenti = new TableViewer(cagenti, SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvAgenti.getTable().setHeaderVisible(true);
		tvAgenti.getTable().setLinesVisible(true);
		tvAgenti.getTable().setLayoutData(gdVH);

		tvAgenti.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				if ((colloquio != null) && (colloquio.getAgenti() != null)){
					return colloquio.getAgenti().toArray();
					//ColloquiAnagraficheModel_Ang
				}else{
					return null;
				}
			}
		});
		
		tvAgenti.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				ColloquiAgentiModel_Age cam_age = (ColloquiAgentiModel_Age)element;
				switch (columnIndex){
					case 0 : return (cam_age.getAgente() != null)
									? cam_age.getAgente().getNome() + " " + cam_age.getAgente().getCognome()
									: "";
					case 1 : return cam_age.getCommento();
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
		
		TableColumn tcAnagrafica = new TableColumn(tvAgenti.getTable(),SWT.CENTER,0);
		tcAnagrafica.setText("Agente");
		tcAnagrafica.setWidth(200);
		
		TableColumn tcCommento = new TableColumn(tvAgenti.getTable(),SWT.CENTER,1);
		tcCommento.setText("Commento");
		tcCommento.setWidth(200);
		
		TableViewerColumn tvcCommento = new TableViewerColumn(tvAgenti,tcCommento);
		tvcCommento.setLabelProvider(new CellLabelProvider(){
			@Override
			public void update(ViewerCell cell) {
				ColloquiAgentiModel_Age cModel = ((ColloquiAgentiModel_Age)cell.getElement());
				cell.setText(cModel.getCommento());				
			}
			
		});
		tvcCommento.setEditingSupport(new EditingSupport(tvAgenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvAgenti.getTable());
			}

			@Override
			protected Object getValue(Object element) {
				if (element != null){
					ColloquiAgentiModel_Age cam = (ColloquiAgentiModel_Age)element;
					return cam.getCommento();
				}
				return null;
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element != null){
					((ColloquiAgentiModel_Age)element).setCommento(String.valueOf(value));
					tvAgenti.refresh();
				}				
			}
			
		});

		
		sectionAgenti.setClient(cagenti);
		
	};
	
	private void bindData(DataBindingContext bindingContext){

//		if ((colloquio.getCodImmobileAbbinato() != null) && (colloquio.getCodImmobileAbbinato() != 0)){
//			opi.setEnabled(true);
//		}else{
//			opi.setEnabled(false);
//		}
				
		c_tipologiacolloquio.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				
				if ((colloquio.getCodImmobileAbbinato() == null) || (colloquio.getCodImmobileAbbinato() == 0)){
					return MobiliaDatiBaseCache.getInstance().getTipologieColloqui().toArray();
				}else{
					return MobiliaDatiBaseCache.getInstance().getTipologieColloquiWithoutRicerca().toArray();	
				}
				
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,Object newInput) {
			}
			
		});
		
		c_tipologiacolloquio.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				return ((TipologieColloquiVO)element).getDescrizione();
			}
			
		});
		
		c_tipologiacolloquio.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				colloquio.setTipologia((TipologieColloquiVO)((StructuredSelection)event.getSelection()).getFirstElement());
				enableLists();
			}
			
		});
						
		c_tipologiacolloquio.setInput(new Object());
		if (colloquio.getTipologia() != null){
			int index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getTipologieColloqui(), 
												 colloquio.getTipologia(), 
												 comparatorTipologia);
			Object[] sel = new Object[1];
			sel[0] = MobiliaDatiBaseCache.getInstance().getTipologieColloqui().get(index);
			StructuredSelection ss = new StructuredSelection(sel);
			c_tipologiacolloquio.setSelection(ss);
		}else{
			Object[] sel = new Object[1];
			if ((colloquio.getCodImmobileAbbinato() == null) || (colloquio.getCodImmobileAbbinato() == 0)){
				sel[0] = MobiliaDatiBaseCache.getInstance().getTipologieColloqui().get(0);
			}else{
				sel[0] = MobiliaDatiBaseCache.getInstance().getTipologieColloquiWithoutRicerca().get(0);
			}
			StructuredSelection ss = new StructuredSelection(sel);
			c_tipologiacolloquio.setSelection(ss);
		}

		c_agenteinseritore.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {				
				return MobiliaDatiBaseCache.getInstance().getAgenti().toArray();
			}

			@Override
			public void dispose() {

				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {

				
			}
			
		});
		
		c_agenteinseritore.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				return ((AgentiVO)element).getCognome() + " " + ((AgentiVO)element).getNome();
			}
			
		});
		
		c_agenteinseritore.setInput(new Object());
		
		if ((colloquio.getAgenteInseritore() != null) && 
			(colloquio.getAgenteInseritore() != null)){
			
			int index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getAgenti(), 
												 colloquio.getAgenteInseritore(), 
												 comparatorAgenti);
			Object[] sel = new Object[1];
			sel[0] = MobiliaDatiBaseCache.getInstance().getAgenti().get(index);
			StructuredSelection ss = new StructuredSelection(sel);
			c_agenteinseritore.setSelection(ss);
		}
		
		c_agenteinseritore.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				colloquio.setAgenteInseritore((AgentiVO)((StructuredSelection)event.getSelection()).getFirstElement());				
			}
			
		});
		
		
		
		bindingContext.bindValue(SWTObservables.observeText(luogo,SWT.Modify), 
				 				 PojoObservables.observeValue(colloquio, "luogoIncontro"),
				 				 null, 
				 				 null);

		bindingContext.bindValue(SWTObservables.observeText(descrizione,SWT.Modify), 
				 				 PojoObservables.observeValue(colloquio, "descrizione"),
				 				 null, 
				 				 null);
		
		bindingContext.bindValue(SWTObservables.observeText(descrizione,SWT.Modify), 
				 PojoObservables.observeValue(colloquio, "descrizione"),
				 null, 
				 null);
		
		bindingContext.bindValue(SWTObservables.observeText(varieAgenzia,SWT.Modify), 
				 PojoObservables.observeValue(colloquio, "commentoAgenzia"),
				 null, 
				 null);

		bindingContext.bindValue(SWTObservables.observeText(varieClienti,SWT.Modify), 
				 PojoObservables.observeValue(colloquio, "commentoCliente"),
				 null, 
				 null);		
		
		dcdataincontro.setText(formatter.format(colloquio.getDataColloquio().getTime()));
		
		oraincontro.setText(formatterTime.format(colloquio.getDataColloquio().getTime()));
		
		datainserimento.setText(formatter.format(colloquio.getDataInserimento().getTime()) + " " +
								formatterTime.format(colloquio.getDataInserimento().getTime()));
		
		bindingContext.bindValue(SWTObservables.observeSelection(inagenda), 
				 				 PojoObservables.observeValue(colloquio, "scadenziere"),
				 				 null, 
				 				 null);
		
		immobile.setText(((colloquio.getImmobileAbbinato() != null)
						  ?colloquio.getImmobileAbbinato().toString()
						  :"")
						 );		
		
		tvAnagrafiche.setInput(new Object());
		tvAgenti.setInput(new Object());
		tvCriteri.setInput(colloquio.getCriteriRicerca());		
		checkCriteri(colloquio);
		
		tvAllegati.setInput(new Object());
		
		if (colloquio.getWinkGCalendarModels() != null && colloquio.getWinkGCalendarModels().size() > 0){
			f.setImage(colloquioGCalImg);
			this.setTitleImage(colloquioGCalImg);
		}else{
			f.setImage(colloquioImg);
			this.setTitleImage(colloquioImg);			
		}

		
	}
	
	private void checkCriteri(ColloquiModel colloquio){
		
		if (colloquio.getCodTipologiaColloquio() == 1){
			
			SearchEngineImmobili sei = new SearchEngineImmobili(colloquio.getCriteriRicerca());
			ArrayList result = sei.find();
			if (result == null){
				lrisultatoCriteri.setBackground(new Color(null, new RGB(255, 0, 0)));
				lrisultatoCriteri.setText("Interrogazione non corretta rivedere criteri");
			}else{
				lrisultatoCriteri.setBackground(new Color(null, new RGB(0, 255, 0)));
				lrisultatoCriteri.setText("Interrogazione corretta");
			}			
		}

	}
	
	@Override
	public void setFocus() {
		updateViews();
		if (c_agenteinseritore != null && c_agenteinseritore.getContentProvider() != null){			
			c_agenteinseritore.setInput(new Object());
			if (colloquio != null && colloquio.getAgenteInseritore() != null){
					
					int index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getAgenti(), 
														 colloquio.getAgenteInseritore(), 
														 comparatorAgenti);
					if (index > -1){
						Object[] sel = new Object[1];
						sel[0] = MobiliaDatiBaseCache.getInstance().getAgenti().get(index);
						StructuredSelection ss = new StructuredSelection(sel);
						c_agenteinseritore.setSelection(ss);
					}
			}			
		}
		
	}
	
	private void updateViews(){
		
		if (colloquio != null){
			
			IViewPart ivp = Activator.getDefault()
					 				 .getWorkbench()
					 				 .getActiveWorkbenchWindow()
					 				 .getActivePage()
					 				 .findView(ColloquiView.ID);
			
			if (ivp != null){
				((ColloquiView)ivp).setImmobile(null);
			}

			ivp = Activator.getDefault()
			   			   .getWorkbench()
			   			   .getActiveWorkbenchWindow()
			   			   .getActivePage()
			   			   .findView(RecapitiView.ID);
			
			if (ivp != null){
				((RecapitiView)ivp).setAnagrafiche(null);
			}

			ivp = Activator.getDefault()
			   			   .getWorkbench()
			   			   .getActiveWorkbenchWindow()
			   			   .getActivePage()
			   			   .findView(ImmaginiImmobiliView.ID);

			if (ivp != null){
				((ImmaginiImmobiliView)ivp).setImmobile(null);
			}
			
			ivp = Activator.getDefault()
			   			   .getWorkbench()
			   			   .getActiveWorkbenchWindow()
			   			   .getActivePage()
			   			   .findView(AbbinamentiView.ID);
			
			if (ivp != null){
				((AbbinamentiView)ivp).setColloquio(colloquio);;
				
			}
			
			ivp = Activator.getDefault()
			   			   .getWorkbench()
			   			   .getActiveWorkbenchWindow()
			   			   .getActivePage()
			   			   .findView(ListaAffittiView.ID);

			if (ivp != null){
				((ListaAffittiView)ivp).setImmobile(null);
			}

			ivp = Activator.getDefault()
						   .getWorkbench()
						   .getActiveWorkbenchWindow()
						   .getActivePage()
						   .findView(ImmobiliPropietaView.ID);

			if (ivp != null){
				((ImmobiliPropietaView)ivp).setAnagrafica(null);
			}
			
			IViewPart eavv = Activator.getDefault()
	   				  .getWorkbench()
	   				  .getActiveWorkbenchWindow()
	   				  .getActivePage()
	   				  .findView(EAVView.ID);

			if (eavv != null){
				if ((colloquio.getEntity() != null) && (colloquio.getEntity().getAttributes()!= null)){
					((EAVView)eavv).setAttributes(colloquio.getEntity().getAttributes(), colloquio.getCodColloquio());
					((EAVView)eavv).setCompareView(!isInCompareMode);
				}
			}

			
			
/*
			for (int i=0; i < vr.length; i++){
				IViewPart ivp = vr[i].getView(false);
				if (ivp instanceof RecapitiView){
					((RecapitiView)ivp).setAnagrafica(null);					
				}
				if (ivp instanceof ImmaginiImmobiliView){
					((ImmaginiImmobiliView)ivp).setImmobile(null);
				} 
				if (ivp instanceof ColloquiView){
					((ColloquiView)ivp).setImmobile(null);
				}
				if (ivp instanceof AbbinamentiView){
					((AbbinamentiView)ivp).setImmobile(null);
					((AbbinamentiView)ivp).setAnagrafica(null);
				} 
				if (ivp instanceof ImmobiliPropietaView){
					((ImmobiliPropietaView)ivp).setAnagrafica(null);
				} 

			}
	*/				
		}
		
	}

	public ColloquiModel getColloquio() {
		return colloquio;
	}

	public void setColloquio(ColloquiModel colloquio) {
		isInCompareMode = false;
		DataBindingContext bindingContext = new DataBindingContext();
		this.setPartName(colloquio.toString());
		this.colloquio = colloquio;
		bindData(bindingContext);
		enableLists();
		if ((this.colloquio.getWinkGCalendarModels() != null) && (this.colloquio.getWinkGCalendarModels().size() > 0)){
			removeICALUIDAction.setChecked(true);
			removeICALUIDAction.setImageDescriptor(noGCalImg);
			removeICALUIDAction.setToolTipText("Dissocia da Google Calendar");
//			removeICALUIDAction.setDescription("Rimuovi da Google Calendar");
		}else{
			removeICALUIDAction.setChecked(false);
			removeICALUIDAction.setImageDescriptor(gCalImg);
			removeICALUIDAction.setToolTipText("Invia al Google Calendar");
		}
		
	}
	
	public void setImmobile(ImmobiliModel immobile){
		if (colloquio != null){
			colloquio.setImmobileAbbinato(immobile);
			this.immobile.setText(immobile.toString());
			DataBindingContext bindingContext = new DataBindingContext();
			bindData(bindingContext);
		}
	}

	public void setCriteriColloquio(RicercheModel ricerca){
		
		ArrayList<ColloquiCriteriRicercaVO> criteri = (ArrayList)ricerca.getCriteri().clone();
		Iterator<ColloquiCriteriRicercaVO> it = criteri.iterator();
		
		while (it.hasNext()){
		
			ColloquiCriteriRicercaVO crm = it.next();
			crm.setCodCriterioRicerca(null);
			crm.setCodColloquio(colloquio.getCodColloquio());
			crm.setCodRicerca(null);
			
		}
		
		getColloquio().setCriteriRicerca(criteri);
		setColloquio(getColloquio());
		this.ricerca = ricerca;
		lRicercaSelectedName.setText(ricerca.getNome());
		lRicercaSelectedName.pack();
		lRicercaSelectedName.redraw();		
		checkCriteri(colloquio);
		
	}
	
	private void enableLists(){
		
		if (colloquio.getCodTipologiaColloquio() != null){
			
			switch (colloquio.getCodTipologiaColloquio().intValue()){
				case 1:	sectionAnagrafiche.setExpanded(false);
						sectionAnagrafiche.setEnabled(true);
						sectionAgenti.setExpanded(false);
						sectionAgenti.setEnabled(false);
						sectionCriteri.setExpanded(true);
						sectionCriteri.setEnabled(true);
						sectionAllegati.setExpanded(false);
						sectionAllegati.setEnabled(true);
						break;
				case 2: sectionAnagrafiche.setExpanded(true);
						sectionAnagrafiche.setEnabled(true);
						sectionAgenti.setExpanded(false);
						sectionAgenti.setEnabled(true);
						sectionCriteri.setExpanded(false);
						sectionCriteri.setEnabled(false);
						sectionAllegati.setExpanded(false);
						sectionAllegati.setEnabled(true);
						break;
				case 3: sectionAnagrafiche.setExpanded(false);
						sectionAnagrafiche.setEnabled(true);
						sectionAgenti.setExpanded(false);
						sectionAgenti.setEnabled(true);
						sectionCriteri.setExpanded(false);
						sectionCriteri.setEnabled(false);
						sectionAllegati.setExpanded(false);
						sectionAllegati.setEnabled(true);
						break;
				default : sectionAnagrafiche.setExpanded(false);
						  sectionAnagrafiche.setEnabled(false);
	 					  sectionAgenti.setExpanded(false);
	 					  sectionAgenti.setEnabled(false);
	 					  sectionCriteri.setExpanded(false);
	 					  sectionCriteri.setEnabled(false);
	 					  sectionAllegati.setExpanded(false);
	 					  sectionAllegati.setEnabled(false);					
			}
			
			f.reflow(true);
			
		}
	}
	
	public void addAgente(AgentiVO agente){
		ColloquiAgentiModel_Age cam = new ColloquiAgentiModel_Age();
		cam.setCodColloquio(colloquio.getCodColloquio());
		cam.setCodAgente(agente.getCodAgente());
		colloquio.getAgenti().add(cam);
		tvAgenti.setInput(new Object());
	}

	public ColloquiAgentiModel_Age findAgente(ColloquiAgentiModel_Age cam_a){
		ColloquiAgentiModel_Age returnValue = null;
		Iterator it = colloquio.getAgenti().iterator();
		while (it.hasNext()) {
			ColloquiAgentiModel_Age object = (ColloquiAgentiModel_Age) it.next();
			if (object.getCodAgente().intValue() == cam_a.getCodAgente().intValue());
			returnValue = object;			
		}
		return returnValue;
	}

	public void addAnagrafica(AnagraficheModel anagrafica){
		ColloquiAnagraficheModel_Ang cam = new ColloquiAnagraficheModel_Ang();
		cam.setCodColloquio(colloquio.getCodColloquio());
		cam.setCodAnagrafica(anagrafica.getCodAnagrafica());
		colloquio.getAnagrafiche().add(cam);
		tvAnagrafiche.setInput(new Object());
	}
	
	public ColloquiAnagraficheModel_Ang findAnagrafica(ColloquiAnagraficheModel_Ang cam_a){
		ColloquiAnagraficheModel_Ang returnValue = null;
		Iterator it = colloquio.getAnagrafiche().iterator();
		while (it.hasNext()) {
			ColloquiAnagraficheModel_Ang object = (ColloquiAnagraficheModel_Ang) it.next();
			if (object.getCodAnagrafica().intValue() == cam_a.getCodAnagrafica().intValue());
			returnValue = object;			
		}
		return returnValue;
	}
	
	private void reloadLineNumber(){
		if (colloquio.getCriteriRicerca() != null){
			int count = 0;
			for (Iterator<ColloquiCriteriRicercaVO> iterator = colloquio.getCriteriRicerca().iterator(); iterator.hasNext();) {
				ColloquiCriteriRicercaVO ccrVO = iterator.next(); 
				ccrVO.setLineNumber(count);
				count++;
			}
		}
	}

	public RicercheModel getRicerca() {
		if (ricerca == null){
			ricerca = new RicercheModel();
			ricerca.setTipo(EnvSettingsFactory.getInstance()
	   				   						  .getTipologieColloqui()
	   				   						  .get(0)
	   				   						  .getCodTipologiaColloquio());
		}
		
		ArrayList al = (ArrayList)colloquio.getCriteriRicerca().clone();

		ArrayList <ColloquiCriteriRicercaModel> alm = new ArrayList<ColloquiCriteriRicercaModel>();
		Iterator it = al.iterator();
		while (it.hasNext()) {
			ColloquiCriteriRicercaModel object = (ColloquiCriteriRicercaModel) it.next();
			object.setCodColloquio(0);
			object.setCodCriterioRicerca(0);
			alm.add(object);
		}
		ricerca.setCriteri(alm);			
		
		return ricerca;
	}

	public void setCompareView(boolean enabled){
		
		ihNewAnagrafica.setEnabled(!enabled);
		ihCancellaAnagrafica.setEnabled(!enabled);		
		ihNewAllegati.setEnabled(!enabled);
		ihCancellaAllegati.setEnabled(!enabled);		
		ihNewCriterio.setEnabled(!enabled);
		ihCancellaCriterio.setEnabled(!enabled);
		ihNewAgenti.setEnabled(!enabled);
		ihCancellaAgenti.setEnabled(!enabled);		

		
		c_agenteinseritore.getCombo().setEnabled(!enabled);
		c_tipologiacolloquio.getCombo().setEnabled(!enabled);
		dcdataincontro.setEnabled(!enabled);
		oraincontro.setEditable(!enabled);
		luogo.setEditable(!enabled);
		immobile.setEditable(!enabled);
		descrizione.setEditable(!enabled);
		varieAgenzia.setEditable(!enabled);
		varieClienti.setEditable(!enabled);
		datainserimento.setEditable(!enabled);
		inagenda.setEnabled(!enabled);
		
		tvAnagrafiche.getTable().setEnabled(!enabled);
		tvCriteri.getTable().setEnabled(!enabled);
		tvAgenti.getTable().setEnabled(!enabled);
		tvAllegati.getTable().setEnabled(!enabled);

		opi.setEnabled(!enabled);
		salvaColloquio.setEnabled(!enabled);
		cancellaColloquio.setEnabled(!enabled);
		removeICALUIDAction.setEnabled(!enabled);

		isInCompareMode = true;
		for (int i = 0; i < f.getToolBarManager().getItems().length; i++) {
			f.getToolBarManager().getItems()[i].setVisible(!enabled);
			f.getToolBarManager().getItems()[i].update();
		} 
		f.getToolBarManager().update(true);
		f.updateToolBar();

	}

}
