package winkhouse.view.anagrafica;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.operation.IRunnableWithProgress;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.anagrafiche.ApriDettaglioAnagraficaAction;
import winkhouse.action.anagrafiche.ChangeAnagraficheFilterAction;
import winkhouse.action.anagrafiche.FiltraAnagraficheAction;
import winkhouse.action.anagrafiche.GeoGroupingAction;
import winkhouse.action.anagrafiche.NuovaAnagraficaAction;
import winkhouse.action.anagrafiche.OrdinaAnagraficheAction;
import winkhouse.action.anagrafiche.RefreshAnagraficheAction;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.ClassiClientiDAO;
import winkhouse.dao.ComuniDAO;
import winkhouse.helper.ProfilerHelper;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ClassiClientiModel;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.ComuniVO;



public class AnagraficaTreeView extends ViewPart 
								{

	private TreeViewer viewer = null;
	private AnagraficheDAO anagraficheDAO = new AnagraficheDAO();
	private ClassiClientiDAO classiDAO = new ClassiClientiDAO();
	private ComuniDAO comuniDAO = new ComuniDAO();
	public final static String ID = "winkhouse.anagrafichetreeview";
	private Image anagraficaImg = Activator.getDefault().getImageDescriptor("icons/anagrafica.png").createImage();
	private Image classeImg = Activator.getDefault().getImageDescriptor("icons/classianagrafiche.png").createImage();
	private Image anagraficaImmobileImg = Activator.getDefault().getImageDescriptor("icons/anagraficaImmobile.png").createImage();
	private Image geoImg = Activator.getImageDescriptor("icons/cercacomune.png").createImage();
	private boolean geogrouping = false; 
	
	@Override
	public void createPartControl(Composite parent) {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(new GeoGroupingAction("Raggruppamento geografico",
									  Action.AS_CHECK_BOX));
		
		mgr.add(new OrdinaAnagraficheAction("Ordina anagrafiche", Action.AS_CHECK_BOX));
		mgr.add(new FiltraAnagraficheAction("Attiva filtro anagrafiche", Action.AS_CHECK_BOX));
		mgr.add(new ChangeAnagraficheFilterAction("Visualizza solo i propietari", Action.AS_CHECK_BOX));
		mgr.add(new RefreshAnagraficheAction("Ricarica anagrafiche", 
				  Activator.getImageDescriptor("/icons/adept_reinstall.png")));

		mgr.add(new NuovaAnagraficaAction("Nuova anagrafica",
				Activator.getImageDescriptor("/icons/sample2.gif")));

		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.getTree().addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				try {
					if (((StructuredSelection)viewer.getSelection()).getFirstElement() instanceof AnagraficheVO){
						AnagraficheVO aVO = (AnagraficheVO)((StructuredSelection)viewer.getSelection()).getFirstElement();
						ApriDettaglioAnagraficaAction adaa = new ApriDettaglioAnagraficaAction(new AnagraficheModel(aVO), null);
						adaa.run();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
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
					
					
					
						MenuItem miNuovo = new MenuItem(m,SWT.PUSH);
						miNuovo.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
						miNuovo.setText("Nuova anagrafica");
						miNuovo.addSelectionListener(new SelectionListener(){

							@Override
							public void widgetDefaultSelected(SelectionEvent e) {
							}

							@Override
							public void widgetSelected(SelectionEvent e) {
								
								AnagraficheModel aModel = new AnagraficheModel();
								if (((StructuredSelection)viewer.getSelection()).getFirstElement() instanceof ClassiClientiVO){
									
									try {											
										Object o = ((StructuredSelection)viewer.getSelection()).getFirstElement();
										if ((o != null) && (o instanceof ClassiClientiVO)){
											ClassiClientiVO ccVO = (ClassiClientiVO)o;
											aModel.setCodClasseCliente(ccVO.getCodClasseCliente());
										}
										
									} catch (Exception e1) {										
										e1.printStackTrace();
									}
								}						
								ApriDettaglioAnagraficaAction adaa = new ApriDettaglioAnagraficaAction(aModel, null);
								adaa.run();
								
							}
							
						});
						
					
					if (o instanceof AnagraficheVO){
						
						MenuItem miCancella = new MenuItem(m,SWT.PUSH);
						miCancella.setImage(Activator.getImageDescriptor("/icons/edittrash.png").createImage());
						miCancella.setText("Cancella anagrafica");
						
					}
					
					
					viewer.getTree().setMenu(m);
				}
				
			}
			
		});

	//	viewer.setInput(new Object());
	}
	
	private class AnagraficheLoader implements IRunnableWithProgress{
		Object[] alAnagrafiche = null;
		Object parentElement = null;
		public AnagraficheLoader(Object[] alAnagrafiche, Object parentElement){
			this.alAnagrafiche = alAnagrafiche;
			this.parentElement = parentElement;
		}
		
		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, 
														 InterruptedException {
			if (parentElement instanceof ClassiClientiVO){
				alAnagrafiche = anagraficheDAO.getAnagraficheByClasse(AnagraficheModel.class.getName(),((ClassiClientiVO)parentElement).getCodClasseCliente()).toArray();
			}else{
				ArrayList alClassi = classiDAO.list(ClassiClientiVO.class.getName()); 
				alClassi.addAll(anagraficheDAO.getAnagraficheByClasse(AnagraficheModel.class.getName(),0));
				alAnagrafiche = alClassi.toArray();
			}

			
		}
		
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
			
			if (isGeogrouping()){
				if (parentElement instanceof ComuniVO){
					if (((ComuniVO)parentElement).getProvincia().equalsIgnoreCase("") == false){
						return comuniDAO.getComuniByProvinciaAnagrafiche(((ComuniVO)parentElement).getProvincia()).toArray();
					}
					if (((ComuniVO)parentElement).getComune().equalsIgnoreCase("") == false){
						ArrayList alClassi = classiDAO.listByComune(((ComuniVO)parentElement).getComune());
						ArrayList anagrafiche = anagraficheDAO.getAnagraficheByComuneClasse(AnagraficheModel.class.getName(),
																						    ((ComuniVO)parentElement).getComune(),
																						    null);
						anagrafiche = ProfilerHelper.getInstance().filterAnagrafiche(anagrafiche, false);
						alClassi.addAll(anagrafiche);
						return alClassi.toArray();
					}else{
						if (parentElement instanceof ClassiClientiVO){		
							ArrayList anagrafiche = anagraficheDAO.getAnagraficheByComuneClasse(AnagraficheModel.class.getName(),
																								((ClassiClientiModel)parentElement).getComune(),
																							    ((ClassiClientiModel)parentElement).getCodClasseCliente());
							anagrafiche = ProfilerHelper.getInstance().filterAnagrafiche(anagrafiche, false);
							return anagrafiche.toArray();
						}
					}						
				}
				if (parentElement instanceof ClassiClientiVO){		
					ArrayList anagrafiche = anagraficheDAO.getAnagraficheByComuneClasse(AnagraficheModel.class.getName(),
																						((ClassiClientiModel)parentElement).getComune(),
																					    ((ClassiClientiModel)parentElement).getCodClasseCliente());
					anagrafiche = ProfilerHelper.getInstance().filterAnagrafiche(anagrafiche, false);
					return anagrafiche.toArray();
				}
				
				return comuniDAO.getProvincieAnagrafiche().toArray();
			}else{
		
				if (parentElement instanceof ClassiClientiVO){
					ArrayList anagrafiche = anagraficheDAO.getAnagraficheByClasse(AnagraficheModel.class.getName(),((ClassiClientiVO)parentElement).getCodClasseCliente());
					anagrafiche = ProfilerHelper.getInstance().filterAnagrafiche(anagrafiche, false);
					return anagrafiche.toArray();
				}else{
						ArrayList alClassi = classiDAO.list(ClassiClientiVO.class.getName());
						ArrayList anagrafiche = anagraficheDAO.getAnagraficheByClasse(AnagraficheModel.class.getName(),null);
						anagrafiche = ProfilerHelper.getInstance().filterAnagrafiche(anagrafiche, false);
						alClassi.addAll(anagrafiche);
						return alClassi.toArray();
					}
					
				}
			
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {	
			
			return (element instanceof ClassiClientiVO) || (element instanceof ComuniVO);
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
			if (obj instanceof ClassiClientiVO){
				returnValue = ((ClassiClientiVO)obj).getDescrizione();
			}
			if (obj instanceof AnagraficheVO){
				returnValue = ((AnagraficheVO)obj).getCodAnagrafica() + " - " +
							  ((!((AnagraficheVO)obj).getRagioneSociale().equalsIgnoreCase(""))
							   ? ((AnagraficheVO)obj).getRagioneSociale()
							   : "") + " - " +
							  ((!((AnagraficheVO)obj).getCognome().equalsIgnoreCase(""))
							   ? ((AnagraficheVO)obj).getCognome()
							   : "") + " - " +
							  ((!((AnagraficheVO)obj).getNome().equalsIgnoreCase(""))
							   ? ((AnagraficheVO)obj).getNome()
							   : "");
							   
			}			
			return returnValue;
		}

		public Image getImage(Object obj) {
			if (obj instanceof ComuniVO){
				return geoImg;
			}
			if (obj instanceof AnagraficheVO){
				if (
					(((AnagraficheModel)obj).getImmobili() != null) &&
					(((AnagraficheModel)obj).getImmobili().size() > 0)
				   ){
					return anagraficaImmobileImg;
				}else{
					return anagraficaImg;
				}
			}else{			
				return classeImg;
			}			
		}
	}
	
	
	@Override
	public void setFocus() {
		viewer.getTree().setFocus();
	/*	RefreshAnagraficheAction raa = new RefreshAnagraficheAction();
		raa.run();*/

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
