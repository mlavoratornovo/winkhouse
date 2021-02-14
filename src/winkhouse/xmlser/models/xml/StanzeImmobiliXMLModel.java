package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.StanzeImmobiliModel;
import winkhouse.vo.StanzeImmobiliVO;

public class StanzeImmobiliXMLModel extends StanzeImmobiliModel 
									implements XMLSerializable {


	public StanzeImmobiliXMLModel(StanzeImmobiliVO siVO) {
		super(siVO);
	}

	public StanzeImmobiliXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<StanzeImmobiliXMLModel> STANZEIMMOBILI_XML = new XMLFormat<StanzeImmobiliXMLModel>(StanzeImmobiliXMLModel.class){
		
        public void write(StanzeImmobiliXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codStanzeImmobili", a_xml.getCodStanzeImmobili());
			xml.setAttribute("codTipologiaStanza", a_xml.getCodTipologiaStanza());
			xml.setAttribute("codImmobile", a_xml.getCodImmobile());
			xml.setAttribute("mq", a_xml.getMq());			
        }
                
        public void read(InputElement xml, StanzeImmobiliXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	


}
