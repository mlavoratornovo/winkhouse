package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AllegatiColloquiModel;
import winkhouse.vo.AllegatiColloquiVO;

public class AllegatiColloquiXMLModel extends AllegatiColloquiModel 
									  implements XMLSerializable {

	public AllegatiColloquiXMLModel(AllegatiColloquiVO acVO) {
		setCodAllegatiColloquio(acVO.getCodAllegatiColloquio());
		setCodColloquio(acVO.getCodColloquio());
		setDescrizione(acVO.getDescrizione());
		setFromPath(acVO.getFromPath());
		setNome(acVO.getNome());
	}

	public AllegatiColloquiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<AllegatiColloquiXMLModel> ALLEGATICOLLOQUI_XML = new XMLFormat<AllegatiColloquiXMLModel>(AllegatiColloquiXMLModel.class){
		
        public void write(AllegatiColloquiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAllegatiColloquio", a_xml.getCodAllegatiColloquio());
			xml.setAttribute("codColloquio", a_xml.getCodColloquio());
			xml.setAttribute("descrizione", a_xml.getDescrizione());
			xml.setAttribute("fromPath", a_xml.getFromPath());
			xml.setAttribute("nome", a_xml.getNome());
        }
                
        public void read(InputElement xml, AllegatiColloquiXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	


}
