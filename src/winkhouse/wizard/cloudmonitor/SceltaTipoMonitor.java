package winkhouse.wizard.cloudmonitor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import winkhouse.model.winkcloud.ConnectorTypes;
import winkhouse.wizard.NewMonitorWizard;

public class SceltaTipoMonitor extends WizardPage {
	
	private Composite container = null;
	
	public SceltaTipoMonitor(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	public SceltaTipoMonitor(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		
		setTitle(getName());
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;

		GridLayout gl2 = new GridLayout();
		gl2.numColumns = 1;
				
		container = new Composite(parent,SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(gdFillHV);
		
		Button radioFTP = new Button(container,SWT.RADIO);
		radioFTP.setText("FTP");
		
		radioFTP.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				
			}

			@Override
			public void mouseUp(MouseEvent e) {								
				((NewMonitorWizard)getWizard()).setTipoMonitor(ConnectorTypes.FTP);
			}
			
		});
		
		Button gDRIVE = new Button(container,SWT.RADIO);
		gDRIVE.setText("WINKCLOUD");
		gDRIVE.addMouseListener(new MouseListener(){


			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				
			}

			@Override
			public void mouseUp(MouseEvent e) {								
				((NewMonitorWizard)getWizard()).setTipoMonitor(ConnectorTypes.WINKCLOUD);
			}
			
		});
		
		radioFTP.setSelection(true);
		
		setControl(container);

	}

}
