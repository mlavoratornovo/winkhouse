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
import org.eclipse.swt.widgets.ToolBar;

import winkhouse.action.stampa.StampaAnagraficheListAction;
import winkhouse.helper.ProfilerHelper;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.wizard.RicercaWizard;


public class ListaRisultatiAnagrafiche extends WizardPage {

	private CheckboxTableViewer tvRisultatiRicerca = null;
	private Composite container = null;
	private ArrayList risultati = null;
	private AnagraficheVC anagraficheVC = new AnagraficheVC();
	
	public ListaRisultatiAnagrafiche(String pageName) {
		super(pageName);

	}

	public ListaRisultatiAnagrafiche(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);

	}

	private class AnagraficheVC extends ViewerComparator{
		
		private int propertyIndex;
	    private static final int DESCENDING = 1;
	    private int direction = DESCENDING;

	    public AnagraficheVC() {
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
						
			AnagraficheModel am1 = (AnagraficheModel) e1;
			AnagraficheModel am2 = (AnagraficheModel) e2;
			int rc = 0;
	        switch (propertyIndex) {
	        case 1:
	            rc = am1.getNome().compareTo(am2.getNome());
	            break;
	        case 2:
	            rc = am1.getCognome().compareTo(am2.getCognome());
	            break;
	        case 3:
	            rc = am1.getIndirizzo().compareTo(am2.getIndirizzo());
	            break;
	        case 4:
	            rc = am1.getCitta().compareTo(am2.getCitta());
	            break;
	        case 5:
	        	rc = am1.getCap().compareTo(am2.getCap());
	        	break;
	        case 6:
	        	rc = am1.getProvincia().compareTo(am2.getProvincia());
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
            	anagraficheVC.setColumn(index);
                int dir = anagraficheVC.getDirection();
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
		tbm.add(new StampaAnagraficheListAction("Report anagrafiche", 
				Action.AS_DROP_DOWN_MENU));
		tbm.update(true);		
		
		Table t = new Table(container,SWT.HORIZONTAL|SWT.VERTICAL|SWT.CHECK);
		tvRisultatiRicerca = new CheckboxTableViewer(t);
		tvRisultatiRicerca.getTable().setLayoutData(gdFillHV);
		tvRisultatiRicerca.getTable().setHeaderVisible(true);
		tvRisultatiRicerca.getTable().setLinesVisible(true);
		tvRisultatiRicerca.setComparator(anagraficheVC);
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
				case 1 : return ((AnagraficheModel)element).getNome();
				case 2 : return ((AnagraficheModel)element).getCognome();
				case 3 : return ((AnagraficheModel)element).getIndirizzo();
				case 4 : return ((AnagraficheModel)element).getCitta();
				case 5 : return ((AnagraficheModel)element).getCap();
				case 6 : return ((AnagraficheModel)element).getProvincia();
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
		tcCitta.setText("Nome");
		tcCitta.addSelectionListener(getSelectionAdapter(tcCitta, 1));
		
		TableColumn tcProvincia = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,2);
		tcProvincia.setWidth(100);
		tcProvincia.setText("Cognome");
		tcProvincia.addSelectionListener(getSelectionAdapter(tcProvincia, 2));
		
		TableColumn tcIndirizzo = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,3);
		tcIndirizzo.setWidth(150);
		tcIndirizzo.setText("Indirizzo");
		tcIndirizzo.addSelectionListener(getSelectionAdapter(tcIndirizzo, 3));
		
		TableColumn tcDescrizione = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,4);
		tcDescrizione.setWidth(150);
		tcDescrizione.setText("Citta");
		tcDescrizione.addSelectionListener(getSelectionAdapter(tcDescrizione, 4));
		
		TableColumn tcPrezzo = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,5);
		tcPrezzo.setWidth(50);
		tcPrezzo.setText("Cap");
		tcPrezzo.addSelectionListener(getSelectionAdapter(tcPrezzo, 5));
		
		TableColumn tcMq = new TableColumn(tvRisultatiRicerca.getTable(),SWT.CENTER,6);
		tcMq.setWidth(50);
		tcMq.setText("Provincia");
		tcMq.addSelectionListener(getSelectionAdapter(tcMq, 6));
		
		setControl(container);
	}


	public ArrayList getRisultati() {
		return risultati;
	}

	public void setRisultati(ArrayList risultati) {
		this.risultati = risultati;
		ProfilerHelper.getInstance().filterAnagrafiche(risultati, false);
		tvRisultatiRicerca.setInput(this.risultati);
	}

}
