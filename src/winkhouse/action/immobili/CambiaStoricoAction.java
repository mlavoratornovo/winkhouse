package winkhouse.action.immobili;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.dao.ImmobiliDAO;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.immobili.DettaglioImmobileView;


public class CambiaStoricoAction extends Action {

	public CambiaStoricoAction() {
	}

	public CambiaStoricoAction(String text) {
		super(text);
	}

	public CambiaStoricoAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public CambiaStoricoAction(String text, int style) {
		super(text, style);
	}

	@Override
	public void run() {
		DettaglioImmobileView div = (DettaglioImmobileView)PlatformUI.getWorkbench()
		 															 .getActiveWorkbenchWindow()
		 															 .getActivePage()
		 															 .getActivePart();
				
		div.getImmobile().setStorico(!div.getImmobile().isStorico());
		
//		ImmobiliDAO iDAO = new ImmobiliDAO();
//		iDAO.saveUpdate(div.getImmobile(), null, true);
		WinkhouseUtils.getInstance().getCayenneObjectContext().commitChanges();
		div.getViewSite().getPage().hideView(div);
		RefreshImmobiliAction ria = new RefreshImmobiliAction();
		ria.run();

	}

	
	
}
