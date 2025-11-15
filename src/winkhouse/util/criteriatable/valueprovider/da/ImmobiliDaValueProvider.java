package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.AnagraficheDAO;
import winkhouse.dao.ImmobiliDAO;
import winkhouse.model.ImmobiliModel;
import winkhouse.orm.Immobili;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.criteriatable.valueprovider.da.AnagraficheDaValueProvider.AnagraficheDAaValue;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ImmobiliVO;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class ImmobiliDaValueProvider extends BaseDaValueProvider {

	public ImmobiliDaValueProvider() {
		
	}

	private ArrayList<IDaAValueObject> immobili = null;
	
    public class ImmobileDAaValue implements IDaAValueObject{
		
		private Immobili immobile = null;
		
		public ImmobileDAaValue(Immobili immobile){
			this.immobile = immobile; 
		}
		
		@Override
		public String getDisplayValue() {
			return this.immobile.getCitta() + " " + 
				   this.immobile.getIndirizzo();
				   
		}

		@Override
		public Object getBindValue() {
			return this.immobile;
		}

		@Override
		public String getCodValue() {
			return String.valueOf(this.immobile.getCodImmobile());
		}
		
	}
    
	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		if ((entityOwner == ICriteriaOwners.COLLOQUI) && 
			(criteriaTableModel.getPropertiesFieldDescriptior()
				   	   	   	   .getMethodName()
	                           .equalsIgnoreCase(ColloquiMethodName.GET_IMMOBILE_ABBINATO))){
					return true;
		}
		return false;
	}

	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {
		
		if (immobili == null){
			
			immobili = new ArrayList<IDaAValueObject>();
			ImmobiliDAO iDAO = new ImmobiliDAO();
			ArrayList al = iDAO.list(ImmobiliModel.class.getName());
			
			for (Iterator iterator = al.iterator(); iterator.hasNext();) {
				
				Immobili im = (Immobili) iterator.next();
				immobili.add(new ImmobileDAaValue(im));
				
			}
			
		}
		
		return immobili;
		
	}
	
}
