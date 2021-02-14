package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.AttributeDAO;
import winkhouse.model.AttributeModel;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class EnumCampiPersonaliDaValueProvider extends BaseDaValueProvider {
	
	private AttributeModel am = null;
	
	public EnumCampiPersonaliDaValueProvider() {
		
	}
	
	private class EnumValue implements IDaAValueObject{
		
		private String value = null;
		
		public EnumValue(String value){
			this.value = value;
		}
		
		@Override
		public Object getBindValue() {
			return this.value;
		}

		@Override
		public String getCodValue() {
			return this.value;
		}

		@Override
		public String getDisplayValue() {
			return this.value;
		}
		
	}

	@Override
	public IDaAValueObject getDaAValueObject(ICriteriaTableModel ctm) {
		AttributeDAO aDAO = new AttributeDAO();
		am = aDAO.getAttributeByID(Integer.valueOf(ctm.getPropertiesFieldDescriptior().getMethodName()));
		return super.getDaAValueObject(ctm);
	}

	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {
		
		if (am != null){
			
			ArrayList<IDaAValueObject> returnValue = new ArrayList<IDaAValueObject>();
			Iterator<String> it = am.getAlEnums().iterator();
			while(it.hasNext()){
				returnValue.add(new EnumValue(it.next()));
			}
			return returnValue;
			
		}
		return new ArrayList<IDaAValueObject>();
	}

	@Override
	public boolean canProvide(Integer entityOwner,
							  ICriteriaTableModel criteriaTableModel) {		
		am = getAttributeModel(criteriaTableModel);
		if (am != null && am.getFieldType().equalsIgnoreCase(Enum.class.getName())){
			return true;
		}else{
			return false;
		}
		
	}
	
	protected AttributeModel getAttributeModel(ICriteriaTableModel criteriaTableModel){
			
		if (criteriaTableModel.getPropertiesFieldDescriptior().getIsPersonal()){
			AttributeDAO adao = new AttributeDAO();
			
			am = adao.getAttributeByID(Integer.valueOf(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName()));
			if (am != null && am.getFieldType().equalsIgnoreCase(Enum.class.getName())){
				return am;
			}else{
				return null;
			}
						
		}else{
			return null;
		}

	}
}
