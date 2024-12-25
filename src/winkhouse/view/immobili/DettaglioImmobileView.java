package winkhouse.view.immobili;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.PojoProperties;
//import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
//import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProvider;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
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
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.vafada.swtcalendar.SWTCalendarEvent;
import org.vafada.swtcalendar.SWTCalendarListener;

import winkhouse.Activator;
import winkhouse.action.immobili.CambiaStoricoAction;
import winkhouse.action.immobili.CancellaImmobile;
import winkhouse.action.immobili.OpenPopupComuniAction;
import winkhouse.action.immobili.RefreshDettaglioImmobile;
import winkhouse.action.immobili.SalvaImmobile;
import winkhouse.action.stampa.StampaImmobiliAction;
import winkhouse.dao.EntityDAO;
import winkhouse.dialogs.custom.FileDialogCellEditor;
import winkhouse.dialogs.custom.SWTCalendarDialog;
import winkhouse.model.EntityModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Agenti;
import winkhouse.orm.Allegatiimmobili;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Classicliente;
import winkhouse.orm.Classienergetiche;
import winkhouse.orm.Comuni;
import winkhouse.orm.Daticatastali;
import winkhouse.orm.Immobili;
import winkhouse.orm.Immobilipropietari;
import winkhouse.orm.Riscaldamenti;
import winkhouse.orm.Stanzeimmobili;
import winkhouse.orm.Statoconservativo;
import winkhouse.orm.Tipologiastanze;
import winkhouse.orm.Tipologieimmobili;
import winkhouse.perspective.ImmobiliPerspective;
import winkhouse.util.IPrefersPerspective;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.affitti.ListaAffittiView;
import winkhouse.view.anagrafica.ImmobiliPropietaView;
import winkhouse.view.common.AbbinamentiView;
import winkhouse.view.common.ColloquiView;
import winkhouse.view.common.EAVView;
import winkhouse.view.common.MapView;
import winkhouse.view.common.RecapitiView;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ImmobiliVO;


public class DettaglioImmobileView extends ViewPart
								   implements IPrefersPerspective,IPropertySource{
	
	public final static String ID = "winkhouse.dettaglioimmobileview";

	private FormToolkit ft = null;
	private ScrolledForm f = null;
	
	private Immobili immobile = null;
	
	Label codImmobile = null;
	
	// posizione immobile
	Text triferimento = null;
	Text tcitta = null;
	Text tprovincia = null;
	Text tcap = null;
	Text tzona = null;
	Text tindirizzo = null;
	Text tncivico = null;
	//Anagrafica Immobile
	Text tcognome = null;
	Text tnome = null;
	Text tcittaangrafica = null;
	Text tprovinciaanagrafica = null;
	Text tcapanagrafica = null;
	Text tindirizzoanagrafica = null;
	ComboViewer cvclassiclienti = null;
	ComboViewer cvagenteinseritoreanagrafica = null;
	Text tcommento = null;
	
	// dati immobile
	Text tannoCostruzione = null;
	ControlDecoration decAnnoCostruzione = null;
	ComboViewer cvtipologia = null;
	Text tprezzo = null;
	ControlDecoration decprezzo = null;
	Text tliberoda = null;
	ComboViewer cvstato = null;
	Text tmutuo = null;
	ControlDecoration decMutuo = null;
	Text tmq = null;
	ControlDecoration decmq = null;
	ComboViewer cvriscaldamenti = null;
	Text tspese = null;
	ControlDecoration decSpese = null;
	Text tdescrizione = null;
	Text tdescrizioneMutuo = null;
	
	// stanze immobile
	TableViewer tvStanze = null;
	TableColumn tcTipologiaStaza = null;
	TableColumn tcmq = null;
	String[] desTipologieStanze = null;
	TextCellEditor tceMq = null;
	// dati agenzia
	Text dcdatainserimento = null;
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	ComboViewer cvagenteinseritore = null;
	Button visibile = null;
	Button affitto = null;
	Text varie = null;
	// allegati immobile
	TableViewer tvAllegati = null;
	TableColumn tcDescrizione = null;
	TableColumn tcPathFrom = null;
	
	// dati catastali
	TableViewer tvDatiCatastali = null;
	TableColumn tcfoglio = null;
	TableColumn tcparticella = null;
	TableColumn tcsubalterno = null;
	TableColumn tccategoria = null;
	TableColumn tcrendita = null;
	TableColumn tcredditoDomenicale = null;
	TableColumn tcredditoAgricolo = null;
	TableColumn tcdimensione = null;
	
	ComboViewer cvclasseenergetica = null;
	
	RefreshDettaglioImmobile refreshDettaglioImmobile = null;
	SalvaImmobile salvaImmobile = null;
	CancellaImmobile cancellaImmobile = null;		
//	OpenPopUpAnagrafiche openPopUpAnagrafiche = null;	
	CambiaStoricoAction cambiaStoricoAction = null;
	StampaImmobiliAction stampaImmobiliAction = null;
	Button bOpenDialog = null; 
	Button bOpenDialog2 = null;
	
	ImageHyperlink ihNewStanza = null;		
	ImageHyperlink ihCancellaStanza = null;		
	ImageHyperlink ihNewDatiCatastali = null;		
	ImageHyperlink ihCancellaDatiCatastali = null;		
	ImageHyperlink ihNewAllegato = null;		
	ImageHyperlink ihCancellaAllegato = null;
	ImageHyperlink ihVediCartellaAllegati = null;	
	Image iInfo = Activator.getImageDescriptor("icons/info.png").createImage();
	
	boolean isInCompareMode = false;
	
	private Comparator<Tipologiastanze> comparatorTipologieStanze = new Comparator<Tipologiastanze>(){
		@Override
		public int compare(Tipologiastanze arg0, Tipologiastanze arg1) {
			return Integer.valueOf(arg0.getCodTipologiaStanza()).compareTo(Integer.valueOf(arg1.getCodTipologiaStanza()));
		}		
	};	
	
	private Comparator<Tipologieimmobili> comparatorTipologia = new Comparator<Tipologieimmobili>(){

		@Override
		public int compare(Tipologieimmobili arg0,Tipologieimmobili arg1) {
			return Integer.valueOf(arg0.getCodTipologiaImmobile()).compareTo(Integer.valueOf(arg0.getCodTipologiaImmobile()));
		}
		
	};

	private Comparator<Statoconservativo> comparatorStatoConservativo = new Comparator<Statoconservativo>(){

		@Override
		public int compare(Statoconservativo arg0,Statoconservativo arg1) {
			return Integer.valueOf(arg0.getCodStatoConservativo()).compareTo(Integer.valueOf(arg1.getCodStatoConservativo()));
		}
		
	};

	private Comparator<Riscaldamenti> comparatorRiscaldamenti = new Comparator<Riscaldamenti>(){

		@Override
		public int compare(Riscaldamenti arg0,Riscaldamenti arg1) {
			return Integer.valueOf(arg0.getCodRiscaldamento()).compareTo(Integer.valueOf(arg1.getCodRiscaldamento()));
		}
		
	};

	private Comparator<Agenti> comparatorAgenti = new Comparator<Agenti>(){

		@Override
		public int compare(Agenti arg0,Agenti arg1) {
			return Integer.valueOf(arg0.getCodAgente()).compareTo(arg1.getCodAgente());
		}
		
	};

	private Comparator<Classicliente> comparatorClassiCliente = new Comparator<Classicliente>(){

		@Override
		public int compare(Classicliente arg0,Classicliente arg1) {
			return Integer.valueOf(arg0.getCodClasseCliente()).compareTo(Integer.valueOf(arg1.getCodClasseCliente()));
		}
		
	};

	private Comparator<Classienergetiche> comparatorClassiEnergetiche = new Comparator<Classienergetiche>(){

		@Override
		public int compare(Classienergetiche arg0,Classienergetiche arg1) {
			return Integer.valueOf(arg0.getCodClasseEnergetica()).compareTo(Integer.valueOf(arg1.getCodClasseEnergetica())); 
		}
		
	};
	
	public DettaglioImmobileView() {

	}

	@Override
	public void createPartControl(Composite parent) {

		ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createScrolledForm(parent);
		f.setExpandVertical(true);
		f.setImage(Activator.getImageDescriptor("/icons/immobile.png").createImage());
		f.setText("Dettaglio immobile");
		f.getBody().setLayout(new GridLayout());
		
		refreshDettaglioImmobile = new RefreshDettaglioImmobile();
		salvaImmobile = new SalvaImmobile();
		cancellaImmobile = new CancellaImmobile();		
//		openPopUpAnagrafiche = new OpenPopUpAnagrafiche();
		
		cambiaStoricoAction = new CambiaStoricoAction("Sposta in archivo storico/corrente",
													  Activator.getImageDescriptor("icons/filesave16.png"));
				
		stampaImmobiliAction = new StampaImmobiliAction("Report immobili", 
										 				Action.AS_DROP_DOWN_MENU);
		
		IToolBarManager mgr = f.getToolBarManager();
		mgr.add(new OpenPopupComuniAction("Scegli comune", 
				 						  Activator.getImageDescriptor("icons/cercacomune.png")));
		mgr.add(refreshDettaglioImmobile);
		mgr.add(salvaImmobile);
		mgr.add(cancellaImmobile);		
		mgr.add(cambiaStoricoAction);
		mgr.add(stampaImmobiliAction);
		
		f.updateToolBar();
		
		fillTipologieStanze();
		
		Composite cCod = ft.createComposite(f.getBody());
		
		GridLayout gl = new GridLayout(2, false);		
		cCod.setLayout(gl);
		
		GridData gd = new GridData(SWT.FILL, SWT.BOTTOM, true, false);		
		cCod.setLayoutData(gd);
		
		GridData gd2 = new GridData(SWT.LEFT, SWT.TOP, false, false);
		Label codImmobileLbl = ft.createLabel(cCod, "Codice database : ");
		codImmobileLbl.setForeground(new Color(null,12,0,126));
		Font font = new Font(null,"Arial",10,SWT.BOLD);
		codImmobileLbl.setFont(font);
		codImmobileLbl.setLayoutData(gd2);
		codImmobile = ft.createLabel(cCod, "");
		codImmobile.setLayoutData(gd);
		
		posizioneImmobili();
//		anagraficaImmobile();
		datiImmobile();
		stanzeImmobile();
		agenzia();
		allegatiImmobile();
		datiCatastali();
		f.reflow(true);
	}
	
	private void posizioneImmobili(){
		
		Section section_position = ft.createSection(f.getBody(), 
													Section.DESCRIPTION|Section.TITLE_BAR|Section.TWISTIE);
		
		section_position.setExpanded(true);
		section_position.addExpansionListener(new ExpansionAdapter(){

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

		GridData gdExpH20 = new GridData();
		gdExpH20.grabExcessHorizontalSpace = true;		
		gdExpH20.horizontalAlignment = SWT.FILL;
		gdExpH20.minimumHeight = 20;

		GridData gdExpH = new GridData();
		gdExpH.grabExcessHorizontalSpace = true;
		gdExpH.verticalAlignment = SWT.FILL;
		gdExpH.horizontalAlignment = SWT.FILL;
		gdExpH.minimumHeight = 30;

		GridData gdExpImageH = new GridData();
		gdExpImageH.verticalAlignment = SWT.CENTER;
//		gdExpImageH.grabExcessVerticalSpace = true;
		gdExpImageH.minimumHeight = 30;

		GridData gdExpIndirizzoH = new GridData();
		gdExpIndirizzoH.verticalAlignment = SWT.FILL;
		gdExpIndirizzoH.horizontalAlignment = SWT.FILL;
		gdExpIndirizzoH.grabExcessVerticalSpace = true;
//		gdExpIndirizzoH.minimumHeight = 30;
		gdExpIndirizzoH.grabExcessHorizontalSpace = true;

		GridData gdExpHProvinciaCap = new GridData();
		gdExpHProvinciaCap.widthHint	 = 50;
		gdExpHProvinciaCap.minimumHeight = 30;
		
		GridData gdExpVHS = new GridData();
		gdExpVHS.grabExcessHorizontalSpace = true;
		gdExpVHS.horizontalAlignment = SWT.FILL;
		gdExpVHS.verticalIndent = 0;
		gdExpVHS.horizontalIndent = 0;
		gdExpVHS.heightHint	= 60;

		GridData gdExpIndirizzoVHS = new GridData();
		gdExpIndirizzoVHS.grabExcessHorizontalSpace = true;
		gdExpIndirizzoVHS.verticalAlignment = SWT.TOP;
		gdExpIndirizzoVHS.horizontalAlignment = SWT.FILL;
		gdExpIndirizzoVHS.verticalIndent = 0;
		gdExpIndirizzoVHS.horizontalIndent = 0;
		
		
		GridData gdExpHZona = new GridData();
		gdExpHZona.grabExcessHorizontalSpace = true;		
		gdExpHZona.horizontalAlignment = SWT.FILL;
		gdExpHZona.heightHint = 20;

		GridData gdExpHRiferimento = new GridData();						
		gdExpHRiferimento.minimumHeight = 20;
		gdExpHRiferimento.widthHint = 100;

		section_position.setLayout(new GridLayout());
		section_position.setLayoutData(gdExpVH);
		section_position.setText("Posizione");
		section_position.setDescription("posizione dell'immobile");		
		
		Composite cposizione = ft.createComposite(section_position,SWT.FLAT);
//		cposizione.setBackground(new Color(null,100,12,140));
		GridLayout glposizione = new GridLayout();
		
		cposizione.setLayout(glposizione);
		cposizione.setLayoutData(gdExpVH);
		
		
		Label lriferimento = ft.createLabel(cposizione, "Codice riferimento");
		lriferimento.setLayoutData(gdExpHRiferimento);
		triferimento = ft.createText(cposizione, "", SWT.DOUBLE_BUFFERED);
		triferimento.setLayoutData(gdExpHRiferimento);

		GridLayout glcitta = new GridLayout(3,false);		
		glcitta.marginWidth = 1;
				
		Composite ccitta = ft.createComposite(cposizione);
		ccitta.setLayoutData(gdExpVHS);
		ccitta.setLayout(glcitta);		

		Label lcitta = ft.createLabel(ccitta, "Citt\u00E1");
		lcitta.setLayoutData(gdExpH);
		Label lprovincia = ft.createLabel(ccitta, "Provincia");
		lprovincia.setLayoutData(gdExpHProvinciaCap);
		Label lcap = ft.createLabel(ccitta, "Cap");
		lcap.setLayoutData(gdExpHProvinciaCap);
		
		tcitta = ft.createText(ccitta, "", SWT.DOUBLE_BUFFERED);		
		tcitta.setLayoutData(gdExpH);
		
		tprovincia = ft.createText(ccitta, "", SWT.DOUBLE_BUFFERED);
		tprovincia.setLayoutData(gdExpHProvinciaCap);
		tcap = ft.createText(ccitta, "", SWT.DOUBLE_BUFFERED);
		tcap.setLayoutData(gdExpHProvinciaCap);
		
		Label lzona = ft.createLabel(cposizione, "Zona");
		lzona.setLayoutData(gdExpHZona);
		tzona = ft.createText(cposizione, "", SWT.DOUBLE_BUFFERED);
		tzona.setLayoutData(gdExpHZona);
		
		GridData gdncivico = new GridData();
		gdncivico.minimumWidth = 300;
		gdncivico.widthHint = 100;
		
		GridLayout glindirizzo = new GridLayout(2,false);
		
		Composite cindirizzo = ft.createComposite(cposizione);
		cindirizzo.setLayoutData(gdExpIndirizzoVHS);
		cindirizzo.setLayout(glindirizzo);
//		cindirizzo.setBackground(new Color(null,100,100,50));
		
		Label lindirizzo = ft.createLabel(cindirizzo, "Indirizzo");
		lindirizzo.setLayoutData(gdExpH);
		
		Label lncivico = ft.createLabel(cindirizzo, "Numero");
		lncivico.setLayoutData(gdncivico);
		
		tindirizzo = ft.createText(cindirizzo, "", SWT.BORDER);		
		tindirizzo.setLayoutData(gdExpH);
		tindirizzo.setToolTipText("E' consigliato inserire prima il numero seguito dalla via per corretto funzionamento vista Mappa");

		tncivico = ft.createText(cindirizzo, "", SWT.BORDER);
		tncivico.setLayoutData(gdncivico);		
 
		ft.paintBordersFor(ccitta);
		ft.paintBordersFor(cposizione);
		ft.paintBordersFor(cindirizzo);
		
		section_position.setClient(cposizione);
	}
	
	private void bindPosizioneImmobile(DataBindingContext bindingContext){
		if (immobile != null){
			
			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(triferimento), 
									 PojoProperties.value("rif").observe(immobile),
									 null, 
									 null);

			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tcitta), 
									 PojoProperties.value("citta").observe(immobile),
									 null, 
									 null);

			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tprovincia), 
									 PojoProperties.value("provincia").observe(immobile),
									 null, 
									 null);

			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tcap), 
									 PojoProperties.value("cap").observe(immobile),
									 null, 
									 null);
			
			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tzona), 
									 PojoProperties.value("zona").observe(immobile),
									 null, 
									 null);

			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tindirizzo), 
									 PojoProperties.value("indirizzo").observe(immobile),
									 null, 
									 null);

			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tncivico), 
									 PojoProperties.value("ncivico").observe(immobile),
									 null, 
									 null);
			
		}
	}
	
	private void datiImmobile(){
		
		Section section = ft.createSection(f.getBody(), 
				   						   Section.DESCRIPTION|Section.TITLE_BAR|
				   						   Section.TWISTIE);

		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				f.reflow(true);
			}
			
		});

//		section.setExpanded(true);
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;

		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;		

		GridData gdExpH = new GridData();
		gdExpH.grabExcessHorizontalSpace = true;		
		gdExpH.horizontalAlignment = SWT.FILL;
//		gdExpH.verticalAlignment = SWT.BOTTOM;
		
		GridData gdExpHSC = new GridData();
		gdExpHSC.grabExcessHorizontalSpace = true;
		gdExpHSC.grabExcessVerticalSpace = false;
		gdExpHSC.horizontalAlignment = SWT.FILL;
		gdExpHSC.verticalAlignment = SWT.TOP;

		GridData gdExpHLabel2 = new GridData();
		gdExpHLabel2.grabExcessHorizontalSpace = true;
		gdExpHLabel2.grabExcessVerticalSpace = true;
		gdExpHLabel2.horizontalAlignment = SWT.FILL;
		gdExpHLabel2.verticalAlignment = SWT.BOTTOM;


		GridData gdExpHSp3 = new GridData();
		gdExpHSp3.grabExcessHorizontalSpace = true;
		gdExpHSp3.grabExcessVerticalSpace = true;
		gdExpHSp3.horizontalAlignment = SWT.FILL;
		gdExpHSp3.verticalAlignment = SWT.FILL;
		gdExpHSp3.horizontalSpan = 3;

		section.setLayout(new GridLayout());
		section.setLayoutData(gdExpVH);
		section.setText("Dati immobile");
		section.setDescription("dati descrittivi dell'immobile");		

		Composite cdati = ft.createComposite(section);
		GridLayout gldati = new GridLayout();
		gldati.numColumns = 3;
		cdati.setLayout(gldati);
		cdati.setLayoutData(gdExpVH);
		
//		cdati.setBackground(new Color(null,123,223,100));
		Label ltipologia = ft.createLabel(cdati, "Tipologia");
		Label lstato = ft.createLabel(cdati, "Stato conservativo");
		//lstato.setLayoutData(gdExpHLabel2);
		Label lriscaldamenti = ft.createLabel(cdati, "Impianto riscaldamento");

		cvtipologia = new ComboViewer(cdati,SWT.DROP_DOWN | SWT.READ_ONLY | SWT.DOUBLE_BUFFERED);
		cvtipologia.getCombo().setLayoutData(gdExpHSC);

		cvstato = new ComboViewer(cdati,SWT.DROP_DOWN | SWT.READ_ONLY | SWT.DOUBLE_BUFFERED);
		cvstato.getCombo().setLayoutData(gdExpHSC);

		cvriscaldamenti = new ComboViewer(cdati,SWT.DROP_DOWN | SWT.READ_ONLY | SWT.DOUBLE_BUFFERED);
		cvriscaldamenti.getCombo().setLayoutData(gdExpHSC);

		Label lclasseenergetica = ft.createLabel(cdati, "Classe energetica");
		Label lannoCostruzione = ft.createLabel(cdati, "Anno costruzione");
		Label lmq = ft.createLabel(cdati, "Metri quadrati");

		cvclasseenergetica = new ComboViewer(cdati,SWT.DROP_DOWN | SWT.READ_ONLY | SWT.DOUBLE_BUFFERED);
		cvclasseenergetica.getCombo().setLayoutData(gdExpHSC);
		
		tannoCostruzione = ft.createText(cdati, "",SWT.DOUBLE_BUFFERED);
		tannoCostruzione.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
		
		tannoCostruzione.setLayoutData(gdExpHSC);
		tannoCostruzione.addListener(SWT.KeyUp, new Listener(){

			public void handleEvent(Event event) {
				Integer annocostruzione = new Integer(tannoCostruzione.getText());
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				if (annocostruzione > c.get(Calendar.YEAR)){
					decAnnoCostruzione.setImage(Activator.getDefault().getImageDescriptor("/icons/error.png").createImage());
					decAnnoCostruzione.setDescriptionText("Anno di costruzione maggiore dell'anno attuale");
					f.getBody().redraw();
				}else{
					decAnnoCostruzione.setImage(null);
					decAnnoCostruzione.setDescriptionText(null);	
					f.getBody().redraw();
				}
			}
			
		});
		
		tannoCostruzione.addVerifyListener(new VerifyListener(){
			public void verifyText(VerifyEvent e) {				
				try {
					Integer annocostruzione = new Integer(e.text.replaceAll("[.]",""));
					e.text = String.valueOf(annocostruzione);
				} catch (NumberFormatException e1) {
					e.text = "";
				}					
			}			
		});
		
		decAnnoCostruzione = new ControlDecoration(tannoCostruzione,SWT.TOP | SWT.RIGHT);
		
		tmq = ft.createText(cdati, "",SWT.DOUBLE_BUFFERED);
		tmq.setLayoutData(gdExpHSC);
		tmq.addVerifyListener(new VerifyListener(){
			public void verifyText(VerifyEvent e) {				
				try {
					Integer mq = new Integer(e.text.replaceAll("[.]",""));
//					Integer prezzo = new Integer(e.text);
				} catch (NumberFormatException e1) {
					e.text = "";
				}					
			}			
		});
		tmq.addListener(SWT.KeyUp, new Listener(){

			public void handleEvent(Event event) {
				Integer mq = new Integer(tmq.getText());
				if (mq < 0){
					decmq.setImage(Activator.getDefault().getImageDescriptor("/icons/error.png").createImage());
					decmq.setDescriptionText("La superficie in metri quadri non pu� essere minore di 0");
					f.getBody().redraw();
				}else{
					decmq.setImage(null);
					decmq.setDescriptionText(null);	
					f.getBody().redraw();
				}
			}
			
		});
		
		decmq = new ControlDecoration(tmq,SWT.TOP | SWT.RIGHT);
		
		Label lprezzo = ft.createLabel(cdati, "Prezzo");
		Label lmutuo = ft.createLabel(cdati, "Mutuo");		
		Label lspese = ft.createLabel(cdati, "Spese");
				
		tprezzo = ft.createText(cdati, "",SWT.DOUBLE_BUFFERED);		
		tprezzo.setLayoutData(gdExpHSC);
		tprezzo.addVerifyListener(new VerifyListener(){
			public void verifyText(VerifyEvent e) {				
				try {
					Integer prezzo = new Integer(e.text.replaceAll("[.]",""));
//					Integer prezzo = new Integer(e.text);
				} catch (NumberFormatException e1) {
					e.text = "";
				}					
			}			
		});
		tprezzo.addListener(SWT.KeyUp, new Listener(){

			public void handleEvent(Event event) {
				Integer prezzo = new Integer(tprezzo.getText());
				if (prezzo < 0){
					decprezzo.setImage(Activator.getDefault().getImageDescriptor("/icons/error.png").createImage());
					decprezzo.setDescriptionText("Il prezzo non pu� essere minore di 0");
					f.getBody().redraw();
				}else{
					decprezzo.setImage(null);
					decprezzo.setDescriptionText(null);	
					f.getBody().redraw();
				}
			}
			
		});
		
		decprezzo = new ControlDecoration(tprezzo,SWT.TOP | SWT.RIGHT);
		
		tmutuo = ft.createText(cdati, "",SWT.DOUBLE_BUFFERED);
		tmutuo.setLayoutData(gdExpHSC);
		tmutuo.addVerifyListener(new VerifyListener(){
			public void verifyText(VerifyEvent e) {				
				try {
					Integer mutuo = new Integer(e.text.replaceAll("[.]",""));
//					Integer prezzo = new Integer(e.text);
				} catch (NumberFormatException e1) {
					e.text = "";
				}					
			}			
		});
		tmutuo.addListener(SWT.KeyUp, new Listener(){

			public void handleEvent(Event event) {
				Integer mutuo = new Integer(tmutuo.getText());
				if (mutuo < 0){
					decMutuo.setImage(Activator.getDefault().getImageDescriptor("/icons/error.png").createImage());
					decMutuo.setDescriptionText("Il mutuo non pu� essere minore di 0");
					f.getBody().redraw();
				}else{
					decMutuo.setImage(null);
					decMutuo.setDescriptionText(null);	
					f.getBody().redraw();
				}
			}
			
		});
		
		decMutuo = new ControlDecoration(tmutuo,SWT.TOP | SWT.RIGHT);

		tspese = ft.createText(cdati, "",SWT.DOUBLE_BUFFERED);
		tspese.setLayoutData(gdExpHSC);
		tspese.addVerifyListener(new VerifyListener(){
			public void verifyText(VerifyEvent e) {				
				try {
					Integer spese = new Integer(e.text.replaceAll("[.]",""));
//					Integer prezzo = new Integer(e.text);
				} catch (NumberFormatException e1) {
					e.text = "";
				}					
			}			
		});
		tspese.addListener(SWT.KeyUp, new Listener(){

			public void handleEvent(Event event) {
				Integer spese = new Integer(tmq.getText());
				if (spese < 0){
					decSpese.setImage(Activator.getDefault().getImageDescriptor("/icons/error.png").createImage());
					decSpese.setDescriptionText("Le spese non possono essere minore di 0");
					f.getBody().redraw();
				}else{
					decSpese.setImage(null);
					decSpese.setDescriptionText(null);	
					f.getBody().redraw();
				}
			}
			
		});
		
		decSpese = new ControlDecoration(tspese,SWT.TOP | SWT.RIGHT);
		
		Label lliberoda = ft.createLabel(cdati, "Libero da");
		GridData gdliberoda = new GridData();		
		gdliberoda.horizontalSpan = 3;
		lliberoda.setLayoutData(gdliberoda);
		
		Composite cLiberoDa = new Composite(cdati,SWT.NONE);

		GridLayout glLiberoDa = new GridLayout();
		glLiberoDa.numColumns = 2;
		glLiberoDa.marginHeight = 0;
		glLiberoDa.marginBottom = 0;
		glLiberoDa.marginRight = 0;
		glLiberoDa.marginLeft = 0;
		
		cLiberoDa.setLayout(glLiberoDa);
		GridData gdliberodacomp = new GridData(SWT.LEFT, SWT.FILL, false, false);
		gdliberodacomp.horizontalSpan = 3;
		cLiberoDa.setLayoutData(gdliberodacomp);
		
		tliberoda = new Text(cLiberoDa,SWT.DOUBLE_BUFFERED);
		tliberoda.setEditable(false);
		GridData dfdcdata = new GridData();
		dfdcdata.minimumWidth = 200;
		dfdcdata.widthHint = 200;
		
		tliberoda.setLayoutData(dfdcdata);
		bOpenDialog2 = new Button(cLiberoDa,SWT.PUSH|SWT.FLAT);
		bOpenDialog2.setImage(Activator.getImageDescriptor("/icons/calendario.png").createImage());
		bOpenDialog2.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {

                final SWTCalendarDialog cal = new SWTCalendarDialog(getViewSite().getShell().getDisplay());

                cal.addDateChangedListener(new SWTCalendarListener() {

                    public void dateChanged(SWTCalendarEvent calendarEvent) {

                    	tliberoda.setText(formatter.format(calendarEvent.getCalendar().getTime()));
                    	immobile.setDatalibero(calendarEvent.getCalendar().getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());                    	
                    	
                    }

                });



                if (tliberoda.getText() != null && tliberoda.getText().length() > 0) {

                    try {

                        Date d = formatter.parse(tliberoda.getText());

                        cal.setDate(d);

                    } catch (ParseException pe) {



                    }

                }

                cal.open();



            }

        });
		
		tliberoda.setLayoutData(gdExpH);
		
		
		

		
		Composite c = ft.createComposite(cdati);
		c.setLayoutData(gdExpHSp3);
		GridLayout gldescs = new GridLayout();
		gldescs.numColumns = 2;
		c.setLayout(gldescs);
		ft.paintBordersFor(c);
		Label ldescrizione = ft.createLabel(c, "Descrizione");
		Label ldescrizioneMutuo = ft.createLabel(c, "Descrizione mutuo");		

		GridData gdMemo = new GridData();
		gdMemo.minimumHeight = 50;
		gdMemo.grabExcessHorizontalSpace = true;
		gdMemo.grabExcessVerticalSpace = true;
		gdMemo.horizontalAlignment = SWT.FILL;
		gdMemo.verticalAlignment = SWT.FILL;
		
		
		tdescrizione = ft.createText(c, "", SWT.MULTI|SWT.DOUBLE_BUFFERED|SWT.V_SCROLL|SWT.WRAP|SWT.H_SCROLL);
		tdescrizione.setLayoutData(gdMemo);
		tdescrizioneMutuo = ft.createText(c, "", SWT.MULTI|SWT.DOUBLE_BUFFERED|SWT.V_SCROLL|SWT.WRAP|SWT.H_SCROLL);
		tdescrizioneMutuo.setLayoutData(gdMemo);
		
		ft.paintBordersFor(cdati);
		
		section.setClient(cdati);
	}
	
	private void bindDatiImmobile(DataBindingContext bindingContext){
		if (immobile != null){
			codImmobile.setText((immobile.getCodImmobile() == 0)? "nessuno":String.valueOf(immobile.getCodImmobile()));
			
			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tannoCostruzione),																
									 PojoProperties.value("annoCostruzione").observe(immobile),
					                 null, 
					                 null);
			
			cvtipologia.setContentProvider(new IStructuredContentProvider(){
	
				@Override
				public Object[] getElements(Object inputElement) {				 
					return MobiliaDatiBaseCache.getInstance().getTipologieImmobili().toArray();
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
					return ((Tipologieimmobili)element).getDescrizione();
				}
				
			});
			
			cvtipologia.addSelectionChangedListener(new ISelectionChangedListener(){
	
				@Override
				public void selectionChanged(SelectionChangedEvent event) {								
					immobile.setTipologieimmobili(immobile.getObjectContext().localObject((Tipologieimmobili)((StructuredSelection)event.getSelection()).getFirstElement()));
				}
				
			});
							
			cvtipologia.setInput(new Object());
			if (immobile.getTipologieimmobili() != null){
				int index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getTipologieImmobili(), immobile.getTipologieimmobili(), comparatorTipologia);
				Object[] sel = new Object[1];
				sel[0] = MobiliaDatiBaseCache.getInstance().getTipologieImmobili().get(index);
				StructuredSelection ss = new StructuredSelection(sel);
				cvtipologia.setSelection(ss);		

				ArrayList<Tipologieimmobili> tipologie = MobiliaDatiBaseCache.getInstance().getTipologieImmobili();
				int count = 0;
				boolean findit = false;
				for (Iterator iterator = tipologie.iterator(); iterator.hasNext();) {
					if (immobile.getTipologieimmobili().getCodTipologiaImmobile() == ((Tipologieimmobili)iterator.next()).getCodTipologiaImmobile()){
						findit = true;
						break;
					}else{
						count++;
					}
					
				}
				if (findit == false){
					count = -1;
				}
				if (count > -1){
					sel = new Object[1];
					sel[0] = MobiliaDatiBaseCache.getInstance().getTipologieImmobili().get(count);
					ss = new StructuredSelection(sel);
					cvtipologia.setSelection(ss);
				}
				
			}
			
			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tprezzo),																
					 PojoProperties.value("prezzo").observe(immobile),
	                 null, 
	                 null);
						
			if (immobile.getDatalibero() != null){
				tliberoda.setText(formatter.format(Date.from(immobile.getDatalibero().atStartOfDay(ZoneId.systemDefault()).toInstant())));
			}
			
			cvstato.setContentProvider(new IStructuredContentProvider(){
	
				@Override
				public Object[] getElements(Object inputElement) {
					return MobiliaDatiBaseCache.getInstance().getStatiConservativi().toArray();
				}
	
				@Override
				public void dispose() {
				}
	
				@Override
				public void inputChanged(Viewer viewer, Object oldInput,Object newInput) {
				}
				
			});
			
			cvstato.setLabelProvider(new LabelProvider(){
	
				@Override
				public String getText(Object element) {
					return ((Statoconservativo)element).getDescrizione();
				}
				
			});
			
			cvstato.addSelectionChangedListener(new ISelectionChangedListener(){
	
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					immobile.setStatoconservativo(immobile.getObjectContext().localObject((Statoconservativo)((StructuredSelection)event.getSelection()).getFirstElement()));				
				}
				
			});
			
			cvstato.setInput(new Object());		
			
			if (immobile.getStatoconservativo() != null){
				ArrayList<Statoconservativo> stati = MobiliaDatiBaseCache.getInstance().getStatiConservativi();
				int count = 0;
				boolean findit = false;
				for (Iterator iterator = stati.iterator(); iterator.hasNext();) {
					if (immobile.getStatoconservativo().getCodStatoConservativo() == ((Statoconservativo)iterator.next()).getCodStatoConservativo()){
						findit = true;
						break;
					}else{
						count++;
					}
					
				}
				if (findit == false){
					count = -1;
				}
				
				int index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getStatiConservativi(), immobile.getStatoconservativo(), comparatorStatoConservativo);
				Object[] sel = new Object[1];
				sel[0] = MobiliaDatiBaseCache.getInstance().getStatiConservativi().get(index);
				StructuredSelection ss = new StructuredSelection(sel);
				cvstato.setSelection(ss);		
			}
			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tmutuo),																
					 PojoProperties.value("mutuo").observe(immobile),
	                 null, 
	                 null);

			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tmq),																
					 PojoProperties.value("mq").observe(immobile),
	                 null, 
	                 null);
		
			cvriscaldamenti.setContentProvider(new IStructuredContentProvider(){
	
				@Override
				public Object[] getElements(Object inputElement) {
					return MobiliaDatiBaseCache.getInstance().getRiscaldamenti().toArray();
				}
	
				@Override
				public void dispose() {
				}
	
				@Override
				public void inputChanged(Viewer viewer, Object oldInput,Object newInput) {
				}
				
			});
			
			cvriscaldamenti.setLabelProvider(new LabelProvider(){
	
				@Override
				public String getText(Object element) {
					return ((Riscaldamenti)element).getDescrizione();
				}
				
			});
			
			cvriscaldamenti.addSelectionChangedListener(new ISelectionChangedListener(){
	
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					immobile.setRiscaldamenti(immobile.getObjectContext().localObject((Riscaldamenti)((StructuredSelection)event.getSelection()).getFirstElement()));				
				}
				
			});
			
			cvriscaldamenti.setInput(new Object());
			
			
			if (immobile.getRiscaldamenti() != null){			
				int index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getRiscaldamenti(), immobile.getRiscaldamenti(), comparatorRiscaldamenti);
				Object[] sel = new Object[1];
				sel[0] = MobiliaDatiBaseCache.getInstance().getRiscaldamenti().get(index);
				StructuredSelection ss = new StructuredSelection(sel);
				cvriscaldamenti.setSelection(ss);		
			}
			
			cvclasseenergetica.setContentProvider(new IStructuredContentProvider(){
				
				@Override
				public Object[] getElements(Object inputElement) {				 
					return MobiliaDatiBaseCache.getInstance().getClassiEnergetiche().toArray();
				}
	
				@Override
				public void dispose() {
				}
	
				@Override
				public void inputChanged(Viewer viewer, Object oldInput,Object newInput) {
				}
				
			});
			
			cvclasseenergetica.setLabelProvider(new LabelProvider(){
	
				@Override
				public String getText(Object element) {
					return ((Classienergetiche)element).getNome();
				}
				
			});
			
			cvclasseenergetica.addSelectionChangedListener(new ISelectionChangedListener(){
	
				@Override
				public void selectionChanged(SelectionChangedEvent event) {					
					immobile.setClassienergetiche(immobile.getObjectContext().localObject((Classienergetiche)((StructuredSelection)event.getSelection()).getFirstElement()));
				}
				
			});
							
			cvclasseenergetica.setInput(new Object());
			if (immobile.getClassienergetiche() != null){
				int index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getClassiEnergetiche(), immobile.getClassienergetiche(), comparatorClassiEnergetiche);
				Object[] sel = new Object[1];				
				try {
					sel[0] = MobiliaDatiBaseCache.getInstance().getClassiEnergetiche().get(index);
					StructuredSelection ss = new StructuredSelection(sel);
					cvclasseenergetica.setSelection(ss);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tspese),																
					 PojoProperties.value("spese").observe(immobile),
	                 null, 
	                 null);

			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tdescrizione),																
					 PojoProperties.value("descrizione").observe(immobile),
	                 null, 
	                 null);

			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tdescrizioneMutuo),																
					 PojoProperties.value("mutuodescrizione").observe(immobile),
	                 null, 
	                 null);

			bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(tspese),																
					 PojoProperties.value("spese").observe(immobile),
	                 null, 
	                 null);
	
		}
	}

	private void stanzeImmobile(){
		
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
		
		GridData gdExpVHTable = new GridData();
		gdExpVHTable.grabExcessHorizontalSpace = true;
		gdExpVHTable.grabExcessVerticalSpace = true;
		gdExpVHTable.horizontalAlignment = SWT.FILL;
		gdExpVHTable.verticalAlignment = SWT.FILL;	
		gdExpVHTable.minimumHeight = 80;

		section.setLayout(new GridLayout());
		section.setLayoutData(gdExpVH);
		section.setText("Stanze immobile");
		section.setDescription("stanze immobile");
		
		Composite contenitore = ft.createComposite(section,SWT.NONE);
		contenitore.setLayout(new GridLayout());
		contenitore.setLayoutData(gdExpVH);
		ft.paintBordersFor(contenitore);

		Composite toolbar = ft.createComposite(contenitore,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ihNewStanza = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNewStanza.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNewStanza.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNewStanza.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
//				if (immobile.getCodImmobile() != 0) {
					Stanzeimmobili siModel = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Stanzeimmobili.class);					
					
					immobile.addToStanzeimmobilis(siModel);
					tvStanze.setInput(immobile.getStanzeimmobilis());
					tvStanze.refresh();
					
					TableItem ti = tvStanze.getTable().getItem(tvStanze.getTable().getItemCount()-1);
					Object[] sel = new Object[1];
					sel[0] = ti.getData();

					StructuredSelection ss = new StructuredSelection(sel);
					
					tvStanze.setSelection(ss, true);

					Event ev = new Event();
					ev.item = ti;
					ev.data = ti.getData();
					ev.widget = tvStanze.getTable();
					tvStanze.getTable().notifyListeners(SWT.Selection, ev);
					
//				}else {
//					MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
//							"Inserimento stanza", 
//							"Eseguire il salvataggio dell'immobile");
//				}

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ihCancellaStanza = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancellaStanza.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaStanza.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaStanza.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (tvStanze.getSelection() != null){
					Iterator it = ((StructuredSelection)tvStanze.getSelection()).iterator();
					while (it.hasNext()) {
						Stanzeimmobili siVO = (Stanzeimmobili)it.next();
						immobile.removeFromStanzeimmobilis(siVO);
					}
					tvStanze.refresh();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		
		
		tvStanze = new TableViewer(contenitore,SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvStanze.getTable().setLayoutData(gdExpVHTable);
		tvStanze.getTable().setHeaderVisible(true);
		tvStanze.getTable().setLinesVisible(true);
		
		tcTipologiaStaza = new TableColumn(tvStanze.getTable(),SWT.CENTER,0);
		tcTipologiaStaza.setText("Tipologia");
		tcTipologiaStaza.setWidth(200);
		
		tcmq = new TableColumn(tvStanze.getTable(),SWT.CENTER,1);
		tcmq.setText("Metri quadri");
		tcmq.setWidth(70);

		section.setClient(contenitore);
	}

	private void datiCatastali(){
		
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
		
		GridData gdExpVHTable = new GridData();
		gdExpVHTable.grabExcessHorizontalSpace = true;
		gdExpVHTable.grabExcessVerticalSpace = true;
		gdExpVHTable.horizontalAlignment = SWT.FILL;
		gdExpVHTable.verticalAlignment = SWT.FILL;	
		gdExpVHTable.minimumHeight = 80;

		section.setLayout(new GridLayout());
		section.setLayoutData(gdExpVH);
		section.setText("Dati catastali immobile");
		section.setDescription("Dati catastali immobile");
		
		Composite contenitore = ft.createComposite(section,SWT.NONE);
		contenitore.setLayout(new GridLayout());
		contenitore.setLayoutData(gdExpVH);
		ft.paintBordersFor(contenitore);

		Composite toolbar = ft.createComposite(contenitore,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ihNewDatiCatastali = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNewDatiCatastali.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNewDatiCatastali.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNewDatiCatastali.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				Daticatastali dcVO = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Daticatastali.class);								
				immobile.addToDaticatastalis(dcVO);
				tvDatiCatastali.setInput(immobile.getDaticatastalis());
				tvDatiCatastali.refresh();
				
				TableItem ti = tvDatiCatastali.getTable().getItem(tvDatiCatastali.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvDatiCatastali.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvDatiCatastali.getTable();
				tvDatiCatastali.getTable().notifyListeners(SWT.Selection, ev);

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ihCancellaDatiCatastali = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancellaDatiCatastali.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaDatiCatastali.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaDatiCatastali.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (tvDatiCatastali.getSelection() != null){
					Iterator it = ((StructuredSelection)tvDatiCatastali.getSelection()).iterator();
					while (it.hasNext()) {
						Daticatastali dcVO = (Daticatastali)it.next();
						immobile.removeFromDaticatastalis(dcVO);
					}
					tvDatiCatastali.refresh();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});		
		
		tvDatiCatastali = new TableViewer(contenitore,SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvDatiCatastali.getTable().setLayoutData(gdExpVHTable);
		tvDatiCatastali.getTable().setHeaderVisible(true);
		tvDatiCatastali.getTable().setLinesVisible(true);
				
		tcfoglio = new TableColumn(tvDatiCatastali.getTable(),SWT.CENTER,0);
		tcfoglio.setText("Foglio");
		tcfoglio.setWidth(70);
		
		tcparticella = new TableColumn(tvDatiCatastali.getTable(),SWT.CENTER,1);
		tcparticella.setText("Particella");
		tcparticella.setWidth(70);

		tcsubalterno = new TableColumn(tvDatiCatastali.getTable(),SWT.CENTER,2);
		tcsubalterno.setText("Subalterno");
		tcsubalterno.setWidth(70);

		tccategoria = new TableColumn(tvDatiCatastali.getTable(),SWT.CENTER,3);
		tccategoria.setText("Categoria");
		tccategoria.setWidth(120);

		tcrendita = new TableColumn(tvDatiCatastali.getTable(),SWT.CENTER,4);
		tcrendita.setText("Rendita");
		tcrendita.setWidth(70);

		tcredditoDomenicale = new TableColumn(tvDatiCatastali.getTable(),SWT.CENTER,5);
		tcredditoDomenicale.setText("Reddito dominicale");
		tcredditoDomenicale.setWidth(120);

		tcredditoAgricolo = new TableColumn(tvDatiCatastali.getTable(),SWT.CENTER,6);
		tcredditoAgricolo.setText("Reddito agricolo");
		tcredditoAgricolo.setWidth(120);

		tcdimensione = new TableColumn(tvDatiCatastali.getTable(),SWT.CENTER,7);
		tcdimensione.setText("Dimensione");
		tcdimensione.setWidth(70);



		section.setClient(contenitore);
	}

	private void bindStanzeImmobile(DataBindingContext bindingContext){
		if (immobile != null){
			tvStanze.setContentProvider(new IStructuredContentProvider(){
	
				@Override
				public Object[] getElements(Object inputElement) {
					return (immobile.getStanzeimmobilis() == null)
					       ? new ArrayList().toArray()
					       : immobile.getStanzeimmobilis().toArray();
				}
	
				@Override
				public void dispose() {
				}
	
				@Override
				public void inputChanged(Viewer viewer, Object oldInput,
						Object newInput) {
				}
				
			});
			
			tvStanze.setLabelProvider(new ITableLabelProvider(){
	
				@Override
				public Image getColumnImage(Object element, int columnIndex) {
					return null;
				}
	
				@Override
				public String getColumnText(Object element, int columnIndex) {
					Stanzeimmobili siModel = (Stanzeimmobili)element;
					switch (columnIndex){
						case 0: return (siModel.getTipologiastanze() == null)
									   ? ""
									   : (siModel.getTipologiastanze().getDescrizione() == null)
									     ? ""
									     : siModel.getTipologiastanze().getDescrizione();					        
						
						case 1: return (siModel.getMq() == 0)
						   			   ? "0"
						   			   : String.valueOf(siModel.getMq());
						
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
			
			TableViewerColumn tcvTipologia = new TableViewerColumn(tvStanze,tcTipologiaStaza);
			tcvTipologia.setLabelProvider(new CellLabelProvider(){
	
				@Override
				public void update(ViewerCell cell) {
					Stanzeimmobili siModel = (Stanzeimmobili)cell.getElement();
					cell.setText((siModel.getTipologiastanze() == null)
								 ? ""
								 : (siModel.getTipologiastanze().getDescrizione() == null)
								 	? ""
								 	: siModel.getTipologiastanze().getDescrizione());
					
				}
				
			});
			tcvTipologia.setEditingSupport(new EditingSupport(tvStanze){
	
				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
	
				@Override
				protected CellEditor getCellEditor(Object element) {
					fillTipologieStanze();
					ComboBoxCellEditor cbce = new ComboBoxCellEditor(tvStanze.getTable(),
																	 desTipologieStanze,
																	 SWT.READ_ONLY|SWT.DROP_DOWN);												
					return cbce;
				}
	
				@Override
				protected Object getValue(Object element) {
					int index = -1; 
					Stanzeimmobili siModel = (Stanzeimmobili)element;
					if (siModel.getTipologiastanze() != null){
						
						index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getTipologieStanze(), 
														 siModel.getTipologiastanze(),
											             comparatorTipologieStanze);
					}
					return index;
				}
	
				@Override
				protected void setValue(Object element, Object value) {
					if (((Integer)value).intValue() > -1){
						Stanzeimmobili siModel = (Stanzeimmobili)element;
						siModel.setTipologiastanze(MobiliaDatiBaseCache.getInstance().getTipologieStanze().get((Integer)value));
						tvStanze.refresh();
					}
				}
				
			});
				
			TableViewerColumn tcvMq = new TableViewerColumn(tvStanze,tcmq);
			
			tceMq = new TextCellEditor(tvStanze.getTable());
			
			tcvMq.setLabelProvider(new CellLabelProvider(){
	
				@Override
				public void update(ViewerCell cell) {
					
					cell.setText(
							(((Stanzeimmobili)cell.getElement()).getMq()==0)
							? "0"
							: String.valueOf(((Stanzeimmobili)cell.getElement()).getMq())
								);
					
				}
				
			});
			
			tcvMq.setEditingSupport(new EditingSupport(tvStanze){
	
				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
	
				@Override
				protected CellEditor getCellEditor(Object element) {
					return tceMq;
				}
	
				@Override
				protected Object getValue(Object element) {
					return (((Stanzeimmobili)element).getMq() == 0)
		   			   	   ? "0"
				   		   : String.valueOf(((Stanzeimmobili)element).getMq());
				}
	
				@Override
				protected void setValue(Object element, Object value) {
					Integer mq = null;
					try {
						mq = Integer.valueOf((String)value);
					} catch (NumberFormatException e) {					
						mq = 0;
					}
					((Stanzeimmobili)element).setMq(mq);
					tvStanze.refresh();
				}
				
			});
			
			tvStanze.setInput(new Object());
			tvStanze.refresh();
		
		}
		
	};	

	private void bindDatiCatastali(DataBindingContext bindingContext){
		if (immobile != null){
			tvDatiCatastali.setContentProvider(new IStructuredContentProvider(){
	
				@Override
				public Object[] getElements(Object inputElement) {
					return (immobile.getDaticatastalis()==null)
					       ? new ArrayList().toArray()
					       : immobile.getDaticatastalis().toArray();
				}
	
				@Override
				public void dispose() {
				}
	
				@Override
				public void inputChanged(Viewer viewer, Object oldInput,
						Object newInput) {
				}
				
			});
			
			tvDatiCatastali.setLabelProvider(new ITableLabelProvider(){
	
				@Override
				public Image getColumnImage(Object element, int columnIndex) {
					return null;
				}
	
				@Override
				public String getColumnText(Object element, int columnIndex) {
					Daticatastali dcVO = (Daticatastali)element;
					switch (columnIndex){
						case 0: return dcVO.getFoglio();					        
						case 1: return dcVO.getParticella();
						case 2: return dcVO.getSubalterno();
						case 3: return String.valueOf(dcVO.getRendita());
						case 4: return String.valueOf(dcVO.getRedditodomenicale());
						case 5: return String.valueOf(dcVO.getRedditoagrario());
						case 6: return String.valueOf(dcVO.getDimensione());
						
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
			
			TableViewerColumn tvcFoglio = new TableViewerColumn(tvDatiCatastali,tcfoglio);
			tvcFoglio.setLabelProvider(new CellLabelProvider(){
	
				@Override
				public void update(ViewerCell cell) {
					Daticatastali dcVO = (Daticatastali)cell.getElement();
					cell.setText(dcVO.getFoglio());					
				}
				
			});
			tvcFoglio.setEditingSupport(new EditingSupport(tvDatiCatastali){
	
				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
	
				@Override
				protected CellEditor getCellEditor(Object element) {				
					return new TextCellEditor(tvDatiCatastali.getTable());
				}
	
				@Override
				protected Object getValue(Object element) {					
					Daticatastali dcVO = (Daticatastali)element;
					return dcVO.getFoglio();
				}
	
				@Override
				protected void setValue(Object element, Object value) {
					Daticatastali dcVO = (Daticatastali)element;
					dcVO.setFoglio((String)value);
					tvDatiCatastali.refresh();
					
				}
				
			});

			TableViewerColumn tvcParticella = new TableViewerColumn(tvDatiCatastali,tcparticella);
			tvcParticella.setLabelProvider(new CellLabelProvider(){
	
				@Override
				public void update(ViewerCell cell) {
					Daticatastali dcVO = (Daticatastali)cell.getElement();
					cell.setText(dcVO.getParticella());					
				}
				
			});
			tvcParticella.setEditingSupport(new EditingSupport(tvDatiCatastali){
	
				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
	
				@Override
				protected CellEditor getCellEditor(Object element) {				
					return new TextCellEditor(tvDatiCatastali.getTable());
				}
	
				@Override
				protected Object getValue(Object element) {					
					Daticatastali dcVO = (Daticatastali)element;
					return dcVO.getParticella();
				}
	
				@Override
				protected void setValue(Object element, Object value) {
					Daticatastali dcVO = (Daticatastali)element;
					dcVO.setParticella((String)value);
					tvDatiCatastali.refresh();
					
				}
				
			});

			TableViewerColumn tvcSubalterno = new TableViewerColumn(tvDatiCatastali,tcsubalterno);
			tvcSubalterno.setLabelProvider(new CellLabelProvider(){
	
				@Override
				public void update(ViewerCell cell) {
					Daticatastali dcVO = (Daticatastali)cell.getElement();
					cell.setText(dcVO.getSubalterno());					
				}
				
			});
			tvcSubalterno.setEditingSupport(new EditingSupport(tvDatiCatastali){
	
				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
	
				@Override
				protected CellEditor getCellEditor(Object element) {				
					return new TextCellEditor(tvDatiCatastali.getTable());
				}
	
				@Override
				protected Object getValue(Object element) {					
					Daticatastali dcVO = (Daticatastali)element;
					return dcVO.getSubalterno();
				}
	
				@Override
				protected void setValue(Object element, Object value) {
					Daticatastali dcVO = (Daticatastali)element;
					dcVO.setSubalterno((String)value);
					tvDatiCatastali.refresh();
					
				}
				
			});

			TableViewerColumn tvcCategoria = new TableViewerColumn(tvDatiCatastali,tccategoria);
			tvcCategoria.setLabelProvider(new CellLabelProvider(){
	
				@Override
				public void update(ViewerCell cell) {
					Daticatastali dcVO = (Daticatastali)cell.getElement();
					cell.setText(dcVO.getCategoria());					
				}
				
			});
			tvcCategoria.setEditingSupport(new EditingSupport(tvDatiCatastali){
	
				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
	
				@Override
				protected CellEditor getCellEditor(Object element) {				
					return new TextCellEditor(tvDatiCatastali.getTable());
				}
	
				@Override
				protected Object getValue(Object element) {					
					Daticatastali dcVO = (Daticatastali)element;
					return dcVO.getCategoria();
				}
	
				@Override
				protected void setValue(Object element, Object value) {
					Daticatastali dcVO = (Daticatastali)element;
					dcVO.setCategoria((String)value);
					tvDatiCatastali.refresh();
					
				}
				
			});
			
			
			TableViewerColumn tvcRendita = new TableViewerColumn(tvDatiCatastali,tcrendita);	
			
			tvcRendita.setLabelProvider(new CellLabelProvider(){
	
				@Override
				public void update(ViewerCell cell) {
					
					cell.setText(
							(((Daticatastali)cell.getElement()).getRendita() == 0.0)
							? "0.0"
							: String.valueOf(((Daticatastali)cell.getElement()).getRendita())
								);
					
				}
				
			});
			
			tvcRendita.setEditingSupport(new EditingSupport(tvDatiCatastali){
	
				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
	
				@Override
				protected CellEditor getCellEditor(Object element) {
					return new TextCellEditor(tvDatiCatastali.getTable());
				}
	
				@Override
				protected Object getValue(Object element) {
					return (((Daticatastali)element).getRendita() == 0.0)
							? "0.0"
							: String.valueOf(((Daticatastali)element).getRendita());
				}
	
				@Override
				protected void setValue(Object element, Object value) {
					Double rendita = null;
					try {
						rendita = Double.valueOf((String)value);
					} catch (NumberFormatException e) {					
						rendita = 0.0;
					}
					((Daticatastali)element).setRendita(rendita);
					tvDatiCatastali.refresh();
				}
				
			});
			
			TableViewerColumn tvcRedditoDomenicale = new TableViewerColumn(tvDatiCatastali,tcredditoDomenicale);	
			
			tvcRedditoDomenicale.setLabelProvider(new CellLabelProvider(){
	
				@Override
				public void update(ViewerCell cell) {
					
					cell.setText(
							(((Daticatastali)cell.getElement()).getRedditodomenicale() == 0.0)
							? "0.0"
							: String.valueOf(((Daticatastali)cell.getElement()).getRedditodomenicale())
								);
					
				}
				
			});
			
			tvcRedditoDomenicale.setEditingSupport(new EditingSupport(tvDatiCatastali){
	
				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
	
				@Override
				protected CellEditor getCellEditor(Object element) {
					return new TextCellEditor(tvDatiCatastali.getTable());
				}
	
				@Override
				protected Object getValue(Object element) {
					return (((Daticatastali)element).getRedditodomenicale() == 0.0)
							? "0.0"
							: String.valueOf(((Daticatastali)element).getRedditodomenicale());
				}
	
				@Override
				protected void setValue(Object element, Object value) {
					Double reddito = null;
					try {
						reddito = Double.valueOf((String)value);
					} catch (NumberFormatException e) {					
						reddito = 0.0;
					}
					((Daticatastali)element).setRedditodomenicale(reddito);
					tvDatiCatastali.refresh();
				}
				
			});
			
			TableViewerColumn tvcRedditoAgricolo = new TableViewerColumn(tvDatiCatastali,tcredditoAgricolo);	
			
			tvcRedditoAgricolo.setLabelProvider(new CellLabelProvider(){
	
				@Override
				public void update(ViewerCell cell) {
					
					cell.setText(
							(((Daticatastali)cell.getElement()).getRedditoagrario()==0.0)
							? "0"
							: String.valueOf(((Daticatastali)cell.getElement()).getRedditoagrario())
								);
					
				}
				
			});
			
			tvcRedditoAgricolo.setEditingSupport(new EditingSupport(tvDatiCatastali){
	
				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
	
				@Override
				protected CellEditor getCellEditor(Object element) {
					return new TextCellEditor(tvDatiCatastali.getTable());
				}
	
				@Override
				protected Object getValue(Object element) {
					return (((Daticatastali)element).getRedditoagrario()==0.0)
							? "0"
							: String.valueOf(((Daticatastali)element).getRedditoagrario());
				}
	
				@Override
				protected void setValue(Object element, Object value) {
					Double rendita = null;
					try {
						rendita = Double.valueOf((String)value);
					} catch (NumberFormatException e) {					
						rendita = 0.0;
					}
					((Daticatastali)element).setRedditoagrario(rendita);
					tvDatiCatastali.refresh();
				}
				
			});
			
			TableViewerColumn tvcDimensione = new TableViewerColumn(tvDatiCatastali,tcdimensione);	
			
			tvcDimensione.setLabelProvider(new CellLabelProvider(){
	
				@Override
				public void update(ViewerCell cell) {
					
					cell.setText(
							(((Daticatastali)cell.getElement()).getDimensione()==0.0)
							? "0"
							: String.valueOf(((Daticatastali)cell.getElement()).getDimensione())
								);
					
				}
				
			});
			
			tvcDimensione.setEditingSupport(new EditingSupport(tvDatiCatastali){
	
				@Override
				protected boolean canEdit(Object element) {
					return true;
				}
	
				@Override
				protected CellEditor getCellEditor(Object element) {
					return new TextCellEditor(tvDatiCatastali.getTable());
				}
	
				@Override
				protected Object getValue(Object element) {
					return (((Daticatastali)element).getDimensione()==0.0)
							? "0"
							: String.valueOf(((Daticatastali)element).getDimensione());
				}
	
				@Override
				protected void setValue(Object element, Object value) {
					Double rendita = null;
					try {
						rendita = Double.valueOf((String)value);
					} catch (NumberFormatException e) {					
						rendita = 0.0;
					}
					((Daticatastali)element).setDimensione(rendita);
					tvDatiCatastali.refresh();
				}
				
			});
			
			tvDatiCatastali.setInput(new Object());
			tvDatiCatastali.refresh();
		
		}
		
	};	

	private void allegatiImmobile(){
		
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
		
		GridData gdExpVHTable = new GridData();
		gdExpVHTable.grabExcessHorizontalSpace = true;
		gdExpVHTable.grabExcessVerticalSpace = true;
		gdExpVHTable.horizontalAlignment = SWT.FILL;
		gdExpVHTable.verticalAlignment = SWT.FILL;	
		gdExpVHTable.minimumHeight = 80;
		
		section.setLayout(new GridLayout());
		section.setLayoutData(gdExpVH);
		section.setText("Allegati immobile");
		section.setDescription("documenti allegati all'immobile");
		
		Composite contenitore = ft.createComposite(section,SWT.NONE);
		contenitore.setLayout(new GridLayout());
		contenitore.setLayoutData(gdExpVH);
		ft.paintBordersFor(contenitore);
		
		Composite toolbar = ft.createComposite(contenitore,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ihNewAllegato = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNewAllegato.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNewAllegato.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNewAllegato.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				Allegatiimmobili aiVO = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Allegatiimmobili.class);
				
				immobile.addToAllegatiimmobilis(aiVO);
				tvAllegati.setInput(immobile.getAllegatiimmobilis());
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
						Allegatiimmobili aiVO = (Allegatiimmobili)it.next();
						immobile.removeFromAllegatiimmobilis(aiVO);
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

		ihVediCartellaAllegati = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihVediCartellaAllegati.setImage(Activator.getImageDescriptor("/icons/folder.png").createImage());
		ihVediCartellaAllegati.setHoverImage(Activator.getImageDescriptor("/icons/folder_over.png").createImage());
		ihVediCartellaAllegati.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				if (getImmobile().getCodImmobile() != 0){
					
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
												       				
	                File f = new File(pathRepositoryAllegati + File.separator + getImmobile().getCodImmobile());
					if (!f.exists()){
						f.mkdirs();
					}
					try {
						Desktop.getDesktop().open(f);
					} catch (IOException e1) {
						MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
												"Apertura cartella", 
												"Impossibile aprire la cartella degli allegati");
					} 
				}else{
					MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
											"Apertura cartella", 
											"Eseguire il salvataggio dell\'immobile per accedere a questa funzionalit�");
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
				
				pathRepositoryAllegati += File.separator + "immobili";
				
				Allegatiimmobili aiVO = (Allegatiimmobili)((StructuredSelection)tvAllegati.getSelection()).getFirstElement();
				
				if ((aiVO.getFromPath() == null) ||
					(aiVO.getFromPath().equalsIgnoreCase(""))){	
					if ((aiVO.getNome() != null) &&
							(!aiVO.getNome().equalsIgnoreCase(""))){

						Program.launch(pathRepositoryAllegati +
								       File.separator +
								       aiVO.getCodImmobile() +
								       File.separator +
								       aiVO.getNome());
					}
				}else if((aiVO.getFromPath() != null) &&
						(!aiVO.getFromPath().equalsIgnoreCase(""))){
					Program.launch(aiVO.getFromPath());					
				}
			}
		});
		
		
		tcDescrizione = new TableColumn(tvAllegati.getTable(),SWT.CENTER,0);
		tcDescrizione.setWidth(100);
		tcDescrizione.setText("Descrizione");
				
		tcPathFrom = new TableColumn(tvAllegati.getTable(),SWT.CENTER,1);
		tcPathFrom.setWidth(200);
		tcPathFrom.setText("Documento");
		
		section.setClient(contenitore);
	}
	
	private void bindAllegatiImmobile(DataBindingContext bindingContext){
		if (immobile != null){
			tvAllegati.setContentProvider(new IStructuredContentProvider(){
	
				@Override
				public Object[] getElements(Object inputElement) {
					return (immobile.getAllegatiimmobilis()==null)
					       ? new ArrayList<Allegatiimmobili>().toArray()
					       : immobile.getAllegatiimmobilis().toArray();
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
					Allegatiimmobili aiVO = (Allegatiimmobili)element;
					switch (columnIndex){
						case 0: return (aiVO.getCommento() == null)
									   ? ""
									   : aiVO.getCommento();
						
						case 1: return (aiVO.getNome() == null)
						   			   ? ""
						   			   : aiVO.getNome();
						
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
			
			TableViewerColumn tvcDescrizione = new TableViewerColumn(tvAllegati,tcDescrizione);
			tvcDescrizione.setLabelProvider(new CellLabelProvider(){
	
				@Override
				public void update(ViewerCell cell) {
					if (((Allegatiimmobili)cell.getElement()).getCommento() != null){
						cell.setText(((Allegatiimmobili)cell.getElement()).getCommento());
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
					if (((Allegatiimmobili)element).getCommento() != null){
						return ((Allegatiimmobili)element).getCommento();
					}else{
						return "";
					}
				}
	
				@Override
				protected void setValue(Object element, Object value) {
					((Allegatiimmobili)element).setCommento(String.valueOf(value));
					tvAllegati.refresh();
				}
				
			});
			
			TableViewerColumn tvcPathFrom = new TableViewerColumn(tvAllegati,tcPathFrom);
			tvcPathFrom.setLabelProvider(new CellLabelProvider(){
	
				@Override
				public void update(ViewerCell cell) {
					
					if (((Allegatiimmobili)cell.getElement()).getFromPath() == null){
						cell.setText(((Allegatiimmobili)cell.getElement()).getNome());
					}else{
						cell.setText(((Allegatiimmobili)cell.getElement()).getFromPath());
					}
				}
				
			});
			tvcPathFrom.setEditingSupport(new EditingSupport(tvAllegati){
	
				@Override
				protected boolean canEdit(Object element) {
					if (((Allegatiimmobili)element).getNome().equalsIgnoreCase("")){
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
					if (((Allegatiimmobili)element).getFromPath() != null){
						return ((Allegatiimmobili)element).getFromPath();
					}else{
						return ((Allegatiimmobili)element).getNome();
					}
				}
	
				@Override
				protected void setValue(Object element, Object value) {
					((Allegatiimmobili)element).setFromPath(String.valueOf(value));
					((Allegatiimmobili)element).setNome(((Allegatiimmobili)element).getFromPath()
																					   .substring(((Allegatiimmobili)element).getFromPath()
																							   								   .lastIndexOf(File.separator)+1)
													      );
					tvAllegati.refresh();
				}
				
			});
			
			tvAllegati.setInput(new Object());
			tvAllegati.refresh();
			
		}
	};	
	
	private void agenzia(){
		
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

		GridData gdExpVHCS3 = new GridData();
		gdExpVHCS3.grabExcessHorizontalSpace = true;
		gdExpVHCS3.grabExcessVerticalSpace = true;
		gdExpVHCS3.horizontalAlignment = SWT.FILL;
		gdExpVHCS3.verticalAlignment = SWT.FILL;
		gdExpVHCS3.horizontalSpan = 4;
		
		GridData gdExpH = new GridData();
		gdExpH.grabExcessHorizontalSpace = true;		
		gdExpH.horizontalAlignment = SWT.FILL;
		
		section.setLayout(new GridLayout());
		section.setLayoutData(gdExpVH);
		section.setText("Dati agenzia");
		section.setDescription("dati agenzia");

		Composite cdatiagenzia = ft.createComposite(section);
		cdatiagenzia.setLayoutData(gdExpVH);
		GridLayout gl = new GridLayout();
		gl.numColumns = 4;
		cdatiagenzia.setLayout(gl);
		ft.paintBordersFor(cdatiagenzia);
		
		Label lDataInserimento = ft.createLabel(cdatiagenzia, "Data inserimento");
		Label lAgenteInseritore = ft.createLabel(cdatiagenzia, "Agente inseritore",SWT.FLAT);
		Label lVisone = ft.createLabel(cdatiagenzia, "Visionabile", SWT.FLAT);		
		Label laffitto = ft.createLabel(cdatiagenzia, "Affittabile", SWT.FLAT);
		
		Composite cDataInserimento = new Composite(cdatiagenzia,SWT.NONE);
		GridLayout glDataInserimento = new GridLayout();
		glDataInserimento.numColumns = 2;
		cDataInserimento.setLayout(glDataInserimento);
		dcdatainserimento = ft.createText(cDataInserimento,"",SWT.DOUBLE_BUFFERED);
		dcdatainserimento.setEditable(false);
		GridData dfdcdata = new GridData();
		dfdcdata.minimumWidth = 200;
		dfdcdata.widthHint = 200;
		
		dcdatainserimento.setLayoutData(dfdcdata);
		bOpenDialog = new Button(cDataInserimento,SWT.PUSH|SWT.FLAT);
		bOpenDialog.setImage(Activator.getImageDescriptor("/icons/calendario.png").createImage());
		bOpenDialog.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {

                final SWTCalendarDialog cal = new SWTCalendarDialog(getViewSite().getShell().getDisplay());

                cal.addDateChangedListener(new SWTCalendarListener() {

                    public void dateChanged(SWTCalendarEvent calendarEvent) {

                    	dcdatainserimento.setText(formatter.format(calendarEvent.getCalendar().getTime()));
                    	immobile.setDatainserimento(calendarEvent.getCalendar().getTime().toInstant()
          				      									 .atZone(ZoneId.systemDefault()).toLocalDate());
                    }

                });



                if (dcdatainserimento.getText() != null && dcdatainserimento.getText().length() > 0) {

                    try {

                        Date d = formatter.parse(dcdatainserimento.getText());

                        cal.setDate(d);

                    } catch (ParseException pe) {



                    }

                }

                cal.open();



            }

        });
//		dcdatainserimento.setLayoutData(gdExpH);
		cvagenteinseritore = new ComboViewer(cdatiagenzia,SWT.DROP_DOWN | SWT.READ_ONLY | SWT.DOUBLE_BUFFERED);
		cvagenteinseritore.getCombo().setLayoutData(gdExpH);
		visibile = new Button(cdatiagenzia,SWT.CHECK);
		affitto = new Button(cdatiagenzia,SWT.CHECK);
		
		Label lVarie = ft.createLabel(cdatiagenzia, "Varie", SWT.FLAT);
		varie = ft.createText(cdatiagenzia,"",SWT.MULTI|SWT.V_SCROLL|SWT.WRAP|SWT.H_SCROLL);
		GridData gd = new GridData();
		gd.minimumHeight = 50;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalSpan = 4;
		
		varie.setLayoutData(gd);
		
		section.setClient(cdatiagenzia);
	}
	
	private void bindAgenzia(DataBindingContext bindingContext){
		if (immobile != null){
			if (immobile.getDatainserimento() != null){				
				dcdatainserimento.setText(formatter.format(Date.from(immobile.getDatainserimento().atStartOfDay(ZoneId.systemDefault()).toInstant())));
			}
			
//			bindingContext.bindValue(SWTObservables.observeText(
//					varie,SWT.Modify), 
//					PojoObservables.observeValue(immobile, "varie"),
//					null, 
//					null);
	
			affitto.addMouseListener(new MouseListener(){
	
				@Override
				public void mouseDoubleClick(MouseEvent e) {
			
					
				}
	
				@Override
				public void mouseDown(MouseEvent e) {
			
					
				}
	
				@Override
				public void mouseUp(MouseEvent e) {
					immobile.setAffitto(affitto.getSelection());
				}
				
			});
			
			affitto.setSelection(immobile.isAffitto());
			
			visibile.addMouseListener(new MouseListener(){
	
				@Override
				public void mouseDoubleClick(MouseEvent e) {
			
					
				}
	
				@Override
				public void mouseDown(MouseEvent e) {
			
					
				}
	
				@Override
				public void mouseUp(MouseEvent e) {
					immobile.setVisione(visibile.getSelection());
				}
				
			});
			
			visibile.setSelection(immobile.isVisione());
	
			
			cvagenteinseritore.setContentProvider(new IStructuredContentProvider(){
	
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
			
			cvagenteinseritore.setLabelProvider(new LabelProvider(){
	
				@Override
				public String getText(Object element) {
					return ((Agenti)element).getCognome() + " " + ((Agenti)element).getNome();
				}
				
			});
			
			cvagenteinseritore.addSelectionChangedListener(new ISelectionChangedListener(){
	
				@Override
				public void selectionChanged(SelectionChangedEvent event) {					
					immobile.setAgenti(immobile.getObjectContext().localObject((Agenti)((StructuredSelection)event.getSelection()).getFirstElement()));				
				}
				
			});
							
			cvagenteinseritore.setInput(new Object());
			
			if (immobile.getAgenti() != null){
				int index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getAgenti(), immobile.getAgenti(), comparatorAgenti);
				Object[] sel = new Object[1];
				sel[0] = MobiliaDatiBaseCache.getInstance().getAgenti().get(index);
				StructuredSelection ss = new StructuredSelection(sel);
				cvagenteinseritore.setSelection(ss);
			}

		}


	}
		
	@Override
	public void setFocus() {
		DataBindingContext bindingContext = new DataBindingContext();
//		bindAnagrafica(bindingContext);
		bindDatiImmobile(bindingContext);
		bindStanzeImmobile(bindingContext);
		bindAgenzia(bindingContext);
		bindDatiCatastali(bindingContext);
		updateViews();
	}
	
	private void updateViews(){

		if (immobile != null){
			
			IViewPart ivp = PlatformUI
					 				 .getWorkbench()
					 				 .getActiveWorkbenchWindow()
					 				 .getActivePage()
					 				 .findView(ColloquiView.ID);
			
			if (ivp != null){
				((ColloquiView)ivp).setImmobile(immobile);		
				((ColloquiView)ivp).setCompareView(isInCompareMode);
			}

			ivp = PlatformUI
			   			   .getWorkbench()
			   			   .getActiveWorkbenchWindow()
			   			   .getActivePage()
			   			   .findView(RecapitiView.ID);
			
			if (ivp != null){
				ArrayList<Anagrafiche> anagrafiche = new ArrayList<Anagrafiche>();
				if (immobile.getImmobilipropietaris() == null || immobile.getImmobilipropietaris().size() > 0) {
					for (Immobilipropietari ip : immobile.getImmobilipropietaris()) {
						anagrafiche.add(ip.getAnagrafiche());
					}
				}				  				  
				((RecapitiView)ivp).setAnagrafiche(anagrafiche);
				((RecapitiView)ivp).setCompareView(isInCompareMode);
			}

			ivp = PlatformUI
		   			   	   .getWorkbench()
		   			   	   .getActiveWorkbenchWindow()
		   			   	   .getActivePage()
		   			   	   .findView(AnagrafichePropietarieView.ID);
		
			if (ivp != null){
				((AnagrafichePropietarieView)ivp).setAnagrafica(immobile);
				((AnagrafichePropietarieView)ivp).setCompareView(isInCompareMode);
			}			
			
			ivp = PlatformUI
			   			   .getWorkbench()
			   			   .getActiveWorkbenchWindow()
			   			   .getActivePage()
			   			   .findView(ImmaginiImmobiliView.ID);

			if (ivp != null){
				((ImmaginiImmobiliView)ivp).setImmobile(immobile);
				((ImmaginiImmobiliView)ivp).setCompareView(isInCompareMode);
				
			}
						
			ivp = PlatformUI
			   			   .getWorkbench()
			   			   .getActiveWorkbenchWindow()
			   			   .getActivePage()
			   			   .findView(ListaAffittiView.ID);

			if (ivp != null){
				((ListaAffittiView)ivp).setImmobile(immobile);
			}

			ivp = PlatformUI
						   .getWorkbench()
						   .getActiveWorkbenchWindow()
						   .getActivePage()
						   .findView(ImmobiliPropietaView.ID);

			if (ivp != null){
				((ImmobiliPropietaView)ivp).setAnagrafica(null);
			}
			
			IViewPart eavv = PlatformUI
	   				  .getWorkbench()
	   				  .getActiveWorkbenchWindow()
	   				  .getActivePage()
	   				  .findView(EAVView.ID);

			if (eavv != null){
				EntityDAO eDAO = new EntityDAO();
				EntityModel e = eDAO.getEntityByClassName(ImmobiliVO.class.getName());
				
				if ((e != null) && (e.getAttributes()!= null)){
					((EAVView)eavv).setAttributes(e.getAttributes(), immobile.getCodImmobile());
					((EAVView)eavv).setCompareView(isInCompareMode);
				}
				
			}

			IViewPart mapv = PlatformUI
									  .getWorkbench()
									  .getActiveWorkbenchWindow()
									  .getActivePage()
									  .findView(MapView.ID);

			if (mapv != null){

				((MapView)mapv).searchGeoData(immobile.getIndirizzo(), immobile.getNcivico(), immobile.getCitta());

			}

			
			WinkhouseUtils.getInstance().setLastCodImmobileSelected(immobile.getCodImmobile());
			WinkhouseUtils.getInstance().setLastEntityTypeFocused(ImmobiliModel.class.getName());
						
			ivp = PlatformUI
		   			   .getWorkbench()
		   			   .getActiveWorkbenchWindow()
		   			   .getActivePage()
		   			   .findView(AbbinamentiView.ID);
		
			if (ivp != null){				
				((AbbinamentiView)ivp).setImmobile(immobile);
				((AbbinamentiView)ivp).setCompareView(isInCompareMode);

			}
			
		}
		
	}

	public Immobili getImmobile() {
		return this.immobile;
	}

	public void setImmobile(Immobili immobile) {
		isInCompareMode = false;
		DataBindingContext bindingContext = new DataBindingContext();
		this.immobile = immobile;
		if ((this.immobile.getCitta() == null) && 
			(this.immobile.getIndirizzo() == null)){
				setPartName("Nuovo Immobile");
		}else{
				setPartName(this.immobile.getCitta() + " - " + this.immobile.getIndirizzo());
		}
		
		bindDatiImmobile(bindingContext);
//		bindAnagrafica(bindingContext);
		bindPosizioneImmobile(bindingContext);
		bindStanzeImmobile(bindingContext);
		bindAgenzia(bindingContext);
		bindAllegatiImmobile(bindingContext);
		bindDatiCatastali(bindingContext);
		updateViews();
	}

	public void fillTipologieStanze(){		
		MobiliaDatiBaseCache.getInstance().getTipologieStanze();
		Collections.sort(MobiliaDatiBaseCache.getInstance().getTipologieStanze(), comparatorTipologieStanze);
		desTipologieStanze = new String[MobiliaDatiBaseCache.getInstance().getTipologieStanze().size()];
		Iterator<Tipologiastanze> it = MobiliaDatiBaseCache.getInstance().getTipologieStanze().iterator();
		int count = 0;
		while(it.hasNext()){
			desTipologieStanze[count] = it.next().getDescrizione();
			count++;
		}		
	}
	
	@Override
	public String getPreferredPerspectiveId() {
		return ImmobiliPerspective.ID;
	}

	@Override
	public Object getEditableValue() {

		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor ipd = new IPropertyDescriptor() {
			
			@Override
			public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {
		
				return false;
			}
			
			@Override
			public ILabelProvider getLabelProvider() {
		
				return null;
			}
			
			@Override
			public Object getId() {
		
				return null;
			}
			
			@Override
			public Object getHelpContextIds() {
		
				return null;
			}
			
			@Override
			public String[] getFilterFlags() {
		
				return null;
			}
			
			@Override
			public String getDisplayName() {
		
				return null;
			}
			
			@Override
			public String getDescription() {
		
				return null;
			}
			
			@Override
			public String getCategory() {
		
				return null;
			}
			
			@Override
			public CellEditor createPropertyEditor(Composite parent) {
		
				return null;
			}
		};
		IPropertyDescriptor[] ipdes = new IPropertyDescriptor[1];
		ipdes[0] = ipd;
		
		return ipdes;
	}

	@Override
	public Object getPropertyValue(Object id) {

		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {

		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {

		
	}

	@Override
	public void setPropertyValue(Object id, Object value) {

		
	}

	public void setCompareView(boolean enabled){
		
		refreshDettaglioImmobile.setEnabled(!enabled);
		salvaImmobile.setEnabled(!enabled);
		cancellaImmobile.setEnabled(!enabled);
//		openPopUpAnagrafiche.setEnabled(enabled);
		cambiaStoricoAction.setEnabled(!enabled);
		stampaImmobiliAction.setEnabled(!enabled);
		
		triferimento.setEditable(!enabled);
		tcitta.setEditable(!enabled);		
		tprovincia.setEditable(!enabled);
		tcap.setEditable(!enabled);
		tzona.setEditable(!enabled);
		tindirizzo.setEditable(!enabled);
		
		cvtipologia.getCombo().setEnabled(!enabled);
		cvstato.getCombo().setEnabled(!enabled);
		cvriscaldamenti.getCombo().setEnabled(!enabled);
		cvclasseenergetica.getCombo().setEnabled(!enabled);
		
		tannoCostruzione.setEditable(!enabled);
		
		tmq.setEditable(!enabled);		
		tprezzo.setEditable(!enabled);
		tmutuo.setEditable(!enabled);
		tspese.setEditable(!enabled);		
		tliberoda.setEditable(!enabled);
		tdescrizione.setEditable(!enabled);

		tvStanze.getTable().setEnabled(!enabled);
		tvDatiCatastali.getTable().setEnabled(!enabled);
		tvAllegati.getTable().setEnabled(!enabled);
		
//		tcognome.setEditable(enabled);
//		tnome.setEditable(enabled);
//		tcittaangrafica.setEditable(enabled);		
//		tprovinciaanagrafica.setEditable(enabled);
//		tcapanagrafica.setEditable(enabled);
//		tindirizzoanagrafica.setEditable(enabled);
//		
//		cvclassiclienti.getCombo().setEnabled(enabled);
//		cvagenteinseritoreanagrafica.getCombo().setEnabled(enabled);
		
//		tcommento.setEditable(enabled);
		
		dcdatainserimento.setEditable(!enabled);
		bOpenDialog.setEnabled(!enabled);
		bOpenDialog2.setEnabled(!enabled);
		cvagenteinseritore.getCombo().setEnabled(!enabled);
		
		visibile.setEnabled(!enabled);
		affitto.setEnabled(!enabled);
		varie.setEditable(!enabled);
		
		ihNewStanza.setEnabled(!enabled);		
		ihCancellaStanza.setEnabled(!enabled);		

		ihNewDatiCatastali.setEnabled(!enabled);
		ihCancellaDatiCatastali.setEnabled(!enabled);
		
		ihNewAllegato.setEnabled(!enabled);		
		ihCancellaAllegato.setEnabled(!enabled);		

		isInCompareMode = enabled;
		for (int i = 0; i < f.getToolBarManager().getItems().length; i++) {
			f.getToolBarManager().getItems()[i].setVisible(!enabled);
			f.getToolBarManager().getItems()[i].update();
		} 
		f.getToolBarManager().update(true);
		f.updateToolBar();

	}
	
	public String getComune(){
		return immobile.getCitta();
	}
	
	public void setComune(Comuni cVO){
		tcitta.setText(cVO.getComune());
		tcap.setText(cVO.getCap());
		tprovincia.setText(cVO.getProvincia());
	}
	
}
