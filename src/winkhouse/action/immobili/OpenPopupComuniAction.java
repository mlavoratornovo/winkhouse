package winkhouse.action.immobili;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.common.PopUpSceltaComune;
import winkhouse.view.immobili.DettaglioImmobileView;

public class OpenPopupComuniAction extends Action {

	public OpenPopupComuniAction() {
		// TODO Auto-generated constructor stub
	}

	public OpenPopupComuniAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public OpenPopupComuniAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public OpenPopupComuniAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void run() {
		
		DettaglioImmobileView div = (DettaglioImmobileView)PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								 									    			.getActivePage()
								 									    			.getActivePart();
		
		PopUpSceltaComune popsc = new PopUpSceltaComune(div,"setComune",div.getComune());
		
		
	}

	
}
