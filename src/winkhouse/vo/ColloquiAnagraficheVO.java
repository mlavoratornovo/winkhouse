package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ColloquiAnagraficheVO implements Serializable{

	private Integer codColloquioAnagrafiche = null;
	private Integer codColloquio = null;
	private Integer codAnagrafica = null;
	private String commento = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public ColloquiAnagraficheVO() {
		commento = "";
	}

	public ColloquiAnagraficheVO(ResultSet rs) throws SQLException {
		codColloquioAnagrafiche = rs.getInt("CODCOLLOQUIANAGRAFICHE");
		codColloquio = rs.getInt("CODCOLLOQUIO");
		if (rs.wasNull()){
			codColloquio = null;
		}
		
		codAnagrafica = rs.getInt("CODANAGRAFICA");
		if (rs.wasNull()){
			codAnagrafica = null;
		}
		
		commento = rs.getString("DESCRIZIONE");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		

	}
	
	public Integer getCodColloquioAnagrafiche() {
		return codColloquioAnagrafiche;
	}

	public void setCodColloquioAnagrafiche(Integer codColloquioAnagrafiche) {
		this.codColloquioAnagrafiche = codColloquioAnagrafiche;
	}

	public String getCommento() {
		return commento;
	}

	public void setCommento(String commento) {
		this.commento = commento;
	}

	public Integer getCodColloquio() {
		return codColloquio;
	}

	public void setCodColloquio(Integer codColloquio) {
		this.codColloquio = codColloquio;
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
