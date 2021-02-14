package winkhouse.action.recapiti;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.common.RecapitiView;


public class RefreshRecapitiAction extends Action {

	public RefreshRecapitiAction() {}

	public RefreshRecapitiAction(String text) {
		super(text);
	}

	public RefreshRecapitiAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public RefreshRecapitiAction(String text, int style) {
		super(text, style);
	}

	@Override
	public void run() {
		RecapitiView rv = null;
		
		rv = (RecapitiView)PlatformUI.getWorkbench()
									 .getActiveWorkbenchWindow()
				  					 .getActivePage()
				  					 .getActivePart();
		
		//rv.setAnagrafiche(null);
		
		ApriDettaglioRecapitiAction adra = new ApriDettaglioRecapitiAction(rv.getAnagrafiche(),true);
		adra.run();
		
//		rv.setAnagrafica(rv.getAnagrafica());

	}

}
