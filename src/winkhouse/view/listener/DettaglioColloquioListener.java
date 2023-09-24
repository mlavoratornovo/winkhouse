package winkhouse.view.listener;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

public class DettaglioColloquioListener implements IPartListener {

	@Override
	public void partActivated(IWorkbenchPart part) {


	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {


	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		DettaglioImmobiliListener dil = new DettaglioImmobiliListener();
		dil.partClosed(part);
		DettaglioAnagraficaListener dal = new DettaglioAnagraficaListener();
		dal.partClosed(part);

	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		

	}

	@Override
	public void partOpened(IWorkbenchPart part) {


	}

}
