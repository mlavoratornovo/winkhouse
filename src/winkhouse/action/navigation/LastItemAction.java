package winkhouse.action.navigation;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import winkhouse.action.anagrafiche.ApriDettaglioAnagraficaAction;
import winkhouse.action.immobili.ApriDettaglioImmobileAction;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.helper.ProfilerHelper;
import winkhouse.model.AnagraficheModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Immobili;
import winkhouse.perspective.AffittiPerspective;
import winkhouse.perspective.AnagrafichePerspective;
import winkhouse.perspective.ImmobiliPerspective;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.anagrafica.DettaglioAnagraficaView;
import winkhouse.view.immobili.DettaglioImmobileView;


public class LastItemAction extends Action {

	public static String ID = "winkhouse.lastitemaction";
	
	public LastItemAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	@Override
	public void run() {

		IPerspectiveDescriptor ipd = PlatformUI.getWorkbench()
 		   									   .getActiveWorkbenchWindow()
 		   									   .getActivePage()
 		   									   .getPerspective();

		if (ipd.getId().equalsIgnoreCase(ImmobiliPerspective.ID)){

			if (ProfilerHelper.getInstance().getPermessoUI(DettaglioImmobileView.ID)){
				
				Integer codImm = WinkhouseUtils.getInstance().getCodiciImmobili()
						 									 .get(WinkhouseUtils.getInstance()
						 								     .getCodiciImmobili().size() - 1);

				ImmobiliDAO iDAO = new ImmobiliDAO();
				Immobili im = iDAO.getImmobileById(codImm);
				
				ApriDettaglioImmobileAction adia = new ApriDettaglioImmobileAction(im,null);
				adia.run();
			}else{
				MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						  					  "Controllo permessi accesso vista",
						  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
						  					  " non ha il permesso di accedere alla vista " + 
						  					  DettaglioImmobileView.ID);
			}
			
		}
		
		if (ipd.getId().equalsIgnoreCase(AffittiPerspective.ID)){
			
			IWorkbenchPart wp = PlatformUI.getWorkbench()
	   		   							  .getActiveWorkbenchWindow()
	   		   							  .getActivePage()
	   		   							  .getActivePart();
			
			Integer codImm = WinkhouseUtils.getInstance()
			 								 .getCodiciImmobili()
			 								 .get(WinkhouseUtils.getInstance()
			 													  .getCodiciImmobili().size() - 1);

			ImmobiliDAO iDAO = new ImmobiliDAO();
			Immobili im = null;
			
			im = iDAO.getImmobileById(codImm);
			int count = WinkhouseUtils.getInstance()
			  						    .getCodiciImmobili().size() - 1;
			while (!im.isAffitto()){
				count--;
				codImm = WinkhouseUtils.getInstance()
				 						 .getCodiciImmobili().get(count);
				im = iDAO.getImmobileById(codImm);
			}
			
			if (wp instanceof DettaglioImmobileView){
				if (ProfilerHelper.getInstance().getPermessoUI(DettaglioImmobileView.ID)){
					ApriDettaglioImmobileAction adia = new ApriDettaglioImmobileAction(im,null);
					adia.run();
				}else{
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							  					  "Controllo permessi accesso vista",
							  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
							  					  " non ha il permesso di accedere alla vista " + 
							  					  DettaglioImmobileView.ID);
				}
				
			}else{
				if (ProfilerHelper.getInstance().getPermessoUI(DettaglioImmobileView.ID)){
					ApriDettaglioImmobileAction adia = new ApriDettaglioImmobileAction(im,null);
					adia.run();
				}else{
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							  					  "Controllo permessi accesso vista",
							  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
							  					  " non ha il permesso di accedere alla vista " + 
							  					  DettaglioImmobileView.ID);
				}
			}
		}
		
		if (ipd.getId().equalsIgnoreCase(AnagrafichePerspective.ID)){
		
			IWorkbenchPart wp = PlatformUI.getWorkbench()
									      .getActiveWorkbenchWindow()
									      .getActivePage()
									      .getActivePart();
		
			Integer codAnag = WinkhouseUtils.getInstance()
											  .getCodiciAnagrafiche()
											  .get(WinkhouseUtils.getInstance()
													  			   .getCodiciAnagrafiche().size() - 1);
		
			AnagraficheDAO aDAO = new AnagraficheDAO();
			AnagraficheModel am = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), 
																			codAnag);
			
			if (WinkhouseUtils.getInstance()
								.isAnagraficheFiltered()){

				if (WinkhouseUtils.getInstance()
									.getAngraficheFilterType() == WinkhouseUtils.PROPIETARI){
			
					am = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), 
							   									   codAnag);
					
					int i = 1;
					while ((am.getImmobili() == null) || 
						   (am.getImmobili().size() == 0)){
						
						codAnag = WinkhouseUtils.getInstance()
												  .getCodiciAnagrafiche().get(WinkhouseUtils.getInstance()
											  			   									  .getCodiciAnagrafiche().size() - i);	
						WinkhouseUtils.getInstance().setLastCodAnagraficaSelected(codAnag);
						am = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), 
								   									   codAnag);
						i++;
						
					}
			
			
				}
			
				if (WinkhouseUtils.getInstance()
									.getAngraficheFilterType() == WinkhouseUtils.RICHIEDENTI){
			
					am = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), 
							   									   codAnag);
			
					int i = 1;
					while ((am.getImmobili() != null) && 
						   (am.getImmobili().size() > 0)){
					
						codAnag = WinkhouseUtils.getInstance()
								  				  .getCodiciAnagrafiche().get(WinkhouseUtils.getInstance()
		  			   									  									  .getCodiciAnagrafiche().size() - i);
						WinkhouseUtils.getInstance().setLastCodAnagraficaSelected(codAnag);
						am = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), 
													   				   codAnag);
						i++;
					
					}
			
				}
			
			}else{
				am = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), 
															   codAnag);
			}			
			
		
			if (wp instanceof DettaglioAnagraficaView){
				if (ProfilerHelper.getInstance().getPermessoUI(DettaglioAnagraficaView.ID)){
//					ApriDettaglioAnagraficaAction adaa = new ApriDettaglioAnagraficaAction(am,null);
//					adaa.run();
				}else{
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							  					  "Controllo permessi accesso vista",
							  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
							  					  " non ha il permesso di accedere alla vista " + 
							  					  DettaglioAnagraficaView.ID);
				}
			}else{
				if (ProfilerHelper.getInstance().getPermessoUI(DettaglioAnagraficaView.ID)){
//					ApriDettaglioAnagraficaAction adaa = new ApriDettaglioAnagraficaAction(am,null);
//					adaa.run();
				}else{
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
							  					  "Controllo permessi accesso vista",
							  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
							  					  " non ha il permesso di accedere alla vista " + 
							  					  DettaglioAnagraficaView.ID);
				}
			}
		
		}
		
	}

}
