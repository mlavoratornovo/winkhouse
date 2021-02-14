package winkhouse.wizard.colloqui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

public class AgentiAssegnati extends WizardPage {

	private Composite container = null;
	
	public AgentiAssegnati(String pageName) {
		super(pageName);
	}

	public AgentiAssegnati(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	@Override
	public void createControl(Composite parent) {
		setControl(container);
	}

}
