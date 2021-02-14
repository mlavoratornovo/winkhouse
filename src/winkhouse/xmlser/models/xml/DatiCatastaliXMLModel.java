package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.DatiCatastaliVO;

public class DatiCatastaliXMLModel extends DatiCatastaliVO 
								   implements XMLSerializable {

	public DatiCatastaliXMLModel(DatiCatastaliVO dcVO) {
		setCodDatiCatastali(dcVO.getCodDatiCatastali());
		setCodImmobile(dcVO.getCodImmobile());
		setCategoria(dcVO.getCategoria());
		setDimensione(dcVO.getDimensione());
		setFoglio(dcVO.getFoglio());
		setParticella(dcVO.getParticella());
		setRedditoAgricolo(dcVO.getRedditoAgricolo());
		setRedditoDomenicale(dcVO.getRedditoDomenicale());
		setRendita(dcVO.getRendita());
		setSubalterno(dcVO.getSubalterno());
	}

	public DatiCatastaliXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<DatiCatastaliXMLModel> IMMAGINE_XML = new XMLFormat<DatiCatastaliXMLModel>(DatiCatastaliXMLModel.class){
		
        public void write(DatiCatastaliXMLModel dc_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codDatiCatastali", dc_xml.getCodDatiCatastali());
        	xml.setAttribute("codImmobile", dc_xml.getCodImmobile());
        	xml.setAttribute("categoria", dc_xml.getCategoria());
        	xml.setAttribute("foglio", dc_xml.getFoglio());        	
        	xml.setAttribute("particella", dc_xml.getParticella());
        	xml.setAttribute("subalterno", dc_xml.getSubalterno());
        	xml.setAttribute("dimensione", dc_xml.getDimensione());
        	xml.setAttribute("redditoAgricolo", dc_xml.getRedditoAgricolo());
        	xml.setAttribute("redditoDomenicale", dc_xml.getRedditoDomenicale());
        	xml.setAttribute("rendita", dc_xml.getRendita());
        }
                
        public void read(InputElement xml, DatiCatastaliXMLModel dc_xml) throws XMLStreamException{
       }
        
   };	
	
}
