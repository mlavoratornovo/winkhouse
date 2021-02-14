package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WinkSysVO implements Serializable{
	
	private String propertyName = null;
	private String propertyValue = null;
	
	public WinkSysVO(){}

	
	public WinkSysVO(ResultSet rs) throws SQLException{
		propertyName = rs.getString("PROPERTYNAME");
		propertyValue = rs.getString("PROPERTYVALUE");
	}
	
	public String getPropertyName() {
		return propertyName;
	}

	
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	
	public String getPropertyValue() {
		return propertyValue;
	}

	
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

}
