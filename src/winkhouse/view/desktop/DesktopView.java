package winkhouse.view.desktop;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.viewers.internal.ZoomManager;
import org.eclipse.zest.core.widgets.CGraphNode;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.DirectedGraphLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalShift;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.VerticalLayoutAlgorithm;
import org.jsoup.select.NodeVisitor;

import winkhouse.Activator;
import winkhouse.action.affitti.ApriAffittiAction;
import winkhouse.action.anagrafiche.ApriDettaglioAnagraficaPerspectiveAction;
import winkhouse.action.colloqui.ApriDettaglioColloquioAction;
import winkhouse.action.desktop.AddPromemoriaAction;
import winkhouse.action.desktop.ApriDettaglioPromemoriaAction;
import winkhouse.action.desktop.ChangeDesktopTypeAction;
import winkhouse.action.desktop.DeletePromemoriaAction;
import winkhouse.action.desktop.RefreshPromemoriaAction;
import winkhouse.action.immobili.ApriDettaglioImmobilePerspectiveAction;
import winkhouse.dao.AgentiDAO;
import winkhouse.dao.PromemoriaDAO;
import winkhouse.model.AffittiModel;
import winkhouse.model.AgentiModel;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.model.PromemoriaModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.desktop.model.MyNode;
import winkhouse.view.desktop.provider.GraphLabelProvider;
import winkhouse.view.desktop.provider.ZestNodeContentProvider;
import winkhouse.vo.AgentiVO;

public class DesktopView extends ViewPart implements IZoomableWorkbenchPart{

	public final static String ID = "winkhouse.desktopview";
	public final static int PROMEMORIA_TYPE = 0;
	public final static int STRUTTURA_TYPE = 1;
	
	private AddPromemoriaAction apa = null;
	private DeletePromemoriaAction dpa = null;
	private RefreshPromemoriaAction rpa = null;
	private ChangeDesktopTypeAction cdta = null;
	private SelettoreZoom zs = null;
	private AgentiChoser ac = null;
	private Pager pager = null;
	private SelettoreDisposizione selettoreDisposizione = null;
	private StructureLevel selettoreStructureLevel = null;
	private AgentiModel agente = null;
	private Integer itemxpage = 10;
	private Integer currentpage = 1;
	private Integer pagenumber = 1;
	private int desktop_type = PROMEMORIA_TYPE;
	private ZoomManager zoomManager = null;
	private ComboViewer agenti = null;
	private ImageDescriptor resetAgenti = Activator.getImageDescriptor("/icons/resetagenti.png");
	private ResetAgenteAction ras = null;
	private Integer structureLevel = 1;
	private ArrayList rootNodes = new ArrayList();
	GraphViewer desktop = null;
	
	private class Pager extends ControlContribution{
		
		private Combo cElementsPerPage = null;
		private ImageHyperlink ih_prima = null;
		private ImageHyperlink ih_indietro = null;
		private ImageHyperlink ih_ultima = null;
		private ImageHyperlink ih_avanti = null;
		private Label pageNumber = null;
		
		public Pager(String id){
			super(id);
		}

		@Override
		protected Control createControl(Composite parent) {
			
			Composite cPager = new Composite(parent, SWT.FLAT);
			cPager.setLayout(new GridLayout(6,false));
			GridData gd = new GridData();
			gd.verticalAlignment = SWT.TOP;
			ih_prima = new ImageHyperlink(cPager, SWT.FLAT);
			ih_prima.setLayoutData(gd);
			ih_prima.setImage(Activator.getImageDescriptor("/icons/2leftarrow_12.png").createImage());
			ih_prima.addMouseListener(new MouseListener(){

				@Override
				public void mouseUp(MouseEvent e) {
					DesktopView.this.setCurrentpage(1);
					pageNumber.setText("1");
					RefreshPromemoriaAction rpa = new RefreshPromemoriaAction(DesktopView.this);
					rpa.run();

				}

				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
				}
				
			});

			ih_indietro = new ImageHyperlink(cPager, SWT.FLAT);
			ih_indietro.setLayoutData(gd);
			ih_indietro.setImage(Activator.getImageDescriptor("/icons/1leftarrow_12.png").createImage());
			ih_indietro.addMouseListener(new MouseListener(){

				@Override
				public void mouseUp(MouseEvent e) {
					if (DesktopView.this.getCurrentpage() > 1){
						DesktopView.this.setCurrentpage(DesktopView.this.getCurrentpage() - 1);
						pageNumber.setText(String.valueOf(DesktopView.this.getCurrentpage()));
						RefreshPromemoriaAction rpa = new RefreshPromemoriaAction(DesktopView.this);
						rpa.run();						
					}
				}

				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
				}
				
			});

			GridData gdc = new GridData();
			gdc.verticalAlignment = SWT.TOP;
			gdc.grabExcessHorizontalSpace = true;
			
			cElementsPerPage = new Combo(cPager,SWT.BORDER);
			cElementsPerPage.setLayoutData(gdc);
			cElementsPerPage.setToolTipText("Numero di elementi per pagina");
			cElementsPerPage.add(String.valueOf(10));
			cElementsPerPage.add(String.valueOf(20));
			cElementsPerPage.add(String.valueOf(30));
			cElementsPerPage.add(String.valueOf(40));
			cElementsPerPage.add(String.valueOf(50));
			cElementsPerPage.select(0);
			cElementsPerPage.addSelectionListener(new SelectionListener() {
				
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					
					int index = ((Combo)e.getSource()).getSelectionIndex();
					
					DesktopView.this.setItemxpage(Integer.valueOf(((Combo)e.getSource()).getItem(index)));
					DesktopView.this.setCurrentpage(1);
					pageNumber.setText(String.valueOf(DesktopView.this.getCurrentpage()));
					RefreshPromemoriaAction rpa = new RefreshPromemoriaAction(DesktopView.this);
					rpa.run();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
			
					
				}
			});

			pageNumber = new Label(cPager, SWT.NONE);
			pageNumber.setText("1  ");
			pageNumber.setFont(new Font(PlatformUI.getWorkbench().getDisplay(), "Arial", 8, SWT.NORMAL));
			
			ih_avanti = new ImageHyperlink(cPager, SWT.FLAT);
			ih_avanti.setLayoutData(gd);
			ih_avanti.setImage(Activator.getImageDescriptor("/icons/1rightarrow_12.png").createImage());
			ih_avanti.addMouseListener(new MouseListener(){

				@Override
				public void mouseUp(MouseEvent e) {
					if (DesktopView.this.getCurrentpage() < DesktopView.this.getPagenumber()){
						DesktopView.this.setCurrentpage(DesktopView.this.getCurrentpage() + 1);
						pageNumber.setText(String.valueOf(DesktopView.this.getCurrentpage()));
						RefreshPromemoriaAction rpa = new RefreshPromemoriaAction(DesktopView.this);
						rpa.run();						
					}

				}

				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
				}
				
			});
			
			ih_ultima = new ImageHyperlink(cPager, SWT.FLAT);
			ih_ultima.setLayoutData(gd);
			ih_ultima.setImage(Activator.getImageDescriptor("/icons/2rightarrow_12.png").createImage());
			ih_ultima.addMouseListener(new MouseListener(){

				@Override
				public void mouseUp(MouseEvent e) {
					DesktopView.this.setCurrentpage(DesktopView.this.getPagenumber());
					pageNumber.setText(String.valueOf(DesktopView.this.getCurrentpage()));
					RefreshPromemoriaAction rpa = new RefreshPromemoriaAction(DesktopView.this);
					rpa.run();						
				}

				@Override
				public void mouseDoubleClick(MouseEvent e) {
				}
				
				@Override
				public void mouseDown(MouseEvent e) {
				}
				
			});
			
			return cPager;
		}
		
		
		
	}
	
	private class SelettoreDisposizione extends ControlContribution{

		private Combo cLayout = null;
		
		public SelettoreDisposizione(String id){
			super(id);
		}
		@Override
		protected Control createControl(Composite parent) {
			
			cLayout = new Combo(parent,SWT.BORDER);
			
			cLayout.setToolTipText("Disposizione degli elementi");
			cLayout.add("Sparso");
			cLayout.add("Albero");
			cLayout.add("Griglia");
			cLayout.add("Albero orizzontale");
			cLayout.add("Radiale");
			cLayout.add("Verticale");
			cLayout.add("Sopostameto orizzontale");
			cLayout.add("Impilati");
			cLayout.select(0);
			cLayout.addSelectionListener(new SelectionListener() {
				
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					
					int index = ((Combo)e.getSource()).getSelectionIndex();
					switch (index) {
						case 0: DesktopView.this.getDesktop()
												.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),true);
								break;
						case 1: DesktopView.this.getDesktop()
												.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),true);
								break;
						case 2: DesktopView.this.getDesktop()
												.setLayoutAlgorithm(new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),true);
								break;
						case 3: DesktopView.this.getDesktop()
												.setLayoutAlgorithm(new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),true);
								break;
						case 4: DesktopView.this.getDesktop()
												.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),true);
								break;
						case 5: DesktopView.this.getDesktop()
												.setLayoutAlgorithm(new VerticalLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),true);
								break;
						case 6: DesktopView.this.getDesktop()
												.setLayoutAlgorithm(new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING),true);
								break;
						case 7: DesktopView.this.getDesktop()
												.setLayoutAlgorithm(new DirectedGraphLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING),true);
								break;
						default:
							break;
					}
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
			
					
				}
			});

			return cLayout;
		}
		
	}

	private class SelettoreZoom extends ControlContribution{

		private Combo cLayout = null;
		
		public SelettoreZoom(String id){
			super(id);
		}
		@Override
		protected Control createControl(Composite parent) {
			
			cLayout = new Combo(parent,SWT.BORDER);
			
			cLayout.setToolTipText("Numero di elementi per pagina");
			cLayout.add("25%");
			cLayout.add("50%");
			cLayout.add("75%");
			cLayout.add("100%");
			cLayout.add("125%");
			cLayout.add("150%");
			cLayout.add("200%");
			
			cLayout.select(3);
			cLayout.addSelectionListener(new SelectionListener() {
				
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					
					int index = ((Combo)e.getSource()).getSelectionIndex();
					DesktopView.this.getZoomManager().setZoomAsText(((Combo)e.getSource()).getItem(index));
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
			
					
				}
			});

			return cLayout;
		}
		
	}

	public class StructureLevel extends ControlContribution{

		private Combo cLayout = null;
		
		public StructureLevel(String id){
			super(id);
		}
		@Override
		protected Control createControl(Composite parent) {
			
			cLayout = new Combo(parent,SWT.BORDER);
			
			cLayout.setToolTipText("Livello profondità struttura");
			cLayout.add("1");
			cLayout.add("2");
			cLayout.add("3");
			cLayout.add("4");
			cLayout.add("5");
			cLayout.add("6");
			cLayout.add("7");
			cLayout.add("8");
			cLayout.add("9");
			cLayout.add("10");
			
			cLayout.select(0);
			cLayout.addSelectionListener(new SelectionListener() {
				
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					
					int index = ((Combo)e.getSource()).getSelectionIndex();
					DesktopView.this.structureLevel = Integer.valueOf(((Combo)e.getSource()).getItem(index));
					DesktopView.this.addItemToExplore(DesktopView.this.rootNodes);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
			
					
				}
			});
			cLayout.setEnabled(false);
			return cLayout;
		}
		public Combo getcLayout() {
			return cLayout;
		}
		
	}

	private class DesktopMouseListener extends MouseAdapter{
		
		private DesktopView dv = null;
		
		public DesktopMouseListener(DesktopView dv){
			this.dv = dv;
		}
	
		public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
        	Graph g = ((Graph)e.getSource());
			if (g.getSelection().size() >0){
				CGraphNode sel = (CGraphNode)g.getSelection().get(0);
				MyNode myn = (MyNode)sel.getData();		
				if (myn.getType().equalsIgnoreCase(WinkhouseUtils.PROMEMORIA)){
					ApriDettaglioPromemoriaAction adpa = new ApriDettaglioPromemoriaAction(dv, (PromemoriaModel)myn.getObjModel());
					adpa.run();
				}
				if (myn.getType().equalsIgnoreCase(WinkhouseUtils.IMMOBILI)){					
					ApriDettaglioImmobilePerspectiveAction adipa = new ApriDettaglioImmobilePerspectiveAction((ImmobiliModel)myn.getObjModel());
					adipa.setComparerView(true);
					adipa.run();
				}
				if (myn.getType().equalsIgnoreCase(WinkhouseUtils.ANAGRAFICHE)){
					ApriDettaglioAnagraficaPerspectiveAction adapa = new ApriDettaglioAnagraficaPerspectiveAction((AnagraficheModel)myn.getObjModel());
					adapa.setComparerView(true);
					adapa.run();					
				}
				if (myn.getType().equalsIgnoreCase(WinkhouseUtils.AFFITTI)){
					ApriAffittiAction aaa = new ApriAffittiAction((AffittiModel)myn.getObjModel());					
					aaa.run();					
				}
				if (myn.getType().equalsIgnoreCase(WinkhouseUtils.COLLOQUI)){
					ApriDettaglioColloquioAction adca = new ApriDettaglioColloquioAction((ColloquiModel)myn.getObjModel());
					adca.setComparerView(true);
					adca.run();					
				}
				if (myn.getType().equalsIgnoreCase(MyNode.FILELINK) || myn.getType().equalsIgnoreCase(MyNode.URLLINK)){
					Program.launch(myn.getName());

				}
				
			}
        	  
							
		}

		@Override
		public void mouseDown(MouseEvent e) {								
		}

		@Override
		public void mouseUp(MouseEvent e) {
			super.mouseUp(e);
			Graph g = ((Graph)e.getSource());
			if (g.getSelection().size() >0){
				CGraphNode sel = (CGraphNode)g.getSelection().get(0);
				MyNode myn = (MyNode)sel.getData();				
				myn.setSelected(!myn.isSelected());
			}
			dv.getDesktop().refresh();				
		}

	}

	private class AgentiChoser extends ControlContribution{
		
		public AgentiChoser(String id){
			super(id);
		}

		@Override
		protected Control createControl(Composite parent) {
			
			agenti = new ComboViewer(parent,SWT.BORDER);
			agenti.setLabelProvider(new ILabelProvider() {
				
				@Override
				public void removeListener(ILabelProviderListener listener) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean isLabelProperty(Object element, String property) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public void dispose() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void addListener(ILabelProviderListener listener) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public String getText(Object element) {
					 
					return ((AgentiVO)element).toString();
				}
				
				@Override
				public Image getImage(Object element) {
					// TODO Auto-generated method stub
					return null;
				}
			});
			agenti.setContentProvider(new IStructuredContentProvider() {
				
				@Override
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {			
				}
				
				@Override
				public void dispose() {
				}
				
				@Override
				public Object[] getElements(Object inputElement) {
					if (WinkhouseUtils.getInstance().getLoggedAgent() == null){
						return new AgentiDAO().list(AgentiModel.class.getName()).toArray();
					}else{
						return new Object[]{new AgentiDAO().getAgenteById(AgentiModel.class.getName(), WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente())}; 
					}
				}
			});
			agenti.getCombo().setToolTipText("Cambia raggruppamento dati");
			agenti.addSelectionChangedListener(new ISelectionChangedListener() {
				
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					if (desktop_type == PROMEMORIA_TYPE){ 
						AgentiModel amodel = null;
						StructuredSelection	ss = (StructuredSelection)event.getSelection();
						if (ss != null && ss.getFirstElement() != null){
							amodel = (AgentiModel)ss.getFirstElement();						
						}
						if (amodel != null){
							setAgente(amodel);
						}
						
					}
				}
			});
			
			agenti.setInput(new Object());
			return agenti.getCombo();
		}
		
		
		
	}

	private class ResetAgenteAction extends Action{

		public ResetAgenteAction(String text, ImageDescriptor image) {
			super(text, image);
		}

		@Override
		public void run() {
			if (desktop_type == PROMEMORIA_TYPE){
				setAgente(WinkhouseUtils.getInstance().getLoggedAgent());				
				agenti.setSelection(null);
			}			
		}
		
	}
	
	public DesktopView() {

	}

	@Override
	public void createPartControl(Composite parent) {
		
		desktop = new GraphViewer(parent, SWT.BORDER);
		desktop.getGraphControl().setBackground(new Color(PlatformUI.getWorkbench().getDisplay(), 254, 255, 210));
		
		zoomManager = new ZoomManager(desktop.getGraphControl().getRootLayer(), desktop.getGraphControl().getViewport());
		
		ras = new ResetAgenteAction("Togli filtro agente",resetAgenti);
		cdta = new ChangeDesktopTypeAction("", Action.AS_CHECK_BOX);
		apa = new AddPromemoriaAction (this);
		dpa = new DeletePromemoriaAction(this);
		rpa = new RefreshPromemoriaAction(this);
		ac = new AgentiChoser("agentiChoser");
		pager = new Pager("pager");
		selettoreDisposizione = new SelettoreDisposizione("disposizione");
		selettoreStructureLevel = new StructureLevel("livello struttura");
		zs = new SelettoreZoom("zoom");
		
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		
		mgr.add(ras);
		mgr.add(ac);
		mgr.add(zs);
		mgr.add(new Separator());		
		mgr.add(cdta);
		mgr.add(new Separator());
		mgr.add(selettoreDisposizione);
		mgr.add(selettoreStructureLevel);
		mgr.add(new Separator());
		mgr.add(apa);
		mgr.add(dpa);
		mgr.add(rpa);	
		mgr.add(new Separator());
		mgr.add(pager);
		
		desktop.setContentProvider(new ZestNodeContentProvider());
		desktop.setLabelProvider(new GraphLabelProvider());
		desktop.getGraphControl().addMouseListener(new DesktopMouseListener(this));
		
		setAgente(WinkhouseUtils.getInstance().getLoggedAgent());

		LayoutAlgorithm layout = setLayout();	    
		desktop.setLayoutAlgorithm(layout, true);
		desktop.applyLayout();		
		fillToolBar();
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private LayoutAlgorithm setLayout() {
	    LayoutAlgorithm layout;
	    //layout = new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	    // layout = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	    //layout = new GridLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	    // layout = new
	    // HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	    // layout = new
	    layout = new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	    return layout;
	}
	
	private void fillToolBar() {
	    ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
	    IActionBars bars = getViewSite().getActionBars();
	    bars.getMenuManager().add(toolbarZoomContributionViewItem);
	}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return desktop;
	}

	public GraphViewer getDesktop() {
		return desktop;
	}

	public void setAgente(AgentiModel agente){
		
		this.agente = agente;
		
		ArrayList<MyNode> nodes = new ArrayList<MyNode>();
		ArrayList<PromemoriaModel> alp = null;
		
		PromemoriaDAO pDAO = new PromemoriaDAO();
		if (agente != null){
			alp = pDAO.getPromemoriaByAgente(PromemoriaModel.class.getName(), agente.getCodAgente());
			alp.addAll(pDAO.getAllPromemoriaAgenteIsNull(PromemoriaModel.class.getName()));
		}else{
			alp = pDAO.getAllPromemoria(PromemoriaModel.class.getName());
		}
		
		pagenumber = alp.size() / itemxpage;
		pagenumber = (pagenumber==0)?1:pagenumber;
		List<PromemoriaModel> al = alp.subList((currentpage == 1)?0:currentpage * itemxpage, 
											   ((alp.size() - (currentpage * itemxpage)) > itemxpage)?(currentpage * itemxpage) + itemxpage:(currentpage * itemxpage)+(alp.size() - (currentpage * itemxpage))); 
		
		for (PromemoriaModel promemoriaModel : al) {
			nodes.add(new MyNode(getDesktop_type(),promemoriaModel.getCodPromemoria().toString(), promemoriaModel.getDescrizione(),promemoriaModel,WinkhouseUtils.PROMEMORIA,this.structureLevel));
		}

		desktop.setInput(nodes);
		
	}
	
	public void addItemToExplore(ArrayList objectsToAdd){
		
		ArrayList<MyNode> nodes = new ArrayList<MyNode>();
		pagenumber = objectsToAdd.size() / itemxpage;
		pagenumber = (pagenumber==0)?1:pagenumber;
		
		List al = new ArrayList();
		try {
			al = objectsToAdd.subList((currentpage == 1)?0:currentpage * itemxpage, 
										   ((objectsToAdd.size() - (currentpage * itemxpage)) > itemxpage)?(currentpage * itemxpage) + itemxpage:(currentpage * itemxpage)+(objectsToAdd.size() - (currentpage * itemxpage)));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		this.rootNodes = new ArrayList();
		if (desktop.getInput() != null && desktop.getInput() instanceof ArrayList){
			//nodes = (ArrayList<MyNode>)desktop.getInput();
			for (Object obj : al) {
				if (obj instanceof ImmobiliModel){
					MyNode rootnode = new MyNode(getDesktop_type(),((ImmobiliModel)obj).getCodImmobile().toString(), ((ImmobiliModel)obj).toString(),obj,WinkhouseUtils.IMMOBILI, 1);
					this.rootNodes.add(rootnode.getObjModel());
					exploreHierarchyItem(nodes, rootnode, 1);
				}
				if (obj instanceof AnagraficheModel){
					MyNode rootnode = new MyNode(getDesktop_type(),((AnagraficheModel)obj).getCodAnagrafica().toString(), ((AnagraficheModel)obj).toString(),obj,WinkhouseUtils.ANAGRAFICHE, 1);					
					this.rootNodes.add(rootnode.getObjModel());
					exploreHierarchyItem(nodes, rootnode, 1);
				}
			}
			
			desktop.setInput(nodes);
		}
		
	}
	
	private void exploreHierarchyItem(ArrayList<MyNode> alcontainer, MyNode rootnode, int currentlevel){
		
		//System.out.println(rootnode.getId() + "_" + rootnode.getName());
		
		alcontainer.add(rootnode);	
				
		List<MyNode> childs = rootnode.getConnectedTo();
		currentlevel++;
		for (MyNode child: childs){
			if (currentlevel <= this.structureLevel){
				if (!nodeExist(alcontainer, child)){				
					this.exploreHierarchyItem(alcontainer, child, currentlevel);
				}				
			}
						
		}
		
	}
	
	protected boolean nodeExist(ArrayList<MyNode> container, MyNode node){
		for (MyNode cnode : container){
			if ((cnode.getType().equalsIgnoreCase(node.getType())) && ((cnode.getId().equalsIgnoreCase(node.getId())))){
				return true;
			}
		}
		return false;
	}
	
	public AgentiModel getAgente() {
		return agente;
	}

	public Integer getItemxpage() {
		return itemxpage;
	}

	public void setItemxpage(Integer itemxpage) {
		this.itemxpage = itemxpage;
	}

	public Integer getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(Integer currentpage) {
		this.currentpage = currentpage;
	}

	public Integer getPagenumber() {
		return pagenumber;
	}

	public void setPagenumber(Integer pagenumber) {
		this.pagenumber = pagenumber;
	}
	
	public int getDesktop_type() {
		return desktop_type;
	}
	
	public void setDesktop_type(int desktop_type) {
		this.desktop_type = desktop_type;
	}

	public ZoomManager getZoomManager() {
		return zoomManager;
	}

	public void setZoomManager(ZoomManager zoomManager) {
		this.zoomManager = zoomManager;
	}

	public Integer getStructureLevel() {
		return structureLevel;
	}

	public DeletePromemoriaAction getDpa() {
		return dpa;
	}

	public StructureLevel getSelettoreStructureLevel() {
		return selettoreStructureLevel;
	}

	public ComboViewer getAgenti() {
		return agenti;
	}

	public AddPromemoriaAction getApa() {
		return apa;
	}
	
}
