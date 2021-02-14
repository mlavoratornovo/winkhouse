package winkhouse.view.listener;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

import winkhouse.util.DBLogManager;
import winkhouse.view.db.DBMonitorView;

public class DBMonitorListener implements IPartListener {

	public DBMonitorListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void partActivated(IWorkbenchPart part) {
		try {
			if (part instanceof DBMonitorView) {
				DBLogManager.getInstance().setGuiUpdaterMethod(part.getClass().getMethod("updateconsole", String.class));
				DBLogManager.getInstance().setUpdaterObj(part);
				DBLogManager.getInstance().flushLogByUpdMethod();				
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		if (part instanceof DBMonitorView) {
			DBLogManager.getInstance().setGuiUpdaterMethod(null);
			DBLogManager.getInstance().setUpdaterObj(null);
		}
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

}
