package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.RiscaldamentiDAO;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.vo.RiscaldamentiVO;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class RiscaldamentoDaValueProvider extends BaseDaValueProvider {

	private ArrayList<IDaAValueObject> riscaldamenti = null;
	
	public class RiscaldamentoDAaValue implements IDaAValueObject{
		
		private RiscaldamentiVO riscaldamento = null;
		
		public RiscaldamentoDAaValue(RiscaldamentiVO riscaldamento){
			this.riscaldamento = riscaldamento; 
		}
		
		@Override
		public String getDisplayValue() {
			return this.riscaldamento.getDescrizione();
		}

		@Override
		public Object getBindValue() {
			return this.riscaldamento;
		}

		@Override
		public String getCodValue() {
			return String.valueOf(this.riscaldamento.getCodRiscaldamento());
		}
		
	}

	public RiscaldamentoDaValueProvider() {

	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		if (((entityOwner == ICriteriaOwners.IMMOBILI) || (entityOwner == ICriteriaOwners.AFFITTI)) && 
			(criteriaTableModel.getPropertiesFieldDescriptior()
					   	   	   .getMethodName()
					   	   	   .equalsIgnoreCase(ImmobiliMethodName.GET_CODRISCALDAMENTO))){
			return true;
		}
		return false;
	}

	
	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {
		
		if (riscaldamenti == null){
			
			riscaldamenti = new ArrayList<IDaAValueObject>();
			RiscaldamentiDAO rDAO = new RiscaldamentiDAO();
			ArrayList al = rDAO.list(RiscaldamentiVO.class.getName());
			
			for (Iterator iterator = al.iterator(); iterator.hasNext();) {
				
				RiscaldamentiVO rvo = (RiscaldamentiVO) iterator.next();
				riscaldamenti.add(new RiscaldamentoDAaValue(rvo));
				
			}
			
		}
		return riscaldamenti;
		
		
	}

}
