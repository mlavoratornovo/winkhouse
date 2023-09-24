package winkhouse.view.listener;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.affitti.DettaglioAffittiView;
import winkhouse.view.common.EAVView;

public class DettaglioAffittoListener implements IPartListener {

	public DettaglioAffittoListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		
		if (part instanceof DettaglioAffittiView){
			
			IViewPart ivpEAV = PlatformUI.getWorkbench()
						 				 .getActiveWorkbenchWindow()
						 				 .getActivePage() 
						 				 .findView(EAVView.ID);
			if (ivpEAV != null){
				EAVView eav = (EAVView)ivpEAV;
				eav.setAttributes(null, 0);
			}

		}

	}

//	@Override
	public void partDeactivated(IWorkbenchPart part) {
		
		if (part instanceof DettaglioAffittiView){
			
//			IViewPart ivpEAV = PlatformUI.getWorkbench()
//						 				 .getActiveWorkbenchWindow()
//						 				 .getActivePage() 
//						 				 .findView(EAVView.ID);
//			if (ivpEAV != null){
//				EAVView eav = (EAVView)ivpEAV;
//				eav.setAttributes(null, 0);
//			}

		}


	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

}
