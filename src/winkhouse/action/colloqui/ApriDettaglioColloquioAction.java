package winkhouse.action.colloqui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.helper.ProfilerHelper;
import winkhouse.model.ColloquiModel;
import winkhouse.util.WinkhouseUtils;
import winkhouse.view.colloqui.DettaglioColloquioView;
import winkhouse.view.colloqui.handler.DettaglioColloquioHandler;

public class ApriDettaglioColloquioAction extends Action {

	private ColloquiModel colloquio = null;	
	private boolean comparerView = false;
	
	public ApriDettaglioColloquioAction(ColloquiModel colloquio) {
		this.colloquio = colloquio;
	}

	public ApriDettaglioColloquioAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public ApriDettaglioColloquioAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public ApriDettaglioColloquioAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void run() {
		if (ProfilerHelper.getInstance().getPermessoUI(DettaglioColloquioView.ID)){
			DettaglioColloquioHandler.getInstance().setComparerView(isComparerView());
			DettaglioColloquioHandler.getInstance().getDettaglioColloquio(this.colloquio);
			DettaglioColloquioHandler.getInstance().setComparerView(!isComparerView());
		}else{
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					  					  "Controllo permessi accesso vista",
					  					  "L'agente " + WinkhouseUtils.getInstance().getLoggedAgent().toString() + 
					  					  " non ha il permesso di accedere alla vista " + 
					  					DettaglioColloquioView.ID);
		}
			
	}

	
	public boolean isComparerView() {
		return comparerView;
	}
	

	public void setComparerView(boolean comparerView) {
		this.comparerView = comparerView;
	}

	
	
}
