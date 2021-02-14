package winkhouse.action.immobili;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.view.immobili.ImmobiliTreeView;

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
		ImmobiliTreeView itv = (ImmobiliTreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow()
								 										  .getActivePage()
								 										  .getActivePart();
		if (isChecked()){
			itv.setGeogrouping(true);
			itv.getViewer().setInput(new Object());
		}else{
			itv.setGeogrouping(false);
			itv.getViewer().setInput(new Object());
		}
	}

	
}
