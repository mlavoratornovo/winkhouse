package winkhouse.action.agenda;

import java.util.Calendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.agenda.CalendarioView;

public class NextYearAction extends Action {

	public NextYearAction() {
	}

	public NextYearAction(String text, ImageDescriptor image) {
		super(text, image);
		setToolTipText("Avanti di un anno");
	}

	@Override
	public void run() {
		CalendarioView cv = (CalendarioView)PlatformUI.getWorkbench()
						  							  .getActiveWorkbenchWindow()
						  							  .getActivePage()
						  							  .getActivePart();
		if (cv != null){
		
			Calendar c = Calendar.getInstance();
			c.setTime(cv.getDataDASearch());
			c.add(Calendar.YEAR, 1);			
			cv.setDaysLabel(c);
			
			Calendar c1 = Calendar.getInstance();
			c1.setTime(cv.getDataDASearch());
			c1.add(Calendar.YEAR, 1);
			cv.setDataSearch(c1);
		}
	}


}
