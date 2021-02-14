package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ColloquiVO implements Serializable{

	private Integer codColloquio = null;
	private String descrizione = null;
	private Integer codAgenteInseritore = null;
	private Integer codImmobileAbbinato = null;
	private Integer codTipologiaColloquio = null;
	private Date dataInserimento = null;
	private Date dataColloquio = null;		
	private String luogoIncontro = null;
	private Boolean scadenziere = null;
	private String commentoAgenzia = null;
	private String commentoCliente = null;
	private Integer codParent = null;
	private String iCalUid = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public ColloquiVO() {
		descrizione = "";
		dataInserimento = new Date();
		dataColloquio = new Date();		
		luogoIncontro = "";
		scadenziere = false;
		commentoAgenzia = "";
		commentoCliente = "";
		iCalUid = "";
	}

	public ColloquiVO(ResultSet rs) throws SQLException {
		codColloquio = rs.getInt("CODCOLLOQUIO");
		descrizione = rs.getString("DESCRIZIONE");
		codAgenteInseritore = rs.getInt("CODAGENTEINSERITORE");
		if (rs.wasNull()){
			codAgenteInseritore = null;
		}
		
		codImmobileAbbinato = rs.getInt("CODIMMOBILEABBINATO");
		if (rs.wasNull()){
			codImmobileAbbinato = null;
		}
		
		codTipologiaColloquio = rs.getInt("CODTIPOLOGIACOLLOQUIO");
		if (rs.wasNull()){
			codTipologiaColloquio = null;
		}
		
		dataInserimento = rs.getTimestamp("DATAINSERIMENTO");
		dataColloquio = rs.getTimestamp("DATACOLLOQUIO");		
		luogoIncontro = rs.getString("LUOGO");
		scadenziere = rs.getBoolean("SCADENZIERE");
		commentoAgenzia = rs.getString("COMMENTOAGENZIA");
		commentoCliente = rs.getString("COMMENTOCLIENTE");
		codParent = rs.getInt("CODPARENT");
		if (rs.wasNull()){
			codParent = null;
		}
		
		iCalUid = rs.getString("ICALUID");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}
	
	public Integer getCodColloquio() {
		return codColloquio;
	}

	public void setCodColloquio(Integer codColloquio) {
		this.codColloquio = codColloquio;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Date getDataColloquio() {
		return dataColloquio;
	}

	public void setDataColloquio(Date dataColloquio) {
		this.dataColloquio = dataColloquio;
	}

	public String getLuogoIncontro() {
		return luogoIncontro;
	}

	public void setLuogoIncontro(String luogoIncontro) {
		this.luogoIncontro = luogoIncontro;
	}

	public Boolean getScadenziere() {
		return scadenziere;
	}

	public void setScadenziere(Boolean scadenziere) {
		this.scadenziere = scadenziere;
	}

	public String getCommentoAgenzia() {
		return commentoAgenzia;
	}

	public void setCommentoAgenzia(String commentoAgenzia) {
		this.commentoAgenzia = commentoAgenzia;
	}

	public String getCommentoCliente() {
		return commentoCliente;
	}

	public void setCommentoCliente(String commentoCliente) {
		this.commentoCliente = commentoCliente;
	}

	public Date getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public Integer getCodAgenteInseritore() {
		return codAgenteInseritore;
	}

	public void setCodAgenteInseritore(Integer codAgenteInseritore) {
		this.codAgenteInseritore = codAgenteInseritore;
	}

	public Integer getCodImmobileAbbinato() {
		return codImmobileAbbinato;
	}

	public void setCodImmobileAbbinato(Integer codImmobileAbbinato) {
		this.codImmobileAbbinato = codImmobileAbbinato;
	}

	public Integer getCodTipologiaColloquio() {
		return codTipologiaColloquio;
	}

	public void setCodTipologiaColloquio(Integer codTipologiaColloquio) {
		this.codTipologiaColloquio = codTipologiaColloquio;
	}

	public Integer getCodParent() {
		return codParent;
	}

	public void setCodParent(Integer codParent) {
		this.codParent = codParent;
	}

	public String getiCalUid() {
		return iCalUid;
	}

	public void setiCalUid(String iCalUid) {
		this.iCalUid = iCalUid;
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
