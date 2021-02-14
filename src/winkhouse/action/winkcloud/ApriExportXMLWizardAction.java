package winkhouse.action.winkcloud;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.xmldeser.wizard.importer.ImporterWizard;
import winkhouse.xmlser.wizard.exporter.ExporterWizard;

public class ApriExportXMLWizardAction extends Action {

	public ApriExportXMLWizardAction() {
		// TODO Auto-generated constructor stub
	}

	public ApriExportXMLWizardAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public ApriExportXMLWizardAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public ApriExportXMLWizardAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void run() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ExporterWizard wizard = new ExporterWizard();
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.setPageSize(400, 300);
		dialog.open();
	}

	
}
