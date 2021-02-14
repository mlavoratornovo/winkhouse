package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Classe VO che rappresenta una riga della tabella IMMOBILI
 */

public class ImmobiliVO implements Serializable{
	
	private Integer codImmobile = null;
	private String rif = null;
	private String indirizzo = null;
	private String provincia = null;
	private String cap = null;
	private String citta = null;
	private String zona = null;	
	private Date dataInserimento = null;
	private Date dataLibero = null;
	private String descrizione = null;
	private String mutuoDescrizione = null;
	private Double prezzo = null;
	private Double mutuo = null;
	private Double spese = null;
	private String varie = null;
	private Boolean visione = null;
	private Boolean storico = null;
	private Boolean affittabile = null;
	private Integer mq = null;
	private Integer annoCostruzione = null;
	private Integer codAgenteInseritore = null;
	private Integer codAnagrafica = null;
	private Integer codRiscaldamento = null;
	private Integer codStato = null;
	private Integer codTipologia = null;
	private Integer codClasseEnergetica = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;
	private String ncivico = null;

	
	/**
	 * Costruttore standard che inizializza tutti i campi della classe :
	 * Stringa -> "" 
	 * Integer -> 0
	 * Date    -> new Date()
	 * Double  -> 0.0
	 * boolean -> false
	 */
	public ImmobiliVO() {
		rif = "";
		indirizzo = "";
		provincia = "";
		cap = "";
		citta = "";
		zona = "";	
		dataInserimento = new Date();
		dataLibero = new Date();
		descrizione = "";
		mutuoDescrizione = "";
		prezzo = 0.0;
		mutuo = 0.0;
		spese = 0.0;
		varie = "";
		visione = false;
		storico = false;
		affittabile = false;
		mq = 0;
		annoCostruzione = 0;
	}

	public ImmobiliVO(ResultSet rs) throws SQLException{

		codImmobile = rs.getInt("CODIMMOBILE");
		rif = rs.getString("RIF");
		indirizzo = rs.getString("INDIRIZZO");
		ncivico = rs.getString("NCIVICO");
		provincia = rs.getString("PROVINCIA");
		cap = rs.getString("CAP");
		citta = rs.getString("CITTA");
		zona = rs.getString("ZONA");	
		dataInserimento = rs.getTimestamp("DATAINSERIMENTO");
		dataLibero = rs.getTimestamp("DATALIBERO");
		descrizione = rs.getString("DESCRIZIONE");
		mutuoDescrizione = rs.getString("MUTUODESCRIZIONE");
		prezzo = rs.getDouble("PREZZO");
		mutuo = rs.getDouble("MUTUO");
		spese = rs.getDouble("SPESE");
		varie = rs.getString("VARIE");
		visione = rs.getBoolean("VISIONE");
		storico = rs.getBoolean("STORICO");
		affittabile = rs.getBoolean("AFFITTO");
		mq = rs.getInt("MQ");
		annoCostruzione = rs.getInt("ANNOCOSTRUZIONE");
		codAgenteInseritore = rs.getInt("CODAGENTEINSERITORE");
		
		if (rs.wasNull()){
			codAgenteInseritore = null;
		}
		
		codAnagrafica = rs.getInt("CODANAGRAFICA");
		if (rs.wasNull()){
			codAnagrafica = null;
		}
		
		codRiscaldamento = rs.getInt("CODRISCALDAMENTO");
		if (rs.wasNull()){
			codRiscaldamento = null;
		}
		
		codStato = rs.getInt("CODSTATO");
		if (rs.wasNull()){
			codStato = null;
		}
		
		codTipologia = rs.getInt("CODTIPOLOGIA");
		if (rs.wasNull()){
			codTipologia = null;
		}
		
		codClasseEnergetica = rs.getInt("CODCLASSEENERGETICA");
		if (rs.wasNull()){
			codClasseEnergetica = null;
		}
		
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		

	}	
	
	public Integer getCodImmobile() {
		return codImmobile;
	}

	public void setCodImmobile(Integer codImmobile) {
		this.codImmobile = codImmobile;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getZona() {
		return zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public Date getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public Date getDataLibero() {
		return dataLibero;
	}

	public void setDataLibero(Date dataLibero) {
		this.dataLibero = dataLibero;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Double prezzo) {
		this.prezzo = prezzo;
	}

	public Double getMutuo() {
		return mutuo;
	}

	public void setMutuo(Double mutuo) {
		this.mutuo = mutuo;
	}

	public Double getSpese() {
		return spese;
	}

	public void setSpese(Double spese) {
		this.spese = spese;
	}

	public String getVarie() {
		return varie;
	}

	public void setVarie(String varie) {
		this.varie = varie;
	}

	public Boolean getVisione() {
		return visione;
	}

	public void setVisione(Boolean visione) {
		this.visione = visione;
	}

	public Boolean getStorico() {
		return storico;
	}

	public void setStorico(Boolean storico) {
		this.storico = storico;
	}

	public Integer getMq() {
		return mq;
	}

	public void setMq(Integer mq) {
		this.mq = mq;
	}


	public Integer getAnnoCostruzione() {
		return annoCostruzione;
	}

	public void setAnnoCostruzione(Integer annoCostruzione) {
		this.annoCostruzione = annoCostruzione;
	}

	public String getMutuoDescrizione() {
		return mutuoDescrizione;
	}

	public void setMutuoDescrizione(String mutuoDescrizione) {
		this.mutuoDescrizione = mutuoDescrizione;
	}

	public Integer getCodAgenteInseritore() {
		return codAgenteInseritore;
	}

	public void setCodAgenteInseritore(Integer codAgenteInseritore) {
		this.codAgenteInseritore = codAgenteInseritore;
	}

	public Integer getCodAnagrafica() {
		return codAnagrafica;
	}

	public void setCodAnagrafica(Integer codAnagrafica) {
		this.codAnagrafica = codAnagrafica;
	}

	public Integer getCodRiscaldamento() {
		return codRiscaldamento;
	}

	public void setCodRiscaldamento(Integer codRiscaldamento) {
		this.codRiscaldamento = codRiscaldamento;
	}

	public Integer getCodStato() {
		return codStato;
	}

	public void setCodStato(Integer codStato) {
		this.codStato = codStato;
	}

	public Integer getCodTipologia() {
		return codTipologia;
	}

	public void setCodTipologia(Integer codTipologia) {
		this.codTipologia = codTipologia;
	}

	public String getRif() {
		return rif;
	}

	public void setRif(String rif) {
		this.rif = rif;
	}

	public Boolean getAffittabile() {
		return affittabile;
	}

	public void setAffittabile(Boolean affittabile) {
		this.affittabile = affittabile;
	}

	public Integer getCodClasseEnergetica() {
		return codClasseEnergetica;
	}

	public void setCodClasseEnergetica(Integer codClasseEnergetica) {
		this.codClasseEnergetica = codClasseEnergetica;
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

	public String getNcivico() {
		return ncivico;
	}

	public void setNcivico(String ncivico) {
		this.ncivico = ncivico;
	}

}
