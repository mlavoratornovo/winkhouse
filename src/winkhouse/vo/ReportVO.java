package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import winkhouse.util.WinkhouseUtils;


public class ReportVO implements Serializable{

	private Integer codReport = null;
	private String nome = null;
	private String tipo = null;
	private String template = null;
	private String descrizione = null;
	private Boolean isList = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	
	public static WinkhouseUtils.ReportTypes[] tipireport =  {WinkhouseUtils.getInstance(). new ReportTypes(WinkhouseUtils.IMMOBILI,"Immobili"),
																WinkhouseUtils.getInstance(). new ReportTypes(WinkhouseUtils.ANAGRAFICHE,"Anagrafiche"),
																WinkhouseUtils.getInstance(). new ReportTypes(WinkhouseUtils.COLLOQUI,"Colloqui"),
																WinkhouseUtils.getInstance(). new ReportTypes(WinkhouseUtils.APPUNTAMENTI,"Appuntamenti"),
																WinkhouseUtils.getInstance(). new ReportTypes(WinkhouseUtils.AFFITTI,"Affitti")};
	public static String[] stampabili =  {WinkhouseUtils.IMMOBILI,
										  WinkhouseUtils.ANAGRAFICHE,
										  WinkhouseUtils.COLLOQUI,
										  WinkhouseUtils.AGENTI,
										  WinkhouseUtils.AGENTICOLLOQUIO,
										  WinkhouseUtils.ALLEGATICOLLOQUIO,
										  WinkhouseUtils.ANAGRAFICHECOLLOQUIO,
										  WinkhouseUtils.CONTATTI,
										  WinkhouseUtils.IMMAGINI,
										  WinkhouseUtils.STANZEIMMOBILI};
	
	public ReportVO() {
		nome = "";
		tipo = "";
		template = "";
		descrizione = "";
		isList = Boolean.FALSE;
	}
	
	public ReportVO(ResultSet rs) throws SQLException{
		codReport = rs.getInt("CODREPORT");
		nome = rs.getString("NOME");
		tipo = rs.getString("TIPO");
		template = rs.getString("TEMPLATE");
		descrizione = rs.getString("DESCRIZIONE");
		isList = rs.getBoolean("ISLIST");
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		

	}

	public Integer getCodReport() {
		return codReport;
	}

	public void setCodReport(Integer codReport) {
		this.codReport = codReport;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Boolean getIsList() {
		return isList;
	}

	public void setIsList(Boolean isList) {
		this.isList = isList;
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
