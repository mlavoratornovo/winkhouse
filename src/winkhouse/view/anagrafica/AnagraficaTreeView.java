package winkhouse.view.anagrafica;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.types.ValueObjectTypeFactory;
import org.apache.cayenne.query.ObjectSelect;
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
import winkhouse.db.orm.CayenneContextManager;
import winkhouse.helper.ProfilerHelper;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ClassiClientiModel;
import winkhouse.orm.Anagrafiche;
import winkhouse.orm.Classicliente;
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
	private Image anagraficaImg = Activator.getImageDescriptor("icons/anagrafica.png").createImage();
	private Image classeImg = Activator.getImageDescriptor("icons/classianagrafiche.png").createImage();
	private Image anagraficaImmobileImg = Activator.getImageDescriptor("icons/anagraficaImmobile.png").createImage();
	private Image geoImg = Activator.getImageDescriptor("icons/cercacomune.png").createImage();
	private boolean geogrouping = false; 
	
	@Override
	public void createPartControl(Composite parent) {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(new GeoGroupingAction("Raggruppamento geografico",
									  Action.AS_CHECK_BOX));
		
		mgr.add(new OrdinaAnagraficheAction("Ordina anagrafiche", Action.AS_CHECK_BOX));
		mgr.add(new FiltraAnagraficheAction("Attiva filtro anagrafiche", Action.AS_CHECK_BOX));
		mgr.add(new ChangeAnagraficheFilterAction("Visualizza solo i proprietari", Action.AS_CHECK_BOX));
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
					if (((StructuredSelection)viewer.getSelection()).getFirstElement() instanceof Anagrafiche){
						Anagrafiche aVO = (Anagrafiche)((StructuredSelection)viewer.getSelection()).getFirstElement();
						ApriDettaglioAnagraficaAction adaa = new ApriDettaglioAnagraficaAction(aVO, null);
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
								
								Anagrafiche aModel = new Anagrafiche();
								if (((StructuredSelection)viewer.getSelection()).getFirstElement() instanceof ClassiClientiVO){
									
									try {											
										Object o = ((StructuredSelection)viewer.getSelection()).getFirstElement();
										if ((o != null) && (o instanceof ClassiClientiVO)){
											ClassiClientiVO ccVO = (ClassiClientiVO)o;
											aModel.setClassicliente(null);
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
			if (parentElement instanceof Classicliente){
				Classicliente cc = classiDAO.getClasseClienteById(((ClassiClientiVO)parentElement).getCodClasseCliente());
				alAnagrafiche = anagraficheDAO.getAnagraficheByClasse(cc).toArray();
			}else{
				
				ArrayList alClassi = classiDAO.list(null); 
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
						ArrayList anagrafiche = anagraficheDAO.getAnagraficheByComuneClasse(((ComuniVO)parentElement).getComune(),
																						    null);
						anagrafiche = ProfilerHelper.getInstance().filterAnagrafiche(anagrafiche, false);
						alClassi.addAll(anagrafiche);
						return alClassi.toArray();
					}else{
						if (parentElement instanceof ClassiClientiVO){		
							Classicliente cc = classiDAO.getClasseClienteById(((ClassiClientiModel)parentElement).getCodClasseCliente());
							ArrayList anagrafiche = anagraficheDAO.getAnagraficheByComuneClasse(((ClassiClientiModel)parentElement).getComune(),cc);
							anagrafiche = ProfilerHelper.getInstance().filterAnagrafiche(anagrafiche, false);
							cc = null;
							return anagrafiche.toArray();
						}
					}						
				}
				if (parentElement instanceof ClassiClientiVO){
					Classicliente cc = classiDAO.getClasseClienteById(((ClassiClientiModel)parentElement).getCodClasseCliente());
					ArrayList anagrafiche = anagraficheDAO.getAnagraficheByComuneClasse(((ClassiClientiModel)parentElement).getComune(),cc);
					anagrafiche = ProfilerHelper.getInstance().filterAnagrafiche(anagrafiche, false);
					cc = null;
					return anagrafiche.toArray();
				}
				ArrayList anagrafiche = anagraficheDAO.getAnagraficheByNullProvincia();
				ArrayList provincie = comuniDAO.getProvincieAnagrafiche();
				provincie.addAll(anagrafiche);
				return provincie.toArray();
			}else{
		
				if (parentElement instanceof Classicliente){
					return ((Classicliente)parentElement).getAnagrafiches().toArray();
				}else{
						ArrayList alClassi = classiDAO.list(ClassiClientiVO.class.getName());
						ArrayList anagrafiche = anagraficheDAO.getAnagraficheByNullClasse(); 
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
			
			return (element instanceof ComuniVO) || (element instanceof ClassiClientiModel) || ((element instanceof Classicliente) && (((Classicliente)element).getAnagrafiches().size() > 0));
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
			if (obj instanceof Classicliente){
				returnValue = ((Classicliente)obj).getDescrizione();
			}
			if (obj instanceof ClassiClientiModel){
				returnValue = ((ClassiClientiModel)obj).getDescrizione();
			}
			
			if (obj instanceof Anagrafiche){
				returnValue = ""+((Anagrafiche)obj).getCodAnagrafica() + " - " +
							  ((!((Anagrafiche)obj).getRagsoc().equalsIgnoreCase(""))
							   ? ((Anagrafiche)obj).getRagsoc()+" - "
							   : "") +
							  ((!((Anagrafiche)obj).getCognome().equalsIgnoreCase(""))
							   ? ((Anagrafiche)obj).getCognome()
							   : "") + " - " +
							  ((!((Anagrafiche)obj).getNome().equalsIgnoreCase(""))
							   ? ((Anagrafiche)obj).getNome()
							   : "");
							   
			}			
			return returnValue;
		}

		public Image getImage(Object obj) {
			if (obj instanceof ComuniVO){
				return geoImg;
			}
			if (obj instanceof Anagrafiche){
				if (
					(((Anagrafiche)obj).getImmobilis() != null) &&
					(((Anagrafiche)obj).getImmobilis().size() > 0)
				   ){
					return anagraficaImmobileImg;
				}else{
					return anagraficaImg;
				}
			}
			return classeImg;
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