package winkhouse.action.winkcloud;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.xmldeser.wizard.importer.ImporterWizard;
import winkhouse.xmldeser.wizard.importer.vo.ImporterVO;

public class ApriWinkCloudImportXMLWizardAction extends Action {
	
	private ImporterVO ivo = null;
	
	public ApriWinkCloudImportXMLWizardAction(String text, ImageDescriptor image, ImporterVO ivo) {
		super(text, image);
		this.ivo = ivo; 
	}

	@Override
	public void run() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ImporterWizard wizard = new ImporterWizard();
		wizard.setImporterVO(ivo);
		wizard.setFromWinkCloud(true);
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.setPageSize(400, 300);
		dialog.open();	
	}

}
