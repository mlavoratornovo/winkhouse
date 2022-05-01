package winkhouse.vo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;

import org.apache.cayenne.Cayenne;

import winkhouse.orm.Tipologiecolloqui;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

public class TipologieColloquiVO implements XMLSerializable,Serializable{

	private Integer codTipologiaColloquio = null;
	private String descrizione = null;
	private Integer codUserUpdate = null;
	private Date dateUpdate = null;
	
	public TipologieColloquiVO() {
		descrizione = "";				
	}
	
	public TipologieColloquiVO(ResultSet rs) throws SQLException {
		codTipologiaColloquio = rs.getInt("CODTIPOLOGIACOLLOQUIO");
		descrizione = rs.getString("DESCRIZIONE");		
		codUserUpdate = rs.getInt("CODUSERUPDATE");
		if (rs.wasNull()){
			codUserUpdate = null;
		}
		
		dateUpdate = rs.getTimestamp("DATEUPDATE");		
		
	}
	
	public TipologieColloquiVO(Tipologiecolloqui rs){
		codTipologiaColloquio = (int) Cayenne.longPKForObject(rs);
		descrizione = rs.getDescrizione();		
		codUserUpdate = (rs.getAgenti() != null)?(int) Cayenne.longPKForObject(rs):null;
		
		dateUpdate = (rs.getDateupdate() != null)?java.util.Date.from(rs.getDateupdate()
				.atZone(ZoneId.systemDefault()).toInstant()):null;
		
	}

	protected static final XMLFormat<TipologieColloquiVO> XML = new XMLFormat<TipologieColloquiVO>(TipologieColloquiVO.class) {
        public void write(TipologieColloquiVO tcVO, OutputElement xml) throws XMLStreamException {
            xml.setAttribute("codtipologiacolloqui",tcVO.getCodTipologiaColloquio());
            xml.setAttribute("descrizione",tcVO.getDescrizione());            
        }
        public void read(InputElement xml, TipologieColloquiVO tcVO) throws XMLStreamException {
            tcVO.setCodTipologiaColloquio(xml.getAttribute("codtipologiacolloqui", new Integer(0)));
            tcVO.setDescrizione(xml.getAttribute("descrizione", "tipologia colloquio"));
       }
   };

	
	public Integer getCodTipologiaColloquio() {
		return codTipologiaColloquio;
	}

	public void setCodTipologiaColloquio(Integer codTipologiaColloquio) {
		this.codTipologiaColloquio = codTipologiaColloquio;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public String toString() {		
		return getDescrizione();
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
