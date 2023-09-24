package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;

import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.DaAValueProvider;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class BaseDaValueProvider extends DaAValueProvider {

	public BaseDaValueProvider() {}

	@Override
	public IDaAValueObject getDaAValueObject(ICriteriaTableModel ctm) {
		if (ctm.getDaValue() != null){
			for (IDaAValueObject davo : getDaAValueObjects()) {
				if (davo.getCodValue() == ctm.getDaValue().getCodValue()){
					return davo;
				}
			}
		}
		return null;
	}

	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {

		return null;
	}

	@Override
	public boolean canProvide(Integer entityOwner,
			ICriteriaTableModel criteriaTableModel) {

		return false;
	}

}
