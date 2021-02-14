package winkhouse.action.anagrafiche;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.view.anagrafica.AnagraficaTreeView;

public class GeoGroupingAction extends Action {

	public GeoGroupingAction() {
		// TODO Auto-generated constructor stub
	}

	public GeoGroupingAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public GeoGroupingAction(String text, ImageDescriptor image) {
		super(text, image);
		
	}

	public GeoGroupingAction(String text, int style) {
		super(text, style);
		setImageDescriptor(Activator.getImageDescriptor("icons/cercacomune.png"));
	}
	
	@Override
	public void run() {
		AnagraficaTreeView atv = (AnagraficaTreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								 										  		.getActivePage()
								 										  		.getActivePart();
		if (isChecked()){
			atv.setGeogrouping(true);
			atv.getViewer().setInput(new Object());
		}else{
			atv.setGeogrouping(false);
			atv.getViewer().setInput(new Object());
		}
	}

	
}
