package winkhouse.action.anagrafiche;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.helper.ProfilerHelper;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.anagrafica.PopUpRicercaAnagrafica;
import winkhouse.view.immobili.AnagrafichePropietarieView;



public class OpenPopUpAnagrafiche extends Action {	
	
	public OpenPopUpAnagrafiche() {
	}

	public OpenPopUpAnagrafiche(String text) {
		super(text);
	}

	public OpenPopUpAnagrafiche(String text, ImageDescriptor image) {
		super(text, image);
	}

	public OpenPopUpAnagrafiche(String text, int style) {
		super(text, style);
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getDefault().getImageDescriptor("icons/anagrafiche16.png");
	}

	@Override
	public String getToolTipText() {
		return "Apri lista anagrafiche";
	}

	@Override
	public void run() {
		
		if (ProfilerHelper.getInstance().getPermessoUI(PopUpRicercaAnagrafica.ID)){
				PopUpRicercaAnagrafica rAnagrafiche = new PopUpRicercaAnagrafica(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell());				
				AnagrafichePropietarieView div = (AnagrafichePropietarieView)PlatformUI.getWorkbench()
																			 	       .getActiveWorkbenchWindow()
																			           .getActivePage()
																			           .findView(AnagrafichePropietarieView.ID);
				
				rAnagrafiche.setCallerObj(div);
				rAnagrafiche.setSetterMethodName("addAnagrafica");
				rAnagrafiche.open();
		}else{
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					  					  "Controllo permessi accesso vista",
					  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
					  					  " non ha il permesso di accedere alla vista " + 
					  					  PopUpRicercaAnagrafica.ID);
		}
		
	}

}
