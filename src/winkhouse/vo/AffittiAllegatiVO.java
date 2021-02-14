package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AffittiAllegatiVO implements Serializable{
	
	private Integer codAffittiAllegati = null;
	private Integer codAffitto = null;
	private String nome = null;
	private String descrizione = null;
	private String fromPath = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public AffittiAllegatiVO() {
		nome = "";
		descrizione = "";
		fromPath = "";
	}

	public AffittiAllegatiVO(ResultSet rs) throws SQLException {
		codAffittiAllegati = rs.getInt("CODAFFITTIALLEGATI");
		if (rs.wasNull()){
			codAffittiAllegati = null;
		}
		
		codAffitto = rs.getInt("CODAFFITTO");
		if (rs.wasNull()){
			codAffitto = null;
		}
		
		nome = rs.getString("NOME");
		descrizione = rs.getString("DESCRIZIONE");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}

	public Integer getCodAffittiAllegati() {
		return codAffittiAllegati;
	}

	public void setCodAffittiAllegati(Integer codAffittiAllegati) {
		this.codAffittiAllegati = codAffittiAllegati;
	}

	public Integer getCodAffitto() {
		return codAffitto;
	}

	public void setCodAffitto(Integer codAffitto) {
		this.codAffitto = codAffitto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getFromPath() {
		return fromPath;
	}

	public void setFromPath(String fromPath) {
		this.fromPath = fromPath;
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
