package winkhouse.dialogs.custom;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import winkhouse.Activator;

public class SWTCalendarCellEditor extends DialogCellEditor {

	Calendar calendar = null;
	
	public SWTCalendarCellEditor(Composite parent,Calendar c) {
		super(parent);
		this.calendar = c;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		
		SWTCaledar cal = new SWTCaledar(cellEditorWindow);
		cal.setDate(calendar);
  		cal.open();
		return cal.getReturnValue();
	}

	
	protected class SWTCaledar{
		
		private Image winImage= Activator.getImageDescriptor("icons/calendario.png").createImage();
		private Shell shell;	
	    
	    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    private DateTime dt = null;
	    private Display display;
		private Calendar returnCalendar = null;
		
		public SWTCaledar(Control c){
						
			display = c.getShell().getDisplay();
				
			shell = new Shell(display,SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			shell.setBounds(display.getCursorLocation().x,
							display.getCursorLocation().y,190,170);
			
			shell.setLayout(new GridLayout());
			shell.setBackground(new Color(null,255,255,255));
	        shell.setImage(winImage);
	        
	        dt = new DateTime(shell, SWT.CALENDAR);
	        dt.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	        dt.addSelectionListener(new SelectionListener(
	        		) {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					DateTime dt = ((DateTime)e.widget);
					Calendar c = Calendar.getInstance(Locale.ITALIAN);
					c.set(Calendar.YEAR, dt.getYear());
					c.set(Calendar.MONTH, dt.getMonth());
					c.set(Calendar.DAY_OF_MONTH, dt.getDay());
					returnCalendar = c;
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					
				}
			});
	        
		}
		
		public void open(){
			shell.open();
		    while (!shell.isDisposed()) {
		    	if (!display.readAndDispatch()) display.sleep();
		    }
		}
		
		public Calendar getReturnValue() {
			return returnCalendar;
		}
		
		public void setDate(Calendar c){
			if (c != null){
				dt.setYear(c.get(Calendar.YEAR));
				dt.setMonth(c.get(Calendar.MONDAY));
				dt.setDay(c.get(Calendar.DAY_OF_MONTH));
				returnCalendar = c;
			}
		}

	}


	public Calendar getCalendar() {
		return calendar;
	}


	public void setCalendar(Calendar c) {
		this.calendar = c;
	}

}