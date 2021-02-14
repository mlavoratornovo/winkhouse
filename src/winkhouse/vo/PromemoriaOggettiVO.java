package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PromemoriaOggettiVO implements Serializable{

	private Integer codPromemoria = null;
	private Integer codOggetto = null;
	private Integer tipo = null;
	
	public PromemoriaOggettiVO() {
		// TODO Auto-generated constructor stub
	}

	public PromemoriaOggettiVO(ResultSet rs) throws SQLException{
		setCodPromemoria(rs.getInt("CODPROMEMORIA"));
		setCodOggetto(rs.getInt("CODOGGETTO"));
		if (rs.wasNull()){
			setCodOggetto(null);
		}
		
		setTipo(rs.getInt("TIPO"));
	}

	public Integer getCodPromemoria() {
		return codPromemoria;
	}

	public void setCodPromemoria(Integer codPromemoria) {
		this.codPromemoria = codPromemoria;
	}

	public Integer getCodOggetto() {
		return codOggetto;
	}

	public void setCodOggetto(Integer codOggetto) {
		this.codOggetto = codOggetto;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
}
