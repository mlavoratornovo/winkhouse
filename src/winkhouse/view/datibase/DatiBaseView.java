package winkhouse.view.datibase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.cayenne.ObjectContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.dao.AgentiDAO;
import winkhouse.dao.ClassiClientiDAO;
import winkhouse.dao.ClassiEnergeticheDAO;
import winkhouse.dao.RiscaldamentiDAO;
import winkhouse.dao.StatoConservativoDAO;
import winkhouse.dao.TipiAppuntamentiDAO;
import winkhouse.dao.TipologiaContattiDAO;
import winkhouse.dao.TipologiaStanzeDAO;
import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.helper.AgentiHelper;
import winkhouse.helper.ClassiClientiHelper;
import winkhouse.helper.ClassiEnergeticheHelper;
import winkhouse.helper.ContattiHelper;
import winkhouse.helper.RiscaldamentiHelper;
import winkhouse.helper.StatoConservativoHelper;
import winkhouse.helper.TipiAppuntamentiHelper;
import winkhouse.helper.TipologiaContattiHelper;
import winkhouse.helper.TipologiaStanzeHelper;
import winkhouse.helper.TipologieImmobiliHelper;
import winkhouse.orm.Agenti;
import winkhouse.orm.Classicliente;
import winkhouse.orm.Classienergetiche;
import winkhouse.orm.Contatti;
import winkhouse.orm.Riscaldamenti;
import winkhouse.orm.Statoconservativo;
import winkhouse.orm.Tipiappuntamenti;
import winkhouse.orm.Tipologiastanze;
import winkhouse.orm.Tipologiecontatti;
import winkhouse.orm.Tipologieimmobili;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.util.WinkhouseUtils;


public class DatiBaseView extends ViewPart {

	public final static String ID = "winkhouse.datibaseview";
	
	private ScrolledForm form = null;
	private FormToolkit tool = null;
	//--- riscaldamenti
	private TableViewer tvRiscaldamenti = null;
	private ArrayList<Riscaldamenti> riscaldamenti = null;
	//--- causecolloqui
//	private TableViewer tvCausaliColloqui = null;
//	private ArrayList<CauseColloquiVO> causecolloqui = null;
	//--- causecolloqui
	private TableViewer tvClassiClienti = null;
	private ArrayList<Classicliente> classiclienti = null;
	//--- statoconservativo
	private TableViewer tvStatoConservativo = null;
	private ArrayList<Statoconservativo> statoconservativo = null;
	//--- tipologiastanze
	private TableViewer tvTipologiaStanze = null;
	private ArrayList<Tipologiastanze> tipologiastanze = null;
	//--- tipologiecolloqui
//	private TableViewer tvTipologieColloqui = null;
//	private ArrayList<TipologieColloquiVO> tipologiecolloqui = null;
	//--- tipologieimmobili
	private TableViewer tvTipologieImmobili = null;
	private ArrayList<Tipologieimmobili> tipologieimmobili = null;
	//--- tipologiecontatti
	private TableViewer tvTipologieContatti = null;
	private ArrayList<Tipologiecontatti> tipologiecontatti = null;
	//--- agenti
	private TableViewer tvAgenti = null;
	private ArrayList<Agenti> agenti = null;
	//-- recapiti
	private TableViewer tvRecapiti = null;
	
	private TableViewer tvTipiAppuntamenti = null;
	private ArrayList<Tipiappuntamenti> tipiappuntamenti = null;

	private TableViewer tvClassiEnergetiche = null;
	private ArrayList<Classienergetiche> classienergetiche = null;
	
	private String[] desTipologieRecapiti = null;
	private TextCellEditor tceContatto = null;
	private TextCellEditor tceDescrizione = null;
	
	private final String DESCRIZIONE = "Descrizione";
	private final String ORDINE = "Ordine";
	
	private final String CAP = "Cap";
	private final String CITTA = "Citta";
	private final String PROVINCIA = "Provincia";
	private final String INDIRIZZO = "Indirizzo";
	private final String NOME = "Nome";
	private final String COGNOME = "Cognome";
	private final String USERNAME = "Username";
	private final String PASSWORD = "Password";
	
	private final static String COLOREGCAL = "Colore"; 
	
	private String[] descTableArray = {"Descrizione"};
	private GridData tabella = null;
	
	private ObjectContext oc = null;
	private ObjectContext ocAgenti = null;
	private ObjectContext ocContatti = null;
	
	public DatiBaseView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {
		oc = WinkhouseUtils.getInstance().getCayenneObjectContext();
		ocAgenti = WinkhouseUtils.getInstance().getNewCayenneObjectContext();
		ocContatti = WinkhouseUtils.getInstance().getNewCayenneObjectContext();
		
		tabella = new GridData();
		tabella.grabExcessHorizontalSpace = true;
		tabella.horizontalAlignment = SWT.FILL;
		tabella.heightHint = 200;

		tool = new FormToolkit(PlatformUI.getWorkbench().getDisplay());
		form = tool.createScrolledForm(parent);
		form.setText("Dati di base");
		form.getBody().setLayout(new GridLayout());
		form.setImage(Activator.getImageDescriptor("/icons/db.png").createImage());
//		form.setBackgroundImage(Activator.getImageDescriptor("/icons/Ilumi18.png").createImage());
		createTipologieContattiSection();
		createAgentiSection();
		createTipiAppuntamentiSection();
//		createCauseColloquiSection();
	//	createTipologieColloquiSection();
		createClassiClientiSection();
		createTipologieImmobiliSection();		
		createRiscaldamentiSection();
		createTipologieStanzeSection();
		createStatoConservativoSection();	
		createClassiEnergeticheSection();
		
	}
	
	private void createAgentiSection(){
				
		Section section = tool.createSection(
				   				form.getBody(), 
				   				Section.DESCRIPTION|Section.TITLE_BAR|
				   				Section.TWISTIE);

		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
			
		});
		
		fillTipologieRecapiti();

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
		section.setDescription("Agenti dell'agenzia immobiliare");
		
		Composite sectionClient = tool.createComposite(section);
		sectionClient.setLayoutData(gdSection);
		sectionClient.setLayout(new GridLayout());
		Composite toolbar = new Composite(sectionClient,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ImageHyperlink ihNew = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.setToolTipText("Nuovo agente");
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				Agenti aModel = ocAgenti.newObject(Agenti.class);
				aModel.initData();
				
				agenti.add(aModel);
				tvAgenti.setInput(agenti);
				tvAgenti.refresh();
				
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

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihConferma = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihConferma.setImage(Activator.getImageDescriptor("/icons/document-save.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/document-save_over.png").createImage());
		ihConferma.setToolTipText("Salva le modifiche");
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				AgentiHelper ah = new AgentiHelper();
				//if (ah.checkPws(agenti)){
					ah.updateDatiBase(agenti, ocAgenti);
					
					//agenti = null;
					tvAgenti.setInput(new Object());
					tvAgenti.refresh();
//				}else{
//					MessageDialog.openInformation(getSite().getShell(), 
//												  "Password duplicate", 
//												  "Sono presenti due o pi� coppie di username password uguali");
//				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihUndo = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihUndo.setImage(Activator.getImageDescriptor("/icons/adept_reinstall.png").createImage());
		ihUndo.setHoverImage(Activator.getImageDescriptor("/icons/adept_reinstall_hover.png").createImage());
		ihUndo.setToolTipText("Ricarica dall'archivio");
		ihUndo.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {				
				agenti = null;
				tvAgenti.setInput(new Object());
				tvAgenti.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.setToolTipText("Cancella l'elemento selezionato");
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				if (tvAgenti.getSelection() != null){
					int count = 0;
					boolean result = true;
					Iterator it = ((StructuredSelection)tvAgenti.getSelection()).iterator();
					AgentiHelper ah = new AgentiHelper();					
					while (it.hasNext()) {
						Agenti aVO = (Agenti)it.next();
						if (ah.deleteAgente(aVO, ocAgenti)){
							agenti.remove(aVO);							
						}						
						
					}
					agenti = null;
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
//		tvAgenti.setCellModifier(new ICellModifier() {
//			
//			@Override
//			public void modify(Object element, String property, Object value) {
//				// TODO Auto-generated method stub				
//			}
//			
//			@Override
//			public Object getValue(Object element, String property) {
//				switch (property) {
//				case "nome":
//					return ((Agenti)element).getNome();					
//				default:
//					break;
//				}
//				return null;
//			}
//			
//			@Override
//			public boolean canModify(Object element, String property) {
//				return true;
//			}
//		});
		TableViewerEditor.create(tvAgenti,
								 new ColumnViewerEditorActivationStrategy(tvAgenti),
								 ColumnViewerEditor.TABBING_HORIZONTAL|ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR|ColumnViewerEditor.TABBING_VERTICAL);
		
		tvAgenti.getTable().setHeaderVisible(true);
		tvAgenti.getTable().setLinesVisible(true);
		tvAgenti.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				tvRecapiti.setInput(new Object());
			}
		});
		
		TableColumn tcNome = new TableColumn(tvAgenti.getTable(),SWT.CENTER,0);
		tcNome.setText(NOME);
		tcNome.setWidth(200);
		
		TableViewerColumn tcvNome = new TableViewerColumn(tvAgenti,tcNome);
		tcvNome.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Agenti)cell.getElement()).getNome());
			}
		}
		);

		tcvNome.setEditingSupport(new EditingSupport(tvAgenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvAgenti.getTable());
				tce.setValue("");
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Agenti)element).getNome();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Agenti){
					((Agenti)(element)).setNome((String)value);
					tvAgenti.refresh();
				}
			}
			
		});

		TableColumn tcCognome = new TableColumn(tvAgenti.getTable(),SWT.CENTER,1);
		tcCognome.setText(COGNOME);
		tcCognome.setWidth(200);
		
		TableViewerColumn tcvCognome = new TableViewerColumn(tvAgenti,tcCognome);
		tcvCognome.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Agenti)cell.getElement()).getCognome());
			}
		}
		);
		tcvCognome.setEditingSupport(new EditingSupport(tvAgenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvAgenti.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Agenti)element).getCognome();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Agenti){
					((Agenti)(element)).setCognome((String)value);
					tvAgenti.refresh();
				}
			}
			
		});
		
		TableColumn tcCap = new TableColumn(tvAgenti.getTable(),SWT.CENTER,2);
		tcCap.setText(CAP);
		tcCap.setWidth(50);
		
		TableViewerColumn tcvCap = new TableViewerColumn(tvAgenti,tcCap);
		tcvCap.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Agenti)cell.getElement()).getCap());
			}
		}
		);
		
		tcvCap.setEditingSupport(new EditingSupport(tvAgenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvAgenti.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Agenti)element).getCap();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Agenti){
					((Agenti)(element)).setCap((String)value);
					tvAgenti.refresh();
				}
			}
			
		});

		TableColumn tcProvincia = new TableColumn(tvAgenti.getTable(),SWT.CENTER,3);
		tcProvincia.setText(PROVINCIA);
		tcProvincia.setWidth(100);
		
		TableViewerColumn tcvProvincia = new TableViewerColumn(tvAgenti,tcProvincia);
		tcvProvincia.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Agenti)cell.getElement()).getProvincia());
			}
		}
		);
		
		tcvProvincia.setEditingSupport(new EditingSupport(tvAgenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvAgenti.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Agenti)element).getProvincia();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Agenti){
					((Agenti)(element)).setProvincia((String)value);
					tvAgenti.refresh();
				}
			}
			
		});

		TableColumn tcCitta = new TableColumn(tvAgenti.getTable(),SWT.CENTER,4);
		tcCitta.setText(CITTA);
		tcCitta.setWidth(150);
		
		TableViewerColumn tcvCitta = new TableViewerColumn(tvAgenti,tcCitta);
		tcvCitta.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Agenti)cell.getElement()).getCitta());
			}
		}
		);
		
		tcvCitta.setEditingSupport(new EditingSupport(tvAgenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvAgenti.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Agenti)element).getCitta();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Agenti){
					((Agenti)(element)).setCitta((String)value);
					tvAgenti.refresh();
				}
			}
			
		});
		
		TableColumn tcIndirizzo = new TableColumn(tvAgenti.getTable(),SWT.CENTER,5);
		tcIndirizzo.setText(INDIRIZZO);
		tcIndirizzo.setWidth(200);
		
		TableViewerColumn tcvIndirizzo = new TableViewerColumn(tvAgenti,tcIndirizzo);
		tcvIndirizzo.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Agenti)cell.getElement()).getIndirizzo());
			}
		}
		);
		
		tcvIndirizzo.setEditingSupport(new EditingSupport(tvAgenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvAgenti.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Agenti)element).getIndirizzo();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Agenti){
					((Agenti)(element)).setIndirizzo((String)value);
					tvAgenti.refresh();
				}
			}
			
		});

		TableColumn tcUsername = new TableColumn(tvAgenti.getTable(),SWT.CENTER,6);
		tcUsername.setText(USERNAME);
		tcUsername.setWidth(200);

		TableViewerColumn tcvUsername = new TableViewerColumn(tvAgenti,tcUsername);
		tcvUsername.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				cell.setText(((Agenti)cell.getElement()).getUsername());
			}
		}
		);
		
		tcvUsername.setEditingSupport(new EditingSupport(tvAgenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {				
				TextCellEditor tce = new TextCellEditor(tvAgenti.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {		
				if (element != null && ((Agenti)element).getUsername() != null){
					return ((Agenti)element).getUsername();
				}else{
					return "";
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				
				
				if (element instanceof Agenti){
					if (element != null){
						((Agenti)(element)).setUsername((String)value);
						tvAgenti.refresh();
					}
				}
				
			}
			
		});
		
		TableColumn tcPassword = new TableColumn(tvAgenti.getTable(),SWT.CENTER,7);
		tcPassword.setText(PASSWORD);
		tcPassword.setWidth(200);

		TableViewerColumn tcvPassword = new TableViewerColumn(tvAgenti,tcPassword);
		tcvPassword.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				if (cell.getElement() != null && ((Agenti)cell.getElement()).getPassword() != null){
					int len = ((Agenti)cell.getElement()).getPassword().length();
					String hiddenpws = "";
					for (int i = 0; i < len; i++) {
						hiddenpws += "*";
					}
					cell.setText(hiddenpws);
				}
			}
		}
		);
		
		tcvPassword.setEditingSupport(new EditingSupport(tvAgenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {				
				TextCellEditor tce = new TextCellEditor(tvAgenti.getTable(),SWT.PASSWORD);
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {	
				if (element != null && ((Agenti)element).getPassword() != null){
					return ((Agenti)element).getPassword();
				}else{
					return "";
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element != null){
					if (element instanceof Agenti){
 					    ((Agenti)(element)).setPassword((String)value);
						tvAgenti.refresh();
					}
				}
			}
			
		});

		
		tvAgenti.setContentProvider(new AgentiContentProvider());
		tvAgenti.setLabelProvider(new AgentiLabelProvider());
		tvAgenti.getTable().setLayoutData(tabella);
		tvAgenti.setInput(new Object());
		
		GridData gdTableRecapiti = new GridData();
		gdTableRecapiti.heightHint = 100;

		Composite cRecapiti = tool.createComposite(sectionClient, SWT.NONE);
		cRecapiti.setLayout(new GridLayout());
		cRecapiti.setLayoutData(gdTableRecapiti);
		Composite toolbarRecapiti = tool.createComposite(cRecapiti,SWT.NONE);
		toolbarRecapiti.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNewRecapiti = tool.createImageHyperlink(toolbarRecapiti, SWT.WRAP);		
		ihNewRecapiti.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNewRecapiti.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNewRecapiti.setToolTipText("Nuovo recapito");
		ihNewRecapiti.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (((StructuredSelection)tvAgenti.getSelection()).getFirstElement() != null){
					Agenti agenteModel = (Agenti)((StructuredSelection)tvAgenti.getSelection()).getFirstElement();
					if (agenteModel.getCodAgente() != 0){
						Contatti cModel = oc.newObject(Contatti.class);
						//cModel.set(agenteModel.getCodAgente());
						agenteModel.getContattis1().add(cModel);
						tvRecapiti.setInput(agenteModel.getContattis1());
						tvRecapiti.refresh();
						TableItem ti = tvRecapiti.getTable().getItem(tvRecapiti.getTable().getItemCount()-1);
						Object[] sel = new Object[1];
						sel[0] = ti.getData();

						StructuredSelection ss = new StructuredSelection(sel);
						
						tvRecapiti.setSelection(ss, true);

						Event ev = new Event();
						ev.item = ti;
						ev.data = ti.getData();
						ev.widget = tvRecapiti.getTable();
						tvRecapiti.getTable().notifyListeners(SWT.Selection, ev);
						
					}else{
						MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
												  "Attenzione", 
								"L'agente selezionato non è stato ancora salvato nel database \n eseguirne il salvataggio prima di aggiungere i recapiti");						
					}
				}else{
					MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							"Errore creazione contatto", 
							"Creare una nuovo agente o visionare il dettaglio di una esistente");
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		ImageHyperlink ihConfermaRecapiti = tool.createImageHyperlink(toolbarRecapiti, SWT.WRAP);		
		ihConfermaRecapiti.setImage(Activator.getImageDescriptor("/icons/document-save.png").createImage());
		ihConfermaRecapiti.setHoverImage(Activator.getImageDescriptor("/icons/document-save_over.png").createImage());
		ihConfermaRecapiti.setToolTipText("Salva le modifiche");
		ihConfermaRecapiti.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (
					(((StructuredSelection)tvAgenti.getSelection()).getFirstElement() != null) &&
					((Agenti)((StructuredSelection)tvAgenti.getSelection()).getFirstElement()).getCodAgente() != 0)
				{
					ContattiHelper ch = new ContattiHelper();
					ch.updateListaContatti((Agenti)((StructuredSelection)tvAgenti.getSelection()).getFirstElement(),
										   null,
										   true);					
				}else{
					MessageBox mb = new MessageBox(PlatformUI.getWorkbench()
															 .getActiveWorkbenchWindow()
															 .getShell(),SWT.ICON_WARNING);
					mb.setText("Salvataggio contatti agenti");
					mb.setMessage("Eseguire il salvataggio dell'agente prima di salvare i contatti");			
					mb.open();			
				}
				tvRecapiti.setInput(new Object());
				tvRecapiti.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		

		ImageHyperlink ihCancellaRecapiti = tool.createImageHyperlink(toolbarRecapiti, SWT.WRAP);		
		ihCancellaRecapiti.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancellaRecapiti.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancellaRecapiti.setToolTipText("Cancella elemento selezionato");
		ihCancellaRecapiti.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (tvRecapiti.getSelection() != null){
					Iterator it = ((StructuredSelection)tvRecapiti.getSelection()).iterator();
					while (it.hasNext()) {
						Contatti cModel = (Contatti)it.next();
						((Agenti)((StructuredSelection)tvAgenti.getSelection()).getFirstElement()).getContattis1()
																									   .remove(cModel);
					}
					tvRecapiti.refresh();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});	
		
//		ImageHyperlink ihGoogleConf = tool.createImageHyperlink(toolbarRecapiti, SWT.WRAP);		
//		ihGoogleConf.setImage(Activator.getImageDescriptor("/icons/googleconf.png").createImage());
//		ihGoogleConf.setHoverImage(Activator.getImageDescriptor("/icons/googleconf_hover.png").createImage());
//		ihGoogleConf.setToolTipText("Configurazioni Google");
//		ihGoogleConf.addMouseListener(new MouseListener(){
//
//			@Override
//			public void mouseUp(MouseEvent e) {
//				
//				if (!tvRecapiti.getSelection().isEmpty()){
//					ContattiModel cM = (ContattiModel)((StructuredSelection)tvRecapiti.getSelection()).getFirstElement();
//					if (WinkhouseUtils.getInstance().isGMailAccount(cM.getContatto())){
//						GoogleConfDialog gcd = new GoogleConfDialog(cM);
//					}else{
//						MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//				  				  				"Errore", 
//								  				"Selezionare un contatto di tipo GMail \n Esempio : xxx@gmail.com");												
//					}
//				}else{
//					MessageDialog.openWarning(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//							  				  "Attenzione", 
//											  "Selezionare un contatto");						
//
//				}
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

		tvRecapiti = new TableViewer(cRecapiti,SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		tvRecapiti.getTable().setLayoutData(gdTableRecapiti);
		tvRecapiti.getTable().setLayoutData(tabella);
		tvRecapiti.getTable().setHeaderVisible(true);
		tvRecapiti.getTable().setLinesVisible(true);
		tvRecapiti.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return (((StructuredSelection)tvAgenti.getSelection()).getFirstElement() == null)
						? new ArrayList().toArray()
						: (((Agenti)((StructuredSelection)tvAgenti.getSelection()).getFirstElement()).getContattis1() != null)
						   ? ((Agenti)((StructuredSelection)tvAgenti.getSelection()).getFirstElement()).getContattis1().toArray()
						   : new ArrayList().toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			
		});
		tvRecapiti.setLabelProvider(new ITableLabelProvider(){

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
		
		TableColumn tcTipologia = new TableColumn(tvRecapiti.getTable(),SWT.CENTER,0);
		tcTipologia.setWidth(150);
		tcTipologia.setText("Tipologia recapito");
				
		TableViewerColumn tcvTipologia = new TableViewerColumn(tvRecapiti,tcTipologia);
		tcvTipologia.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				Contatti cModel = ((Contatti)cell.getElement());
				cell.setText((cModel.getTipologiecontatti() == null)
							 ? ""
							 : (cModel.getTipologiecontatti().getDescrizione() == null)
							 	? ""
							 	: cModel.getTipologiecontatti().getDescrizione());
				
			}
			
		});
		tcvTipologia.setEditingSupport(new EditingSupport(tvRecapiti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				ComboBoxCellEditor cbce = new ComboBoxCellEditor(tvRecapiti.getTable(),
																 desTipologieRecapiti,
																 SWT.READ_ONLY|SWT.DROP_DOWN);												
				return cbce;
			}

			@Override
			protected Object getValue(Object element) {
				int index = -1; 
				Contatti cModel = ((Contatti)element);
				if (cModel.getTipologiecontatti() != null){
					
					index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getTipologieContatti(), 
										             cModel.getTipologiecontatti(),
										             comparatorTipologieContatti);
				}
				return index;
			}

			@Override
			protected void setValue(Object element, Object value) {		
				if (((Integer)value).intValue() > -1){
					Contatti cModel = ((Contatti)element);
					cModel.setTipologiecontatti(MobiliaDatiBaseCache.getInstance().getTipologieContatti().get((Integer)value));
					tvRecapiti.refresh();
				}
			}
			
		});
		
		TableColumn tcContatto = new TableColumn(tvRecapiti.getTable(),SWT.CENTER,1);
		tcContatto.setWidth(150);
		tcContatto.setText("Recapito");

		TableViewerColumn tcvContatto = new TableViewerColumn(tvRecapiti,tcContatto);
		
		tceContatto = new TextCellEditor(tvRecapiti.getTable());
		
		tcvContatto.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				cell.setText(
						(((Contatti)cell.getElement()).getContatto()==null)
						? "nuovo recapito"
						: String.valueOf(((Contatti)cell.getElement()).getContatto())
							);
				
			}
			
		});
		
		tcvContatto.setEditingSupport(new EditingSupport(tvRecapiti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return tceContatto;
			}

			@Override
			protected Object getValue(Object element) {
				return (((Contatti)element).getContatto() == null)
	   			   	   ? "nuovo recapito"
			   		   : String.valueOf(((Contatti)element).getContatto());
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Contatti)element).setContatto(String.valueOf(value));
				tvRecapiti.refresh();
			}
			
		});

		TableColumn tcDescrizione = new TableColumn(tvRecapiti.getTable(),SWT.CENTER,2);
		tcContatto.setWidth(150);
		tcContatto.setText("Descrizione");

		TableViewerColumn tcvDescrizione = new TableViewerColumn(tvRecapiti,tcDescrizione);
		
		tceDescrizione = new TextCellEditor(tvRecapiti.getTable());
		
		tcvDescrizione.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				cell.setText(
						(((Contatti)cell.getElement()).getDescrizione()==null)
						? ""
						: String.valueOf(((Contatti)cell.getElement()).getDescrizione())
							);
				
			}
			
		});
		
		tcvDescrizione.setEditingSupport(new EditingSupport(tvRecapiti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return tceDescrizione;
			}

			@Override
			protected Object getValue(Object element) {
				return (((Contatti)element).getDescrizione() == null)
	   			   	   ? ""
			   		   : String.valueOf(((Contatti)element).getDescrizione());
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Contatti)element).setContatto(String.valueOf(value));
				tvRecapiti.refresh();
			}
			
		});		
		section.setClient(sectionClient);
		
	}
	
	private class AgentiContentProvider implements IStructuredContentProvider {

		public AgentiContentProvider() {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			Object[] returnValue = null;
			if (agenti == null) {
				agenti = new AgentiDAO().list(ocAgenti);
			}			
			returnValue = agenti.toArray();
			return returnValue;
		}

		@Override
		public void dispose() {		
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class AgentiLabelProvider implements ITableLabelProvider {
		
		public AgentiLabelProvider(){			
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
	
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			String label = "";
			switch(columnIndex){
				case 0 : label= ((Agenti)element).getNome();
						 break;
				case 1 : label= ((Agenti)element).getCognome();
						 break;
				case 2 : label= ((Agenti)element).getCap();
				 		 break;
				case 3 : label= ((Agenti)element).getProvincia();
						 break;
				case 4 : label= ((Agenti)element).getCitta();
		 		 		 break;
				case 5 : label= ((Agenti)element).getIndirizzo();
		 		 		 break;
				case 6 : label= ((Agenti)element).getUsername();
		 		 		 break;		 		 		 
				case 7 : int len = (((Agenti)element).getPassword() != null)?((Agenti)element).getPassword().length():0;
						 String hiddenpws = "";
						 for (int i = 0; i < len; i++) {
							 hiddenpws += "*";
						 }
						 label = hiddenpws;
						 break;
		 		 		 
		 		default : break;
					 
			}
			return label;
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

		
		
		
	}
	
	public void fillTipologieRecapiti(){		
		MobiliaDatiBaseCache.getInstance().getTipologieContatti();
		Collections.sort(MobiliaDatiBaseCache.getInstance().getTipologieContatti(), comparatorTipologieContatti);
		desTipologieRecapiti = new String[MobiliaDatiBaseCache.getInstance().getTipologieContatti().size()];
		Iterator<Tipologiecontatti> it = MobiliaDatiBaseCache.getInstance().getTipologieContatti().iterator();
		int count = 0;
		while(it.hasNext()){
			desTipologieRecapiti[count] = it.next().getDescrizione();
			count++;
		}		
	}
	
	private Comparator<Tipologiecontatti> comparatorTipologieContatti = new Comparator<Tipologiecontatti>(){
		@Override
		public int compare(Tipologiecontatti arg0, Tipologiecontatti arg1) {
			if ((arg0.getDescrizione() == null) && (arg1.getDescrizione() == null)){
				return 0;
			}else if ((arg0.getDescrizione() != null) && (arg1.getDescrizione() == null)){
				return 1;
			}else if ((arg0.getDescrizione() == null) && (arg1.getDescrizione() != null)){ 
				return -1;
			}else{
				return arg0.getDescrizione().compareTo(arg1.getDescrizione());
			}
		}		
	};

/*
	private void createCauseColloquiSection(){

		Section section = tool.createSection(
				   				form.getBody(), 
				   				Section.DESCRIPTION|Section.TITLE_BAR|
				   				Section.TWISTIE);

		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		
		GridData gdtabella = new GridData();
		gdtabella.grabExcessHorizontalSpace = true;
		gdtabella.grabExcessVerticalSpace = true;
		gdtabella.horizontalAlignment = SWT.FILL;
		gdtabella.verticalAlignment = SWT.FILL;
		
		section.setLayoutData(gdtabella);
		section.setText("Causali colloqui");
		section.setDescription("Causali colloqui impostabili durante la registrazione dei colloqui");
		
		Composite sectionClient = tool.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		Composite toolbar = new Composite(sectionClient,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNew = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				CauseColloquiVO cVO = new CauseColloquiVO();
				cVO.setDescrizione("Nuova causa colloquio");
				causecolloqui.add(cVO);
				tvCausaliColloqui.setInput(causecolloqui);
				tvCausaliColloqui.refresh();
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihConferma = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihConferma.setImage(Activator.getImageDescriptor("/icons/document-save.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/document-save_over.png").createImage());
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				CauseColloquiHelper cch = new CauseColloquiHelper();
				tvCausaliColloqui.setInput(cch.updateDatiBase(causecolloqui));
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihUndo = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihUndo.setImage(Activator.getImageDescriptor("/icons/adept_reinstall.png").createImage());
		ihUndo.setHoverImage(Activator.getImageDescriptor("/icons/adept_reinstall_hover.png").createImage());
		ihUndo.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {				
				causecolloqui = new ArrayList<CauseColloquiVO>(new CauseColloquiDAO().list());
				tvCausaliColloqui.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (tvCausaliColloqui.getSelection() != null){
					Iterator it = ((StructuredSelection)tvCausaliColloqui.getSelection()).iterator();
					while (it.hasNext()) {
						CauseColloquiVO ccVO = (CauseColloquiVO)it.next();
						causecolloqui.remove(ccVO);
					}
					tvCausaliColloqui.refresh();
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
		tvCausaliColloqui = new TableViewer(sectionClient);
		tvCausaliColloqui.getTable().setHeaderVisible(true);
		tvCausaliColloqui.getTable().setLinesVisible(true);
		
		TableColumn tcNome = new TableColumn(tvCausaliColloqui.getTable(),SWT.CENTER,0);
		tcNome.setText(DESCRIZIONE);
		tcNome.setWidth(200);
		
		TableViewerColumn tcvNome = new TableViewerColumn(tvCausaliColloqui,tcNome);
		tcvNome.setEditingSupport(new EditingSupport(tvCausaliColloqui){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvCausaliColloqui.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((CauseColloquiVO)element).getDescrizione();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof CauseColloquiVO){
					((CauseColloquiVO)(element)).setDescrizione((String)value);
					tvCausaliColloqui.refresh();
				}
			}
			
		});
		
		tcvNome.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {				
				return ((CauseColloquiVO)element).getDescrizione();
			}
			
		});
		
		tvCausaliColloqui.setContentProvider(new CausaliColloquiContentProvider());
		tvCausaliColloqui.setLabelProvider(new CausaliColloquiLabelProvider());
		tvCausaliColloqui.getTable().setLayoutData(tabella);
		tvCausaliColloqui.setInput(new Object());
		section.setClient(sectionClient);
		
	}
	
	private class CausaliColloquiContentProvider implements IStructuredContentProvider {

		public CausaliColloquiContentProvider() {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			Object[] returnValue = null;
			if (causecolloqui == null){
				causecolloqui = new ArrayList<CauseColloquiVO>(new CauseColloquiDAO().list());				
			}
			returnValue = causecolloqui.toArray();
			return returnValue;
		}

		@Override
		public void dispose() {		
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class CausaliColloquiLabelProvider extends LabelProvider{
		
		public CausaliColloquiLabelProvider(){			
		}

		@Override
		public String getText(Object element) {			
			return ((CauseColloquiVO)element).getDescrizione();
		}
		
		
		
	}

*/
	private void createClassiClientiSection(){

		Section section = tool.createSection(
				   				form.getBody(), 
				   				Section.DESCRIPTION|Section.TITLE_BAR|
				   				Section.TWISTIE);
		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
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
		
		section.setLayoutData(gdtabella);
		section.setText("Categorie clienti");
		section.setDescription("Categorie in cui catalogare la clientela dell'agenzia");
		
		Composite sectionClient = tool.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		Composite toolbar = new Composite(sectionClient,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNew = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.setToolTipText("Nuova categoria cliente");
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				Classicliente cVO = new Classicliente();
				cVO.setOrdine(1);
				cVO.setDescrizione("Nuova categoria cliente");
				classiclienti.add(cVO);
				tvClassiClienti.setInput(classiclienti);
				tvClassiClienti.refresh();
				
				TableItem ti = tvClassiClienti.getTable().getItem(tvClassiClienti.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvClassiClienti.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvClassiClienti.getTable();
				tvClassiClienti.getTable().notifyListeners(SWT.Selection, ev);
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihConferma = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihConferma.setImage(Activator.getImageDescriptor("/icons/document-save.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/document-save_over.png").createImage());
		ihConferma.setToolTipText("Salva le modifiche");
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				ClassiClientiHelper cch = new ClassiClientiHelper();				
				cch.updateDatiBase(classiclienti);
				classiclienti = null;
				tvClassiClienti.setInput(new Object());
				tvClassiClienti.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihUndo = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihUndo.setImage(Activator.getImageDescriptor("/icons/adept_reinstall.png").createImage());
		ihUndo.setHoverImage(Activator.getImageDescriptor("/icons/adept_reinstall_hover.png").createImage());
		ihUndo.setToolTipText("Ricarica dall'archivio");
		ihUndo.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {				
				classiclienti = new ArrayList<Classicliente>(new ClassiClientiDAO().list(null));
				tvClassiClienti.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.setToolTipText("Cancella elemento selezionato");
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				if (tvClassiClienti.getSelection() != null){
					int count = 0;
					boolean result = true;
					Iterator it = ((StructuredSelection)tvClassiClienti.getSelection()).iterator();
					ClassiClientiHelper cch = new ClassiClientiHelper();					
					while (it.hasNext()) {
						Classicliente ccVO = (Classicliente)it.next();
						if (cch.deleteClasseCliente(ccVO)){
							classiclienti.remove(ccVO);							
						}						
						
					}
					classiclienti = null;
					tvClassiClienti.setInput(new Object());
					tvClassiClienti.refresh();
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
		tvClassiClienti = new TableViewer(sectionClient,SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvClassiClienti.getTable().setHeaderVisible(true);
		tvClassiClienti.getTable().setLinesVisible(true);
		
		TableColumn tcNome = new TableColumn(tvClassiClienti.getTable(),SWT.CENTER,0);
		tcNome.setText(DESCRIZIONE);
		tcNome.setWidth(200);
		
		TableViewerColumn tcvNome = new TableViewerColumn(tvClassiClienti,tcNome);
		tcvNome.setEditingSupport(new EditingSupport(tvClassiClienti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvClassiClienti.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Classicliente)element).getDescrizione();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Classicliente){
					((Classicliente)(element)).setDescrizione((String)value);
					tvClassiClienti.refresh();
				}
			}
			
		});
		
		tcvNome.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {				
				return ((Classicliente)element).getDescrizione();
			}
			
		});

		TableColumn tcOrdine = new TableColumn(tvClassiClienti.getTable(),SWT.CENTER,1);
		tcOrdine.setText(ORDINE);
		tcOrdine.setWidth(200);
		
		TableViewerColumn tcvOrdine = new TableViewerColumn(tvClassiClienti,tcOrdine);
		tcvOrdine.setEditingSupport(new EditingSupport(tvClassiClienti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvClassiClienti.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return String.valueOf(((Classicliente)element).getOrdine());
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Classicliente){
					try{
						((Classicliente)(element)).setOrdine(Integer.valueOf((String)value));
						tvClassiClienti.refresh();						
					}catch(Exception cce){
						((Classicliente)(element)).setOrdine(1);
						cce.printStackTrace();
					}
				}
			}
			
		});
		
		tcvOrdine.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {				
				return String.valueOf(
										(((Classicliente)element).getOrdine() == 0)
										? 1
										: ((Classicliente)element).getOrdine()
									 );
			}
			
		});
		
		tvClassiClienti.setContentProvider(new ClassiClientiContentProvider());
		tvClassiClienti.setLabelProvider(new ClassiClientiLabelProvider());
		tvClassiClienti.getTable().setLayoutData(tabella);
		tvClassiClienti.setInput(new Object());
		section.setClient(sectionClient);
		
	}

	private class ClassiClientiContentProvider implements IStructuredContentProvider {

		public ClassiClientiContentProvider() {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			Object[] returnValue = null;
			if (classiclienti == null){
				classiclienti = new ArrayList<Classicliente>(new ClassiClientiDAO().list(null));				
			}
			returnValue = classiclienti.toArray();
			return returnValue;
		}

		@Override
		public void dispose() {		
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class ClassiClientiLabelProvider implements ITableLabelProvider{
		
		public ClassiClientiLabelProvider(){			
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			switch(columnIndex){
				case 0: return ((Classicliente)element).getDescrizione();		
				case 1: return String.valueOf(((Classicliente)element).getOrdine());
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
		
	}
	
	private class TipiAppuntamentiContentProvider implements IStructuredContentProvider {

		public TipiAppuntamentiContentProvider() {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			Object[] returnValue = null;
			if (tipiappuntamenti == null){
				tipiappuntamenti = new ArrayList<Tipiappuntamenti>(new TipiAppuntamentiDAO().listTipiAppuntamenti());				
			}
			returnValue = tipiappuntamenti.toArray();
			return returnValue;
		}

		@Override
		public void dispose() {		
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class TipiAppuntamentiLabelProvider implements ITableLabelProvider{
		
		public TipiAppuntamentiLabelProvider(){			
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			switch(columnIndex){
				case 0: return ((Tipiappuntamenti)element).getDescrizione();
				case 1: return String.valueOf(((Tipiappuntamenti)element).getOrdine());
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

	}
	
	private class ClassiEnergeticheContentProvider implements IStructuredContentProvider {

		public ClassiEnergeticheContentProvider() {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			Object[] returnValue = null;
			if (classienergetiche == null){
				classienergetiche = new ArrayList<Classienergetiche>(new ClassiEnergeticheDAO().listClassiEnergetiche());				
			}
			returnValue = classienergetiche.toArray();
			return returnValue;
		}

		@Override
		public void dispose() {		
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class ClassiEnergeticheLabelProvider implements ITableLabelProvider{
		
		public ClassiEnergeticheLabelProvider(){			
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			switch(columnIndex){
				case 0: return ((Classienergetiche)element).getNome();
				case 1: return ((Classienergetiche)element).getDescrizione();
				case 2: return String.valueOf(((Classienergetiche)element).getOrdine());
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

	}	
	
	private void createRiscaldamentiSection(){

		Section section = tool.createSection(
				   				form.getBody(), 
				   				Section.DESCRIPTION|Section.TITLE_BAR|
				   				Section.TWISTIE);

		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
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
		
		section.setLayoutData(gdtabella);
		section.setText("Impianti riscaldamento");
		section.setDescription("Tipologie degli impianti di riscaldamento degli immobili");
		
		Composite sectionClient = tool.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		Composite toolbar = new Composite(sectionClient,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNew = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.setToolTipText("Nuova tipologia riscaldamento");
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				Riscaldamenti rVO = oc.newObject(Riscaldamenti.class);
				rVO.setDescrizione("Nuovo Riscaldamento");
				riscaldamenti.add(rVO);
				tvRiscaldamenti.setInput(riscaldamenti);
				tvRiscaldamenti.refresh();
				
				TableItem ti = tvRiscaldamenti.getTable().getItem(tvRiscaldamenti.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvRiscaldamenti.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvRiscaldamenti.getTable();
				tvRiscaldamenti.getTable().notifyListeners(SWT.Selection, ev);

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihConferma = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihConferma.setImage(Activator.getImageDescriptor("/icons/document-save.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/document-save_over.png").createImage());
		ihConferma.setToolTipText("Salva le modifiche");
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				RiscaldamentiHelper rh = new RiscaldamentiHelper();				
				rh.updateDatiBase(riscaldamenti);
				riscaldamenti = null;
				tvRiscaldamenti.setInput(new Object());
				tvRiscaldamenti.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihUndo = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihUndo.setImage(Activator.getImageDescriptor("/icons/adept_reinstall.png").createImage());
		ihUndo.setHoverImage(Activator.getImageDescriptor("/icons/adept_reinstall_hover.png").createImage());
		ihUndo.setToolTipText("Ricarica dall'archivio");
		ihUndo.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {				
				riscaldamenti = new ArrayList<Riscaldamenti>(new RiscaldamentiDAO().list());
				tvRiscaldamenti.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.setToolTipText("Cancella l'elemento selezionato");
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				if (tvRiscaldamenti.getSelection() != null){
					int count = 0;
					boolean result = true;
					Iterator it = ((StructuredSelection)tvRiscaldamenti.getSelection()).iterator();
					RiscaldamentiHelper rh = new RiscaldamentiHelper();					
					while (it.hasNext()) {
						Riscaldamenti rVO = (Riscaldamenti)it.next();
						if (rh.deleteRiscaldamento(rVO)){
							riscaldamenti.remove(rVO);							
						}						
						
					}
					riscaldamenti = null;
					tvRiscaldamenti.setInput(new Object());
					tvRiscaldamenti.refresh();
				}
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		tvRiscaldamenti = new TableViewer(sectionClient,SWT.MULTI|SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvRiscaldamenti.getTable().setHeaderVisible(true);
		tvRiscaldamenti.getTable().setLinesVisible(true);
		
		TableColumn tcNome = new TableColumn(tvRiscaldamenti.getTable(),SWT.CENTER,0);
		tcNome.setText(DESCRIZIONE);
		tcNome.setWidth(200);
		
		TableViewerColumn tcvNome = new TableViewerColumn(tvRiscaldamenti,tcNome);
		tcvNome.setEditingSupport(new EditingSupport(tvRiscaldamenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvRiscaldamenti.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Riscaldamenti)element).getDescrizione();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Riscaldamenti){
					((Riscaldamenti)(element)).setDescrizione((String)value);
					tvRiscaldamenti.refresh();
				}
			}
			
		});
		
		tcvNome.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {				
				return ((Riscaldamenti)element).getDescrizione();
			}
			
		});
		
		tvRiscaldamenti.getTable().setLayoutData(tabella);
		section.setClient(sectionClient);
		
		tvRiscaldamenti.setContentProvider(new RiscaldamentiContentProvider());
		tvRiscaldamenti.setLabelProvider(new RiscaldamentiLabelProvider());
		tvRiscaldamenti.setInput(new Object());
		
	}
	
	private class RiscaldamentiContentProvider implements IStructuredContentProvider {

		public RiscaldamentiContentProvider() {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			Object[] returnValue = null;
			if (riscaldamenti == null){
				riscaldamenti = new ArrayList<Riscaldamenti>(new RiscaldamentiDAO().list());				
			}
			returnValue = riscaldamenti.toArray();
			return returnValue;
		}

		@Override
		public void dispose() {		
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class RiscaldamentiLabelProvider extends LabelProvider{
		
		public RiscaldamentiLabelProvider(){			
		}

		@Override
		public String getText(Object element) {			
			return ((Riscaldamenti)element).getDescrizione();
		}
		
		
		
	}

	private void createStatoConservativoSection(){

		Section section = tool.createSection(
				   				form.getBody(), 
				   				Section.DESCRIPTION|Section.TITLE_BAR|
				   				Section.TWISTIE);

		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
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
		
		section.setLayoutData(gdtabella);
		section.setText("Stati conservativi");
		section.setDescription("Stati conservativi degli immobili registrati");
		
		Composite sectionClient = tool.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		Composite toolbar = new Composite(sectionClient,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNew = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.setToolTipText("Nuovo stato conservativo");
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				Statoconservativo scVO = oc.newObject(Statoconservativo.class);;
				scVO.setDescrizione("Nuovo stato conservativo");
				statoconservativo.add(scVO);
				tvStatoConservativo.setInput(statoconservativo);
				tvStatoConservativo.refresh();
				
				TableItem ti = tvStatoConservativo.getTable().getItem(tvStatoConservativo.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvStatoConservativo.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvStatoConservativo.getTable();
				tvStatoConservativo.getTable().notifyListeners(SWT.Selection, ev);
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihConferma = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihConferma.setImage(Activator.getImageDescriptor("/icons/document-save.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/document-save_over.png").createImage());
		ihConferma.setToolTipText("Salva le modifiche");
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				StatoConservativoHelper sch = new StatoConservativoHelper();		
				sch.updateDatiBase(statoconservativo);
				statoconservativo = null;
				tvStatoConservativo.setInput(new Object());
				tvStatoConservativo.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihUndo = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihUndo.setImage(Activator.getImageDescriptor("/icons/adept_reinstall.png").createImage());
		ihUndo.setHoverImage(Activator.getImageDescriptor("/icons/adept_reinstall_hover.png").createImage());
		ihUndo.setToolTipText("Ricarica dall'archivio");
		ihUndo.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {				
				statoconservativo = new ArrayList<Statoconservativo>(new StatoConservativoDAO().list());
				tvStatoConservativo.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.setToolTipText("Cancella elemento selezionato");
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				if (tvStatoConservativo.getSelection() != null){
					int count = 0;
					boolean result = true;
					Iterator it = ((StructuredSelection)tvStatoConservativo.getSelection()).iterator();
					StatoConservativoHelper sch = new StatoConservativoHelper();					
					while (it.hasNext()) {
						Statoconservativo scVO = (Statoconservativo)it.next();
						if (sch.deleteStatoConservativo(scVO)){
							statoconservativo.remove(scVO);							
						}						
						
					}
					statoconservativo = null;
					tvStatoConservativo.setInput(new Object());
					tvStatoConservativo.refresh();
				}
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		tvStatoConservativo = new TableViewer(sectionClient,SWT.MULTI|SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvStatoConservativo.getTable().setHeaderVisible(true);
		tvStatoConservativo.getTable().setLinesVisible(true);
		
		TableColumn tcNome = new TableColumn(tvStatoConservativo.getTable(),SWT.CENTER,0);
		tcNome.setText(DESCRIZIONE);
		tcNome.setWidth(200);
		
		TableViewerColumn tcvNome = new TableViewerColumn(tvStatoConservativo,tcNome);
		tcvNome.setEditingSupport(new EditingSupport(tvStatoConservativo){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvStatoConservativo.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Statoconservativo)element).getDescrizione();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Statoconservativo){
					((Statoconservativo)(element)).setDescrizione((String)value);
					tvStatoConservativo.refresh();
				}
			}
			
		});
		
		tcvNome.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {				
				return ((Statoconservativo)element).getDescrizione();
			}
			
		});
		
		tvStatoConservativo.getTable().setLayoutData(tabella);
		section.setClient(sectionClient);
		
		tvStatoConservativo.setContentProvider(new StatoConservativoContentProvider());
		tvStatoConservativo.setLabelProvider(new StatoConservativoLabelProvider());
		tvStatoConservativo.setInput(new Object());
		
	}

	private class StatoConservativoContentProvider implements IStructuredContentProvider {

		public StatoConservativoContentProvider() {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			Object[] returnValue = null;
			if (statoconservativo == null){
				statoconservativo = new ArrayList<Statoconservativo>(new StatoConservativoDAO().list());				
			}
			returnValue = statoconservativo.toArray();
			return returnValue;
		}

		@Override
		public void dispose() {		
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class StatoConservativoLabelProvider extends LabelProvider{
		
		public StatoConservativoLabelProvider(){			
		}

		@Override
		public String getText(Object element) {			
			return ((Statoconservativo)element).getDescrizione();
		}
		
		
		
	}
	
	
	private void createTipologieStanzeSection(){

		Section section = tool.createSection(
				   				form.getBody(), 
				   				Section.DESCRIPTION|Section.TITLE_BAR|
				   				Section.TWISTIE);

		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
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
		
		section.setLayoutData(gdtabella);
		section.setText("Tipologie stanze");
		section.setDescription("Tipologie delle stanze registrate per gli immobili");
		
		Composite sectionClient = tool.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		Composite toolbar = new Composite(sectionClient,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNew = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.setToolTipText("Nuova tipologia stanza");
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				Tipologiastanze scVO = oc.newObject(Tipologiastanze.class);
				scVO.setDescrizione("Nuovo tipologia stanze");
				tipologiastanze.add(scVO);
				tvTipologiaStanze.setInput(tipologiastanze);
				tvTipologiaStanze.refresh();
				
				TableItem ti = tvTipologiaStanze.getTable().getItem(tvTipologiaStanze.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvTipologiaStanze.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvTipologiaStanze.getTable();
				tvTipologiaStanze.getTable().notifyListeners(SWT.Selection, ev);
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihConferma = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihConferma.setImage(Activator.getImageDescriptor("/icons/document-save.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/document-save_over.png").createImage());
		ihConferma.setToolTipText("Salva le modifiche");
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				TipologiaStanzeHelper tsh = new TipologiaStanzeHelper();		
				tsh.updateDatiBase(tipologiastanze);
				tipologiastanze = null;
				tvTipologiaStanze.setInput(new Object());
				tvTipologiaStanze.refresh();
				
				TipologiaStanzeHelper sch = new TipologiaStanzeHelper();
				tvTipologiaStanze.setInput(sch.updateDatiBase(tipologiastanze));
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihUndo = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihUndo.setImage(Activator.getImageDescriptor("/icons/adept_reinstall.png").createImage());
		ihUndo.setHoverImage(Activator.getImageDescriptor("/icons/adept_reinstall_hover.png").createImage());
		ihUndo.setToolTipText("Ricarica dall'archivio");
		ihUndo.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {				
				tipologiastanze = new TipologiaStanzeDAO().list();
				tvTipologiaStanze.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.setToolTipText("Cancella l'elemento selezionato");
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (tvTipologiaStanze.getSelection() != null){
					int count = 0;
					boolean result = true;
					Iterator it = ((StructuredSelection)tvTipologiaStanze.getSelection()).iterator();
					TipologiaStanzeHelper tsh = new TipologiaStanzeHelper();					
					while (it.hasNext()) {
						Tipologiastanze tsVO = (Tipologiastanze)it.next();
						if (tsh.deleteTipologiaStanze(tsVO)){
							tipologiastanze.remove(tsVO);							
						}						
						
					}
					tipologiastanze = null;
					tvTipologiaStanze.setInput(new Object());
					tvTipologiaStanze.refresh();
				}
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		tvTipologiaStanze = new TableViewer(sectionClient,SWT.MULTI|SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvTipologiaStanze.getTable().setHeaderVisible(true);
		tvTipologiaStanze.getTable().setLinesVisible(true);
		
		TableColumn tcNome = new TableColumn(tvTipologiaStanze.getTable(),SWT.CENTER,0);
		tcNome.setText(DESCRIZIONE);
		tcNome.setWidth(200);
		
		TableViewerColumn tcvNome = new TableViewerColumn(tvTipologiaStanze,tcNome);
		tcvNome.setEditingSupport(new EditingSupport(tvTipologiaStanze){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvTipologiaStanze.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Tipologiastanze)element).getDescrizione();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Tipologiastanze){
					((Tipologiastanze)(element)).setDescrizione((String)value);
					tvTipologiaStanze.refresh();
				}
			}
			
		});
		
		tcvNome.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {				
				return ((Tipologiastanze)element).getDescrizione();
			}
			
		});
		
		tvTipologiaStanze.getTable().setLayoutData(tabella);
		section.setClient(sectionClient);
		
		tvTipologiaStanze.setContentProvider(new TipologiaStanzeContentProvider());
		tvTipologiaStanze.setLabelProvider(new TipologiaStanzeLabelProvider());
		tvTipologiaStanze.setInput(new Object());
		
	}

	private class TipologiaStanzeContentProvider implements IStructuredContentProvider {

		public TipologiaStanzeContentProvider() {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			Object[] returnValue = null;
			if (tipologiastanze == null){
				tipologiastanze = new TipologiaStanzeDAO().list();				
			}
			returnValue = tipologiastanze.toArray();
			return returnValue;
		}

		@Override
		public void dispose() {		
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class TipologiaStanzeLabelProvider extends LabelProvider{
		
		public TipologiaStanzeLabelProvider(){			
		}

		@Override
		public String getText(Object element) {			
			return ((Tipologiastanze)element).getDescrizione();
		}
		
		
		
	}

	/*	private void createTipologieColloquiSection(){

		Section section = tool.createSection(
				   				form.getBody(), 
				   				Section.DESCRIPTION|Section.TITLE_BAR|
				   				Section.TWISTIE);

		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		
		GridData gdtabella = new GridData();
		gdtabella.grabExcessHorizontalSpace = true;
		gdtabella.grabExcessVerticalSpace = true;
		gdtabella.horizontalAlignment = SWT.FILL;
		gdtabella.verticalAlignment = SWT.FILL;		
		
		section.setLayoutData(gdtabella);
		section.setText("Tipologie colloqui");
		section.setDescription("Tipologie dei colloqui registrati");
		
		Composite sectionClient = tool.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		Composite toolbar = new Composite(sectionClient,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNew = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				TipologieColloquiVO scVO = new TipologieColloquiVO();
				scVO.setDescrizione("Nuovo tipologia colloquio");
				tipologiecolloqui.add(scVO);
				tvTipologieColloqui.setInput(tipologiecolloqui);
				tvTipologieColloqui.refresh();
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihConferma = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihConferma.setImage(Activator.getImageDescriptor("/icons/document-save.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/document-save_over.png").createImage());
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				TipologieColloquiHelper sch = new TipologieColloquiHelper();
				tvTipologieColloqui.setInput(sch.updateDatiBase(tipologiecolloqui));
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihUndo = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihUndo.setImage(Activator.getImageDescriptor("/icons/adept_reinstall.png").createImage());
		ihUndo.setHoverImage(Activator.getImageDescriptor("/icons/adept_reinstall_hover.png").createImage());
		ihUndo.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {				
				tipologiecolloqui = new ArrayList<TipologieColloquiVO>(new TipologieColloquiDAO().list());
				tvTipologieColloqui.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (tvTipologieColloqui.getSelection() != null){
					Iterator<TipologieColloquiVO> it = ((StructuredSelection)tvTipologieColloqui.getSelection()).iterator();
					while (it.hasNext()) {
						TipologieColloquiVO scVO = (TipologieColloquiVO)it.next();
						tipologiecolloqui.remove(scVO);
					}
					tvTipologieColloqui.refresh();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		tvTipologieColloqui = new TableViewer(sectionClient,SWT.MULTI);
		tvTipologieColloqui.getTable().setHeaderVisible(true);
		tvTipologieColloqui.getTable().setLinesVisible(true);
		
		TableColumn tcNome = new TableColumn(tvTipologieColloqui.getTable(),SWT.CENTER,0);
		tcNome.setText(DESCRIZIONE);
		tcNome.setWidth(200);
		
		TableViewerColumn tcvNome = new TableViewerColumn(tvTipologieColloqui,tcNome);
		tcvNome.setEditingSupport(new EditingSupport(tvTipologieColloqui){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvTipologieColloqui.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((TipologieColloquiVO)element).getDescrizione();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof TipologieColloquiVO){
					((TipologieColloquiVO)(element)).setDescrizione((String)value);
					tvTipologieColloqui.refresh();
				}
			}
			
		});
		
		tcvNome.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {				
				return ((TipologieColloquiVO)element).getDescrizione();
			}
			
		});
		
		tvTipologieColloqui.getTable().setLayoutData(tabella);
		section.setClient(sectionClient);
		
		tvTipologieColloqui.setContentProvider(new TipologiaColloquiContentProvider());
		tvTipologieColloqui.setLabelProvider(new TipologiaColloquiLabelProvider());
		tvTipologieColloqui.setInput(new Object());
		
	}

	private class TipologiaColloquiContentProvider implements IStructuredContentProvider {

		public TipologiaColloquiContentProvider() {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			Object[] returnValue = null;
			if (tipologiecolloqui == null){
				tipologiecolloqui = new ArrayList<TipologieColloquiVO>(EnvSettingsFactory.getInstance().getTipologieColloqui());				
			}
			returnValue = tipologiecolloqui.toArray();
			return returnValue;
		}

		@Override
		public void dispose() {		
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class TipologiaColloquiLabelProvider extends LabelProvider{
		
		public TipologiaColloquiLabelProvider(){			
		}

		@Override
		public String getText(Object element) {			
			return ((TipologieColloquiVO)element).getDescrizione();
		}
		
		
		
	}
	*/
	private void createTipologieContattiSection(){

		Section section = tool.createSection(
				   				form.getBody(), 
				   				Section.DESCRIPTION|Section.TITLE_BAR|
				   				Section.TWISTIE);

		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
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
		
		section.setLayoutData(gdtabella);
		section.setText("Tipologie recapiti");
		section.setDescription("Tipologie dei recapiti registrati");
		
		Composite sectionClient = tool.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		Composite toolbar = new Composite(sectionClient,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNew = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.setToolTipText("Nuova tipologia recapito");
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				Tipologiecontatti scVO = oc.newObject(Tipologiecontatti.class);
				scVO.setDescrizione("Nuovo tipologia contatto");
				tipologiecontatti.add(scVO);
				tvTipologieContatti.setInput(tipologiecontatti);
				tvTipologieContatti.refresh();
				
				TableItem ti = tvTipologieContatti.getTable().getItem(tvTipologieContatti.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvTipologieContatti.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvTipologieContatti.getTable();
				tvTipologieContatti.getTable().notifyListeners(SWT.Selection, ev);

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihConferma = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihConferma.setImage(Activator.getImageDescriptor("/icons/document-save.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/document-save_over.png").createImage());
		ihConferma.setToolTipText("Salva le modifiche");
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				TipologiaContattiHelper tch = new TipologiaContattiHelper();		
				tch.updateDatiBase(tipologiecontatti);
				tipologiecontatti = null;
				tvTipologieContatti.setInput(new Object());
				tvTipologieContatti.refresh();
				fillTipologieRecapiti();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihUndo = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihUndo.setImage(Activator.getImageDescriptor("/icons/adept_reinstall.png").createImage());
		ihUndo.setHoverImage(Activator.getImageDescriptor("/icons/adept_reinstall_hover.png").createImage());
		ihUndo.setToolTipText("Ricarica dall'archivio");
		ihUndo.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {				
				tipologiecontatti = new TipologiaContattiDAO().list();
				tvTipologieContatti.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.setToolTipText("Cancella elemento selezionato");
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (tvTipologieContatti.getSelection() != null){
					int count = 0;
					boolean result = true;
					Iterator it = ((StructuredSelection)tvTipologieContatti.getSelection()).iterator();
					TipologiaContattiHelper tch = new TipologiaContattiHelper();					
					while (it.hasNext()) {
						Tipologiecontatti tcVO = (Tipologiecontatti)it.next();
						if (tch.deleteTipologiaContatti(tcVO)){
							tipologiecontatti.remove(tcVO);							
						}						
						
					}
					tipologiecontatti = null;
					tvTipologieContatti.setInput(new Object());
					tvTipologieContatti.refresh();
				}
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		tvTipologieContatti = new TableViewer(sectionClient,SWT.MULTI|SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvTipologieContatti.getTable().setHeaderVisible(true);
		tvTipologieContatti.getTable().setLinesVisible(true);
		
		TableColumn tcNome = new TableColumn(tvTipologieContatti.getTable(),SWT.CENTER,0);
		tcNome.setText(DESCRIZIONE);
		tcNome.setWidth(200);
		
		TableViewerColumn tcvNome = new TableViewerColumn(tvTipologieContatti,tcNome);
		tcvNome.setEditingSupport(new EditingSupport(tvTipologieContatti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvTipologieContatti.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Tipologiecontatti)element).getDescrizione();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Tipologiecontatti){
					((Tipologiecontatti)(element)).setDescrizione((String)value);
					tvTipologieContatti.refresh();
				}
			}
			
		});
		
		tcvNome.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {				
				return ((Tipologiecontatti)element).getDescrizione();
			}
			
		});
		
		tvTipologieContatti.getTable().setLayoutData(tabella);
		section.setClient(sectionClient);
		
		tvTipologieContatti.setContentProvider(new TipologieContattiContentProvider());
		tvTipologieContatti.setLabelProvider(new TipologieContattiLabelProvider());
		tvTipologieContatti.setInput(new Object());
		
	}

	private class TipologieContattiContentProvider implements IStructuredContentProvider {

		public TipologieContattiContentProvider() {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			Object[] returnValue = null;
			if (tipologiecontatti == null){
				tipologiecontatti = new TipologiaContattiDAO().list();				
			}
			returnValue = tipologiecontatti.toArray();
			return returnValue;
		}

		@Override
		public void dispose() {		
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class TipologieContattiLabelProvider extends LabelProvider{
		
		public TipologieContattiLabelProvider(){			
		}

		@Override
		public String getText(Object element) {			
			return ((Tipologiecontatti)element).getDescrizione();
		}
		
		
		
	}

	private void createTipologieImmobiliSection(){

		Section section = tool.createSection(
				   				form.getBody(), 
				   				Section.DESCRIPTION|Section.TITLE_BAR|
				   				Section.TWISTIE);

		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
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
		
		section.setLayoutData(gdtabella);
		section.setText("Tipologie immobili");
		section.setDescription("Tipologie degli immobili registrati");
		
		Composite sectionClient = tool.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		Composite toolbar = new Composite(sectionClient,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNew = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.setToolTipText("Nuova tipologia immobile");
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				Tipologieimmobili scVO = oc.newObject(Tipologieimmobili.class);
				scVO.setDescrizione("Nuovo tipologia immobile");
				tipologieimmobili.add(scVO);
				tvTipologieImmobili.setInput(tipologieimmobili);
				tvTipologieImmobili.refresh();
				
				TableItem ti = tvTipologieImmobili.getTable().getItem(tvTipologieImmobili.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvTipologieImmobili.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvTipologieImmobili.getTable();
				tvTipologieImmobili.getTable().notifyListeners(SWT.Selection, ev);

				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihConferma = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihConferma.setImage(Activator.getImageDescriptor("/icons/document-save.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/document-save_over.png").createImage());
		ihConferma.setToolTipText("Salva le modifiche");
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				TipologieImmobiliHelper tih = new TipologieImmobiliHelper();		
				tih.updateDatiBase(tipologieimmobili);
				tipologieimmobili = null;
				tvTipologieImmobili.setInput(new Object());
				tvTipologieImmobili.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihUndo = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihUndo.setImage(Activator.getImageDescriptor("/icons/adept_reinstall.png").createImage());
		ihUndo.setHoverImage(Activator.getImageDescriptor("/icons/adept_reinstall_hover.png").createImage());
		ihUndo.setToolTipText("Ricarica dall'archivio");
		ihUndo.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {				
				tipologieimmobili = new TipologieImmobiliDAO().list();
				tvTipologieImmobili.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.setToolTipText("Cancella l'elemento selezionato");
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (tvTipologieImmobili.getSelection() != null){
					int count = 0;
					boolean result = true;
					Iterator it = ((StructuredSelection)tvTipologieImmobili.getSelection()).iterator();
					TipologieImmobiliHelper tih = new TipologieImmobiliHelper();					
					while (it.hasNext()) {
						Tipologieimmobili tiVO = (Tipologieimmobili)it.next();
						if (tih.deleteTipologiaImmobili(tiVO)){
							tipologieimmobili.remove(tiVO);							
						}						
						
					}
					tipologieimmobili = null;
					tvTipologieImmobili.setInput(new Object());
					tvTipologieImmobili.refresh();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		
		tvTipologieImmobili = new TableViewer(sectionClient,SWT.MULTI|SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvTipologieImmobili.getTable().setHeaderVisible(true);
		tvTipologieImmobili.getTable().setLinesVisible(true);
		
		tvTipologieImmobili.getTable().setLayoutData(tabella);
		
		TableColumn tcNome = new TableColumn(tvTipologieImmobili.getTable(),SWT.CENTER,0);
		tcNome.setText(DESCRIZIONE);
		tcNome.setWidth(200);
		
		TableViewerColumn tcvNome = new TableViewerColumn(tvTipologieImmobili,tcNome);
		tcvNome.setEditingSupport(new EditingSupport(tvTipologieImmobili){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvTipologieImmobili.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Tipologieimmobili)element).getDescrizione();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Tipologieimmobili){
					((Tipologieimmobili)(element)).setDescrizione((String)value);
					tvTipologieImmobili.refresh();
				}
			}
			
		});
		
		tcvNome.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {				
				return ((Tipologieimmobili)element).getDescrizione();
			}
			
		});
		
//		tvTipologieImmobili.getTable().setLayoutData(tabella);
		section.setClient(sectionClient);
		
		tvTipologieImmobili.setContentProvider(new TipologieImmobiliContentProvider());
		tvTipologieImmobili.setLabelProvider(new TipologieImmobiliLabelProvider());
		tvTipologieImmobili.setInput(new Object());
		
	}

	private class TipologieImmobiliContentProvider implements IStructuredContentProvider {

		public TipologieImmobiliContentProvider() {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			Object[] returnValue = null;
			if (tipologieimmobili == null){
				tipologieimmobili = new TipologieImmobiliDAO().list();				
			}
			returnValue = tipologieimmobili.toArray();
			return returnValue;
		}

		@Override
		public void dispose() {		
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class TipologieImmobiliLabelProvider extends LabelProvider{
		
		public TipologieImmobiliLabelProvider(){			
		}

		@Override
		public String getText(Object element) {			
			return ((Tipologieimmobili)element).getDescrizione();
		}
		
		
		
	}

	
	
	@Override
	public void setFocus() {


	}
	
	private void createTipiAppuntamentiSection(){

		Section section = tool.createSection(
				   				form.getBody(), 
				   				Section.DESCRIPTION|Section.TITLE_BAR|
				   				Section.TWISTIE);
		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
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
		
		section.setLayoutData(gdtabella);
		section.setText("Tipi appuntamenti");
		section.setDescription("Categorie in cui catalogare le tipologie di appuntamento");
		
		Composite sectionClient = tool.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		Composite toolbar = new Composite(sectionClient,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNew = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.setToolTipText("Nuovo tipo appuntamento");
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				Tipiappuntamenti taVO = oc.newObject(Tipiappuntamenti.class);
				taVO.setOrdine(1);
				taVO.setDescrizione("Nuovo tipo appuntamento");
				tipiappuntamenti.add(taVO);
				tvTipiAppuntamenti.setInput(tipiappuntamenti);
				tvTipiAppuntamenti.refresh();
				
				TableItem ti = tvTipiAppuntamenti.getTable().getItem(tvTipiAppuntamenti.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvTipiAppuntamenti.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvTipiAppuntamenti.getTable();
				tvTipiAppuntamenti.getTable().notifyListeners(SWT.Selection, ev);
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihConferma = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihConferma.setImage(Activator.getImageDescriptor("/icons/document-save.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/document-save_over.png").createImage());
		ihConferma.setToolTipText("Salva le modifiche");
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				TipiAppuntamentiHelper tah = new TipiAppuntamentiHelper();				
				tah.updateDatiBase(tipiappuntamenti);
				tipiappuntamenti = null;
				tvTipiAppuntamenti.setInput(new Object());
				tvTipiAppuntamenti.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihUndo = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihUndo.setImage(Activator.getImageDescriptor("/icons/adept_reinstall.png").createImage());
		ihUndo.setHoverImage(Activator.getImageDescriptor("/icons/adept_reinstall_hover.png").createImage());
		ihUndo.setToolTipText("Ricarica dall'archivio");
		ihUndo.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {				
				tipiappuntamenti = new TipiAppuntamentiDAO().listTipiAppuntamenti();
				tvTipiAppuntamenti.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.setToolTipText("Cancella elemento selezionato");
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				if (tvTipiAppuntamenti.getSelection() != null){
					int count = 0;
					boolean result = true;
					Iterator it = ((StructuredSelection)tvTipiAppuntamenti.getSelection()).iterator();
					TipiAppuntamentiHelper tah = new TipiAppuntamentiHelper();					
					while (it.hasNext()) {
						Tipiappuntamenti taVO = (Tipiappuntamenti)it.next();
						if (tah.deleteTipiAppuntamenti(taVO)){
							tipiappuntamenti.remove(taVO);							
						}						
						
					}
					tipiappuntamenti = null;
					tvTipiAppuntamenti.setInput(new Object());
					tvTipiAppuntamenti.refresh();
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
		tvTipiAppuntamenti = new TableViewer(sectionClient,SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvTipiAppuntamenti.getTable().setHeaderVisible(true);
		tvTipiAppuntamenti.getTable().setLinesVisible(true);
		
		TableColumn tcNome = new TableColumn(tvTipiAppuntamenti.getTable(),SWT.CENTER,0);
		tcNome.setText(DESCRIZIONE);
		tcNome.setWidth(200);
		
		TableViewerColumn tcvNome = new TableViewerColumn(tvTipiAppuntamenti,tcNome);
		tcvNome.setEditingSupport(new EditingSupport(tvTipiAppuntamenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvTipiAppuntamenti.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Tipiappuntamenti)element).getDescrizione();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Tipiappuntamenti){
					((Tipiappuntamenti)(element)).setDescrizione((String)value);
					tvTipiAppuntamenti.refresh();
				}
			}
			
		});
		
		tcvNome.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {				
				return ((Tipiappuntamenti)element).getDescrizione();
			}
			
		});
		
		TableColumn tcOrdine = new TableColumn(tvTipiAppuntamenti.getTable(),SWT.CENTER,1);
		tcOrdine.setText(ORDINE);
		tcOrdine.setWidth(50);
		
		TableViewerColumn tcvOrdine = new TableViewerColumn(tvTipiAppuntamenti,tcOrdine);
		tcvOrdine.setEditingSupport(new EditingSupport(tvTipiAppuntamenti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvTipiAppuntamenti.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return String.valueOf(((Tipiappuntamenti)element).getOrdine());
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Tipiappuntamenti){
					try{
						((Tipiappuntamenti)(element)).setOrdine(Integer.valueOf((String)value));
						tvTipiAppuntamenti.refresh();						
					}catch(Exception cce){
						((Tipiappuntamenti)(element)).setOrdine(0);
						cce.printStackTrace();
					}
				}
			}
			
		});
		
		tcvOrdine.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {				
				return String.valueOf(
										(((Tipiappuntamenti)element).getOrdine() == 0)
										? 1
										: ((Tipiappuntamenti)element).getOrdine()
									 );
			}
			
		});
		
		tvTipiAppuntamenti.setContentProvider(new TipiAppuntamentiContentProvider());
		tvTipiAppuntamenti.setLabelProvider(new TipiAppuntamentiLabelProvider());
		tvTipiAppuntamenti.getTable().setLayoutData(tabella);
		tvTipiAppuntamenti.setInput(new Object());
		section.setClient(sectionClient);
		
	}
	
	private void createClassiEnergeticheSection(){

		Section section = tool.createSection(
				   				form.getBody(), 
				   				Section.DESCRIPTION|Section.TITLE_BAR|
				   				Section.TWISTIE);
		section.addExpansionListener(new ExpansionAdapter(){

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
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
		
		section.setLayoutData(gdtabella);
		section.setText("Classi Energetiche");
		section.setDescription("Classi energetiche in cui catalogare gli immobili");
		
		Composite sectionClient = tool.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		Composite toolbar = new Composite(sectionClient,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihNew = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.setToolTipText("Nuova classe energetica");
		ihNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				Classienergetiche ceVO = oc.newObject(Classienergetiche.class);				
				ceVO.setOrdine(1);
				ceVO.setDescrizione("Nuova classe energetica");
				classienergetiche.add(ceVO);
				tvClassiEnergetiche.setInput(classienergetiche);
				tvClassiEnergetiche.refresh();
				
				TableItem ti = tvClassiEnergetiche.getTable().getItem(tvClassiEnergetiche.getTable().getItemCount()-1);
				Object[] sel = new Object[1];
				sel[0] = ti.getData();

				StructuredSelection ss = new StructuredSelection(sel);
				
				tvClassiEnergetiche.setSelection(ss, true);

				Event ev = new Event();
				ev.item = ti;
				ev.data = ti.getData();
				ev.widget = tvClassiEnergetiche.getTable();
				tvClassiEnergetiche.getTable().notifyListeners(SWT.Selection, ev);
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihConferma = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihConferma.setImage(Activator.getImageDescriptor("/icons/document-save.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/document-save_over.png").createImage());
		ihConferma.setToolTipText("Salva le modifiche");
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				ClassiEnergeticheHelper ceh = new ClassiEnergeticheHelper();							
				ceh.updateDatiBase(classienergetiche);
				classienergetiche = null;
				tvClassiEnergetiche.setInput(new Object());
				tvClassiEnergetiche.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihUndo = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihUndo.setImage(Activator.getImageDescriptor("/icons/adept_reinstall.png").createImage());
		ihUndo.setHoverImage(Activator.getImageDescriptor("/icons/adept_reinstall_hover.png").createImage());
		ihUndo.setToolTipText("Ricarica dall'archivio");
		ihUndo.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {				
				classienergetiche = new ClassiEnergeticheDAO().listClassiEnergetiche();
				tvClassiEnergetiche.refresh();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = tool.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.setToolTipText("Cancella elemento selezionato");
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				
				if (tvClassiEnergetiche.getSelection() != null){
					int count = 0;
					boolean result = true;
					Iterator it = ((StructuredSelection)tvClassiEnergetiche.getSelection()).iterator();
					ClassiEnergeticheHelper ceh = new ClassiEnergeticheHelper();					
					while (it.hasNext()) {
						Classienergetiche ceVO = (Classienergetiche)it.next();
						if (ceh.deleteClasseEnergetica(ceVO)){
							classienergetiche.remove(ceVO);							
						}						
						
					}
					classienergetiche = null;
					tvClassiEnergetiche.setInput(new Object());
					tvClassiEnergetiche.refresh();
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
		tvClassiEnergetiche = new TableViewer(sectionClient,SWT.FULL_SELECTION|SWT.HORIZONTAL|SWT.VERTICAL);
		tvClassiEnergetiche.getTable().setHeaderVisible(true);
		tvClassiEnergetiche.getTable().setLinesVisible(true);
		
		TableColumn tcNome = new TableColumn(tvClassiEnergetiche.getTable(),SWT.CENTER,0);
		tcNome.setText(NOME);
		tcNome.setWidth(200);
		
		TableViewerColumn tcvNome = new TableViewerColumn(tvClassiEnergetiche,tcNome);
		tcvNome.setEditingSupport(new EditingSupport(tvClassiEnergetiche){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvClassiEnergetiche.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Classienergetiche)element).getNome();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Classienergetiche){
					((Classienergetiche)(element)).setNome((String)value);
					tvClassiEnergetiche.refresh();
				}
			}
			
		});
		
		tcvNome.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {				
				return ((Classienergetiche)element).getNome();
			}
			
		});

		TableColumn tcDescrizione = new TableColumn(tvClassiEnergetiche.getTable(),SWT.CENTER,1);
		tcDescrizione.setText(DESCRIZIONE);
		tcDescrizione.setWidth(200);
		
		TableViewerColumn tcvDescrizione = new TableViewerColumn(tvClassiEnergetiche,tcDescrizione);
		tcvDescrizione.setEditingSupport(new EditingSupport(tvClassiEnergetiche){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvClassiEnergetiche.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return ((Classienergetiche)element).getDescrizione();
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Classienergetiche){
					((Classienergetiche)(element)).setDescrizione((String)value);
					tvClassiEnergetiche.refresh();
				}
			}
			
		});
		
		tcvDescrizione.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {				
				return ((Classienergetiche)element).getDescrizione();
			}
			
		});
		
		TableColumn tcOrdine = new TableColumn(tvClassiEnergetiche.getTable(),SWT.CENTER,2);
		tcOrdine.setText(ORDINE);
		tcOrdine.setWidth(50);
		
		TableViewerColumn tcvOrdine = new TableViewerColumn(tvClassiEnergetiche,tcOrdine);
		tcvOrdine.setEditingSupport(new EditingSupport(tvClassiEnergetiche){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvClassiEnergetiche.getTable());
				return tce; 
			}

			@Override
			protected Object getValue(Object element) {				
				return String.valueOf(((Classienergetiche)element).getOrdine());
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Classienergetiche){
					try{
						((Classienergetiche)(element)).setOrdine(Integer.valueOf((String)value));
						tvClassiEnergetiche.refresh();						
					}catch(Exception cce){
						((Classienergetiche)(element)).setOrdine(0);
						cce.printStackTrace();
					}
				}
			}
			
		});
		
		tcvOrdine.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public String getText(Object element) {				
				return String.valueOf(
										(((Classienergetiche)element).getOrdine() == 0)
										? 1
										: ((Classienergetiche)element).getOrdine()
									 );
			}
			
		});
		
		tvClassiEnergetiche.setContentProvider(new ClassiEnergeticheContentProvider());
		tvClassiEnergetiche.setLabelProvider(new ClassiEnergeticheLabelProvider());
		tvClassiEnergetiche.getTable().setLayoutData(tabella);
		tvClassiEnergetiche.setInput(new Object());
		section.setClient(sectionClient);
		
	}

}
