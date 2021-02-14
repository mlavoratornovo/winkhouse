package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AppuntamentiModel;
import winkhouse.vo.AppuntamentiVO;

public class AppuntamentiXMLModel extends AppuntamentiModel 
								  implements XMLSerializable {

	public AppuntamentiXMLModel(AppuntamentiVO appuntamento) {
		super(appuntamento);
	}

	public AppuntamentiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AppuntamentiXMLModel> APPUNTAMENTI_XML = new XMLFormat<AppuntamentiXMLModel>(AppuntamentiXMLModel.class){
		
        public void write(AppuntamentiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAppuntamento", a_xml.getCodAppuntamento());
        	xml.setAttribute("codPadre", a_xml.getCodPadre());
        	xml.setAttribute("codTipoAppuntamento", a_xml.getCodTipoAppuntamento());
        	xml.setAttribute("dataInserimento", 
        					 ((a_xml.getDataInserimento() != null)
        					  ? a_xml.getDataInserimento().toString()
        					  : null));
        	xml.setAttribute("dataAppuntamento", 
        					 ((a_xml.getDataAppuntamento() != null)
        					  ? a_xml.getDataAppuntamento().toString()
        					  : null));
        	xml.setAttribute("dataFineAppuntamento", 
        					 ((a_xml.getDataFineAppuntamento() != null)
        					  ? a_xml.getDataFineAppuntamento().toString()
        					  : null));

        	xml.setAttribute("descrizione", a_xml.getDescrizione());
        	xml.setAttribute("luogo", a_xml.getLuogo());
        	xml.setAttribute("iCalUID", a_xml.getiCalUID());
        }
                
        public void read(InputElement xml, AppuntamentiXMLModel c_xml) throws XMLStreamException{
       }
        
   };	
	
	
}
