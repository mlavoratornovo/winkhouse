package winkhouse.wizard.cancellazioni;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public class ListaOggetti extends WizardPage {

	public ListaOggetti(String pageName) {
		super(pageName);
	}

	public ListaOggetti(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite parent) {

	}

}
