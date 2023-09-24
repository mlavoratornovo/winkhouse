package winkhouse.action.dbmonitor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import winkhouse.db.server.BackUpHelper;

public class BackUpAction extends Action {

	public static String ID = "winkhouse.backupaction";
	
	public BackUpAction() {
		setId(ID);
	}

	public BackUpAction(String text) {
		super(text);
		setId(ID);
	}

	public BackUpAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	public BackUpAction(String text, int style) {
		super(text, style);
		setId(ID);
	}


	@Override
	public void run() {
		BackUpHelper uDBHelper = new BackUpHelper();
		uDBHelper.executeBackUp();
	}

}
