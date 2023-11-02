package winkhouse.view.desktop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.PojoProperties;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
//import org.eclipse.core.databinding.beans.PojoObservables;
//import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICheckStateListener;
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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import winkhouse.Activator;
import winkhouse.action.desktop.SavePromemoriaAction;
import winkhouse.dialogs.custom.FileDialogCellEditor;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.PromemoriaModel;
import winkhouse.model.PromemoriaOggettiModel;
import winkhouse.util.MobiliaDatiBaseCache;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.PromemoriaLinksVO;
import winkhouse.wizard.RicercaWizard;

public class PopUpDettaglioPromemoria {
	
	private Shell popup = null;
	private ComboViewer cAgente = null;
	private Text descrizioneMemo = null;
	private DesktopView dv = null;
	private SavePromemoriaAction spa = null; 
	private PromemoriaModel promemoria = null; 
	private Image immobile = Activator.getImageDescriptor("icons/gohome.png").createImage();
	private Image anagrafica = Activator.getImageDescriptor("icons/anagrafica_16.png").createImage();
	private Image CHECKED = Activator.getImageDescriptor("icons/chkall.png").createImage();
	private Image UNCHECKED = Activator.getImageDescriptor("icons/chknone.png").createImage();
	private CheckboxTableViewer tvlink = null;
	private TableViewer tvfilelink = null;
	private String regurl = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
	private String regurlnohttp = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";
	
	private Comparator<AgentiVO> comparatorAgenti = new Comparator<AgentiVO>(){

		@Override
		public int compare(AgentiVO arg0,AgentiVO arg1) {
			if (arg0 != null && arg1 != null && arg0.getCodAgente() != null && arg1.getCodAgente()!= null){
				if (arg0.getCodAgente().intValue() == arg1.getCodAgente().intValue()){
					return 0;
				}else if (arg0.getCodAgente().intValue() > arg1.getCodAgente().intValue()){
					return 1;
				}else{
					return -1;
				}
			}else{
				return 0;
			}
		}
		
	};
	
	public PopUpDettaglioPromemoria() {}
	
	public PopUpDettaglioPromemoria(DesktopView dv){
		this.dv = dv;
		createContent();
	} 
	
	private void createContent(){
		
		popup = new Shell(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
				  		  SWT.APPLICATION_MODAL|SWT.SHELL_TRIM);
		
		popup.setText("Dettaglio promemoria");
		popup.setImage(Activator.getDefault()
								.getImageDescriptor("icons/kontact_notes.png").createImage());
		popup.setBackground(new Color(null,255,255,255));
		popup.setSize(300, 300);
		
		GridLayout gl = new GridLayout();
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridData gdH = new GridData(SWT.FILL, SWT.FILL, true, false);
		GridData gdHR = new GridData(SWT.RIGHT, SWT.FILL, true, false);
		
		popup.setLayout(gl);
		popup.setLayoutData(gd);
		
		FormToolkit ft = new FormToolkit(popup.getDisplay());		
		Form f = ft.createForm(popup);
				
		f.getBody().setLayout(gl);
		f.getBody().setLayoutData(gd);
		f.setLayout(gl);
		f.setLayoutData(gd);

		spa = new SavePromemoriaAction(this);
		
		CTabFolder tabFolder = new CTabFolder(f.getBody(), SWT.TOP);
		tabFolder.setLayoutData(gd);
		getMainTab(tabFolder,ft,f);
		getLinkTab(tabFolder,ft,f);
		getFileUrlTab(tabFolder, ft, f);
		tabFolder.pack();

		Button bsalva = ft.createButton(f.getBody(), "", SWT.FLAT);
		bsalva.setBackground(new Color(null,255,255,255));
		bsalva.setImage(spa.getImageDescriptor().createImage());
		bsalva.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				spa.run();				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});

		ft.paintBordersFor(f.getBody());
		
		popup.open();
	}
	
	protected void bindDatiPromemoria(){
		
		DataBindingContext bindingContext = new DataBindingContext();
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(descrizioneMemo), 
								 PojoProperties.value("descrizione").observe(promemoria),
								 null, 
								 null);

		
		cAgente.setContentProvider(new IStructuredContentProvider(){

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
		
		cAgente.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				return ((AgentiVO)element).getCognome() + " " + ((AgentiVO)element).getNome();
			}
			
		});
		
		cAgente.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				promemoria.setAgente(((AgentiVO)((StructuredSelection)event.getSelection()).getFirstElement()));				
			}
			
		});
						
		cAgente.setInput(new Object());
		
		if (promemoria.getCodAgente() != null && promemoria.getCodAgente() != 0){
			int index = Collections.binarySearch(MobiliaDatiBaseCache.getInstance().getAgenti(), promemoria.getAgente(), comparatorAgenti);
			Object[] sel = new Object[1];
			sel[0] = MobiliaDatiBaseCache.getInstance().getAgenti().get(index);
			StructuredSelection ss = new StructuredSelection(sel);
			cAgente.setSelection(ss);
		}
		
		tvlink.setInput(promemoria);
		
	}

	public PromemoriaModel getPromemoria() {
		return promemoria;
	}

	public void setPromemoria(PromemoriaModel promemoria) {
		this.promemoria = promemoria;
		bindDatiPromemoria();
	}

	public void closeMe(){
		
		dv.setAgente(dv.getAgente());
		popup.close();
		
	}
	
	private void getMainTab(CTabFolder tabFolder,FormToolkit ft,Form f){
		
		CTabItem returnValue = null;
		
		GridLayout gl = new GridLayout();
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridData gdH = new GridData(SWT.FILL, SWT.FILL, true, false);
		GridData gdHR = new GridData(SWT.RIGHT, SWT.FILL, true, false);
		
		returnValue = new CTabItem (tabFolder, SWT.NONE);
		returnValue.setText ("Dati promemoria");
		
		Composite c = ft.createComposite(tabFolder);
		c.setLayout(gl);
		c.setLayoutData(gdHR);
				
		Label l_agente = ft.createLabel(c, "Agente");
		cAgente = new ComboViewer(c,SWT.DROP_DOWN | SWT.READ_ONLY | SWT.DOUBLE_BUFFERED);
		cAgente.getCombo().setLayoutData(gdH);
		
		Label l_promemoria = ft.createLabel(c, "Promemoria");		
		descrizioneMemo = ft.createText(c, "", SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL);
		descrizioneMemo.setLayoutData(gd);
		
		ft.paintBordersFor(c);

		returnValue.setControl (c);
		
	}
	
	private void getLinkTab(CTabFolder tabFolder,FormToolkit ft,Form f){
		
		CTabItem returnValue = null;
		
		GridLayout gl = new GridLayout();
		GridLayout gl2 = new GridLayout();
		gl2.numColumns = 2;
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridData gdH = new GridData(SWT.FILL, SWT.FILL, true, false);
		GridData gdHR = new GridData(SWT.RIGHT, SWT.FILL, true, false);
		
		returnValue = new CTabItem (tabFolder, SWT.NONE);
		returnValue.setText ("Anagrafiche/Immobili");
		
		Composite c = ft.createComposite(tabFolder);
		c.setLayout(gl);
		c.setLayoutData(gdHR);

		Composite ct = ft.createComposite(c);
		ct.setLayout(gl2);
		ct.setLayoutData(gdH);
		
		Button bcerca = ft.createButton(ct, "", SWT.FLAT);
		bcerca.setBackground(new Color(null,255,255,255));
		bcerca.setImage(Activator.getImageDescriptor("icons/kfind.png").createImage());
		
		bcerca.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				RicercaWizard wizard = new RicercaWizard(RicercaWizard.PROMEMORIA);
				wizard.setWiztype(RicercaWizard.PROMEMORIA);
				wizard.setReturnObject(PopUpDettaglioPromemoria.this);
				wizard.setReturnObjectMethodName("addLinkedObject");
				wizard.setReturnType(ArrayList.class);
				WizardDialog dialog = new WizardDialog(window.getShell(), wizard);	
				dialog.setPageSize(400, 300);
				WinkhouseUtils.getInstance()
							  .setRicercaWiz(wizard);
				dialog.open();				
				
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
		
		Button bcancella = ft.createButton(ct, "", SWT.FLAT);
		bcancella.setBackground(new Color(null,255,255,255));
		bcancella.setImage(Activator.getImageDescriptor("icons/button_cancel.png").createImage());
		bcancella.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				for (Object iterable_element : tvlink.getCheckedElements()) {
					promemoria.getLinkedObjects().remove(iterable_element);											
				} 
				tvlink.setInput(promemoria);
			}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});
		
		Table t = new Table(c,SWT.HORIZONTAL|SWT.VERTICAL|SWT.CHECK);		
		tvlink = new CheckboxTableViewer(t);
		tvlink.getTable().setLayoutData(gd);
		tvlink.getTable().setHeaderVisible(true);
		tvlink.getTable().setLinesVisible(true);
		
		tvlink.addCheckStateListener(new ICheckStateListener() {
			
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				
				if (event.getChecked()){
				}else{
				}
								
			}
		});
		
		tvlink.setContentProvider(new IStructuredContentProvider(){

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
				if (inputElement instanceof PromemoriaModel){
					return ((PromemoriaModel)inputElement).getLinkedObjects().toArray();
				}
				return null;
			}
			
		});
		
		tvlink.setLabelProvider(new ITableLabelProvider() {
			
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
							
				return ((PromemoriaOggettiModel)element).getLinkedObject().toString();
			}
			
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				
				if (((PromemoriaOggettiModel)element).getTipo() == RicercaWizard.IMMOBILI){
					return immobile; 
				}
				if (((PromemoriaOggettiModel)element).getTipo() == RicercaWizard.ANAGRAFICHE){
					return anagrafica; 
				}
				
				return null;
			}
		});
		
		TableColumn tcDescrizione = new TableColumn(tvlink.getTable(),SWT.CENTER,0);
		tcDescrizione.setWidth(240);
		tcDescrizione.setText("Descrizione");		
		
		ft.paintBordersFor(c);

		returnValue.setControl (c);
		
	}
	
	private void getFileUrlTab(CTabFolder tabFolder,FormToolkit ft,Form f){
		
		CTabItem returnValue = null;
		
		GridLayout gl = new GridLayout();
		GridLayout gl2 = new GridLayout();
		gl2.numColumns = 2;
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridData gdH = new GridData(SWT.FILL, SWT.FILL, true, false);
		GridData gdHR = new GridData(SWT.RIGHT, SWT.FILL, true, false);
		
		returnValue = new CTabItem (tabFolder, SWT.NONE);
		returnValue.setText ("File/Url");
		
		Composite c = ft.createComposite(tabFolder);
		c.setLayout(gl);
		c.setLayoutData(gdHR);

		Composite ct = ft.createComposite(c);
		ct.setLayout(gl2);
		ct.setLayoutData(gdH);
		
		Button bcerca = ft.createButton(ct, "", SWT.FLAT);
		bcerca.setBackground(new Color(null,255,255,255));
		bcerca.setImage(Activator.getImageDescriptor("icons/filenew.png").createImage());
		
		bcerca.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				
				if (getPromemoria().getLinksUrls() == null){
					
					ArrayList<PromemoriaLinksVO> plVOs = new ArrayList<PromemoriaLinksVO>();
					PromemoriaLinksVO plvo = new PromemoriaLinksVO();
					plvo.setCodPromemoria(getPromemoria().getCodPromemoria());
					plVOs.add(plvo);
					getPromemoria().setLinksUrls(plVOs);
					tvfilelink.setInput(promemoria);
					
				}else{
					
					PromemoriaLinksVO plvo = new PromemoriaLinksVO();
					plvo.setCodPromemoria(getPromemoria().getCodPromemoria());
					getPromemoria().getLinksUrls().add(plvo);
					tvfilelink.setInput(promemoria);
					
				}
				
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
		
		Button bcancella = ft.createButton(ct, "", SWT.FLAT);
		bcancella.setBackground(new Color(null,255,255,255));
		bcancella.setImage(Activator.getImageDescriptor("icons/button_cancel.png").createImage());
		bcancella.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				if (!((StructuredSelection)tvfilelink.getSelection()).isEmpty()){
					Iterator it = ((StructuredSelection)tvfilelink.getSelection()).iterator();
					while(it.hasNext()){
						promemoria.getLinksUrls().remove((PromemoriaLinksVO)it.next());						
					}
					tvfilelink.refresh(true, false);
				}else{
					MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
											  "Cancellazione link/url",
											  "Selezionare almeno un elemento da cancellare");
				}
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});
		
		Table t = new Table(c,SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);		
		tvfilelink = new TableViewer(t);
		tvfilelink.getTable().setLayoutData(gd);
		tvfilelink.getTable().setHeaderVisible(true);
		tvfilelink.getTable().setLinesVisible(true);
		
//		tvfilelink.addCheckStateListener(new ICheckStateListener() {
//			
//			@Override
//			public void checkStateChanged(CheckStateChangedEvent event) {
//				
//				if (event.getChecked()){
//				}else{
//				}
//								
//			}
//		});
		
		tvfilelink.setContentProvider(new IStructuredContentProvider(){

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
				if (inputElement instanceof PromemoriaModel){
					return ((PromemoriaModel)inputElement).getLinksUrls().toArray();
				}
				return null;
			}
			
		});
		
//		tvfilelink.setLabelProvider(new ITableLabelProvider() {
//			
//			@Override
//			public void removeListener(ILabelProviderListener listener) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public boolean isLabelProperty(Object element, String property) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public void dispose() {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void addListener(ILabelProviderListener listener) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public String getColumnText(Object element, int columnIndex) {
//				if (columnIndex == 1){
//					return ((PromemoriaLinksVO)element).getUrilink();
//				}
//				return "";
//			}
//
//			@Override
//			public Image getColumnImage(Object element, int columnIndex) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//		});


		
		TableColumn tcisFile = new TableColumn(tvfilelink.getTable(),SWT.CENTER,0);
		tcisFile.setWidth(40);
		tcisFile.setText("File");
		
		TableViewerColumn tcvisFile = new TableViewerColumn(tvfilelink,tcisFile);
		tcvisFile.setLabelProvider(new ColumnLabelProvider() {			
			
			@Override
			public Image getImage(Object element) {

				return (((PromemoriaLinksVO)element).getIsFile())
					   ? CHECKED
					   : UNCHECKED;
			}

			@Override
			public String getText(Object element) {
				return "";
			}
			
		});
		tcvisFile.setEditingSupport(new EditingSupport(tvfilelink) {
			
			@Override
			protected void setValue(Object element, Object value) {				
				((PromemoriaLinksVO)element).setIsFile((Boolean)value);
				tvfilelink.update(element, null);
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((PromemoriaLinksVO)element).getIsFile();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {	
				return new CheckboxCellEditor(tvfilelink.getTable(),SWT.CHECK);
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});

		TableColumn tcDescrizione = new TableColumn(tvfilelink.getTable(),SWT.CENTER,1);
		tcDescrizione.setWidth(200);
		tcDescrizione.setText("File/Url");		
		
		TableViewerColumn tvcdescrizone = new TableViewerColumn(tvfilelink, tcDescrizione);
		tvcdescrizone.setLabelProvider(new ColumnLabelProvider() {			
			
			@Override
			public String getText(Object element) {
				return ((PromemoriaLinksVO)element).getUrilink();
			}
			
		});
		
		tvcdescrizone.setEditingSupport(new EditingSupport(tvfilelink) {
			
			@Override
			protected void setValue(Object element, Object value) {
				((PromemoriaLinksVO)element).setUrilink(String.valueOf(value));	
				tvfilelink.update(element, null);
			}
			
			@Override
			protected Object getValue(Object element) {
				return ((PromemoriaLinksVO)element).getUrilink();
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				if (!((PromemoriaLinksVO)element).getIsFile()){
					return new TextCellEditor(tvfilelink.getTable());
				}else{
					FileDialogCellEditor fdce = new FileDialogCellEditor(tvfilelink.getTable());
					fdce.setButtonImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
					fdce.setTootTipButton("Seleziona documento");
					return fdce;
				}
				
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;				
			}
		});
		
		ft.paintBordersFor(c);

		returnValue.setControl (c);
		
	}
	
	public void addLinkedObject(ArrayList linkedObjects){
		
		for (Object object : linkedObjects) {
			
			boolean findit = false;
			
			Integer immobili_match_cod = null;
			Integer anagrafica_match_cod = null;
			
			if (object instanceof ImmobiliModel){
				immobili_match_cod = ((ImmobiliModel)object).getCodImmobile();
			}
			
			if (object instanceof AnagraficheModel){
				anagrafica_match_cod = ((AnagraficheModel)object).getCodAnagrafica();
			}
			
			for (PromemoriaOggettiModel objectmodel : promemoria.getLinkedObjects()) {
				if (((PromemoriaOggettiModel)objectmodel).getTipo() == RicercaWizard.IMMOBILI){
					if (((ImmobiliModel)((PromemoriaOggettiModel)objectmodel).getLinkedObject()).getCodImmobile() == immobili_match_cod){
						findit = true;
						break;
					}
				}
				if (((PromemoriaOggettiModel)objectmodel).getTipo() == RicercaWizard.ANAGRAFICHE){
					if (((AnagraficheModel)((PromemoriaOggettiModel)objectmodel).getLinkedObject()).getCodAnagrafica() == anagrafica_match_cod){
						findit = true;
						break;
					}
				}
			}
			
			if (!findit){
				PromemoriaOggettiModel pom = new PromemoriaOggettiModel();
				pom.setCodPromemoria(promemoria.getCodPromemoria());
				if (object instanceof ImmobiliModel){
					pom.setCodOggetto(immobili_match_cod);
					pom.setTipo(RicercaWizard.IMMOBILI);
				}
				
				if (object instanceof AnagraficheModel){
					pom.setCodOggetto(anagrafica_match_cod);
					pom.setTipo(RicercaWizard.ANAGRAFICHE);
				}
				
				promemoria.getLinkedObjects().add(pom);
			}
			
		}
		
		tvlink.setInput(promemoria);
		
	}

}