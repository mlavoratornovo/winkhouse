package winkhouse.view.colloqui;

import java.util.ArrayList;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.colloqui.ApriDettaglioColloquioAction;
import winkhouse.action.colloqui.CancellaColloquio;
import winkhouse.action.colloqui.NuovoColloquioAction;
import winkhouse.action.colloqui.RefreshColloquiAction;
import winkhouse.configuration.EnvSettingsFactory;
import winkhouse.dao.AgentiDAO;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.ClassiClientiDAO;
import winkhouse.dao.ColloquiDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.model.AgentiColloqui;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ColloquiAgentiModel_Age;
import winkhouse.model.ColloquiAnagraficheModel_Ang;
import winkhouse.model.ColloquiModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.view.anagrafica.DettaglioAnagraficaView;
import winkhouse.view.anagrafica.handler.DettaglioAnagraficaHandler;
import winkhouse.view.colloqui.handler.DettaglioColloquioHandler;
import winkhouse.view.immobili.DettaglioImmobileView;
import winkhouse.view.immobili.handler.DettaglioImmobiliHandler;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.ColloquiAgentiVO;
import winkhouse.vo.ColloquiVO;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.IntegerMonthVO;
import winkhouse.vo.IntegerYearVO;
import winkhouse.vo.TipologieColloquiVO;
import winkhouse.vo.TipologieImmobiliVO;

public class ColloquiTreeView extends ViewPart {
	
	public final static String ID = "winkhouse.colloquitreeview";
	private TreeViewer viewer = null;
	private Combo cGrouping = null;
	private String grouping = TIPO_COLLOQUIO;
	
	private final static String TIPO_COLLOQUIO = "Tipologia colloquio"; 
	private final static String AGENTE_INSERITORE = "Agente inseritore";
	private final static String AGENTE_PARTECIPANTE = "Agente partecipante";
	private final static String IMMOBILE = "Tipologia immobile";
	private final static String ANAGRAFICA = "Classe cliente";
	private final static String DATAINSERIMENTO = "Data inserimento";
	private final static String DATACOLLOQUIO = "Data colloquio";
	
	private Image colloquiImg = Activator.getImageDescriptor("icons/colloqui16x16.png").createImage();
	private Image tipicolloquiImg = Activator.getImageDescriptor("icons/tipicolloqui16.png").createImage();
	private Image agentiImg = Activator.getImageDescriptor("icons/looknfeel.png").createImage();
	private Image classianagraficheImg = Activator.getImageDescriptor("icons/wizardexport/classianagrafiche.png").createImage();
	private Image tipiimmobiliImg = Activator.getImageDescriptor("icons/wizardexport/tipiimmobili.png").createImage();
	private Image immobiliImg = Activator.getImageDescriptor("icons/gohome.png").createImage();
	private Image anagraficaImg = Activator.getImageDescriptor("icons/anagrafica_16.png").createImage();
	private Image calendarioImg = Activator.getImageDescriptor("icons/calendario.png").createImage();
	
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
			
			if (parentElement instanceof TipologieColloquiVO){
				
				ColloquiDAO cDAO = new ColloquiDAO();
				return cDAO.getColloquiByTipologia(ColloquiModel.class.getName(), 
												   ((TipologieColloquiVO) parentElement).getCodTipologiaColloquio()).toArray();
				
			}else if (parentElement instanceof AgentiColloqui){
				
				ColloquiDAO cDAO = new ColloquiDAO();
				
				if (((AgentiColloqui)parentElement).getAgenti_colloquio_type() == AgentiColloqui.AGENTI_COLLOQUIO_INSERITORI ){
					if (((AgentiColloqui)parentElement).getCodAgente() != null){
						return cDAO.getColloquiByAgenteInseritore(ColloquiModel.class.getName(), ((AgentiColloqui)parentElement).getCodAgente()).toArray();
					}else{
						return cDAO.getColloquiWithoutAgenteInseritore(ColloquiModel.class.getName()).toArray();
					}
				}
				
				if (((AgentiColloqui)parentElement).getAgenti_colloquio_type() == AgentiColloqui.AGENTI_COLLOQUIO_PARTECIPANTI ){
					if (((AgentiColloqui)parentElement).getCodAgente() != null){
						return cDAO.getColloquiByAgentePartecipante(ColloquiModel.class.getName(), ((AgentiColloqui)parentElement).getCodAgente()).toArray();
					}else{
						return cDAO.getColloquiWithoutAgentePartecipante(ColloquiModel.class.getName()).toArray();
					}
				}
				
			}else if (parentElement instanceof TipologieImmobiliVO){
				ImmobiliDAO iDAO = new ImmobiliDAO();
				return iDAO.getImmobiliColloquiByTipologiaImmobile(ImmobiliModel.class.getName(), ((TipologieImmobiliVO)parentElement).getCodTipologiaImmobile()).toArray();
				
			}else if (parentElement instanceof ImmobiliModel){
				ColloquiDAO cDAO = new ColloquiDAO();
				return cDAO.getColloquiByImmobile(ColloquiModel.class.getName(), ((ImmobiliModel)parentElement).getCodImmobile()).toArray();
				
			}else if (parentElement instanceof ClassiClientiVO){
				AnagraficheDAO aDAO = new AnagraficheDAO();
				return aDAO.getAnagraficheColloquiByClasse(AnagraficheModel.class.getName(), ((ClassiClientiVO)parentElement).getCodClasseCliente()).toArray();
				
			}else if (parentElement instanceof AnagraficheModel){
				ColloquiDAO cDAO = new ColloquiDAO();
				return cDAO.getColloquiByAnagraficaAllTypes(ColloquiModel.class.getName(), ((AnagraficheModel)parentElement).getCodAnagrafica()).toArray();
				
			}else if ((parentElement instanceof IntegerYearVO)){
				
				ColloquiDAO cDAO = new ColloquiDAO();
				
				if (grouping.equalsIgnoreCase(DATACOLLOQUIO)){
					ArrayList<IntegerMonthVO> al = cDAO.listMonthsByYearDataColloquio(IntegerMonthVO.class.getName(), ((IntegerYearVO)parentElement).getNum());
					for (IntegerMonthVO object : al) {
						object.setYear(((IntegerYearVO)parentElement).getNum());
					}
					return al.toArray();
				}
				if (grouping.equalsIgnoreCase(DATAINSERIMENTO)){
					ArrayList<IntegerMonthVO> al = cDAO.listMonthsByYearDataInserimento(IntegerMonthVO.class.getName(), ((IntegerYearVO)parentElement).getNum());
					for (IntegerMonthVO object : al) {
						object.setYear(((IntegerYearVO)parentElement).getNum());
					}
					return al.toArray();
					
				}
				
			}else if (parentElement instanceof IntegerMonthVO){
				ColloquiDAO cDAO = new ColloquiDAO();
				return cDAO.listColloquiDataInserimentoBy(ColloquiModel.class.getName(), 
														  ((IntegerMonthVO)parentElement).getYear(),
														  ((IntegerMonthVO)parentElement).getNum()).toArray();

			}else{
			
				if (grouping.equalsIgnoreCase(TIPO_COLLOQUIO)){
					return EnvSettingsFactory.getInstance().getTipologieColloqui().toArray();
				}
				if (grouping.equalsIgnoreCase(AGENTE_INSERITORE)) {
					
					AgentiDAO aDAO = new AgentiDAO();
					
					AgentiColloqui nessuno = new AgentiColloqui();
					nessuno.setCognome("senza agente inseritore");
					nessuno.setAgenti_colloquio_type(AgentiColloqui.AGENTI_COLLOQUIO_INSERITORI);
					ArrayList agenti_ins = aDAO.listAgentiColloquiInseritori(AgentiColloqui.class.getName());
					agenti_ins.add(nessuno);
					
					return agenti_ins.toArray();
				}
				if (grouping.equalsIgnoreCase(AGENTE_PARTECIPANTE)){
					
					AgentiDAO aDAO = new AgentiDAO();
					
					AgentiColloqui nessuno = new AgentiColloqui();
					nessuno.setCognome("senza agenti partecipanti");
					nessuno.setAgenti_colloquio_type(AgentiColloqui.AGENTI_COLLOQUIO_PARTECIPANTI);
					ArrayList agenti_par = aDAO.listAgentiColloquiPartecipanti(AgentiColloqui.class.getName());
					agenti_par.add(nessuno);
					
					return agenti_par.toArray();
				}
				if (grouping.equalsIgnoreCase(IMMOBILE)){
					TipologieImmobiliDAO tiDAO = new TipologieImmobiliDAO();
					return tiDAO.list(TipologieImmobiliVO.class.getName()).toArray();
				}
				if (grouping.equalsIgnoreCase(ANAGRAFICA)){
					ClassiClientiDAO ccDAO = new ClassiClientiDAO();
					return ccDAO.list(ClassiClientiVO.class.getName()).toArray();
				}
				if (grouping.equalsIgnoreCase(DATACOLLOQUIO)){
					ColloquiDAO cDAO = new ColloquiDAO();
					return cDAO.listYearsByDataColloquio(IntegerYearVO.class.getName()).toArray();
				}
				if (grouping.equalsIgnoreCase(DATAINSERIMENTO)){
					ColloquiDAO cDAO = new ColloquiDAO();
					return cDAO.listYearsByDataInserimento(IntegerYearVO.class.getName()).toArray();
				}				
			}
			
			return new Object[]{};
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			
			if (element instanceof ColloquiModel){
				return false;
			}
			return true; 
		}

	}
	
	
	class ViewLabelProvider extends LabelProvider {
		
		public String getText(Object obj) {
			
			String returnValue = null;
			
			if (obj instanceof TipologieColloquiVO){
				returnValue = obj.toString();
			}

			if (obj instanceof TipologieImmobiliVO){
				returnValue = ((TipologieImmobiliVO)obj).getDescrizione();
			}
			if (obj instanceof AgentiVO){
				returnValue = ((AgentiVO)obj).toString();
			}
			if (obj instanceof ClassiClientiVO){
				returnValue = ((ClassiClientiVO)obj).toString();
			}			
			if (obj instanceof ColloquiVO){
				returnValue = ((ColloquiVO)obj).toString();
			}
			if (obj instanceof ImmobiliModel){
				returnValue = ((ImmobiliModel)obj).toString();
			}
			if (obj instanceof AnagraficheModel){
				returnValue = ((AnagraficheModel)obj).toString();
			}
			if ((obj instanceof IntegerYearVO) || (obj instanceof IntegerMonthVO)){
				returnValue = obj.toString();
			}
			
			return returnValue;
		}

		public Image getImage(Object obj) {
			
			if ((obj instanceof TipologieColloquiVO)){ 
				return tipicolloquiImg;
			}
			if (obj instanceof ColloquiVO){
				return colloquiImg;
			}
			if (obj instanceof AgentiVO){
				return agentiImg;
			}
			if (obj instanceof ClassiClientiVO){
				return classianagraficheImg;
			}
			if (obj instanceof TipologieImmobiliVO){
				return tipiimmobiliImg;
			}
			
			if (obj instanceof ImmobiliModel){
				return immobiliImg;
			}
			if (obj instanceof AnagraficheModel){
				return anagraficaImg;
			}
			if ((obj instanceof IntegerYearVO) || (obj instanceof IntegerMonthVO)){
				return calendarioImg;
			}
			
			return null;
		}
	}
	
	
	public ColloquiTreeView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {
		
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(new GrouppingChoser("grouping"));
		mgr.add(new RefreshColloquiAction("Ricarica colloqui", 
				  						  Activator.getImageDescriptor("/icons/adept_reinstall.png")));
		mgr.add(new NuovoColloquioAction("Nuovo Colloquio", 
				Activator.getImageDescriptor("/icons/filenew.png"))); 

		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.getTree().addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
				if (((StructuredSelection)viewer.getSelection()).getFirstElement() instanceof ImmobiliVO){
					ImmobiliVO iVO = (ImmobiliVO)((StructuredSelection)viewer.getSelection()).getFirstElement();
					DettaglioImmobileView div = DettaglioImmobiliHandler.getInstance()
																		.getDettaglioImmobile(iVO);
				}
				if (((StructuredSelection)viewer.getSelection()).getFirstElement() instanceof ColloquiModel){
					ColloquiModel cModel = (ColloquiModel)((StructuredSelection)viewer.getSelection()).getFirstElement();
					DettaglioColloquioView dcv = DettaglioColloquioHandler.getInstance()
																		  .getDettaglioColloquio(cModel);
				}
				if (((StructuredSelection)viewer.getSelection()).getFirstElement() instanceof AnagraficheVO){
					AnagraficheVO aVO = (AnagraficheVO)((StructuredSelection)viewer.getSelection()).getFirstElement();
					DettaglioAnagraficaView dav = DettaglioAnagraficaHandler.getInstance()
																		    .getDettaglioAnagrafica(aVO);
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
					
					if (o instanceof ImmobiliVO){
					
						MenuItem miNuovo = new MenuItem(m,SWT.PUSH);
						miNuovo.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
						miNuovo.setText("Nuovo Colloquio");
						miNuovo.addSelectionListener(new SelectionListener(){

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}

							@Override
							public void widgetSelected(SelectionEvent e) {
								
								ColloquiModel cModel;
								
								cModel = new ColloquiModel();
								cModel.setCodImmobileAbbinato(((ImmobiliVO)((StructuredSelection)viewer.getSelection()).getFirstElement()).getCodImmobile());
								cModel.setCodTipologiaColloquio(2);
								
								DettaglioColloquioView dcv = DettaglioColloquioHandler.getInstance()
																					  .getDettaglioColloquio(cModel);
								
								ApriDettaglioColloquioAction adca = new ApriDettaglioColloquioAction(cModel);
								adca.run();
										
							}
							
						});
						
					}
					
					if (o instanceof AnagraficheModel){
						
						MenuItem miNuovo = new MenuItem(m,SWT.PUSH);
						miNuovo.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
						miNuovo.setText("Nuovo Colloquio");
						miNuovo.addSelectionListener(new SelectionListener(){

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}

							@Override
							public void widgetSelected(SelectionEvent e) {
								
								ColloquiModel cModel;
								ColloquiAnagraficheModel_Ang cam_a = new ColloquiAnagraficheModel_Ang();
								cam_a.setCodAnagrafica(((AnagraficheModel)((StructuredSelection)viewer.getSelection()).getFirstElement()).getCodAnagrafica());
								cModel = new ColloquiModel();
								
								cModel.getAnagrafiche().add(cam_a);
								cModel.setCodTipologiaColloquio(1);
								
								DettaglioColloquioView dcv = DettaglioColloquioHandler.getInstance()
																					  .getDettaglioColloquio(cModel);
								
								ApriDettaglioColloquioAction adca = new ApriDettaglioColloquioAction(cModel);
								adca.run();
										
							}
							
						});
						
					}

					if (o instanceof AgentiColloqui){
						
						MenuItem miNuovo = new MenuItem(m,SWT.PUSH);
						miNuovo.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
						miNuovo.setText("Nuovo Colloquio");
						miNuovo.addSelectionListener(new SelectionListener(){

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}

							@Override
							public void widgetSelected(SelectionEvent e) {
								
								ColloquiModel cModel;
								
								cModel = new ColloquiModel();
								
								AgentiColloqui ac = ((AgentiColloqui)((StructuredSelection)viewer.getSelection()).getFirstElement());
								
								if (ac.getAgenti_colloquio_type() == AgentiColloqui.AGENTI_COLLOQUIO_INSERITORI){
									cModel.setCodAgenteInseritore(ac.getCodAgente());
									cModel.setCodTipologiaColloquio(3);
								}
								if (ac.getAgenti_colloquio_type() == AgentiColloqui.AGENTI_COLLOQUIO_PARTECIPANTI){
									ColloquiAgentiVO caVO = new ColloquiAgentiVO();
									caVO.setCodAgente(ac.getCodAgente());
									ColloquiAgentiModel_Age cam_a = new ColloquiAgentiModel_Age(caVO);
									cModel.getAgenti().add(cam_a);
									cModel.setCodTipologiaColloquio(3);
								}
								
								DettaglioColloquioView dcv = DettaglioColloquioHandler.getInstance()
																					  .getDettaglioColloquio(cModel);
								
								ApriDettaglioColloquioAction adca = new ApriDettaglioColloquioAction(cModel);
								adca.run();
										
							}
							
						});
						
					}

					if (o instanceof ColloquiModel){
						
						MenuItem miCancella = new MenuItem(m,SWT.PUSH);
						miCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
						miCancella.setText("Cancella Colloquio");
						miCancella.addSelectionListener(new SelectionListener(){

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}

							@Override
							public void widgetSelected(SelectionEvent e) {
								
								Object o = ((StructuredSelection)viewer.getSelection()).getFirstElement();
								if (o instanceof ColloquiModel){
									CancellaColloquio cc = new CancellaColloquio();
									cc.run();
								}
								
							}
							
						});
						
					}
					
					viewer.getTree().setMenu(m);
				}
				
			}
			
		});		
		
		viewer.setInput(new Object());
	}

	private class GrouppingChoser extends ControlContribution{
		
		public GrouppingChoser(String id){
			super(id);
		}

		@Override
		protected Control createControl(Composite parent) {
			cGrouping = new Combo(parent,SWT.BORDER);
			cGrouping.setToolTipText("Cambia raggruppamento dati");
			cGrouping.add(TIPO_COLLOQUIO);					
			cGrouping.add(AGENTE_INSERITORE);
			cGrouping.add(AGENTE_PARTECIPANTE);
			cGrouping.add(IMMOBILE);
			cGrouping.add(ANAGRAFICA);
			cGrouping.add(DATACOLLOQUIO);
			cGrouping.add(DATAINSERIMENTO);
			cGrouping.select(0);
			cGrouping.addSelectionListener(new SelectionListener() {
				
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					int index = ((Combo)e.getSource()).getSelectionIndex();
					switch (index) {
						case 0:
							grouping = TIPO_COLLOQUIO;
							break;
						case 1:
							grouping = AGENTE_INSERITORE;
							break;
						case 2:
							grouping = AGENTE_PARTECIPANTE;
							break;
						case 3:
							grouping = IMMOBILE;
							break;
						case 4:
							grouping = ANAGRAFICA;
							break;
						case 5:
							grouping = DATACOLLOQUIO;
							break;
						case 6:
							grouping = DATAINSERIMENTO;
							break;

					default:
						grouping = TIPO_COLLOQUIO;
					}
					
					RefreshColloquiAction rca = new RefreshColloquiAction();
					rca.run();
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
			
					
				}
			});
			return cGrouping;
		}
		
		
		
	}

	public TreeViewer getViewer() {
		return viewer;
	}
	
	@Override
	public void setFocus() {}

}
