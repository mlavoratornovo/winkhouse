package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AttributeValueVO implements Serializable{

	private Integer idValue = null;
	private Integer idField = null;
	private Integer idObject = null;
	private String fieldValue = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;
	  
	public AttributeValueVO() {

	}
	
	public AttributeValueVO(ResultSet rs) throws SQLException {
		
		idValue = rs.getInt("IDVALUE");
		if (rs.wasNull()){
			idValue = null;
		}
		
		idField = rs.getInt("IDFIELD");;
		if (rs.wasNull()){
			idField = null;
		}
		
		idObject = rs.getInt("IDOBJECT");;
		if (rs.wasNull()){
			idObject = null;
		}
		
		fieldValue = rs.getString("FIELDVALUE");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		

	}

	public Integer getIdValue() {
		return idValue;
	}

	public void setIdValue(Integer idValue) {
		this.idValue = idValue;
	}

	public Integer getIdField() {
		return idField;
	}

	public void setIdField(Integer idField) {
		this.idField = idField;
	}

	public Integer getIdObject() {
		return idObject;
	}

	public void setIdObject(Integer idObject) {
		this.idObject = idObject;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	
	public Integer getCodUserUpdate() {
		return codUserUpdate;
	}

	
	public void setCodUserUpdate(Integer codUserUpdate) {
		this.codUserUpdate = codUserUpdate;
	}

	
	public Date getDateUpdate() {
		return dateUpdate;
	}

	
	public void setDateUpdate(Date dateUpdate) {
		this.dateUpdate = dateUpdate;
	}
	

}
