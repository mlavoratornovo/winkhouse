package winkhouse.action.affitti;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.helper.ProfilerHelper;
import winkhouse.model.AffittiModel;
import winkhouse.orm.Affitti;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.affitti.DettaglioAffittiView;
import winkhouse.view.affitti.handler.DettaglioAffittiHandler;

public class ApriAffittiAction extends Action {
	
	private Affitti affitto = null;	
	
	public ApriAffittiAction(Affitti affitto) {
		this.affitto = affitto;
	}

	public ApriAffittiAction(String text) {
		super(text);
	}

	public ApriAffittiAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public ApriAffittiAction(String text, int style) {
		super(text, style);
	}

	
	@Override
	public void run() {
		if (ProfilerHelper.getInstance().getPermessoUI(DettaglioAffittiView.ID)){			
			DettaglioAffittiHandler.getInstance().getDettaglioAffitto(this.affitto);			
		}else{
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					  					  "Controllo permessi accesso vista",
					  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
					  					  " non ha il permesso di accedere alla vista " + 
					  					DettaglioAffittiView.ID);
		}

	}
	
}
