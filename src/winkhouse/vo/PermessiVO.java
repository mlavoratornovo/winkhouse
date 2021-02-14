package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class PermessiVO implements Serializable{
	
	private Integer codPermesso = null;
	private Integer codAgente = null;
	private Integer codRicerca = null;
	private Integer codUserUpdate = null;
	private String descrizione = null;
	private Boolean isNot = null;
	private Boolean canWrite = null;	
	private Date dateUpdate = null;
	
	public PermessiVO(){
		isNot = false;
		canWrite = false;
	}
	
	public PermessiVO(ResultSet rs) throws SQLException{
	
		codPermesso = rs.getInt("CODPERMESSO");
		codAgente = rs.getInt("CODAGENTE");
		if (rs.wasNull()){
			codAgente = null;
		}
		
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		codRicerca = rs.getInt("CODRICERCA");
		if (rs.wasNull()){
			codRicerca = null;
		}
		
		descrizione = rs.getString("DESCRIZIONE");
		isNot = rs.getBoolean("ISNOT");		
		canWrite = rs.getBoolean("CANWRITE");
		dateUpdate = rs.getTimestamp("DATEUPDATE");
	}

	public Integer getCodPermesso() {
		return codPermesso;
	}

	public void setCodPermesso(Integer codPermesso) {
		this.codPermesso = codPermesso;
	}

	public Integer getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(Integer codAgente) {
		this.codAgente = codAgente;
	}

	public Integer getCodRicerca() {
		return codRicerca;
	}

	public void setCodRicerca(Integer codRicerca) {
		this.codRicerca = codRicerca;
	}

	public Integer getCodUserUpdate() {
		return codUserUpdate;
	}

	public void setCodUserUpdate(Integer codUserUpdate) {
		this.codUserUpdate = codUserUpdate;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Boolean getIsNot() {
		return isNot;
	}

	public void setIsNot(Boolean isNot) {
		this.isNot = isNot;
	}

	public Date getDateUpdate() {
		return dateUpdate;
	}

	public void setDateUpdate(Date dateUpdate) {
		this.dateUpdate = dateUpdate;
	}

	public Boolean getCanWrite() {
		return canWrite;
	}

	public void setCanWrite(Boolean canWrite) {
		this.canWrite = canWrite;
	}

}
