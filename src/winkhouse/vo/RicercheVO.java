package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class RicercheVO implements Serializable{
	
	final public static int RICERCHE_IMMOBILI = 1;
	final public static int RICERCHE_IMMOBILI_AFFITTI = 2;
	final public static int RICERCHE_ANAGRAFICHE = 3;
	final public static int RICERCHE_COLLOQUI = 7;

	final public static int PERMESSI_IMMOBILI = 4;
	final public static int PERMESSI_IMMOBILI_AFFITTI = 5;
	final public static int PERMESSI_ANAGRAFICHE = 6;
	final public static int PERMESSI_COLLOQUI = 8;
	
	private Integer codRicerca = null;
	private String nome = null;
	private String descrizione = null;
	private Integer tipo = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public RicercheVO() {		
		nome = "";
		descrizione = "";
		tipo = 0;
	}
	
	public RicercheVO(ResultSet rs) throws SQLException {
		codRicerca = rs.getInt("CODRICERCA");
		nome = rs.getString("NOME");
		descrizione = rs.getString("DESCRIZIONE");
		tipo = rs.getInt("TIPO");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		

	}

	public Integer getCodRicerca() {
		return codRicerca;
	}

	public void setCodRicerca(Integer codRicerca) {
		this.codRicerca = codRicerca;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
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
