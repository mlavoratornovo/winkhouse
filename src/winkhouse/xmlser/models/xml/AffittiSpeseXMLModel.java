package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AffittiSpeseModel;
import winkhouse.vo.AffittiSpeseVO;

public class AffittiSpeseXMLModel extends AffittiSpeseModel 
								  implements XMLSerializable {

	public AffittiSpeseXMLModel(AffittiSpeseVO asVO) {
		super(asVO);
	}

	public AffittiSpeseXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AffittiSpeseXMLModel> AFFITTIRATE_XML = new XMLFormat<AffittiSpeseXMLModel>(AffittiSpeseXMLModel.class){
		
        public void write(AffittiSpeseXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAffittiSpese", a_xml.getCodAffittiSpese());
			xml.setAttribute("descrizione", a_xml.getDescrizione());
			xml.setAttribute("codAffitto", a_xml.getCodAffitto());
			xml.setAttribute("codAnagrafica", a_xml.getCodAnagrafica());
			xml.setAttribute("codParent", a_xml.getCodParent());
			xml.setAttribute("dataFine",
							 ((a_xml.getDataFine() != null)
							  ? a_xml.getDataFine().toString()
							  : null));
			xml.setAttribute("dataInizio",
					 		 ((a_xml.getDataInizio() != null)
							  ? a_xml.getDataInizio().toString()
							  : null));
			
			xml.setAttribute("dataPagato",
					 		((a_xml.getDataPagato() != null)
							 ? a_xml.getDataPagato().toString()
							 : null));			
			xml.setAttribute("importo", a_xml.getImporto());
			xml.setAttribute("scadenza",
			 				((a_xml.getScadenza() != null)
			 				 ? a_xml.getScadenza().toString()
			 				 : null));			
			xml.setAttribute("versato", a_xml.getVersato());			
        }
                
        public void read(InputElement xml, AffittiSpeseXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	


}
