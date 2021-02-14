package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AgentiAppuntamentiVO implements Serializable{

	private Integer codAgentiAppuntamenti = null;
	private Integer codAgente = null;
	private Integer codAppuntamento = null;
	private String note = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public AgentiAppuntamentiVO() {
		note = "";
	}

	public AgentiAppuntamentiVO(ResultSet rs) throws SQLException {
		codAgentiAppuntamenti = rs.getInt("CODAGENTIAPPUNTAMENTI");
		if (rs.wasNull()){
			codAgentiAppuntamenti = null;
		}
		
		codAgente = rs.getInt("CODAGENTE");
		if (rs.wasNull()){
			codAgente = null;
		}
		
		codAppuntamento = rs.getInt("CODAPPUNTAMENTO");
		if (rs.wasNull()){
			codAppuntamento = null;
		}
		
		note = rs.getString("NOTE");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		

	}

	public Integer getCodAgentiAppuntamenti() {
		return codAgentiAppuntamenti;
	}

	public void setCodAgentiAppuntamenti(Integer codAgentiAppuntamenti) {
		this.codAgentiAppuntamenti = codAgentiAppuntamenti;
	}

	public Integer getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(Integer codAgente) {
		this.codAgente = codAgente;
	}

	public Integer getCodAppuntamento() {
		return codAppuntamento;
	}

	public void setCodAppuntamento(Integer codAppuntamento) {
		this.codAppuntamento = codAppuntamento;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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
