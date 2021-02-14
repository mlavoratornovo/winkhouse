package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.RicercheModel;
import winkhouse.vo.RicercheVO;

public class RicercheXMLModel extends RicercheModel implements XMLSerializable {

	public RicercheXMLModel(RicercheVO rVO){
		setCodRicerca(rVO.getCodRicerca());
		setTipo(rVO.getTipo());
		setDescrizione(rVO.getDescrizione());
		setNome(rVO.getNome());		
	}

	public RicercheXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<RicercheXMLModel> RICERCHE_XML = new XMLFormat<RicercheXMLModel>(RicercheXMLModel.class){
		
        public void write(RicercheXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codRicerca", a_xml.getCodRicerca());
			xml.setAttribute("nome", a_xml.getNome());
			xml.setAttribute("descrizione", a_xml.getDescrizione());
			xml.setAttribute("tipo", a_xml.getTipo());			
        }
                
        public void read(InputElement xml, RicercheXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	


}
