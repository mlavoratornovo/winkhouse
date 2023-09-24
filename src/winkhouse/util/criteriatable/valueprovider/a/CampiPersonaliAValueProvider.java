package winkhouse.util.criteriatable.valueprovider.a;

import winkhouse.model.AttributeModel;
import winkhouse.util.criteriatable.valueprovider.da.CampiPersonaliDaAValueProvider;
import winkhouse.util.criteriatable.valueprovider.da.TextDaValueProvider;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class CampiPersonaliAValueProvider extends CampiPersonaliDaAValueProvider {

	public CampiPersonaliAValueProvider(){
		super();
	}

	@Override
	public IDaAValueObject getDaAValueObject(ICriteriaTableModel ctm) {
		if (ctm.getDaValue() == null){
			
			TextDaValueProvider tdavp = new TextDaValueProvider();
			AttributeModel am = attributeDAO.getAttributeByID(Integer.valueOf(ctm.getPropertiesFieldDescriptior().getMethodName()));
			TextDaValueProvider.TextDaAValue tdav = null;
			if (am.getFieldType().equalsIgnoreCase(String.class.getName())){
				tdav = tdavp.new TextDaAValue("");
			}
			if (am.getFieldType().equalsIgnoreCase(Integer.class.getName())){
				tdav = tdavp.new TextDaAValue("0");
			}
			if (am.getFieldType().equalsIgnoreCase(Double.class.getName())){
				tdav = tdavp.new TextDaAValue("0.0");
			}
			return tdav;
		}
		return ctm.getAValue();	}


	
}
