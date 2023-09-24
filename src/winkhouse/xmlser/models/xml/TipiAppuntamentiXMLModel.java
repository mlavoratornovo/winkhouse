package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.TipiAppuntamentiVO;

public class TipiAppuntamentiXMLModel extends TipiAppuntamentiVO 
									  implements XMLSerializable{

	public TipiAppuntamentiXMLModel(TipiAppuntamentiVO taVO) {
		setCodTipoAppuntamento(taVO.getCodTipoAppuntamento());
		setDescrizione(taVO.getDescrizione());
		setgCalColor(taVO.getgCalColor());
		setOrdine(taVO.getOrdine());
	}

	public TipiAppuntamentiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<TipiAppuntamentiXMLModel> TIPIAPPUNTAMENTI_XML = new XMLFormat<TipiAppuntamentiXMLModel>(TipiAppuntamentiXMLModel.class){
		
        public void write(TipiAppuntamentiXMLModel ta_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codTipoAppuntamento", ta_xml.getCodTipoAppuntamento());
			xml.setAttribute("descrizione", ta_xml.getDescrizione());
			xml.setAttribute("gCalColor", ta_xml.getgCalColor());
			xml.setAttribute("ordine", ta_xml.getOrdine());        	
        }
        
        public void read(InputElement xml, TipiAppuntamentiXMLModel ta_xml) throws XMLStreamException{
       }
        
   };	

}
