package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ColloquiAgentiVO implements Serializable{

	private Integer codColloquioAgenti = null;
	private Integer codColloquio = null;
	private Integer codAgente = null;
	private String commento = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public ColloquiAgentiVO() {
		commento = "";
	}

	public ColloquiAgentiVO(ResultSet rs) throws SQLException {
		codColloquioAgenti = rs.getInt("CODCOLLOQUIOAGENTE");
		codColloquio = rs.getInt("CODCOLLOQUIO");
		if (rs.wasNull()){
			codColloquio = null;
		}
		
		codAgente = rs.getInt("CODAGENTE");
		if (rs.wasNull()){
			codAgente = null;
		}

		commento = rs.getString("COMMENTO");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}

		dateUpdate = rs.getTimestamp("DATEUPDATE");		

	}	
	
	public Integer getCodColloquioAgenti() {
		return codColloquioAgenti;
	}

	public void setCodColloquioAgenti(Integer codColloquioAgenti) {
		this.codColloquioAgenti = codColloquioAgenti;
	}

	public Integer getCodColloquio() {
		return codColloquio;
	}

	public void setCodColloquio(Integer codColloquio) {
		this.codColloquio = codColloquio;
	}

	public Integer getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(Integer codAgente) {
		this.codAgente = codAgente;
	}

	public String getCommento() {
		return commento;
	}

	public void setCommento(String commento) {
		this.commento = commento;
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
