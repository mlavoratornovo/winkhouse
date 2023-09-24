package winkhouse.wizard.cancellazioni;

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

public class SelezioneTipologia extends WizardPage {

	private Composite composite = null;
	
	public SelezioneTipologia(String pageName) {
		super(pageName);
	}

	public SelezioneTipologia(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite parent) {
		
		composite = new Composite(parent,SWT.NONE);
		composite.setLayout(new GridLayout());
		
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		
		Button radioCancellaTutto = new Button(composite,SWT.RADIO);
		Label labelCancellaTutto = new Label(composite,SWT.NONE);
		labelCancellaTutto.setText("Elimina l'oggetto selezionato e tutti quelli ad esso collegati, \n viene mostrata una anteprima non editabile nella schermata successiva");

		Button radioCancellaManuale = new Button(composite,SWT.RADIO);
		Label labelCancellaManuale = new Label(composite,SWT.NONE);
		labelCancellaManuale.setText("Elimina l'oggetto selezionato e tutti quelli ad esso collegati, \n viene mostrata una anteprima editabile nella schermata successiva");

		Button radioStorico = new Button(composite,SWT.RADIO);
		radioStorico.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
		
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
		
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				
			}
			
		});
		Label labelStorico = new Label(composite,SWT.NONE);
		labelStorico.setText("Sposta l'oggetto nello storico, non avviene nessuna cancellazione dagli archivi");
		
		setControl(composite);
	}

}
