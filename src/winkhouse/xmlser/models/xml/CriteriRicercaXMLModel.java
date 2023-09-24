package winkhouse.xmlser.models.xml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.XMLFormat.InputElement;
import javolution.xml.XMLFormat.OutputElement;
import javolution.xml.stream.XMLStreamException;
import winkhouse.model.CriteriRicercaModel;
import winkhouse.vo.ColloquiCriteriRicercaVO;

public class CriteriRicercaXMLModel extends CriteriRicercaModel 
									implements XMLSerializable {

	public CriteriRicercaXMLModel(ColloquiCriteriRicercaVO ccrVO) {
		super(ccrVO);
	}

	public CriteriRicercaXMLModel(ResultSet rs) throws SQLException {
		super(rs);
	}
	
	protected static final XMLFormat<CriteriRicercaXMLModel> CRITERIORICERCA_XML = new XMLFormat<CriteriRicercaXMLModel>(CriteriRicercaXMLModel.class){
		
        public void write(CriteriRicercaXMLModel cr_xml, OutputElement xml)throws XMLStreamException {
        	xml.setAttribute("codColloquio", cr_xml.getCodColloquio());
        	xml.setAttribute("codCriterioRicerca", cr_xml.getCodCriterioRicerca());
        	xml.setAttribute("codRicerca", cr_xml.getCodRicerca());        	
        	xml.setAttribute("fromValue", cr_xml.getFromValue());
        	xml.setAttribute("toValue", cr_xml.getToValue());
        	xml.setAttribute("getterMethodName", cr_xml.getGetterMethodName());
        	xml.setAttribute("logicalOperator", cr_xml.getLogicalOperator());        	
        }
                
        public void read(InputElement xml, CriteriRicercaXMLModel cr_xml) throws XMLStreamException{
        	
       }
        
   };	


}
