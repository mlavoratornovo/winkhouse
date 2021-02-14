package winkhouse.wizard.cloudmonitor;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
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
import winkhouse.model.winkcloud.restmsgs.MessageCode;
import winkhouse.util.WinkhouseUtils;
import winkhouse.wizard.NewMonitorWizard;

public class WinkCloudMonitorSettings extends WizardPage {
	
	private Composite container = null;
	private ComboViewer cvurlservizio = null;
	private Image imgTest = Activator.getImageDescriptor("icons/adept_reinstall.png").createImage();
	private Text txt_codice = null;
	private Text txt_polling = null;
	
	public WinkCloudMonitorSettings(String pageName) {
		super(pageName);
	}

	public WinkCloudMonitorSettings(String pageName, String title,
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
		gdFillH.grabExcessVerticalSpace = false;
		gdFillH.horizontalAlignment = SWT.FILL;
		
		GridData gdFillH2 = new GridData();
		gdFillH2.grabExcessHorizontalSpace = true;
		gdFillH2.grabExcessVerticalSpace = false;
		gdFillH2.horizontalAlignment = SWT.FILL;
		gdFillH2.horizontalSpan = 2;

		GridData gdFillH20 = new GridData();
		gdFillH20.grabExcessHorizontalSpace = true;
		gdFillH20.grabExcessVerticalSpace = false;
		gdFillH20.horizontalAlignment = SWT.FILL;
		gdFillH20.heightHint = 20;

		
		GridLayout gl2 = new GridLayout();
		gl2.numColumns = 3;
				
		container = new Composite(parent,SWT.NONE);
		container.setLayout(gl2);
		container.setLayoutData(gdFillHV);
		
		Label lbl_urlServizio = new Label(container, SWT.FLAT);
		lbl_urlServizio.setText("Url servizio : ");
		
		cvurlservizio = new ComboViewer(container,SWT.DOUBLE_BUFFERED);
		cvurlservizio.getCombo().setLayoutData(gdFillH);
		cvurlservizio.setContentProvider(new IStructuredContentProvider(){
			
			@Override
			public Object[] getElements(Object inputElement) {
				
				String urls = WinkhouseUtils.getInstance().getPreferenceStore().getString("winkcloudurls");
			    String[] values = urls.split(";");
			    
			    return  values;
			    
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,Object newInput) {
			}
			
		});
		
		cvurlservizio.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				return element.toString();
			}
			
		});
		
		cvurlservizio.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				((NewMonitorWizard)getWizard()).getMonitorHTTP()
											   .getConnector()
											   .setUrl((String)((StructuredSelection)event.getSelection()).getFirstElement());
				((NewMonitorWizard)getWizard()).getContainer().updateButtons();
			}
			
		});
						
		cvurlservizio.setInput(new Object());
		
		Button btest = new Button(container, SWT.FLAT);
		btest.setImage(imgTest);
		btest.setToolTipText("Testa connessione al servizio");
		btest.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				if (((NewMonitorWizard)getWizard()).getMonitorHTTP().checkStatus()){					
					((NewMonitorWizard)getWizard()).getContainer().updateButtons();
					MessageDialog.openInformation(container.getShell(), "Connessione al servizio", "Url valido per il servizio winkcloud");
				}else{					
					((NewMonitorWizard)getWizard()).getMonitorHTTP().getConnector().setUrl(null);
					MessageDialog.openError(container.getShell(), "Connessione al servizio", "Url non valido per il servizio winkcloud");
				}
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}
		});
		
		Label lbl_codice = new Label(container, SWT.FLAT);
		lbl_codice.setText("Codice ");
		
		txt_codice = new Text(container, SWT.NONE);
		txt_codice.setLayoutData(gdFillH);
		txt_codice.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				((NewMonitorWizard)getWizard()).getContainer().updateButtons();
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button bottienicodice = new Button(container, SWT.FLAT);
		bottienicodice.setImage(imgTest);
		bottienicodice.setToolTipText("Ottieni codice winkcloud");
		bottienicodice.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				
				String code = ((NewMonitorWizard)getWizard()).getMonitorHTTP().getCode();
				
				if (code != null){
					txt_codice.setText(code);
					((NewMonitorWizard)getWizard()).getMonitorHTTP().getConnector().setCode(code);
					((NewMonitorWizard)getWizard()).getContainer().updateButtons();
				}else{
					MessageDialog.openError(container.getShell(), "Acquisizione codice winkcloud", "Impossibile reperire il codice identificativo per operare con winkcloud");
				}
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}
		});
		
		Label lblpolling = new Label(container, SWT.FLAT);
		lblpolling.setText("Intervallo ricerca (sec.) : ");
		txt_polling = new Text(container, SWT.BORDER);
		txt_polling.setLayoutData(gdFillH2);
		
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
		
		DataBindingContext bindingContext = new DataBindingContext();
		bindConnector(bindingContext);
		txt_polling.setText("30");
		
		setControl(container);

	}

	private void bindConnector(DataBindingContext bindingContext){
		
		bindingContext.bindValue(SWTObservables.observeText(
				 				 txt_polling,SWT.Modify), 
				 				 PojoObservables.observeValue(((NewMonitorWizard)getWizard()).getMonitorHTTP(), "pollingIntervall"),
				 				 null, 
				 				 null);
		
		bindingContext.bindValue(SWTObservables.observeText(
								 txt_codice,SWT.Modify), 
				 				 PojoObservables.observeValue(((NewMonitorWizard)getWizard()).getMonitorHTTP().getConnector(), "code"),
				 				 null, 
				 				 null);
		
	}

}
