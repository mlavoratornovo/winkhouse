package winkhouse.view.immobili;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.Activator;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.model.ImmobiliModel;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ImmobiliVO;
import winkhouse.vo.TipologieImmobiliVO;



public class PopUpRicercaImmobile extends Dialog {

	private TreeViewer viewer = null;
	private ImmobiliDAO immobiliDAO = new ImmobiliDAO();
	private TipologieImmobiliDAO tipologieImmobiliDAO = new TipologieImmobiliDAO();
//	private Shell popup = null;
	private Object callerObj = null;
	private String setterMethodName = null;
	private Text indirizzo = null;
	
	
	public PopUpRicercaImmobile(Shell shell) {
		super(shell);		
	}
	

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(SWT.CLOSE | SWT.BORDER | SWT.RESIZE);
	}

	@Override
    protected Point getInitialSize() {
        return new Point(450, 300);
    }
	
	@Override
	protected void cancelPressed() {
		super.cancelPressed();
	}


	@Override
	protected void okPressed() {
		
		if ((((StructuredSelection)viewer.getSelection()).getFirstElement() != null) && 
		    (((StructuredSelection)viewer.getSelection()).getFirstElement() instanceof ImmobiliVO)){
			ImmobiliVO iVO = (ImmobiliVO)((StructuredSelection)viewer.getSelection()).getFirstElement();
			ImmobiliModel iModel = new ImmobiliModel(iVO);
			returnValue(iModel);
			close();
		}else{
			MessageBox mb = new MessageBox(getShell(),SWT.ERROR);
			mb.setText("Errore selezione");
			mb.setMessage("Selezionare un immobile");
			mb.open();
		}

	}
	

	private void createContent(Composite container){
		
//		popup = new Shell(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//						  SWT.APPLICATION_MODAL|SWT.SHELL_TRIM);
//		
//		popup.setText("Seleziona immobile");
//		popup.setImage(Activator.getDefault()
//								.getImageDescriptor("icons/gohome.png").createImage());
//		popup.setBackground(new Color(null,255,255,255));
//		popup.setSize(300, 300);
		
		GridLayout gl = new GridLayout();
		GridLayout gl2 = new GridLayout(2,false);
		
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
		
		GridData gdH = new GridData();
		gdH.grabExcessHorizontalSpace = true;		
		gdH.horizontalAlignment = SWT.FILL;
		
		GridData gdcontainer = new GridData();
		gdcontainer.grabExcessHorizontalSpace = true;		
		gdcontainer.horizontalAlignment = SWT.FILL;
		gdcontainer.minimumHeight = 25;
		
		GridData gdH2 = new GridData();
		gdH2.grabExcessHorizontalSpace = true;		
		gdH2.horizontalAlignment = SWT.FILL;
		gdH2.verticalSpan = 2;
				
		container.setLayout(gl);
		container.setLayoutData(gd);
		container.setBackground(new Color(null,255,255,255));
		
		Label l = new Label(container,SWT.FLAT);
		l.setBackground(new Color(null,255,255,255));
		l.setText("Indirizzo");
		l.setLayoutData(gdH);

		Composite c = new Composite(container,SWT.NONE);
		c.setBackground(new Color(null,255,255,255));
		c.setLayout(gl2);
		c.setLayoutData(gdcontainer);
		

		indirizzo = new Text(c,SWT.BORDER);
		indirizzo.setLayoutData(gdH);
		indirizzo.setToolTipText("Città indirizzo");
		
		ImageHyperlink cerca = new ImageHyperlink(c,SWT.WRAP);
//		cerca.setLayoutData(gdH);
		cerca.setImage(Activator.getImageDescriptor("/icons/kfind.png").createImage());
		cerca.setHoverImage(Activator.getImageDescriptor("/icons/kfind.png").createImage());
		cerca.setBackground(new Color(null,255,255,255));
		cerca.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				viewer.setFilters(new ViewerFilter[]{new ViewerFilter() {
					
					@Override
					public boolean select(Viewer viewer, Object parentElement, Object element) {
						
						if (indirizzo.getText().equalsIgnoreCase("")){
							
							return true;
							
						}else{
							
							if (element instanceof ImmobiliModel){
								
								return (((ImmobiliModel)element).getCitta() + " " + ((ImmobiliModel)element).getIndirizzo()).toLowerCase().contains(indirizzo.getText().toLowerCase()) ||  
									   (((ImmobiliModel)element).getCitta() + " " + ((ImmobiliModel)element).getIndirizzo()).toLowerCase().startsWith(indirizzo.getText().toLowerCase());
								
							}else{
								return true;
							}
							
						}
						
					}
					
				}});
				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
				
		viewer = new TreeViewer(container, SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.getTree().setLayoutData(gd);
		 		
		viewer.setInput(new Object());
		
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
			if (parentElement instanceof TipologieImmobiliVO){
				return immobiliDAO.getImmobiliByTipologia(ImmobiliModel.class.getName(), ((TipologieImmobiliVO)parentElement).getCodTipologiaImmobile()).toArray();
			}else{
				ArrayList alTipologie = tipologieImmobiliDAO.list(TipologieImmobiliVO.class.getName());

				alTipologie.addAll(immobiliDAO.getImmobiliByTipologia(ImmobiliModel.class.getName(), 0));					
				
				return alTipologie.toArray();
				
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

	class ViewLabelProvider extends LabelProvider {
		
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
	
	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite c = (Composite)super.createDialogArea(parent);
		c.getShell().setText("Ricerca Selezione immobili");
		createContent(c);
		
		return c;
	}
	public Object getCallerObj() {
		return callerObj;
	}

	public void setCallerObj(Object callerObj) {
		this.callerObj = callerObj;
	}

	public String getSetterMethodName() {
		return setterMethodName;
	}

	public void setSetterMethodName(String setterMethodName) {
		this.setterMethodName = setterMethodName;
	}
	
	private void returnValue(ImmobiliModel returnObj){
		
		if ((callerObj != null) && (setterMethodName != null)){
		
				try {
					Method m = callerObj.getClass().getMethod(setterMethodName, ImmobiliModel.class);
					m.invoke(callerObj, returnObj);			
					close();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			
		}
		
	}
	
}
