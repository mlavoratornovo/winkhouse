package winkhouse.util.criteriatable.celleditors.a;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.eclipse.swt.widgets.Composite;

import winkhouse.widgets.data.ICriteriaTableModel;

public class CriteriaSWTCalendarCellEditor
		extends
		winkhouse.util.criteriatable.celleditors.da.CriteriaSWTCalendarCellEditor {

	public CriteriaSWTCalendarCellEditor(Composite composite, String data) {
		super(composite, data);
	}

	@Override
	protected Calendar setSWTCalendarDate(ICriteriaTableModel criteriaTableModel) {
		
		Calendar c = Calendar.getInstance(Locale.ITALIAN);
		
		if (criteriaTableModel.getAValue() != null && criteriaTableModel.getAValue().getCodValue() != null &&
			!criteriaTableModel.getAValue().getCodValue().equalsIgnoreCase("")){
			
			this.setCalendar(c);
			try {
				Date d = formatterENG.parse(criteriaTableModel.getAValue().getCodValue());
				c.setTime(d);
			} catch (ParseException e) {
				c.setTime(new Date());
			}
			
		}else{
			c.setTime(new Date());
		}
		
		return c;
		
	}

	
}
