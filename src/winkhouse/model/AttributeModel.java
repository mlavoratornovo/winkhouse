package winkhouse.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import winkhouse.dao.AttributeValueDAO;
import winkhouse.vo.AttributeVO;

public class AttributeModel extends AttributeVO {

	private AttributeValueModel value = null;	
	private ArrayList<String> alEnums = null;
	
	public AttributeModel() {

	}

	public AttributeModel(ResultSet rs) throws SQLException {
		super(rs);
	}
		
	public AttributeValueModel getValue(Integer idInstaceObject){
		
		if (value == null){
			if (idInstaceObject != null){
				AttributeValueDAO avDAO = new AttributeValueDAO();
				value = avDAO.getAttributeValueByIdAttributeIdObject(super.getIdAttribute(), idInstaceObject);
			}	
		}
		
		return value;
		
	}

	public void setValue(AttributeValueModel value) {
		this.value = value;
	}

	public String getFieldTypeForReport(){
		
		if (getFieldType().equalsIgnoreCase(Integer.class.getName())){
			return "Numerico intero";
		}else if (getFieldType().equalsIgnoreCase(Double.class.getName())){
			return "Numerico con virgola";
		}else if (getFieldType().equalsIgnoreCase(String.class.getName())){
			return "Testo";
		}else if (getFieldType().equalsIgnoreCase(Date.class.getName())){
			return "Data";
		}else{
			return "";
		}
		
	}
	
	public String getValueForReport(Integer idInstaceObject){
		if (getValue(idInstaceObject) != null){
			return getValue(idInstaceObject).getFieldValue();
		}else{
			return "";
		}
		
	}

	public ArrayList<String> getAlEnums() {
		if (alEnums == null){
			alEnums = new ArrayList<String>();
			alEnums.add("");
			StringTokenizer st = new StringTokenizer(this.getEnumFieldValues(), "|");
			while (st.hasMoreTokens()) {
				alEnums.add(st.nextToken());
			}
		}
		return alEnums;
	}

}
