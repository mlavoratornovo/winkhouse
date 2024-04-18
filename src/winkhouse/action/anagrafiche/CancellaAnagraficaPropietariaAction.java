package winkhouse.action.anagrafiche;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

import winkhouse.model.AnagraficheModel;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Immobili;
import winkhouse.orm.Immobilipropietari;
import winkhouse.view.immobili.AnagrafichePropietarieView;

public class CancellaAnagraficaPropietariaAction extends Action {

	public CancellaAnagraficaPropietariaAction() {
		// TODO Auto-generated constructor stub
	}

	public CancellaAnagraficaPropietariaAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public CancellaAnagraficaPropietariaAction(String text,
			ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public CancellaAnagraficaPropietariaAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void run() {
	
		AnagrafichePropietarieView dav = (AnagrafichePropietarieView)PlatformUI.getWorkbench()
																			   .getActiveWorkbenchWindow()
																			   .getActivePage()
																			   .getActivePart();

		Immobili immobili = dav.getImmobile();
		StructuredSelection ss = (StructuredSelection)dav.getTvAnagrafichePropietarie().getSelection();
		
		Iterator it = ss.iterator();
		while (it.hasNext()){
			Immobilipropietari am = (Immobilipropietari)it.next();
			immobili.removeFromImmobilipropietaris(am);
			
		}
		dav.getTvAnagrafichePropietarie().refresh();
		
	}

	
	
}
