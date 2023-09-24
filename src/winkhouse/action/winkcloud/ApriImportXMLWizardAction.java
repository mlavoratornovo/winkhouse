package winkhouse.action.winkcloud;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.wizard.ColloquiWizard;
import winkhouse.xmldeser.wizard.importer.ImporterWizard;

public class ApriImportXMLWizardAction extends Action {

	public ApriImportXMLWizardAction() {
		// TODO Auto-generated constructor stub
	}

	public ApriImportXMLWizardAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public ApriImportXMLWizardAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public ApriImportXMLWizardAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void run() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ImporterWizard wizard = new ImporterWizard();
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.setPageSize(400, 300);
		dialog.open();
	}

	
}
