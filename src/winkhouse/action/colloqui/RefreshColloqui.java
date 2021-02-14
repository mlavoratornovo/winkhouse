package winkhouse.action.colloqui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.common.ColloquiView;


public class RefreshColloqui extends Action {

	public RefreshColloqui() {

	}

	public RefreshColloqui(String text) {
		super(text);

	}

	public RefreshColloqui(String text, ImageDescriptor image) {
		super(text, image);

	}

	public RefreshColloqui(String text, int style) {
		super(text, style);

	}

	@Override
	public void run() {
		
		ColloquiView cv = null;
		
			
		IViewReference vr = PlatformUI.getWorkbench()
	   					  			  .getActiveWorkbenchWindow()
	   					  			  .getActivePage()
	   					  			  .findViewReference(ColloquiView.ID);

		if (vr != null){
			cv = (ColloquiView)vr.getView(true);
			
			if (cv.getImmobile() != null){
				cv.getImmobile().setColloqui(null);
				cv.setImmobile(cv.getImmobile());
			}
			
			if (cv.getAnagrafica() != null){
				cv.getAnagrafica().setColloqui(null);
				cv.setAnagrafica(cv.getAnagrafica());
			}
		}
	}

}
