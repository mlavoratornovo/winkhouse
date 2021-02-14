package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ContattiVO implements Serializable{

	private Integer codContatto = null;
	private String contatto = null;
	private String descrizione = null;
	private Integer codTipologiaContatto = null;
	private Integer codAnagrafica = null;
	private Integer codAgente = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public ContattiVO() {
		contatto = "";
		descrizione = "";
	}
	
	public ContattiVO(ResultSet rs) throws SQLException {
		codContatto = rs.getInt("CODCONTATTO");
		contatto = rs.getString("CONTATTO");
		descrizione = rs.getString("DESCRIZIONE"); 
		codTipologiaContatto = rs.getInt("CODTIPOLOGIACONTATTO");
		if (rs.wasNull()){
			codTipologiaContatto = null;
		}
		
		codAnagrafica = rs.getInt("CODANAGRAFICA");
		if (rs.wasNull()){
			codAnagrafica = null;
		}
		
		codAgente = rs.getInt("CODAGENTE");
		if (rs.wasNull()){
			codAgente = null;
		}
		
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}

	public Integer getCodContatto() {
		return codContatto;
	}

	public void setCodContatto(Integer codContatto) {
		this.codContatto = codContatto;
	}

	public String getContatto() {
		return contatto;
	}

	public void setContatto(String contatto) {
		this.contatto = contatto;
	}
	
	public Integer getCodAnagrafica() {
		return codAnagrafica;
	}

	public void setCodAnagrafica(Integer codAnagrafica) {
		this.codAnagrafica = codAnagrafica;
	}

	public Integer getCodTipologiaContatto() {
		return codTipologiaContatto;
	}

	public void setCodTipologiaContatto(Integer codTipologiaContatto) {
		this.codTipologiaContatto = codTipologiaContatto;
	}

	public Integer getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(Integer codAgente) {
		this.codAgente = codAgente;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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
