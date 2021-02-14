package winkhouse.xmlser.wizard.exporter;

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

import winkhouse.export.helpers.ExportedRicercheHelper;
import winkhouse.model.RicercheModel;
import winkhouse.Activator;



public class PopUpEditRicerca {

	private Shell popup = null;
	private Text nome = null;
	private Text descrizione = null;
	private RicercheModel ricerche = null;
	
	public PopUpEditRicerca() {
		createContent();
	}
	
	private void createContent(){
		
		popup = new Shell(Activator.getDefault().getWorkbench()
												.getActiveWorkbenchWindow()
												.getShell(),
												
				  		  SWT.APPLICATION_MODAL|SWT.SHELL_TRIM);
		popup.setText("Salvataggio ricerca");
		popup.setImage(Activator.getDefault()
							    .getImageDescriptor("icons/wizardexport/kfind.png")
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
		gd1.grabExcessVerticalSpace = true;
		gd1.horizontalAlignment = SWT.FILL;
		gd1.verticalAlignment = SWT.FILL;
		
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
		f.setLayoutData(gd1);		
		
		Label lnome = ft.createLabel(f.getBody(), "Nome");
		lnome.setLayoutData(gdFillH);
		nome = ft.createText(f.getBody(), "");
		nome.setLayoutData(gdFillH);
		Label ldescrizione = ft.createLabel(f.getBody(), "Descrizione");
		ldescrizione.setLayoutData(gdFillH);
		descrizione = ft.createText(f.getBody(), "", SWT.MULTI);	
		descrizione.setLayoutData(gd);
		
		Composite cButtons = ft.createComposite(f.getBody());
		cButtons.setLayoutData(gdFill);
		cButtons.setLayout(new GridLayout(2,false));
		
		ImageHyperlink ihConferma = new ImageHyperlink(cButtons, SWT.WRAP);	
		//ihConferma.setLayoutData(gdFill);
		ihConferma.setImage(Activator.getImageDescriptor("/icons/wizardexport/adept_commit.png").createImage());
		ihConferma.setHoverImage(Activator.getImageDescriptor("/icons/wizardexport/adept_commit_over.png").createImage());
		ihConferma.setBackground(new Color(null,255,255,255));
		ihConferma.addMouseListener(new MouseListener(){

			@Override
			public void mouseUp(MouseEvent e) {
				if (!ricerche.getNome().trim().equalsIgnoreCase("")){
					ExportedRicercheHelper rh = new ExportedRicercheHelper();
					if (!rh.saveRicerca(ricerche)){
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

}
