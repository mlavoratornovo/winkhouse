package winkhouse.xmlser.wizard.exporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.export.helpers.ExportedRicercheHelper;
import winkhouse.model.RicercheModel;
import winkhouse.Activator;



public class PopUpRicercaRicerche {

	private Shell popup = null;
	private Text tNome = null;
	private Button filtraRicerche = null;
	private ViewerFilter filtroRicerche = null;
	private TableViewer viewer = null;
	private Object callerObj = null;
	private String setterMethodName = null;
	private Integer idTipoRicerca = null;
	private ExportedRicercheHelper ricercheHelper = null;
	
	public PopUpRicercaRicerche(Integer idTipoRicerca,Object callerObj, String methodName) {
		super();		
		this.idTipoRicerca = idTipoRicerca;
		ricercheHelper = new ExportedRicercheHelper();
		setCallerObj(callerObj);
		setSetterMethodName(methodName);
		createContent();
	}
	
	private void createContent(){
		popup = new Shell(Activator.getDefault().getWorkbench()
												.getActiveWorkbenchWindow()
												.getShell(),
				  		  SWT.APPLICATION_MODAL|SWT.SHELL_TRIM);
		popup.setText("Seleziona ricerca");
		popup.setImage(Activator.getDefault()
							    .getImageDescriptor("icons/wizardexport/ricercabig.png")
							    .createImage());
		popup.setBackground(new Color(null,255,255,255));
		popup.setSize(500, 300);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalSpan = 2;

		GridData gd1 = new GridData();
		gd1.grabExcessHorizontalSpace = true;
		gd1.grabExcessVerticalSpace = true;
		gd1.horizontalAlignment = SWT.FILL;
		gd1.verticalAlignment = SWT.FILL;

		popup.setLayout(gl);
				
		GridData gdFillH = new GridData();
		gdFillH.grabExcessHorizontalSpace = true;
		gdFillH.horizontalAlignment = SWT.FILL;
		
		FormToolkit ft = new FormToolkit(popup.getDisplay());
		Form f = ft.createForm(popup);
		f.getBody().setLayout(new GridLayout());
		f.setLayoutData(gd1);
		
		Composite cRicerca = ft.createComposite(f.getBody());	
		cRicerca.setBackground(new Color(null,255,255,255));
		GridLayout gl2 = new GridLayout();
		gl2.numColumns = 2;
		cRicerca.setLayout(gl2);
		cRicerca.setLayoutData(gdFillH);
		
		Label lNome = ft.createLabel(cRicerca,"Nome");				

		Label dummy = ft.createLabel(cRicerca,"");				
		
		tNome = ft.createText(cRicerca,"");
		tNome.setLayoutData(gdFillH);
		tNome.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (filtraRicerche.getSelection()){
					viewer.refresh();
				}				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});

		filtraRicerche = ft.createButton(cRicerca,"",SWT.TOGGLE|SWT.FLAT);
//		filtraRicerche.setBackground(new Color(null,255,255,255));
		filtraRicerche.setImage(Activator.getImageDescriptor("/icons/wizardexport/ricercabig.png").createImage());
		filtraRicerche.setToolTipText("filtra");
		filtraRicerche.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(((Button)e.widget).getSelection()){
					viewer.addFilter(filtroRicerche);
					viewer.refresh();
				}else{
					viewer.removeFilter(filtroRicerche);
					viewer.refresh();
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		filtroRicerche = new ViewerFilter() {
			
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
								
				String nome = (((RicercheModel)element).getNome() == null) 
				 			  ? "" 
				 			  : ((RicercheModel)element).getNome().toLowerCase();

				
				if (nome.startsWith(tNome.getText().toLowerCase())){
					return true;
				}else{
					return false;
				}
			}
		};
		
		ft.paintBordersFor(cRicerca);
		
		Composite c = ft.createComposite(f.getBody(),SWT.NONE);
		c.setBackground(new Color(null,255,255,255));
		c.setLayout(new GridLayout());
		
		viewer = new TableViewer(c, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		
		TableColumn tcNome = new TableColumn(viewer.getTable(),SWT.CENTER,0);
		tcNome.setText("Nome");
		tcNome.setWidth(150);

		TableColumn tcCognome = new TableColumn(viewer.getTable(),SWT.CENTER,1);
		tcCognome.setText("Descrizione");
		tcCognome.setWidth(300);

		viewer.getTable().setLayoutData(gd);
		viewer.setInput(new Object());
		
		Composite cButtons = ft.createComposite(f.getBody());
		GridLayout glbuttons = new GridLayout(2,false);
		cButtons.setLayout(glbuttons);
		
		GridData gdRight = new GridData();
		gdRight.horizontalAlignment = SWT.RIGHT;
		cButtons.setLayoutData(gdRight);
		
		ImageHyperlink ihConferma = new ImageHyperlink(cButtons, SWT.WRAP);		
		ihConferma.setImage(Activator.getImageDescriptor("/icons/wizardexport/adept_commit.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/adept_commit_over.png").createImage());
		ihConferma.setBackground(new Color(null,255,255,255));
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if ((((StructuredSelection)viewer.getSelection()).getFirstElement() != null) && 
					(((StructuredSelection)viewer.getSelection()).getFirstElement() instanceof RicercheModel)){
					RicercheModel ricerca = (RicercheModel)((StructuredSelection)viewer.getSelection()).getFirstElement();					
					returnValue(ricerca);
				}else{
					MessageBox mb = new MessageBox(popup,SWT.ERROR);
					mb.setText("Errore selezione");
					mb.setMessage("Selezionare una ricerca");			
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

		ImageHyperlink ihCancella = new ImageHyperlink(cButtons, SWT.WRAP);		
		ihCancella.setImage(Activator.getImageDescriptor("/icons/wizardexport/button_cancel.png").createImage());
		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/button_cancel_over.png").createImage());
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
		ft.paintBordersFor(c);
		popup.open();		
		
	}
	
	class ViewContentProvider implements IStructuredContentProvider{
		
		@Override
		public Object[] getElements(Object inputElement) {
			return ricercheHelper.getRicercheByType(idTipoRicerca).toArray();
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	class ViewLabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			String retunValue = null;
			switch(columnIndex){
				case 0: retunValue = ((RicercheModel)element).getNome();
						break;
				case 1: retunValue = ((RicercheModel)element).getDescrizione();
						break;
			}
			return retunValue;
		}

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
		
	}

	public Object getCallerObj() {
		return callerObj;
	}

	private void setCallerObj(Object callerObj) {
		this.callerObj = callerObj;
	}

	public String getSetterMethodName() {
		return setterMethodName;
	}

	private void setSetterMethodName(String setterMethodName) {
		this.setterMethodName = setterMethodName;
	}
	
	private void returnValue(RicercheModel returnObj){
		
		if ((callerObj != null) && (setterMethodName != null)){
		
				try {
					Method m = callerObj.getClass().getMethod(setterMethodName, RicercheModel.class);
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