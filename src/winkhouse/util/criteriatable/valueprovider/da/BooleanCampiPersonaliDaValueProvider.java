package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;

import winkhouse.dao.AttributeDAO;
import winkhouse.vo.AttributeVO;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class BooleanCampiPersonaliDaValueProvider extends BaseDaValueProvider {

	public BooleanCampiPersonaliDaValueProvider() {
		
	}

	private ArrayList<IDaAValueObject> si_no_values = null;

	public class SiNoDAaValue implements IDaAValueObject{
		
		private Boolean value = null;
		
		public SiNoDAaValue(Boolean value){
			this.value = value; 
		}
		
		@Override
		public String getDisplayValue() {
			return (this.value == null)
					? "No"
					: (this.value)
					  ? "Si"
					  : "No";
		}

		@Override
		public Object getBindValue() {
			return this.value;
		}

		@Override
		public String getCodValue() {
			return String.valueOf(this.value);
		}
		
	}
	
	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if (criteriaTableModel.getPropertiesFieldDescriptior().getIsPersonal()){
			AttributeDAO adao = new AttributeDAO();
			
			AttributeVO avo = adao.getAttributeByID(Integer.valueOf(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName()));
			if (avo != null && avo.getFieldType().equalsIgnoreCase(Boolean.class.getName())){
				return true;
			}else{
				return false;
			}
						
		}else{
			return false;
		}
	}

	
	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {
		
		if (si_no_values == null){
			
			si_no_values = new ArrayList<IDaAValueObject>();
			si_no_values.add(new SiNoDAaValue(true));
			si_no_values.add(new SiNoDAaValue(false));
			
		}
		return si_no_values;
	}
	
	
}
