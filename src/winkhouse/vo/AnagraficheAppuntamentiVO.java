package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AnagraficheAppuntamentiVO implements Serializable{

	private Integer codAnagraficheAppuntamenti = null;
	private Integer codAnagrafica = null;
	private Integer codAppuntamento = null;
	private String note = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public AnagraficheAppuntamentiVO() {}
	
	public AnagraficheAppuntamentiVO(ResultSet rs) throws SQLException {
		codAnagraficheAppuntamenti = rs.getInt("CODANAGRAFICHEAPPUNTAMENTI");
		if (rs.wasNull()){
			codAnagraficheAppuntamenti = null;
		}
		
		codAnagrafica = rs.getInt("CODANAGRAFICA");
		if (rs.wasNull()){
			codAnagrafica = null;
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

	public Integer getCodAnagraficheAppuntamenti() {
		return codAnagraficheAppuntamenti;
	}

	public void setCodAnagraficheAppuntamenti(Integer codAnagraficheAppuntamenti) {
		this.codAnagraficheAppuntamenti = codAnagraficheAppuntamenti;
	}

	public Integer getCodAnagrafica() {
		return codAnagrafica;
	}

	public void setCodAnagrafica(Integer codAnagrafica) {
		this.codAnagrafica = codAnagrafica;
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
