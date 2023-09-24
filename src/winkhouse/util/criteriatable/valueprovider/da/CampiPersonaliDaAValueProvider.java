package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;

import winkhouse.dao.AttributeDAO;
import winkhouse.model.AttributeModel;
import winkhouse.vo.AttributeValueVO;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class CampiPersonaliDaAValueProvider extends BaseDaValueProvider {

	private Integer entityOwner = null;
	private ICriteriaTableModel criteriaTableModel = null;
	protected AttributeDAO attributeDAO = null;
	
	public CampiPersonaliDaAValueProvider() {
		attributeDAO = new AttributeDAO();
	}

	public class CampiPersonaliDaAValue implements IDaAValueObject{

		private AttributeValueVO valoreCampoPersonalizzato = null;
		
		public CampiPersonaliDaAValue(AttributeValueVO valoreCampoPersonalizzato){
			this.valoreCampoPersonalizzato = valoreCampoPersonalizzato;
		}
		
		@Override
		public String getDisplayValue() {
			return this.valoreCampoPersonalizzato.getFieldValue();
		}

		@Override
		public String getCodValue() {
			return this.valoreCampoPersonalizzato.getFieldValue();
		}

		@Override
		public Object getBindValue() {
			return this.valoreCampoPersonalizzato;
		}
		
	}
	
	
	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {
		return null;
	}

	@Override
	public boolean canProvide(Integer entityOwner,ICriteriaTableModel criteriaTableModel) {
				
		boolean returnvalue = false;
		try{
			Integer.valueOf(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName());
		}catch(Exception e){
			return false;
		}
		AttributeDAO adao = new AttributeDAO();
		AttributeModel attributeModel = adao.getAttributeByID(Integer.valueOf(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName()));

		if (attributeModel.getFieldType().equalsIgnoreCase(Boolean.class.getName()) || attributeModel.getFieldType().equalsIgnoreCase(Enum.class.getName())){
			returnvalue = false;
		}else{
			returnvalue = true;
		}

		if (criteriaTableModel.getPropertiesFieldDescriptior().getIsPersonal() && returnvalue){
			return true;
		}
		return returnvalue;
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
		return ctm.getDaValue();
	}

}
