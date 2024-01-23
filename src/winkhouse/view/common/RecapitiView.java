package winkhouse.view.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.recapiti.CancellaRecapitoAction;
import winkhouse.action.recapiti.ConfermaRecapitiAction;
import winkhouse.action.recapiti.NuovoRecapitoAction;
import winkhouse.action.recapiti.RefreshRecapitiAction;
import winkhouse.model.AbbinamentiModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Contatti;
import winkhouse.orm.Tipologiecontatti;
import winkhouse.util.MobiliaDatiBaseCache;



public class RecapitiView extends ViewPart {

	public final static String ID = "winkhouse.recapitiview";
	
	private TableViewer tvRecapiti = null;
	private FormToolkit ft = null;
	private Form f = null;	
	private ArrayList<Anagrafiche> anagrafiche = null;
	private String[] desTipologieRecapiti = null;
	private TextCellEditor tceContatto = null;
	private TextCellEditor tceDescrizione = null;
	
//	private RefreshRecapitiAction refreshRecapitiAction = null;
	private NuovoRecapitoAction nuovoRecapitoAction = null;
	private CancellaRecapitoAction cancellaRecapitoAction = null;
	
	private ImageHyperlink ihConferma = null;
	private ImageHyperlink ihCancella = null;
	private ImageHyperlink ihNew = null;
	private ArrayList<Contatti> contatti = null;
	private ColumnSorter comparator = null;
	
	public RecapitiView() {
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

	private class ColumnSorter extends ViewerComparator{
		
		private int propertyIndex;
	    private static final int DESCENDING = 1;
	    private int direction = DESCENDING;

		public ColumnSorter(){
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
			
			int returnValue = 0;
			
			String tipologia1 = (((Contatti)e1).getTipologiecontatti() == null)
								 ? ""
								 : (((Contatti)e1).getTipologiecontatti().getDescrizione() == null)
								 	? ""
								 	: ((Contatti)e1).getTipologiecontatti().getDescrizione();
			String tipologia2 = (((Contatti)e2).getTipologiecontatti() == null)
								 ? ""
								 : (((Contatti)e2).getTipologiecontatti().getDescrizione() == null)
								 	? ""
								 	: ((Contatti)e2).getTipologiecontatti().getDescrizione();
						
			String contatto1 = (((Contatti)e1).getContatto()==null)
								? "nuovo recapito"
								: String.valueOf(((Contatti)e1).getContatto());
			String contatto2 = (((Contatti)e2).getContatto()==null)
								? "nuovo recapito"
								: String.valueOf(((Contatti)e2).getContatto());

			
			String descrizione1 = (((Contatti)e1).getDescrizione()==null)
									? ""
									: String.valueOf(((Contatti)e1).getDescrizione());
			String descrizione2 = (((Contatti)e2).getDescrizione()==null)
									? ""
									: String.valueOf(((Contatti)e2).getDescrizione());
			
			String anagrafica1 = (((Contatti)e1).getAnagrafiche() == null)
					 			  ? ""
					 			  : (((Contatti)e1).getAnagrafiche().toString() == null)
					 			     ? ""
					 			     : ((Contatti)e1).getAnagrafiche().toString();
			String anagrafica2 = (((Contatti)e2).getAnagrafiche() == null)
					 			  ? ""
					 			  : (((Contatti)e2).getAnagrafiche().toString() == null)
					 			     ? ""
					 			     : ((Contatti)e2).getAnagrafiche().toString();
			
			switch (propertyIndex) {
			case 0: {
				if ((tipologia1 == null) && (tipologia2 == null)){
					returnValue = 0;
				}else if ((tipologia1 != null) && (tipologia2 == null)){
					returnValue = 1;
				}else if ((tipologia1 == null) && (tipologia2 != null)){
					returnValue = -1;
				}else{
					returnValue = tipologia1.compareTo(tipologia2);
				}
				break;
			}
			case 1: {
				if ((contatto1 == null) && (contatto2 == null)){
					returnValue = 0;
				}else if ((contatto1 != null) && (contatto2 == null)){
					returnValue = 1;
				}else if ((contatto1 == null) && (contatto2 != null)){
					returnValue = -1;
				}else{
					returnValue = contatto1.compareTo(contatto2);
				}
				break;
			}
			case 2: {
				if ((descrizione1 == null) && (descrizione2 == null)){
					returnValue = 0;
				}else if ((descrizione1 != null) && (descrizione2 == null)){
					returnValue = 1;
				}else if ((descrizione1 == null) && (descrizione2 != null)){
					returnValue = -1;
				}else{
					returnValue = descrizione1.compareTo(descrizione2);
				}
				break;
			}
			case 3: {
				if ((anagrafica1 == null) && (anagrafica2 == null)){
					returnValue = 0;
				}else if ((anagrafica1 != null) && (anagrafica2 == null)){
					returnValue = 1;
				}else if ((anagrafica1 == null) && (anagrafica2 != null)){
					returnValue = -1;
				}else{
					returnValue = anagrafica1.compareTo(anagrafica2);
				}
				break;
			} 
	        default:
	        	returnValue = 0;
	        }
			
			if (direction == DESCENDING) {
				returnValue = -returnValue;
	        }
			
			return returnValue;
		}
		
	}
	
	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
        SelectionAdapter selectionAdapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                comparator.setColumn(index);
                int dir = comparator.getDirection();
                tvRecapiti.getTable().setSortDirection(dir);
                tvRecapiti.getTable().setSortColumn(column);
                tvRecapiti.refresh();
            }
        };
        return selectionAdapter;
    }


	@Override
	public void createPartControl(Composite parent) {
		
		ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createForm(parent);
		f.setImage(Activator.getImageDescriptor("icons/recapiti.png").createImage());
		f.setText("Recapiti");
		f.getBody().setLayout(new GridLayout());
		
//		refreshRecapitiAction = new RefreshRecapitiAction("Ricarica recapiti", 
//				  										  Activator.getImageDescriptor("icons/adept_reinstall.png"));
		
		nuovoRecapitoAction = new NuovoRecapitoAction("Nuovo recapito", 
													  Activator.getImageDescriptor("icons/filenew.png"));
		
//		confermaRecapitiAction = new ConfermaRecapitiAction("Conferma recapiti", 
//				  									  	    Activator.getImageDescriptor("icons/document-save.png"));

		cancellaRecapitoAction = new CancellaRecapitoAction("Cancella recapito", 
			  	    										Activator.getImageDescriptor("icons/edittrash.png"));

//		getViewSite().getActionBars().getToolBarManager().add(confermaRecapitiAction);
		getViewSite().getActionBars().getToolBarManager().add(nuovoRecapitoAction);
		getViewSite().getActionBars().getToolBarManager().add(cancellaRecapitoAction);
//		getViewSite().getActionBars().getToolBarManager().add(refreshRecapitiAction);
		
		f.updateToolBar();
		
		fillTipologieRecapiti();
		
		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;	
		
		tvRecapiti = new TableViewer(f.getBody(),SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		tvRecapiti.getTable().setLayoutData(gdExpVH);
		tvRecapiti.getTable().setHeaderVisible(true);
		tvRecapiti.getTable().setLinesVisible(true);
		tvRecapiti.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				
				return getContatti().toArray();
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
		comparator = new ColumnSorter();
		tvRecapiti.setComparator(comparator);
		
		TableColumn tcTipologia = new TableColumn(tvRecapiti.getTable(),SWT.CENTER,0);
		tcTipologia.setWidth(150);
		tcTipologia.setText("Tipologia recapito");
		tcTipologia.addSelectionListener(getSelectionAdapter(tcTipologia, 0));
		
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
				fillTipologieRecapiti();
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
					cModel.setTipologiecontatti((MobiliaDatiBaseCache.getInstance().getTipologieContatti().get((Integer)value)));
					tvRecapiti.refresh();
				}
			}
			
		});
			
		TableColumn tcContatto = new TableColumn(tvRecapiti.getTable(),SWT.CENTER,1);
		tcContatto.setWidth(150);
		tcContatto.setText("Recapito");
		tcContatto.addSelectionListener(getSelectionAdapter(tcContatto, 1));

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
		tcDescrizione.setWidth(150);
		tcDescrizione.setText("Descrizione");
		tcDescrizione.addSelectionListener(getSelectionAdapter(tcDescrizione, 2));
		
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
				((Contatti)element).setDescrizione(String.valueOf(value));
				tvRecapiti.refresh();
			}
			
		});

		
		TableColumn tcAnagrafica = new TableColumn(tvRecapiti.getTable(),SWT.CENTER,3);
		tcAnagrafica.setWidth(150);
		tcAnagrafica.setText("Anagrafica");
		tcAnagrafica.addSelectionListener(getSelectionAdapter(tcAnagrafica, 3));
		
		TableViewerColumn tcvAnagrafica = new TableViewerColumn(tvRecapiti,tcAnagrafica);
		tcvAnagrafica.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				Contatti cModel = ((Contatti)cell.getElement());
				cell.setText((cModel.getAnagrafiche() == null)
							 ? ""
							 : (cModel.getAnagrafiche().toString() == null)
							 	? ""
							 	: cModel.getAnagrafiche().toString());
				
			}
			
		});
		tcvAnagrafica.setEditingSupport(new EditingSupport(tvRecapiti){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				
				ComboBoxViewerCellEditor cbvce = new ComboBoxViewerCellEditor(tvRecapiti.getTable(),SWT.READ_ONLY|SWT.DROP_DOWN);
				
				cbvce.setContentProvider(new IStructuredContentProvider() {
					
					@Override
					public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
					}
					
					@Override
					public void dispose() {
					}
					
					@Override
					public Object[] getElements(Object inputElement) {
						return anagrafiche.toArray();
					}
				});
				cbvce.setInput(anagrafiche);
				return cbvce;
			}

			@Override
			protected Object getValue(Object element) {
				if (((Contatti)element).getAnagrafiche() != null){
					int count = 0;
					for (Anagrafiche anagrafica : anagrafiche) {
						if (((Contatti)element).getAnagrafiche().getCodAnagrafica() == anagrafica.getCodAnagrafica()){
							return count;							
						}else{
							count ++;
						}
					}
					return -1;
				}else{
					return -1;
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				if (value != null){
					((Contatti)element).setAnagrafiche((Anagrafiche)value);
					for (Anagrafiche anagrafica : anagrafiche) {
						if (anagrafica.getCodAnagrafica() == ((Anagrafiche)value).getCodAnagrafica()){
							anagrafica.addToContattis((Contatti)element);
							break;
						}
					}
					tvRecapiti.refresh();
				}
			}
			
		});

	}

	@Override
	public void setFocus() {
		
		if ((getAnagrafiche() != null) && (getAnagrafiche().size() > 0)){			
			tvRecapiti.setInput(new Object());	
		}		

	}

	public void fillTipologieRecapiti(){		
		
		Collections.sort(MobiliaDatiBaseCache.getInstance().getTipologieContatti(), comparatorTipologieContatti);
		desTipologieRecapiti = new String[MobiliaDatiBaseCache.getInstance().getTipologieContatti().size()];
		Iterator<Tipologiecontatti> it = MobiliaDatiBaseCache.getInstance().getTipologieContatti().iterator();
		int count = 0;
		while(it.hasNext()){
			desTipologieRecapiti[count] = it.next().getDescrizione();
			count++;
		}		
	}

	public void setAnagrafiche(ArrayList<Anagrafiche> anagrafica) {
		this.anagrafiche = anagrafica;
		contatti = null;
		tvRecapiti.setInput(new Object());
	}

	public void setCompareView(boolean enabled){
		
		tvRecapiti.getTable().setEnabled(!enabled);
//		refreshRecapitiAction.setEnabled(!enabled);
//		confermaRecapitiAction.setEnabled(!enabled);
		cancellaRecapitoAction.setEnabled(!enabled);
		nuovoRecapitoAction.setEnabled(!enabled);		

	}


	public ArrayList<Anagrafiche> getAnagrafiche() {
		return anagrafiche;
	}

	public ArrayList<Contatti> getContatti(){
		
		if (contatti == null){
			contatti = new ArrayList<Contatti>();		
			if (getAnagrafiche() != null){
				for (Anagrafiche anagrafica : getAnagrafiche()) {
					contatti.addAll(anagrafica.getContattis());
				}
			}
		}
		
		return contatti;

	}

	public TableViewer getTvRecapiti() {
		return tvRecapiti;
	}
}
