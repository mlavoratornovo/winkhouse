package winkhouse.action.agenda;

import java.util.Calendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import winkhouse.view.agenda.CalendarioView;

public class NextMonthAction extends Action {

	public NextMonthAction() {
	}

	public NextMonthAction(String text, ImageDescriptor image) {
		super(text, image);
		setToolTipText("Avanza di un mese");
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
			c.add(Calendar.MONTH, 1);			
			cv.setDaysLabel(c);
			
			Calendar c1 = Calendar.getInstance();
			c1.setTime(cv.getDataDASearch());
			c1.add(Calendar.MONTH, 1);
			cv.setDataSearch(c1);
		}

	}
}
