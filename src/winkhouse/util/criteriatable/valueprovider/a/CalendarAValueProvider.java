package winkhouse.util.criteriatable.valueprovider.a;

import winkhouse.util.criteriatable.valueprovider.da.CalendarDaValueProvider;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class CalendarAValueProvider extends CalendarDaValueProvider {

	public CalendarAValueProvider() {
	
	}

	@Override
	public IDaAValueObject getDaAValueObject(ICriteriaTableModel ctm) {
		return ctm.getAValue();
	}

}
