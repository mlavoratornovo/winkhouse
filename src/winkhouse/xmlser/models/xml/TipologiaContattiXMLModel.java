package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.TipologiaContattiVO;

public class TipologiaContattiXMLModel extends TipologiaContattiVO 
									   implements XMLSerializable {

	public TipologiaContattiXMLModel(TipologiaContattiVO tcVO) {
		setCodTipologiaContatto(tcVO.getCodTipologiaContatto());
		setDescrizione(tcVO.getDescrizione());
	}

	public TipologiaContattiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<TipologiaContattiXMLModel> RISCALDAMENTI_XML = new XMLFormat<TipologiaContattiXMLModel>(TipologiaContattiXMLModel.class){
		
        public void write(TipologiaContattiXMLModel tc_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codTipologiaContatto", tc_xml.getCodTipologiaContatto());
			xml.setAttribute("descrizione", tc_xml.getDescrizione());			        	
        }
        
        public void read(InputElement xml, TipologiaContattiXMLModel tc_xml) throws XMLStreamException{
       }
        
   };	


}
