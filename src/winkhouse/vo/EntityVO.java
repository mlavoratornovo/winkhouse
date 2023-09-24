package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;



public class EntityVO implements Serializable{

	private Integer idClassEntity = null;
	private String className = null;
	private String description = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;
	
	public static final String[] fieldTypes = {String.class.getName(),
											   Integer.class.getName(),
											   Double.class.getName(),
											   Date.class.getName(),
											   Boolean.class.getName(),
											   Enum.class.getName()}; 
	

	public EntityVO() {

	}

	public EntityVO(ResultSet rs) throws SQLException{
		idClassEntity = rs.getInt("IDCLASSENTITY");
		className = rs.getString("CLASSNAME");
		description = rs.getString("DESCRIPTION");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}
	
	public Integer getIdClassEntity() {
		return idClassEntity;
	}

	public void setIdClassEntity(Integer idClassEntity) {
		this.idClassEntity = idClassEntity;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
