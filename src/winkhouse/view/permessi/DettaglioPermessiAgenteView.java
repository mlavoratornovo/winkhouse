package winkhouse.view.permessi;

import java.util.ArrayList;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
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
import winkhouse.action.permessi.RefreshPermessiAgente;
import winkhouse.action.permessi.SalvaPermessiAgente;
import winkhouse.model.AgentiModel;
import winkhouse.model.PermessiModel;
import winkhouse.model.PermessiUIModel;
import winkhouse.model.RicercheModel;
import winkhouse.util.CriteriaTableUtilsFactory;
import winkhouse.util.WinkhouseUtils;
import winkhouse.util.WinkhouseUtils.DialogInfo;
import winkhouse.util.WinkhouseUtils.PerspectiveInfo;
import winkhouse.util.WinkhouseUtils.ViewInfo;
import winkhouse.vo.PermessiUIVO;
import winkhouse.vo.RicercheVO;
import winkhouse.wizard.RicercaWizard;

public class DettaglioPermessiAgenteView extends ViewPart {

	public final static String ID = "winkhouse.dettagliopermessiagente";
	
	private FormToolkit ft = null;
	private ScrolledForm f = null;

	private CheckboxTableViewer tvProspettive = null;
	private CheckboxTableViewer tvViste = null;
	
	private TableViewer tvRegole = null;
	private TableViewer tvCriteri = null;
	private Composite contenitore = null;
	private AgentiModel agente = null;
	
	private Label desagente = null;
	
	private SalvaPermessiAgente salvaPermessiAgente = null;
	private RefreshPermessiAgente refreshPermessiAgente = null;
	
	private abstract class CenterImageLabelProvider extends OwnerDrawLabelProvider {
	
		protected void measure(Event event, Object element) {}
	
		protected void paint(Event event, Object element) {
	 
			Image img = getImage(element);
	
			if (img != null) {
				Rectangle bounds = ((TableItem) event.item).getBounds(event.index);
				Rectangle imgBounds = img.getBounds();
				bounds.width /= 2;
				bounds.width -= imgBounds.width / 2;
				bounds.height /= 2;
				bounds.height -= imgBounds.height / 2;
	 
				int x = bounds.width > 0 ? bounds.x + bounds.width : bounds.x;
				int y = bounds.height > 0 ? bounds.y + bounds.height : bounds.y;
	 
				event.gc.drawImage(img, x, y);
			}
		}
	 
		protected abstract Image getImage(Object element);
	}
	
	public DettaglioPermessiAgenteView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		
		ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createScrolledForm(parent);
		f.setExpandVertical(true);
		f.setImage(Activator.getImageDescriptor("/icons/lock.png").createImage());
		f.setText("Dettaglio permessi agente");
		f.getBody().setLayout(new GridLayout());
		
		IToolBarManager mgr = f.getToolBarManager();
		
		salvaPermessiAgente = new SalvaPermessiAgente();
		refreshPermessiAgente = new RefreshPermessiAgente();
		
		mgr.add(salvaPermessiAgente);
		mgr.add(refreshPermessiAgente);
		
		f.updateToolBar();
		desagente = ft.createLabel(f.getBody(), "");
		
		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = false;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;	

		desagente.setLayoutData(gdExpVH);
		sectionProspettiveViewDialog();		
		sectionDati();

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private void sectionProspettiveViewDialog(){

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
		gdExpVH.grabExcessVerticalSpace = false;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;	
		
		GridData gdExpVHTable = new GridData();
		gdExpVHTable.grabExcessHorizontalSpace = true;
		gdExpVHTable.grabExcessVerticalSpace = true;
		gdExpVHTable.horizontalAlignment = SWT.FILL;
		gdExpVHTable.verticalAlignment = SWT.FILL;	
		gdExpVHTable.minimumHeight = 80;
		gdExpVHTable.heightHint = 200;
		
		section.setLayout(new GridLayout());
		section.setLayoutData(gdExpVH);
		section.setText("Prospettive Viste Finestre");
		section.setDescription("Permessi di visualizzazione delle prospettive, delle viste e delle finestre popup");
		
		Composite contenitore = ft.createComposite(section,SWT.NONE);
		contenitore.setLayout(new GridLayout(2,true));
		contenitore.setLayoutData(gdExpVH);
		ft.paintBordersFor(contenitore);
		
		Label l_perspective = ft.createLabel(contenitore, "Prospettive");
		Label l_viewdialogs = ft.createLabel(contenitore, "Viste e Finestre popup");
		
		Table t = new Table(contenitore,SWT.HORIZONTAL|SWT.VERTICAL|SWT.CHECK);		
		tvProspettive = new CheckboxTableViewer(t);
		tvProspettive.getTable().setLayoutData(gdExpVHTable);
		tvProspettive.getTable().setHeaderVisible(true);
		tvProspettive.getTable().setLinesVisible(true);
		
		tvProspettive.addCheckStateListener(new ICheckStateListener() {
			
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				
				if (event.getChecked()){
					PermessiUIModel pum = (PermessiUIModel)event.getElement();
					pum.setSelected(true);
					agente.getPermessiUIPerspectiveModels().add(pum);
				}else{
					agente.getPermessiUIPerspectiveModels().remove((PermessiUIModel)event.getElement());
				}
								
			}
		});
		
		tvProspettive.setContentProvider(new IStructuredContentProvider(){

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof ArrayList){
					return ((ArrayList)inputElement).toArray();
				}
				return null;
			}
			
		});
		
		tvProspettive.setLabelProvider(new ITableLabelProvider() {
			
			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public String getColumnText(Object element, int columnIndex) {
				
			
				if (element instanceof PermessiUIVO){
									
					switch(columnIndex){
						case 0: return WinkhouseUtils.getInstance().getPerspectiveByID(((PermessiUIVO)element).getPerspectiveId()).getDescrizione();
								  
					}
					
				}
				return null;
			}
			
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				if (element instanceof PermessiUIVO){
					
					switch(columnIndex){
						case 0: return WinkhouseUtils.getInstance().getPerspectiveByID(((PermessiUIVO)element).getPerspectiveId()).getImmagine();
								  
					}
					
				}
				return null;
			}
		});
		
		tvProspettive.setCheckStateProvider(new ICheckStateProvider() {
			
			@Override
			public boolean isGrayed(Object element) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isChecked(Object element) {
				// TODO Auto-generated method stub
				return ((PermessiUIModel)element).isSelected();
			}
		});
		
		TableColumn tcDescrizione = new TableColumn(tvProspettive.getTable(),SWT.CENTER,0);
		tcDescrizione.setWidth(200);
		tcDescrizione.setText("Descrizione");

		Table tv = new Table(contenitore,SWT.HORIZONTAL|SWT.VERTICAL|SWT.CHECK);
		tvViste = new CheckboxTableViewer(tv);
		tvViste.getTable().setLayoutData(gdExpVHTable);
		tvViste.getTable().setHeaderVisible(true);
		tvViste.getTable().setLinesVisible(true);
		
		tvViste.addCheckStateListener(new ICheckStateListener() {
			
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				
				PermessiUIModel pum = (PermessiUIModel)event.getElement();				
				if (event.getChecked()){
					pum.setSelected(true);
					if (pum.getType() == WinkhouseUtils.VISTA){
						agente.getPermessiUIViewModels().add(pum);	
					}
					if (pum.getType() == WinkhouseUtils.DIALOG){
						agente.getPermessiUIDialogModels().add(pum);	
					}					
				}else{
					if (pum.getType() == WinkhouseUtils.VISTA){
						agente.getPermessiUIViewModels().remove((PermessiUIModel)event.getElement());	
					}
					if (pum.getType() == WinkhouseUtils.DIALOG){
						agente.getPermessiUIDialogModels().remove((PermessiUIModel)event.getElement());	
					}										
					
				}
								
			}
		});
		
		tvViste.setContentProvider(new IStructuredContentProvider(){

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof ArrayList){
					return ((ArrayList)inputElement).toArray();
				}
				return null;
			}
			
		});
		
		tvViste.setLabelProvider(new ITableLabelProvider() {
			
			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public String getColumnText(Object element, int columnIndex) {
				
			
				if (element instanceof PermessiUIVO){
									
					switch(columnIndex){
						case 0: return  (((PermessiUIVO)element).getViewId() != null)
										? WinkhouseUtils.getInstance().getViewByID(((PermessiUIVO)element).getViewId()).getDescrizione()
										: WinkhouseUtils.getInstance().getDialogByID(((PermessiUIVO)element).getDialogId()).getDescrizione();
								  
					}
					
				}
				return null;
			}
			
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				if (element instanceof PermessiUIVO){
					
					switch(columnIndex){
						case 0: return  (((PermessiUIVO)element).getViewId() != null)
										? WinkhouseUtils.getInstance().getViewByID(((PermessiUIVO)element).getViewId()).getImmagine()
										: WinkhouseUtils.getInstance().getDialogByID(((PermessiUIVO)element).getDialogId()).getImmagine();
					}
					
				}
				return null;
			}
		});

		tvViste.setCheckStateProvider(new ICheckStateProvider() {
			
			@Override
			public boolean isGrayed(Object element) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isChecked(Object element) {
				// TODO Auto-generated method stub
				return ((PermessiUIModel)element).isSelected();
			}
		});

		TableColumn tcDescrizioneDialog = new TableColumn(tvViste.getTable(),SWT.CENTER,0);
		tcDescrizioneDialog.setWidth(250);
		tcDescrizioneDialog.setText("Descrizione");
		
		
		section.setClient(contenitore);

	}	

	private void sectionDati(){

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
		section.setText("Dati");
		section.setDescription("Permessi assegnati sui dati");

		contenitore = ft.createComposite(section,SWT.NONE);
		contenitore.setLayout(new GridLayout());
		contenitore.setLayoutData(gdExpVH);
		
		Composite toolbar = new Composite(contenitore,SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		ImageHyperlink ihNew = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihNew.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihNew.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		ihNew.setToolTipText("Nuovo permesso");
		ihNew.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				
				RicercaWizard wizard = new RicercaWizard(RicercaWizard.PERMESSI);
				wizard.setWiztype(RicercaWizard.PERMESSI);
				wizard.setAgente(agente);
				WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
													   wizard);	
				dialog.setPageSize(400, 300);
//				WinkhouseUtils.getInstance()
//								.setRicercaWiz(wizard);
				dialog.open();

			}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
			
		});

//		ImageHyperlink ihUndo = ft.createImageHyperlink(toolbar, SWT.WRAP);		
//		ihUndo.setImage(Activator.getImageDescriptor("/icons/kfind.png").createImage());
//		ihUndo.setHoverImage(Activator.getImageDescriptor("/icons/kfind_over.png").createImage());
//		ihUndo.setToolTipText("Visualizza risultati permesso");
//		ihUndo.addMouseListener(new MouseListener() {
//			
//			@Override
//			public void mouseUp(MouseEvent e) {
//				StructuredSelection ss = (StructuredSelection)tvRegole.getSelection();
//				if (ss.getFirstElement() != null){
//				}else{
//					MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
//											  "Selezione permesso", 
//											  "Selezionare il permesso di cui visualizzare i dettagli");
//				}
//
//			}
//			
//			@Override
//			public void mouseDown(MouseEvent e) {
//				
//			}
//			
//			@Override
//			public void mouseDoubleClick(MouseEvent e) {
//				
//			}
//		});
		
		ImageHyperlink ihCancella = ft.createImageHyperlink(toolbar, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/edittrash_hover.png").createImage());
		ihCancella.setToolTipText("Cancella elemento selezionato");
		ihCancella.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				StructuredSelection ss = (StructuredSelection)tvRegole.getSelection();
				if (ss.getFirstElement() != null){
					agente.getPermessiModels().remove(ss.getFirstElement());
					tvRegole.refresh();
				}else{
					MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
											  "Selezione elementi da cancellare", 
											  "Selezionare l'elemento da cancellare");
				}
				
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}
		});
		
		ft.paintBordersFor(contenitore);

		tvRegole = new TableViewer(contenitore,SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		tvRegole.getTable().setLayoutData(gdExpVH);
		tvRegole.getTable().setHeaderVisible(true);
		tvRegole.getTable().setLinesVisible(true);
		tvRegole.setContentProvider(new IStructuredContentProvider(){

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
		tvRegole.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				
				return null;
				
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				
				PermessiModel pModel = ((PermessiModel)element);
				switch (columnIndex){
				case 0: return (pModel.getRicercaModel() != null)?pModel.getRicercaModel().getNome():"";					        
				
				case 1: return (pModel.getRicercaModel() != null)?pModel.getRicercaModel().getDescrizione():"";
				
				case 2: if (pModel.getRicercaModel() != null){
					
							switch(pModel.getRicercaModel().getTipo()){
							case RicercheVO.PERMESSI_ANAGRAFICHE: return "Ricerche anagrafiche";
							case RicercheVO.PERMESSI_IMMOBILI: return "Ricerche immobili";
							case RicercheVO.PERMESSI_IMMOBILI_AFFITTI: return "Ricerche affitti";
							default: return "";
							}
						}else{
							return "";
						}
				case 3: return "";
				case 4: return "";
				
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
		tvRegole.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection ss = (StructuredSelection)event.getSelection();
				Object o = ss.getFirstElement();
				if (o != null && o instanceof PermessiModel){
					PermessiModel pm = (PermessiModel)o;
					if (pm.getRicercaModel() != null){
						int oldtipo = pm.getRicercaModel().getTipo(); 
						if (oldtipo == RicercheVO.PERMESSI_IMMOBILI){
							pm.getRicercaModel().setTipo(RicercheVO.RICERCHE_IMMOBILI);
						}
						if (oldtipo == RicercheVO.PERMESSI_ANAGRAFICHE){
							pm.getRicercaModel().setTipo(RicercheVO.RICERCHE_ANAGRAFICHE);
						}
						if (oldtipo == RicercheVO.PERMESSI_IMMOBILI_AFFITTI){
							pm.getRicercaModel().setTipo(RicercheVO.RICERCHE_IMMOBILI_AFFITTI);
						}					
						setCriteriaTv(contenitore, pm.getRicercaModel().getTipo(), pm.getRicercaModel().getCriteri());
						pm.getRicercaModel().setTipo(oldtipo);
					}
				}
				
			}
		});
		
		TableColumn tcDescrizionePermesso = new TableColumn(tvRegole.getTable(),SWT.CENTER,0);
		tcDescrizionePermesso.setWidth(250);
		tcDescrizionePermesso.setText("Descrizione Permesso");

		TableColumn tcNomeRicerca = new TableColumn(tvRegole.getTable(),SWT.CENTER,1);
		tcNomeRicerca.setWidth(250);
		tcNomeRicerca.setText("Nome Ricerca");
		
		TableColumn tcDescrizioneRicerca = new TableColumn(tvRegole.getTable(),SWT.CENTER,2);
		tcDescrizioneRicerca.setWidth(250);
		tcDescrizioneRicerca.setText("Descrizione Ricerca");

		TableColumn tcNot = new TableColumn(tvRegole.getTable(),SWT.CENTER,3);
		tcNot.setWidth(100);
		tcNot.setText("Escludi selezione");
		
		TableViewerColumn tcvNot = new TableViewerColumn(tvRegole, tcNot);
		tcvNot.setEditingSupport(new EditingSupport(tvRegole) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((PermessiModel)element).setIsNot((Boolean)value);
				tvRegole.update(element, null);
				
			}
			
			@Override
			protected Object getValue(Object element) {				
				return ((PermessiModel)element).getIsNot();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		tcvNot.setLabelProvider(new CenterImageLabelProvider(){

			@Override
			public Image getImage(Object element) {
				return (((PermessiModel)element).getIsNot())
						? WinkhouseUtils.CHECK
						: WinkhouseUtils.UNCHECK;			
			}

			
		});
		
		TableColumn tcWrite = new TableColumn(tvRegole.getTable(),SWT.CENTER,4);
		tcWrite.setWidth(70);
		tcWrite.setText("Scrittura");

		TableViewerColumn tcvWrite = new TableViewerColumn(tvRegole, tcWrite);
		tcvWrite.setEditingSupport(new EditingSupport(tvRegole) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((PermessiModel)element).setCanWrite((Boolean)value);
				tvRegole.update(element, null);
			}
			
			@Override
			protected Object getValue(Object element) {				
				return ((PermessiModel)element).getCanWrite();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		tcvWrite.setLabelProvider(new CenterImageLabelProvider(){

			@Override
			public Image getImage(Object element) {
				return (((PermessiModel)element).getCanWrite())
						? WinkhouseUtils.CHECK
						: WinkhouseUtils.UNCHECK;			
			}
						
		});

		
		Label lcriteri = ft.createLabel(contenitore, "Criteri");
		
		section.setClient(contenitore);

	}
	
	protected void setCriteriaTv(Composite parent, int criteriaType, ArrayList criteri){

		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;	

		CriteriaTableUtilsFactory ctuf = new CriteriaTableUtilsFactory();
		if (tvCriteri != null){ 
			tvCriteri.getTable().dispose();
		}
				
		switch (criteriaType){
		case RicercheVO.RICERCHE_IMMOBILI :
										   tvCriteri = ctuf.getSearchImmobiliCriteriaTable(parent,criteri);
										   break;
		case RicercheVO.RICERCHE_ANAGRAFICHE :tvCriteri = null;
											  tvCriteri = ctuf.getSearchAnagraficheCriteriaTable(parent,criteri);
		   									  break;	
		case RicercheVO.RICERCHE_IMMOBILI_AFFITTI :tvCriteri = null;
												   tvCriteri = ctuf.getSearchImmobiliAffittiCriteriaTable(parent,criteri);
		   										   break;	
										   
		}
		
		tvCriteri.getTable().setLayoutData(gdExpVH);
		tvCriteri.getTable().setHeaderVisible(true);
		tvCriteri.getTable().setLinesVisible(true);
		parent.layout(true);
		
		f.getBody().redraw();	
		f.redraw();
		tvCriteri.setInput(criteri);

	}	
	
	public void refreshMe(){
		if (this.agente != null){
			agente.setPermessiDialogUIModels(null);
			agente.setPermessiModels(null);
			agente.setPermessiPerspectiveUIModels(null);
			agente.setPermessiViewUIModels(null);
			setAgente(this.agente);
		}
	}
	
	public void setAgente(AgentiModel agente){
		
		this.agente = agente;
		desagente.setText(this.agente.toString());
		
		ArrayList<PerspectiveInfo> persectiveInfo = WinkhouseUtils.getInstance().getPerspectiveInfo();
		ArrayList<ViewInfo> viewInfo = WinkhouseUtils.getInstance().getViewInfo();
		ArrayList<DialogInfo> dialogInfo = WinkhouseUtils.getInstance().getDialogInfo();
		
		ArrayList<PermessiUIModel> permessiUI_perspective = agente.getPermessiUIPerspectiveModels();
		ArrayList<PermessiUIModel> permessiUI_view = agente.getPermessiUIViewModels();
		ArrayList<PermessiUIModel> permessiUI_dialog = agente.getPermessiUIDialogModels();
		
		for (PerspectiveInfo perspective : persectiveInfo) {			
			boolean find_it = false;
			for (PermessiUIModel permessoUIModel : permessiUI_perspective) {
				permessoUIModel.setType(WinkhouseUtils.PROSPETTIVA);
				if (perspective.getId().equalsIgnoreCase(permessoUIModel.getPerspectiveId())){
					find_it = true;
					permessoUIModel.setSelected(true);
					break;
				}
			}
			if (find_it == false){
				
				PermessiUIModel pUIModel = new PermessiUIModel();
				
				pUIModel.setCodAgente(agente.getCodAgente());
				pUIModel.setPerspectiveId(perspective.getId());
				
				permessiUI_perspective.add(pUIModel);
			}
		}
		
		tvProspettive.setInput(permessiUI_perspective);		
		
		for (ViewInfo view : viewInfo) {
			boolean find_it = false;
			for (PermessiUIModel permessoUIModel : permessiUI_view) {
				permessoUIModel.setType(WinkhouseUtils.VISTA);
				if (view.getId().equalsIgnoreCase(permessoUIModel.getViewId())){
					find_it = true;
					permessoUIModel.setSelected(true);
					break;
				}
			}
			if (find_it == false){
				
				PermessiUIModel pUIModel = new PermessiUIModel();
				
				pUIModel.setCodAgente(agente.getCodAgente());
				pUIModel.setViewId(view.getId());
				
				permessiUI_view.add(pUIModel);
			}				
		}
		for (DialogInfo dialog : dialogInfo) {
			boolean find_it = false;
			for (PermessiUIModel permessoUIModel : permessiUI_dialog) {
				permessoUIModel.setType(WinkhouseUtils.DIALOG);
				if (dialog.getId().equalsIgnoreCase(permessoUIModel.getDialogId())){
					find_it = true;
					permessoUIModel.setSelected(true);
					break;
				}
			}
			if (find_it == false){
				
				PermessiUIModel pUIModel = new PermessiUIModel();
				
				pUIModel.setCodAgente(agente.getCodAgente());
				pUIModel.setDialogId(dialog.getId());
				
				permessiUI_view.add(pUIModel);
			}
		}
		
		tvViste.setInput(permessiUI_view);
		tvRegole.setInput(agente.getPermessiModels());
		
	}

	public AgentiModel getAgente() {
		return agente;
	}

	public void addPermessoDatiByRicerca(RicercheModel ricerca){
		
		PermessiModel pm = new PermessiModel();
		pm.setCodRicerca(ricerca.getCodRicerca());
		pm.setCodAgente(agente.getCodAgente());
		
		agente.getPermessiModels().add(pm);
		tvRegole.refresh();
		
	}

	public TableViewer getTvRegole() {
		return tvRegole;
	}
	
}