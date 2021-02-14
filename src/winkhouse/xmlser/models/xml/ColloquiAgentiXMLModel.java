package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.ColloquiAgentiVO;

public class ColloquiAgentiXMLModel extends ColloquiAgentiVO 
									implements XMLSerializable {

	public ColloquiAgentiXMLModel(ColloquiAgentiVO caVO) {
		setCodAgente(caVO.getCodAgente());
		setCodColloquio(caVO.getCodColloquio());
		setCodColloquioAgenti(caVO.getCodColloquioAgenti());
		setCommento(caVO.getCommento());
	}

	public ColloquiAgentiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<ColloquiAgentiXMLModel> COLLOQUIAGENTI_XML = new XMLFormat<ColloquiAgentiXMLModel>(ColloquiAgentiXMLModel.class){
		
        public void write(ColloquiAgentiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAgente", a_xml.getCodAgente());
			xml.setAttribute("codColloquio", a_xml.getCodColloquio());
			xml.setAttribute("codColloquioAgenti", a_xml.getCodColloquioAgenti());
			xml.setAttribute("commento", a_xml.getCommento());
        }
                
        public void read(InputElement xml, ColloquiAgentiXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	


}
