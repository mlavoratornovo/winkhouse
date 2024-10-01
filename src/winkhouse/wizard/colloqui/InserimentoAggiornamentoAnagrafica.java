package winkhouse.wizard.colloqui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.Activator;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.helper.AnagraficheHelper;
import winkhouse.helper.ContattiHelper;
//import winkhouse.model.Anagrafiche;
//import winkhouse.model.Colloquianagrafiche;
//import winkhouse.model.Contatti;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Colloquianagrafiche;
import winkhouse.orm.Contatti;
import winkhouse.orm.Immobili;
import winkhouse.orm.Tipologiecontatti;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.anagrafica.PopUpRicercaAnagrafica;
//import winkhouse.vo.AnagraficheVO;
//import winkhouse.vo.ColloquiAnagraficheVO;
//import winkhouse.vo.ContattiVO;
//import winkhouse.vo.TipologiaContattiVO;
import winkhouse.wizard.ColloquiWizard;



public class InserimentoAggiornamentoAnagrafica extends WizardPage {

	private Composite container = null;
	private TableViewer tvcontatti = null;
	private CheckboxTableViewer tvanagrafica = null;
	private Table tanagrafica = null;
	private String[] desTipologieRecapiti = null;
	private Text tCognome = null;
	private Text tNome = null;	
	private Text tIndirizzo = null;
	Button filtraAnagrafiche = null;
	private Image anagraficaImg = Activator.getImageDescriptor("icons/anagrafica.png").createImage();
	private Image anagraficaImmobileImg = Activator.getImageDescriptor("icons/anagraficaImmobile.png").createImage();
	

	private ArrayList<Anagrafiche> anagrafiche = new ArrayList<Anagrafiche>();
	
	private Comparator<Anagrafiche> cAnagrafiche = new Comparator<Anagrafiche>(){

		@Override
		public int compare(Anagrafiche arg0, Anagrafiche arg1) {
			int returnValue = 0;
			if (arg0.getCodAnagrafica()!=0){				
				if ((arg0.getCodAnagrafica() == arg0.getCodAnagrafica())){
					returnValue = 0;
				}
				if ((arg0.getCodAnagrafica() < arg0.getCodAnagrafica())){
					returnValue = -1;
				}				
				if ((arg0.getCodAnagrafica() > arg0.getCodAnagrafica())){
					returnValue = 1;
				}								
			}else if (arg0.getCodAnagrafica()!=0){
				returnValue = 1;
			}else{
				returnValue = -1;
			}
			return returnValue;
		}
		
	};
	
	private ViewerFilter filtroAnagrafiche = null;
	
	public InserimentoAggiornamentoAnagrafica(String pageName) {
		super(pageName);
	}

	public InserimentoAggiornamentoAnagrafica(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}
	
	private Comparator<Tipologiecontatti> comparatorTipologieContatti = new Comparator<Tipologiecontatti>(){
		@Override
		public int compare(Tipologiecontatti arg0, Tipologiecontatti arg1) {
			if ((arg0 != null) && (arg1 != null)){
				if ((arg0.getDescrizione() == null) && (arg1.getDescrizione() == null)){
					return 0;
				}else if ((arg0.getDescrizione() != null) && (arg1.getDescrizione() == null)){
					return 1;
				}else if ((arg0.getDescrizione() == null) && (arg1.getDescrizione() != null)){ 
					return -1;
				}else{
					return arg0.getDescrizione().compareTo(arg1.getDescrizione());
				}
			}else if ((arg0 == null) && (arg1 != null)){
				return -1;
			}else if ((arg0 != null) && (arg1 == null)){
				return 1;
			}else{
				return 0;
			}				
		}		
	};
		
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
	
	@Override
	public void createControl(Composite parent) {
		
		setTitle(getName());
		fillTipologieRecapiti();
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		container = new Composite(parent,SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(gdFillHV);		
								
		filtraAnagrafiche = new Button(container, SWT.BORDER);
		filtraAnagrafiche.setImage(Activator.getImageDescriptor("/icons/kfind.png").createImage());
		filtraAnagrafiche.setToolTipText("filtra");
		filtraAnagrafiche.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				PopUpRicercaAnagrafica pra = new PopUpRicercaAnagrafica(((Button)e.getSource()).getShell());				
				pra.setCallerObj(getWizard().getContainer().getCurrentPage());
				pra.setSetterMethodName("addAnagrafica");
				pra.open();
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});;
		
		tanagrafica = new Table(container,SWT.FILL|SWT.FULL_SELECTION|SWT.CHECK);		
		tvanagrafica = new CheckboxTableViewer(tanagrafica);
		tvanagrafica.getTable().setLayoutData(gdFillHV);
		tvanagrafica.getTable().setHeaderVisible(true);
		tvanagrafica.getTable().setLinesVisible(true);

		TableColumn tcCheck = new TableColumn(tvanagrafica.getTable(),SWT.CENTER,0);
		tcCheck.setWidth(20);
		tcCheck.setText("");		

		TableColumn tcImage = new TableColumn(tvanagrafica.getTable(),SWT.CENTER,1);
		tcImage.setWidth(20);
		tcImage.setText("");		
		
		TableColumn tcCognome = new TableColumn(tvanagrafica.getTable(),SWT.CENTER,2);
		tcCognome.setWidth(100);
		tcCognome.setText("Cognome");
		
		TableViewerColumn tvcCognome = new TableViewerColumn(tvanagrafica,tcCognome);
		tvcCognome.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				cell.setText((((Anagrafiche)cell.getElement()).getCognome() == null)
							 ? ""
							 : ((Anagrafiche)cell.getElement()).getCognome());				
			}
			
		});
		tvcCognome.setEditingSupport(new EditingSupport(tvanagrafica){

			@Override
			protected boolean canEdit(Object element) {
				return (((Anagrafiche)element).getCodAnagrafica() == 0);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvanagrafica.getTable());												
				return tce;
			}

			@Override
			protected Object getValue(Object element) {
				return (((Anagrafiche)element).getCognome() == null)
						? ""
						:((Anagrafiche)element).getCognome();
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Anagrafiche)element).setCognome((String)value);
				tvanagrafica.refresh();
			}
			
		});
		
		
		TableColumn tcNome = new TableColumn(tvanagrafica.getTable(),SWT.CENTER,3);
		tcNome.setWidth(100);
		tcNome.setText("Nome");

		TableViewerColumn tvcNome = new TableViewerColumn(tvanagrafica,tcNome);
		tvcNome.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				cell.setText((((Anagrafiche)cell.getElement()).getNome() == null)
							 ? ""
							 : ((Anagrafiche)cell.getElement()).getNome());				
			}
			
		});
		tvcNome.setEditingSupport(new EditingSupport(tvanagrafica){

			@Override
			protected boolean canEdit(Object element) {
				return (((Anagrafiche)element).getCodAnagrafica() == 0);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvanagrafica.getTable());												
				return tce;
			}

			@Override
			protected Object getValue(Object element) {
				return (((Anagrafiche)element).getNome() == null)
						? ""
						: ((Anagrafiche)element).getNome();
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Anagrafiche)element).setNome((String)value);
				tvanagrafica.refresh();
			}
			
		});
				
		TableColumn tcIndirizzo = new TableColumn(tvanagrafica.getTable(),SWT.CENTER,4);
		tcIndirizzo.setWidth(100);
		tcIndirizzo.setText("Indirizzo");
		
		TableViewerColumn tvcIndirizzo = new TableViewerColumn(tvanagrafica,tcIndirizzo);
		tvcIndirizzo.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				cell.setText((((Anagrafiche)cell.getElement()).getIndirizzo() == null)
							 ? ""
							 : ((Anagrafiche)cell.getElement()).getIndirizzo());				
			}
			
		});
		tvcIndirizzo.setEditingSupport(new EditingSupport(tvanagrafica){

			@Override
			protected boolean canEdit(Object element) {
				return (((Anagrafiche)element).getCodAnagrafica() == 0);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvanagrafica.getTable());												
				return tce;
			}

			@Override
			protected Object getValue(Object element) {
				return (((Anagrafiche)element).getIndirizzo() == null)
						? ""
						: ((Anagrafiche)element).getIndirizzo();
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Anagrafiche)element).setIndirizzo((String)value);
				tvanagrafica.refresh();
			}
			
		});
		
		tvanagrafica.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				
				if (anagrafiche == null){
					anagrafiche = new ArrayList<Anagrafiche>();
				}
				return anagrafiche.toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			
		});
		
		tvanagrafica.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				if (columnIndex == 1){
					if(((Anagrafiche)element).getImmobilis().size() > 0){
						return anagraficaImmobileImg;
					}else{
						return anagraficaImg;
					}
				}else{
					return null;
				}
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				switch (columnIndex){
				case 2: return (((Anagrafiche)element).getCognome() == null)
							   ? ""
							   : ((Anagrafiche)element).getCognome();
				
				case 3: return (((Anagrafiche)element).getNome() == null)
				   			   ? ""
				   			   : ((Anagrafiche)element).getNome();
							   
				case 4: return (((Anagrafiche)element).getIndirizzo() == null)
	   			   			   ? ""
			   			       : ((Anagrafiche)element).getIndirizzo();
				
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
		
		tvanagrafica.addPostSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Object[] selezioni = tvanagrafica.getCheckedElements();				
				
				Anagrafiche aVO = (Anagrafiche)((StructuredSelection)event.getSelection()).getFirstElement();
				
				if (aVO == null){
					if (selezioni.length > 0){
						aVO = (Anagrafiche)selezioni[selezioni.length - 1];
					}
				}
				
				for (int i = 0; i < selezioni.length; i++) {					
										
					Colloquianagrafiche caM_A = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Colloquianagrafiche.class);
					caM_A.setColloqui(((ColloquiWizard)getWizard()).getColloquio());
					caM_A.setAnagrafiche((Anagrafiche)selezioni[i]);					
					
					((ColloquiWizard)getWizard()).getColloquio().addToColloquianagrafiches(caM_A);	
				}
				
				
				if (aVO != null){
					tvcontatti.setInput(aVO.getContattis());
				}else{
					tvcontatti.setInput(new ArrayList());
				}
				((ColloquiWizard)getWizard()).getContainer().updateButtons();
			}
			
		});
		tvanagrafica.setInput(new Object());
		
		Composite cContatti = new Composite(container,SWT.NONE);
		cContatti.setLayout(new GridLayout());
		cContatti.setLayoutData(gdFillHV);
		
		Composite toolbarContatti = new Composite(cContatti,SWT.NONE);
		toolbarContatti.setLayout(new FillLayout(SWT.HORIZONTAL));
		ImageHyperlink ihContattiNew = new ImageHyperlink(toolbarContatti, SWT.WRAP);
		ihContattiNew.setToolTipText("contatti");
		ihContattiNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihContattiNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihContattiNew.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (((StructuredSelection)tvanagrafica.getSelection()).getFirstElement() != null){
					Anagrafiche aVO = (Anagrafiche)((StructuredSelection)tvanagrafica.getSelection()).getFirstElement();
					if (aVO.getCodAnagrafica() != 0){
						Contatti cVO = new Contatti();
						cVO.setAnagrafiche(aVO);	
						aVO.addToContattis(cVO);
						tvcontatti.setInput(aVO.getContattis());
						tvcontatti.refresh();
						TableItem ti = tvcontatti.getTable().getItem(tvcontatti.getTable().getItemCount()-1);
						Object[] sel = new Object[1];
						sel[0] = ti.getData();
	
						StructuredSelection ss = new StructuredSelection(sel);
						
						tvcontatti.setSelection(ss, true);
	
						Event ev = new Event();
						ev.item = ti;
						ev.data = ti.getData();
						ev.widget = tvcontatti.getTable();
						tvcontatti.getTable().notifyListeners(SWT.Selection, ev);
						
						getWizard().getContainer().updateButtons();
					}else{
						MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
								  				  "Attenzione", 
												  "L'anagrafica selezionata non è stato ancora salvato nel database \n " +
												  "eseguirne il salvataggio prima di aggiungere i recapiti");						

					}
				}else{
					MessageBox b = new MessageBox(container.getShell(),SWT.ICON_WARNING);
					b.setText("Attenzione");
					b.setMessage("Selezionare una anagrafica");
					b.open();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihContattiConferma = new ImageHyperlink(toolbarContatti, SWT.WRAP);		
		ihContattiConferma.setImage(Activator.getImageDescriptor("/icons/adept_commit.png").createImage());
		ihContattiConferma.setHoverImage(Activator.getImageDescriptor("/icons/adept_commit_hover.png").createImage());
		ihContattiConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (((StructuredSelection)tvanagrafica.getSelection()).getFirstElement() != null){
					Anagrafiche aVO = (Anagrafiche)((StructuredSelection)tvanagrafica.getSelection()).getFirstElement();
					if ((aVO != null) && aVO.getCodAnagrafica() != 0){
						ContattiHelper ch = new ContattiHelper();						
// TODO convertire in cayenne						ch.updateListaContatti(aVO,null,true);

						getWizard().getContainer().updateButtons();
					}else{
						MessageDialog md = new MessageDialog(
					            getShell(),
					            "Attenzione",
					            null,
					            "Selezionare una anagrafica salvata, \n o salvare l'anagrafica corrente",
					            MessageDialog.WARNING,
					            new String[] { "OK"},
					            0);
						
						md.open();
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

		ImageHyperlink ihContattiUndo = new ImageHyperlink(toolbarContatti, SWT.WRAP);		
		ihContattiUndo.setImage(Activator.getImageDescriptor("/icons/adept_reinstall.png").createImage());
		ihContattiUndo.setHoverImage(Activator.getImageDescriptor("/icons/adept_reinstall_hover.png").createImage());
		ihContattiUndo.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {	
				if (tvanagrafica.getSelection() != null){
					Anagrafiche aVO = (Anagrafiche)((StructuredSelection)tvanagrafica.getSelection()).getFirstElement();
					//aVO.setContatti(null);
					tvcontatti.refresh();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihContattiCancella = new ImageHyperlink(toolbarContatti, SWT.WRAP);		
		ihContattiCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihContattiCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihContattiCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (((StructuredSelection)tvanagrafica.getSelection()).getFirstElement() != null){
					Iterator it = ((StructuredSelection)tvcontatti.getSelection()).iterator();
					Anagrafiche aVO = (Anagrafiche)((StructuredSelection)tvanagrafica.getSelection()).getFirstElement();
					while (it.hasNext()) {
						Contatti cModel = (Contatti)it.next();
						aVO.removeFromContattis(cModel);
					}
					tvcontatti.refresh();
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		tvcontatti = new TableViewer(cContatti,SWT.FILL|SWT.FULL_SELECTION);
		tvcontatti.getTable().setLayoutData(gdFillHV);
		tvcontatti.getTable().setHeaderVisible(true);
		tvcontatti.getTable().setLinesVisible(true);
		
		tvcontatti.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				if (((StructuredSelection)tvanagrafica.getSelection()).getFirstElement() != null){
					Anagrafiche aVO = (Anagrafiche)((StructuredSelection)tvanagrafica.getSelection()).getFirstElement();
					if (aVO != null){
						return aVO.getContattis().toArray();
					}else{
						return null;
					}
				}else{
					return (inputElement instanceof List)
							? ((List)inputElement).toArray()
							: null;
				}
								
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			
		});

		
		TableColumn tcTipologia = new TableColumn(tvcontatti.getTable(),SWT.CENTER,0);
		tcTipologia.setWidth(150);
		tcTipologia.setText("Tipologia recapito");
		
		TableViewerColumn tcvTipologia = new TableViewerColumn(tvcontatti,tcTipologia);
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
		
		tcvTipologia.setEditingSupport(new EditingSupport(tvcontatti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				ComboBoxCellEditor cbce = new ComboBoxCellEditor(tvcontatti.getTable(),
																 desTipologieRecapiti,
																 SWT.READ_ONLY|SWT.DROP_DOWN);												
				return cbce;
			}

			@Override
			protected Object getValue(Object element) {
				Contatti cModel = ((Contatti)element);
				int index = -1; 
			
				if (cModel.getTipologiecontatti() != null){
					
					index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getTipologieContatti(), 
													 cModel.getTipologiecontatti(),
										             comparatorTipologieContatti);
				}
				return index;
			}

			@Override
			protected void setValue(Object element, Object value) {
				Contatti cModel = ((Contatti)element);
				if (((Integer)value).intValue() > -1){
					cModel.setTipologiecontatti(MobiliaDatiBaseCache.getInstance().getTipologieContatti().get((Integer)value));
					tvcontatti.refresh();
				}
			}
			
		});
		
		TableColumn tcContatto = new TableColumn(tvcontatti.getTable(),SWT.CENTER,1);
		tcContatto.setWidth(150);
		tcContatto.setText("Recapito");		
	
		TableViewerColumn tcvContatto = new TableViewerColumn(tvcontatti,tcContatto);
		
		
		
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
		
		tcvContatto.setEditingSupport(new EditingSupport(tvcontatti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(tvcontatti.getTable());
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
				tvcontatti.refresh();
			}
			
		});		
		setControl(container);
	}
	
	@Override
	public boolean canFlipToNextPage() {
		
		ArrayList<Colloquianagrafiche> al = new ArrayList(((ColloquiWizard)getWizard()).getColloquio().getColloquianagrafiches());
		if ((al == null) || (al.size() == 0)){
			return false;
		}
		Iterator<Colloquianagrafiche> it = al.iterator();
		while (it.hasNext()){
			Colloquianagrafiche caM_Ang = it.next();
			if ((caM_Ang == null) || (caM_Ang.getAnagrafiche() == null)){
				return false;
			}else{
				if (caM_Ang.getAnagrafiche().getContattis() != null){
					Iterator<Contatti> ite = caM_Ang.getAnagrafiche().getContattis().iterator();
					while (ite.hasNext()){
						if (ite.next().getCodContatto() == 0){
							return false;
						}
					}
				}
			}
		}
						
		return true;
	}

	public void recheckAnagrafiche(){
		
		List<Colloquianagrafiche> al = ((ColloquiWizard)getWizard()).getColloquio().getColloquianagrafiches();
		
		if (al != null){
			
			Iterator<Colloquianagrafiche> it = al.iterator();
			ArrayList alchecks = new ArrayList();
			
			while (it.hasNext()){
				
				Colloquianagrafiche caM_Ang = it.next();
				
				int index = Collections.binarySearch(anagrafiche, caM_Ang.getAnagrafiche(), cAnagrafiche);
				
				if (index > -1){
					alchecks.add(anagrafiche.get(index));
				}
				
			}
			tvanagrafica.setCheckedElements(alchecks.toArray());
		}
	}

	public void addAnagrafica(Anagrafiche anagrafica){
		if (this.anagrafiche == null){
			this.anagrafiche = new ArrayList<Anagrafiche>();
		}
		//inserire controllo se gi� presente
		this.anagrafiche.add(anagrafica);
		tvanagrafica.refresh();
	}
}
