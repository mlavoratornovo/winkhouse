package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.AffittiRateModel;
import winkhouse.vo.AffittiRateVO;

public class AffittiRateXMLModel extends AffittiRateModel 
								 implements XMLSerializable {

	public AffittiRateXMLModel(AffittiRateVO arVO) {
		super(arVO);
	}

	public AffittiRateXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<AffittiRateXMLModel> AFFITTIRATE_XML = new XMLFormat<AffittiRateXMLModel>(AffittiRateXMLModel.class){
		
        public void write(AffittiRateXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codAffittiRate", a_xml.getCodAffittiRate());
			xml.setAttribute("codAffitto", a_xml.getCodAffitto());
			xml.setAttribute("codAnagrafica", a_xml.getCodAnagrafica());
			xml.setAttribute("codParent", a_xml.getCodParent());
			xml.setAttribute("nota", a_xml.getNota());
			xml.setAttribute("mese", a_xml.getMese());
			xml.setAttribute("scadenza",
					 ((a_xml.getScadenza() != null)
					 ? a_xml.getScadenza().toString()
					 : null));					

			xml.setAttribute("dataPagato", a_xml.getDataPagato().toString());
			xml.setAttribute("importo", a_xml.getImporto());
        }
                
        public void read(InputElement xml, AffittiRateXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	
	
}
