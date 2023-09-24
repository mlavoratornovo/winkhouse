package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.TipologieImmobiliVO;

public class TipologieImmobiliXMLModel extends TipologieImmobiliVO 
									   implements XMLSerializable {

	public TipologieImmobiliXMLModel(TipologieImmobiliVO tiVO) {
		setCodTipologiaImmobile(tiVO.getCodTipologiaImmobile());
		setDescrizione(tiVO.getDescrizione());
	}

	public TipologieImmobiliXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<TipologieImmobiliXMLModel> TIPIIMMOBILI_XML = new XMLFormat<TipologieImmobiliXMLModel>(TipologieImmobiliXMLModel.class){
		
        public void write(TipologieImmobiliXMLModel ti_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codTipologiaImmobile", ti_xml.getCodTipologiaImmobile());
			xml.setAttribute("descrizione", ti_xml.getDescrizione());			        	
        }
        
        public void read(InputElement xml, TipologieImmobiliXMLModel ti_xml) throws XMLStreamException{
       }
        
   };	


}
