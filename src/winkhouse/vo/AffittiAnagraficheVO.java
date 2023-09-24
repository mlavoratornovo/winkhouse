package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AffittiAnagraficheVO implements Serializable{

	private Integer codAffittiAnagrafiche = null;
	private Integer codAffitto = null;
	private Integer codAnagrafica = null;
	private String nota = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public AffittiAnagraficheVO(){
		nota = "";		
	}
	
	public AffittiAnagraficheVO(ResultSet rs) throws SQLException{
		codAffittiAnagrafiche = rs.getInt("CODAFFITTIANAGRAFICHE");
		if (rs.wasNull()){
			codAffittiAnagrafiche = null;
		}
		
		codAffitto = rs.getInt("CODAFFITTO");
		if (rs.wasNull()){
			codAffitto = null;
		}
		
		codAnagrafica = rs.getInt("CODANAGRAFICA");
		if (rs.wasNull()){
			codAnagrafica = null;
		}
		
		nota = rs.getString("NOTA");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		

	}

	public Integer getCodAffittiAnagrafiche() {
		return codAffittiAnagrafiche;
	}

	public void setCodAffittiAnagrafiche(Integer codAffittiAnagrafiche) {
		this.codAffittiAnagrafiche = codAffittiAnagrafiche;
	}

	public Integer getCodAffitto() {
		return codAffitto;
	}

	public void setCodAffitto(Integer codAffitto) {
		this.codAffitto = codAffitto;
	}

	public Integer getCodAnagrafica() {
		return codAnagrafica;
	}

	public void setCodAnagrafica(Integer codAnagrafica) {
		this.codAnagrafica = codAnagrafica;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
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
