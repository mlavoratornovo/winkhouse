package winkhouse.view.agenda;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

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
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
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
import winkhouse.action.agenda.CercaAppuntamentiAction;
import winkhouse.action.agenda.NextMonthAction;
import winkhouse.action.agenda.NextYearAction;
import winkhouse.action.agenda.NuovoAppuntamentoAction;
import winkhouse.action.agenda.PreviousMonthAction;
import winkhouse.action.agenda.PreviousYearAction;
import winkhouse.dao.AgentiDAO;
import winkhouse.model.AgentiModel;
import winkhouse.model.AppuntamentiModel;
import winkhouse.model.ColloquiModel;
import winkhouse.orm.Appuntamenti;
import winkhouse.orm.Colloqui;
import winkhouse.view.agenda.handler.DettaglioAppuntamentoHandler;
import winkhouse.view.colloqui.DettaglioColloquioView;

public class CalendarioView extends ViewPart {
	
	public final static String ID = "winkhouse.calendarioview";
	private Form form = null;
	private GridData gdHV = null;
	private FormToolkit ft = null;
	private CalendarioGroup[] giorni = new CalendarioGroup[42];
	private Font dayNumber = null;
	private ComboViewer cvAgenti = null;
	private Combo mesi = null;
	private Combo anni = null;
	private AgentiModel agenteSearch = null;
	private Image imgAppuntamento = Activator.getImageDescriptor("icons/korgac16.png").createImage();
	private Image imgColloquio = Activator.getImageDescriptor("icons/colloqui1612.png").createImage();
	private Image imgCalendar = Activator.getImageDescriptor("icons/google_calendar.png").createImage();

	public CalendarioView() {}

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
	
	public class CalendarioGroup {

		private Calendar cday = null;
		private TableViewer tv = null;
		private Group dayGroup = null;
		
		public CalendarioGroup(Composite parent, int style, Calendar cday) {
			
			dayGroup = new Group(parent, style);
			this.cday = cday;
			dayGroup.setLayout(new GridLayout());
			tv = new TableViewer(dayGroup,SWT.FLAT|SWT.FULL_SELECTION);
			tv.getTable().setLinesVisible(false);
			GridData gd = new GridData(SWT.FILL,SWT.FILL,true,true);

			tv.getTable().setLayoutData(gd);
			
		    TableColumn tcimage = new TableColumn(tv.getTable(), SWT.CENTER,0);
		    tcimage.setWidth(20);

		    TableColumn tcdes = new TableColumn(tv.getTable(), SWT.LEFT,1);
		    tcdes.setWidth(125);
		    
		    tv.setContentProvider(new IStructuredContentProvider() {
				
				@Override
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
				
				@Override
				public void dispose() {}
				
				@Override
				public Object[] getElements(Object inputElement) {
					if (inputElement instanceof ArrayList){
						return ((ArrayList)inputElement).toArray();
					}
					return null;
				}
			});
		    
		    tv.setLabelProvider(new ITableLabelProvider() {
				
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
					if (columnIndex == 1){
						if (element instanceof AppuntamentiModel){
							
							return ((AppuntamentiModel)element).getDescrizione();
						}else if (element instanceof ColloquiModel){
							return ((ColloquiModel)element).getDescrizione();
						}else{
							return "";									
						}						
						
					}
					return null;
				}
				
				@Override
				public Image getColumnImage(Object element, int columnIndex) {
					if (columnIndex == 0){
						if (element instanceof AppuntamentiModel){
							if (
									(((AppuntamentiModel)element).getiCalUID() == null) ||
									(((AppuntamentiModel)element).getiCalUID().equalsIgnoreCase(""))
								){
							    	return imgAppuntamento;
							}else{
									return imgCalendar;
							}							
						}else if (element instanceof ColloquiModel){
							return imgColloquio;
						}else{
							return null;									
						}	
					}
					return null;
				}
			});
		    tv.setSorter(new OrdinamentoData());
		    tv.getTable().addMouseListener(new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent e) {
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					Object o = ((StructuredSelection)tv.getSelection()).getFirstElement();
					if (o instanceof Appuntamenti){
						Appuntamenti apm = (Appuntamenti)o;
						DettaglioAppuntamentoView dav = DettaglioAppuntamentoHandler.getInstance()
																					.getDettaglioAppuntamento(apm);
					}
					if (o instanceof Colloqui){
						Colloqui cm = (Colloqui)o;
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

		}
		
		public Group getGroupControl(){
			return dayGroup;
		}

		public Calendar getCday() {
			return cday;
		}

		public void setCday(Calendar cday) {
			this.cday = cday;
		}
		
		public void setAppuntamenti(ArrayList appuntamenti){
			tv.setInput(appuntamenti);
		}

		public TableViewer getTv() {
			return tv;
		}
				
	} 
	
	
	public class monthSelector extends ControlContribution{
		
		public monthSelector(String id){
			super(id);
		}

		@Override
		protected Control createControl(Composite parent) {
			
			Composite c = new Composite(parent, SWT.NONE);
			GridLayout gl = new GridLayout(2, false);
			c.setLayout(gl);
			
			Label lMese = new Label(c, SWT.NONE);
			lMese.setText("Mese :");
			
			mesi = new Combo(c, SWT.NONE);
			mesi.add("Gennaio", 0);
			mesi.add("Febbraio", 1);
			mesi.add("Marzo", 2);
			mesi.add("Aprile", 3);
			mesi.add("Maggio", 4);
			mesi.add("Giugno", 5);
			mesi.add("Luglio", 6);
			mesi.add("Agosto", 7);
			mesi.add("Settembre", 8);
			mesi.add("Ottobre", 9);
			mesi.add("Novembre", 10);
			mesi.add("Dicembre", 11);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			mesi.select(cal.get(Calendar.MONTH));
			mesi.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
															
					Calendar c = Calendar.getInstance();
					c.set(Calendar.YEAR, Integer.valueOf(anni.getItem(anni.getSelectionIndex())));
					c.set(Calendar.MONTH, mesi.getSelectionIndex());
					c.set(Calendar.DAY_OF_MONTH, 1);
					setDaysLabel(c);
					
				}
				
			});
			return c;
		}
				
	}
	
	public class yearSelector extends ControlContribution{
		
		public yearSelector(String id){
			super(id);
		}

		@Override
		protected Control createControl(Composite parent) {
			
			Composite c = new Composite(parent, SWT.NONE);
			GridLayout gl = new GridLayout(2, false);
			c.setLayout(gl);
			
			Label lMese = new Label(c, SWT.NONE);
			lMese.setText("anno :");
			
			anni = new Combo(c, SWT.NONE);
			
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int anno = cal.get(Calendar.YEAR);
			int selected = 0;
			int count = 0;
			
			for (int i = anno - 10; i < anno + 10 ; i++){
				anni.add(String.valueOf(i));
				if (anno == i){
					selected = count;
				}
				count++;
			}
			anni.select(selected);
			anni.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					
					Calendar c = Calendar.getInstance();
					c.set(Calendar.YEAR, Integer.valueOf(anni.getItem(anni.getSelectionIndex())));
					c.set(Calendar.MONTH, mesi.getSelectionIndex());
					c.set(Calendar.DAY_OF_MONTH, 1);
					setDaysLabel(c);
					
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
			cvAgenti = new ComboViewer(c, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.DOUBLE_BUFFERED);
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

	private Comparator dateSorter = new Comparator() {

		@Override
		public int compare(Object o1, Object o2) {
			if ((o1 instanceof AppuntamentiModel) && (o2 instanceof ColloquiModel)) {
				return ((AppuntamentiModel)o1).getDataAppuntamento()
										  	  .compareTo(((ColloquiModel)o2).getDataColloquio());
				
			}else if ((o1 instanceof AppuntamentiModel) && (o2 instanceof AppuntamentiModel)) {
				return ((AppuntamentiModel)o1).getDataAppuntamento()
					  	  					  .compareTo(((AppuntamentiModel)o2).getDataAppuntamento());
				
			}else if ((o1 instanceof ColloquiModel) && (o2 instanceof AppuntamentiModel)) {
				return ((ColloquiModel)o1).getDataColloquio()
	  					  				  .compareTo(((AppuntamentiModel)o2).getDataAppuntamento());
				
			}else{
				return ((ColloquiModel)o1).getDataColloquio()
	  					  				  .compareTo(((ColloquiModel)o2).getDataColloquio());
				
			}
						
		}
	
	};
	
	@Override
	public void createPartControl(Composite parent) {
		
		ft = new FormToolkit(this.getViewSite().getShell().getDisplay());
		
		form = ft.createForm(parent);
		form.setImage(Activator.getImageDescriptor("/icons/calendario22.png").createImage());
		form.setText("Calendario");
		
		IToolBarManager mgrView = getViewSite().getActionBars().getToolBarManager();
		IToolBarManager mgr = form.getToolBarManager();
		NuovoAppuntamentoAction naa = new NuovoAppuntamentoAction("Nuovo appuntamento", 
																  Activator.getImageDescriptor("icons/sample2.gif"));		
		mgrView.add(naa);
		WizardGCalendarSyncAction wgcsa = new WizardGCalendarSyncAction("wizard sincronizzazione con GCalendar",
        																Activator.getImageDescriptor("icons/google_calendar.png"));
		mgrView.add(wgcsa);	
		mgr.add(new PreviousYearAction("", 
									   Activator.getImageDescriptor("icons/2leftarrow_16.png")));
		mgr.add(new PreviousMonthAction("", 
				   						Activator.getImageDescriptor("icons/1leftarrow_16.png")));
		mgr.add(new NextMonthAction("", 
				   					Activator.getImageDescriptor("icons/1rightarrow_16.png")));
		mgr.add(new NextYearAction("", 
				   				   Activator.getImageDescriptor("icons/2rightarrow_16.png")));
		
		mgr.add(new monthSelector("mesi"));
		mgr.add(new yearSelector("anni"));
		mgr.add(new inputAgente("agente"));
		mgr.add(new CercaAppuntamentiAction(CalendarioView.class.getName()));
		form.updateToolBar();		
		
		gdHV = new GridData(SWT.FILL, SWT.FILL, true, true);
		
		GridLayout gl7 = new GridLayout(7,true);
		form.getBody().setLayout(gl7);
		dayNumber = new Font(null,"Arial",8,SWT.BOLD); 
		Font dayNames = new Font(null,"Arial",12,SWT.BOLD); 
		
		Label lunedi = ft.createLabel(form.getBody(), "Luned�");
		lunedi.setFont(dayNames);
		lunedi.setForeground(new Color(null,12,0,126));
		Label martedi = ft.createLabel(form.getBody(), "Marted�");
		martedi.setFont(dayNames);
		martedi.setForeground(new Color(null,12,0,126));
		Label mercoledi = ft.createLabel(form.getBody(), "Mercoled�");
		mercoledi.setFont(dayNames);
		mercoledi.setForeground(new Color(null,12,0,126));
		Label giovedi = ft.createLabel(form.getBody(), "Gioved�");
		giovedi.setFont(dayNames);
		giovedi.setForeground(new Color(null,12,0,126));
		Label venerdi = ft.createLabel(form.getBody(), "Venerd�");
		venerdi.setFont(dayNames);
		venerdi.setForeground(new Color(null,12,0,126));
		Label sabato = ft.createLabel(form.getBody(), "Sabato");
		sabato.setFont(dayNames);
		sabato.setForeground(new Color(null,12,0,126));
		Label domenica = ft.createLabel(form.getBody(), "Domenica");
		domenica.setFont(dayNames);
		domenica.setForeground(new Color(null,12,0,126));
		domenica.setForeground(new Color(null,new RGB(255,0,0)));
		
		for (int i = 0; i < 42; i++){
			createDayDetail(form.getBody(),i);
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());		
		
		setDaysLabel(c);
				
	}
	
	public void setDaysLabel(Calendar c){
				
		
		int max = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		int min = c.getActualMinimum(Calendar.DAY_OF_MONTH);
		
		c.set(Calendar.DAY_OF_MONTH, 1);
		
		int firstdayweek = c.get(Calendar.DAY_OF_WEEK);
		firstdayweek = (firstdayweek == 1)? 6 : firstdayweek - 2;
		
		for (int i = 0;i < firstdayweek; i++){
			giorni[i].getGroupControl().setText("");
			giorni[i].setCday(null);
		}
		
		for (int i = firstdayweek;i < giorni.length; i++){
			if (i > max+firstdayweek - 1){
				break;
			}
			giorni[i].getGroupControl().setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
			
			if (isToday(c)){
				//giorni[i].getGroupControl().setBackground(new Color(null,100,200,253));
				giorni[i].getTv().getTable().setBackground(new Color(null,160,221,254));
			}else{
				giorni[i].getGroupControl().setBackground(new Color(null,255,255,255));
				giorni[i].getTv().getTable().setBackground(new Color(null,255,255,255));
			}
		
			if (c.get(Calendar.DAY_OF_WEEK) == 1){
				giorni[i].getGroupControl().setForeground(new Color(null,new RGB(255,0,0)));
			}else{
				giorni[i].getGroupControl().setForeground(new Color(null,12,0,126));
			}
			giorni[i].setCday((Calendar)c.clone());
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		for (int i = max+firstdayweek;i < 42; i++){
			giorni[i].getGroupControl().setText("");
			giorni[i].setCday(null);
		}

		
		form.redraw();
		
	}
	
	private void createDayDetail(Composite parent,int i){
		
		Composite c = ft.createComposite(parent);
		c.setLayoutData(gdHV);
		c.setLayout(new GridLayout());
		
		CalendarioGroup daylabel = new CalendarioGroup(c, SWT.NONE,null);
		daylabel.getGroupControl().setBackground(new Color(null,new RGB(255,255,255)));
		daylabel.getGroupControl().setFont(dayNumber);
		daylabel.getGroupControl().setForeground(new Color(null,12,0,126));
		daylabel.getGroupControl().setLayoutData(gdHV);
		giorni[i] = daylabel;
		ft.paintBordersFor(parent);
	}
	
	private boolean isToday(Calendar day){
		int daygiorno = day.get(Calendar.DAY_OF_MONTH);
		int monthgiorno = day.get(Calendar.MONTH);
		int yeargiorno = day.get(Calendar.YEAR);		
		Calendar ctoday = Calendar.getInstance();
		ctoday.setTime(new Date());
		int daytoday = ctoday.get(Calendar.DAY_OF_MONTH);
		int monthtoday = ctoday.get(Calendar.MONTH);
		int yeartoday = ctoday.get(Calendar.YEAR);					

		if ((daygiorno==daytoday) && (monthgiorno==monthtoday) && (yeargiorno==yeartoday)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void setFocus() {
		cvAgenti.setInput(new Object());
	}
	
	public AgentiModel getAgenteSearch() {
		return agenteSearch;
	}
	
	public void setSearchResults(ArrayList results){
		
		Collections.sort(results,dateSorter);
		
		for (int i = 0;i < giorni.length; i++){
			
			if (giorni[i].getCday() != null){
				
				ArrayList dayapps = new ArrayList();
				
				Iterator it = results.iterator();
				while (it.hasNext()) {
					
					Object object = it.next();
					
					int daygiorno = giorni[i].getCday().get(Calendar.DAY_OF_MONTH);
					int monthgiorno = giorni[i].getCday().get(Calendar.MONTH);
					int yeargiorno = giorni[i].getCday().get(Calendar.YEAR);					
					
					
												
						Calendar cobj = Calendar.getInstance();
						if (object instanceof AppuntamentiModel){	
							cobj.setTime(((AppuntamentiModel)object).getDataAppuntamento());
						}else{
							cobj.setTime(((ColloquiModel)object).getDataColloquio());
						}
						
						int dayobj = cobj.get(Calendar.DAY_OF_MONTH);
						int monthobj = cobj.get(Calendar.MONTH);
						int yearobj = cobj.get(Calendar.YEAR);						
						
						if ((daygiorno == dayobj) && (monthgiorno == monthobj) && (yeargiorno == yearobj)){									 
							dayapps.add(object);
						}
					
					
				}
						
				giorni[i].setAppuntamenti((ArrayList)dayapps.clone());
				
			}
		}
		form.redraw();
		
	}
	
	public Date getDataDASearch(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, Integer.valueOf(anni.getItem(anni.getSelectionIndex())));
		c.set(Calendar.MONTH, mesi.getSelectionIndex());
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();

	}
	
	public void setDataSearch(Calendar c){
		
		Calendar ctoday = Calendar.getInstance();
		ctoday.setTime(new Date());
		
		int anno = ctoday.get(Calendar.YEAR);
		int selected = 0;
		int count = 0;
		for (int i = anno - 10; i < anno + 10 ; i++){			
			if (c.get(Calendar.YEAR) == i){
				selected = count;
			}
			count++;
		}
		anni.select(selected);
		mesi.select(c.get(Calendar.MONTH));
		CercaAppuntamentiAction caa = new CercaAppuntamentiAction(CalendarioView.class.getName());
		caa.run();
		
	}
	
	public Date getDataASearch(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, Integer.valueOf(anni.getItem(anni.getSelectionIndex())));
		c.set(Calendar.MONTH, mesi.getSelectionIndex());
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();

	}
	
	

}
