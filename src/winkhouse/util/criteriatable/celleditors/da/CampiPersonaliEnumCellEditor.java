package winkhouse.util.criteriatable.celleditors.da;

import org.eclipse.swt.widgets.Composite;

import winkhouse.dao.AttributeDAO;
import winkhouse.model.AttributeModel;
import winkhouse.util.criteriatable.celleditors.WinkComboBoxViewerCellEditor;
import winkhouse.util.criteriatable.valueprovider.da.EnumCampiPersonaliDaValueProvider;
import winkhouse.widgets.data.ICriteriaTableModel;

public class CampiPersonaliEnumCellEditor extends WinkComboBoxViewerCellEditor {

	public CampiPersonaliEnumCellEditor(Composite c) {
		super(c);
		EnumCampiPersonaliDaValueProvider bcpdvp = new EnumCampiPersonaliDaValueProvider();		
		setInput(bcpdvp.getDaAValueObjects());						
	}

	@Override
	public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
		
		if (criteriaTableModel.getPropertiesFieldDescriptior().getIsPersonal()){
			AttributeDAO adao = new AttributeDAO();
			AttributeModel attributeModel = adao.getAttributeByID(Integer.valueOf(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName()));

			if (attributeModel.getFieldType().equalsIgnoreCase(Enum.class.getName())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
}
