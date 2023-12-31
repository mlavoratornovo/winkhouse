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
import winkhouse.perspective.AffittiPerspective;
import winkhouse.perspective.AnagrafichePerspective;
import winkhouse.perspective.ImmobiliPerspective;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.anagrafica.DettaglioAnagraficaView;
import winkhouse.view.immobili.DettaglioImmobileView;


public class NextItemAction extends Action {
	
	public static String ID = "winkhouse.nextitemaction";
	
	public NextItemAction(String text, ImageDescriptor image) {
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
				IWorkbenchPart wp = PlatformUI.getWorkbench()
											  .getActiveWorkbenchWindow()
											  .getActivePage()
											  .getActivePart();
	
				Integer codImm = WinkhouseUtils.getInstance()
					 							 .getCodiciImmobili()
					 							 .get(WinkhouseUtils.getInstance()
							 										  .getNextCodImmobile());
			
				ImmobiliDAO iDAO = new ImmobiliDAO();
				ImmobiliModel im = (ImmobiliModel)iDAO.getImmobileById(ImmobiliModel.class.getName(), 
																	   codImm);		
				
			
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
			
			int index = WinkhouseUtils.getInstance()
			  							.getNextCodImmobile();
			Integer codImm = WinkhouseUtils.getInstance()
			 								 .getCodiciImmobili()
			 								 .get(index);

			ImmobiliDAO iDAO = new ImmobiliDAO();
			ImmobiliModel im = null;
			
			im = (ImmobiliModel)iDAO.getImmobileById(ImmobiliModel.class.getName(), codImm);
			int count = index;
			while (!im.getAffittabile()){
				count++;
				codImm = WinkhouseUtils.getInstance()
				 						 .getCodiciImmobili().get(count);
				im = (ImmobiliModel)iDAO.getImmobileById(ImmobiliModel.class.getName(), codImm);
			}
			
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
		if (ipd.getId().equalsIgnoreCase(AnagrafichePerspective.ID)){
		
		
			Integer codAnag = WinkhouseUtils.getInstance()
											  .getCodiciAnagrafiche()
											  .get(WinkhouseUtils.getInstance()
													  			   .getNextCodAnagrafiche());
		
			AnagraficheDAO aDAO = new AnagraficheDAO();
			AnagraficheModel am = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), 
																			codAnag);
		
			
			if (WinkhouseUtils.getInstance()
					.isAnagraficheFiltered()){

				if (WinkhouseUtils.getInstance()
									.getAngraficheFilterType() == WinkhouseUtils.PROPIETARI){
			
					am = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), 
							   									   codAnag);
					
					
					while ((am.getImmobili() == null) || 
						   (am.getImmobili().size() == 0)){
						
						codAnag = WinkhouseUtils.getInstance()
												  .getCodiciAnagrafiche().get(WinkhouseUtils.getInstance()
											  			   									  .getNextCodAnagrafiche());
						WinkhouseUtils.getInstance().setLastCodAnagraficaSelected(codAnag);
						am = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), 
								   									   codAnag);
						
						
					}
			
			
				}
			
				if (WinkhouseUtils.getInstance()
									.getAngraficheFilterType() == WinkhouseUtils.RICHIEDENTI){
			
					am = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), 
							   									   codAnag);
			
					while ((am.getImmobili() != null) && 
						   (am.getImmobili().size() > 0)){
					
						codAnag = WinkhouseUtils.getInstance()
								  				  .getCodiciAnagrafiche().get(WinkhouseUtils.getInstance()
											  			   									  .getNextCodAnagrafiche());	
						WinkhouseUtils.getInstance().setLastCodAnagraficaSelected(codAnag);
						am = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), 
													   				   codAnag);						
					
					}
			
				}
			
			}else{
				am = (AnagraficheModel)aDAO.getAnagraficheById(AnagraficheModel.class.getName(), 
															   codAnag);
			}			
			
			
			if (ProfilerHelper.getInstance().getPermessoUI(DettaglioImmobileView.ID)){
//				ApriDettaglioAnagraficaAction adaa = new ApriDettaglioAnagraficaAction(am,null);
//				adaa.run();
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
