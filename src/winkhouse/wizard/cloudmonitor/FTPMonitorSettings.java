package winkhouse.wizard.cloudmonitor;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import winkhouse.Activator;
import winkhouse.wizard.NewMonitorWizard;

public class FTPMonitorSettings extends WizardPage {

	private Composite container = null;
		
	private Text txt_indirizzo = null;
	private Text txt_porta = null;
	private Text txt_username = null;
	private Text txt_password = null;
	private Text txt_polling = null;
	private Text txt_percorso = null;
	private Button chk_winkhouse = null;
	
	private Image imgtest = Activator.getImageDescriptor("icons/wizardmonitor/testconnessione.png").createImage();
	
	public FTPMonitorSettings(String pageName) {
		super(pageName);
	}

	public FTPMonitorSettings(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite parent) {
		
		setTitle(getName());
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;

		GridData gdFillH = new GridData();
		gdFillH.grabExcessHorizontalSpace = true;
		gdFillH.horizontalAlignment = SWT.FILL;
		
		
		GridLayout gl2 = new GridLayout();
		gl2.numColumns = 2;
				
		container = new Composite(parent,SWT.NONE);
		container.setLayout(gl2);
		container.setLayoutData(gdFillHV);
		
		Label lblTipo = new Label(container, SWT.FLAT);
		lblTipo.setText("Ricerca Winkhouse");
		chk_winkhouse = new Button(container, SWT.CHECK);
		chk_winkhouse.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				((NewMonitorWizard)getWizard()).getNewMonitor().setWink(((Button)e.getSource()).getSelection());
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Label lblIndirizzo = new Label(container, SWT.FLAT);
		lblIndirizzo.setText("Indirizzo : ");
		txt_indirizzo = new Text(container, SWT.BORDER);
		txt_indirizzo.setLayoutData(gdFillH);
		
		Label lblporta = new Label(container, SWT.FLAT);
		lblporta.setText("Porta : ");
		txt_porta = new Text(container, SWT.BORDER);
		txt_porta.setText("21");
		
		Label lblpercorso = new Label(container, SWT.FLAT);
		lblpercorso.setText("Percorso : ");
		txt_percorso = new Text(container, SWT.BORDER);			
		txt_percorso.setLayoutData(gdFillH);
		
		Label lblusername = new Label(container, SWT.FLAT);
		lblusername.setText("Username : ");
		txt_username = new Text(container, SWT.BORDER);		
		txt_username.setLayoutData(gdFillH);
		
		Label lblpassword = new Label(container, SWT.FLAT);
		lblpassword.setText("Password : ");
		txt_password = new Text(container, SWT.BORDER|SWT.PASSWORD);
		txt_password.setLayoutData(gdFillH);

		Label lblpolling = new Label(container, SWT.FLAT);
		lblpolling.setText("Intervallo ricerca (sec.) : ");
		txt_polling = new Text(container, SWT.BORDER);
		txt_polling.setLayoutData(gdFillH);
		
		txt_polling.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent e) {
				
				try {
					Long.valueOf(e.text);
				} catch (Exception e1) {
					e.doit = false;
				}
				
				
			}
		});
		

		Button test = new Button(container, SWT.FLAT);
		test.setImage(imgtest);
		test.setToolTipText("Prova ad eseguire la connessione con i parametri inseriti");
		GridData x = new GridData();
		x.horizontalSpan = 2;
		x.horizontalAlignment = SWT.CENTER;
		x.grabExcessHorizontalSpace = true;
		test.setLayoutData(x);
		test.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				if (((NewMonitorWizard)getWizard()).getNewMonitor().testConnection()){
					MessageDialog.openInformation(getShell(), "Prova connessione", "Connessione stabilita con successo");
				}else{
					MessageDialog.openError(getShell(), "Prova connessione", "Impossibile stabilire una connessione");
				}
				((NewMonitorWizard)getWizard()).getContainer().updateButtons();
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
		DataBindingContext bindingContext = new DataBindingContext();
		bindConnector(bindingContext);
		txt_polling.setText("30");
		setControl(container);

	}
	
	private void bindConnector(DataBindingContext bindingContext){
		
		
			
			bindingContext.bindValue(SWTObservables.observeText(
									 txt_indirizzo,SWT.Modify), 
									 PojoObservables.observeValue(((NewMonitorWizard)getWizard()).getNewMonitor().getConnector(), "url"),
									 null, 
									 null);
	
			bindingContext.bindValue(SWTObservables.observeText(
					 txt_porta,SWT.Modify), 
					 PojoObservables.observeValue(((NewMonitorWizard)getWizard()).getNewMonitor().getConnector(), "porta"),
					 null, 
					 null);

			bindingContext.bindValue(SWTObservables.observeText(
					 txt_percorso,SWT.Modify), 
					 PojoObservables.observeValue(((NewMonitorWizard)getWizard()).getNewMonitor().getConnector(), "percorso"),
					 null, 
					 null);
			
			bindingContext.bindValue(SWTObservables.observeText(
					 txt_username,SWT.Modify), 
					 PojoObservables.observeValue(((NewMonitorWizard)getWizard()).getNewMonitor().getConnector(), "username"),
					 null, 
					 null);

			bindingContext.bindValue(SWTObservables.observeText(
					 txt_password,SWT.Modify), 
					 PojoObservables.observeValue(((NewMonitorWizard)getWizard()).getNewMonitor().getConnector(), "password"),
					 null, 
					 null);

			bindingContext.bindValue(SWTObservables.observeText(
					 txt_polling,SWT.Modify), 
					 PojoObservables.observeValue(((NewMonitorWizard)getWizard()).getNewMonitor(), "pollingIntervall"),
					 null, 
					 null);
			

	}
	
}
