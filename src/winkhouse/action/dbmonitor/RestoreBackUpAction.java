package winkhouse.action.dbmonitor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.db.server.BackUpHelper;

public class RestoreBackUpAction extends Action {

	public static String ID = "winkhouse.restorebackupaction";
	
	public RestoreBackUpAction() {
		setId(ID);
	}

	public RestoreBackUpAction(String text) {
		super(text);
		setId(ID);
	}

	public RestoreBackUpAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	public RestoreBackUpAction(String text, int style) {
		super(text, style);
		setId(ID);
	}

	
	@Override
	public void run() {
		
		if (MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
			"ATTENZIONE Riavvio dopo ripristino", 
			"Per ripristinare i dati sarà necessario riavviare il programma, eseguire il ripristino e RIAVVIARE ORA ?")){
			
			BackUpHelper uDBHelper = new BackUpHelper();
			uDBHelper.restoreDBBackUp();
			PlatformUI.getWorkbench().restart();
			
		}
		
	}

	
}
