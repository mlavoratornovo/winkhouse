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
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.Activator;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.db.ConnectionManager;
import winkhouse.helper.AnagraficheHelper;
import winkhouse.helper.ContattiHelper;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiAnagraficheModel_Ang;
import winkhouse.model.ContattiModel;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.view.anagrafica.PopUpRicercaAnagrafica;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ColloquiAnagraficheVO;
import winkhouse.vo.ContattiVO;
import winkhouse.vo.TipologiaContattiVO;
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
	private Image anagraficaImg = Activator.getDefault().getImageDescriptor("icons/anagrafica.png").createImage();
	private Image anagraficaImmobileImg = Activator.getDefault().getImageDescriptor("icons/anagraficaImmobile.png").createImage();
	

	private ArrayList<AnagraficheModel> anagrafiche = new ArrayList<AnagraficheModel>();
	
	private Comparator<AnagraficheVO> cAnagrafiche = new Comparator<AnagraficheVO>(){

		@Override
		public int compare(AnagraficheVO arg0, AnagraficheVO arg1) {
			int returnValue = 0;
			if ((arg0.getCodAnagrafica()!=null) && (arg0.getCodAnagrafica()!=null)){				
				if ((arg0.getCodAnagrafica().intValue() == arg0.getCodAnagrafica().intValue())){
					returnValue = 0;
				}
				if ((arg0.getCodAnagrafica().intValue() < arg0.getCodAnagrafica().intValue())){
					returnValue = -1;
				}				
				if ((arg0.getCodAnagrafica().intValue() > arg0.getCodAnagrafica().intValue())){
					returnValue = 1;
				}								
			}else if ((arg0.getCodAnagrafica()!=null) && (arg0.getCodAnagrafica()==null)){
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

	private Comparator<TipologiaContattiVO> comparatorTipologieContatti = new Comparator<TipologiaContattiVO>(){
		@Override
		public int compare(TipologiaContattiVO arg0, TipologiaContattiVO arg1) {
			if ((arg0 != null) && (arg1 != null)){
				if ((arg0.getDescrizione() != null) && (arg1.getDescrizione() != null)){
					return arg0.getDescrizione().compareTo(arg1.getDescrizione());
				}else if ((arg0.getDescrizione() == null) && (arg1.getDescrizione() != null)){
					return 1;
				}else if ((arg0.getDescrizione() != null) && (arg1.getDescrizione() == null)){
					return -1;
				}else{
					return 0;
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
		Iterator<TipologiaContattiVO> it = MobiliaDatiBaseCache.getInstance().getTipologieContatti().iterator();
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
				cell.setText((((AnagraficheVO)cell.getElement()).getCognome() == null)
							 ? ""
							 : ((AnagraficheVO)cell.getElement()).getCognome());				
			}
			
		});
		tvcCognome.setEditingSupport(new EditingSupport(tvanagrafica){

			@Override
			protected boolean canEdit(Object element) {
				return (((AnagraficheVO)element).getCodAnagrafica() == null);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvanagrafica.getTable());												
				return tce;
			}

			@Override
			protected Object getValue(Object element) {
				return (((AnagraficheVO)element).getCognome() == null)
						? ""
						:((AnagraficheVO)element).getCognome();
			}

			@Override
			protected void setValue(Object element, Object value) {
				((AnagraficheVO)element).setCognome((String)value);
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
				cell.setText((((AnagraficheVO)cell.getElement()).getNome() == null)
							 ? ""
							 : ((AnagraficheVO)cell.getElement()).getNome());				
			}
			
		});
		tvcNome.setEditingSupport(new EditingSupport(tvanagrafica){

			@Override
			protected boolean canEdit(Object element) {
				return (((AnagraficheVO)element).getCodAnagrafica() == null);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvanagrafica.getTable());												
				return tce;
			}

			@Override
			protected Object getValue(Object element) {
				return (((AnagraficheVO)element).getNome() == null)
						? ""
						: ((AnagraficheVO)element).getNome();
			}

			@Override
			protected void setValue(Object element, Object value) {
				((AnagraficheVO)element).setNome((String)value);
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
				cell.setText((((AnagraficheVO)cell.getElement()).getIndirizzo() == null)
							 ? ""
							 : ((AnagraficheVO)cell.getElement()).getIndirizzo());				
			}
			
		});
		tvcIndirizzo.setEditingSupport(new EditingSupport(tvanagrafica){

			@Override
			protected boolean canEdit(Object element) {
				return (((AnagraficheVO)element).getCodAnagrafica() == null);
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				TextCellEditor tce = new TextCellEditor(tvanagrafica.getTable());												
				return tce;
			}

			@Override
			protected Object getValue(Object element) {
				return (((AnagraficheVO)element).getIndirizzo() == null)
						? ""
						: ((AnagraficheVO)element).getIndirizzo();
			}

			@Override
			protected void setValue(Object element, Object value) {
				((AnagraficheVO)element).setIndirizzo((String)value);
				tvanagrafica.refresh();
			}
			
		});
		
		tvanagrafica.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				
				if (anagrafiche == null){
					anagrafiche = new ArrayList<AnagraficheModel>();
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
					if(((AnagraficheModel)element).getImmobili().size() > 0){
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
				case 2: return (((AnagraficheVO)element).getCognome() == null)
							   ? ""
							   : ((AnagraficheVO)element).getCognome();
				
				case 3: return (((AnagraficheVO)element).getNome() == null)
				   			   ? ""
				   			   : ((AnagraficheVO)element).getNome();
							   
				case 4: return (((AnagraficheVO)element).getIndirizzo() == null)
	   			   			   ? ""
			   			       : ((AnagraficheVO)element).getIndirizzo();
				
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
				ArrayList<ColloquiAnagraficheModel_Ang> colloquiAngrafiche = new ArrayList<ColloquiAnagraficheModel_Ang>();
				
				AnagraficheModel aVO = (AnagraficheModel)((StructuredSelection)event.getSelection()).getFirstElement();
				
				if (aVO == null){
					if (selezioni.length > 0){
						aVO = (AnagraficheModel)selezioni[selezioni.length - 1];
					}
				}
				
				for (int i = 0; i < selezioni.length; i++) {					
										
					ColloquiAnagraficheModel_Ang caM_A = new ColloquiAnagraficheModel_Ang();
					caM_A.setCodColloquio(((ColloquiWizard)getWizard()).getColloquio().getCodColloquio());
					caM_A.setAnagrafica((AnagraficheModel)selezioni[i]);					
					colloquiAngrafiche.add(caM_A);
					
				}
				((ColloquiWizard)getWizard()).getColloquio().setAnagrafiche(colloquiAngrafiche);
				if (aVO != null){
					tvcontatti.setInput(aVO.getContatti());
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
					AnagraficheModel aVO = (AnagraficheModel)((StructuredSelection)tvanagrafica.getSelection()).getFirstElement();
					if (aVO.getCodAnagrafica() != null && aVO.getCodAnagrafica() != 0){
						ContattiModel cVO = new ContattiModel();
						cVO.setCodAnagrafica(aVO.getCodAnagrafica());	
						if (aVO.getContatti() == null){
							aVO.setContatti(new ArrayList());
						}
						aVO.getContatti().add(cVO);
						tvcontatti.setInput(aVO.getContatti());
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
						MessageDialog.openWarning(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
								  				  "Attenzione", 
												  "L'anagrafica selezionata non � stato ancora salvato nel database \n " +
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
					AnagraficheModel aVO = (AnagraficheModel)((StructuredSelection)tvanagrafica.getSelection()).getFirstElement();
					if ((aVO != null) && (aVO.getCodAnagrafica() != null && aVO.getCodAnagrafica() != 0)){
						ContattiHelper ch = new ContattiHelper();						
						ch.updateListaContatti(aVO,null,true);
						//aVO.setContatti(contatti);
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
					AnagraficheModel aVO = (AnagraficheModel)((StructuredSelection)tvanagrafica.getSelection()).getFirstElement();
					aVO.setContatti(null);
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
					AnagraficheModel aVO = (AnagraficheModel)((StructuredSelection)tvanagrafica.getSelection()).getFirstElement();
					while (it.hasNext()) {
						ContattiModel cModel = (ContattiModel)it.next();
						aVO.getContatti().remove(cModel);
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
					AnagraficheModel aVO = (AnagraficheModel)((StructuredSelection)tvanagrafica.getSelection()).getFirstElement();
					if (aVO != null){
						return aVO.getContatti().toArray();
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
				ContattiModel cModel = ((ContattiModel)cell.getElement());
				cell.setText((cModel.getTipologia() == null)
							 ? ""
							 : (cModel.getTipologia().getDescrizione() == null)
							 	? ""
							 	: cModel.getTipologia().getDescrizione());
				
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
				ContattiModel cModel = ((ContattiModel)element);
				int index = -1; 
			
				if (cModel.getTipologia() != null){
					
					index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getTipologieContatti(), 
													 cModel.getTipologia(),
										             comparatorTipologieContatti);
				}
				return index;
			}

			@Override
			protected void setValue(Object element, Object value) {
				ContattiModel cModel = ((ContattiModel)element);
				if (((Integer)value).intValue() > -1){
					cModel.setTipologia(MobiliaDatiBaseCache.getInstance()
															.getTipologieContatti()
															.get((Integer)value));
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
						(((ContattiModel)cell.getElement()).getContatto()==null)
						? "nuovo recapito"
						: String.valueOf(((ContattiModel)cell.getElement()).getContatto())
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
				return (((ContattiVO)element).getContatto() == null)
	   			   	   ? "nuovo recapito"
			   		   : String.valueOf(((ContattiVO)element).getContatto());
			}

			@Override
			protected void setValue(Object element, Object value) {
				((ContattiVO)element).setContatto(String.valueOf(value));
				tvcontatti.refresh();
			}
			
		});		
		setControl(container);
	}
	
	@Override
	public boolean canFlipToNextPage() {
		
		ArrayList<ColloquiAnagraficheModel_Ang> al = ((ColloquiWizard)getWizard()).getColloquio().getAnagrafiche();
		if ((al == null) || (al.size() == 0)){
			return false;
		}
		Iterator<ColloquiAnagraficheModel_Ang> it = al.iterator();
		while (it.hasNext()){
			ColloquiAnagraficheModel_Ang caM_Ang = it.next();
			if ((caM_Ang == null) || (caM_Ang.getCodAnagrafica() == null)){
				return false;
			}else{
				if (caM_Ang.getAnagrafica().getContatti() != null){
					Iterator<ContattiVO> ite = caM_Ang.getAnagrafica().getContatti().iterator();
					while (ite.hasNext()){
						if (ite.next().getCodContatto() == null){
							return false;
						}
					}
				}
			}
		}
						
		return true;
	}

	public void recheckAnagrafiche(){
		
		List<ColloquiAnagraficheModel_Ang> al = ((ColloquiWizard)getWizard()).getColloquio().getAnagrafiche();
		
		if (al != null){
			
			Iterator<ColloquiAnagraficheModel_Ang> it = al.iterator();
			ArrayList alchecks = new ArrayList();
			
			while (it.hasNext()){
				
				ColloquiAnagraficheModel_Ang caM_Ang = it.next();
				
				int index = Collections.binarySearch(anagrafiche, caM_Ang.getAnagrafica(), cAnagrafiche);
				
				if (index > -1){
					alchecks.add(anagrafiche.get(index));
				}
				
			}
			tvanagrafica.setCheckedElements(alchecks.toArray());
		}
	}

	public void addAnagrafica(AnagraficheModel anagrafica){
		if (this.anagrafiche == null){
			this.anagrafiche = new ArrayList<AnagraficheModel>();
		}
		//inserire controllo se gi� presente
		this.anagrafiche.add(anagrafica);
		tvanagrafica.refresh();
	}
}
