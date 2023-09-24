package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.StatoConservativoVO;

public class StatoConservativoXMLModel extends StatoConservativoVO 
									   implements XMLSerializable {

	public StatoConservativoXMLModel(StatoConservativoVO scVO) {
		setCodStatoConservativo(scVO.getCodStatoConservativo());
		setDescrizione(scVO.getDescrizione());
	}

	public StatoConservativoXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<StatoConservativoXMLModel> TIPIIMMOBILI_XML = new XMLFormat<StatoConservativoXMLModel>(StatoConservativoXMLModel.class){
		
        public void write(StatoConservativoXMLModel sc_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codStatoConservativo", sc_xml.getCodStatoConservativo());
			xml.setAttribute("descrizione", sc_xml.getDescrizione());			        	
        }
        
        public void read(InputElement xml, StatoConservativoXMLModel sc_xml) throws XMLStreamException{
       }
        
   };	
}
