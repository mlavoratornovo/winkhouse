package winkhouse.action.agenda;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.AppuntamentiModel;
import winkhouse.view.agenda.DettaglioAppuntamentoView;


public class NuovoAppuntamentoAction extends Action {


	public NuovoAppuntamentoAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	@Override
	public void run() {
		try {
			DettaglioAppuntamentoView dav = (DettaglioAppuntamentoView)PlatformUI.getWorkbench()
					  								  							 .getActiveWorkbenchWindow()
					  								  							 .getActivePage()
					  								  							 .showView(DettaglioAppuntamentoView.ID);
			AppuntamentiModel am = new AppuntamentiModel();
			dav.setAppuntamento(am);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}


}
