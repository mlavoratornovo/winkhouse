package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PromemoriaLinksVO implements Serializable{

	private Integer codPromemoria = null;
	private Boolean isFile = false;
	private String descrizione = null;
	private String urilink = null;
	
	public PromemoriaLinksVO() {
		setDescrizione("");
		setUrilink("");
	}

	public PromemoriaLinksVO(ResultSet rs) throws SQLException{
		setCodPromemoria(rs.getInt("CODPROMEMORIA"));
		setDescrizione(rs.getString("DESCRIZIONE"));
		setUrilink(rs.getString("URILINK"));
		setIsFile(rs.getBoolean("ISFILE"));
	}

	public Integer getCodPromemoria() {
		return codPromemoria;
	}

	public void setCodPromemoria(Integer codPromemoria) {
		this.codPromemoria = codPromemoria;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getUrilink() {
		return urilink;
	}

	public void setUrilink(String urilink) {
		this.urilink = urilink;
	}

	
	public Boolean getIsFile() {
		return isFile;
	}
	

	public void setIsFile(Boolean isFile) {
		this.isFile = isFile;
	}

}
