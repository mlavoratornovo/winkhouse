package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javolution.util.FastList;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.ColloquiModel;
import winkhouse.vo.ColloquiCriteriRicercaVO;
import winkhouse.vo.ColloquiVO;

public class ColloquiXMLModel extends ColloquiModel implements XMLSerializable {

	public ColloquiXMLModel(ColloquiVO cVO) {
		super(cVO);
	}

	public ColloquiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<ColloquiXMLModel> COLLOQUIO_XML = new XMLFormat<ColloquiXMLModel>(ColloquiXMLModel.class){
		
        public void write(ColloquiXMLModel c_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codColloquio", c_xml.getCodColloquio());
        	xml.setAttribute("scadenziere", c_xml.getScadenziere());
        	xml.setAttribute("codAgenteInseritore", c_xml.getCodAgenteInseritore());
        	xml.setAttribute("codImmobileAbbinato", c_xml.getCodImmobileAbbinato());
        	xml.setAttribute("codParent", c_xml.getCodParent());
        	xml.setAttribute("codTipologiaColloquio", c_xml.getCodTipologiaColloquio());
        	xml.setAttribute("descrizione", c_xml.getDescrizione());
        	xml.setAttribute("commentoCliente", c_xml.getCommentoCliente());
        	xml.setAttribute("commentoAgenzia", c_xml.getCommentoAgenzia());
        	xml.setAttribute("iCalUid", c_xml.getiCalUid());
        	xml.setAttribute("luogoIncontro", c_xml.getLuogoIncontro());
        	xml.setAttribute("dataColloquio", 
        					 ((c_xml.getDataColloquio() != null)
        					  ? c_xml.getDataColloquio().toString()
        					  : null));
        	xml.setAttribute("dataInserimento", 
					 		 ((c_xml.getDataInserimento() != null)
					 		  ? c_xml.getDataInserimento().toString()
					 	      : null));

        }
                
        public void read(InputElement xml, ColloquiXMLModel c_xml) throws XMLStreamException{
       }
        
   };	

	
}
