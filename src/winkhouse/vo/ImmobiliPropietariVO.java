package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImmobiliPropietariVO implements Serializable{
	
	private Integer codImmobile = null;
	private Integer codAnagrafica = null;

	public ImmobiliPropietariVO(){
	}

	public ImmobiliPropietariVO(ResultSet rs) throws SQLException {
		codImmobile = rs.getInt("CODIMMOBILE");
		codAnagrafica = rs.getInt("CODANAGRAFICA");
	}

	
	public Integer getCodAnagrafica() {
		return codAnagrafica;
	}

	
	public void setCodAnagrafica(Integer codAnagrafica) {
		this.codAnagrafica = codAnagrafica;
	}


	public Integer getCodImmobile() {
		return codImmobile;
	}


	public void setCodImmobile(Integer codImmobile) {
		this.codImmobile = codImmobile;
	}

}
