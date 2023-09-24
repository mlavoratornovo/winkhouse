package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AttributeVO implements Serializable{

	private Integer idAttribute = null; 
	private Integer idClassEntity = null; 
	private String attributeName = null; 
	private String fieldType = null; 
	private Integer linkedIdEntity = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;
	private String enumFieldValues = null;

	
	public AttributeVO() {
		
	}

	public AttributeVO(ResultSet rs) throws SQLException {
		
		idAttribute = rs.getInt("IDATTRIBUTE");
		if (rs.wasNull()){
			idAttribute = null;
		}
		
		idClassEntity = rs.getInt("IDCLASSENTITY");
		if (rs.wasNull()){
			idClassEntity = null;
		}
		
		attributeName = rs.getString("ATTRIBUTENAME"); 
		fieldType = rs.getString("FIELDTYPE"); 		
		linkedIdEntity = rs.getInt("LINKEDIDENTITY");
		if (rs.wasNull()){
			linkedIdEntity = null;;
		}		
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		enumFieldValues = rs.getString("ENUMFIELDVALUES");
	}

	public Integer getIdAttribute() {
		return idAttribute;
	}

	public void setIdAttribute(Integer idAttribute) {
		this.idAttribute = idAttribute;
	}

	public Integer getIdClassEntity() {
		return idClassEntity;
	}

	public void setIdClassEntity(Integer idClassEntity) {
		this.idClassEntity = idClassEntity;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Integer getLinkedIdEntity() {
		return linkedIdEntity;
	}

	public void setLinkedIdEntity(Integer linkedIdEntity) {
		this.linkedIdEntity = linkedIdEntity;
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

	public String getEnumFieldValues() {
		return enumFieldValues;
	}

	public void setEnumFieldValues(String enumFieldValues) {
		this.enumFieldValues = enumFieldValues;
	}

}
