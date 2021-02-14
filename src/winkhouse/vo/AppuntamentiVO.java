package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AppuntamentiVO implements Serializable{

	private Integer codAppuntamento = null;
	private Date dataInserimento = null;
	private Date dataAppuntamento = null;
	private Date dataFineAppuntamento = null;
	private Integer codTipoAppuntamento = null;
	private Integer codPadre = null;	
	private String iCalUID = null;
	private String descrizione = null;
	private String luogo = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public AppuntamentiVO(){
		codAppuntamento = null;
		dataInserimento = new Date();
		dataAppuntamento = new Date();
		dataFineAppuntamento = null;
		codTipoAppuntamento = null;
		codPadre = null;		
		descrizione = "";
		luogo = "";
		iCalUID = "";
	}

	public AppuntamentiVO(ResultSet rs) throws SQLException{
		codAppuntamento = rs.getInt("CODAPPUNTAMENTO");
		if (rs.wasNull()){
			codAppuntamento = null;
		}
		
		dataInserimento = rs.getTimestamp("DATAINSERIMENTO");
		dataAppuntamento = rs.getTimestamp("DATAAPPUNTAMENTO");
		descrizione = rs.getString("DESCRIZIONE");
		luogo = rs.getString("LUOGO");
		dataFineAppuntamento = (rs.getTimestamp("DATAFINEAPPUNTAMENTO")==null)
								? rs.getTimestamp("DATAAPPUNTAMENTO")
								: rs.getTimestamp("DATAFINEAPPUNTAMENTO");
								
		codTipoAppuntamento = rs.getInt("CODTIPOAPPUNTAMENTO");
		if (rs.wasNull()){
			codTipoAppuntamento = null;
		}
		
		codPadre = rs.getInt("CODPADRE");
		if (rs.wasNull()){
			codPadre = null;
		}
		
		iCalUID = (rs.getString("ICALUID") == null)
				  ? ""
				  : rs.getString("ICALUID");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}	
	
	public Integer getCodAppuntamento() {
		return codAppuntamento;
	}

	public void setCodAppuntamento(Integer codAppuntamento) {
		this.codAppuntamento = codAppuntamento;
	}

	public Date getDataInserimento() {		
		return dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public Date getDataAppuntamento() {
		return dataAppuntamento;
	}

	public void setDataAppuntamento(Date dataAppuntamento) {
		this.dataAppuntamento = dataAppuntamento;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getLuogo() {
		return luogo;
	}

	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}
	
	public Date getDataFineAppuntamento() {
		return dataFineAppuntamento;
	}
	
	public void setDataFineAppuntamento(Date dataFineAppuntamento) {
		this.dataFineAppuntamento = dataFineAppuntamento;
	}
	
	public Integer getCodTipoAppuntamento() {
		return codTipoAppuntamento;
	}
	
	public void setCodTipoAppuntamento(Integer codTipoAppuntamento) {
		this.codTipoAppuntamento = codTipoAppuntamento;
	}
	
	public Integer getCodPadre() {
		return codPadre;
	}
	
	public void setCodPadre(Integer codPadre) {
		this.codPadre = codPadre;
	}
	
	public String getiCalUID() {
		return iCalUID;
	}
	
	public void setiCalUID(String iCalUID) {
		this.iCalUID = iCalUID;
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
