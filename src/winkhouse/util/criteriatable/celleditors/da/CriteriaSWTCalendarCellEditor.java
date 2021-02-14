package winkhouse.util.criteriatable.celleditors.da;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import winkhouse.dao.AttributeDAO;
import winkhouse.dialogs.custom.SWTCalendarCellEditor;
import winkhouse.model.AttributeModel;
import winkhouse.util.AnagraficheMethodName;
import winkhouse.util.ColloquiMethodName;
import winkhouse.util.ImmobiliAffittiMethodName;
import winkhouse.util.ImmobiliMethodName;
import winkhouse.util.criteriatable.valueprovider.da.CalendarDaValueProvider;
import winkhouse.widgets.data.ICriteriaOwners;
import winkhouse.widgets.data.ICriteriaTableModel;
import winkhouse.widgets.data.editing.IDaACellEditor;

public class CriteriaSWTCalendarCellEditor extends SWTCalendarCellEditor
										   implements IDaACellEditor {

		protected SimpleDateFormat formatterENG = new SimpleDateFormat("yyyy-MM-dd");
		protected SimpleDateFormat formatterIT = new SimpleDateFormat("dd-MM-yyyy");
		private AttributeDAO attributeDAO = null;
		private ICriteriaTableModel currentCriteriaTableModel = null;		
		
		public CriteriaSWTCalendarCellEditor(Composite composite, String data){
			
			super(composite,null);
			attributeDAO = new AttributeDAO();
			if (data != null && !data.equalsIgnoreCase("")){
				Calendar c = Calendar.getInstance(Locale.ITALIAN);
				this.setCalendar(c);
				try {
					Date d = formatterENG.parse(data);
					c.setTime(d);
				} catch (ParseException e) {
					c.setTime(new Date());
				}
			}					

		}

		@Override
		public boolean canProvide(Integer entityOwner, ICriteriaTableModel criteriaTableModel) {
			
			this.currentCriteriaTableModel = criteriaTableModel;
					
			if (entityOwner == ICriteriaOwners.IMMOBILI){
				
				if (criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ImmobiliMethodName.GET_DATALIBERO){
					setSWTCalendarDate(criteriaTableModel);
					return true;
					
				}
				
			}
			if (entityOwner == ICriteriaOwners.ANAGRAFICHE){
				
				if (criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == AnagraficheMethodName.GET_DATAINSERIMENTO){
					setSWTCalendarDate(criteriaTableModel);
					return true;
					
				}
				
			}
			if (entityOwner == ICriteriaOwners.AFFITTI){
				
				if ((criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ImmobiliAffittiMethodName.GET_DATALIBERO) ||
					(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ImmobiliAffittiMethodName.PERIODOAFFITTO) ||
					(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ImmobiliAffittiMethodName.SCADENZA_RATA) ||
					(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ImmobiliAffittiMethodName.DATAPAGATO_SPESA) ||
					(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ImmobiliAffittiMethodName.SCADENZA_SPESA) ||
					(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ImmobiliAffittiMethodName.DATAPAGATO_RATA)){
					setSWTCalendarDate(criteriaTableModel);
					return true;
					
				}
				
			}
			if (entityOwner == ICriteriaOwners.COLLOQUI){
				
				if ((criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ColloquiMethodName.GET_DATA_COLLOQUIO) ||
					(criteriaTableModel.getPropertiesFieldDescriptior().getMethodName() == ColloquiMethodName.GET_DATA_INSERIMENTO)){
					setSWTCalendarDate(criteriaTableModel);
					return true;
					
				}
				
			}
			
			
			if (criteriaTableModel.getPropertiesFieldDescriptior().getIsPersonal()){
				
				Integer codAttribute = Integer.valueOf(criteriaTableModel.getPropertiesFieldDescriptior()
																		 .getMethodName());
				
				AttributeModel am = attributeDAO.getAttributeByID(codAttribute);
				
				if (am.getFieldType().equalsIgnoreCase(Date.class.getName())){
					setSWTCalendarDate(criteriaTableModel);
					return true;
				}else{
					return false;
				}
				
			}
			
			return false;
		}
		
		protected Calendar setSWTCalendarDate(ICriteriaTableModel criteriaTableModel){
			
			Calendar c = Calendar.getInstance(Locale.ITALIAN);
			
			if (criteriaTableModel.getDaValue() != null && criteriaTableModel.getDaValue().getCodValue() != null &&
				!criteriaTableModel.getDaValue().getCodValue().equalsIgnoreCase("")){
								
				this.setCalendar(c);
				try {
					Date d = formatterENG.parse(criteriaTableModel.getDaValue().getCodValue());
					c.setTime(d);
				} catch (ParseException e) {
					c.setTime(new Date());
				}
				
			}else{
				c.setTime(new Date());
			}	
			return c;
			
		}

		@Override
		protected Object openDialogBox(Control cellEditorWindow) {
			
			SWTCaledar cal = new SWTCaledar(cellEditorWindow);
			cal.setDate(this.setSWTCalendarDate(currentCriteriaTableModel));
	  		cal.open();
	  		CalendarDaValueProvider cdavp = new CalendarDaValueProvider();
	  		CalendarDaValueProvider.CalendarDaAValue cdaavo = cdavp.new CalendarDaAValue(cal.getReturnValue());
			return cdaavo;
		}

}
