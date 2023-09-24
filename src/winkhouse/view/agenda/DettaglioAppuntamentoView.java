package winkhouse.view.agenda;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.PojoProperties;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
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
import winkhouse.action.agenda.CancellaAppuntamentoAction;
import winkhouse.action.agenda.RemoveICALUIDAction;
import winkhouse.action.agenda.SalvaAppuntamentoAction;
import winkhouse.action.stampa.StampaAppuntamentiAction;
import winkhouse.dao.AgentiAppuntamentiDAO;
import winkhouse.helper.ProfilerHelper;
import winkhouse.model.AgentiAppuntamentiModel;
import winkhouse.model.AnagraficheAppuntamentiModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.ContattiModel;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.agenti.PopUpRicercaAgenti;
import winkhouse.view.anagrafica.PopUpRicercaAnagrafica;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.TipiAppuntamentiVO;



public class DettaglioAppuntamentoView extends ViewPart {

	public final static String ID = "winkhouse.dettaglioappuntamentoview";
	private ScrolledForm f = null;	
	private FormToolkit ft = null;
	private Image appuntamentoImg = Activator.getImageDescriptor("icons/korgac.png").createImage();
	private Image appuntamentoGCalImg = Activator.getImageDescriptor("icons/korgac-gcal.png").createImage();
	private ImageDescriptor noGCalImg = Activator.getImageDescriptor("icons/no_google_calendar.png");
	private ImageDescriptor gCalImg = Activator.getImageDescriptor("icons/google_calendar.png");
	
	private CalendarCombo dataAppuntamento = null;
	private Text oraincontro = null;
	private CalendarCombo dataAppuntamentofine = null;
	private Text oraincontrofine = null;	
	private ComboViewer cvtipologia = null;
	private Text luogo = null;
	private Text descrizione = null;
	private GridData gdExpVH = null;
	private GridData gdExpVH2 = null;
	private TableViewer tvAgenti = null;
	private TableViewer tvRecapitiAgenti = null;
	private TableViewer tvAnagrafiche = null;
	private TableViewer tvRecapitiAnagrafiche = null;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
	private SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private GridData tabella = null;
	private AppuntamentiModel appuntamento = null;
	
	private ImageHyperlink ihNewAgenti = null;
	private ImageHyperlink ihCancellaAgenti = null;		
	private ImageHyperlink ihNewAnagrafiche = null;
	private ImageHyperlink ihCancellaAnagrafiche = null;		
	
	private SalvaAppuntamentoAction salvaAppuntamentoAction = null;
	private CancellaAppuntamentoAction cancellaAppuntamentoAction = null;
	private RemoveICALUIDAction removeICALUIDAction = null;
	
	
	private boolean isInCompareMode = false;
	
	public DettaglioAppuntamentoView() {}

	private Comparator<TipiAppuntamentiVO> comparatorTipologia = new Comparator<TipiAppuntamentiVO>(){

		@Override
		public int compare(TipiAppuntamentiVO arg0,TipiAppuntamentiVO arg1) {
			if (arg0.getCodTipoAppuntamento().intValue() == arg1.getCodTipoAppuntamento().intValue()){
				return 0;
			}else if (arg0.getCodTipoAppuntamento().intValue() > arg1.getCodTipoAppuntamento().intValue()){
				return 1;
			}else{
				return -1;
			}				
		}
		
	};

	@Override
	public void createPartControl(Composite parent) {
		
		ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createScrolledForm(parent);		
		f.setExpandVertical(true);
		f.setImage(appuntamentoImg);
		f.setText("Dettaglio appuntamento");
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		
		f.getBody().setLayout(gl);
		
		IToolBarManager mgr = f.getToolBarManager();
		salvaAppuntamentoAction = new SalvaAppuntamentoAction(ListaAppuntamentiView.class.getName());
		mgr.add(salvaAppuntamentoAction);
		cancellaAppuntamentoAction = new CancellaAppuntamentoAction(ListaAppuntamentiView.class.getName());
		mgr.add(cancellaAppuntamentoAction);
		removeICALUIDAction = new RemoveICALUIDAction(null,Action.AS_CHECK_BOX);
	
		mgr.add(removeICALUIDAction);
		mgr.add(new StampaAppuntamentiAction("Report appuntamenti", 
				    						 Action.AS_DROP_DOWN_MENU));		

		f.updateToolBar();

		tabella = new GridData();
		tabella.grabExcessHorizontalSpace = true;
		tabella.horizontalAlignment = SWT.FILL;
		tabella.heightHint = 200;

		gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
//		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;		
		

		gdExpVH2 = new GridData();
		gdExpVH2.grabExcessHorizontalSpace = true;
		gdExpVH2.grabExcessVerticalSpace = true;
		gdExpVH2.horizontalAlignment = SWT.FILL;
		gdExpVH2.verticalAlignment = SWT.FILL;
		gdExpVH2.horizontalSpan = 2;
		
		GridData gdExpVHDesc = new GridData();
		gdExpVHDesc.grabExcessHorizontalSpace = true;
		gdExpVHDesc.grabExcessVerticalSpace = true;
		gdExpVHDesc.horizontalAlignment = SWT.FILL;
		gdExpVHDesc.verticalAlignment = SWT.FILL;
		gdExpVHDesc.minimumHeight = 50;
		gdExpVHDesc.horizontalSpan = 2;
		
		GridData gdluogo = new GridData();
		gdluogo.grabExcessHorizontalSpace = true;		
		gdluogo.horizontalAlignment = SWT.FILL;
		gdluogo.horizontalSpan = 2;

		GridData gdtipo = new GridData();
		gdtipo.grabExcessHorizontalSpace = false;		
		gdtipo.horizontalAlignment = SWT.BEGINNING;
		gdtipo.horizontalSpan = 2;
		gdtipo.heightHint = 20; 

		
		Group gInizio = new Group(f.getBody(), SWT.NONE);
		gInizio.setBackground(new Color(null,new RGB(255,255,255)));
		gInizio.setText("Data inizio");
		
		gInizio.setLayout(new GridLayout());
		gInizio.setLayoutData(gdExpVH);
		
		Label ldataAppuntamento = ft.createLabel(gInizio, "data appuntamento :");		
		dataAppuntamento = new CalendarCombo(gInizio, SWT.READ_ONLY|SWT.DOUBLE_BUFFERED);
		dataAppuntamento.addCalendarListener(new ICalendarListener() {
			
			@Override
			public void popupClosed() {}
			
			@Override
			public void dateRangeChanged(Calendar arg0, Calendar arg1) {}
			
			@Override
			public void dateChanged(Calendar arg0) {
				
				Date tmp = null;
				try {
					tmp = formatterDateTime.parse(formatter.format(arg0.getTime()) + " " + oraincontro.getText());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Date tmpfine = null;
				try {
					tmpfine = formatterDateTime.parse(dataAppuntamentofine.getDateAsString() + " " + oraincontrofine.getText());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (tmpfine != null && tmp != null){ 
					if (tmp.compareTo(tmpfine) <= 0){
						appuntamento.setDataAppuntamento(tmp);
					}else{
						MessageDialog.openWarning(Activator.getDefault()
								   						   .getWorkbench()
								   						   .getActiveWorkbenchWindow()
								   						   .getShell(),
								   				  "Attenzione !", 
												  "La data/ora appuntamento � maggiore della data/ora di fine. \n al salvataggio verr� valorizzata con il valore della data/ora fine");
					}
				}else{
					appuntamento.setDataAppuntamento(tmp);
				}
					
				
			}
		});
		
		Label loraAppuntamento = ft.createLabel(gInizio, "ora appuntamento :");
		oraincontro = ft.createText(gInizio,"",SWT.DOUBLE_BUFFERED);
		oraincontro.setText(formatterTime.format(new Date()));
		oraincontro.setTextLimit(5);
		oraincontro.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
		
				
				try {
					Date tmp = formatterDateTime.parse(dataAppuntamento.getDateAsString() + " " + oraincontro.getText());
					Date tmpfine = formatterDateTime.parse(dataAppuntamentofine.getDateAsString() + " " + oraincontrofine.getText());
					if (tmp.compareTo(tmpfine) <= 0){
						appuntamento.setDataAppuntamento(tmp);
					}else{
						MessageDialog.openWarning(Activator.getDefault()
								   						   .getWorkbench()
								   						   .getActiveWorkbenchWindow()
								   						   .getShell(),
								   				  "Attenzione !", 
												  "La data/ora appuntamento � maggiore della data/ora di fine. \n al salvataggio verr� valorizzata con il valore della data/ora fine");
					}
										
				} catch (ParseException e1) {					
					appuntamento.setDataAppuntamento(new Date());
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
		
				
			}
		});
		
		oraincontro.addVerifyListener(new VerifyListener(){

			@Override
			public void verifyText(VerifyEvent e) {
								
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
		
		oraincontro.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				
				String[] oramin = oraincontro.getText().split(":");
				long orario = 0;
				if (oramin.length == 2){
					orario = (((Integer.valueOf(oramin[0])*60)*60)*1000) + ((Integer.valueOf(oramin[1])*60)*1000);
				}
/*            	getColloquio().setDataColloquio(
            					  new Date(getColloquio().getDataColloquio().getTime() + orario)
            								   );*/
				
			}
			
		});

		Group gFine = new Group(f.getBody(), SWT.NONE);
		gFine.setBackground(new Color(null,new RGB(255,255,255)));
		gFine.setText("Data fine");
		
		gFine.setLayout(new GridLayout());
		gFine.setLayoutData(gdExpVH);
		
		Label ldataAppuntamentofine = ft.createLabel(gFine, "data fine appuntamento :");		
		dataAppuntamentofine = new CalendarCombo(gFine, SWT.READ_ONLY|SWT.DOUBLE_BUFFERED);
		dataAppuntamentofine.addCalendarListener(new ICalendarListener() {
			
			@Override
			public void popupClosed() {}
			
			@Override
			public void dateRangeChanged(Calendar arg0, Calendar arg1) {}
			
			@Override
			public void dateChanged(Calendar arg0) {
				try {
					Date tmpfine = formatterDateTime.parse(formatter.format(arg0.getTime()) + " " + oraincontrofine.getText());
					Date tmpinizio = formatterDateTime.parse(dataAppuntamento.getDateAsString() + " " + oraincontro.getText());
					if (tmpfine.compareTo(tmpinizio) >= 0){
						appuntamento.setDataFineAppuntamento(tmpfine);
					}else{
						MessageDialog.openWarning(Activator.getDefault()
								   						   .getWorkbench()
								   						   .getActiveWorkbenchWindow()
								   						   .getShell(),
								   				  "Attenzione !", 
												  "La data/ora fine appuntamento � minore della data di inizio. \n al salvataggio verr� valorizzata con il valore della data/ora inizio");

					}
				} catch (ParseException e) {
					appuntamento.setDataFineAppuntamento(new Date());
				}
				
			}
		});
		
		Label loraAppuntamentofine = ft.createLabel(gFine, "ora fine appuntamento :");
		oraincontrofine = ft.createText(gFine,"",SWT.DOUBLE_BUFFERED);
		oraincontrofine.setText(formatterTime.format(new Date()));
		oraincontrofine.setTextLimit(5);
		oraincontrofine.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
		
				
				try {
					Date tmpfine = formatterDateTime.parse(dataAppuntamentofine.getDateAsString() + " " + oraincontrofine.getText());
					Date tmpinizio = formatterDateTime.parse(dataAppuntamento.getDateAsString() + " " + oraincontro.getText());
					if (tmpfine.compareTo(tmpinizio) >= 0){
						appuntamento.setDataFineAppuntamento(tmpfine);
					}else{
   						MessageDialog.openWarning(Activator.getDefault()
		   						     					   .getWorkbench()
		   						     					   .getActiveWorkbenchWindow()
		   						     					   .getShell(),
		   						     			  "Attenzione !", 
   												  "La data/ora fine appuntamento � minore della data di inizio. \n al salvataggio verr� valorizzata con il valore della data/ora inizio");
						
					}
				} catch (ParseException e1) {					
					appuntamento.setDataFineAppuntamento(new Date());
				}
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				
			}
		});
		
		oraincontrofine.addVerifyListener(new VerifyListener(){

			@Override
			public void verifyText(VerifyEvent e) {
								
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
							oraincontrofine.setText(oraincontrofine.getText(0, 2) + "0");
							e.doit = false;
						}
					}
					if(e.start == 4){
						try {
							int quattro = Integer.valueOf(String.valueOf(e.character)).intValue();
							e.doit = true;
						} catch (NumberFormatException e1) {
							oraincontrofine.setText(oraincontrofine.getText(0, 3) + "0");
							e.doit = false;
						}
					}
				}
				
			}
			
		});
		
		oraincontrofine.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
			}

			@Override
			public void focusLost(FocusEvent e) {
				
				String[] oramin = oraincontrofine.getText().split(":");
				long orario = 0;
				if (oramin.length == 2){
					orario = (((Integer.valueOf(oramin[0])*60)*60)*1000) + ((Integer.valueOf(oramin[1])*60)*1000);
				}
/*            	getColloquio().setDataColloquio(
            					  new Date(getColloquio().getDataColloquio().getTime() + orario)
            								   );*/
				
			}
			
		});

		Label ltipoappuntamento = ft.createLabel(f.getBody(),"Tipo appuntamento");		
		cvtipologia = new ComboViewer(f.getBody(),SWT.DOUBLE_BUFFERED);
		cvtipologia.getCombo().setLayoutData(gdtipo);
		
		Label lluogo = ft.createLabel(f.getBody(),"Luogo appuntamento");		
		luogo = ft.createText(f.getBody(), "", SWT.DOUBLE_BUFFERED);
		luogo.setLayoutData(gdluogo);
		
		Label ldescrizione = ft.createLabel(f.getBody(),"Descrizione appuntamento");		
		descrizione = ft.createText(f.getBody(), "", SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL|SWT.WRAP);
		descrizione.setLayoutData(gdExpVHDesc);
		
		createAgentiSection();
		createAnagraficheSection();
		ft.paintBordersFor(gInizio);
		ft.paintBordersFor(gFine);
		ft.paintBordersFor(f.getBody());
		f.reflow(true);
	}

	private void createAgentiSection(){

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

		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		
		GridData gdtabella = new GridData();
		gdtabella.grabExcessHorizontalSpace = true;
		gdtabella.grabExcessVerticalSpace = true;
		gdtabella.horizontalAlignment = SWT.FILL;
		gdtabella.verticalAlignment = SWT.FILL;		

		GridData gdSection = new GridData();
		gdSection.grabExcessHorizontalSpace = true;
		gdSection.grabExcessVerticalSpace = true;
		gdSection.horizontalAlignment = SWT.FILL;
		gdSection.verticalAlignment = SWT.FILL;		
		
		section.setLayoutData(gdtabella);
		section.setText("Agenti");
		section.setDescription("Agenti presenti all'appuntamento");
		
		Composite sectionClient = ft.createComposite(section);
		ft.paintBordersFor(sectionClient);
		sectionClient.setLayoutData(gdSection);
		sectionClient.setLayout(new GridLayout());
		Composite toolbar = new Composite(sectionClient,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ImageHyperlink ihNewAgenti = ft.createImageHyperlink(toolbar, SWT.WRAP);		
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
				}else{
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
		  					  "Controllo permessi accesso vista",
		  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
		  					  " non ha il permesso di accedere alla vista " + 
		  					PopUpRicercaAgenti.ID);
}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancellaAgenti = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancellaAgenti.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaAgenti.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaAgenti.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {		
				
				if (tvAgenti.getSelection() != null){
					int count = 0;
					boolean result = true;
					Iterator it = ((StructuredSelection)tvAgenti.getSelection()).iterator();
					while (it.hasNext()) {
						appuntamento.getAgenti().remove((AgentiAppuntamentiModel)it.next());
					}
					tvAgenti.setInput(new Object());
					tvAgenti.refresh();
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
		tvAgenti = new TableViewer(sectionClient,SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
	/*	TableViewerEditor.create(tvAgenti,
								 new ColumnViewerEditorActivationStrategy(tvAgenti),
								 ColumnViewerEditor.TABBING_HORIZONTAL|ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR|ColumnViewerEditor.TABBING_VERTICAL);
		*/
		tvAgenti.getTable().setHeaderVisible(true);
		tvAgenti.getTable().setLinesVisible(true);
		tvAgenti.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				tvRecapitiAgenti.setInput(new Object());
			}
		});
		TableColumn tcNome = new TableColumn(tvAgenti.getTable(),SWT.CENTER,0);
		tcNome.setText("Nome");
		tcNome.setWidth(200);
		
		TableColumn tcCognome = new TableColumn(tvAgenti.getTable(),SWT.CENTER,1);
		tcCognome.setText("Cognome");
		tcCognome.setWidth(200);
				
		TableColumn tcCap = new TableColumn(tvAgenti.getTable(),SWT.CENTER,2);
		tcCap.setText("Cap");
		tcCap.setWidth(50);
		
		TableColumn tcProvincia = new TableColumn(tvAgenti.getTable(),SWT.CENTER,3);
		tcProvincia.setText("Provincia");
		tcProvincia.setWidth(100);
				
		TableColumn tcCitta = new TableColumn(tvAgenti.getTable(),SWT.CENTER,4);
		tcCitta.setText("Citta");
		tcCitta.setWidth(150);
		
		TableColumn tcIndirizzo = new TableColumn(tvAgenti.getTable(),SWT.CENTER,5);
		tcIndirizzo.setText("Indirizzo");
		tcIndirizzo.setWidth(200);
		
		TableColumn tcNote = new TableColumn(tvAgenti.getTable(),SWT.CENTER,6);
		tcNote.setText("Note");
		tcNote.setWidth(200);
		
		TableViewerColumn tvcNote = new TableViewerColumn(tvAgenti, tcNote);
		tvcNote.setEditingSupport(new EditingSupport(tvAgenti) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((AgentiAppuntamentiModel)element).setNote(String.valueOf(value));
				tvAgenti.refresh();
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((AgentiAppuntamentiModel)element).getNote();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvAgenti.getTable());
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		
		tvcNote.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				cell.setText(((AgentiAppuntamentiModel)cell.getElement()).getNote());
			}
		});
		
		tvAgenti.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return (appuntamento != null && appuntamento.getAgenti() != null)
					   ? appuntamento.getAgenti().toArray()
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
		
		tvAgenti.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				switch(columnIndex){
				case 0:return (((AgentiAppuntamentiModel)element).getAgente() != null)
							   ?((AgentiAppuntamentiModel)element).getAgente().getNome()
							   : "";
				case 1:return (((AgentiAppuntamentiModel)element).getAgente() != null)
				 			   ?((AgentiAppuntamentiModel)element).getAgente().getCognome()
				 			   : "";
				case 2:return (((AgentiAppuntamentiModel)element).getAgente() != null)
							   ?((AgentiAppuntamentiModel)element).getAgente().getCap()
							   : "";
				case 3:return (((AgentiAppuntamentiModel)element).getAgente() != null)
							   ?((AgentiAppuntamentiModel)element).getAgente().getProvincia()
							   : "";
				case 4:return (((AgentiAppuntamentiModel)element).getAgente() != null)
							   ?((AgentiAppuntamentiModel)element).getAgente().getCitta()
							   : "";
				case 5:return (((AgentiAppuntamentiModel)element).getAgente() != null)
							   ?((AgentiAppuntamentiModel)element).getAgente().getIndirizzo()
							   : "";
				case 6:return ((AgentiAppuntamentiModel)element).getNote();
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
		tvAgenti.getTable().setLayoutData(tabella);
		tvAgenti.setInput(new Object());
		
		GridData gdTableRecapiti = new GridData();
		gdTableRecapiti.heightHint = 100;

		Composite cRecapiti = ft.createComposite(sectionClient, SWT.NONE);
		ft.paintBordersFor(cRecapiti);
		cRecapiti.setLayout(new GridLayout());
		cRecapiti.setLayoutData(gdTableRecapiti);

		tvRecapitiAgenti = new TableViewer(cRecapiti,SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		tvRecapitiAgenti.getTable().setLayoutData(gdTableRecapiti);
		tvRecapitiAgenti.getTable().setLayoutData(tabella);
		tvRecapitiAgenti.getTable().setHeaderVisible(true);
		tvRecapitiAgenti.getTable().setLinesVisible(true);
		tvRecapitiAgenti.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return (((StructuredSelection)tvAgenti.getSelection()).getFirstElement() == null)
						? new ArrayList().toArray()
						: (((AgentiAppuntamentiModel)((StructuredSelection)tvAgenti.getSelection()).getFirstElement()).getAgente()
																													 .getContatti() == null)
						   ? new ArrayList().toArray()
						   : ((AgentiAppuntamentiModel)((StructuredSelection)tvAgenti.getSelection()).getFirstElement()).getAgente()
							 																							.getContatti().toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			
		});
		tvRecapitiAgenti.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				ContattiModel cModel = ((ContattiModel)element);
				switch (columnIndex){
				case 0: return ((cModel.getTipologia() == null)
							   ? ""
							   : (cModel.getTipologia().getDescrizione() == null)
							     ? ""
							     : cModel.getTipologia().getDescrizione());					        
				
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
		TableColumn tcTipologia = new TableColumn(tvRecapitiAgenti.getTable(),SWT.CENTER,0);
		tcTipologia.setWidth(150);
		tcTipologia.setText("Tipologia recapito");
				
		TableColumn tcContatto = new TableColumn(tvRecapitiAgenti.getTable(),SWT.CENTER,1);
		tcContatto.setWidth(150);
		tcContatto.setText("Recapito");
				
		TableColumn tcDescrizione = new TableColumn(tvRecapitiAgenti.getTable(),SWT.CENTER,2);
		tcDescrizione.setWidth(150);
		tcDescrizione.setText("Descrizione");

		section.setClient(sectionClient);
		
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

		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		
		GridData gdtabella = new GridData();
		gdtabella.grabExcessHorizontalSpace = true;
		gdtabella.grabExcessVerticalSpace = true;
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
		
		ihNewAnagrafiche = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNewAnagrafiche.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNewAnagrafiche.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNewAnagrafiche.addMouseListener(new MouseListener(){

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

		ihCancellaAnagrafiche = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancellaAnagrafiche.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaAnagrafiche.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaAnagrafiche.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				if (tvAnagrafiche.getSelection() != null){
					Iterator it = ((StructuredSelection)tvAnagrafiche.getSelection()).iterator();
					while (it.hasNext()) {
						appuntamento.getAnagrafiche()
									.remove(it.next());						
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
		
		TableColumn tcCognome = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,1);
		tcCognome.setText("Cognome");
		tcCognome.setWidth(200);

		TableColumn tcRagioneSociale = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,2);
		tcRagioneSociale.setText("Ragione sociale");
		tcRagioneSociale.setWidth(200);

		TableColumn tcCap = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,3);
		tcCap.setText("Cap");
		tcCap.setWidth(50);
		
		TableColumn tcProvincia = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,4);
		tcProvincia.setText("Provincia");
		tcProvincia.setWidth(100);
				
		TableColumn tcCitta = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,5);
		tcCitta.setText("Citta");
		tcCitta.setWidth(150);
		
		TableColumn tcIndirizzo = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,6);
		tcIndirizzo.setText("Indirizzo");
		tcIndirizzo.setWidth(200);
		
		TableColumn tcNote = new TableColumn(tvAnagrafiche.getTable(),SWT.CENTER,7);
		tcNote.setText("Note");
		tcNote.setWidth(200);
		
		TableViewerColumn tvcNote = new TableViewerColumn(tvAnagrafiche, tcNote);
		tvcNote.setEditingSupport(new EditingSupport(tvAnagrafiche) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((AnagraficheAppuntamentiModel)element).setNote(String.valueOf(value));
				tvAnagrafiche.refresh();
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((AnagraficheAppuntamentiModel)element).getNote();
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
				cell.setText(((AnagraficheAppuntamentiModel)cell.getElement()).getNote());
			}
		});
		
		tvAnagrafiche.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return (appuntamento != null && appuntamento.getAnagrafiche() != null)
				   		? appuntamento.getAnagrafiche().toArray()
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
				case 0:return (((AnagraficheAppuntamentiModel)element).getAnagrafica() != null)
							   ?((AnagraficheAppuntamentiModel)element).getAnagrafica().getNome()
							   : "";
				case 1:return (((AnagraficheAppuntamentiModel)element).getAnagrafica() != null)
							   ?((AnagraficheAppuntamentiModel)element).getAnagrafica().getCognome()
							   : "";
				case 2:return (((AnagraficheAppuntamentiModel)element).getAnagrafica() != null)
						      ?((AnagraficheAppuntamentiModel)element).getAnagrafica().getRagioneSociale()
						      : "";							   
				case 3:return (((AnagraficheAppuntamentiModel)element).getAnagrafica() != null)
							   ?((AnagraficheAppuntamentiModel)element).getAnagrafica().getCap()
							   : "";
				case 4:return (((AnagraficheAppuntamentiModel)element).getAnagrafica() != null)
							   ?((AnagraficheAppuntamentiModel)element).getAnagrafica().getProvincia()
							   : "";
				case 5:return (((AnagraficheAppuntamentiModel)element).getAnagrafica() != null)
							   ?((AnagraficheAppuntamentiModel)element).getAnagrafica().getCitta()
							   : "";
				case 6:return (((AnagraficheAppuntamentiModel)element).getAnagrafica() != null)
							   ?((AnagraficheAppuntamentiModel)element).getAnagrafica().getIndirizzo()
							   : "";
				case 7:return ((AnagraficheAppuntamentiModel)element).getNote();
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
				: (((AnagraficheAppuntamentiModel)((StructuredSelection)tvAnagrafiche.getSelection()).getFirstElement()).getAnagrafica()
																													    .getContatti() == null)
				   ? new ArrayList().toArray()
				   : ((AnagraficheAppuntamentiModel)((StructuredSelection)tvAnagrafiche.getSelection()).getFirstElement()).getAnagrafica()
					 																									  .getContatti().toArray();
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
				ContattiModel cModel = ((ContattiModel)element);
				switch (columnIndex){
				case 0: return ((cModel.getTipologia() == null)
							   ? ""
							   : (cModel.getTipologia().getDescrizione() == null)
							     ? ""
							     : cModel.getTipologia().getDescrizione());					        
				
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
		setAppuntamento(appuntamento);
	}

	private void bindAppuntamento(DataBindingContext bindingContext){

		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(luogo), 
								 PojoProperties.value("luogo").observe(appuntamento),
								 null, 
								 null);
		
		dataAppuntamento.setText(formatter.format(appuntamento.getDataAppuntamento().getTime()));
		
		oraincontro.setText(formatterTime.format(appuntamento.getDataAppuntamento().getTime()));
		
		if (appuntamento.getDataFineAppuntamento() != null){
			dataAppuntamentofine.setText(formatter.format(appuntamento.getDataFineAppuntamento().getTime()));
			oraincontrofine.setText(formatterTime.format(appuntamento.getDataFineAppuntamento().getTime()));
		}
		
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(descrizione), 
								 PojoProperties.value("descrizione").observe(appuntamento),
								 null, 
								 null);
		
		cvtipologia.setContentProvider(new IStructuredContentProvider(){
			
			@Override
			public Object[] getElements(Object inputElement) {				 
				return MobiliaDatiBaseCache.getInstance().getTipiAppuntamenti().toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,Object newInput) {
			}
			
		});
		
		cvtipologia.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				return ((TipiAppuntamentiVO)element).getDescrizione();
			}
			
		});
		
		cvtipologia.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				appuntamento.setTipoAppuntamento((TipiAppuntamentiVO)((StructuredSelection)event.getSelection()).getFirstElement());				
			}
			
		});
						
		cvtipologia.setInput(new Object());
		if (appuntamento.getTipoAppuntamento() != null){
			int index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getTipiAppuntamenti(), appuntamento.getTipoAppuntamento(), comparatorTipologia);
			if (index > -1){
				Object[] sel = new Object[1];
				sel[0] = MobiliaDatiBaseCache.getInstance().getTipiAppuntamenti().get(index);
				StructuredSelection ss = new StructuredSelection(sel);
				cvtipologia.setSelection(ss);
			}
		}
		
		if (appuntamento.getWinkGCalendarModels() != null && appuntamento.getWinkGCalendarModels().size() > 0){
			f.setImage(appuntamentoGCalImg);
			this.setTitleImage(appuntamentoGCalImg);
		}else{
			f.setImage(appuntamentoImg);
			this.setTitleImage(appuntamentoImg);
			
		}
		
		tvAgenti.setInput(new Object());
		tvRecapitiAgenti.setInput(new Object());
		tvAnagrafiche.setInput(new Object());
		tvRecapitiAnagrafiche.setInput(new Object());
		
		if ((this.appuntamento.getWinkGCalendarModels() != null) && (this.appuntamento.getWinkGCalendarModels().size() > 0)){
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
	
	private boolean checkAgente(Integer codAgente){
		boolean returnValue = true;
		Iterator<AgentiAppuntamentiModel> it = (appuntamento.getAgenti() == null)
											   ? new ArrayList<AgentiAppuntamentiModel>().iterator()
											   : appuntamento.getAgenti().iterator();
		AgentiAppuntamentiDAO aaDAO = new AgentiAppuntamentiDAO();		
		while(it.hasNext()){
			AgentiAppuntamentiModel aapm = it.next();
			if (aapm.getCodAgente() == null && aapm.getCodAgentiAppuntamenti() != null){
				aaDAO.deleteAgentiAppuntamenti(aapm.getCodAgentiAppuntamenti(), null, true);
				returnValue = true;
				break;				
			}
			if (aapm.getCodAgente().intValue() == codAgente.intValue()){
				returnValue = false;
				break;
			}
		}
		return returnValue;
	}
	
	private boolean checkAnagrafica(Integer codAnagrafica){
		boolean returnValue = true;
		Iterator<AnagraficheAppuntamentiModel> it = (appuntamento.getAnagrafiche() == null)
													? new ArrayList<AnagraficheAppuntamentiModel>().iterator()
													: appuntamento.getAnagrafiche().iterator();
		while(it.hasNext()){
			if (it.next().getCodAnagrafica().intValue() == codAnagrafica.intValue()){
				returnValue = false;
				break;
			}
		}
		return returnValue;
	}
	
	public void addAgente(AgentiVO agente){
		if (checkAgente(agente.getCodAgente())){
			AgentiAppuntamentiModel aam = new AgentiAppuntamentiModel();
			aam.setCodAppuntamento(appuntamento.getCodAppuntamento());
			aam.setCodAgente(agente.getCodAgente());
			if (appuntamento.getAgenti() == null){
				ArrayList<AgentiAppuntamentiModel> agenti = new ArrayList<AgentiAppuntamentiModel>();				
				appuntamento.setAgenti(agenti);
			}
			appuntamento.getAgenti().add(aam);			
			tvAgenti.setInput(new Object());			
		}else{
			MessageDialog.openWarning(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
					  				  "Errore selezione agente", 
					  				  "Agente presente in lista");			
		}
	}

	public void addAnagrafica(AnagraficheModel anagrafica){
		if (checkAnagrafica(anagrafica.getCodAnagrafica())){
			AnagraficheAppuntamentiModel aaModel = new AnagraficheAppuntamentiModel();
			aaModel.setCodAnagrafica(anagrafica.getCodAnagrafica());
			aaModel.setCodAppuntamento(appuntamento.getCodAppuntamento());
			if (appuntamento.getAnagrafiche() == null){
				ArrayList<AnagraficheAppuntamentiModel> anagrafiche = new ArrayList<AnagraficheAppuntamentiModel>();				
				appuntamento.setAnagrafiche(anagrafiche);
			}			
			appuntamento.getAnagrafiche().add(aaModel);
			tvAnagrafiche.setInput(new Object());
			tvAnagrafiche.refresh();
		}else{
			MessageDialog.openWarning(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
					  				  "Errore selezione anagrafica", 
					  				  "Anagrafica presente in lista");			
		}
	}

	public AppuntamentiModel getAppuntamento() {
		return appuntamento;
	}

	public void setAppuntamento(AppuntamentiModel appuntamento) {
		if (appuntamento != null){
			isInCompareMode = false;
			DataBindingContext bindingContext = new DataBindingContext();
			this.appuntamento = appuntamento;
			if (appuntamento.getDataAppuntamento() != null){
				this.setPartName(formatter.format(appuntamento.getDataAppuntamento())+ 
								 " " +
								 formatterTime.format(appuntamento.getDataAppuntamento()));
			}
			bindAppuntamento(bindingContext);
		}
	}

	public void setCompareView(boolean enabled){
		
		dataAppuntamento.setEnabled(enabled);
		oraincontro.setEditable(enabled);
		dataAppuntamentofine.getCombo().setEnabled(enabled);
		oraincontrofine.setEditable(enabled);	
		cvtipologia.getCombo().setEnabled(enabled);
		luogo.setEditable(enabled);
		descrizione.setEditable(enabled);
		tvAgenti.getTable().setEnabled(enabled);
		tvRecapitiAgenti.getTable().setEnabled(enabled);
		tvAnagrafiche.getTable().setEnabled(enabled);
		tvRecapitiAnagrafiche.getTable().setEnabled(enabled);
		
		ihNewAgenti.setEnabled(enabled);
		ihCancellaAgenti.setEnabled(enabled);
		ihNewAnagrafiche.setEnabled(enabled);
		ihCancellaAnagrafiche.setEnabled(enabled);		
		
		salvaAppuntamentoAction.setEnabled(enabled);
		cancellaAppuntamentoAction.setEnabled(enabled);
		removeICALUIDAction.setEnabled(enabled);
		
		isInCompareMode = true;

	}

}
