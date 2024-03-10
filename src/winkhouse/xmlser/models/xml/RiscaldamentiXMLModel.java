package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.orm.Riscaldamenti;
import winkhouse.vo.RiscaldamentiVO;

public class RiscaldamentiXMLModel extends Riscaldamenti 
								   implements XMLSerializable {

	public RiscaldamentiXMLModel(Riscaldamenti rVO) {
		setCodRiscaldamento(rVO.getCodRiscaldamento());
		setDescrizione(rVO.getDescrizione());
	}

//	public RiscaldamentiXMLModel(ResultSet rs) throws SQLException {
//		super(rs);
//	}
	
	protected static final XMLFormat<RiscaldamentiXMLModel> RISCALDAMENTI_XML = new XMLFormat<RiscaldamentiXMLModel>(RiscaldamentiXMLModel.class){
		
        public void write(RiscaldamentiXMLModel ta_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codRiscaldamento", ta_xml.getCodRiscaldamento());
			xml.setAttribute("descrizione", ta_xml.getDescrizione());			        	
        }
        
        public void read(InputElement xml, RiscaldamentiXMLModel ta_xml) throws XMLStreamException{
       }
        
   };	


}
