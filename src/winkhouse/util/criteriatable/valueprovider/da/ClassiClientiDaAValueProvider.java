package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.ClassiClientiDAO;
import winkhouse.util.AnagraficheMethodName;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class ClassiClientiDaAValueProvider extends BaseDaValueProvider {

	private ArrayList<IDaAValueObject> classiClienti = null;
	
	public class ClassiClientiDAaValue implements IDaAValueObject{
		
		private ClassiClientiVO classeCliente = null;
		
		public ClassiClientiDAaValue(ClassiClientiVO classeCliente){
			this.classeCliente = classeCliente; 
		}
		
		@Override
		public String getDisplayValue() {
			return this.classeCliente.getDescrizione();
		}

		@Override
		public Object getBindValue() {
			return this.classeCliente;
		}

		@Override
		public String getCodValue() {
			return String.valueOf(this.classeCliente.getCodClasseCliente());
		}
		
	}

	
	public ClassiClientiDaAValueProvider() {
	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel){
		if ((entityOwner == ICriteriaOwners.ANAGRAFICHE) && 
			(criteriaTableModel.getPropertiesFieldDescriptior()
				   	   	   	   .getMethodName()
				   	   	   	   .equalsIgnoreCase(AnagraficheMethodName.GET_CODCLASSECLIENTE))){
				return true;
		}
		return false;
		
	}
		
	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {
		
		if (classiClienti == null){
			
			classiClienti = new ArrayList<IDaAValueObject>();
			ClassiClientiDAO ccDAO = new ClassiClientiDAO();
			ArrayList al = ccDAO.list(ClassiClientiVO.class.getName());
			
			for (Iterator iterator = al.iterator(); iterator.hasNext();) {
				
				ClassiClientiVO ccvo = (ClassiClientiVO) iterator.next();
				classiClienti.add(new ClassiClientiDAaValue(ccvo));
				
			}
			
		}
		return classiClienti;
		
		
	}


}
