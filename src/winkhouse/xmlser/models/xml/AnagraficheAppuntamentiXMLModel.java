package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AnagraficheAppuntamentiModel;
import winkhouse.vo.AnagraficheAppuntamentiVO;

public class AnagraficheAppuntamentiXMLModel extends AnagraficheAppuntamentiModel 
											 implements XMLSerializable {

	public AnagraficheAppuntamentiXMLModel(AnagraficheAppuntamentiVO aaVO) {
		setCodAnagrafica(aaVO.getCodAnagrafica());
		setCodAnagraficheAppuntamenti(aaVO.getCodAnagraficheAppuntamenti());
		setCodAppuntamento(aaVO.getCodAppuntamento());
		setNote(aaVO.getNote());
	}

	public AnagraficheAppuntamentiXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AnagraficheAppuntamentiXMLModel> ANAGRAFICHEAPPUNTAMENTI_XML = new XMLFormat<AnagraficheAppuntamentiXMLModel>(AnagraficheAppuntamentiXMLModel.class){
		
        public void write(AnagraficheAppuntamentiXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAnagraficheAppuntamenti", a_xml.getCodAnagraficheAppuntamenti());
			xml.setAttribute("codAppuntamento", a_xml.getCodAppuntamento());
			xml.setAttribute("note", a_xml.getNote());
			xml.setAttribute("codAnagrafica", a_xml.getCodAnagrafica());
        }
                
        public void read(InputElement xml, AnagraficheAppuntamentiXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	

}
