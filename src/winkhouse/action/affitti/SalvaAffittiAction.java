package winkhouse.action.affitti;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.dao.AffittiDAO;
import winkhouse.helper.AffittiHelper;
import winkhouse.helper.EntityHelper;
import winkhouse.helper.OptimisticLockHelper;
import winkhouse.model.AffittiModel;
import winkhouse.model.AttributeModel;
import winkhouse.orm.Affitti;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.affitti.DettaglioAffittiView;
import winkhouse.view.affitti.ListaAffittiView;

public class SalvaAffittiAction extends Action {

	public SalvaAffittiAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	@Override
	public void run() {
		
		DettaglioAffittiView dav = null;
		
		try {
			dav = (DettaglioAffittiView)PlatformUI.getWorkbench()
												  .getActiveWorkbenchWindow()
												  .getActivePage()
												  .getActivePart();
		} catch (Exception e) {}
		
		if ((dav != null) && (dav.getAffitto() != null)){
			
			Affitti afM = dav.getAffitto();
			
			Calendar cInizio = Calendar.getInstance();
			Calendar cFine = Calendar.getInstance();
			cInizio.setTime(Date.from(afM.getDatainizio().atZone(ZoneId.systemDefault()).toInstant()));
			
			if (afM.getDatafine() != null){
				cFine.setTime(Date.from(afM.getDatafine().atZone(ZoneId.systemDefault()).toInstant()));
			}
			
			if (cInizio.before(cFine) || afM.getDatafine() == null){
			
				OptimisticLockHelper olh = new OptimisticLockHelper();
				
				afM.setDateupdate(LocalDateTime.ofInstant(new Date().toInstant(),ZoneId.systemDefault()));
				if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
					afM.setAgenti(WinkhouseUtils.getInstance().getLoggedAgent());
				}
				
				String decision = olh.checkOLAffitto(afM);
				
				if (decision.equalsIgnoreCase(OptimisticLockHelper.SOVRASCRIVI)){
				
					AffittiHelper ah = new AffittiHelper();
					EntityHelper eh = new EntityHelper();
					afM.getObjectContext().commitChanges();
//					HashMap result = ah.saveAffitto(afM, null);
//					if ((Boolean)result.get(AffittiHelper.RESULT_AFFITTI_DB)){
//						for (AttributeModel attribute : afM.getAttributes()){
//							if (attribute.getValue(afM.getCodAffitti()) != null){
//								attribute.getValue(afM.getCodAffitti()).setIdObject(afM.getCodAffitti());
//								if (!eh.saveAttributeValue(attribute.getValue(afM.getCodAffitti()))){
//									MessageDialog.openError(PlatformUI.getWorkbench()
//											  						  .getActiveWorkbenchWindow()
//											                          .getShell(), 
//															"Errore salvataggio", 
//															"Errore durante il salvataggio del campo " + attribute.getAttributeName());
//								}
//							}
//						}
//	
//						ListaAffittiView lav = (ListaAffittiView)PlatformUI.getWorkbench()
//								  										   .getActiveWorkbenchWindow()
//								  										   .getActivePage()
//								  										   .findView(ListaAffittiView.ID);
//						lav.setImmobile(afM.getImmobile());
//						dav.setAffitto(afM);
//					}else{
//						MessageDialog.openError(PlatformUI.getWorkbench()
//														  .getActiveWorkbenchWindow()
//														  .getShell(), 
//												"Errore salvataggio affitto", 
//												"L'affitto non � stato salvato a causa di un errore nel salvataggio");		
//					}
					
				}else if (decision.equalsIgnoreCase(OptimisticLockHelper.VISUALIZZA)){
					
					try {
						DettaglioAffittiView div_comp = (DettaglioAffittiView)PlatformUI.getWorkbench()
																					 	  .getActiveWorkbenchWindow()
																					 	  .getActivePage()
																					 	  .showView(DettaglioAffittiView.ID,
																				        		   	 String.valueOf(afM.getCodAffitti()) + "Comp",
																				        		   	 IWorkbenchPage.VIEW_CREATE);
						AffittiDAO a_compDAO = new AffittiDAO();
						Affitti affitto_comp = a_compDAO.getAffittoByID(afM.getCodAffitti());
						
						div_comp.setAffitto(affitto_comp);
						div_comp.setCompareView(false);
						
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
							
				
			}else{
				MessageDialog.openError(PlatformUI.getWorkbench()
												  .getActiveWorkbenchWindow()
												  .getShell(), 
										"Errore salvataggio affitto",
										"La data di inizio deve essere precedente rispetto alla data di fine");
			}
			
		}
		
	}


}
