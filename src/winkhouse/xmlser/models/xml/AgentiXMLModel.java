package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javolution.util.FastList;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AgentiModel;
import winkhouse.model.ContattiModel;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.ContattiVO;


public class AgentiXMLModel extends AgentiModel implements XMLSerializable {

	public AgentiXMLModel(AgentiVO agentiVO) {
		super(agentiVO);
	}

	public AgentiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<AgentiXMLModel> AGENTI_XML = new XMLFormat<AgentiXMLModel>(AgentiXMLModel.class){
		
        public void write(AgentiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAgente", a_xml.getCodAgente());
			xml.setAttribute("nome", a_xml.getNome());
			xml.setAttribute("cognome", a_xml.getCognome());
			xml.setAttribute("provincia", a_xml.getProvincia());
			xml.setAttribute("cap", a_xml.getCap());
			xml.setAttribute("citta", a_xml.getCitta());
			xml.setAttribute("indirizzo", a_xml.getIndirizzo());
        }
                
        public void read(InputElement xml, AgentiXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	


}
