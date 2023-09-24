package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ComuniVO implements Serializable{

	private Integer codComune = null;
	private String codIstat = null;
	private String comune = null;
	private String provincia = null;
	private String regione = null;
	private String prefisso = null;
	private String cap = null;
	private String codFisco = null;
	private Integer abitanti = null;
	private String link = null;
	
	public ComuniVO() {

	}

	public ComuniVO(ResultSet rs) throws SQLException {
		setCodComune(rs.getInt("CODCOMUNE"));
		setCodIstat(rs.getString("CODISTAT"));
		setComune(rs.getString("COMUNE"));
		setProvincia(rs.getString("PROVINCIA"));
		setRegione(rs.getString("REGIONE"));
		setPrefisso(rs.getString("PREFISSO"));
		setCap(rs.getString("CAP"));
		setCodFisco(rs.getString("CODFISCO"));
		setAbitanti(rs.getInt("ABITANTI"));
		setLink(rs.getString("LINK"));
	}

	public Integer getCodComune() {
		return codComune;
	}

	public void setCodComune(Integer codComune) {
		this.codComune = codComune;
	}

	public String getCodIstat() {
		return codIstat;
	}

	public void setCodIstat(String codIstat) {
		this.codIstat = codIstat;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getRegione() {
		return regione;
	}

	public void setRegione(String regione) {
		this.regione = regione;
	}

	public String getPrefisso() {
		return prefisso;
	}

	public void setPrefisso(String prefisso) {
		this.prefisso = prefisso;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCodFisco() {
		return codFisco;
	}

	public void setCodFisco(String codFisco) {
		this.codFisco = codFisco;
	}

	public Integer getAbitanti() {
		return abitanti;
	}

	public void setAbitanti(Integer abitanti) {
		this.abitanti = abitanti;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}
