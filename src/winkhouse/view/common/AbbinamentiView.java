package winkhouse.view.common;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
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
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.abbinamenti.AbbinamentiManuale;
import winkhouse.action.abbinamenti.CancellaAbbinamenti;
import winkhouse.action.abbinamenti.FindAbbinamenti;
import winkhouse.action.anagrafiche.ApriDettaglioAnagraficaAction;
import winkhouse.action.immobili.ApriDettaglioImmobileAction;
import winkhouse.dao.AbbinamentiDAO;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Colloqui;
import winkhouse.orm.Immobili;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.TipologieColloquiVO;



public class AbbinamentiView extends ViewPart {

	public final static String ID = "winkhouse.anagraficheabbinateview";
	private TreeViewer tvAbinamenti = null;
	private Immobili immobile = null;
	private Colloqui colloquio = null;
	private Anagrafiche anagrafica = null;
	private ArrayList<?> ricerca = null;
	private ArrayList<?> manuale = null;
	
	private ArrayList<Abbinamenti> abbinameti = new ArrayList<Abbinamenti>(2);
	
	private AbbinamentiManuali abbinamentiManuali = null;
	private AbbinamentiRicerca abbinamentiRicerca = null;
		
	private Image abbinamentiManualiImg = Activator.getImageDescriptor("icons/abbinamenti12.png").createImage();
	private Image abbinamentiRicercaImg = Activator.getImageDescriptor("icons/kfind14.png").createImage();
	private Image anagraficheImg = Activator.getImageDescriptor("icons/anagrafica12.png").createImage();
	private Image immobiliImg = Activator.getImageDescriptor("icons/gohome12.png").createImage();

	private AbbinamentiManuale aam = null;			
	private CancellaAbbinamenti ca = null;
	private ColumnSorter comparator = null;
	
	private interface Abbinamenti{};
	
	private class AbbinamentiRicerca implements Abbinamenti{
		
		private ArrayList<?> abbinamenti = null;
		
		public AbbinamentiRicerca(){}

		public ArrayList<?> getAbbinamenti() {
			return this.abbinamenti;
		}

		public void setAbbinamenti(ArrayList<?> abbinamenti) {
			this.abbinamenti = abbinamenti;
		}
		
	}
	
	private class AbbinamentiManuali implements Abbinamenti{
		
		private ArrayList<?> abbinamenti = null;
		
		public AbbinamentiManuali(){}

		public ArrayList<?> getAbbinamenti() {
			return this.abbinamenti;
		}

		public void setAbbinamenti(ArrayList<?> abbinamenti) {
			this.abbinamenti = abbinamenti;
		}
		
	}
	
//	private class StartModel{
//		public StartModel(){}
//	};
	
	
	public AbbinamentiView() {	
	}

	private class AbbinamentiContentProvider implements IStructuredContentProvider,
	 												 	ITreeContentProvider{

		@Override
		public Object[] getChildren(Object parentElement) {
			return getElements(parentElement);
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return (element instanceof winkhouse.orm.Abbinamenti)?false:true;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof AbbinamentiManuali){
				return (((AbbinamentiManuali)inputElement).getAbbinamenti() != null)
					   ?((AbbinamentiManuali)inputElement).getAbbinamenti().toArray()
					   : new Object[0];
			}else if (inputElement instanceof AbbinamentiRicerca){
				return (((AbbinamentiRicerca)inputElement).getAbbinamenti() != null)
					   ?((AbbinamentiRicerca)inputElement).getAbbinamenti().toArray()
					   : new Object[0];
			}else if (inputElement instanceof ArrayList){
				return ((ArrayList<?>)inputElement).toArray();
			}else{
				return new Object[0];
			}

		}

		@Override
		public void dispose() {
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {			
			
		}
		
	}
	
	private class AbbinamentiLabelProvider extends LabelProvider 
										   implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if ((columnIndex == 1) && (element instanceof AbbinamentiManuali)){
				return abbinamentiManualiImg;
			}else if ((columnIndex == 1) && (element instanceof AbbinamentiRicerca)){
				return abbinamentiRicercaImg;
			}else if ((columnIndex == 3) && (element instanceof winkhouse.orm.Abbinamenti)){

/*				return (PlatformUI.getWorkbench()
				   		   		  .getActiveWorkbenchWindow()
				   		   		  .getActivePage()
				   		   		  .getPerspective()
				   		   		  .getId().equalsIgnoreCase(ImmobiliPerspective.ID))
				   	   ? anagraficheImg
					   : immobiliImg;
*/
				if (WinkhouseUtils.getInstance()
									.getLastEntityTypeFocused() != null){
					
				   if (WinkhouseUtils.getInstance()
 								       .getLastEntityTypeFocused()
									   .equalsIgnoreCase(Immobili.class.getName())){
					   return anagraficheImg;
				   }else if (WinkhouseUtils.getInstance()
						   					 .getLastEntityTypeFocused()
						   					 .equalsIgnoreCase(Anagrafiche.class.getName())){
					   return immobiliImg;
				   }else{
					   return null;
				   }
				
				}else{
					return null;
				}
				
			}else if ((columnIndex == 3) && (element instanceof Anagrafiche)){

				return anagraficheImg;
					   
			}else if ((columnIndex == 3) && (element instanceof Immobili)){
				
				return immobiliImg;
				
			}else{
				return null;
			}			
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			
			if ((columnIndex == 2) && (element instanceof AbbinamentiManuali)){
				return "Manuali";
			}else if ((columnIndex == 2) && (element instanceof AbbinamentiRicerca)){
				return "Ricerca";
			}else if (columnIndex == 4){
				if (element instanceof winkhouse.orm.Abbinamenti){
					return (immobile != null)
						   ? ((winkhouse.orm.Abbinamenti)element).getAnagrafiche().getNome()
						   : (anagrafica != null)
						   	 ? ((winkhouse.orm.Abbinamenti)element).getImmobili().getDescrizione()
						   	 : "";
				}else if (element instanceof Immobili){
					return ((Immobili)element).getDescrizione();
				}else if (element instanceof Anagrafiche){
					return ((Anagrafiche)element).getNome();
				}else return "";				
			}else if (columnIndex == 5){
				if (element instanceof winkhouse.orm.Abbinamenti){
					return (immobile != null)
						   ? ((winkhouse.orm.Abbinamenti)element).getAnagrafiche().getCognome()
						   : (anagrafica != null)
						   	 ? ((winkhouse.orm.Abbinamenti)element).getImmobili().getCitta()
						   	 : "";
				}else if (element instanceof Immobili){
					return ((Immobili)element).getCitta();
				}else if (element instanceof Anagrafiche){
					return ((Anagrafiche)element).getCognome();
				}else return "";				
			}else if (columnIndex == 6){
				if (element instanceof winkhouse.orm.Abbinamenti){
					return (immobile != null)
						   ? ((winkhouse.orm.Abbinamenti)element).getAnagrafiche().getIndirizzo()
						   : (anagrafica != null)
						   	 ? ((winkhouse.orm.Abbinamenti)element).getImmobili().getIndirizzo()
						   	 : "";
				}else if (element instanceof Immobili){
					return ((Immobili)element).getIndirizzo();
				}else if (element instanceof Anagrafiche){
					return ((Anagrafiche)element).getIndirizzo();
				}else return "";				
			}else if (columnIndex == 7){
				if (element instanceof winkhouse.orm.Abbinamenti){
					return (immobile != null)
						   ? ((winkhouse.orm.Abbinamenti)element).getAnagrafiche().getProvincia()
						   : (anagrafica != null)
						   	 ? ((winkhouse.orm.Abbinamenti)element).getImmobili().getProvincia()
						   	 : "";
				}else if (element instanceof Immobili){
					return ((Immobili)element).getProvincia();
				}else if (element instanceof Anagrafiche){
					return ((Anagrafiche)element).getProvincia();
				}else return "";				
			}else if (columnIndex == 8){
				if (element instanceof winkhouse.orm.Abbinamenti){
					return (immobile != null)
						   ? ((winkhouse.orm.Abbinamenti)element).getAnagrafiche().getCitta()
						   : (anagrafica != null)
						   	 ? String.valueOf(((winkhouse.orm.Abbinamenti)element).getImmobili().getPrezzo())
						   	 : "";
				}else if (element instanceof Immobili){
					return String.valueOf(((Immobili)element).getPrezzo());
				}else if (element instanceof Anagrafiche){
					return ((Anagrafiche)element).getCitta();
				}else return "";				
			}else if (columnIndex == 9){
				if (element instanceof winkhouse.orm.Abbinamenti){
					return (immobile != null)
						   ? ((winkhouse.orm.Abbinamenti)element).getAnagrafiche().getCap()
						   : (anagrafica != null)
						   	 ? String.valueOf(((winkhouse.orm.Abbinamenti)element).getImmobili().getMq())
						   	 : "";
				}else if (element instanceof Immobili){
					return String.valueOf(((Immobili)element).getMq());
				}else if (element instanceof Anagrafiche){
					return ((Anagrafiche)element).getCap();
				}else return "";				
			}else{
				return "";
			}			
		}
		
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
			
			String col4_1 = null;
			String col4_2 = null;
			
			if (e1 instanceof winkhouse.orm.Abbinamenti){
				col4_1 = (immobile != null)
					   ? ((winkhouse.orm.Abbinamenti)e1).getAnagrafiche().getNome()
					   : (anagrafica != null)
					   	 ? ((winkhouse.orm.Abbinamenti)e1).getImmobili().getDescrizione()
					   	 : "";
			}else if (e1 instanceof Immobili){
				col4_1 = ((Immobili)e1).getDescrizione();
			}else if (e1 instanceof Anagrafiche){
				col4_1 = ((Anagrafiche)e1).getNome();
			}else{ col4_1 =  "";}

			if (e2 instanceof winkhouse.orm.Abbinamenti){
				col4_2 = (immobile != null)
					   ? ((winkhouse.orm.Abbinamenti)e2).getAnagrafiche().getNome()
					   : (anagrafica != null)
					   	 ? ((winkhouse.orm.Abbinamenti)e2).getImmobili().getDescrizione()
					   	 : "";
			}else if (e2 instanceof Immobili){
				col4_2 = ((Immobili)e2).getDescrizione();
			}else if (e2 instanceof Anagrafiche){
				col4_2 = ((Anagrafiche)e2).getNome();
			}else{ col4_2 =  "";}
			
			String col5_1 = null;
			String col5_2 = null;
			
			if (e1 instanceof winkhouse.orm.Abbinamenti){
				col5_1 = (immobile != null)
					   ? ((winkhouse.orm.Abbinamenti)e1).getAnagrafiche().getCognome()
					   : (anagrafica != null)
					   	 ? ((winkhouse.orm.Abbinamenti)e1).getImmobili().getCitta()
					   	 : "";
			}else if (e1 instanceof Immobili){
				col5_1 = ((Immobili)e1).getCitta();
			}else if (e1 instanceof Anagrafiche){
				col5_1 = ((Anagrafiche)e1).getCognome();
			}else col5_1 = "";

			if (e2 instanceof winkhouse.orm.Abbinamenti){
				col5_2 = (immobile != null)
					   ? ((winkhouse.orm.Abbinamenti)e2).getAnagrafiche().getCognome()
					   : (anagrafica != null)
					   	 ? ((winkhouse.orm.Abbinamenti)e2).getImmobili().getCitta()
					   	 : "";
			}else if (e2 instanceof Immobili){
				col5_2 = ((Immobili)e2).getCitta();
			}else if (e2 instanceof Anagrafiche){
				col5_2 = ((Anagrafiche)e2).getCognome();
			}else col5_2 = "";

			
			String col6_1 = null;
			String col6_2 = null;

			if (e1 instanceof winkhouse.orm.Abbinamenti){
				col6_1 = (immobile != null)
					   ? ((winkhouse.orm.Abbinamenti)e1).getAnagrafiche().getIndirizzo()
					   : (anagrafica != null)
					   	 ? ((winkhouse.orm.Abbinamenti)e1).getImmobili().getIndirizzo()
					   	 : "";
			}else if (e1 instanceof Immobili){
				col6_1 = ((Immobili)e1).getIndirizzo();
			}else if (e1 instanceof Anagrafiche){
				col6_1 = ((Anagrafiche)e1).getIndirizzo();
			}else col6_1 = "";				

			if (e2 instanceof winkhouse.orm.Abbinamenti){
				col6_2 = (immobile != null)
					   ? ((winkhouse.orm.Abbinamenti)e2).getAnagrafiche().getIndirizzo()
					   : (anagrafica != null)
					   	 ? ((winkhouse.orm.Abbinamenti)e2).getImmobili().getIndirizzo()
					   	 : "";
			}else if (e2 instanceof Immobili){
				col6_2 = ((Immobili)e2).getIndirizzo();
			}else if (e2 instanceof Anagrafiche){
				col6_2 = ((Anagrafiche)e2).getIndirizzo();
			}else col6_2 = "";				

			
			String col7_1 = null;
			String col7_2 = null;

			if (e1 instanceof winkhouse.orm.Abbinamenti){
				col7_1 =  (immobile != null)
					   ? ((winkhouse.orm.Abbinamenti)e1).getAnagrafiche().getProvincia()
					   : (anagrafica != null)
					   	 ? ((winkhouse.orm.Abbinamenti)e1).getImmobili().getProvincia()
					   	 : "";
			}else if (e1 instanceof Immobili){
				col7_1 =  ((Immobili)e1).getProvincia();
			}else if (e1 instanceof Anagrafiche){
				col7_1 =  ((Anagrafiche)e1).getProvincia();
			}else col7_1 =  "";				

			if (e2 instanceof winkhouse.orm.Abbinamenti){
				col7_2 =  (immobile != null)
					   ? ((winkhouse.orm.Abbinamenti)e2).getAnagrafiche().getProvincia()
					   : (anagrafica != null)
					   	 ? ((winkhouse.orm.Abbinamenti)e2).getImmobili().getProvincia()
					   	 : "";
			}else if (e2 instanceof Immobili){
				col7_2 =  ((Immobili)e2).getProvincia();
			}else if (e2 instanceof Anagrafiche){
				col7_2 =  ((Anagrafiche)e2).getProvincia();
			}else col7_2 =  "";				

			String col8_1 = null;
			String col8_2 = null;

			if (e1 instanceof winkhouse.orm.Abbinamenti){
				col8_1 =  (immobile != null)
					   ? ((winkhouse.orm.Abbinamenti)e1).getAnagrafiche().getCitta()
					   : (anagrafica != null)
					   	 ? String.valueOf(((winkhouse.orm.Abbinamenti)e1).getImmobili().getPrezzo())
					   	 : "";
			}else if (e1 instanceof Immobili){
				col8_1 =  String.valueOf(((Immobili)e1).getPrezzo());
			}else if (e1 instanceof Anagrafiche){
				col8_1 =  ((Anagrafiche)e1).getCitta();
			}else col8_1 =  "";				

			if (e2 instanceof winkhouse.orm.Abbinamenti){
				col8_2 =  (immobile != null)
					   ? ((winkhouse.orm.Abbinamenti)e2).getAnagrafiche().getCitta()
					   : (anagrafica != null)
					   	 ? String.valueOf(((winkhouse.orm.Abbinamenti)e2).getImmobili().getPrezzo())
					   	 : "";
			}else if (e2 instanceof Immobili){
				col8_2 =  String.valueOf(((Immobili)e2).getPrezzo());
			}else if (e2 instanceof Anagrafiche){
				col8_2 =  ((Anagrafiche)e2).getCitta();
			}else col8_2 =  "";				

			String col9_1 = null;
			String col9_2 = null;
			
			if (e1 instanceof winkhouse.orm.Abbinamenti){
				col9_1 = (immobile != null)
					   ? ((winkhouse.orm.Abbinamenti)e1).getAnagrafiche().getCap()
					   : (anagrafica != null)
					   	 ? String.valueOf(((winkhouse.orm.Abbinamenti)e1).getImmobili().getMq())
					   	 : "";
			}else if (e1 instanceof Immobili){
				col9_1 = String.valueOf(((Immobili)e1).getMq());
			}else if (e1 instanceof Anagrafiche){
				col9_1 = ((Anagrafiche)e1).getCap();
			}else col9_1 =  "";				

			if (e2 instanceof winkhouse.orm.Abbinamenti){
				col9_2 =  (immobile != null)
					   ? ((winkhouse.orm.Abbinamenti)e2).getAnagrafiche().getCap()
					   : (anagrafica != null)
					   	 ? String.valueOf(((winkhouse.orm.Abbinamenti)e2).getImmobili().getMq())
					   	 : "";
			}else if (e2 instanceof Immobili){
				col9_2 =  String.valueOf(((Immobili)e2).getMq());
			}else if (e2 instanceof Anagrafiche){
				col9_2 =  ((Anagrafiche)e2).getCap();
			}else col9_2 =  "";				

			
			switch (propertyIndex) {
			case 4: {
				if ((col4_1 == null) && (col4_2 == null)){
					returnValue = 0;
				}else if ((col4_1 != null) && (col4_2 == null)){
					returnValue = 1;
				}else if ((col4_1 == null) && (col4_2 != null)){
					returnValue = -1;
				}else{
					returnValue = col4_1.compareTo(col4_2);
				}
				break;
			}
			case 5: {
				if ((col5_1 == null) && (col5_2 == null)){
					returnValue = 0;
				}else if ((col5_1 != null) && (col5_2 == null)){
					returnValue = 1;
				}else if ((col5_1 == null) && (col5_2 != null)){
					returnValue = -1;
				}else{
					returnValue = col5_1.compareTo(col5_2);
				}
				break;
			}
			case 6: {
				if ((col6_1 == null) && (col6_2 == null)){
					returnValue = 0;
				}else if ((col6_1 != null) && (col6_2 == null)){
					returnValue = 1;
				}else if ((col6_1 == null) && (col6_2 != null)){
					returnValue = -1;
				}else{
					returnValue = col6_1.compareTo(col6_2);
				}
				break;
			}
			case 7: {
				if ((col7_1 == null) && (col7_2 == null)){
					returnValue = 0;
				}else if ((col7_1 != null) && (col7_2 == null)){
					returnValue = 1;
				}else if ((col7_1 == null) && (col7_2 != null)){
					returnValue = -1;
				}else{
					returnValue = col7_1.compareTo(col7_2);
				}
				break;
			} 
			case 8: {
				if ((col8_1 == null) && (col8_2 == null)){
					returnValue = 0;
				}else if ((col8_1 != null) && (col8_2 == null)){
					returnValue = 1;
				}else if ((col8_1 == null) && (col8_2 != null)){
					returnValue = -1;
				}else{
					returnValue = col8_1.compareTo(col8_2);
				}
				break;
			}
			case 9: {
				if ((col9_1 == null) && (col9_2 == null)){
					returnValue = 0;
				}else if ((col9_1 != null) && (col9_2 == null)){
					returnValue = 1;
				}else if ((col9_1 == null) && (col9_2 != null)){
					returnValue = -1;
				}else{
					returnValue = col9_1.compareTo(col9_2);
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
	
	private SelectionAdapter getSelectionAdapter(final TreeColumn column, final int index) {
        SelectionAdapter selectionAdapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                comparator.setColumn(index);
                int dir = comparator.getDirection();
                tvAbinamenti.getTree().setSortDirection(dir);
                tvAbinamenti.getTree().setSortColumn(column);
                tvAbinamenti.refresh();
            }
        };
        return selectionAdapter;
    }
	
	@Override
	public void createPartControl(Composite parent) {
		
		FormToolkit ft = new FormToolkit(getViewSite().getShell().getDisplay());
		Form f = ft.createForm(parent);
		f.setImage(Activator.getImageDescriptor("icons/anagraficheabbinate.png").createImage());
		f.setText("Abbinamenti");
		f.getBody().setLayout(new GridLayout());
		
		abbinameti.add(new AbbinamentiManuali());
		abbinameti.add(new AbbinamentiRicerca());
		
		FindAbbinamenti fai = new FindAbbinamenti();
		fai.setToolTipText("Ricerca abbinamenti");
		fai.setImageDescriptor(Activator.getImageDescriptor("icons/viewmagfit.png"));
		getViewSite().getActionBars().getToolBarManager().add(fai);
		
		aam = new AbbinamentiManuale("Abbinamenti manuale",
									 Activator.getImageDescriptor("icons/abbinamenti.png"));
		
		getViewSite().getActionBars().getToolBarManager().add(aam);
		
		ca = new CancellaAbbinamenti();
		
		getViewSite().getActionBars().getToolBarManager().add(ca);
		
		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;	
		
		tvAbinamenti = new TreeViewer(f.getBody(),SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION|SWT.MULTI);
		tvAbinamenti.getTree().setLayoutData(gdExpVH);
		tvAbinamenti.getTree().setHeaderVisible(true);
		tvAbinamenti.getTree().setLinesVisible(true);
		tvAbinamenti.getTree().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (((StructuredSelection)tvAbinamenti.getSelection()).getFirstElement() instanceof Immobili){
					ApriDettaglioImmobileAction adia = new ApriDettaglioImmobileAction((Immobili)((StructuredSelection)tvAbinamenti.getSelection()).getFirstElement(),null);
					adia.run();
				}
				if (((StructuredSelection)tvAbinamenti.getSelection()).getFirstElement() instanceof Anagrafiche){
//					ApriDettaglioAnagraficaAction adaa = new ApriDettaglioAnagraficaAction((Anagrafiche)((StructuredSelection)tvAbinamenti.getSelection()).getFirstElement(), null);
//					adaa.run();
					
				}				
				 
				if (((StructuredSelection)tvAbinamenti.getSelection()).getFirstElement() instanceof Abbinamenti){
					winkhouse.orm.Abbinamenti am = (winkhouse.orm.Abbinamenti)((StructuredSelection)tvAbinamenti.getSelection()).getFirstElement();
					if (immobile != null){
//						ApriDettaglioAnagraficaAction adaa = new ApriDettaglioAnagraficaAction(am.getAnagrafiche(), null);
//						adaa.run();
					}else if (anagrafica != null){
						ApriDettaglioImmobileAction adia = new ApriDettaglioImmobileAction(am.getImmobili(),null);
						adia.run();
					}
				}
			}
		});	
		
		tvAbinamenti.setContentProvider(new AbbinamentiContentProvider());
		
		tvAbinamenti.setLabelProvider(new AbbinamentiLabelProvider());
		comparator = new ColumnSorter();
		tvAbinamenti.setComparator(comparator);
		
		TreeColumn tc0 = new TreeColumn(tvAbinamenti.getTree(),SWT.LEFT,0);
		tc0.setWidth(20);
		TreeColumn tc1 = new TreeColumn(tvAbinamenti.getTree(),SWT.LEFT,1);
		tc1.setWidth(20);
		
		TreeColumn tc2 = new TreeColumn(tvAbinamenti.getTree(),SWT.LEFT,2);
		tc2.setWidth(150);
		TreeColumn tc3 = new TreeColumn(tvAbinamenti.getTree(),SWT.LEFT,3);
		tc3.setWidth(20);

		TreeColumn tc4 = new TreeColumn(tvAbinamenti.getTree(),SWT.LEFT,4);
		tc4.setWidth(150);
		tc4.addSelectionListener(getSelectionAdapter(tc4, 4));
		
		TreeColumn tc5 = new TreeColumn(tvAbinamenti.getTree(),SWT.LEFT,5);
		tc5.setWidth(150);
		tc5.addSelectionListener(getSelectionAdapter(tc5, 5));
		
		TreeColumn tc6 = new TreeColumn(tvAbinamenti.getTree(),SWT.LEFT,6);
		tc6.setWidth(150);
		tc6.addSelectionListener(getSelectionAdapter(tc6, 6));
		
		TreeColumn tc7 = new TreeColumn(tvAbinamenti.getTree(),SWT.LEFT,7);
		tc7.setWidth(150);		
		tc7.addSelectionListener(getSelectionAdapter(tc7, 7));
		
		TreeColumn tc8 = new TreeColumn(tvAbinamenti.getTree(),SWT.LEFT,8);
		tc8.setWidth(150);
		tc8.addSelectionListener(getSelectionAdapter(tc8, 8));
				
		TreeColumn tc9 = new TreeColumn(tvAbinamenti.getTree(),SWT.LEFT,9);
		tc9.setWidth(150);
		tc9.addSelectionListener(getSelectionAdapter(tc9, 9));
		
		TreeColumn tc10 = new TreeColumn(tvAbinamenti.getTree(),SWT.LEFT,10);
		tc10.setWidth(150);
	/*	TreeColumn tc11 = new TreeColumn(tvAbinamenti.getTree(),SWT.CENTER,11);
		tc11.setWidth(150);
		TreeColumn tc12 = new TreeColumn(tvAbinamenti.getTree(),SWT.CENTER,12);
		tc10.setWidth(150);
*/
	}

	@Override
	public void setFocus() {


	}

	public AbbinamentiManuali getAbbinamentiManuali() {
		return abbinamentiManuali;
	}

	public void setAbbinamentiManuali(AbbinamentiManuali abbinamentiManuali) {
		this.abbinamentiManuali = abbinamentiManuali;
	}

	public AbbinamentiRicerca getAbbinamentiRicerca() {
		return abbinamentiRicerca;
	}

	public void setAbbinamentiRicerca(AbbinamentiRicerca abbinamentiRicerca) {
		this.abbinamentiRicerca = abbinamentiRicerca;
	}

	public TreeViewer getTvAnagrafiche() {
		return tvAbinamenti;
	}

	public ArrayList<?> getRicerca() {
		return ricerca;
	}

	public void setRicerca(ArrayList<?> resultRicerca) {
		this.ricerca = resultRicerca;
		abbinamentiRicerca = new AbbinamentiRicerca();
		abbinamentiRicerca.setAbbinamenti(this.ricerca);
		abbinameti.set(1, abbinamentiRicerca);
		tvAbinamenti.setInput(abbinameti);
		
	}

	public ArrayList<?> getManuale() {
		return manuale;
	}

	public void setManuale(ArrayList<Abbinamenti> resultManuale) {
		this.manuale = resultManuale;
		abbinamentiManuali = new AbbinamentiManuali();
		abbinamentiManuali.setAbbinamenti(this.manuale);
		abbinameti.set(0, abbinamentiManuali);
		tvAbinamenti.setInput(abbinameti);
	}

	public Immobili getImmobile() {
		return immobile;
	}

	public void setImmobile(Immobili immobileModel) {
		
		if (immobileModel != null){
			if (this.immobile == null){
				if (immobileModel != null){
					this.immobile = immobileModel;		

				}
			}else if ((immobileModel != null) && 
					  (this.immobile.getCodImmobile() != immobileModel.getCodImmobile())){
				
					this.immobile = immobileModel;		
				
			}else{
				tvAbinamenti.refresh();
			}
			this.anagrafica = null;
			this.colloquio = null;
			setManuale(new ArrayList(this.immobile.getAbbinamentis()));
			setRicerca(new ArrayList<winkhouse.orm.Abbinamenti>());	
			
		}else{
			this.immobile = null;
			this.colloquio = null;
			tvAbinamenti.setInput(new ArrayList<Abbinamenti>());
			tvAbinamenti.refresh();
		}
	}

	public Anagrafiche getAnagrafica() {
		return anagrafica;
	}

	public void setAnagrafica(Anagrafiche anagraficaModel) {
		
		if (anagraficaModel != null){
			if (this.anagrafica == null){
				if (anagraficaModel != null){
					this.anagrafica = anagraficaModel;		
					this.immobile = null;
					this.colloquio = null;
					setManuale(new ArrayList(this.anagrafica.getAbbinamentis()));
					setRicerca(new ArrayList<winkhouse.orm.Abbinamenti>());	
					
				}
			}else if ((anagraficaModel != null) && 
					  (this.anagrafica.getCodAnagrafica() != anagraficaModel.getCodAnagrafica())){
				
					this.anagrafica = anagraficaModel;		
					this.immobile = null;
					this.colloquio = null;
					setManuale(new ArrayList(this.anagrafica.getAbbinamentis()));					
					setRicerca(new ArrayList<winkhouse.orm.Abbinamenti>());	

			}else{
				tvAbinamenti.refresh();
			}
		}else{
			this.anagrafica = null;
			this.colloquio = null;
			tvAbinamenti.setInput(new Object());
			tvAbinamenti.refresh();			
		}
		
	}

	public void setCompareView(boolean enabled){
		
		aam.setEnabled(!enabled);
		ca.setEnabled(!enabled);		

	}

	public Colloqui getColloquio() {
		return colloquio;
	}

	public void setColloquio(Colloqui colloquio) {
		
		if (colloquio != null){
			this.immobile = null;
			this.anagrafica = null;
			this.colloquio = colloquio;;
			tvAbinamenti.setInput(new Object());
			tvAbinamenti.refresh();			
		}
	}
	
}
