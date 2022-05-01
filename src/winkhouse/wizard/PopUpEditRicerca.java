package winkhouse.wizard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import winkhouse.Activator;
import winkhouse.helper.RicercheHelper;
import winkhouse.model.RicercheModel;



public class PopUpEditRicerca {

	private Shell popup = null;
	private Text nome = null;
	private Text descrizione = null;
	private RicercheModel ricerche = null;
	private Object callerObj = null;
	private String setterMethodName = null;

	public PopUpEditRicerca() {
		createContent();
	}
	
	public PopUpEditRicerca(Object callerObj, String methodName) {
		createContent();
		setSetterMethodName(methodName);
		setCallerObj(callerObj);
	}
	
	
	
	private void createContent(){
		popup = new Shell(Activator.getDefault().getWorkbench()
												.getActiveWorkbenchWindow()
												.getShell(),
				  		  SWT.APPLICATION_MODAL|SWT.SHELL_TRIM);
		popup.setText("Salvataggio ricerca");
		popup.setImage(Activator.getDefault()
							    .getImageDescriptor("icons/kfind.png")
							    .createImage());
		popup.setBackground(new Color(null,255,255,255));
		popup.setSize(300, 300);
				
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.FILL;
//		gd.horizontalSpan = 2;

		GridData gd1 = new GridData();
		gd1.grabExcessHorizontalSpace = true;
//		gd1.grabExcessVerticalSpace = true;
		gd1.horizontalAlignment = SWT.FILL;
		gd1.verticalAlignment = SWT.FILL;
		gd1.minimumHeight = 30;
		
		GridData gdFillH = new GridData();
		gdFillH.grabExcessHorizontalSpace = true;
		gdFillH.horizontalAlignment = SWT.FILL;
		
//		gdFillH.horizontalSpan = 2;

		GridData gdFill = new GridData();
		gdFill.grabExcessHorizontalSpace = true;
		gdFill.horizontalAlignment = SWT.RIGHT;

		popup.setLayout(new GridLayout());
		//popup.setLayoutData(gd);
			
		FormToolkit ft = new FormToolkit(popup.getDisplay());
		Form f = ft.createForm(popup);
		f.getBody().setLayout(new GridLayout());
		f.setLayoutData(gd);		
		
		Label lnome = ft.createLabel(f.getBody(), "Nome");
		lnome.setLayoutData(gdFillH);
		nome = ft.createText(f.getBody(), "");
		nome.setLayoutData(gd1);
		Label ldescrizione = ft.createLabel(f.getBody(), "Descrizione");
		ldescrizione.setLayoutData(gdFillH);
		descrizione = ft.createText(f.getBody(), "", SWT.MULTI);	
		descrizione.setLayoutData(gd);
		
		Composite cButtons = ft.createComposite(f.getBody());
		cButtons.setLayoutData(gdFill);
		cButtons.setLayout(new GridLayout(2,false));
		
		ImageHyperlink ihConferma = new ImageHyperlink(cButtons, SWT.WRAP);	
		//ihConferma.setLayoutData(gdFill);
		ihConferma.setImage(Activator.getImageDescriptor("/icons/adept_commit.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/adept_commit_hover.png").createImage());
		ihConferma.setBackground(new Color(null,255,255,255));
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (!ricerche.getNome().trim().equalsIgnoreCase("")){
					RicercheHelper rh = new RicercheHelper();
					if (!rh.saveUpdateRicerca(ricerche)){
						MessageDialog.openError(PlatformUI.getWorkbench()
														  .getActiveWorkbenchWindow()
														  .getShell(), 
												"Errore salvataggio ricerca", 
												"Si è verificato un errore nel salvataggio della ricerca");		
					}else{
						MessageDialog.openInformation(PlatformUI.getWorkbench()
		  						  						  		.getActiveWorkbenchWindow()
		  						  						  		.getShell(), 
		  						  					 "Salvataggio ricerca", 
													 "Salvataggio ricerca eseguito correttamente");
						returnValue(ricerche);
						popup.close();
						
					}
				}else{
					MessageDialog.openError(PlatformUI.getWorkbench()
							  						  .getActiveWorkbenchWindow()
							  						  .getShell(), 
							  				"Errore salvataggio ricerca", 
											"Inserire il nome della ricerca");							
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
		ihCancella.setLayoutData(gdFill);
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

		ft.paintBordersFor(f.getBody());
		
	}
	
	public void setRicerca(RicercheModel rm){
		this.ricerche = rm;
		DataBindingContext bindingContext = new DataBindingContext();
		bindRicerca(bindingContext);
		popup.open();
	}
	
	private void bindRicerca(DataBindingContext bindingContext){
		
		nome.setText(String.valueOf(ricerche.getNome()));
		
		bindingContext.bindValue(SWTObservables.observeText(
													nome,SWT.Modify
															), 
								 PojoObservables.observeValue(ricerche, "nome"),
				                 null, 
				                 null);

		descrizione.setText(String.valueOf(ricerche.getNome()));
		
		bindingContext.bindValue(SWTObservables.observeText(
													descrizione,SWT.Modify
															), 
								 PojoObservables.observeValue(ricerche, "descrizione"),
				                 null, 
				                 null);
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
