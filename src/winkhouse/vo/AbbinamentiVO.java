package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AbbinamentiVO implements Serializable{

	private Integer codAbbinamento = null;
	private Integer codImmobile = null;
	private Integer codAnagrafica = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public AbbinamentiVO(){
		super();
	}
	
	public AbbinamentiVO(ResultSet rs) throws SQLException {
		super();				
		codAbbinamento = rs.getInt("CODABBINAMENTO");
		if (rs.wasNull()){
			codAbbinamento = null;
		}
				
		codImmobile = rs.getInt("CODIMMOBILE");
		if (rs.wasNull()){
			codImmobile = null;
		}
		
		codAnagrafica = rs.getInt("CODANAGRAFICA");
		if (rs.wasNull()){
			codAnagrafica = null;
		}
		
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}

	public Integer getCodAbbinamento() {
		return codAbbinamento;
	}

	public void setCodAbbinamento(Integer codAbbinamento) {
		this.codAbbinamento = codAbbinamento;
	}

	public Integer getCodImmobile() {
		return codImmobile;
	}

	public void setCodImmobile(Integer codImmobile) {
		this.codImmobile = codImmobile;
	}

	public Integer getCodAnagrafica() {
		return codAnagrafica;
	}

	public void setCodAnagrafica(Integer codAnagrafica) {
		this.codAnagrafica = codAnagrafica;
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
