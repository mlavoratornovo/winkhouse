package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.ImmobiliModel;
import winkhouse.vo.ImmobiliVO;

public class ImmobiliXMLModel extends ImmobiliModel 
							  implements XMLSerializable {


	public ImmobiliXMLModel(ImmobiliVO iVO) {
		super(iVO);
	}

	public ImmobiliXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<ImmobiliXMLModel> IMMOBILI_XML = new XMLFormat<ImmobiliXMLModel>(ImmobiliXMLModel.class){
		
        public void write(ImmobiliXMLModel i_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codImmobile", i_xml.getCodImmobile());
        	xml.setAttribute("rif", i_xml.getRif());
        	xml.setAttribute("annoCostruzione", i_xml.getAnnoCostruzione());
        	xml.setAttribute("indirizzo", i_xml.getIndirizzo());
        	xml.setAttribute("citta", i_xml.getCitta());
        	xml.setAttribute("zona", i_xml.getZona());
        	xml.setAttribute("provincia", i_xml.getProvincia());
        	xml.setAttribute("cap", i_xml.getCap());        	
        	xml.setAttribute("codAnagrafica", i_xml.getCodAnagrafica());
        	xml.setAttribute("storico", i_xml.getStorico());
        	xml.setAttribute("codAgenteInseritore", i_xml.getCodAgenteInseritore());
        	xml.setAttribute("codTipologia", i_xml.getCodTipologia());
        	xml.setAttribute("codStato", i_xml.getCodStato());
        	xml.setAttribute("codRiscaldamento", i_xml.getCodRiscaldamento());
        	xml.setAttribute("codClasseEnergetica", i_xml.getCodClasseEnergetica());
        	xml.setAttribute("dataInserimento", 
        					 ((i_xml.getDataInserimento() != null)
        					  ? i_xml.getDataInserimento().toString()
        					  : null));
        	xml.setAttribute("descrizione", i_xml.getDescrizione());
        	xml.setAttribute("prezzo", i_xml.getPrezzo());
        	xml.setAttribute("mutuo", i_xml.getMutuo());
        	xml.setAttribute("spese", i_xml.getSpese());
        	xml.setAttribute("varie", i_xml.getVarie());
        	xml.setAttribute("mutuoDescrizione", i_xml.getMutuoDescrizione());
        	xml.setAttribute("mq", i_xml.getMq());
        	xml.setAttribute("visione", i_xml.getVisione());
        	xml.setAttribute("affittabile", i_xml.getAffittabile());
        	xml.setAttribute("dataLibero", 
					 		 ((i_xml.getDataLibero() != null)
					 		  ? i_xml.getDataLibero().toString()
					 		  : null));
        	
        }
                
        public void read(InputElement xml, ImmobiliXMLModel i_xml) throws XMLStreamException{
       }
        
   };	

	
}
