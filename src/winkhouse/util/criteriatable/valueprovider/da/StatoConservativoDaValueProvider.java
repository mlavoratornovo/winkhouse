package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.StatoConservativoDAO;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.vo.StatoConservativoVO;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.DaAValueProvider;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class StatoConservativoDaValueProvider extends DaAValueProvider {

	private ArrayList<IDaAValueObject> statiConservativi = null;
	
	public class StatoConservativoDAaValue implements IDaAValueObject{
		
		private StatoConservativoVO statoConservativo = null;
		
		public StatoConservativoDAaValue(StatoConservativoVO statoConservativo){
			this.statoConservativo = statoConservativo; 
		}
		
		@Override
		public String getDisplayValue() {
			return this.statoConservativo.getDescrizione();
		}

		@Override
		public Object getBindValue() {
			return this.statoConservativo;
		}

		@Override
		public String getCodValue() {
			return String.valueOf(this.statoConservativo.getCodStatoConservativo());
		}
		
	}

	
	public StatoConservativoDaValueProvider() {
	}


	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if (((entityOwner == ICriteriaOwners.IMMOBILI) || (entityOwner == ICriteriaOwners.AFFITTI)) && 
			(criteriaTableModel.getPropertiesFieldDescriptior()
						   	   .getMethodName()
						   	   .equalsIgnoreCase(ImmobiliMethodName.GET_CODSTATO))){
				return true;
		}
		return false;
	}
	

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
		
		if (statiConservativi == null){
			
			statiConservativi = new ArrayList<IDaAValueObject>();
			StatoConservativoDAO stDAO = new StatoConservativoDAO();
			ArrayList al = stDAO.list(StatoConservativoVO.class.getName());
			
			for (Iterator iterator = al.iterator(); iterator.hasNext();) {
				
				StatoConservativoVO stvo = (StatoConservativoVO) iterator.next();
				statiConservativi.add(new StatoConservativoDAaValue(stvo));
				
			}
			
		}
		return statiConservativi;
		
	}

}
