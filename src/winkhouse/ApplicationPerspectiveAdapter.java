package winkhouse;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.part.ViewPart;

import winkhouse.action.anagrafiche.RefreshAnagraficheAction;
import winkhouse.action.immobili.RefreshImmobiliAction;
import winkhouse.action.recapiti.ApriDettaglioRecapitiAction;
import winkhouse.helper.ProfilerHelper;
import winkhouse.model.ImmobiliModel;
import winkhouse.perspective.AffittiPerspective;
import winkhouse.perspective.AnagrafichePerspective;
import winkhouse.perspective.ImmobiliPerspective;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.anagrafica.AnagraficaTreeView;
import winkhouse.view.common.AbbinamentiView;
import winkhouse.view.common.ColloquiView;
import winkhouse.view.common.RecapitiView;
import winkhouse.view.immobili.ImmaginiImmobiliView;
import winkhouse.view.immobili.ImmobiliTreeView;


public class ApplicationPerspectiveAdapter extends PerspectiveAdapter {

	//private String lastperspectiveId = "";
	
	public ApplicationPerspectiveAdapter() {

	}

	@SuppressWarnings("restriction")
	@Override
	public void perspectiveActivated(IWorkbenchPage page,
									 IPerspectiveDescriptor perspective) {
		
		if (ProfilerHelper.getInstance().getPermessoUI(perspective.getId())){
		
			
			super.perspectiveActivated(page, perspective);
			
			if ((perspective.getId().equalsIgnoreCase(ImmobiliPerspective.ID)) && 			
				(!WinkhouseUtils.getInstance()
								  .getLastPerspectiveSelected()
								  .equalsIgnoreCase(perspective.getId()))){
				if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null){
					WinkhouseUtils.getInstance()
					  				.setLastPerspectiveSelected(perspective.getId());
					ViewPart vp = (ViewPart)PlatformUI.getWorkbench()
							  						  .getActiveWorkbenchWindow()
							  						  .getActivePage()
							  						  .findView(ImmobiliTreeView.ID);
					
//					WorkbenchWindow app = (WorkbenchWindow)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getData();
//					CoolBarManager coolBar = app.getCoolBarManager();
//					IContributionItem[] ci = coolBar.getItems();
					
					if (vp != null){
						//((ImmobiliTreeView)(vp)).setFocus();
						RefreshImmobiliAction ria = new RefreshImmobiliAction();
						ria.run();
					}
									
				}
			}else if ((perspective.getId().equalsIgnoreCase(AnagrafichePerspective.ID)) && 		    
					  (!WinkhouseUtils.getInstance()
							  			.getLastPerspectiveSelected()
							  			.equalsIgnoreCase(perspective.getId()))){
	
				if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null){
					ViewPart vp = (ViewPart)PlatformUI.getWorkbench()
							  						  .getActiveWorkbenchWindow()
							  						  .getActivePage()
							  						  .findView(AnagraficaTreeView.ID);
					WinkhouseUtils.getInstance()
									.setLastPerspectiveSelected(perspective.getId());
	
					if(vp != null){
						//((AnagraficaTreeView)(vp)).setFocus();
						RefreshAnagraficheAction raa = new RefreshAnagraficheAction();
						raa.run();
					}
									
				}
			}else if ((perspective.getId().equalsIgnoreCase(AffittiPerspective.ID)) && 			
					  (!WinkhouseUtils.getInstance()
							  		    .getLastPerspectiveSelected()
							  		    .equalsIgnoreCase(perspective.getId()))){
				WinkhouseUtils.getInstance()
						      .setLastPerspectiveSelected(perspective.getId());
				
				Display.getDefault().asyncExec(new Runnable() {
				    @Override
				    public void run() {
						ViewPart vp = (ViewPart)PlatformUI.getWorkbench()
								                          .getActiveWorkbenchWindow()
								                          .getActivePage()
								                          .findView(ImmobiliTreeView.ID);
	
						if (vp != null){
							RefreshImmobiliAction ria = new RefreshImmobiliAction();
							ria.run();
						}
	
				    }
				});
				
			}else{
				WinkhouseUtils.getInstance()
								.setLastPerspectiveSelected(perspective.getId());
			}
			
		}else{
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
										  "Controllo permessi accesso prospettiva",
										  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
										  " non ha il permesso di accedere alla prospettiva " + 
										  perspective.getId());

			
			page.closePerspective(perspective, false, true);			
			
//			ViewPart vp = (ViewPart)PlatformUI.getWorkbench()
//					  .getActiveWorkbenchWindow()
//					  .getActivePage()
//					  .findView(ImmobiliTreeView.ID);
//			IWorkbenchPartReference wpr = page.getReference(vp.getSite().getPart());
//			page.setPartState(wpr, IWorkbenchPage.STATE_MINIMIZED);
//			
//			page.setPartState(wpr, IWorkbenchPage.STATE_RESTORED);
			
		}
	}

	@Override
	public void perspectiveDeactivated(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		super.perspectiveDeactivated(page, perspective);

			IViewReference[] vr = Activator.getDefault().getWorkbench()
										   .getActiveWorkbenchWindow().getActivePage()
										   .getViewReferences();

			for (int i=0; i < vr.length; i++){
				IViewPart ivp = vr[i].getView(true);
				if (ivp instanceof RecapitiView){
					ApriDettaglioRecapitiAction adra = new ApriDettaglioRecapitiAction(null,false);
					adra.run();
//					
//					((RecapitiView)ivp).setAnagrafica(new AnagraficheModel());
				}
				if (ivp instanceof ImmaginiImmobiliView){
					((ImmaginiImmobiliView)ivp).setImmobile(new ImmobiliModel());
				} 
				if (ivp instanceof ColloquiView){
					((ColloquiView)ivp).setImmobile(new ImmobiliModel());
				} 
				if (ivp instanceof AbbinamentiView){
					((AbbinamentiView)ivp).setImmobile(null);
					((AbbinamentiView)ivp).setAnagrafica(null);
				}

			}


	}

}
