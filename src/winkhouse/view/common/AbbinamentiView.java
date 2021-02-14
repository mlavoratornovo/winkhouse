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
import winkhouse.model.AbbinamentiModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.TipologieColloquiVO;



public class AbbinamentiView extends ViewPart {

	public final static String ID = "winkhouse.anagraficheabbinateview";
	private TreeViewer tvAbinamenti = null;
	private ImmobiliModel immobile = null;
	private ColloquiModel colloquio = null;
	private AnagraficheModel anagrafica = null;
	private ArrayList ricerca = null;
	private ArrayList manuale = null;
	
	private ArrayList abbinnameti = new ArrayList(2);
	
	private AbbinamentiManuali abbinamentiManuali = null;
	private AbbinamentiRicerca abbinamentiRicerca = null;
	
	private Image abbinamentiImg = Activator.getImageDescriptor("icons/anagraficheabbinate12.png").createImage();
	private Image abbinamentiManualiImg = Activator.getImageDescriptor("icons/abbinamenti12.png").createImage();
	private Image abbinamentiRicercaImg = Activator.getImageDescriptor("icons/kfind14.png").createImage();
	private Image anagraficheImg = Activator.getImageDescriptor("icons/anagrafica12.png").createImage();
	private Image immobiliImg = Activator.getImageDescriptor("icons/gohome12.png").createImage();

	private AbbinamentiManuale aam = null;			
	private CancellaAbbinamenti ca = null;
	private ColumnSorter comparator = null;
	
	private class AbbinamentiRicerca{
		
		private ArrayList abbinamenti = null;
		
		public AbbinamentiRicerca(){}

		public ArrayList getAbbinamenti() {
			return this.abbinamenti;
		}

		public void setAbbinamenti(ArrayList abbinamenti) {
			this.abbinamenti = abbinamenti;
		}
		
	}
	
	private class AbbinamentiManuali{
		
		private ArrayList abbinamenti = null;
		
		public AbbinamentiManuali(){}

		public ArrayList getAbbinamenti() {
			return this.abbinamenti;
		}

		public void setAbbinamenti(ArrayList abbinamenti) {
			this.abbinamenti = abbinamenti;
		}
		
	}
	
	private class StartModel{
		public StartModel(){}
	};
	
	
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
			return (element instanceof AbbinamentiModel)?false:true;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof AbbinamentiManuali){
				return (((AbbinamentiManuali)inputElement).getAbbinamenti() != null)
					   ?((AbbinamentiManuali)inputElement).getAbbinamenti().toArray()
					   : new ArrayList().toArray();
			}else if (inputElement instanceof AbbinamentiRicerca){
				return (((AbbinamentiRicerca)inputElement).getAbbinamenti() != null)
					   ?((AbbinamentiRicerca)inputElement).getAbbinamenti().toArray()
					   : new ArrayList().toArray();
			}else if (inputElement instanceof ArrayList){
				return ((ArrayList)inputElement).toArray();
			}else{
				return new ArrayList().toArray();
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
			}else if ((columnIndex == 3) && (element instanceof AbbinamentiModel)){

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
									   .equalsIgnoreCase(ImmobiliModel.class.getName())){
					   return anagraficheImg;
				   }else if (WinkhouseUtils.getInstance()
						   					 .getLastEntityTypeFocused()
						   					 .equalsIgnoreCase(AnagraficheModel.class.getName())){
					   return immobiliImg;
				   }else{
					   return null;
				   }
				
				}else{
					return null;
				}
				
			}else if ((columnIndex == 3) && (element instanceof AnagraficheModel)){

				return anagraficheImg;
					   
			}else if ((columnIndex == 3) && (element instanceof ImmobiliModel)){
				
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
				if (element instanceof AbbinamentiModel){
					return (immobile != null)
						   ? ((AbbinamentiModel)element).getAnagrafica().getNome()
						   : (anagrafica != null)
						   	 ? ((AbbinamentiModel)element).getImmobile().getDescrizione()
						   	 : "";
				}else if (element instanceof ImmobiliModel){
					return ((ImmobiliModel)element).getDescrizione();
				}else if (element instanceof AnagraficheModel){
					return ((AnagraficheModel)element).getNome();
				}else return "";				
			}else if (columnIndex == 5){
				if (element instanceof AbbinamentiModel){
					return (immobile != null)
						   ? ((AbbinamentiModel)element).getAnagrafica().getCognome()
						   : (anagrafica != null)
						   	 ? ((AbbinamentiModel)element).getImmobile().getCitta()
						   	 : "";
				}else if (element instanceof ImmobiliModel){
					return ((ImmobiliModel)element).getCitta();
				}else if (element instanceof AnagraficheModel){
					return ((AnagraficheModel)element).getCognome();
				}else return "";				
			}else if (columnIndex == 6){
				if (element instanceof AbbinamentiModel){
					return (immobile != null)
						   ? ((AbbinamentiModel)element).getAnagrafica().getIndirizzo()
						   : (anagrafica != null)
						   	 ? ((AbbinamentiModel)element).getImmobile().getIndirizzo()
						   	 : "";
				}else if (element instanceof ImmobiliModel){
					return ((ImmobiliModel)element).getIndirizzo();
				}else if (element instanceof AnagraficheModel){
					return ((AnagraficheModel)element).getIndirizzo();
				}else return "";				
			}else if (columnIndex == 7){
				if (element instanceof AbbinamentiModel){
					return (immobile != null)
						   ? ((AbbinamentiModel)element).getAnagrafica().getProvincia()
						   : (anagrafica != null)
						   	 ? ((AbbinamentiModel)element).getImmobile().getProvincia()
						   	 : "";
				}else if (element instanceof ImmobiliModel){
					return ((ImmobiliModel)element).getProvincia();
				}else if (element instanceof AnagraficheModel){
					return ((AnagraficheModel)element).getProvincia();
				}else return "";				
			}else if (columnIndex == 8){
				if (element instanceof AbbinamentiModel){
					return (immobile != null)
						   ? ((AbbinamentiModel)element).getAnagrafica().getCitta()
						   : (anagrafica != null)
						   	 ? String.valueOf(((AbbinamentiModel)element).getImmobile().getPrezzo())
						   	 : "";
				}else if (element instanceof ImmobiliModel){
					return String.valueOf(((ImmobiliModel)element).getPrezzo());
				}else if (element instanceof AnagraficheModel){
					return ((AnagraficheModel)element).getCitta();
				}else return "";				
			}else if (columnIndex == 9){
				if (element instanceof AbbinamentiModel){
					return (immobile != null)
						   ? ((AbbinamentiModel)element).getAnagrafica().getCap()
						   : (anagrafica != null)
						   	 ? String.valueOf(((AbbinamentiModel)element).getImmobile().getMq())
						   	 : "";
				}else if (element instanceof ImmobiliModel){
					return String.valueOf(((ImmobiliModel)element).getMq());
				}else if (element instanceof AnagraficheModel){
					return ((AnagraficheModel)element).getCap();
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
			
			if (e1 instanceof AbbinamentiModel){
				col4_1 = (immobile != null)
					   ? ((AbbinamentiModel)e1).getAnagrafica().getNome()
					   : (anagrafica != null)
					   	 ? ((AbbinamentiModel)e1).getImmobile().getDescrizione()
					   	 : "";
			}else if (e1 instanceof ImmobiliModel){
				col4_1 = ((ImmobiliModel)e1).getDescrizione();
			}else if (e1 instanceof AnagraficheModel){
				col4_1 = ((AnagraficheModel)e1).getNome();
			}else{ col4_1 =  "";}

			if (e2 instanceof AbbinamentiModel){
				col4_2 = (immobile != null)
					   ? ((AbbinamentiModel)e2).getAnagrafica().getNome()
					   : (anagrafica != null)
					   	 ? ((AbbinamentiModel)e2).getImmobile().getDescrizione()
					   	 : "";
			}else if (e2 instanceof ImmobiliModel){
				col4_2 = ((ImmobiliModel)e2).getDescrizione();
			}else if (e2 instanceof AnagraficheModel){
				col4_2 = ((AnagraficheModel)e2).getNome();
			}else{ col4_2 =  "";}
			
			String col5_1 = null;
			String col5_2 = null;
			
			if (e1 instanceof AbbinamentiModel){
				col5_1 = (immobile != null)
					   ? ((AbbinamentiModel)e1).getAnagrafica().getCognome()
					   : (anagrafica != null)
					   	 ? ((AbbinamentiModel)e1).getImmobile().getCitta()
					   	 : "";
			}else if (e1 instanceof ImmobiliModel){
				col5_1 = ((ImmobiliModel)e1).getCitta();
			}else if (e1 instanceof AnagraficheModel){
				col5_1 = ((AnagraficheModel)e1).getCognome();
			}else col5_1 = "";

			if (e2 instanceof AbbinamentiModel){
				col5_2 = (immobile != null)
					   ? ((AbbinamentiModel)e2).getAnagrafica().getCognome()
					   : (anagrafica != null)
					   	 ? ((AbbinamentiModel)e2).getImmobile().getCitta()
					   	 : "";
			}else if (e2 instanceof ImmobiliModel){
				col5_2 = ((ImmobiliModel)e2).getCitta();
			}else if (e2 instanceof AnagraficheModel){
				col5_2 = ((AnagraficheModel)e2).getCognome();
			}else col5_2 = "";

			
			String col6_1 = null;
			String col6_2 = null;

			if (e1 instanceof AbbinamentiModel){
				col6_1 = (immobile != null)
					   ? ((AbbinamentiModel)e1).getAnagrafica().getIndirizzo()
					   : (anagrafica != null)
					   	 ? ((AbbinamentiModel)e1).getImmobile().getIndirizzo()
					   	 : "";
			}else if (e1 instanceof ImmobiliModel){
				col6_1 = ((ImmobiliModel)e1).getIndirizzo();
			}else if (e1 instanceof AnagraficheModel){
				col6_1 = ((AnagraficheModel)e1).getIndirizzo();
			}else col6_1 = "";				

			if (e2 instanceof AbbinamentiModel){
				col6_2 = (immobile != null)
					   ? ((AbbinamentiModel)e2).getAnagrafica().getIndirizzo()
					   : (anagrafica != null)
					   	 ? ((AbbinamentiModel)e2).getImmobile().getIndirizzo()
					   	 : "";
			}else if (e2 instanceof ImmobiliModel){
				col6_2 = ((ImmobiliModel)e2).getIndirizzo();
			}else if (e2 instanceof AnagraficheModel){
				col6_2 = ((AnagraficheModel)e2).getIndirizzo();
			}else col6_2 = "";				

			
			String col7_1 = null;
			String col7_2 = null;

			if (e1 instanceof AbbinamentiModel){
				col7_1 =  (immobile != null)
					   ? ((AbbinamentiModel)e1).getAnagrafica().getProvincia()
					   : (anagrafica != null)
					   	 ? ((AbbinamentiModel)e1).getImmobile().getProvincia()
					   	 : "";
			}else if (e1 instanceof ImmobiliModel){
				col7_1 =  ((ImmobiliModel)e1).getProvincia();
			}else if (e1 instanceof AnagraficheModel){
				col7_1 =  ((AnagraficheModel)e1).getProvincia();
			}else col7_1 =  "";				

			if (e2 instanceof AbbinamentiModel){
				col7_2 =  (immobile != null)
					   ? ((AbbinamentiModel)e2).getAnagrafica().getProvincia()
					   : (anagrafica != null)
					   	 ? ((AbbinamentiModel)e2).getImmobile().getProvincia()
					   	 : "";
			}else if (e2 instanceof ImmobiliModel){
				col7_2 =  ((ImmobiliModel)e2).getProvincia();
			}else if (e2 instanceof AnagraficheModel){
				col7_2 =  ((AnagraficheModel)e2).getProvincia();
			}else col7_2 =  "";				

			String col8_1 = null;
			String col8_2 = null;

			if (e1 instanceof AbbinamentiModel){
				col8_1 =  (immobile != null)
					   ? ((AbbinamentiModel)e1).getAnagrafica().getCitta()
					   : (anagrafica != null)
					   	 ? String.valueOf(((AbbinamentiModel)e1).getImmobile().getPrezzo())
					   	 : "";
			}else if (e1 instanceof ImmobiliModel){
				col8_1 =  String.valueOf(((ImmobiliModel)e1).getPrezzo());
			}else if (e1 instanceof AnagraficheModel){
				col8_1 =  ((AnagraficheModel)e1).getCitta();
			}else col8_1 =  "";				

			if (e2 instanceof AbbinamentiModel){
				col8_2 =  (immobile != null)
					   ? ((AbbinamentiModel)e2).getAnagrafica().getCitta()
					   : (anagrafica != null)
					   	 ? String.valueOf(((AbbinamentiModel)e2).getImmobile().getPrezzo())
					   	 : "";
			}else if (e2 instanceof ImmobiliModel){
				col8_2 =  String.valueOf(((ImmobiliModel)e2).getPrezzo());
			}else if (e2 instanceof AnagraficheModel){
				col8_2 =  ((AnagraficheModel)e2).getCitta();
			}else col8_2 =  "";				

			String col9_1 = null;
			String col9_2 = null;
			
			if (e1 instanceof AbbinamentiModel){
				col9_1 = (immobile != null)
					   ? ((AbbinamentiModel)e1).getAnagrafica().getCap()
					   : (anagrafica != null)
					   	 ? String.valueOf(((AbbinamentiModel)e1).getImmobile().getMq())
					   	 : "";
			}else if (e1 instanceof ImmobiliModel){
				col9_1 = String.valueOf(((ImmobiliModel)e1).getMq());
			}else if (e1 instanceof AnagraficheModel){
				col9_1 = ((AnagraficheModel)e1).getCap();
			}else col9_1 =  "";				

			if (e2 instanceof AbbinamentiModel){
				col9_2 =  (immobile != null)
					   ? ((AbbinamentiModel)e2).getAnagrafica().getCap()
					   : (anagrafica != null)
					   	 ? String.valueOf(((AbbinamentiModel)e2).getImmobile().getMq())
					   	 : "";
			}else if (e2 instanceof ImmobiliModel){
				col9_2 =  String.valueOf(((ImmobiliModel)e2).getMq());
			}else if (e2 instanceof AnagraficheModel){
				col9_2 =  ((AnagraficheModel)e2).getCap();
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
		
		abbinnameti.add(new AbbinamentiManuali());
		abbinnameti.add(new AbbinamentiRicerca());
		
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
				if (((StructuredSelection)tvAbinamenti.getSelection()).getFirstElement() instanceof ImmobiliModel){
					ApriDettaglioImmobileAction adia = new ApriDettaglioImmobileAction((ImmobiliModel)((StructuredSelection)tvAbinamenti.getSelection()).getFirstElement(),null);
					adia.run();
				}
				if (((StructuredSelection)tvAbinamenti.getSelection()).getFirstElement() instanceof AnagraficheModel){
					ApriDettaglioAnagraficaAction adaa = new ApriDettaglioAnagraficaAction((AnagraficheModel)((StructuredSelection)tvAbinamenti.getSelection()).getFirstElement(), null);
					adaa.run();
					
				}				
				 
				if (((StructuredSelection)tvAbinamenti.getSelection()).getFirstElement() instanceof AbbinamentiModel){
					AbbinamentiModel am = (AbbinamentiModel)((StructuredSelection)tvAbinamenti.getSelection()).getFirstElement();
					if (immobile != null){
						ApriDettaglioAnagraficaAction adaa = new ApriDettaglioAnagraficaAction(am.getAnagrafica(), null);
						adaa.run();
					}else if (anagrafica != null){
						ApriDettaglioImmobileAction adia = new ApriDettaglioImmobileAction(am.getImmobile(),null);
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

	public ArrayList getRicerca() {
		return ricerca;
	}

	public void setRicerca(ArrayList resultRicerca) {
		this.ricerca = resultRicerca;
		abbinamentiRicerca = new AbbinamentiRicerca();
		abbinamentiRicerca.setAbbinamenti(this.ricerca);
		abbinnameti.set(1, abbinamentiRicerca);
		tvAbinamenti.setInput(abbinnameti);
		
	}

	public ArrayList getManuale() {
		return manuale;
	}

	public void setManuale(ArrayList resultManuale) {
		this.manuale = resultManuale;
		abbinamentiManuali = new AbbinamentiManuali();
		abbinamentiManuali.setAbbinamenti(this.manuale);
		abbinnameti.set(0, abbinamentiManuali);
		tvAbinamenti.setInput(abbinnameti);
	}

	public ImmobiliModel getImmobile() {
		return immobile;
	}

	public void setImmobile(ImmobiliModel immobileModel) {
		
		if (immobileModel != null){
			if (this.immobile == null){
				if (immobileModel != null){
					this.immobile = immobileModel;		
					this.anagrafica = null;
					this.colloquio = null;
					if (this.immobile.getCodImmobile() != null){
						AbbinamentiDAO aDAO = new AbbinamentiDAO();
						ArrayList abbinamentiImmobili = aDAO.findAbbinamentiByCodImmobile(AbbinamentiModel.class.getName(), 
								                                                  		  this.immobile.getCodImmobile());
						setManuale(abbinamentiImmobili);
					}
					setRicerca(new ArrayList());	

				}
			}else if ((immobileModel != null) && 
					  (this.immobile.getCodImmobile() != immobileModel.getCodImmobile())){
				
					this.immobile = immobileModel;		
					this.anagrafica = null;
					this.colloquio = null;
					if (this.immobile.getCodImmobile() != null){
						AbbinamentiDAO aDAO = new AbbinamentiDAO();
						ArrayList abbinamentiImmobili = aDAO.findAbbinamentiByCodImmobile(AbbinamentiModel.class.getName(), 
								                                                  		  this.immobile.getCodImmobile());
						setManuale(abbinamentiImmobili);
					}
					setRicerca(new ArrayList());	
				
			}else{
				tvAbinamenti.refresh();
			}
		}else{
			this.immobile = null;
			this.colloquio = null;
			tvAbinamenti.setInput(new ArrayList());
			tvAbinamenti.refresh();
		}
	}

	public AnagraficheModel getAnagrafica() {
		return anagrafica;
	}

	public void setAnagrafica(AnagraficheModel anagraficaModel) {
		
		if (anagraficaModel != null){
			if (this.anagrafica == null){
				if (anagraficaModel != null){
					this.anagrafica = anagraficaModel;		
					this.immobile = null;
					this.colloquio = null;
					AbbinamentiDAO aDAO = new AbbinamentiDAO();
					if (this.anagrafica.getCodAnagrafica() != null){
						ArrayList abbinamentiAnagrafica = aDAO.findAbbinamentiByCodAnagrafica(AbbinamentiModel.class.getName(), 
								                                                    		  this.anagrafica.getCodAnagrafica());
						setManuale(abbinamentiAnagrafica);
					}
					setRicerca(new ArrayList());	
					
				}
			}else if ((anagraficaModel != null) && 
					  (this.anagrafica.getCodAnagrafica() != anagraficaModel.getCodAnagrafica())){
				
					this.anagrafica = anagraficaModel;		
					this.immobile = null;
					this.colloquio = null;
					AbbinamentiDAO aDAO = new AbbinamentiDAO();
					ArrayList abbinamentiAnagrafica = aDAO.findAbbinamentiByCodAnagrafica(AbbinamentiModel.class.getName(), 
							                                                    		  this.anagrafica.getCodAnagrafica());
					setManuale(abbinamentiAnagrafica);
					
					setRicerca(new ArrayList());	

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

	public ColloquiModel getColloquio() {
		return colloquio;
	}

	public void setColloquio(ColloquiModel colloquio) {
		
		if (colloquio != null){
			this.immobile = null;
			this.anagrafica = null;
			this.colloquio = colloquio;;
			tvAbinamenti.setInput(new Object());
			tvAbinamenti.refresh();			
		}
	}
	
}
