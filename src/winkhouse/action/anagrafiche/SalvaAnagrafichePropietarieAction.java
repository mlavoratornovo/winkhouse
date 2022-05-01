package winkhouse.action.anagrafiche;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.helper.ImmobiliHelper;
import winkhouse.view.immobili.AnagrafichePropietarieView;

public class SalvaAnagrafichePropietarieAction extends Action {

	public SalvaAnagrafichePropietarieAction() {
		// TODO Auto-generated constructor stub
	}

	public SalvaAnagrafichePropietarieAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public SalvaAnagrafichePropietarieAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public SalvaAnagrafichePropietarieAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void run() {
		AnagrafichePropietarieView apv = null;
		
		apv = (AnagrafichePropietarieView)PlatformUI.getWorkbench()
									  				.getActiveWorkbenchWindow()
									  				.getActivePage()
									  				.getActivePart();
		
		if (apv.getImmobile() != null && apv.getImmobile().getCodImmobile() != null && apv.getImmobile().getCodImmobile() != 0){
			
			ImmobiliHelper ih = new ImmobiliHelper();
			
			if (ih.updateImmobiliPropietari(apv.getImmobile(), null, true) == true){
				apv.getImmobile().setAnagrafichePropietarie(null);
				apv.getTvAnagrafichePropietarie().refresh();
			}
			
		}else{
			MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
									  "Salvataggio anagrafiche proprietarie",
									  "Eseguire il salvataggio dell'immobile");
		}
		
	}

	
}
