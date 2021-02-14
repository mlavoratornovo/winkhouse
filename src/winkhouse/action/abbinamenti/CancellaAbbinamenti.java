package winkhouse.action.abbinamenti;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.dao.AbbinamentiDAO;
import winkhouse.model.AbbinamentiModel;
import winkhouse.view.common.AbbinamentiView;



public class CancellaAbbinamenti extends Action {

	public CancellaAbbinamenti() {
		super();
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getImageDescriptor("icons/edittrash.png");
	}

	@Override
	public String getText() {
		return "Cancella abbinamenti selezionati";
	}	

	@Override
	public void run() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		ProgressMonitorDialog pmd = new ProgressMonitorDialog(window.getShell());
		IRunnableWithProgress irwpAddAbbinamentiAnagrafiche = new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, 
															 InterruptedException {
				
				AbbinamentiView av = (AbbinamentiView)PlatformUI.getWorkbench()
																.getActiveWorkbenchWindow()
																.getActivePage()
																.findView(AbbinamentiView.ID);

				StructuredSelection ss = (StructuredSelection)av.getTvAnagrafiche().getSelection();
				Iterator it = ss.iterator();
				AbbinamentiDAO aDAO = new AbbinamentiDAO();
				
				ArrayList al = null;
				while (it.hasNext()){
					AbbinamentiModel am = (AbbinamentiModel)it.next();
					aDAO.deleteAbbinamenti(am.getCodAbbinamento(), null, true);
				}
				if (av.getImmobile() != null){
					al = (ArrayList)aDAO.findAbbinamentiByCodImmobile(AbbinamentiModel.class.getName(), 
																	  av.getImmobile().getCodImmobile());
				}
				if (av.getAnagrafica() != null){
					al = (ArrayList)aDAO.findAbbinamentiByCodAnagrafica(AbbinamentiModel.class.getName(), 
						    											av.getAnagrafica().getCodAnagrafica());					
				}
				av.setManuale(al);

			}
		};
		
		try {
			pmd.run(false, true, irwpAddAbbinamentiAnagrafiche);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			MessageBox mb = new MessageBox(window.getShell(),SWT.ERROR);
			mb.setText("impossibile cancellare tutti gli abbinamenti");
			mb.setMessage("impossibile cancellare tutti gli abbinamenti");			
			mb.open();							

		} catch (InterruptedException e) {
			e.printStackTrace();
			MessageBox mb = new MessageBox(window.getShell(),SWT.ERROR);
			mb.setText("impossibile cancellare tutti gli abbinamenti");
			mb.setMessage("impossibile cancellare tutti gli abbinamenti");			
			mb.open();																	
		}
	}


}
