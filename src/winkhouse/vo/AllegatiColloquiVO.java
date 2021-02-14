package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AllegatiColloquiVO implements Serializable{

	private Integer codAllegatiColloquio = null;
	private Integer codColloquio = null;
	private String descrizione = null;
	private String nome = null;
	private String fromPath = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;
	
	public AllegatiColloquiVO() {
		descrizione = "";
		nome = "";
		fromPath = "";
	}

	public AllegatiColloquiVO(ResultSet rs) throws SQLException {
		
		codAllegatiColloquio = rs.getInt("CODALLEGATICOLLOQUIO");
		if (rs.wasNull()){
			codAllegatiColloquio = null;
		}
		
		codColloquio = rs.getInt("CODCOLLOQUIO");
		if (rs.wasNull()){
			codColloquio = null;
		}
		
		descrizione = rs.getString("COMMENTO");
		nome = rs.getString("NOME");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		

	}
	
	public Integer getCodAllegatiColloquio() {
		return codAllegatiColloquio;
	}

	public void setCodAllegatiColloquio(Integer codAllegatiColloquio) {
		this.codAllegatiColloquio = codAllegatiColloquio;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Integer getCodColloquio() {
		return codColloquio;
	}

	public void setCodColloquio(Integer codColloquio) {
		this.codColloquio = codColloquio;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFromPath() {
		return fromPath;
	}

	public void setFromPath(String fromPath) {
		this.fromPath = fromPath;
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
