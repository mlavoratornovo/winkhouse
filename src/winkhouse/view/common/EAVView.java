package winkhouse.view.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import winkhouse.Activator;
import winkhouse.action.entity.EntityAttributeRefreshAction;
import winkhouse.action.entity.SaveEntityAttributeValueAction;
import winkhouse.dialogs.custom.SWTCalendarCellEditor;
import winkhouse.model.AttributeModel;
import winkhouse.model.AttributeValueModel;
import winkhouse.view.common.celleditors.DoubleTextCellEditor;
import winkhouse.view.common.celleditors.IntegerTextCellEditor;

public class EAVView extends ViewPart {

	public final static String ID = "winkhouse.eavview";

	private Image CHECK = Activator.getImageDescriptor("icons/chkall.png").createImage();
	private Image UNCHECK = Activator.getImageDescriptor("icons/chknone.png").createImage();
	private TableViewer tvAttributes = null;
	private FormToolkit ft = null;
	private Form f = null;	
	private ArrayList<AttributeModel> attributes = null; 
	private Integer instanceID = null;
	private SimpleDateFormat formatterIT = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatterENG = new SimpleDateFormat("yyyy-MM-dd");

	private SaveEntityAttributeValueAction saveEntityAttributeValueAction = null;
	private EntityAttributeRefreshAction entityAttributeRefreshAction = null;
	
	
	public class EavLabelProvider implements ITableLabelProvider,ITableFontProvider{

		public EavLabelProvider(){}
		
		@Override
		public void addListener(ILabelProviderListener listener) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
		}

		@Override
		public Font getFont(Object element, int columnIndex) {
			
			if(columnIndex == 0){
				Font f = new Font(null, "Arial", 8, SWT.BOLD);
				return f;
			}
			return null;
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			AttributeModel aModel = ((AttributeModel)element);
			switch (columnIndex){
			case 0: return (aModel.getAttributeName() == null)
						   ? ""
						   : aModel.getAttributeName();
			
			case 1: return ((aModel.getValue(instanceID) == null)
			   			   ? ""
			   			   : aModel.getValue(instanceID).getFieldValue());
			
			default : return "";
			}
		}
		
	}
	
	public EAVView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {
		ft = new FormToolkit(getViewSite().getShell().getDisplay());
		f = ft.createForm(parent);
		f.setImage(Activator.getImageDescriptor("icons/campi_personali.png").createImage());
		f.setText("Campi personali");
		f.getBody().setLayout(new GridLayout());
		
		
		
		saveEntityAttributeValueAction = new SaveEntityAttributeValueAction("Salva campi personali", 
				   															Activator.getImageDescriptor("/icons/document-save.png"));

		entityAttributeRefreshAction = new EntityAttributeRefreshAction("Ricarica campi personali", 
		  		 														Activator.getImageDescriptor("icons/adept_reinstall.png"));
		
		
		getViewSite().getActionBars().getToolBarManager().add(saveEntityAttributeValueAction);
		
		getViewSite().getActionBars().getToolBarManager().add(entityAttributeRefreshAction);

		f.updateToolBar();
		
		GridData gdExpVH = new GridData();
		gdExpVH.grabExcessHorizontalSpace = true;
		gdExpVH.grabExcessVerticalSpace = true;
		gdExpVH.horizontalAlignment = SWT.FILL;
		gdExpVH.verticalAlignment = SWT.FILL;	
		
		tvAttributes = new TableViewer(f.getBody(),SWT.HORIZONTAL|SWT.VERTICAL|SWT.FULL_SELECTION);
		tvAttributes.getTable().setLayoutData(gdExpVH);
		tvAttributes.getTable().setHeaderVisible(true);
		tvAttributes.getTable().setLinesVisible(true);
		tvAttributes.setContentProvider(new IStructuredContentProvider(){

			@Override
			public Object[] getElements(Object inputElement) {
				return (attributes == null)
						? new ArrayList().toArray()
						: attributes.toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			
		});
		tvAttributes.setLabelProvider(new EavLabelProvider());
		
		TableColumn tcAttributeName = new TableColumn(tvAttributes.getTable(),SWT.CENTER,0);
		tcAttributeName.setWidth(150);
		tcAttributeName.setText("Nome campo");
				
			
		TableColumn tcAttributeValue = new TableColumn(tvAttributes.getTable(),SWT.CENTER,1);
		tcAttributeValue.setWidth(150);
		tcAttributeValue.setText("Valore");
		
		TableViewerColumn tcvAttributeValue = new TableViewerColumn(tvAttributes,tcAttributeValue);
		
		tcvAttributeValue.setLabelProvider(new CellLabelProvider(){

			@Override
			public void update(ViewerCell cell) {
				
				if (((AttributeModel)cell.getElement()).getFieldType().equalsIgnoreCase(Date.class.getName())){
					if ((((AttributeModel)cell.getElement()).getValue(instanceID)!= null) &&
						(((AttributeModel)cell.getElement()).getValue(instanceID).getFieldValue() != null) &&
					    (!((AttributeModel)cell.getElement()).getValue(instanceID).getFieldValue().equalsIgnoreCase("")) &&
					    (((AttributeModel)cell.getElement()).getValue(instanceID).getFieldValue() != null)){
						
						Date d;
						try {
							d = formatterIT.parse(((AttributeModel)cell.getElement()).getValue(instanceID).getFieldValue());
							cell.setText(((AttributeModel)cell.getElement()).getValue(instanceID).getFieldValue());
						} catch (ParseException e) {
							try {
								d = formatterENG.parse(((AttributeModel)cell.getElement()).getValue(instanceID).getFieldValue());
								cell.setText(formatterIT.format(d));
							} catch (ParseException e1) {
								cell.setText("");
							}							
						}
						
					}
				}else if (((AttributeModel)cell.getElement()).getFieldType().equalsIgnoreCase(Boolean.class.getName())){
						if (((AttributeModel)cell.getElement()).getValue(instanceID) == null || 
							Boolean.valueOf(((AttributeModel)cell.getElement()).getValue(instanceID).getFieldValue())==false){
							cell.setText("No");
						}else{
							cell.setText("Si");
						}
				}else if (((AttributeModel)cell.getElement()).getFieldType().equalsIgnoreCase(Enum.class.getName())){
					cell.setText((((AttributeModel)cell.getElement()).getValue(instanceID)!= null)
					 	 	      ? ((AttributeModel)cell.getElement()).getValue(instanceID).getFieldValue()
					 	 	      : "");
				}else{
					cell.setText((((AttributeModel)cell.getElement()).getValue(instanceID)!= null)
					 	 	 ? ((AttributeModel)cell.getElement()).getValue(instanceID).getFieldValue()
					 	 	 : "");
				}

			}
			
		});
		
		tcvAttributeValue.setEditingSupport(new EditingSupport(tvAttributes){

			@Override
			protected boolean canEdit(Object element) {
				return true;
			}

			@Override
			protected CellEditor getCellEditor(Object element) {
				if (((AttributeModel)element).getFieldType().equalsIgnoreCase(String.class.getName())){
					return new TextCellEditor(tvAttributes.getTable());
				}
				if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Integer.class.getName())){
					return new IntegerTextCellEditor(tvAttributes.getTable());
				}
				if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Double.class.getName())){
					return new DoubleTextCellEditor(tvAttributes.getTable());
				}
				if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Date.class.getName())){
					SWTCalendarCellEditor fdce = null;					
					if ((((AttributeModel)element).getValue(instanceID) != null) && 
						(((AttributeModel)element).getValue(instanceID).getFieldValue() != null)){
							try {
								Date dstart = formatterENG.parse(((AttributeModel)element).getValue(instanceID).getFieldValue());
								Calendar c = Calendar.getInstance(Locale.ITALIAN);
								c.setTime(dstart);
								fdce = new SWTCalendarCellEditor(tvAttributes.getTable(),c);
							} catch (ParseException e) {
								Calendar c = Calendar.getInstance(Locale.ITALIAN);
								c.setTime(new Date());
								fdce = new SWTCalendarCellEditor(tvAttributes.getTable(),c);
							}
													
					}else{
						Calendar c = Calendar.getInstance(Locale.ITALIAN);
						c.setTime(new Date());
						fdce = new SWTCalendarCellEditor(tvAttributes.getTable(),c);
					}
					return fdce;

				}
				if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Boolean.class.getName())){
					
					String[] values = new String[2];
					
					values[0] = "Si";
					values[1] = "No";		
							
					ComboBoxCellEditor cbce = new ComboBoxCellEditor(tvAttributes.getTable(),
							 										 values,
							 										 SWT.READ_ONLY|SWT.DROP_DOWN);
					return cbce;

					
				}
				if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Enum.class.getName())){
					String[] values = ((AttributeModel)element).getAlEnums().toArray(new String[((AttributeModel)element).getAlEnums().size()]); 
					ComboBoxCellEditor cbce = new ComboBoxCellEditor(tvAttributes.getTable(),
																	 values,
							 										 SWT.READ_ONLY|SWT.DROP_DOWN);
					return cbce;

					
				}
				
				return null;
				
			}

			@Override
			protected Object getValue(Object element) {
				
				if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Date.class.getName())){
					Date d;
					try {
						if (((AttributeModel)element).getValue(instanceID) != null){
							d = formatterENG.parse(((AttributeModel)element).getValue(instanceID).getFieldValue());
							return formatterIT.format(d);
						}
						return "";
					} catch (Exception e) {
						return "";
					}
					
				}else{
					if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Boolean.class.getName())){
						return (((AttributeModel)element).getValue(instanceID)== null)
				   			   ? 1
						   	   : (Boolean.valueOf(((AttributeModel)element).getValue(instanceID).getFieldValue())
						   		  ? 0: 1);
						
					}else if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Enum.class.getName())){
						
						if (((AttributeModel)element).getValue(instanceID) != null){
							for (int i = 0; i < ((AttributeModel)element).getAlEnums().size(); i++) {							
							
								if (((String)((AttributeModel)element).getAlEnums().get(i)).equalsIgnoreCase(((AttributeModel)element).getValue(instanceID).getFieldValue())){
									return i;
								}
							}
							return 0;
						}
						return 0;
					}else{
						return (((AttributeModel)element).getValue(instanceID)== null)
			   			   	   ? ""
					   		   : ((AttributeModel)element).getValue(instanceID).getFieldValue();
					}
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				
				if (((AttributeModel)element).getValue(instanceID) == null){
					
					AttributeValueModel aValue = new AttributeValueModel();
					aValue.setFieldValue(String.valueOf(value));
					aValue.setIdField(((AttributeModel)element).getIdAttribute());
					aValue.setIdObject(instanceID);
					if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Date.class.getName())){
						Date d;
						try {																					
							aValue.setFieldValue(formatterENG.format(((Calendar)value).getTime()));
						} catch (Exception e) {
							aValue.setFieldValue(null);
						}
						((AttributeModel)element).setValue(aValue);
						
					}else{
						if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Integer.class.getName())){
							try {
								Integer.valueOf((String)value);
								aValue.setFieldValue((String)value);
								((AttributeModel)element).setValue(aValue);
							} catch (NumberFormatException e) {
								aValue.setFieldValue(null);
							}
						}else if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Double.class.getName())){
							try {
								if (((String)value).indexOf(",") > 0){
									value = ((String)value).replace(',', '.');
								}
								Double.valueOf((String)value);								
								aValue.setFieldValue((String)value);
								((AttributeModel)element).setValue(aValue);
							} catch (NumberFormatException e) {
								aValue.setFieldValue(null);
							} 
						}else if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Boolean.class.getName())){
							aValue.setFieldValue((((Integer)value) == 0)?"true":"false");
							((AttributeModel)element).setValue(aValue);
						}else if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Enum.class.getName())){
							aValue.setFieldValue((((Integer)value) == 0)?null:((AttributeModel)element).getAlEnums().get((((Integer)value))));
							((AttributeModel)element).setValue(aValue);
						}else{
							((AttributeModel)element).setValue(aValue);
						}
					}
				}else{
					if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Date.class.getName())){
						Date d;
						try {
							((AttributeModel)element).getValue(instanceID).setFieldValue(formatterENG.format(((Calendar)value).getTime()));
						} catch (Exception e) {
							((AttributeModel)element).getValue(instanceID).setFieldValue(null);
						}
						
					}else{		
						if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Integer.class.getName())){
							try {
								Integer.valueOf((String)value);
								((AttributeModel)element).getValue(instanceID).setFieldValue((String)value);
							} catch (NumberFormatException e) {
								((AttributeModel)element).getValue(instanceID).setFieldValue(null);
							}
						}else if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Double.class.getName())){
							try {
								if (((String)value).indexOf(",") > 0){
									value = ((String)value).replace(',', '.');
								}
								Double.valueOf((String)value);								
								((AttributeModel)element).getValue(instanceID).setFieldValue((String)value);
							} catch (NumberFormatException e) {
								((AttributeModel)element).getValue(instanceID).setFieldValue(null);
							} 
						}else if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Boolean.class.getName())){
							try {
								((AttributeModel)element).getValue(instanceID).setFieldValue((((Integer)value) == 0)?"true":"false");
							} catch (NumberFormatException e) {
								((AttributeModel)element).getValue(instanceID).setFieldValue(String.valueOf(false));
							}
						}else if (((AttributeModel)element).getFieldType().equalsIgnoreCase(Enum.class.getName())){
								
								((AttributeModel)element).getValue(instanceID).setFieldValue(((AttributeModel)element).getAlEnums().get((((Integer)value))));
						}else{
								((AttributeModel)element).getValue(instanceID).setFieldValue(String.valueOf(value));
							}
						}
					
					
				}
				tvAttributes.refresh();
			}
			
		});
				
	}

	@Override
	public void setFocus() {

		
	}
	
	public ArrayList<AttributeModel> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(ArrayList<AttributeModel> attributes, Integer instanceID) {
		this.attributes = attributes;
		this.instanceID = instanceID;
		tvAttributes.setInput(this.attributes);
	}

	public Integer getInstanceID() {
		return instanceID;
	}

	public TableViewer getTvAttributes() {
		return tvAttributes;
	}

	public void setTvAttributes(TableViewer tvAttributes) {
		this.tvAttributes = tvAttributes;
	}

	public void setCompareView(ArrayList<AttributeModel> attributes, Integer instanceID){
		
		saveEntityAttributeValueAction.setEnabled(false);
		entityAttributeRefreshAction.setEnabled(false);
		
		tvAttributes.getTable().setEnabled(false);
		
		setAttributes(attributes, instanceID);

	}

	public void setCompareView(boolean enabled){
		saveEntityAttributeValueAction.setEnabled(!enabled);
		tvAttributes.getTable().setEnabled(!enabled);
	}
}
