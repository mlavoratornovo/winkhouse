package winkhouse.view.immobili;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.immobili.ApriDettaglioImmobileAction;
import winkhouse.action.immobili.CancellaImmobile;
import winkhouse.action.immobili.GeoGroupingAction;
import winkhouse.action.immobili.NuovoImmobileAction;
import winkhouse.action.immobili.RefreshImmobiliAction;
import winkhouse.dao.ClassiEnergeticheDAO;
import winkhouse.dao.ComuniDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.dao.RiscaldamentiDAO;
import winkhouse.dao.StatoConservativoDAO;
import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.helper.ProfilerHelper;
import winkhouse.orm.Classienergetiche;
import winkhouse.orm.Immobili;
import winkhouse.orm.Riscaldamenti;
import winkhouse.orm.Statoconservativo;
import winkhouse.orm.Tipologieimmobili;
import winkhouse.perspective.AffittiPerspective;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.immobili.handler.DettaglioImmobiliHandler;
import winkhouse.vo.ComuniVO;



public class ImmobiliTreeView extends ViewPart {
	
	public final static String ID = "winkhouse.immobilitreeview";
	private TreeViewer viewer;
	private Text tCerca = null;
	private Combo cGrouping = null;
	private TipologieImmobiliDAO tiDAO =  new TipologieImmobiliDAO();
	private StatoConservativoDAO scDAO =  new StatoConservativoDAO();
	private RiscaldamentiDAO rDAO = new RiscaldamentiDAO();
	private ClassiEnergeticheDAO ceDAO = new ClassiEnergeticheDAO();
	private ImmobiliDAO iDAO =  new ImmobiliDAO();
	private ComuniDAO comuniDAO = new ComuniDAO();
	private Image homeImg = Activator.getImageDescriptor("icons/gohome.png").createImage();
	private Image homeFolderImg = Activator.getImageDescriptor("icons/folder_home.png").createImage();
	private Image geoImg = Activator.getImageDescriptor("icons/cercacomune.png").createImage();
	
	private String grouping = TIPI_IMMOBILI; 
	private boolean geogrouping = false; 
	
	private final static String TIPI_IMMOBILI = "Tipologie immobili";	
	private final static String IMPIANTI_RISCALDAMENTO = "Impianti riscaldamento";
	private final static String STATO_CONSERVATIVO = "Stato Conservativo";
	private final static String CLASSE_ENERGETICA = "Classe energetica";

	
	public ImmobiliTreeView() {
	}


	class ViewContentProvider implements ITreeContentProvider {
		
		@Override
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			
			if (parentElement instanceof Tipologieimmobili){
				if(PlatformUI.getWorkbench()
							 .getActiveWorkbenchWindow()
							 .getActivePage()
							 .getPerspective()
							 .getId().equalsIgnoreCase(AffittiPerspective.ID)){
					
					ArrayList<Immobili> immobili = new ArrayList<Immobili>();
					
					if (parentElement instanceof Tipologieimmobili){
						immobili = iDAO.getImmobiliByTipologiaIsAffittiComune(((Tipologieimmobili)parentElement),
																			  ((Tipologieimmobili)parentElement).getComune());
					}else{		
						immobili = iDAO.getImmobiliByTipologiaIsAffitti(((Tipologieimmobili)parentElement));
					}
					ProfilerHelper.getInstance().filterAffitti(immobili, false);
					return immobili.toArray();
				}else{
					
					ArrayList<Immobili> immobili = new ArrayList<Immobili>();
					
					if (parentElement instanceof Tipologieimmobili){
						immobili = iDAO.getImmobiliByTipologiaComune(((Tipologieimmobili)parentElement),
								   							  	     ((Tipologieimmobili)parentElement).getComune());											
					}else{						
						immobili = iDAO.getImmobiliByTipologia(((Tipologieimmobili)parentElement));					
						
					}
					immobili = ProfilerHelper.getInstance().filterImmobili(immobili, false);
					return immobili.toArray();
					
				}
			}else if (parentElement instanceof Riscaldamenti){
				if(PlatformUI.getWorkbench()
							 .getActiveWorkbenchWindow()
							 .getActivePage()
							 .getPerspective()
							 .getId().equalsIgnoreCase(AffittiPerspective.ID)){
					
					ArrayList<Immobili> immobili = new ArrayList<Immobili>();
					
					if (parentElement instanceof Riscaldamenti){
						immobili = iDAO.getImmobiliByRiscaldamentoIsAffittiComune(Immobili.class.getName(), 
						    													  ((Riscaldamenti)parentElement).getCodRiscaldamento(),
						    													  ((Riscaldamenti)parentElement).getComune());
					}else{
						immobili = iDAO.getImmobiliByRiscaldamentoIsAffitti(Immobili.class.getName(), 
								 											((Riscaldamenti)parentElement).getCodRiscaldamento());
						
					}
					ProfilerHelper.getInstance().filterAffitti(immobili, false);
					return immobili.toArray();
					
				}else{
					ArrayList<Immobili> immobili = new ArrayList<Immobili>(); 
				
					if (parentElement instanceof Riscaldamenti){
						immobili = iDAO.getImmobiliByRiscaldamentoComune(Immobili.class.getName(),
								   								   		 ((Riscaldamenti)parentElement).getCodRiscaldamento(),
								   								   		 ((Riscaldamenti)parentElement).getComune());						
					}else{
						immobili = iDAO.getImmobiliByRiscaldamento(Immobili.class.getName(),
																   ((Riscaldamenti)parentElement).getCodRiscaldamento());
					}
						       															 
					immobili = ProfilerHelper.getInstance().filterImmobili(immobili, false);
					return immobili.toArray();
				}
				
			}else if (parentElement instanceof Statoconservativo){
				if(PlatformUI.getWorkbench()
						 .getActiveWorkbenchWindow()
						 .getActivePage()
						 .getPerspective()
						 .getId().equalsIgnoreCase(AffittiPerspective.ID)){
					
					ArrayList<Immobili> immobili = new ArrayList<Immobili>();
					if (parentElement instanceof Statoconservativo){
						immobili = iDAO.getImmobiliByStatoConservativoIsAffittiComune(Immobili.class.getName(), 
																			          ((Statoconservativo)parentElement).getCodStatoConservativo(),
																			          ((Statoconservativo)parentElement).getComune());
						
					}else{
						immobili = iDAO.getImmobiliByStatoConservativoIsAffitti(Immobili.class.getName(), 
																				((Statoconservativo)parentElement).getCodStatoConservativo());
					}
					ProfilerHelper.getInstance().filterAffitti(immobili, false);
					return immobili.toArray();
				}else{
					ArrayList<Immobili> immobili = new ArrayList<Immobili>();
					if (parentElement instanceof Statoconservativo){
						immobili = iDAO.getImmobiliByStatoConservativoComune(Immobili.class.getName(), 
																	   		 ((Statoconservativo)parentElement).getCodStatoConservativo(),
																	   		 ((Statoconservativo)parentElement).getComune());
					}else{
						immobili = iDAO.getImmobiliByStatoConservativo(Immobili.class.getName(), 
								   									   ((Statoconservativo)parentElement).getCodStatoConservativo());

					}
					immobili = ProfilerHelper.getInstance().filterImmobili(immobili, false);
					return immobili.toArray();
										
				}
			}else if (parentElement instanceof Classienergetiche){
					if(PlatformUI.getWorkbench()
							 .getActiveWorkbenchWindow()
							 .getActivePage()
							 .getPerspective()
							 .getId().equalsIgnoreCase(AffittiPerspective.ID)){
						
					
						ArrayList<Immobili> immobili = new ArrayList<Immobili>();
						
						if (parentElement instanceof Classienergetiche){
							immobili = iDAO.listImmobiliByCodClasseEnergeticaIsAffittiComune(Immobili.class.getName(),
																						  	 ((Classienergetiche)parentElement).getCodClasseEnergetica(),
																						  	 ((Classienergetiche)parentElement).getComune());							
						}else{
							immobili = iDAO.listImmobiliByCodClasseEnergeticaIsAffitti(Immobili.class.getName(),
																					   ((Classienergetiche)parentElement).getCodClasseEnergetica());
						}
																										  
						ProfilerHelper.getInstance().filterAffitti(immobili, false);
						return immobili.toArray();
					}else{
						ArrayList<Immobili> immobili = new ArrayList<Immobili>();
						if (parentElement instanceof Classienergetiche){
							immobili = iDAO.listImmobiliByCodClasseEnergeticaComune(Immobili.class.getName(), 
																			  ((Classienergetiche)parentElement).getCodClasseEnergetica(),
																			  ((Classienergetiche)parentElement).getComune());
						}else{
							immobili = iDAO.listImmobiliByCodClasseEnergetica(Immobili.class.getName(), 
									  										  ((Classienergetiche)parentElement).getCodClasseEnergetica());							
						}
						immobili = ProfilerHelper.getInstance().filterImmobili(immobili, false);
						return immobili.toArray();
											
					}				
			}else if (parentElement instanceof ComuniVO){
				
				ArrayList<Immobili> gruppo_list = new ArrayList<Immobili>();
				
				if (!((ComuniVO)parentElement).getProvincia().equalsIgnoreCase("")){
//					return comuniDAO.getComuniByProvincia(((ComuniVO)parentElement).getProvincia()).toArray();
					return comuniDAO.getComuniByProvinciaImmobili(((ComuniVO)parentElement).getProvincia()).toArray();
				}
				if (!((ComuniVO)parentElement).getComune().equalsIgnoreCase("")){
					
					if (grouping.equalsIgnoreCase(TIPI_IMMOBILI)){
						
						if(PlatformUI.getWorkbench()
								 .getActiveWorkbenchWindow()
								 .getActivePage()
								 .getPerspective()
								 .getId().equalsIgnoreCase(AffittiPerspective.ID)){
							
							gruppo_list = (ArrayList)tiDAO.listByAffittiComune(((ComuniVO)parentElement).getComune());
							gruppo_list.addAll(iDAO.getImmobiliByTipologiaIsAffittiComune(Immobili.class.getName(), null, ((ComuniVO)parentElement).getComune()));
						}else{
							gruppo_list = (ArrayList)tiDAO.listByComune(((ComuniVO)parentElement).getComune());
							gruppo_list.addAll(iDAO.getImmobiliByTipologiaComune(Immobili.class.getName(), null, ((ComuniVO)parentElement).getComune()));					
						}
					}
					if (grouping.equalsIgnoreCase(IMPIANTI_RISCALDAMENTO)){
						
						if(PlatformUI.getWorkbench()
								 .getActiveWorkbenchWindow()
								 .getActivePage()
								 .getPerspective()
								 .getId().equalsIgnoreCase(AffittiPerspective.ID)){
							
							gruppo_list = (ArrayList)rDAO.listByAffittiComune(((ComuniVO)parentElement).getComune());
							gruppo_list.addAll(iDAO.getImmobiliByRiscaldamentoIsAffittiComune(Immobili.class.getName(), null, ((ComuniVO)parentElement).getComune()));
						}else{
							gruppo_list = (ArrayList)rDAO.listByComune(((ComuniVO)parentElement).getComune());
							gruppo_list.addAll(iDAO.getImmobiliByRiscaldamentoComune(Immobili.class.getName(), null, ((ComuniVO)parentElement).getComune()));					
						}
					}
					if (grouping.equalsIgnoreCase(STATO_CONSERVATIVO)){
						
						if(PlatformUI.getWorkbench()
								 .getActiveWorkbenchWindow()
								 .getActivePage()
								 .getPerspective()
								 .getId().equalsIgnoreCase(AffittiPerspective.ID)){
						
							gruppo_list = (ArrayList)scDAO.listByAffittiComune(((ComuniVO)parentElement).getComune());
							gruppo_list.addAll(iDAO.getImmobiliByStatoConservativoIsAffittiComune(Immobili.class.getName(), null, ((ComuniVO)parentElement).getComune()));
						}else{
							gruppo_list = (ArrayList)scDAO.listByComune(((ComuniVO)parentElement).getComune());
							gruppo_list.addAll(iDAO.getImmobiliByStatoConservativoComune(Immobili.class.getName(), null, ((ComuniVO)parentElement).getComune()));					
						}
					}
					if (grouping.equalsIgnoreCase(CLASSE_ENERGETICA)){
						
						if(PlatformUI.getWorkbench()
								 .getActiveWorkbenchWindow()
								 .getActivePage()
								 .getPerspective()
								 .getId().equalsIgnoreCase(AffittiPerspective.ID)){
						
							gruppo_list = (ArrayList)ceDAO.listByAffittiComune(((ComuniVO)parentElement).getComune());
							gruppo_list.addAll(iDAO.getImmobiliByClasseEnergeticaIsAffittiComune(Immobili.class.getName(), null, ((ComuniVO)parentElement).getComune()));
						}else{
							gruppo_list = (ArrayList)ceDAO.listByComune(((ComuniVO)parentElement).getComune());
							gruppo_list.addAll(iDAO.getImmobiliByClasseEnergeticaComune(Immobili.class.getName(), null, ((ComuniVO)parentElement).getComune()));					
						}
					}
					
					if(PlatformUI.getWorkbench()
							 .getActiveWorkbenchWindow()
							 .getActivePage()
							 .getPerspective()
							 .getId().equalsIgnoreCase(AffittiPerspective.ID)){
						ProfilerHelper.getInstance().filterAffitti(gruppo_list, false);
					}else{
						gruppo_list = ProfilerHelper.getInstance().filterImmobili(gruppo_list, false);
					}
					
				}
				return gruppo_list.toArray();
			}else{
				if (isGeogrouping()){
					return comuniDAO.getProvincieImmobili().toArray();
				}else{
					ArrayList<Immobili> gruppo_list = new ArrayList<Immobili>();
					if (grouping.equalsIgnoreCase(TIPI_IMMOBILI)){
						
//						if(PlatformUI.getWorkbench()
//								 .getActiveWorkbenchWindow()
//								 .getActivePage()
//								 .getPerspective()
//								 .getId().equalsIgnoreCase(AffittiPerspective.ID)){
//							
//							gruppo_list = (ArrayList)tiDAO.listByAffitti(TipologieImmobiliVO.class.getName());
//							gruppo_list.addAll(iDAO.getImmobiliByTipologiaIsAffitti(Immobili.class.getName(), null));
//						}else{
						gruppo_list = (ArrayList)tiDAO.list(null);
						gruppo_list.addAll(iDAO.getImmobiliByTipologia(null));					
//						}
					}
					if (grouping.equalsIgnoreCase(IMPIANTI_RISCALDAMENTO)){
						
//						if(PlatformUI.getWorkbench()
//								 .getActiveWorkbenchWindow()
//								 .getActivePage()
//								 .getPerspective()
//								 .getId().equalsIgnoreCase(AffittiPerspective.ID)){
//							
//							gruppo_list = (ArrayList)rDAO.listByAffitti(RiscaldamentiVO.class.getName());
//							gruppo_list.addAll(iDAO.getImmobiliByRiscaldamentoIsAffitti(Immobili.class.getName(), null));
//						}else{
						gruppo_list = (ArrayList)rDAO.list(null);
						gruppo_list.addAll(iDAO.getImmobiliByRiscaldamento(null));					
//						}
					}
					if (grouping.equalsIgnoreCase(STATO_CONSERVATIVO)){
						
//						if(PlatformUI.getWorkbench()
//								 .getActiveWorkbenchWindow()
//								 .getActivePage()
//								 .getPerspective()
//								 .getId().equalsIgnoreCase(AffittiPerspective.ID)){
//						
//							gruppo_list = (ArrayList)scDAO.listByAffitti(StatoConservativoVO.class.getName());
//							gruppo_list.addAll(iDAO.getImmobiliByStatoConservativoIsAffitti(Immobili.class.getName(), null));
//						}else{
						gruppo_list = (ArrayList)scDAO.list(null);
						gruppo_list.addAll(iDAO.getImmobiliByStatoConservativo(null));					
//						}
					}
					if (grouping.equalsIgnoreCase(CLASSE_ENERGETICA)){
						
//						if(PlatformUI.getWorkbench()
//								 .getActiveWorkbenchWindow()
//								 .getActivePage()
//								 .getPerspective()
//								 .getId().equalsIgnoreCase(AffittiPerspective.ID)){
//						
//							gruppo_list = (ArrayList)ceDAO.listByAffitti(ClasseEnergeticaVO.class.getName());
//							gruppo_list.addAll(iDAO.getImmobiliByClasseEnergeticaIsAffitti(Immobili.class.getName(), null));
//						}else{
						gruppo_list = (ArrayList)ceDAO.listClassiEnergetiche(null);
						gruppo_list.addAll(iDAO.getImmobiliByClasseEnergetica(null));					
//						}
					}
					
					if(PlatformUI.getWorkbench()
							 .getActiveWorkbenchWindow()
							 .getActivePage()
							 .getPerspective()
							 .getId().equalsIgnoreCase(AffittiPerspective.ID)){
						ProfilerHelper.getInstance().filterAffitti(gruppo_list, false);
					}else{
						gruppo_list = ProfilerHelper.getInstance().filterImmobili(gruppo_list, false);
					}
					
					return gruppo_list.toArray();
				}
			}
			
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {			
			if ((element instanceof Tipologieimmobili) || 
				(element instanceof Riscaldamenti) ||
				(element instanceof Statoconservativo) ||
				(element instanceof ComuniVO) ||
				(element instanceof Classienergetiche)){
				return true;
			}else{
				return false;
			}
		}

	}

	class ViewLabelProvider extends LabelProvider {
		
		public String getText(Object obj) {
			String returnValue = null;
			if (obj instanceof ComuniVO){
				return (((ComuniVO)obj).getProvincia().equalsIgnoreCase("") == false)
						?((ComuniVO)obj).getProvincia()
						:((ComuniVO)obj).getComune();
			}

			if (obj instanceof Tipologieimmobili){
				returnValue = ((Tipologieimmobili)obj).getDescrizione();
			}
			if (obj instanceof Riscaldamenti){
				returnValue = ((Riscaldamenti)obj).getDescrizione();
			}
			if (obj instanceof Statoconservativo){
				returnValue = ((Statoconservativo)obj).getDescrizione();
			}			
			if (obj instanceof Immobili){
				returnValue = ((Immobili)obj).toString();
			}
			if (obj instanceof Classienergetiche){
				returnValue = ((Classienergetiche)obj).toString();
			}			
			
			return returnValue;
		}

		public Image getImage(Object obj) {
			if (obj instanceof ComuniVO){
				return geoImg;
			}
			if (obj instanceof Immobili){
				return homeImg;
			}else{			
				return homeFolderImg;
			}			
		}
	}
	
	private class cercaCodice extends Action{
		TreeViewer tv = null;
		public cercaCodice(TreeViewer tree){
			tv = tree;
		}
		
		@Override
		public ImageDescriptor getImageDescriptor() {			
			return Activator.getImageDescriptor("icons/kfind.png");
		}

		@Override
		public String getToolTipText() {
			return super.getToolTipText();
		}

		@Override
		public void run() {
			
			boolean isnumber = true;
			if (isnumber){
				tv.expandAll();
				
				TreeItem ti = searchTreeItem(tv.getTree().getItems(), tCerca.getText());
				if (ti == null){
					MessageBox mb = new MessageBox(getViewSite().getShell());
					mb.setMessage("Riferimento immobile non trovato");
					mb.open();
				}else{
					for (TreeItem treeItem : tv.getTree().getItems()) {
						if(treeItem != ti.getParentItem()){
							treeItem.setExpanded(false);
						}
					}
					//tv.collapseAll();
					
					Object[] sel = new Object[1];
					sel[0] = ti.getData();
					StructuredSelection ss = new StructuredSelection(sel);
					
					tv.setSelection(ss, true);
	//				SelectionChangedEvent e = new SelectionChangedEvent(tv,ss);
					Event e = new Event();
					e.item = ti;
					e.data = ti.getData();
					e.widget = tv.getTree();
					tv.getTree().notifyListeners(SWT.Selection, e);
				}
			}else{
				MessageBox mb = new MessageBox(getViewSite().getShell());
				mb.setMessage("Riferimento immobile non valido");
				mb.open();				
			}
			
		}
		
		private TreeItem searchTreeItem(TreeItem[] lista, String cod){
			TreeItem ti = null;
			//ImmobileVO returnValue = null;
			for (int i = 0; i < lista.length; i++) {
				if (lista[i].getData() instanceof Tipologieimmobili){
					ti = searchTreeItem(lista[i].getItems(),cod);
					if (ti != null){
						break;
					}
					
				}else{
					if(((Immobili)lista[i].getData()).getRif().equalsIgnoreCase(cod)){
						
						ti = lista[i];
						break;
					}/*else{
						lista[i].getParentItem().setExpanded(false);
					}*/
				}
			}
			return ti;
		}
		
	}

	private class GrouppingChoser extends ControlContribution{
		
		public GrouppingChoser(String id){
			super(id);
		}

		@Override
		protected Control createControl(Composite parent) {
			cGrouping = new Combo(parent,SWT.BORDER);
			cGrouping.setToolTipText("Cambia raggruppamento dati");
			cGrouping.add(TIPI_IMMOBILI);					
			cGrouping.add(IMPIANTI_RISCALDAMENTO);
			cGrouping.add(STATO_CONSERVATIVO);
			cGrouping.add(CLASSE_ENERGETICA);
			cGrouping.select(0);
			cGrouping.addSelectionListener(new SelectionListener() {
				
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = ((Combo)e.getSource()).getSelectionIndex();
					switch (index) {
						case 0:
							grouping = TIPI_IMMOBILI;
							break;
						case 1:
							grouping = IMPIANTI_RISCALDAMENTO;
							break;
						case 2:
							grouping = STATO_CONSERVATIVO;
							break;
						case 3:
							grouping = CLASSE_ENERGETICA;
							break;

					default:
						grouping = "TIPI_IMMOBILI";
					}
					
					RefreshImmobiliAction ria = new RefreshImmobiliAction();
					ria.run();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
			
					
				}
			});
			return cGrouping;
		}
		
		
		
	}
	
	private class InputRicercaToolBar extends ControlContribution{

		public InputRicercaToolBar(String id) {
			super(id);
		}
		
		@Override
		protected Control createControl(Composite parent) {
			tCerca = new Text(parent,SWT.BORDER);
			tCerca.setToolTipText("Cerca per riferimento immobile");
			tCerca.addKeyListener(new KeyListener() {
				
				@Override
				public void keyReleased(KeyEvent e) {
					if((e.keyCode == 13) || (e.keyCode == 16777296)){
						new cercaCodice(viewer).run();
					}
					
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			return tCerca;
		}
		
	}

	public void createPartControl(Composite parent) {
		
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		
		mgr.add(new GeoGroupingAction("Raggruppamento geografico",Action.AS_CHECK_BOX));
		mgr.add(new GrouppingChoser("grouping"));
		mgr.add(new InputRicercaToolBar("ricerca"));		
	    mgr.add(new cercaCodice(viewer));
		mgr.add(new RefreshImmobiliAction("Ricarica immobili", 
										  Activator.getImageDescriptor("/icons/adept_reinstall.png")));

		mgr.add(new NuovoImmobileAction("Nuovo immobile",
										Activator.getImageDescriptor("/icons/sample2.gif")));
		
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());		
		viewer.getTree().addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
				if (((StructuredSelection)viewer.getSelection()).getFirstElement() instanceof Immobili){
					Immobili iVO = (Immobili)((StructuredSelection)viewer.getSelection()).getFirstElement();
					DettaglioImmobileView div = DettaglioImmobiliHandler.getInstance()
																		.getDettaglioImmobile(iVO);
				}
			  
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				
				if((e.button == 2)||(e.button == 3)){
					
					Object o = ((StructuredSelection)viewer.getSelection()).getFirstElement();
					Menu m = new Menu(viewer.getTree());
					
					if (o instanceof Tipologieimmobili){
					
						MenuItem miNuovo = new MenuItem(m,SWT.PUSH);
						miNuovo.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
						miNuovo.setText("Nuovo immobile");
						miNuovo.addSelectionListener(new SelectionListener(){

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}

							@Override
							public void widgetSelected(SelectionEvent e) {
								
								Immobili iModel = WinkhouseUtils.getInstance().getCayenneObjectContext().newObject(Immobili.class);
								
								DettaglioImmobileView div = DettaglioImmobiliHandler.getInstance()
																					.getDettaglioImmobile(iModel);
									
								Tipologieimmobili tiVO = (Tipologieimmobili)((StructuredSelection)viewer.getSelection()).getFirstElement();
								
								ApriDettaglioImmobileAction adia = new ApriDettaglioImmobileAction(iModel,tiVO);
								adia.run();
										
							}
							
						});
						
					}
					if (o instanceof Immobili){
						
						MenuItem miCancella = new MenuItem(m,SWT.PUSH);
						miCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
						miCancella.setText("Cancella immobile");
						miCancella.addSelectionListener(new SelectionListener(){

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}

							@Override
							public void widgetSelected(SelectionEvent e) {
								
								Object o = ((StructuredSelection)viewer.getSelection()).getFirstElement();
								if (o instanceof Immobili){
									CancellaImmobile ci = new CancellaImmobile((Immobili)o);
									ci.run();
								}
																		
							}
							
						});
						
					}
					
					
					viewer.getTree().setMenu(m);
				}
				
			}
			
		});
		viewer.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				
				Object o = ((StructuredSelection)event.getSelection()).getFirstElement();
				
				if (o instanceof Immobili){
					ApriDettaglioImmobileAction adia = new ApriDettaglioImmobileAction((Immobili)o,null);
					adia.run();
				}
				
			}
			
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	/*	RefreshImmobiliAction ria = new RefreshImmobiliAction();
		ria.run();*/

	}

	public TreeViewer getViewer() {
		return viewer;
	}

	
	public boolean isGeogrouping() {
		return geogrouping;
	}

	
	public void setGeogrouping(boolean geogrouping) {
		this.geogrouping = geogrouping;
	}


}