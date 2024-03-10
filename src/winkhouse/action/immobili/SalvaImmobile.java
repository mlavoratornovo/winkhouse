package winkhouse.action.immobili;

import java.time.ZoneId;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.action.recapiti.ApriDettaglioRecapitiAction;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.helper.EntityHelper;
import winkhouse.helper.ImmobiliHelper;
import winkhouse.helper.OptimisticLockHelper;
import winkhouse.model.AttributeModel;
import winkhouse.orm.Immobili;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.immobili.DettaglioImmobileView;
import winkhouse.view.immobili.ImmaginiImmobiliView;
import winkhouse.view.immobili.ImmobiliTreeView;
import winkhouse.view.immobili.handler.DettaglioImmobiliHandler;



public class SalvaImmobile extends Action {

	public SalvaImmobile() {}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getImageDescriptor("icons/document-save.png");
	}

	@Override
	public String getText() {
		return "Salva immobile";
	}

	@Override
	public void run() {
//		try {
			DettaglioImmobileView div = (DettaglioImmobileView)PlatformUI.getWorkbench()
			 															 .getActiveWorkbenchWindow()
			 															 .getActivePage()
			 															 .getActivePart();
			if (
				(div.getImmobile().getAnagrafiche()!= null) &&				
				(
				 (
				  (div.getImmobile().getAnagrafiche().getCognome() != null) && 
				  (!div.getImmobile().getAnagrafiche().getCognome().equalsIgnoreCase("")) 
				  ) ||
				 (
				  (div.getImmobile().getAnagrafiche().getNome() != null) &&
				  (!div.getImmobile().getAnagrafiche().getNome().equalsIgnoreCase(""))
				 )
				) &&
				((!div.getImmobile().getCitta().equalsIgnoreCase("")) &&
				 (!div.getImmobile().getIndirizzo().equalsIgnoreCase(""))
				)
			   ){
				
				EntityHelper eh = new EntityHelper();
				ImmobiliHelper ih = new ImmobiliHelper();
				OptimisticLockHelper olh = new OptimisticLockHelper();
				
				Immobili immobile = div.getImmobile();
				immobile.setDateupdate(new Date().toInstant()
					      .atZone(ZoneId.systemDefault())
					      .toLocalDateTime());
				if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
					immobile.setAgenti1(WinkhouseUtils.getInstance().getLoggedAgent());
				}
				
				String decision = olh.checkOLImmobile(immobile);
				
				if (decision.equalsIgnoreCase(OptimisticLockHelper.SOVRASCRIVI)){
					boolean isnew = (immobile.getCodImmobile() == 0)?true:false;
					WinkhouseUtils.getInstance().getCayenneObjectContext().commitChanges();
					WinkhouseUtils.getInstance().setCodiciImmobili(null);
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
												  "Salvataggio immobile",
												  "Salvataggio eseguito");
					
//					if (ih.saveImmobile(immobile)){
//						for (AttributeModel attribute : immobile.getAttributes()){
//							if (attribute.getValue(immobile.getCodImmobile()) != null){
//								attribute.getValue(immobile.getCodImmobile()).setIdObject(immobile.getCodImmobile());
//								if (!eh.saveAttributeValue(attribute.getValue(immobile.getCodImmobile()))){
//									MessageDialog.openError(div.getSite().getShell(), 
//															"Errore salvataggio", 
//															"Errore durante il salvataggio del campo " + attribute.getAttributeName());
//								}
//							}
//						}
//						immobile.setAllegati(null);
//						immobile.setStanze(null);
//						if (isnew){
//							PlatformUI.getWorkbench()
//									  .getActiveWorkbenchWindow()
//									  .getActivePage()
//									  .hideView(div);
//							
//							div = DettaglioImmobiliHandler.getInstance()
//													      .getDettaglioImmobile(immobile);
//						}
//						div.setImmobile(immobile);
//						
//						ImmaginiImmobiliView iiv = (ImmaginiImmobiliView)PlatformUI.getWorkbench()
//																				   .getActiveWorkbenchWindow()
//																				   .getActivePage()
//																				   .findView(ImmaginiImmobiliView.ID);
//						if (iiv != null){
//							iiv.setImmobile(immobile);
//						}
//						WinkhouseUtils.getInstance().setCodiciImmobili(null);
//						MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
//													  "Salvataggio immobile",
//													  "Salvataggio eseguito");
//					}else{
//						MessageBox mb = new MessageBox(div.getSite().getShell(),SWT.ERROR);
//						mb.setText("Errore salvataggio");
//						mb.setMessage("Si è verificato un errore nel salvataggio dell'immobile ");			
//						mb.open();
//	
//					}
				}else if (decision.equalsIgnoreCase(OptimisticLockHelper.VISUALIZZA)){
					
					try {
						DettaglioImmobileView div_comp = (DettaglioImmobileView)PlatformUI.getWorkbench()
																					 	  .getActiveWorkbenchWindow()
																					 	  .getActivePage()
																					 	  .showView(DettaglioImmobileView.ID,
																				        		   	 String.valueOf(immobile.getCodImmobile()) + "Comp",
																				        		   	 IWorkbenchPage.VIEW_CREATE);
						ImmobiliDAO i_compDAO = new ImmobiliDAO();
						Immobili immobile_comp = (Immobili)i_compDAO.getImmobileById(immobile.getCodImmobile());
						
						div_comp.setImmobile(immobile_comp);
						div_comp.setCompareView(false);
						
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			}else{
				MessageBox mb = new MessageBox(div.getSite().getShell(),SWT.ERROR);
				mb.setText("Errore salvataggio");
				mb.setMessage("Inserire cognome e nome anagrafica proprietaria \ne inserire città e indirizzo immobile ");			
				mb.open();
			}
			ImmobiliTreeView itv = (ImmobiliTreeView)PlatformUI.getWorkbench()
			 		  						 				   .getActiveWorkbenchWindow()
			 		  						                   .getActivePage()
			 		  						                   .findView(ImmobiliTreeView.ID);
			itv.getViewer().refresh();
			
	/*	} catch (PartInitException e) {
			e.printStackTrace();
		}*/
	}

	
}
