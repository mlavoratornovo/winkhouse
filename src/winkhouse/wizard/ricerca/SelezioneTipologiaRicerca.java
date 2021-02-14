package winkhouse.wizard.ricerca;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import winkhouse.wizard.RicercaWizard;


public class SelezioneTipologiaRicerca extends WizardPage {

	private Composite container = null;
	
	public SelezioneTipologiaRicerca(String pageName) {
		super(pageName);

	}

	public SelezioneTipologiaRicerca(String pageName, String title,
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

		GridLayout gl2 = new GridLayout();
		gl2.numColumns = 1;
				
		container = new Composite(parent,SWT.NONE);
		container.setLayout(new GridLayout());
		container.setLayoutData(gdFillHV);
		
		Button radioRicerca = new Button(container,SWT.RADIO);
		radioRicerca.setText("Ricerca immobili");
		radioRicerca.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				
			}

			@Override
			public void mouseUp(MouseEvent e) {								
				((RicercaWizard)getWizard()).getRicerca()
											.setType(RicercaWizard.IMMOBILI);
			}
			
		});
		
		Button radioVisita = new Button(container,SWT.RADIO);
		radioVisita.setText("Ricerca anagrafiche");
		radioVisita.setVisible(!(((RicercaWizard)getWizard()).getWiztype() == RicercaWizard.RICERCACLOUD));
		radioVisita.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				
			}

			@Override
			public void mouseUp(MouseEvent e) {								
				((RicercaWizard)getWizard()).getRicerca()
											.setType(RicercaWizard.ANAGRAFICHE);
			}
			
		});

		Button radioAffitti = new Button(container,SWT.RADIO);
		radioAffitti.setText("Ricerca immobili affitti");
		radioAffitti.setVisible(!(((RicercaWizard)getWizard()).getWiztype() == RicercaWizard.RICERCACLOUD));
		radioAffitti.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				
			}

			@Override
			public void mouseUp(MouseEvent e) {								
				((RicercaWizard)getWizard()).getRicerca()
											.setType(RicercaWizard.AFFITTI);
			}
			
		});
		Button radioColloqui = new Button(container,SWT.RADIO);
		radioColloqui.setText("Ricerca colloqui");
		radioColloqui.setVisible(!(((RicercaWizard)getWizard()).getWiztype() == RicercaWizard.RICERCACLOUD));
		radioColloqui.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				
			}

			@Override
			public void mouseUp(MouseEvent e) {								
				((RicercaWizard)getWizard()).getRicerca()
											.setType(RicercaWizard.COLLOQUI);
			}
			
		});
		
		((RicercaWizard)getWizard()).getRicerca()
									.setType(RicercaWizard.IMMOBILI);		
		
		setControl(container);

	}

}
