package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AnagraficheVO implements Serializable{

	private Integer codAnagrafica = null;
	private String nome = null;
	private String cognome = null;
	private String ragioneSociale = null;
	private String indirizzo = null;
	private String ncivico = null;
	private String provincia = null;
	private String cap = null;
	private String citta = null;
	private Date dataInserimento = null;
	private String commento = null;
	private Boolean storico = null;
	private Integer codClasseCliente = null;
	private Integer codAgenteInseritore = null;	
	private String codiceFiscale = null;
	private String partitaIva = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public AnagraficheVO() {
		super();		
		nome = "";
		cognome = "";
		ragioneSociale = "";
		indirizzo = "";
		provincia = "";
		cap = "";
		citta = "";
		dataInserimento = new Date();
		commento = "";
		storico = false;
		codiceFiscale = null;
		partitaIva = null;		
	}	

	public AnagraficheVO(ResultSet rs) throws SQLException {
		super();
		codAnagrafica = rs.getInt("CODANAGRAFICA");
		if (rs.wasNull()){
			codAnagrafica = null;
		}
		nome = rs.getString("NOME");
		cognome = rs.getString("COGNOME");
		ragioneSociale = rs.getString("RAGSOC");
		indirizzo = rs.getString("INDIRIZZO");
		ncivico = rs.getString("NCIVICO");
		provincia = rs.getString("PROVINCIA");
		cap = rs.getString("CAP");
		citta = rs.getString("CITTA");
		dataInserimento = rs.getTimestamp("DATAINSERIMENTO");
		commento = rs.getString("COMMENTO");
		storico = rs.getBoolean("STORICO");
		
		codClasseCliente = rs.getInt("CODCLASSECLIENTE");
		if (rs.wasNull()){
			codClasseCliente = null;
		}
		
		codAgenteInseritore = rs.getInt("CODAGENTEINSERITORE");
		if (rs.wasNull()){
			codAgenteInseritore = null;
		}
		
		codiceFiscale = rs.getString("CODICEFISCALE");
		partitaIva = rs.getString("PIVA");			
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}	
	
	public Integer getCodAnagrafica() {
		return codAnagrafica;
	}

	public void setCodAnagrafica(Integer codAnagrafica) {
		this.codAnagrafica = codAnagrafica;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
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

	public Date getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public String getCommento() {
		return commento;
	}

	public void setCommento(String commento) {
		this.commento = commento;
	}

	public Boolean getStorico() {
		return storico;
	}

	public void setStorico(Boolean storico) {
		this.storico = storico;
	}
	
	public Integer getCodClasseCliente() {
		return codClasseCliente;
	}

	public void setCodClasseCliente(Integer codClasseCliente) {
		this.codClasseCliente = codClasseCliente;
	}

	public Integer getCodAgenteInseritore() {
		return codAgenteInseritore;
	}

	public void setCodAgenteInseritore(Integer codAgenteInseritore) {
		this.codAgenteInseritore = codAgenteInseritore;
	}

	@Override
	public String toString() {		
		return getNome() + " " + getCognome() + " - " + getCitta() + " " + getIndirizzo();
	}

	public String getRagioneSociale() {
		return this.ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
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
