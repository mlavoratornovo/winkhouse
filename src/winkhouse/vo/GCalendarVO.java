package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class GCalendarVO implements Serializable{
	
	private Integer codGCalendar = null;
	private String privateUrl = null;
	private String allUrl = null;
	private Integer codGData = null;	
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public GCalendarVO(){
		privateUrl = "";
		allUrl = "";
	}
	
	public GCalendarVO(ResultSet rs) throws SQLException{
		codGCalendar = rs.getInt("CODGCALENDAR");
		privateUrl = rs.getString("PRIVATEURL");
		allUrl = rs.getString("ALLURL");
		codGData = rs.getInt("CODGDATA");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		

	}

	public Integer getCodGCalendar() {
		return codGCalendar;
	}

	public void setCodGCalendar(Integer codGCalendar) {
		this.codGCalendar = codGCalendar;
	}

	public String getPrivateUrl() {
		return privateUrl;
	}

	public void setPrivateUrl(String privateUrl) {
		this.privateUrl = privateUrl;
	}

	public String getAllUrl() {
		return allUrl;
	}

	public void setAllUrl(String allUrl) {
		this.allUrl = allUrl;
	}

	public Integer getCodGData() {
		return codGData;
	}

	public void setCodGData(Integer codGData) {
		this.codGData = codGData;
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
