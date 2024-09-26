package winkhouse.wizard.colloqui;

import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.Activator;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Tipologieimmobili;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.TipologieImmobiliVO;
import winkhouse.wizard.ColloquiWizard;



public class SelezioneImmobile extends WizardPage {

	private Composite container = null;
	private TreeViewer tvImmobili = null;
	private TipologieImmobiliDAO tiDAO =  new TipologieImmobiliDAO();
	private ImmobiliDAO iDAO =  new ImmobiliDAO();
	Text timmobile = null;
	Text tcodice = null;
	
	public SelezioneImmobile(String pageName) {
		super(pageName);
	}

	public SelezioneImmobile(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}
	
	private class ViewContentProvider implements ITreeContentProvider {
		
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
				return iDAO.getImmobiliByTipologia(ImmobiliModel.class.getName(), ((TipologieImmobiliVO)parentElement).getCodTipologiaImmobile()).toArray();
			}else{
				ArrayList tipologie = (ArrayList)tiDAO.list(TipologieImmobiliVO.class.getName());
				tipologie.addAll(iDAO.getImmobiliByTipologia(ImmobiliModel.class.getName(), 0));
				return tipologie.toArray();
			}
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {			
			return element instanceof TipologieImmobiliVO;
		}

	}

	private class ViewLabelProvider extends LabelProvider {
		
		public String getText(Object obj) {
			String returnValue = null;
			if (obj instanceof TipologieImmobiliVO){
				returnValue = ((TipologieImmobiliVO)obj).getDescrizione();
			}
			if (obj instanceof ImmobiliModel){
				returnValue = ((ImmobiliModel)obj).toString();
			}			
			return returnValue;
		}

		public Image getImage(Object obj) {
			if (obj instanceof ImmobiliModel){
				return Activator.getDefault().getImageDescriptor("icons/gohome.png").createImage();
			}else{			
				return Activator.getDefault().getImageDescriptor("icons/folder_home.png").createImage();
			}			
		}
	}
	
		private  void ricercaCodice() {
			
			boolean isnumber = true;
			if (isnumber){
				tvImmobili.expandAll();
				TreeItem ti = searchTreeItem(tvImmobili.getTree().getItems(), tcodice.getText());
				if (ti == null){
					MessageBox mb = new MessageBox(getShell());
					mb.setMessage("Riferimento immobile non trovato");
					mb.open();
				}else{
					Object[] sel = new Object[1];
					sel[0] = ti.getData();
					StructuredSelection ss = new StructuredSelection(sel);
					
					tvImmobili.setSelection(ss, true);
	//				SelectionChangedEvent e = new SelectionChangedEvent(tv,ss);
					Event e = new Event();
					e.item = ti;
					e.data = ti.getData();
					e.widget = tvImmobili.getTree();
					tvImmobili.getTree().notifyListeners(SWT.Selection, e);
				}
			}else{
				MessageBox mb = new MessageBox(getShell());
				mb.setMessage("Riferimento immobile non valido");
				mb.open();				
			}
		}
		
		private TreeItem searchTreeItem(TreeItem[] lista, String cod){
			TreeItem ti = null;
			//ImmobileVO returnValue = null;
			for (int i = 0; i < lista.length; i++) {
				if (lista[i].getData() instanceof TipologieImmobiliVO){
					ti = searchTreeItem(lista[i].getItems(),cod);
					if (ti != null){
						break;
					}
					
				}else{
					if(((ImmobiliVO)lista[i].getData()).getRif().equalsIgnoreCase(cod)){
						
						ti = lista[i];
						break;
					}
				}
			}
			return ti;
		}
		
	
	

	@Override
	public void createControl(Composite parent) {
		setTitle(getName());
		container  = new Composite(parent, SWT.NONE);
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;

		container.setLayout(new GridLayout());
		container.setLayoutData(gdFillHV);
		
		Composite toolbar = new Composite(container,SWT.NONE);
		GridLayout gl3 = new GridLayout();
		gl3.numColumns = 3;
		
		toolbar.setLayout(gl3);
		
		GridData gdFillH = new GridData();
		gdFillH.grabExcessHorizontalSpace = true;
		gdFillH.horizontalAlignment = SWT.FILL;
		
		Label lcodice = new Label(toolbar,SWT.NONE);
		lcodice.setText("Riferimento immobile");
		
		tcodice = new Text(toolbar,SWT.NONE);
		
		ImageHyperlink ihCerca = new ImageHyperlink(toolbar, SWT.WRAP);	
		ihCerca.setToolTipText("Cerca");
		ihCerca.setImage(Activator.getImageDescriptor("/icons/kfind.png").createImage());
		ihCerca.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				ricercaCodice();				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});
//		ihCerca.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());

		
		tvImmobili = new TreeViewer(container);
		tvImmobili.getTree().setLayoutData(gdFillHV);
		tvImmobili.setContentProvider(new ViewContentProvider());
		tvImmobili.setLabelProvider(new ViewLabelProvider());		
		tvImmobili.getTree().addMouseListener(new MouseListener(){
			
			public void mouseDoubleClick(MouseEvent e) {
				
			}
			
			public void mouseDown(MouseEvent e) {
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				
				ImmobiliVO iVO = (tvImmobili.getSelection()!= null)
								 ? (((StructuredSelection)tvImmobili.getSelection()).getFirstElement() != null)
								   ? (ImmobiliVO)((StructuredSelection)tvImmobili.getSelection()).getFirstElement()
								   : null
								 : null;
				
				if (iVO != null){
					((ColloquiWizard)getWizard()).getColloquio().setCodImmobileAbbinato(iVO.getCodImmobile());
					timmobile.setText(iVO.getCodImmobile() + " - " +iVO.getIndirizzo());
				}
				
				((ColloquiWizard)getWizard()).getContainer().updateButtons();
				
			}
			
		});
		
		tvImmobili.setInput(new Object());
		
		Label immobile = new Label(container,SWT.NONE);
		immobile.setText("Immobile selezionato");
		
		timmobile = new Text(container,SWT.FLAT);
		timmobile.setLayoutData(gdFillH);
		
		setControl(container);
	}

}
