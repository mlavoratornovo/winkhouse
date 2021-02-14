package winkhouse.util.criteriatable.valueprovider.da;

import java.util.ArrayList;

import winkhouse.util.AnagraficheMethodName;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.ImmobiliAffittiMethodName;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;

public class TextDaValueProvider extends BaseDaValueProvider {

	public TextDaValueProvider() {

	}

	public class TextDaAValue implements IDaAValueObject{
		
		private String strValue = null;
		
		public TextDaAValue(String strValue){
			this.strValue = strValue;
		}
		
		@Override
		public String getDisplayValue() {
			return this.strValue;
		}

		@Override
		public String getCodValue() {
			return this.strValue;
		}

		@Override
		public Object getBindValue() {
			return null;
		}
		
	}

	@Override
	public ArrayList<IDaAValueObject> getDaAValueObjects() {

		return null;
	}

	@Override
	public boolean canProvide(Integer entityOwner,ICriteriaTableModel criteriaTableModel) {
		if ((entityOwner == ICriteriaOwners.IMMOBILI) || entityOwner == ICriteriaOwners.AFFITTI){
			if (criteriaTableModel.getPropertiesFieldDescriptior()
					  			  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_ANNOCOSTRUZIONE) ||
			    criteriaTableModel.getPropertiesFieldDescriptior()
					  			  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CAP) ||
				criteriaTableModel.getPropertiesFieldDescriptior()
								  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CITTA) ||
				criteriaTableModel.getPropertiesFieldDescriptior()
								  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_INDIRIZZO) ||
				criteriaTableModel.getPropertiesFieldDescriptior()
								  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_MQ) ||
				criteriaTableModel.getPropertiesFieldDescriptior()
								  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_MUTUO) ||
				criteriaTableModel.getPropertiesFieldDescriptior()
								  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_PREZZO) ||
				criteriaTableModel.getPropertiesFieldDescriptior()
								  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_NUMEROSTANZE) ||
				criteriaTableModel.getPropertiesFieldDescriptior()
								  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_PROVINCIA) || 
				criteriaTableModel.getPropertiesFieldDescriptior()
								  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_RIF) ||
				criteriaTableModel.getPropertiesFieldDescriptior()
								  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_SPESE) ||
				criteriaTableModel.getPropertiesFieldDescriptior()
							      .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_ZONA) ||
				criteriaTableModel.getPropertiesFieldDescriptior()
								  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.MMQSTANZA)){
				return true;
			}
		}
		if (entityOwner == ICriteriaOwners.ANAGRAFICHE){
			if (!criteriaTableModel.getPropertiesFieldDescriptior()
					   			   .getMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CODAGENTEINSERITORE) &&
			    !criteriaTableModel.getPropertiesFieldDescriptior()
					   			   .getMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_CODCLASSECLIENTE) &&
			    !criteriaTableModel.getPropertiesFieldDescriptior()
					               .getMethodName().equalsIgnoreCase(AnagraficheMethodName.GET_DATAINSERIMENTO)){
				return true;
			}
		}

		if (entityOwner == ICriteriaOwners.AFFITTI){
			if (criteriaTableModel.getPropertiesFieldDescriptior()
					   			  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.COGNOME_ANAGRAFICA) ||
			    criteriaTableModel.getPropertiesFieldDescriptior()
					   			  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.NOME_ANAGRAFICA) ||
			    criteriaTableModel.getPropertiesFieldDescriptior()
					              .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.RAGIONESOCIALE_ANAGRAFICA) ||
			    criteriaTableModel.getPropertiesFieldDescriptior()
			    				  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.RAGIONESOCIALE_ANAGRAFICA) ||
			    criteriaTableModel.getPropertiesFieldDescriptior()
				                  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CAUZIONE) ||
  			    criteriaTableModel.getPropertiesFieldDescriptior()
                                  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_RATA)){

				
				return true;
			}
		}

		if (entityOwner == ICriteriaOwners.COLLOQUI){
			if (criteriaTableModel.getPropertiesFieldDescriptior()
					   			  .getMethodName().equalsIgnoreCase(ColloquiMethodName.GET_COMMENTO_AGENZIA) ||
			    criteriaTableModel.getPropertiesFieldDescriptior()
					   			  .getMethodName().equalsIgnoreCase(ColloquiMethodName.GET_COMMENTO_CLIENTE) ||
			    criteriaTableModel.getPropertiesFieldDescriptior()
					              .getMethodName().equalsIgnoreCase(ColloquiMethodName.GET_DESCRIZIONE) ||
			    criteriaTableModel.getPropertiesFieldDescriptior()
			    				  .getMethodName().equalsIgnoreCase(ColloquiMethodName.GET_LUOGO_INCONTRO)){
				
				return true;
			}
		}

		return false;
	}

	@Override
	public IDaAValueObject getDaAValueObject(ICriteriaTableModel ctm) {
		return ctm.getDaValue();
	}

}
