package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AffittiAnagraficheModel;
import winkhouse.vo.AffittiAnagraficheVO;

public class AffittiAnagraficheXMLModel extends AffittiAnagraficheModel
										implements XMLSerializable {

	public AffittiAnagraficheXMLModel(AffittiAnagraficheVO aaVO) {
		setCodAffittiAnagrafiche(aaVO.getCodAffittiAnagrafiche());
		setCodAffitto(aaVO.getCodAffitto());
		setCodAnagrafica(aaVO.getCodAnagrafica());
		setNota(aaVO.getNota());
	}

	public AffittiAnagraficheXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<AffittiAnagraficheXMLModel> AFFITTIANAGRAFICHE_XML = new XMLFormat<AffittiAnagraficheXMLModel>(AffittiAnagraficheXMLModel.class){
		
        public void write(AffittiAnagraficheXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAffittiAnagrafiche", a_xml.getCodAffittiAnagrafiche());
			xml.setAttribute("codAffitto", a_xml.getCodAffitto());
			xml.setAttribute("codAnagrafica", a_xml.getCodAnagrafica());
			xml.setAttribute("nota", a_xml.getNota());
        }
                
        public void read(InputElement xml, AffittiAnagraficheXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	

}
