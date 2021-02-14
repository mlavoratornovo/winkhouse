package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.TipologiaStanzeVO;

public class TipologiaStanzeXMLModel extends TipologiaStanzeVO 
									 implements XMLSerializable {

	public TipologiaStanzeXMLModel(TipologiaStanzeVO tsVO) {
		setCodTipologiaStanza(tsVO.getCodTipologiaStanza());
		setDescrizione(tsVO.getDescrizione());		
	}

	public TipologiaStanzeXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<TipologiaStanzeXMLModel> TIPIIMMOBILI_XML = new XMLFormat<TipologiaStanzeXMLModel>(TipologiaStanzeXMLModel.class){
		
        public void write(TipologiaStanzeXMLModel ts_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codTipologiaStanza", ts_xml.getCodTipologiaStanza());
			xml.setAttribute("descrizione", ts_xml.getDescrizione());			        	
        }
        
        public void read(InputElement xml, TipologiaStanzeXMLModel ts_xml) throws XMLStreamException{
       }
        
   };	

}
