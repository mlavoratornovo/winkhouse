package winkhouse.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.helper.ProfilerHelper;
import winkhouse.perspective.AffittiPerspective;
import winkhouse.perspective.AnagrafichePerspective;
import winkhouse.perspective.ColloquiPerspective;
import winkhouse.perspective.ImmobiliPerspective;
import winkhouse.util.WinkhouseUtils;
import winkhouse.wizard.RicercaWizard;



public class WizardRicercaAction extends Action {

	public static String ID = "winkhouse.wizardricercaaction";
	
	public WizardRicercaAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	@Override
	public void run() {
		if (ProfilerHelper.getInstance().getPermessoUI(RicercaWizard.ID)){

			String currentPerspectiveId = PlatformUI.getWorkbench()
				    								.getActiveWorkbenchWindow()
				    								.getActivePage()
				    								.getPerspective().getId();
					
			if ((currentPerspectiveId != null) && 
					((currentPerspectiveId.equalsIgnoreCase(ImmobiliPerspective.ID)) || 
					 (currentPerspectiveId.equalsIgnoreCase(AnagrafichePerspective.ID)) ||
					 (currentPerspectiveId.equalsIgnoreCase(AffittiPerspective.ID)) ||
					 (currentPerspectiveId.equalsIgnoreCase(ColloquiPerspective.ID)))){
				
				RicercaWizard wizard = null;
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (currentPerspectiveId.equalsIgnoreCase(ImmobiliPerspective.ID)){
					wizard = new RicercaWizard(RicercaWizard.IMMOBILI);					
				}else if (currentPerspectiveId.equalsIgnoreCase(AffittiPerspective.ID)){
					wizard = new RicercaWizard(RicercaWizard.AFFITTI);
				}else if (currentPerspectiveId.equalsIgnoreCase(AnagrafichePerspective.ID)){
					wizard = new RicercaWizard(RicercaWizard.ANAGRAFICHE);
				}else if (currentPerspectiveId.equalsIgnoreCase(ColloquiPerspective.ID)){
					wizard = new RicercaWizard(RicercaWizard.COLLOQUI);
				}
				if (wizard != null){
					WizardDialog dialog = new WizardDialog(window.getShell(), wizard);	
					dialog.setPageSize(400, 300);
					WinkhouseUtils.getInstance()
								  .setRicercaWiz(wizard);
					dialog.open();
				}else{
					MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							   SWT.ERROR);
					mb.setText("impossibile eseguire ricerche in questa prospettiva");
					mb.setMessage("impossibile eseguire ricerche in questa prospettiva");			
					mb.open();																		
				}
					
			}else{
				MessageBox mb = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
											   SWT.ERROR);
				mb.setText("impossibile eseguire ricerche in questa prospettiva");
				mb.setMessage("impossibile eseguire ricerche in questa prospettiva");			
				mb.open();													

			}

		}else{
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					  					  "Controllo permessi accesso vista",
					  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
					  					  " non ha il permesso di accedere alla vista " + 
					  					  RicercaWizard.ID);
			
		}
		
	}

	
}
