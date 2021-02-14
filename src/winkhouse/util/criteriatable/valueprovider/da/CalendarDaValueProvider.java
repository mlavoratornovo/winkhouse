package winkhouse.util.criteriatable.valueprovider.da;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import winkhouse.dao.AttributeDAO;
import winkhouse.model.AttributeModel;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.ImmobiliAffittiMethodName;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class CalendarDaValueProvider extends BaseDaValueProvider {

	private AttributeDAO attributeDAO = null;
	
	public CalendarDaValueProvider() {
		attributeDAO = new AttributeDAO();
	}

	public class CalendarDaAValue implements IDaAValueObject{
		
		private SimpleDateFormat formatterIT = new SimpleDateFormat("dd/MM/yyyy");
		private SimpleDateFormat formatterENG = new SimpleDateFormat("yyyy-MM-dd");
		private Calendar calendarValue = null;
		
		public CalendarDaAValue(Calendar calendarValue){
			this.calendarValue = calendarValue;
		}

		@Override
		public String getDisplayValue() {
			if (this.calendarValue != null){
				return formatterIT.format(this.calendarValue.getTime());
			}else{
				return "";
			}
		}

		@Override
		public String getCodValue() {
			if (this.calendarValue != null){
				return formatterENG.format(this.calendarValue.getTime());
			}else{
				return "";
			}
		}

		@Override
		public Object getBindValue() {
			return this.calendarValue;
		}
		
	} 

	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {
		return null;
	}

	@Override
	public boolean canProvide(Integer entityOwner,ICriteriaTableModel criteriaTableModel) {
		
		if ((entityOwner == ICriteriaOwners.IMMOBILI) || (entityOwner == ICriteriaOwners.AFFITTI)){
			
			if (criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ImmobiliMethodName.GET_DATALIBERO){
				
				return true;
				
			}
			
		}
		if (entityOwner == ICriteriaOwners.AFFITTI){
			
			if ((criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ImmobiliAffittiMethodName.PERIODOAFFITTO) ||
				(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ImmobiliAffittiMethodName.DATAPAGATO_RATA) ||
				(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ImmobiliAffittiMethodName.SCADENZA_RATA) ||	
				(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ImmobiliAffittiMethodName.DATAPAGATO_SPESA) ||
				(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ImmobiliAffittiMethodName.SCADENZA_SPESA)){
				
				return true;
				
			}
			
		}
		
		if (entityOwner == ICriteriaOwners.COLLOQUI){
			
			if ((criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ColloquiMethodName.GET_DATA_COLLOQUIO) ||
				(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ColloquiMethodName.GET_DATA_INSERIMENTO)){
				
				return true;
				
			}
			
		}
		
		if (criteriaTableModel.getPropertiesFieldDescriptior().getIsPersonal()){
			
			Integer codAttribute = Integer.valueOf(criteriaTableModel.getPropertiesFieldDescriptior()
																	 .getMethodName());
			
			AttributeModel am = attributeDAO.getAttributeByID(codAttribute);
			
			if (am.getFieldType().equalsIgnoreCase(Date.class.getName())){
				return true;
			}else{
				return false;
			}
			
		}
		
		return false;
		
	}


	@Override
	public IDaAValueObject getDaAValueObject(ICriteriaTableModel ctm) {
		return ctm.getDaValue();
	}

}
