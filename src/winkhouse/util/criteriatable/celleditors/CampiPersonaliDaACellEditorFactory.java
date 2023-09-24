package winkhouse.util.criteriatable.celleditors;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import winkhouse.dao.AttributeDAO;
import winkhouse.model.AttributeModel;
import winkhouse.util.criteriatable.celleditors.da.CampiPersonaliEnumCellEditor;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.IDaACellEditor;
import winkhouse.widgets.data.editing.providers.IDaAValueObject;
import winkhouse.widgets.data.factory.DaACellEditorFactory;

public class CampiPersonaliDaACellEditorFactory extends DaACellEditorFactory {

	private ArrayList<IDaACellEditor> cellEditors = new ArrayList<IDaACellEditor>();
	private AttributeDAO attributeDAO = new AttributeDAO();
	
	public CampiPersonaliDaACellEditorFactory() {
	
	}
	
	private class EnumValue implements IDaAValueObject{
		
		private String value = null;
		
		public EnumValue(String value){
			this.value = value;
		}
		
		@Override
		public Object getBindValue() {
			return this.value;
		}

		@Override
		public String getCodValue() {
			return this.value;
		}

		@Override
		public String getDisplayValue() {
			return this.value;
		}
		
	}

	
	public class EnumComboContentProvider implements IStructuredContentProvider{
		
		private AttributeModel attributeModel = null;
		
		public EnumComboContentProvider(AttributeModel am){
			attributeModel = am;
		}

		@Override
		public void dispose() {
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (attributeModel != null){
				
				ArrayList<IDaAValueObject> returnValue = new ArrayList<IDaAValueObject>();
				Iterator<String> it = attributeModel.getAlEnums().iterator();
				while(it.hasNext()){
					returnValue.add(new EnumValue(it.next()));
				}
				return returnValue.toArray();
				
			}
			return new ArrayList<IDaAValueObject>().toArray();			
		}
		
		
		
	}
		
	public IDaACellEditor provideData(Integer entityOwner, ICriteriaTableModel criteriaTableModel){
		
		for (IDaACellEditor cellEditor : cellEditors) {
			
			if (cellEditor.canProvide(entityOwner,criteriaTableModel)){
				
				if (criteriaTableModel.getPropertiesFieldDescriptior().getIsPersonal()){
					if ((criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() != null ) &&
						!(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName().trim().equalsIgnoreCase(""))){
						try{
							AttributeModel am = attributeDAO.getAttributeByID(Integer.valueOf(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName()));
							if (cellEditor instanceof CampiPersonaliEnumCellEditor){
								((CampiPersonaliEnumCellEditor)cellEditor).setContentProvider(new EnumComboContentProvider(am));
							}
							
						}catch(Exception e){
							return cellEditor;
						}
					}
				} 
				return cellEditor;
				
			}
			
		}
		return null;
		
	}
	
	public void addCellEditor(IDaACellEditor cellEditor) throws ClassCastException{
		
		if (cellEditor instanceof CellEditor){
			cellEditors.add(cellEditor);
		}else{
			throw new ClassCastException("Il prametro passato non è di tipo org.eclipse.jface.viewers.CellEditor");
		}
		
	}
	

}
