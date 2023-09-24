package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AgentiAppuntamentiModel;
import winkhouse.vo.AgentiAppuntamentiVO;

public class AgentiAppuntamentiXMLModel extends AgentiAppuntamentiModel
										implements XMLSerializable {

	public AgentiAppuntamentiXMLModel(AgentiAppuntamentiVO apVO) {
		setCodAgente(apVO.getCodAgente());
		setCodAgentiAppuntamenti(apVO.getCodAgentiAppuntamenti());
		setCodAppuntamento(apVO.getCodAppuntamento());
		setNote(apVO.getNote());
	}

	public AgentiAppuntamentiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<AgentiAppuntamentiXMLModel> AGENTIAPPUNTAMENTI_XML = new XMLFormat<AgentiAppuntamentiXMLModel>(AgentiAppuntamentiXMLModel.class){
		
        public void write(AgentiAppuntamentiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAgentiAppuntamenti", a_xml.getCodAgentiAppuntamenti());
			xml.setAttribute("codAgente", a_xml.getCodAgente());
			xml.setAttribute("codAppuntamento", a_xml.getCodAppuntamento());			
			xml.setAttribute("note", a_xml.getNote());
        }
                
        public void read(InputElement xml, AgentiAppuntamentiXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	


}
