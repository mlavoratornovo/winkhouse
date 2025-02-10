package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;
import java.util.Iterator;

import winkhouse.dao.ClassiEnergeticheDAO;
import winkhouse.orm.Classienergetiche;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.vo.ClasseEnergeticaVO;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class ClasseEnergeticaDaAValueProvider extends BaseDaValueProvider {

	private ArrayList<IDaAValueObject> classiEnergetiche = null;
	
	public class ClasseEnergeticaDAaValue implements IDaAValueObject{
		
		private Classienergetiche classeEnergetica = null;
		
		public ClasseEnergeticaDAaValue(Classienergetiche classeEnergetica){
			this.classeEnergetica = classeEnergetica; 
		}
		
		@Override
		public String getDisplayValue() {
			return this.classeEnergetica.getDescrizione();
		}

		@Override
		public Object getBindValue() {
			return this.classeEnergetica;
		}

		@Override
		public String getCodValue() {
			return String.valueOf(this.classeEnergetica.getCodClasseEnergetica());
		}
		
	}

	
	public ClasseEnergeticaDaAValueProvider() {
	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel){
		if (((entityOwner == ICriteriaOwners.IMMOBILI) || (entityOwner == ICriteriaOwners.AFFITTI)) && 
			(criteriaTableModel.getPropertiesFieldDescriptior()
				   	   	   	   .getMethodName()
				   	   	   	   .equalsIgnoreCase(ImmobiliMethodName.GET_CODCLASSEENERGETICA))){
				return true;
		}
		return false;
		
	}
		
	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {
		
		if (classiEnergetiche == null){
			
			classiEnergetiche = new ArrayList<IDaAValueObject>();
			ClassiEnergeticheDAO ceDAO = new ClassiEnergeticheDAO();
			ArrayList<Classienergetiche> al = ceDAO.listClassiEnergetiche(null);
			
			for (Iterator iterator = al.iterator(); iterator.hasNext();) {
				
				Classienergetiche cevo = (Classienergetiche) iterator.next();
				classiEnergetiche.add(new ClasseEnergeticaDAaValue(cevo));
				
			}
			
		}
		return classiEnergetiche;
		
		
	}

}
