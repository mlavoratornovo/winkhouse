package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.AgentiDAO;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.vo.AgentiVO;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class AgenteDaValueProvider extends BaseDaValueProvider {

	private ArrayList<IDaAValueObject> agenti = null;
	
	public class AgentiDAaValue implements IDaAValueObject{
		
		private AgentiVO agente = null;
		
		public AgentiDAaValue(AgentiVO agente){
			this.agente = agente; 
		}
		
		@Override
		public String getDisplayValue() {
			return this.agente.getCognome() + " " + this.agente.getNome();
		}

		@Override
		public Object getBindValue() {
			return this.agente;
		}

		@Override
		public String getCodValue() {
			return String.valueOf(this.agente.getCodAgente());
		}
		
	}

	
	public AgenteDaValueProvider() {

	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel){
		if (((entityOwner == ICriteriaOwners.IMMOBILI) || (entityOwner == ICriteriaOwners.AFFITTI)) && 
			(criteriaTableModel.getPropertiesFieldDescriptior()
			   	   	   	   	   .getMethodName()
                               .equalsIgnoreCase(ImmobiliMethodName.GET_CODAGENTEINSERITORE))){
				return true;
		}else if ((entityOwner == ICriteriaOwners.COLLOQUI) && 
				  (criteriaTableModel.getPropertiesFieldDescriptior()
	   	   	   	   				     .getMethodName()
	   	   	   	   				     .equalsIgnoreCase(ColloquiMethodName.GET_AGENTI))){
				return true;
			
		}
		return false;
		
	}
	
	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {
		
		if (agenti == null){
			
			agenti = new ArrayList<IDaAValueObject>();
			AgentiDAO aDAO = new AgentiDAO();
			ArrayList al = aDAO.list(AgentiVO.class.getName());
			
			for (Iterator iterator = al.iterator(); iterator.hasNext();) {
				
				AgentiVO avo = (AgentiVO) iterator.next();
				agenti.add(new AgentiDAaValue(avo));
				
			}
			
		}
		return agenti;		
		
	}

}
