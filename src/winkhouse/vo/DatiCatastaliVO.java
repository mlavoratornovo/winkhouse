package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DatiCatastaliVO implements Serializable{
	
	private Integer codDatiCatastali = null;
	private String foglio = null;
	private String particella = null;
	private String subalterno = null;
	private String categoria = null;
	private Double rendita = null;
	private Double redditoDomenicale = null;
	private Double redditoAgricolo = null;
	private Double dimensione = null;
	private Integer codImmobile = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public DatiCatastaliVO() {
		super();
		foglio = "";
		particella = "";
		subalterno = "";
		categoria = "";
		rendita = 0.0;
		redditoDomenicale = 0.0;
		redditoAgricolo = 0.0;
		dimensione = 0.0;
	}

	public DatiCatastaliVO(ResultSet rs) throws SQLException {
		super();
		codDatiCatastali = rs.getInt("CODDATICATASTALI");
		foglio = rs.getString("FOGLIO");
		particella = rs.getString("PARTICELLA");
		subalterno = rs.getString("SUBALTERNO");
		categoria = rs.getString("CATEGORIA");
		rendita = rs.getDouble("RENDITA");
		redditoDomenicale = rs.getDouble("REDDITODOMENICALE");
		redditoAgricolo = rs.getDouble("REDDITOAGRARIO");
		dimensione = rs.getDouble("DIMENSIONE");
		codImmobile = rs.getInt("CODIMMOBILE");
		if (rs.wasNull()){
			codImmobile = null;
		}
		
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		

	}

	public Integer getCodDatiCatastali() {
		return codDatiCatastali;
	}

	public void setCodDatiCatastali(Integer codDatiCatastali) {
		this.codDatiCatastali = codDatiCatastali;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getSubalterno() {
		return subalterno;
	}

	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Double getRendita() {
		return rendita;
	}

	public void setRendita(Double rendita) {
		this.rendita = rendita;
	}

	public Double getRedditoDomenicale() {
		return redditoDomenicale;
	}

	public void setRedditoDomenicale(Double redditoDomenicale) {
		this.redditoDomenicale = redditoDomenicale;
	}

	public Double getRedditoAgricolo() {
		return redditoAgricolo;
	}

	public void setRedditoAgricolo(Double redditoAgricolo) {
		this.redditoAgricolo = redditoAgricolo;
	}

	public Double getDimensione() {
		return dimensione;
	}

	public void setDimensione(Double dimensione) {
		this.dimensione = dimensione;
	}

	public Integer getCodImmobile() {
		return codImmobile;
	}

	public void setCodImmobile(Integer codImmobile) {
		this.codImmobile = codImmobile;
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
