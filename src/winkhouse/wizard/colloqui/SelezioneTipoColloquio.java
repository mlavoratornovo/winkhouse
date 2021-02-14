package winkhouse.wizard.colloqui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import winkhouse.configuration.EnvSettingsFactory;
import winkhouse.wizard.ColloquiWizard;


public class SelezioneTipoColloquio extends WizardPage {

	private Composite container = null;
	
	public SelezioneTipoColloquio(String pageName) {
		super(pageName);
	}

	public SelezioneTipoColloquio(String pageName, String title,
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
		
		Label ricercaDescrizione = new Label(container,SWT.NONE);
		ricercaDescrizione.setText("Colloquio con soggetto in ricerca di immobile");

		Button radioRicerca = new Button(container,SWT.RADIO);
		radioRicerca.setText("Ricerca");
		radioRicerca.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				
			}

			@Override
			public void mouseUp(MouseEvent e) {								
				((ColloquiWizard)getWizard()).getColloquio()
											 .setTipologia(
													 EnvSettingsFactory.getInstance()
													 				   .getTipologieColloqui().get(0)
													 	   );
			}
			
		});
		
		Label visitaDescrizione = new Label(container,SWT.NONE);
		visitaDescrizione.setText("Colloquio per appuntamento visita immobile");
		Button radioVisita = new Button(container,SWT.RADIO);
		radioVisita.setText("Visita");
		radioVisita.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				
			}

			@Override
			public void mouseUp(MouseEvent e) {								
				((ColloquiWizard)getWizard()).getColloquio()
											 .setTipologia(
													 EnvSettingsFactory.getInstance()
													 				   .getTipologieColloqui().get(1));
				((ColloquiWizard)getWizard()).getColloquio()
											 .setScadenziere(Boolean.TRUE);
													 	   
			}
			
		});

		Label genericoDescrizione = new Label(container,SWT.NONE);
		genericoDescrizione.setText("Colloquio generico");

		Button radioGenerico = new Button(container,SWT.RADIO);
		radioGenerico.setText("Generico");
		radioGenerico.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				
			}

			@Override
			public void mouseUp(MouseEvent e) {				
				((ColloquiWizard)getWizard()).getColloquio()
											 .setTipologia(
													 EnvSettingsFactory.getInstance()
													 				   .getTipologieColloqui().get(2)
													 	  );
			}
			
		});


		((ColloquiWizard)getWizard()).getColloquio()
									 .setTipologia(
											 EnvSettingsFactory.getInstance()
											 				   .getTipologieColloqui().get(0)
											 				);		
		
		setControl(container);
	}

}
