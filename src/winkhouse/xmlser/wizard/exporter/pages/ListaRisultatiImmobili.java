package winkhouse.xmlser.wizard.exporter.pages;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;

import winkhouse.model.ImmobiliModel;
import winkhouse.Activator;
import winkhouse.xmlser.wizard.exporter.ExporterWizard;


public class ListaRisultatiImmobili extends WizardPage {
	
	private CheckboxTableViewer tvRisultatiRicerca = null;
	private Composite container = null;
	private ArrayList risultati = null;
	
	public ListaRisultatiImmobili(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	public ListaRisultatiImmobili(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		// TODO Auto-generated constructor stub
	}

	class SelezionaTuttoAction extends Action{

		CheckboxTableViewer table = null;
		
		public SelezionaTuttoAction(CheckboxTableViewer table,int type){
			super("Seleziona tutto",type);			
			setImageDescriptor(Activator.getImageDescriptor("/icons/wizardexport/chkall.png"));
			this.table = table;
		}
		
		@Override
		public void run() {
			
			if (isChecked()){
				
				setDescription("Deseleziona tutto");
				setToolTipText("Deseleziona tutto");
				setImageDescriptor(Activator.getImageDescriptor("/icons/wizardexport/chknone.png"));
				TableItem[] items = table.getTable().getItems();
				for (int i = 0; i < items.length; i++) {
					items[i].setChecked(true);
					if (!((ExporterWizard)getWizard()).getExporterVO()
							 						  .getRisultati_selected()
							 						  .contains(items[i].getData())){
					
						((ExporterWizard)getWizard()).getExporterVO()
					 							 	 .getRisultati_selected()
					 							 	 .add(items[i].getData());
					}

				}
				
				
			}else{
				
				setDescription("Seleziona tutto");
				setToolTipText("Seleziona tutto");
				setImageDescriptor(Activator.getImageDescriptor("/icons/wizardexport/chkall.png"));
				TableItem[] items = table.getTable().getItems();
				Object[] checked = tvRisultatiRicerca.getCheckedElements();
				for (int i = 0; i < items.length; i++) {
					items[i].setChecked(false);
					if (((ExporterWizard)getWizard()).getExporterVO()
					 								 .getRisultati_selected()
					 								 .contains(items[i].getData())){

						((ExporterWizard)getWizard()).getExporterVO()
					 							 	 .getRisultati_selected()
					 							 	 .remove(items[i].getData());
					}
					
				}
				
			}
			
			getWizard().getContainer().updateButtons();
			
		}
		
	}
	
	@Override
	public void createControl(Composite parent) {
		
		setTitle(((ExporterWizard)getWizard()).getVersion());
		setDescription("Lista risultati immobili, selezionare quelli da esportare");
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		container = new Composite(parent,SWT.NONE);		
		container.setLayout(new GridLayout());
		container.setLayoutData(gdFillHV);
		
		ToolBar tb = new ToolBar(container, SWT.HORIZONTAL|SWT.FLAT|SWT.RIGHT);
		ToolBarManager tbm = new ToolBarManager(tb);

		
		Table t = new Table(container,SWT.HORIZONTAL|SWT.VERTICAL|SWT.CHECK);
		tvRisultatiRicerca = new CheckboxTableViewer(t);
		tvRisultatiRicerca.getTable().setLayoutData(gdFillHV);
		tvRisultatiRicerca.getTable().setHeaderVisible(true);
		tvRisultatiRicerca.getTable().setLinesVisible(true);
		tvRisultatiRicerca.addCheckStateListener(new ICheckStateListener() {
			
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				if (event.getChecked()){
					if (!((ExporterWizard)getWizard()).getExporterVO()
		 						  						  .getRisultati_selected()
		 						  						  .contains(event.getElement())){
	
							((ExporterWizard)getWizard()).getExporterVO()
													 	 .getRisultati_selected()
													 	 .add(event.getElement());
					}
				}else{
					((ExporterWizard)getWizard()).getExporterVO()
				 	 							 .getRisultati_selected()
				 	 							 .remove(event.getElement());					
				}
				
				getWizard().getContainer().updateButtons();
				
			}
		});
		/*		tvRisultatiRicerca.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection ss = (StructuredSelection)event.getSelection();
				ImmobiliModel im = (ImmobiliModel)ss.getFirstElement();				
				try {
					PlatformUI.getWorkbench()
							  .showPerspective("winkhouse.immobili", PlatformUI.getWorkbench().getActiveWorkbenchWindow());
					DettaglioImmobiliHandler.getInstance().getDettaglioImmobile(im);
				} catch (WorkbenchException e) {
					MessageBox mb = new MessageBox(getShell(),SWT.ERROR);
					mb.setText("Errore");
					mb.setMessage("Errore apertura pagina immobili");			
					mb.open();
				}
			}
			
		});*/
		tvRisultatiRicerca.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return ((ArrayList)inputElement).toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
			
		});
		tvRisultatiRicerca.setLabelProvider(new ITableLabelProvider(){

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				
				switch(columnIndex){
				case 1 : return ((ImmobiliModel)element).getCitta();
				case 2 : return ((ImmobiliModel)element).getProvincia();
				case 3 : return ((ImmobiliModel)element).getIndirizzo();
				case 4 : return ((ImmobiliModel)element).getDescrizione();
				case 5 : return String.valueOf(((ImmobiliModel)element).getPrezzo());
				case 6 : return String.valueOf(((ImmobiliModel)element).getMq());
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
		
		TableColumn tcchk = new TableColumn(tvRisultatiRicerca.getTable(),SWT.LEFT,0);
		tcchk.setWidth(20);

		TableColumn tcCitta = new TableColumn(tvRisultatiRicerca.getTable(),SWT.LEFT,1);
		tcCitta.setWidth(100);
		tcCitta.setText("Citta");

		TableColumn tcProvincia = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,2);
		tcProvincia.setWidth(100);
		tcProvincia.setText("Provincia");
		
		TableColumn tcIndirizzo = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,3);
		tcIndirizzo.setWidth(150);
		tcIndirizzo.setText("Indirizzo");

		TableColumn tcDescrizione = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,4);
		tcDescrizione.setWidth(150);
		tcDescrizione.setText("Descrizione");

		TableColumn tcPrezzo = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,5);
		tcPrezzo.setWidth(50);
		tcPrezzo.setText("Prezzo");

		TableColumn tcMq = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,6);
		tcMq.setWidth(50);
		tcMq.setText("MQ");
		
		tbm.add(new SelezionaTuttoAction(tvRisultatiRicerca,Action.AS_CHECK_BOX));
		tbm.update(true);

		
		setControl(container);
	}

	public ArrayList getRisultati() {
		return risultati;
	}

	public void setRisultati(ArrayList risultati) {
		this.risultati = risultati;
		tvRisultatiRicerca.setInput(this.risultati);
	}

}
