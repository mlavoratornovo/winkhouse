package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class StatoConservativoVO implements Serializable{

	private Integer codStatoConservativo = null;
	private String descrizione = null;		
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public StatoConservativoVO() {
		descrizione = "";		
	}

	public StatoConservativoVO(ResultSet rs) throws SQLException {
		codStatoConservativo = rs.getInt("CODSTATOCONSERVATIVO");
		descrizione = rs.getString("DESCRIZIONE");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}

	public Integer getCodStatoConservativo() {
		return codStatoConservativo;
	}

	public void setCodStatoConservativo(Integer codStatoConservativo) {
		this.codStatoConservativo = codStatoConservativo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	@Override
	public String toString() {		
		return getDescrizione();
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
