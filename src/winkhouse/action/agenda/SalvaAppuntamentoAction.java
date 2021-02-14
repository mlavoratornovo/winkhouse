package winkhouse.action.agenda;

import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.AppuntamentiDAO;
import winkhouse.helper.AppuntamentiHelper;
import winkhouse.helper.OptimisticLockHelper;
import winkhouse.model.AppuntamentiModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.agenda.CalendarioView;
import winkhouse.view.agenda.DettaglioAppuntamentoView;
import winkhouse.view.agenda.ListaAppuntamentiView;
import winkhouse.view.agenda.handler.DettaglioAppuntamentoHandler;
import winkhouse.view.immobili.DettaglioImmobileView;
import winkhouse.vo.AppuntamentiVO;



public class SalvaAppuntamentoAction extends Action {

	private String returnView = null;
	
	public SalvaAppuntamentoAction(String returnView) {
		this.returnView = returnView;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getImageDescriptor("icons/document-save.png");
	}

	@Override
	public String getText() {
		return "Salva appuntamento";
	}

	@Override
	public void run() {
		DettaglioAppuntamentoView dav = (DettaglioAppuntamentoView)PlatformUI.getWorkbench()
				  															 .getActiveWorkbenchWindow()
				  															 .getActivePage()
				  															 .getActivePart();
		if (dav != null){
			
			AppuntamentiModel am = dav.getAppuntamento();
			
			OptimisticLockHelper olh = new OptimisticLockHelper();
						
			am.setDateUpdate(new Date());
			if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
				am.setCodUserUpdate(WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());
			}
			
			String decision = olh.checkOLAppuntamento(am);
			
			if (decision.equalsIgnoreCase(OptimisticLockHelper.SOVRASCRIVI)){
			
			
				if ((am.getCodAppuntamento() != null) && (am.getCodAppuntamento() != 0)){
					AppuntamentiDAO aDAO = new AppuntamentiDAO();
					AppuntamentiVO dbapVO = (AppuntamentiVO)aDAO.getAppuntamentoByID(AppuntamentiVO.class.getName(), am.getCodAppuntamento());
					
					if (am.getDataAppuntamento().compareTo(dbapVO.getDataAppuntamento()) != 0){
						am.setiCalUID(null);
					}else if(am.getDataFineAppuntamento().compareTo(dbapVO.getDataFineAppuntamento()) != 0){
						am.setiCalUID(null);
					}
						
					
				}
				
				if ((am.getAgenti() != null) && (am.getAgenti().size() > 0)){
					AppuntamentiHelper ah = new AppuntamentiHelper();
					if (ah.saveAppuntamento(am)){
						PlatformUI.getWorkbench()
				 		  		  .getActiveWorkbenchWindow()
				 		  		  .getActivePage()
				 		  		  .hideView(dav);
						dav = DettaglioAppuntamentoHandler.getInstance()
														  .getDettaglioAppuntamento(am);
	
					}
				
					CercaAppuntamentiAction caa = new CercaAppuntamentiAction(returnView);
					caa.run();
					if (returnView.equalsIgnoreCase(ListaAppuntamentiView.class.getName())){
						CercaAppuntamentiAction caa2 = new CercaAppuntamentiAction(CalendarioView.class.getName());
						caa2.run();					
					}else{
						CercaAppuntamentiAction caa2 = new CercaAppuntamentiAction(ListaAppuntamentiView.class.getName());
						caa2.run();										
					}
					
				}else{
					MessageDialog.openWarning(Activator.getDefault()
													   .getWorkbench()
													   .getActiveWorkbenchWindow()
													   .getShell(),
											  "Errore salvataggio appuntamento", 
											  "Selezionare un agente");
	
				}
				
			}else if (decision.equalsIgnoreCase(OptimisticLockHelper.VISUALIZZA)){
				
				try {
					DettaglioAppuntamentoView div_comp = (DettaglioAppuntamentoView)PlatformUI.getWorkbench()
																				 	  .getActiveWorkbenchWindow()
																				 	  .getActivePage()
																				 	  .showView(DettaglioImmobileView.ID,
																			        		   	String.valueOf(am.getCodAppuntamento()) + "Comp",
																			        		   	IWorkbenchPage.VIEW_CREATE);
					AppuntamentiDAO i_compDAO = new AppuntamentiDAO();
					AppuntamentiModel appuntamento_comp = (AppuntamentiModel)i_compDAO.getAppuntamentoByID(AppuntamentiModel.class.getName(), am.getCodAppuntamento());
					
					div_comp.setAppuntamento(appuntamento_comp);
					div_comp.setCompareView(false);
					
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}

			
		}		
	}
	
	

}
