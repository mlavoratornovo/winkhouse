package winkhouse.action.anagrafiche;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.immobili.AnagrafichePropietarieView;

public class RefreshAnagrafichePropietarieAction extends Action {

	public RefreshAnagrafichePropietarieAction() {
		// TODO Auto-generated constructor stub
	}

	public RefreshAnagrafichePropietarieAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public RefreshAnagrafichePropietarieAction(String text,
			ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public RefreshAnagrafichePropietarieAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void run() {
		
		AnagrafichePropietarieView dav = (AnagrafichePropietarieView)PlatformUI.getWorkbench()
				   															   .getActiveWorkbenchWindow()
				   															   .getActivePage()
				   															   .getActivePart();
		
		//dav.getImmobile().setAnagrafichePropietarie(null);
		dav.getTvAnagrafichePropietarie().refresh();
		
	}

	
}
