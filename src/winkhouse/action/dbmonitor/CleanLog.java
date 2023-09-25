package winkhouse.action.dbmonitor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.db.DBMonitorView;

public class CleanLog extends Action {

	public CleanLog(String text, ImageDescriptor image) {
		super(text, image);
		setToolTipText("Pulisci il contenuto mostrato nel log");
	}

	@Override
	public void run() {
		
		DBMonitorView mv = (DBMonitorView)PlatformUI.getWorkbench()
				  						  			.getActiveWorkbenchWindow()
				  						  			.getActivePage()
				  						  			.findView(DBMonitorView.ID);
		
		mv.getConsole().setText("");

	}


}
