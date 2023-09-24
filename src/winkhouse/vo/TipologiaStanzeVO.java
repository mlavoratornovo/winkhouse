package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.Tipologiastanze;

public class TipologiaStanzeVO implements Serializable{

	private Integer codTipologiaStanza = null;
	private String descrizione = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public TipologiaStanzeVO() {
		descrizione = "";		
	}
	
	public TipologiaStanzeVO(ResultSet rs) throws SQLException {
		codTipologiaStanza = rs.getInt("CODTIPOLOGIASTANZE");
		descrizione = rs.getString("DESCRIZIONE");	
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		

	}

	public TipologiaStanzeVO(Tipologiastanze rs){
		codTipologiaStanza = (int) Cayenne.longPKForObject(rs);
		descrizione = rs.getDescrizione();	
		codUserUpdate = (rs.getAgenti() != null)?(int) Cayenne.longPKForObject(rs):null;
		
		dateUpdate = (rs.getDateupdate() != null)?java.util.Date.from(rs.getDateupdate()
				.atZone(ZoneId.systemDefault()).toInstant()):null;

	}

	public Integer getCodTipologiaStanza() {
		return codTipologiaStanza;
	}

	public void setCodTipologiaStanza(Integer codTipologiaStanza) {
		this.codTipologiaStanza = codTipologiaStanza;
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
