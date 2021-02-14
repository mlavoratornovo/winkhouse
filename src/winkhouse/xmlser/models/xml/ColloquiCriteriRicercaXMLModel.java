package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.vo.ColloquiCriteriRicercaVO;

public class ColloquiCriteriRicercaXMLModel extends ColloquiCriteriRicercaVO
											implements XMLSerializable {

	public ColloquiCriteriRicercaXMLModel(ColloquiCriteriRicercaVO ccrVO) {
		setCodCriterioRicerca(ccrVO.getCodCriterioRicerca());
		setCodColloquio(ccrVO.getCodColloquio());
		setCodRicerca(ccrVO.getCodRicerca());
		setFromValue(ccrVO.getFromValue());
		setGetterMethodName(ccrVO.getGetterMethodName());
		setLineNumber(ccrVO.getLineNumber());
		setLogicalOperator(ccrVO.getLogicalOperator());
		setToValue(ccrVO.getToValue());
	}

	public ColloquiCriteriRicercaXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}

	protected static final XMLFormat<ColloquiCriteriRicercaXMLModel> COLLOQUICRITERI_XML = new XMLFormat<ColloquiCriteriRicercaXMLModel>(ColloquiCriteriRicercaXMLModel.class){
		
        public void write(ColloquiCriteriRicercaXMLModel a_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codCriterioRicerca", a_xml.getCodCriterioRicerca());
			xml.setAttribute("codColloquio", a_xml.getCodColloquio());
			xml.setAttribute("codRicerca", a_xml.getCodRicerca());
			xml.setAttribute("fromValue", a_xml.getFromValue());
			xml.setAttribute("toValue", a_xml.getToValue());
			xml.setAttribute("getterMethodName", a_xml.getGetterMethodName());
			xml.setAttribute("logicalOperator", a_xml.getLogicalOperator());
			xml.setAttribute("lineNumber", a_xml.getLineNumber());
        }
                
        public void read(InputElement xml, ColloquiCriteriRicercaXMLModel a_xml) throws XMLStreamException{
        	
       }
        
   };	

	
}
