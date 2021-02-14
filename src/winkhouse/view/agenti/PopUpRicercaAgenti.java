package winkhouse.view.agenti;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.Activator;
import winkhouse.dao.AgentiDAO;
import winkhouse.vo.AgentiVO;



public class PopUpRicercaAgenti {
	
	public final static String ID = "winkhouse.selezione_agenti";
	
	private TableViewer viewer = null;
	private AgentiDAO agentiDAO = new AgentiDAO();
	private Shell popup = null;
	private Object callerObj = null;
	private String setterMethodName = null;
	
	public PopUpRicercaAgenti() {
		super();
		createContent();
	}

	private void createContent(){
		popup = new Shell(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
						  SWT.APPLICATION_MODAL|SWT.SHELL_TRIM);
		popup.setText("Seleziona agente");
		popup.setImage(Activator.getDefault()
								.getImageDescriptor("icons/looknfeel.png").createImage());
		popup.setBackground(new Color(null,255,255,255));
		popup.setSize(300, 300);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalSpan = 2;
		popup.setLayout(gl);
		Composite c = new Composite(popup,SWT.NONE);
		c.setBackground(new Color(null,255,255,255));
		c.setLayout(gl);
		viewer = new TableViewer(c, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.getTable().setLayoutData(gd);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		
		TableColumn tcNome = new TableColumn(viewer.getTable(),SWT.CENTER,0);
		tcNome.setText("Nome");
		tcNome.setWidth(100);
		
		TableColumn tcCognome = new TableColumn(viewer.getTable(),SWT.CENTER,1);
		tcCognome.setText("Cognome");
		tcCognome.setWidth(100);
		
		TableColumn tcCitta = new TableColumn(viewer.getTable(),SWT.CENTER,2);
		tcCitta.setText("Citta");
		tcCitta.setWidth(100);
		
		TableColumn tcIndirizzo = new TableColumn(viewer.getTable(),SWT.CENTER,3);
		tcIndirizzo.setText("Indirizzo");
		tcIndirizzo.setWidth(200);
		
		viewer.setInput(new Object());
		
		ImageHyperlink ihConferma = new ImageHyperlink(c, SWT.WRAP);		
		ihConferma.setImage(Activator.getImageDescriptor("/icons/adept_commit.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/adept_commit_hover.png").createImage());
		ihConferma.setBackground(new Color(null,255,255,255));
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if ((((StructuredSelection)viewer.getSelection()).getFirstElement() != null) && 
					(((StructuredSelection)viewer.getSelection()).getFirstElement() instanceof AgentiVO)){
					AgentiVO aVO = (AgentiVO)((StructuredSelection)viewer.getSelection()).getFirstElement();					
					returnValue(aVO);
				}else{
					MessageBox mb = new MessageBox(popup,SWT.ERROR);
					mb.setText("Errore selezione");
					mb.setMessage("Selezionare un agente");			
					mb.open();
				}				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});

		ImageHyperlink ihCancella = new ImageHyperlink(c, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/button_cancel.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/button_cancel_hover.png").createImage());
		ihCancella.setBackground(new Color(null,255,255,255));
		ihCancella.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				popup.close();				
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}
			
		});
		
		c.setLayoutData(gd);
		
	//	popup.pack();
		popup.open();
	}
	
	class ViewContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {			
			return agentiDAO.list(AgentiVO.class.getName()).toArray();
		}

		@Override
		public void dispose() {}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
		
	}

	class ViewLabelProvider implements ITableLabelProvider {
		
		@Override
		public void addListener(ILabelProviderListener listener) {}

		@Override
		public void dispose() {}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			switch(columnIndex){
			case 0 : return ((AgentiVO)element).getNome();
			case 1 : return ((AgentiVO)element).getCognome();
			case 2 : return ((AgentiVO)element).getCitta();
			case 3 : return ((AgentiVO)element).getIndirizzo();
			default:return "";
			}
		}
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
	
	private void returnValue(AgentiVO returnObj){
		
		if ((callerObj != null) && (setterMethodName != null)){
		
				try {
					Method m = callerObj.getClass().getMethod(setterMethodName, AgentiVO.class);
					m.invoke(callerObj, returnObj);			
					popup.close();
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
