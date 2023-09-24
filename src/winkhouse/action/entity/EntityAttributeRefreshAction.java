package winkhouse.action.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.dao.AttributeDAO;
import winkhouse.model.AttributeModel;
import winkhouse.view.common.EAVView;

public class EntityAttributeRefreshAction extends Action{

	public EntityAttributeRefreshAction(String text, ImageDescriptor image) {
		super(text,image);
	}
	
	@Override
	public void run() {
		
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(PlatformUI.getWorkbench()
			    														.getActiveWorkbenchWindow()
			    														.getShell());
		try {
			pmd.run(false, false, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,InterruptedException {
					EAVView eav = null;

					eav = (EAVView)PlatformUI.getWorkbench()
											 .getActiveWorkbenchWindow()
											 .getActivePage()			
											 .findView(EAVView.ID);
					
					ArrayList<AttributeModel> ats = eav.getAttributes();
					if (ats.size() > 0){
						Integer idEntity = ((AttributeModel)ats.get(0)).getIdClassEntity();
						ats = new AttributeDAO().getAttributeByIdEntity(idEntity);
						eav.setAttributes(ats, eav.getInstanceID());
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
