package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.AgentiDAO;
import winkhouse.dao.AnagraficheDAO;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.criteriatable.valueprovider.da.AgenteDaValueProvider.AgentiDAaValue;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class AnagraficheDaValueProvider extends BaseDaValueProvider {
	
	private ArrayList<IDaAValueObject> anagrafiche = null;
	
	public class AnagraficheDAaValue implements IDaAValueObject{
		
		private AnagraficheVO anagrafica = null;
		
		public AnagraficheDAaValue(AnagraficheVO anagrafica){
			this.anagrafica = anagrafica; 
		}
		
		@Override
		public String getDisplayValue() {
			return this.anagrafica.getCognome() + " " + this.anagrafica.getNome();
		}

		@Override
		public Object getBindValue() {
			return this.anagrafica;
		}

		@Override
		public String getCodValue() {
			return String.valueOf(this.anagrafica.getCodAnagrafica());
		}
		
	}

	public AnagraficheDaValueProvider() {
		
	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		if ((entityOwner == ICriteriaOwners.COLLOQUI) && 
			(criteriaTableModel.getPropertiesFieldDescriptior()
				   	   	   	   .getMethodName()
	                           .equalsIgnoreCase(ColloquiMethodName.GET_ANAGRAFICHE))){
					return true;
		}
		return false;
	}

	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {
		
		if (anagrafiche == null){
			
			anagrafiche = new ArrayList<IDaAValueObject>();
			AnagraficheDAO aDAO = new AnagraficheDAO();
			ArrayList al = aDAO.list(AnagraficheVO.class.getName());
			
			for (Iterator iterator = al.iterator(); iterator.hasNext();) {
				
				AnagraficheVO avo = (AnagraficheVO) iterator.next();
				anagrafiche.add(new AnagraficheDAaValue(avo));
				
			}
			
		}
		
		return anagrafiche;
		
	}
}
