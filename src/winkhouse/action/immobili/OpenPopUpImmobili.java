package winkhouse.action.immobili;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.colloqui.DettaglioColloquioView;
import winkhouse.view.immobili.PopUpRicercaImmobile;


public class OpenPopUpImmobili extends Action {

	public OpenPopUpImmobili() {
	}

	public OpenPopUpImmobili(String text) {
		super(text);
	}

	public OpenPopUpImmobili(String text, ImageDescriptor image) {
		super(text, image);
	}

	public OpenPopUpImmobili(String text, int style) {
		super(text, style);
	}

	@Override
	public String getToolTipText() {
		return "Apri lista immobili";
	}

	@Override
	public void run() {
		
		PopUpRicercaImmobile rImmobili = new PopUpRicercaImmobile(PlatformUI.getWorkbench()
				  															.getActiveWorkbenchWindow()
				  															.getShell());
		
		DettaglioColloquioView dcv = (DettaglioColloquioView)PlatformUI.getWorkbench()
																	  .getActiveWorkbenchWindow()
																	  .getActivePage()
																	  .getActivePart();
		
		rImmobili.setCallerObj(dcv);
		rImmobili.setSetterMethodName("setImmobile");
		rImmobili.open();
	}

}
