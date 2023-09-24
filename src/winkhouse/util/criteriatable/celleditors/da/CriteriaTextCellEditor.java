package winkhouse.util.criteriatable.celleditors.da;

import java.util.Date;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import winkhouse.dao.AttributeDAO;
import winkhouse.model.AttributeModel;
import winkhouse.util.AnagraficheMethodName;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.ImmobiliAffittiMethodName;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.criteriatable.celleditors.TextDoubleCellEditorValidator;
import winkhouse.util.criteriatable.celleditors.TextIntegerCellEditorValidator;
import winkhouse.util.criteriatable.celleditors.WinkCellEditorListener;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.IDaACellEditor;

public class CriteriaTextCellEditor extends TextCellEditor 
									implements IDaACellEditor {

	private AttributeDAO attributeDAO = null;
	
	public CriteriaTextCellEditor(Composite c){
		super(c);			
		attributeDAO = new AttributeDAO();
		this.addListener(new WinkCellEditorListener(this));			
	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if (criteriaTableModel.getPropertiesFieldDescriptior().getIsPersonal()){
			AttributeModel attributeModel = attributeDAO.getAttributeByID(Integer.valueOf(criteriaTableModel.getPropertiesFieldDescriptior()
																				 							.getMethodName()));
			
			if (attributeModel.getFieldType().equalsIgnoreCase(Date.class.getName()) || 
				attributeModel.getFieldType().equalsIgnoreCase(Boolean.class.getName()) || 
				attributeModel.getFieldType().equalsIgnoreCase(Enum.class.getName())){
				return false;
			}else{
				if (attributeModel.getFieldType().equalsIgnoreCase(Integer.class.getName())){
					this.setValidator(new TextIntegerCellEditorValidator());
				}else if (attributeModel.getFieldType().equalsIgnoreCase(Double.class.getName())){
					this.setValidator(new TextDoubleCellEditorValidator());
				}else{
					this.setValidator(null);
				}
				return true;
			}
		}else{
			if (entityOwner == ICriteriaOwners.IMMOBILI){
				if (criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_ANNOCOSTRUZIONE) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_MQ) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
 									  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_NUMEROSTANZE) ||
 					criteriaTableModel.getPropertiesFieldDescriptior()
 	 								  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.MMQSTANZA)){
					this.setValidator(new TextIntegerCellEditorValidator());
					return true;
				}
				if (criteriaTableModel.getPropertiesFieldDescriptior()
						  			  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_MUTUO) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
						  			  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_PREZZO) ||
 				 	criteriaTableModel.getPropertiesFieldDescriptior()
		 							  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_SPESE)){
					
					this.setValidator(new TextDoubleCellEditorValidator());
					return true;
					
				} 										  
				if (criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CAP) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_CITTA) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_INDIRIZZO) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
 									  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_PROVINCIA) || 
 					criteriaTableModel.getPropertiesFieldDescriptior()
 									  .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_RIF) ||
	 				criteriaTableModel.getPropertiesFieldDescriptior()
		 						      .getMethodName().equalsIgnoreCase(ImmobiliMethodName.GET_ZONA)){
					this.setValidator(null);
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
						              .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_ANNOCOSTRUZIONE) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
						              .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_MQ) ||
				    criteriaTableModel.getPropertiesFieldDescriptior()
							          .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_NUMEROSTANZE)){
					this.setValidator(new TextIntegerCellEditorValidator());
					return true;
				}
				if (criteriaTableModel.getPropertiesFieldDescriptior()
						  			  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_MUTUO) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
						  			  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_PREZZO) ||
				 	criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_SPESE) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CAUZIONE) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_RATA)){
					
					this.setValidator(new TextDoubleCellEditorValidator());
					return true;
					
				} 										  
				if (criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CAP) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_CITTA) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_INDIRIZZO) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_PROVINCIA) || 
					criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_RIF) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
								      .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.GET_ZONA) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.NOME_ANAGRAFICA) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.COGNOME_ANAGRAFICA) ||
					criteriaTableModel.getPropertiesFieldDescriptior()
									  .getMethodName().equalsIgnoreCase(ImmobiliAffittiMethodName.RAGIONESOCIALE_ANAGRAFICA)){
					
					this.setValidator(null);
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
					this.setValidator(null);
					return true;
				}
			}
			
		}
		return false;
	}
		
}
