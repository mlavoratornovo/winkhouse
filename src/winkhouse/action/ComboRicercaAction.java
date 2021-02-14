package winkhouse.action;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.dao.RicercheDAO;
import winkhouse.helper.ProfilerHelper;
import winkhouse.model.ReportModel;
import winkhouse.model.RicercheModel;
import winkhouse.perspective.AffittiPerspective;
import winkhouse.perspective.AnagrafichePerspective;
import winkhouse.perspective.ColloquiPerspective;
import winkhouse.perspective.ImmobiliPerspective;
import winkhouse.util.WinkhouseUtils;
import winkhouse.vo.RicercheVO;
import winkhouse.wizard.RicercaWizard;

public class ComboRicercaAction extends Action implements IAction, IMenuCreator {

	private Menu fMenu;
	
	public ComboRicercaAction() {
	}

	public ComboRicercaAction(String text) {
		super(text);
	}

	public ComboRicercaAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public ComboRicercaAction(String text, int style) {
		super(text, style);
	}

	@Override
	public void dispose() {
		if (fMenu != null) {
			fMenu.dispose();
			fMenu = null;
		}
	}
	
	private class RicercaAction extends Action{
		
		private RicercheModel ricerca = null;
		
		public RicercaAction(String label, RicercheModel ricerca){
			super(label);
			this.ricerca = ricerca;
		}

		@Override
		public void run() {
			
			if (ProfilerHelper.getInstance().getPermessoUI(RicercaWizard.ID)){

				String currentPerspectiveId = PlatformUI.getWorkbench()
					    								.getActiveWorkbenchWindow()
					    								.getActivePage()
					    								.getPerspective().getId();
						
				if (currentPerspectiveId != null){
					
					RicercaWizard wizard = null;
					if (currentPerspectiveId.equalsIgnoreCase(ImmobiliPerspective.ID)){
						wizard = new RicercaWizard(RicercaWizard.IMMOBILI);					
					}else if (currentPerspectiveId.equalsIgnoreCase(AffittiPerspective.ID)){
						wizard = new RicercaWizard(RicercaWizard.AFFITTI);
					}else if (currentPerspectiveId.equalsIgnoreCase(AnagrafichePerspective.ID)){
						wizard = new RicercaWizard(RicercaWizard.ANAGRAFICHE);
					}else if (currentPerspectiveId.equalsIgnoreCase(ColloquiPerspective.ID)){
						wizard = new RicercaWizard(RicercaWizard.COLLOQUI);
					}

					IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					wizard = new RicercaWizard(RicercaWizard.IMMOBILI);
					WizardDialog dialog = new WizardDialog(window.getShell(), wizard);	
					dialog.setPageSize(400, 300);
					WinkhouseUtils.getInstance()
								  .setRicercaWiz(wizard);
					wizard.getRicerca().setRicerca(ricerca);
					dialog.open();
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

	@Override
	public Menu getMenu(Control parent) {
		
		if (fMenu != null)
			fMenu.dispose();
		fMenu = new Menu(parent);
				
		RicercheDAO rDAO = new RicercheDAO();
		Integer tipoRicerca = null;
		String currentPerspectiveId = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage()
				.getPerspective().getId();

		if (currentPerspectiveId.equalsIgnoreCase(ImmobiliPerspective.ID)){
			tipoRicerca = RicercheVO.RICERCHE_IMMOBILI;					
		}else if (currentPerspectiveId.equalsIgnoreCase(AffittiPerspective.ID)){
			tipoRicerca = RicercheVO.RICERCHE_IMMOBILI_AFFITTI;
		}else if (currentPerspectiveId.equalsIgnoreCase(AnagrafichePerspective.ID)){
			tipoRicerca = RicercheVO.RICERCHE_ANAGRAFICHE;
		}else if (currentPerspectiveId.equalsIgnoreCase(ColloquiPerspective.ID)){
			tipoRicerca = RicercheVO.RICERCHE_COLLOQUI;
		}
		
        if (tipoRicerca != null){
        	
    		ArrayList al = rDAO.getRichercheByTipo(RicercheModel.class.getName(), tipoRicerca);
			
    		Iterator it = al.iterator();
    		while (it.hasNext()){
    			RicercheModel rm = (RicercheModel)it.next();
    			addActionToMenu(fMenu, new RicercaAction(rm.getNome(), rm));
    		}
        	
        } 
		
		return fMenu; 
	}
	
	protected void addActionToMenu(Menu parent, Action action) {
		ActionContributionItem item = new ActionContributionItem(action);
		item.fill(parent, -1);
	}

	@Override
	public Menu getMenu(Menu parent) {
		// TODO Auto-generated method stub
		return null;
	}


}
