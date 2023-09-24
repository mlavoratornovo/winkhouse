package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AllegatiImmobiliVO implements Serializable{

	private Integer codAllegatiImmobili = null;
	private Integer codImmobile = null;
	private String commento = null;
	private String nome = null;
	private String fromPath = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public AllegatiImmobiliVO() {
		commento = "";
		nome = "";
		fromPath = "";
	}

	public AllegatiImmobiliVO(ResultSet rs) throws SQLException{
		codAllegatiImmobili = rs.getInt("CODALLEGATIIMMOBILI");
		if (rs.wasNull()){
			codAllegatiImmobili = null;
		}
		
		codImmobile = rs.getInt("CODIMMOBILE");;
		if (rs.wasNull()){
			codImmobile = null;
		}
		
		commento = rs.getString("COMMENTO");
		nome = rs.getString("NOME");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}

	public Integer getCodAllegatiImmobili() {
		return codAllegatiImmobili;
	}

	public void setCodAllegatiImmobili(Integer codAllegatiImmobili) {
		this.codAllegatiImmobili = codAllegatiImmobili;
	}

	public Integer getCodImmobile() {
		return codImmobile;
	}

	public void setCodImmobile(Integer codImmobile) {
		this.codImmobile = codImmobile;
	}

	public String getCommento() {
		return commento;
	}

	public void setCommento(String commento) {
		this.commento = commento;
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
		return getNome();
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
