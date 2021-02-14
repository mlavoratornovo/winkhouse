package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.ColloquiAnagraficheVO;

public class ColloquiAnagraficheXMLModel extends ColloquiAnagraficheVO
										 implements XMLSerializable {

	public ColloquiAnagraficheXMLModel(ColloquiAnagraficheVO caVO) {
		setCodAnagrafica(caVO.getCodAnagrafica());
		setCodColloquio(caVO.getCodColloquio());
		setCodColloquioAnagrafiche(caVO.getCodColloquioAnagrafiche());
		setCommento(caVO.getCommento());
	}

	public ColloquiAnagraficheXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<ColloquiAnagraficheXMLModel> AFFITTIRATE_XML = new XMLFormat<ColloquiAnagraficheXMLModel>(ColloquiAnagraficheXMLModel.class){
		
        public void write(ColloquiAnagraficheXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAnagrafica", a_xml.getCodAnagrafica());
			xml.setAttribute("codColloquio", a_xml.getCodColloquio());
			xml.setAttribute("codColloquioAnagrafiche", a_xml.getCodColloquioAnagrafiche());
			xml.setAttribute("commento", a_xml.getCommento());
        }
                
        public void read(InputElement xml, ColloquiAnagraficheXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	

	
}
