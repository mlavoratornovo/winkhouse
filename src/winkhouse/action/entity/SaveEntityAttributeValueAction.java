package winkhouse.action.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.helper.EntityHelper;
import winkhouse.model.AttributeModel;
import winkhouse.view.common.EAVView;

public class SaveEntityAttributeValueAction extends Action {

	public SaveEntityAttributeValueAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	@Override
	public void run() {
		
		
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench()
																	    .getActiveWorkbenchWindow()
																	    .getShell());
		try {
			pmd.run(false, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
																 InterruptedException {
					EAVView eav = null;
					
					eav = (EAVView)PlatformUI.getWorkbench()
													  .getActiveWorkbenchWindow()
								  					  .getActivePage()
								  					  .findView(EAVView.ID);
					
					EntityHelper eh = new EntityHelper();
 					ArrayList<AttributeModel> al = eav.getAttributes();
 					
 					if ((eav.getInstanceID()!= null) && (eav.getInstanceID()!= 0)){ 
	 					if (al != null){
							for (AttributeModel attribute : eav.getAttributes()){
								if (attribute.getValue(eav.getInstanceID()) != null){
									if (!eh.saveAttributeValue(attribute.getValue(eav.getInstanceID()))){
										MessageDialog.openError(eav.getSite().getShell(), 
																"Errore salvataggio", 
																"Errore durante il salvataggio del campo " + attribute.getAttributeName());
									}
								}
							}
							eav.getTvAttributes().refresh();
	 					}
 					}else{
 						MessageDialog.openError(eav.getSite().getShell(), 
 												"Errore salvataggio", 
 												"ID oggetto mancante. \n" +
 												"Salvare prima l'immobile, l'anagrafica, il colloquio o l'affitto aperti in dettalio");
 					}
					
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

		
		
	}

	
	
}
