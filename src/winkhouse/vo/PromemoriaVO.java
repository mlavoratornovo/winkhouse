package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class PromemoriaVO implements Serializable{

	private Integer codPromemoria = null;
	private Integer codAgente = null;
	private String descrizione = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;
		
	public PromemoriaVO() {
		// TODO Auto-generated constructor stub
	}

	public PromemoriaVO(ResultSet rs) throws SQLException {
		
		codPromemoria = rs.getInt("CODPROMEMORIA");
		codAgente = rs.getInt("CODAGENTE");
		if (rs.wasNull()){
			codAgente = null;
		}
		
		descrizione = rs.getString("DESCRIZIONE");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}

	public Integer getCodPromemoria() {
		return codPromemoria;
	}

	public void setCodPromemoria(Integer codPromemoria) {
		this.codPromemoria = codPromemoria;
	}

	public Integer getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(Integer codAgente) {
		this.codAgente = codAgente;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
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
