package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AffittiRateVO implements Serializable{

	private Integer CodAffittiRate = null;
	private Integer CodAffitto = null;
	private Integer CodParent = null;
	private Integer Mese = null;
	private Date Scadenza = null;
	private Date DataPagato = null;
	private Double Importo = null;
	private Integer CodAnagrafica = null;
	private String Nota = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;

	public AffittiRateVO() {
		Calendar c = Calendar.getInstance(Locale.ITALIAN);
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		Mese = new Integer(0);
		Scadenza = c.getTime();
		DataPagato = c.getTime();
		Importo = new Double(0.0);
		Nota = "";		
	}
	
	public AffittiRateVO(AffittiRateVO arVO) {
		CodAffittiRate = arVO.getCodAffittiRate();
		CodAffitto = arVO.getCodAffitto();
		CodParent = arVO.getCodParent();
		Mese = arVO.getMese();
		Scadenza = arVO.getScadenza();
		DataPagato = arVO.getDataPagato();
		Importo = arVO.getImporto();
		CodAnagrafica = arVO.getCodAnagrafica();
		Nota = arVO.getNota();		
		codUserUpdate = arVO.getCodUserUpdate();
		dateUpdate = arVO.getDateUpdate();		
		
	}
	
	public AffittiRateVO(ResultSet rs) throws SQLException {
		CodAffittiRate = new Integer(rs.getInt("CODAFFITTIRATE"));
		if (rs.wasNull()){
			CodAffittiRate = null;
		}
		
		CodAffitto = new Integer(rs.getInt("CODAFFITTO"));
		if (rs.wasNull()){
			CodAffitto = null;
		}
		
		CodParent = new Integer(rs.getInt("CODPARENT"));
		if (rs.wasNull()){
			CodParent = null;
		}
		
		Mese = new Integer(rs.getInt("MESE"));
		Scadenza = rs.getTimestamp("SCADENZA");
		DataPagato = rs.getTimestamp("DATAPAGATO");
		Importo = new Double(rs.getDouble("IMPORTO"));
		CodAnagrafica = new Integer(rs.getInt("CODANAGRAFICA"));
		Nota = rs.getString("NOTA");		
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}

	public Integer getCodAffittiRate() {
		return CodAffittiRate;
	}

	public void setCodAffittiRate(Integer codAffittiRate) {
		CodAffittiRate = codAffittiRate;
	}

	public Integer getCodAffitto() {
		return CodAffitto;
	}

	public void setCodAffitto(Integer codAffitto) {
		CodAffitto = codAffitto;
	}

	public Integer getCodParent() {
		return CodParent;
	}

	public void setCodParent(Integer codParent) {
		CodParent = codParent;
	}

	public Integer getMese() {
		return Mese;
	}

	public void setMese(Integer mese) {
		Mese = mese;
	}

	public Date getScadenza() {
		return Scadenza;
	}

	public void setScadenza(Date scadenza) {
		Scadenza = scadenza;
	}

	public Date getDataPagato() {
		return DataPagato;
	}

	public void setDataPagato(Date dataPagato) {
		DataPagato = dataPagato;
	}

	public Double getImporto() {
		return Importo;
	}

	public void setImporto(Double importo) {
		Importo = importo;
	}

	public Integer getCodAnagrafica() {
		return CodAnagrafica;
	}

	public void setCodAnagrafica(Integer codAnagrafica) {
		CodAnagrafica = codAnagrafica;
	}

	public String getNota() {
		return Nota;
	}

	public void setNota(String nota) {
		Nota = nota;
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
