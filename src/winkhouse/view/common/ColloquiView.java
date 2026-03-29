package winkhouse.view.common;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.cayenne.access.ToManyList;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.colloqui.NuovoColloquioAction;
import winkhouse.action.colloqui.RefreshColloqui;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Colloqui;
import winkhouse.orm.Immobili;
import winkhouse.view.colloqui.handler.DettaglioColloquioHandler;



public class ColloquiView extends ViewPart {

	public final static String ID = "winkhouse.colloquiview";
		
	private TreeViewer tvColloqui = null;
	private FormToolkit ft = null;
	private Form f = null;	
	private Immobili immobile = null;
	private Anagrafiche anagrafica = null;
	private Image colloquiVisiteImg = Activator.getImageDescriptor("icons/colloquivisite.png").createImage();
	private Image colloquiImg = Activator.getImageDescriptor("icons/colloqui16.png").createImage();
	private Image colloquiRicerca = Activator.getImageDescriptor("icons/colloquiricerca.png").createImage();
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	private NuovoColloquioAction nuovoColloquioAction = null; 
	private RefreshColloqui refreshColloqui = null;
	private ColumnSorter comparator = null;
	
	public ColloquiView() {
		
	}
	
	
	private class OrdinamentoData extends ViewerComparator{

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			Calendar c1 = Calendar.getInstance();			
			Calendar c2 = Calendar.getInstance();
			if ((e1 instanceof Colloqui) && 
				(e2 instanceof Colloqui)){
				c1.setTime(Date.from(((Colloqui)e1).getDatacolloquio().atZone(ZoneId.systemDefault()).toInstant()));
				c2.setTime(Date.from(((Colloqui)e2).getDatacolloquio().atZone(ZoneId.systemDefault()).toInstant()));				
			} 
			
			if (c1.before(c2)){
				return 1;
			}else if (c1.after(c2)){
				return -1;
			}else{
				return 0;
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
						
			LocalDateTime data1 = ((Colloqui)e1).getDatacolloquio();
			LocalDateTime data2 = ((Colloqui)e2).getDatacolloquio();
						
			String agente1 = (((Colloqui)e1).getAgenti1() != null)
					  ? ((Colloqui)e1).getAgenti1().getCognome() + " " + 
						((Colloqui)e1).getAgenti1().getNome()
					  : "";
			String agente2 = (((Colloqui)e1).getAgenti1() != null)
					  ? ((Colloqui)e1).getAgenti1().getCognome() + " " + 
						((Colloqui)e1).getAgenti1().getNome()
					  : "";
			
			String luogo1 = ((Colloqui)e1).getLuogo();
			String luogo2 = ((Colloqui)e2).getLuogo();
			
			switch (propertyIndex) {
			case 3: {
				if ((data1 == null) && (data2 == null)){
					returnValue = 0;
				}else if ((data1 != null) && (data2 == null)){
					returnValue = 1;
				}else if ((data1 == null) && (data2 != null)){
					returnValue = -1;
				}else{
					if (data1.isBefore(data2)){
						returnValue = 1;
					}else if (data1.isAfter(data2)){
						returnValue = -1;
					}else{
						returnValue = 0;
					}

					
				}
				break;
			}
			case 4: {
//				if ((agente1 == null) && (agente2 == null)){
//					returnValue = 0;
//				}else if ((agente1 != null) && (agente2 == null)){
//					returnValue = 1;
//				}else if ((agente1 == null) && (agente2 != null)){
//					returnValue = -1;
//				}else{
					returnValue = agente1.compareTo(agente2);
//				}
				break;
			}
			case 5: {
				if ((luogo1 == null) && (luogo2 == null)){
					returnValue = 0;
				}else if ((luogo1 != null) && (luogo2 == null)){
					returnValue = 1;
				}else if ((luogo1 == null) && (luogo2 != null)){
					returnValue = -1;
				}else{
					returnValue = luogo1.compareTo(luogo2);
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
                tvColloqui.getTree().setSortDirection(dir);
                tvColloqui.getTree().setSortColumn(column);
                tvColloqui.refresh();
            }
        };
        return selectionAdapter;
    }
	
	@Override
	public void createPartControl(Composite parent) {
		
		ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createForm(parent);
		f.setImage(Activator.getImageDescriptor("/icons/colloqui.png").createImage());
		f.setText("Colloqui");
		f.getBody().setLayout(new GridLayout());
		
		
		
		nuovoColloquioAction = new NuovoColloquioAction("Nuovo Colloquio", 
				 										Activator.getImageDescriptor("/icons/filenew.png")); 
		refreshColloqui = new RefreshColloqui("Ricarica colloqui", 
											  Activator.getImageDescriptor("/icons/adept_reinstall.png"));
				
		getViewSite().getActionBars().getToolBarManager().add(nuovoColloquioAction); 
		getViewSite().getActionBars().getToolBarManager().add(refreshColloqui);

		f.updateToolBar();

		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;	
		/*
		Composite toolbar = ft.createComposite(f.getBody(),SWT.NONE);
		toolbar.setLayout(new FillLayout(SWT.HORIZONTAL));
*/
		int colloquioType = 0;
		tvColloqui = new TreeViewer(f.getBody(),SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		tvColloqui.getTree().setLayoutData(gdExpVH);
		tvColloqui.getTree().setHeaderVisible(true);
		tvColloqui.getTree().setLinesVisible(true);
		tvColloqui.setContentProvider(new ColloquiContentProvider());
		tvColloqui.setLabelProvider(new ColloquiLabelProvider());
		tvColloqui.setComparator(new OrdinamentoData());
		tvColloqui.addDoubleClickListener(new IDoubleClickListener(){

			@Override
			public void doubleClick(DoubleClickEvent event) {
				
				TreeSelection ts = (TreeSelection)event.getSelection();
				if (ts.getFirstElement() instanceof Colloqui){
					
					Colloqui cm = (Colloqui)ts.getFirstElement();
					
					DettaglioColloquioHandler.getInstance()
											 .getDettaglioColloquio(cm);
					
//					IViewReference vr = PlatformUI.getWorkbench()
//												  .getActiveWorkbenchWindow()
//												  .getActivePage()
//												  .findViewReference(DettaglioColloquioView.ID,String.valueOf(cm.getCodColloquio()));
//					DettaglioColloquioView dcv = null;
//					try {
//						if (vr != null){
//							dcv = (DettaglioColloquioView)PlatformUI.getWorkbench()
//																    .getActiveWorkbenchWindow()
//																    .getActivePage()															 
//																    .showView(vr.getId(),
//																    		  vr.getSecondaryId(),
//																    		  IWorkbenchPage.VIEW_VISIBLE);
//						}else{
//							dcv = (DettaglioColloquioView)PlatformUI.getWorkbench()
//														 		    .getActiveWorkbenchWindow()
//														            .getActivePage()															 
//														            .showView(DettaglioColloquioView.ID,
//														            		  String.valueOf(cm.getCodColloquio()),
//														            		  IWorkbenchPage.VIEW_CREATE);
//							dcv.setColloquio(cm);
//						}
//						
//						PlatformUI.getWorkbench()
//			 		    		  .getActiveWorkbenchWindow()
//			 		    		  .getActivePage()
//			 		    		  .bringToTop(dcv);
//						
//					} catch (PartInitException e) {
//						e.printStackTrace();
//					}
					
				}				
				
			}
			
		});
		comparator = new ColumnSorter();
		tvColloqui.setComparator(comparator);
		
		TreeColumn column0 = new TreeColumn(tvColloqui.getTree(), SWT.LEFT, 0);
		column0.setWidth(20);

		TreeColumn column1 = new TreeColumn(tvColloqui.getTree(), SWT.LEFT, 1);
		column1.setWidth(25);		

		TreeColumn tcTipologia = new TreeColumn(tvColloqui.getTree(),SWT.CENTER,2);
		tcTipologia.setWidth(100);
		tcTipologia.setText("Tipologia");
		
		TreeColumn tcData = new TreeColumn(tvColloqui.getTree(),SWT.CENTER,3);
		tcData.setWidth(150);
		tcData.setText("Data colloquio");
		tcData.addSelectionListener(getSelectionAdapter(tcData, 3));
		
		TreeColumn tcAgente = new TreeColumn(tvColloqui.getTree(),SWT.CENTER,4);
		tcAgente.setWidth(150);
		tcAgente.setText("Agente inseritore");
		tcAgente.addSelectionListener(getSelectionAdapter(tcAgente, 4));
		
		TreeColumn tcLuogo = new TreeColumn(tvColloqui.getTree(),SWT.CENTER,5);
		tcLuogo.setWidth(200);
		tcLuogo.setText("Luogo");
		tcLuogo.addSelectionListener(getSelectionAdapter(tcLuogo, 5));

		TreeColumn tcCriteri = new TreeColumn(tvColloqui.getTree(),SWT.CENTER,6);
		tcCriteri.setWidth(200);
		tcCriteri.setText("Criteri ricerca");
		
		
	}

	@Override
	public void setFocus() {
		
		if (anagrafica != null){
			setAnagrafica(anagrafica);
		}
		if (immobile != null){
			setImmobile(immobile);
		}

	}

	public Immobili getImmobile() {
		return immobile;
	}

	public void setImmobile(Immobili immobile) {
		this.anagrafica = null;
		this.immobile = immobile;
		if (immobile != null){
			tvColloqui.setInput(immobile.getColloquis());
		}else{
			tvColloqui.setInput(new ArrayList());
		}
	}

	private class ColloquiContentProvider implements IStructuredContentProvider,
    												 ITreeContentProvider{

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		public Object[] getChildren(Object arg0) {

			Object[] returnValue = {arg0};			
			if ( arg0 instanceof ToManyList){
				returnValue = ((ToManyList<Colloqui>)arg0).toArray();
			}
			if ( arg0 instanceof List){
				returnValue = ((List<Colloqui>)arg0).toArray();
			}
			
//
//			if ( arg0 instanceof ColloquiModelVisiteCollection){
//				returnValue = ((ColloquiModelVisiteCollection)arg0).getColloquiVisite().toArray();
//			}
//			
//			if ( arg0 instanceof ColloquiModelAnagraficaCollection){
//				returnValue = ((ColloquiModelAnagraficaCollection)arg0).getColloqui().toArray();
//			}
//
//			if ( arg0 instanceof ColloquiModelRicercaCollection){
//				returnValue = ((ColloquiModelRicercaCollection)arg0).getColloquiRicerca().toArray();
//			}

			return returnValue;

		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return (getChildren(element).length==0)?false:true;
		}

	}
	
	private class ColloquiLabelProvider extends LabelProvider 
 										implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			Image returnValue = null;
			if (columnIndex == 1){
				if (element instanceof ArrayList){
					returnValue = null;
				}
				if (element instanceof Colloqui){
					switch (((Colloqui)element).getCodtipologiacolloquio()) {
					case 1: {						
						returnValue = colloquiRicerca;
					}
					case 2: {
						returnValue = colloquiVisiteImg;
					}					
					default:
						returnValue = colloquiImg;
					}
				
				}								
				
			}
			return returnValue;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			
			String returnValue = null;
			
			if (element instanceof Colloqui){
				if (columnIndex == 2){
					if (immobile != null){
						returnValue = "Colloqui immobile";
					}else if(anagrafica != null) {
						returnValue = "Colloqui anagrafiche";
					}
				}				
			}
			
			if (element instanceof Colloqui){
				if (columnIndex == 2){
					
					switch (((Colloqui)element).getCodtipologiacolloquio()) {
					case 1: {						
						returnValue = "Ricerca";
						break;
					}
					case 2: {
						returnValue = "Visita";
						break;
					}					
					default:
						returnValue = "Generico";
						break;
					}				
				}
				if (columnIndex == 3){	
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
					returnValue = ((Colloqui)element).getDatacolloquio().format(formatter);
				}
				if (columnIndex == 4){
					returnValue = (((Colloqui)element).getAgenti1() != null)
								  ? ((Colloqui)element).getAgenti1().getCognome() + " " + 
									((Colloqui)element).getAgenti1().getNome()
								  : ""; 
				}
				if (columnIndex == 5){
					returnValue = ((Colloqui)element).getLuogo();
				}
				if (columnIndex == 6){
					String descriteria = "";
//					for (Object iterable_element : ((ColloquiModel)element).getCriteriRicerca()) {
//						if (iterable_element instanceof ColloquiCriteriRicercaModel){
//							descriteria = descriteria + ((ColloquiCriteriRicercaModel)iterable_element).toString();	
//						}
//						
//					}
					returnValue = descriteria;
				}				
				
			}
			
			return returnValue;
		}		
		
	}

	public Anagrafiche getAnagrafica() {
		return anagrafica;
	}

	public void setAnagrafica(Anagrafiche anagrafica) {
		if (anagrafica != null){
			this.anagrafica = anagrafica;		
			this.immobile = null;
			tvColloqui.setInput(anagrafica.getColloquianagrafiches().stream()
					  .map(ca -> ca.getColloqui()).collect(Collectors.toList()));
		}
	}

	public TreeViewer getTvColloqui() {
		return tvColloqui;
	}

	public void setCompareView(boolean enabled){
		
//		tvColloqui.getTree().setEnabled(!enabled);
		nuovoColloquioAction.setEnabled(!enabled); 
		refreshColloqui.setEnabled(!enabled);		

	}
}
