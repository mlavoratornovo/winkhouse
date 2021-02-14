package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.configuration.EnvSettingsFactory;
import winkhouse.dao.TipologieImmobiliDAO;
import winkhouse.helper.TipologieColloquiHelper;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.criteriatable.valueprovider.da.TipologiaImmobileDaValueProvider.TipologiaImmobileDAaValue;
import winkhouse.vo.TipologieColloquiVO;
import winkhouse.vo.TipologieImmobiliVO;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class TipologiaColloquiDaValueProvider extends BaseDaValueProvider {

	public TipologiaColloquiDaValueProvider() {
	}
	
	private ArrayList<IDaAValueObject> tipologieColloqui = null;

	public class TipologiaColloquiDAaValue implements IDaAValueObject{
		
		private TipologieColloquiVO tipologieColloqui = null;
		
		public TipologiaColloquiDAaValue(TipologieColloquiVO tipologieColloqui){
			this.tipologieColloqui = tipologieColloqui; 
		}
		
		@Override
		public String getDisplayValue() {
			return this.tipologieColloqui.getDescrizione();
		}

		@Override
		public Object getBindValue() {
			return this.tipologieColloqui;
		}

		@Override
		public String getCodValue() {
			return String.valueOf(this.tipologieColloqui.getCodTipologiaColloquio());
		}
		
	}
	
	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if ((entityOwner == ICriteriaOwners.COLLOQUI) &&
			(criteriaTableModel.getPropertiesFieldDescriptior()
							   .getMethodName()
							   .equalsIgnoreCase(ColloquiMethodName.GET_CODTIPOLOGIA))){
			return true;
		}
		return false;
	}

	
	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {
		
		if (tipologieColloqui == null){
			
			tipologieColloqui = new ArrayList<IDaAValueObject>();
			ArrayList al = EnvSettingsFactory.getInstance().getTipologieColloqui();
			for (Iterator iterator = al.iterator(); iterator.hasNext();) {
				
				TipologieColloquiVO tivo = (TipologieColloquiVO) iterator.next();
				tipologieColloqui.add(new TipologiaColloquiDAaValue(tivo));
				
			}
			
		}
		return tipologieColloqui;
	}

	

	

}
