package winkhouse.util.criteriatable.valueprovider.a;

import winkhouse.util.criteriatable.valueprovider.da.TextDaValueProvider;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class TextAValueProvider extends TextDaValueProvider {

	public TextAValueProvider() {}

	@Override
	public IDaAValueObject getDaAValueObject(ICriteriaTableModel ctm) {
		return ctm.getAValue();
	}
	
	

}
