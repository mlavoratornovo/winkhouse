package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class TipiAppuntamentiVO implements Serializable{

	private Integer codTipoAppuntamento = null;
	private String descrizione = null;
	private String gCalColor = null;
	private Integer ordine = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public TipiAppuntamentiVO() {
		descrizione = "";
		gCalColor = "";
		ordine = 0;
	}

	public TipiAppuntamentiVO(ResultSet rs) throws SQLException {
		codTipoAppuntamento = rs.getInt("CODTIPOAPPUNTAMENTO");
		descrizione = rs.getString("DESCRIZIONE");
		gCalColor = rs.getString("GCALCOLOR");
		ordine = rs.getInt("ORDINE");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}	
	
	public Integer getCodTipoAppuntamento() {
		return codTipoAppuntamento;
	}

	public void setCodTipoAppuntamento(Integer codTipoAppuntamento) {
		this.codTipoAppuntamento = codTipoAppuntamento;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getgCalColor() {
		return gCalColor;
	}

	public void setgCalColor(String gCalColor) {
		this.gCalColor = gCalColor;
	}

	public Integer getOrdine() {
		return ordine;
	}

	public void setOrdine(Integer ordine) {
		this.ordine = ordine;
	}

	@Override
	public String toString() {
		return getDescrizione();
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
