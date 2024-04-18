package winkhouse.action.agenda;

import java.time.ZoneId;
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
import winkhouse.orm.Appuntamenti;
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
			
			Appuntamenti am = dav.getAppuntamento();
			
			OptimisticLockHelper olh = new OptimisticLockHelper();
						
			am.setDateupdate(new Date().toInstant()
				      .atZone(ZoneId.systemDefault())
				      .toLocalDateTime());
			if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
				am.setAgenti(WinkhouseUtils.getInstance().getLoggedAgent());
			}
			
			String decision = olh.checkOLAppuntamento(am);
			
			if (decision.equalsIgnoreCase(OptimisticLockHelper.SOVRASCRIVI)){
			
			
				if (am.getCodAppuntamento() != 0){
					AppuntamentiDAO aDAO = new AppuntamentiDAO();
					Appuntamenti dbapVO = aDAO.getAppuntamentoByID(am.getCodAppuntamento());
					
					if (am.getDataappuntamento().compareTo(dbapVO.getDataappuntamento()) != 0){
						am.setIcaluid(null);
					}else if(am.getDatafineappuntamento().compareTo(dbapVO.getDatafineappuntamento()) != 0){
						am.setIcaluid(null);
					}
						
					
				}
				
				if ((am.getAgentiappuntamentis() != null) && (am.getAgentiappuntamentis().size() > 0)){
//					AppuntamentiHelper ah = new AppuntamentiHelper();
//					if (ah.saveAppuntamento(am)){
					am.getEditObjectContext().commitChanges();
						PlatformUI.getWorkbench()
				 		  		  .getActiveWorkbenchWindow()
				 		  		  .getActivePage()
				 		  		  .hideView(dav);
						dav = DettaglioAppuntamentoHandler.getInstance()
														  .getDettaglioAppuntamento(am);
	
//					}
				
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
					Appuntamenti appuntamento_comp = i_compDAO.getAppuntamentoByID(am.getCodAppuntamento());
					
					div_comp.setAppuntamento(appuntamento_comp);
					div_comp.setCompareView(false);
					
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}

			
		}		
	}
	
	

}
