package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class StanzeImmobiliVO implements Serializable{

	private Integer codStanzeImmobili = null;
	private Integer codImmobile = null;
	private Integer codTipologiaStanza = null;
	private Integer mq = null;	
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public StanzeImmobiliVO() {
		mq = 0;	
	}

	public StanzeImmobiliVO(ResultSet rs) throws SQLException {
		codStanzeImmobili = rs.getInt("CODSTANZEIMMOBILI");
		codImmobile = rs.getInt("CODIMMOBILE");
		if (rs.wasNull()){
			codImmobile = null;
		}
		
		codTipologiaStanza = rs.getInt("CODTIPOLOGIASTANZA");
		if (rs.wasNull()){
			codTipologiaStanza = null;
		}
		
		mq = rs.getInt("MQ");	
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}

	public Integer getMq() {
		return mq;
	}

	public void setMq(Integer mq) {
		this.mq = mq;
	}

	public Integer getCodStanzeImmobili() {
		return codStanzeImmobili;
	}

	public void setCodStanzeImmobili(Integer codStanzeImmobili) {
		this.codStanzeImmobili = codStanzeImmobili;
	}

	public Integer getCodImmobile() {
		return codImmobile;
	}

	public void setCodImmobile(Integer codImmobile) {
		this.codImmobile = codImmobile;
	}

	public Integer getCodTipologiaStanza() {
		return codTipologiaStanza;
	}

	public void setCodTipologiaStanza(Integer codTipologiaStanza) {
		this.codTipologiaStanza = codTipologiaStanza;
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
