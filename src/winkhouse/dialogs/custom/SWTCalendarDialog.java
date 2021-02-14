package winkhouse.dialogs.custom;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.vafada.swtcalendar.SWTCalendar;
import org.vafada.swtcalendar.SWTCalendarListener;

public class SWTCalendarDialog extends DialogCellEditor{	private Image buttonImage= null;	private String tootTipButton = null;	private Shell shell;    private SWTCalendar swtcal;    private Display display;	public SWTCalendarDialog() {	}	public SWTCalendarDialog(Composite parent) {		super(parent);	}	public SWTCalendarDialog(Composite parent, int style) {		super(parent, style);	}	public Image getButtonImage() {		return buttonImage;	}	public void setButtonImage(Image buttonImage) {		this.buttonImage = buttonImage;	}	public String getTootTipButton() {		return tootTipButton;	}	public void setTootTipButton(String tootTipButton) {		this.tootTipButton = tootTipButton;	}	
    @Override	protected Object openDialogBox(Control cellEditorWindow) {    	SWTCalendarDialog cal = new SWTCalendarDialog(cellEditorWindow.getDisplay());    	cal.open();    	//DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALIAN);    	CriteriDateFormat cdf = new CriteriDateFormat();		return cdf.format(cal.getCalendar().getTime());	}        private class CriteriDateFormat extends DateFormat{		@Override		public StringBuffer format(Date date, StringBuffer toAppendTo,								   FieldPosition fieldPosition) {			Calendar cal = Calendar.getInstance();			cal.setTime(date);			int anno  = cal.get(Calendar.YEAR);			int mese  = cal.get(Calendar.MONTH)+1;			int giorno  = cal.get(Calendar.DAY_OF_MONTH);			toAppendTo.append(anno);			toAppendTo.append('-');			toAppendTo.append((mese < 10)? "0" + mese:mese);			toAppendTo.append('-');			toAppendTo.append((giorno < 10)? "0" + giorno:giorno);									return toAppendTo;		}		@Override		public Date parse(String source, ParsePosition pos) {						if ((source != null) && (!source.trim().equalsIgnoreCase(""))){				String[] data = source.split("-");				if (data.length == 3){					Calendar cal = Calendar.getInstance();					cal.set(Calendar.YEAR, Integer.valueOf(data[0]));					cal.set(Calendar.MONTH, Integer.valueOf(data[1]));					cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(data[2]));					return cal.getTime();				}else{					return new Date();				}			}else{				return new Date();			}					}    	    	    	    }

    public SWTCalendarDialog(Display display) {
        this.display = display;
        shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.CLOSE);
        shell.setLayout(new RowLayout());
        swtcal = new SWTCalendar(shell);        
    }

    public void open() {
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }

    public Calendar getCalendar() {
        return swtcal.getCalendar();
    }

    public void setDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        swtcal.setCalendar(calendar);
    }

    public void addDateChangedListener(SWTCalendarListener listener) {
        swtcal.addSWTCalendarListener(listener);
    }
}
