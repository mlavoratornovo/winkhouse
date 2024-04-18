package winkhouse.view.affitti;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.affitti.ApriAffittiAction;
import winkhouse.action.affitti.CancellaAffittiAction;
import winkhouse.action.affitti.NuovoAffittoAction;
import winkhouse.dao.AffittiDAO;
import winkhouse.model.AffittiModel;
import winkhouse.model.ContattiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Affitti;
import winkhouse.orm.Immobili;
import winkhouse.vo.ContattiVO;



public class ListaAffittiView extends ViewPart {

	public final static String ID = "winkhouse.listaaffittiview";
	private FormToolkit ft = null;
	private Form f = null;
	private TableViewer tvAffitti = null;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private Immobili immobile = null;

	private NuovoAffittoAction naa = null;
	private CancellaAffittiAction caa = null;		
	private ColumnSorter comparator = null;
	
	public ListaAffittiView() {}

	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
        SelectionAdapter selectionAdapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                comparator.setColumn(index);
                int dir = comparator.getDirection();
                tvAffitti.getTable().setSortDirection(dir);
                tvAffitti.getTable().setSortColumn(column);
                tvAffitti.refresh();
            }
        };
        return selectionAdapter;
    }

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
			
			String immobile1 = (((AffittiModel)e1).getImmobile().toString() == null)
								 ? ""
								 : (((AffittiModel)e1).getImmobile().toString() == null)
								 	? ""
								 	: ((AffittiModel)e1).getImmobile().toString();
			String immobile2 = (((AffittiModel)e2).getImmobile().toString() == null)
								 ? ""
								 : (((AffittiModel)e2).getImmobile().toString() == null)
								 	? ""
								 	: ((AffittiModel)e2).getImmobile().toString();
						
			Date datainizio1 = ((AffittiModel)e1).getDataInizio();
			Date datainizio2 = ((AffittiModel)e2).getDataInizio();
			
			Date datafine1 = ((AffittiModel)e1).getDataFine();
			Date datafine2 = ((AffittiModel)e2).getDataFine();
			
			Double cauzione1 = ((AffittiModel)e1).getCauzione();
			Double cauzione2 = ((AffittiModel)e2).getCauzione();

			Double rata1 = ((AffittiModel)e1).getRata();
			Double rata2 = ((AffittiModel)e2).getRata();
			
			switch (propertyIndex) {
			case 0: {
				if ((immobile1 == null) && (immobile2 == null)){
					returnValue = 0;
				}else if ((immobile1 != null) && (immobile2 == null)){
					returnValue = 1;
				}else if ((immobile1 == null) && (immobile2 != null)){
					returnValue = -1;
				}else{
					returnValue = immobile1.compareTo(immobile2);
				}
				break;
			}
			case 1: {
				if ((datainizio1 == null) && (datainizio2 == null)){
					returnValue = 0;
				}else if ((datainizio1 != null) && (datainizio2 == null)){
					returnValue = 1;
				}else if ((datainizio1 == null) && (datainizio2 != null)){
					returnValue = -1;
				}else{
					if (datainizio1.before(datainizio2)){
						returnValue = 1;
					}else if (datainizio1.after(datainizio2)){
						returnValue = -1;
					}else{
						returnValue = 0;
					}
				}
				break;
			}
			case 2: {
				if ((datafine1 == null) && (datafine2 == null)){
					returnValue = 0;
				}else if ((datafine1 != null) && (datafine2 == null)){
					returnValue = 1;
				}else if ((datafine1 == null) && (datafine2 != null)){
					returnValue = -1;
				}else{
					if (datafine1.before(datafine2)){
						returnValue = 1;
					}else if (datafine1.after(datafine2)){
						returnValue = -1;
					}else{
						returnValue = 0;
					}
				}
				break;
			}
			case 3: {
				if ((cauzione1 == null) && (cauzione2 == null)){
					returnValue = 0;
				}else if ((cauzione1 != null) && (cauzione2 == null)){
					returnValue = 1;
				}else if ((cauzione1 == null) && (cauzione2 != null)){
					returnValue = -1;
				}else{
					returnValue = (cauzione1 < cauzione2)?1:(cauzione1 > cauzione2)?-1:0;
				}
				break;
			}
			case 4: {
				if ((rata1 == null) && (rata2 == null)){
					returnValue = 0;
				}else if ((rata1 != null) && (rata2 == null)){
					returnValue = 1;
				}else if ((rata1 == null) && (rata2 != null)){
					returnValue = -1;
				}else{
					returnValue = (rata1 < rata2)?1:(rata1 > rata2)?-1:0;
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
	
	private class OrdinamentoData extends ViewerSorter{

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			Calendar c1 = Calendar.getInstance();			
			Calendar c2 = Calendar.getInstance();
			
			if (c1.before(c2)){
				return 1;
			}else if (c1.after(c2)){
				return -1;
			}else{
				return 0;
			}
		}
		
	} 
	
	@Override
	public void createPartControl(Composite parent) {
		
		ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createForm(parent);
		f.setImage(Activator.getImageDescriptor("/icons/affitti20.png").createImage());
		f.setText("Affitti");
		f.getBody().setLayout(new GridLayout());

		IToolBarManager mgrView = getViewSite().getActionBars().getToolBarManager();
		IToolBarManager mgr = f.getToolBarManager();
		
		naa = new NuovoAffittoAction("Nuovo affitto", 
									 Activator.getImageDescriptor("icons/sample2.gif"));
		
		caa = new CancellaAffittiAction("Cancella affitti", 
										Activator.getImageDescriptor("icons/edittrash.png"),
										CancellaAffittiAction.DELETE_MULTIPLE);		

		mgrView.add(naa);
		mgrView.add(caa);

		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;
				
		tvAffitti = new TableViewer(f.getBody(),SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION|SWT.MULTI);
		tvAffitti.getTable().setLayoutData(gdExpVH);
		tvAffitti.getTable().setHeaderVisible(true);
		tvAffitti.getTable().setLinesVisible(true);
	//	tvAffitti.setSorter(new OrdinamentoData());
		tvAffitti.getTable().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Object o = ((StructuredSelection)tvAffitti.getSelection()).getFirstElement();
				Affitti apm = (Affitti)o;
				ApriAffittiAction aaa = new ApriAffittiAction(apm);
				aaa.run();
			}
		});
		
		tvAffitti.setContentProvider(new IStructuredContentProvider() {
			
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				
			}
			
			@Override
			public void dispose() {
				
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof ArrayList){
					return ((ArrayList)inputElement).toArray();
				}else{
					return null;
				}				
			}
		});
		
		tvAffitti.setLabelProvider(new ITableLabelProvider() {
			
			@Override
			public void removeListener(ILabelProviderListener listener) {
			}
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}
			
			@Override
			public void dispose() {
			}
			
			@Override
			public void addListener(ILabelProviderListener listener) {
			}
			
			@Override
			public String getColumnText(Object element, int columnIndex) {
				switch(columnIndex){
					case 0: return ((AffittiModel)element).getImmobile().toString();
					case 1: return formatter.format(((AffittiModel)element).getDataInizio());					
					case 2: return (((AffittiModel)element).getDataFine() != null)
							       ? formatter.format(((AffittiModel)element).getDataFine())
							       : "";
					case 3 : return String.valueOf(((AffittiModel)element).getCauzione());					
					case 4 : return String.valueOf(((AffittiModel)element).getRata());					
					default : return "";
				}				
			}
			
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
					return null;
			}
		});
		comparator = new ColumnSorter();
		tvAffitti.setComparator(comparator);

		TableColumn tcImmobile = new TableColumn(tvAffitti.getTable(),SWT.CENTER,0);
		tcImmobile.setWidth(300);
		tcImmobile.setText("Immobile");
		tcImmobile.addSelectionListener(getSelectionAdapter(tcImmobile, 0));
		
		TableColumn tcDataInizio = new TableColumn(tvAffitti.getTable(),SWT.CENTER,1);
		tcDataInizio.setWidth(100);
		tcDataInizio.setText("Data inizio");
		tcDataInizio.addSelectionListener(getSelectionAdapter(tcDataInizio, 1));
		
		TableColumn tcDataFine = new TableColumn(tvAffitti.getTable(),SWT.CENTER,2);
		tcDataFine.setWidth(100);
		tcDataFine.setText("Data fine");
		tcDataFine.addSelectionListener(getSelectionAdapter(tcDataFine, 2));
		
		TableColumn tcCauzione = new TableColumn(tvAffitti.getTable(),SWT.CENTER,3);
		tcCauzione.setWidth(100);
		tcCauzione.setText("Cauzione");
		tcCauzione.addSelectionListener(getSelectionAdapter(tcCauzione, 3));
		
		TableColumn tcRata = new TableColumn(tvAffitti.getTable(),SWT.CENTER,4);
		tcRata.setWidth(100);
		tcRata.setText("Rata");
		tcRata.addSelectionListener(getSelectionAdapter(tcRata, 4));

	}

	@Override
	public void setFocus() {


	}
	
	public void setImmobile(Immobili immobile){
		this.immobile = immobile;
		AffittiDAO afDAO = new AffittiDAO();
		if (this.immobile.getCodImmobile() != 0){
			ArrayList al = afDAO.getAffittiByCodImmobile(AffittiModel.class.getName(), 
														 this.immobile.getCodImmobile());
			tvAffitti.setInput(al);
		}
	}

	public Immobili getImmobile() {
		return immobile;
	}

	public TableViewer getTvAffitti() {
		return tvAffitti;
	}

	public void setCompareView(Immobili immobile){
		
		naa.setEnabled(false);
		caa.setEnabled(false);

		setImmobile(immobile);

	}
}
