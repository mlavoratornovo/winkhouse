package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ClasseEnergeticaVO implements Serializable{

	private Integer codClasseEnergetica = null;
	private String nome = null; 
	private String descrizione = null;
	private Integer ordine = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public ClasseEnergeticaVO() {
		nome = "";
		descrizione = "";
		ordine = 0;
	}

	public ClasseEnergeticaVO(ResultSet rs) throws SQLException {
		codClasseEnergetica = rs.getInt("CODCLASSEENERGETICA");
		nome = rs.getString("NOME");
		descrizione = rs.getString("DESCRIZIONE");
		ordine = rs.getInt("ORDINE");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}

		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}
	
	public Integer getCodClasseEnergetica() {
		return codClasseEnergetica;
	}

	public void setCodClasseEnergetica(Integer codClasseEnergetica) {
		this.codClasseEnergetica = codClasseEnergetica;
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

	public Integer getOrdine() {
		return ordine;
	}

	public void setOrdine(Integer ordine) {
		this.ordine = ordine;
	}

	@Override
	public String toString() {
		return getNome() + " " + getDescrizione();
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
