package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.ContattiModel;
import winkhouse.vo.ContattiVO;
import winkhouse.vo.TipologiaContattiVO;

public class ContattiXMLModel extends ContattiModel implements XMLSerializable {

	public ContattiXMLModel(ContattiVO cVO) {
		super(cVO);
	}

	public ContattiXMLModel(ResultSet rs) throws SQLException {
		super(rs);	
	}

	protected static final XMLFormat<ContattiXMLModel> CONTATTI_XML = new XMLFormat<ContattiXMLModel>(ContattiXMLModel.class){
		
        public void write(ContattiXMLModel c_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codContatto", c_xml.getCodContatto());
			xml.setAttribute("descrizione", c_xml.getDescrizione());
			xml.setAttribute("codTipologiaContatto", c_xml.getCodTipologiaContatto());
			xml.setAttribute("codAnagrafica", c_xml.getCodAnagrafica());
			xml.setAttribute("codAgente", c_xml.getCodAgente());
			xml.setAttribute("contatto", c_xml.getContatto());
        }
                
        public void read(InputElement xml, ContattiXMLModel c_xml) throws XMLStreamException{
       }
        
   };	

	
}
