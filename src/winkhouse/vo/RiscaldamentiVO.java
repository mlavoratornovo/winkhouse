package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class RiscaldamentiVO implements Serializable{

	private Integer codRiscaldamento = null;
	private String descrizione = null;		
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public RiscaldamentiVO() {
		descrizione = "";				
	}

	public RiscaldamentiVO(ResultSet rs) throws SQLException {
		codRiscaldamento = rs.getInt("CODRISCALDAMENTO");
		descrizione = rs.getString("DESCRIZIONE");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}
	
	public Integer getCodRiscaldamento() {
		return codRiscaldamento;
	}

	public void setCodRiscaldamento(Integer codRiscaldamento) {
		this.codRiscaldamento = codRiscaldamento;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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
