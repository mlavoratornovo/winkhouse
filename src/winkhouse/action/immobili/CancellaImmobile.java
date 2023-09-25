package winkhouse.action.immobili;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.helper.ImmobiliHelper;
import winkhouse.model.ImmobiliModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.immobili.DettaglioImmobileView;



public class CancellaImmobile extends Action {

	private ImmobiliModel immobile = null;
	
	public CancellaImmobile() {}

	public CancellaImmobile(ImmobiliModel immobile) {
		this.immobile = immobile;
	}
	@Override
	
public ImageDescriptor getImageDescriptor() {
		return Activator.getImageDescriptor("icons/edittrash.png");
	}

	@Override
	public String getText() {
		return "Cancella immobile";
	}

	@Override
	public void run() {
			
			if (immobile == null){
				DettaglioImmobileView div = (DettaglioImmobileView)PlatformUI.getWorkbench()
						  													 .getActiveWorkbenchWindow()
						  													 .getActivePage()
						  													 .getActivePart();
				
				if (MessageDialog.openConfirm(PlatformUI.getWorkbench()
														.getActiveWorkbenchWindow()
														.getShell(), 
											  "Cancellazione immobile", 
											  "La cancellazione elimina in modo permanente tutti i dati relativi all'immobile. \n" +
											  "Per evitare la cancellazione permanente � possibile spostare l'immobile nell'archivo storico. \n" +
			  	  							  "Per procedere con la cancellazione permanente premere OK altrimenti premere Cancel ")){
	
					ImmobiliModel immobile = div.getImmobile();
		
					ImmobiliHelper ih = new ImmobiliHelper();
					if ((Boolean)ih.deleteImmobile(immobile,null).get(ImmobiliHelper.RESULT_DELETE_IMMOBILE_DATA_DB)){
						PlatformUI.getWorkbench()
								  .getActiveWorkbenchWindow()
								  .getActivePage()
								  .hideView(div);
						RefreshImmobiliAction ria = new RefreshImmobiliAction();
						ria.run();
						WinkhouseUtils.getInstance().setCodiciImmobili(null);
					}
					
				}
				
			}else{
				
				if (MessageDialog.openConfirm(PlatformUI.getWorkbench()
														.getActiveWorkbenchWindow()
														.getShell(), 
											  "Cancellazione immobile", 
											  "La cancellazione elimina in modo permanente tutti i dati relativi all'immobile. \n" +
											  "Per evitare la cancellazione permanente � possibile spostare l'immobile nell'archivo storico. \n" +
											  "Per procedere con la cancellazione permanente premere OK altrimenti premere Cancel ")){

					ImmobiliHelper ih = new ImmobiliHelper();
					if ((Boolean)ih.deleteImmobile(immobile,null).get(ImmobiliHelper.RESULT_DELETE_IMMOBILE_DATA_DB)){
						RefreshImmobiliAction ria = new RefreshImmobiliAction();
						ria.run();
						WinkhouseUtils.getInstance().setCodiciImmobili(null);
					}
				}
				
			}
		
	}

}
