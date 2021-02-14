package winkhouse.view.anagrafica;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.Activator;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.WinkSysDAO;
import winkhouse.helper.AnagraficheHelper;
import winkhouse.helper.ProfilerHelper;
import winkhouse.model.AnagraficheModel;
import winkhouse.util.IWinkSysProperties;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.AnagraficheVO;



public class PopUpRicercaAnagrafica extends Dialog{
	
	public final static String ID = "winkhouse.seleziona_anagrafica";
	
	private TableViewer viewer = null;
	private AnagraficheDAO anagraficheDAO = new AnagraficheDAO();
	private WinkSysDAO winkSysDAO = new WinkSysDAO();
	//private Shell popup = null;
	private Object callerObj = null;
	private String setterMethodName = null;
	private ViewerFilter filtroAnagrafiche = null;
	private Text tCognome = null;
	private Text tNome = null;
	private Text tIndirizzo = null;
	private Button filtraAnagrafiche = null;
	
	public PopUpRicercaAnagrafica(Shell shell) {
		super(shell);		
	}
	

	@Override
	protected void setShellStyle(int newShellStyle) {
		// TODO Auto-generated method stub
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
		    (((StructuredSelection)viewer.getSelection()).getFirstElement() instanceof AnagraficheVO)){
			AnagraficheVO aVO = (AnagraficheVO)((StructuredSelection)viewer.getSelection()).getFirstElement();
			AnagraficheModel aModel = new AnagraficheModel(aVO);
			returnValue(aModel);
			close();
		}else{
			MessageBox mb = new MessageBox(getShell(),SWT.ERROR);
			mb.setText("Errore selezione");
			mb.setMessage("Selezionare una anagrafica");			
			mb.open();
		}

	}


	private void createContent(Composite c){
		
//		popup = new Shell(shell,
//						  SWT.APPLICATION_MODAL|SWT.SHELL_TRIM);
//		popup.setText("Seleziona anagrafica");
//		popup.setImage(Activator.getDefault()
//								.getImageDescriptor("icons/classianagrafiche.png").createImage());
//		popup.setBackground(new Color(null,255,255,255));
//		popup.setSize(300, 300);
//		GridLayout gl = new GridLayout();
//		gl.numColumns = 2;
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalSpan = 2;
		
		GridData savegd = new GridData();
		savegd.horizontalSpan = 4;
		savegd.horizontalAlignment = SWT.LEFT;		
		
		c.setLayout(new GridLayout());
		c.setLayoutData(gd);
		
		GridData gdFillH = new GridData();
		gdFillH.grabExcessHorizontalSpace = true;
		gdFillH.horizontalAlignment = SWT.FILL;

		FormToolkit ft = new FormToolkit(getShell().getDisplay());
		
		Composite cRicerca = ft.createComposite(c);	
		
		Button salvaAnagrafica = ft.createButton(cRicerca, "", SWT.FLAT);
		salvaAnagrafica.setLayoutData(savegd);
		salvaAnagrafica.setImage(Activator.getDefault()
										  .getImageDescriptor("icons/document-save.png").createImage());
		salvaAnagrafica.setToolTipText("Crea una nuova anagrafica con i dati inseriti nelle caselle di testo sottostanti");
		salvaAnagrafica.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				
				AnagraficheModel aModel = new AnagraficheModel();
				
				aModel.setNome(tNome.getText());
				aModel.setCognome(tCognome.getText());
				aModel.setRagioneSociale(tIndirizzo.getText());
				
				if (
						(((aModel.getCognome() != null) && (!aModel.getCognome().equalsIgnoreCase(""))) && 
						 ((aModel.getNome() != null) && (!aModel.getNome().equalsIgnoreCase("")))) ||
						 ((aModel.getRagioneSociale() != null) && (!aModel.getRagioneSociale().equalsIgnoreCase("")))
					){
					AnagraficheHelper ah = new AnagraficheHelper();
					if (ah.updateAnagrafiche(aModel, false, null, true) == true){
						MessageDialog.openInformation(getShell(), "Salvataggio anagrafica", "Anagrafica inserita con successo");
						viewer.setInput(new Object());
					}else{
						MessageDialog.openError(getShell(), "Salvataggio anagrafica", "Impossibile salvare l'anagrafica");
					}
				}else{
					MessageDialog.openError(getShell(), "Errore salvataggio anagrafica", "Inserire cognome e nome o ragione sociale dell'anagrafica");
				}
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {}
		});
		
		GridLayout gl4 = new GridLayout();
		gl4.numColumns = 4;
		cRicerca.setLayout(gl4);
		cRicerca.setLayoutData(gdFillH);
		
		Label lCognome = ft.createLabel(cRicerca, "Cognome");
		Label lNome = ft.createLabel(cRicerca,"Nome");
		Label lIndirizzo = ft.createLabel(cRicerca,"Ragione sociale");
		Label dummy = ft.createLabel(cRicerca,"");			
		
		tCognome = ft.createText(cRicerca,"",SWT.DOUBLE_BUFFERED);
		ft.paintBordersFor(cRicerca);
		tCognome.setLayoutData(gdFillH);
		tCognome.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (filtraAnagrafiche.getSelection()){
					viewer.refresh();
				}				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		tNome = ft.createText(cRicerca,"",SWT.DOUBLE_BUFFERED);
		tNome.setLayoutData(gdFillH);
		tNome.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (filtraAnagrafiche.getSelection()){
					viewer.refresh();
				}				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		tIndirizzo = ft.createText(cRicerca,"",SWT.DOUBLE_BUFFERED);
		tIndirizzo.setLayoutData(gdFillH);
		tIndirizzo.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (filtraAnagrafiche.getSelection()){
					viewer.refresh();
				}				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		filtraAnagrafiche = ft.createButton(cRicerca, "", SWT.TOGGLE|SWT.FLAT);
		filtraAnagrafiche.setImage(Activator.getImageDescriptor("/icons/kfind.png").createImage());
		filtraAnagrafiche.setToolTipText("filtra");
		filtraAnagrafiche.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(((Button)e.widget).getSelection()){
					((Button)e.widget).setBackground(new Color(null,255,255,255));
					viewer.addFilter(filtroAnagrafiche);
					viewer.refresh();
				}else{
					((Button)e.widget).setBackground(null);
					viewer.removeFilter(filtroAnagrafiche);
					viewer.refresh();
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		/*
		ImageHyperlink ihRicerca = new ImageHyperlink(cRicerca, SWT.WRAP);
		ihRicerca.setToolTipText("filtra");
		ihRicerca.setImage(Activator.getImageDescriptor("/icons/filenew.png").createImage());
		ihRicerca.setHoverImage(Activator.getImageDescriptor("/icons/filenewhover.png").createImage());
		*/
		filtroAnagrafiche = new ViewerFilter() {
			
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				
				String cognome = (((AnagraficheModel)element).getCognome() == null) 
								 ? "" 
								 : ((AnagraficheModel)element).getCognome().toLowerCase();
				
				String nome = (((AnagraficheModel)element).getNome() == null) 
				 			  ? "" 
				 			  : ((AnagraficheModel)element).getNome().toLowerCase();

				String indirizzo = (((AnagraficheModel)element).getRagioneSociale() == null) 
				 				   ? "" 
				 				   : ((AnagraficheModel)element).getRagioneSociale().toLowerCase();
				
				if (
						cognome.startsWith(tCognome.getText().toLowerCase()) && 
						nome.startsWith(tNome.getText().toLowerCase()) &&
						indirizzo.startsWith(tIndirizzo.getText().toLowerCase())
					){
					return true;
				}else{
					return false;
				}
			}
		};
		
		
		Composite c_int = new Composite(c,SWT.NONE);
		c_int.setBackground(new Color(null,255,255,255));
		c_int.setLayout(new GridLayout());
		c_int.setLayoutData(gd);
		
		viewer = new TableViewer(c_int, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		
		TableColumn tcNome = new TableColumn(viewer.getTable(),SWT.CENTER,0);
		tcNome.setText("Cognome");
		tcNome.setWidth(100);

		TableColumn tcCognome = new TableColumn(viewer.getTable(),SWT.CENTER,1);
		tcCognome.setText("Nome");
		tcCognome.setWidth(100);
		
		TableColumn tcCitta = new TableColumn(viewer.getTable(),SWT.CENTER,2);
		tcCitta.setText("Ragione sociale");
		tcCitta.setWidth(100);
				
		viewer.getTable().setLayoutData(gd);
		viewer.setInput(new Object());
		
//		ImageHyperlink ihConferma = new ImageHyperlink(c_int, SWT.WRAP);		
//		ihConferma.setImage(Activator.getImageDescriptor("/icons/adept_commit.png").createImage());
//		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/adept_commit_hover.png").createImage());
//		ihConferma.setBackground(new Color(null,255,255,255));
//		ihConferma.addMouseListener(new MouseListener(){
//
//			@Override
//			public void mouseUp(MouseEvent e) {
//				if ((((StructuredSelection)viewer.getSelection()).getFirstElement() != null) && 
//					(((StructuredSelection)viewer.getSelection()).getFirstElement() instanceof AnagraficheVO)){
//					AnagraficheVO aVO = (AnagraficheVO)((StructuredSelection)viewer.getSelection()).getFirstElement();
//					AnagraficheModel aModel = new AnagraficheModel(aVO);
//					returnValue(aModel);
//			
//				}else{
//					MessageBox mb = new MessageBox(getShell(),SWT.ERROR);
//					mb.setText("Errore selezione");
//					mb.setMessage("Selezionare una anagrafica");			
//					mb.open();
//				}				
//			}
//
//			@Override
//			public void mouseDoubleClick(MouseEvent e) {
//			}
//
//			@Override
//			public void mouseDown(MouseEvent e) {
//			}
//			
//		});
//
//		ImageHyperlink ihCancella = new ImageHyperlink(c_int, SWT.WRAP);		
//		ihCancella.setImage(Activator.getImageDescriptor("/icons/button_cancel.png").createImage());
//		ihCancella.setHoverImage(Activator.getImageDescriptor("/icons/button_cancel_hover.png").createImage());
//		ihCancella.setBackground(new Color(null,255,255,255));
//		ihCancella.addMouseListener(new MouseListener(){
//
//			@Override
//			public void mouseUp(MouseEvent e) {
//				getShell().close();				
//			}
//
//			@Override
//			public void mouseDoubleClick(MouseEvent e) {
//			}
//
//			@Override
//			public void mouseDown(MouseEvent e) {
//			}
//			
//		});
		
		
		ft.paintBordersFor(cRicerca);
		//popup.pack();
		//popup.open();
	}
	
	class ViewContentProvider implements IStructuredContentProvider{
		
		
		
		@Override
		public Object[] getElements(Object inputElement) {
			ArrayList al = anagraficheDAO.list(AnagraficheModel.class.getName());
			if (WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN) != null &&
				Boolean.valueOf(WinkhouseUtils.getInstance().getHm_winkSys().get(IWinkSysProperties.LOGIN))){	
				ProfilerHelper.getInstance().filterAnagrafiche(al, false);
			}
			return al.toArray();
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
				case 1: retunValue = ((AnagraficheModel)element).getNome();
						break;
				case 0: retunValue = ((AnagraficheModel)element).getCognome();
						break;
				case 2: retunValue = ((AnagraficheModel)element).getRagioneSociale();
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

	@Override
	protected Control createDialogArea(Composite parent) {
		
		Composite c = (Composite)super.createDialogArea(parent);
		c.getShell().setText("Ricerca Creazione Selezione anagrafiche");
		createContent(c);
		
		return c;
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
	
	private void returnValue(AnagraficheModel returnObj){
		
		if ((callerObj != null) && (setterMethodName != null)){
		
				try {
					Method m = callerObj.getClass().getMethod(setterMethodName, AnagraficheModel.class);
					m.invoke(callerObj, returnObj);			
					//popup.close();
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
