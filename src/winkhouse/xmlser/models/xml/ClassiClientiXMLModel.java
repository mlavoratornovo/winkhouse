package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.ClassiClientiVO;

public class ClassiClientiXMLModel extends ClassiClientiVO 
								   implements XMLSerializable {

	public ClassiClientiXMLModel(ClassiClientiVO ccVO) {
		setCodClasseCliente(ccVO.getCodClasseCliente());
		setDescrizione(ccVO.getDescrizione());
		setOrdine(ccVO.getOrdine());
	}

	public ClassiClientiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<ClassiClientiXMLModel> CLASSICLIENTI_XML = new XMLFormat<ClassiClientiXMLModel>(ClassiClientiXMLModel.class){
		
        public void write(ClassiClientiXMLModel ta_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codClasseCliente", ta_xml.getCodClasseCliente());
			xml.setAttribute("descrizione", ta_xml.getDescrizione());
			xml.setAttribute("ordine", ta_xml.getOrdine());        	
        }
        
        public void read(InputElement xml, ClassiClientiXMLModel ta_xml) throws XMLStreamException{
       }
        
   };	


}
