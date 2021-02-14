package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WinkGCalendarVO implements Serializable{
	
	private Integer codWinkGCalendar = null;
	private Integer codAppuntamento = null;
	private Integer codColloquio = null;
	private Integer codAgente = null;
	private String calendarId = null;
	private String eventId = null;
	
	public WinkGCalendarVO(Integer codAgente,Integer codAppuntamento,Integer codColloquio,String calendarId,String eventId) {
		this.codAgente = codAgente;
		this.codAppuntamento = codAppuntamento;
		this.codColloquio = codColloquio;
		this.calendarId = calendarId;
		this.eventId = eventId;
	}

	public WinkGCalendarVO(ResultSet rs) throws SQLException{
		this.codWinkGCalendar = rs.getInt("CODWINKGCALENDAR");
		this.codAgente = rs.getInt("CODAGENTE");
		if (rs.wasNull()){
			this.codAgente = null;
		}
		
		this.codAppuntamento = rs.getInt("CODAPPUNTAMENTO");
		if (rs.wasNull()){
			this.codAppuntamento = null;
		}
		
		this.codColloquio = rs.getInt("CODCOLLOQUIO");
		if (rs.wasNull()){
			this.codColloquio = null;
		}
		
		this.calendarId = rs.getString("CALENDARID");
		this.eventId = rs.getString("EVENTID");
	}
	
	public Integer getCodAppuntamento() {
		return codAppuntamento;
	}

	public void setCodAppuntamento(Integer codAppuntamento) {
		this.codAppuntamento = codAppuntamento;
	}

	public Integer getCodColloquio() {
		return codColloquio;
	}

	public void setCodColloquio(Integer codColloquio) {
		this.codColloquio = codColloquio;
	}

	public String getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(String calendarId) {
		this.calendarId = calendarId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Integer getCodWinkGCalendar() {
		return codWinkGCalendar;
	}

	public void setCodWinkGCalendar(Integer codWinkGCalendar) {
		this.codWinkGCalendar = codWinkGCalendar;
	}

	
	public Integer getCodAgente() {
		return codAgente;
	}

	
	public void setCodAgente(Integer codAgente) {
		this.codAgente = codAgente;
	}

}
