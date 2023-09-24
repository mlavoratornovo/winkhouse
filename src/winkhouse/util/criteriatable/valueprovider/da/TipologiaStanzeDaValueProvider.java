package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.TipologiaStanzeDAO;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.vo.TipologiaStanzeVO;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class TipologiaStanzeDaValueProvider extends BaseDaValueProvider {

	private ArrayList<IDaAValueObject> tipologieStanze = null;
	
	public TipologiaStanzeDaValueProvider() {

	}

	public class TipologiaStanzeDAaValue implements IDaAValueObject{

		private TipologiaStanzeVO tipologiaStanze = null;
		
		public TipologiaStanzeDAaValue(TipologiaStanzeVO tipologiaStanze){
			this.tipologiaStanze = tipologiaStanze; 
		}
		
		@Override
		public Object getBindValue() {
			return this.tipologiaStanze;
		}

		@Override
		public String getCodValue() {
			return String.valueOf(this.tipologiaStanze.getCodTipologiaStanza());
		}

		@Override
		public String getDisplayValue() {
			return this.tipologiaStanze.getDescrizione();	
		}
		
		
		
	}

	
	

	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {
		
		if (tipologieStanze == null){
			
			tipologieStanze = new ArrayList<IDaAValueObject>();
			TipologiaStanzeDAO tsDAO = new TipologiaStanzeDAO();
			ArrayList al = tsDAO.list(TipologiaStanzeVO.class.getName());
			
			for (Iterator iterator = al.iterator(); iterator.hasNext();) {
				
				TipologiaStanzeVO tsvo = (TipologiaStanzeVO) iterator.next();
				tipologieStanze.add(new TipologiaStanzeDAaValue(tsvo));
				
			}
			
		}
		return tipologieStanze;
	}	

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if (((entityOwner == ICriteriaOwners.IMMOBILI) || (entityOwner == ICriteriaOwners.AFFITTI)) &&
			(criteriaTableModel.getPropertiesFieldDescriptior()
							   .getMethodName()
							   .equalsIgnoreCase(ImmobiliMethodName.MTIPOLOGIASTANZA))){
			return true;
		}
		return false;
	}
	
}
