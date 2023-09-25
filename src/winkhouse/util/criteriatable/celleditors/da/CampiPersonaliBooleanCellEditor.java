package winkhouse.util.criteriatable.celleditors.da;

import org.eclipse.swt.widgets.Composite;

import winkhouse.dao.AttributeDAO;
import winkhouse.model.AttributeModel;
import winkhouse.util.criteriatable.celleditors.WinkComboBoxViewerCellEditor;
import winkhouse.util.criteriatable.valueprovider.da.BooleanCampiPersonaliDaValueProvider;
import winkhouse.widgets.data.ICriteriaTableModel;

public class CampiPersonaliBooleanCellEditor extends WinkComboBoxViewerCellEditor {

	public CampiPersonaliBooleanCellEditor(Composite c) {
		super(c);
		BooleanCampiPersonaliDaValueProvider bcpdvp = new BooleanCampiPersonaliDaValueProvider();		
		setInput(bcpdvp.getDaAValueObjects());						
	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if (criteriaTableModel.getPropertiesFieldDescriptior().getIsPersonal()){
			AttributeDAO adao = new AttributeDAO();
			AttributeModel attributeModel = adao.getAttributeByID(Integer.valueOf(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName()));

			if (attributeModel.getFieldType().equalsIgnoreCase(Boolean.class.getName())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

}