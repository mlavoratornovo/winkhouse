package winkhouse.action.recapiti;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.AnagraficheModel;
import winkhouse.model.ContattiModel;
import winkhouse.view.common.RecapitiView;

public class CancellaRecapitoAction extends Action {

	public CancellaRecapitoAction() {
		// TODO Auto-generated constructor stub
	}

	public CancellaRecapitoAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public CancellaRecapitoAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public CancellaRecapitoAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void run() {
		
		RecapitiView rv = null;
		
		rv = (RecapitiView)PlatformUI.getWorkbench()
									 .getActiveWorkbenchWindow()
				  					 .getActivePage()
				  					 .getActivePart();
		
		if (rv.getTvRecapiti().getSelection() != null){
			
			Iterator it = ((StructuredSelection)rv.getTvRecapiti().getSelection()).iterator();
			
			while (it.hasNext()) {
				
				ContattiModel cModel = (ContattiModel)it.next();
//				for (AnagraficheModel anagrafica : rv.getAnagrafiche()) {
//					if (anagrafica.getCodAnagrafica() == cModel.getCodAnagrafica()){
//						anagrafica.getContatti().remove(cModel);
//					}
//				}
				
			}
			rv.getTvRecapiti().refresh();
		}		
		
	}

	
}
