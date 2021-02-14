package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.AffittiAllegatiVO;

public class AffittiAllegatiXMLModel extends AffittiAllegatiVO implements
		XMLSerializable {

	public AffittiAllegatiXMLModel(AffittiAllegatiVO aaVO) {
		setCodAffittiAllegati(aaVO.getCodAffittiAllegati());
		setCodAffitto(aaVO.getCodAffitto());
		setDescrizione(aaVO.getDescrizione());
		setNome(aaVO.getNome());
		setFromPath(aaVO.getFromPath());
	}

	public AffittiAllegatiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<AffittiAllegatiXMLModel> AFFITTIALLEGATI_XML = new XMLFormat<AffittiAllegatiXMLModel>(AffittiAllegatiXMLModel.class){
		
        public void write(AffittiAllegatiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAffittiAllegati", a_xml.getCodAffittiAllegati());
			xml.setAttribute("CodAffitto", a_xml.getCodAffitto());
			xml.setAttribute("nome", a_xml.getNome());
			xml.setAttribute("descrizione", a_xml.getDescrizione());
			xml.setAttribute("fromPath", a_xml.getFromPath());
        }
                
        public void read(InputElement xml, AffittiAllegatiXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	


}
