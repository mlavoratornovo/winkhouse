package winkhouse.xmlser.wizard.exporter.pages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import winkhouse.xmlser.wizard.exporter.ExporterWizard;

public class SelectTypeExport extends WizardPage {

	public SelectTypeExport(String pageName) {
		super(pageName);
		// TODO Auto-generated constructor stub
	}

	public SelectTypeExport(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);

	}

	@Override
	public void createControl(Composite parent) {
		
		setTitle(((ExporterWizard)getWizard()).getVersion());
		
		GridLayout gl = new GridLayout();
		
		gl.marginTop = 30;
		gl.marginLeft = 30;
		gl.verticalSpacing = 20;
		
		GridData gdFillHV = new GridData();
		gdFillHV.grabExcessHorizontalSpace = true;
		gdFillHV.grabExcessVerticalSpace = true;
		gdFillHV.verticalAlignment = SWT.FILL;
		gdFillHV.horizontalAlignment = SWT.FILL;
		
		Composite container = new Composite(parent,SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(gdFillHV);

		/*
		parent.setLayout(gl);
		*/
		Button radio_immobili = new Button(container, SWT.RADIO);
		radio_immobili.setText("Immobili");
		radio_immobili.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				((ExporterWizard)getWizard()).getExporterVO()
											 .setType(ExporterWizard.IMMOBILI);
				
				((ExporterWizard)getWizard()).getContainer().updateButtons();
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
		Button radio_anagrafiche = new Button(container, SWT.RADIO);
		radio_anagrafiche.setText("Anagrafiche");
		radio_anagrafiche.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				((ExporterWizard)getWizard()).getExporterVO()
											 .setType(ExporterWizard.ANAGRAFICHE);
				
				((ExporterWizard)getWizard()).getContainer().updateButtons();
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
/*
		Button radio_colloqui = new Button(container, SWT.RADIO);
		radio_colloqui.setText("Colloqui");
		radio_colloqui.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				((ExporterWizard)getWizard()).getExporterVO()
											 .setType(ExporterWizard.COLLOQUI);
				
				((ExporterWizard)getWizard()).getContainer().updateButtons();
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});

		Button radio_appuntamenti = new Button(container, SWT.RADIO);
		radio_appuntamenti.setText("Appuntamenti");
		radio_appuntamenti.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				((ExporterWizard)getWizard()).getExporterVO()
											 .setType(ExporterWizard.APPUNTAMENTI);
				
				((ExporterWizard)getWizard()).getContainer().updateButtons();
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
*/
		Button radio_datibase = new Button(container, SWT.RADIO);
		radio_datibase.setText("Dati di base");
		radio_datibase.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				((ExporterWizard)getWizard()).getExporterVO()
											 .setType(ExporterWizard.DATI_BASE);
				
				((ExporterWizard)getWizard()).getContainer().updateButtons();
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		
		setControl(container);

	}

}
