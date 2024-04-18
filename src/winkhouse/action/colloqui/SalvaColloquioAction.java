package winkhouse.action.colloqui;

import java.time.ZoneId;
import java.util.Date;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.ColloquiDAO;
import winkhouse.helper.ColloquiHelper;
import winkhouse.helper.EntityHelper;
import winkhouse.helper.OptimisticLockHelper;
import winkhouse.model.AttributeModel;
import winkhouse.model.ColloquiModel;
import winkhouse.orm.Colloqui;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.colloqui.DettaglioColloquioView;



public class SalvaColloquioAction extends Action {

	private Colloqui cModel = null;
	public SalvaColloquioAction() {

	}
	
	public SalvaColloquioAction(Colloqui colloquio) {
		cModel = colloquio;
	}	

	public SalvaColloquioAction(String text) {
		super(text);
	}

	public SalvaColloquioAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public SalvaColloquioAction(String text, int style) {
		super(text, style);
	}

	@Override
	public void run() {
		
		if (cModel != null){
			ColloquiHelper ch = new ColloquiHelper();
			
			cModel.setDateupdate(new Date().toInstant()
				      .atZone(ZoneId.systemDefault())
				      .toLocalDateTime());
			if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
				cModel.setAgenti(WinkhouseUtils.getInstance().getLoggedAgent());
			}
			OptimisticLockHelper olh = new OptimisticLockHelper();
			String decision = olh.checkOLColloquio(cModel);
			
			if (decision.equalsIgnoreCase(OptimisticLockHelper.SOVRASCRIVI)){
			
				cModel.getEditObjectContext().commitChanges();
//				if (ch.saveColloquio(cModel)){
//					
//					EntityHelper eh = new EntityHelper();
//					
//					for (AttributeModel attribute : cModel.getAttributes()){
//						if (attribute.getValue(cModel.getCodColloquio()) != null){
//							attribute.getValue(cModel.getCodColloquio()).setIdObject(cModel.getCodColloquio());
//							if (!eh.saveAttributeValue(attribute.getValue(cModel.getCodColloquio()))){
//								MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
//														"Errore salvataggio", 
//														"Errore durante il salvataggio del campo " + attribute.getAttributeName());
//							}
//						}
//					}
//					
//					MessageDialog.openInformation(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//												  "Salvataggio colloquio", 
//												  "Salvataggio dati colloquio eseguito con successo");
//	
//				}else{
//					MessageDialog.openError(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
//											"Errore salvataggio colloquio", 
//											"Si � verificato un errore nel salvataggio del colloquio");
//	
//				}
				
			}else if (decision.equalsIgnoreCase(OptimisticLockHelper.VISUALIZZA)){
				
				try {
					DettaglioColloquioView div_comp = (DettaglioColloquioView)PlatformUI.getWorkbench()
																				 	  	.getActiveWorkbenchWindow()
																				 	  	.getActivePage()
																				 	  	.showView(DettaglioColloquioView.ID,
																			        		   	  String.valueOf(cModel.getCodColloquio()) + "Comp",
																			        		   	  IWorkbenchPage.VIEW_CREATE);
					ColloquiDAO c_compDAO = new ColloquiDAO();
					Colloqui colloqui_comp = c_compDAO.getColloquioById(cModel.getCodColloquio());
					
					div_comp.setColloquio(colloqui_comp);
					div_comp.setCompareView(false);
					
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
			
		}
		

	}
	
	

}
