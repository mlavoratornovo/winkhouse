package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javolution.util.FastList;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AnagraficheModel;
import winkhouse.vo.AbbinamentiVO;
import winkhouse.vo.AgentiVO;
import winkhouse.vo.AnagraficheVO;
import winkhouse.vo.ClassiClientiVO;
import winkhouse.vo.ColloquiVO;
import winkhouse.vo.ContattiVO;

public class AnagraficheXMLModel extends AnagraficheModel 
								 implements XMLSerializable {


	public AnagraficheXMLModel(AnagraficheVO anagraficheVO) {
		super(anagraficheVO);
	}

	public AnagraficheXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AnagraficheXMLModel> ANAGRAFICHE_XML = new XMLFormat<AnagraficheXMLModel>(AnagraficheXMLModel.class){
		
        public void write(AnagraficheXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAnagrafica", a_xml.getCodAnagrafica());
        	xml.setAttribute("cognome", a_xml.getCognome());
        	xml.setAttribute("nome", a_xml.getNome());
        	xml.setAttribute("ragioneSociale", a_xml.getRagioneSociale());
        	xml.setAttribute("partitaIva", a_xml.getPartitaIva());
        	xml.setAttribute("codiceFiscale", a_xml.getCodiceFiscale());
        	xml.setAttribute("provincia", a_xml.getProvincia());
        	xml.setAttribute("cap", a_xml.getCap());
        	xml.setAttribute("citta", a_xml.getCitta());
        	xml.setAttribute("indirizzo", a_xml.getIndirizzo());
        	xml.setAttribute("commento", a_xml.getCommento());
        	xml.setAttribute("storico", a_xml.getStorico());
        	xml.setAttribute("codAgenteInseritore", a_xml.getCodAgenteInseritore());
        	xml.setAttribute("codClasseCliente", a_xml.getCodClasseCliente());
        	xml.setAttribute("dataInserimento", 
        					 ((a_xml.getDataInserimento() != null)
        					 ? a_xml.getDataInserimento().toString()
        					 : null));
        }
                
        public void read(InputElement xml, AnagraficheXMLModel c_xml) throws XMLStreamException{
       }
        
   };	

}
