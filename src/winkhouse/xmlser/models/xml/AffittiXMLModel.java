package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AffittiModel;
import winkhouse.vo.AffittiVO;

public class AffittiXMLModel extends AffittiModel implements XMLSerializable {

	public AffittiXMLModel(AffittiVO affitti) {
		super(affitti);
	}

	public AffittiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AffittiXMLModel> AFFITTI_XML = new XMLFormat<AffittiXMLModel>(AffittiXMLModel.class){
		
        public void write(AffittiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAffitti", a_xml.getCodAffitti());
        	xml.setAttribute("codImmobile", a_xml.getCodImmobile());
        	xml.setAttribute("codAgenteIns", a_xml.getCodAgenteIns());        	
        	xml.setAttribute("descrizione", a_xml.getDescrizione());
        	xml.setAttribute("cauzione", a_xml.getCauzione());
        	xml.setAttribute("rata", a_xml.getRata());
        	xml.setAttribute("dataInizio", 
        					 ((a_xml.getDataInizio() != null)
        					  ? a_xml.getDataInizio().toString()
        					  : null));
        	xml.setAttribute("dataFine",
					 		 ((a_xml.getDataFine() != null)
					 		  ? a_xml.getDataFine().toString()
					 		  : null));
        	
        }
                
        public void read(InputElement xml, AffittiXMLModel c_xml) throws XMLStreamException{
       }
        
   };	

}
