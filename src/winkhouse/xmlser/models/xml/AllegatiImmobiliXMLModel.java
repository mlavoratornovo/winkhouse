package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.AllegatiImmobiliVO;

public class AllegatiImmobiliXMLModel extends AllegatiImmobiliVO 
									  implements XMLSerializable {

	public AllegatiImmobiliXMLModel(AllegatiImmobiliVO aiVO) {
		setCodAllegatiImmobili(aiVO.getCodAllegatiImmobili());
		setCodImmobile(aiVO.getCodImmobile());
		setCommento(aiVO.getCommento());
		setFromPath(aiVO.getFromPath());
		setNome(aiVO.getNome());
	}

	public AllegatiImmobiliXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AllegatiImmobiliXMLModel> AFFITTIRATE_XML = new XMLFormat<AllegatiImmobiliXMLModel>(AllegatiImmobiliXMLModel.class){
		
        public void write(AllegatiImmobiliXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAllegatiImmobili", a_xml.getCodAllegatiImmobili());
			xml.setAttribute("codImmobile", a_xml.getCodImmobile());
			xml.setAttribute("nome", a_xml.getNome());
			xml.setAttribute("commento", a_xml.getCommento());
			xml.setAttribute("fromPath", a_xml.getFromPath());
        }
                
        public void read(InputElement xml, AllegatiImmobiliXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	

}
