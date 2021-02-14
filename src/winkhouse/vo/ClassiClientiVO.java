package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ClassiClientiVO implements Serializable{

	private Integer codClasseCliente = null;
	private String descrizione = null;
	private Integer ordine = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public ClassiClientiVO() {
		descrizione = "";
		ordine = 0;				
	}

	public ClassiClientiVO(ResultSet rs) throws SQLException {
		codClasseCliente = rs.getInt("CODCLASSECLIENTE");
		descrizione = rs.getString("DESCRIZIONE");
		ordine = rs.getInt("ORDINE");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}

		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}

	public Integer getCodClasseCliente() {
		return codClasseCliente;
	}

	public void setCodClasseCliente(Integer codClasseCliente) {
		this.codClasseCliente = codClasseCliente;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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
