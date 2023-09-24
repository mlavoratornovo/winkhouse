package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.Tipologiecontatti;

public class TipologiaContattiVO implements Serializable{

	private Integer codTipologiaContatto = null;
	private String descrizione = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public TipologiaContattiVO() {
		descrizione = "";				
	}

	public TipologiaContattiVO(ResultSet rs) throws SQLException {
		codTipologiaContatto = rs.getInt("CODTIPOLOGIACONTATTO");
		descrizione = rs.getString("DESCRIZIONE");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		

	}

	public TipologiaContattiVO(Tipologiecontatti rs){
		codTipologiaContatto = (int) Cayenne.longPKForObject(rs);
		descrizione = rs.getDescrizione();	
		codUserUpdate = (rs.getAgenti() != null)?(int) Cayenne.longPKForObject(rs):null;
		
		dateUpdate = (rs.getDateupdate() != null)?java.util.Date.from(rs.getDateupdate()
				.atZone(ZoneId.systemDefault()).toInstant()):null;

	}
	
	public Integer getCodTipologiaContatto() {
		return codTipologiaContatto;
	}

	public void setCodTipologiaContatto(Integer codTipologiaContatto) {
		this.codTipologiaContatto = codTipologiaContatto;
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
