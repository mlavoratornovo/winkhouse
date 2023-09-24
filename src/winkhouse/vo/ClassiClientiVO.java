package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.Classicliente;

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
	
	public ClassiClientiVO(Classicliente rs){
		codClasseCliente = (int) Cayenne.longPKForObject(rs);
		descrizione = rs.getDescrizione();
		ordine = rs.getOrdine();
		codUserUpdate = (rs.getAgenti() != null)?(int) Cayenne.longPKForObject(rs):null;

		dateUpdate = (rs.getDateupdate() != null)?java.util.Date.from(rs.getDateupdate()
				.atZone(ZoneId.systemDefault()).toInstant()):null;
		
		
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
