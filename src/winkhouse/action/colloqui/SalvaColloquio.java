package winkhouse.action.colloqui;

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
import winkhouse.dao.ColloquiDAO;
import winkhouse.helper.ColloquiHelper;
import winkhouse.helper.EntityHelper;
import winkhouse.helper.OptimisticLockHelper;
import winkhouse.model.AttributeModel;
import winkhouse.model.ColloquiModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.colloqui.DettaglioColloquioView;
import winkhouse.view.colloqui.handler.DettaglioColloquioHandler;



public class SalvaColloquio extends Action {

	public SalvaColloquio() {
	}

	public SalvaColloquio(String text) {
		super(text);
	}

	public SalvaColloquio(String text, ImageDescriptor image) {
		super(text, image);
	}

	public SalvaColloquio(String text, int style) {
		super(text, style);
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getImageDescriptor("icons/document-save.png");
	}

	@Override
	public String getText() {
		return "Salva colloquio";
	}

	@Override
	public void run() {
		DettaglioColloquioView dcv = null;
		try {
			dcv = (DettaglioColloquioView)PlatformUI.getWorkbench()
					  								.getActiveWorkbenchWindow()
					  								.getActivePage()
					  								.getActivePart();
		} catch (Exception e) {
			dcv = null;
		}
		if (dcv != null){
			ColloquiModel cm = dcv.getColloquio() ;
			if (cm != null){
				ColloquiHelper ch = new ColloquiHelper();
				
				cm.setDateUpdate(new Date());
				if (WinkhouseUtils.getInstance().getLoggedAgent() != null){
					cm.setCodUserUpdate(WinkhouseUtils.getInstance().getLoggedAgent().getCodAgente());
				}
				OptimisticLockHelper olh = new OptimisticLockHelper();
				String decision = olh.checkOLColloquio(cm);
				
				if (decision.equalsIgnoreCase(OptimisticLockHelper.SOVRASCRIVI)){
				
				
					if (ch.saveColloquio(cm)){
						
						EntityHelper eh = new EntityHelper();
						
						for (AttributeModel attribute : cm.getAttributes()){
							if (attribute.getValue(cm.getCodColloquio()) != null){
								attribute.getValue(cm.getCodColloquio()).setIdObject(cm.getCodColloquio());
								if (!eh.saveAttributeValue(attribute.getValue(cm.getCodColloquio()))){
									MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
															"Errore salvataggio", 
															"Errore durante il salvataggio del campo " + attribute.getAttributeName());
								}
							}
						}
											
						MessageBox mb = new MessageBox(dcv.getSite().getShell(),SWT.ICON_INFORMATION);
						mb.setText("Salvataggio");
						mb.setMessage("Salvataggio colloquio eseguito corretamente");			
						mb.open();			
						cm.resetCache();
						dcv.setColloquio(cm);
						
						PlatformUI.getWorkbench()
								  .getActiveWorkbenchWindow()
								  .getActivePage()
								  .hideView(dcv);
						
						
						DettaglioColloquioHandler.getInstance().getDettaglioColloquio(cm);
						
						RefreshColloqui rc = new RefreshColloqui();
						rc.run();
						
					}else{
						MessageBox mb = new MessageBox(dcv.getSite().getShell(),SWT.ERROR);
						mb.setText("Errore salvataggio");
						mb.setMessage("Salvataggio colloquio non eseguito");			
						mb.open();					
					}
					
				}else if (decision.equalsIgnoreCase(OptimisticLockHelper.VISUALIZZA)){
					
					try {
						DettaglioColloquioView div_comp = (DettaglioColloquioView)PlatformUI.getWorkbench()
																					 	  	.getActiveWorkbenchWindow()
																					 	  	.getActivePage()
																					 	  	.showView(DettaglioColloquioView.ID,
																				        		   	  String.valueOf(cm.getCodColloquio()) + "Comp",
																				        		   	  IWorkbenchPage.VIEW_CREATE);
						ColloquiDAO c_compDAO = new ColloquiDAO();
						ColloquiModel colloqui_comp = (ColloquiModel)c_compDAO.getColloquioById(ColloquiModel.class.getName(), cm.getCodColloquio());
						
						div_comp.setColloquio(colloqui_comp);
						div_comp.setCompareView(false);
						
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
				
			}else{
				MessageBox mb = new MessageBox(dcv.getSite().getShell(),SWT.ERROR);
				mb.setText("Errore salvataggio");
				mb.setMessage("Ops!! Errore inaspettato durante il salvataggio colloquio");			
				mb.open();									
			}
		}else{
			MessageBox mb = new MessageBox(dcv.getSite().getShell(),SWT.ERROR);
			mb.setText("Errore salvataggio");
			mb.setMessage("Ops!! Errore inaspettato durante il salvataggio colloquio");			
			mb.open();								
		}
	}


}
