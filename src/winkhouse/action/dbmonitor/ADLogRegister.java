package winkhouse.action.dbmonitor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.Activator;
import winkhouse.db.server.HSQLDBHelper;
import winkhouse.view.db.DBMonitorView;

public class ADLogRegister extends Action {

	private ImageDescriptor noconnect = Activator.getImageDescriptor("icons/connect_no.png");
	private ImageDescriptor connect = Activator.getImageDescriptor("icons/connect_established.png");
	
	public final static String CONNECT_TOOLTIP = "Connettiti al log del database";
	public final static String NO_CONNECT_TOOLTIP = "Disconnetti dal log del database";
	
	public ADLogRegister(String text, int style) {
		super(text, style);
		setImageDescriptor(noconnect);
		setToolTipText(text);		
	}

	@Override
	public void run() {
		DBMonitorView mv = (DBMonitorView)PlatformUI.getWorkbench()
				  						  			.getActiveWorkbenchWindow()
				  						  			.getActivePage()
				  						  			.findView(DBMonitorView.ID);
		
		if (isChecked()){
			
			setImageDescriptor(connect);
			setToolTipText(NO_CONNECT_TOOLTIP);
			HSQLDBHelper.getInstance().setDoLog(true); 
			
		}else{
			
			setImageDescriptor(noconnect);
			setToolTipText(CONNECT_TOOLTIP);
			HSQLDBHelper.getInstance().setDoLog(false);			
			
		}
		
	}
	
	


}
