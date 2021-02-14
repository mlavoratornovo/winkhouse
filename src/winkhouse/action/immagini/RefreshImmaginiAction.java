package winkhouse.action.immagini;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.immobili.ImmaginiImmobiliView;


public class RefreshImmaginiAction extends Action {

	public RefreshImmaginiAction() {

	}

	public RefreshImmaginiAction(String text) {
		super(text);

	}

	public RefreshImmaginiAction(String text, ImageDescriptor image) {
		super(text, image);

	}

	public RefreshImmaginiAction(String text, int style) {
		super(text, style);

	}

	@Override
	public void run() {
		ImmaginiImmobiliView iiv = null;
		
		iiv = (ImmaginiImmobiliView)PlatformUI.getWorkbench()
									 .getActiveWorkbenchWindow()
				  					 .getActivePage()
				  					 .getActivePart();
		if (iiv.getImmobile() != null){
			iiv.getImmobile().setImmagini(null);
			iiv.setImmobile(iiv.getImmobile());
		}else{
			MessageDialog.openWarning(PlatformUI.getWorkbench()
												.getActiveWorkbenchWindow()
												.getShell(),
									  "Attenzione", 
									  "Selezionare un dettaglio immobile");
		}
	}

}
