package winkhouse.wizard.ricerca;

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
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;

import winkhouse.Activator;
import winkhouse.action.stampa.StampaImmobiliListAction;
import winkhouse.helper.ProfilerHelper;
import winkhouse.model.ImmobiliModel;
import winkhouse.wizard.RicercaWizard;


public class ListaRisultatiImmobili extends WizardPage {
	
	private CheckboxTableViewer tvRisultatiRicerca = null;
	private Composite container = null;
	private ArrayList risultati = null;
	private ImmobiliVC immobiliVC = new ImmobiliVC();
	
	public ListaRisultatiImmobili(String pageName) {
		super(pageName);

	}

	public ListaRisultatiImmobili(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);

	}

	private class ImmobiliVC extends ViewerComparator{
		
		private int propertyIndex;
	    private static final int DESCENDING = 1;
	    private int direction = DESCENDING;

	    public ImmobiliVC() {
	        this.propertyIndex = 0;
	        direction = DESCENDING;
	    }

	    public int getDirection() {
	        return direction == 1 ? SWT.DOWN : SWT.UP;
	    }

	    public void setColumn(int column) {
	        if (column == this.propertyIndex) {
	            // Same column as last sort; toggle the direction
	            direction = 1 - direction;
	        } else {
	            // New column; do an ascending sort
	            this.propertyIndex = column;
	            direction = DESCENDING;
	        }
	    }

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			
			ImmobiliModel im1 = (ImmobiliModel) e1;
			ImmobiliModel im2 = (ImmobiliModel) e2;
			int rc = 0;
	        switch (propertyIndex) {
	        case 1:
	            rc = im1.getCitta().compareTo(im2.getCitta());
	            break;
	        case 2:
	            rc = im1.getProvincia().compareTo(im2.getProvincia());
	            break;
	        case 3:
	            rc = im1.getIndirizzo().compareTo(im2.getIndirizzo());
	            break;
	        case 4:
	            rc = im1.getDescrizione().compareTo(im2.getDescrizione());
	            break;
	        case 5:
	        	rc = im1.getPrezzo().compareTo(im2.getPrezzo());
	        	break;
	        case 6:
	        	rc = im1.getMq().compareTo(im2.getMq());
	        	break;
	        	
	        default:
	            rc = 0;
	        }
	        // If descending order, flip the direction
	        if (direction == DESCENDING) {
	            rc = -rc;
	        }
	        return rc;
			
		}
	    
	}
	
	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
		
        SelectionAdapter selectionAdapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	immobiliVC.setColumn(index);
                int dir = immobiliVC.getDirection();
                tvRisultatiRicerca.getTable().setSortDirection(dir);
                tvRisultatiRicerca.getTable().setSortColumn(column);
                tvRisultatiRicerca.refresh();
            }
            
        };
        
        return selectionAdapter;
    }
	
	@Override
	public void createControl(Composite parent) {
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
		tbm.add(new StampaImmobiliListAction("Report immobili", 
				Action.AS_DROP_DOWN_MENU));

		
		Table t = new Table(container,SWT.HORIZONTAL|SWT.VERTICAL|SWT.CHECK);
		tvRisultatiRicerca = new CheckboxTableViewer(t);
		tvRisultatiRicerca.getTable().setLayoutData(gdFillHV);
		tvRisultatiRicerca.getTable().setHeaderVisible(true);
		tvRisultatiRicerca.getTable().setLinesVisible(true);
		tvRisultatiRicerca.setComparator(immobiliVC);
		tvRisultatiRicerca.addCheckStateListener(new ICheckStateListener() {
			
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				Object[] checked = tvRisultatiRicerca.getCheckedElements();
				((RicercaWizard)getWizard()).getRicerca().setRisultati(new ArrayList());
				for (int i = 0; i < checked.length; i++) {
					((RicercaWizard)getWizard()).getRicerca()
												.getRisultati()
												.add(checked[i]);
				}
				
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
		tcCitta.addSelectionListener(getSelectionAdapter(tcCitta, 1));
		
		TableColumn tcProvincia = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,2);
		tcProvincia.setWidth(100);
		tcProvincia.setText("Provincia");
		tcProvincia.addSelectionListener(getSelectionAdapter(tcProvincia, 2));
		
		TableColumn tcIndirizzo = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,3);
		tcIndirizzo.setWidth(150);
		tcIndirizzo.setText("Indirizzo");
		tcIndirizzo.addSelectionListener(getSelectionAdapter(tcIndirizzo, 3));

		TableColumn tcDescrizione = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,4);
		tcDescrizione.setWidth(150);
		tcDescrizione.setText("Descrizione");
		tcDescrizione.addSelectionListener(getSelectionAdapter(tcDescrizione, 4));

		TableColumn tcPrezzo = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,5);
		tcPrezzo.setWidth(50);
		tcPrezzo.setText("Prezzo");
		tcPrezzo.addSelectionListener(getSelectionAdapter(tcPrezzo, 5));

		TableColumn tcMq = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,6);
		tcMq.setWidth(50);
		tcMq.setText("MQ");
		tcMq.addSelectionListener(getSelectionAdapter(tcMq, 6));
		
		tbm.add(new SelezionaTuttoAction(tvRisultatiRicerca,Action.AS_CHECK_BOX));		
		tbm.update(true);
		
		setControl(container);
	}

	public ArrayList getRisultati() {
		return risultati;
	}

	public void setRisultati(ArrayList risultati) {
		this.risultati = risultati;
		if (((RicercaWizard)getWizard()).getRicerca().getType() == RicercaWizard.IMMOBILI){
			ProfilerHelper.getInstance().filterImmobili(this.risultati, false);
		}
		if (((RicercaWizard)getWizard()).getRicerca().getType() == RicercaWizard.AFFITTI){
			ProfilerHelper.getInstance().filterAffitti(this.risultati, false);
		}		
		tvRisultatiRicerca.setInput(this.risultati);
	}

	class SelezionaTuttoAction extends Action{

		CheckboxTableViewer table = null;
		
		private ImageDescriptor CHKALL = Activator.getImageDescriptor("/icons/chkall20.png");
		private ImageDescriptor CHKNONE = Activator.getImageDescriptor("/icons/chknone20.png");
		
		public SelezionaTuttoAction(CheckboxTableViewer table,int type){
			super("Seleziona tutto",type);			
			setImageDescriptor(CHKALL);
			this.table = table;
		}
		
		@Override
		public void run() {
			
			if (isChecked()){
				
				setDescription("Deseleziona tutto");
				setImageDescriptor(CHKNONE);
				TableItem[] items = table.getTable().getItems();
				((RicercaWizard)getWizard()).getRicerca().setRisultati(new ArrayList());
				((RicercaWizard)getWizard()).getRicerca().getRisultati().addAll((ArrayList)table.getInput());
				
				for (int i = 0; i < items.length; i++) {
					items[i].setChecked(true);
				}
				
				
				
				
			}else{
				
				setDescription("Seleziona tutto");
				setImageDescriptor(CHKALL);
				TableItem[] items = table.getTable().getItems();
				((RicercaWizard)getWizard()).getRicerca().setRisultati(null);
				Object[] checked = table.getCheckedElements();
				for (int i = 0; i < items.length; i++) {
					items[i].setChecked(false);
					
				}
				
			}
			
			getWizard().getContainer().updateButtons();
			
		}
		
	}
}
