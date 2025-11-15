package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.orm.Tipologieimmobili;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.vo.TipologieImmobiliVO;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class TipologiaImmobileDaValueProvider extends BaseDaValueProvider {

	private ArrayList<IDaAValueObject> tipologieImmobili = null;

	public class TipologiaImmobileDAaValue implements IDaAValueObject{
		
		private Tipologieimmobili tipologiaImmobili = null;
		
		public TipologiaImmobileDAaValue(Tipologieimmobili tipologiaImmobili){
			this.tipologiaImmobili = tipologiaImmobili; 
		}
		
		@Override
		public String getDisplayValue() {
			return this.tipologiaImmobili.getDescrizione();
		}

		@Override
		public Object getBindValue() {
			return this.tipologiaImmobili;
		}

		@Override
		public String getCodValue() {
			return String.valueOf(this.tipologiaImmobili.getCodTipologiaImmobile());
		}
		
	}
	
	public TipologiaImmobileDaValueProvider() {

	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if (((entityOwner == ICriteriaOwners.IMMOBILI) || (entityOwner == ICriteriaOwners.AFFITTI)) &&
			(criteriaTableModel.getPropertiesFieldDescriptior()
							   .getMethodName()
							   .equalsIgnoreCase(ImmobiliMethodName.GET_CODTIPOLOGIA))){
			return true;
		}
		return false;
	}

	
	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {
		
		if (tipologieImmobili == null){
			
			tipologieImmobili = new ArrayList<IDaAValueObject>();
			TipologieImmobiliDAO tiDAO = new TipologieImmobiliDAO();
			ArrayList al = tiDAO.list(null);
			
			for (Iterator iterator = al.iterator(); iterator.hasNext();) {
				
				Tipologieimmobili tivo = (Tipologieimmobili) iterator.next();
				tipologieImmobili.add(new TipologiaImmobileDAaValue(tivo));
				
			}
			
		}
		return tipologieImmobili;
	}

	

}
