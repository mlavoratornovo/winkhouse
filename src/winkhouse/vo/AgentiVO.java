package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import winkhouse.util.WinkhouseUtils;

public class AgentiVO {

	private Integer codAgente = null;
	private String nome = null;
	private String cognome = null;
	private String indirizzo = null;
	private String provincia = null;
	private String cap = null;
	private String citta = null;
	private String username = null;
	private String password = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;
	
	public AgentiVO() {
		super();		
		nome = "";
		cognome = "";
		indirizzo = "";
		provincia = "";
		cap = "";
		citta = "";		
	}

	public AgentiVO(ResultSet rs) throws SQLException {
		super();		
		codAgente = rs.getInt("CODAGENTE");
		if (rs.wasNull()){
			codAgente = null;
		}
		
		nome = rs.getString("NOME");
		cognome = rs.getString("COGNOME");
		indirizzo = rs.getString("INDIRIZZO");
		provincia = rs.getString("PROVINCIA");
		cap = rs.getString("CAP");
		citta = rs.getString("CITTA");
		username = rs.getString("USERNAME");
		if (rs.getString("PASSWORD") != null){
			password = WinkhouseUtils.getInstance().DecryptStringStandard(rs.getString("PASSWORD"));
		}
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}
	
	public Integer getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(Integer codAgente) {
		this.codAgente = codAgente;
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

	@Override
	public String toString() {
		return getNome() + " " + getCognome() + " - " + getCitta();
	}

	
	public String getUsername() {
		return username;
	}

	
	public void setUsername(String username) {
		this.username = username;
	}

	
	public String getPassword() {
		return password;
	}

	
	public void setPassword(String password) {
		this.password = password;
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
