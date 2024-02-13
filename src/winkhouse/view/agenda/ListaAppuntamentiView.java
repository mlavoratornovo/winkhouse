package winkhouse.view.agenda;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.nebula.widgets.calendarcombo.CalendarCombo;
import org.eclipse.nebula.widgets.calendarcombo.ICalendarListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.WizardGCalendarSyncAction;
import winkhouse.action.agenda.CancellaAppuntamentoAction;
import winkhouse.action.agenda.CercaAppuntamentiAction;
import winkhouse.action.agenda.NuovoAppuntamentoAction;
import winkhouse.dao.AgentiDAO;
import winkhouse.model.AgentiModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.ColloquiModel;
import winkhouse.orm.Appuntamenti;
import winkhouse.view.agenda.handler.DettaglioAppuntamentoHandler;
import winkhouse.view.colloqui.DettaglioColloquioView;



public class ListaAppuntamentiView extends ViewPart {

	public final static String ID = "winkhouse.listaappuntamentiview";
	private FormToolkit ft = null;
	private Form f = null;
	private TableViewer tvAgenda = null;
	private CalendarCombo dataDa = null;
	private CalendarCombo dataA = null;
	private ComboViewer cvAgenti = null;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private Image imgAppuntamento = Activator.getImageDescriptor("icons/korgac16.png").createImage();
	private Image imgColloquio = Activator.getImageDescriptor("icons/colloqui16.png").createImage();
	private Image imgCalendar = Activator.getImageDescriptor("icons/google_calendar.png").createImage();
	private AgentiModel agenteSearch = null;
	private Date dataDASearch = null;
	private Calendar calDA = null;
	private Calendar calA = null;
	private Date dataASearch = null;
	private ColumnSorter comparator = null;
	
	public ListaAppuntamentiView() {

	}
	
	private class ColumnSorter extends ViewerComparator {
		
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
			
			Date dDA1 = (e1 instanceof AppuntamentiModel)
						? ((AppuntamentiModel)e1).getDataAppuntamento()
						: ((ColloquiModel)e1).getDataColloquio();
			Date dDA2 = (e2 instanceof AppuntamentiModel)
						? ((AppuntamentiModel)e2).getDataAppuntamento()
						: ((ColloquiModel)e2).getDataColloquio();;

			Date dA1 = (e1 instanceof AppuntamentiModel)
						? ((AppuntamentiModel)e1).getDataFineAppuntamento()
						: null;
			Date dA2 = (e2 instanceof AppuntamentiModel)
						? ((AppuntamentiModel)e2).getDataFineAppuntamento()
						: null;
						
			String luogo1 = (e1 instanceof AppuntamentiModel)
							? ((AppuntamentiModel)e1).getLuogo()
							: ((ColloquiModel)e1).getLuogoIncontro();
			String luogo2 = (e2 instanceof AppuntamentiModel)
							? ((AppuntamentiModel)e2).getLuogo()
							: ((ColloquiModel)e2).getLuogoIncontro();
			
			String anagrafiche1 = (e1 instanceof AppuntamentiModel)
								   ? ((AppuntamentiModel)e1).getAnagraficheDescription()
								   : ((ColloquiModel)e1).getAnagraficheDescription();
			String anagrafiche2 = (e2 instanceof AppuntamentiModel)
								   ? ((AppuntamentiModel)e2).getAnagraficheDescription()
								   : ((ColloquiModel)e2).getAnagraficheDescription();

			String agenti1 = (e1 instanceof AppuntamentiModel)
							  ? ((AppuntamentiModel)e1).getAgentiDescription()
							  : ((ColloquiModel)e1).getAgentiDescription();
							  
			String agenti2 = (e2 instanceof AppuntamentiModel)
					  		  ? ((AppuntamentiModel)e2).getAgentiDescription()
					  		  : ((ColloquiModel)e2).getAgentiDescription();

			switch (propertyIndex) {
			case 2: {
				if ((dDA1 == null) && (dDA2 == null)){
					returnValue = 0;
				}else if ((dDA1 != null) && (dDA2 == null)){
					returnValue = 1;
				}else if ((dDA1 == null) && (dDA2 != null)){
					returnValue = -1;
				}else{
					if (dDA1.before(dDA2)){
						returnValue = 1;
					}else if (dDA1.after(dDA2)){
						returnValue = -1;
					}else{
						returnValue = 0;
					}
				}
				break;
			}
			case 3: {
				if ((dA1 == null) && (dA2 == null)){
					returnValue = 0;
				}else if ((dA1 != null) && (dA2 == null)){
					returnValue = 1;
				}else if ((dA1 == null) && (dA2 != null)){
					returnValue = -1;
				}else{
					if (dA1.before(dA2)){
						returnValue = 1;
					}else if (dA1.after(dA2)){
						returnValue = -1;
					}else{
						returnValue = 0;
					}
				}
				break;
			}
			case 4: {
				if ((agenti1 == null) && (agenti2 == null)){
					returnValue = 0;
				}else if ((agenti1 != null) && (agenti2 == null)){
					returnValue = 1;
				}else if ((agenti1 == null) && (agenti2 != null)){
					returnValue = -1;
				}else{
					returnValue = agenti1.compareTo(agenti2);
				}
				break;
			}
			case 5: {
				if ((anagrafiche1 == null) && (anagrafiche2 == null)){
					returnValue = 0;
				}else if ((anagrafiche1 != null) && (anagrafiche2 == null)){
					returnValue = 1;
				}else if ((anagrafiche1 == null) && (anagrafiche2 != null)){
					returnValue = -1;
				}else{
					returnValue = anagrafiche1.compareTo(anagrafiche2);
				}
				break;
			} 
			case 6: {
				if ((agenti1 == null) && (agenti2 == null)){
					returnValue = 0;
				}else if ((agenti1 != null) && (agenti2 == null)){
					returnValue = 1;
				}else if ((agenti1 == null) && (agenti2 != null)){
					returnValue = -1;
				}else{
					returnValue = agenti1.compareTo(agenti2);
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
			if ((e1 instanceof AppuntamentiModel) && 
				(e2 instanceof AppuntamentiModel)){
				c1.setTime(((AppuntamentiModel)e1).getDataAppuntamento());
				c2.setTime(((AppuntamentiModel)e2).getDataAppuntamento());				
			} 
			if ((e1 instanceof ColloquiModel) && 
				(e2 instanceof AppuntamentiModel)){
				c1.setTime(((ColloquiModel)e1).getDataColloquio());
				c2.setTime(((AppuntamentiModel)e2).getDataAppuntamento());				
			} 
			if ((e1 instanceof AppuntamentiModel) && 
				(e2 instanceof ColloquiModel)){
				c1.setTime(((AppuntamentiModel)e1).getDataAppuntamento());
				c2.setTime(((ColloquiModel)e2).getDataColloquio());				
			} 
			if ((e1 instanceof ColloquiModel) && 
				(e2 instanceof ColloquiModel)){
				c1.setTime(((ColloquiModel)e1).getDataColloquio());
				c2.setTime(((ColloquiModel)e2).getDataColloquio());				
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
	
	private class inputDataDa extends ControlContribution{
		
		public inputDataDa(String id){
			super(id);
		}

		@Override
		protected Control createControl(Composite parent) {
			
			Composite c = new Composite(parent, SWT.NONE);
			GridLayout gl = new GridLayout(3, false);
			c.setLayout(gl);
			Label ldataDA = new Label(c, SWT.NONE);
			ldataDA.setText("Filtro data da :");
			dataDa = new CalendarCombo(c, SWT.READ_ONLY);

			dataDa.setDate(calDA);
			dataDa.addCalendarListener(new ICalendarListener() {
				
				@Override
				public void popupClosed() {
				}
				
				@Override
				public void dateRangeChanged(Calendar arg0, Calendar arg1) {
				}
				
				@Override
				public void dateChanged(Calendar arg0) {
					dataDASearch = arg0.getTime();					
				}
			});
			
			ImageHyperlink ihConferma = new ImageHyperlink(c, SWT.WRAP);		
			ihConferma.setImage(Activator.getImageDescriptor("icons/button_cancel12.png").createImage());
			ihConferma.setHoverImage(Activator.getImageDescriptor("icons/button_cancel12.png").createImage());
			ihConferma.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent e) {
					dataDa.setText("");
					dataDASearch = null;
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
					
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					
				}
			});
			return c;
			
		}
		
	}
	
	private class inputDataA extends ControlContribution{
		
		public inputDataA(String id){
			super(id);
		}

		@Override
		protected Control createControl(Composite parent) {

			Composite c = new Composite(parent, SWT.NONE);
			GridLayout gl = new GridLayout(3, false);
			c.setLayout(gl);
			Label ldataA = new Label(c, SWT.NONE);
			ldataA.setText("Filtro data a :");
			dataA = new CalendarCombo(c, SWT.READ_ONLY);
			
			dataA.setDate(calA);
			dataA.addCalendarListener(new ICalendarListener() {
				
				@Override
				public void popupClosed() {
					
				}
				
				@Override
				public void dateRangeChanged(Calendar arg0, Calendar arg1) {
				}
				
				@Override
				public void dateChanged(Calendar arg0) {
					dataASearch = arg0.getTime();
				}
			});
			
			ImageHyperlink ihConferma = new ImageHyperlink(c, SWT.WRAP);		
			ihConferma.setImage(Activator.getImageDescriptor("icons/button_cancel12.png").createImage());
			ihConferma.setHoverImage(Activator.getImageDescriptor("icons/button_cancel12.png").createImage());
			ihConferma.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent e) {
					dataA.setText("");
					dataASearch = null;

				}
				
				@Override
				public void mouseDown(MouseEvent e) {
					
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					
				}
			});
			return c;
			
		}
		
	}
	
	public class inputAgente extends ControlContribution{
		
		public inputAgente(String id){
			super(id);
		}

		@Override
		protected Control createControl(Composite parent) {
			Composite c = new Composite(parent, SWT.NONE);
			GridLayout gl = new GridLayout(3, false);
			c.setLayout(gl);
			Label lAgente = new Label(c, SWT.NONE);
			lAgente.setText("Filtro agente :");
			cvAgenti = new ComboViewer(c,SWT.DROP_DOWN | SWT.READ_ONLY | SWT.DOUBLE_BUFFERED);
			cvAgenti.addSelectionChangedListener(new ISelectionChangedListener() {
				
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					StructuredSelection ss = (StructuredSelection)event.getSelection();
					agenteSearch = (AgentiModel)ss.getFirstElement();					
				}
			});
			cvAgenti.setContentProvider(new IStructuredContentProvider() {
				
				@Override
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
					
				}
				
				@Override
				public void dispose() {
					
				}

				@Override
				public Object[] getElements(Object inputElement) {
					AgentiDAO aDAO = new AgentiDAO();
					return aDAO.list(AgentiModel.class.getName()).toArray();
				}
			});
			cvAgenti.setLabelProvider(new LabelProvider() {

				@Override
				public String getText(Object element) {
					return super.getText(((AgentiModel)element).getNome() + " " + ((AgentiModel)element).getCognome());
				}
				
			});
					
			ImageHyperlink ihConferma = new ImageHyperlink(c, SWT.WRAP);		
			ihConferma.setImage(Activator.getImageDescriptor("icons/button_cancel12.png").createImage());
			ihConferma.setHoverImage(Activator.getImageDescriptor("icons/button_cancel12.png").createImage());
			ihConferma.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent e) {
					cvAgenti.setInput(new Object());
					agenteSearch = null;
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
					
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					
				}
			});
			cvAgenti.setInput(new Object());
			return c;
		}
		
		
	}

	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
        SelectionAdapter selectionAdapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                comparator.setColumn(index);
                int dir = comparator.getDirection();
                tvAgenda.getTable().setSortDirection(dir);
                tvAgenda.getTable().setSortColumn(column);
                tvAgenda.refresh();
            }
        };
        return selectionAdapter;
    }
	
	@Override
	public void createPartControl(Composite parent) {
		calDA = Calendar.getInstance();
		calDA.setTime(new Date());
		calDA.set(Calendar.HOUR_OF_DAY, 0);
		calDA.set(Calendar.MINUTE, 0);
		calDA.set(Calendar.SECOND, 0);						
		calA = Calendar.getInstance();
		calA.setTime(new Date());
		calA.set(Calendar.HOUR_OF_DAY, 23);
		calA.set(Calendar.MINUTE, 59);
		calA.set(Calendar.SECOND, 59);						

		dataDASearch = calDA.getTime();
		dataASearch = calA.getTime();
		

		ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createForm(parent);
		f.setImage(Activator.getImageDescriptor("/icons/korgac.png").createImage());
		f.setText("Appuntamenti");
		f.getBody().setLayout(new GridLayout());
		IToolBarManager mgrView = getViewSite().getActionBars().getToolBarManager();
		IToolBarManager mgr = f.getToolBarManager();
		NuovoAppuntamentoAction naa = new NuovoAppuntamentoAction("Nuovo appuntamento", 
																  Activator.getImageDescriptor("icons/sample2.gif"));		
		mgrView.add(naa);
		CancellaAppuntamentoAction caa = new CancellaAppuntamentoAction(ListaAppuntamentiView.class.getName());
		mgrView.add(caa);
		WizardGCalendarSyncAction wgcsa = new WizardGCalendarSyncAction("wizard sincronizzazione con GCalendar",
        																Activator.getImageDescriptor("icons/google_calendar.png"));
		//GoogleCalendarAction gca = new GoogleCalendarAction();
		mgrView.add(wgcsa);
		
		mgr.add(new inputAgente("agente"));
		mgr.add(new inputDataDa("datada"));
		mgr.add(new inputDataA("dataa"));
		mgr.add(new CercaAppuntamentiAction(ListaAppuntamentiView.class.getName()));
		f.updateToolBar();

		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;
				
		tvAgenda = new TableViewer(f.getBody(),SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION|SWT.MULTI);
		tvAgenda.getTable().setLayoutData(gdExpVH);
		tvAgenda.getTable().setHeaderVisible(true);
		tvAgenda.getTable().setLinesVisible(true);
		
		tvAgenda.setSorter(new OrdinamentoData());
		
		tvAgenda.getTable().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Object o = ((StructuredSelection)tvAgenda.getSelection()).getFirstElement();
				if (o instanceof Appuntamenti){
					Appuntamenti apm = (Appuntamenti)o;
					DettaglioAppuntamentoView dav = DettaglioAppuntamentoHandler.getInstance()
																				.getDettaglioAppuntamento(apm);
				}
				if (o instanceof ColloquiModel){
					ColloquiModel cm = (ColloquiModel)o;
					IViewReference vr = PlatformUI.getWorkbench()
												  .getActiveWorkbenchWindow()
												  .getActivePage()
												  .findViewReference(DettaglioColloquioView.ID,String.valueOf(cm.getCodColloquio()));
					DettaglioColloquioView dcv = null;
					try {
						if (vr != null){
							dcv = (DettaglioColloquioView)PlatformUI.getWorkbench()
																	.getActiveWorkbenchWindow()
																	.getActivePage()															 
																	.showView(vr.getId(),vr.getSecondaryId(),IWorkbenchPage.VIEW_VISIBLE);
						}else{
							dcv = (DettaglioColloquioView)PlatformUI.getWorkbench()
														 		    .getActiveWorkbenchWindow()
														            .getActivePage()															 
														            .showView(DettaglioColloquioView.ID,String.valueOf(cm.getCodColloquio()),IWorkbenchPage.VIEW_CREATE);
							dcv.setColloquio(cm);
						}

						PlatformUI.getWorkbench()
								  .getActiveWorkbenchWindow()
								  .getActivePage()
								  .bringToTop(dcv);

					} catch (PartInitException ex) {
						ex.printStackTrace();
					}
				}				
			}
		});
		
		tvAgenda.setContentProvider(new IStructuredContentProvider() {
			
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
		
		tvAgenda.setLabelProvider(new ITableLabelProvider() {
			
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
					case 2: return (element instanceof AppuntamentiModel)
									? formatterDateTime.format(((AppuntamentiModel)element).getDataAppuntamento())
									: formatterDateTime.format(((ColloquiModel)element).getDataColloquio());
					case 3: return (element instanceof AppuntamentiModel)
									? formatterDateTime.format(((AppuntamentiModel)element).getDataFineAppuntamento())
									: "";									
					case 4: return (element instanceof AppuntamentiModel)
									? ((AppuntamentiModel)element).getLuogo()
									: ((ColloquiModel)element).getLuogoIncontro();
					case 5: return (element instanceof AppuntamentiModel)
									? ((AppuntamentiModel)element).getAnagraficheDescription()
									: ((ColloquiModel)element).getAnagraficheDescription();
					case 6: return (element instanceof AppuntamentiModel)
									? ((AppuntamentiModel)element).getAgentiDescription()
									: ((ColloquiModel)element).getAgentiDescription();
					case 7: return (element instanceof AppuntamentiModel)
									? ((AppuntamentiModel)element).getDescrizione()
									: ((ColloquiModel)element).getDescrizione();
					default : return "";
				}				
			}
			
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				if (columnIndex == 0){
					return (element instanceof AppuntamentiModel)?imgAppuntamento:imgColloquio;
				}else if (columnIndex == 1){
					if (element instanceof AppuntamentiModel){
						if (((AppuntamentiModel)element).getWinkGCalendarModels().size() == 0){
							return null;
						}else{
							return imgCalendar;
						}
					}else if (element instanceof ColloquiModel){
						if (((ColloquiModel)element).getiCalUid() == null || ((ColloquiModel)element).getiCalUid().equalsIgnoreCase("")){
							return null;
						}else{
							return imgCalendar;
						}
						
					}else{
						return null;
					}
				}else{
					return null;
				}
			}
		});
		comparator = new ColumnSorter();
		tvAgenda.setComparator(comparator);
		
		TableColumn tcIcon = new TableColumn(tvAgenda.getTable(),SWT.CENTER,0);
		tcIcon.setWidth(26);				

		TableColumn tcIconCalendar = new TableColumn(tvAgenda.getTable(),SWT.CENTER,1);
		tcIconCalendar.setWidth(24);				
		
		TableColumn tcData = new TableColumn(tvAgenda.getTable(),SWT.CENTER,2);
		tcData.setWidth(150);
		tcData.setText("Data appuntamento");		
		tcData.addSelectionListener(getSelectionAdapter(tcData, 2));
		
		TableColumn tcDataFine = new TableColumn(tvAgenda.getTable(),SWT.CENTER,3);
		tcDataFine.setWidth(150);
		tcDataFine.setText("Data fine appuntamento");
		tcDataFine.addSelectionListener(getSelectionAdapter(tcDataFine, 3));
		
		TableColumn tcLuogo = new TableColumn(tvAgenda.getTable(),SWT.CENTER,4);
		tcLuogo.setWidth(150);
		tcLuogo.setText("Luogo");
		tcLuogo.addSelectionListener(getSelectionAdapter(tcDataFine, 4));
		
		TableColumn tcAnagrafica = new TableColumn(tvAgenda.getTable(),SWT.CENTER,5);
		tcAnagrafica.setWidth(200);
		tcAnagrafica.setText("Anagrafiche");		
		tcAnagrafica.addSelectionListener(getSelectionAdapter(tcAnagrafica, 5));
		
		TableColumn tcAgenti = new TableColumn(tvAgenda.getTable(),SWT.CENTER,6);
		tcAgenti.setWidth(200);
		tcAgenti.setText("Agenti");		
		tcAgenti.addSelectionListener(getSelectionAdapter(tcAgenti, 6));
		
		TableColumn tcNote = new TableColumn(tvAgenda.getTable(),SWT.CENTER,7);
		tcNote.setWidth(200);
		tcNote.setText("Note");
		//tcLuogo.addSelectionListener(getSelectionAdapter(tcDataFine, 4));

	}

	@Override
	public void setFocus() {

		cvAgenti.setInput(new Object());
	}

	public CalendarCombo getDataDa() {
		return dataDa;
	}

	public AgentiModel getAgenteSearch() {
		return agenteSearch;
	}

	public Date getDataASearch() {
		return dataASearch;
	}

	public Date getDataDASearch() {
		return dataDASearch;
	}

	public void setSearchResults(ArrayList results){
		tvAgenda.setInput(results);
	}

	public TableViewer getTvAgenda() {
		return tvAgenda;
	}
}
