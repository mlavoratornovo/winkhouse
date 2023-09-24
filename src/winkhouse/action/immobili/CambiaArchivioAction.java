package winkhouse.action.immobili;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import winkhouse.util.WinkhouseUtils;
import winkhouse.view.immobili.DettaglioImmobileView;
import winkhouse.view.immobili.ImmobiliTreeView;


public class CambiaArchivioAction extends Action {

	public CambiaArchivioAction() {
	}

	public CambiaArchivioAction(String text) {
		super(text);
	}

	public CambiaArchivioAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public CambiaArchivioAction(String text, int style) {
		super(text, style);
	}

	@Override
	public void run() {
		if (isChecked()){
			WinkhouseUtils.getInstance().setTipoArchivio(WinkhouseUtils.ARCHIVIO_STORICO);
			setToolTipText("Visualizza archivio corrente");
		}else{
			WinkhouseUtils.getInstance().setTipoArchivio(WinkhouseUtils.ARCHIVIO_CORRENTE);
			setToolTipText("Visualizza archivio storico");
		}
		IViewReference[] ivr = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								                        .getActivePage()
								                        .getViewReferences();
		
		for (int i = 0; i < ivr.length; i++) {
			if (ivr[i].getId().equalsIgnoreCase(DettaglioImmobileView.ID)){
				PlatformUI.getWorkbench()
		 		  		  .getActiveWorkbenchWindow()
		 		  		  .getActivePage().hideView(ivr[i]);
				
			}
			if (ivr[i].getId().equalsIgnoreCase(ImmobiliTreeView.ID)){
				RefreshImmobiliAction ria = new RefreshImmobiliAction();
				ria.run();				
			}
		}
	}
	
}
